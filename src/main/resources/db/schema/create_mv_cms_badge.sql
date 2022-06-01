DROP MATERIALIZED VIEW mv_cms_badge
/
CREATE MATERIALIZED VIEW mv_cms_badge
   REFRESH FORCE
   AS SELECT -- pulls CMS in bulk rather than one at a time throgh CMS functions
             cav.asset_code,
             cav.locale,
             -- pivot CMS data into a single row
             MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL))                AS cms_name,
             MAX (DECODE (cav.key, 'EARNED_IMAGE_LARGE', cav.cms_value, NULL))  AS earned_image_large,
             MAX (DECODE (cav.key, 'EARNED_IMAGE_MEDIUM', cav.cms_value, NULL)) AS earned_image_medium,
             MAX (DECODE (cav.key, 'EARNED_IMAGE_SMALL', cav.cms_value, NULL))  AS earned_image_small,
             MAX (DECODE (cav.key, 'EARN_MULTIPLE', cav.cms_value, NULL))       AS earn_multiple,
             MAX (DECODE (cav.key, 'DESCRIPTION', cav.cms_value, NULL))         AS cms_desc,
             -- include PK fields
             cav.asset_id,
             cav.content_key_id,
             cav.content_id
        FROM ( -- flattens CMS data across tables
               SELECT a.code AS asset_code,
                      c.locale,
                      cd.key,
                      cd.value_str cms_value,
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
                  AND a.code = 'promotion.badge'
                  AND c.id = cd.content_id
             ) cav
    GROUP BY cav.asset_code,
             cav.locale,
             cav.asset_id,
             cav.content_key_id,
             cav.content_id
/
CREATE INDEX mv_cms_badge_idx1
   ON mv_cms_badge
   ( asset_code,
     locale,
     cms_name,
     earned_image_small
   )
/
