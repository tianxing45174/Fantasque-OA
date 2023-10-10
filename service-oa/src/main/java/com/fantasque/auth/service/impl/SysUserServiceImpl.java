package com.fantasque.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasque.auth.mapper.SysUserMapper;
import com.fantasque.auth.service.SysUserService;
import com.fantasque.model.system.SysUser;
import com.fantasque.vo.system.SysUserQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* @author tianx
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-10-09 02:03:40
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {

    @Override
    public IPage pageQueryUser(Long page, Long limit, SysUserQueryVo sysUserQueryVo) {
        //创建page对象
        Page<SysUser> pageParam = new Page<>(page,limit);

        //封装条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //获取查询条件
        String username = sysUserQueryVo.getKeyword();
        Long deptId = sysUserQueryVo.getDeptId();
        Long postId = sysUserQueryVo.getPostId();
        Long roleId = sysUserQueryVo.getRoleId();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        //判断条件不为空
        //like 模糊查询 keyword
        if (!StringUtils.isEmpty(username)) {
            wrapper.like(SysUser::getUsername,username);
        }
        if (deptId != null) {
            wrapper.eq(SysUser::getDeptId,deptId);
        }
        if (postId != null) {
            wrapper.eq(SysUser::getPostId,postId);
        }
        //ge 大于等于
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        //le 小于等于
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le(SysUser::getCreateTime,createTimeEnd);
        }

        IPage<SysUser> pageModel = this.getBaseMapper().selectPage(pageParam, wrapper);
        return pageModel;
    }

    @Transactional
    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser sysUser = this.getById(id);
        if(status.intValue() == 1) {
            sysUser.setStatus(status);
        } else {
            sysUser.setStatus(0);
        }
        this.updateById(sysUser);
    }
}




