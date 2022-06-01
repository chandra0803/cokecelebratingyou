/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.claim.ClaimGroupDAO;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

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
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ClaimGroupDAOImpl extends BaseDAO implements ClaimGroupDAO
{
  /**
   * Overridden from @see com.biperf.core.dao.claim.ClaimGroupDAO#saveClaimGroup(com.biperf.core.domain.claim.ClaimGroup)
   * @param claimGroup
   */
  public ClaimGroup saveClaimGroup( ClaimGroup claimGroup )
  {
    ClaimGroup savedClaimGroup = (ClaimGroup)HibernateUtil.saveOrUpdateOrDeepMerge( claimGroup );
    // run lock to insure that sub-objects are reattached to this session, since some of
    // the subobjects may have come from another session.
    getSession().flush();
    getSession().refresh( savedClaimGroup );

    return savedClaimGroup;
  }

  /**
   * Overridden from @see com.biperf.core.dao.claim.ClaimGroupDAO#getClaimGroupById(java.lang.Long)
   * @param claimGroupId
   */
  public ClaimGroup getClaimGroupById( Long claimGroupId )
  {
    return (ClaimGroup)getSession().get( ClaimGroup.class, claimGroupId );
  }

  /**
   * Overridden from @see com.biperf.core.dao.claim.ClaimGroupDAO#getClaimGroupByIdWithAssociations(java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param claimGroupId
   * @param associationRequestCollection
   */
  public ClaimGroup getClaimGroupByIdWithAssociations( Long claimGroupId, AssociationRequestCollection associationRequestCollection )
  {
    ClaimGroup claimGroup = (ClaimGroup)getSession().get( ClaimGroup.class, claimGroupId );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( claimGroup );
    }

    return claimGroup;
  }

  /**
   * Returns the claim groups specified by the given query constraint.
   *
   * @param queryConstraint  the query constraint.
   * @param associationRequestCollection  initializes properties of the returned claim groups.
   * @return the specified claim groups, as a <code>List</code> of {@link ClaimGroup}
   */
  public List getClaimGroupList( JournalClaimGroupQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    List claimGroupList = HibernateUtil.getObjectList( queryConstraint );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( claimGroupList );
    }

    return claimGroupList;
  }

  /**
   * Overridden from @see com.biperf.core.dao.claim.ClaimGroupDAO#getClaimGroupList(com.biperf.core.dao.claim.hibernate.ClaimGroupQueryConstraint)
   * @param claimGroupQueryConstraint
   */
  public List getClaimGroupList( ClaimGroupQueryConstraint claimGroupQueryConstraint )
  {
    return HibernateUtil.getObjectList( claimGroupQueryConstraint );

  }

  /**
   * Overridden from @see com.biperf.core.dao.claim.ClaimGroupDAO#getClaimGroupListCount(com.biperf.core.dao.claim.hibernate.ClaimGroupQueryConstraint)
   * @param claimGroupQueryConstraint
   */
  public int getClaimGroupListCount( ClaimGroupQueryConstraint claimGroupQueryConstraint )
  {
    return HibernateUtil.getObjectListCount( claimGroupQueryConstraint );
  }

  @Override
  public Journal getJournalForClaimGroup( Long claimGroupId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.JournalForClaimGroup" );

    query.setLong( "claimGroupId", claimGroupId );
    return (Journal)query.uniqueResult();
  }

}
