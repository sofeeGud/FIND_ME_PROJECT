CREATE TABLE USERS(
                      ID NUMERIC,
                      CONSTRAINT USERS_PK PRIMARY KEY(ID),
                      FIRST_NAME VARCHAR(50),
                      LAST_NAME VARCHAR(50),
                      PHONE VARCHAR(15),
                      COUNTRY VARCHAR(50),
                      COUNTRY_ID NUMERIC,
              /*        CONSTRAINT COUNTRY_FK FOREIGN KEY(COUNTRY_ID) REFERENCES COUNTRY(ID),*/
                      CITY VARCHAR(50),
                      AGE NUMERIC,
                      DATE_REGISTERED TIMESTAMP,
                      DATE_LAST_ACTIVE TIMESTAMP,
                      RELATIONSHIP_STATUS VARCHAR(20),
                      RELIGION VARCHAR(20),
                      SCHOOL VARCHAR(100),
                      UNIVERSITY VARCHAR(100),
                      PASSWORD VARCHAR(50),
                      EMAIL VARCHAR(50),
                      PHOTO OID
);

CREATE SEQUENCE USER_ID_SEQ
    MINVALUE 1
    INCREMENT BY 1;