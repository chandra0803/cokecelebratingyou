/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/mailing/Mailing.java,v $
 */

package com.biperf.core.domain.mailing;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.utils.GuidUtils;

/**
 * Mailing
 * 
 *
 */
public class Mailing extends BaseDomain
{

  // Using guid for equals and hash code because there isn't a known business key.
  private String guid;
  private String sender;
  private MailingType mailingType;
  private Timestamp deliveryDate;
  private Message message;
  private boolean sendSMSOnly = false;
  // transient variable
  private boolean messageTypeSMS;

  private MailingBatch mailingBatch;

  private Set mailingRecipients = new LinkedHashSet();
  private Set mailingMessageLocales = new LinkedHashSet();
  private Set mailingAttachmentInfos = new LinkedHashSet();

  public Mailing()
  {
    this.guid = GuidUtils.generateGuid();
  }

  public void addMailingRecipients( Set recipients )
  {
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient recipient = (MailingRecipient)iter.next();
      addMailingRecipient( recipient );
    }
  }

  public void addMailingRecipient( MailingRecipient mailingRecipient )
  {
    mailingRecipient.setMailing( this );
    mailingRecipients.add( mailingRecipient );
  }

  public void addMailingMessageLocale( MailingMessageLocale mailingMessageLocale )
  {
    mailingMessageLocale.setMailing( this );
    mailingMessageLocales.add( mailingMessageLocale );
  }

  public void addMailingAttachmentInfo( MailingAttachmentInfo mailingAttachmentInfo )
  {
    mailingAttachmentInfo.setMailing( this );
    mailingAttachmentInfos.add( mailingAttachmentInfo );
  }

  public boolean isNonStandardMailing()
  {
    return this.getMailingType().getCode().equals( MailingType.SYSTEM ) || this.getMailingType().getCode().equals( MailingType.RESEND );
  }

  public Timestamp getDeliveryDate()
  {
    return deliveryDate;
  }

  public void setDeliveryDate( Timestamp deliveryDate )
  {
    this.deliveryDate = deliveryDate;
  }

  public boolean isSendSMSOnly()
  {
    return sendSMSOnly;
  }

  public void setSendSMSOnly( boolean sendSMSOnly )
  {
    this.sendSMSOnly = sendSMSOnly;
  }

  public Message getMessage()
  {
    return message;
  }

  public void setMessage( Message message )
  {
    this.message = message;
  }

  public String getSender()
  {
    return sender;
  }

  public void setSender( String sender )
  {
    this.sender = sender;
  }

  public Set getMailingRecipients()
  {
    return mailingRecipients;
  }

  public void setMailingRecipients( Set mailingRecipients )
  {
    this.mailingRecipients = mailingRecipients;
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public Set getMailingMessageLocales()
  {
    return mailingMessageLocales;
  }

  public void setMailingMessageLocales( Set mailingMessageLocales )
  {
    this.mailingMessageLocales = mailingMessageLocales;
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
    if ( ! ( object instanceof Mailing ) )
    {
      return false;
    }

    final Mailing mailing = (Mailing)object;

    if ( guid != null && !guid.equals( mailing.getGuid() ) )
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

  public MailingType getMailingType()
  {
    return mailingType;
  }

  public void setMailingType( MailingType mailingType )
  {
    this.mailingType = mailingType;
  }

  public Set getMailingAttachmentInfos()
  {
    return mailingAttachmentInfos;
  }

  public void setMailingAttachmentInfos( Set mailingAttachmentInfos )
  {
    this.mailingAttachmentInfos = mailingAttachmentInfos;
  }

  public MailingBatch getMailingBatch()
  {
    return mailingBatch;
  }

  public void setMailingBatch( MailingBatch mailingBatch )
  {
    this.mailingBatch = mailingBatch;
  }

  public boolean isMessageTypeSMS()
  {
    return this.messageTypeSMS;
  }

  public void setMessageTypeSMS( boolean messageTypeSMS )
  {
    this.messageTypeSMS = messageTypeSMS;
  }

}
