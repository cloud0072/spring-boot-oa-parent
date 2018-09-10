package com.caolei.base.service.impl;

import com.caolei.base.pojo.Role;
import com.caolei.base.repository.RoleRepository;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.common.util.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cloud0072
 */
@Service
public class RoleServiceImpl
        implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private EhCacheManager shiroCacheManager;

    @Override
    public JpaRepository<Role, String> repository() {
        return roleRepository;
    }

    @Override
    public Role update(Role input,
                       HttpServletRequest request,
                       HttpServletResponse response) {

        Role role = findById(input.getId());
        role.setName(input.getName());
        role.setCode(input.getCode());
        role.setDescription(input.getDescription());
        if (input.getPermissions() != null) {
            role.setPermissions(input.getPermissions());
        }
        save(role);
        return role;
    }

    @Override
    public Role findRoleByName(String name) {
        Role role = new Role();
        role.setName(name);
        return find(Example.of(role));
    }

    @Override
    public Role findRoleByCode(String code) {
        Role role = new Role();
        role.setCode(code);
        return find(Example.of(role));
    }

    @Override
    public void clearRoleCache() {
        Cache<Object, Object> cache = shiroCacheManager.getCache("org.apache.shiro.realm.jdbc.JdbcRealm.authorizationCache");
//        shiroCacheManager.destroy();//清除全部缓存
//        LifecycleUtils.destroy(cache);//清除某个缓存
        Subject subject = SecurityUtils.getSubject();
        /*subject.getPrincipal()------>登录名
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        //第一个参数为用户名,想要操作权限的用户，第二个参数为realmName,
        SimplePrincipalCollection principals = new SimplePrincipalCollection(subject.getPrincipal(),realmName);
        */
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        JdbcRealm jdbcRealm = (JdbcRealm) securityManager.getRealms().iterator().next();
        //删除登陆人
        jdbcRealm.getAuthorizationCache().remove(subject.getPrincipal());
        //删除登陆人对应的缓存
        jdbcRealm.getAuthorizationCache().remove(subject.getPrincipals());
        //重新加载subject
        subject.releaseRunAs();
    }
}