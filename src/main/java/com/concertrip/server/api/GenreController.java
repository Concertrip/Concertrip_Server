package com.concertrip.server.api;

import com.concertrip.server.domain.Genre;
import com.concertrip.server.model.SubscribeReq;
import com.concertrip.server.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/genre")
public class GenreController {
    private final GenreService genreService;
    public GenreController(GenreService genreService) { this.genreService = genreService; }

    @PostMapping("")
    public ResponseEntity addGenre(@RequestBody final Genre genre) {
        return new ResponseEntity<>(genreService.save(genre), HttpStatus.OK);
    }

    //TODO: 구독 여부
    @GetMapping("/all")
    public ResponseEntity findAll() {
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    //TODO: 토큰 타입 확인, 다가오는 이벤트 리스트 추가
    @GetMapping("/detail")
    public ResponseEntity findById(
            @RequestHeader (value = "Authorization") final Integer token,
            @RequestParam(value = "id") final String _id) {
        return new ResponseEntity<>(genreService.findById(_id, token), HttpStatus.OK);
    }
}
