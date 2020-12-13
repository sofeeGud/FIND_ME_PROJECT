package com.findme.validation;

import com.findme.exceptions.BadRequestException;
import com.findme.models.RelationshipStatus;

public class FriendsStatusValidator extends AbstractRelationshipValidator {
    private final RelationshipStatus CURRENT_STATUS = RelationshipStatus.REQUESTED;
    private final RelationshipStatus NEW_STATUS = RelationshipStatus.FRIENDS;

    @Override
    protected void checkParam(RelationshipValidatorParams params) throws BadRequestException {

        if(params.getNewStatus().equals(NEW_STATUS)) {
            if(params.getOldStatus() != CURRENT_STATUS){
                throw new BadRequestException("Relationship validation fail. FRIENDS - Request can not be processed from user "+params.getUserFromId()+" to user "+params.getUserToId());
            }
        }
    }
}
