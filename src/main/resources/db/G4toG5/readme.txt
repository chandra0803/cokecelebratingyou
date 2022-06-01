Here are the scripts to be run for both G4 and G5 schemas in the same sequence.

On G4 Schema..

1). conversion_prep.sql
2). grant_select_from_G4_to_G5.sql
             Note : Need to change the prefix 'TargetPrefix' to G5 prefix to be updated while calling prc_grant_select_g4_to_g5


On G5 Schema..

1). create_synonyms_on_G5.sql
                  Note : SourcePrefix Need to be assigned with the G4 schema to be converted (line # 163)
2). convert_cms_data.sql
3). Convert_Tier1_data.sql
                  Note : Need to change the prefix 'SourcePrefix' to G4 prefix to be converted while calling prc_tier_1_data_convert
4). Convert_Tier2_data.sql
                  Note : Need to change the prefix 'SourcePrefix' to G4 prefix to be converted while calling  prc_tier_2_data_convert
5). prc_set_sequences.sql
*************