/*
 * (c) 2005 BI, Inc. All rights reserved. $Source:
 * /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionPayoutForm.java,v $
 */

package com.biperf.core.ui.promotion;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.RoundingMethod;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ChallengePointPayoutForm <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>meadows</td>
 * <td>December 15, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengePointPayoutForm extends PromotionGoalPayoutForm
{
  private static final long serialVersionUID = 1L;

  private String awardThreshold = ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_NONE;
  private String awardIncrement = ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_FIXED;
  private String awardThresholdFixedAmount;
  private String awardThresholdPctAmount;
  private String awardIncrementFixedAmount;
  private String awardIncrementPctBaseAmount;
  private String awardIncrementPctThreshAmount;
  private String primaryAwardPerIncrement;
  private String challengepointAwardType;
  private String challengePointAwardTypeCode;

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    while ( getGoalLevelValueListSize() < 3 )
    {
      addEmptyGoalLevel();
    }
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

    if ( getAwardThreshold() != null && !getAwardThreshold().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_NONE ) )
    {
      if ( getAwardThreshold().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_FIXED ) && !validatePositiveInteger( getAwardThresholdFixedAmount() ) )
      {
        actionErrors.add( "awardThreshold", new ActionMessage( "promotion.payout.challengepoint.errors.AWARD_THRESHOLD_VALUE" ) );
      }
      if ( getAwardThreshold().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_PCT_BASE ) && !validatePositiveInteger( getAwardThresholdPctAmount() ) )
      {
        actionErrors.add( "awardThreshold", new ActionMessage( "promotion.payout.challengepoint.errors.AWARD_THRESHOLD_VALUE" ) );
      }
    }

    if ( getAwardIncrement() != null && !getAwardIncrement().equals( "" ) )
    {
      if ( getAwardIncrement().equals( ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_FIXED ) && !validatePositiveInteger( getAwardIncrementFixedAmount() ) )
      {
        actionErrors.add( "awardIncrement", new ActionMessage( "promotion.payout.challengepoint.errors.AWARD_INCREMENT_VALUE" ) );
      }
      if ( getAwardIncrement().equals( ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_PCT_BASE ) && !validatePositiveInteger( getAwardIncrementPctBaseAmount() ) )
      {
        actionErrors.add( "awardIncrement", new ActionMessage( "promotion.payout.challengepoint.errors.AWARD_INCREMENT_VALUE" ) );
      }
      if ( getAwardIncrement().equals( ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_PCT_THRS ) && !validatePositiveInteger( getAwardIncrementPctThreshAmount() ) )
      {
        actionErrors.add( "awardIncrement", new ActionMessage( "promotion.payout.challengepoint.errors.AWARD_INCREMENT_VALUE" ) );
      }
    }
    else
    {
      actionErrors.add( "awardIncrement", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.payout.challengepoint", "AWARD_INCREMENT" ) ) );
    }

    if ( getPrimaryAwardPerIncrement() == null || !validatePositiveInteger( getPrimaryAwardPerIncrement() ) )
    {
      actionErrors.add( "primaryAwardPerIncrement", new ActionMessage( "promotion.payout.challengepoint.errors.AWARD_PER_INCREMENT" ) );
    }

    if ( StringUtils.isBlank( getAchievementRuleTypeCode() ) )
    {
      actionErrors.add( "achievementRuleTypeCode",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.payout.challengepoint", "ACHIEVEMENT_RULE" ) ) );
    }

    if ( StringUtils.isBlank( getRoundingMethod() ) )
    {
      actionErrors.add( "roundingMethod", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.payout.challengepoint", "ROUNDING_METHOD" ) ) );
    }

    if ( !StringUtils.isBlank( getBaseUnit() ) && StringUtils.isBlank( getBaseUnitPosition() ) )
    {
      actionErrors.add( "baseUnitPosition",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.payout.challengepoint", "UNIT_LABEL_POSITION" ) ) );
    }
    return actionErrors;
  }

  /**
   * Loads the promotion and any goal levels
   * 
   * @param promo
   */
  public void loadPromotion( Promotion promo )
  {
    super.loadPromotion( promo );

    ChallengePointPromotion promotion = (ChallengePointPromotion)promo;

    this.awardThreshold = promotion.getAwardThresholdType();
    if ( this.awardThreshold == null )
    {
      this.awardThreshold = ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_NONE;
    }
    if ( this.awardThreshold != null && !this.awardThreshold.equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_NONE ) )
    {
      if ( promotion.getAwardThresholdValue() != null )
      {
        if ( this.awardThreshold.equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_FIXED ) )
        {
          this.awardThresholdFixedAmount = promotion.getAwardThresholdValue().toString();
        }
        else
        {
          this.awardThresholdPctAmount = promotion.getAwardThresholdValue().toString();
        }
      }
    }
    if ( promotion.getAwardIncrementType() != null && !promotion.getAwardIncrementType().equals( "" ) )
    {
      this.awardIncrement = promotion.getAwardIncrementType();
      if ( promotion.getAwardIncrementValue() != null )
      {
        if ( this.awardIncrement.equals( ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_FIXED ) )
        {
          this.awardIncrementFixedAmount = promotion.getAwardIncrementValue().toString();
        }
        else if ( this.awardIncrement.equals( ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_PCT_BASE ) )
        {
          this.awardIncrementPctBaseAmount = promotion.getAwardIncrementValue().toString();
        }
        else
        {
          this.awardIncrementPctThreshAmount = promotion.getAwardIncrementValue().toString();
        }
      }
    }

    if ( promotion.getAwardPerIncrement() != null )
    {
      this.primaryAwardPerIncrement = promotion.getAwardPerIncrement().toString();
    }

    if ( promotion.getChallengePointAwardType() != null )
    {
      this.challengePointAwardTypeCode = promotion.getChallengePointAwardType().getCode();
      this.challengepointAwardType = promotion.getChallengePointAwardType().getName();
    }

  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Promotion
   */
  @Override
  public Promotion toDomainObject()
  {
    // Create a new Promotion since one was not passed in
    return toDomainObject( new ChallengePointPromotion() );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @param promotion
   * @return Promotion
   */
  public Promotion toDomainObject( ChallengePointPromotion promotion )
  {
    promotion = (ChallengePointPromotion)super.toDomainObject( promotion );

    if ( getAchievementPrecision().equals( AchievementPrecision.ZERO ) )
    {
      promotion.setRoundingMethod( RoundingMethod.lookup( RoundingMethod.STANDARD ) );
    }
    else
    {
      promotion.setRoundingMethod( RoundingMethod.lookup( getRoundingMethod() ) );
    }
    promotion.setAwardThresholdType( this.getAwardThreshold() );
    if ( this.getAwardThreshold() != null && !this.getAwardThreshold().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_NONE ) )
    {
      if ( this.getAwardThreshold().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_FIXED ) )
      {
        if ( this.getAwardThresholdFixedAmount() != null && !this.getAwardThresholdFixedAmount().equals( "" ) )
        {
          promotion.setAwardThresholdValue( new Integer( this.getAwardThresholdFixedAmount() ) );
        }
      }
      else
      {
        if ( this.getAwardThresholdPctAmount() != null && !this.getAwardThresholdPctAmount().equals( "" ) )
        {
          promotion.setAwardThresholdValue( new Integer( this.getAwardThresholdPctAmount() ) );
        }
      }
    }
    promotion.setAwardIncrementType( this.getAwardIncrement() );
    if ( this.getAwardIncrement() != null && !this.getAwardIncrement().equals( "" ) )
    {
      if ( this.getAwardIncrement().equals( ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_FIXED ) )
      {
        if ( this.getAwardIncrementFixedAmount() != null && !this.getAwardIncrementFixedAmount().equals( "" ) )
        {
          promotion.setAwardIncrementValue( new Integer( this.getAwardIncrementFixedAmount() ) );
        }
      }
      else if ( this.getAwardIncrement().equals( ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_PCT_BASE ) )
      {
        if ( this.getAwardIncrementPctBaseAmount() != null && !this.getAwardIncrementPctBaseAmount().equals( "" ) )
        {
          promotion.setAwardIncrementValue( new Integer( this.getAwardIncrementPctBaseAmount() ) );
        }
      }
      else
      {
        if ( this.getAwardIncrementPctThreshAmount() != null && !this.getAwardIncrementPctThreshAmount().equals( "" ) )
        {
          promotion.setAwardIncrementValue( new Integer( this.getAwardIncrementPctThreshAmount() ) );
        }
      }
    }
    if ( this.getPrimaryAwardPerIncrement() != null && !this.getPrimaryAwardPerIncrement().equals( "" ) )
    {
      promotion.setAwardPerIncrement( new Integer( this.getPrimaryAwardPerIncrement() ) );
    }

    return promotion;
  }

  public String getAwardIncrement()
  {
    return awardIncrement;
  }

  public void setAwardIncrement( String awardIncrement )
  {
    this.awardIncrement = awardIncrement;
  }

  public String getAwardThreshold()
  {
    return awardThreshold;
  }

  public void setAwardThreshold( String awardThreshold )
  {
    this.awardThreshold = awardThreshold;
  }

  public String getPrimaryAwardPerIncrement()
  {
    return primaryAwardPerIncrement;
  }

  public void setPrimaryAwardPerIncrement( String primaryAwardPerIncrement )
  {
    this.primaryAwardPerIncrement = primaryAwardPerIncrement;
  }

  public String getAwardThresholdFixedAmount()
  {
    return awardThresholdFixedAmount;
  }

  public void setAwardThresholdFixedAmount( String awardThresholdFixedAmount )
  {
    this.awardThresholdFixedAmount = awardThresholdFixedAmount;
  }

  public String getAwardThresholdPctAmount()
  {
    return awardThresholdPctAmount;
  }

  public void setAwardThresholdPctAmount( String awardThresholdPctAmount )
  {
    this.awardThresholdPctAmount = awardThresholdPctAmount;
  }

  public String getAwardIncrementFixedAmount()
  {
    return awardIncrementFixedAmount;
  }

  public void setAwardIncrementFixedAmount( String awardIncrementFixedAmount )
  {
    this.awardIncrementFixedAmount = awardIncrementFixedAmount;
  }

  public String getAwardIncrementPctBaseAmount()
  {
    return awardIncrementPctBaseAmount;
  }

  public void setAwardIncrementPctBaseAmount( String awardIncrementPctBaseAmount )
  {
    this.awardIncrementPctBaseAmount = awardIncrementPctBaseAmount;
  }

  public String getAwardIncrementPctThreshAmount()
  {
    return awardIncrementPctThreshAmount;
  }

  public void setAwardIncrementPctThreshAmount( String awardIncrementPctThreshAmount )
  {
    this.awardIncrementPctThreshAmount = awardIncrementPctThreshAmount;
  }

  public String getChallengepointAwardType()
  {
    return challengepointAwardType;
  }

  public void setChallengepointAwardType( String challengepointAwardType )
  {
    this.challengepointAwardType = challengepointAwardType;
  }

  public String getChallengePointAwardTypeCode()
  {
    return challengePointAwardTypeCode;
  }

  public void setChallengePointAwardTypeCode( String challengePointAwardTypeCode )
  {
    this.challengePointAwardTypeCode = challengePointAwardTypeCode;
  }

}
