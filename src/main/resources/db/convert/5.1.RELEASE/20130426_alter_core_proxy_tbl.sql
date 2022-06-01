ALTER TABLE proxy
ADD allow_leaderboard 	NUMBER(1,0) DEFAULT 0
/
ALTER TABLE proxy
MODIFY allow_leaderboard NOT NULL
/
