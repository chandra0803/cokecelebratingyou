CREATE OR REPLACE PROCEDURE prc_update_cms_content_data
IS

/*-----------------------------------------------------------------------------

  Procedure Name:
  Purposer: Recognition Step Status Report
  
  Modification History
  
  Person        Date        Comments
  -----------   ----------  ----------------------------------------------------
  Arun S        03/02/2017  Initial Creation

-----------------------------------------------------------------------------*/


  --cursor
  CURSOR cur_cms_data IS
    SELECT code, content_key_id, db_val,
           CASE
                WHEN db_val = 'false,no' THEN 'no'
                WHEN db_val = 'true,yes' THEN 'yes'
                WHEN db_val = 'de,de_DE' THEN 'de_DE'
                WHEN db_val = 'es,es_ES' THEN 'es_ES'
                WHEN db_val = 'fr,fr_CA' THEN 'fr_CA'
                WHEN db_val = 'en,en_US' THEN 'en_US'
                WHEN db_val = 'mx_za,mx_zac' THEN 'mx_za'
                WHEN db_val = 'mx_yu,mx_yuc' THEN 'mx_yu'
                WHEN db_val = 'mx_ve,mx_ver' THEN 'mx_ve'
                WHEN db_val = 'mx_tl,mx_tlax' THEN 'mx_tl'
                WHEN db_val = 'mx_tamp,mx_tm' THEN 'mx_tm'
                WHEN db_val = 'mx_tab,mx_tb' THEN 'mx_tb'
                WHEN db_val = 'mx_so,mx_son'    THEN 'mx_so'
                WHEN db_val = 'mx_sl,mx_slp'    THEN 'mx_sl'
                WHEN db_val = 'mx_si,mx_sin'    THEN 'mx_si'
                WHEN db_val = 'mx_qr,mx_qroo'   THEN 'mx_qr'
                WHEN db_val = 'mx_qro,mx_qt'    THEN 'mx_qt'
                WHEN db_val = 'mx_pu,mx_pue'    THEN 'mx_pu'
                WHEN db_val = 'mx_oa,mx_oax'    THEN 'mx_oa'
                WHEN db_val = 'mx_na,mx_nay'    THEN 'mx_na'
                WHEN db_val = 'mx_mo,mx_mor'    THEN 'mx_mo'
                WHEN db_val = 'mx_mi,mx_mich'   THEN 'mx_mi'
                WHEN db_val = 'mx_mex,mx_mx'    THEN 'mx_mx'
                WHEN db_val = 'mx_ja,mx_jal'    THEN 'mx_ja'
                WHEN db_val = 'mx_hg,mx_hgo'    THEN 'mx_hg'
                WHEN db_val = 'mx_gt,mx_gto'    THEN 'mx_gt'
                WHEN db_val = 'mx_gr,mx_gro'    THEN 'mx_gr'
                WHEN db_val = 'mx_dg,mx_dgo'    THEN 'mx_dg'
                WHEN db_val = 'mx_ch,mx_chih'   THEN 'mx_ch'
                WHEN db_val = 'mx_cl,mx_coli'   THEN 'mx_cl'
                WHEN db_val = 'mx_co,mx_coah'   THEN 'mx_co'
                WHEN db_val = 'mx_chps,mx_cs'   THEN 'mx_cs'
                WHEN db_val = 'mx_camp,mx_cm'   THEN 'mx_cm'
                WHEN db_val = 'mx_bcs,mx_bs'    THEN 'mx_bs'
                WHEN db_val = 'mx_ag,mx_ags'    THEN 'mx_ag'
                WHEN db_val = 'pr_pr,pri_pr'    THEN 'pr_pr'
                WHEN db_val = 'mx_bc,mx_bcn'    THEN 'mx_bc'
                WHEN db_val = 'mx_em,mx_emex'   THEN 'mx_em'
                WHEN db_val = 'Zertifikat,certificate' THEN 'certificate'
                WHEN db_val = 'DailyTip,dailyTip'   THEN 'dailyTip'
                WHEN db_val = 'No,no'               THEN 'no'
                WHEN db_val = 'stag,train'          THEN 'train'
                WHEN db_val = 'haveNot,have_not'    THEN 'haveNot'
                WHEN db_val = 'BIW,game'            THEN 'game'
                WHEN db_val = 'Range,range'         THEN 'range'
                WHEN db_val = 'Hard,hard'           THEN 'hard'
                WHEN db_val = 'Specifyaudience,specifyaudience' THEN 'specifyaudience'
                WHEN db_val = 'ManualApproval,manualApproval'   THEN 'manualApproval'
                WHEN db_val = 'Weekly,weekly'       THEN 'weekly'
                --WHEN db_val = 'Hard,hard'           THEN 'Hard'
                WHEN db_val = 'Daily,daily'         THEN 'daily'
                --WHEN db_val = 'received,receiver'   THEN 'received,receiver'
                WHEN db_val = 'Manual,manual'       THEN 'manual'
                WHEN db_val = 'team,unlimited size team' THEN 'team'
                WHEN db_val = 'both,limited size team'   THEN 'both'
                --WHEN db_val = 'Manual,manual'           THEN 'Manual,manual'
                WHEN db_val = 'Points,points'           THEN 'points'
                --WHEN db_val = 'Manual,manual'           THEN 'Manual,manual'
                WHEN db_val = 'Nominator,nominator'     THEN 'nominator'
                WHEN db_val = 'Approver,approver'       THEN 'approver'
                WHEN db_val = 'goalQuestChallengePoint,goalquestModule' THEN 'goalquestModule'
                WHEN db_val = 'claimModule,productClaim'THEN 'claimModule'
                --WHEN db_val = 'Manual,manual'           THEN 'Manual,manual'
                WHEN db_val = 'Africa/Windhoek,Afrique/Windhoek'    THEN 'Africa/Windhoek'
                WHEN db_val = 'America/Denver,Amérique/Denver'      THEN 'America/Denver'
                WHEN db_val = 'America/Eirunepe,Amérique/Eirunepe'  THEN 'America/Eirunepe'
                WHEN db_val = 'America/Los_Angeles,Amérique/Los Angeles'    THEN 'America/Los_Angeles'
                WHEN db_val = 'Antarctica/Casey,Antarctique/Casey'  THEN 'Antarctica/Casey'
                WHEN db_val = 'Antarctica/Davis,Antarctique/Davis'  THEN 'Antarctica/Davis'
                WHEN db_val = 'Pacific/Apia,Pacifique/Apia'         THEN 'Pacific/Apia'
                WHEN db_val = 'activities,information'              THEN 'information'
                WHEN db_val = 'reports,shop'        THEN 'reports'
                WHEN db_val = 'programs,social'     THEN 'programs'
                WHEN db_val = 'home,recognition'    THEN 'recognition'
                WHEN db_val = 'manager,throwdown'   THEN 'manager'
                WHEN db_val = 'program,promotion'   THEN 'program'
                WHEN db_val = 'no,non'              THEN 'no'
                WHEN db_val = 'game,hobbies'              THEN 'game'
                WHEN db_val = 'city,hobbies'              THEN 'city'
                WHEN db_val ='CM-Code-CM,Code' THEN 'Code'
           ELSE NULL     
            END valid_val,
           CASE
                WHEN db_val = 'false,no' THEN 'false'
                WHEN db_val = 'true,yes' THEN 'true'
                WHEN db_val = 'de,de_DE' THEN 'de'
                WHEN db_val = 'es,es_ES' THEN 'es'
                WHEN db_val = 'fr,fr_CA' THEN 'fr'
                WHEN db_val = 'en,en_US' THEN 'en'
                WHEN db_val = 'mx_za,mx_zac' THEN 'mx_zac'
                WHEN db_val = 'mx_yu,mx_yuc' THEN 'mx_yuc'
                WHEN db_val = 'mx_ve,mx_ver' THEN 'mx_ver'
                WHEN db_val = 'mx_tl,mx_tlax' THEN 'mx_tlax'
                WHEN db_val = 'mx_tamp,mx_tm' THEN 'mx_tamp'
                WHEN db_val = 'mx_tab,mx_tb' THEN 'mx_tab'
                WHEN db_val = 'mx_so,mx_son'    THEN 'mx_son'
                WHEN db_val = 'mx_sl,mx_slp'    THEN 'mx_slp'
                WHEN db_val = 'mx_si,mx_sin'    THEN 'mx_sin'
                WHEN db_val = 'mx_qr,mx_qroo'   THEN 'mx_qroo'
                WHEN db_val = 'mx_qro,mx_qt'    THEN 'mx_qro'
                WHEN db_val = 'mx_pu,mx_pue'    THEN 'mx_pue'
                WHEN db_val = 'mx_oa,mx_oax'    THEN 'mx_oax'
                WHEN db_val = 'mx_na,mx_nay'    THEN 'mx_nay'
                WHEN db_val = 'mx_mo,mx_mor'    THEN 'mx_mor'
                WHEN db_val = 'mx_mi,mx_mich'   THEN 'mx_mich'
                WHEN db_val = 'mx_mex,mx_mx'    THEN 'mx_mex'
                WHEN db_val = 'mx_ja,mx_jal'    THEN 'mx_jal'
                WHEN db_val = 'mx_hg,mx_hgo'    THEN 'mx_hgo'
                WHEN db_val = 'mx_gt,mx_gto'    THEN 'mx_gto'
                WHEN db_val = 'mx_gr,mx_gro'    THEN 'mx_gro'
                WHEN db_val = 'mx_dg,mx_dgo'    THEN 'mx_dgo'
                WHEN db_val = 'mx_ch,mx_chih'   THEN 'mx_chih'
                WHEN db_val = 'mx_cl,mx_coli'   THEN 'mx_coli'
                WHEN db_val = 'mx_co,mx_coah'   THEN 'mx_coah'
                WHEN db_val = 'mx_chps,mx_cs'   THEN 'mx_chps'
                WHEN db_val = 'mx_camp,mx_cm'   THEN 'mx_camp'
                WHEN db_val = 'mx_bcs,mx_bs'    THEN 'mx_bcs'
                WHEN db_val = 'mx_ag,mx_ags'    THEN 'mx_ags'
                WHEN db_val = 'pr_pr,pri_pr'    THEN 'pri_pr'
                WHEN db_val = 'mx_bc,mx_bcn'    THEN 'mx_bcn'
                WHEN db_val = 'mx_em,mx_emex'   THEN 'mx_emex'
                WHEN db_val = 'Zertifikat,certificate' THEN 'Zertifikat'
                WHEN db_val = 'DailyTip,dailyTip'   THEN 'DailyTip'
                WHEN db_val = 'No,no'               THEN 'No'
                WHEN db_val = 'stag,train'          THEN 'stag'
                WHEN db_val = 'haveNot,have_not'    THEN 'have_not'
                WHEN db_val = 'BIW,game'            THEN 'BIW'
                WHEN db_val = 'Range,range'         THEN 'Range'
                WHEN db_val = 'Hard,hard'           THEN 'Hard'
                WHEN db_val = 'Specifyaudience,specifyaudience' THEN 'Specifyaudience'
                WHEN db_val = 'ManualApproval,manualApproval'   THEN 'ManualApproval'
                WHEN db_val = 'Weekly,weekly'       THEN 'Weekly'
                --WHEN db_val = 'Hard,hard'           THEN 'Hard,hard'
                WHEN db_val = 'Daily,daily'         THEN 'Daily'
                --WHEN db_val = 'received,receiver'   THEN 'received,receiver'
                WHEN db_val = 'Manual,manual'       THEN 'Manual'
                WHEN db_val = 'team,unlimited size team' THEN 'unlimited size team'
                WHEN db_val = 'both,limited size team'  THEN 'limited size team'
                --WHEN db_val = 'Manual,manual'           THEN 'Manual,manual'
                WHEN db_val = 'Points,points'           THEN 'Points'
                --WHEN db_val = 'Manual,manual'           THEN 'Manual,manual'
                WHEN db_val = 'Nominator,nominator'     THEN 'Nominator'
                WHEN db_val = 'Approver,approver'       THEN 'Approver'
                WHEN db_val = 'goalQuestChallengePoint,goalquestModule' THEN 'goalQuestChallengePoint'
                WHEN db_val = 'claimModule,productClaim' THEN 'productClaim'
                --WHEN db_val = 'Manual,manual'           THEN 'Manual,manual'
                WHEN db_val = 'Africa/Windhoek,Afrique/Windhoek'    THEN 'Afrique/Windhoek'
                WHEN db_val = 'America/Denver,Amérique/Denver'      THEN 'Amérique/Denver'
                WHEN db_val = 'America/Eirunepe,Amérique/Eirunepe'  THEN 'Amérique/Eirunepe'
                WHEN db_val = 'America/Los_Angeles,Amérique/Los Angeles'    THEN 'Amérique/Los Angeles'
                WHEN db_val = 'Antarctica/Casey,Antarctique/Casey'  THEN 'Antarctique/Casey'
                WHEN db_val = 'Antarctica/Davis,Antarctique/Davis'  THEN 'Antarctique/Davis'
                WHEN db_val = 'Pacific/Apia,Pacifique/Apia'         THEN 'Pacifique/Apia'
                WHEN db_val = 'activities,information'              THEN 'activities'
                WHEN db_val = 'reports,shop'        THEN 'shop'
                WHEN db_val = 'programs,social'     THEN 'social'
                WHEN db_val = 'home,recognition'    THEN 'home'
                WHEN db_val = 'manager,throwdown'   THEN 'throwdown'
                WHEN db_val = 'program,promotion'   THEN 'promotion'
                WHEN db_val = 'no,non'              THEN 'non'
                WHEN db_val = 'game,hobbies'              THEN 'hobbies'
                WHEN db_val = 'city,hobbies'              THEN 'hobbies'
                WHEN db_val = 'CM-Code-CM,Code' THEN 'CM-Code-CM'
           ELSE NULL
            END invalid_val
      FROM (
            SELECT code, content_key_id --,val
                   ,LISTAGG(  val, ',') WITHIN GROUP (ORDER BY val) db_val 
              FROM (
                    SELECT DISTINCT a.code, cc.content_key_id ,dbms_lob.substr(ccd.VALUE) val
                      FROM cms_content cc,
                           cms_content_data ccd,
                           (SELECT ca.code, cck.id 
                              FROM cms_asset ca, 
                                   cms_content_key cck,
                                   cms_content cc,
                                   cms_content_data ccd 
                             WHERE -- ca.id=10015107 and
                                   ca.id = cck.asset_id 
                               AND cck.id = cc.content_key_id 
                               AND cc.content_status ='Live'
                               AND cc.id = ccd.content_id 
                               AND KEY = 'CODE'
                             GROUP BY ca.code, cck.id 
                            HAVING COUNT(DISTINCT dbms_lob.substr(ccd.VALUE)) >1
                            ) a
                     WHERE a.id = cc.content_key_id 
                       AND cc.id = ccd.content_id  
                       AND ccd.key ='CODE'
                       AND cc.content_status ='Live'
                    ) cm
              GROUP BY code, content_key_id
            );
      

  --execution log variables
  C_process_name          CONSTANT execution_log.process_name%type  := 'prc_update_cms_content_data';
  C_release_level         CONSTANT execution_log.release_level%type := '1';
  C_severity_i            CONSTANT execution_log.severity%TYPE := 'INFO';  
  C_severity_e            CONSTANT execution_log.severity%TYPE := 'ERROR';

  --procedure variables
  v_stage                 VARCHAR2(500);
  v_log_msg               execution_log.text_line%TYPE;
  v_count                 PLS_INTEGER := 0;

BEGIN

  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,
                          'Process started',
                          NULL);

  v_stage := 'Update cms_content_data';
  FOR rec_cms_data IN cur_cms_data LOOP

    UPDATE cms_content_data  --cms_content_data_bkp03022017
       SET VALUE = rec_cms_data.valid_val
     WHERE KEY = 'CODE'
       AND content_id IN (
                            SELECT ccd.content_id
                              FROM cms_content_data ccd,
                                   cms_content cc
                             WHERE ccd.content_id    = cc.ID
                               AND cc.content_key_id = rec_cms_data.content_key_id
                               AND ccd.KEY           = 'CODE'
                               AND cc.content_status = 'Live'
                               AND dbms_lob.substr(ccd.VALUE) = rec_cms_data.invalid_val
                          );

    v_count := v_count + 1;
    
  END LOOP;

  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,
                          'Process completed, Count of Rec Updated: '||v_count,
                          NULL);
--  COMMIT;


EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Failed at Stage: '||v_stage||' --> '||SQLERRM,
                            NULL);
--    COMMIT;
  
END;
/
BEGIN 
  PRC_UPDATE_CMS_CONTENT_DATA;
  COMMIT;
END;
