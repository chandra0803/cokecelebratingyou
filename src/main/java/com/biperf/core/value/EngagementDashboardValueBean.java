
package com.biperf.core.value;

import java.util.List;

/**
 * 
 * EngagementDashboardValueBean.
 * 
 * @author kandhi
 * @since Jun 3, 2014
 * @version 1.0
 */
public class EngagementDashboardValueBean
{
  private EngagementSummaryValueBean engagementSummaryValueBean;
  private List<EngagementTeamMembersValueBean> engagementTeamMembersValueBeanList;
  private List<EngagementTeamsValueBean> engagementTeamsValueBeanList;
  private List<EngagementTeamSumValueBean> engagementTeamSumValueBeanList;
  private EngagementAveragesValueBean engagementAveragesValueBean;
  private List<EngagementBehaviorValueBean> engagementBehaviorValueBeanList;
  private List<NodeBean> nodeBeanList;
  private List<NodeBean> managerNodeBeanList;
  private List<EngagementSiteVisitsLoginValueBean> engagementSiteVisitsLoginValueBeanList;
  private String userName;

  public List<EngagementSiteVisitsLoginValueBean> getEngagementSiteVisitsLoginValueBeanList()
  {
    return engagementSiteVisitsLoginValueBeanList;
  }

  public void setEngagementSiteVisitsLoginValueBeanList( List<EngagementSiteVisitsLoginValueBean> engagementSiteVisitsLoginValueBeanList )
  {
    this.engagementSiteVisitsLoginValueBeanList = engagementSiteVisitsLoginValueBeanList;
  }

  public EngagementAveragesValueBean getEngagementAveragesValueBean()
  {
    return engagementAveragesValueBean;
  }

  public void setEngagementAveragesValueBean( EngagementAveragesValueBean engagementAveragesValueBean )
  {
    this.engagementAveragesValueBean = engagementAveragesValueBean;
  }

  public List<EngagementBehaviorValueBean> getEngagementBehaviorValueBeanList()
  {
    return engagementBehaviorValueBeanList;
  }

  public void setEngagementBehaviorValueBeanList( List<EngagementBehaviorValueBean> engagementBehaviorValueBeanList )
  {
    this.engagementBehaviorValueBeanList = engagementBehaviorValueBeanList;
  }

  public List<NodeBean> getNodeBeanList()
  {
    return nodeBeanList;
  }

  public void setNodeBeanList( List<NodeBean> nodeBeanList )
  {
    this.nodeBeanList = nodeBeanList;
  }

  public List<EngagementTeamsValueBean> getEngagementTeamsValueBeanList()
  {
    return engagementTeamsValueBeanList;
  }

  public void setEngagementTeamsValueBeanList( List<EngagementTeamsValueBean> engagementTeamsValueBeanList )
  {
    this.engagementTeamsValueBeanList = engagementTeamsValueBeanList;
  }

  public EngagementSummaryValueBean getEngagementSummaryValueBean()
  {
    return engagementSummaryValueBean;
  }

  public void setEngagementSummaryValueBean( EngagementSummaryValueBean engagementSummaryValueBean )
  {
    this.engagementSummaryValueBean = engagementSummaryValueBean;
  }

  public List<EngagementTeamMembersValueBean> getEngagementTeamMembersValueBeanList()
  {
    return engagementTeamMembersValueBeanList;
  }

  public void setEngagementTeamMembersValueBeanList( List<EngagementTeamMembersValueBean> engagementTeamMembersValueBeanList )
  {
    this.engagementTeamMembersValueBeanList = engagementTeamMembersValueBeanList;
  }

  public List<EngagementTeamSumValueBean> getEngagementTeamSumValueBeanList()
  {
    return engagementTeamSumValueBeanList;
  }

  public void setEngagementTeamSumValueBeanList( List<EngagementTeamSumValueBean> engagementTeamSumValueBeanList )
  {
    this.engagementTeamSumValueBeanList = engagementTeamSumValueBeanList;
  }

  public List<NodeBean> getManagerNodeBeanList()
  {
    return managerNodeBeanList;
  }

  public void setManagerNodeBeanList( List<NodeBean> managerNodeBeanList )
  {
    this.managerNodeBeanList = managerNodeBeanList;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

}
