/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/mailing/MailingRecipient.java,v $
 */

package com.biperf.core.domain.mailing;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

/**
 * MailingRecipient.
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
 * <td>zahler</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MailingRecipient extends BaseDomain
{
  private String guid;
  private Mailing mailing;
  private User user;
  private String previewEmailAddress;
  private String previewSMSAddress;
  private Timestamp dateSent;
  private Timestamp smsDateSent;
  private String locale = "en"; // default value
  private Long claimRecipientId;
  private Long alternateEmailAddrId;
  private Long alternatePhoneId;

  private boolean isValidRecipient;
  private Set mailingRecipientDataSet = new LinkedHashSet();

  public void addMailingRecipientDataSet( Set mailingRecipientDataSet )
  {
    for ( Iterator iter = mailingRecipientDataSet.iterator(); iter.hasNext(); )
    {
      MailingRecipientData mailingRecipientData = (MailingRecipientData)iter.next();
      addMailingRecipientData( mailingRecipientData );
    }
  }

  /**
   * @param mailingRecipientData
   */
  public void addMailingRecipientData( MailingRecipientData mailingRecipientData )
  {
    mailingRecipientData.setMailingRecipient( this );
    mailingRecipientDataSet.add( mailingRecipientData );
  }

  /**
   * Add MailingRecipientData objects to MailingRecipient given a Map of key value pairs.
   * 
   * @param dataMap
   */
  public void addMailingRecipientDataFromMap( Map dataMap )
  {
    for ( Iterator dataMapIter = dataMap.keySet().iterator(); dataMapIter.hasNext(); )
    {
      MailingRecipientData mailingRecipientData = new MailingRecipientData();
      String key = (String)dataMapIter.next();
      if ( dataMap.get( key ) != null )
      {
        String value = dataMap.get( key ).toString();

        mailingRecipientData.setKey( key );
        mailingRecipientData.setValue( value );

        addMailingRecipientData( mailingRecipientData );
      }
    }
  }

  /**
   * @return MailingMessageLocale for the locale of Recipient
   */
  public MailingMessageLocale getMailingMessageLocale()
  {
    for ( Iterator iter = mailing.getMailingMessageLocales().iterator(); iter.hasNext(); )
    {
      MailingMessageLocale mailingMessageLocale = (MailingMessageLocale)iter.next();

      if ( mailingMessageLocale.getLocale().equals( this.locale ) )
      {
        return mailingMessageLocale;
      }
    }

    return null;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public Timestamp getDateSent()
  {
    return dateSent;
  }

  public void setDateSent( Timestamp dateSent )
  {
    this.dateSent = dateSent;
  }

  public Timestamp getSMSDateSent()
  {
    return smsDateSent;
  }

  public void setSMSDateSent( Timestamp smsDateSent )
  {
    this.smsDateSent = smsDateSent;
  }

  public Mailing getMailing()
  {
    return mailing;
  }

  public void setMailing( Mailing mailing )
  {
    this.mailing = mailing;
  }

  public String getPreviewSMSAddress()
  {
    return previewSMSAddress;
  }

  public void setPreviewSMSAddress( String previewSMSAddress )
  {
    this.previewSMSAddress = previewSMSAddress;
  }

  public String getPreviewEmailAddress()
  {
    return previewEmailAddress;
  }

  public void setPreviewEmailAddress( String previewEmailAddress )
  {
    this.previewEmailAddress = previewEmailAddress;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public Long getAlternateEmailAddrId()
  {
    return alternateEmailAddrId;
  }

  public void setAlternateEmailAddrId( Long alternateEmailAddrId )
  {
    this.alternateEmailAddrId = alternateEmailAddrId;
  }

  public Long getAlternatePhoneId()
  {
    return alternatePhoneId;
  }

  public void setAlternatePhoneId( Long alternatePhoneId )
  {
    this.alternatePhoneId = alternatePhoneId;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof MailingRecipient ) )
    {
      return false;
    }

    final MailingRecipient mailingRecipient = (MailingRecipient)object;

    if ( guid != null && !guid.equals( mailingRecipient.getGuid() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int hashcode
   */
  public int hashCode()
  {
    return guid.hashCode();
  }

  public Set getMailingRecipientDataSet()
  {
    return mailingRecipientDataSet;
  }

  public void setMailingRecipientDataSet( Set mailingRecipientDataSet )
  {
    this.mailingRecipientDataSet = mailingRecipientDataSet;
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public Long getClaimRecipientId()
  {
    return claimRecipientId;
  }

  public void setClaimRecipientId( Long claimRecipientId )
  {
    this.claimRecipientId = claimRecipientId;
  }

  public boolean isValidRecipient()
  {
    return isValidRecipient;
  }

  public void setValidRecipient( boolean isValidRecipient )
  {
    this.isValidRecipient = isValidRecipient;
  }
}
