package com.findme.validation.relationship;

import com.findme.exceptions.BadRequestException;
import com.findme.models.RelationshipStatus;
import lombok.extern.log4j.Log4j;

@Log4j
public class RejectedStatusValidator extends AbstractRelationshipValidator {
    private final RelationshipStatus CURRENT_STATUS = RelationshipStatus.REQUESTED;
    private final RelationshipStatus NEW_STATUS = RelationshipStatus.REJECTED;

    @Override
    protected void checkParam(RelationshipValidatorParams params) throws BadRequestException {
        if (params.getNewStatus().equals(NEW_STATUS) && params.getOldStatus() != CURRENT_STATUS) {
            log.warn("Relationship validation fail. REJECTED - Request can not be processed from user " + params.getUserFromId() + " to user " + params.getUserToId());
            throw new BadRequestException("Relationship validation fail. REJECTED - Request can not be processed from user " + params.getUserFromId() + " to user " + params.getUserToId());
        }
        new RequestedStatusValidator().checkParam(params);
    }
}
