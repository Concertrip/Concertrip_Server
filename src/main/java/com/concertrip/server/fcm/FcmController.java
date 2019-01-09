package com.concertrip.server.fcm;

import com.concertrip.server.dto.Notice;
import com.concertrip.server.model.FcmReq;
import com.concertrip.server.service.NoticeService;
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
    private NoticeService noticeService;

    public FcmController(FcmService fcmService, NoticeService noticeService) {
        this.fcmService = fcmService;
        this.noticeService = noticeService;
    }

    @PostMapping("")
    public ResponseEntity send(@RequestBody final FcmReq fcmReq) {
        try {
            return new ResponseEntity<>(fcmService.send2(fcmReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
