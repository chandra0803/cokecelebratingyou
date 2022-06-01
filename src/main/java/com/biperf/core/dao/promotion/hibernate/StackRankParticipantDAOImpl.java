/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.StackRankParticipantDAO;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class StackRankParticipantDAOImpl extends BaseDAO implements StackRankParticipantDAO
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Persistence Methods
  // ---------------------------------------------------------------------------
  /**
   * Returns the specified stack rank participant.
   * 
   * @param id the ID of the stack rank participant to get.
   * @return the specified stack rank participant.
   */
  public StackRankParticipant getStackRankParticipant( Long id )
  {
    return (StackRankParticipant)getSession().get( StackRankParticipant.class, id );
  }

  /**
   * Returns a List of stack rank participant objects based on a queryConstraint
   * 
   * @param queryConstraint the queryConstraint object.
   * @return the list of specified stack rank participants.
   */
  public List getStackRankParticipantList( StackRankParticipantQueryConstraint queryConstraint )
  {
    return HibernateUtil.getObjectList( queryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.StackRankParticipantDAO#getStackRankParticipantList(com.biperf.core.dao.promotion.hibernate.StackRankParticipantQueryConstraint,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param queryConstraint
   * @param associationRequest
   * @return the list of specified stack rank participants.
   */
  public List getStackRankParticipantList( StackRankParticipantQueryConstraint queryConstraint, AssociationRequestCollection associationRequest )
  {
    List stackRankParticipantList = HibernateUtil.getObjectList( queryConstraint );

    if ( associationRequest != null )
    {
      Iterator stackRankParticipantIter = stackRankParticipantList.iterator();
      while ( stackRankParticipantIter.hasNext() )
      {
        associationRequest.process( (StackRankParticipant)stackRankParticipantIter.next() );
      }
    }

    return stackRankParticipantList;
  }

  /**
   * Saves the given stack rank participant.
   * 
   * @param stackRankParticipant the stack rank participant to save.
   * @return the saved version of the stack rank participant.
   */
  public StackRankParticipant saveStackRankParticipant( StackRankParticipant stackRankParticipant )
  {
    return (StackRankParticipant)HibernateUtil.saveOrUpdateOrShallowMerge( stackRankParticipant );
  }
}
