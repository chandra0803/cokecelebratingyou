
package com.biperf.core.ui.engagement;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.EngagementSummaryType;
import com.biperf.core.value.EngagementTeamMembersValueBean;

/**
 * 
 * EngagementTeamIndividualsView.
 * 
 * @author kandhi
 * @since May 29, 2014
 * @version 1.0
 */
public class EngagementTeamIndividualsView
{
  private EngagementTeamMetaView meta;
  private List<EngagementTeamIndividualsMembersView> members;

  public EngagementTeamIndividualsView( List<EngagementTeamMembersValueBean> engagementTeamMembersValueBeanList, int totalTeamCount, int pageNumber, String sortedOn, String sortedBy )
  {
    super();
    this.meta = new EngagementTeamMetaView( totalTeamCount, pageNumber, sortedOn, sortedBy );
    this.members = populateTeamIndividualsView( engagementTeamMembersValueBeanList );
  }

  /**
   * Populate Individual view in the team
   * @param engagementDashboardValueBean
   * @return
   */
  private List<EngagementTeamIndividualsMembersView> populateTeamIndividualsView( List<EngagementTeamMembersValueBean> engagementTeamMembersValueBeanList )
  {
    List<EngagementTeamIndividualsMembersView> members = new ArrayList<EngagementTeamIndividualsMembersView>();
    if ( engagementTeamMembersValueBeanList != null )
    {
      for ( EngagementTeamMembersValueBean engagementTeamMembersValueBean : engagementTeamMembersValueBeanList )
      {
        List<EngagementTeamIndividualsMembersDataView> engagementTeamIndividualsMembersDataViewList = new ArrayList<EngagementTeamIndividualsMembersDataView>();
        EngagementTeamIndividualsMembersDataView engagementTeamIndividualsMembersDataView = null;
        engagementTeamIndividualsMembersDataView = new EngagementTeamIndividualsMembersDataView( engagementTeamMembersValueBean.getCompanyGoal(),
                                                                                                 engagementTeamMembersValueBean.getScore(),
                                                                                                 EngagementSummaryType.PARTICIPATION_SCORE );
        engagementTeamIndividualsMembersDataViewList.add( engagementTeamIndividualsMembersDataView );
        engagementTeamIndividualsMembersDataView = new EngagementTeamIndividualsMembersDataView( engagementTeamMembersValueBean.getSentTarget(),
                                                                                                 engagementTeamMembersValueBean.getSentCnt(),
                                                                                                 EngagementSummaryType.RECOGNITIONS_SENT );
        engagementTeamIndividualsMembersDataViewList.add( engagementTeamIndividualsMembersDataView );
        engagementTeamIndividualsMembersDataView = new EngagementTeamIndividualsMembersDataView( engagementTeamMembersValueBean.getReceivedTarget(),
                                                                                                 engagementTeamMembersValueBean.getReceivedCnt(),
                                                                                                 EngagementSummaryType.RECOGNITIONS_RECEIVED );
        engagementTeamIndividualsMembersDataViewList.add( engagementTeamIndividualsMembersDataView );
        engagementTeamIndividualsMembersDataView = new EngagementTeamIndividualsMembersDataView( engagementTeamMembersValueBean.getConnectedToTarget(),
                                                                                                 engagementTeamMembersValueBean.getConnectedToCnt(),
                                                                                                 EngagementSummaryType.RECOGNIZED );
        engagementTeamIndividualsMembersDataViewList.add( engagementTeamIndividualsMembersDataView );
        engagementTeamIndividualsMembersDataView = new EngagementTeamIndividualsMembersDataView( engagementTeamMembersValueBean.getConnectedFromTarget(),
                                                                                                 engagementTeamMembersValueBean.getConnectedFromCnt(),
                                                                                                 EngagementSummaryType.RECOGNIZED_BY );
        engagementTeamIndividualsMembersDataViewList.add( engagementTeamIndividualsMembersDataView );
        engagementTeamIndividualsMembersDataView = new EngagementTeamIndividualsMembersDataView( engagementTeamMembersValueBean.getLoginActivityTarget(),
                                                                                                 engagementTeamMembersValueBean.getLoginActivityCnt(),
                                                                                                 EngagementSummaryType.VISITS );
        engagementTeamIndividualsMembersDataViewList.add( engagementTeamIndividualsMembersDataView );

        EngagementTeamIndividualsMembersView member = new EngagementTeamIndividualsMembersView( engagementTeamMembersValueBean.getNodeId(),
                                                                                                engagementTeamMembersValueBean.getUserId(),
                                                                                                engagementTeamMembersValueBean.getFirstName(),
                                                                                                engagementTeamMembersValueBean.getLastName(),
                                                                                                engagementTeamMembersValueBean.getAvatarUrl(),
                                                                                                engagementTeamMembersValueBean.isRecognizedRecent(),
                                                                                                engagementTeamIndividualsMembersDataViewList );
        members.add( member );
      }
    }
    return members;
  }

  public EngagementTeamMetaView getMeta()
  {
    return meta;
  }

  public void setMeta( EngagementTeamMetaView meta )
  {
    this.meta = meta;
  }

  public List<EngagementTeamIndividualsMembersView> getMembers()
  {
    return members;
  }

  public void setMembers( List<EngagementTeamIndividualsMembersView> members )
  {
    this.members = members;
  }

}
