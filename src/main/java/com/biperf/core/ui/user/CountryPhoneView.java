
package com.biperf.core.ui.user;

import java.io.Serializable;

import com.biperf.core.domain.country.Country;

@SuppressWarnings( "serial" )
public class CountryPhoneView implements Serializable
{
  private Long countryId;
  private String label;

  public CountryPhoneView( Country country )
  {
    this.countryId = country.getId();
    this.label = country.getCountryNameandPhoneCodeDisplay();
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( countryId == null ) ? 0 : countryId.hashCode() );
    result = prime * result + ( ( label == null ) ? 0 : label.hashCode() );
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
    CountryPhoneView other = (CountryPhoneView)obj;
    if ( countryId == null )
    {
      if ( other.countryId != null )
      {
        return false;
      }
    }
    else if ( !countryId.equals( other.countryId ) )
    {
      return false;
    }
    if ( label == null )
    {
      if ( other.label != null )
      {
        return false;
      }
    }
    else if ( !label.equals( other.label ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "CountryPhoneView [countryId=" + countryId + ", label=" + label + "]";
  }

}
