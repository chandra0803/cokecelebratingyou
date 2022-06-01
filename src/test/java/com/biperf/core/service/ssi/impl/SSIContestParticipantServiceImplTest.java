
package com.biperf.core.service.ssi.impl;

import static com.biperf.core.service.ssi.impl.SSIServiceTestUtil.buildSSIContest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.dao.ssi.SSIContestParticipantDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestParticipantProgress;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.utils.MockContentReader;
import com.biperf.core.value.ssi.SSIConetstParticipantActivityValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

public class SSIContestParticipantServiceImplTest extends MockObjectTestCase
{

  private Mock mockSsiContestDAO = null;
  private Mock mockSsiContestParticipantDAO = null;
  private Mock mockSsiContestService = null;

  SSIContestParticipantServiceImpl ssiContestParticipantService = new SSIContestParticipantServiceImpl()
  {
    protected boolean isDoThisGetThat( SSIContest contest )
    {
      return false;
    }
  };

  public SSIContestParticipantServiceImplTest( String test )
  {
    super( test );
  }

  protected void setUp() throws Exception
  {
    super.setUp();
    mockSsiContestDAO = new Mock( SSIContestDAO.class );
    mockSsiContestParticipantDAO = new Mock( SSIContestParticipantDAO.class );
    mockSsiContestService = new Mock( SSIContestService.class );
    ssiContestParticipantService.setSsiContestDAO( (SSIContestDAO)mockSsiContestDAO.proxy() );
    ssiContestParticipantService.setSsiContestParticipantDAO( (SSIContestParticipantDAO)mockSsiContestParticipantDAO.proxy() );
    ssiContestParticipantService.setSsiContestService( (SSIContestService)mockSsiContestService.proxy() );
    ContentReaderManager.setContentReader( new MockContentReader() );
  }

