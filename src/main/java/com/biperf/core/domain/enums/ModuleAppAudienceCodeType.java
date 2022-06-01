/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ModuleAppAudienceCodeType.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PrimaryAudienceType.
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
 * <td>jenniget</td>
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@SuppressWarnings( "serial" )
public class ModuleAppAudienceCodeType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.tiles.moduleapp.audiencecodetype"; // Module
                                                                                            // App
                                                                                            // Audience
                                                                                            // Type

  public static final String STANDARD = "standard";
  public static final String DISABLED = "disabled";
  public static final String PROMOTION_GIVER_AUDIENCE = "promotiongiveraudience";
  public static final String NONE = "none";
  public static final String CONTENT_MANAGER = "contentManager";
  public static final String WHATS_NEW = "whatsnew";
  public static final String MANAGER_TOOLKIT = "managertoolkit";
  public static final String LEADERBOARD = "leaderboard";
  public static final String BADGE = "badges";
  public static final String BUDGET = "budgetTrackerModule";
  public static final String APPROVALS = "approvalsModule";
  public static final String PURL_RECOGNITION = "purlRecognitionModule";
  public static final String COMMUNICATIONS = "communicationsModule";
  public static final String NEWS = "newsModule";
  public static final String PUBLIC_RECOGNITION = "publicRecognitionModule";
  public static final String ON_THE_SPOT_CARD = "onTheSpotCardModule";
  public static final String QUIZ = "quiz";
  public static final String SHOP_TRAVEL = "shopTravel";
  public static final String SHOP_EVENTS = "shopEvents";
  public static final String SHOP_MERCHANDISE = "shopMerchandise";
  public static final String TIP = "tip";
  public static final String NOMINATION = "nomination";
  public static final String GENERIC_BANNER = "banner";
  public static final String SHOP_INTL_MERCH_MODULE = "shopIntlMerchandiseModule";
  public static final String GOAL_QUEST = "goalquestModule";
  public static final String CHALLENGE_POINT = "challengepointModule";
  public static final String GOALQUEST_CP_MANAGER = "managerGoalquestCPModule";
  public static final String PRODUCT_CLAIM = "claimModule";
  public static final String FORUM = "forumModule";
  public static final String THROWDOWN = "throwdownModule";
  public static final String THROWDOWN_MATCH_LIST = "throwdownMatchListModule";
  public static final String INSTANT_POLL = "instantPollModule";
  public static final String CELEBRATION = "celebrationModule";
  public static final String ENGAGEMENT = "engagementModule";
  public static final String ENGAGEMENT_MANAGER = "engagementManagerModule";
  public static final String HIGHLIGHTED_REPORT = "highlightedReportModule";
  public static final String SSI_PARTICIPANT = "ssiParticipantObjectiveModule";
  public static final String NOMINATION_INPROGRESS = "nominationsProgressModule";
  public static final String NOMINATION_WINNERS = "nominationsWinnersModule";
  public static final String SEARCH = "heroModule";
  public static final String X_PROMOTIONAL = "xpromoModule";
  public static final String RECOGNITION_ADVISOR = "recognitionAdvisorModule";
  //client customization
  public static final String CAREER_MOMENTS = "careerMomentsModule";
  public static final String BUNCHBALL = "bunchBallModule";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ModuleAppAudienceCodeType()
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
    return getPickListFactory().getPickList( ModuleAppAudienceCodeType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ModuleAppAudienceCodeType lookup( String code )
  {
    return (ModuleAppAudienceCodeType)getPickListFactory().getPickListItem( ModuleAppAudienceCodeType.class, code );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  /**
   * Checks to see if this pickList is of type ALL_ACTIVE_PAX_CODE.
   * 
   * @return boolean
   */
  public boolean isStandard()
  {
    return getCode().equalsIgnoreCase( STANDARD );
  }

  /**
   * Returns true if this object represents the "specify audiences" primary audience type; returns
   * false otherwise.
   * 
   * @return true if this object represents the "specify audiences" primary audience type; false
   *         otherwise.
   */
  public boolean isPromotionGiverAudience()
  {
    return getCode().equalsIgnoreCase( PROMOTION_GIVER_AUDIENCE );
  }

  public boolean isWhatsNew()
  {
    return getCode().equalsIgnoreCase( WHATS_NEW );
  }

  public boolean isManagerToolkit()
  {
    return getCode().equalsIgnoreCase( MANAGER_TOOLKIT );
  }

  public boolean isLeaderboard()
  {
    return getCode().equalsIgnoreCase( LEADERBOARD );
  }

  public boolean isForum()
  {
    return getCode().equalsIgnoreCase( FORUM );
  }

  public boolean isHighLightedReport()
  {
    return getCode().equalsIgnoreCase( HIGHLIGHTED_REPORT );
  }

  public boolean isCelebration()
  {
    return getCode().equalsIgnoreCase( CELEBRATION );
  }

  public boolean isBadge()
  {
    return getCode().equalsIgnoreCase( BADGE );
  }

  public boolean isQuiz()
  {
    return getCode().equalsIgnoreCase( QUIZ );
  }

  public boolean isCommunications()
  {
    return getCode().equalsIgnoreCase( COMMUNICATIONS );
  }

  public boolean isNews()
  {
    return getCode().equalsIgnoreCase( NEWS );
  }

  public boolean isPublicRecognition()
  {
    return getCode().equalsIgnoreCase( PUBLIC_RECOGNITION );
  }

  public boolean isOnTheSpotCard()
  {
    return getCode().equalsIgnoreCase( ON_THE_SPOT_CARD );
  }

  public boolean isShopTravel()
  {
    return getCode().equalsIgnoreCase( SHOP_TRAVEL );
  }

  public boolean isShopEvents()
  {
    return getCode().equalsIgnoreCase( SHOP_EVENTS );
  }

  public boolean isShopMerchandise()
  {
    return getCode().equalsIgnoreCase( SHOP_MERCHANDISE );
  }

  public boolean isShopIntlMerch()
  {
    return getCode().equalsIgnoreCase( SHOP_INTL_MERCH_MODULE );
  }

  public boolean isGoalQuest()
  {
    return getCode().equalsIgnoreCase( GOAL_QUEST );
  }

  public boolean isChallengePoint()
  {
    return getCode().equalsIgnoreCase( CHALLENGE_POINT );
  }

  public boolean isSearch()
  {
    return getCode().equalsIgnoreCase( SEARCH );
  }
}
