/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/HomePageItem.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;

/*
 * HomePageItem <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar 6, 2006</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class HomePageItem extends BaseDomain
{
  private String productId;
  private String catalogId;
  private String productSetId;
  private String copy;
  private String description;
  private String detailImgUrl;
  private String tmbImageUrl;
  private String longDescription;

  private Promotion promotion;
  private String displayLevelName;

  public String getCatalogId()
  {
    return catalogId;
  }

  public void setCatalogId( String catalogId )
  {
    this.catalogId = catalogId;
  }

  public String getCopy()
  {
    return copy;
  }

  public void setCopy( String copy )
  {
    this.copy = copy;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getDetailImgUrl()
  {
    return detailImgUrl;
  }

  public void setDetailImgUrl( String detailImgUrl )
  {
    this.detailImgUrl = detailImgUrl;
  }

  public String getProductId()
  {
    return productId;
  }

  public void setProductId( String productId )
  {
    this.productId = productId;
  }

  public String getProductSetId()
  {
    return productSetId;
  }

  public void setProductSetId( String productSetId )
  {
    this.productSetId = productSetId;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public String getTmbImageUrl()
  {
    return tmbImageUrl;
  }

  public void setTmbImageUrl( String tmbImageUrl )
  {
    this.tmbImageUrl = tmbImageUrl;
  }

  public String getDisplayLevelName()
  {
    return displayLevelName;
  }

  public void setDisplayLevelName( String levelDisplayName )
  {
    this.displayLevelName = levelDisplayName;
  }

  public String getLongDescription()
  {
    return longDescription;
  }

  public void setLongDescription( String longDescription )
  {
    this.longDescription = longDescription;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( object == null )
    {
      return false;
    }
    if ( getClass() != object.getClass() )
    {
      return false;
    }
    final HomePageItem other = (HomePageItem)object;

    if ( promotion == null )
    {
      if ( other.promotion != null )
      {
        return false;
      }
    }
    else if ( !promotion.equals( other.promotion ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @return int
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   */
  public int hashCode()
  {
    return getId() != null ? getId().hashCode() : 0;
  }

  public HomePageItem deepCopy()
  {
    HomePageItem clone = new HomePageItem();
    clone.setCatalogId( getCatalogId() );
    clone.setCopy( getCopy() );
    clone.setDescription( getDescription() );
    clone.setDetailImgUrl( getDetailImgUrl() );
    clone.setProductId( getProductId() );
    clone.setProductSetId( getProductSetId() );
    clone.setTmbImageUrl( getTmbImageUrl() );
    clone.setDisplayLevelName( getDisplayLevelName() );
    clone.setLongDescription( getLongDescription() );

    return clone;
  }
}
