/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/FormattedValueBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;

/**
 * .
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
 * <td>sedey</td>
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsNoEmailBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Long claimId;
  private String promotionName;
  private String recipientDisplayName;
  private String recipientCountryCode;
  private String recipientNodeName;
  private String recipientDepartment;
  private String recipientJobPosition;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getRecipientDisplayName()
  {
    return recipientDisplayName;
  }

  public void setRecipientDisplayName( String recipientDisplayName )
  {
    this.recipientDisplayName = recipientDisplayName;
  }

  public String getRecipientCountryCode()
  {
    return recipientCountryCode;
  }

  public void setRecipientCountryCode( String recipientCountryCode )
  {
    this.recipientCountryCode = recipientCountryCode;
  }

  public String getRecipientNodeName()
  {
    return recipientNodeName;
  }

  public void setRecipientNodeName( String recipientNodeName )
  {
    this.recipientNodeName = recipientNodeName;
  }

  public String getRecipientDepartment()
  {
    return recipientDepartment;
  }

  public void setRecipientDepartment( String recipientDepartment )
  {
    this.recipientDepartment = recipientDepartment;
  }

  public String getRecipientJobPosition()
  {
    return recipientJobPosition;
  }

  public void setRecipientJobPosition( String recipientJobPosition )
  {
    this.recipientJobPosition = recipientJobPosition;
  }

}
