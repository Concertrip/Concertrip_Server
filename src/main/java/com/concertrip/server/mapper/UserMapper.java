package com.concertrip.server.mapper;

import com.concertrip.server.dto.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(id, password, name) VALUES(#{user.id}, #{user.password}, #{user.name})")
    void save(@Param("user") final User user);

    @Select("SELECT userIdx FROM user WHERE id = #{userId}")
    Integer findUserIdxByToken(@Param("userId") final String userId);

    @Select("SELECT * FROM user WHERE id=#{userId} LIMIT 1")
    User findUserById(@Param("userId") final String userId);


}
