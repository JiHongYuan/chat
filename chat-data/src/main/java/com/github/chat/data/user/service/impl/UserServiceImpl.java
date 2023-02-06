package com.github.chat.data.user.service.impl;

import com.github.chat.data.MybatisContext;
import com.github.chat.data.user.entity.UserEntity;
import com.github.chat.data.user.mapper.UserMapper;
import com.github.chat.data.user.service.UserService;
import org.apache.ibatis.session.SqlSession;

/**
 * @author jihongyuan
 * @date 2023/2/6 11:12
 */
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final SqlSession sqlSession;
    public UserServiceImpl() {
        this.sqlSession = MybatisContext.getSqlSession();
        this.userMapper = sqlSession.getMapper(UserMapper.class);
    }

    @Override
    public boolean existUsername(String username){
        return userMapper.selectCountByUsername(username) > 0;
    }

    @Override
    public void login(String username, String password) {
        // TODO 密码解密
        UserEntity userEntity = userMapper.selectOneByUsernameAndPassword(username, password);

        if (userEntity == null) {
            throw new RuntimeException("账号或密码错误");
        }

        String type = userEntity.getStatus();
        if (!"1".equals(type)) {
            throw new RuntimeException("状态异常");
        }
    }

    @Override
    public void add(String username, String password) {
        UserEntity user = UserEntity.builder()
                .username(username)
                .password(password)
                .status("1")
                .build();
        userMapper.insertUser(user);
        sqlSession.commit();
    }

}
