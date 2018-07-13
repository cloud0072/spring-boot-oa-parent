package com.caolei.system.installer;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.Operation;
import com.caolei.system.po.EntityResource;
import com.caolei.system.pojo.Permission;
import com.caolei.system.pojo.Role;
import com.caolei.system.pojo.User;
import com.caolei.system.repository.EntityResourceRepository;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import com.caolei.system.utils.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 自动初始化数据库
 *
 * @author cloud0072
 * @date 2018/6/12 22:39
 */
@Component
public class SystemModuleInstaller
        implements ApplicationRunner, Ordered {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private EntityResourceRepository entityResourceRepository;

    @Override
    public void run(ApplicationArguments args) {
        System.err.println("ModuleInstaller -> ModuleInstaller start...");

        initialize();

        System.err.println("ModuleInstaller -> system_module finished...");
    }

    /**
     * 初始化数据库
     *
     * @author cloud0072
     * @date 2018/6/12 22:39
     */
    @Transactional(rollbackFor = Exception.class)
    void initialize() {
        /*
         * 添加实体
         */
        Set<Class<?>> classes = ReflectUtils.getClasses("com.caolei.system.pojo");
        List<EntityResource> hasExistEntityResources = entityResourceRepository.findAll().stream()
                .peek(entityResource -> entityResource.setId(null)).collect(toList());
        List<EntityResource> entityResources = new ArrayList<>();

        classes.forEach(clazz -> {
            if (BaseEntity.class.isAssignableFrom(clazz)) {
                entityResources.add(new EntityResource((Class<? extends BaseEntity>) clazz));
            }
        });
        entityResources.removeAll(hasExistEntityResources);
        entityResourceRepository.saveAll(entityResources);

        /*
         * 添加权限
         */
        entityResources.addAll(hasExistEntityResources);
        List<Permission> hasExistPermissions = permissionService.findAll().stream()
                .peek(permission -> permission.setId(null)).collect(toList());
        List<Permission> authPermissions = new ArrayList<>();

        entityResources.forEach(entity -> Arrays.stream(Operation.values())
                .forEach(operation -> authPermissions.add(new Permission(entity, operation, null, true))));
        authPermissions.removeAll(hasExistPermissions);
        permissionService.saveAll(authPermissions);

        /*
         * 注册分组
         */
        List<Role> hasExistRoles = roleService.findAll().stream()
                .peek(role -> role.setId(null)).collect(toList());
        Role superuserRole = new Role("超级管理员", "superuser", "拥有管理系统所有权限", true);
        Role userRole = new Role("用户", "user", "普通用户", true);
        if (!hasExistRoles.contains(superuserRole)) {
            List<Role> roles = new ArrayList<>();
            roles.add(superuserRole);
            roles.add(userRole);
            roles.removeAll(hasExistRoles);
            //先保存一次，这样Role才有id 不然直接多对多保存会导致Role一方id没有初始化
            roleService.saveAll(roles);

            //superUser
            superuserRole.setPermissions(permissionService.findAll(Example.of(new Permission(null, Operation.ALL, null, true))));
            //user
            userRole.setPermissions(permissionService.findAll(Example.of(new Permission(new EntityResource(User.class), Operation.ALL, null, true))));
            roleService.saveAll(roles);
        }

        /*
         * 注册用户
         */
        String account = "admin";
        if (null == userService.findUserByAccount(account)) {
            User admin = new User(account, account, account, null, true).setDefaultValue();
            admin.setSuperUser(true);
            userService.register(admin);
            admin = userService.findUserByAccount(account);
            admin.setRoles(Arrays.asList(superuserRole, userRole));
//            admin.setPermissions(permissionService.findAll());
            userService.save(admin);
        }

    }

    /**
     * run执行的顺序,从小到大
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:39
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
