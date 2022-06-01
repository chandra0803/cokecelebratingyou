/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/product/ProductLibraryForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.product;

import com.biperf.core.service.product.ProductSearchCriteria;
import com.biperf.core.ui.BaseForm;

/**
 * ProductLibraryForm.
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
public class ProductLibraryForm extends BaseForm
{
  private String productName;
  private String itemNumber;
  private Long categoryId;
  private Long subCategoryId;

  private Long[] deleteProductIds;

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

  public Long getSubCategoryId()
  {
    return subCategoryId;
  }

  public void setSubCategoryId( Long subCategoryId )
  {
    this.subCategoryId = subCategoryId;
  }

  public ProductSearchCriteria toDomainObject()
  {
    ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria();
    productSearchCriteria.setProductName( productName );
    productSearchCriteria.setItemNumber( itemNumber );
    productSearchCriteria.setCategoryId( categoryId );
    productSearchCriteria.setSubCategoryId( subCategoryId );
    return productSearchCriteria;
  }

  public Long[] getDeleteProductIds()
  {
    return deleteProductIds;
  }

  public void setDeleteProductIds( Long[] deleteProductIds )
  {
    this.deleteProductIds = deleteProductIds;
  }

}
