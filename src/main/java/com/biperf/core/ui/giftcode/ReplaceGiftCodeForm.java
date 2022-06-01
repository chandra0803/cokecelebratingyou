/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/giftcode/ReplaceGiftCodeForm.java,v $
 */

package com.biperf.core.ui.giftcode;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ReplaceGiftCodeForm extends BaseForm
{
  private String method;
  private String oldGiftCode;
  private String oldGiftCodeCopy;
  private String emailAddress;
  private String message;
  private boolean detailsAvailable;
  private String participantName;
  private String giftCodeIssueDate;
  private String promotionName;

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    // make sure gift code was not changed in between Find and Submit
    if ( "replaceGiftCode".equals( method ) && oldGiftCode != null && !oldGiftCode.equals( oldGiftCodeCopy ) )
    {
      method = "findGiftCode";
      detailsAvailable = false;
      oldGiftCodeCopy = null;
    }

    // must have email address with Submit
    if ( "replaceGiftCode".equals( method ) && StringUtils.isBlank( emailAddress ) )
    {
      actionErrors.add( "emailAddress", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.goalquest.replacegiftcode.EMAIL_ADDRESS" ) ) );
    }

    return actionErrors;
  }

  public String getOldGiftCode()
  {
    return oldGiftCode;
  }

  public void setOldGiftCode( String oldGiftCode )
  {
    this.oldGiftCode = oldGiftCode;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getOldGiftCodeCopy()
  {
    return oldGiftCodeCopy;
  }

  public void setOldGiftCodeCopy( String oldGiftCodeCopy )
  {
    this.oldGiftCodeCopy = oldGiftCodeCopy;
  }

  public boolean getDetailsAvailable()
  {
    return detailsAvailable;
  }

  public void setDetailsAvailable( boolean detailsAvailable )
  {
    this.detailsAvailable = detailsAvailable;
  }

  public String getParticipantName()
  {
    return participantName;
  }

  public void setParticipantName( String participantName )
  {
    this.participantName = participantName;
  }

  public String getGiftCodeIssueDate()
  {
    return giftCodeIssueDate;
  }

  public void setGiftCodeIssueDate( String giftCodeIssueDate )
  {
    this.giftCodeIssueDate = giftCodeIssueDate;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }
}
