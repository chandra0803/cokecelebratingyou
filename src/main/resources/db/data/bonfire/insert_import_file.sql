ALTER SESSION SET nls_date_format='DD-MON-RRRR HH24:MI:SS'
/

---
--- IMPORT_FILE data
---

--- Budget Import Files
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (100, 'Budget Import File 1', 'bud', 0, 0, 'stg', 0, TO_DATE('01-JAN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), null, null,
        null, null, 0)
/
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (120, 'Budget Import File 2', 'bud', 0, 0, 'ver', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), null, null, 1)
/
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (140, 'Budget Import File 3', 'bud', 0, 0, 'imp', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), 2)
/

--- Deposit Import Files
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (200, 'Deposit Import File 1', 'dep', 0, 0, 'stg', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), null, null,
        null, null, 0)
/
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (220, 'Deposit Import File 2', 'dep', 0, 0, 'ver', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), null, null, 1)
/
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (240, 'Deposit Import File 3', 'dep', 0, 0, 'imp', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), 2)
/

--- Hierarchy Import Files
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (300, 'Hierarchy Import File 1', 'hier', 32, 0, 'stg', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        null, null, null, null, 0)
/
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (320, 'Hierarchy Import File 2', 'hier', 0, 0, 'ver', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), null, null, 1)
/
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (340, 'Hierarchy Import File 3', 'hier', 0, 0, 'imp', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), 2)
/

--- Participant Import Files
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (400, 'Participant Import File 1', 'par', 1, 1, 'stg', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        null, null, null, null, 0)
/
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (420, 'Participant Import File 2', 'par', 2, 0, 'ver', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), null, null, 1)
/
INSERT INTO import_file (import_file_id, file_name, file_type, import_record_count,
                         import_record_error_count, status, staged_by, date_staged,
                         verified_by, date_verified, imported_by, date_imported,
                         version)
VALUES (440, 'Participant Import File 3', 'par', 1, 0, 'imp', 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), 0, TO_DATE('01-JUN-2005 00:00:00', 'DD-MM-YYYY HH24:MI:SS'), 2)
/
