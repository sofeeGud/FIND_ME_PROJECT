package com.findme.validation.post;

import com.findme.exceptions.BadRequestException;
import com.findme.models.RelationshipStatus;
import lombok.extern.log4j.Log4j;

@Log4j
public class UserPagePostedValidator extends AbstractPostValidator {
    @Override
    protected void checkParam(PostValidatorParams params) throws BadRequestException {
        if (!params.getPost().getUserPosted().getId().equals(params.getPost().getUserPagePosted().getId())
                && (params.getRelBtwAuthorAndPagePostedUser() == null
                || !params.getRelBtwAuthorAndPagePostedUser()
                          .getStatus().equals(RelationshipStatus.FRIENDS))) {
            log.warn("Post page id validation fail. User id:" + params.getPost().getUserPagePosted().getId() + " is not a friend.");
            throw new BadRequestException("Post page id validation fail. User id:" + params.getPost().getUserPagePosted().getId() + " is not a friend.");
        }
        new MessageValidator().checkParam(params);
    }
}

