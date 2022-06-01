/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.impl;

import java.util.List;

import com.biperf.core.dao.promotion.StackRankParticipantDAO;
import com.biperf.core.dao.promotion.hibernate.StackRankParticipantQueryConstraint;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.StackRankParticipantService;

/**
 * StackRankParticipantServiceImpl
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
 * <td>Tammy Cheng</td>
 * <td>Mar 16, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankParticipantServiceImpl implements StackRankParticipantService
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private StackRankParticipantDAO stackRankParticipantDAO;

  // ---------------------------------------------------------------------------
  // Service Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the specified stack rank participant.
   * 
   * @param stackRankParticipantId
   * @return the specified stack rank participant.
   */
  public StackRankParticipant getStackRankParticipant( Long stackRankParticipantId )
  {
    return stackRankParticipantDAO.getStackRankParticipant( stackRankParticipantId );
  }

  /**
   * Returns a list of stack rank participant objects.
   * 
   * @param queryConstraint
   * @return a list of stack rank participant objects
   */
  public List getStackRankParticipantList( StackRankParticipantQueryConstraint queryConstraint )
  {
    return stackRankParticipantDAO.getStackRankParticipantList( queryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.StackRankParticipantService#getStackRankParticipantList(com.biperf.core.dao.promotion.hibernate.StackRankParticipantQueryConstraint,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param queryConstraint
   * @param associationRequest
   * @return a list of stack rank participant objects
   */
  public List getStackRankParticipantList( StackRankParticipantQueryConstraint queryConstraint, AssociationRequestCollection associationRequest )
  {
    return stackRankParticipantDAO.getStackRankParticipantList( queryConstraint, associationRequest );
  }

  /**
   * Saves the given stack rank participant.
   * 
   * @param stackRankParticipant the stack rank participant to save.
   * @return the saved version of the stack rank participant.
   */
  public StackRankParticipant saveStackRankParticipant( StackRankParticipant stackRankParticipant )
  {
    return stackRankParticipantDAO.saveStackRankParticipant( stackRankParticipant );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------
  /**
   * Returns the stack rank participant dao.
   * 
   * @return the stack rank participant dao.
   */
  public StackRankParticipantDAO getStackRankParticipantDAO()
  {
    return stackRankParticipantDAO;
  }

  /**
   * Set the stack rank participant dao.
   * 
   * @param stackRankParticipantDAO
   */
  public void setStackRankParticipantDAO( StackRankParticipantDAO stackRankParticipantDAO )
  {
    this.stackRankParticipantDAO = stackRankParticipantDAO;
  }

}
