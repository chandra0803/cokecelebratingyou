/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/product/ProductCategorySearchForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import java.util.HashMap;
import java.util.Map;

import com.biperf.core.ui.BaseActionForm;

/**
 * ProductCategorySearchForm.
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
 * <td>June 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCategorySearchForm extends BaseActionForm
{
  private Long categoryId;
  private Long subCategoryId;
  private String method;
  private String returnActionUrl; // action url to return to after a category selection on product
  // category search

  private final Map params = new HashMap(); // parameters that are passed to

  // node search by the caller and can be
  // propagated on the search result links

  public Long getCategoryId()
  {
    return categoryId;
  }

  public void setCategoryId( Long categoryId )
  {
    this.categoryId = categoryId;
  }

  public Long getSubCategoryId()
  {
    return subCategoryId;
  }

  public void setSubCategoryId( Long subCategoryId )
  {
    this.subCategoryId = subCategoryId;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public final Map getParamsMap()
  {
    return params;
  }

  public Object getParams( String key )
  {
    return params.get( key );
  }

  public void setParams( String key, Object value )
  {
    params.put( key, value );
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

}
