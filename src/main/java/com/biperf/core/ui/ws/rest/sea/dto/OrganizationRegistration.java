
package com.biperf.core.ui.ws.rest.sea.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement  
public class OrganizationRegistration
{
  private String orgCode;
  private String orgName;
  private String domainName;
  private String emailAccountName;
  private String emailAccountPassword;
  private String serviceEndPoint;
  private int serviceVersion ;
  private int daysToAbandoned ;

  public OrganizationRegistration(){}
  
  public OrganizationRegistration( String orgCode, String orgName, String domainName, String emailAccountName, String emailAccountPassword, String serviceEndPoint, int serviceVersion, int daysToAbandoned )
  {
    super();
    this.orgCode = orgCode;
    this.orgName = orgName;
    this.domainName = domainName;
    this.emailAccountName = emailAccountName;
    this.emailAccountPassword = emailAccountPassword;
    this.serviceEndPoint = serviceEndPoint;
    this.serviceVersion = serviceVersion ;
    this.daysToAbandoned = daysToAbandoned ;
  }

  public String getOrgCode()
  {
    return orgCode;
  }

  public void setOrgCode( String orgCode )
  {
    this.orgCode = orgCode;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getDomainName()
  {
    return domainName;
  }

  public void setDomainName( String domainName )
  {
    this.domainName = domainName;
  }

  public String getEmailAccountName()
  {
    return emailAccountName;
  }

  public void setEmailAccountName( String emailAccountName )
  {
    this.emailAccountName = emailAccountName;
  }

  public String getEmailAccountPassword()
  {
    return emailAccountPassword;
  }

  public void setEmailAccountPassword( String emailAccountPassword )
  {
    this.emailAccountPassword = emailAccountPassword;
  }

  public String getServiceEndPoint()
  {
    return serviceEndPoint;
  }

  public void setServiceEndPoint( String serviceEndPoint )
  {
    this.serviceEndPoint = serviceEndPoint;
  }
  
  public int getServiceVersion()
  {
    return serviceVersion;
  }

  public void setServiceVersion( int serviceVersion )
  {
    this.serviceVersion = serviceVersion;
  }
  
  public int getDaysToAbandoned()
  {
    return daysToAbandoned;
  }

  public void setDaysToAbandoned( int daysToAbandoned )
  {
    this.daysToAbandoned = daysToAbandoned;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( domainName == null ) ? 0 : domainName.hashCode() );
    result = prime * result + ( ( emailAccountName == null ) ? 0 : emailAccountName.hashCode() );
    result = prime * result + ( ( emailAccountPassword == null ) ? 0 : emailAccountPassword.hashCode() );
    result = prime * result + ( ( orgCode == null ) ? 0 : orgCode.hashCode() );
    result = prime * result + ( ( orgName == null ) ? 0 : orgName.hashCode() );
    result = prime * result + ( ( serviceEndPoint == null ) ? 0 : serviceEndPoint.hashCode() );
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
    OrganizationRegistration other = (OrganizationRegistration)obj;
    if ( domainName == null )
    {
      if ( other.domainName != null )
        return false;
    }
    else if ( !domainName.equals( other.domainName ) )
      return false;
    if ( emailAccountName == null )
    {
      if ( other.emailAccountName != null )
        return false;
    }
    else if ( !emailAccountName.equals( other.emailAccountName ) )
      return false;
    if ( emailAccountPassword == null )
    {
      if ( other.emailAccountPassword != null )
        return false;
    }
    else if ( !emailAccountPassword.equals( other.emailAccountPassword ) )
      return false;
    if ( orgCode == null )
    {
      if ( other.orgCode != null )
        return false;
    }
    else if ( !orgCode.equals( other.orgCode ) )
      return false;
    if ( orgName == null )
    {
      if ( other.orgName != null )
        return false;
    }
    else if ( !orgName.equals( other.orgName ) )
      return false;
    if ( serviceEndPoint == null )
    {
      if ( other.serviceEndPoint != null )
        return false;
    }
    else if ( !serviceEndPoint.equals( other.serviceEndPoint ) )
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "OrganizationRegistration [orgCode=" + orgCode + ", orgName=" + orgName + ", domainName=" + domainName + ", emailAccountName=" + emailAccountName + ", emailAccountPassword="
        + emailAccountPassword + ", serviceEndPoint=" + serviceEndPoint + "]";
  }
}
