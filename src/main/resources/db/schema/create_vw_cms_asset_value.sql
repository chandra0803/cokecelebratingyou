CREATE OR REPLACE VIEW vw_cms_asset_value AS
SELECT -- flattens CMS data across tables
       a.code AS asset_code,
       c.locale,
       cd.key,
       cd.value_str AS cms_value,
       ck.asset_id,
       c.content_key_id,
       cd.content_id
  FROM cms_asset a,
       cms_content_key ck,
       cms_content c,
       cms_content_data_str cd
 WHERE a.id = ck.asset_id
   AND ck.id = c.content_key_id
   AND c.content_status = 'Live'
   AND c.id = cd.content_id
/
