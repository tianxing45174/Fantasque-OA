package com.fantasque.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fantasque.model.system.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author tianx
* @description 针对表【sys_role_menu(角色菜单)】的数据库操作Mapper
* @createDate 2023-10-12 02:48:22
* @Entity com/fantasque/auth.domain.SysRoleMenu
*/
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    public void insertRoleMenuList(@Param("roleId") Long roleId, @Param("assignMenuList") List<Long> assignMenuList);

}




