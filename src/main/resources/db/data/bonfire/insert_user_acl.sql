
---
--- activate the Pax SSN acl and give it to bi-admin
---

UPDATE acl SET IS_ACTIVE=1 WHERE CODE='PAX_SSN'
/

INSERT INTO user_acl (USER_ID, ACL_ID, GUID, TARGET, PERMISSION, CREATED_BY, DATE_CREATED)
VALUES (5662,10,'guid-5662-10','-', 'VIEW_FULL', 0, sysdate)
/
