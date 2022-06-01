/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/email/CMEmailBody.java,v $
 */

package com.biperf.core.service.email;

import com.objectpartners.cms.util.ContentReaderManager;

/**
 * CMEmailBody is used when email text comes from Content Manager. The CMEmailBody object would
 * fetch the text based on the Content Manager's asset key.
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
public class CMEmailBody extends EmailBody
{
  private String assetKey;
  private String appCode;

  /**
   * Contructor with the asset key, locale and appCode needed for fetching data from Content
   * Manager.
   * 
   * @param assetKey
   * @param appCode
   */
  public CMEmailBody( String assetKey, String appCode )
  {
    this.assetKey = assetKey;
    this.appCode = appCode;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.email.EmailBody#getBodyText()
   * @return String
   */
  public String getBodyText()
  {
    /**
     * TODO Implement code to fetch data from Content Manager.
     */
    return ContentReaderManager.getText( appCode, assetKey );
  }

}
