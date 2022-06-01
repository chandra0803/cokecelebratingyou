/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionRulesForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.List;

import com.biperf.core.ui.BaseForm;

/**
 * PromotionRulesForm.
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
 * <td>robinsra</td>
 * <td>Aug 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionRulesForm extends BaseForm
{
  private Long promotionId;
  private String promotionName;
  private String cmsCode;
  private String cmsKey;
  private String method;
  // page that back button need to forward by default it is home page
  private String pageName;
  private List promotionRulesList;

  public String getPageName()
  {
    return pageName;
  }

  public void setPageName( String pageName )
  {
    this.pageName = pageName;
  }

  public String getCmsCode()
  {
    return cmsCode;
  }

  public void setCmsCode( String cmsCode )
  {
    this.cmsCode = cmsCode;
  }

  public String getCmsKey()
  {
    return cmsKey;
  }

  public void setCmsKey( String cmsKey )
  {
    this.cmsKey = cmsKey;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public List getPromotionRulesList()
  {
    return promotionRulesList;
  }

  public void setPromotionRulesList( List promotionRulesList )
  {
    this.promotionRulesList = promotionRulesList;
  }

}
