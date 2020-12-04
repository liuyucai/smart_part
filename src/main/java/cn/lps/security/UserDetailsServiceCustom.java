package cn.lps.security;

import cn.lps.model.base.Response;
import cn.lps.model.request.UserReq;
import cn.lps.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * 重写用户信息获取
 */
@Component
@Slf4j
public class UserDetailsServiceCustom implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * @Author Qiang Yan
     * @Description 重写用户信息加载方法
     * @Date 2018/1/17 16:42
     * @Modified
     */
    @Override
    public UserDetails loadUserByUsername(String userAccount) {
        userAccount= StringEscapeUtils.escapeHtml(userAccount.trim());
        userAccount = StringEscapeUtils.escapeJavaScript(userAccount.trim());
        userAccount =  StringEscapeUtils.escapeSql(userAccount.trim());

        UserReq userInfoReq = new UserReq();
        userInfoReq.setUserAccount(userAccount);

        Response<UserReq> response = userService.queryUserInfo(userInfoReq);

        if (response.isSuccess()) {
            Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
            auths.add(new SimpleGrantedAuthority("admin"));//设置用户角色集合

            UserReq resp = response.getResult();
            UserDetailsCustom userDetailsCustom = new UserDetailsCustom();
            userDetailsCustom.setUserReq(resp);
            userDetailsCustom.setEnabled(true);
            userDetailsCustom.setAuthorities(auths);
            return userDetailsCustom;
        } else {
            log.info("【" + userAccount + "】未找到该用户");
            throw new UsernameNotFoundException("");
        }

    }

    /**
     * @Author Qiang Yan
     * @Description 比较是否已失效
     * @Date 2018/1/29 10:59
     * @Modified
     */
    private Boolean isExpired(Date effectiveEndTime) {
        return effectiveEndTime.before(new Date());
    }
}
