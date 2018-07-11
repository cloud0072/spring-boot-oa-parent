package com.caolei.system.service;

import com.caolei.system.api.CrudService;
import com.caolei.system.pojo.User;

/**
 * @author cloud0072
 */
public interface UserService
        extends CrudService<User, String> {
    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    User register(User user);

    /**
     * 登陆
     *
     * @param user
     * @return
     */
    boolean login(User user);

    /**
     * 登出
     */
    void logout();

    /**
     * 查询用户
     *
     * @param account
     * @return
     */
    User findUserByAccount(String account);

    /**
     * 懒加载
     * @param account
     * @return
     */
    User findUserWithLogsByAccount(String account);


    /**
     * 返回用户角色
     * @param account
     * @return
     */
    User findAuthorInfoByAccount(String account);


}
