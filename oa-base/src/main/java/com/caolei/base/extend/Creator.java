package com.caolei.base.extend;

import com.caolei.base.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
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

    public void modifyBy(User user) {
        if (createTime == null) {
            createTime = new Date();
        }
        if (createUser == null) {
            createUser = user;
        }
        modifyTime = new Date();
        modifyUser = user;
    }

}
