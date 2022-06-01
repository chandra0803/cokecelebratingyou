CREATE OR REPLACE VIEW vw_cms_code_value AS
SELECT -- pulls CMS in bulk rather than one at a time throgh CMS functions
       cav.asset_code,
       cav.locale,
       -- pivot CMS data into a single row
       MAX(DECODE( cav.key, 'CODE', cav.cms_value, NULL)) AS cms_code,
       MAX(DECODE( cav.key, 'ABBR', cav.cms_value, NULL)) AS cms_abbr,
       MAX(DECODE( cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
       MAX(DECODE( cav.key, 'DESC', cav.cms_value, NULL)) AS cms_desc,
       MAX(DECODE( cav.key, 'STATUS', cav.cms_value, NULL)) AS cms_status,
       -- include PK fields
       cav.asset_id,
       cav.content_key_id,
       cav.content_id
  FROM vw_cms_asset_value cav
 GROUP BY cav.asset_code,
       cav.locale,
       cav.asset_id,
       cav.content_key_id,
       cav.content_id
/