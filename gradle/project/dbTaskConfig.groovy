/**
 * This file is used by the DatabaseScriptTaskPlugin to configure the dynamic database tasks.
 * It's a groovy script that gets applied to the project, so the Gradle script syntax is available.
 * Explanation of the format:
 * <pre>
 * name: [
 *   task: true|false                       // True means create a task, false means it'll just be an internal method>
 *   description: "describe task"           // Only needed when task is true. Description for the task
 *   group: project.ext.databaseGroup       // "database". Only needed when task is true. Groups the task based on the functionality
 *   dependsOn: ["taskName"]                // Optional. Task dependencies. List of strings.
 *   condition: {closure}                   // Optional. If defined, the closure's return value determines if the method runs or not.
 *                                                       Uses groovy-truth. If false, the actions will not happen.
 *   actions: [
 *     dbScript("path/to/script.sql"),      // Execute a SQL script
 *     dbScript("script", "extra_task"),    // Execute a SQL script. Also adds this action to a separate task. May list multiple extra tasks.
 *     dbJava("script"),                    // Same as dbScript, but intended to compile Java source on the database
 *     dbSql("select sysdate from dual")    // Execute a SQL statement
 *     dbSublist("name"),                   // Calls an internal method. So you can run the actions of a different list.
 *     dbClosure({closure})                 // Executes an arbitrary closure.
 *   ]
 * ]
 * </pre>
 */

String dbPath = config.dbResourceDir
String CREATE_REPLACE_PLSQL = "create_replace_plsql"

project.ext.dbTaskConfigList << [
  _load_env_specific_plsql_objects: [
    task: false,
    condition: { project.db.environment != "Local" },
    actions: [
      dbScript("$dbPath/plsql/script_kill_existing_db_connections.sql")
    ]
  ],

  _database_compile_all_objects: [
    task: false,
    actions: [
      dbScript("$dbPath/plsql/script_compile_all_objects.sql"),
      dbScript("$dbPath/plsql/script_convert_encrypted_values.sql")
    ]
  ],

  _data_cm_cleanup: [
    task: false,
    condition: { !isProductTeam() },
    actions: [
      dbScript("$dbPath/data/cleanup_cm_data.sql"),
      dbScript("$dbPath/data/bonfire/insert_cm_audience.sql")
    ]
  ],

  _data_cm_copy: [
    task: false,
    actions: [
      dbScript("$dbPath/data/delete_cm_data.sql"),
      dbScript("$dbPath/data/copy_cm_data.sql")
    ]
  ],

  data_cm_copy: [
    task: true,
    description: "copies CM data into the database selected",
    group: project.ext.databaseGroup,
    actions: [
      dbSublist("_data_cm_copy"),
      dbSublist("_data_cm_cleanup")
    ]
  ],

  data_delete_cm_copy_backup: [
    task: true,
    description: "copies backup CM data into the database selected",
    group: project.ext.databaseGroup,
    actions: [
      dbScript("$dbPath/data/delete_cm_data.sql"),
      dbScript("$dbPath/data/copy_cm_data_backup.sql")
    ]
  ],

  _update_installation_data_sysvars: [
    task: false,
    actions: getInstallationDataSysvarsActions() // Actions are built dynamically by this method - located at bottom of this file
  ],

  _data_common_insert: [
    task: false,
    actions: [
      dbScript("$dbPath/data/common/clean_data.sql"),
      dbScript("$dbPath/data/common/insert_os_property_set.sql"),

      dbSublist("_update_installation_data_sysvars"),

      dbScript("$dbPath/data/common/insert_application_user.sql"),
      dbScript("$dbPath/data/common/insert_role.sql"),
      dbScript("$dbPath/data/common/insert_acl.sql"),
      dbScript("$dbPath/data/common/insert_supplier.sql"),
      dbScript("$dbPath/data/common/insert_country.sql"),
      dbScript("$dbPath/data/common/insert_country_suppliers.sql"),
      dbScript("$dbPath/data/common/insert_currency.sql"),
      dbScript("$dbPath/data/common/insert_message.sql"),
      dbScript("$dbPath/data/common/insert_user_role.sql"),
      dbScript("$dbPath/data/common/insert_rec_card.sql"),
      dbScript("$dbPath/data/common/insert_rec_ecard.sql"),
      dbScript("$dbPath/data/common/insert_module_app.sql"),
      dbScript("$dbPath/data/common/insert_encryption_conversion.sql"),
      dbScript("$dbPath/data/common/insert_report.sql"),
      dbScript("$dbPath/data/common/insert_filter_module_app.sql"),
      dbScript("$dbPath/data/common/insert_node_type.sql"),
      dbScript("$dbPath/data/common/insert_locale_date_pattern.sql"),
      dbScript("$dbPath/data/common/insert_survey.sql"),
      dbScript("$dbPath/data/common/insert_survey_question.sql"),
      dbScript("$dbPath/data/common/insert_survey_question_response.sql"),
      dbScript("$dbPath/data/common/insert_workhappier.sql")
    ]
  ],

  _data_insert_bonfire: [
    task: false,
    condition: { isProductTeam() },
    actions: [
      dbScript("$dbPath/data/bonfire/insert_application_user.sql"),
      dbScript("$dbPath/data/bonfire/insert_country.sql"),
      dbScript("$dbPath/data/bonfire/insert_audience.sql"),
      dbScript("$dbPath/data/bonfire/insert_user_address.sql"),
      dbScript("$dbPath/data/bonfire/insert_user_characteristic_type.sql"),
      dbScript("$dbPath/data/bonfire/insert_user_characteristic.sql"),
      dbScript("$dbPath/data/bonfire/insert_user_email_address.sql"),
      dbScript("$dbPath/data/bonfire/insert_user_phone.sql"),
      dbScript("$dbPath/data/bonfire/insert_user_role.sql"),
      dbScript("$dbPath/data/bonfire/insert_participant_contact_method.sql"),
      dbScript("$dbPath/data/bonfire/insert_employer.sql"),
      dbScript("$dbPath/data/bonfire/insert_participant_employer.sql"),
      dbScript("$dbPath/data/bonfire/insert_node_type.sql"),
      dbScript("$dbPath/data/bonfire/insert_hierarchy.sql"),
      dbScript("$dbPath/data/bonfire/insert_hierarchy_node_type.sql"),
      dbScript("$dbPath/data/bonfire/insert_node.sql"),
      dbScript("$dbPath/data/bonfire/insert_nt_characteristic_type.sql"),
      dbScript("$dbPath/data/bonfire/insert_node_characteristic.sql"),
      dbScript("$dbPath/data/bonfire/insert_user_node.sql"),
      dbScript("$dbPath/data/bonfire/insert_participant.sql"),
      dbScript("$dbPath/data/bonfire/insert_import_file.sql"),
      dbScript("$dbPath/data/bonfire/insert_stage_pax_import_record.sql"),
      dbScript("$dbPath/data/bonfire/insert_os_property_set_fileload.sql"),
      dbScript("$dbPath/data/common/insert_rpt_characteristic_lookup.sql"),
      dbScript("$dbPath/data/bonfire/insert_product_85.sql"),
      dbScript("$dbPath/data/bonfire/update_participant_awardbanq.sql"),
      dbScript("$dbPath/data/bonfire/update_os_property_set_chatter.sql"),
      dbScript("$dbPath/data/bonfire/update_os_property_set_shared_services.sql"),
      dbSublist("_data_insert_purl"),
      dbSublist("_data_insert_bonfire_goalquest"),
      dbSublist("_data_insert_bonfire_challengepoint")
    ]
  ],
  
  _data_update_bonfire_post_liquibase: [
    task: false,
    condition: { isProductTeam() },
    actions: [
      dbScript("$dbPath/data/bonfire/update_os_property_set_post_lb.sql")
    ]
  ],  

  _data_insert_purl: [
    task: false,
    condition: { project.config.installation.data.recognition.purl.enable == "true" },
    actions: [
      dbScript("$dbPath/data/bonfire/update_os_propertyset_enable_purl.sql")
    ]
  ],

  _data_insert_bonfire_goalquest: [
    task: false,
    condition: { project.goalquestInstalled },
    actions: [
      dbScript("$dbPath/data/bonfire/insert_promo_goalquest.sql")
    ]
  ],

  _data_insert_bonfire_challengepoint: [
    task: false,
    condition: { project.challengepointInstalled },
    actions: [
      dbScript("$dbPath/data/bonfire/insert_promo_challengepoint.sql")
    ]
  ],

  _data_update_meplus: [
    task: false,
    // Condition: If install variable meplus.enabled is true
    condition: { getInstallSystemVariableList().any { var -> var.key == "meplus.enabled" && var.value == "1" } },
    actions: [
      dbClosure({ println "**** MEPlus Setup ****"}),
      dbScript("$dbPath/data/common/update_meplus_setup.sql")
    ]
  ],

  _data_update_recognition_only: [
    task: false,
    // Condition: If install variable recognition-only.enabled is true
    condition: { getInstallSystemVariableList().any { var -> var.key == "recognition-only.enabled" && var.value == "1" } },
    actions: [
      dbClosure({ println "**** Recognition-only Setup ****"}),
      dbScript("$dbPath/data/common/update_recognition_only_module.sql"),
      dbScript("$dbPath/data/common/update_recognition_only_sysvar.sql"),
      dbScript("$dbPath/data/common/update_recognition_only_report.sql")
    ]
  ],

  _data_update_salesmaker: [
    task: false,
    // Condition: If install variable salesmaker.enabled is true
    condition: {
      boolean runScript = false
      List<Map<String,String>> sysVars = getInstallSystemVariableList()
      sysVars.each({ varMap ->
        if( varMap.key == "salesmaker.enabled" && varMap.value == "1" ) {
          runScript = true
        }
      })
      return runScript
    },
    actions: [
      dbClosure({ println "**** SalesMaker Setup ****"}),
      dbScript("$dbPath/data/common/update_salesmaker_module_setup.sql"),
      dbScript("$dbPath/data/common/update_salesmaker_report_setup.sql"),
      dbScript("$dbPath/data/common/update_salesmaker_sysvar_setup.sql")
    ]
  ],

  data_productclaim_insert: [
    task: true,
    description: "inserts product claim data into the database selected",
    group: project.ext.databaseGroup,
    condition: { project.productClaimInstalled },
    actions: [
      dbScript("$dbPath/data/common/insert_pc_claim_form_templates.sql")
    ]
  ],

  data_recognition_insert: [
    task: true,
    description: "inserts recognition data into the database selected",
    group: project.ext.databaseGroup,
    condition: { project.recognitionInstalled },
    actions: [
      dbScript("$dbPath/data/common/insert_rec_claim_form_templates.sql")
    ]
  ],

  data_insert: [
    task: true,
    description: "inserts project data into the database selected",
    group: project.ext.databaseGroup,
    actions: [
      dbSublist("_data_common_insert"),
      dbSublist("data_recognition_insert"),
      dbSublist("data_productclaim_insert"),
      dbSublist("_data_insert_bonfire"),
      dbSublist("_data_update_meplus"),
      dbSublist("_data_update_recognition_only"),
      dbSublist("_data_update_salesmaker")
    ]
  ],
  
  data_update_post_liquibase: [
    task: true,
    description: "inserts project data into the database selected, post liquibase updates",
    group: project.ext.databaseGroup,
    actions: [
      dbSublist("_data_update_bonfire_post_liquibase")
    ]
  ]  
]

