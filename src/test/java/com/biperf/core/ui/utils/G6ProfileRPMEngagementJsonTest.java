
package com.biperf.core.ui.utils;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.ui.engagement.EngagementView;
import com.biperf.core.ui.profile.ProfileRPMView;
import com.biperf.core.utils.JsonUtils;
import com.biperf.core.value.EngagementAveragesValueBean;
import com.biperf.core.value.EngagementBehaviorValueBean;
import com.biperf.core.value.EngagementDashboardValueBean;
import com.biperf.core.value.EngagementSiteVisitsLoginValueBean;
import com.biperf.core.value.EngagementSummaryValueBean;
import com.biperf.core.value.EngagementTeamMembersValueBean;
import com.biperf.core.value.EngagementTeamSumValueBean;
import com.biperf.core.value.EngagementTeamsValueBean;
import com.biperf.core.value.NodeBean;

public class G6ProfileRPMEngagementJsonTest
{

  public static void main( String[] args )
  {
    ProfileRPMView view = new ProfileRPMView();

    view.setEngagementView( getengagementView() );

    try
    {
      BufferedWriter writer = Files.newBufferedWriter( Paths.get( "C:\\Users\\sedhuram\\Desktop\\json.txt" ) );
      writer.write( JsonUtils.toJsonStringFromObject( dashBoardVB() ) );

      System.out.println( "Done .." );
    }
    catch( Exception e )
    {

    }
  }

  public static EngagementSummaryValueBean getSummaryBean()
  {
    EngagementSummaryValueBean s = new EngagementSummaryValueBean();
    return s;
  }

  public static EngagementView getengagementView()
  {

    EngagementDashboardValueBean dashBoardBean = dashBoardVB();

    EngagementView v = new EngagementView( "mode", 5566L, "timeframeType", 1, 2016, dashBoardBean, new Date(), new Date( System.currentTimeMillis() + 100000000 ), "2000", true, "myNodeName" );

    return v;

  }

  public static EngagementDashboardValueBean dashBoardVB()
  {
    EngagementDashboardValueBean d = new EngagementDashboardValueBean();
    d.setUserName( "BHD-001" );
    d.setEngagementAveragesValueBean( engageMentAverageVB() );
    d.setEngagementBehaviorValueBeanList( getEngagementBehaviourBeanList() );
    d.setEngagementSiteVisitsLoginValueBeanList( getEngagementSiteVisitVBList() );
    d.setEngagementSummaryValueBean( getSummaryVB() );
    d.setEngagementTeamMembersValueBeanList( engagementTeamMembersValueBeanList() );
    d.setUserName( "bhd-001" );
    return d;
  }

  public static List<NodeBean> nodeBeanList()
  {

    return null;
  }

  public static List<NodeBean> managerNodeBeanList()
  {

    return null;
  }

  public static List<EngagementTeamsValueBean> engagementTeamsValueBeanList()
  {

    return null;
  }

  public static List<EngagementTeamSumValueBean> engagementTeamSumValueBeanList()
  {

    return null;
  }

  public static List<EngagementTeamMembersValueBean> engagementTeamMembersValueBeanList()
  {

    List<EngagementTeamMembersValueBean> list = new ArrayList<EngagementTeamMembersValueBean>();

    EngagementTeamMembersValueBean a = new EngagementTeamMembersValueBean();
    a.setAvatarUrl( "abc.com/aaaaa" );

    list.add( a );
    return list;
  }

  public static EngagementSummaryValueBean getSummaryVB()
  {
    EngagementSummaryValueBean summayVB = new EngagementSummaryValueBean();
    summayVB.setAsofDate( new Timestamp( System.currentTimeMillis() ) );
    summayVB.setCompanyGoal( 1 );
    summayVB.setConnectedFromAchievedCnt( 2 );
    summayVB.setConnectedFromCnt( 3 );
    summayVB.setConnectedFromPaxCnt( 4 );
    summayVB.setConnectedFromTarget( 5 );
    summayVB.setConnectedToAchievedCnt( 6 );
    summayVB.setConnectedToCnt( 7 );
    summayVB.setConnectedToPaxCnt( 8 );
    summayVB.setConnectedToTarget( 9 );
    summayVB.setDate( "1/1/2000" );
    summayVB.setDisplayTarget( true );
    summayVB.setLoginAchievedCnt( 1 );
    summayVB.setLoginActivityCnt( 2 );
    summayVB.setLoginActivityTarget( 3 );
    return summayVB;
  }

  public static List<EngagementSiteVisitsLoginValueBean> getEngagementSiteVisitVBList()
  {
    List<EngagementSiteVisitsLoginValueBean> list = new ArrayList<EngagementSiteVisitsLoginValueBean>();
    EngagementSiteVisitsLoginValueBean a = new EngagementSiteVisitsLoginValueBean();
    a.setDate( "1/1/200" );
    a.setLocaleTime( "11:00 CST" );
    a.setTime( "11:15 PM CST" );
    a.setTimeZoneId( "TZ_CST" );

    list.add( a );

    EngagementSiteVisitsLoginValueBean b = new EngagementSiteVisitsLoginValueBean();
    b.setDate( "1/1/200" );
    b.setLocaleTime( "11:00 CST" );
    b.setTime( "11:15 PM CST" );
    b.setTimeZoneId( "TZ_CST" );
    list.add( b );
    return list;
  }

  public static List<EngagementBehaviorValueBean> getEngagementBehaviourBeanList()
  {

    List<EngagementBehaviorValueBean> list = new ArrayList<EngagementBehaviorValueBean>();

    EngagementBehaviorValueBean bean1 = new EngagementBehaviorValueBean();
    bean1.setBadgeImageUrl( "abc.com/123.jpg" );
    bean1.setBehavior( "goodJob" );
    bean1.setReceivedCnt( 2 );
    bean1.setSentCnt( 2 );

    list.add( bean1 );

    EngagementBehaviorValueBean bean2 = new EngagementBehaviorValueBean();
    bean2.setBadgeImageUrl( "abc.com/456.jpg" );
    bean2.setBehavior( "goodJob" );
    bean2.setReceivedCnt( 2 );
    bean2.setSentCnt( 2 );
    list.add( bean2 );
    return list;
  }

  public static EngagementAveragesValueBean engageMentAverageVB()
  {
    EngagementAveragesValueBean averageVB = new EngagementAveragesValueBean();
    averageVB.setConnectedFromCompanyAvg( 1 );
    averageVB.setConnectedFromTeamAvg( 2 );
    averageVB.setConnectedToCompanyAvg( 3 );
    averageVB.setConnectedToTeamAvg( 4 );
    averageVB.setLoginActivityCompanyAvg( 5 );
    averageVB.setLoginActivityTeamAvg( 6 );
    averageVB.setRecRecvCompanyAvg( 7 );
    averageVB.setRecSentCompanyAvg( 8 );
    averageVB.setRecSentTeamAvg( 9 );
    averageVB.setScoreCompanyAvg( 10 );
    return averageVB;
  }

}
