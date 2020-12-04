package cn.lps.config.spring;


import cn.lps.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

@Configuration
/** 启用web安全性 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private Environment env;

    @Autowired
    private FilterSecurityInterceptorCustom filterSecurityInterceptorCustom;


    @Override
    public void configure(WebSecurity web) {
        // 设置不拦截规则
        web.ignoring().antMatchers("/common/**","/css/**","/fonts/**","/images/**","/js/**","/plug-in/**","/img/**","/login","/imageAuthentication/getImage");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭X-Frame-Options 允许iframe
        http.headers().frameOptions().disable();

        //自定义用户名/密码拦截器
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(true);
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager());

        //设置验证失败后的处理器
        loginAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        //设置验证成功后的处理器
        loginAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());

        // 设置拦截规则
        // 自定义accessDecisionManager访问控制器,并开启表达式语言
        http
                .addFilterBefore(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling()
                .and()
                .addFilterAt(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).exceptionHandling()
                .and()
                .addFilterAfter(filterSecurityInterceptorCustom, UsernamePasswordAuthenticationFilter.class).exceptionHandling()
                .and()
                //设置不需要拦截的url
                .authorizeRequests()
                    .antMatchers("/","/login/**","/api/**","/gateway/api/**","/management/error/illegal").permitAll()
                    // 其他地址的访问均需验证权限（需要登录）
                    .anyRequest().authenticated()
                    .and()
//                .addFilterBefore(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling()
//                .and()
                // 指定登录页面的请求路径
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                // 成功退出登录后的url可以用logoutSuccessUrl设置
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    .and()
                // 关闭csrf
                .csrf().disable();
        //session失效后跳转
//        http.sessionManagement().invalidSessionUrl("/login");
//        //只允许一个用户登录,如果同一个账户两次登录,那么第一个账户将被踢下线,跳转到登录页面
//        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true).expiredUrl("/login");
        //登出后删除cookies，清空session
//        http.logout().deleteCookies("JSESSIONID")
//                .invalidateHttpSession(Boolean.valueOf(true));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.eraseCredentials(false);
    }

    @Bean
    public UserDetailsServiceCustom userDetailsServiceImpl() {
        return new UserDetailsServiceCustom();
    }

    @Bean
    public AuthenticationProviderCustom authenticationProvider() {
        AuthenticationProviderCustom provider = new AuthenticationProviderCustom();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsServiceImpl());
//        provider.setPasswordEncoder(new Md5PasswordEncoder());
        //设置盐值
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse(env.getProperty("user.property.to.use"));
        provider.setSaltSource(saltSource);
        return provider;
    }

    /**
     * @Author liuyucai
     * @Description 配置身份验证成功处理器
     * @Date 2020/10/19 17:36
     * @Modified
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new AuthenticationSuccessHandlerCustom(env.getProperty("authentication.success.url"),
//                Integer.parseInt(env.getProperty("session.time.out")));
        return new AuthenticationSuccessHandlerCustom();
    }

    /**
     * @Author liuyucai
     * @Description 配置身份验证失败处理器
     * @Date 2020/10/19 17:36
     * @Modified
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login/fail");
    }

    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilterCustom();
        usernamePasswordAuthenticationFilter.setPostOnly(true);
        usernamePasswordAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        //session并发控制,因为默认的并发控制方法是空方法.这里必须自己配置一个
//        usernamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry()));
        return usernamePasswordAuthenticationFilter;
    }




}
