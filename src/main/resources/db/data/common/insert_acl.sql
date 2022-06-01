---
--- ACL data
---
--- NOTE: system code depends on the ACL code and classnames.  Do not change unless you know what you are doing!
---
INSERT INTO acl (ACL_ID,NAME,CODE,HELP_TEXT,CLASS_NAME, IS_ACTIVE,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES (10,'Participant SSN Access','PAX_SSN','Determines user access to Pax SSN, target is ignored, permissions are VIEW_NONE, VIEW_MASK, VIEW_FULL and EDIT','com.biperf.core.security.acl.PaxSSNAclEntry', 0, 0,sysdate,null,null,1)
/

INSERT INTO acl (ACL_ID,NAME,CODE,HELP_TEXT,CLASS_NAME, IS_ACTIVE,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES (11,'Report Node','RPT_NODE','Determines if a user can report on this node, permission is only ALLOWS at this point, target is node name under which user can report (primary hierarchy)','com.biperf.core.security.acl.ReportNodeAclEntry', 0, 0,sysdate,null,null,1)
/
