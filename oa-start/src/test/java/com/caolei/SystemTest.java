package com.caolei;

import com.caolei.common.constant.Operation;
import com.caolei.base.pojo.OperationLog;
import com.caolei.base.pojo.Permission;
import com.caolei.base.pojo.Role;
import com.caolei.base.pojo.User;
import com.caolei.base.repository.EntityResourceRepository;
import com.caolei.base.repository.PermissionRepository;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.base.service.UserService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SystemTest {

    private static int index = 1;

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private EntityResourceRepository entityResourceRepository;

    private void printIndex() {
        System.err.println(">>>>>>>>>>>>>>>>>" + index++);
    }

    @Test
    public void test01SaveUser() {
        printIndex();
        String key = "cloud0072";
        User user = new User(key, key, key, null, false).setDefaultValue();
        userService.save(user);
    }

    @Test
    public void test02FindUser() {
        printIndex();
        System.out.println(userService.findAuthorInfoByAccount("cloud0072"));
    }

    @Test
    public void test03ModifyUser() {
        printIndex();
        User user = userService.findUserByAccount("cloud0072");
        user.setEmail("352419394@qq.com");
        user.setUserName("caolei");
        userService.save(user);
    }

    @Test
    public void test04FindPermissions() {
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
    public void test05UserAddPermissions() {
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
    public void test06UserFindPermissions() {
        printIndex();
        User user = userService.findUserByAccount("cloud0072");

        System.out.println(user.getPermissions());
    }

    @Test
    public void test07UserAddRoles() {
        printIndex();
        User user = userService.findUserByAccount("cloud0072");

        Role role = roleService.findRoleByCode("user");

        user.setRoles(Collections.singleton(role));

        userService.save(user);
    }

    @Transactional
    @Test
    public void test08UserAddLogs() {
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
    public void test09UserRemoveLogs() {
        printIndex();
        User user = userService.findUserWithLogsByAccount("cloud0072");

        user.getExtend().getLogs().clear();

        userService.save(user);
    }

    @Transactional
    @Test
    public void test10UserFindRoles() {
        printIndex();
        User user = userService.findUserByAccount("cloud0072");
        System.out.println(user.getRoles());
    }

    //    @Test
    public void test11UserRemoveRoles() {
        printIndex();
        User user = userService.findAuthorInfoByAccount("cloud0072");
        user.getRoles().remove(0);
        userService.save(user);
    }

    //    @Test
    public void test13UserRemove() {
        userService.delete(userService.findUserByAccount("cloud0072"));
    }


    @Test
    public void test14JoinFind() {

        List<Permission> permissions = permissionRepository.findPermissionsByRoles_Users_AccountEquals("admin");

        permissions.forEach(permission -> System.out.println(permission.getName()));
    }

    @Test
    public void test18() {
//        List<EntityResource> entities = entityResourceRepository.findByPropertyEquals("name","User");
//        System.out.println(entities);
        Permission permission = permissionRepository.findOne((Specification<Permission>)
                (root, criteriaQuery, criteriaBuilder) -> null).orElse(null);

    }

    @Test
    public void changPassword(){
        User user = userService.findUserByAccount("admin");
        user.setPassword("admin");
        userService.update(user);
    }
}


