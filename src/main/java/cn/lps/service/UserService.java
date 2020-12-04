package cn.lps.service;

import cn.lps.model.base.Response;
import cn.lps.model.request.UserReq;

/**
 * @author liuyucai
 * @Created 2020/10/19
 * @Description
 */
public interface UserService {
    Response<UserReq> queryUserInfo(UserReq userInfoReq);
}
