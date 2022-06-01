
package com.biperf.core.service.publicrecognitionwall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PublicRecognitionParticipant
{
  @JsonProperty
  private String firstName;
  @JsonProperty
  private String lastName;
  @JsonProperty
  private String avatarUrl;
  @JsonProperty
  private String orgUnit;
  @JsonProperty
  private String country;
  @JsonProperty
  private String countryFlagUrl;
  @JsonProperty
  private String jobTitle;
  @JsonProperty
  private String location;

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getOrgUnit()
  {
    return orgUnit;
  }

  public void setOrgUnit( String orgUnit )
  {
    this.orgUnit = orgUnit;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public String getCountryFlagUrl()
  {
    return countryFlagUrl;
  }

  public void setCountryFlagUrl( String countryFlagUrl )
  {
    this.countryFlagUrl = countryFlagUrl;
  }

  public String getJobTitle()
  {
    return jobTitle;
  }

  public void setJobTitle( String jobTitle )
  {
    this.jobTitle = jobTitle;
  }

  public String getLocation()
  {
    return location;
  }

  public void setLocation( String location )
  {
    this.location = location;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( avatarUrl == null ? 0 : avatarUrl.hashCode() );
    result = prime * result + ( country == null ? 0 : country.hashCode() );
    result = prime * result + ( countryFlagUrl == null ? 0 : countryFlagUrl.hashCode() );
    result = prime * result + ( firstName == null ? 0 : firstName.hashCode() );
    result = prime * result + ( jobTitle == null ? 0 : jobTitle.hashCode() );
    result = prime * result + ( lastName == null ? 0 : lastName.hashCode() );
    result = prime * result + ( location == null ? 0 : location.hashCode() );
    result = prime * result + ( orgUnit == null ? 0 : orgUnit.hashCode() );
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
    PublicRecognitionParticipant other = (PublicRecognitionParticipant)obj;
    if ( avatarUrl == null )
    {
      if ( other.avatarUrl != null )
      {
        return false;
      }
    }
    else if ( !avatarUrl.equals( other.avatarUrl ) )
    {
      return false;
    }
    if ( country == null )
    {
      if ( other.country != null )
      {
        return false;
      }
    }
    else if ( !country.equals( other.country ) )
    {
      return false;
    }
    if ( countryFlagUrl == null )
    {
      if ( other.countryFlagUrl != null )
      {
        return false;
      }
    }
    else if ( !countryFlagUrl.equals( other.countryFlagUrl ) )
    {
      return false;
    }
    if ( firstName == null )
    {
      if ( other.firstName != null )
      {
        return false;
      }
    }
    else if ( !firstName.equals( other.firstName ) )
    {
      return false;
    }
    if ( jobTitle == null )
    {
      if ( other.jobTitle != null )
      {
        return false;
      }
    }
    else if ( !jobTitle.equals( other.jobTitle ) )
    {
      return false;
    }
    if ( lastName == null )
    {
      if ( other.lastName != null )
      {
        return false;
      }
    }
    else if ( !lastName.equals( other.lastName ) )
    {
      return false;
    }
    if ( location == null )
    {
      if ( other.location != null )
      {
        return false;
      }
    }
    else if ( !location.equals( other.location ) )
    {
      return false;
    }
    if ( orgUnit == null )
    {
      if ( other.orgUnit != null )
      {
        return false;
      }
    }
    else if ( !orgUnit.equals( other.orgUnit ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "PublicRecognitionParticipant [firstName=" + firstName + ", lastName=" + lastName + ", avatarUrl=" + avatarUrl + ", orgUnit=" + orgUnit + ", country=" + country + ", countryFlatUrl="
        + countryFlagUrl + ", jobTitle=" + jobTitle + ", location=" + location + "]";
  }
}
