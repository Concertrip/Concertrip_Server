package com.concertrip.server.service;

import com.concertrip.server.dal.ArtistsDAL;
import com.concertrip.server.dal.EventsDAL;
import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.domain.Events;
import com.concertrip.server.dto.Precaution;
import com.concertrip.server.mapper.PrecautionMapper;
import com.concertrip.server.model.CommonListReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.EventsDetailReq;
import com.concertrip.server.model.EventsSubscribeReq;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.LinkedList;
import java.util.List;

/**
 * Created hyunjk on 2018-12-25.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@Service
public class EventsService {
    private final EventsDAL eventsDAL;
    private final ArtistsDAL artistsDAL;
    private final ArtistsRepository artistsRepository;
    private final EventsRepository eventsRepository;
    private final PrecautionMapper precautionMapper;

    public EventsService(EventsDAL eventsDAL, ArtistsDAL artistsDAL, ArtistsRepository artistsRepository, EventsRepository eventsRepository, PrecautionMapper precautionMapper) {
        this.eventsDAL = eventsDAL;
        this.artistsDAL = artistsDAL;
        this.artistsRepository = artistsRepository;
        this.eventsRepository = eventsRepository;
        this.precautionMapper = precautionMapper;
    }


    /**
     * 전체 이벤트 정보 가져오기
     *
     * @return DefaultRes
     */
    public DefaultRes selectAll() {
        try {
            List<Events> eventsList = eventsDAL.selectAll();
            if (eventsList.size() == 0) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_EVENT);
            } else {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_EVENTS, eventsList);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes findEventsById(String _id) {
        try {
            EventsDetailReq eventsDetail = eventsRepository.findEvent(_id);
            String[] member = eventsRepository.getMember(_id).getMember();
            List<CommonListReq> memberList = new LinkedList<>();

            for (int i = 0; i < member.length; i++) {
                memberList.add(artistsRepository.getArtistInfo(member[i]));
            }
            eventsDetail.setMemberList(memberList);
            int[] precaution = eventsRepository.getPrecaution(_id).getPrecaution();

            List<Precaution> precautions = new LinkedList<>();

            for(int code : precaution) {

                precautions.add(precautionMapper.getPrecaution(code));
            }

            eventsDetail.setPrecautionList(precautions);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_EVENTS, eventsDetail);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


    /**
     * 이벤트 추가
     *
     * @param events 이벤트 데이터
     * @return DafaultRes
     */
    public DefaultRes insert(Events events) {
        try {
            eventsDAL.insert(events);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_EVENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 이벤트 내용 수정
     *
     * @param events 수정된 이벤트 객체
     * @return
     */
    public DefaultRes update(Events events) {
        try {
            eventsDAL.update(events);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_EVENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 이벤트 삭제
     *
     * @param _id 이벤트 객체의 id
     * @return
     */
    public DefaultRes delete(String _id) {
        try {
            eventsDAL.delete(_id);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_EVENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
