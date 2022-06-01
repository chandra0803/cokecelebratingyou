CREATE OR REPLACE FUNCTION fnc_claim_report_extract
 (p_in_promotionId      IN  rpt_claim_detail.promotion_id%TYPE,
  p_in_promotion_status IN  promotion.promotion_status%TYPE,  
  p_in_claim_status     IN  rpt_claim_detail.claim_status%TYPE,
  p_in_submitted        IN  VARCHAR,     -- (Show All/Have/Have Not)
  p_in_fromdate         IN  VARCHAR,
  p_in_todate           IN  VARCHAR,
  p_in_organization_id  IN  rpt_claim_detail.node_id%TYPE,
  p_in_filterpromo      IN  VARCHAR,
  p_in_languageCode     IN  VARCHAR,
  p_in_country_id       IN  rpt_claim_detail.submitter_country_id%TYPE,
  p_in_participant_status IN rpt_claim_detail.submitter_pax_status%TYPE,
  p_in_job_position     IN   rpt_claim_detail.submitter_job_position%TYPE,
  p_in_department       IN   rpt_claim_detail.submitter_department%TYPE
)RETURN SYS_REFCURSOR IS
 /*
    Purpost : Created Dynamic product columns with respect to Orgnization.  
      
   Chidamba     10/15/2012   Initial Creation
  */
  ref_cursor SYS_REFCURSOR;  
  l_query long := 'select replace(h.node_name,''_'',NULL) Org_Unit ';
BEGIN
  
  -- Create dynamic SQL statement.  
  for x in (SELECT distinct product_name FROM rpt_claim_product order by 1)
    loop
        l_query := l_query ||
              replace(
              replace( q'|, sum(decode(product_name,$1$,product_qty)) $2$|',
                     '$1$',
                     dbms_assert.enquote_literal(x.product_name) ),
                     '$2$',
                     dbms_assert.simple_sql_name( '"' || x.product_name || '"' ) );
    end loop;
   
   l_query := l_query ||'  ,sum(product_qty) Total 
                           ,h.is_leaf
                      FROM rpt_claim_detail d
                          ,rpt_claim_product p
                          ,promotion pr
                          ,rpt_hierarchy h
                    WHERE d.claim_id          = p.claim_id
                      AND h.node_id           = d.node_id
                      AND pr.promotion_id     = d.promotion_id
                      AND (('||NVL(to_char(p_in_promotionId),'''''')||' IS NULL) OR ('||NVL(to_char(p_in_promotionId),'''''')||' IS NOT NULL 
                                AND pr.promotion_id IN (SELECT * FROM TABLE(get_array('||NVL(to_char(p_in_promotionId),'''''')||')))))   --multiple selections
                      AND pr.promotion_status = NVL('||NVL(p_in_promotion_status,'''''')||',pr.promotion_status)
                      AND d.claim_status      = NVL('||NVL(p_in_claim_status,'''''')||',d.claim_status)
                      AND NVL(d.date_submitted,TRUNC(sysdate)) BETWEEN fnc_locale_to_date_dt('''||p_in_fromDate||''','''||NVL(p_in_languageCode,'en_US')||''') 
                                                                   AND fnc_locale_to_date_dt('''||p_in_toDate||''','''||NVL(p_in_languageCode,'en_US')||''')
                      AND NVL(d.node_id,0)         = NVL('||NVL(to_char(p_in_organization_id),'''''')||',NVL(d.node_id,0))
                      AND d.submitter_country_id   = NVL('||NVL(to_char(p_in_country_id),'''''')||',d.submitter_country_id) 
                      AND d.submitter_job_position = NVL('||NVL(p_in_job_position,'''''')||',d.submitter_job_position)
                      AND d.submitter_department   = NVL('||NVL(p_in_department,'''''')||',d.submitter_department)  
		      AND (('''||p_in_submitted||''' = ''show_all'' AND  1 = 1)
		            OR ('''||p_in_submitted||''' = ''have'' AND  1 = 1)
			    OR ('''||p_in_submitted||''' = ''have_not'' AND  0 = 1))
		            
                    GROUP BY h.node_name ,
                             h.is_leaf' ;
   
   --dbms_output.put_line( l_query ); --testing  
  OPEN ref_cursor FOR l_query ;
  RETURN ref_cursor;
END; 
/