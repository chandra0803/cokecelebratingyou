-- dependent tables
delete USER_ACL
/
delete USER_ADDRESS
/
delete USER_ROLE
/
delete USER_PHONE
/
delete PARTICIPANT_EMPLOYER
/
delete PARTICIPANT_CONTACT_METHOD
/
delete PARTICIPANT
/
delete USER_EMAIL_ADDRESS
/
delete NODE_CHARACTERISTIC
/
delete USER_NODE
/
delete USER_CHARACTERISTIC
/
delete HIERARCHY_NODE_TYPE
/
delete BUDGET_HISTORY
/
delete NODE cascade
/
commit
/
delete application_user cascade 
/
delete role cascade
/
delete acl cascade
/
delete employer cascade
/
delete HIERARCHY cascade
/
delete CHARACTERISTIC cascade
/
delete NODE_TYPE cascade
/

delete OS_PROPERTYSET cascade
/
delete PICK_LIST_ITEM cascade
/
delete BUDGET cascade
/
delete BUDGET_MASTER cascade
/
delete country
/
delete currency
/
delete supplier
/
delete claim_form_step_element
/
delete claim_form_step
/
delete promotion
/
delete claim_form
/
commit
/