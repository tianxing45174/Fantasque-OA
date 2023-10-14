package com.fantasque.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fantasque.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

/**
* @author tianx
* @description 针对表【sys_menu(菜单表)】的数据库操作Mapper
* @createDate 2023-10-12 02:47:58
* @Entity com/fantasque/auth.domain.SysMenu
*/
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}




