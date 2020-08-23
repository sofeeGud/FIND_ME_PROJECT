CREATE TABLE MESSAGE(
                        ID NUMERIC,
                        CONSTRAINT MESSAGE_PK PRIMARY KEY(ID),
                        TEXT OID,
                        DATE_SENT TIMESTAMP,
                        DATE_READ TIMESTAMP,
                        USER_FROM_ID NUMERIC,
                        CONSTRAINT USER_FROM_FK FOREIGN KEY(USER_FROM_ID) REFERENCES USERS(ID),
                        USER_TO_ID NUMERIC,
                        CONSTRAINT USER_TO_FK FOREIGN KEY(USER_TO_ID) REFERENCES USERS(ID)
);

CREATE SEQUENCE MESSAGE_ID_SEQ
    MINVALUE 1
    INCREMENT BY 1;