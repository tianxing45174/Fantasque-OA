package com.fantasque.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fantasque.model.system.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
* @author LaFantasque
* @description 针对表【sys_role(角色)】的数据库操作Mapper
* @createDate 2023-09-20 17:01:41
* @Entity service-oa.domain.SysRole
*/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

}