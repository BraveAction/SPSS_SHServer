package com.spss.smarthome.model;

import lombok.Data;

/**
 * Created by gxy on 18/06/15
 * 用于记录用户会话
 */
@Data
public class Session {
    Long date;
    Topic topic;
}
