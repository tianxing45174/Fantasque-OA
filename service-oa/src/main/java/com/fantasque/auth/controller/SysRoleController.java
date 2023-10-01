package com.fantasque.auth.controller;

import com.fantasque.auth.service.SysRoleService;
import com.fantasque.model.system.SysRole;
import com.fantasque.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LaFantasque
 * @version 1.0
 */
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询所有角色
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {
        System.out.println("查询所有角色");
        List<SysRole> sysRoleList = sysRoleService.list();
        return Result.ok(sysRoleList);
    }
}
