package com.fantasque.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fantasque.auth.mapper.SysUserMapper;
import com.fantasque.auth.service.SysMenuService;
import com.fantasque.auth.service.SysUserService;
import com.fantasque.common.exception.MyException;
import com.fantasque.common.result.ResultCodeEnum;
import com.fantasque.model.system.SysUser;
import com.fantasque.vo.system.RouterVo;
import com.fantasque.vo.system.SysUserQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
* @author tianx
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-10-09 02:03:40
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {

    private final int Enable = 1;
    private final int Disable = 0;

    @Autowired
    private SysMenuService sysMenuService;

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
        if (null != deptId) {
            wrapper.eq(SysUser::getDeptId,deptId);
        }
        if (null != postId) {
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

        return this.getBaseMapper().selectPage(pageParam, wrapper);
    }

    @Transactional
    @Override
    public void updateStatus(Long id, Integer newStatus) {
        SysUser sysUser = this.getById(id);
        Integer oldStatus = sysUser.getStatus();
        if (newStatus.intValue() != oldStatus.intValue()) { // 状态是否改变
            if(Enable == newStatus) {
                sysUser.setStatus(Enable); // 启用
            } else {
                sysUser.setStatus(Disable); // 停用
            }
            this.updateById(sysUser);
        }
    }

    @Override
    public SysUser getByUsername(String username) {
        SysUser user = this.getOne(new QueryWrapper<SysUser>().eq("username", username));
        return user;
    }

    @Override
    public void saveUser(SysUser user) {
        SysUser sysUser = this.getByUsername(user.getUsername());
        if (null != sysUser) {
            System.out.println("该用户已存在");
            throw new MyException(ResultCodeEnum.REGISTER_ERROR);
        }
        if (11 != user.getPhone().length()) {
            System.out.println("电话号码格式错误");
            throw new MyException(ResultCodeEnum.PHONE_ERROR);
        }
        // 使用MD5加密
//        String encode = MD5.encrypt(user.getUsername());
        String encode = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encode);
        System.out.println(user);
        this.save(user);
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> result = new HashMap<>();
        // 获取用户信息
        SysUser sysUser = this.getByUsername(username);
        //根据用户id获取菜单权限值
        List<RouterVo> routerVoList = sysMenuService.findUserMenuListByUserId(sysUser.getId());
        //根据用户id获取用户按钮权限
        List<String> permsList = sysMenuService.findUserPermsListByUserId(sysUser.getId());
        // 封装信息
        result.put("name", sysUser.getName());
        // 返回可操作菜单
        result.put("routers", routerVoList);
        // 返回可操作按钮
        result.put("buttons", permsList);
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //当前权限控制使用不到，我们暂时忽略
        result.put("roles",  new HashSet<>());
        return result;
    }
}




