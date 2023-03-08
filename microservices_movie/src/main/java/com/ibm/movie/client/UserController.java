package com.ibm.movie.client;

import com.ibm.movie.pojo.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 通过OpenFeign实现服务调用，整合Hystrix实现服务熔断，防止应用雪崩效应
 */
@FeignClient(value = "microservices-user",fallback = UserControllerImpl.class)
public interface UserController {

    /*
    Tips:1、RequestMapping中的value注意要完整路径
         2、PathVariable中必须要带参数
         3、主程序加自动装配Feign注解 @EnableFeignClients
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public User findById(@PathVariable(value = "id") Integer id);


}
