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
public class ProductClaimImportFieldRecord extends BaseDomain
{

  private Long claimFormStepElementId;
  private String claimFormStepElementName;
  private String claimFormStepElementValue;

  private ProductClaimImportRecord productClaimImportRecord;

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
   * @return value of claimFormStepElementId property
   */
  public Long getClaimFormStepElementId()
  {
    return claimFormStepElementId;
  }

  /**
   * @param claimFormStepElementId value for claimFormStepElementId property
   */
  public void setClaimFormStepElementId( Long claimFormStepElementId )
  {
    this.claimFormStepElementId = claimFormStepElementId;
  }

  /**
   * @return value of claimFormStepElementName property
   */
  public String getClaimFormStepElementName()
  {
    return claimFormStepElementName;
  }

  /**
   * @param claimFormStepElementName value for claimFormStepElementName property
   */
  public void setClaimFormStepElementName( String claimFormStepElementName )
  {
    this.claimFormStepElementName = claimFormStepElementName;
  }

  /**
   * @return value of claimFormStepElementValue property
   */
  public String getClaimFormStepElementValue()
  {
    return claimFormStepElementValue;
  }

  /**
   * @param claimFormStepElementValue value for claimFormStepElementValue property
   */
  public void setClaimFormStepElementValue( String claimFormStepElementValue )
  {
    this.claimFormStepElementValue = claimFormStepElementValue;
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
