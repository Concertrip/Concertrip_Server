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
    public static final String CREATED_ARTISTS = "아티스트 추가 성공 성공";
    public static final String UPDATE_ARTISTS = "아티스트 정보 수정 성공";
    public static final String DELETE_ARTISTS = "아티스트 삭제 성공";

    public static final String SEARCH_SUCCESS = "검색 성공";
    public static final String NOT_FOUND_TAG = "검색 태그를 찾을 수 없습니다.";

    public static final String DB_ERROR = "데이터베이스 에러";
}
