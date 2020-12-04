package cn.lps.config.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"cn.lps.controller"})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * @Description: 配置视图解析器
     * @param:
     * @return:
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * @Description: 配置文件上传解析器
     * @param:
     * @return:
     * @author: YanQiang
     * @Date: 2018/1/15 14:45
     */
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        //设定文件上传的最大值10MB，15*1024*1024
        multipartResolver.setMaxUploadSize(500*1024*1024);
        multipartResolver.setDefaultEncoding("UTF-8");
        return multipartResolver;
    }


    /**
     * @Author Qiang Yan
     * @Description 重载该方法重新配置消息解析器
     * @Date 2018/1/18 18:56
     * @Modified
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.parseMediaType("text/html;charset=UTF-8"));
        mediaTypeList.add(MediaType.parseMediaType("text/plain;charset=UTF-8"));
        stringHttpMessageConverter.setSupportedMediaTypes(mediaTypeList);
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter = new BufferedImageHttpMessageConverter();
        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        ResourceHttpMessageConverter resourceHttpMessageConverter = new ResourceHttpMessageConverter();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        converters.add(stringHttpMessageConverter);
        converters.add(mappingJackson2HttpMessageConverter);
        converters.add(formHttpMessageConverter);
        converters.add(bufferedImageHttpMessageConverter);
        converters.add(byteArrayHttpMessageConverter);
        converters.add(resourceHttpMessageConverter);
        super.configureMessageConverters(converters);
    }


    //重写拦截器配置方法
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(STSReqInterceptor);//注册请求拦截器
//    }
}
