package com.fantasque.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasque.auth.mapper.SysRoleMenuMapper;
import com.fantasque.auth.service.SysRoleMenuService;
import com.fantasque.model.system.SysRoleMenu;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tianx
* @description 针对表【sys_role_menu(角色菜单)】的数据库操作Service实现
* @createDate 2023-10-12 02:48:22
*/
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
    implements SysRoleMenuService {

    @Override
    public void saveRoleMenuList(Long roleId, List<Long> assignMenuList) {
        this.baseMapper.insertRoleMenuList(roleId, assignMenuList);
    }
}




