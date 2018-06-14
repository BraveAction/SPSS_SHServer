package com.spss.smarthome.dao;

import com.spss.smarthome.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户Dao接口
 */
@Repository
@Mapper
public interface UserDao {
    @Select("SELECT * FROM user WHERE USERNAME = #{userName} OR PHONE = #{phone}")
    User findByName(String userName);

    @Select("SELECT * FROM user WHERE USERNAME = #{userName} OR PHONE = #{phone}")
    User findUser(User user);

    @Select("SELECT * FROM user WHERE ID = #{id}")
    User findById(int id);

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Insert("INSERT INTO user(userName, password, phone) VALUES(#{userName}, #{password},#{phone})")
    Long insert(User user);

    @Update("UPDATE user SET password = #{password} WHERE phone = #{phone}")
    Long updatePassword(User user);
}
