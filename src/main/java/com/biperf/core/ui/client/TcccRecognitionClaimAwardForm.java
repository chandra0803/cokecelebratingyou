
package com.biperf.core.ui.client;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.TcccClientUtils;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.opensymphony.oscache.util.StringUtil;

public class TcccRecognitionClaimAwardForm extends BaseForm
{
  private static final long serialVersionUID = 1331663132072910880L;

  private Long claimId;
  private Long claimItemId;
  private String currencyCode;

  private Long currencyFullAmt;
  private String currencyFullAmtString;
  private String pointsHalfAmt;
  private String pointsFullAmt;
  private Long currencyHalfAmt;
  private String currencyHalfAmtString;
  private String awardType;

  private String promotionName;
  private Long recipientId;
  private String recipientName;
  private String recipientAvatar;
  private String recipientCountryCode;
  private String recipientCountryName;
  private String recipientDeptName;
  private String recipientJobName;
  private Long submitterId;
  private String submitterName;
  private String submitterAvatar;
  private String submitterCountryCode;
  private String submitterCountryName;
  private String submitterDeptName;
  private String submitterJobName;
  private String date;
  private String behaviour;
  private String comments;

  private boolean cashAvailable;
  private boolean optOut;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getClaimItemId()
  {
    return claimItemId;
  }

  public void setClaimItemId( Long claimItemId )
  {
    this.claimItemId = claimItemId;
  }

  public String getCurrencyCode()
  {
    return currencyCode;
  }

  public void setCurrencyCode( String currencyCode )
  {
    this.currencyCode = currencyCode;
  }

  public Long getCurrencyFullAmt()
  {
    return currencyFullAmt;
  }

  public void setCurrencyFullAmt( Long currencyFullAmt )
  {
    this.currencyFullAmt = currencyFullAmt;
  }

  public String getCurrencyFullAmtString()
  {
    return currencyFullAmtString;
  }

  public void setCurrencyFullAmtString( String currencyFullAmtString )
  {
    this.currencyFullAmtString = currencyFullAmtString;
  }

  public String getPointsHalfAmt()
  {
    return pointsHalfAmt;
  }

  public void setPointsHalfAmt( String pointsHalfAmt )
  {
    this.pointsHalfAmt = pointsHalfAmt;
  }

  public String getPointsFullAmt()
  {
    return pointsFullAmt;
  }

