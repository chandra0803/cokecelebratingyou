/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/supplier/Supplier.java,v $
 */

package com.biperf.core.domain.supplier;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SupplierStatusType;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Supplier domain object which represents a supplier within the Beacon application.
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
 * <td>June 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Supplier extends BaseDomain
{
  public final static long ID_BI_BANK = 159;
  public final static long ID_OPISA = 160;
  public final static long ID_BII = 161;
  public final static long ID_PAYROLL_EXTRACT = 162;
  public final static long ID_EDENRED = 163;
  public static final String BI_BANK = "BI Bank";
  public static final String BII = "BII";

  public static final String INTERNAL = "internal";
  public static final String EXTERNAL = "external";

  public static final String CM_SUPPLIER_ASSET_PREFIX = "supplier.awards.page";
  public static final String CM_SUPPLIER_ASSET_TYPE = "_SupplierAwardsPage";
  public static final String CM_SUPPLIER_SECTION = "supplier_awards_page";
  public static final String CM_SUPPLIER_IMAGE_KEY = "IMAGE_NAME";
  public static final String CM_SUPPLIER_IMAGE_KEY_DESC = "Image Name";
  public static final String CM_SUPPLIER_NAME_KEY = "SUPPLIER_NAME";
  public static final String CM_SUPPLIER_NAME_KEY_DESC = "Supplier Name";
  public static final String CM_SUPPLIER_DESCRIPTION_KEY = "SUPPLIER_DESCRIPTION";
  public static final String CM_SUPPLIER_DESCRIPTION_DESC = "Supplier Description";
  public static final String CM_SUPPLIER_BUTTON_KEY = "BUTTON_NAME";
  public static final String CM_SUPPLIER_BUTTON_KEY_DESC = "Button Name";
  public static final String CM_MERCH_SUPPLIER_NAME_KEY = "MENU_MERCHANDISE";
  public static final String CM_MERCH_SUPPLIER_BUTTON_KEY = "MERCHANDISE_BUTTON";
  public static final String CM_REDEEM_ASSET_KEY = "redeem.menu";

  private SupplierStatusType status;

  private String supplierName;
  private String supplierType;
  private String description;
  private String catalogUrl;
  private String catalogTargetId;
  private String statementUrl;
  private String statementTargetId;

  // Supplier awards page
  private String imageName;
  private String buttonName;

  private String cmAssetCode = "";
  private String imageCmKey = "";
  private String titleCmKey = "";
  private String descCmKey = "";
  private String buttonCmKey = "";

  private Boolean allowPartnerSso;

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Supplier [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{supplierName=" + this.getSupplierName() + "}, " );
    buf.append( "{supplierType=" + this.getSupplierType() + "}, " );
    buf.append( "{description=" + this.getDescription() + "}, " );
    buf.append( "{status=" + this.getStatus().getCode() + "}, " );
    buf.append( "{catalogUrl=" + this.getCatalogUrl() + "}, " );
    buf.append( "{catalogTargetId=" + this.getCatalogTargetId() + "}" );
    buf.append( "{statementUrl=" + this.getStatementUrl() + "}" );
    buf.append( "{statementTargetId=" + this.getStatementTargetId() + "}" );
    buf.append( "{cmAssetCode=" + this.getCmAssetCode() + "}" );
    buf.append( "{imageCmKey=" + this.getImageCmKey() + "}" );
    buf.append( "{titleCmKey=" + this.getTitleCmKey() + "}" );
    buf.append( "{descCmKey=" + this.getDescCmKey() + "}" );
    buf.append( "{buttonCmKey=" + this.getButtonCmKey() + "}" );
    buf.append( "{allowPartnerSso" ).append( this.getAllowPartnerSso() ).append( "}" );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Supplier ) )
    {
      return false;
    }

    final Supplier supplier = (Supplier)object;

    if ( getSupplierName() != null ? !getSupplierName().equals( supplier.getSupplierName() ) : supplier.getSupplierName() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return getSupplierName() != null ? getSupplierName().hashCode() : 0;
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

  public SupplierStatusType getStatus()
  {
    return status;
  }

  public void setStatus( SupplierStatusType status )
  {
    this.status = status;
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

  public void setImageName( String imageName )
  {
    this.imageName = imageName;
  }

  public String getImageName()
  {
    return imageName;
  }

  public void setButtonName( String buttonName )
  {
    this.buttonName = buttonName;
  }

  public String getButtonName()
  {
    return buttonName;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setImageCmKey( String imageCmKey )
  {
    this.imageCmKey = imageCmKey;
  }

  public String getImageCmKey()
  {
    return imageCmKey;
  }

  public void setTitleCmKey( String titleCmKey )
  {
    this.titleCmKey = titleCmKey;
  }

  public String getTitleCmKey()
  {
    return titleCmKey;
  }

  public void setDescCmKey( String descCmKey )
  {
    this.descCmKey = descCmKey;
  }

  public String getDescCmKey()
  {
    return descCmKey;
  }

  public void setButtonCmKey( String buttonCmKey )
  {
    this.buttonCmKey = buttonCmKey;
  }

  public String getButtonCmKey()
  {
    return buttonCmKey;
  }

  public String getSupplierNameText()
  {
    return CmsResourceBundle.getCmsBundle().getString( getCmAssetCode(), Supplier.CM_SUPPLIER_NAME_KEY );
  }

  public String getMerchSupplierNameText()
  {
    return CmsResourceBundle.getCmsBundle().getString( Supplier.CM_REDEEM_ASSET_KEY, Supplier.CM_MERCH_SUPPLIER_NAME_KEY );
  }

  public String getMerchButtonText()
  {
    return CmsResourceBundle.getCmsBundle().getString( Supplier.CM_REDEEM_ASSET_KEY, Supplier.CM_MERCH_SUPPLIER_BUTTON_KEY );
  }

  public String getSupplierDescText()
  {
    String description = CmsResourceBundle.getCmsBundle().getString( getCmAssetCode(), Supplier.CM_SUPPLIER_DESCRIPTION_KEY );
    if ( description.startsWith( "???" ) )
    {
      description = "";
    }
    return description;
  }

  public String getImageNameText()
  {
    return CmsResourceBundle.getCmsBundle().getString( getCmAssetCode(), Supplier.CM_SUPPLIER_IMAGE_KEY );
  }

  public String getButtonText()
  {
    return CmsResourceBundle.getCmsBundle().getString( getCmAssetCode(), Supplier.CM_SUPPLIER_BUTTON_KEY );
  }

  public void setAllowPartnerSso( Boolean allowPartnerSso )
  {
    this.allowPartnerSso = allowPartnerSso;
  }

  public Boolean getAllowPartnerSso()
  {
    return allowPartnerSso;
  }

}
