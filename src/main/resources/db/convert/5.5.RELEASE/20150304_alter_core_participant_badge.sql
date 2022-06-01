alter table participant_badge add ( ssi_contest_id  NUMBER(18) )
/
alter table participant_badge modify badge_rule_id NUMBER(18) NOT NULL
/