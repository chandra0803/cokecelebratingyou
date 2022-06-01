
package com.biperf.core.domain.banner;

public class GenericBannerDetailBean
{
  private String id;
  private String text;
  private String linkText;
  private String linkUrl;
  private String target;
  private String bannerImageUrl;
  private String bannerImageUrl_max;
  private String bannerImageUrl_mobile;
  private String classes;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public String getLinkText()
  {
    return linkText;
  }

  public void setLinkText( String linkText )
  {
    this.linkText = linkText;
  }

  public String getLinkUrl()
  {
    return linkUrl;
  }

  public void setLinkUrl( String linkUrl )
  {
    this.linkUrl = linkUrl;
  }

  public String getTarget()
  {
    return target;
  }

  public void setTarget( String target )
  {
    this.target = target;
  }

  public String getClasses()
  {
    return classes;
  }

  public void setClasses( String classes )
  {
    this.classes = classes;
  }

  public String getBannerImageUrl()
  {
    return bannerImageUrl;
  }

  public void setBannerImageUrl( String bannerImageUrl )
  {
    this.bannerImageUrl = bannerImageUrl;
  }

  public String getBannerImageUrl_max()
  {
    return bannerImageUrl_max;
  }

  public void setBannerImageUrl_max( String bannerImageUrl_max )
  {
    this.bannerImageUrl_max = bannerImageUrl_max;
  }

  public String getBannerImageUrl_mobile()
  {
    return bannerImageUrl_mobile;
  }

  public void setBannerImageUrl_mobile( String bannerImageUrl_mobile )
  {
    this.bannerImageUrl_mobile = bannerImageUrl_mobile;
  }
}
