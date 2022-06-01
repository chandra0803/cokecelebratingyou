CREATE MATERIALIZED VIEW LOG ON mv_cms_asset_value
   WITH ROWID, SEQUENCE (asset_code, locale, key, cms_value,
                         asset_rowid, content_key_rowid, content_rowid
                        ) INCLUDING NEW VALUES
/
