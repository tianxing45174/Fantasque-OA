package com.fantasque;

import com.fantasque.auth.ServiceAuthApplication;
import com.fantasque.auth.mapper.SysRoleMapper;
import com.fantasque.model.system.SysRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LaFantasque
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceAuthApplication.class)
public class SysRoleMapperTest {
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Test
    public void MapperTest(){
        List<SysRole> users = sysRoleMapper.selectList(null);
        users.forEach(System.out::println);
    }
}
