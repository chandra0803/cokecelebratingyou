/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/supplier/SupplierForm.java,v $
 */

package com.biperf.core.ui.supplier;

import java.sql.Timestamp;

import com.biperf.core.domain.enums.SupplierStatusType;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;

/**
 * Supplier ActionForm transfer object.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SupplierForm extends BaseForm
{
  private Long supplierId;
  private Long version;
  private String supplierName;
  private String supplierType = "internal";
  private String description;
  private String status;
  private String catalogUrl;
  private String catalogTargetId;
  private String statementUrl;
  private String statementTargetId;
  private String method;
  private String createdBy = "";
  private long dateCreated;
  private String cmAssetCode = "";

  private Boolean allowPartnerSso = Boolean.FALSE;

  /**
   * Load Supplier Item
   * 
   * @param supplier
   */
  public void load( Supplier supplier )
  {
    this.supplierId = supplier.getId();
    this.version = supplier.getVersion();
    this.supplierName = supplier.getSupplierName();
    this.supplierType = supplier.getSupplierType();
    this.description = supplier.getDescription();
    this.status = supplier.getStatus().getCode();
    this.catalogUrl = supplier.getCatalogUrl();
    this.catalogTargetId = supplier.getCatalogTargetId();
    this.statementUrl = supplier.getStatementUrl();
    this.statementTargetId = supplier.getStatementTargetId();
    this.createdBy = supplier.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = supplier.getAuditCreateInfo().getDateCreated().getTime();
    this.allowPartnerSso = supplier.getAllowPartnerSso();
    this.cmAssetCode = supplier.getCmAssetCode();
  }

  /**
   * Sets the supplier in the Domain Object
   * 
   * @return supplier
   */
  public Supplier toDomainObject()
  {
    Supplier supplier = new Supplier();
    supplier.setId( this.supplierId );
    supplier.setVersion( this.version );
    supplier.setSupplierName( this.supplierName );
    supplier.setDescription( this.description );
    supplier.setSupplierType( this.supplierType );
    supplier.setStatus( SupplierStatusType.lookup( this.status ) );
    supplier.setCatalogUrl( this.catalogUrl );
    supplier.setCatalogTargetId( this.catalogTargetId );
    supplier.setStatementUrl( this.statementUrl );
    supplier.setStatementTargetId( this.statementTargetId );
    supplier.setAllowPartnerSso( this.allowPartnerSso );

    if ( null != supplier.getId() )
    {
      supplier.setCmAssetCode( this.cmAssetCode );
    }

    if ( this.supplierType.equals( "internal" ) )
    {
      supplier.setImageName( "AwardslinQ" );
      supplier.setButtonName( "View Catalog" );
    }
    else if ( this.supplierType.equals( "external" ) )
    {
      supplier.setImageName( "International Vendor" );
      supplier.setButtonName( "View Catalog" );
    }

    if ( StringUtils.isEmpty( this.createdBy ) )
    {
      supplier.getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    }
    else
    {
      supplier.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }

    supplier.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );

    return supplier;
  }

  public String getCatalogTargetId()
  {
    return catalogTargetId;
  }

  public void setCatalogTargetId( String catalogTargetId )
  {
    this.catalogTargetId = catalogTargetId;
  }

  public String getCatalogUrl()
  {
    return catalogUrl;
  }

  public void setCatalogUrl( String catalogUrl )
  {
    this.catalogUrl = catalogUrl;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getStatementTargetId()
  {
    return statementTargetId;
  }

  public void setStatementTargetId( String statementTargetId )
  {
    this.statementTargetId = statementTargetId;
  }

  public String getStatementUrl()
  {
    return statementUrl;
  }

  public void setStatementUrl( String statementUrl )
  {
    this.statementUrl = statementUrl;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public Long getSupplierId()
  {
    return supplierId;
  }

  public void setSupplierId( Long supplierId )
  {
    this.supplierId = supplierId;
  }

  public String getSupplierName()
  {
    return supplierName;
  }

  public void setSupplierName( String supplierName )
  {
    this.supplierName = supplierName;
  }

  public String getSupplierType()
  {
    return supplierType;
  }

  public void setSupplierType( String supplierType )
  {
    this.supplierType = supplierType;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public Boolean getAllowPartnerSso()
  {
    return allowPartnerSso;
  }

  public void setAllowPartnerSso( Boolean allowPartnerSso )
  {
    this.allowPartnerSso = allowPartnerSso;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

}
