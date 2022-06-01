
package com.biperf.core.ui.quiz;

public class QuizTakeBadge
{
  private Long id;
  private String type;
  private String name;
  private String howToEarnText;
  private String img;

  public QuizTakeBadge()
  {
    super();
  }

  public QuizTakeBadge( Long badgeId, String badgeName, String imgUrl )
  {
    if ( badgeId != null )
    {
      this.setId( badgeId );
      this.name = badgeName;
      this.img = imgUrl;
    }
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
