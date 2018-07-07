package com.caolei.system.pojo;


import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.TableConstant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


/**
 * 用户详情
 * @author cloud0072
 * @date 2018/6/12 23:06
 */
@Table
@Entity
public class UserDetail extends BaseEntity {

    @Column
    private Date birthday;

    @Override
    public String tableName() {
        return TableConstant.USER_DETAILS;
    }

}
