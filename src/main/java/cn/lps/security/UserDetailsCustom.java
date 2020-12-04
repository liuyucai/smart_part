package cn.lps.security;
import cn.lps.model.request.UserReq;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * 自定义用户详细信息类
 */
@ToString
@Data
public class UserDetailsCustom implements UserDetails {

    private boolean enabled;
    private Boolean isAdmin = false;
    private UserReq userReq;
    private Collection<? extends GrantedAuthority> authorities;

//    这个必须写，且返回值，否则无法调用成功处理器
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return userReq.getPassword();
    }
    @Override
    public String getUsername() {
        return userReq.getUserName();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
