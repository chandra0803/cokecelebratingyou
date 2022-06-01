/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion;

import java.util.List;

import com.biperf.core.dao.promotion.hibernate.StackRankParticipantQueryConstraint;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * StackRankParticipantService
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
public interface StackRankParticipantService extends SAO
{
  /**
   * The key to an object that implements this interface in the Spring application context.
   */
  public static final String BEAN_NAME = "stackRankParticipantService";

  /**
   * Returns the specified stack rank participant.
   * 
   * @param stackRankParticipantId the ID of the stack rank participant to get.
   * @return the specified stack rank participant.
   */
  StackRankParticipant getStackRankParticipant( Long stackRankParticipantId );

  /**
   * Returns a List of stack rank participant objects based on a queryConstraint
   * 
   * @param queryConstraint the queryConstraint object
   * @return a list of stack rank participant objects
   */
  List getStackRankParticipantList( StackRankParticipantQueryConstraint queryConstraint );

  /**
   * Returns a List of stack rank participant objects based on a queryConstraint
   * 
   * @param queryConstraint the queryConstraint object
   * @param associationRequest
   * @return a list of stack rank participant objects
   */
  List getStackRankParticipantList( StackRankParticipantQueryConstraint queryConstraint, AssociationRequestCollection associationRequest );

  /**
   * Saves the given stack rank participant.
   * 
   * @param stackRankParticipant the stack rank participant to save.
   * @return the saved version of the stack rank participant.
   */
  StackRankParticipant saveStackRankParticipant( StackRankParticipant stackRankParticipant );

}
