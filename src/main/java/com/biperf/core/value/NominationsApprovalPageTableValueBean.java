/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author poddutur
 * @since May 17, 2016
 */
public class NominationsApprovalPageTableValueBean
{
  int totalNumberOfApprovals;
  private List<NominationsApprovalBoxValueBean> approvalBoxList = new ArrayList<NominationsApprovalBoxValueBean>();
  private List<NominationsApprovalPagePreviousLevelApproversValueBean> previousLevelApproversList = new ArrayList<NominationsApprovalPagePreviousLevelApproversValueBean>();
  private List<NominationsApprovalPageNextLevelApproversValueBean> nextLevelApproversList = new ArrayList<NominationsApprovalPageNextLevelApproversValueBean>();
  private List<NominationsApprovalDetailsValueBean> nominationsApprovalDetailsList = new ArrayList<NominationsApprovalDetailsValueBean>();
  private List<NominationsApprovalTeamMembersValueBean> nominationsApprovalTeamMembersList = new ArrayList<NominationsApprovalTeamMembersValueBean>();
  private List<NominationsApprovalBehaviorsValueBean> nominationsApprovalBehaviorsList = new ArrayList<NominationsApprovalBehaviorsValueBean>();
  private List<NominationsApprovalCustomValueBean> nominationsApprovalCustomList = new ArrayList<NominationsApprovalCustomValueBean>();
  private List<NominationsApprovalAwardDetailsValueBean> nominationsApprovalAwardDetailsList = new ArrayList<NominationsApprovalAwardDetailsValueBean>();
  private List<NominationsApprovalTimePeriodsValueBean> nominationsApprovalTimePeriodsList = new ArrayList<NominationsApprovalTimePeriodsValueBean>();
  private List<NominationsApprovalDetailsValueBean> cumulativeNominationsApprovalDetailsList = new ArrayList<NominationsApprovalDetailsValueBean>();

  public int getTotalNumberOfApprovals()
  {
    return totalNumberOfApprovals;
  }

  public void setTotalNumberOfApprovals( int totalNumberOfApprovals )
  {
    this.totalNumberOfApprovals = totalNumberOfApprovals;
  }

  public List<NominationsApprovalDetailsValueBean> getNominationsApprovalDetailsList()
  {
    return nominationsApprovalDetailsList;
  }

  public void setNominationsApprovalDetailsList( List<NominationsApprovalDetailsValueBean> nominationsApprovalDetailsList )
  {
    this.nominationsApprovalDetailsList = nominationsApprovalDetailsList;
  }

  public List<NominationsApprovalTeamMembersValueBean> getNominationsApprovalTeamMembersList()
  {
    return nominationsApprovalTeamMembersList;
  }

  public void setNominationsApprovalTeamMembersList( List<NominationsApprovalTeamMembersValueBean> nominationsApprovalTeamMembersList )
  {
    this.nominationsApprovalTeamMembersList = nominationsApprovalTeamMembersList;
  }

  public List<NominationsApprovalBehaviorsValueBean> getNominationsApprovalBehaviorsList()
  {
    return nominationsApprovalBehaviorsList;
  }

  public void setNominationsApprovalBehaviorsList( List<NominationsApprovalBehaviorsValueBean> nominationsApprovalBehaviorsList )
  {
    this.nominationsApprovalBehaviorsList = nominationsApprovalBehaviorsList;
  }

  public List<NominationsApprovalCustomValueBean> getNominationsApprovalCustomList()
  {
    return nominationsApprovalCustomList;
  }

  public void setNominationsApprovalCustomList( List<NominationsApprovalCustomValueBean> nominationsApprovalCustomList )
  {
    this.nominationsApprovalCustomList = nominationsApprovalCustomList;
  }

  public List<NominationsApprovalAwardDetailsValueBean> getNominationsApprovalAwardDetailsList()
  {
    return nominationsApprovalAwardDetailsList;
  }

  public void setNominationsApprovalAwardDetailsList( List<NominationsApprovalAwardDetailsValueBean> nominationsApprovalAwardDetailsList )
  {
    this.nominationsApprovalAwardDetailsList = nominationsApprovalAwardDetailsList;
  }

  public List<NominationsApprovalPagePreviousLevelApproversValueBean> getPreviousLevelApproversList()
  {
    return previousLevelApproversList;
  }

  public void setPreviousLevelApproversList( List<NominationsApprovalPagePreviousLevelApproversValueBean> previousLevelApproversList )
  {
    this.previousLevelApproversList = previousLevelApproversList;
  }

  public List<NominationsApprovalPageNextLevelApproversValueBean> getNextLevelApproversList()
  {
    return nextLevelApproversList;
  }

  public void setNextLevelApproversList( List<NominationsApprovalPageNextLevelApproversValueBean> nextLevelApproversList )
  {
    this.nextLevelApproversList = nextLevelApproversList;
  }

  public List<NominationsApprovalTimePeriodsValueBean> getNominationsApprovalTimePeriodsList()
  {
    return nominationsApprovalTimePeriodsList;
  }

  public void setNominationsApprovalTimePeriodsList( List<NominationsApprovalTimePeriodsValueBean> nominationsApprovalTimePeriodsList )
  {
    this.nominationsApprovalTimePeriodsList = nominationsApprovalTimePeriodsList;
  }

  public List<NominationsApprovalBoxValueBean> getApprovalBoxList()
  {
    return approvalBoxList;
  }

  public void setApprovalBoxList( List<NominationsApprovalBoxValueBean> approvalBoxList )
  {
    this.approvalBoxList = approvalBoxList;
  }

  public List<NominationsApprovalDetailsValueBean> getCumulativeNominationsApprovalDetailsList()
  {
    return cumulativeNominationsApprovalDetailsList;
  }

  public void setCumulativeNominationsApprovalDetailsList( List<NominationsApprovalDetailsValueBean> cumulativeNominationsApprovalDetailsList )
  {
    this.cumulativeNominationsApprovalDetailsList = cumulativeNominationsApprovalDetailsList;
  }

}
