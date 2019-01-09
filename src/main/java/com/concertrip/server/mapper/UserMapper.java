package com.concertrip.server.mapper;

import com.concertrip.server.dto.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(id, password, name, fcmToken) VALUES(#{user.id}, #{user.password}, #{user.name}, #{user.fcmToken})")
    void save(@Param("user") final User user);

    @Select("SELECT * FROM user WHERE id = #{id} AND password = #{pw}")
    User findByNameAndPassword(@Param("id") final String id, @Param("pw") final String pw);

    @Select("SELECT * FROM user WHERE userIdx = #{idx}")
    User findByIdx(@Param("idx") final Integer idx);

    @Select("SELECT userIdx FROM user WHERE id = #{userId}")
    Integer findUserIdxByToken(@Param("userId") final String userId);

    @Select("SELECT * FROM user WHERE id = #{userId} LIMIT 1")
    User findUserById(@Param("userId") final String userId);

    @Update("UPDATE user SET fcmToken = #{fcmToken} WHERE userIdx = #{userIdx}")
    void updateToken(@Param("fcmToken") final String fcmToken, @Param("userIdx") final Integer userIdx);

    @Select("SELECT * FROM user WHERE userIdx = #{token}")
    User findUserByToken(@Param("token") final Integer token);

    @Select("SELECT id FROM user WHERE userIdx = #{userIdx}")
    String findUserToken(@Param("userIdx") final Integer userIdx);



}
