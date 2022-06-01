CREATE OR REPLACE PACKAGE pkg_report_login_activity IS

/*-----------------------------------------------------------------------------

  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Arun          12/10/2012  Initial creation
  Ravi Dhanekula  12/11/2012 Added start and end date as additional parameters to fnc_logged_in_cnt and fnc_total_visits
   
-----------------------------------------------------------------------------*/

FUNCTION fnc_eligible_participants 
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE   
  )
  RETURN NUMBER;

FUNCTION fnc_logged_in_cnt
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE,
   p_in_start_date  IN VARCHAR2,
   p_in_end_date IN VARCHAR2, 
   p_in_locale   IN VARCHAR2   
  )
  RETURN NUMBER;

FUNCTION fnc_total_visits
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE,
   p_in_start_date  IN VARCHAR2,
   p_in_end_date IN VARCHAR2, 
   p_in_locale   IN VARCHAR2   
  )
  RETURN NUMBER;

FUNCTION fnc_pax_elig_count 
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE,   
   p_in_country_id  IN VARCHAR2  
  )
  RETURN NUMBER;

FUNCTION fnc_pax_total_visits
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE,   
   p_in_country_id  IN VARCHAR2,
   p_in_start_date  IN VARCHAR2,
   p_in_end_date    IN VARCHAR2, 
   p_in_locale      IN VARCHAR2   
  )
  RETURN NUMBER;

FUNCTION fnc_pax_logged_in_cnt
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE,   
   p_in_country_id  IN VARCHAR2,
   p_in_start_date  IN VARCHAR2,
   p_in_end_date    IN VARCHAR2, 
   p_in_locale      IN VARCHAR2   
  )
  RETURN NUMBER;
  
END;
/
CREATE OR REPLACE PACKAGE BODY pkg_report_login_activity IS

/*-----------------------------------------------------------------------------
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Arun          12/10/2012  Initial creation
  Ravi Dhanekula  12/11/2012 Added start and end date as additional parameters to fnc_logged_in_cnt and fnc_total_visits
 
-----------------------------------------------------------------------------*/

FUNCTION fnc_eligible_participants 
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE
  )
  RETURN NUMBER
IS

/*-----------------------------------------------------------------------------
  Purpose: Retrieves count of eligible participants for the input node and its
           child nodes
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Arun          12/10/2012  Initial creation
-----------------------------------------------------------------------------*/

  v_eli_pax NUMBER := 0;

BEGIN

  SELECT COUNT(DISTINCT un.user_id)
    INTO v_eli_pax
    FROM user_node un,
         participant p,
         vw_curr_pax_employer pe
   WHERE un.user_id = p.user_id 
     AND un.is_primary = 1 
     AND p.user_id  = pe.user_id(+)
     AND p.status   = NVL (p_in_pax_status, p.status)
     AND un.role    = NVL (p_in_pax_role, un.role)
     AND ((p_in_pax_dept is NULL) OR pe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept))))
     AND un.node_id IN (SELECT node_id
                          FROM node
                       CONNECT BY PRIOR node_id = parent_node_id
                         START WITH node_id = p_in_node_id);
                                    
  RETURN v_eli_pax;

EXCEPTION
  WHEN OTHERS THEN
    RETURN v_eli_pax;
END fnc_eligible_participants;

FUNCTION fnc_logged_in_cnt
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE,
   p_in_start_date  IN VARCHAR2,
   p_in_end_date IN VARCHAR2, 
   p_in_locale   IN VARCHAR2   
  )
  RETURN NUMBER
IS

/*-----------------------------------------------------------------------------
  Purpose: Retrieves count of logged in participants for the input node and its
           child nodes
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Arun          12/10/2012  Initial creation
-----------------------------------------------------------------------------*/

  v_logged_in_pax NUMBER := 0;

