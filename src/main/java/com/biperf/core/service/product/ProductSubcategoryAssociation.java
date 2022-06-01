/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/product/ProductSubcategoryAssociation.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.product;

import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * ProductSubcategoryAssociation.
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
 * <td>Sep 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductSubcategoryAssociation extends BaseAssociationRequest
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    ProductCategory productCategory = (ProductCategory)domainObject;
    initialize( productCategory.getSubcategories() );
  }

}
