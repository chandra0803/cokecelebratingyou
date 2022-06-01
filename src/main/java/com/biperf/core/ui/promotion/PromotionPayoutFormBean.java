/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionPayoutFormBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;

/**
 * PromotionPayoutFormBean.
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
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutFormBean implements Serializable
{
  private Long promoPayoutId;
  private Long categoryId;
  private String categoryName;
  private Long subcategoryId;
  private String subcategoryName;
  private Long productId;
  private String productName;
  private String childQuantity;
  private String parentQuantity;
  private String removePayout;
  private String createdBy = "";
  private long dateCreated;
  private Long version;
  private boolean includePayout;
  private String productOrCategoryStartDate = "";
  private String productOrCategoryEndDate = "";

  public PromotionPayoutFormBean()
  {
    parentQuantity = "1";
    childQuantity = "1";
    categoryId = new Long( 0 );
    subcategoryId = new Long( 0 );
    productId = new Long( 0 );
  }

  public Long getCategoryId()
  {
    return categoryId;
  }

  public void setCategoryId( Long categoryId )
  {
    this.categoryId = categoryId;
  }

  public Long getProductId()
  {
    return productId;
  }

  public void setProductId( Long productId )
  {
    this.productId = productId;
  }

  public Long getSubcategoryId()
  {
    return subcategoryId;
  }

  public void setSubcategoryId( Long subcategoryId )
  {
    this.subcategoryId = subcategoryId;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getCategoryName()
  {
    return categoryName;
  }

  public void setCategoryName( String categoryName )
  {
    this.categoryName = categoryName;
  }

  public String getSubcategoryName()
  {
    return subcategoryName;
  }

  public void setSubcategoryName( String subcategoryName )
  {
    this.subcategoryName = subcategoryName;
  }

  public String getProductName()
  {
    return productName;
  }

  public void setProductName( String productName )
  {
    this.productName = productName;
  }

  public String getRemovePayout()
  {
    return removePayout;
  }

  public String getChildQuantity()
  {
    return childQuantity;
  }

  public void setChildQuantity( String childQuantity )
  {
    this.childQuantity = childQuantity;
  }

  public void setRemovePayout( String removePayout )
  {
    this.removePayout = removePayout;
  }

  public Long getPromoPayoutId()
  {
    return promoPayoutId;
  }

  public void setPromoPayoutId( Long promoPayoutId )
  {
    this.promoPayoutId = promoPayoutId;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public boolean isIncludePayout()
  {
    return includePayout;
  }

  public void setIncludePayout( boolean includePayout )
  {
    this.includePayout = includePayout;
  }

  public String getParentQuantity()
  {
    return parentQuantity;
  }

  public void setParentQuantity( String parentQuantity )
  {
    this.parentQuantity = parentQuantity;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof PromotionPayoutFormBean ) )
    {
      return false;
    }

    final PromotionPayoutFormBean promoPayoutFormBean = (PromotionPayoutFormBean)object;

    if ( promoPayoutFormBean.getCategoryId() != null && !promoPayoutFormBean.getCategoryId().equals( this.getCategoryId() ) )
    {
      return false;
    }
    if ( promoPayoutFormBean.getSubcategoryId() != null && !promoPayoutFormBean.getSubcategoryId().equals( this.getSubcategoryId() ) )
    {
      return false;
    }
    if ( promoPayoutFormBean.getProductId() != null && !promoPayoutFormBean.getProductId().equals( this.getProductId() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getCategoryId() != null ? getCategoryId().hashCode() : 0;
    result = 29 * result + ( getSubcategoryId() != null ? getSubcategoryId().hashCode() : 0 );
    result = 29 * result + ( getProductId() != null ? getProductId().hashCode() : 0 );
    return result;
  }

  public String getProductOrCategoryEndDate()
  {
    return productOrCategoryEndDate;
  }

  public void setProductOrCategoryEndDate( String productOrCategoryEndDate )
  {
    this.productOrCategoryEndDate = productOrCategoryEndDate;
  }

  public String getProductOrCategoryStartDate()
  {
    return productOrCategoryStartDate;
  }

  public void setProductOrCategoryStartDate( String productOrCategoryStartDate )
  {
    this.productOrCategoryStartDate = productOrCategoryStartDate;
  }

}
