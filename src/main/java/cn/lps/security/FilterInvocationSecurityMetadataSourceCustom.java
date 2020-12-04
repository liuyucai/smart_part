package cn.lps.security;

import cn.lps.model.base.Response;
import cn.lps.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 通过请求地址获取权限角色集合
 */
@Component
@Slf4j
public class FilterInvocationSecurityMetadataSourceCustom implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private SystemService systemService;

    /**
     * @Author Qiang Yan
     * @Description 传入请求地址，返回拥有请求权限的角色集合
     * @Date 2018/2/5 16:30
     * @Modified
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String reqUrl = ((FilterInvocation) object).getRequestUrl();//获取请求的url
        int firstQuestionMarkIndex = reqUrl.indexOf("?");
        if (firstQuestionMarkIndex != -1) {//截取出URL地址
            reqUrl = reqUrl.substring(0, firstQuestionMarkIndex);
        }

        Iterator<String> iter = null;
        HashMap<String, Collection<ConfigAttribute>> result = null;

        try {

            Response<HashMap<String, Collection<ConfigAttribute>>> authorityResourceMap = systemService.getAuthorityResourceMap();
            if (authorityResourceMap.isSuccess()){
                result = authorityResourceMap.getResult();
                iter = result.keySet().iterator();
            }
        } catch (Exception e) {
            log.error("无法获取权限控制资源", e);
            throw new IllegalArgumentException();//如果无法获取权限控制资源，则不允许用户继续请求
        }

        String matchUrl = null;
        while (iter.hasNext()) {
            String url = iter.next();
            if (reqUrl.startsWith(url)) {//遍历比较请求的url是否在map里面
                if (matchUrl == null || matchUrl.length() < url.length()) { //初次匹配或当前匹配的url更长则更新匹配url
                    matchUrl = url;
                }
            }
        }
        if (!StringUtils.isEmpty(matchUrl)) {
            if(result!=null){
                return result.get(matchUrl);//取出有权的角色
            }else{
                return null;
            }
        }else {
            throw new AccessDeniedException("请求url为："+reqUrl+"，该用户未拥有此请求的权限！");//无权访问则向上抛出异常
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
