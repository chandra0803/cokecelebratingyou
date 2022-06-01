
package com.biperf.core.ui.participant;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.promotion.MatchTeamProgress;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ParticipantThrowdownProgressForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private Long userId;
  private Long promotionId;
  private String method;
  private String addReplaceType;
  private String newQuantity;
  private int numberOfRounds;
  private MatchTeamProgress matchTeamProgress;
  private int roundNumber;

  /**
   * Overridden from @see com.biperf.core.ui.BaseForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }
    if ( promotionId != null && promotionId.longValue() > 0 )
    {
      if ( this.roundNumber == 0 )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.throwdown.promo.detail.PROMOTION_ROUND_NUMBER" ) ) );
      }

      if ( this.newQuantity == null || this.newQuantity.equals( "" ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.throwdown.promo.detail.NEW_QUANTITY" ) ) );
      }
      else
      {
        boolean validProgress = validateProgress( this.newQuantity );
        if ( !validProgress )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.NONNUMERIC", CmsResourceBundle.getCmsBundle().getString( "participant.throwdown.promo.detail.NEW_QUANTITY" ) ) );
        }
      }
      if ( this.addReplaceType == null || this.addReplaceType.equals( "" ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.throwdown.promo.detail.LOAD_TYPE" ) ) );
      }
    }
    return errors;
  }

  public boolean validateProgress( String minimumQualifier )
  {
    BigDecimal numericValue = getNumericValue( minimumQualifier );
    return numericValue != null;
  }

  protected BigDecimal getNumericValue( String string )
  {
    BigDecimal numericValue = null;
    try
    {
      if ( org.apache.commons.lang3.StringUtils.isNotEmpty( string ) )
      {
        numericValue = NumberUtils.createBigDecimal( string );
      }
    }
    catch( NumberFormatException nfe )
    {
    }
    return numericValue;
  }

  public String getAddReplaceType()
  {
    return addReplaceType;
  }

  public void setAddReplaceType( String addReplaceType )
  {
    this.addReplaceType = addReplaceType;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getNewQuantity()
  {
    return newQuantity;
  }

  public void setNewQuantity( String newQuantity )
  {
    this.newQuantity = newQuantity;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public void clearValues()
  {
    this.newQuantity = null;
    this.addReplaceType = null;
  }

  public int getNumberOfRounds()
  {
    return numberOfRounds;
  }

  public void setNumberOfRounds( int numberOfRounds )
  {
    this.numberOfRounds = numberOfRounds;
  }

  public MatchTeamProgress getMatchTeamProgress()
  {
    return matchTeamProgress;
  }

  public void setMatchTeamProgress( MatchTeamProgress matchTeamProgress )
  {
    this.matchTeamProgress = matchTeamProgress;
  }

  public int getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( int roundNumber )
  {
    this.roundNumber = roundNumber;
  }

}