BEGIN


  SELECT COUNT(DISTINCT la.user_id) cnt_have_logged_in
    INTO v_logged_in_pax
    FROM login_activity la,
         user_node un,
         participant p,
         vw_curr_pax_employer pe
   WHERE la.user_id          = un.user_id 
     AND un.is_primary = 1
     AND un.user_id          = p.user_id
     AND p.user_id           = pe.user_id(+)
     AND p.status            = NVL (p_in_pax_status, p.status)
     AND un.role             = NVL (p_in_pax_role, un.role)
     AND (pe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))
          OR (p_in_pax_dept is NULL))
     AND TRUNC(la.login_date_time)  BETWEEN fnc_locale_to_date_dt(p_in_start_date, p_in_locale)  
                                 AND fnc_locale_to_date_dt(p_in_end_date, p_in_locale)     
     AND un.node_id          IN (SELECT node_id
                                   FROM node
                                CONNECT BY PRIOR node_id = parent_node_id
                                  START WITH node_id     = p_in_node_id);

  RETURN v_logged_in_pax;

EXCEPTION
  WHEN OTHERS THEN
    RETURN v_logged_in_pax;
END fnc_logged_in_cnt;

FUNCTION fnc_total_visits
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE,
   p_in_start_date  IN VARCHAR2,
   p_in_end_date IN VARCHAR2, 
   p_in_locale   IN VARCHAR2  
  )
  RETURN NUMBER
IS

/*-----------------------------------------------------------------------------
  Purpose: Retrieves count of total visits by participants for the input node 
           and its child nodes
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Arun          12/10/2012  Initial creation
-----------------------------------------------------------------------------*/

  v_pax_visits NUMBER := 0;

BEGIN

  SELECT COUNT(la.user_id) cnt_visits
    INTO v_pax_visits
    FROM login_activity la,
         user_node un,
         participant p,
         vw_curr_pax_employer pe
   WHERE la.user_id          = un.user_id
     AND un.is_primary = 1
     AND un.user_id          = p.user_id
     AND p.user_id           = pe.user_id(+)
     AND p.status            = NVL (p_in_pax_status, p.status)
     AND un.role             = NVL (p_in_pax_role, un.role)
     AND (pe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))
           OR (p_in_pax_dept is NULL))
     AND TRUNC(la.login_date_time)  BETWEEN fnc_locale_to_date_dt(p_in_start_date, p_in_locale)  
                                 AND fnc_locale_to_date_dt(p_in_end_date, p_in_locale)     
     AND un.node_id          IN (SELECT node_id
                                   FROM node
                                CONNECT BY PRIOR node_id = parent_node_id
                                  START WITH node_id     = p_in_node_id);

  RETURN v_pax_visits;

EXCEPTION
  WHEN OTHERS THEN
    RETURN v_pax_visits;
END fnc_total_visits;


FUNCTION fnc_pax_total_visits
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE,  
   p_in_country_id  IN VARCHAR2,  
   p_in_start_date  IN VARCHAR2,
   p_in_end_date    IN VARCHAR2, 
   p_in_locale      IN VARCHAR2   
  )
  RETURN NUMBER
IS

/*-----------------------------------------------------------------------------
  Purpose: Retrieves total pax visit count for the input node and its child nodes
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Arun          12/24/2012  Initial creation
-----------------------------------------------------------------------------*/
  
  v_total_count NUMBER := 0;  

BEGIN

  SELECT COUNT(user_id)
    INTO v_total_count
    FROM login_activity LA
   WHERE  EXISTS  (SELECT un.user_id
                       FROM user_node un,
                            participant p,
                            vw_curr_pax_employer pe,
                            user_address ua
                      WHERE un.user_id    = p.user_id
                        AND un.is_primary = 1
                        AND p.user_id     = pe.user_id (+)
                        AND un.user_id    = ua.user_id
                        AND ua.is_primary = 1 --Need to validate 01/28/2013
                        AND p.status      = NVL (p_in_pax_status, p.status)
                        AND un.role       = NVL (p_in_pax_role, un.role)
                        AND (pe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))    OR (p_in_pax_dept is NULL))
                        AND (ua.country_id      IN (SELECT * FROM TABLE(get_array_varchar(p_in_country_id)))    OR (p_in_country_id is NULL))
                        AND un.node_id    IN (SELECT node_id
                                                FROM node
                                             CONNECT BY PRIOR node_id = parent_node_id
                                               START WITH node_id     = p_in_node_id
                                              )
                       AND un.user_id = la.user_id
                    )
    AND TRUNC(login_date_time) BETWEEN fnc_locale_to_date_dt (p_in_start_date, p_in_locale)
                                   AND fnc_locale_to_date_dt (p_in_end_date,   p_in_locale);
                                                       
  RETURN v_total_count;
  
