CREATE MATERIALIZED VIEW mv_cms_asset_value
   REFRESH FORCE
   AS SELECT -- flattens CMS data across tables
             a.code AS asset_code,
             c.locale,
             cd.key,
             cd.value_str AS cms_value,
             -- source table row IDs
             a.ROWID AS asset_rowid,
             ck.ROWID AS content_key_rowid,
             c.ROWID AS content_rowid,
             cd.ROWID AS content_data_str_rowid
        FROM cms_asset a,
             cms_content_key ck,
             cms_content c,
             cms_content_data_str cd
       WHERE a.id = ck.asset_id
         AND ck.id = c.content_key_id
         AND c.content_status = 'Live'
         AND c.id = cd.content_id
/

/* Create indexes */
CREATE INDEX mv_cms_asset_value_idx1
   ON mv_cms_asset_value
   ( asset_code,
     locale,
     key,
     cms_value
   )
/
