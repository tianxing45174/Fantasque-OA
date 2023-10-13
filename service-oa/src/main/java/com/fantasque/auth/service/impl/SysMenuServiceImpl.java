package com.fantasque.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasque.auth.mapper.SysMenuMapper;
import com.fantasque.auth.service.SysMenuService;
import com.fantasque.common.exception.MyException;
import com.fantasque.model.system.SysMenu;
import com.fantasque.result.ResultCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* @author tianx
* @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
* @createDate 2023-10-12 02:47:58
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService {

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




