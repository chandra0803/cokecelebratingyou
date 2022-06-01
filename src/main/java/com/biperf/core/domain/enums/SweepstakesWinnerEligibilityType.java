/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/SweepstakesWinnerEligibilityType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * SweepstakesWinnerEligibilityType.
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
 * <td>Oct 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SweepstakesWinnerEligibilityType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.sweepstakes.winnerEligibilityType";

  // Nomination Codes
  public static final String NOMINATORS_ONLY_CODE = "nominators";
  public static final String NOMINATORS_AND_NOMINEES_COMBINED_CODE = "nomcombined";
  public static final String NOMINATORS_AND_NOMINEES_SEPARATE_CODE = "nomseparate";
  public static final String NOMINEES_ONLY_CODE = "nominees";

  // Recognition Codes
  public static final String GIVERS_ONLY_CODE = "givers";
  public static final String GIVERS_AND_RECEIVERS_COMBINED_CODE = "combined";
  public static final String GIVERS_AND_RECEIVERS_SEPARATE_CODE = "separate";
  public static final String RECEIVERS_ONLY_CODE = "receivers";

  // product claim codes
  public static final String SUBMITTERS_ONLY_CODE = "submittersdraw";
  public static final String TEAM_MEMBERS_ONLY_CODE = "teammembersdraw";
  public static final String SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE = "separatedraw";
  public static final String SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE = "combineddraw";

  public static final String PAX_SELECTED_SURVEY_ONLY_CODE = "paxSelectedSurvey";

  public static final String BADGE_RECEIVER_ONLY_CODE = "badgereceiver";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SweepstakesWinnerEligibilityType()
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
    return getPickListFactory().getPickList( SweepstakesWinnerEligibilityType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SweepstakesWinnerEligibilityType lookup( String code )
  {
    return (SweepstakesWinnerEligibilityType)getPickListFactory().getPickListItem( SweepstakesWinnerEligibilityType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionAudienceType getDefaultItem()
  // {
  // return (PromotionAudienceType)getPickListFactory().getDefaultPickListItem(
  // PromotionAudienceType.class );
  // }
  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static SweepstakesWinnerEligibilityType getProductClaimDefaultItem()
  {
    return (SweepstakesWinnerEligibilityType)getPickListFactory().getPickListItem( SweepstakesWinnerEligibilityType.class, SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE );
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

  public boolean isPrimaryOnly()
  {
    return this.getCode().equals( GIVERS_ONLY_CODE ) || this.getCode().equals( SUBMITTERS_ONLY_CODE ) || this.getCode().equals( NOMINATORS_ONLY_CODE );
  }

  public boolean isPrimarySecondaryCombined()
  {
    return this.getCode().equals( GIVERS_AND_RECEIVERS_COMBINED_CODE ) || this.getCode().equals( SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE )
        || this.getCode().equals( NOMINATORS_AND_NOMINEES_COMBINED_CODE );
  }

  public boolean isPrimarySecondarySeparate()
  {
    return this.getCode().equals( GIVERS_AND_RECEIVERS_SEPARATE_CODE ) || this.getCode().equals( SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE )
        || this.getCode().equals( NOMINATORS_AND_NOMINEES_SEPARATE_CODE );
  }

  public boolean isSecondaryOnly()
  {
    return this.getCode().equals( RECEIVERS_ONLY_CODE ) || this.getCode().equals( TEAM_MEMBERS_ONLY_CODE ) || this.getCode().equals( NOMINEES_ONLY_CODE );
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of sweepstakeWinnersType specific to claim
   */
  public static List getClaimEligibleWinnersList()
  {
    List claimEligibleWinnersList = new ArrayList();

    claimEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( SUBMITTERS_ONLY_CODE ) );
    claimEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE ) );
    claimEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE ) );
    claimEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( TEAM_MEMBERS_ONLY_CODE ) );

    return claimEligibleWinnersList;
  }

  /**
   * Get the pick list from getClaimEligibleWinnersList(). Minus items not relevant to a product
   * claim when there are no team members
   * 
   * @return List of sweepstakeWinnersType specific to claim without team items
   */
  public static List getClaimEligibleWinnersWithoutTeamList()
  {
    List claimEligibleWinnersList = SweepstakesWinnerEligibilityType.getClaimEligibleWinnersList();

    claimEligibleWinnersList.remove( SweepstakesWinnerEligibilityType.lookup( SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE ) );
    claimEligibleWinnersList.remove( SweepstakesWinnerEligibilityType.lookup( SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE ) );
    claimEligibleWinnersList.remove( SweepstakesWinnerEligibilityType.lookup( TEAM_MEMBERS_ONLY_CODE ) );

    return claimEligibleWinnersList;
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of sweepstakeWinnersType specific to recognition
   */
  public static List getRecognitionEligibleWinnersList()
  {
    List recognitionEligibleWinnersList = new ArrayList();

    recognitionEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( GIVERS_ONLY_CODE ) );
    recognitionEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( GIVERS_AND_RECEIVERS_COMBINED_CODE ) );
    recognitionEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( GIVERS_AND_RECEIVERS_SEPARATE_CODE ) );
    recognitionEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( RECEIVERS_ONLY_CODE ) );

    return recognitionEligibleWinnersList;
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of sweepstakeWinnersType specific to nomination
   */
  public static List getNominationEligibleWinnersList()
  {
    List nominationEligibleWinnersList = new ArrayList();

    nominationEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( NOMINATORS_ONLY_CODE ) );
    nominationEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( NOMINATORS_AND_NOMINEES_COMBINED_CODE ) );
    nominationEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( NOMINATORS_AND_NOMINEES_SEPARATE_CODE ) );
    nominationEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( NOMINEES_ONLY_CODE ) );

    return nominationEligibleWinnersList;
  }

  public static List getSurveyEligibleWinnersList()
  {
    List surveyEligibleWinnersList = new ArrayList();

    surveyEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( PAX_SELECTED_SURVEY_ONLY_CODE ) );

    return surveyEligibleWinnersList;
  }

  public static List getBadgeEligibleWinnersList()
  {
    List badgeEligibleWinnersList = new ArrayList();

    badgeEligibleWinnersList.add( SweepstakesWinnerEligibilityType.lookup( BADGE_RECEIVER_ONLY_CODE ) );

    return badgeEligibleWinnersList;
  }

}
