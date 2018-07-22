package com.caolei.system.api;

import com.caolei.system.pojo.User;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
public class Creator {

    @Column
    private Date createTime;
    @Column
    private Date modifyTime;
    @Column
    private User createUser;
    @Column
    private User modifyUser;

    public void modify(User user) {
        if (createTime == null) {
            createTime = new Date();
        }
        if (createUser == null) {
            createUser = user;
        }
        modifyTime = new Date();
        modifyUser = user;
    }

    public Creator() {
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public User getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(User modifyUser) {
        this.modifyUser = modifyUser;
    }

}
