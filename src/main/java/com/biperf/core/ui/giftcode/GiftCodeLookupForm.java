/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/giftcode/GiftCodeLookupForm.java,v $
 */

package com.biperf.core.ui.giftcode;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * EventForm transfer object.
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
 * <td>babu</td>
 * <td>Oct 29, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GiftCodeLookupForm extends BaseForm
{
  private MerchOrder merchOrder = null;
  private String giftCode;
  private String referenceNumber;
  private String levelCMKey;
  private String recipientFirstName;
  private String recipientLastName;
  private java.util.Date issuedDate;
  private boolean redeemed;
  private String method;

  public String getGiftCode()
  {
    return giftCode;
  }

  public void setGiftCode( String giftCode )
  {
    this.giftCode = giftCode;
  }

  public java.util.Date getIssuedDate()
  {
    return issuedDate;
  }

  public void setIssuedDate( java.util.Date issuedDate )
  {
    this.issuedDate = issuedDate;
  }

  public String getLevelCMKey()
  {
    return levelCMKey;
  }

  public void setLevelCMKey( String levelLabel )
  {
    this.levelCMKey = levelLabel;
  }

  public String getRecipientFirstName()
  {
    return recipientFirstName;
  }

  public void setRecipientFirstName( String recipientFirstName )
  {
    this.recipientFirstName = recipientFirstName;
  }

  public String getRecipientLastName()
  {
    return recipientLastName;
  }

  public void setRecipientLastName( String recipientLastName )
  {
    this.recipientLastName = recipientLastName;
  }

  public String getReferenceNumber()
  {
    return referenceNumber;
  }

  public void setReferenceNumber( String referenceNumber )
  {
    this.referenceNumber = referenceNumber;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isRedeemed()
  {
    return redeemed;
  }

  public void setRedeemed( boolean isRedeemed )
  {
    this.redeemed = isRedeemed;
  }

  public MerchOrder getMerchOrder()
  {
    return merchOrder;
  }

  public void setMerchOrder( MerchOrder merchOrder )
  {
    this.merchOrder = merchOrder;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = new ActionErrors();

    if ( this.method != null )
    {
      if ( ( null == getGiftCode() || getGiftCode().length() == 0 ) && ( null == getReferenceNumber() || getReferenceNumber().length() == 0 ) )
      {
        errors.add( "giftcodes", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "giftcode.lookup", "INVALID_SEARCH" ) ) );
      }
    }
    return errors;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( "giftCode=" + giftCode );
    sb.append( "\nreferenceNumber=" + referenceNumber );
    sb.append( "\nlevelCMKey=" + levelCMKey );
    sb.append( "\nrecipientFirstName=" + recipientFirstName );
    sb.append( "\nrecipientLastName=" + recipientLastName );
    sb.append( "\nredeemed=" + redeemed );
    sb.append( "\nissuedDate=" + issuedDate );

    return sb.toString();
  }
}
