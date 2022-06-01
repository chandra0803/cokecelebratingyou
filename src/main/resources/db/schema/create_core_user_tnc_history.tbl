CREATE SEQUENCE USER_TNC_HISTORY_PK_SQ INCREMENT BY 1 START WITH 5000
/
CREATE TABLE USER_TNC_HISTORY
(USER_TNC_HISTORY_ID   NUMBER(18)          NOT NULL,
 USER_ID               NUMBER(18)          NOT NULL,
 TNC_ACTION            VARCHAR2(30 CHAR)   NOT NULL,
 CREATED_BY            VARCHAR2(30 CHAR)   NOT NULL,
 DATE_CREATED          DATE                NOT NULL,
 CONSTRAINT USER_TNC_HISTORY_PK                PRIMARY KEY(USER_TNC_HISTORY_ID),
 CONSTRAINT USER_TNC_HISTORY_USER_ID_FK FOREIGN KEY (USER_ID) REFERENCES APPLICATION_USER(USER_ID))
/
comment on column user_tnc_history.user_tnc_history_id is 'Unique key to identify the user tnc history'
/
comment on column user_tnc_history.user_id is 'Foreign key of user'
/
comment on column user_tnc_history.tnc_action is 'Captured tnc action of the user'
/
comment on column user_tnc_history.created_by is '- Retrofitted'
/
comment on column user_tnc_history.date_created is '- Retrofitted'
/
