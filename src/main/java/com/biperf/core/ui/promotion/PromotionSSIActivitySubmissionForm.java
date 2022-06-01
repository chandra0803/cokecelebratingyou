
package com.biperf.core.ui.promotion;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * PromotionSSIActivitySubmissionForm.
 * 
 * @author kandhi
 * @since Oct 29, 2014
 * @version 1.0
 */
public class PromotionSSIActivitySubmissionForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;
  private String method;
  private String returnActionUrl;
  private Long promotionId;
  private String promotionName;
  private String promotionTypeCode;
  private String promotionTypeName;
  private String promotionStatus;
  private boolean expired;
  private boolean live;

  private Boolean allowActivityUpload;
  private Boolean allowClaimSubmission;
  private String activityFormName;
  private String claimFormId;
  private Integer daysToApproveClaim;

  public Boolean getAllowActivityUpload()
  {
    return allowActivityUpload;
  }

  public void setAllowActivityUpload( Boolean allowActivityUpload )
  {
    this.allowActivityUpload = allowActivityUpload;
  }

  public Boolean getAllowClaimSubmission()
  {
    return allowClaimSubmission;
  }

  public void setAllowClaimSubmission( Boolean allowClaimSubmission )
  {
    this.allowClaimSubmission = allowClaimSubmission;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
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

  public boolean isExpired()
  {
    return expired;
  }

  public void setExpired( boolean expired )
  {
    this.expired = expired;
  }

  public boolean isLive()
  {
    return live;
  }

  public void setLive( boolean live )
  {
    this.live = live;
  }

  public String getActivityFormName()
  {
    return activityFormName;
  }

  public void setActivityFormName( String activityFormName )
  {
    this.activityFormName = activityFormName;
  }

  public String getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( String claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public Integer getDaysToApproveClaim()
  {
    return daysToApproveClaim;
  }

  public void setDaysToApproveClaim( Integer daysToApproveClaim )
  {
    this.daysToApproveClaim = daysToApproveClaim;
  }

  /**
   * Populate the form from the domain object
   * @param promotion
   */
  public void load( Promotion promotion )
  {
    SSIPromotion ssiPromotion = (SSIPromotion)promotion;
    this.promotionName = ssiPromotion.getName();
    this.promotionTypeCode = ssiPromotion.getPromotionType().getCode();
    this.promotionTypeName = ssiPromotion.getPromotionType().getName();
    this.expired = ssiPromotion.isExpired();
    this.live = ssiPromotion.isLive();

    this.allowActivityUpload = ssiPromotion.getAllowActivityUpload();
    this.allowClaimSubmission = ssiPromotion.getAllowClaimSubmission();
    this.activityFormName = ssiPromotion.getClaimForm() != null ? ssiPromotion.getClaimForm().getName() : null;
    this.claimFormId = ssiPromotion.getClaimForm() != null ? String.valueOf( ssiPromotion.getClaimForm().getId() ) : null;
    this.daysToApproveClaim = ssiPromotion.getDaysToApproveClaim();
  }

  /**
   * Populate the domain object for the save
   * @param promotion
   * @return
   */
  public Promotion toDomainObject( SSIPromotion ssiPromotion )
  {
    ssiPromotion.setAllowActivityUpload( this.allowActivityUpload );
    ssiPromotion.setAllowClaimSubmission( this.allowClaimSubmission );
    if ( this.allowClaimSubmission )
    {
      ClaimForm claimForm = new ClaimForm();
      claimForm.setId( Long.parseLong( this.claimFormId ) );
      ssiPromotion.setClaimForm( claimForm );
      ssiPromotion.setDaysToApproveClaim( this.daysToApproveClaim );
    }
    else
    {
      ssiPromotion.setClaimForm( null );
      ssiPromotion.setDaysToApproveClaim( null );
    }
    return ssiPromotion;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( allowActivityUpload == null )
    {
      errors.add( "allowActivityUpload",
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.activitysubmission.SPREADSHEET_UPLOAD" ) ) );
    }
    if ( allowClaimSubmission == null )
    {
      errors.add( "allowClaimSubmission",
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.activitysubmission.CLAIM_SUBMISSION" ) ) );
    }
    else
    {
      if ( allowClaimSubmission )
      {
        if ( StringUtil.isNullOrEmpty( claimFormId ) )
        {
          errors.add( "claimFormId", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.ACTIVITY_FORM" ) ) );
        }
        if ( this.daysToApproveClaim == null )
        {
          errors.add( "daysToApproveClaim",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.activitysubmission.DAYS_TO_APPROVE_CLAIM" ) ) );
        }
        else if ( this.daysToApproveClaim.intValue() <= 0 )
        {
          errors.add( "daysToApproveClaim",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.activitysubmission.DAYS_TO_APPROVE_CLAIM" ) ) );
        }
      }
    }
    if ( allowActivityUpload != null && allowClaimSubmission != null && !allowActivityUpload && !allowClaimSubmission )
    {
      errors.add( "method", new ActionMessage( "promotion.ssi.activitysubmission.ATLEAST_ONE_YES" ) );
    }
    return errors;
  }

}
