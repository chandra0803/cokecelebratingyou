/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.participant.Participant;

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
 * <td>zahler</td>
 * <td>Oct 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizFacts implements PayoutCalculationFacts
{
  private Claim claim;
  private Participant participant;

  public QuizFacts( Claim claim, Participant participant )
  {
    this.claim = claim;
    this.participant = participant;
  }

  public Claim getClaim()
  {
    return claim;
  }

  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

}
