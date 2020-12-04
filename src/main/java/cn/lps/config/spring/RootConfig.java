package cn.lps.config.spring;
import cn.lps.config.spring.AsyncExecutorConfig;
import cn.lps.config.spring.DataConfig;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 主配置类
 * Created by Yan on 2018/01/15.
 */
@Configuration
@Import({SecurityConfig.class,AsyncExecutorConfig.class, DataConfig.class})//加载其他配置类
@ComponentScan(basePackages = {
        "cn.lps.service",
        "cn.lps.dao",
        "cn.lps.security",
        }
        , excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})//配置自动扫描包
@EnableAspectJAutoProxy//开启Aop代理
@EnableScheduling//开启spring计划任务支持
public class RootConfig {
    /**
     * @Author Qiang Yan
     * @Description 配置验证框架
     * @Date 2018/1/16 15:38
     * @Modified
     */
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
        return localValidatorFactoryBean;
    }
}
