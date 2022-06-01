UPDATE os_propertyset
SET STRING_VAL = (SELECT FNC_JAVA_ENCRYPT( entry ) FROM webservices)
WHERE  ENTITY_NAME ='shared.services.security.key.dev'
/
UPDATE os_propertyset
SET STRING_VAL = (SELECT FNC_JAVA_ENCRYPT( entry ) FROM webservices)
WHERE  ENTITY_NAME ='shared.services.security.key.qa'
/
UPDATE os_propertyset
SET STRING_VAL = (SELECT FNC_JAVA_ENCRYPT( entry ) FROM webservices)
WHERE  ENTITY_NAME ='shared.services.security.key.preprod'
/


