
package com.biperf.core.ui.engagement;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.EngagementSummaryType;
import com.biperf.core.value.EngagementTeamsValueBean;

/**
 * 
 * EngagementTeamTeamsView.
 * 
 * @author kandhi
 * @since May 29, 2014
 * @version 1.0
 */
public class EngagementTeamTeamsView
{
  private EngagementTeamMetaView meta;
  private List<EngagementTeamTeamsMembersView> members;

  public EngagementTeamTeamsView( List<EngagementTeamsValueBean> engagementTeamsValueBeanList, int totalTeamsCount, int pageNumber, String sortedOn, String sortedBy )
  {
    super();
    this.meta = new EngagementTeamMetaView( totalTeamsCount, pageNumber, sortedOn, sortedBy );
    this.members = populateTeamsMembersView( engagementTeamsValueBeanList );
  }

  /**
   * Populate Teams data in team view
   * @param engagementDashboardValueBean
   * @return
   */
  private List<EngagementTeamTeamsMembersView> populateTeamsMembersView( List<EngagementTeamsValueBean> engagementTeamsValueBeanList )
  {
    List<EngagementTeamTeamsMembersView> engagementTeamTeamsMembersViewList = new ArrayList<EngagementTeamTeamsMembersView>();
    if ( engagementTeamsValueBeanList != null )
    {
      for ( EngagementTeamsValueBean engagementTeamsValueBean : engagementTeamsValueBeanList )
      {
        EngagementTeamTeamsMembersDataView engagementTeamTeamsMembersDataView = null;
        List<EngagementTeamTeamsMembersDataView> engagementTeamTeamsMembersDataViewList = new ArrayList<EngagementTeamTeamsMembersDataView>();
        engagementTeamTeamsMembersDataView = new EngagementTeamTeamsMembersDataView( engagementTeamsValueBean.getCompanyGoal(),
                                                                                     engagementTeamsValueBean.getScore(),
                                                                                     EngagementSummaryType.PARTICIPATION_SCORE );
        engagementTeamTeamsMembersDataViewList.add( engagementTeamTeamsMembersDataView );
        engagementTeamTeamsMembersDataView = new EngagementTeamTeamsMembersDataView( engagementTeamsValueBean.getSentTarget(),
                                                                                     engagementTeamsValueBean.getSentCnt(),
                                                                                     EngagementSummaryType.RECOGNITIONS_SENT );
        engagementTeamTeamsMembersDataViewList.add( engagementTeamTeamsMembersDataView );
        engagementTeamTeamsMembersDataView = new EngagementTeamTeamsMembersDataView( engagementTeamsValueBean.getReceivedTarget(),
                                                                                     engagementTeamsValueBean.getReceivedCnt(),
                                                                                     EngagementSummaryType.RECOGNITIONS_RECEIVED );
        engagementTeamTeamsMembersDataViewList.add( engagementTeamTeamsMembersDataView );
        engagementTeamTeamsMembersDataView = new EngagementTeamTeamsMembersDataView( engagementTeamsValueBean.getConnectedToTarget(),
                                                                                     engagementTeamsValueBean.getConnectedToCnt(),
                                                                                     EngagementSummaryType.RECOGNIZED );
        engagementTeamTeamsMembersDataViewList.add( engagementTeamTeamsMembersDataView );
        engagementTeamTeamsMembersDataView = new EngagementTeamTeamsMembersDataView( engagementTeamsValueBean.getConnectedFromTarget(),
                                                                                     engagementTeamsValueBean.getConnectedFromCnt(),
                                                                                     EngagementSummaryType.RECOGNIZED_BY );
        engagementTeamTeamsMembersDataViewList.add( engagementTeamTeamsMembersDataView );
        engagementTeamTeamsMembersDataView = new EngagementTeamTeamsMembersDataView( engagementTeamsValueBean.getLoginActivityTarget(),
                                                                                     engagementTeamsValueBean.getLoginActivityCnt(),
                                                                                     EngagementSummaryType.VISITS );
        engagementTeamTeamsMembersDataViewList.add( engagementTeamTeamsMembersDataView );

        EngagementTeamTeamsMembersView engagementTeamTeamsMembersView = new EngagementTeamTeamsMembersView( engagementTeamsValueBean.getNodeId(),
                                                                                                            engagementTeamsValueBean.getNodeName(),
                                                                                                            engagementTeamsValueBean.getManagerName(),
                                                                                                            engagementTeamTeamsMembersDataViewList );
        engagementTeamTeamsMembersViewList.add( engagementTeamTeamsMembersView );
      }
    }
    return engagementTeamTeamsMembersViewList;
  }

  public EngagementTeamMetaView getMeta()
  {
    return meta;
  }

  public void setMeta( EngagementTeamMetaView meta )
  {
    this.meta = meta;
  }

  public List<EngagementTeamTeamsMembersView> getMembers()
  {
    return members;
  }

  public void setMembers( List<EngagementTeamTeamsMembersView> members )
  {
    this.members = members;
  }

}
