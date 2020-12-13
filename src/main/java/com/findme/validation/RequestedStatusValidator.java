package com.findme.validation;

import com.findme.exceptions.BadRequestException;
import com.findme.models.RelationshipStatus;

import java.util.Arrays;

public class RequestedStatusValidator extends AbstractRelationshipValidator {
    private final RelationshipStatus[] CURRENT_STATUS = {RelationshipStatus.REJECTED, RelationshipStatus.DELETED, RelationshipStatus.CANCELED, RelationshipStatus.FRIENDS};
    private final RelationshipStatus NEW_STATUS = RelationshipStatus.REQUESTED;


    @Override
    protected void checkParam(RelationshipValidatorParams params) throws BadRequestException {

        if(params.getNewStatus().equals(NEW_STATUS)) {
            if(Arrays.stream(CURRENT_STATUS).noneMatch(params.getOldStatus()::equals)) {
                throw new BadRequestException("Relationship validation fail. REQUESTED - Request can not be processed from user "+params.getUserFromId()+" to user "+params.getUserToId());
            }
            if(params.getFriendsCnt() >= 100) {
                throw new BadRequestException("Relationship validation fail. User "+params.getUserFromId()+" exceeded the limit on the number of friends");
            }
            if(params.getOutgoingReqCnt() >= 10) {
                throw new BadRequestException("Relationship validation fail. User "+params.getUserFromId()+" exceeded the limit on the number of outgoing requests");
            }
        }
    }
}
