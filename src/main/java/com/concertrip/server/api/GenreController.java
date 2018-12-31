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

    @GetMapping("/all")
    public ResponseEntity findAll() {
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity findById(
            @RequestHeader (value = "Authorization") final String token,
            @RequestParam(value = "id") final String _id) {
        return new ResponseEntity<>(genreService.findById(_id, token), HttpStatus.OK);
    }

    @PostMapping("/sub")
    public ResponseEntity subscribe(
            @RequestHeader (value = "Authorization") final String token,
            @RequestBody final SubscribeReq subscribeReq
    ) {
        return new ResponseEntity<>(genreService.subscribe(subscribeReq.get_id(), token), HttpStatus.OK);
    }

}
