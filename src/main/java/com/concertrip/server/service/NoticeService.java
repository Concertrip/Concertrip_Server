package com.concertrip.server.service;

import com.concertrip.server.dto.Notice;
import com.concertrip.server.fcm.FcmService;
import com.concertrip.server.mapper.NoticeMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HYEON on 2019-01-09.
 */

@Slf4j
@Service
public class NoticeService {
    private NoticeMapper noticeMapper;

    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    @Transactional
    public DefaultRes save(final Notice notice) {
        try{
            noticeMapper.save(notice);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_NOTICE, notice);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
