ALTER TABLE FILTER_MODULE_APP ADD ( 
		    SEARCH_ENABLED NUMBER(1)  DEFAULT 1 NOT NULL
    )
/
COMMENT ON COLUMN FILTER_MODULE_APP.SEARCH_ENABLED    IS ' search_enabled search '
/