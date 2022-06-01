/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.fileload;

import com.biperf.core.domain.BaseDomain;

/**
 * .
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
 * <td>wadzinsk</td>
 * <td>Mar 9, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductClaimImportProductRecord extends BaseDomain
{
  private Long productId;
  private String productName;
  private Integer soldQuantity;

  private ProductClaimImportRecord productClaimImportRecord;

  private Long productCharacteristicId1;
  private String productCharacteristicName1;
  private String productCharacteristicValue1;

  private Long productCharacteristicId2;
  private String productCharacteristicName2;
  private String productCharacteristicValue2;

  private Long productCharacteristicId3;
  private String productCharacteristicName3;
  private String productCharacteristicValue3;

  private Long productCharacteristicId4;
  private String productCharacteristicName4;
  private String productCharacteristicValue4;

  private Long productCharacteristicId5;
  private String productCharacteristicName5;
  private String productCharacteristicValue5;

  /**
   * Ensure equality between this and the object param. Overridden from
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

    if ( ! ( object instanceof ParticipantImportRecord ) )
    {
      return false;
    }

    ParticipantImportRecord participantImportRecord = (ParticipantImportRecord)object;

    if ( this.getId() != null && this.getId().equals( participantImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * @return value of productCharacteristicName1 property
   */
  public String getProductCharacteristicName1()
  {
    return productCharacteristicName1;
  }

  /**
   * @param productCharacteristicName1 value for productCharacteristicName1 property
   */
  public void setProductCharacteristicName1( String productCharacteristicName1 )
  {
    this.productCharacteristicName1 = productCharacteristicName1;
  }

  /**
   * @return value of productCharacteristicName2 property
   */
  public String getProductCharacteristicName2()
  {
    return productCharacteristicName2;
  }

  /**
   * @param productCharacteristicName2 value for productCharacteristicName2 property
   */
  public void setProductCharacteristicName2( String productCharacteristicName2 )
  {
    this.productCharacteristicName2 = productCharacteristicName2;
  }

  /**
   * @return value of productCharacteristicName3 property
   */
  public String getProductCharacteristicName3()
  {
    return productCharacteristicName3;
  }

  /**
   * @param productCharacteristicName3 value for productCharacteristicName3 property
   */
  public void setProductCharacteristicName3( String productCharacteristicName3 )
  {
    this.productCharacteristicName3 = productCharacteristicName3;
  }

  /**
   * @return value of productCharacteristicName4 property
   */
  public String getProductCharacteristicName4()
  {
    return productCharacteristicName4;
  }

  /**
   * @param productCharacteristicName4 value for productCharacteristicName4 property
   */
  public void setProductCharacteristicName4( String productCharacteristicName4 )
  {
    this.productCharacteristicName4 = productCharacteristicName4;
  }

  /**
   * @return value of productCharacteristicName5 property
   */
  public String getProductCharacteristicName5()
  {
    return productCharacteristicName5;
  }

  /**
   * @param productCharacteristicName5 value for productCharacteristicName5 property
   */
  public void setProductCharacteristicName5( String productCharacteristicName5 )
  {
    this.productCharacteristicName5 = productCharacteristicName5;
  }

  /**
   * @return value of productCharacteristicValue1 property
   */
  public String getProductCharacteristicValue1()
  {
    return productCharacteristicValue1;
  }

  /**
   * @param productCharacteristicValue1 value for productCharacteristicValue1 property
   */
  public void setProductCharacteristicValue1( String productCharacteristicValue1 )
  {
    this.productCharacteristicValue1 = productCharacteristicValue1;
  }

  /**
   * @return value of productCharacteristicValue2 property
   */
  public String getProductCharacteristicValue2()
  {
    return productCharacteristicValue2;
  }

  /**
   * @param productCharacteristicValue2 value for productCharacteristicValue2 property
   */
  public void setProductCharacteristicValue2( String productCharacteristicValue2 )
  {
    this.productCharacteristicValue2 = productCharacteristicValue2;
  }

  /**
   * @return value of productCharacteristicValue3 property
   */
  public String getProductCharacteristicValue3()
  {
    return productCharacteristicValue3;
  }

  /**
   * @param productCharacteristicValue3 value for productCharacteristicValue3 property
   */
  public void setProductCharacteristicValue3( String productCharacteristicValue3 )
  {
    this.productCharacteristicValue3 = productCharacteristicValue3;
  }

  /**
   * @return value of productCharacteristicValue4 property
   */
  public String getProductCharacteristicValue4()
  {
    return productCharacteristicValue4;
  }

  /**
   * @param productCharacteristicValue4 value for productCharacteristicValue4 property
   */
  public void setProductCharacteristicValue4( String productCharacteristicValue4 )
  {
    this.productCharacteristicValue4 = productCharacteristicValue4;
  }

  /**
   * @return value of productCharacteristicValue5 property
   */
  public String getProductCharacteristicValue5()
  {
    return productCharacteristicValue5;
  }

  /**
   * @param productCharacteristicValue5 value for productCharacteristicValue5 property
   */
  public void setProductCharacteristicValue5( String productCharacteristicValue5 )
  {
    this.productCharacteristicValue5 = productCharacteristicValue5;
  }

  /**
   * @return value of productName property
   */
  public String getProductName()
  {
    return productName;
  }

  /**
   * @param productName value for productName property
   */
  public void setProductName( String productName )
  {
    this.productName = productName;
  }

  /**
   * @return value of soldQuantity property
   */
  public Integer getSoldQuantity()
  {
    return soldQuantity;
  }

  /**
   * @param soldQuantity value for soldQuantity property
   */
  public void setSoldQuantity( Integer soldQuantity )
  {
    this.soldQuantity = soldQuantity;
  }

  /**
   * @return value of productCharacteristicId1 property
   */
  public Long getProductCharacteristicId1()
  {
    return productCharacteristicId1;
  }

  /**
   * @param productCharacteristicId1 value for productCharacteristicId1 property
   */
  public void setProductCharacteristicId1( Long productCharacteristicId1 )
  {
    this.productCharacteristicId1 = productCharacteristicId1;
  }

  /**
   * @return value of productCharacteristicId2 property
   */
  public Long getProductCharacteristicId2()
  {
    return productCharacteristicId2;
  }

  /**
   * @param productCharacteristicId2 value for productCharacteristicId2 property
   */
  public void setProductCharacteristicId2( Long productCharacteristicId2 )
  {
    this.productCharacteristicId2 = productCharacteristicId2;
  }

  /**
   * @return value of productCharacteristicId3 property
   */
  public Long getProductCharacteristicId3()
  {
    return productCharacteristicId3;
  }

  /**
   * @param productCharacteristicId3 value for productCharacteristicId3 property
   */
  public void setProductCharacteristicId3( Long productCharacteristicId3 )
  {
    this.productCharacteristicId3 = productCharacteristicId3;
  }

  /**
   * @return value of productCharacteristicId4 property
   */
  public Long getProductCharacteristicId4()
  {
    return productCharacteristicId4;
  }

  /**
   * @param productCharacteristicId4 value for productCharacteristicId4 property
   */
  public void setProductCharacteristicId4( Long productCharacteristicId4 )
  {
    this.productCharacteristicId4 = productCharacteristicId4;
  }

  /**
   * @return value of productCharacteristicId5 property
   */
  public Long getProductCharacteristicId5()
  {
    return productCharacteristicId5;
  }

  /**
   * @param productCharacteristicId5 value for productCharacteristicId5 property
   */
  public void setProductCharacteristicId5( Long productCharacteristicId5 )
  {
    this.productCharacteristicId5 = productCharacteristicId5;
  }

  /**
   * @return value of productId property
   */
  public Long getProductId()
  {
    return productId;
  }

  /**
   * @param productId value for productId property
   */
  public void setProductId( Long productId )
  {
    this.productId = productId;
  }

  /**
   * @return value of productClaimImportRecord property
   */
  public ProductClaimImportRecord getProductClaimImportRecord()
  {
    return productClaimImportRecord;
  }

  /**
   * @param productClaimImportRecord value for productClaimImportRecord property
   */
  public void setProductClaimImportRecord( ProductClaimImportRecord productClaimImportRecord )
  {
    this.productClaimImportRecord = productClaimImportRecord;
  }

}
