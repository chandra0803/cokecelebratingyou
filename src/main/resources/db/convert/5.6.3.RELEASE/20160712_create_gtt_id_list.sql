CREATE GLOBAL TEMPORARY TABLE GTT_ID_LIST
( ID                 NUMBER(18)     NOT NULL,
  REC_SEQ            NUMBER(18)     NOT NULL,
  REF_TEXT_1         VARCHAR2(100),
  REF_TEXT_2         VARCHAR2(100),
  REF_NBR_1          NUMBER
)
ON COMMIT PRESERVE ROWS
/

CREATE INDEX gtt_id_list_idx1
   ON gtt_id_list
   (id,
    rec_seq,
    ref_text_1
   )
/

CREATE INDEX gtt_id_list_idx2
   ON gtt_id_list
   (ref_text_1,
    id,
    ref_text_2,
    ref_nbr_1,
    rec_seq
   )
/

CREATE INDEX gtt_id_list_idx3
   ON gtt_id_list
   (rec_seq,
    id,
    ref_text_1,
    ref_text_2,
    ref_nbr_1
   )
/