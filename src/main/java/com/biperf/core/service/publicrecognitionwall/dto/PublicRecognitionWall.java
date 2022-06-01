
package com.biperf.core.service.publicrecognitionwall.dto;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PublicRecognitionWall
{
  @JsonProperty
  @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX" )
  private Date asOfDate;
  
  @JsonProperty
  private Set<RecognitionEntry> recognitions = new LinkedHashSet<RecognitionEntry>();

  public Date getAsOfDate()
  {
    return asOfDate;
  }

  public void setAsOfDate( Date asOfDate )
  {
    this.asOfDate = asOfDate;
  }

  public Set<RecognitionEntry> getRecognitions()
  {
    return recognitions;
  }

  public void setRecognitions( Set<RecognitionEntry> recognitions )
  {
    this.recognitions = recognitions;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( asOfDate == null ? 0 : asOfDate.hashCode() );
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
    PublicRecognitionWall other = (PublicRecognitionWall)obj;
    if ( asOfDate == null )
    {
      if ( other.asOfDate != null )
      {
        return false;
      }
    }
    else if ( !asOfDate.equals( other.asOfDate ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "PublicRecognitionWall [asOfDate=" + asOfDate + "]";
  }
}
