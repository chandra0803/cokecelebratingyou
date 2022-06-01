/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/email/EmailHeader.java,v $
 */

package com.biperf.core.service.email;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * EmailHeader.
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
 * <td>sharma</td>
 * <td>Apr 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EmailHeader
{
  private String sender;
  private String personal;
  private String[] recipients;
  private String[] ccRecipients;
  private String[] bccRecipients;
  private String subject;
  private String replyTo;

  public String getReplyTo()
  {
    return replyTo;
  }

  public void setReplyTo( String replyTo )
  {
    this.replyTo = replyTo;
  }

  public String getPersonal()
  {
    return personal;
  }

  public void setPersonal( String personal )
  {
    this.personal = personal;
  }

  /**
   * @return value of bccRrecipients property
   */
  public String[] getBccRecipients()
  {
    return bccRecipients;
  }

  /**
   * @param bccRrecipients value for bccRrecipients property
   */
  public void setBccRecipients( String[] bccRrecipients )
  {
    this.bccRecipients = bccRrecipients;
  }

  /**
   * @return value of ccRecipients property
   */
  public String[] getCcRecipients()
  {
    return ccRecipients;
  }

  /**
   * @param ccRecipients value for ccRecipients property
   */
  public void setCcRecipients( String[] ccRecipients )
  {
    this.ccRecipients = ccRecipients;
  }

  /**
   * @return value of recipients property
   */
  public String[] getRecipients()
  {
    return recipients;
  }

  /**
   * @param recipients value for recipients property
   */
  public void setRecipients( String[] recipients )
  {
    this.recipients = recipients;
  }

  /**
   * @return value of sender property
   */
  public String getSender()
  {
    return sender;
  }

  /**
   * @param sender value for sender property
   */
  public void setSender( String sender )
  {
    this.sender = sender;
  }

  /**
   * @return value of subject property
   */
  public String getSubject()
  {
    return subject;
  }

  /**
   * @param subject value for subject property
   */
  public void setSubject( String subject )
  {
    if ( subject == null )
    {
      this.subject = "";
    }
    else
    {
      this.subject = subject;
    }

  }

  /**
   * Validate the necessary data and its syntax for sending Email
   * 
   * @return boolean
   */
  public boolean validate()
  {
    // TODO should validate BCC recipients || recipients || CC recipients
    EmailValidator emailValidator = EmailValidator.getInstance();
    if ( emailValidator.isValid( sender ) )
    {
      if ( recipients != null && !validateEmailAddrArray( recipients ) && ccRecipients != null && !validateEmailAddrArray( ccRecipients ) && bccRecipients != null
          && !validateEmailAddrArray( bccRecipients ) )
      {
        return false;
      }
      if ( !StringUtils.isEmpty( subject ) )
      {
        return true;
      }
    }

    return false;
  }

  private boolean validateEmailAddrArray( String[] emailAddrArray )
  {
    if ( emailAddrArray == null )
    {
      return false;
    }
    EmailValidator emailValidator = EmailValidator.getInstance();
    for ( int i = 0; i < recipients.length; i++ )
    {
      String emailAddr = recipients[i];
      if ( !emailValidator.isValid( emailAddr ) )
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    ToStringBuilder toStringBuffer = new ToStringBuilder( this );
    toStringBuffer.append( "sender", this.getSender() );
    toStringBuffer.append( "recipients", this.getRecipients() );
    toStringBuffer.append( "ccRecipients", this.getCcRecipients() );
    toStringBuffer.append( "bccRecipients", this.getBccRecipients() );
    toStringBuffer.append( "subject", this.getSubject() );
    return toStringBuffer.toString();
  }
}
