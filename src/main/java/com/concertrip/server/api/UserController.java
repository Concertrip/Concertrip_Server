package com.concertrip.server.api;

import com.concertrip.server.Service.UserService;
import com.concertrip.server.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.concertrip.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/guest")
    public ResponseEntity signUpGuest() {
        try {
            String uuid = UUID.randomUUID().toString();
            User guest = new User();
            guest.setId(uuid);
            guest.setPassword(uuid);
            guest.setName("guest");
            return new ResponseEntity<>(userService.save(guest), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
