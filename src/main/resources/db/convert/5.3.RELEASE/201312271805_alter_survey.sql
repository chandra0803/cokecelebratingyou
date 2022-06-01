ALTER TABLE survey 
ADD (
		 module_type    VARCHAR2(30)
    )
/
ALTER TABLE survey_question_response 
ADD (
		  response_count 	   	  	    	    NUMBER(18),
   		  IS_OPEN_ENDED						NUMBER(1,0) DEFAULT 0,
   		  IS_OPEN_ENDED_REQUIRED				NUMBER(1,0) DEFAULT 1
    )
/