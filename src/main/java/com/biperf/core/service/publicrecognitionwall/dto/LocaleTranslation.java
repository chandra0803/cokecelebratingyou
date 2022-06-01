
package com.biperf.core.service.publicrecognitionwall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocaleTranslation
{
  @JsonProperty
  private String locale;
  @JsonProperty
  private String text;

  public LocaleTranslation()
  {
  }

  public LocaleTranslation( String locale, String text )
  {
    this.locale = locale;
    this.text = text;
  }

  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( locale == null ? 0 : locale.hashCode() );
    result = prime * result + ( text == null ? 0 : text.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    LocaleTranslation other = (LocaleTranslation)obj;
    if ( locale == null )
    {
      if ( other.locale != null )
      {
        return false;
      }
    }
    else if ( !locale.equals( other.locale ) )
    {
      return false;
    }
    if ( text == null )
    {
      if ( other.text != null )
      {
        return false;
      }
    }
    else if ( !text.equals( other.text ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "LocaleTranslation [locale=" + locale + ", text=" + text + "]";
  }

}
