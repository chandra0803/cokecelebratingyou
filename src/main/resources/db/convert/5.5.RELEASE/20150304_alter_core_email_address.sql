ALTER TABLE USER_EMAIL_ADDRESS ADD CONSTRAINT user_email_address_UK 
UNIQUE (USER_ID,EMAIL_TYPE,IS_PRIMARY)
/