package com.github.chat.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jihongyuan
 * @date 2023/2/6 11:24
 */
@Slf4j
public class MybatisContext {

    private static SqlSessionFactory sqlSessionFactory;

    public static synchronized SqlSession getSqlSession() {
        if (sqlSessionFactory == null) {

            String resource = "mybatis-config.xml";
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream(resource);
            } catch (IOException e) {
                log.error("Mybatis load resource error", e);
                throw new RuntimeException(e);
            }
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
        return sqlSessionFactory.openSession();
    }

}
