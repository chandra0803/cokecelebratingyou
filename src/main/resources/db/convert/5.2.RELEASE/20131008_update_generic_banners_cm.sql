--rename home.genericbanners  keys 
--[LARGE_IMAGE_URL] [BANNER_IMAGE_URL_4X4]
--[HALF_LARGE_IMAGE_URL] BANNER_IMAGE_URL_4X2]
--[MEDIUM_IMAGE_URL] BANNER_IMAGE_URL_2X2]

update cms_content_data set key ='BANNER_IMAGE_URL_4X4' where key ='LARGE_IMAGE_URL'  and content_id in (
SELECT id
  FROM cms_content
 WHERE content_key_id IN
          (SELECT id
             FROM cms_content_key
            WHERE asset_id IN (SELECT ID
                                 FROM cms_asset
                                WHERE code = 'home.genericbanners')) )
/

update cms_content_data set key ='BANNER_IMAGE_URL_4X2' where key ='HALF_LARGE_IMAGE_URL' and content_id in (
SELECT id
  FROM cms_content
 WHERE content_key_id IN
          (SELECT id
             FROM cms_content_key
            WHERE asset_id IN (SELECT ID
                                 FROM cms_asset
                                WHERE code = 'home.genericbanners')) )
/

update cms_content_data set key ='BANNER_IMAGE_URL_2X2' where key ='MEDIUM_IMAGE_URL' and content_id in (
SELECT id
  FROM cms_content
 WHERE content_key_id IN
          (SELECT id
             FROM cms_content_key
            WHERE asset_id IN (SELECT ID
                                 FROM cms_asset
                                WHERE code = 'home.genericbanners')) )
/

update cms_asset_type_item set key ='BANNER_IMAGE_URL_4X4' where key ='LARGE_IMAGE_URL' and asset_type_id in (
          SELECT id
             FROM cms_asset_type
            WHERE id IN (SELECT asset_type_id
                                 FROM cms_asset
                                WHERE code = 'home.genericbanners') ) 
/

update cms_asset_type_item set key ='BANNER_IMAGE_URL_4X2' where key ='HALF_LARGE_IMAGE_URL' and asset_type_id in (
          SELECT id
             FROM cms_asset_type
            WHERE id IN (SELECT asset_type_id
                                 FROM cms_asset
                                WHERE code = 'home.genericbanners') ) 
/

update cms_asset_type_item set key ='BANNER_IMAGE_URL_2X2' where key ='MEDIUM_IMAGE_URL' and asset_type_id in (
          SELECT id
             FROM cms_asset_type
            WHERE id IN (SELECT asset_type_id
                                 FROM cms_asset
                                WHERE code = 'home.genericbanners') ) 
/


