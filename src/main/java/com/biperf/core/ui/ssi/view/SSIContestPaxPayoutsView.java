
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.value.ssi.SSIContestParticipantPayoutsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class SSIContestPaxPayoutsView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private int currentPage;
  private String sortedBy;
  private String sortedOn;
  private int recordsPerPage;
  private int recordsTotal;
  private List<SSIContestParticipantPayoutsValueBean> participants = new ArrayList<SSIContestParticipantPayoutsValueBean>();

  public SSIContestPaxPayoutsView()
  {
  }

  public SSIContestPaxPayoutsView( WebErrorMessage message )
  {
    this.messages.add( message );
  }

  public SSIContestPaxPayoutsView( SSIContestPayoutsValueBean ssiContestPayoutsValueBean, int currentPage, String sortedBy, String sortedOn, int recordsPerPage )
  {
    super();
    this.currentPage = currentPage;
    this.sortedBy = sortedBy;
    this.sortedOn = sortedOn;
    this.recordsPerPage = recordsPerPage;
    this.recordsTotal = ssiContestPayoutsValueBean.getTotalParticipantCount();
    this.participants = ssiContestPayoutsValueBean.getPartiticpantPayoutsList();
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public int getRecordsPerPage()
  {
    return recordsPerPage;
  }

  public void setRecordsPerPage( int recordsPerPage )
  {
    this.recordsPerPage = recordsPerPage;
  }

  public int getRecordsTotal()
  {
    return recordsTotal;
  }

  public void setRecordsTotal( int recordsTotal )
  {
    this.recordsTotal = recordsTotal;
  }

  public List<SSIContestParticipantPayoutsValueBean> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<SSIContestParticipantPayoutsValueBean> participants )
  {
    this.participants = participants;
  }

  /*
   * private void populatePaxPayouts(SSIContestSummaryValueBean paxPayoutsValueBean){ boolean
   * isPayoutInPoints = SSIPayoutType.POINTS_CODE.equalsIgnoreCase(
   * paxPayoutsValueBean.getPayoutType() ); String contestType =
   * paxPayoutsValueBean.getContestType(); for(SSIContestSummaryTDPaxResultBean paxResult :
   * paxPayoutsValueBean.getPaxResults()) { SSIContestParticipantPayoutsValueBean paxPayout = new
   * SSIContestParticipantPayoutsValueBean(); //paxPayout.setId( paxResult.getUserId().toString());
   * paxPayout.setLastName( paxResult.getLastName() ); if(SSIContestType.OBJECTIVES.equals(
   * contestType)) { populatePaxObjectivesPayouts( isPayoutInPoints, paxResult, paxPayout ); } else
   * if(SSIContestType.STEP_IT_UP.equals( contestType)) { populatePaxStepItUpPayouts(
   * isPayoutInPoints, paxResult, paxPayout ); } else if(SSIContestType.DO_THIS_GET_THAT.equals(
   * contestType)) { populatePaxDTGTPayouts( isPayoutInPoints, paxResult, paxPayout ); } else
   * if(SSIContestType.STACK_RANK.equals( contestType)) { populatePaxStackRankPayouts(
   * isPayoutInPoints, paxResult, paxPayout ); } participants.add( paxPayout ); } } protected void
   * populatePaxObjectivesPayouts( boolean isPayoutInPoints, SSIContestSummaryTDPaxResultBean
   * paxResult, SSIContestParticipantPayoutsValueBean paxPayout ) { paxPayout.setGoal(
   * paxResult.getObjective() ); paxPayout.setProgress( paxResult.getPercentToObjective() );
   * if(isPayoutInPoints) { paxPayout.setObjectivePayout( paxResult.getObjectivePayout() );
   * paxPayout.setBonusPayout( paxResult.getBonusAmount() ); paxPayout.setPayout(
   * paxResult.getPayoutAmount() ); } else { paxPayout.setPayoutDescription(
   * "payout description here" ); // TODO; paxPayout.setQty( paxResult.getObjectivePayout() );
   * paxPayout.setPayoutValue( paxResult.getPayoutAmount() ); } } protected void
   * populatePaxStepItUpPayouts( boolean isPayoutInPoints, SSIContestSummaryTDPaxResultBean
   * paxResult, SSIContestParticipantPayoutsValueBean paxPayout ) { paxPayout.setActivityAmount(
   * paxResult.getCurrentActivity() ); paxPayout.setLevelAchieved(
   * paxResult.getLevelCompleted().toString() ); if(isPayoutInPoints) {
   * //paxPayout.setObjectivePayout( paxResult.getLevelPayout().toString() ); //TODO; Check this
   * paxPayout.setLevelPayout( paxResult.getLevelPayout().toString()); paxPayout.setTotalPayout(
   * paxResult.getPayoutAmount()); } else { paxPayout.setPayoutDescription(
   * "payout description here" ); // TODO; paxPayout.setQty( paxResult.getLevelPayout().toString()
   * ); paxPayout.setPayoutValue( paxResult.getPayoutAmount() ); } } protected void
   * populatePaxStackRankPayouts( boolean isPayoutInPoints, SSIContestSummaryTDPaxResultBean
   * paxResult, SSIContestParticipantPayoutsValueBean paxPayout ) { } protected void
   * populatePaxDTGTPayouts( boolean isPayoutInPoints, SSIContestSummaryTDPaxResultBean paxResult,
   * SSIContestParticipantPayoutsValueBean paxPayout ) { //paxPayout.setProgress()
   * //paxPayout.setQualifiedActivity( qualifiedActivity );( paxResult.getCurrentActivity() );
   * //paxPayout.setPayoutIncrements( payoutIncrements );( paxResult.getLevelCompleted().toString()
   * ); if(isPayoutInPoints) { paxPayout.setTotalPayout( paxResult.getPayoutValue1().toString()); //
   * TODO; which activity ?? } else { paxPayout.setPayoutDescription( "payout description here" );
   * // TODO; paxPayout.setPayoutValue( paxResult.getPayoutQuantity1().toString()); } }
   */

}
