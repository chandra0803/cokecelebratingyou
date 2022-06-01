DELETE 
FROM   cms_content_data 
WHERE  content_id IN (SELECT id 
                      FROM   cms_content 
                      WHERE  content_key_id IN (SELECT id 
                                                FROM   cms_content_key 
                                                WHERE 
                             asset_id IN (SELECT id 
                                              FROM   cms_asset 
                                              WHERE code='diyCommunications.defaultBanners')))
/
DELETE
FROM   cms_content 
WHERE  content_key_id IN (SELECT id 
                          FROM   cms_content_key 
                          WHERE  asset_id IN (SELECT id 
                                              FROM   cms_asset 
                                              WHERE code='diyCommunications.defaultBanners' ))
/
DELETE
FROM   cms_content_key_audience_lnk 
WHERE  content_key_id IN (SELECT id 
                          FROM   cms_content_key 
                          WHERE  asset_id IN (SELECT id 
                                              FROM   cms_asset 
                                              WHERE  code = 
                                             'diyCommunications.defaultBanners'))
/
DELETE
                          FROM   cms_content_key 
                          WHERE  asset_id IN (SELECT id 
                                              FROM   cms_asset 
                                              WHERE code='diyCommunications.defaultBanners' )
/
DELETE FROM cms_asset_type_item 
WHERE  asset_type_id IN (SELECT asset_type_id 
                         FROM   cms_asset 
                         WHERE  code = 'diyCommunications.defaultBanners') 
       AND KEY LIKE '%BANNER_IMAGE_URL%'
/
DELETE 
FROM   cms_content_data 
WHERE  content_id IN (SELECT id 
                      FROM   cms_content 
                      WHERE  content_key_id IN (SELECT id 
                                                FROM   cms_content_key 
                                                WHERE 
                             asset_id IN (SELECT id 
                                              FROM   cms_asset 
                                              WHERE code LIKE '%diy_banner.content%')))
/
DELETE
FROM   cms_content 
WHERE  content_key_id IN (SELECT id 
                          FROM   cms_content_key 
                          WHERE  asset_id IN (SELECT id 
                                              FROM   cms_asset 
                                              WHERE code LIKE '%diy_banner.content%' ))
/
DELETE
FROM   cms_content_key_audience_lnk 
WHERE  content_key_id IN (SELECT id 
                          FROM   cms_content_key 
                          WHERE  asset_id IN (SELECT id 
                                              FROM   cms_asset 
                                              WHERE  code LIKE '%diy_banner.content%'))
/
DELETE
                          FROM   cms_content_key 
                          WHERE  asset_id IN (SELECT id 
                                              FROM   cms_asset 
                                              WHERE code LIKE '%diy_banner.content%' )
/
DELETE FROM cms_asset_type_item 
WHERE  asset_type_id IN (SELECT asset_type_id 
                         FROM   cms_asset 
                         WHERE  code LIKE '%diy_banner.content%' ) 
       AND KEY LIKE '%BANNER_IMAGE_URL%' 
/