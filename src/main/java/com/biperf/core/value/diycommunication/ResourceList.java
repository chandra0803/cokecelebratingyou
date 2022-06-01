
package com.biperf.core.value.diycommunication;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceList implements Serializable
{
  private String id;
  @JsonProperty( "isNew" )
  private boolean isNew = false;
  private String languageDisplay;
  private int index;
  private String imageId;
  private String language;
  private String title;
  private String link;
  private String imageSize;
  private String imageSize_max;
  private String imageSize_mobile;
  private String content;
  private String story;
  private String headline;
  private boolean defaultLang;
  private boolean systemLanguage;
  private boolean systemLocale;

  public ResourceList()
  {

  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public void setIsNew( boolean isNew )
  {
    this.isNew = isNew;
  }

  public boolean getIsNew()
  {
    return isNew;
  }

  public String getLanguageDisplay()
  {
    return languageDisplay;
  }

  public void setLanguageDisplay( String languageDisplay )
  {
    this.languageDisplay = languageDisplay;
  }

  public void setIndex( int index )
  {
    this.index = index;
  }

  public int getIndex()
  {
    return index;
  }

  public void setImageId( String imageId )
  {
    this.imageId = imageId;
  }

  public String getImageId()
  {
    return imageId;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getLink()
  {
    return link;
  }

  public void setLink( String link )
  {
    this.link = link;
  }

  public void setLanguage( String language )
  {
    this.language = language;
  }

  public String getLanguage()
  {
    return language;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent( String content )
  {
    this.content = content;
  }

  public String getStory()
  {
    return story;
  }

  public void setStory( String story )
  {
    this.story = story;
  }

  public String getHeadline()
  {
    return headline;
  }

  public void setHeadline( String headline )
  {
    this.headline = headline;
  }

  public boolean isIsDefaultLang()
  {
    return defaultLang;
  }

  public void setIsDefaultLang( boolean defaultLang )
  {
    this.defaultLang = defaultLang;
  }

  public boolean isIsSystemLanguage()
  {
    return systemLanguage;
  }

  public void setIsSystemLanguage( boolean systemLanguage )
  {
    this.systemLanguage = systemLanguage;
  }

  public boolean isSystemLocale()
  {
    return systemLocale;
  }

  public void setSystemLocale( boolean systemLocale )
  {
    this.systemLocale = systemLocale;
  }

  public String getImageSize()
  {
    return imageSize;
  }

  public void setImageSize( String imageSize )
  {
    this.imageSize = imageSize;
  }

  public String getImageSize_max()
  {
    return imageSize_max;
  }

  public void setImageSize_max( String imageSize_max )
  {
    this.imageSize_max = imageSize_max;
  }

  public String getImageSize_mobile()
  {
    return imageSize_mobile;
  }

  public void setImageSize_mobile( String imageSize_mobile )
  {
    this.imageSize_mobile = imageSize_mobile;
  }

}
