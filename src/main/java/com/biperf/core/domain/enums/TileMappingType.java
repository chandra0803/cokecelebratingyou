/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/TileMappingType.java,v $
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
public class TileMappingType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.tiles.moduleapp.tilemappingtype"; // Tile
                                                                                           // Mapping
                                                                                           // Type

  public static final String RESOURCE_CENTER = "resourceCenterModule";
  public static final String APPROVALS = "approvalsModule";
  public static final String BUDGET_TRACKER = "budgetTrackerModule";
  public static final String COMMUNICATIONS = "communicationsModule";
  public static final String NEWS = "newsModule";
  public static final String LEADER_BOARD = "leaderboardModule";
  public static final String ON_THE_SPOT = "onTheSpotCardModule";
  public static final String PUBLIC_RECOGNITION = "publicRecognitionModule";
  public static final String REPORTS = "reports";
  public static final String MANAGER_TOOL_KIT = "managerToolkitModule";
  public static final String TIP = "tipModule";
  public static final String QUIZ = "quizModule";
  public static final String PLATEAU_AWARDS = "plateauAwardsModule";
  public static final String CLICK_THRU = "clickThru";
  public static final String BROWSE_AWARDS = "browseAwards";
  public static final String NOMINATION = "nominationsModule";
  public static final String PRODUCT_CLAIM = "claimModule";
  public static final String RECOGNITION = "recognitionModule";
  public static final String BADGES = "gamificationModule";
  public static final String FEATURED_DESTINATIONS = "featuredDestinations";
  public static final String FORUM = "forum";
  public static final String GOALQUEST_CP_MANAGER = "managerGoalquestCPModule";
  public static final String GOALQUEST = "goalquestModule";
  public static final String CHALLENGEPOINT = "challengepointModule";
  public static final String RECOGNITION_CENTER = "recognitionCenter";
  public static final String PURL = "purlRecognitionModule";
  public static final String HIGHLIGHTED_REPORT = "reportsModule1";
  public static final String SHOP_BANNERS = "bannerModuleModule";
  public static final String MERCHANDISE = "shopMerchandiseModule";
  // public static final String EVENTS = "shopEventsModule";
  public static final String DIY_TRAVEL = "shopTravelModule";
  public static final String EXPERIENCES = "shopExperienceModule";
  public static final String TRAVEL_ENVOY = "shopEnvoyTravelModule";
  // public static final String REAL_DEAL = "shopRealDealModule";
  // public static final String TRAVEL_GREAT_DEAL = "shopTravelGreatDealsModule";
  // public static final String GREAT_DEAL = "shopGreatDealsModule";
  public static final String PASSPORT_TRAVEL = "shopPassportTravelModule";
  public static final String INTL_CATALOGUE = "shopIntlMerchandiseModule";
  public static final String SMACK_TALK = "throwdownSmackTalkModule";
  public static final String INTSANT_POLL = "instantPollModule";
  public static final String CELEBRATION = "celebrationModule";
  public static final String TRAINING_CAMP = "throwdownTrainingCampModule";
  public static final String THROWDOWN_NEWS = "throwdownNewsModule";
  public static final String THROWDOWN_STANDINGS = "throwdownStandingsModule";
  public static final String THROWDOWN_PROMONAME_SELECTOR = "throwdownPromoSelectModule";
  public static final String THROWDOWN_MATCH_SUMMARY = "throwdownMatchSummaryModule";
  public static final String THROWDOWN_RANKINGS = "throwdownRankingsModule";
  public static final String THROWDOWN_MATCH_LIST = "throwdownAllMatchesModule";
  public static final String ENGAGEMENT = "engagementModule";
  public static final String ENGAGEMENT_MANAGER = "engagementManagerModule";

  // ssi promotion tiles
  public static final String SSI_PARTICIPANT = "SSIParticipantModule";
  public static final String SSI_MANAGER = "SSIManagerModule";
  public static final String SSI_CREATOR = "SSICreatorModule";
  public static final String SSI_CREATE_A_CONTEST = "SSICreatorContestsModule";

  public static final String NOMINATION_INPROGRESS = "nominationsProgressModule";
  public static final String NOMINATION_WINNERS = "nominationsWinnersModule";

  public static final String SEARCH = "heroModule";
  public static final String WORK_HAPPIER = "workHappierPaxModule";
  public static final String X_PROMOTIONAL = "xpromoModule";

  // added as part of the throw down combined module
  public static final String THROWDOWN_COMBINED = "throwdownCombinedModule";

  public static final String PURL_CELEBRATE = "purlCelebrateModule";
  public static final String RA_ADVISOR = "recognitionAdvisorModule";
  
  //Client customization
  public static final String CAREER_MOMENTS = "careerMomentsModule";
  public static final String BUNCHBALL = "bunchBallModule";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected TileMappingType()
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
    return getPickListFactory().getPickList( TileMappingType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static TileMappingType lookup( String code )
  {
    return (TileMappingType)getPickListFactory().getPickListItem( TileMappingType.class, code );
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

  public boolean isShopTile()
  {
    return PLATEAU_AWARDS.equals( getCode() ) || BROWSE_AWARDS.equals( getCode() ) || MERCHANDISE.equals( getCode() ) || DIY_TRAVEL.equals( getCode() ) || EXPERIENCES.equals( getCode() )
        || TRAVEL_ENVOY.equals( getCode() ) || PASSPORT_TRAVEL.equals( getCode() ) || INTL_CATALOGUE.equals( getCode() );
  }
}
