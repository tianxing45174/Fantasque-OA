package com.fantasque.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fantasque.model.system.SysRoleMenu;

import java.util.List;

/**
* @author tianx
* @description 针对表【sys_role_menu(角色菜单)】的数据库操作Service
* @createDate 2023-10-12 02:48:22
*/
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    void saveRoleMenuList(Long roleId, List<Long> assignMenuList);
}
