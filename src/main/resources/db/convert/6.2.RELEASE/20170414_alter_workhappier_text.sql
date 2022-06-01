UPDATE OS_PROPERTYSET
SET ENTITY_KEY = 'WorkHappier'
WHERE ENTITY_NAME = 'work.happier'
/
UPDATE MODULE_APP 
SET NAME = 'WorkHappier', DESCRIPTION = 'Desc - WorkHappier'
WHERE NAME = 'Work Happier'
/
UPDATE MESSAGE 
SET MESSAGE_NAME = 'WorkHappier Report Extract Message'
WHERE MESSAGE_NAME = 'Work Happier Report Extract Message'
/