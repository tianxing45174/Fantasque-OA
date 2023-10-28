package com.fantasque.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasque.auth.mapper.SysMenuMapper;
import com.fantasque.auth.service.SysMenuService;
import com.fantasque.auth.service.SysRoleMenuService;
import com.fantasque.common.exception.MyException;
import com.fantasque.common.result.ResultCodeEnum;
import com.fantasque.model.system.SysMenu;
import com.fantasque.model.system.SysRoleMenu;
import com.fantasque.vo.system.AssignMenuVo;
import com.fantasque.vo.system.MetaVo;
import com.fantasque.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author tianx
* @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
* @createDate 2023-10-12 02:47:58
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService {

    private final int Admin = 1;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> getMenu() {
        //获取全部权限菜单
        List<SysMenu> sysMenuList = this.list();
        if (CollectionUtils.isEmpty(sysMenuList)) return null;
        //构造菜单
        return this.buildMenuTree(sysMenuList);
    }

    /**
     * 根据角色Id获取授权权限数据
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        List<SysMenu> allMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
        List<SysRoleMenu> roleMenuList = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        List<Long> menuIdList = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        System.out.println("该用户拥有的权限菜单id为" + menuIdList);
//      //
        allMenuList.stream().forEach(i -> i.setSelect(menuIdList.contains(i.getId())) );
        return buildMenuTree(allMenuList);
    }

    @Transactional
    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        // 传入的 List
        List<Long> assignMenuIdList = assignMenuVo.getMenuIdList();
        System.out.println("传入的权限菜单id" + assignMenuIdList);
        // 查询当前分配的权限菜单
        List<SysRoleMenu> roleMenuList = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", assignMenuVo.getRoleId()));
        List<Long> roleMenuIdList = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        System.out.println("当前分配的权限菜单: " + roleMenuIdList);
        // 新分配的权限菜单
        List<Long> assignMenuList = assignMenuIdList.stream().filter(i -> !roleMenuIdList.contains(i)).collect(Collectors.toList());
        System.out.println("新分配的权限菜单id: " + assignMenuList);
        // 撤销分配的权限菜单id
        List<SysRoleMenu> unassignMenuIdList = roleMenuList.stream().filter(i -> !assignMenuIdList.contains(i.getMenuId())).collect(Collectors.toList());
        System.out.println("撤销分配的权限菜单:" + unassignMenuIdList);
        // 撤销分配的权限角色菜单表id
        List<Long> unassignRoleMenuIdList = unassignMenuIdList.stream().map(SysRoleMenu::getId).collect(Collectors.toList());
        System.out.println("撤销分配的权限角色菜单表id: " + unassignRoleMenuIdList);

        // 判断撤销权限是否为空
        if (!unassignRoleMenuIdList.isEmpty()) {
            // 撤销权限菜单
            sysRoleMenuService.removeByIds(unassignRoleMenuIdList);
        }
        // 判断 新分配权限菜单是否为空
        if (!assignMenuList.isEmpty()) {
            // 新分配权限菜单
            sysRoleMenuService.saveRoleMenuList(assignMenuVo.getRoleId(), assignMenuList);
        }
    }

    /**
     * 查询用户可以使用的菜单列表
     * @param userId 默认 userId == 1 的为 超级管理员
     * @return
     */
    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenuList;
        if (Admin == userId) {
            // 超级管理员 获取所有权限
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue));
        } else {
            sysMenuList = this.baseMapper.findListByUserId(userId);
        }
        List<SysMenu> sysMenuTree = buildMenuTree(sysMenuList);
        // 构造路由
        List<RouterVo> routerVoList = this.buildRouter(sysMenuTree);
        return routerVoList;
    }

    /**
     * 获取用户可使用按钮权限
     * @return
     */
    @Override
    public List<String> findUserPermsListByUserId(Long userId) {
        //超级管理员admin账号id为：1
        List<SysMenu> sysMenuList;
        if (Admin == userId) {
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
        } else {
            sysMenuList = this.baseMapper.findListByUserId(userId);
        }
        List<String> permsList = sysMenuList.stream().filter(item -> item.getType() == 2).map(SysMenu::getPerms).collect(Collectors.toList());
        return permsList;
    }

    /**
     * 重写 remove 方法 删除的菜单没有子菜单时才能删除
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        //  查找 删除的菜单 是否 有子菜单
        int count = this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (count > 0) {
            throw new MyException(ResultCodeEnum.REMOVE_MENU_ERROR);
        }
        this.baseMapper.deleteById(id);
        return false;
    }

    /**
     * 使用递归构建菜单
     * @param sysMenuList 所有权限菜单
     * @return 树形结构 菜单
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> sysMenuList) {
        // 存储菜单数据
        List<SysMenu> menu = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (0 == sysMenu.getParentId()) {
                menu.add(findChildren(sysMenu,sysMenuList));
            }
        }
        return menu;
    }

    /**
     * 递归查找子节点
     * @param sysMenu 父节点
     * @param sysMenuList 所有权限菜单
     * @return 已封装数据
     */
    private SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {

        for (SysMenu item : sysMenuList) {
            // 子节点ParentId == 父节点id 该父节点存在子节点
            if(item.getParentId().longValue() == sysMenu.getId().longValue()) {
                if (null == sysMenu.getChildren()) { //确保children不为null
                    sysMenu.setChildren(new ArrayList<>());
                }
                // 保存子节点
                sysMenu.getChildren().add(findChildren(item,sysMenuList));
            }
        }
        return sysMenu;
    }

    /**
     * 根据菜单构建路由
     * @return 用户菜单路由
     */
    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();
            //如果当前是菜单，需将按钮对应的路由加载出来，如：“角色授权”按钮对应的路由在“系统管理”下面
            if(1 == menu.getType()) {
                List<SysMenu> hiddenMenuList = children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
//                if (CollectionUtils.isEmpty(hiddenMenuList)) {
//                    continue;
//                }
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if(children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由地址
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }
}




