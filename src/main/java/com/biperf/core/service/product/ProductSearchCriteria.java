/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/product/ProductSearchCriteria.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.product;

import java.io.Serializable;

/**
 * ProductSearchCriteria.
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
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductSearchCriteria implements Serializable
{
  private String productName;
  private String itemNumber;
  private Long categoryId;
  private Long subCategoryId;

  public Long getSubCategoryId()
  {
    return subCategoryId;
  }

  public void setSubCategoryId( Long subCategoryId )
  {
    this.subCategoryId = subCategoryId;
  }

  public Long getCategoryId()
  {
    return categoryId;
  }

  public void setCategoryId( Long categoryId )
  {
    this.categoryId = categoryId;
  }

  public String getItemNumber()
  {
    return itemNumber;
  }

  public void setItemNumber( String itemNumber )
  {
    this.itemNumber = itemNumber;
  }

  public String getProductName()
  {
    return productName;
  }

  public void setProductName( String productName )
  {
    this.productName = productName;
  }

}
