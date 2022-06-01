/**
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionAwardsFormBean.java,v $
 * (c) 2005 BI, Inc.  All rights reserved. 
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;

/**
 * PromotionAwardsFormBean.
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
 * <td>babu</td>
 * <td>Oct 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAwardsFormBean implements Serializable
{
  Long budgetMasterId;
  String budgetMasterName;
  private String budgetType;

  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public String getBudgetMasterName()
  {
    return budgetMasterName;
  }

  public void setBudgetMasterName( String budgetMasterName )
  {
    this.budgetMasterName = budgetMasterName;
  }

  public void setBudgetType( String budgetType )
  {
    this.budgetType = budgetType;
  }

  public String getBudgetType()
  {
    return budgetType;
  }

}
