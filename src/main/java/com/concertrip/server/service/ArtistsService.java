package com.concertrip.server.service;

import com.concertrip.server.dal.ArtistsDAL;
import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.model.*;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HYEON on 2018-12-26.
 */

@Slf4j
@Service
public class ArtistsService {
    private final ArtistsDAL artistsDAL;
    private final ArtistsRepository artistsRepository;
    private final SearchService searchService;
    private final SubscribeService subscribeService;


    public ArtistsService(ArtistsDAL artistsDAL, ArtistsRepository artistsRepository, SearchService searchService, SubscribeService subscribeService) {
        this.artistsDAL = artistsDAL;
        this.artistsRepository = artistsRepository;
        this.searchService = searchService;
        this.subscribeService = subscribeService;
    }

    /**
     * 아티스트 상세 페이지 조회
     * 
     */
    @Transactional
    public DefaultRes findArtistById(String id, final Integer token) {
        try {
            if (ObjectUtils.isEmpty(token)) {
                return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
            }
            ArtistDetailReq artistDetailReq = artistsRepository.findArtistDetailById(id);
            if(ObjectUtils.isEmpty(artistDetailReq)) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS);
            }
            artistDetailReq.setSubscribe(subscribeService.isSubscribe(token, "artist", id));
            artistDetailReq.setSubscribeNum(subscribeService.subscribeNum("artist", id));

            Artists artists = artistsRepository.findArtistsBy_id(id);
            String[] members = artists.getMember();
            List<CommonListReq> memberList = new ArrayList<>();

            for (String member:members) {
                memberList.add(artistsRepository.findArtistsByName(member));
            }
            artistDetailReq.setMemberList(memberList);

            String name = artists.getName();
            artistDetailReq.setEventsList(searchService.searchEvent(token, name));

            searchService.setGroup(artistDetailReq.getEventsList(), "event");

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, artistDetailReq);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 아티스트 추가
     *
     * @param artists 아티스트 데이터
     * @return DefaultRes
     */

    public DefaultRes insertArtist(Artists artists) {
        try {
            Artists artists1 = artistsDAL.findArtistsByName(artists.getName());
            if (artists1 == null) {
                artistsDAL.insertArtist(artists);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_ARTISTS);
            } else {
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.ALREADY_ARTISTS);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 아티스트 내용 수정
     *
     * @param artists 수정된 아티스트 객체
     * @return
     */
    public DefaultRes updateArtist(Artists artists) {
        try {
            artistsDAL.updateArtist(artists);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_ARTISTS);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 아티스트 삭제
     *
     * @param _id 이벤트 객체 id
     * @return
     */
    public DefaultRes deleteArtist(String _id) {

        try {
            Artists artists = artistsDAL.findArtists(_id);
            if (artists == null) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS);
            } else {
                artistsDAL.deleteArtist(_id);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_ARTISTS);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}

