/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.ManagerWebRulesAudienceType;
import com.biperf.core.domain.enums.PartnerWebRulesAudienceType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.WebRulesAudienceType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionWebRulesForm.
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
 * <td>asondgeroth</td>
 * <td>Jul 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionWebRulesForm extends BaseForm
{
  public static final String SESSION_KEY = "promotionWebRulesForm";

  private String[] deletePromotionWebRulesAudience;
  private String promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;
  private String audience;
  private String audienceId;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private boolean active;
  private String webRulesText;
  private Long version;

  private boolean sameAsPrimaryAudience;
  private boolean allActivePaxAudience;
  private boolean expired;

  // parent data
  private boolean hasParent;
  private String parentStartDate;
  private String parentEndDate;

  private String managerAudience;
  private String managerAudienceId;
  private String managerWebRulesText;
  private boolean managerAsPrimaryAudience;
  private boolean allActiveManagerAudience;
  private String[] deletePromotionManagerWebRulesAudience;

  private String partnerAudience;
  private String partnerAudienceId;
  private String partnerWebRulesText;
  private boolean partnerAsPrimaryAudience;
  private boolean allActivePartnerAudience;
  private String[] deletePromotionPartnerWebRulesAudience;

  public boolean partnerAvailable;

  private String method;

  public void load( Promotion promotion )
  {
    this.promotionId = promotion.getId().toString();
    this.promotionName = promotion.getName();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.active = promotion.isWebRulesActive();
    this.version = promotion.getVersion();
    this.expired = promotion.isExpired();

    this.hasParent = promotion.hasParent();
    if ( hasParent )
    {
      ProductClaimPromotion parentPromotion = ( (ProductClaimPromotion)promotion ).getParentPromotion();

      if ( parentPromotion.getSubmissionStartDate() != null )
      {
        this.parentStartDate = DateUtils.toDisplayString( parentPromotion.getSubmissionStartDate() );
      }

      if ( parentPromotion.getSubmissionEndDate() != null )
      {
        this.parentEndDate = DateUtils.toDisplayString( parentPromotion.getSubmissionEndDate() );
      }
    }

    if ( promotion.getWebRulesAudienceType() != null )
    {
      setAudience( promotion.getWebRulesAudienceType().getCode() );
    }
    else
    {
      setAudience( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE );
    }

    if ( promotion.getWebRulesStartDate() != null )
    {
      this.startDate = DateUtils.toDisplayString( promotion.getWebRulesStartDate() );
    }
    if ( promotion.getWebRulesEndDate() != null )
    {
      this.endDate = DateUtils.toDisplayString( promotion.getWebRulesEndDate() );
    }

    if ( promotion.getCmAssetCode() != null && promotion.getWebRulesCmKey() != null )
    {
      this.webRulesText = CmsResourceBundle.getCmsBundle().getString( promotion.getCmAssetCode(), promotion.getWebRulesCmKey() );
    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion gqPromotion = (GoalQuestPromotion)promotion;
      if ( gqPromotion.getManagerCmAssetCode() != null && gqPromotion.getManagerWebRulesCmKey() != null )
      {
        this.managerWebRulesText = CmsResourceBundle.getCmsBundle().getString( gqPromotion.getManagerCmAssetCode(), gqPromotion.getManagerWebRulesCmKey() );
      }

      if ( gqPromotion.getManagerWebRulesAudienceType() != null )
      {
        setManagerAudience( gqPromotion.getManagerWebRulesAudienceType().getCode() );
      }
      else
      {
        setManagerAudience( ManagerWebRulesAudienceType.ALL_ACTIVE_PAX_CODE );
      }

      if ( promotion.getPartnerAudienceType() != null )
      {
        this.partnerAvailable = isPartnerAvailable( promotion );
        if ( gqPromotion.getPartnerCmAssetCode() != null && gqPromotion.getPartnerWebRulesCmKey() != null )
        {
          this.partnerWebRulesText = CmsResourceBundle.getCmsBundle().getString( gqPromotion.getPartnerCmAssetCode(), gqPromotion.getPartnerWebRulesCmKey() );
        }

        if ( gqPromotion.getPartnerWebRulesAudienceType() != null )
        {
          setPartnerAudience( gqPromotion.getPartnerWebRulesAudienceType().getCode() );
        }
        else
        {
          setPartnerAudience( PartnerWebRulesAudienceType.ALL_ACTIVE_PAX_CODE );
        }
      }
    }
  }

  /**
   * Builds the promotionWebRules from the information on this.
   * 
   * @param promotion
   * @param errors
   */
  public void buildPromotionWebRules( Promotion promotion, ActionMessages errors )
  {

    promotion.setWebRulesActive( this.isActive() );
    promotion.setVersion( this.getVersion() );

    if ( promotion.isWebRulesActive() )
    {
      promotion.setWebRulesAudienceType( WebRulesAudienceType.lookup( this.audience ) );
      if ( this.getStartDate() != null && !this.getStartDate().equals( "" ) )
      {
        promotion.setWebRulesStartDate( DateUtils.toDate( this.startDate ) );

        if ( this.endDate != null && this.endDate.length() > 0 )
        {
          if ( DateUtils.toDate( this.startDate ).before( DateUtils.toDate( this.endDate ) ) )
          {
            promotion.setWebRulesEndDate( DateUtils.toDate( this.endDate ) );
          }
          else
          {
            // The date is before the start date
            errors.add( "endDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE_RANGE ) );
          }
        }
        else
        {
          promotion.setWebRulesEndDate( null );
        }
      }

      if ( promotion.isGoalQuestOrChallengePointPromotion() )
      {
        ( (GoalQuestPromotion)promotion ).setManagerWebRulesAudienceType( ManagerWebRulesAudienceType.lookup( this.managerAudience ) );
        if ( isPartnerAvailable( promotion ) )
        {
          if ( this.partnerWebRulesText == null || StringUtils.isBlank( this.partnerWebRulesText ) )
          {
            errors.add( partnerWebRulesText, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.webrules.RULES_TEXT" ) ) );
          }
          else
          {
            ( (GoalQuestPromotion)promotion ).setPartnerWebRulesAudienceType( PartnerWebRulesAudienceType.lookup( this.partnerAudience ) );
          }
        }
      }
    } // end if(isActive)
  }

  /**
   * Validate the form before submitting Overridden from Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param actionMapping
   * @param request
   * @return ActionErrors
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

    // if web rules are active, do date validations
    if ( this.active )
    {
      if ( !this.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) && !this.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT )
          && !this.getPromotionTypeCode().equals( PromotionType.THROWDOWN ) )
      {
        Date dateStart = null;
        Date dateEnd = null;
        Date parentStart = null;
        Date parentEnd = null;

        SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
        sdf.setLenient( false );
        // *** validate start date ***
        if ( startDate == null || startDate.length() == 0 )
        {
          actionErrors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.webrules.START" ) ) );
        }
        else
        {
          // Now validate the start date
          try
          {
            dateStart = sdf.parse( startDate );
          }
          catch( ParseException e )
          {
            actionErrors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.webrules.START" ) ) );
          }
        }

        // *** validate end date ***
        if ( endDate != null && endDate.length() > 0 )
        {
          try
          {
            dateEnd = sdf.parse( endDate );
            if ( dateEnd.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
            {
              // The date is before current date
              actionErrors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_TO_DATE ) );
            }
            if ( dateStart != null && dateEnd.before( dateStart ) )
            {
              // The date is before the start date
              actionErrors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE_RANGE ) );
            }

          }
          catch( ParseException e )
          {
            actionErrors.add( "endDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.webrules.END" ) ) );
          }
        }

        // if an error refers to more than one field leave it global errors
        // If the promotion has a parent, then make sure the child dates come within the range
        // of the parent dates
        if ( hasParent )
        {
          try
          {
            parentStart = sdf.parse( parentStartDate );

            // Child start date can't be before parent start date
            if ( dateStart != null && dateStart.before( parentStart ) )
            {
              actionErrors.add( "startDate", new ActionMessage( "promotion.basics.errors.CHILD_DATE_RANGE" ) );
            }
          }
          catch( ParseException e1 )
          {
            // This shouldn't ever happen, since a parent promotion must be completed
            actionErrors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PARENT_START" ) ) );
          }

          // If the parent has end dates, then check the child date ranges
          if ( parentEndDate != null && parentEndDate.length() > 0 )
          {
            try
            {
              parentEnd = sdf.parse( parentEndDate );

              // Child start date can't be after parent end date
              if ( dateStart != null && dateStart.after( parentEnd ) )
              {
                actionErrors.add( "startDate", new ActionMessage( "promotion.basics.errors.CHILD_DATE_RANGE" ) );
              }

              // If the child has an end date, check it against the parent end date
              if ( endDate != null && endDate.length() > 0 )
              {
                // Child end date can't be after parent end date
                if ( dateEnd != null && dateEnd.after( parentEnd ) )
                {
                  actionErrors.add( "endDate", new ActionMessage( "promotion.basics.errors.CHILD_DATE_RANGE" ) );
                }
              }
            }
            catch( ParseException e1 )
            {
              // This shouldn't ever happen, since a parent promotion must be completed
              actionErrors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PARENT_END" ) ) );
            }
          }
        } // end if(hasParent)
      }
      if ( this.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) || this.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
      {
        if ( this.managerWebRulesText != null && StringUtils.isBlank( this.managerWebRulesText ) )
        {
          actionErrors.add( managerWebRulesText, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.webrules.RULES_TEXT" ) ) );
        }
      }
    } // if web rules are active
    return actionErrors;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String[] getDeletePromotionWebRulesAudience()
  {
    return this.deletePromotionWebRulesAudience;
  }

  public void setDeletePromotionWebRulesAudience( String[] deletePromotionWebRulesAudience )
  {
    this.deletePromotionWebRulesAudience = deletePromotionWebRulesAudience;
  }

  public String getAudienceId()
  {
    return audienceId;
  }

  public void setAudienceId( String audienceId )
  {
    this.audienceId = audienceId;
  }

  public String getAudience()
  {
    return audience;
  }

  public void setAudience( String audience )
  {
    this.audience = audience;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public boolean isAllActivePaxAudience()
  {
    return allActivePaxAudience;
  }

  public boolean getAllActivePaxAudience()
  {
    return allActivePaxAudience;
  }

  public void setAllActivePaxAudience( boolean allActivePaxAudience )
  {
    this.allActivePaxAudience = allActivePaxAudience;
  }

  public boolean isSameAsPrimaryAudience()
  {
    return sameAsPrimaryAudience;
  }

  public boolean getSameAsPrimaryAudience()
  {
    return sameAsPrimaryAudience;
  }

  public void setSameAsPrimaryAudience( boolean sameAsPrimaryAudience )
  {
    this.sameAsPrimaryAudience = sameAsPrimaryAudience;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getWebRulesText()
  {
    return webRulesText;
  }

  public void setWebRulesText( String webRulesText )
  {
    this.webRulesText = webRulesText;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMethod()
  {
    return this.method;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public boolean isExpired()
  {
    return expired;
  }

  public void setExpired( boolean expired )
  {
    this.expired = expired;
  }

  public boolean isHasParent()
  {
    return hasParent;
  }

  public void setHasParent( boolean hasParent )
  {
    this.hasParent = hasParent;
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

  public String getParentEndDate()
  {
    return parentEndDate;
  }

  public void setParentEndDate( String parentEndDate )
  {
    this.parentEndDate = parentEndDate;
  }

  public String getParentStartDate()
  {
    return parentStartDate;
  }

  public void setParentStartDate( String parentStartDate )
  {
    this.parentStartDate = parentStartDate;
  }

  public static String getJsString( String input )
  {
    String output = null;
    if ( input != null )
    {
      output = input.replace( "\'", "\\'" );
      output = output.replace( "\"", "\\\"" );
      output = output.replace( "\r", "\\r" );
      output = output.replace( "\n", "\\n" );
    }
    return output;
  }

  public boolean isPartnerAvailable( Promotion promotion )
  {
    if ( promotion.getPartnerAudienceType() != null )
    {
      partnerAvailable = true;
    }
    else
    {
      partnerAvailable = false;
    }
    return partnerAvailable;
  }

  public boolean getPartnerAvailable()
  {
    return partnerAvailable;
  }

  public void setPartnerAvailable( boolean partnerAvailable )
  {
    this.partnerAvailable = partnerAvailable;
  }

  public String getManagerAudience()
  {
    return managerAudience;
  }

  public void setManagerAudience( String managerAudience )
  {
    this.managerAudience = managerAudience;
  }

  public String getManagerAudienceId()
  {
    return managerAudienceId;
  }

  public void setManagerAudienceId( String managerAudienceId )
  {
    this.managerAudienceId = managerAudienceId;
  }

  public String getManagerWebRulesText()
  {
    return managerWebRulesText;
  }

  public void setManagerWebRulesText( String managerWebRulesText )
  {
    this.managerWebRulesText = managerWebRulesText;
  }

  public boolean isManagerAsPrimaryAudience()
  {
    return managerAsPrimaryAudience;
  }

  public void setManagerAsPrimaryAudience( boolean managerAsPrimaryAudience )
  {
    this.managerAsPrimaryAudience = managerAsPrimaryAudience;
  }

  public boolean isAllActiveManagerAudience()
  {
    return allActiveManagerAudience;
  }

  public void setAllActiveManagerAudience( boolean allActiveManagerAudience )
  {
    this.allActiveManagerAudience = allActiveManagerAudience;
  }

  public String[] getDeletePromotionManagerWebRulesAudience()
  {
    return deletePromotionManagerWebRulesAudience;
  }

  public void setDeletePromotionManagerWebRulesAudience( String[] deletePromotionManagerWebRulesAudience )
  {
    this.deletePromotionManagerWebRulesAudience = deletePromotionManagerWebRulesAudience;
  }

  public String getPartnerAudience()
  {
    return partnerAudience;
  }

  public void setPartnerAudience( String partnerAudience )
  {
    this.partnerAudience = partnerAudience;
  }

  public String getPartnerAudienceId()
  {
    return partnerAudienceId;
  }

  public void setPartnerAudienceId( String partnerAudienceId )
  {
    this.partnerAudienceId = partnerAudienceId;
  }

  public String getPartnerWebRulesText()
  {
    return partnerWebRulesText;
  }

  public void setPartnerWebRulesText( String partnerWebRulesText )
  {
    this.partnerWebRulesText = partnerWebRulesText;
  }

  public boolean isPartnerAsPrimaryAudience()
  {
    return partnerAsPrimaryAudience;
  }

  public void setPartnerAsPrimaryAudience( boolean partnerAsPrimaryAudience )
  {
    this.partnerAsPrimaryAudience = partnerAsPrimaryAudience;
  }

  public boolean isAllActivePartnerAudience()
  {
    return allActivePartnerAudience;
  }

  public void setAllActivePartnerAudience( boolean allActivePartnerAudience )
  {
    this.allActivePartnerAudience = allActivePartnerAudience;
  }

  public String[] getDeletePromotionPartnerWebRulesAudience()
  {
    return deletePromotionPartnerWebRulesAudience;
  }

  public void setDeletePromotionPartnerWebRulesAudience( String[] deletePromotionPartnerWebRulesAudience )
  {
    this.deletePromotionPartnerWebRulesAudience = deletePromotionPartnerWebRulesAudience;
  }

}
