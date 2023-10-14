package com.fantasque.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasque.auth.mapper.SysMenuMapper;
import com.fantasque.auth.service.SysMenuService;
import com.fantasque.auth.service.SysRoleMenuService;
import com.fantasque.common.exception.MyException;
import com.fantasque.model.system.SysMenu;
import com.fantasque.model.system.SysRoleMenu;
import com.fantasque.result.ResultCodeEnum;
import com.fantasque.vo.system.AssignMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
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

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> getMenu() {
        //获取全部权限菜单
        List<SysMenu> sysMenuList = this.list();
        if (CollectionUtils.isEmpty(sysMenuList)) return null;
        //构造菜单
        List<SysMenu> result = this.buildMenu(sysMenuList);
        return result;
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
        List<SysMenu> sysMenuList = buildMenu(allMenuList);
        return sysMenuList;
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
    private List<SysMenu> buildMenu(List<SysMenu> sysMenuList) {
        // 存储菜单数据
        List<SysMenu> menu = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId().longValue() == 0) {
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
                if (sysMenu.getChildren() == null) { //确保children不为null
                    sysMenu.setChildren(new ArrayList<>());
                }
                // 保存子节点
                sysMenu.getChildren().add(findChildren(item,sysMenuList));
            }
        }
        return sysMenu;
    }
}




