/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/mailing/MailingRecipientData.java,v $
 */

package com.biperf.core.domain.mailing;

import com.biperf.core.domain.BaseDomain;

/**
 * MailingRecipientData.
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
public class MailingRecipientData extends BaseDomain
{
  private MailingRecipient mailingRecipient;
  private String key;
  private String value;

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

  public MailingRecipient getMailingRecipient()
  {
    return mailingRecipient;
  }

  public void setMailingRecipient( MailingRecipient mailingRecipient )
  {
    this.mailingRecipient = mailingRecipient;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
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
    if ( ! ( object instanceof MailingRecipientData ) )
    {
      return false;
    }

    final MailingRecipientData mailingRecipientData = (MailingRecipientData)object;

    if ( getMailingRecipient() != null ? !getMailingRecipient().equals( mailingRecipientData.getMailingRecipient() ) : mailingRecipientData.getMailingRecipient() != null )
    {
      return false;
    }
    if ( getKey() != null ? !getKey().equals( mailingRecipientData.getKey() ) : mailingRecipientData.getKey() != null )
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
    if ( getMailingRecipient() != null && getKey() != null )
    {
      return 29 * getMailingRecipient().hashCode() + getKey().hashCode();
    }
    return 0;
  }

  public int compareTo( Object obj )
  {
    MailingRecipientData mrd = (MailingRecipientData)obj;
    return this.getMailingRecipient().getDateSent().compareTo( mrd.getMailingRecipient().getDateSent() );
  }

}
