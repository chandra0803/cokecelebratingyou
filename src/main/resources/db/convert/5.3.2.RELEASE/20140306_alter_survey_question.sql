alter table survey_question add(RESPONSE_TYPE		 	 VARCHAR2(30) DEFAULT 'standardResponse' NOT NULL,
                                IS_OPEN_ENDED_REQUIRED	 NUMBER(1,0) DEFAULT 1,
                                START_SELECTION_LABEL	 VARCHAR2(100),
                                END_SELECTION_LABEL		 VARCHAR2(100),
                                START_SELECTION_VALUE	 NUMBER(18),
                                END_SELECTION_VALUE		 NUMBER(18),
                                PRECISION_VALUE				 NUMBER(18,2))
/
alter table survey_question_response drop(IS_OPEN_ENDED_REQUIRED, IS_OPEN_ENDED)
/
alter table participant_survey_response modify(survey_question_response_id NUMBER(18) NULL )
/
alter table participant_survey_response add(SLIDER_RESPONSE NUMBER(14, 2))
/