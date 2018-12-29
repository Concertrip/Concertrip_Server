package com.concertrip.server.mapper;

import com.concertrip.server.dto.Precaution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created hyunjk on 2018-12-29.
 * Github : https://github.com/hyunjkluz
 */
@Mapper
public interface PrecautionMapper {
    @Select("SELECT * FROM precaution WHERE code = #{code}")
    Precaution getPrecaution(@Param("code") final int code);
}
