/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionPayout.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.utils.DateUtils;

/**
 * Promotion.
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
 * <td>leep</td>
 * <td>Jun 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayout extends BaseDomain implements Cloneable
{

  private int quantity;
  private PromotionPayoutGroup promotionPayoutGroup;
  private Product product;
  private ProductCategory productCategory;

  private Date productOrCategoryStartDate;
  private Date productOrCategoryEndDate;

  public PromotionPayout()
  {
    super();
  }

  public PromotionPayout( PromotionPayout copy )
  {
    this.promotionPayoutGroup = copy.getPromotionPayoutGroup();
    this.product = copy.getProduct();
    this.productCategory = copy.getProductCategory();
  }

  /**
   * @return Returns the product.
   */
  public Product getProduct()
  {
    return product;
  }

  /**
   * @param product
   */
  public void setProduct( Product product )
  {
    this.product = product;
  }

  /**
   * @return productCategory.
   */
  public ProductCategory getProductCategory()
  {
    return productCategory;
  }

  /**
   * @param productCategory
   */
  public void setProductCategory( ProductCategory productCategory )
  {
    this.productCategory = productCategory;
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

    if ( ! ( object instanceof PromotionPayout ) )
    {
      return false;
    }

    PromotionPayout otherPromotionPayout = (PromotionPayout)object;

    if ( otherPromotionPayout.getProduct() != null && !otherPromotionPayout.getProduct().equals( this.getProduct() ) )
    {
      equals = false;
    }

    if ( otherPromotionPayout.getProductCategory() != null && !otherPromotionPayout.getProductCategory().equals( this.getProductCategory() ) )
    {
      equals = false;
    }

    if ( otherPromotionPayout.getPromotionPayoutGroup() != null && !otherPromotionPayout.getPromotionPayoutGroup().equals( this.getPromotionPayoutGroup() ) )
    {
      equals = false;
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
    result = this.getProduct() != null ? this.getProduct().hashCode() : 0;
    result = 29 * result + ( getProductCategory() != null ? getProductCategory().hashCode() : 0 );
    result = 29 * result + ( getPromotionPayoutGroup().getPromotion() != null ? getPromotionPayoutGroup().getPromotion().hashCode() : 0 );

    // TODO refactor to promotionPayoutGroup
    // result = 29 * result + ( getPromotion() != null ? getPromotion().hashCode() : 0 );

    return result;
  }

  /**
   * Clones this, removes the auditInfo and id. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    PromotionPayout promotionPayout = (PromotionPayout)super.clone();
    promotionPayout.resetBaseDomain();

    return promotionPayout;

  }

  public PromotionPayoutGroup getPromotionPayoutGroup()
  {
    return this.promotionPayoutGroup;
  }

  public void setPromotionPayoutGroup( PromotionPayoutGroup promotionPayoutGroup )
  {
    this.promotionPayoutGroup = promotionPayoutGroup;
  }

  /**
   * @return value of quantity property
   */
  public int getQuantity()
  {
    return quantity;
  }

  /**
   * @param quantityRequired value for quantity property
   */
  public void setQuantity( int quantityRequired )
  {
    this.quantity = quantityRequired;
  }

  /**
   * Return either the product name or the product category name, one of which must be set on a
   * populated PromotionPayout object.
   * 
   * @return String
   */
  public String getRequiredProductOrProductCategoryName()
  {
    String name = null;

    if ( product != null )
    {
      name = product.getName();
    }
    else
    {
      name = productCategory.getName();
    }

    return name;
  }

  public Date getProductOrCategoryEndDate()
  {
    return productOrCategoryEndDate;
  }

  public void setProductOrCategoryEndDate( Date productOrCategoryEndDate )
  {
    this.productOrCategoryEndDate = productOrCategoryEndDate;
  }

  public Date getProductOrCategoryStartDate()
  {
    return productOrCategoryStartDate;
  }

  public void setProductOrCategoryStartDate( Date productOrCategoryStartDate )
  {
    this.productOrCategoryStartDate = productOrCategoryStartDate;
  }

  public String getDisplayStartDate()
  {
    return DateUtils.toDisplayString( productOrCategoryStartDate );
  }

  public String getDisplayEndDate()
  {
    return DateUtils.toDisplayString( productOrCategoryEndDate );
  }

}
