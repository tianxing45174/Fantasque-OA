package com.fantasque.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fantasque.auth.service.SysRoleService;
import com.fantasque.auth.service.SysUserService;
import com.fantasque.model.system.SysUser;
import com.fantasque.common.result.Result;
import com.fantasque.common.result.ResultCodeEnum;
import com.fantasque.vo.system.AssignRoleVo;
import com.fantasque.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author LaFantasque
 * @version 1.0
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService service;

    @Autowired
    private SysRoleService sysRoleService;

    //用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        SysUserQueryVo sysUserQueryVo) {
        System.out.println("分页查询用户,当前页数: "+ page +"查询数量:" + limit + "，查询条件: " + sysUserQueryVo.toString());
        //调用mp的方法实现条件分页查询
        IPage pageModel = service.pageQueryUser(page, limit, sysUserQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        System.out.println("获取用户id为" + id + " 的信息");
        SysUser user = service.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "新增用户信息")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser user) {
        System.out.println("新增用户:" + user.getUsername() + " 的信息");
        service.saveUser(user);
        return Result.ok();
    }

    @ApiOperation(value = "更新用户信息")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysUser user) {
        System.out.println("更新用户:" + user.getUsername() + " 的信息");
        if (11 != user.getPhone().length()) {
            System.out.println("电话号码格式错误");
            return Result.fail().resultCodeEnum(ResultCodeEnum.PHONE_ERROR);
        }
        service.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        System.out.println("删除用户id为:" + id + " 的信息");
        service.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "获取用户角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        System.out.println("获取用户ID为:" + userId + "的角色数据");
        Map<String, Object> roleMap = sysRoleService.findRoleByUserId(userId);
        return Result.ok(roleMap);
    }

    @ApiOperation(value = "为用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignRoleVo assignRoleVo) {
        System.out.println("获取用户ID为:" + assignRoleVo.getUserId() + " 的用户分配" + assignRoleVo.getRoleIdList() + "角色");
        sysRoleService.doAssign(assignRoleVo);
        return Result.ok();
    }

    @ApiOperation(value = "更新用户状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        System.out.println("更新用户id:" + id + "的状态为[" + (status.equals(1)?"启用":"停用") + "]");
        service.updateStatus(id, status);
        return Result.ok();
    }
}
