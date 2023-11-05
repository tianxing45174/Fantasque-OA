package com.fantasque.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fantasque.model.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author tianx
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2023-10-09 02:03:40
* @Entity com/fantasque/auth.domain.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<String> getUsernameByRoleId(@Param("roleId") Long roleId);
}




