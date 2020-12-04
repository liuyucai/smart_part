package cn.lps.service;

import cn.lps.model.base.Response;
import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author liuyucai
 * @Created 2020/10/20
 * @Description
 */
public interface SystemService {
    Response<HashMap<String, Collection<ConfigAttribute>>> getAuthorityResourceMap();
}
