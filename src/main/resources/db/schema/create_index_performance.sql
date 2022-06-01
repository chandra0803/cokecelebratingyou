-- G6 query peformance indexes
DECLARE
   -- in-line process
   PROCEDURE add_idx
   ( i_idx_name      IN VARCHAR2,
     i_on_clause     IN VARCHAR2,
     i_uniq_clause   IN VARCHAR2 DEFAULT NULL
   ) IS
      CURSOR c_find_index IS
      SELECT ui.index_name
        FROM user_indexes ui
       WHERE ui.index_name = UPPER(i_idx_name);
   BEGIN
      dbms_output.put_line('Create ' || i_idx_name);

      FOR rec IN c_find_index LOOP
         EXECUTE IMMEDIATE 'DROP INDEX ' || rec.index_name;
      END LOOP;

      EXECUTE IMMEDIATE 'CREATE ' || i_uniq_clause || ' INDEX ' || i_idx_name || ' ON ' || i_on_clause;
   END add_idx;
BEGIN
   add_idx('claim_group_id_idx', 'claim(claim_group_id, claim_id)');
   add_idx('claim_item_idx4', 'claim_item(date_approved, approval_status_type, claim_id, claim_item_id)');
   add_idx('claim_item_approver_idx4', 'claim_item_approver(date_approved, approval_status_type, claim_item_id, claim_group_id, notification_date)');

   add_idx('participant_badge_idx3', 'participant_badge(claim_id, badge_rule_id)');

   add_idx('participant_employer_idx4', 'participant_employer(department_type, user_id)');

   add_idx('public_recognition_comment_i2', 'public_recognition_comment(team_id, user_id)');
   add_idx('public_recognition_like_idx2', 'public_recognition_like(team_id, user_id)');

   add_idx('purl_recipient_idx3', 'purl_recipient(user_id, award_date, promotion_id, purl_recipient_id)');
   add_idx('purl_recipient_idx4', 'purl_recipient(award_date, user_id, promotion_id, purl_recipient_id)');

   add_idx('user_connections_idx2', 'user_connections(sender_id, date_created, receiver_id, id)');
   add_idx('user_connections_idx3', 'user_connections(receiver_id, date_created, sender_id, id)');

END;
/
