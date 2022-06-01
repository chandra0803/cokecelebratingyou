
package com.biperf.core.service.ssi.impl;

import static com.biperf.core.service.ssi.impl.SSIServiceTestUtil.buildSSIContest;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.gamification.GamificationDAO;
import com.biperf.core.dao.ssi.SSIContestAwardThemNowDAO;
import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.dao.ssi.SSIContestParticipantDAO;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.utils.MockContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public class SSIContestAwardThemNowServiceImplTest extends MockObjectTestCase
{

  private Mock mockSsiContestAwardThemNowDAO = null;
  private Mock mockSsiContestDAO = null;
  private Mock mockSSIContestService = null;
  private Mock mockCmAssetService = null;
  private Mock mockGamificationDAO = null;
  private Mock mockSsiContestParticipantDAO = null;
  private Mock mockProcessService = null;

  public SSIContestAwardThemNowServiceImplTest( String test )
  {
    super( test );
  }

  private SSIContestAwardThemNowServiceImpl ssiContestAwardThemNowService = new SSIContestAwardThemNowServiceImpl();

  private SSIContestServiceImpl ssiContestService = new SSIContestServiceImpl();

  protected void setUp() throws Exception
  {
    super.setUp();
    mockSsiContestDAO = new Mock( SSIContestDAO.class );
    mockSsiContestAwardThemNowDAO = new Mock( SSIContestAwardThemNowDAO.class );
    mockProcessService = new Mock( ProcessService.class );
    mockSSIContestService = new Mock( SSIContestService.class );
    mockCmAssetService = new Mock( CMAssetService.class );
    mockGamificationDAO = new Mock( GamificationDAO.class );
    mockSsiContestParticipantDAO = new Mock( SSIContestParticipantDAO.class );

    ssiContestAwardThemNowService.setSsiContestDAO( (SSIContestDAO)mockSsiContestDAO.proxy() );
    ssiContestAwardThemNowService.setSsiContestAwardThemNowDAO( (SSIContestAwardThemNowDAO)mockSsiContestAwardThemNowDAO.proxy() );
    ssiContestAwardThemNowService.setCmAssetService( (CMAssetService)mockCmAssetService.proxy() );
    ssiContestAwardThemNowService.setGamificationDAO( (GamificationDAO)mockGamificationDAO.proxy() );
    ssiContestAwardThemNowService.setSsiContestParticipantDAO( (SSIContestParticipantDAO)mockSsiContestParticipantDAO.proxy() );

    ContentReaderManager.setContentReader( new MockContentReader() );
  }

  public void testUpdateAwardThemNowContestStatus()
  {
    Long contestId = 1L;
    short issuanceNumber = 1;

    SSIContest attachSSIContest = buildSSIContest( false );
    attachSSIContest.getPromotion().setContestApprovalLevels( 0 );
    attachSSIContest.setStatus( (SSIContestStatus)MockPickListFactory.getMockPickListItem( SSIContestStatus.class, SSIContestStatus.LIVE ) );
    attachSSIContest.setContestType( (SSIContestType)MockPickListFactory.getMockPickListItem( SSIContestType.class, SSIContestType.AWARD_THEM_NOW ) );

    mockSsiContestDAO.expects( once() ).method( "approveContestPayouts" ).withAnyArguments().will( returnValue( false ) );
    mockSsiContestDAO.expects( once() ).method( "getContestByIdWithAssociations" ).will( returnValue( attachSSIContest ) );
    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( attachSSIContest ) );
    mockSsiContestAwardThemNowDAO.expects( once() ).method( "updateAwardThemNowContestStatus" );

    try
    {
      ssiContestAwardThemNowService.updateAwardThemNowContestStatus( contestId, issuanceNumber );

    }
    catch( ServiceErrorException e )
    {
      fail();
    }

  }
}
