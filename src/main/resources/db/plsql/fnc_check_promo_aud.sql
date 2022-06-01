CREATE OR REPLACE FUNCTION      fnc_check_promo_aud (
   p_in_giver_recvr_type   IN VARCHAR2,
   p_in_promotion_type     IN VARCHAR2,
   p_in_promotion_id       IN VARCHAR2)
   RETURN NUMBER
IS
   /******************************************************
   -- Ravi Dhanekula 07/15/2014  Initial Creation
   --Suresh J       08/28/2015 - Bug 63665 - When Badge Promotion selected in filter page,function retruns 0  
   ******************************************************/

   v_return   NUMBER;
BEGIN
   IF p_in_giver_recvr_type = 'receiver'
   THEN
      SELECT COUNT (1)
        INTO v_return
        FROM PROMOTION
       WHERE     promotion_type = NVL (p_in_promotion_type, promotion_type)
             AND (  p_in_promotion_id IS NULL
                  OR promotion_id IN (SELECT *
                                        FROM TABLE (
                                                get_array_varchar (
                                                   p_in_promotion_id)))
                 OR  promotion_id        IN     --08/28/2015 
                      (select eligible_promotion_id 
                          from badge_promotion 
                            where promotion_id IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_promotion_id))))   --08/28/2015
                          )
             AND CASE
                    WHEN secondary_audience_type = 'sameasprimaryaudience'
                    THEN
                       primary_audience_type
                    ELSE
                       secondary_audience_type
                 END = 'allactivepaxaudience'
             AND ROWNUM < 2;
   ELSE
      IF NVL (p_in_promotion_type, ' ') <> 'quiz'
      THEN
         SELECT COUNT (1)
           INTO v_return
           FROM PROMOTION
          WHERE     primary_audience_type = 'allactivepaxaudience'
                AND promotion_type =
                       NVL (p_in_promotion_type, promotion_type)
                AND (   p_in_promotion_id IS NULL
                     OR promotion_id IN (SELECT *
                                           FROM TABLE (
                                                   get_array_varchar (
                                                      p_in_promotion_id))))
                AND ROWNUM < 2;
      ELSE
         SELECT COUNT (1)
           INTO v_return
           FROM PROMOTION
          WHERE     primary_audience_type = 'allactivepaxaudience'
                AND promotion_type IN ('quiz', 'diy_quiz')
                AND (   p_in_promotion_id IS NULL
                     OR promotion_id IN (SELECT *
                                           FROM TABLE (
                                                   get_array_varchar (
                                                      p_in_promotion_id))))
                AND ROWNUM < 2;
      END IF;
   END IF;

   RETURN v_return;
EXCEPTION
   WHEN OTHERS
   THEN
      prc_execution_log_entry (
         'FNC_CHECK_PROMO_AUD',
         1,
         'ERROR',
            'giver_recvr_type: '
         || p_in_giver_recvr_type
         || ' promo: '
         || p_in_promotion_id
         || SQLERRM,
         NULL);
      v_return := 0;
      RETURN v_return;
END;
/
