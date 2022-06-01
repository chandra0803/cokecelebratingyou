CREATE TABLE eng_user_connected_to_from
(
user_id			number(18),
connected_to	number(12),
connected_from 	number(12),
trans_month		number(10),
trans_year		number(10),
time_frame		varchar2(10)
)
/
CREATE INDEX ENG_USER_CONN_IDX1 ON eng_user_connected_to_from(user_id)
/
CREATE INDEX ENG_USER_CONN_IDX2 ON eng_user_connected_to_from(USER_ID,TIME_FRAME, TRANS_MONTH, TRANS_YEAR)
/