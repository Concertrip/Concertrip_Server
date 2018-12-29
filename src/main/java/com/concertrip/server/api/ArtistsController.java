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

//    @GetMapping("")
//    public ResponseEntity getAllArtists() {
//        return new ResponseEntity<>(artistsService.selectArtistAll(), HttpStatus.OK);
//    }

    @GetMapping("")
    public ResponseEntity getArtistInfo(@RequestParam(value = "name", defaultValue = "")final String name) {
        if (name.equals("")) return new ResponseEntity(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
        return new ResponseEntity(artistsService.findArtistInfo(name), HttpStatus.OK);
    }

    @GetMapping("detail")
    public ResponseEntity getArtistsById(@RequestParam(value = "_id", defaultValue = "") final String _id) {
        if (_id.equals("")) return new ResponseEntity(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
        return new ResponseEntity(artistsService.findEventAll(_id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity addArtists(@RequestBody final Artists artists) {
        return new ResponseEntity(artistsService.insertArtist(artists), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity updateArtists(@RequestBody final Artists artists) {
        artistsService.updateArtist(artists);
        return new ResponseEntity(artistsService.updateArtist(artists), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity deleteArtists(@RequestParam(value = "_id", defaultValue = "") final String _id) {
        if(_id.equals("")) return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
        return new ResponseEntity<>(artistsService.deleteArtist(_id), HttpStatus.OK);
    }

    @GetMapping("/{artistId}/bell")
    public ResponseEntity subscribeEvents(
            @RequestHeader (value = "Autorization") final String token,
            @PathVariable("artistId") final String artistId) {
        try {
            if (artistId.isEmpty()) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
            }
            if (token.isEmpty()) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER), HttpStatus.OK);
            }
            return new ResponseEntity<>(artistsService.subscribe(artistId, token), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }
}
