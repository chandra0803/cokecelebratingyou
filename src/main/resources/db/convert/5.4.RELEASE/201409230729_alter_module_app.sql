ALTER TABLE MODULE_APP ADD ( 
		    TEMPLATE_NAME			VARCHAR2(50),
    		VIEW_NAME			VARCHAR2(50)
    )
/
COMMENT ON COLUMN MODULE_APP.TEMPLATE_NAME    IS ' name '
/
COMMENT ON COLUMN MODULE_APP.VIEW_NAME    IS 'Used to override UI viewName attribute for homeApp.moduleCollection, can be null'
/
COMMENT ON COLUMN MODULE_APP.TEMPLATE_NAME    IS 'Used to override UI templateName attribute for homeApp.moduleCollection, can be null'
/