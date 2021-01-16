package com.findme.validation.post;

import com.findme.exceptions.BadRequestException;

public class MessageValidator extends AbstractPostValidator {
    @Override
    protected void checkParam(PostValidatorParams params) throws BadRequestException {

        if (params.getPost().getMessage().length() > 200) {
            throw new BadRequestException("Post message validation fail. Message [" + params.getPost().getMessage() + "] is be more than 200 symbols");
        }
    }

}
