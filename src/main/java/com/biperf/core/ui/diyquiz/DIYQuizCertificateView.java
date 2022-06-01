
package com.biperf.core.ui.diyquiz;

/**
 * 
 * DIYQuizCertificateView.
 * 
 * @author kandhi
 * @since Jul 10, 2013
 * @version 1.0
 */
public class DIYQuizCertificateView
{
  private Long id;
  private String name;
  private String img;
  private String imgLg;

  public DIYQuizCertificateView( Long id, String name, String img, String imgLg )
  {
    super();
    this.id = id;
    this.name = name;
    this.img = img;
    this.imgLg = imgLg;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getImg()
  {
    return img;
  }

  public void setImg( String img )
  {
    this.img = img;
  }

  public String getImgLg()
  {
    return imgLg;
  }

  public void setImgLg( String imgLg )
  {
    this.imgLg = imgLg;
  }

}
