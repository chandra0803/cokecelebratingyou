
package com.biperf.core.domain.client;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;

/**
 * TcccCountryRule.
 * 
 * This class is created as part of Client Customization for WIP #42683
 * 
 * @author dudam
 * @since Feb 09, 2018
 * @version 1.0
 */
public class TcccCountryRule extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  // Not binding all DB columns to the domain as we don't need them yet.
  private String countryCode;
  private String businessUnit;
  private Date dateEnd;
  private boolean allowOnlinePurl;

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getBusinessUnit()
  {
    return businessUnit;
  }

  public void setBusinessUnit( String businessUnit )
  {
    this.businessUnit = businessUnit;
  }

  public Date getDateEnd()
  {
    return dateEnd;
  }

  public void setDateEnd( Date dateEnd )
  {
    this.dateEnd = dateEnd;
  }

  public boolean isAllowOnlinePurl()
  {
    return allowOnlinePurl;
  }

  public void setAllowOnlinePurl( boolean allowOnlinePurl )
  {
    this.allowOnlinePurl = allowOnlinePurl;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( businessUnit == null ) ? 0 : businessUnit.hashCode() );
    result = prime * result + ( ( countryCode == null ) ? 0 : countryCode.hashCode() );
    result = prime * result + ( ( dateEnd == null ) ? 0 : dateEnd.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( obj == null )
      return false;
    if ( getClass() != obj.getClass() )
      return false;
    TcccCountryRule other = (TcccCountryRule)obj;
    if ( businessUnit == null )
    {
      if ( other.businessUnit != null )
        return false;
    }
    else if ( !businessUnit.equals( other.businessUnit ) )
      return false;
    if ( countryCode == null )
    {
      if ( other.countryCode != null )
        return false;
    }
    else if ( !countryCode.equals( other.countryCode ) )
      return false;
    if ( dateEnd == null )
    {
      if ( other.dateEnd != null )
        return false;
    }
    else if ( !dateEnd.equals( other.dateEnd ) )
      return false;
    return true;
  }

}
