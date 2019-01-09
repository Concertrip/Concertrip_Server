package com.concertrip.server.api;

import com.concertrip.server.dto.Notice;
import com.concertrip.server.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.concertrip.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created by HYEON on 2019-01-09.
 */

@Slf4j
@RestController
@RequestMapping("api/notice")
public class NoticeController {
    private NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }


    @PostMapping("")
    public ResponseEntity saveNotice(@RequestBody final Notice notice) {
        try{
            return new ResponseEntity<>(noticeService.save(notice), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