project.ext.dbTaskConfigList << [
  schema_cm_backup_delete: [
    task: true,
    description: "deletes the cm backup of the database schema selected",
    group: project.ext.databaseGroup,
    condition: { project.db.environment == "Local" },
    actions: [
      dbScript("$dbPath/drop_cm_backup.ddl")
    ]
  ],

  schema_cm_backup_recreate: [
    task: true,
    description: "recreates the selected database CM backup schema",
    group: project.ext.databaseGroup,
    actions: [
      dbScript("$dbPath/drop_cm_backup.ddl"),
      dbScript("$dbPath/schema/create_cm_backup.tbl")
    ]
  ],

  schema_cm_delete: [
  task: true,
  description: "deletes the content manager of the selected database schema",
  group: project.ext.databaseGroup,
  condition: { project.db.environment == "Local" },
  actions: [
      dbScript("$dbPath/drop_cm.ddl"),
    ]
  ],

  schema_cm_recreate: [
    task: true,
    description: "recreates the CM schema of the selected database",
    group: project.ext.databaseGroup,
    actions: [
      dbScript("$dbPath/drop_cm.ddl"),
      dbScript("$dbPath/schema/create_cm.tbl"),
      dbScript("$dbPath/data/common/update_cms_asset_pax_or_admin_A.sql"),
      dbScript("$dbPath/data/common/update_cms_asset_pax_or_admin_P.sql")
    ]
  ],

  schema_delete_local: [
    task: true,
    description: "deletes the local database schema",
    group: project.ext.databaseGroup,
    condition: { project.db.environment == "Local" },
    actions: [
      dbScript("$dbPath/drop.ddl"),
      dbScript("$dbPath/drop_nom_rec_common.ddl"),
      dbScript("$dbPath/drop_ref_pc_common.ddl"),
      dbScript("$dbPath/drop_ref_rec_common.ddl"),
      dbScript("$dbPath/drop_nomination.ddl"),
      dbScript("$dbPath/drop_product_claim.ddl"),
      dbScript("$dbPath/drop_quiz.ddl"),
      dbScript("$dbPath/drop_survey.ddl"),
      dbScript("$dbPath/drop_recognition.ddl"),
      dbScript("$dbPath/drop_engagement.ddl"),
      dbScript("$dbPath/drop_ssi.ddl"),
      dbScript("$dbPath/drop_goalquest.ddl"),
      dbScript("$dbPath/drop_challengepoint.ddl"),
      dbScript("$dbPath/drop_thankqonline.ddl"),
      dbScript("$dbPath/drop_wellness.ddl"),
      dbScript("$dbPath/drop_throwdown.ddl")
    ]
  ],

  schema_execution_log_recreate: [
    task: false,
    description: "recreates the execution log tables",
    group: project.ext.databaseGroup,
    actions: [
      dbScript("$dbPath/drop_execution_log.ddl"),
      dbScript("$dbPath/schema/create_core_execution_log.tbl")
    ]
  ],

  schema_engagement_recreate: [
    task: true,
    description: "recreates the engagement schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.engagementInstalled },
    actions: [
      dbScript("$dbPath/drop_engagement.ddl"),
      dbScript("$dbPath/schema/create_eng_promo_engagement_promotions.tbl"),
      dbScript("$dbPath/schema/create_eng_promo_engagement.tbl"),
      dbScript("$dbPath/schema/create_eng_engagement_log.tbl"),
      dbScript("$dbPath/schema/create_eng_promo_engagement_rules.tbl"),
      dbScript("$dbPath/schema/create_eng_promo_engagement_rules_audience.tbl"),
      dbScript("$dbPath/schema/create_eng_engagement_score_summary.tbl"),
      dbScript("$dbPath/schema/create_eng_engagement_score_detail.tbl"),
      dbScript("$dbPath/schema/create_eng_engagement_behavior_summary.tbl"),
      dbScript("$dbPath/schema/create_eng_engagement_chart.tbl"),
      dbScript("$dbPath/schema/create_eng_user_connected_to_from.tbl"),
      dbScript("$dbPath/schema/create_eng_stage_score_detail.tbl"),
      dbScript("$dbPath/plsql/pkg_engagement.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_engagement_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_delete_engagement_promo.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  schema_goalquest_recreate: [
    task: true,
    description: "recreates the goalquest schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.goalquestInstalled },
    actions: [
      dbScript("$dbPath/drop_goalquest.ddl"),
      dbScript("$dbPath/drop_challengepoint.ddl"),
      dbScript("$dbPath/schema/create_GQ_rpt_goal_selection_summary.tbl"),
      dbScript("$dbPath/schema/create_gq_promo_goalquest.tbl"),
      dbScript("$dbPath/schema/create_gq_goalquest_goallevel.tbl"),
      dbScript("$dbPath/schema/create_gq_paxgoal.tbl"),
      dbScript("$dbPath/schema/create_gq_goalquest_participant_activity.tbl"),
      dbScript("$dbPath/schema/create_gq_participant_partner.tbl"),
      dbScript("$dbPath/schema/create_GQ_rpt_goal_ROI.tbl"),
      dbScript("$dbPath/schema/create_gq_rpt_goal_partner.tbl"),

      dbScript("$dbPath/plsql/prc_insert_stg_gq_base_data.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_gq_goal_data.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_gq_progress_dat.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_gq_vin_nbr.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_gq_base_data_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_gq_goal_data_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_gq_progress_data_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_gq_vin_nbr_import.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_goalquest.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_rpt_goal_partner.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_rpt_repository_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_getGQPaxMiniProfile.sql", CREATE_REPLACE_PLSQL),

      dbScript("$dbPath/schema/create_cp_promo_challengepoint.tbl"),
      dbScript("$dbPath/schema/create_cp_challengepoint_award.tbl"),
      dbScript("$dbPath/schema/create_cp_challengepoint_progress.tbl"),
      dbScript("$dbPath/schema/create_cp_rpt_cp_selection_summary.tbl"),
      dbScript("$dbPath/schema/create_cp_rpt_cp_production.tbl"),
      dbScript("$dbPath/plsql/prc_insert_stg_cp_base_data.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_cp_data.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_cp_progress_dat.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_cp_base_data_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_cp_data_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_cp_progress_data_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_challangepoint.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  schema_leaderboard_recreate: [
    task: true,
    description: "recreates the leaderboard schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.leaderboardInstalled },
    actions: [
      dbScript("$dbPath/drop_leaderboard.ddl"),
      dbScript("$dbPath/schema/create_lb_leaderboard.tbl"),
      dbScript("$dbPath/schema/create_lb_leaderboard_participant.tbl"),
      dbScript("$dbPath/schema/create_lb_leaderboard_pax_activity.tbl"),
      dbScript("$dbPath/schema/create_lb_stage_leaderboard_load.tbl"),
      dbScript("$dbPath/plsql/prc_lb_stg_leaderboard_insert.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_lb_stage_leaderboard_load.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  _schema_nom_rec_common_recreate: [
    task: false,
    actions: [
      dbScript("$dbPath/drop_nom_rec_common.ddl"),
      dbScript("$dbPath/schema/create_nom_rec_odd_cast_category.tbl"),
      dbScript("$dbPath/schema/create_nom_rec_card.tbl"),
      dbScript("$dbPath/schema/create_nom_rec_odd_cast_card.tbl"),
      dbScript("$dbPath/schema/create_nom_rec_ecard.tbl"),
      dbScript("$dbPath/schema/create_nom_rec_ecard_locale.tbl"),
      dbScript("$dbPath/schema/create_nom_rec_promo_behavior.tbl"),
      dbScript("$dbPath/schema/create_nom_rec_promo_card.tbl")
    ]
  ],

  _schema_nomination_recreate: [
    task: false,
    condition: { project.nominationInstalled },
    actions: [
      dbScript("$dbPath/drop_nomination.ddl"),
      dbScript("$dbPath/schema/create_nom_promo_nomination.tbl"),
      dbScript("$dbPath/schema/create_nom_promo_nomination_level.tbl"),
      dbScript("$dbPath/schema/create_nom_promo_time_period.tbl"),
      dbScript("$dbPath/schema/create_nom_promo_wizard_order.tbl"),
      dbScript("$dbPath/schema/create_nom_nomination_claim.tbl"),
      dbScript("$dbPath/schema/create_nom_nomination_claim_behaviors.tbl"),
      dbScript("$dbPath/schema/create_nom_approver_option.tbl"),
      dbScript("$dbPath/schema/create_approver_criteria.tbl"),
      dbScript("$dbPath/schema/create_approver.tbl"),
      dbScript("$dbPath/schema/create_stage_nom_approvers_import.tbl"),
      dbScript("$dbPath/schema/create_tmp_winner_nomination_detail.tbl"),
      dbScript("$dbPath/schema/create_tmp_nomination_window_modal_dl.tbl"),
      dbScript("$dbPath/schema/create_tmp_approver_nomi_detail.tbl"),
      dbScript("$dbPath/schema/create_tmp_approver_cumul_nomi_detail.tbl"),
      dbScript("$dbPath/schema/create_tmp_winner_nomination_summary.tbl"),
      dbScript("$dbPath/schema/temp_hier_level_nodes.tbl"),
      dbScript("$dbPath/plsql/pkg_report_nomination.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_nom_approver_verify_import.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stage_nom_approver_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stg_nom_approver_insert.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_list_pend_nominations.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_refresh_pend_nom_approver.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_custom_approver_list_cas.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_nomination.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_nomi_aging.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_list_past_win_nominations.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_nomination_modal_window.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_nominations_my_win_list.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_list_nom_inprogress.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_nom_more_info_page.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_nomination_certificate_dtl.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  schema_nomination_recreate: [
    task: true,
    description: "recreates the nomination schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.nominationInstalled },
    actions: [
      dbSublist("_schema_nom_rec_common_recreate"),
      dbSublist("_schema_nomination_recreate"),
    ]
  ],

  _schema_ref_pc_common_recreate: [
    task: false,
    actions: [
      dbScript("$dbPath/drop_ref_pc_common.ddl"),
      dbScript("$dbPath/schema/create_pc_product.tbl"),
      dbScript("$dbPath/schema/create_pc_product_characteristic.tbl"),
      dbScript("$dbPath/schema/create_pc_claim_product.tbl"),
      dbScript("$dbPath/schema/create_pc_claim_product_characteristic.tbl")
    ]
  ],

  _schema_productclaim_recreate: [
    task: false,
    condition: { project.productClaimInstalled },
    actions: [
      dbScript("$dbPath/drop_product_claim.ddl"),
      dbScript("$dbPath/schema/create_pc_product_claim.tbl"),
      dbScript("$dbPath/schema/create_pc_minimum_qualifier_status.tbl"),
      dbScript("$dbPath/schema/create_pc_promo_product_claim.tbl"),
      dbScript("$dbPath/schema/create_pc_promo_payout_group.tbl"),
      dbScript("$dbPath/schema/create_pc_promo_payout.tbl"),
      dbScript("$dbPath/schema/create_pc_promo_team_position.tbl"),
      dbScript("$dbPath/schema/create_pc_rpt_claim_product.tbl"),
      dbScript("$dbPath/schema/create_pc_rpt_claim_product_summary.tbl"),
      dbScript("$dbPath/schema/create_pc_promo_stack_rank_payout_group.tbl"),
      dbScript("$dbPath/schema/create_pc_promo_stack_rank_payout.tbl"),
      dbScript("$dbPath/plsql/fnc_get_category_id.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_productchar_id.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_getsubcategory_id.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_claim.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_product_claim.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stage_product_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_product.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stage_prd_claim.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stack_ranking.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  schema_productclaim_recreate: [
    task: true,
    description: "recreates the product claim schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.productClaimInstalled },
    actions: [
      dbSublist("_schema_ref_pc_common_recreate"),
      dbSublist("_schema_productclaim_recreate"),
    ]
  ],

  schema_quiz_recreate: [
    task: true,
    description: "recreates the quiz schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.quizInstalled },
    actions: [
      dbScript("$dbPath/drop_quiz.ddl"),
      dbScript("$dbPath/schema/create_quiz.tbl"),
      dbScript("$dbPath/schema/create_quiz_question.tbl"),
      dbScript("$dbPath/schema/create_quiz_question_answer.tbl"),
      dbScript("$dbPath/schema/create_quiz_claim.tbl"),
      dbScript("$dbPath/schema/create_quiz_response.tbl"),
      dbScript("$dbPath/schema/create_quiz_promo_quiz.tbl"),
      dbScript("$dbPath/schema/create_quiz_rpt_quiz_activity_summary.tbl"),
      dbScript("$dbPath/schema/create_quiz_rpt_quiz_analysis.tbl"),
      dbScript("$dbPath/schema/create_quiz_quiz_learning_object.tbl"),
      dbScript("$dbPath/schema/create_quiz_claim_item.tbl"),
      dbScript("$dbPath/schema/create_diy_quiz.tbl"),
      dbScript("$dbPath/schema/create_diy_quiz_participant.tbl"),
      dbScript("$dbPath/plsql/pkg_quiz_reports.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_quiz_analysis_reports.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  _schema_ref_rec_common_recreate: [
    task: false,
    actions: [
      dbScript("$dbPath/drop_ref_rec_common.ddl"),
      dbScript("$dbPath/schema/create_core_merch_order.tbl"),
      dbScript("$dbPath/schema/create_core_merch_order_bill_code.tbl"),
      dbScript("$dbPath/schema/create_core_activitymerchorder.tbl")
    ]
  ],

  _schema_recognition_recreate: [
    task: false,
    condition: { project.recognitionInstalled },
    actions: [
      dbScript("$dbPath/drop_recognition.ddl"),
      dbScript("$dbPath/schema/create_rec_recognition_claim.tbl"),
      dbScript("$dbPath/schema/create_rec_promo_recognition.tbl"),
      dbScript("$dbPath/schema/create_rec_scheduled_recognition.tbl"),
      dbScript("$dbPath/schema/create_rec_promo_certificate.tbl"),
      dbScript("$dbPath/schema/create_rec_promo_home_page_item.tbl"),
      dbScript("$dbPath/schema/create_rec_rpt_award_item_activity.tbl"),
      dbScript("$dbPath/schema/create_rec_rpt_program_reference.tbl"),
      dbScript("$dbPath/schema/create_rec_tmp_award_activity_rpt.tbl"),
      dbScript("$dbPath/schema/create_rec_tmp_program_reference_rpt.tbl"),
      dbScript("$dbPath/schema/create_rec_rpt_award_item_selection.tbl"),
      dbScript("$dbPath/schema/create_rec_rpt_award_order_detail.tbl"),
      dbScript("$dbPath/schema/create_rec_tmp_award_item_selection_rpt.tbl"),
      dbScript("$dbPath/schema/create_rec_rpt_award_earnings.tbl"),
      dbScript("$dbPath/schema/create_rec_tmp_nonredemption_reminder.tbl"),
      dbScript("$dbPath/schema/create_rec_promo_budget_sweep.tbl"),

      dbScript("$dbPath/schema/create_rec_purl_recipient.tbl"),
      dbScript("$dbPath/schema/create_rec_purl_recipient_cfse.tbl"),
      dbScript("$dbPath/schema/create_rec_purl_contributor.tbl"),
      dbScript("$dbPath/schema/create_rec_purl_contributor_media.tbl"),
      dbScript("$dbPath/schema/create_rec_purl_contributor_comment.tbl"),
      dbScript("$dbPath/schema/create_rec_rpt_purl_contribution_detail.tbl"),
      dbScript("$dbPath/schema/create_rec_rpt_purl_contributioin_summary.tbl"),
      dbScript("$dbPath/schema/create_rec_rpt_purl_recipient.tbl"),

      dbScript("$dbPath/plsql/prc_stg_award_record_insert.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stage_award_level_load.sql", CREATE_REPLACE_PLSQL),

      dbScript("$dbPath/plsql/prc_rpt_awrd_ord_dtl_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_rpt_award_earnings_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_populate_reminder_email.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_recog_counts.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_list_recognition_wall.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_purl_celebration_page.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  schema_recognition_recreate: [
    task: true,
    description: "recreates the recognition schema in the database selected",
    group: project.ext.databaseGroup,
    actions: [
      dbSublist("_schema_nom_rec_common_recreate"),
      dbSublist("_schema_ref_rec_common_recreate"),
      dbSublist("_schema_recognition_recreate")
    ]
  ],

  schema_ssi_recreate: [
    task: true,
    description: "recreates the ssi schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.ssiInstalled },
    actions: [
      dbScript("$dbPath/drop_ssi.ddl"),
      dbScript("$dbPath/schema/create_ssi_promo_ssi.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_atn.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_document.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_approver.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_manager.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_participant.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_activity.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_pax_progress.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_level.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_pax_stack_rank.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_sr_payout.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_claim_field.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_pax_claim.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_pax_claim_field.tbl"),
      dbScript("$dbPath/schema/create_ssi_gtt.tbl"),
      dbScript("$dbPath/schema/create_gtt_node_and_below.tbl"),
      dbScript("$dbPath/schema/create_ssi_stage_pax_progress.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_pax_payout.tbl"),
      dbScript("$dbPath/schema/create_rpt_ssi_contest_detail.tbl"),
      dbScript("$dbPath/schema/create_stage_ssi_import.tbl"),
      dbScript("$dbPath/schema/rpt_ssi_award_detail.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_bill_code.sql"),
      dbScript("$dbPath/schema/create_ssi_admin_actions.tbl"),
      dbScript("$dbPath/schema/create_ssi_contest_superviewer.tbl"),
      dbScript("$dbPath/plsql/prc_rpt_ssi_award_detail.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_ssi_contest.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_ssi_contest_data.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_ssi_progress_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_report_ssi_contest.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_pkg_ssi_atn_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_pkg_ssi_dtgt_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_pkg_ssi_objective_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_pkg_ssi_stack_rank_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_pkg_ssi_step_it_up_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_ssi_contest_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_ssi_err_record_extract.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  schema_survey_recreate: [
    task: true,
    description: "recreates the survey schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.surveyInstalled },
    actions: [
      dbScript("$dbPath/drop_survey.ddl"),
      dbScript("$dbPath/schema/create_survey.tbl"),
      dbScript("$dbPath/schema/create_svy_promo_survey.tbl"),
      dbScript("$dbPath/schema/create_survey_question.tbl"),
      dbScript("$dbPath/schema/create_survey_question_response.tbl"),
      dbScript("$dbPath/schema/create_participant_survey.tbl"),
      dbScript("$dbPath/schema/create_participant_survey_response.tbl"),
      dbScript("$dbPath/schema/create_promo_goalquest_survey.tbl"),
      dbScript("$dbPath/schema/create_svy_rpt.tbl"),
      dbScript("$dbPath/plsql/pkg_report_survey.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  schema_throwdown_recreate: [
    task: true,
    description: "recreates the throwdown schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.throwdownInstalled },
    actions: [
      dbScript("$dbPath/drop_throwdown.ddl"),
      dbScript("$dbPath/schema/create_td_promo_throwdown.tbl"),
      dbScript("$dbPath/schema/create_td_division.tbl"),
      dbScript("$dbPath/schema/create_td_division_payout.tbl"),
      dbScript("$dbPath/schema/create_td_division_audience.tbl"),
      dbScript("$dbPath/schema/create_td_round.tbl"),
      dbScript("$dbPath/schema/create_td_team.tbl"),
      dbScript("$dbPath/schema/create_td_match.tbl"),
      dbScript("$dbPath/schema/create_td_matchteam_outcome.tbl"),
      dbScript("$dbPath/schema/create_td_matchteam_outcome_progress.tbl"),
      dbScript("$dbPath/schema/create_td_smacktalk_comment.tbl"),
      dbScript("$dbPath/schema/create_td_smacktalk_like.tbl"),
      dbScript("$dbPath/schema/create_td_stack_rank.tbl"),
      dbScript("$dbPath/schema/create_td_stack_rank_node.tbl"),
      dbScript("$dbPath/schema/create_td_stack_rank_participant.tbl"),
      dbScript("$dbPath/schema/create_td_promo_stack_rank_payout_group.tbl"),
      dbScript("$dbPath/schema/create_td_promo_stack_rank_payout.tbl"),
      dbScript("$dbPath/schema/create_td_stage_td_progress_data_import.tbl"),
      dbScript("$dbPath/schema/create_td_rpt_throwdown_activity.tbl"),
      dbScript("$dbPath/plsql/pkg_report_throwdown.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_td_progress_data_load.sql", CREATE_REPLACE_PLSQL)
    ]
  ],

  schema_wellness_recreate: [
    task: true,
    description: "recreates the welness schema in the database selected",
    group: project.ext.databaseGroup,
    condition: { project.wellnessInstalled },
    actions: [
      dbScript("$dbPath/drop_wellness.ddl"),
      dbScript("$dbPath/schema/create_well_promo_wellness.tbl")
    ]
  ],

  schema_purge_recyclebin: [
    task: false,
    actions: [
      dbScript("$dbPath/purge_recyclebin.ddl")
    ]
  ],

  schema_recreate: [
    task: true,
    description: "recreates the project schema in the database selected",
    group: project.ext.databaseGroup,
    actions: [
      // first, drop all the tables except CM
      dbScript("$dbPath/drop.ddl"),

      // next, create all the tables (in this order)
      dbScript("$dbPath/schema/create_tmp_budget_transfer.tbl"),
      dbScript("$dbPath/schema/create_core_supplier.tbl"),
      dbScript("$dbPath/schema/create_core_currency.tbl"),
      dbScript("$dbPath/schema/create_core_country.tbl"),
      dbScript("$dbPath/schema/create_core_country_suppliers.tbl"),
      dbScript("$dbPath/schema/create_core_ospropertyset.tbl"),
      dbScript("$dbPath/schema/create_core_role.tbl"),
      dbScript("$dbPath/schema/create_core_user.tbl"),
      dbScript("$dbPath/schema/create_core_user_tnc_history.tbl"),
      dbScript("$dbPath/schema/create_core_acl.tbl"),
      dbScript("$dbPath/schema/create_core_user_acl.tbl"),
      dbScript("$dbPath/schema/create_core_hierarchy.tbl"),
      dbScript("$dbPath/schema/create_core_user_role.tbl"),
      dbScript("$dbPath/schema/create_core_user_type_role.tbl"),
      dbScript("$dbPath/schema/create_core_pli.ddl"),
      dbScript("$dbPath/schema/create_core_characteristic.tbl"),
      dbScript("$dbPath/schema/create_core_user_address.tbl"),
      dbScript("$dbPath/schema/create_core_user_phone.tbl"),
      dbScript("$dbPath/schema/create_core_user_characteristic.tbl"),
      dbScript("$dbPath/schema/create_core_node_type.tbl"),
      dbScript("$dbPath/schema/create_core_node.tbl"),
      dbScript("$dbPath/schema/create_core_usernode.tbl"),
      dbScript("$dbPath/schema/create_core_user_facebook.tbl"),
      dbScript("$dbPath/schema/create_core_user_twitter.tbl"),
      dbScript("$dbPath/schema/create_core_usernode_history.tbl"),
      dbScript("$dbPath/schema/create_core_email_address.tbl"),
      dbScript("$dbPath/schema/create_core_employer.tbl"),
      dbScript("$dbPath/schema/create_core_hierarchynode.tbl"),
      dbScript("$dbPath/schema/create_core_hierarchy_node_type.tbl"),
      dbScript("$dbPath/schema/create_core_participant.tbl"),
      dbScript("$dbPath/schema/create_core_participant_employer.tbl"),
      dbScript("$dbPath/schema/create_core_participant_contact_methods.tbl"),
      dbScript("$dbPath/schema/create_core_node_characteristic.tbl"),
      dbScript("$dbPath/schema/create_core_participant_aboutme.tbl"),
      dbScript("$dbPath/schema/create_core_budget.tbl"),
      dbScript("$dbPath/schema/create_core_mock_account_transaction.tbl"),
      dbScript("$dbPath/schema/create_core_claim_form.tbl"),
      dbScript("$dbPath/schema/create_core_claim_form_step.tbl"),
      dbScript("$dbPath/schema/create_core_claim_form_step_element.tbl"),
      dbScript("$dbPath/schema/create_core_claim_form_step_email.tbl"),
      dbScript("$dbPath/schema/create_core_audience.tbl"),
      dbScript("$dbPath/schema/create_core_audience_criteria.tbl"),
      dbScript("$dbPath/schema/create_core_audience_criteria_char.tbl"),
      dbScript("$dbPath/schema/create_core_participant_audience.tbl"),
      dbScript("$dbPath/schema/create_core_claim.tbl"),
      dbScript("$dbPath/schema/create_core_claim_item.tbl"),
      dbScript("$dbPath/schema/create_core_claim_item_approver.tbl"),
      dbScript("$dbPath/schema/create_core_claim_approver_snapshot.tbl"),
      dbScript("$dbPath/schema/create_core_claim_cfse.tbl"),
      dbScript("$dbPath/schema/create_core_calculator.tbl"),
      dbScript("$dbPath/schema/create_core_calculator_criterion.tbl"),
      dbScript("$dbPath/schema/create_core_calculator_criterion_rating.tbl"),
      dbScript("$dbPath/schema/create_core_calculator_payout.tbl"),
      dbScript("$dbPath/schema/create_core_calculator_response.tbl"),
      dbScript("$dbPath/schema/create_core_promotion.tbl"),
      dbScript("$dbPath/schema/create_core_promo_approval_option.tbl"),
      dbScript("$dbPath/schema/create_core_promo_approval_option_reason.tbl"),
      dbScript("$dbPath/schema/create_core_promo_approval_participant.tbl"),
      dbScript("$dbPath/schema/create_core_promo_audience.tbl"),
      dbScript("$dbPath/schema/create_core_promo_cfse_validation.tbl"),
      dbScript("$dbPath/schema/create_core_promo_notification.tbl"),
      dbScript("$dbPath/schema/create_core_promo_sweepstake_drawing.tbl"),
      dbScript("$dbPath/schema/create_core_promo_sweepstake_winners.tbl"),
      dbScript("$dbPath/schema/create_core_proxy.tbl"),
      dbScript("$dbPath/schema/create_core_proxy_module.tbl"),
      dbScript("$dbPath/schema/create_core_proxy_module_promotion.tbl"),
      dbScript("$dbPath/schema/create_core_claim_participant.tbl"),
      dbScript("$dbPath/schema/create_core_journal.tbl"),
      dbScript("$dbPath/schema/create_core_journal_bill_code.tbl"),
      dbScript("$dbPath/schema/create_core_claim_group.tbl"),
      dbScript("$dbPath/schema/create_core_stack_rank.tbl"),
      dbScript("$dbPath/schema/create_core_stack_rank_node.tbl"),
      dbScript("$dbPath/schema/create_core_stack_rank_participant.tbl"),
      dbScript("$dbPath/schema/create_promo_merch_country.tbl"),
      dbScript("$dbPath/schema/create_promo_merch_program_level.tbl"),
      dbScript("$dbPath/schema/create_core_activity.tbl"),
      dbScript("$dbPath/schema/create_core_activityjournal.tbl"),
      dbScript("$dbPath/schema/create_core_payout_calculation_audit.tbl"),
      dbScript("$dbPath/schema/create_core_import_file.tbl"),
      dbScript("$dbPath/schema/create_core_import_record.tbl"),
      dbScript("$dbPath/schema/create_core_stage_budget_import_record.tbl"),
      dbScript("$dbPath/schema/create_core_stage_deposit_import_record.tbl"),
      dbScript("$dbPath/schema/create_core_stage_hierarchy_import_record.tbl"),
      dbScript("$dbPath/schema/create_core_stage_pax_import_record.tbl"),
      dbScript("$dbPath/schema/create_core_stage_product_import_record.tbl"),
      dbScript("$dbPath/schema/create_core_stage_quiz_import_record.tbl"),
      dbScript("$dbPath/schema/create_GQ_stage_gq_base_data_import.tbl"),
      dbScript("$dbPath/schema/create_GQ_stage_gq_goal_data_import.tbl"),
      dbScript("$dbPath/schema/create_GQ_stage_gq_progress_data_import.tbl"),
      dbScript("$dbPath/schema/create_GQ_stage_gq_vin_nbr_import.tbl"),
      dbScript("$dbPath/schema/create_core_import_record_error.tbl"),
      dbScript("$dbPath/schema/create_core_message.tbl"),
      dbScript("$dbPath/schema/create_core_participant_communication_preferences.tbl"),
      dbScript("$dbPath/schema/create_vue_stage_core_role_owners.tbl"),
      dbScript("$dbPath/schema/create_core_comm_log.tbl"),
      dbScript("$dbPath/schema/create_core_comm_log_comment.tbl"),
      dbScript("$dbPath/schema/create_core_mailing_batch.tbl"),
      dbScript("$dbPath/schema/create_core_mailing.tbl"),
      dbScript("$dbPath/schema/create_core_mailing_attachment_info.tbl"),
      dbScript("$dbPath/schema/create_core_mailing_recipient.tbl"),
      dbScript("$dbPath/schema/create_core_mailing_recipient_data.tbl"),
      dbScript("$dbPath/schema/create_core_mailing_message_locale.tbl"),
      dbScript("$dbPath/schema/create_core_process.tbl"),
      dbScript("$dbPath/schema/create_core_process_role.tbl"),
      dbScript("$dbPath/schema/create_core_process_invocation.tbl"),
      dbScript("$dbPath/schema/create_core_process_invocation_parameter.tbl"),
      dbScript("$dbPath/schema/create_core_process_invocation_parameter_value.tbl"),
      dbScript("$dbPath/schema/create_core_process_invocation_comment.tbl"),
      dbScript("$dbPath/schema/create_core_quartz_tables.tbl"),
      dbScript("$dbPath/schema/create_core_dev_quartz_tables.tbl"),
      dbScript("$dbPath/schema/create_calendar_table.tbl"),
      dbScript("$dbPath/schema/create_calendar_table_insert.sql"),
      dbScript("$dbPath/schema/stage_prd_claim_import_record.tbl"),
      dbScript("$dbPath/schema/create_pc_stage_product_claim_import_record.tbl"),
      dbScript("$dbPath/schema/create_pc_stage_product_claim_import_product_record.tbl"),
      dbScript("$dbPath/schema/create_pc_stage_product_claim_import_field_record.tbl"),
      dbScript("$dbPath/schema/create_core_tmp_audience_user_id.tbl"),
      dbScript("$dbPath/schema/create_core_tmp_dependent_hierarchy.tbl"),
      dbScript("$dbPath/schema/create_core_tmp_duplicate_participant.tbl"),
      dbScript("$dbPath/schema/create_login_activity.tbl"),
      dbScript("$dbPath/schema/create_welcome_message.tbl"),
      dbScript("$dbPath/schema/create_welcome_message_audience.tbl"),
      dbScript("$dbPath/schema/create_core_context.sql"),
      dbScript("$dbPath/schema/create_core_encryption_conversion.sql"),
      dbScript("$dbPath/schema/create_core_external_table.sql"),
      dbScript("$dbPath/schema/create_vw_curr_pax_employer.sql"),
      dbScript("$dbPath/schema/create_vw_cms_asset_value.sql"),
      dbScript("$dbPath/schema/create_vw_cms_code_value.sql"),
      dbScript("$dbPath/schema/create_core_user_country_changes.tbl"),
      dbScript("$dbPath/schema/create_core_badge.tbl"),
      dbScript("$dbPath/schema/create_core_badge_rule.tbl"),
      dbScript("$dbPath/schema/create_core_badge_promotion.tbl"),
      dbScript("$dbPath/schema/create_core_participant_badge.tbl"),
      dbScript("$dbPath/schema/create_core_public_recognition_like.tbl"),
      dbScript("$dbPath/schema/create_core_public_recognition_user_connections.tbl"),
      dbScript("$dbPath/schema/create_core_public_recognition_comment.tbl"),
      dbScript("$dbPath/schema/create_core_participant_followers.tbl"),
      dbScript("$dbPath/schema/create_core_stage_badge_load.tbl"),
      dbScript("$dbPath/schema/create_core_participant_alert.tbl"),
      dbScript("$dbPath/schema/create_core_alert_message.tbl"),
      dbScript("$dbPath/schema/create_core_gtt_pax_audience.tbl"),
      dbScript("$dbPath/schema/create_core_file_store.tbl"),
      dbScript("$dbPath/schema/create_core_strongmail_user.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_locale_date_pattern.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_participant_employer.tbl"),
      dbScript("$dbPath/schema/create_instant_poll.tbl"),
      dbScript("$dbPath/schema/create_instant_poll_audience.tbl"),
      dbScript("$dbPath/schema/create_core_audience_role.tbl"),
      dbScript("$dbPath/schema/create_core_diy_communications.tbl"),
      dbScript("$dbPath/schema/create_celebration_manager_message.tbl"),
      dbScript("$dbPath/schema/create_core_gtt_ex_pax_audience.tbl"),
      dbScript("$dbPath/schema/create_core_stage_inactive_budget_rd.tbl"),
      dbScript("$dbPath/schema/create_core_promo_bill_code.tbl"),
      dbScript("$dbPath/schema/create_wh_workhappier.tbl"),
      dbScript("$dbPath/schema/create_participant_group.tbl"),
      dbScript("$dbPath/schema/create_participant_group_dtls.tbl"),
      dbScript("$dbPath/schema/create_gtt_id_list.tbl"),
      dbScript("$dbPath/schema/create_post_process_jobs.tbl"),
      dbScript("$dbPath/schema/create_post_process_payout_calc.tbl"),
      dbScript("$dbPath/schema/create_gtt_manager_id.tbl"),
      dbScript("$dbPath/schema/create_core_video_upload_detail.tbl"),

      // Core Forum tables
      dbScript("$dbPath/schema/create_core_forum_topic.tbl"),
      dbScript("$dbPath/schema/create_core_forum_topic_audience.tbl"),
      dbScript("$dbPath/schema/create_core_forum_discussion.tbl"),
      dbScript("$dbPath/schema/create_core_forum_discussion_like.tbl"),

      // Core Award Generator tables
      dbScript("$dbPath/schema/create_core_awardgenerator.tbl"),
      dbScript("$dbPath/schema/create_core_awardgen_award.tbl"),
      dbScript("$dbPath/schema/create_core_awardgen_batch.tbl"),
      dbScript("$dbPath/schema/create_core_awardgen_participant.tbl"),
      dbScript("$dbPath/schema/create_core_awardgen_manager.tbl"),

      // Core Report tables
      dbScript("$dbPath/schema/create_core_report.tbl"),
      dbScript("$dbPath/schema/create_core_report_chart.tbl"),
      dbScript("$dbPath/schema/create_core_report_dashboard.tbl"),
      dbScript("$dbPath/schema/create_core_report_dashboard_item.tbl"),
      dbScript("$dbPath/schema/create_core_report_parameter.tbl"),
      dbScript("$dbPath/schema/create_core_report_dashboard_item_param.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_hierarchy.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_hierarchy_summary.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_refresh_date.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_characteristic.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_pax_promo_eligibility.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_pax_promo_elig_stage.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_budget.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_characteristic_lookup.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_enrollment_detail.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_enrollment_summary.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_awardmedia_detail.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_awardmedia_summary.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_claim_summary.tbl"),
      dbScript("$dbPath/schema/create_core_tmp_report_ids.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_qcard.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_badge_detail.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_badge_summary.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_promo_node_activity.tbl"),
      dbScript("$dbPath/schema/create_extract_temp_table_session.tbl"),
      dbScript("$dbPath/schema/create_extract_characteristic_cms.tbl"),
      dbScript("$dbPath/schema/create_table_rpt_hierarchy_rollup.sql"),
      dbScript("$dbPath/schema/create_table_rpt_work_happier_detail.sql"),
      dbScript("$dbPath/schema/create_rpt_promo_elig_counts.tbl"),
      dbScript("$dbPath/schema/create_gtt_recog_report_count.tbl"),
      dbScript("$dbPath/schema/create_gtt_pax_report.tbl"),
      dbScript("$dbPath/schema/create_rpt_behavior_summary.tbl"),
      dbScript("$dbPath/schema/create_rpt_behavior_detail.tbl"),
      dbScript("$dbPath/schema/create_core_cash_currency_current.tbl"),
      dbScript("$dbPath/schema/create_core_cash_currency_history.tbl"),
      dbScript("$dbPath/schema/create_core_rpt_cash_budget.tbl"),

      // these need to be in Core section because they are used by the Pax Overall Activity Report
      dbScript("$dbPath/schema/create_core_rpt_claim_detail.tbl"),
      dbScript("$dbPath/schema/create_quiz_rpt_quiz_activity_details.tbl"),
      dbScript("$dbPath/schema/create_nom_rpt_nomination_detail.tbl"),
      dbScript("$dbPath/schema/create_rec_rpt_recognition_detail.tbl"),
      dbScript("$dbPath/schema/create_nom_rec_claim_recipient.tbl"),
      dbScript("$dbPath/schema/create_GQ_rpt_goal_selection_detail.tbl"),
      dbScript("$dbPath/schema/create_cp_rpt_cp_selection_detail.tbl"),
      dbScript("$dbPath/schema/create_rec_pax_address_book.tbl"),
      dbScript("$dbPath/schema/create_cp_rpt_cp_manager_override.tbl"),
      dbScript("$dbPath/schema/create_gq_rpt_goal_manager_override.tbl"),

      // Create Challengepoint related staging and reporting tables
      dbScript("$dbPath/schema/create_cp_stage_cp_base_data_import.tbl"),
      dbScript("$dbPath/schema/create_cp_stage_cp_data_import.tbl"),
      dbScript("$dbPath/schema/create_cp_stage_cp_progress_data_import.tbl"),

      // Create Award Level related staging table
      dbScript("$dbPath/schema/create_rec_stage_award_level_import.tbl"),

      // Create G3-Redo related new tables
      dbScript("$dbPath/schema/create_module_app.tbl"),
      dbScript("$dbPath/schema/create_module_app_audience.tbl"),
      dbScript("$dbPath/schema/create_filter_module_app.tbl"),

      // User Password history tables
      dbScript("$dbPath/schema/create_user_pwd_history.tbl"),

      // last, create the functions
      dbScript("$dbPath/plsql/fnc_locale_to_char_dt.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_locale_to_char_dt_time.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_locale_to_char_dt_time_sec.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_locale_to_date_dt.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_locale_to_date_dt_time.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_locale_to_date_dt_time_sec.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_pk_nextval.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_3des_decrypt.sql"),
      dbScript("$dbPath/plsql/fnc_3des_encrypt.sql"),
      dbScript("$dbPath/plsql/fnc_decrypt.sql"),
      dbScript("$dbPath/plsql/fnc_encrypt.sql"),
      dbScript("$dbPath/plsql/fnc_java_decrypt.sql"),
      dbScript("$dbPath/plsql/fnc_md5_hash.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_java_md5_hash.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_fnc_sha_hash.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_system_variable.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_system_var_boolean.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_getcharacteristic_id.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_cms_asset_code_value.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_cms_asset_code_val_extr.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_manager_node.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/schema/vue_characteristic_name.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_cms_picklist_code.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_node_id.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_node_path.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_roleid.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_installed_module.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_unix_call.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_pipe_parse.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_cms_state_code_value.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_fmt_achievement_nbr.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_dir_name.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_format_user_name.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_get_directory_file_name.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_import_record_error.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/schema/node_type_name.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_report_chart_period_nbr.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_user_name.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_final_appr_name.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_data_has_delimiter.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_is_date.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_is_number.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_osp_bool.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_execution_log_entry.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_hierarchy.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stg_pax_record_insert.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_budget.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_budget_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_refresh_t_and_c_audience.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_budget_verify_import.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_deposit.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_deposit_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_hierarchy_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_common.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_characteristic.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_enrollment.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_budget.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_work_happier.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_badge_date_count.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_badge_eligibility.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_badge.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_awardmedia.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_populate_promo_elig.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_insert_stg_prd_claim.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stack_ranking.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stage_prd_claim.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_core_rpt_enrollment_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_rpt_claims_submitted_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_mark_pax_plateau_award.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_user_characteristics.sql", CREATE_REPLACE_PLSQL),
      // Function to get badge count
      dbScript("$dbPath/plsql/fnc_get_rpt_badge_count.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_recognition.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_quiz_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_cms_picklist_value.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_bulk_pax_stage.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_hierarchy_verify_import.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_core_rpt_budget_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_participant_verify_import.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_hierarchy_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_aes256_encrypt.sql"),
      dbScript("$dbPath/plsql/fnc_aes256_decrypt.sql"),
      dbScript("$dbPath/plsql/fnc_generate_key.sql"),
      dbScript("$dbPath/plsql/fnc_java_encrypt.sql"),
      dbScript("$dbPath/plsql/fnc_java_decrypt.sql"),
      dbScript("$dbPath/plsql/fnc_generate_key_temp.sql"),
      dbScript("$dbPath/plsql/fnc_java_encrypt_temp.sql"),
      dbScript("$dbPath/plsql/fnc_java_decrypt_temp.sql"),
      dbScript("$dbPath/plsql/fnc_java_encrypt_old.sql"),
      dbScript("$dbPath/plsql/fnc_java_decrypt_old.sql"),
      dbScript("$dbPath/plsql/pkg_set_runtime_variable.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_generate_seed_data.sql"),
      dbScript("$dbPath/plsql/script_initial_key_setup.sql"),
      dbScript("$dbPath/plsql/fnc_cms_language_validation.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_clear_lob_space.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_report_refresh.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stage_badge_insert.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stage_badge_load.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_badge_verify_import.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_build_audience.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_promo_budget_meter.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_create_array.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_login_activity.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_rpt_promo_node_inactive.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_report_promo_node_activity.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_rpt_qcard_refresh.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_badge_count_by_user.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_extracts.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_badge_count_by_param.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_random_password.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_qrtz_post_process_job_del.sql", CREATE_REPLACE_PLSQL),

      dbScript("$dbPath/plsql/prc_welcome_email_prep.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_client_reports.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_delete_audience.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_lock_control_audience.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_budget_transfer.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_copy_cms_asset.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_badge_cms_value.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_manager_id.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_award_generator.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_award_generator_extract.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stg_inactive_budget_rd_ins.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_stage_inactive_budgets_rd.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_inactive_budget_rd.sql", CREATE_REPLACE_PLSQL),

      // online reports packages
      dbScript("$dbPath/plsql/fnc_check_promo_aud.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_awards_received.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_badge_activity.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_challengepoint.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_claims_activity.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_enroll_reports.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_goalquest.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_individual_activity.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_login_activity.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_nomi_given.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_nomi_receive.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_plateau_item_select.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_plateau_levels.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_quiz_activity.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_quiz_analysis.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_recog_given.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_recog_list_of_givers.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_recog_purl_activity.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_recog_received.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_recog_recipients.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_survey_reports.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_throwdown_activity.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_pkg_query_behaviors_report.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_pkg_report_behavior.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_point_budget_balance.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_query_cash_budget_balance.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_report_cash_budget.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_ccs_recognition_wall.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_ref_criteria_aud_inactive.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_const.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/pkg_rdsadmin_s3_tasks.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_instance.sql", CREATE_REPLACE_PLSQL),      
      dbScript("$dbPath/plsql/create_fnc_get_dir_name_like.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_fnc_format_comments_clob.sql", CREATE_REPLACE_PLSQL),      
      dbScript("$dbPath/schema/create_mv_cms_asset_value1.sql"),
      dbScript("$dbPath/schema/create_mv_cms_asset_value2.sql"),
      dbScript("$dbPath/schema/create_mv_cms_asset_value3.sql"),
      dbScript("$dbPath/schema/create_mv_cms_code_value.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/schema/create_mv_cms_user_characteristic.sql", CREATE_REPLACE_PLSQL),

      // mobile recognition app
      dbScript("$dbPath/schema/create_core_user_device.tbl"),

      // G6
      dbScript("$dbPath/plsql/create_prc_get_user_info.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_get_userid_by_phone.sql", CREATE_REPLACE_PLSQL),      
      dbScript("$dbPath/schema/create_mv_cms_badge.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_osp_int.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_get_manager_node.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_purge_old_log_data.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_get_user_contact_info.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_g_to_honeycomb_sync.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_fnc_biw_only_role.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_get_user_autocomp_by_phone.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_get_user_autocomp_by_email.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_prc_get_user_contact_by_phone.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_fnc_format_comments.sql", CREATE_REPLACE_PLSQL),
      dbJava("$dbPath/plsql/create_randomuuid_java.sql",CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/create_fnc_randomuuid.sql", CREATE_REPLACE_PLSQL),

      // Java functions for date time patterns
      dbScript("$dbPath/plsql/fnc_java_get_date_pattern.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_java_get_date_time_pattern.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_java_get_dt_time_sec_ptrn.sql", CREATE_REPLACE_PLSQL),
      
      // Pax Account Authorization DB data script
      dbScript("$dbPath/plsql/create_prc_pax_acc_auth_script.sql", CREATE_REPLACE_PLSQL),
      
      // Load environment specific plsql objects
      dbSublist("_load_env_specific_plsql_objects"),
      
      // InActivate BIW Users Process procedure
      dbScript("$dbPath/plsql/prc_inactivate_biw_users.sql", CREATE_REPLACE_PLSQL),      
      
      // RA procedures
      dbScript("$dbPath/plsql/prc_ra_reminder.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_ra_nh_reminder.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_ra_od_reminder.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/fnc_ra_actions.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_ra_promo_level.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_ra_welcome_email.sql", CREATE_REPLACE_PLSQL),
      dbScript("$dbPath/plsql/prc_ra_unused_bud_details.sql", CREATE_REPLACE_PLSQL),
      
      //BudgetTransfer procedure
      dbScript("$dbPath/plsql/prc_budget_move_transfer.sql", CREATE_REPLACE_PLSQL),

      // Schema for installed modules (each with its own condition(s))
      dbSublist("_schema_nom_rec_common_recreate"),
      dbSublist("_schema_ref_rec_common_recreate"),
      dbSublist("_schema_ref_pc_common_recreate"),
      dbSublist("_schema_nomination_recreate"),
      dbSublist("_schema_recognition_recreate"),
      dbSublist("schema_engagement_recreate"),
      dbSublist("_schema_productclaim_recreate"),
      dbSublist("schema_quiz_recreate"),
      dbSublist("schema_goalquest_recreate"),
      dbSublist("schema_survey_recreate"),
      dbSublist("schema_wellness_recreate"),
      dbSublist("schema_leaderboard_recreate"),
      dbSublist("schema_throwdown_recreate"),
      dbSublist("schema_ssi_recreate"),

      // Keep this last. This reclaims resources after a drop
      dbSublist("schema_purge_recyclebin")
    ]
  ]
]

project.ext.dbTaskConfigList << [
  create_replace_plsql_delivery: [
    task: true,
    description: "Run client custom plsql scripts",
    group: project.ext.databaseGroup,
    actions: [
      // Begin client custom plsql scripts
      // Example:
      //dbScript("$dbPath/plsql/client/custom_procedure_1.sql"),
      //dbScript("$dbPath/plsql/client/custom_procedure_2.sql")
      
      
      // End client custom plsql scripts
    ]
  ],
  
  create_replace_plsql_all: [
    task: true,
    description: "Run all plsql scripts",
    group: project.ext.databaseGroup,
    actions: [
      dbSublist("create_replace_plsql"),
      dbSublist("create_replace_plsql_delivery")
    ]
  ]
]

project.ext.dbTaskConfigList << [
  database_recreate_schema: [
    task: true,
    description: "Conventional part of database recreate - old create and insert scripts",
    group: project.ext.zapGroup,
    actions: [
      // Confirm db recreate
      dbClosure({
        if(!promptForConfirmation("Are you sure you want to recreate the $db.environment database?"))
        {
          throw new RuntimeException("Database recreate cancelled by user")
        }
      }),

      // CM schema create depends on execution log table existing
      dbSublist("schema_execution_log_recreate"),

      // Track who recreated the database via the first entry in execution_log
      dbSql("call prc_execution_log_entry('database_recreate', '1', 'INFO', 'Database recreated by ${obtainBuildUsername()}', null)"),

      dbSublist("schema_cm_recreate"),
      dbSublist("schema_recreate"),

      dbSublist("data_insert")
    ]
  ],
  
  database_recreate_bonfire: [
    task: true,
    description: "CM and Bonfire data part of database recreate",
    group: project.ext.zapGroup,
    actions: [
      // If local DB, import local cmdata.xml. Otherwise, import from goldcopy
      dbClosure({
        if(project.db.environment in ["Local","Test"]) {
          doCmDataImport()
        }
        else
        {
          doGoldcopyCmDataExport()
          doGoldcopyCmDataImport()
          if(project.config.cmdiff.goldcopyCmDiff) {
            doCreateGoldcopyCmDiff()
            doGoldcopyCmDiff()
          }
        }
      }),

      dbSublist("_data_cm_cleanup"),

      dbClosure({
        doCmBonfireImport()
      }),

      dbSublist("_database_compile_all_objects")
    ]
  ],
  
  update_post_liquibase: [
    task: true,
    description: "Bonfire data part of database recreate to be executed post liquibase update",
    group: project.ext.zapGroup,
    actions: [
      // All 'changeme' data of system variable will go in here, so that beta, gamma, gyodas system variables can be part of DB recreate
      dbSublist("data_update_post_liquibase")
    ]
  ]  
  
]



project.ext.dbTaskConfigList << [
  database_recreate: [
    task: true,
    description: "Recreates the selected database",
    group: project.ext.databaseGroup,
    dependsOn: ["classes", "database_recreate_schema", "database_recreate_bonfire", "update", "update_post_liquibase"],
    actions: []
  ]
]


/**
 * This method is used to update the system variables based on install wizard properties
 * @return DB Actions needed to update system variables
 */
List getInstallationDataSysvarsActions() {
  List actions = []

  // The type, key, and value of each system variable from install wizard
  List<Map<String,String>> sysVarList = getInstallSystemVariableList()

  // Manually adding this one.  I don't know why, but build.xml was doing it so I should too.
  sysVarList << [type: "String", key: "purl.url.dev", value: "http://www.changeme.com/"]

  // Transform each system variable into an action for the list
  sysVarList.each { sysVarMap ->
    String column = null
    String key = null
    String keyType = null
    String value = null

    key = sysVarMap.key

    switch(sysVarMap.type) {
      case "boolean":
        column = "BOOLEAN_VAL"
        value = sysVarMap.value
        keyType = "1"
      break
      case "Integer":
        column = "INT_VAL"
        value = sysVarMap.value
        keyType = "2"
      break
      case "String":
        column = "STRING_VAL"
        value = "'${sysVarMap.value}'"
        keyType = "5"
      break
      case "Email":
        column = "STRING_VAL"
        value = "'${sysVarMap.value}'"
        keyType = "5"
      break
      case "Date":
        column = "DATE_VAL"
        value = "to_date('${sysVarMap.value}', 'mm/dd/yyyy')"
        keyType = "7"
      break
      case "Long":
        column = "LONG_VAL"
        value = sysVarMap.value
        keyType = "3"
      break
      case "Double":
        column = "DOUBLE_VAL"
        value = sysVarMap.value
        keyType = "4"
      break
      default:
        throw new Exception("The value of property sysVarMap.type (${sysVarMap.type}) is unknown.")
      break
    }

    // Everybody likes a little status update, right?
    actions << dbClosure({ println "Updating System Property $key with value $value" })

    actions << dbSql("""
      BEGIN
          UPDATE os_propertyset
          set $column = $value
          WHERE ENTITY_NAME = '$key';

          IF NOT SQL%FOUND THEN
              INSERT INTO os_propertyset(ENTITY_ID, KEY_TYPE, ENTITY_NAME, ENTITY_KEY, $column)
              VALUES(ENTITY_ID_PK_SQ.nextval,$keyType,'$key','$key',$value);
          END IF ;
      END ;
    """)

    // Insert a sleep after each statement for Oracle XE support
    actions << dbClosure({ sleep(500) })
  }

  return actions
}