EXCEPTION
  WHEN OTHERS THEN
    RETURN v_total_count;
END fnc_pax_total_visits;


FUNCTION fnc_pax_elig_count 
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE, 
   p_in_country_id  IN VARCHAR2
  )
  RETURN NUMBER
IS

/*-----------------------------------------------------------------------------
  Purpose: Retrieves count of eligible participants for the input node and its
           child nodes
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Arun          12/24/2012  Initial creation
-----------------------------------------------------------------------------*/

  v_eli_pax NUMBER := 0;

BEGIN

  SELECT COUNT(DISTINCT un.user_id)
    INTO v_eli_pax
    FROM user_node un,
         participant p,
         vw_curr_pax_employer pe,
         user_address ua
   WHERE un.user_id    = p.user_id 
     AND un.is_primary = 1
     AND p.user_id     = pe.user_id(+)
     AND p.user_id    = ua.user_id
     AND ua.is_primary = 1
     AND p.status      = NVL (p_in_pax_status, p.status)
     AND un.role       = NVL (p_in_pax_role, un.role)
     AND (pe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))    OR (p_in_pax_dept is NULL))
     AND (ua.country_id      IN (SELECT * FROM TABLE(get_array_varchar(p_in_country_id)))    OR (p_in_country_id is NULL))
     AND un.node_id IN (SELECT node_id
                          FROM node
                       CONNECT BY PRIOR node_id = parent_node_id
                         START WITH node_id = p_in_node_id);
                                    
  RETURN v_eli_pax;

EXCEPTION
  WHEN OTHERS THEN
    RETURN v_eli_pax;
END fnc_pax_elig_count;


FUNCTION fnc_pax_logged_in_cnt
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN participant_employer.department_type%TYPE,   
   p_in_country_id  VARCHAR2,   
   p_in_start_date  IN VARCHAR2,
   p_in_end_date    IN VARCHAR2, 
   p_in_locale      IN VARCHAR2   
  )
  RETURN NUMBER

IS

/*-----------------------------------------------------------------------------
  Purpose: Retrieves count of logged in participants for the input node and its
           child nodes
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Arun          12/24/2012  Initial creation
-----------------------------------------------------------------------------*/
  
  v_total_count NUMBER := 0;
  
BEGIN  

  SELECT COUNT(DISTINCT user_id)
    INTO v_total_count
    FROM login_activity
   WHERE user_id IN (SELECT un.user_id
                       FROM user_node un,
                            participant p,
                            vw_curr_pax_employer pe,
                            user_address ua
                      WHERE un.user_id    = p.user_id
                        AND un.is_primary = 1
                        AND p.user_id     = pe.user_id(+)
                        AND pe.user_id    = ua.user_id
                        AND ua.is_primary = 1
                        AND p.status      = NVL (p_in_pax_status, p.status)
                        AND un.role       = NVL (p_in_pax_role, un.role)
                        AND (pe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))    OR (p_in_pax_dept is NULL))
                        AND (ua.country_id      IN (SELECT * FROM TABLE(get_array_varchar(p_in_country_id)))    OR (p_in_country_id is NULL))
                        AND un.node_id    IN (SELECT node_id
                                                FROM node
                                             CONNECT BY PRIOR node_id = parent_node_id
                                               START WITH node_id     = p_in_node_id
                                              )
                    )
    AND TRUNC(login_date_time) BETWEEN fnc_locale_to_date_dt (p_in_start_date, p_in_locale)
                                   AND fnc_locale_to_date_dt (p_in_end_date,   p_in_locale);
                                                       
  RETURN v_total_count;

EXCEPTION
  WHEN OTHERS THEN
    RETURN v_total_count;
END fnc_pax_logged_in_cnt;


END;
/