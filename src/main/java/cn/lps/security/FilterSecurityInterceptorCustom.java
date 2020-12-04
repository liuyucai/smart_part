package cn.lps.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 访问权限校验拦截器
 */
@Component
@Slf4j
public class FilterSecurityInterceptorCustom extends AbstractSecurityInterceptor implements Filter{

    @Autowired
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Autowired
    @Qualifier("accessDecisionManagerCustom")
    @Override
    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        super.setAccessDecisionManager(accessDecisionManager);
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpSession session = req.getSession();
        if (session.getAttribute("loginUser") != null) {
            UserDetailsCustom custom = (UserDetailsCustom) session.getAttribute("loginUser");
            if (!custom.getIsAdmin()) {//超级管理员跳过校验
                FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
                invoke(fi);//权限校验，校验用户是否拥有该权限
            } else {
                //如果是admin用户，跳过校验
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
    /**
     * @Author liuyucai
     * @Description 校验权限
     * @Date 2020/10/20 15:47
     */
    private void invoke(FilterInvocation fi) throws IOException, ServletException {
        InterceptorStatusToken token = null;
        try {
            /**
             * beforeInvocation(Object object)
             * 对访问受保护对象的权限校验
             * 该方法内部流程为：
             * ->SecurityMetadataSource 获取ConfigAttribute属性信息（从数据库或者其他数据源地方）
             *          getAttributes()
             *      ->AccessDecisionManager()  基于AccessDecisionVoter实现授权访问
             *               Decide()
             *            ->AccessDecisionVoter  受AccessDecisionManager委托实现授权访问
             *                    vote()
             * 实现：
             * 1、 创建一个自定义的SecurityMetadataSource类，从数据库中获取权限集合
             *          FilterInvocationSecurityMetadataSourceCustom
             * 2、 创建一个自定义的AccessDecisionManager类，重写Decide()方法，判断该用户是否拥有权限，并返回
             *          AccessDecisionManagerCustom
             */
            token = super.beforeInvocation(fi);
        } catch (Exception e) {
            if (e instanceof AccessDeniedException) {
                log.error(" 尝试访问无权访问的地址：" + fi.getRequestUrl(),e);
                //这里必须要注意，每个用户必须都有/management/error/illegal的url权限，否则会一直循环
                fi.getHttpResponse().sendRedirect("/management/error/illegal");//无权限访问，重定向到无权限页面
            }
            return;
        }
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }
}
