package com.fantasque.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fantasque.model.system.SysMenu;
import com.fantasque.vo.system.AssignMenuVo;
import com.fantasque.vo.system.RouterVo;

import java.util.List;

/**
* @author tianx
* @description 针对表【sys_menu(菜单表)】的数据库操作Service
* @createDate 2023-10-12 02:47:58
*/
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> getMenu();

    List<SysMenu> findMenuByRoleId(Long roleId);

    void doAssign(AssignMenuVo assignMenuVo);

    List<RouterVo> findUserMenuListByUserId(Long userId);

    List<String> findUserPermsListByUserId(Long userId);
}
