package com.ibm.user.controller;

import com.ibm.user.pojo.User;
import com.ibm.user.service.UserService;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static Log log = LogFactory.getLog(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public List<User> findAll(){

        return userService.findAll();

    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public String add(@RequestBody User user){

        userService.add(user);
        return "添加成功";
    }
    /**
     * 根据id查询用户
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public User findById(@PathVariable Integer id){
        log.info("进入User的findById方法");
        return userService.findById(id);
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public String update(@RequestBody User user,@PathVariable Integer id){
        //设置id
        user.setId(id);
        userService.update(user);
        return "修改成功";
    }

    /**
     * 根据id删除用户
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public String deleteById(@PathVariable Integer id){
        userService.deleteById(id);
        return "删除成功";
    }
}
