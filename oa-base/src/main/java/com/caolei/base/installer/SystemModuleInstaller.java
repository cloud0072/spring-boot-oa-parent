package com.caolei.base.installer;

import com.caolei.base.extend.EntityResource;
import com.caolei.base.pojo.Permission;
import com.caolei.base.pojo.Role;
import com.caolei.base.pojo.User;
import com.caolei.base.repository.EntityResourceRepository;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.base.service.UserService;
import com.caolei.common.api.entity.BaseEntity;
import com.caolei.common.constant.Operation;
import com.caolei.common.util.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * 自动初始化数据库
 *
 * @author cloud0072
 * @date 2018/6/12 22:39
 */
@Slf4j
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
        log.error("start ...");

        initialize();

        log.error("finished...");
    }

    /**
     * 初始化数据库
     *
     * @author cloud0072
     * @date 2018/6/12 22:39
     */
    @Transactional(rollbackFor = Exception.class)
    protected void initialize() {
        /*
         * 添加实体
         */
        Set<Class<?>> classes = ReflectUtils.getClasses("com.caolei.base.pojo");
        List<EntityResource> entityResources = classes.stream().filter(BaseEntity.class::isAssignableFrom)
                .map(clazz -> new EntityResource((Class<? extends BaseEntity>) clazz)).collect(toList());

        List<EntityResource> hasExistEntityResources = entityResourceRepository.findAll();
        entityResources.removeIf(entity -> hasExistEntityResources.stream().anyMatch(has -> has.getName().equals(entity.getName())));
        try {
            entityResourceRepository.saveAll(entityResources);
        } catch (Exception ignored) {
        }
        /*
         * 添加权限
         */
        entityResources.addAll(hasExistEntityResources);
        List<Permission> hasExistPermissions = permissionService.findAll();
        List<Permission> authPermissions = new ArrayList<>();

        entityResources.forEach(entity -> Arrays.stream(Operation.values())
                .forEach(operation -> authPermissions.add(new Permission(entity, operation, null, true))));
        authPermissions.removeIf(entity -> hasExistPermissions.stream().anyMatch(has -> has.getName().equals(entity.getName())));
        try {
            permissionService.saveAll(authPermissions);
        } catch (Exception ignored) {
        }

        /*
         * 注册分组
         */
        List<Role> hasExistRoles = roleService.findAll();
        Role superuserRole = new Role("超级管理员", "superuser", "拥有管理系统所有权限", true);
        Role userRole = new Role("用户", "user", "普通用户", true);
        if (hasExistRoles.stream().noneMatch(role -> role.getCode().equals(superuserRole.getCode()))) {
            List<Role> roles = new ArrayList<>();
            roles.add(superuserRole);
            roles.add(userRole);
            roles.removeAll(hasExistRoles);
            //先保存一次，这样Role才有id 不然直接多对多保存会导致Role一方id没有初始化
            try {
                roleService.saveAll(roles);
            } catch (Exception ignored) {
            }
            //superUser
            Permission superUserPermission = new Permission(null, Operation.ALL, null, true);
            superuserRole.setPermissions(permissionService.findAll(Example.of(superUserPermission)));
            //user
            Permission userPermission = new Permission(new EntityResource(User.class), Operation.ALL, null, true);
            userRole.setPermissions(permissionService.findAll(Example.of(userPermission)));
            try {
                roleService.saveAll(roles);
            } catch (Exception ignored) {
            }
        }

        /*
         * 注册用户
         */
        String account = "admin";
        if (null == userService.findUserByAccount(account)) {
            User admin = new User(account, account, account, null, true).setDefaultValue();
            admin.setSuperUser(true);
            userService.save(admin);

            admin = userService.findAuthorInfoByAccount(account);
            admin.getRoles().addAll(Arrays.asList(superuserRole, userRole));
            try {
                userService.save(admin);
            } catch (Exception ignored) {
            }
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
