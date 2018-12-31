package com.concertrip.server.utils;

public class ResponseMessage {
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";

    public static final String READ_EVENTS = "이벤트 조회 성공";
    public static final String NOT_FOUND_EVENT = "이벤트를 찾을 수 없습니다";
    public static final String CREATED_EVENT = "이벤트 추가 성공 성공";
    public static final String UPDATE_EVENT = "이벤트 정보 수정 성공";
    public static final String DELETE_EVENT = "이벤트 삭제 성공";

    public static final String READ_ARTISTS = "아티스트 조회 성공";
    public static final String NOT_FOUND_ARTISTS = "아티스트를 찾을 수 없습니다.";
    public static final String CREATED_ARTISTS = "아티스트 추가 성공";
    public static final String ALREADY_ARTISTS = "이미 등록된 아티스트가 있습니다.";
    public static final String UPDATE_ARTISTS = "아티스트 정보 수정 성공";
    public static final String DELETE_ARTISTS = "아티스트 삭제 성공";

    public static final String READ_GENRE= "장르 조회 성공";
    public static final String NOT_FOUND_GENRE = "장르를 찾을 수 없습니다";
    public static final String CREATED_GENRE = "장르 추가 성공";
    public static final String ALREADY_GENRE = "이미 등록된 장르가 있습니다";
    public static final String UPDATE_GENRE = "장르 정보 수정 성공";
    public static final String DELETE_GENRE = "장르 삭제 성공";

    public static final String READ_TICKETS = "티켓 조회 성공";
    public static final String NOT_FOUND_TICKETS = "티켓을 찾을 수 없습니다.";
    public static final String CREATED_TICKETS = "티켓 추가 성공";
    public static final String UPDATE_TICKETS = "티켓 정보 수정 성공";
    public static final String DELETE_TICKETS = "티켓 삭제 성공";

    public static final String SEARCH_SUCCESS = "검색 성공";
    public static final String NOT_FOUND_TAG = "검색 태그를 찾을 수 없습니다.";

    public static final String SUBSCRIBE = "구독 완료";
    public static final String UNSUBSCRIBE = "구독 취소 완료";

    public static final String READ_TOTAL_CALENDAR = "전체 캘린더 검색 성공";
    public static final String READ_EVENT_CALENDAR = "이벤트 캘린더 검색 성공";
    public static final String READ_ARTIST_CALENDAR = "아티스트 캘린더 검색 성공";

    public static final String NO_CALENDAR = "캘린더 정보 없음";

    public static final String NOT_FOUND_TOTAL_CALENDAR = "전체 캘린더를 찾을 수 없습니다.";
    public static final String NOT_FOUND_EVENT_CALENDAR = "이벤트 캘린더를 찾을 수 없습니다.";
    public static final String NOT_FOUND_ARTIST_CALENDAR = "아티스크 캘린더를 찾을 수 없습니다.";

    public static final String DB_ERROR = "데이터베이스 에러";
}
