//package com.fantasque.common.util;
//
//import com.fantasque.model.system.SysMenu;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author LaFantasque
// * @version 1.0
// */
//public class MenuHelper {
//
//    /**
//     * 使用递归构建菜单
//     * @param sysMenuList 所有权限菜单
//     * @return 树形结构 菜单
//     */
//    private List<SysMenu> buildMenu(List<SysMenu> sysMenuList) {
//        // 存储菜单数据
//        List<SysMenu> menu = new ArrayList<>();
//        for (SysMenu sysMenu : sysMenuList) {
//            if (sysMenu.getParentId().longValue() == 0) {
//                menu.add(findChildren(sysMenu,sysMenuList));
//            }
//        }
//        return menu;
//    }
//
//    /**
//     * 递归查找子节点
//     * @param sysMenu 父节点
//     * @param sysMenuList 所有权限菜单
//     * @return 已封装数据
//     */
//    private SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
//
//        for (SysMenu item : sysMenuList) {
//            // 子节点ParentId == 父节点id 该父节点存在子节点
//            if(item.getParentId().longValue() == sysMenu.getId().longValue()) {
//                if (sysMenu.getChildren() == null) { //确保children不为null
//                    sysMenu.setChildren(new ArrayList<>());
//                }
//                // 保存子节点
//                sysMenu.getChildren().add(findChildren(item,sysMenuList));
//            }
//        }
//        return sysMenu;
//    }
//}