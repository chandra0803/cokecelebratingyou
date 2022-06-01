/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.participant.Participant;

/**
 * RecognitionFacts.
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
 * <td>zahler</td>
 * <td>Oct 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NominationFacts implements PayoutCalculationFacts
{
  private Approvable approvable;
  private Participant participant;

  public NominationFacts( Approvable approvable, Participant participant )
  {
    this.approvable = approvable;
    this.participant = participant;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Approvable getApprovable()
  {
    return approvable;
  }

  public void setApprovable( Approvable approvable )
  {
    this.approvable = approvable;
  }

}
