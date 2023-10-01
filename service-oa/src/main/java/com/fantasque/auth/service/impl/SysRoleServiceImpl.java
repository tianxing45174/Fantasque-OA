package com.fantasque.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasque.auth.mapper.SysRoleMapper;
import com.fantasque.auth.service.SysRoleService;
import com.fantasque.model.system.SysRole;
import com.fantasque.vo.system.SysRoleQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author LaFantasque
* @description 针对表【sys_role(角色)】的数据库操作Service实现
* @createDate 2023-09-20 17:01:41
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService {

    @Override
    public IPage pageQueryRole(Long page, Long limit, SysRoleQueryVo sysRoleQueryVo) {
        // 创建Page对象
        Page<SysRole> pageParam = new Page<>(page, limit);

        // 获取查询条件
        String roleName = sysRoleQueryVo.getRoleName();

        // 封装条件
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(roleName)) {
            wrapper.like(SysRole::getRoleName,roleName);
        }

        IPage<SysRole> pageModel = this.getBaseMapper().selectPage(pageParam, wrapper);
        return pageParam;
    }
}




