package com.fantasque.auth.controller;

import com.fantasque.auth.service.SysMenuService;
import com.fantasque.model.system.SysMenu;
import com.fantasque.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LaFantasque
 * @version 1.0
 */
@Api(tags = "权限菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService service;

    @ApiOperation(value = "获取菜单")
    @GetMapping("/getMenu")
    public Result getMenu() {
        System.out.println("获取菜单");
        List<SysMenu> list = service.getMenu();
        return Result.ok(list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu) {
        System.out.println("新增[" + sysMenu.getName() + "]菜单");
        service.save(sysMenu);
        return Result.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysMenu sysMenu) {
        System.out.println("修改[" + sysMenu.getName() + "]菜单");
        service.updateById(sysMenu);
        return Result.ok();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        System.out.println("删除ID为[" + id + "]的菜单");
        service.removeById(id);
        return Result.ok();
    }

}