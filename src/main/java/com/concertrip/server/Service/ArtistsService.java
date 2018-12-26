package com.concertrip.server.Service;

import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by HYEON on 2018-12-26.
 */

@Slf4j
@Service
public class ArtistsService {

    private final ArtistsRepository artistsRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 생성자 의존성 주입
     * @param artistsRepository
     * @param mongoTemplate
     */
    public ArtistsService(ArtistsRepository artistsRepository, MongoTemplate mongoTemplate) {
        this.artistsRepository = artistsRepository;
        this.mongoTemplate = mongoTemplate;
    }
    /**
     * 전체 아티스트 정보 가져오기
     *
     * @return DefaultRes
     */
    public DefaultRes selectArtistAll() {
        try {
            List<Artists> artistsList = artistsRepository.findAll();
            if (artistsList.size() == 0) {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_ARTISTS);
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
     * 아티스트 추가
     *
     * @param artists 아티스트 데이터
     * @return DefaultRes
     */

    public DefaultRes insertArtist(Artists artists) {
        try {
            mongoTemplate.insert(artists, "artists");
            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_ARTISTS);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
    /**
     * 아티스트 내용 수정
     * @param artists 수정된 아티스트 객체
     * @return
     */
    public DefaultRes updateArtist(Artists artists) {
        try {
            mongoTemplate.save(artists, "artists");
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
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(_id));
            mongoTemplate.remove(query, "artists");

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_ARTISTS);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

}
