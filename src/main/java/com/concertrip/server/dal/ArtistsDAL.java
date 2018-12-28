package com.concertrip.server.dal;

import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.domain.Events;
import com.concertrip.server.model.ArtistsReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.EventsReq;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by HYEON on 2018-12-26.
 */

@Service
public class ArtistsDAL {
    private final ArtistsRepository artistsRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 생성자 의존성 주입
     * @param artistsRepository
     * @param mongoTemplate
     */
    public ArtistsDAL(ArtistsRepository artistsRepository, MongoTemplate mongoTemplate) {
        this.artistsRepository = artistsRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 전체 아티스트 정보 가져오기
     *
     */
    public List<Artists> selectArtistAll() {
        return artistsRepository.findAll();
    }
    // _id 로 가져오기
    public Artists findArtists(String _id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(_id));
        Artists artists = mongoTemplate.findOne(query, Artists.class);
        return artists;
    }

    //아티스트 name으로 가져오기
    public Artists findArtistsByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        Artists artists = mongoTemplate.findOne(query, Artists.class);
        return artists;
    }

    public String getArtistsImgByName(String name) {
        Artists artists = findArtistsByName(name);
        return artists.getProfileImg();
    }

    public ArtistsReq findArtistsForCal(String _id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(_id));
        ArtistsReq artists = mongoTemplate.findOne(query, ArtistsReq.class);

        return artists;
    }

    /**
     * 아티스트 추가
     *
     * @param artists 아티스트 데이터
     */
    public void insertArtist(Artists artists) {
        mongoTemplate.insert(artists, "artists");
    }

    /**
     * 아티스트 내용 수정
     * @param artists 수정된 아티스트 객체
     */
    public void updateArtist(Artists artists) {
        mongoTemplate.save(artists, "artists");
    }

    /**
     * 아티스트 삭제
     *
     * @param _id 이벤트 객체 id
     */
    public void deleteArtist(String _id) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(_id));
            mongoTemplate.remove(query, "artists");
    }

}

