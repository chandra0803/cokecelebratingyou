
package com.biperf.core.service.awardbanq.impl;

import java.util.ArrayList;
import java.util.List;

public class MerchLevelProductValueObject
{
  private String catalogId;
  private String detailImageURL;
  private String productCopy;
  private List<String> productIds = new ArrayList<String>();
  private String productSetId;
  private String thumbnailImageURL;
  private ProductGroupDescriptionsVO productGroupDescriptions = new ProductGroupDescriptionsVO();

  public MerchLevelProductValueObject()
  {

  }

  public String getCatalogId()
  {
    return catalogId;
  }

  public void setCatalogId( String catalogId )
  {
    this.catalogId = catalogId;
  }

  public String getDetailImageURL()
  {
    return detailImageURL;
  }

  public void setDetailImageURL( String detailImageURL )
  {
    this.detailImageURL = detailImageURL;
  }

  public String getProductCopy()
  {
    return productCopy;
  }

  public void setProductCopy( String productCopy )
  {
    this.productCopy = productCopy;
  }

  public List<String> getProductIds()
  {
    return productIds;
  }

  public void setProductIds( List<String> productIds )
  {
    this.productIds = productIds;
  }

  public String getProductSetId()
  {
    return productSetId;
  }

  public void setProductSetId( String productSetId )
  {
    this.productSetId = productSetId;
  }

  public String getThumbnailImageURL()
  {
    return thumbnailImageURL;
  }

  public void setThumbnailImageURL( String thumbnailImageURL )
  {
    this.thumbnailImageURL = thumbnailImageURL;
  }

  public ProductGroupDescriptionsVO getProductGroupDescriptions()
  {
    return productGroupDescriptions;
  }

  public void setProductGroupDescriptions( ProductGroupDescriptionsVO productGroupDescriptions )
  {
    this.productGroupDescriptions = productGroupDescriptions;
  }

}
