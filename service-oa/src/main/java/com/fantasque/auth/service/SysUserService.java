package com.fantasque.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fantasque.model.system.SysUser;
import com.fantasque.vo.system.SysUserQueryVo;

/**
* @author tianx
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-10-09 02:03:40
*/
public interface SysUserService extends IService<SysUser> {

    public IPage pageQueryUser(Long page, Long limit, SysUserQueryVo sysUserQueryVo);

    void updateStatus(Long id, Integer status);
}
