/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/goalquest/PendingGoalQuestAwardSummary.java,v $
 */

package com.biperf.core.domain.goalquest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.Promotion;

/*
 * PendingGoalQuestAwardSummary <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Satish</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Satish</td> <td>Jan 4, 2007</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class PendingGoalQuestAwardSummary implements Serializable
{
  private Promotion promotion;

  private List participantGoalQuestAwardSummaryList;
  private List participantGQResults;

  private List managerGoalQuestAwardSummaryList;
  private List managerOverrideResults;

  private List partnerGoalQuestAwardSummaryList;
  private List partnerGQResults;

  public List getManagerGoalQuestAwardSummaryList()
  {
    return managerGoalQuestAwardSummaryList;
  }

  public void setManagerGoalQuestAwardSummaryList( List managerGoalQuestAwardSummaryList )
  {
    this.managerGoalQuestAwardSummaryList = managerGoalQuestAwardSummaryList;
  }

  public List getParticipantGoalQuestAwardSummaryList()
  {
    return participantGoalQuestAwardSummaryList;
  }

  public void setParticipantGoalQuestAwardSummaryList( List participantGoalQuestAwardSummaryList )
  {
    this.participantGoalQuestAwardSummaryList = participantGoalQuestAwardSummaryList;
  }

  public List getParticipantGQResults()
  {
    return participantGQResults;
  }

  public void setParticipantGQResults( List participantGQResults )
  {
    this.participantGQResults = participantGQResults;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public List getManagerOverrideResults()
  {
    return managerOverrideResults;
  }

  public void setManagerOverrideResults( List managerOverrideResults )
  {
    this.managerOverrideResults = managerOverrideResults;
  }

  public List getPartnerGoalQuestAwardSummaryList()
  {
    return partnerGoalQuestAwardSummaryList;
  }

  public void setPartnerGoalQuestAwardSummaryList( List partnerGoalQuestAwardSummaryList )
  {
    this.partnerGoalQuestAwardSummaryList = partnerGoalQuestAwardSummaryList;
  }

  public List getPartnerGQResults()
  {
    return partnerGQResults;
  }

  public void setPartnerGQResults( List partnerGQResults )
  {
    this.partnerGQResults = partnerGQResults;
  }

  public List getGoalCalculationResultList()
  {
    List goalCalculationList = this.getParticipantGQResults() == null ? new ArrayList() : this.getParticipantGQResults();

    if ( this.getManagerOverrideResults() != null )
    {
      goalCalculationList.addAll( this.getManagerOverrideResults() );
    }

    if ( this.getPartnerGQResults() != null )
    {
      goalCalculationList.addAll( this.getPartnerGQResults() );
    }

    return goalCalculationList;
  }
}
