package com.concertrip.server.service;

import com.concertrip.server.dal.ArtistsDAL;
import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.mapper.ArtistsSubscribeMapper;
import com.concertrip.server.model.ArtistDetailReq;
import com.concertrip.server.model.ArtistsReq;
import com.concertrip.server.model.ArtistsSubscribeReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by HYEON on 2018-12-26.
 */

@Slf4j
@Service
public class ArtistsService {
    private final ArtistsDAL artistsDAL;
    private final ArtistsSubscribeMapper artistsSubscribeMapper;
    private final ArtistsRepository artistsRepository;

    public ArtistsService(ArtistsDAL artistsDAL, ArtistsRepository artistsRepository,ArtistsSubscribeMapper artistsSubscribeMapper) {
        this.artistsDAL = artistsDAL;
        this.artistsSubscribeMapper = artistsSubscribeMapper;
        this.artistsRepository = artistsRepository;
    }

    /**
     * 전체 아티스트 정보 가져오기
     *
     * @return DefaultRes
     */
    public DefaultRes selectArtistAll() {
        try {
            List<Artists> artistsList = artistsDAL.selectArtistAll();
            if (artistsList.size() == 0) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS);
            } else {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, artistsList);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }

    }

    /**
     * 아티스트 이름으로 조회
     *
     * @param _id
     * @return
     */

    public DefaultRes findArtistById(String _id) {
        try {
            Artists artists = artistsDAL.findArtists(_id);
            if (artists == null) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS);
            } else {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, artists);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 아티스트 상세 페이지 조회
     */
    @Transactional
    public DefaultRes findEventAll(String _id) {
        try {
            ArtistDetailReq artistDetailReq = artistsRepository.findArtist(_id);
            log.info("지금 subscribe가 안되서 데베 오류로 뜨는거 같움");
            log.info(artistDetailReq.toString());
            String[] memberImg = new String[artistDetailReq.getMember().length];
            for(int i = 0; i < memberImg.length; i++){
                Artists artists = artistsRepository.findOneByName(artistDetailReq.getMember()[i]);
                memberImg[i] = artists.getProfileImg();
            }
            artistDetailReq.setMemberImg(memberImg);

            log.info(artistDetailReq.toString());
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, artistDetailReq);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 아티스트 리스트 조회
     */
    public DefaultRes findArtistInfo (String name) {
        try{
            List<ArtistsReq> artistsReq = artistsRepository.findByName(name);
            log.info("aaaaaaaaaaaaaaaa");
            log.info(artistsReq.toString());
            if (artistsReq.equals("")) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS);
            else return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, artistsReq);
        } catch (Exception e) {
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

    /**
     * 아티스트 구독 및 취소
     *
     * @param artistId
     * @param token    사용자 판별 토큰
     * @return
     */
    public DefaultRes subscribe(final String artistId, final String token) {
        try {
            ArtistsSubscribeReq asReq = artistsSubscribeMapper.isSubscribe(token, artistId);
            if (asReq == null) {
                artistsSubscribeMapper.subscribe(artistId, token);
            } else {
                artistsSubscribeMapper.unSubscribe(token, artistId);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.SUBSCRIBE_EVENT);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

}

