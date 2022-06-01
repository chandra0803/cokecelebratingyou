ALTER TABLE MERCH_ORDER
ADD date_last_reminded DATE 
/
COMMENT ON COLUMN merch_order.date_last_reminded IS 'The date on which a manager last send a reminder to the participant.' 
/
