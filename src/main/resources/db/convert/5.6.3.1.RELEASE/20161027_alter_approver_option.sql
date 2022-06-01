alter table approver_option
add (sequence_num number(18))
/
 
update approver_option
set sequence_num = (approval_round-1)
/
 
alter table approver_option
modify (sequence_num number(18) not null)
/
