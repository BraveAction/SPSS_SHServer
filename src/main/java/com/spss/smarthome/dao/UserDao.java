package com.spss.smarthome.dao;

import com.spss.smarthome.controller.form.InitPasswordForm;
import com.spss.smarthome.controller.form.UserAtHouse;
import com.spss.smarthome.controller.form.UserSignUpForm;
import com.spss.smarthome.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户Dao接口
 */
@Repository
@Mapper
public interface
UserDao {
    @Select("SELECT * FROM user WHERE PHONE = #{phone}")
    User findByPhone(String phone);

    @Select("SELECT * FROM user WHERE USERNAME = #{userName} ")
    User findByName(String userName);

    @Select("SELECT * FROM user WHERE USERNAME = #{userName} OR PHONE = #{phone}")
    User findUser(UserSignUpForm userSignUpForm);

    @Select("SELECT * FROM user WHERE ID = #{id}")
    User findById(int id);

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Insert("INSERT INTO user(userName, password, phone) VALUES(#{userName}, #{password},#{phone})")
    Long insert(UserSignUpForm user);

    @Update("UPDATE user SET password = #{password} WHERE phone = #{phone}")
    Long updatePassword(InitPasswordForm initPasswordForm);

    @Update("UPDATE user SET password=#{newPwd} WHERE id=#{userId}")
    Long changePwd(@Param("userId") String userId, @Param("newPwd") String newPwd);

    @Update("UPDATE user SET phone=#{newPhone} WHERE id=#{userId}")
    Long changePhone(@Param("userId") String userId, @Param("newPhone") String newPhone);

    @Update("UPDATE user SET userName=#{newName} WHERE id=#{userId}")
    Long changeUserName(@Param("userId") String userId, @Param("newName") String newName);

    @Select(" SELECT u.id, u.userName, h.rcode, CASE h.rcode WHEN 0 THEN '管理员' WHEN 1 THEN '主人' ELSE '客人' END AS rName FROM house h, user u WHERE h.gId IN (SELECT id FROM gateway WHERE mac = #{mac}) AND h.userId != #{userId} AND h.userId == u.id")
    List<UserAtHouse> findUserByGatewayMac(@Param("userId") Long userId, @Param("mac") String mac);

}
