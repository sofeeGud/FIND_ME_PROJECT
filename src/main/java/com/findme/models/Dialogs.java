package com.findme.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class Dialogs {
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private Long messageId;
    private String messageText;
    private Long messageUserFromId;
    private Long messageUserToId;
    private Date messageDateSent;
    private Date messageDateRead;
    private Integer newMessagesCount;
}

