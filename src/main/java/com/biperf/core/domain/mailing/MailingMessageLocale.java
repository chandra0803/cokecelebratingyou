/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/mailing/MailingMessageLocale.java,v $
 */

package com.biperf.core.domain.mailing;

import com.biperf.core.domain.BaseDomain;

/**
 * MailingMessageLocale.
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
public class MailingMessageLocale extends BaseDomain
{
  public static final String DEFAULT_LOCALE = "en";
  private Mailing mailing;
  private String locale;
  private String subject;
  // private byte[] htmlMessage;
  // private byte[] plainMessage;
  private String htmlMessage;
  private String plainMessage;
  private String textMessage;

  public String getHtmlMessage()
  {
    // try {
    // return new String(htmlMessage, "UTF8" );
    // } catch (Exception e) {
    // //log.error("Invalid encoding - could not decode htmlMessage string.");
    // }
    //
    // return null;

    return this.htmlMessage;
  }

  public void setHtmlMessage( String htmlMessage )
  {
    // this.htmlMessage = htmlMessage.getBytes();
    this.htmlMessage = htmlMessage;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public Mailing getMailing()
  {
    return mailing;
  }

  public void setMailing( Mailing mailing )
  {
    this.mailing = mailing;
  }

  public String getPlainMessage()
  {
    return plainMessage;
    // try {
    // return new String(plainMessage, "UTF8" );
    // } catch (Exception e) {
    // //log.error("Invalid encoding - could not decode htmlMessage string.");
    // }
    //
    // return null;
  }

  public void setPlainMessage( String plainMessage )
  {
    // this.plainMessage = plainMessage.getBytes();
    this.plainMessage = plainMessage;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public String getTextMessage()
  {
    return textMessage;
  }

  public void setTextMessage( String textMessage )
  {
    this.textMessage = textMessage;
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
    if ( ! ( object instanceof MailingMessageLocale ) )
    {
      return false;
    }

    final MailingMessageLocale mailingMessageLocale = (MailingMessageLocale)object;

    if ( getMailing() != null ? !getMailing().equals( mailingMessageLocale.getMailing() ) : mailingMessageLocale.getMailing() != null )
    {
      return false;
    }
    if ( getLocale() != null ? !getLocale().equals( mailingMessageLocale.getLocale() ) : mailingMessageLocale.getLocale() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int hashCode
   */
  public int hashCode()
  {
    if ( getMailing() != null && getLocale() != null )
    {
      return 29 * getMailing().hashCode() + getLocale().hashCode();
    }
    return 0;
  }

}
