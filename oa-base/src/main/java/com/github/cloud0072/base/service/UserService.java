package com.github.cloud0072.base.service;

import com.github.cloud0072.base.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cloud0072
 */
public interface UserService
        extends BaseCrudService<User>, UserDetailsService {

    /**
     * 注册用户 对用户密码进行加密并保存
     * @param user
     * @param request
     * @param response
     * @return
     */
    User saveWithoutEncrypt(User user,
                            HttpServletRequest request,
                            HttpServletResponse response);

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
