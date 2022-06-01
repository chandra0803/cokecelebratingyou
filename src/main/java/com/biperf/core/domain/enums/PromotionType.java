/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PromotionType.
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
 * <td>sondgero</td>
 * <td>Jun 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promotiontype";

  public static final String PRODUCT_CLAIM = "product_claim";
  public static final String RECOGNITION = "recognition";
  public static final String QUIZ = "quiz";
  public static final String DIY_QUIZ = "diy_quiz";
  public static final String NOMINATION = "nomination";
  public static final String SURVEY = "survey";
  public static final String GOALQUEST = "goalquest";
  public static final String CHALLENGE_POINT = "challengepoint";
  public static final String WELLNESS = "wellness";
  public static final String THROWDOWN = "throwdown";
  public static final String INSTANTPOLL = "instantpoll";
  public static final String BADGE = "badge";
  public static final String ENGAGEMENT = "engagement";
  public static final String SELF_SERV_INCENTIVES = "self_serv_incentives";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PromotionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( PromotionType.class );
  }

  public static List getPromotionList()
  {
    List finalList = new ArrayList();
    for ( Iterator iter = PromotionType.getList().iterator(); iter.hasNext(); )
    {
      PromotionType promotionType = (PromotionType)iter.next();
      if ( !promotionType.getCode().equals( INSTANTPOLL ) && !promotionType.getCode().equals( BADGE ) )
      {
        finalList.add( promotionType );
      }
    }
    return finalList;
  }

  public static List getSurveyList()
  {
    List surveyGqCpMods = new ArrayList();
    if ( PromotionType.lookup( SURVEY ) != null )
    {
      surveyGqCpMods.add( PromotionType.lookup( SURVEY ) );
    }
    if ( PromotionType.lookup( CHALLENGE_POINT ) != null )
    {
      surveyGqCpMods.add( PromotionType.lookup( CHALLENGE_POINT ) );
    }
    if ( PromotionType.lookup( GOALQUEST ) != null )
    {
      surveyGqCpMods.add( PromotionType.lookup( GOALQUEST ) );
    }

    return surveyGqCpMods;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static PromotionType lookup( String code )
  {
    return (PromotionType)getPickListFactory().getPickListItem( PromotionType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionType
   */
  public static PromotionType getDefaultItem()
  {
    return (PromotionType)getPickListFactory().getDefaultPickListItem( PromotionType.class );
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

  public boolean isProductClaimPromotion()
  {
    return PRODUCT_CLAIM.equals( getCode() );
  }

  public boolean isThrowDownPromotion()
  {
    return THROWDOWN.equals( getCode() );
  }

  public boolean isRecognitionPromotion()
  {
    return RECOGNITION.equals( getCode() );
  }

  public boolean isQuizPromotion()
  {
    return QUIZ.equals( getCode() );
  }

  public boolean isNominationPromotion()
  {
    return NOMINATION.equals( getCode() );
  }

  public boolean isSurveyPromotion()
  {
    return SURVEY.equals( getCode() );
  }

  public boolean isBadgePromotion()
  {
    return BADGE.equals( getCode() );
  }

  public boolean isEngagementPromotion()
  {
    return ENGAGEMENT.equals( getCode() );
  }

  public boolean isSelfServIncentivesPromotion()
  {
    return SELF_SERV_INCENTIVES.equals( getCode() );
  }

}
