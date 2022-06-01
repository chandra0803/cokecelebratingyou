
package com.biperf.core.ui.diyquiz;

/**
 * 
 * DIYQuizBadgeView.
 * 
 * @author kandhi
 * @since Jul 10, 2013
 * @version 1.0
 */
public class DIYQuizBadgeView
{
  private Long id;
  private String type;
  private String name;
  private String howToEarnText;
  private String img;

  public DIYQuizBadgeView( Long id, String type, String name, String howToEarnText, String img )
  {
    super();
    this.id = id;
    this.type = type;
    this.name = name;
    this.howToEarnText = howToEarnText;
    this.img = img;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getHowToEarnText()
  {
    return howToEarnText;
  }

  public void setHowToEarnText( String howToEarnText )
  {
    this.howToEarnText = howToEarnText;
  }

  public String getImg()
  {
    return img;
  }

  public void setImg( String img )
  {
    this.img = img;
  }

}
