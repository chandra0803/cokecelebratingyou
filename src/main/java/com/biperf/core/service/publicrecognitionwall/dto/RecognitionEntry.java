
package com.biperf.core.service.publicrecognitionwall.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RecognitionEntry
{
  @JsonProperty
  @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX" )
  private Date recognitionDate; // GMT
  @JsonProperty
  private String comments;
  @JsonProperty
  private String promotionName;
  @JsonProperty
  private String promotionType;
  @JsonProperty
  private Set<LocaleTranslation> behavior = new HashSet<LocaleTranslation>();
  @JsonProperty
  private String eCardUrl;

  @JsonProperty
  private PublicRecognitionParticipant giver;
  @JsonProperty
  private Set<PublicRecognitionParticipant> receivers = new HashSet<PublicRecognitionParticipant>();

  public PublicRecognitionParticipant getGiver()
  {
    return giver;
  }

  public void setGiver( PublicRecognitionParticipant giver )
  {
    this.giver = giver;
  }

  public Set<PublicRecognitionParticipant> getReceivers()
  {
    return receivers;
  }

  public void setReceivers( Set<PublicRecognitionParticipant> receivers )
  {
    this.receivers = receivers;
  }

  public Date getRecognitionDate()
  {
    return recognitionDate;
  }

  public void setRecognitionDate( Date recognitionDate )
  {
    this.recognitionDate = recognitionDate;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public Set<LocaleTranslation> getBehavior()
  {
    return behavior;
  }

  public void setBehavior( Set<LocaleTranslation> behavior )
  {
    this.behavior = behavior;
  }

  public String geteCardUrl()
  {
    return eCardUrl;
  }

  public void seteCardUrl( String eCardUrl )
  {
    this.eCardUrl = eCardUrl;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( behavior == null ? 0 : behavior.hashCode() );
    result = prime * result + ( comments == null ? 0 : comments.hashCode() );
    result = prime * result + ( eCardUrl == null ? 0 : eCardUrl.hashCode() );
    result = prime * result + ( giver == null ? 0 : giver.hashCode() );
    result = prime * result + ( promotionName == null ? 0 : promotionName.hashCode() );
    result = prime * result + ( promotionType == null ? 0 : promotionType.hashCode() );
    result = prime * result + ( receivers == null ? 0 : receivers.hashCode() );
    result = prime * result + ( recognitionDate == null ? 0 : recognitionDate.hashCode() );
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
    RecognitionEntry other = (RecognitionEntry)obj;
    if ( behavior == null )
    {
      if ( other.behavior != null )
      {
        return false;
      }
    }
    else if ( !behavior.equals( other.behavior ) )
    {
      return false;
    }
    if ( comments == null )
    {
      if ( other.comments != null )
      {
        return false;
      }
    }
    else if ( !comments.equals( other.comments ) )
    {
      return false;
    }
    if ( eCardUrl == null )
    {
      if ( other.eCardUrl != null )
      {
        return false;
      }
    }
    else if ( !eCardUrl.equals( other.eCardUrl ) )
    {
      return false;
    }
    if ( giver == null )
    {
      if ( other.giver != null )
      {
        return false;
      }
    }
    else if ( !giver.equals( other.giver ) )
    {
      return false;
    }
    if ( promotionName == null )
    {
      if ( other.promotionName != null )
      {
        return false;
      }
    }
    else if ( !promotionName.equals( other.promotionName ) )
    {
      return false;
    }
    if ( promotionType == null )
    {
      if ( other.promotionType != null )
      {
        return false;
      }
    }
    else if ( !promotionType.equals( other.promotionType ) )
    {
      return false;
    }
    if ( receivers == null )
    {
      if ( other.receivers != null )
      {
        return false;
      }
    }
    else if ( !receivers.equals( other.receivers ) )
    {
      return false;
    }
    if ( recognitionDate == null )
    {
      if ( other.recognitionDate != null )
      {
        return false;
      }
    }
    else if ( !recognitionDate.equals( other.recognitionDate ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "RecognitionEntry [giver=" + giver + ", receivers=" + receivers + ", recognitionDate=" + recognitionDate + ", comments=" + comments + ", promotionName=" + promotionName + ", promotionType="
        + promotionType + ", behavior=" + behavior + ", eCardUrl=" + eCardUrl + "]";
  }
}
