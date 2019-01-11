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

@Slf4j
@Service
public class UserService {
    private final UserMapper userMapper;
    private final JwtService jwtService;

    /**
     * UserMapper 생성자 의존성 주입
     *
     * @param userMapper
     * @param jwtService
     */
    public UserService(final UserMapper userMapper, final JwtService jwtService) {
        this.userMapper = userMapper;
        this.jwtService = jwtService;
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

            final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.getUserIdx()));

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER, tokenDto);
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


    @Transactional
    public DefaultRes setFcmToken(final User user) {
        try {
            Integer userIdx = userMapper.findUserIdxByToken(user.getId());
            if (userIdx == null) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
            } else {
                userMapper.updateToken(user.getFcmToken(), userIdx);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);

        }
    }

    public boolean isRealUser(final Integer token) {
        if (userMapper.findUserByToken(token) == null) {
            return false;
        } else {
            return true;

        }
    }
}
