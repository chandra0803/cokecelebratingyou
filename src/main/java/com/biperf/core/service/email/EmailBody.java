/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/email/EmailBody.java,v $
 */

package com.biperf.core.service.email;

/**
 * EmailBody.
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
public abstract class EmailBody
{
  private boolean isHtmlFormat = false;

  /**
   * Get the text to be sent in the Email Body
   * 
   * @return String
   */
  public abstract String getBodyText();

  /**
   * @return value of isHtmlFormat property
   */
  public boolean isHtmlFormat()
  {
    return isHtmlFormat;
  }

  /**
   * @param isHtmlFormat value for isHtmlFormat property
   */
  public void setHtmlFormat( boolean isHtmlFormat )
  {
    this.isHtmlFormat = isHtmlFormat;
  }

}
