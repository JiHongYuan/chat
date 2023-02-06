package org.github.jhy.chat.server.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * HikariCP
 *
 * @author jihongyuan
 * @date 2023/2/6 10:52
 */
public class HikariCPDataSourceFactory extends UnpooledDataSourceFactory {
    public HikariCPDataSourceFactory() {
        this.dataSource = new HikariDataSource();
    }
}
