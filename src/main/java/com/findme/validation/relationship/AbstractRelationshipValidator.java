package com.findme.validation.relationship;

import com.findme.exceptions.BadRequestException;

public abstract class AbstractRelationshipValidator {
    private AbstractRelationshipValidator nextChainValidator;

    public void setNextAbstractChainValidator(AbstractRelationshipValidator nextChainValidator) {
        this.nextChainValidator = nextChainValidator;
    }

    public void check(RelationshipValidatorParams params) throws BadRequestException {
        checkParam(params);
        if(nextChainValidator!= null)
            nextChainValidator.check(params);
    }

    abstract void checkParam(RelationshipValidatorParams params) throws BadRequestException ;
}
