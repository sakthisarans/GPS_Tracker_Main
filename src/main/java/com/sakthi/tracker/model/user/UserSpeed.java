package com.sakthi.tracker.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Getter@Setter@AllArgsConstructor
public class UserSpeed {
    private Date date;
    private String Speed;
}
