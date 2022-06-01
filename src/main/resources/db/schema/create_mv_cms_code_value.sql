DROP MATERIALIZED VIEW mv_cms_code_value
/
CREATE MATERIALIZED VIEW mv_cms_code_value
   REFRESH FORCE
   AS SELECT -- pulls CMS in bulk rather than one at a time throgh CMS functions
             cav.asset_code,
             cav.locale,
             -- pivot CMS data into a single row
             MAX (DECODE (cav.key, 'CODE', cav.cms_value, NULL)) AS cms_code,
             MAX (DECODE (cav.key, 'ABBR', cav.cms_value, NULL)) AS cms_abbr,
             MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
             MAX (DECODE (cav.key, 'DESC', cav.cms_value, NULL)) AS cms_desc,
             MAX (DECODE (cav.key, 'STATUS', cav.cms_value, NULL)) AS cms_status,
             asset_rowid,
             content_key_rowid,
             content_rowid,
             COUNT(*) AS pivot_count
        FROM mv_cms_asset_value cav
    GROUP BY cav.asset_code,
             cav.locale,
             asset_rowid,
             content_key_rowid,
             content_rowid
/

/* Create indexes */
CREATE INDEX mv_cms_code_value_idx1
   ON mv_cms_code_value
   ( asset_code,
     locale,
     cms_code,
     cms_name
   )
/
