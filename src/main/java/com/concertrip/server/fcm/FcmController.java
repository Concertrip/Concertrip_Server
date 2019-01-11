package com.concertrip.server.fcm;

import com.concertrip.server.mapper.NoticeMapper;
import com.concertrip.server.model.FcmReq;
import com.concertrip.server.service.JwtService;
import com.concertrip.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
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
    private JwtService jwtService;

    public FcmController(FcmService fcmService, NoticeMapper noticeMapper, JwtService jwtService) {
        this.fcmService = fcmService;
        this.noticeMapper = noticeMapper;
        this.jwtService = jwtService;
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

    @Auth
    @GetMapping("/list")
    public ResponseEntity list(@RequestHeader(value = "Authorization") final String token) {
        try {
            JwtService.Token decodedToken = jwtService.decode(token);
            return new ResponseEntity<>(noticeMapper.findByUserIdx(decodedToken.getUser_idx()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
