package cn.lps.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class LpsHttpServiceRemote {

    public static String remote(String content,String uri) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String resultJson = "";
        HttpPost httppost = new HttpPost(uri);
        try {
            HttpEntity re = new StringEntity(content, HTTP.UTF_8);
            httppost.setHeader("Content-Type","application/json; charset=utf-8");
            httppost.setEntity(re);
            log.info("开始调用");
            log.info("请求内容：" + content);
            HttpResponse response = httpClient.execute(httppost);
            System.out.println("响应结果：");
            resultJson = getContent(response);
        } catch (UnsupportedEncodingException e) {
            log.error("",e);
        } catch (ClientProtocolException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        } catch (Exception e) {
            log.error("", e);
        } finally{
            httpClient.getConnectionManager().shutdown();
        }
        return resultJson;
    }

    private static String getContent(HttpResponse response) throws Exception {
        StringBuilder sb = new StringBuilder();
        String resultContent = "";
        if (response.getStatusLine().getStatusCode() == 200) {
            sb.append(response.getStatusLine());
            sb.append("\n");
            sb.append(StringUtils.join(response.getAllHeaders(), "\n"));
            if (null != response.getEntity()) {
                try {
                    sb.append("\n");
                    resultContent = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    sb.append(resultContent);
                } catch (Exception e) {
                    sb.append("\n");
                    sb.append("转换出错");
                }
            }
        } else {
            log.error("远程访问失败！");
        }
        log.info(sb.toString());
        return resultContent;
    }
}
