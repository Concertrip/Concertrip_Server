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
    private int ticketIdx;
    private int serialNum;
    private String seat;
    private int barcodeNum;
    private String title;
    private Date date;
    private String location;
    private String eventId;
    private int userIdx;
}
