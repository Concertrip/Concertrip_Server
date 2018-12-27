package com.concertrip.server.api;

import com.concertrip.server.Service.ArtistsService;
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
@RequestMapping("artists")
public class ArtistsController {
    private final ArtistsService artistsService;

    public ArtistsController(ArtistsService artistsService) {
        this.artistsService = artistsService;
    }

    @GetMapping("")
    public ResponseEntity getAllArtists() {
        return new ResponseEntity<>(artistsService.selectArtistAll(), HttpStatus.OK);
    }

    @GetMapping("/{artistsId}")
    public ResponseEntity getArtistsById(@PathVariable(value = "artistsId") final String _id) {
        if (_id.equals("")) return new ResponseEntity(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
        return new ResponseEntity(artistsService.findArtistById(_id), HttpStatus.OK);
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
    public ResponseEntity deleteArtists(@RequestParam(value = "id", defaultValue = "") final String _id) {
        if(_id.equals("")) return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
        return new ResponseEntity<>(artistsService.deleteArtist(_id), HttpStatus.OK);
    }
}
