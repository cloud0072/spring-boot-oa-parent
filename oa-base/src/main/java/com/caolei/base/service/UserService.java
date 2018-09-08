package com.caolei.base.service;

import com.caolei.common.api.service.BaseCrudService;
import com.caolei.base.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cloud0072
 */
public interface UserService
        extends BaseCrudService<User> {
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
     *
     * @param account
     * @return
     */
    User findUserWithLogsByAccount(String account);


    /**
     * 返回用户角色
     *
     * @param account
     * @return
     */
    User findAuthorInfoByAccount(String account);

}