  public void testContestParticipantProgressSave()
  {

    SSIContest contest = buildSSIContest( true );
    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( contest ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "getContestParticipantsProgresses" ).will( returnValue( new ArrayList<SSIContestParticipantProgress>() ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "getParticipant" ).will( returnValue( null ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "saveContestParticipantProgress" );

    Date activityDate = new Date();
    List<SSIConetstParticipantActivityValueBean> participantActivities = new ArrayList<SSIConetstParticipantActivityValueBean>();
    Iterator<SSIContestParticipant> paxIterator = contest.getContestParticipants().iterator();
    SSIConetstParticipantActivityValueBean paxActivity1 = new SSIConetstParticipantActivityValueBean();
    paxActivity1.setId( paxIterator.next().getParticipant().getId() );
    SSIConetstParticipantActivityValueBean.Activity activity = paxActivity1.new Activity();
    activity.setTotalActivity( "1.1" );
    activity.setId( 111L );
    paxActivity1.getActivityAsList().add( activity );
    participantActivities.add( paxActivity1 );
    ssiContestParticipantService.saveContestParticipantProgress( contest.getId(), activityDate, participantActivities );

    mockSsiContestDAO.verify();
    mockSsiContestParticipantDAO.verify();

    assertEquals( contest.getLastProgressUpdateDate(), activityDate );

  }

  public void testContestParticipantProgressUpdate()
  {

    SSIContest contest = buildSSIContest( true );

    Date updatedActivityDate = new Date();
    List<SSIConetstParticipantActivityValueBean> participantActivities = new ArrayList<SSIConetstParticipantActivityValueBean>();
    Iterator<SSIContestParticipant> paxIterator = contest.getContestParticipants().iterator();
    SSIConetstParticipantActivityValueBean paxActivity1 = new SSIConetstParticipantActivityValueBean();
    paxActivity1.setId( paxIterator.next().getParticipant().getId() );
    Double updatedActivityAmount = 1.1;
    SSIConetstParticipantActivityValueBean.Activity activity = paxActivity1.new Activity();
    activity.setTotalActivity( updatedActivityAmount.toString() );
    activity.setId( 111L );
    paxActivity1.getActivityAsList().add( activity );
    participantActivities.add( paxActivity1 );

    ArrayList<SSIContestParticipantProgress> attachedPaxProgressList = new ArrayList<SSIContestParticipantProgress>();
    SSIContestParticipantProgress ssiContestParticipantProgress = new SSIContestParticipantProgress();
    ssiContestParticipantProgress.setActivityAmount( 123.0 );
    ssiContestParticipantProgress.setActivityDate( null );
    Participant ssiContestParticipant = new Participant();
    ssiContestParticipant.setId( paxActivity1.getId() );
    ssiContestParticipantProgress.setParticipant( ssiContestParticipant );
    attachedPaxProgressList.add( ssiContestParticipantProgress );

    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( contest ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "getContestParticipantsProgresses" ).will( returnValue( attachedPaxProgressList ) );

    ssiContestParticipantService.saveContestParticipantProgress( contest.getId(), updatedActivityDate, participantActivities );

    mockSsiContestDAO.verify();
    mockSsiContestParticipantDAO.verify();

    assertEquals( ssiContestParticipantProgress.getActivityAmount(), updatedActivityAmount );
    assertEquals( ssiContestParticipantProgress.getActivityDate(), updatedActivityDate );

  }

  public void testDTGTContestParticipantProgressSave()
  {

    SSIContestParticipantServiceImpl ssiContestParticipantService = new SSIContestParticipantServiceImpl()
    {
      protected boolean isDoThisGetThat( SSIContest contest )
      {
        return true;
      }
    };

    ssiContestParticipantService.setSsiContestDAO( (SSIContestDAO)mockSsiContestDAO.proxy() );
    ssiContestParticipantService.setSsiContestParticipantDAO( (SSIContestParticipantDAO)mockSsiContestParticipantDAO.proxy() );

    SSIContest contest = buildSSIContest( true );
    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( contest ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "getContestParticipantsProgresses" ).will( returnValue( new ArrayList<SSIContestParticipantProgress>() ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "getParticipant" ).will( returnValue( null ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "saveContestParticipantProgress" );
    mockSsiContestDAO.expects( once() ).method( "getContestActivityById" ).will( returnValue( null ) );

    Date activityDate = new Date();
    List<SSIConetstParticipantActivityValueBean> participantActivities = new ArrayList<SSIConetstParticipantActivityValueBean>();
    Iterator<SSIContestParticipant> paxIterator = contest.getContestParticipants().iterator();
    SSIConetstParticipantActivityValueBean paxActivity1 = new SSIConetstParticipantActivityValueBean();
    paxActivity1.setId( paxIterator.next().getParticipant().getId() );
    SSIConetstParticipantActivityValueBean.Activity activity = paxActivity1.new Activity();
    activity.setTotalActivity( "1.1" );
    activity.setId( 111L );
    paxActivity1.getActivityAsList().add( activity );
    participantActivities.add( paxActivity1 );
    ssiContestParticipantService.saveContestParticipantProgress( contest.getId(), activityDate, participantActivities );

    mockSsiContestDAO.verify();
    mockSsiContestParticipantDAO.verify();

    assertEquals( contest.getLastProgressUpdateDate(), activityDate );

  }

  public void testDTGTContestParticipantProgressUpdate()
  {

    SSIContestParticipantServiceImpl ssiContestParticipantService = new SSIContestParticipantServiceImpl()
    {
      protected boolean isDoThisGetThat( SSIContest contest )
      {
        return true;
      }
    };

    ssiContestParticipantService.setSsiContestDAO( (SSIContestDAO)mockSsiContestDAO.proxy() );
    ssiContestParticipantService.setSsiContestParticipantDAO( (SSIContestParticipantDAO)mockSsiContestParticipantDAO.proxy() );

    SSIContest contest = buildSSIContest( true );

    Date updatedActivityDate = new Date();
    List<SSIConetstParticipantActivityValueBean> participantActivities = new ArrayList<SSIConetstParticipantActivityValueBean>();
    Iterator<SSIContestParticipant> paxIterator = contest.getContestParticipants().iterator();
    SSIConetstParticipantActivityValueBean paxActivity1 = new SSIConetstParticipantActivityValueBean();
    paxActivity1.setId( paxIterator.next().getParticipant().getId() );
    Double updatedActivityAmount = 1.1;
    participantActivities.add( paxActivity1 );
    SSIConetstParticipantActivityValueBean.Activity activity = paxActivity1.new Activity();
    activity.setTotalActivity( updatedActivityAmount.toString() );
    activity.setId( 111L );
    paxActivity1.getActivityAsList().add( activity );

    ArrayList<SSIContestParticipantProgress> attachedPaxProgressList = new ArrayList<SSIContestParticipantProgress>();
    SSIContestParticipantProgress ssiContestParticipantProgress = new SSIContestParticipantProgress();
    ssiContestParticipantProgress.setActivityAmount( 123.0 );
    ssiContestParticipantProgress.setActivityDate( null );
    Participant ssiContestParticipant = new Participant();
    ssiContestParticipant.setId( paxActivity1.getId() );
    ssiContestParticipantProgress.setParticipant( ssiContestParticipant );
    SSIContestActivity contestActivity = new SSIContestActivity();
    contestActivity.setId( 111L );
    ssiContestParticipantProgress.setContestActivity( contestActivity );
    attachedPaxProgressList.add( ssiContestParticipantProgress );

    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( contest ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "getContestParticipantsProgresses" ).will( returnValue( attachedPaxProgressList ) );

    ssiContestParticipantService.saveContestParticipantProgress( contest.getId(), updatedActivityDate, participantActivities );

    mockSsiContestDAO.verify();
    mockSsiContestParticipantDAO.verify();

    assertEquals( ssiContestParticipantProgress.getActivityAmount(), updatedActivityAmount );
    assertEquals( ssiContestParticipantProgress.getActivityDate(), updatedActivityDate );

  }

  public void testIsLoggedInAsPax()
  {
    SSIContest contest = buildSSIContest( true );
    mockSsiContestService.expects( atLeastOnce() ).method( "getContestById" ).will( returnValue( contest ) );

    boolean bool = ssiContestParticipantService.isSuperViewer( contest, 1L );
    assertEquals( false, bool );

    bool = ssiContestParticipantService.isSuperViewer( contest, 1005L );
    assertEquals( true, bool );

    bool = ssiContestParticipantService.isSuperViewer( contest, 5582L );
    assertEquals( false, bool );
  }

}
