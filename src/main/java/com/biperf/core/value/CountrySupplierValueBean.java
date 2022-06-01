
package com.biperf.core.value;

import java.io.Serializable;

public class CountrySupplierValueBean implements Serializable
{

  private static final long serialVersionUID = 1L;

  private Boolean isPrimary;
  private String supplierName;
  private String supplierId;
  private String selectedSupplierId;
  private String supplierType;

  public CountrySupplierValueBean()
  {

  }

  public Boolean getIsPrimary()
  {
    return isPrimary;
  }

  public void setIsPrimary( Boolean isPrimary )
  {
    this.isPrimary = isPrimary;
  }

  public String getSupplierName()
  {
    return supplierName;
  }

  public void setSupplierName( String supplierName )
  {
    this.supplierName = supplierName;
  }

  public String getSupplierId()
  {
    return supplierId;
  }

  public void setSupplierId( String supplierId )
  {
    this.supplierId = supplierId;
  }

  public String getSupplierType()
  {
    return supplierType;
  }

  public void setSupplierType( String supplierType )
  {
    this.supplierType = supplierType;
  }

  public void setSelectedSupplierId( String selectedSupplierId )
  {
    this.selectedSupplierId = selectedSupplierId;
  }

  public String getSelectedSupplierId()
  {
    return selectedSupplierId;
  }

}
