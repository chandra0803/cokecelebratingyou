/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionRegistrationCode.java,v $
 */

package com.biperf.core.domain.promotion;

import java.io.Serializable;

/**
 * PromotionRegistrationCode.
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
 * <td>viswanat</td>
 * <td>Feb 19, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionRegistrationCode implements Serializable
{
  private String cmAssetCode;
  private String cmKey;
  private String nodeName;
  private String registrationCode;

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public String getRegistrationCode()
  {
    return registrationCode;
  }

  public void setRegistrationCode( String registrationCode )
  {
    this.registrationCode = registrationCode;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getCmKey()
  {
    return cmKey;
  }

  public void setCmKey( String cmKey )
  {
    this.cmKey = cmKey;
  }
}
