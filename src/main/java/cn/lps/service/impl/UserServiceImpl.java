package cn.lps.service.impl;

import cn.lps.model.base.Response;
import cn.lps.model.request.UserReq;
import cn.lps.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liuyucai
 * @Created 2020/10/19
 * @Description
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public Response<UserReq> queryUserInfo(UserReq userInfoReq) {
        Response<UserReq> response = new Response<>();
        UserReq userReq = new UserReq();
        userReq.setUserName("liuyucai");
        userReq.setPassword("123456");
        response.setResult(userReq);
        return response;

    }
}
