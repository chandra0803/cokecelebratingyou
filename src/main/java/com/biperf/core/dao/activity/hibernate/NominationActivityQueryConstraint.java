/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.activity.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.activity.NominationActivity;

/**
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
 * <td>Nov 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class NominationActivityQueryConstraint extends ActivityQueryConstraint
{
  public static final Boolean GIVERS_ONLY = Boolean.TRUE;
  public static final Boolean RECEIVERS_ONLY = Boolean.FALSE;
  public static final Boolean GIVERS_AND_RECEIVERS = null;

  private Long claimGroupId;
  /**
   * If true, only return activities for givers, if false, for receivers, if null, don't constrain
   * (i.e both).
   */
  private Boolean giverOrReceiver;

  private Long approvalRound;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return NominationActivity.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    // Constrain by claimGroup
    if ( claimGroupId != null )
    {
      criteria.add( Restrictions.eq( "activity.claimGroup.id", claimGroupId ) );
    }

    // Constrain by giver/receiver - submitter boolean true represents giver
    if ( giverOrReceiver != null )
    {
      criteria.add( Restrictions.eq( "activity.submitter", giverOrReceiver ) );
    }

    if ( approvalRound != null )
    {
      criteria.add( Restrictions.eq( "activity.approvalRound", approvalRound ) );
    }
    return criteria;
  }

  /**
   * @return value of giverOrReceiver property
   */
  public Boolean getGiverOrReceiver()
  {
    return giverOrReceiver;
  }

  /**
   * @param giverOrReceiver value for giverOrReceiver property
   */
  public void setGiverOrReceiver( Boolean giverOrReceiver )
  {
    this.giverOrReceiver = giverOrReceiver;
  }

  public Long getClaimGroupId()
  {
    return claimGroupId;
  }

  public void setClaimGroupId( Long claimGroupId )
  {
    this.claimGroupId = claimGroupId;
  }

  public Long getApprovalRound()
  {
    return approvalRound;
  }

  public void setApprovalRound( Long approvalRound )
  {
    this.approvalRound = approvalRound;
  }
}
