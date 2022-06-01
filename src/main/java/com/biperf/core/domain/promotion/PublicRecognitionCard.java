
package com.biperf.core.domain.promotion;

public class PublicRecognitionCard
{
  private Long id;
  private String name;
  private String imgUrl;
  private String imgThumbUrl;

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getImgUrl()
  {
    return imgUrl;
  }

  public void setImgUrl( String imgUrl )
  {
    this.imgUrl = imgUrl;
  }

  public String getImgThumbUrl()
  {
    return imgThumbUrl;
  }

  public void setImgThumbUrl( String imgThumbUrl )
  {
    this.imgThumbUrl = imgThumbUrl;
  }

}
