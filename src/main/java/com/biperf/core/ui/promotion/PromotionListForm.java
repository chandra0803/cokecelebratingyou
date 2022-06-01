/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionListForm.java,v $
 */

package com.biperf.core.ui.promotion;

import com.biperf.core.ui.BaseForm;

/**
 * Promotion ActionForm transfer object.
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
 * <td>June 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionListForm extends BaseForm
{
  private String method;
  private String promotionType;
  private String[] deleteUnderConstructionPromos;
  private String[] deleteCompletePromos;
  private String[] deleteExpiredPromos;

  public String[] getDeleteExpiredPromos()
  {
    return deleteExpiredPromos;
  }

  public void setDeleteExpiredPromos( String[] deleteExpiredPromos )
  {
    this.deleteExpiredPromos = deleteExpiredPromos;
  }

  public String[] getDeleteCompletePromos()
  {
    return deleteCompletePromos;
  }

  public void setDeleteCompletePromos( String[] deleteCompletePromos )
  {
    this.deleteCompletePromos = deleteCompletePromos;
  }

  public String[] getDeleteUnderConstructionPromos()
  {
    return deleteUnderConstructionPromos;
  }

  public void setDeleteUnderConstructionPromos( String[] deleteUnderConstructionPromos )
  {
    this.deleteUnderConstructionPromos = deleteUnderConstructionPromos;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

}
