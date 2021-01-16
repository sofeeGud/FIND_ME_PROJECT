package com.findme.validation.post;

import com.findme.exceptions.BadRequestException;
import com.findme.models.RelationshipStatus;

public class UserPagePostedValidator extends AbstractPostValidator {
    @Override
    protected void checkParam(PostValidatorParams params) throws BadRequestException {
        if (!params.getPost().getUserPosted().getId().equals(params.getPost().getUserPagePosted().getId()))
            if (params.getRelBtwAuthorAndPagePostedUser() == null || !params.getRelBtwAuthorAndPagePostedUser().getStatus().equals(RelationshipStatus.FRIENDS)) {
                throw new BadRequestException("Post page id validation fail. User id:[" + params.getPost().getUserPagePosted().getId() + "] is not a friend.");
            }
        new MessageValidator().checkParam(params);
    }
}

