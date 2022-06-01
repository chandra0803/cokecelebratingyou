/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.fileload;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class ProductClaimImportRecord extends ImportRecord
{
  private Set productClaimImportFieldRecords;
  private Set productClaimImportProductRecords;

  private String submitterUserName;
  private Long submitterUserId;

  private String trackToNodeName;
  private Long trackToNodeId;

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
   * @return value of productClaimImportFieldRecords property
   */
  public Set getProductClaimImportFieldRecords()
  {
    return productClaimImportFieldRecords;
  }

  /**
   * @return value of productClaimImportFieldRecords property
   */
  public List getProductClaimImportFieldRecordsAsList()
  {
    return new ArrayList( productClaimImportFieldRecords );
  }

  /**
   * @param productClaimImportFieldRecords value for productClaimImportFieldRecords property
   */
  public void setProductClaimImportFieldRecords( Set productClaimImportFieldRecords )
  {
    this.productClaimImportFieldRecords = productClaimImportFieldRecords;
  }

  /**
   * @return value of productClaimImportProductRecords property
   */
  public Set getProductClaimImportProductRecords()
  {
    return productClaimImportProductRecords;
  }

  /**
   * @return value of productClaimImportProductRecords property
   */
  public List getProductClaimImportProductRecordsAsList()
  {
    return new ArrayList( productClaimImportProductRecords );
  }

  /**
   * @param productClaimImportProductRecords value for productClaimImportProductRecords property
   */
  public void setProductClaimImportProductRecords( Set productClaimImportProductRecords )
  {
    this.productClaimImportProductRecords = productClaimImportProductRecords;
  }

  /**
   * @return value of submitterUserName property
   */
  public String getSubmitterUserName()
  {
    return submitterUserName;
  }

  /**
   * @param submitterUserName value for submitterUserName property
   */
  public void setSubmitterUserName( String submitterUserName )
  {
    this.submitterUserName = submitterUserName;
  }

  /**
   * @return value of trackToNodeName property
   */
  public String getTrackToNodeName()
  {
    return trackToNodeName;
  }

  /**
   * @param trackToNodeName value for trackToNodeName property
   */
  public void setTrackToNodeName( String trackToNodeName )
  {
    this.trackToNodeName = trackToNodeName;
  }

  /**
   * @return value of submitterUserId property
   */
  public Long getSubmitterUserId()
  {
    return submitterUserId;
  }

  /**
   * @param submitterUserId value for submitterUserId property
   */
  public void setSubmitterUserId( Long submitterUserId )
  {
    this.submitterUserId = submitterUserId;
  }

  /**
   * @return value of trackToNodeId property
   */
  public Long getTrackToNodeId()
  {
    return trackToNodeId;
  }

  /**
   * @param trackToNodeId value for trackToNodeId property
   */
  public void setTrackToNodeId( Long trackToNodeId )
  {
    this.trackToNodeId = trackToNodeId;
  }
}
