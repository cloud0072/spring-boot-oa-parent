package com.caolei;

import com.caolei.common.constant.Operation;
import com.caolei.system.pojo.OperationLog;
import com.caolei.system.pojo.Permission;
import com.caolei.system.pojo.Role;
import com.caolei.system.pojo.User;
import com.caolei.system.repository.PermissionRepository;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SystemTest {

    private static int index = 1;

    @Autowired
    PermissionService permissionService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionRepository permissionRepository;

    private void printIndex() {
        System.err.println(">>>>>>>>>>>>>>>>>" + index++);
    }

    @Test
    public void Test01SaveUser() {
        printIndex();
        String key = "cloud0072";
        User user = new User(key, key, key, null, false).setDefaultValue();
        userService.register(user);
    }

    @Test
    public void Test02FindUser() {
        printIndex();
        System.out.println(userService.findAuthorInfoByAccount("cloud0072"));
    }

    @Test
    public void Test03ModifyUser() {
        printIndex();
        User user = userService.findUserByAccount("cloud0072");
        user.setEmail("352419394@qq.com");
        user.setUserName("caolei");
        userService.save(user);
    }

    @Test
    public void Test04FindPermissions() {
        printIndex();
        Permission permission = new Permission();
        permission.setOperation(Operation.CREATE);
        System.out.println(permissionService.findAll(Example.of(permission)));
    }

    /**
     * 不要使用注解@Transactional
     * 否则会导致无法 insert 和 delete
     */
    @Test
    public void Test05UserAddPermissions() {
        printIndex();
        User user = userService.findAuthorInfoByAccount("cloud0072");

        Permission permission = new Permission();
        permission.setOperation(Operation.CREATE);
        List<Permission> permissions = permissionService.findAll(Example.of(permission));
        System.out.println(permissions.size());

        user.getPermissions().addAll(permissions);
        userService.save(user);
    }

    @Transactional
    @Test
    public void Test06UserFindPermissions() {
        printIndex();
        User user = userService.findUserByAccount("cloud0072");

        System.out.println(user.getPermissions());
    }

    @Test
    public void Test07UserAddRoles() {
        printIndex();
        User user = userService.findUserByAccount("cloud0072");

        Role role = roleService.findRoleByCode("user");

        user.setRoles(Collections.singleton(role));

        userService.save(user);
    }

    @Transactional
    @Test
    public void Test08UserAddLogs() {
        printIndex();
        User user = userService.findUserWithLogsByAccount("cloud0072");

        OperationLog log = new OperationLog();
        log.setCreateTime(new Date());
        log.setUser(user);

        user.getExtend().getLogs().addAll(Collections.singletonList(log));

        userService.save(user);
    }

    @Transactional
    @Test
    public void Test09UserRemoveLogs() {
        printIndex();
        User user = userService.findUserWithLogsByAccount("cloud0072");

        user.getExtend().getLogs().clear();

        userService.save(user);
    }

    @Transactional
    @Test
    public void Test10UserFindRoles() {
        printIndex();
        User user = userService.findUserByAccount("cloud0072");
        System.out.println(user.getRoles());
    }

    //    @Test
    public void Test11UserRemoveRoles() {
        printIndex();
        User user = userService.findAuthorInfoByAccount("cloud0072");
        user.getRoles().remove(0);
        userService.save(user);
    }

    //    @Test
    public void Test13UserRemove() {
        userService.delete(userService.findUserByAccount("cloud0072"));
    }


    @Test
    public void Test14JoinFind() {

        List<Permission> permissions = permissionRepository.findPermissionsByRoles_Users_AccountEquals("admin");

        permissions.forEach(permission -> System.out.println(permission.getName()));
    }
}


