/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ImportFileTypeType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The FileLoadType is a concrete instance of a PickListItem which wrappes a type save enum object
 * of a PickList from content manager.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ImportFileTypeType extends PickListItem
{
  public static final String BUDGET = "bud";
  public static final String LEADERBOARD = "leaderboard";
  public static final String DEPOSIT = "dep";
  public static final String HIERARCHY = "hier";
  public static final String PARTICIPANT = "par";
  public static final String PRODUCT = "prod";
  public static final String PRODUCT_CLAIM = "product_claim";
  public static final String QUIZ = "quiz";
  public static final String GQ_BASE_DATA_LOAD = "base";
  public static final String GQ_PROGRESS_DATA_LOAD = "prog";
  public static final String GQ_GOAL_DATA_LOAD = "goal";
  public static final String GQ_VIN_LOAD = "vin_nbr";
  public static final String CP_BASE_DATA_LOAD = "cpbase";
  public static final String CP_PROGRESS_DATA_LOAD = "cpprog";
  public static final String CP_LEVEL_DATA_LOAD = "cplevel";
  public static final String AWARD_LEVEL = "awardlevel";

  public static final String BADGE = "badge";
  public static final String TD_PROGRESS_DATA_LOAD = "tdprog";
  public static final String SSI_PROGRESS_DATA_LOAD = "ssiprog";
  public static final String BUDGET_DISTRIBUTION = "bud_dist";

  public static final String NOMINATION_APPROVER = "nomapprover";
  public static final String SSI_CONTEST_OBJ = "ssicontestobj";
  public static final String SSI_CONTEST_SIU = "ssicontestsiu";
  public static final String SSI_CONTEST_DTGT = "ssicontestdtgt";
  public static final String SSI_CONTEST_SR = "ssicontestsr";
  public static final String SSI_CONTEST_ATN = "ssicontestatn";

  /**
   * the name of the Content Manager asset that stores the text of Import File
   * Type picklist items
   */
  private static final String PICKLIST_ASSET = "picklist.importfile.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ImportFileTypeType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ImportFileTypeType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ImportFileTypeType lookup( String code )
  {
    return (ImportFileTypeType)getPickListFactory().getPickListItem( ImportFileTypeType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ImportFileTypeType getDefaultItem()
  {
    return (ImportFileTypeType)getPickListFactory().getDefaultPickListItem( ImportFileTypeType.class );
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  public boolean isBudget()
  {
    return BUDGET.equalsIgnoreCase( getCode() );
  }

  public boolean isDeposit()
  {
    return DEPOSIT.equalsIgnoreCase( getCode() );
  }

  public boolean isLeaderBoard()
  {
    return LEADERBOARD.equalsIgnoreCase( getCode() );
  }

  public boolean isHierarchy()
  {
    return HIERARCHY.equalsIgnoreCase( getCode() );
  }

  public boolean isParticipant()
  {
    return PARTICIPANT.equalsIgnoreCase( getCode() );
  }

  public boolean isProduct()
  {
    return PRODUCT.equalsIgnoreCase( getCode() );
  }

  public boolean isProductClaim()
  {
    return PRODUCT_CLAIM.equalsIgnoreCase( getCode() );
  }

  public boolean isQuiz()
  {
    return QUIZ.equalsIgnoreCase( getCode() );
  }

  public boolean isGQPaxBase()
  {
    return GQ_BASE_DATA_LOAD.equalsIgnoreCase( getCode() );
  }

  public boolean isGQPaxGoal()
  {
    return GQ_GOAL_DATA_LOAD.equalsIgnoreCase( getCode() );
  }

  public boolean isGQProgress()
  {
    return GQ_PROGRESS_DATA_LOAD.equalsIgnoreCase( getCode() );
  }

  public boolean isGQVin()
  {
    return GQ_VIN_LOAD.equalsIgnoreCase( getCode() );
  }

  public boolean isCPPaxBase()
  {
    return CP_BASE_DATA_LOAD.equalsIgnoreCase( getCode() );
  }

  public boolean isCPPaxLevel()
  {
    return CP_LEVEL_DATA_LOAD.equalsIgnoreCase( getCode() );
  }

  public boolean isCPProgress()
  {
    return CP_PROGRESS_DATA_LOAD.equalsIgnoreCase( getCode() );
  }

  public boolean isAwardLevel()
  {
    return AWARD_LEVEL.equalsIgnoreCase( getCode() );
  }

  public boolean isBadge()
  {
    return BADGE.equalsIgnoreCase( getCode() );
  }

  // ThrowdownProgress

  public boolean isTDProgress()
  {
    return TD_PROGRESS_DATA_LOAD.equalsIgnoreCase( getCode() );
  }

  // SSI Contest
  public boolean isSSIProgress()
  {
    return SSI_PROGRESS_DATA_LOAD.equalsIgnoreCase( getCode() );
  }

  public boolean isSSIContestOBJ()
  {
    return SSI_CONTEST_OBJ.equalsIgnoreCase( getCode() );
  }

  public boolean isSSIContestSIU()
  {
    return SSI_CONTEST_SIU.equalsIgnoreCase( getCode() );
  }

  public boolean isSSIContestDTGT()
  {
    return SSI_CONTEST_DTGT.equalsIgnoreCase( getCode() );
  }

  public boolean isSSIContestSR()
  {
    return SSI_CONTEST_SR.equalsIgnoreCase( getCode() );
  }

  public boolean isSSIContestATN()
  {
    return SSI_CONTEST_ATN.equalsIgnoreCase( getCode() );
  }

  public boolean isBudgetRedistribution()
  {
    return BUDGET_DISTRIBUTION.equalsIgnoreCase( getCode() );
  }
}
