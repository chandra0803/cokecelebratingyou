/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/product/ProductCategory.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.product;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

/**
 * ProductCategory.
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
 * <td>Jun 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCategory extends BaseDomain
{
  public static final Long ALL_PRODUCT_CATEGORIES = new Long( -1 );

  private String name;
  private String description;
  private ProductCategory parentProductCategory;
  private Set subcategories = new LinkedHashSet();
  private Set products = new LinkedHashSet();

  public Set getProducts()
  {
    return products;
  }

  public void setProducts( Set products )
  {
    this.products = products;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public ProductCategory getParentProductCategory()
  {
    return parentProductCategory;
  }

  public void setParentProductCategory( ProductCategory parentProductCategory )
  {
    this.parentProductCategory = parentProductCategory;
  }

  /**
   * @param product
   */
  public void addProduct( Product product )
  {
    product.setProductCategory( this );
    this.products.add( product );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( object instanceof ProductCategory )
    {
      ProductCategory otherCategory = (ProductCategory)object;

      if ( otherCategory.getName() != null && !otherCategory.getName().equals( this.getName() ) )
      {
        equals = false;
      }

      ProductCategory otherParent = otherCategory.getParentProductCategory();
      ProductCategory thisParent = this.getParentProductCategory();
      if ( otherParent == null || thisParent == null )
      {
        if ( otherParent != null )
        {
          equals = false;
        }
        else if ( thisParent != null )
        {
          equals = false;
        }
      }
      else
      {
        if ( otherParent.getName() != null && !otherParent.getName().equals( thisParent.getName() ) )
        {
          equals = false;
        }
      }
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = this.getName() != null ? this.getName().hashCode() : 0;
    result = 29 * result + ( getParentProductCategory() != null ? getParentProductCategory().hashCode() : 0 );

    return result;
  }

  /**
   * @return value of subcategories property
   */
  public Set getSubcategories()
  {
    return subcategories;
  }

  /**
   * @param subcategories value for subcategories property
   */
  public void setSubcategories( Set subcategories )
  {
    this.subcategories = subcategories;
  }

  /**
   * Add subcategory to subcategories.
   * 
   * @param subcategory
   */
  public void addSubcategory( ProductCategory subcategory )
  {
    subcategory.setParentProductCategory( this );
    subcategories.add( subcategory );
  }
}
