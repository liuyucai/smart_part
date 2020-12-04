package cn.lps.config.start;

import cn.lps.config.spring.RootConfig;
import cn.lps.config.spring.WebMvcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @Author Qiang Yan
 * @Description 启动
 * @Date Created in 2018/01/15 11:27
 * @Modified By
 */
@Slf4j
public class PortalOnStartup extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 系统启动时注册filter，启动Security,下面的名称固定
        FilterRegistration securityFilter = servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
        securityFilter.addMappingForUrlPatterns(null, false, "/*");

        // 静态资源映射
        servletContext.getServletRegistration("default").addMapping("*.html", "*.ico","*.js","*.css");
        servletContext.addListener(new HttpSessionEventPublisher());
        super.onStartup(servletContext);
    }


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebMvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 配置servlet过滤器
     *
     * @return
     * @Author liangjianming
     */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{characterEncodingFilter};
    }

    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        servletContext.addListener(RequestContextListener.class);
        super.registerContextLoaderListener(servletContext);
    }
}
