package com.fantasque.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasque.auth.mapper.SysRoleMapper;
import com.fantasque.auth.mapper.SysUserRoleMapper;
import com.fantasque.auth.service.SysRoleService;
import com.fantasque.model.system.SysRole;
import com.fantasque.model.system.SysUserRole;
import com.fantasque.vo.system.AssginRoleVo;
import com.fantasque.vo.system.SysRoleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author LaFantasque
* @description 针对表【sys_role(角色)】的数据库操作Service实现
* @createDate 2023-09-20 17:01:41
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

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

    /**
     * 获取用户角色数据
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> findRoleByUserId(Long userId) {
        Map<String, Object> roleMap = new HashMap<>();

        // 保存所有的角色
        List<SysRole> allRolesList = this.list();
        roleMap.put("allRolesList",allRolesList);

        // 查询userId拥有的角色
        List<Long> roleIdList = findRoleIdList(userId);

        // 保存拥有的角色信息
        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole sysRole : allRolesList) {
            if (roleIdList.contains(sysRole.getId())) {
                assginRoleList.add(sysRole);
            }
        }
        roleMap.put("assginRoleList",assginRoleList);

        return roleMap;
    }

    /**
     * 分配角色
     * @param assginRoleVo
     */
    @Transactional
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        List<Long> assginRoleIdList = assginRoleVo.getRoleIdList();
//        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, assginRoleVo.getUserId()));
//
//        for(Long roleId : assginRoleVo.getRoleIdList()) {
//            if(StringUtils.isEmpty(roleId)) continue;
//            SysUserRole userRole = new SysUserRole();
//            userRole.setUserId(assginRoleVo.getUserId());
//            userRole.setRoleId(roleId);
//            sysUserRoleMapper.insert(userRole);
//        }
        // 查询userId当前拥有的角色
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", assginRoleVo.getUserId()));
        List<Long> roleIdList = userRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        // 新增角色idList
        List<Long> assginRoleList = assginRoleIdList.stream().filter(i -> !roleIdList.contains(i)).collect(Collectors.toList());
        // 撤销角色idList
        List<Long> unassginUserRoleIdList = new ArrayList<>();
        List<Long> unassginRoleList = roleIdList.stream().filter(i -> !assginRoleIdList.contains(i)).collect(Collectors.toList());
        for (SysUserRole userRole : userRoleList) {
            if (unassginRoleList.contains(userRole.getRoleId())) {
                unassginUserRoleIdList.add(userRole.getId());
            }
        }
        System.out.println("新增角色id:" + assginRoleList);
        System.out.println("撤销角色id:" + unassginRoleList);
        System.out.println("删除的用户角色表id:" + unassginUserRoleIdList);
        // 撤销角色
        if (!unassginUserRoleIdList.isEmpty()) {
            sysUserRoleMapper.deleteBatchIds(unassginUserRoleIdList);
        }
        // 新增角色
        if (!assginRoleList.isEmpty()) {
            sysUserRoleMapper.insertRoleList(assginRoleVo.getUserId(),assginRoleList);
        }

    }

    private List<Long> findRoleIdList(Long userId) {
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        List<Long> roleIdList = userRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        return roleIdList;
    }
}




