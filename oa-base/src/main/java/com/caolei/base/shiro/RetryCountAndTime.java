package com.caolei.base.shiro;

import com.caolei.common.autoconfig.Shiro;
import org.apache.shiro.authc.ExcessiveAttemptsException;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryCountAndTime implements Serializable {

    private Shiro shiro;
    private AtomicInteger count;
    private long expiredTime;

    public RetryCountAndTime(Shiro shiro) {
        this.shiro = shiro;
        this.count = new AtomicInteger(0);
        this.expiredTime = System.currentTimeMillis() + shiro.getLockExpiredTime() * 1000;
    }

    public void checkRetryTimes() {
        //没有设置重试次数则默认跳过检测
        if (shiro.getLoginRetryTimes() < 1) {
            return;
        }
        //过期重置验证信息
        if (expiredTime < System.currentTimeMillis()) {
            this.count = new AtomicInteger(0);
            this.expiredTime = System.currentTimeMillis() + shiro.getLockExpiredTime() * 1000;
        }
        if (shiro.getLoginRetryTimes() < count.incrementAndGet()) {
            long second = (expiredTime - System.currentTimeMillis()) / 1000 + 1;
            String timeMessage = second > 60 ? second / 60 + "分钟后重试" : second + "秒后重试";
            throw new ExcessiveAttemptsException("您的登录失败次数过多! 请您在" + timeMessage);
        }
    }
}

