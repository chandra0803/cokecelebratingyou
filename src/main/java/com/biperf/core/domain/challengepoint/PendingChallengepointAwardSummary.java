/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/challengepoint/PendingChallengepointAwardSummary.java,v $
 */

package com.biperf.core.domain.challengepoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;

/*
 * PendingChallengepointAwardSummary <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Satish</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Babu</td> <td>Jul 31, 2008</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class PendingChallengepointAwardSummary implements Serializable
{
  private Promotion promotion;

  private List<ChallengepointAwardSummary> participantChallegenpointAwardSummaryList;
  private List<ChallengepointPaxAwardValueBean> participantChallengepointResults;

  // private ChallengepointAwardSummary managerChallengepointAwardSummary;
  private List<ChallengepointPaxAwardValueBean> managerOverrideResults;

  private ChallengepointAwardSummary challengepointAwardSummaryTotal;

  private List<ChallengepointAwardSummary> managerChallengepointAwardSummary;

  private List<ChallengepointAwardSummary> partnerCPAwardSummaryList;
  private List<ChallengepointPaxAwardValueBean> partnerCPResults;

  /*
   * public ChallengepointAwardSummary getManagerChallengepointAwardSummary() { return
   * managerChallengepointAwardSummary; } public void setManagerChallengepointAwardSummary(
   * ChallengepointAwardSummary managerChallengepointAwardSummary ) {
   * this.managerChallengepointAwardSummary = managerChallengepointAwardSummary; }
   */
  public List<ChallengepointAwardSummary> getParticipantChallegenpointAwardSummaryList()
  {
    return participantChallegenpointAwardSummaryList;
  }

  public void setParticipantChallegenpointAwardSummaryList( List<ChallengepointAwardSummary> participantChallegenpointAwardSummaryList )
  {
    this.participantChallegenpointAwardSummaryList = participantChallegenpointAwardSummaryList;
  }

  public List<ChallengepointPaxAwardValueBean> getParticipantChallengepointResults()
  {
    return participantChallengepointResults;
  }

  public void setParticipantChallengepointResults( List<ChallengepointPaxAwardValueBean> participantChallengepointResults )
  {
    this.participantChallengepointResults = participantChallengepointResults;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public List<ChallengepointPaxAwardValueBean> getManagerOverrideResults()
  {
    return managerOverrideResults;
  }

  public void setManagerOverrideResults( List<ChallengepointPaxAwardValueBean> managerOverrideResults )
  {
    this.managerOverrideResults = managerOverrideResults;
  }

  public List<ChallengepointPaxAwardValueBean> getChallengepointCalculationResultList()
  {
    List<ChallengepointPaxAwardValueBean> cpCalculationList = this.getParticipantChallengepointResults() == null
        ? new ArrayList<ChallengepointPaxAwardValueBean>()
        : this.getParticipantChallengepointResults();

    if ( this.getManagerOverrideResults() != null )
    {
      cpCalculationList.addAll( this.getManagerOverrideResults() );
    }

    if ( this.getPartnerCPResults() != null )
    {
      cpCalculationList.addAll( this.getPartnerCPResults() );
    }

    return cpCalculationList;
  }

  public ChallengepointAwardSummary getChallengepointAwardSummaryTotal()
  {
    return challengepointAwardSummaryTotal;
  }

  public void setChallengepointAwardSummaryTotal( ChallengepointAwardSummary challengepointAwardSummary )
  {
    this.challengepointAwardSummaryTotal = challengepointAwardSummary;
  }

  public List<ChallengepointAwardSummary> getManagerChallengepointAwardSummary()
  {
    return managerChallengepointAwardSummary;
  }

  public void setManagerChallengepointAwardSummary( List<ChallengepointAwardSummary> managerChallengepointAwardSummary )
  {
    this.managerChallengepointAwardSummary = managerChallengepointAwardSummary;
  }

  public List<ChallengepointAwardSummary> getPartnerCPAwardSummaryList()
  {
    return partnerCPAwardSummaryList;
  }

  public void setPartnerCPAwardSummaryList( List<ChallengepointAwardSummary> partnerCPAwardSummaryList )
  {
    this.partnerCPAwardSummaryList = partnerCPAwardSummaryList;
  }

  public List<ChallengepointPaxAwardValueBean> getPartnerCPResults()
  {
    return partnerCPResults;
  }

  public void setPartnerCPResults( List<ChallengepointPaxAwardValueBean> partnerCPResults )
  {
    this.partnerCPResults = partnerCPResults;
  }

}
