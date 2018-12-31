package com.concertrip.server.api;

import com.concertrip.server.service.ArtistsService;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by HYEON on 2018-12-26.
 */

@Slf4j
@RestController
@RequestMapping("api/artist")
public class ArtistsController {
    private final ArtistsService artistsService;

    public ArtistsController(ArtistsService artistsService) {
        this.artistsService = artistsService;
    }

    //TODO: 다가오는 이벤트 리스트 추가
    @GetMapping("detail")
    public ResponseEntity getArtistsById(@RequestParam(value = "id", defaultValue = "") final String _id) {
        if (_id.equals("")) return new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
        return new ResponseEntity<>(artistsService.findArtistById(_id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity addArtists(@RequestBody final Artists artists) {
        return new ResponseEntity<>(artistsService.insertArtist(artists), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity updateArtists(@RequestBody final Artists artists) {
        artistsService.updateArtist(artists);
        return new ResponseEntity<>(artistsService.updateArtist(artists), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity deleteArtists(@RequestParam(value = "id", defaultValue = "") final String id) {
        if(id.equals("")) return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
        return new ResponseEntity<>(artistsService.deleteArtist(id), HttpStatus.OK);
    }

}
