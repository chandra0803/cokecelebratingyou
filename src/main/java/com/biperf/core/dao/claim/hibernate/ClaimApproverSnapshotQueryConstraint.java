/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.claim.ClaimApproverSnapshot;
import com.biperf.core.domain.enums.ApprovableTypeEnum;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;

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
 * <td>Feb 13, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ClaimApproverSnapshotQueryConstraint extends BaseQueryConstraint
{

  private Long approverUserId;

  /**
   * Only only those snapshots with the given approvable Id. This id is can be claim id, 
   * cum no claim group id, etc. It's type must be sepcified with includedApprovableType.
   */
  private List<Long> approvableId;

  /**
   * Include only those snapshot with the included ApprovableType.
   */
  private ApprovableTypeEnum approvableType;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.QueryConstraint#buildCriteria()
   */
  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "snapshot" );

    // Constrain by approvableId
    if ( approvableId != null )
    {
      if ( approvableType == null )
      {
        throw new BeaconRuntimeException( "approvableType must be non-null id approvableId is set" );
      }
      // database/hibernate uses claim id or claimGroup id not approvableId

      if ( approvableType.isClaim() )
      {
        criteria.add( Restrictions.in( "snapshot.claimId", approvableId ) );
      }
      else if ( approvableType.isClaimGroup() )
      {
        criteria.add( Restrictions.in( "snapshot.claimGroupId", approvableId ) );
      }
    }

    // Constrain by approverUserId
    if ( approverUserId != null && !UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
    {
      criteria.add( Restrictions.eq( "snapshot.approverUserId", approverUserId ) );
    }

    return criteria;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.QueryConstraint#getResultClass()
   */
  public Class getResultClass()
  {
    return ClaimApproverSnapshot.class;
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
   * @return value of claimId property
   */
  public List<Long> getApprovableId()
  {
    return approvableId;
  }

  /**
   * @param approvableId value for claimId property
   */
  public void setApprovableId( List<Long> approvableId )
  {
    this.approvableId = approvableId;
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

}
