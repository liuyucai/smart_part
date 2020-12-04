package cn.lps.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 验证码验证及密码解密
 */
@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // 是否开启验证码功能
    private final boolean isOpenValidateCode;

    public LoginAuthenticationFilter(Boolean isOpenValidateCode) {
        this.isOpenValidateCode = isOpenValidateCode;
    }

//    父类AbstractAuthenticationProcessingFilter在dofitter方法中，
//    判断请求路径为/login,且提交方式为post时会执行
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (isOpenValidateCode) {//校验验证码
//            checkValidateCode(request);
        }
//        AuthenticationProviderCustom.privateKey = (String) session.getAttribute(BaseConstants.PASSWORD_PRIVATE_KEY);//设置解密的私钥
//        session.setAttribute(BaseConstants.PASSWORD_PRIVATE_KEY, null);//清空参数
        return super.attemptAuthentication(request, response);
    }


}
