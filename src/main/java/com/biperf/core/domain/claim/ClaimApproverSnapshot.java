/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ApprovableTypeEnum;

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
 * <td>Feb 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimApproverSnapshot extends BaseDomain
{

  private Long approvableId;
  private Long approverUserId;
  private Long sourceNodeId;
  private ApprovableTypeEnum approvableType;

  /**
   * @param approvableId
   * @param approverUserId
   * @param sourceNodeId
   * @param approvableType
   */
  public ClaimApproverSnapshot( Long approvableId, Long approverUserId, Long sourceNodeId, ApprovableTypeEnum approvableType )
  {
    super();
    this.approvableId = approvableId;
    this.approverUserId = approverUserId;
    this.sourceNodeId = sourceNodeId;
    this.approvableType = approvableType;
  }

  public ClaimApproverSnapshot()
  {
    super();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   */
  public boolean equals( Object object )
  {
    // Base on ids since we always have ids when using this object.
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ClaimApproverSnapshot ) )
    {
      return false;
    }

    ClaimApproverSnapshot rhs = (ClaimApproverSnapshot)object;

    if ( this.getApproverUserId() != null ? !this.getApproverUserId().equals( rhs.getApproverUserId() ) : rhs.getApproverUserId() != null )
    {
      return false;
    }

    if ( this.getApprovableId() != null ? !this.getApprovableId().equals( rhs.getApprovableId() ) : rhs.getApprovableId() != null )
    {
      return false;
    }

    if ( this.getApprovableType() != null ? !this.getApprovableType().equals( rhs.getApprovableType() ) : rhs.getApprovableType() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   */
  public int hashCode()
  {
    // Base on ids since we always have ids when using this object.
    int result = 0;

    result += this.getApproverUserId() != null ? this.getApproverUserId().hashCode() : 0;

    result += this.getApprovableId() != null ? this.getApprovableId().hashCode() : 0;

    result += this.getApprovableType() != null ? this.getApprovableType().hashCode() : 0;

    return result;
  }

  /**
   * @return value of approverUserId property
   */
  public Long getApproverUserId()
  {
    return approverUserId;
  }

  /**
   * @param approverUserId value for approverUserId property
   */
  public void setApproverUserId( Long approverUserId )
  {
    this.approverUserId = approverUserId;
  }

  /**
   * @return value of approvableId property
   */
  public Long getApprovableId()
  {
    return approvableId;
  }

  /**
   * @param approvableId value for approvableId property
   */
  public void setApprovableId( Long approvableId )
  {
    this.approvableId = approvableId;
  }

  /**
   * @return value of sourceNodeId property
   */
  public Long getSourceNodeId()
  {
    return sourceNodeId;
  }

  /**
   * @param sourceNodeId value for sourceNodeId property
   */
  public void setSourceNodeId( Long sourceNodeId )
  {
    this.sourceNodeId = sourceNodeId;
  }

  /**
   * @return value of approvableType property
   */
  public ApprovableTypeEnum getApprovableType()
  {
    return approvableType;
  }

  /**
   * @param approvableType value for approvableType property
   */
  public void setApprovableType( ApprovableTypeEnum approvableType )
  {
    this.approvableType = approvableType;
  }

  /**
   * Only used by hibernate
   */
  public Long getClaimGroupId()
  {
    if ( approvableType.isClaimGroup() )
    {
      return approvableId;
    }
    return null;
  }

  /**
   * Only used by hibernate
   */
  public void setClaimGroupId( Long claimGroupId )
  {
    if ( claimGroupId != null )
    {
      approvableId = claimGroupId;
      setApprovableType( ApprovableTypeEnum.CLAIM_GROUP );
    }
  }

  /**
   * Only used by hibernate
   */
  public Long getClaimId()
  {
    if ( approvableType.isClaim() )
    {
      return approvableId;
    }
    return null;
  }

  /**
   * Only used by hibernate
   */
  public void setClaimId( Long claimId )
  {
    if ( claimId != null )
    {
      approvableId = claimId;
      setApprovableType( ApprovableTypeEnum.CLAIM );
    }
  }

}
