package com.github.cloud0072.base.model.extend;

import com.github.cloud0072.base.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Creator implements Serializable {

    @Column
    private LocalDateTime createTime;
    @Column
    private LocalDateTime modifyTime;
    @Column
    private User createUser;
    @Column
    private User modifyUser;

    public Creator modifyBy(User user) {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        if (createUser == null) {
            createUser = user;
        }
        modifyTime = LocalDateTime.now();
        modifyUser = user;
        return this;
    }

}
