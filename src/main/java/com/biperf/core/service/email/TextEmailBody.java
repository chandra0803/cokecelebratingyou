/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/email/TextEmailBody.java,v $
 */

package com.biperf.core.service.email;

/**
 * TextEmailBody is used to carry the text of the email for EmailService.
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
public class TextEmailBody extends EmailBody
{
  String text;

  /**
   * Constructor to create TextEmailBody with text
   * 
   * @param text
   */
  public TextEmailBody( String text )
  {
    if ( text == null )
    {
      this.text = "";
    }
    else
    {
      this.text = text;
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.email.EmailBody#getBodyText()
   * @return String
   */
  public String getBodyText()
  {
    return this.text;
  }

}
