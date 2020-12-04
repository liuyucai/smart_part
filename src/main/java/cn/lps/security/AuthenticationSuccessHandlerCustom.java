package cn.lps.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 验证成功后自定义实现类
 */
@Slf4j
public class AuthenticationSuccessHandlerCustom implements AuthenticationSuccessHandler {

    /**
     * 成功后跳转的地址
     */
    private final String success_url;
    /**
     * session超时事件默认30分钟
     */
    private Integer seesion_time_out = 30;
    private static final String DEFAULT_PASSWORD_EDIT_MSG = "您的账号还在使用默认密码，为了您的账号及系统安全，请尽快修改您账号的密码！";

    public AuthenticationSuccessHandlerCustom() {
        this.success_url = "/";
    }
    public AuthenticationSuccessHandlerCustom(String success_url) {
        this.success_url = success_url;
    }

    /**
     * @Author Qiang Yan
     * @Description 重写onAuthenticationSuccess
     * @Date 2018/1/17 09:50
     * @Modified
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletRequest.getSession().setMaxInactiveInterval(seesion_time_out * 60);
        //验证通过设置用户信息到session
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        httpServletRequest.getSession().setAttribute("loginUser", userDetails);
//        String password = PasswordUtil.encodePassword(userDetails.getUserAccount() + BaseConstants.DEFAULT_PASSWORD, userDetails.getSalt());
//        if (password.equals(userDetails.getPassword())) {
//            httpServletRequest.getSession().setAttribute("DEFAULT_PASSWORD_EDIT_MSG", DEFAULT_PASSWORD_EDIT_MSG);
//        }
        String userAGent = httpServletRequest.getHeader("User-Agent");
        httpServletRequest.getSession().setAttribute("staticFileV",System.currentTimeMillis());//静态日志版本号
//        String hostIp = getHostIp();
//        loginLogAsync.log(userAGent, userDetails, httpServletRequest.getHeader("X-Real-IP"), hostIp);//异步新增登录日志
//        httpServletResponse.sendRedirect(success_url);
    }

//    private static String getHostIp(){
//        try{
//            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
//            while (allNetInterfaces.hasMoreElements()){
//                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
//                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
//                while (addresses.hasMoreElements()){
//                    InetAddress ip = (InetAddress) addresses.nextElement();
//                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
//                            && ip.getHostAddress().indexOf(":")==-1){
//                        return ip.getHostAddress();
//                    }
//                }
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
}
