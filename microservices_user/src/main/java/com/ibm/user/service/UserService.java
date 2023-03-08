package com.ibm.user.service;

import com.ibm.user.dao.UserDao;
import com.ibm.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 查询所有用户
     */
    public List<User> findAll(){
        return userDao.findAll();
    }

    /**
     * 根据id查询用户
     */
    public User findById(Integer id){
        return userDao.findOne(id);

    }

    /**
     * 添加用户
     */
    public void add(User user){
        userDao.save(user);
    }

    /**
     * 修改用户
     */
    public void update(User user){ // user对象必须有数据库存在的id值
        userDao.save(user);
    }

    /**
     * 根据id删除用户
     */
    public void deleteById(Integer id){ userDao.delete(id);
    }

}
