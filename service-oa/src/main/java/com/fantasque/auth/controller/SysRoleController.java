package com.fantasque.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fantasque.auth.service.SysRoleService;
import com.fantasque.model.system.SysRole;
import com.fantasque.result.Result;
import com.fantasque.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LaFantasque
 * @version 1.0
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询所有角色
     * @return
     */
    @ApiOperation(value = "获取全部角色列表")
    @GetMapping("/findAll")
    public Result findAll() {
        System.out.println("查询所有角色");
        List<SysRole> sysRoleList = sysRoleService.list();
        return Result.ok(sysRoleList);
    }

    /**
     *
     * @param page 显示页数
     * @param limit 每页记录数
     * @return
     */
    @ApiOperation(value = "条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageQuerySysRole(@PathVariable("page") Long page,
                                @PathVariable("limit") Long limit,
                                SysRoleQueryVo sysRoleQueryVo){
        IPage pageQueryRole = sysRoleService.pageQueryRole(page, limit, sysRoleQueryVo);
        return Result.ok(pageQueryRole);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @ApiOperation(value = "添加角色")
    @PostMapping("/save")
    public Result save(@RequestBody SysRole role) {
        boolean is_success = sysRoleService.save(role);
        if (is_success) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 根据角色ID修改角色
     * @param role
     * @return
     */
    @ApiOperation(value = "修改角色")
    @PostMapping("/update")
    public Result updateById(@RequestBody SysRole role) {
        System.out.println("修改角色：" + role.getRoleName());
        boolean is_seccess = sysRoleService.updateById(role);
        if (is_seccess) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 根据角色ID查询角色
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询角色")
    @GetMapping("/get/{id}")
    public Result queryById(@PathVariable("id") Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

}