  public void setPointsFullAmt( String pointsFullAmt )
  {
    this.pointsFullAmt = pointsFullAmt;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public Long getCurrencyHalfAmt()
  {
    return currencyHalfAmt;
  }

  public void setCurrencyHalfAmt( Long currencyHalfAmt )
  {
    this.currencyHalfAmt = currencyHalfAmt;
  }

  public String getCurrencyHalfAmtString()
  {
    return currencyHalfAmtString;
  }

  public void setCurrencyHalfAmtString( String currencyHalfAmtString )
  {
    this.currencyHalfAmtString = currencyHalfAmtString;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getRecipientName()
  {
    return recipientName;
  }

  public void setRecipientName( String recipientName )
  {
    this.recipientName = recipientName;
  }

  public String getRecipientCountryCode()
  {
    return recipientCountryCode;
  }

  public void setRecipientCountryCode( String recipientCountryCode )
  {
    this.recipientCountryCode = recipientCountryCode;
  }

  public String getRecipientDeptName()
  {
    return recipientDeptName;
  }

  public void setRecipientDeptName( String recipientDeptName )
  {
    this.recipientDeptName = recipientDeptName;
  }

  public String getRecipientJobName()
  {
    return recipientJobName;
  }

  public void setRecipientJobName( String recipientJobName )
  {
    this.recipientJobName = recipientJobName;
  }

  public String getSubmitterName()
  {
    return submitterName;
  }

  public void setSubmitterName( String submitterName )
  {
    this.submitterName = submitterName;
  }

  public String getSubmitterCountryCode()
  {
    return submitterCountryCode;
  }

  public void setSubmitterCountryCode( String submitterCountryCode )
  {
    this.submitterCountryCode = submitterCountryCode;
  }

  public String getSubmitterDeptName()
  {
    return submitterDeptName;
  }

  public void setSubmitterDeptName( String submitterDeptName )
  {
    this.submitterDeptName = submitterDeptName;
  }

  public String getSubmitterJobName()
  {
    return submitterJobName;
  }

  public void setSubmitterJobName( String submitterJobName )
  {
    this.submitterJobName = submitterJobName;
  }

  public String getDate()
  {
    return date;
  }

  public void setDate( String date )
  {
    this.date = date;
  }

  public String getBehaviour()
  {
    return behaviour;
  }

  public void setBehaviour( String behaviour )
  {
    this.behaviour = behaviour;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public Long getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public String getRecipientAvatar()
  {
    return recipientAvatar;
  }

  public void setRecipientAvatar( String recipientAvatar )
  {
    this.recipientAvatar = recipientAvatar;
  }

  public Long getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  public String getSubmitterAvatar()
  {
    return submitterAvatar;
  }

  public void setSubmitterAvatar( String submitterAvatar )
  {
    this.submitterAvatar = submitterAvatar;
  }

  public String getRecipientCountryName()
  {
    return recipientCountryName;
  }

  public void setRecipientCountryName( String recipientCountryName )
  {
    this.recipientCountryName = recipientCountryName;
  }

  public String getSubmitterCountryName()
  {
    return submitterCountryName;
  }

  public void setSubmitterCountryName( String submitterCountryName )
  {
    this.submitterCountryName = submitterCountryName;
  }

  public boolean isCashAvailable()
  {
    return cashAvailable;
  }

  public void setCashAvailable( boolean cashAvailable )
  {
    this.cashAvailable = cashAvailable;
  }

  public boolean isOptOut()
  {
    return optOut;
  }

  public void setOptOut( boolean optOut )
  {
    this.optOut = optOut;
  }

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }
    if ( StringUtil.isEmpty( this.awardType ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.PROMOTION_RULES" ) ) );
    }
    return actionErrors;
  }

  public void load( AbstractRecognitionClaim claim )
  {
    this.claimId = claim.getId();
    this.promotionName = claim.getPromotion().getName();
    this.date = claim.getDisplaySubmissionDate();
    this.behaviour = claim.getBehavior() != null ? claim.getBehavior().getName() : "";
    this.comments = claim.getSubmitterComments();
    for ( ClaimRecipient claimRecipient : claim.getClaimRecipients() )
    {
      this.claimItemId = claimRecipient.getId();
      this.currencyCode = claimRecipient.getCashCurrencyCode();
      this.currencyFullAmt = claimRecipient.getCustomCashAwardQuantity();
      this.currencyFullAmtString = NumberFormatUtil.getLocaleBasedNumberFormat( claimRecipient.getCustomCashAwardQuantity() );

      this.currencyHalfAmt = TcccClientUtils.convertToPercentCash( getSystemVariableService(), claimRecipient.getCustomCashAwardQuantity() );
      this.currencyHalfAmtString = NumberFormatUtil.getLocaleBasedNumberFormat( this.currencyHalfAmt );

      Long usdCashAmt = TcccClientUtils.convertToUSD( getCokeClientService(), claimRecipient.getCustomCashAwardQuantity(), claimRecipient.getCashCurrencyCode() );
      this.pointsFullAmt = NumberFormatUtil.getLocaleBasedNumberFormat( TcccClientUtils.convertToPoints( getCokeClientService(), usdCashAmt, claimRecipient.getRecipient().getId() ) );
      this.pointsHalfAmt = NumberFormatUtil
          .getLocaleBasedNumberFormat( TcccClientUtils.convertToPercentPoints( getSystemVariableService(), getCokeClientService(), usdCashAmt, claimRecipient.getRecipient().getId() ) );

      this.recipientId = claimRecipient.getRecipient().getId();
      this.recipientName = claimRecipient.getRecipient().getNameLFMWithComma();
      this.recipientAvatar = claimRecipient.getRecipient().getAvatarSmallFullPath();
      this.recipientCountryCode = claimRecipient.getRecipient().getPrimaryCountryCode();
      this.recipientCountryName = claimRecipient.getRecipient().getPrimaryCountryName();
      this.recipientDeptName = claimRecipient.getRecipient().getPaxDeptName();
      this.recipientJobName = claimRecipient.getRecipient().getPaxJobName();
      this.optOut = getParticipantService().isOptedOut( claimRecipient.getRecipient().getId() );
      this.cashAvailable = TcccClientUtils.cashAllowed( getSystemVariableService(), getUserService(), claimRecipient.getRecipient().getId() );
    }

    this.submitterId = claim.getSubmitter().getId();
    this.submitterName = claim.getSubmitter().getNameLFMWithComma();
    this.submitterAvatar = claim.getSubmitter().getAvatarSmallFullPath();
    this.submitterCountryCode = claim.getSubmitter().getPrimaryCountryCode();
    this.submitterCountryName = claim.getSubmitter().getPrimaryCountryName();
    this.submitterDeptName = claim.getSubmitter().getPaxDeptName();
    this.submitterJobName = claim.getSubmitter().getPaxJobName();
  }

  private CokeClientService getCokeClientService()
  {
    return (CokeClientService)BeanLocator.getBean( CokeClientService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

}
