ALTER TABLE public_recognition_like
ADD TEAM_ID NUMBER(18)
/
ALTER TABLE public_recognition_comment
ADD TEAM_ID NUMBER(18)
/
ALTER TABLE public_recognition_comment
ADD COMMENTS_LANG_ID VARCHAR2(10)
/
DECLARE

CURSOR C1 IS
SELECT PRL.CLAIM_ID, NVL (RC.TEAM_ID, NC.TEAM_ID) TEAM_ID,public_recognition_like_id
  FROM public_recognition_like prl, recognition_claim rc, nomination_claim nc
 WHERE prl.claim_id = RC.CLAIM_ID AND PRL.CLAIM_ID = NC.CLAIM_ID(+);
 
 CURSOR C2 IS
 SELECT PRc.CLAIM_ID, NVL (RC.TEAM_ID, NC.TEAM_ID) TEAM_ID,public_recognition_comment_id
  FROM public_recognition_comment prc, recognition_claim rc, nomination_claim nc
 WHERE prc.claim_id = RC.CLAIM_ID AND PRc.CLAIM_ID = NC.CLAIM_ID(+);
 
 BEGIN
 
 
 FOR C1_R IN C1 LOOP
 
 UPDATE  public_recognition_like
 SET team_id = c1_r. team_id
 WHERE public_recognition_like_id = c1_r. public_recognition_like_id;
 
 END LOOP;
 
  FOR C2_R IN C2 LOOP
 
 UPDATE  public_recognition_comment
 SET team_id = c2_r. team_id
 WHERE public_recognition_comment_id = c2_r. public_recognition_comment_id;
 
 END LOOP;
 
 END;
/
 
DECLARE


CURSOR C1 IS
 (SELECT -- get most recent participant employer record per user
                                                            r.claim_id, team_id,public_recognition_comment_id
                                                        FROM ( -- rank records by termination date and employer index in reverse order
                                                              SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.team_id
                                                                        ORDER BY
                                                                           claim_id)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM public_recognition_comment PE
                                                                         ) r
                                                       -- the current employment record has the lowest ranking
                                                       WHERE r.rec_rank <> 1);
 
 CURSOR C2 IS 
 (SELECT -- get most recent participant employer record per user
                                                            r.claim_id, team_id,public_recognition_like_id
                                                        FROM ( -- rank records by termination date and employer index in reverse order
                                                              SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.team_id
                                                                        ORDER BY
                                                                           claim_id)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM public_recognition_like PE
                                                                         ) r
                                                       -- the current employment record has the lowest ranking
                                                       WHERE r.rec_rank <> 1);
 
 BEGIN
 
 
 FOR C1_R IN C1 LOOP
 
 DELETE FROM public_recognition_comment WHERE public_recognition_comment_id = c1_r. public_recognition_comment_id;
 
 END LOOP;
 
 FOR C2_R IN C2 LOOP
 
 DELETE FROM public_recognition_like WHERE public_recognition_like_id = c2_r. public_recognition_like_id;
 
 END LOOP;
 END;
/

DECLARE

CURSOR C1 IS
SELECT prc.user_id,prc.public_recognition_comment_id,au.language_id FROM public_recognition_comment prc,
application_user au
WHERE prc.user_id = au.user_id AND prc.comments_lang_id IS NULL;

v_lang VARCHAR2(10);

BEGIN

BEGIN
SELECT STRING_VAL INTO v_lang FROM os_propertyset WHERE entity_name = 'default.language';

END;
FOR C1_R IN C1 LOOP
UPDATE public_recognition_comment prc
SET comments_lang_id = NVL(c1_r.language_id,v_lang)
WHERE public_recognition_comment_id = c1_r.public_recognition_comment_id;

END LOOP;

END;
/