package com.ibm.movie.controller;

import com.ibm.movie.client.UserController;
import com.ibm.movie.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("movie")
public class MovieController {
    private static Log log = LogFactory.getLog(MovieController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private UserController userController;

    @RequestMapping(value = "/order",method = RequestMethod.GET)
    public String order(){
        //模拟当前用户
        Integer id = 2;

        //查询用户微服务，获取用户具体信息
        //使用RestTemplate远程调用户用户微服务
        /**
         * 参数一：调用url地址
         * 参数二：需要封装的对象类型
         */
        User user = restTemplate.getForObject("http://localhost:8081/user/"+id, User.class);

        System.out.println(user+"==正在购票...");


        return "购票成功";
    }

    /**
     * 从Eureka server获取服务提供方实例
     * @return
     */
    @RequestMapping(value = "/order2",method = RequestMethod.GET)
    public String orderByDiscovery(){
        //模拟当前用户
        Integer id = 2;

        //查询用户微服务，获取用户具体信息
        //使用RestTemplate远程调用户用户微服务
        /**
         * 参数一：调用url地址
         * 参数二：需要封装的对象类型
         */
        List<ServiceInstance> instanceList = discoveryClient.getInstances("microservices_user");
        ServiceInstance ins = instanceList.get(0);
        User user = restTemplate.getForObject("http://"+ ins.getHost() +":"+ ins.getPort() +"/user/"+id, User.class);
        System.out.println(user+"==正在购票...");


        return "购票成功";
    }


    /**
     * 使用Ribbon负载均衡获取提供方实例
     * //Ribbon+RestTemplate+Hystrix
     * //1.引入Hystrix依赖
     * //2.@HystrixCommand
     * //3.增加fallbackMethod
     * //main开启Hystrix装配 @EnableHystrix
     * @return
     */
    @RequestMapping(value = "/order3",method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "orderByRibbonFallback")
    public String orderByRibbon(){
        //模拟当前用户
        Integer id = 2;

        //查询用户微服务，获取用户具体信息
        //使用RestTemplate远程调用户用户微服务
        //通过Ribbon选择一个最合适的服务（默认轮询）
       ServiceInstance ins = loadBalancerClient.choose("microservices-user");
        User user = restTemplate.getForObject("http://"+ ins.getHost() +":"+ ins.getPort() +"/user/"+id, User.class);
        System.out.println(user + "，"+(ins.getPort()+"") +"==正在购票...");
        return "购票成功";
    }

    public String orderByRibbonFallback(){
        return "服务不可用，执行熔断器方案";
    }

    /**
     * 使用Ribbon负载均衡获取提供方实例(简化版)@LoadBalanced
     * Tips:LoadBalancerClient不能和@LoadBalanced同时使用，否则会报No instances available for XXX
     * @return
     */
    @RequestMapping(value = "/order4",method = RequestMethod.GET)
    public String orderByAutowireLoadBanlance(){
        //模拟当前用户
        Integer id = 2;

        //查询用户微服务，获取用户具体信息
        //使用RestTemplate远程调用户用户微服务
        User user = restTemplate.getForObject("http://microservices-user/user/"+id, User.class);
        System.out.println(user + "==正在购票...");
        return "购票成功";
    }


    /**
     * 使用OpenFeign做服务调用+负载均衡（底层实现了Ribbon+RestTemplate）
     * Tips:LoadBalancerClient不能和@LoadBalanced同时使用，否则会报No instances available for XXX
     * @return
     */
    @RequestMapping(value = "/order5",method = RequestMethod.GET)
    public String orderByOpenFeign(){
        log.info("进入了movie的orderByOpenFeign方法");
        //模拟当前用户
        Integer id = 2;

        //查询用户微服务，获取用户具体信息
        //使用RestTemplate远程调用户用户微服务
        User user = userController.findById(id);
        System.out.println(user + "==正在购票...");
        return "购票成功";
    }
}
