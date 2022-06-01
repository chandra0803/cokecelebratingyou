/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionSweepstakesForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionSweepstakesAwardAmountType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SweepstakesClaimEligibilityType;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.SweepstakesBillCode;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionSweepstakesForm.
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
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakesForm extends BaseActionForm
{
  private static final String WINNERS_ASSET_KEY = "promotion.sweepstakes.WINNERS";
  private static final String ELIGIBLE_CLAIMS_KEY = "promotion.sweepstakes.ELIGIBLE_CLAIMS";
  private static final String ELIGIBLE_WINNERS_KEY = "promotion.sweepstakes.ELIGIBLE_WINNERS";
  private static final String MULTIPLE_AWARDS_KEY = "promotion.sweepstakes.MULTIPLE_AWARDS";
  private static final String AWARD_AMOUNT_KEY = "promotion.awards.AMOUNT";
  public static final String DEPT_NAME = "department";
  public static final String ORG_UNIT_NAME = "orgUnitName";
  public static final String COUNTRY_CODE = "countryCode";
  public static final String LOGIN_ID = "userName";

  private String method;
  private String promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;
  private Long version;

  private boolean active;
  private String eligibleWinners;
  private String giversAmount;
  private String giversAmountType;
  private String giversAwardTypeAmount;
  private String giversAwardLevel;
  private String receiversAmount;
  private String receiversAmountType;
  private String receiversAwardTypeAmount;
  private String receiversAwardLevel;
  private String combinedAmount;
  private String combinedAmountType;
  private String combinedAwardTypeAmount;
  private String combinedAwardLevel;
  private String multipleAwards;
  private String multipleAwardsTrimmed;
  private String awardTypeCode;
  private String awardTypeText;

  private String eligibleClaims; // aded this for product claims

  private boolean activeNotEditable = false;

  private boolean billCodesActive;

  private String billCode1;
  private String customValue1;
  private String billCode2;
  private String customValue2;
  private String billCode3;
  private String customValue3;
  private String billCode4;
  private String customValue4;
  private String billCode5;
  private String customValue5;
  private String billCode6;
  private String customValue6;
  private String billCode7;
  private String customValue7;
  private String billCode8;
  private String customValue8;
  private String billCode9;
  private String customValue9;
  private String billCode10;
  private String customValue10;

  public static final String CUSTOM_VALUE = "customValue";

  /**
   * Load the form
   * 
   * @param promotion
   */
  public void load( Promotion promotion )
  {
    promotionId = promotion.getId().toString();
    promotionName = promotion.getName();
    promotionTypeName = promotion.getPromotionType().getName();
    promotionTypeCode = promotion.getPromotionType().getCode();
    promotionStatus = promotion.getPromotionStatus().getCode();
    version = promotion.getVersion();
    active = promotion.isSweepstakesActive();

    // Nomination promotions will use a predefined label and is always a point reward
    if ( PromotionType.NOMINATION.equals( promotionTypeCode ) )
    {
      awardTypeCode = PromotionAwardsType.POINTS;
      awardTypeText = getCMAssetService().getTextFromCmsResourceBundle( "promotion.sweepstakes.NOMINATION_AWARD_LABEL" );
    }
    // Other promotion types will use the label from the award type name
    else if ( promotion.getAwardType() != null )
    {
      awardTypeCode = promotion.getAwardType().getCode();
      awardTypeText = promotion.getAwardType().getName();
    }
    if ( promotion.isQuizPromotion() )
    {
      loadQuizPromotion( (QuizPromotion)promotion );
    }
    else if ( promotion.isSurveyPromotion() )
    {
      loadSurveyPromotion( (SurveyPromotion)promotion );
    }
    else if ( promotion.isBadgePromotion() )
    {
      loadBadgePromotion( (Badge)promotion );
    }
    else
    {
      loadPromotion( promotion );
    }

    this.billCodesActive = promotion.isSwpBillCodesActive();

    List<SweepstakesBillCode> sweepStakesBillCodes = null;

    if ( this.billCodesActive )
    {
      sweepStakesBillCodes = new ArrayList<SweepstakesBillCode>();

      for ( SweepstakesBillCode promoBillCode : promotion.getSweepstakesBillCodes() )
      {
        sweepStakesBillCodes.add( promoBillCode );
      }

      loadPromotionBillCodes( sweepStakesBillCodes );
    }
  }

  private void loadPromotion( Promotion promotion )
  {
    if ( promotion.getSweepstakesMultipleAwardType() != null )
    {
      multipleAwards = promotion.getSweepstakesMultipleAwardType().getCode();
    }
    if ( promotion.getSweepstakesMultipleAwardType() != null )
    {
      multipleAwardsTrimmed = promotion.getSweepstakesMultipleAwardType().getCode();
    }

    if ( promotion.getSweepstakesWinnerEligibilityType() != null )
    {
      eligibleWinners = promotion.getSweepstakesWinnerEligibilityType().getCode();
    }

    if ( promotion.getSweepstakesClaimEligibilityType() != null )
    {
      eligibleClaims = promotion.getSweepstakesClaimEligibilityType().getCode();
    }

    if ( !active )
    {
      multipleAwards = "";
      multipleAwardsTrimmed = "";
      eligibleWinners = "";
      eligibleClaims = "";
    }

    if ( eligibleWinners.equals( SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE ) || eligibleWinners.equals( SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE )
        || eligibleWinners.equals( SweepstakesWinnerEligibilityType.NOMINATORS_ONLY_CODE ) )
    {
      if ( promotion.getSweepstakesPrimaryWinners() != null )
      {
        giversAmount = promotion.getSweepstakesPrimaryWinners().toString();
      }
      if ( promotion.getSweepstakesPrimaryBasisType() != null )
      {
        giversAmountType = promotion.getSweepstakesPrimaryBasisType().getCode();
      }
      if ( !isAwardLevelPromotion() && promotion.getSweepstakesPrimaryAwardAmount() != null )
      {
        giversAwardTypeAmount = promotion.getSweepstakesPrimaryAwardAmount().toString();
      }
      if ( isAwardLevelPromotion() && promotion.getSweepstakesPrimaryAwardLevel() != null )
      {
        giversAwardLevel = promotion.getSweepstakesPrimaryAwardLevel().toString();
      }
    }
    else if ( eligibleWinners.equals( SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE ) || eligibleWinners.equals( SweepstakesWinnerEligibilityType.RECEIVERS_ONLY_CODE )
        || eligibleWinners.equals( SweepstakesWinnerEligibilityType.NOMINEES_ONLY_CODE ) )
    {
      if ( promotion.getSweepstakesSecondaryWinners() != null )
      {
        receiversAmount = promotion.getSweepstakesSecondaryWinners().toString();
      }
      if ( promotion.getSweepstakesSecondaryBasisType() != null )
      {
        receiversAmountType = promotion.getSweepstakesSecondaryBasisType().getCode();
      }
      if ( !isAwardLevelPromotion() && promotion.getSweepstakesSecondaryAwardAmount() != null )
      {
        receiversAwardTypeAmount = promotion.getSweepstakesSecondaryAwardAmount().toString();
      }
      if ( isAwardLevelPromotion() && promotion.getSweepstakesSecondaryAwardLevel() != null )
      {
        receiversAwardLevel = promotion.getSweepstakesSecondaryAwardLevel().toString();
      }
    }
    else if ( eligibleWinners.equals( SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE )
        || eligibleWinners.equals( SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE )
        || eligibleWinners.equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_SEPARATE_CODE ) )
    {
      if ( promotion.getSweepstakesPrimaryWinners() != null )
      {
        giversAmount = promotion.getSweepstakesPrimaryWinners().toString();
      }
      if ( promotion.getSweepstakesPrimaryBasisType() != null )
      {
        giversAmountType = promotion.getSweepstakesPrimaryBasisType().getCode();
      }
      if ( isAwardLevelPromotion() && promotion.getSweepstakesPrimaryAwardLevel() != null )
      {
        giversAwardLevel = promotion.getSweepstakesPrimaryAwardLevel().toString();
      }
      if ( !isAwardLevelPromotion() && promotion.getSweepstakesPrimaryAwardAmount() != null )
      {
        giversAwardTypeAmount = promotion.getSweepstakesPrimaryAwardAmount().toString();
      }
      if ( promotion.getSweepstakesSecondaryWinners() != null )
      {
        receiversAmount = promotion.getSweepstakesSecondaryWinners().toString();
      }
      if ( promotion.getSweepstakesSecondaryBasisType() != null )
      {
        receiversAmountType = promotion.getSweepstakesSecondaryBasisType().getCode();
      }
      if ( !isAwardLevelPromotion() && promotion.getSweepstakesSecondaryAwardAmount() != null )
      {
        receiversAwardTypeAmount = promotion.getSweepstakesSecondaryAwardAmount().toString();
      }
      if ( isAwardLevelPromotion() && promotion.getSweepstakesSecondaryAwardLevel() != null )
      {
        receiversAwardLevel = promotion.getSweepstakesSecondaryAwardLevel().toString();
      }
      if ( promotion.getAwardType() != null && "mechandise".equals( promotion.getAwardType().getCode() ) && promotion.getSweepstakesSecondaryAwardLevel() != null )
      {
        receiversAwardLevel = promotion.getSweepstakesSecondaryAwardLevel().toString();
      }
    }
    else
    {
      // submitters or team members values could be used, since they should be the same when
      // 'combined'
      if ( promotion.getSweepstakesPrimaryWinners() != null )
      {
        if ( promotion.getSweepstakesPrimaryWinners() != null )
        {
          combinedAmount = promotion.getSweepstakesPrimaryWinners().toString();
        }
        if ( promotion.getSweepstakesPrimaryBasisType() != null )
        {
          combinedAmountType = promotion.getSweepstakesPrimaryBasisType().getCode();
        }
        if ( !isAwardLevelPromotion() && promotion.getSweepstakesPrimaryAwardAmount() != null )
        {
          combinedAwardTypeAmount = promotion.getSweepstakesPrimaryAwardAmount().toString();
        }
        if ( isAwardLevelPromotion() && promotion.getSweepstakesPrimaryAwardLevel() != null )
        {
          combinedAwardLevel = promotion.getSweepstakesPrimaryAwardLevel().toString();
        }
      }
      else
      {
        if ( promotion.getSweepstakesSecondaryWinners() != null )
        {
          combinedAmount = promotion.getSweepstakesSecondaryWinners().toString();
        }
        if ( promotion.getSweepstakesSecondaryBasisType() != null )
        {
          combinedAmountType = promotion.getSweepstakesSecondaryBasisType().getCode();
        }
        if ( !isAwardLevelPromotion() && promotion.getSweepstakesSecondaryAwardAmount() != null )
        {
          combinedAwardTypeAmount = promotion.getSweepstakesSecondaryAwardAmount().toString();
        }
        if ( isAwardLevelPromotion() && promotion.getSweepstakesSecondaryAwardAmount() != null )
        {
          combinedAwardLevel = promotion.getSweepstakesSecondaryAwardLevel().toString();
        }
      }
    }

  }

  private void loadQuizPromotion( QuizPromotion promotion )
  {
    if ( promotion.getSweepstakesPrimaryWinners() != null )
    {
      this.giversAmount = promotion.getSweepstakesPrimaryWinners().toString();
    }
    if ( promotion.getSweepstakesPrimaryBasisType() != null )
    {
      this.giversAmountType = promotion.getSweepstakesPrimaryBasisType().getCode();
    }
    if ( promotion.getSweepstakesPrimaryAwardAmount() != null )
    {
      this.giversAwardTypeAmount = promotion.getSweepstakesPrimaryAwardAmount().toString();
    }
  }

  private void loadBadgePromotion( Badge promotion )
  {
    if ( promotion.getSweepstakesPrimaryWinners() != null )
    {
      this.giversAmount = promotion.getSweepstakesPrimaryWinners().toString();
    }
    if ( promotion.getSweepstakesPrimaryBasisType() != null )
    {
      this.giversAmountType = promotion.getSweepstakesPrimaryBasisType().getCode();
    }
    if ( promotion.getSweepstakesPrimaryAwardAmount() != null )
    {
      this.giversAwardTypeAmount = promotion.getSweepstakesPrimaryAwardAmount().toString();
    }

    if ( promotion.getSweepstakesWinnerEligibilityType() != null )
    {
      eligibleWinners = promotion.getSweepstakesWinnerEligibilityType().getCode();
    }

  }

  private void loadSurveyPromotion( SurveyPromotion promotion )
  {
    if ( promotion.getSweepstakesPrimaryWinners() != null )
    {
      this.giversAmount = promotion.getSweepstakesPrimaryWinners().toString();
    }
    if ( promotion.getSweepstakesPrimaryBasisType() != null )
    {
      this.giversAmountType = promotion.getSweepstakesPrimaryBasisType().getCode();
    }
    if ( promotion.getSweepstakesPrimaryAwardAmount() != null )
    {
      this.giversAwardTypeAmount = promotion.getSweepstakesPrimaryAwardAmount().toString();
    }

    if ( promotion.getSweepstakesMultipleAwardType() != null )
    {
      multipleAwards = promotion.getSweepstakesMultipleAwardType().getCode();
    }
    if ( promotion.getSweepstakesMultipleAwardType() != null )
    {
      multipleAwardsTrimmed = promotion.getSweepstakesMultipleAwardType().getCode();
    }

    if ( promotion.getSweepstakesWinnerEligibilityType() != null )
    {
      eligibleWinners = promotion.getSweepstakesWinnerEligibilityType().getCode();
    }

    if ( promotion.getSweepstakesClaimEligibilityType() != null )
    {
      eligibleClaims = promotion.getSweepstakesClaimEligibilityType().getCode();
    }

    if ( !active )
    {
      multipleAwards = "";
      multipleAwardsTrimmed = "";
      eligibleWinners = "";
      eligibleClaims = "";
    }

    if ( eligibleWinners.equals( SweepstakesWinnerEligibilityType.PAX_SELECTED_SURVEY_ONLY_CODE ) )
    {
      if ( promotion.getSweepstakesPrimaryWinners() != null )
      {
        giversAmount = promotion.getSweepstakesPrimaryWinners().toString();
      }
      if ( promotion.getSweepstakesPrimaryBasisType() != null )
      {
        giversAmountType = promotion.getSweepstakesPrimaryBasisType().getCode();
      }
      if ( !isAwardLevelPromotion() && promotion.getSweepstakesPrimaryAwardAmount() != null )
      {
        giversAwardTypeAmount = promotion.getSweepstakesPrimaryAwardAmount().toString();
      }
      if ( isAwardLevelPromotion() && promotion.getSweepstakesPrimaryAwardLevel() != null )
      {
        giversAwardLevel = promotion.getSweepstakesPrimaryAwardLevel().toString();
      }
    }
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @param promotion
   * @return Promotion
   */
  public Promotion toDomainObject( Promotion promotion )
  {
    promotion.setId( new Long( promotionId ) );
    promotion.setName( promotionName );
    promotion.setVersion( version );
    if ( !promotion.isBadgePromotion() )
    {
      promotion.setSweepstakesActive( active );
    }

    if ( promotion.isQuizPromotion() )
    {
      return toQuizDomainObject( (QuizPromotion)promotion );
    }
    if ( promotion.isSurveyPromotion() )
    {
      return toSurveyDomainObject( (SurveyPromotion)promotion );
    }
    if ( promotion.isBadgePromotion() )
    {
      return toBadgeDomainObject( (Badge)promotion );
    }

    if ( isActive() )
    {
      promotion.setSweepstakesWinnerEligibilityType( SweepstakesWinnerEligibilityType.lookup( eligibleWinners ) );

      if ( eligibleWinners.equals( SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE ) || eligibleWinners.equals( SweepstakesWinnerEligibilityType.NOMINATORS_ONLY_CODE )
          || eligibleWinners.equals( SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE ) )
      {
        setPrimaryOnly( promotion );
      }
      else if ( eligibleWinners.equals( SweepstakesWinnerEligibilityType.RECEIVERS_ONLY_CODE ) || eligibleWinners.equals( SweepstakesWinnerEligibilityType.NOMINEES_ONLY_CODE )
          || eligibleWinners.equals( SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE ) )
      {
        setSecondaryOnly( promotion );
      }
      else if ( eligibleWinners.equals( SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE )
          || eligibleWinners.equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_SEPARATE_CODE )
          || eligibleWinners.equals( SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE ) )
      {
        setPrimarySecondarySeparate( promotion );
      }
      else
      {
        setPrimarySecondaryCombined( promotion );
      }

      if ( promotion.isProductClaimPromotion() )
      {
        promotion.setSweepstakesClaimEligibilityType( SweepstakesClaimEligibilityType.lookup( eligibleClaims ) );
      }

      promotion.setSwpBillCodesActive( this.billCodesActive );
      if ( this.billCodesActive )
      {
        promotion.setSweepstakesBillCodes( getPromoBillCodeList( promotion ) );
      }
    }

    return promotion;
  }

  private void setPrimaryOnly( Promotion promotion )
  {
    promotion.setSweepstakesPrimaryWinners( new Integer( giversAmount ) );
    promotion.setSweepstakesPrimaryBasisType( SweepstakesWinnersType.lookup( giversAmountType ) );

    if ( !isAwardLevelPromotion() )
    {
      promotion.setSweepstakesPrimaryAwardAmount( new Long( giversAwardTypeAmount ) );
    }
    else
    {
      promotion.setSweepstakesPrimaryAwardLevel( new Long( giversAwardLevel ) );
    }

    promotion.setSweepstakesMultipleAwardType( SweepstakesMultipleAwardsType.lookup( multipleAwardsTrimmed ) );
  }

  private void setSecondaryOnly( Promotion promotion )
  {
    promotion.setSweepstakesSecondaryWinners( new Integer( receiversAmount ) );
    promotion.setSweepstakesSecondaryBasisType( SweepstakesWinnersType.lookup( receiversAmountType ) );

    if ( !isAwardLevelPromotion() )
    {
      promotion.setSweepstakesSecondaryAwardAmount( new Long( receiversAwardTypeAmount ) );
    }
    else
    {
      promotion.setSweepstakesSecondaryAwardLevel( new Long( receiversAwardLevel ) );
    }

    promotion.setSweepstakesMultipleAwardType( SweepstakesMultipleAwardsType.lookup( multipleAwardsTrimmed ) );
  }

  private void setPrimarySecondarySeparate( Promotion promotion )
  {
    promotion.setSweepstakesPrimaryWinners( new Integer( giversAmount ) );
    promotion.setSweepstakesPrimaryBasisType( SweepstakesWinnersType.lookup( giversAmountType ) );
    promotion.setSweepstakesSecondaryWinners( new Integer( receiversAmount ) );
    promotion.setSweepstakesSecondaryBasisType( SweepstakesWinnersType.lookup( receiversAmountType ) );

    promotion.setSweepstakesMultipleAwardType( SweepstakesMultipleAwardsType.lookup( multipleAwards ) );

    if ( !isAwardLevelPromotion() )
    {
      promotion.setSweepstakesPrimaryAwardAmount( new Long( giversAwardTypeAmount ) );
      promotion.setSweepstakesSecondaryAwardAmount( new Long( receiversAwardTypeAmount ) );
    }
    else
    {
      promotion.setSweepstakesPrimaryAwardLevel( new Long( giversAwardLevel ) );
      promotion.setSweepstakesSecondaryAwardLevel( new Long( receiversAwardLevel ) );
    }
  }

  private void setPrimarySecondaryCombined( Promotion promotion )
  {
    promotion.setSweepstakesPrimaryWinners( new Integer( combinedAmount ) );
    promotion.setSweepstakesPrimaryBasisType( SweepstakesWinnersType.lookup( combinedAmountType ) );

    if ( !isAwardLevelPromotion() )
    {
      promotion.setSweepstakesPrimaryAwardAmount( new Long( combinedAwardTypeAmount ) );
      promotion.setSweepstakesSecondaryAwardAmount( new Long( combinedAwardTypeAmount ) );
    }
    else
    {
      promotion.setSweepstakesPrimaryAwardLevel( new Long( combinedAwardLevel ) );
      promotion.setSweepstakesSecondaryAwardLevel( new Long( combinedAwardLevel ) );
    }
    promotion.setSweepstakesSecondaryWinners( new Integer( combinedAmount ) );
    promotion.setSweepstakesSecondaryBasisType( SweepstakesWinnersType.lookup( combinedAmountType ) );

    promotion.setSweepstakesMultipleAwardType( SweepstakesMultipleAwardsType.lookup( multipleAwardsTrimmed ) );
  }

  /**
   * @param promotion
   * @return Promotion
   */
  public Promotion toQuizDomainObject( QuizPromotion promotion )
  {
    if ( isActive() )
    {
      promotion.setSweepstakesPrimaryWinners( new Integer( giversAmount ) );
      promotion.setSweepstakesPrimaryBasisType( SweepstakesWinnersType.lookup( giversAmountType ) );
      promotion.setSweepstakesPrimaryAwardAmount( new Long( giversAwardTypeAmount ) );
    }

    return promotion;
  }

  /**
   * @param promotion
   * @return Promotion
   */
  public Promotion toSurveyDomainObject( SurveyPromotion promotion )
  {
    if ( isActive() )
    {
      promotion.setSweepstakesPrimaryWinners( new Integer( giversAmount ) );
      promotion.setSweepstakesPrimaryBasisType( SweepstakesWinnersType.lookup( giversAmountType ) );
      promotion.setSweepstakesPrimaryAwardAmount( new Long( giversAwardTypeAmount ) );

      promotion.setSweepstakesWinnerEligibilityType( SweepstakesWinnerEligibilityType.lookup( eligibleWinners ) );

      if ( eligibleWinners.equals( SweepstakesWinnerEligibilityType.PAX_SELECTED_SURVEY_ONLY_CODE ) )
      {
        setPrimaryOnly( promotion );
      }
    }

    return promotion;
  }

  public Promotion toBadgeDomainObject( Badge promotion )
  {
    promotion.setSweepstakesPrimaryWinners( new Integer( giversAmount ) );
    promotion.setSweepstakesPrimaryBasisType( SweepstakesWinnersType.lookup( giversAmountType ) );
    promotion.setSweepstakesPrimaryAwardAmount( new Long( giversAwardTypeAmount ) );

    promotion.setSweepstakesWinnerEligibilityType( SweepstakesWinnerEligibilityType.lookup( eligibleWinners ) );

    if ( eligibleWinners.equals( SweepstakesWinnerEligibilityType.BADGE_RECEIVER_ONLY_CODE ) )
    {
      setPrimaryOnly( promotion );
    }

    return promotion;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }
    if ( !isActive() )
    {
      return actionErrors;
    }
    boolean trackByLoginId = false; // WIP# 25127
    String winnerAssetKey = CmsResourceBundle.getCmsBundle().getString( WINNERS_ASSET_KEY );

    // check for eligibleWinners first,
    if ( !isBaseValidation( actionErrors ) )
    {
      return actionErrors;
    }

    if ( PromotionType.lookup( promotionTypeCode ).isQuizPromotion() )
    {
      if ( !StringUtils.isEmpty( getGiversAmount() ) && Integer.parseInt( getGiversAmount() ) > 100 && PromotionSweepstakesAwardAmountType.PERCENT_CODE.equals( getGiversAmountType() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.PERCENT_OF_ELIGIBLE_EXCEEDS_ERRORS" ) );
      }
      validateRequiredAmount( actionErrors, getGiversAmount(), CmsResourceBundle.getCmsBundle().getString( ELIGIBLE_WINNERS_KEY ) );
      validateRequiredAmount( actionErrors, getGiversAwardTypeAmount(), CmsResourceBundle.getCmsBundle().getString( AWARD_AMOUNT_KEY ) );
    }
    else
    {
      if ( !StringUtils.isEmpty( getEligibleWinners() ) )
      {
        if ( SweepstakesWinnerEligibilityType.lookup( getEligibleWinners() ).isPrimaryOnly() )
        {
          if ( !isAwardLevelPromotion() )
          {
            validateRequiredAmount( actionErrors, getGiversAwardTypeAmount(), getAwardTypeText() );
            validateRequiredAmount( actionErrors, getGiversAmount(), winnerAssetKey );
          }
          else
          {
            // if merch, validate level values
            validateRequiredAmount( actionErrors, getGiversAwardLevel(), winnerAssetKey );
            validateRequiredAmount( actionErrors, getGiversAmount(), winnerAssetKey );
          }

          if ( actionErrors != null && actionErrors.size() > 0 )
          {
            return actionErrors;
          }

          // validation to check if the percent of eligible is > 100 for giver
          if ( PromotionSweepstakesAwardAmountType.PERCENT_CODE.equals( getGiversAmountType() ) )
          {
            if ( getGiversAmount() != null && !getGiversAmount().isEmpty() && Integer.parseInt( getGiversAmount() ) > 100 )
            {
              if ( PromotionType.lookup( promotionTypeCode ).isRecognitionPromotion() )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.GIVERS_PRCNT_ELIGIBLE_EXCEED_ERROR" ) );
              }
              else
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.RCVR_PRCNT_ELIGIBLE_EXCEED_ERRORS" ) );
              }
            }
          }
          // check multipleAwards
          validateMultipleAwards( actionErrors, getMultipleAwardsTrimmed() );
        }
        else if ( SweepstakesWinnerEligibilityType.lookup( getEligibleWinners() ).isSecondaryOnly() )
        {
          if ( PromotionSweepstakesAwardAmountType.PERCENT_CODE.equals( getReceiversAmountType() ) )
          {
            if ( Integer.parseInt( getReceiversAmount() ) > 100 )
            {
              if ( PromotionType.lookup( promotionTypeCode ).isRecognitionPromotion() )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.RCVR_PRCNT_ELIGIBLE_EXCEED_ERRORS" ) );
              }
              else
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.NOMINEES_PRCNT_ELIG_EXCEED_ERRORS" ) );
              }
            }
          }
          if ( !isAwardLevelPromotion() )
          {
            validateRequiredAmount( actionErrors, getReceiversAwardTypeAmount(), getAwardTypeText() );

            validateRequiredAmount( actionErrors, getReceiversAmount(), winnerAssetKey );
          }
          else
          {
            // if merch, validate level values
            validateRequiredAmount( actionErrors, getReceiversAwardLevel(), winnerAssetKey );
          }
          // check multipleAwards
          validateMultipleAwards( actionErrors, getMultipleAwardsTrimmed() );

          if ( actionErrors != null && actionErrors.size() > 0 )
          {
            return actionErrors;
          }
        }
        else if ( SweepstakesWinnerEligibilityType.lookup( getEligibleWinners() ).isPrimarySecondarySeparate() )
        {
          // validation to check if the percent of eligible is > 100 for giver
          if ( PromotionSweepstakesAwardAmountType.PERCENT_CODE.equals( getGiversAmountType() ) )
          {
            if ( Integer.parseInt( getGiversAmount() ) > 100 )
            {
              if ( PromotionType.lookup( promotionTypeCode ).isRecognitionPromotion() )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.GIVERS_PRCNT_ELIGIBLE_EXCEED_ERROR" ) );
              }
              else
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.NOMINATOR_PRCNT_ELIG_EXCEED_ERRORS" ) );
              }
            }
          }
          if ( PromotionSweepstakesAwardAmountType.PERCENT_CODE.equals( getReceiversAmountType() ) )
          {
            if ( Integer.parseInt( getReceiversAmount() ) > 100 )
            {
              if ( PromotionType.lookup( promotionTypeCode ).isRecognitionPromotion() )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.RCVR_PRCNT_ELIGIBLE_EXCEED_ERRORS" ) );
              }
              else
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.NOMINEES_PRCNT_ELIG_EXCEED_ERRORS" ) );
              }
            }
          }
          if ( !isAwardLevelPromotion() && validateRequiredAmount( actionErrors, getGiversAwardTypeAmount(), getAwardTypeText() ) )
          {
            validateRequiredAmount( actionErrors, getReceiversAwardTypeAmount(), getAwardTypeText() );
          }
          // apply level validation if necessary
          if ( isAwardLevelPromotion() && validateRequiredAmount( actionErrors, getGiversAwardLevel(), getAwardTypeText() ) )
          {
            validateRequiredAmount( actionErrors, getReceiversAwardLevel(), getAwardTypeText() );
          }
          // second block
          if ( !isAwardLevelPromotion() && validateRequiredAmount( actionErrors, getGiversAmount(), winnerAssetKey ) )
          {
            validateRequiredAmount( actionErrors, getReceiversAmount(), winnerAssetKey );
          }
          // apply level validation if neccessary
          if ( isAwardLevelPromotion() && validateRequiredAmount( actionErrors, getGiversAmount(), winnerAssetKey ) )
          {
            validateRequiredAmount( actionErrors, getReceiversAmount(), winnerAssetKey );
          }
          // check multipleAwards
          validateMultipleAwards( actionErrors, getMultipleAwards() );

          if ( actionErrors != null && actionErrors.size() > 0 )
          {
            return actionErrors;
          }
        }
        else if ( SweepstakesWinnerEligibilityType.lookup( getEligibleWinners() ).isPrimarySecondaryCombined() )
        {
          // validation to check if the percent of eligible is > 100 for giver
          if ( PromotionSweepstakesAwardAmountType.PERCENT_CODE.equals( getCombinedAmountType() ) )
          {
            if ( Integer.parseInt( getCombinedAmount() ) > 100 )
            {
              if ( PromotionType.lookup( promotionTypeCode ).isRecognitionPromotion() )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.GVR_RCVR_PRCNT_ELIGIBLE_EXCEED_ERRORS" ) );
              }
              else
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.sweepstakes.BOTH_NOM_PRCNT_ELIG_EXCEED_ERRORS" ) );
              }
            }
          }
          if ( !isAwardLevelPromotion() )
          {
            validateRequiredAmount( actionErrors, getCombinedAwardTypeAmount(), getAwardTypeText() );

            validateRequiredAmount( actionErrors, getCombinedAmount(), winnerAssetKey );
          }
          else
          {
            // check for merch levels
            validateRequiredAmount( actionErrors, getCombinedAwardLevel(), winnerAssetKey );
          }
          // check multipleAwards
          validateMultipleAwards( actionErrors, getMultipleAwardsTrimmed() );

          if ( actionErrors != null && actionErrors.size() > 0 )
          {
            return actionErrors;
          }
        }
      }
    }

    if ( PromotionType.lookup( promotionTypeCode ).isSurveyPromotion() )
    {
      if ( StringUtils.isEmpty( getEligibleWinners() ) )
      {
        actionErrors.add( "eligibleWinners",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.sweepstakes.ELIGIBLE_WINNERS" ) ) );
      }
      validateRequiredAmount( actionErrors, getGiversAmount(), winnerAssetKey );
      validateRequiredAmount( actionErrors, getGiversAwardTypeAmount(), getAwardTypeText() );
      // validateMultipleAwards( actionErrors, getMultipleAwardsTrimmed() );
    }

    if ( PromotionType.lookup( promotionTypeCode ).isBadgePromotion() )
    {
      if ( StringUtils.isEmpty( getEligibleWinners() ) )
      {
        actionErrors.add( "eligibleWinners",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.sweepstakes.ELIGIBLE_WINNERS" ) ) );
      }
      validateRequiredAmount( actionErrors, getGiversAmount(), winnerAssetKey );
      validateRequiredAmount( actionErrors, getGiversAwardTypeAmount(), getAwardTypeText() );
      // validateMultipleAwards( actionErrors, getMultipleAwardsTrimmed() );
    }

    if ( actionErrors != null && actionErrors.size() > 0 )
    {
      return actionErrors;
    }

    if ( this.isBillCodesActive() )
    {
      boolean customBillCodeMissing = false;
      int missingBillCodesCount = 0;

      if ( this.billCode1 != null && !this.billCode1.equals( "" ) )
      {
        if ( this.billCode1.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue1.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode1 != null && this.billCode1.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode2 != null && !this.billCode2.equals( "" ) )
      {
        if ( this.billCode2.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue2.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode2 != null && this.billCode2.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode3 != null && !this.billCode3.equals( "" ) )
      {
        if ( this.billCode3.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue3.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode3 != null && this.billCode3.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode4 != null && !this.billCode4.equals( "" ) )
      {
        if ( this.billCode4.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue4.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode4 != null && this.billCode4.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode5 != null && !this.billCode5.equals( "" ) )
      {
        if ( this.billCode5.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue5.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode5 != null && this.billCode5.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode6 != null && !this.billCode6.equals( "" ) )
      {
        if ( this.billCode6.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue6.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode6 != null && this.billCode6.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode7 != null && !this.billCode7.equals( "" ) )
      {
        if ( this.billCode7.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue7.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode7 != null && this.billCode7.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode8 != null && !this.billCode8.equals( "" ) )
      {
        if ( this.billCode8.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue8.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode8 != null && this.billCode8.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode9 != null && !this.billCode9.equals( "" ) )
      {
        if ( this.billCode9.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue9.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode9 != null && this.billCode9.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode10 != null && !this.billCode10.equals( "" ) )
      {
        if ( this.billCode10.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue10.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
        /* WIP# 25127 Start */
        else if ( this.billCode10 != null && this.billCode10.equalsIgnoreCase( "userName" ) )
        {
          trackByLoginId = true;
        }
        /* WIP# 25127 End */
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( missingBillCodesCount == 10 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.bill.code.NO_BILL_CODES" ) );
      }
      else if ( customBillCodeMissing )
      {
        actionErrors.add( "promotionBillCodes",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.bill.code.CUSTOM_BILL_CODE" ) ) );
      }
      else if ( ( promotionTypeCode.equals( PromotionType.RECOGNITION ) || promotionTypeCode.equals( PromotionType.GOALQUEST ) ) && !trackByLoginId )
      {
        actionErrors.add( "promotionBillCodes", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.bill.code.LOGINID_REQ" ) ) );
      }
    }

    return actionErrors;
  }

  private boolean validateRequiredAmount( ActionErrors errors, String amount, String fieldName )
  {
    boolean valid = true;
    if ( StringUtils.isEmpty( amount ) )
    {
      errors.add( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, new ActionMessage( "system.errors.REQUIRED", fieldName ) );
      valid = false;
    }
    else
    {

      int value = 1;
      try
      {
        value = Integer.parseInt( amount );
        if ( value < 1 ) // Per Bug#10952 Award Amount can be zero but not negative
        {
          errors.add( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, new ActionMessage( "promotion.notification.errors.SUB_ZERO", fieldName ) );
          valid = false;
        }
      }
      catch( NumberFormatException e )
      {
        errors.add( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, new ActionMessage( "system.errors.INTEGER", fieldName ) );
        valid = false;
      }
    }
    return valid;
  }

  /**
   * @return promotionId
   */
  public String getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return promotionName
   */
  public String getPromotionName()
  {
    return promotionName;
  }

  /**
   * @param promotionName
   */
  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  /**
   * @param version
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return method
   */
  public String getMethod()
  {
    return this.method;
  }

  public Long getVersion()
  {
    return version;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getCombinedAmount()
  {
    return combinedAmount;
  }

  public void setCombinedAmount( String combinedAmount )
  {
    this.combinedAmount = combinedAmount;
  }

  public String getCombinedAmountType()
  {
    return combinedAmountType;
  }

  public void setCombinedAmountType( String combinedAmountType )
  {
    this.combinedAmountType = combinedAmountType;
  }

  public String getCombinedAwardTypeAmount()
  {
    return combinedAwardTypeAmount;
  }

  public void setCombinedAwardTypeAmount( String combinedAwardTypeAmount )
  {
    this.combinedAwardTypeAmount = combinedAwardTypeAmount;
  }

  public String getEligibleWinners()
  {
    return eligibleWinners;
  }

  public void setEligibleWinners( String eligibleWinners )
  {
    this.eligibleWinners = eligibleWinners;
  }

  public String getGiversAmount()
  {
    return giversAmount;
  }

  public void setGiversAmount( String giversAmount )
  {
    this.giversAmount = giversAmount;
  }

  public String getGiversAmountType()
  {
    return giversAmountType;
  }

  public void setGiversAmountType( String giversAmountType )
  {
    this.giversAmountType = giversAmountType;
  }

  public String getGiversAwardTypeAmount()
  {
    return giversAwardTypeAmount;
  }

  public void setGiversAwardTypeAmount( String giversAwardTypeAmount )
  {
    this.giversAwardTypeAmount = giversAwardTypeAmount;
  }

  public String getMultipleAwards()
  {
    return multipleAwards;
  }

  public void setMultipleAwards( String multipleAwards )
  {
    this.multipleAwards = multipleAwards;
  }

  public String getMultipleAwardsTrimmed()
  {
    return multipleAwardsTrimmed;
  }

  public void setMultipleAwardsTrimmed( String multipleAwardsTrimmed )
  {
    this.multipleAwardsTrimmed = multipleAwardsTrimmed;
  }

  public String getReceiversAmount()
  {
    return receiversAmount;
  }

  public void setReceiversAmount( String receiversAmount )
  {
    this.receiversAmount = receiversAmount;
  }

  public String getReceiversAmountType()
  {
    return receiversAmountType;
  }

  public void setReceiversAmountType( String receiversAmountType )
  {
    this.receiversAmountType = receiversAmountType;
  }

  public String getReceiversAwardTypeAmount()
  {
    return receiversAwardTypeAmount;
  }

  public void setReceiversAwardTypeAmount( String receiversAwardTypeAmount )
  {
    this.receiversAwardTypeAmount = receiversAwardTypeAmount;
  }

  public String getAwardTypeCode()
  {
    return awardTypeCode;
  }

  public void setAwardTypeCode( String awardTypeCode )
  {
    this.awardTypeCode = awardTypeCode;
  }

  public boolean isAwardLevelPromotion()
  {
    return getAwardTypeCode() != null && getAwardTypeCode().equals( "merchandise" ) ? true : false;
  }

  public String getAwardTypeText()
  {
    return awardTypeText;
  }

  public void setAwardTypeText( String awardTypeText )
  {
    this.awardTypeText = awardTypeText;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getEligibleClaims()
  {
    return eligibleClaims;
  }

  public void setEligibleClaims( String eligibleClaims )
  {
    this.eligibleClaims = eligibleClaims;
  }

  public String getCombinedAwardLevel()
  {
    return combinedAwardLevel;
  }

  public void setCombinedAwardLevel( String combinedAwardLevel )
  {
    this.combinedAwardLevel = combinedAwardLevel;
  }

  public String getGiversAwardLevel()
  {
    return giversAwardLevel;
  }

  public void setGiversAwardLevel( String giversAwardLevel )
  {
    this.giversAwardLevel = giversAwardLevel;
  }

  public String getReceiversAwardLevel()
  {
    return receiversAwardLevel;
  }

  public void setReceiversAwardLevel( String receiversAwardLevel )
  {
    this.receiversAwardLevel = receiversAwardLevel;
  }

  /**
   * To validate the basic fields, like eligibleWinners/eligibleClaims dropdowns
   * 
   * @param errors
   * @return boolean
   */
  private boolean isBaseValidation( ActionErrors errors )
  {
    boolean hasErrors = false;
    String eligibleWinnersKey = CmsResourceBundle.getCmsBundle().getString( ELIGIBLE_WINNERS_KEY );

    if ( PromotionType.lookup( promotionTypeCode ).isRecognitionPromotion() || PromotionType.lookup( promotionTypeCode ).isProductClaimPromotion()
        || PromotionType.lookup( promotionTypeCode ).isNominationPromotion() )
    {
      // only for cliam promoion
      if ( PromotionType.lookup( promotionTypeCode ).isProductClaimPromotion() )
      {
        // check for eligibleClaims
        if ( StringUtils.isEmpty( getEligibleClaims() ) )
        {
          String eligibleClaimsKey = CmsResourceBundle.getCmsBundle().getString( ELIGIBLE_CLAIMS_KEY );

          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", eligibleClaimsKey ) );
        }
      }
      if ( StringUtils.isEmpty( getEligibleWinners() ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", eligibleWinnersKey ) );
      }
    }

    // check for errors
    if ( errors.isEmpty() )
    {
      hasErrors = true;
    }

    return hasErrors;
  }

  /**
   * To validate the multipleAwards/multipleAwardsTrimmed drop-downs
   * 
   * @param actionErrors
   * @param multipleAwardsOrTrim
   */
  private void validateMultipleAwards( ActionErrors actionErrors, String multipleAwardsOrTrim )
  {
    if ( StringUtils.isEmpty( multipleAwardsOrTrim ) )
    {
      String multipleAwardsKey = CmsResourceBundle.getCmsBundle().getString( MULTIPLE_AWARDS_KEY );
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", multipleAwardsKey ) );
    }
  }

  private List<SweepstakesBillCode> getPromoBillCodeList( Promotion promotion )
  {
    List<SweepstakesBillCode> promoBillCodes = new ArrayList<SweepstakesBillCode>();

    if ( this.billCode1 != null && !this.billCode1.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 0 ), this.billCode1, this.customValue1 ) );
    }
    if ( this.billCode2 != null && !this.billCode2.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 1 ), this.billCode2, this.customValue2 ) );
    }
    if ( this.billCode3 != null && !this.billCode3.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 2 ), this.billCode3, this.customValue3 ) );
    }
    if ( this.billCode4 != null && !this.billCode4.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 3 ), this.billCode4, this.customValue4 ) );
    }
    if ( this.billCode5 != null && !this.billCode5.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 4 ), this.billCode5, this.customValue5 ) );
    }
    if ( this.billCode6 != null && !this.billCode6.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 5 ), this.billCode6, this.customValue6 ) );
    }
    if ( this.billCode7 != null && !this.billCode7.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 6 ), this.billCode7, this.customValue7 ) );
    }
    if ( this.billCode8 != null && !this.billCode8.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 7 ), this.billCode8, this.customValue8 ) );
    }
    if ( this.billCode9 != null && !this.billCode9.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 8 ), this.billCode9, this.customValue9 ) );
    }
    if ( this.billCode10 != null && !this.billCode10.equals( "" ) )
    {
      promoBillCodes.add( new SweepstakesBillCode( promotion, new Long( 9 ), this.billCode10, this.customValue10 ) );
    }
    return promoBillCodes;
  }

  private void loadPromotionBillCodes( List<SweepstakesBillCode> promotionBillCodes )
  {
    if ( promotionBillCodes != null && promotionBillCodes.size() > 0 )
    {
      Iterator promotionBillCodesList = promotionBillCodes.iterator();
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode1 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode1 != null )
        {
          this.billCode1 = promotionBillCode1.getBillCode();
          this.customValue1 = promotionBillCode1.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode2 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode2 != null )
        {
          this.billCode2 = promotionBillCode2.getBillCode();
          this.customValue2 = promotionBillCode2.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode3 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode3 != null )
        {
          this.billCode3 = promotionBillCode3.getBillCode();
          this.customValue3 = promotionBillCode3.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode4 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode4 != null )
        {
          this.billCode4 = promotionBillCode4.getBillCode();
          this.customValue4 = promotionBillCode4.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode5 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode5 != null )
        {
          this.billCode5 = promotionBillCode5.getBillCode();
          this.customValue5 = promotionBillCode5.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode6 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode6 != null )
        {
          this.billCode6 = promotionBillCode6.getBillCode();
          this.customValue6 = promotionBillCode6.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode7 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode7 != null )
        {
          this.billCode7 = promotionBillCode7.getBillCode();
          this.customValue7 = promotionBillCode7.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode8 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode8 != null )
        {
          this.billCode8 = promotionBillCode8.getBillCode();
          this.customValue8 = promotionBillCode8.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode9 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode9 != null )
        {
          this.billCode9 = promotionBillCode9.getBillCode();
          this.customValue9 = promotionBillCode9.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        SweepstakesBillCode promotionBillCode10 = (SweepstakesBillCode)promotionBillCodesList.next();
        if ( promotionBillCode10 != null )
        {
          this.billCode10 = promotionBillCode10.getBillCode();
          this.customValue10 = promotionBillCode10.getCustomValue();
        }
      }
    }
  }

  public boolean isActiveNotEditable()
  {
    return activeNotEditable;
  }

  public void setActiveNotEditable( boolean activeEditable )
  {
    this.activeNotEditable = activeEditable;
  }

  public boolean isBillCodesActive()
  {
    return billCodesActive;
  }

  public void setBillCodesActive( boolean billCodesActive )
  {
    this.billCodesActive = billCodesActive;
  }

  public String getBillCode1()
  {
    return billCode1;
  }

  public void setBillCode1( String billCode1 )
  {
    this.billCode1 = billCode1;
  }

  public String getCustomValue1()
  {
    return customValue1;
  }

  public void setCustomValue1( String customValue1 )
  {
    this.customValue1 = customValue1;
  }

  public String getBillCode2()
  {
    return billCode2;
  }

  public void setBillCode2( String billCode2 )
  {
    this.billCode2 = billCode2;
  }

  public String getCustomValue2()
  {
    return customValue2;
  }

  public void setCustomValue2( String customValue2 )
  {
    this.customValue2 = customValue2;
  }

  public String getBillCode3()
  {
    return billCode3;
  }

  public void setBillCode3( String billCode3 )
  {
    this.billCode3 = billCode3;
  }

  public String getCustomValue3()
  {
    return customValue3;
  }

  public void setCustomValue3( String customValue3 )
  {
    this.customValue3 = customValue3;
  }

  public String getBillCode4()
  {
    return billCode4;
  }

  public void setBillCode4( String billCode4 )
  {
    this.billCode4 = billCode4;
  }

  public String getCustomValue4()
  {
    return customValue4;
  }

  public void setCustomValue4( String customValue4 )
  {
    this.customValue4 = customValue4;
  }

  public String getBillCode5()
  {
    return billCode5;
  }

  public void setBillCode5( String billCode5 )
  {
    this.billCode5 = billCode5;
  }

  public String getCustomValue5()
  {
    return customValue5;
  }

  public void setCustomValue5( String customValue5 )
  {
    this.customValue5 = customValue5;
  }

  public String getBillCode6()
  {
    return billCode6;
  }

  public void setBillCode6( String billCode6 )
  {
    this.billCode6 = billCode6;
  }

  public String getCustomValue6()
  {
    return customValue6;
  }

  public void setCustomValue6( String customValue6 )
  {
    this.customValue6 = customValue6;
  }

  public String getBillCode7()
  {
    return billCode7;
  }

  public void setBillCode7( String billCode7 )
  {
    this.billCode7 = billCode7;
  }

  public String getCustomValue7()
  {
    return customValue7;
  }

  public void setCustomValue7( String customValue7 )
  {
    this.customValue7 = customValue7;
  }

  public String getBillCode8()
  {
    return billCode8;
  }

  public void setBillCode8( String billCode8 )
  {
    this.billCode8 = billCode8;
  }

  public String getCustomValue8()
  {
    return customValue8;
  }

  public void setCustomValue8( String customValue8 )
  {
    this.customValue8 = customValue8;
  }

  public String getBillCode9()
  {
    return billCode9;
  }

  public void setBillCode9( String billCode9 )
  {
    this.billCode9 = billCode9;
  }

  public String getCustomValue9()
  {
    return customValue9;
  }

  public void setCustomValue9( String customValue9 )
  {
    this.customValue9 = customValue9;
  }

  public String getBillCode10()
  {
    return billCode10;
  }

  public void setBillCode10( String billCode10 )
  {
    this.billCode10 = billCode10;
  }

  public String getCustomValue10()
  {
    return customValue10;
  }

  public void setCustomValue10( String customValue10 )
  {
    this.customValue10 = customValue10;
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }
}
