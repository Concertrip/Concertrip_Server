package com.concertrip.server.mapper;

import com.concertrip.server.dto.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Insert("INSERT INTO user(id, password, name) VALUES(#{user.id}, #{user.password}, #{user.name})")
    void save(@Param("user") final User user);

}
