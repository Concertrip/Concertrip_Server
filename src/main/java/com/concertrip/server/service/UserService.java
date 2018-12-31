package com.concertrip.server.service;


import com.concertrip.server.dto.User;
import com.concertrip.server.mapper.UserMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;

    /**
     * UserMapper 생성자 의존성 주입
     *
     * @param userMapper
     */
    public UserService(final UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 회원 가입
     *
     * @param user 회원 데이터
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes save(final User user) {
        try {
            //TODO: need refactoring
            userMapper.save(user);
            User res = userMapper.findUserById(user.getId());
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER, res.getUserIdx() + "");
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public Integer getUserIdxByToken(final String token) {
        return userMapper.findUserIdxByToken(token);
    }
}
