package com.findme.validation;

import com.findme.models.RelationshipStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Builder
@Getter
@ToString
public class RelationshipValidatorParams {

    private String userFromId;
    private String userToId;
    private RelationshipStatus oldStatus;
    private RelationshipStatus newStatus;
    private Date relationshipDateModified;
    private int friendsCnt;
    private int outgoingReqCnt;

}
