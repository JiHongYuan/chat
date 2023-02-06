package com.github.chat.data.user.mapper;

import com.github.chat.data.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author jihongyuan
 * @date 2023/2/6 10:57
 */
@Mapper
public interface UserMapper {

    int selectCountByUsername(String username);

    UserEntity selectOneByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    void insertUser(UserEntity user);

}
