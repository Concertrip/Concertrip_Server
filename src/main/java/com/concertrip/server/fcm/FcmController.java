package com.concertrip.server.fcm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by HYEON on 2019-01-05.
 */

@Slf4j
@RestController
@RequestMapping("api/fcm")
public class FcmController {
    private FcmService fcmService;

    public FcmController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("")
    public ResponseEntity send(@RequestParam(value = "type", defaultValue = "")final String type,
                               @RequestParam(value = "objIdx", defaultValue = "")final String objIdx) {
        return new ResponseEntity<>(fcmService.send(type,objIdx), HttpStatus.OK);
    }
}
