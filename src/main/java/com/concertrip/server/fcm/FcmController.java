package com.concertrip.server.fcm;

import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.mapper.NoticeMapper;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.model.FcmReq;
import com.concertrip.server.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.concertrip.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created by HYEON on 2019-01-05.
 */

@Slf4j
@RestController
@RequestMapping("api/fcm")
public class FcmController {
    private FcmService fcmService;
    private NoticeMapper noticeMapper;

    public FcmController(FcmService fcmService, NoticeMapper noticeMapper) {
        this.fcmService = fcmService;
        this.noticeMapper = noticeMapper;
    }

    @PostMapping("")
    public ResponseEntity send(@RequestBody final FcmReq fcmReq) {
        try {
            return new ResponseEntity<>(fcmService.sendsave(fcmReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity list(@RequestHeader(value = "Authorization") final int token) {
        try {
            return new ResponseEntity<>(noticeMapper.findByUserIdx(token), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
