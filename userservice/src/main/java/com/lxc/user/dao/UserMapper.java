package com.lxc.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxc.common.entity.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

import java.util.Date;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/20
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    Cursor<User> selectBirthdayUser(@Param("date") Date date);

    int findAccount(String account);

    int findUserName(String username);

    User findUser(User user);

    User getUserById(Integer uid);

    User getMainInfo(Integer uid);

    int insertUser(User user);

    int updateUser(User user);

    String getSaltByAccount(String account);

    int dailyAddCoin();

    //投币
    int payCoin(Integer uid);

    //消费s币
    int payScoin(@Param("uid") Integer uid,@Param("num") Integer num);

    //给用户增加硬币，成长值
    int addCoinExp(@Param("uid")Integer uid,@Param("coin")Integer coin,@Param("scoin")Integer scoin,@Param("growth")Integer growth);

}
