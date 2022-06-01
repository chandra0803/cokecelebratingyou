
package com.biperf.core.domain.country;

import java.io.Serializable;
import java.sql.Timestamp;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.utils.UserManager;

public class CountrySupplier implements AuditCreateInterface, Serializable
{

  private Country country;
  private Supplier supplier;
  private Boolean primary;

  private AuditCreateInfo auditCreateInfo = new AuditCreateInfo();

  public CountrySupplier()
  {
    auditCreateInfo.setCreatedBy( UserManager.getUserId() );
    auditCreateInfo.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  public CountrySupplier( Country country, Supplier supplier )
  {
    this();
    this.setCountry( country );
    this.setSupplier( supplier );
  }

  public AuditCreateInfo getAuditCreateInfo()
  {
    return auditCreateInfo;
  }

  public void setAuditCreateInfo( AuditCreateInfo auditCreateInfo )
  {
    this.auditCreateInfo = auditCreateInfo;
  }

  public void setCountry( Country country )
  {
    this.country = country;
  }

  public Country getCountry()
  {
    return country;
  }

  public void setSupplier( Supplier supplier )
  {
    this.supplier = supplier;
  }

  public Supplier getSupplier()
  {
    return supplier;
  }

  public Boolean getPrimary()
  {
    return this.primary;
  }

  public Boolean isPrimary()
  {
    return this.primary;
  }

  public void setPrimary( Boolean primary )
  {
    this.primary = primary;
  }

  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }

    if ( ! ( obj instanceof CountrySupplier ) )
    {
      return false;
    }

    final CountrySupplier that = (CountrySupplier)obj;

    if ( getCountry() != null ? !getCountry().equals( that.getCountry() ) : that.getCountry() != null )
    {
      return false;
    }
    if ( getSupplier() != null ? !getSupplier().equals( that.getSupplier() ) : that.getSupplier() != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;

    result = getCountry() != null ? getCountry().hashCode() : 0;
    result = 31 * result + ( getSupplier() != null ? getSupplier().hashCode() : 0 );

    return result;
  }
}
