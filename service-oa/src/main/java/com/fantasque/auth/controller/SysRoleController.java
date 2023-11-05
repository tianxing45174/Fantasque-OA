package com.fantasque.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fantasque.auth.service.SysRoleService;
import com.fantasque.model.system.SysRole;
import com.fantasque.common.result.Result;
import com.fantasque.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * 查询角色信息
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "查询角色信息")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        System.out.println("根据id查询角色信息");
        SysRole role = sysRoleService.getById(id);
        return Result.ok(role);
    }

    /**
     *
     * @param page 显示页数
     * @param limit 每页记录数
     * @param sysRoleQueryVo 查询条件
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "角色条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageQuerySysRole(@PathVariable("page") Long page,
                                @PathVariable("limit") Long limit,
                                SysRoleQueryVo sysRoleQueryVo){
        System.out.println("分页查询角色,当前页数: "+ page +"查询数量:" + limit + "，查询条件: " + sysRoleQueryVo.getRoleName());
        IPage pageQueryRole = sysRoleService.pageQueryRole(page, limit, sysRoleQueryVo);
        return Result.ok(pageQueryRole);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation(value = "添加角色")
    @PostMapping("/save")
    public Result save(@RequestBody SysRole role) {
        boolean is_success = sysRoleService.save(role);
        if (is_success) {
            System.out.println("添加角色: " + role);
            return Result.ok();
        }
        return Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation(value = "修改角色信息")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysRole role) {
        System.out.println("修改角色: " + role);
        boolean is_seccess = sysRoleService.updateById(role);
        if (is_seccess) {
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 根据角色id删除角色
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "根据id删除角色")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        System.out.println("删除id为[" + id + "]的角色");
        sysRoleService.removeById(id);
        return Result.ok();
    }

    /**
     * 根据角色ID列表删除角色
     * @param idList
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "根据id列表删除角色")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        System.out.println("删除id为" + idList.toString() + "的角色");
        sysRoleService.removeByIds(idList);
        return Result.ok();
    }
}
