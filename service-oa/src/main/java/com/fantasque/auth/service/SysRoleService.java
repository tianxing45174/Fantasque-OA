package com.fantasque.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fantasque.model.system.SysRole;
import com.fantasque.vo.system.SysRoleQueryVo;

/**
* @author LaFantasque
* @description 针对表【sys_role(角色)】的数据库操作Service
* @createDate 2023-09-20 17:01:41
*/

public interface SysRoleService extends IService<SysRole> {

    public IPage pageQueryRole(Long page, Long limit, SysRoleQueryVo sysRoleQueryVo);

}
