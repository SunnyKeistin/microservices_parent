package com.ibm.movie.client;

import com.ibm.movie.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class UserControllerImpl implements UserController {
    @Override
    public User findById(Integer id) {
        System.out.println("openFeign整合Hystrin正在熔断降级");
        return null;
    }
}
