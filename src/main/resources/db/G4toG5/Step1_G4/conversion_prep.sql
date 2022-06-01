CREATE TABLE prep_encrypt_pax
( USER_ID          NUMBER,
  SSN               VARCHAR2(300 char),
  SECRET_ANSWER     VARCHAR2(300 CHAR),
  AWARDBANQ_NBR     VARCHAR2(300 CHAR),
  CENTRAX_ID        VARCHAR2(300 CHAR),  
  CREATED_BY        VARCHAR2(100 CHAR),
  DATE_CREATED      DATE)
/

CREATE TABLE prep_encrypt_journal
(  journal_id       NUMBER,
  AWARDBANQ_NBR    varchar2(300 char),
  GIFT_CODE_PIN   varchar2(300 char),
  GIFT_CODE       varchar2(300 char),
  CREATED_BY        VARCHAR2(100 CHAR),
  DATE_CREATED      DATE)

/

CREATE TABLE prep_encrypt_merch
( merch_order_id       NUMBER,
  GIFT_CODE       varchar2(300 char),
  GIFT_CODE_KEY   varchar2(300 char),
  CREATED_BY        VARCHAR2(100 CHAR),
  DATE_CREATED      DATE)

/

CREATE OR REPLACE procedure prc_prep_encrypt
is
cursor c1_pax is
select au.user_id,au.ssn, au.secret_answer,p.awardbanq_nbr,p.centrax_id
from participant p,application_user au where
p.user_id(+) = au.user_id;  

cursor c1_journal is
select *
from journal;

cursor c1_merch_order is
select *
from merch_order;

begin
--delete from prep_encrypt;
delete from prep_encrypt_pax;

for c1_r in c1_pax loop
insert into prep_encrypt_pax
select c1_r.user_id,fnc_java_decrypt(c1_r.ssn),fnc_java_decrypt(c1_r.secret_answer),fnc_java_decrypt(c1_r.awardbanq_nbr),fnc_java_decrypt(c1_r.centrax_id),'prc_prep_encrypt',sysdate from dual;
end loop;

delete from prep_encrypt_journal;

for c1_r in c1_journal loop
insert into prep_encrypt_journal
select c1_r.journal_id,fnc_java_decrypt(c1_r.AWARDBANQ_NBR),fnc_java_decrypt(c1_r.GIFT_CODE_PIN),fnc_java_decrypt(c1_r.GIFT_CODE),'prc_prep_encrypt',sysdate from dual;
end loop;

delete from prep_encrypt_merch;

for c1_r in c1_merch_order loop
insert into prep_encrypt_merch
select c1_r.merch_order_id,fnc_java_decrypt(c1_r.GIFT_CODE),fnc_java_decrypt(c1_r.GIFT_CODE_KEY),'prc_prep_encrypt',sysdate from dual;
end loop;

dbms_output.put_line('prc_prep_encrypt completed sucessful');

end;
/


BEGIN
prc_prep_encrypt;
END;
/