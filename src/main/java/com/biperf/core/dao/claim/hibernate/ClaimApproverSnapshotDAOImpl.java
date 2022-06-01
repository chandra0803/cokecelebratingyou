/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.claim.ClaimApproverSnapshotDAO;
import com.biperf.core.domain.claim.ClaimApproverSnapshot;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.promotion.CustomApproverListValueBean;

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
 * <td>Feb 10, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ClaimApproverSnapshotDAOImpl implements ClaimApproverSnapshotDAO
{

  private DataSource dataSource;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimApproverSnapshotDAO#saveClaimApproverSnapshot(com.biperf.core.domain.claim.ClaimApproverSnapshot)
   * @param claimApproverSnapshot
   */
  public ClaimApproverSnapshot saveClaimApproverSnapshot( ClaimApproverSnapshot claimApproverSnapshot )
  {
    return (ClaimApproverSnapshot)HibernateUtil.saveOrUpdateOrShallowMerge( claimApproverSnapshot );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimApproverSnapshotDAO#deleteClaimApproverSnapshot(com.biperf.core.domain.claim.ClaimApproverSnapshot)
   * @param claimApproverSnapshot
   */
  public void deleteClaimApproverSnapshot( ClaimApproverSnapshot claimApproverSnapshot )
  {
    HibernateSessionManager.getSession().delete( claimApproverSnapshot );
  }

  public List getClaimApproverSnapshotList( ClaimApproverSnapshotQueryConstraint claimApproverSnapshotQueryConstraint )
  {
    return HibernateUtil.getObjectList( claimApproverSnapshotQueryConstraint );
  }

  public int getClaimApproverSnapshotListCount( ClaimApproverSnapshotQueryConstraint claimApproverSnapshotQueryConstraint )
  {
    return HibernateUtil.getObjectListCount( claimApproverSnapshotQueryConstraint );
  }

  public List<CustomApproverListValueBean> getCustomApproverList( Long promotionId, Long levelNumber, String userIds, String behaviors, String awardAmount, Long nominatorId, Long isTeam )
  {
    CallPrcNominationCustomApproverList procedure = new CallPrcNominationCustomApproverList( dataSource );
    Map<String, Object> output = procedure.executeProcedure( promotionId, levelNumber, userIds, behaviors, awardAmount, nominatorId, isTeam );
    List<CustomApproverListValueBean> beanList = (ArrayList<CustomApproverListValueBean>)output.get( "p_out_approver_list" );

    return beanList;
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }
}
