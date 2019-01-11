package com.concertrip.server.api;

import com.concertrip.server.domain.Genre;
import com.concertrip.server.service.GenreService;
import com.concertrip.server.service.JwtService;
import com.concertrip.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/genre")
public class GenreController {
    private final GenreService genreService;
    private final JwtService jwtService;

    public GenreController(final GenreService genreService, final JwtService jwtService) {
        this.genreService = genreService;
        this.jwtService = jwtService;
    }

    @PostMapping("")
    public ResponseEntity addGenre(@RequestBody final Genre genre) {
        return new ResponseEntity<>(genreService.save(genre), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity findAll() {
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    @Auth
    @GetMapping("/detail")
    public ResponseEntity findById(
            @RequestHeader (value = "Authorization") final String token,
            @RequestParam(value = "id") final String _id) {
        JwtService.Token decodedToken = jwtService.decode(token);
        return new ResponseEntity<>(genreService.findById(_id, decodedToken.getUser_idx()), HttpStatus.OK);
    }
}
