package com.fantasque.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fantasque.model.system.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author tianx
* @description 针对表【sys_user_role(用户角色)】的数据库操作Mapper
* @createDate 2023-10-10 15:49:33
* @Entity com/fantasque/auth.domain.SysUserRole
*/
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    void insertRoleList(@Param("userId") Long userId,@Param("roleIdList") List<Long> roleIdList);
}




