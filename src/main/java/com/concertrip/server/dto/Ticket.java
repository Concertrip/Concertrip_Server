package com.concertrip.server.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by HYEON on 2018-12-27.
 */

@NotNull
@Data
public class Ticket {
    private int _id;
    private String name;
    private String location;
    private Date date;
    private String seat;
    private int userIdx;
    private String eventId;
}
