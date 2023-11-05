package com.fantasque.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fantasque.model.system.SysUser;
import com.fantasque.vo.system.SysUserQueryVo;

import java.util.List;
import java.util.Map;

/**
* @author tianx
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-10-09 02:03:40
*/
public interface SysUserService extends IService<SysUser> {

    public IPage pageQueryUser(Long page, Long limit, SysUserQueryVo sysUserQueryVo);

    void updateStatus(Long id, Integer status);

    SysUser getByUsername(String username);

    void saveUser(SysUser user);

    Map<String, Object> getUserInfo(String username);

    List<String> getUsernameByRoleId(Long roleId);
}
