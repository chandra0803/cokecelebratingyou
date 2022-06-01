/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/CertificateDisplayForm.java,v $
 */

package com.biperf.core.ui.claim;

import com.biperf.core.ui.BaseForm;

/**
 * CertificateDisplayForm.
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
 * <td>Jan 4, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CertificateDisplayForm extends BaseForm
{
  private String method;
  private String recipientId;
  private String userId;
  private String claimId;
  private String promotionId;

  public String getClaimId()
  {
    return claimId;
  }

  public void setClaimId( String claimId )
  {
    this.claimId = claimId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( String recipientId )
  {
    this.recipientId = recipientId;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }
}
