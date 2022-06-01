/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.claim.hibernate.ClaimApproverSnapshotQueryConstraint;
import com.biperf.core.domain.claim.ClaimApproverSnapshot;
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ClaimApproverSnapshotDAO extends DAO
{

  /**
   * Bean name
   */
  public static final String BEAN_NAME = "claimApproverSnapshotDAO";

  public ClaimApproverSnapshot saveClaimApproverSnapshot( ClaimApproverSnapshot claimApproverSnapshot );

  public void deleteClaimApproverSnapshot( ClaimApproverSnapshot claimApproverSnapshot );

  public List getClaimApproverSnapshotList( ClaimApproverSnapshotQueryConstraint claimApproverSnapshotQueryConstraint );

  public int getClaimApproverSnapshotListCount( ClaimApproverSnapshotQueryConstraint claimApproverSnapshotQueryConstraint );

  public List<CustomApproverListValueBean> getCustomApproverList( Long promotionId, Long levelNumber, String userIds, String behaviors, String awardAmount, Long nominatorId, Long isTeam );

}
