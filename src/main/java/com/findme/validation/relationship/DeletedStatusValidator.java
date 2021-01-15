package com.findme.validation.relationship;

import com.findme.exceptions.BadRequestException;
import com.findme.models.RelationshipStatus;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DeletedStatusValidator extends AbstractRelationshipValidator {

    private final RelationshipStatus CURRENT_STATUS = RelationshipStatus.FRIENDS;
    private final RelationshipStatus NEW_STATUS = RelationshipStatus.DELETED;

    @Override
    protected void checkParam(RelationshipValidatorParams params) throws BadRequestException {
        if(params.getNewStatus().equals(NEW_STATUS) && params.getOldStatus() != CURRENT_STATUS) {
                throw new BadRequestException("Relationship validation fail. DELETED - Request can not be processed from user "+params.getUserFromId()+" to user "+params.getUserToId());
            }
            if(TimeUnit.DAYS.convert(new Date().getTime() - params.getRelationshipDateModified().getTime(), TimeUnit.MILLISECONDS) <= 3) {
                throw new BadRequestException("Relationship validation fail. Relationship from user "+params.getUserFromId()+" to user "+params.getUserToId()+" is < 3 days");
            }
        }
}
