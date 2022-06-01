ALTER TABLE leaderboard_participant
ADD rank  NUMBER(8)  DEFAULT 0
/
ALTER TABLE leaderboard_participant
MODIFY rank NOT NULL
/
