package com.concertrip.server.utils.auth;

import java.lang.annotation.*;

//메소드에 적용
@Target(ElementType.METHOD)
//런타임시까지 참조 가능
@Retention(RetentionPolicy.RUNTIME)
//Java Doc 에도 표시
@Documented
//상속 가능
@Inherited

//인증이 필요한 거라고 알려줌
public @interface Auth {
}
