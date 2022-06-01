--rename EZRecognitionModule to recognitionModule  keys 

update cms_content_data set value='recognitionModule' 
where key ='CODE' and value like 'EZRecognitionModule' and
content_id in (
SELECT id
  FROM cms_content
 WHERE content_key_id IN
          (SELECT id
             FROM cms_content_key
            WHERE asset_id IN (SELECT ID
                                 FROM cms_asset
                                WHERE code = 'picklist.tiles.moduleapp.tilemappingtype.items'))) 