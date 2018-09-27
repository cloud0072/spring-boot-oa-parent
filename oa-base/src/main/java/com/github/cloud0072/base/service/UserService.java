package com.github.cloud0072.base.service;

import com.github.cloud0072.base.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cloud0072
 */
public interface UserService
        extends BaseCrudService<User> , UserDetailsService {

    /**
     *
     * @param user
     * @param request
     * @param response
     * @param encrypt
     * @return
     */
    User save(User user,
              HttpServletRequest request,
              HttpServletResponse response,
              boolean encrypt);

//    /**
//     * 登陆
//     *
//     * @param user
//     * @return
//     */
//    boolean login(User user);
//
//    /**
//     * 登出
//     */
//    void logout();

    /**
     * 查询用户
     *
     * @param account
     * @return
     */
    User findUserByUsername(String account);

    /**
     * 懒加载
     *
     * @param account
     * @return
     */
    User findUserWithLogsByUsername(String account);


    /**
     * 返回用户角色
     *
     * @param account
     * @return
     */
    User findAuthorInfoByUsername(String account);

    /**
     * 修改密码
     * @param userId
     * @param password
     * @param newpassword
     */
    String resetpwd(String userId, String password, String newpassword);
}
