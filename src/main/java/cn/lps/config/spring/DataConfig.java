package cn.lps.config.spring;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.Properties;

/**
 * 数据库相关配置
 * Created by Yan on 2018/01/15.
 */
@Configuration
@MapperScan(basePackages = "cn.lps.dao", sqlSessionFactoryRef = "sqlSessionFactory")//配置扫描*mapper接口
@Slf4j
@EnableTransactionManagement//开启事务注解支持
@PropertySource(value = {"classpath:/druid.properties"})//加载配置文件
public class DataConfig {

    @Autowired
    private Environment env;

    /**
     * @Description: 创建druid数据源
     * @param:
     * @return: BasicDataSource
     * @author: YanQiang
     * @Date: 2018/1/15 10:59
     */
    @Bean(destroyMethod = "close")
    public DruidDataSource dataSource() {
        long beginDate = System.currentTimeMillis();
        DruidDataSource dataSource = new DruidDataSource();
        try {
            dataSource.setDriverClassName(env.getProperty("druid.driverClassName"));
            dataSource.setUrl(env.getProperty("druid.url"));
            dataSource.setUsername(env.getProperty("druid.userName"));
            dataSource.setPassword(env.getProperty("druid.password"));
            dataSource.setInitialSize(Integer.parseInt(env.getProperty("druid.initialSize")));
            dataSource.setMaxActive(Integer.parseInt(env.getProperty("druid.maxActive")));
            dataSource.setMaxIdle(Integer.parseInt(env.getProperty("druid.maxIdle")));
            dataSource.setMinIdle(Integer.parseInt(env.getProperty("druid.minIdle")));
            dataSource.setMaxWait(Long.parseLong(env.getProperty("druid.maxWait")));
            dataSource.setTestOnBorrow(false);
            dataSource.setTestWhileIdle(true);
            dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(env.getProperty("druid.timeBetweenEvictionRunsMillis")));
            dataSource.setNumTestsPerEvictionRun(Integer.parseInt(env.getProperty("druid.numTestsPerEvictionRun")));
            dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(env.getProperty("druid.minEvictableIdleTimeMillis")));
            dataSource.setRemoveAbandoned(true);
            dataSource.setRemoveAbandonedTimeout(Integer.parseInt(env.getProperty("druid.removeAbandonedTimeout")));
            log.info("创建Druid数据源完成...耗时：{}ms", System.currentTimeMillis() - beginDate);
        } catch (Exception e) {
            log.error("创建Druid数据源失败", e);
            throw e;
        }
        return dataSource;
    }


    /**
     * @Author Qiang Yan
     * @Description 注解式事务
     * @Date 2018/1/19 11:11
     * @Modified
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DruidDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * @Description: 配置SessionFactory
     * @param:
     * @return:
     * @author: YanQiang
     * @Date: 2018/1/15 11:00
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DruidDataSource dataSource) throws Exception {
        long beginDate = System.currentTimeMillis();
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource); //添加数据源
        // 设置MyBatis分页插件
//        sessionFactory.setPlugins(new Interceptor[]{this.initPageInterceptor(),this.initPerformanceInterceptor()});//添加拦截器
        try {
            sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:mapper/*.xml"));//配置*mapper.xml路径
        } catch (IOException io) {
            log.error("获取Mapper配置文件失败", io);
            throw io;
        }
        log.info("创建SqlSessionFactory完成...耗时：{}ms", System.currentTimeMillis() - beginDate);
        return sessionFactory.getObject();
    }

    /**
     * @Author Qiang Yan
     * @Description 配置分页插件拦截器
     * @Date 2018/1/19 11:47
     * @Modified
     */
    public PageInterceptor initPageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}
