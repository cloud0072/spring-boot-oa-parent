package com.github.cloud0072.base.config;

import com.github.cloud0072.base.model.BaseEntity;
import com.github.cloud0072.base.model.Permission;
import com.github.cloud0072.base.model.Role;
import com.github.cloud0072.base.model.User;
import com.github.cloud0072.base.model.extend.EntityResource;
import com.github.cloud0072.base.repository.EntityResourceRepository;
import com.github.cloud0072.base.service.PermissionService;
import com.github.cloud0072.base.service.RoleService;
import com.github.cloud0072.base.service.UserService;
import com.github.cloud0072.common.annotation.EntityInfo;
import com.github.cloud0072.common.constant.Operation;
import com.github.cloud0072.common.util.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 自动初始化数据库,注册功能
 *
 * @author cloud0072
 * @date 2018/6/12 22:39
 */
@Slf4j
@Component
public class BaseModuleRegister
        implements ApplicationRunner, Ordered {

    @Autowired
    ServletContext ctx;
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
        log.info("start ...");

        initializeDB();
//        RequestMappingInfo();


        Global.addModule("base", "base");

        log.info("finish ...");
    }

    /**
     * 初始化数据库
     *
     * @author cloud0072
     * @date 2018/6/12 22:39
     */
    @Transactional(rollbackFor = Exception.class)
    protected void initializeDB() {
        /*
         * 添加实体
         */
        List<EntityResource> entityResources = ReflectUtils.getClasses("com.github.cloud0072").stream()
                .filter(clazz -> AnnotationUtils.isAnnotationDeclaredLocally(EntityInfo.class, clazz))
                .filter(BaseEntity.class::isAssignableFrom)
                .map(clazz -> new EntityResource((Class<? extends BaseEntity>) clazz))
                .collect(Collectors.toList());

        List<EntityResource> hasExistEntityResources = entityResourceRepository.findAll();
        entityResources.removeIf(entity -> hasExistEntityResources.stream()
                .anyMatch(has -> has.getName().equals(entity.getName())));
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
        authPermissions.removeIf(entity -> hasExistPermissions.stream()
                .anyMatch(has -> has.getName().equals(entity.getName())));
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
        if (hasExistRoles.stream()
                .noneMatch(role -> role.getCode().equals("superuser"))) {
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
        if (null == userService.findUserByUsername(account)) {
            User admin = new User(account, account, account, null, true).setDefaultValue();
            admin.setSuperUser(true);
            userService.save(admin, null, null, false);

            admin = userService.findAuthorInfoByUsername(account);
            Role superuser = roleService.findRoleByCode("superuser");
            Role user = roleService.findRoleByCode("user");
            admin.getRoles().addAll(Arrays.asList(superuser, user));
            try {
                userService.save(admin, null, null, true);
            } catch (Exception ignored) {
            }
        }

    }

    /**
     * 获取所有url的映射
     */
    public void requestMappingInfo() {
        WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(ctx);

        //获取所有的RequestMapping
        Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext,
                HandlerMapping.class, true, false);

        for (HandlerMapping handlerMapping : allRequestMappings.values()) {
            //本项目只需要RequestMappingHandlerMapping中的URL映射
            if (handlerMapping instanceof RequestMappingHandlerMapping) {
                Map<org.springframework.web.servlet.mvc.method.RequestMappingInfo, HandlerMethod> handlerMethods = ((RequestMappingHandlerMapping) handlerMapping).getHandlerMethods();

                for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet()) {
                    RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
                    HandlerMethod mappingInfoValue = requestMappingInfoHandlerMethodEntry.getValue();

                    PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
                    String controllerName = mappingInfoValue.getBeanType().toString().replace("class", "").trim();
                    String requestMethodName = mappingInfoValue.getMethod().getName();
                    Class<?>[] methodParamTypes = mappingInfoValue.getMethod().getParameterTypes();

                    log.info("patternsCondition :\t" + patternsCondition);
                    log.info("controllerName    :\t" + controllerName);
                    log.info("methodParamTypes  :\t" + Arrays.stream(methodParamTypes).map(Class::getName).collect(toList()));
                    log.info("requestMethodName :\t" + requestMethodName);
                    log.info("==================");

                }
                break;
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
