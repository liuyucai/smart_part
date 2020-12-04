package cn.lps.service.impl;

import cn.lps.model.base.Response;
import cn.lps.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author liuyucai
 * @Created 2020/10/20
 * @Description
 */
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {
    @Override
    public Response<HashMap<String, Collection<ConfigAttribute>>> getAuthorityResourceMap() {
        Response<HashMap<String, Collection<ConfigAttribute>>> response = new Response<>();

        HashMap<String, Collection<ConfigAttribute>> collectionHashMap = new HashMap<>();


        Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
        ConfigAttribute ca = new SecurityConfig("admin"); //将角色编码 封装成spring的权限配置属性
        atts.add(ca);
        collectionHashMap.put("/lps/carArrive", atts);
        collectionHashMap.put("/management/error/illegal", atts);

        response.setResult(collectionHashMap);
        return response;
    }
}
