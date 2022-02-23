package com.lcg.oa.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/*执行顺序：首先通过构造方法创建空的数据源对象，然后调用setProperties方法读取xml文件
 * 并对刚才实例化的数据源进行属性设置；如果在获取数据源的时候必须要进行额外操作，则应重写
 * getDataSource方法*/
public class DruidDataSourceFactory extends UnpooledDataSourceFactory {
    public DruidDataSourceFactory() {
        this.dataSource = new DruidDataSource();
    }

    /**
     * DataSource是javax.sql包提供的接口，用来说明所有的数据源都要实现这个接口
     * DruidDataSource当然也实现了这个接口；这样，当需要更换数据库连接池时，只需要
     * 实例化不同的DataSource实现类即可
     */
    @Override//进行重写代码：code->override，是否进行重写视连接池的类型而定
    public DataSource getDataSource() {
        try {
            ((DruidDataSource) this.dataSource).init();//初始化Druid数据源
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.dataSource;
    }
}
