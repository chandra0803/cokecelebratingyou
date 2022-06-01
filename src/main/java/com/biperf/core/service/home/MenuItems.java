package com.biperf.core.service.home;

public class MenuItems
{
  private String title;
  private String url;
  private String imgPath;
  private int pos;

  public MenuItems()
  {
  }

  public MenuItems( String title, String url, String imgPath, int pos )
  {
    super();
    this.title = title;
    this.url = url;
    this.imgPath = imgPath;
    this.pos = pos;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getImgPath()
  {
    return imgPath;
  }

  public void setImgPath( String imgPath )
  {
    this.imgPath = imgPath;
  }

  public int getPos()
  {
    return pos;
  }

  public void setPos( int pos )
  {
    this.pos = pos;
  }

}
