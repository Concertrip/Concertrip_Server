package com.concertrip.server.model;

import com.concertrip.server.dto.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by HYEON on 2019-01-06.
 */

@Data
@AllArgsConstructor
public class TicketListReq {
    private List<Ticket> ticketcomming ;
    private List<Ticket> ticketcomplete ;
}
