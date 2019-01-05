package com.concertrip.server.mapper;

import com.concertrip.server.dto.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(id, password, name, fcmToken) VALUES(#{user.id}, #{user.password}, #{user.name}, #{user.fcmToken)")
    void save(@Param("user") final User user);

    @Select("SELECT userIdx FROM user WHERE id = #{userId}")
    Integer findUserIdxByToken(@Param("userId") final String userId);

    @Select("SELECT * FROM user WHERE id = #{userId} LIMIT 1")
    User findUserById(@Param("userId") final String userId);

    @Update("UPDATE user SET fcmToken = #{fcmToken} WHERE userIdx = #{userIdx}")
    void updateToken(@Param("fcmToken") final String fcmToken, @Param("userIdx") final Integer userIdx);

}
