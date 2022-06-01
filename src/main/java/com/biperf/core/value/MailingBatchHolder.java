/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/value/MailingBatchHolder.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;

import com.biperf.core.domain.mailing.MailingBatch;

/**
 * MailingBatchByAud
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
 * <td></td>
 * <td>June 8th 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@SuppressWarnings( "serial" )
public class MailingBatchHolder implements Serializable
{
  private MailingBatch paxAchievedMailingBatch;
  private MailingBatch paxNotAchievedMailingBatch;
  private MailingBatch goalNotSelectedBatch;
  private MailingBatch paxProgressMailingBatch;
  private MailingBatch challengePointInterimPayoutBatch;

  /* partner batches */
  private MailingBatch partnerProgressMailingBatch;
  private MailingBatch partnerAchievedMailingBatch;
  private MailingBatch partnerNotAchievedMailingBatch;
  private MailingBatch partnerAchievedNoPayoutMailingBatch;

  public MailingBatch getPaxAchievedMailingBatch()
  {
    return paxAchievedMailingBatch;
  }

  public void setPaxAchievedMailingBatch( MailingBatch paxAchievedMailingBatch )
  {
    this.paxAchievedMailingBatch = paxAchievedMailingBatch;
  }

  public MailingBatch getPaxNotAchievedMailingBatch()
  {
    return paxNotAchievedMailingBatch;
  }

  public void setPaxNotAchievedMailingBatch( MailingBatch paxNotAchievedMailingBatch )
  {
    this.paxNotAchievedMailingBatch = paxNotAchievedMailingBatch;
  }

  public MailingBatch getPaxProgressMailingBatch()
  {
    return paxProgressMailingBatch;
  }

  public void setPaxProgressMailingBatch( MailingBatch paxProgressMailingBatch )
  {
    this.paxProgressMailingBatch = paxProgressMailingBatch;
  }

  public MailingBatch getPartnerAchievedMailingBatch()
  {
    return partnerAchievedMailingBatch;
  }

  public void setPartnerAchievedMailingBatch( MailingBatch partnerAchievedMailingBatch )
  {
    this.partnerAchievedMailingBatch = partnerAchievedMailingBatch;
  }

  public MailingBatch getGoalNotSelectedBatch()
  {
    return goalNotSelectedBatch;
  }

  public void setGoalNotSelectedBatch( MailingBatch goalNotSelectedBatch )
  {
    this.goalNotSelectedBatch = goalNotSelectedBatch;
  }

  public MailingBatch getPartnerNotAchievedMailingBatch()
  {
    return partnerNotAchievedMailingBatch;
  }

  public void setPartnerNotAchievedMailingBatch( MailingBatch partnerNotAchievedMailingBatch )
  {
    this.partnerNotAchievedMailingBatch = partnerNotAchievedMailingBatch;
  }

  public MailingBatch getPartnerAchievedNoPayoutMailingBatch()
  {
    return partnerAchievedNoPayoutMailingBatch;
  }

  public void setPartnerAchievedNoPayoutMailingBatch( MailingBatch partnerAchievedNoPayoutMailingBatch )
  {
    this.partnerAchievedNoPayoutMailingBatch = partnerAchievedNoPayoutMailingBatch;
  }

  public MailingBatch getPartnerProgressMailingBatch()
  {
    return partnerProgressMailingBatch;
  }

  public void setPartnerProgressMailingBatch( MailingBatch partnerProgressMailingBatch )
  {
    this.partnerProgressMailingBatch = partnerProgressMailingBatch;
  }

  public MailingBatch getChallengePointInterimPayoutBatch()
  {
    return challengePointInterimPayoutBatch;
  }

  public void setChallengePointInterimPayoutBatch( MailingBatch cpInterimPayoutBatch )
  {
    this.challengePointInterimPayoutBatch = cpInterimPayoutBatch;
  }
}
