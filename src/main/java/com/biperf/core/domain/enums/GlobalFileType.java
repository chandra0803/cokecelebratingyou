
package com.biperf.core.domain.enums;

import java.util.List;

@SuppressWarnings( "serial" )
public class GlobalFileType extends PickListItem
{
  public static final String BUDGET = "bud";
  public static final String DEPOSIT = "dep";
  public static final String HIERARCHY = "hier";
  public static final String PARTICIPANT = "par";
  public static final String PRODUCT = "prod";
  public static final String SURVEY = "survey";
  public static final String QUIZ = "quiz";
  public static final String GQ_BASE_DATA_LOAD = "base";
  public static final String GQ_PROGRESS_DATA_LOAD = "prog";
  public static final String GQ_GOAL_DATA_LOAD = "goal";
  public static final String GQ_VIN_LOAD = "vin_nbr";
  public static final String CP_BASE_DATA_LOAD = "cpbase";
  public static final String CP_PROGRESS_DATA_LOAD = "cpprog";
  public static final String CP_LEVEL_DATA_LOAD = "cplevel";
  public static final String AWARD_LEVEL = "awardlevel";
  public static final String PURL = "purl";
  public static final String BADGE = "badge";
  public static final String LEADERBOARD = "leaderboard";
  public static final String TD_PROGRESS_DATA_LOAD = "tdprog";
  public static final String BUDGET_DISTRIBUTION = "bud_dist";
  public static final String NOMINATION_APPROVER = "nomapprover";
  public static final String DATABASE_DUMP_FILE = "dbdump";
  public static final String SSI_PROGRESS_DATA_LOAD = "ssiprog";
  public static final String SSI_CONTEST_OBJ = "ssicontestobj";
  public static final String SSI_CONTEST_SIU = "ssicontestsiu";
  public static final String SSI_CONTEST_DTGT = "ssicontestdtgt";
  public static final String SSI_CONTEST_SR = "ssicontestsr";

  private static final String PICKLIST_ASSET = "picklist.global.file.type";

  protected GlobalFileType()
  {
    super();
  }

  @SuppressWarnings( "rawtypes" )
  public static List getList()
  {
    return getPickListFactory().getPickList( GlobalFileType.class );
  }

  public static GlobalFileType lookup( String code )
  {
    return (GlobalFileType)getPickListFactory().getPickListItem( GlobalFileType.class, code );
  }

  public static GlobalFileType getDefaultItem()
  {
    return (GlobalFileType)getPickListFactory().getDefaultPickListItem( GlobalFileType.class );
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

}
