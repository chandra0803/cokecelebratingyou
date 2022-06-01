---
--- CM v2
---
delete CMS_CONTENT_DATA where content_id in
(select id 
 from CMS_CONTENT 
 where content_key_id in
  (select id 
   from CMS_CONTENT_KEY 
   where asset_id in
    (select id 
     from CMS_ASSET 
     where section_id in
      (select id 
       from CMS_SECTION 
       where code in ('budget_master_data','characteristic_data','claim_form_data','hierarchy_data','node_type_data','promotion_data','quiz_question_data','quiz_question_answer','spotlight_levels_data','calculator_criterion_data','calculator_criterion_rating')
      )
     and code not in ('claim_form_data.claimform.100970','claim_form_data.claimform.100952','claim_form_data.claimform.100957')
    )
  )
)
/
delete from CMS_CONTENT where content_key_id in
(select id 
 from CMS_CONTENT_KEY 
 where asset_id in
  (select id 
   from CMS_ASSET 
   where section_id in
    (select id 
     from CMS_SECTION 
     where code in ('budget_master_data','characteristic_data','claim_form_data','hierarchy_data','node_type_data','promotion_data','quiz_question_data','quiz_question_answer','spotlight_levels_data','calculator_criterion_data','calculator_criterion_rating')
    )
   and code not in ('claim_form_data.claimform.100970','claim_form_data.claimform.100952','claim_form_data.claimform.100957')
  )
)
/
delete CMS_CONTENT_KEY_AUDIENCE_LNK where audience_id in
(select id
 from CMS_AUDIENCE
 where code <> 'default')
/
delete CMS_CONTENT_KEY_AUDIENCE_LNK where content_key_id in
(select id 
 from CMS_CONTENT_KEY 
 where asset_id in
  (select id 
   from CMS_ASSET 
   where section_id in
    (select id 
     from CMS_SECTION 
     where code in ('budget_master_data','characteristic_data','claim_form_data','hierarchy_data','node_type_data','promotion_data','quiz_question_data','quiz_question_answer','spotlight_levels_data','calculator_criterion_data','calculator_criterion_rating')
    )
   and code not in ('claim_form_data.claimform.100970','claim_form_data.claimform.100952','claim_form_data.claimform.100957')
  )
)
/
delete CMS_CONTENT_KEY where asset_id in
(select id 
 from CMS_ASSET 
 where section_id in
  (select id 
   from CMS_SECTION 
   where code in ('budget_master_data','characteristic_data','claim_form_data','hierarchy_data','node_type_data','promotion_data','quiz_question_data','quiz_question_answer','spotlight_levels_data','calculator_criterion_data','calculator_criterion_rating')
  )
 and code not in ('claim_form_data.claimform.100970','claim_form_data.claimform.100952','claim_form_data.claimform.100957')
)
/
delete from CMS_ASSET_TYPE_ITEM where asset_type_id in
(select id 
 from CMS_ASSET_TYPE 
 where id in
  (select asset_type_id 
   from CMS_ASSET 
   where section_id in
    (select id 
     from CMS_SECTION 
     where code in ('budget_master_data','characteristic_data','claim_form_data','hierarchy_data','node_type_data','promotion_data','quiz_question_data','quiz_question_answer','spotlight_levels_data','calculator_criterion_data','calculator_criterion_rating')
    )
   and code not in ('claim_form_data.claimform.100970','claim_form_data.claimform.100952','claim_form_data.claimform.100957')
  )
)
/
delete CMS_AUDIENCE where code <> 'default'
/
delete CMS_ASSET where section_id in
(select id 
 from CMS_SECTION 
 where code in ('budget_master_data','characteristic_data','claim_form_data','hierarchy_data','node_type_data','promotion_data','quiz_question_data','quiz_question_answer','spotlight_levels_data','calculator_criterion_data','calculator_criterion_rating')
)
and code not in ('claim_form_data.claimform.100970','claim_form_data.claimform.100952','claim_form_data.claimform.100957')
/
---
--- cleanup content keys without content
--- this happens with edited content in BEACON-DEV
---
delete from cms_content_key_audience_lnk
where content_key_id in
(select id as id
 from cms_content_key
 minus
 select distinct cms_content.content_key_id as id
 from cms_content
)
/
delete from cms_content_key
where id in
(select id as id
 from cms_content_key
 minus
 select distinct cms_content.content_key_id as id
 from cms_content
)
/
delete from cms_asset_type
where id not in
( select asset_type_id from cms_asset
  union
  select asset_type_id from cms_asset_type_item 
)
/
delete from cms_asset_type_item where KEY LIKE '%STORY_IMAGE_URL_%'
/
delete from cms_asset_type_item where KEY LIKE '%BANNER_IMAGE_URL_%'
/
