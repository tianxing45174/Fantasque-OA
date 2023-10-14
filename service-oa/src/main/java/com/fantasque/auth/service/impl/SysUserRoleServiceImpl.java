package com.fantasque.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasque.auth.mapper.SysUserRoleMapper;
import com.fantasque.auth.service.SysUserRoleService;
import com.fantasque.model.system.SysUserRole;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tianx
* @description 针对表【sys_user_role(用户角色)】的数据库操作Service实现
* @createDate 2023-10-12 02:52:10
*/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
    implements SysUserRoleService {

    /**
     * 根据用户ID分配多个角色
     * @param userId
     * @param assignRoleList
     */
    @Override
    public void saveRoleList(Long userId, List<Long> assignRoleList) {
        this.baseMapper.insertRoleList(userId, assignRoleList);
    }
}




