CREATE INDEX USER_LAST_LOW_IDX ON APPLICATION_USER(LOWER("LAST_NAME"))
/
CREATE INDEX USER_FIRST_LOW_IDX ON APPLICATION_USER(LOWER("FIRST_NAME"))
/
CREATE INDEX USER_LAST_UPR_IDX ON APPLICATION_USER(UPPER("LAST_NAME"))
/
CREATE INDEX USER_FIRST_UPR_IDX ON APPLICATION_USER(UPPER("FIRST_NAME"))
/
CREATE UNIQUE INDEX USER_ACTIVE_IDX   ON application_user(user_id,is_active)
/