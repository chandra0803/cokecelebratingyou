
package com.biperf.core.service.ssi.impl;

import static com.biperf.core.service.ssi.impl.SSIServiceTestUtil.addParticipantOne;
import static com.biperf.core.service.ssi.impl.SSIServiceTestUtil.addParticipantTwo;
import static com.biperf.core.service.ssi.impl.SSIServiceTestUtil.addParticipantValueBeanOne;
import static com.biperf.core.service.ssi.impl.SSIServiceTestUtil.buildContest;
import static com.biperf.core.service.ssi.impl.SSIServiceTestUtil.buildContestLevel;
import static com.biperf.core.service.ssi.impl.SSIServiceTestUtil.buildSSIContest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jmock.Mock;

import com.biperf.core.dao.gamification.GamificationDAO;
import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.dao.ssi.SSIContestParticipantDAO;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SSIIndividualBaselineType;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.ssi.SSIContestApproversUpdateAssociation;
import com.biperf.core.service.ssi.SSIContestBillCodesUpdateAssociation;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.ssi.SSIContestContentValueBean;
import com.biperf.core.value.ssi.SSIContestDescriptionValueBean;
import com.biperf.core.value.ssi.SSIContestDocumentValueBean;
import com.biperf.core.value.ssi.SSIContestLanguageValueBean;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;
import com.biperf.core.value.ssi.SSIContestNameValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

public class SSIContestServiceImplTest extends BaseServiceTest
{

  private Mock mockSsiContestDAO = null;
  private Mock mockCmAssetService = null;
  private Mock mockGamificationDAO = null;
  private Mock mockSsiContestParticipantDAO = null;
  private Mock mockSystemVariableService = null;

  public SSIContestServiceImplTest( String test )
  {
    super( test );
  }

  private SSIContestServiceImpl ssiContestService = new SSIContestServiceImpl()
  {
    protected Locale getUserLocale()
    {
      return Locale.US;
    }

    protected void flushHibernateSession()
    {
      // Do nothing during a unit test
    }

    protected SSIContestBillCodesUpdateAssociation buildBillCodesUpdateAssociation( SSIContest detachedContest )
    {
      return new SSIContestBillCodesUpdateAssociation( detachedContest )
      {
        protected void flushHibernateSession()
        {
          // Do nothing during a unit test
        }
      };
    }
  };

  protected void setUp() throws Exception
  {
    super.setUp();
    mockSsiContestDAO = new Mock( SSIContestDAO.class );
    mockCmAssetService = new Mock( CMAssetService.class );
    mockGamificationDAO = new Mock( GamificationDAO.class );
    mockSsiContestParticipantDAO = new Mock( SSIContestParticipantDAO.class );
    mockSystemVariableService = new Mock( SystemVariableService.class );

    ssiContestService.setSsiContestDAO( (SSIContestDAO)mockSsiContestDAO.proxy() );
    ssiContestService.setCmAssetService( (CMAssetService)mockCmAssetService.proxy() );
    ssiContestService.setGamificationDAO( (GamificationDAO)mockGamificationDAO.proxy() );
    ssiContestService.setSsiContestParticipantDAO( (SSIContestParticipantDAO)mockSsiContestParticipantDAO.proxy() );
    ssiContestService.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );
  }

  public void testSaveContest()
  {
    SSIContest expectedContest = buildContest( 100L, SSIContestType.lookup( SSIContestType.OBJECTIVES ) );

    SSIContestContentValueBean valueBean = new SSIContestContentValueBean();

    List<SSIContestLanguageValueBean> languages = new ArrayList<SSIContestLanguageValueBean>();
    List<SSIContestNameValueBean> names = new ArrayList<SSIContestNameValueBean>();
    List<SSIContestMessageValueBean> messages = new ArrayList<SSIContestMessageValueBean>();
    List<SSIContestDescriptionValueBean> descriptions = new ArrayList<SSIContestDescriptionValueBean>();
    List<SSIContestDocumentValueBean> documents = new ArrayList<SSIContestDocumentValueBean>();

    SSIContestLanguageValueBean language1 = new SSIContestLanguageValueBean();
    language1.setId( "en_US" );
    language1.setName( "English" );
    languages.add( language1 );
    SSIContestLanguageValueBean language2 = new SSIContestLanguageValueBean();
    language2.setId( "fr_CA" );
    language2.setName( "French" );
    languages.add( language2 );

    SSIContestNameValueBean name1 = new SSIContestNameValueBean();
    name1.setLanguage( "en_US" );
    name1.setText( "English Contest Name" );
    names.add( name1 );
    SSIContestNameValueBean name2 = new SSIContestNameValueBean();
    name2.setLanguage( "fr_CA" );
    name2.setText( "French Contest Name" );
    names.add( name2 );

    SSIContestDescriptionValueBean description1 = new SSIContestDescriptionValueBean();
    description1.setLanguage( "en_US" );
    description1.setText( "English Contest Description" );
    descriptions.add( description1 );
    SSIContestDescriptionValueBean description2 = new SSIContestDescriptionValueBean();
    description2.setLanguage( "fr_CA" );
    description2.setText( "French Contest Description" );
    descriptions.add( description2 );

    SSIContestMessageValueBean message1 = new SSIContestMessageValueBean();
    message1.setLanguage( "en_US" );
    message1.setText( "English Contest Name" );
    messages.add( message1 );

    SSIContestMessageValueBean message2 = new SSIContestMessageValueBean();
    message2.setLanguage( "fr_CA" );
    message2.setText( "French Contest Name" );
    messages.add( message2 );

    valueBean.setNames( names );
    valueBean.setMessages( messages );
    valueBean.setDescriptions( descriptions );
    valueBean.setDocuments( documents );

    String uniqueAssetName = "Asset Name";
    mockCmAssetService.expects( once() ).method( "getUniqueAssetCode" ).will( returnValue( uniqueAssetName ) );
    mockCmAssetService.expects( atLeastOnce() ).method( "createOrUpdateAsset" );
    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( expectedContest ) );
    if ( expectedContest.getId() == null && expectedContest.getId() < 0 )
    {
      mockSsiContestDAO.expects( once() ).method( "saveContest" ).will( returnValue( expectedContest ) );
    }

    List<UpdateAssociationRequest> updateAssociations = new ArrayList<UpdateAssociationRequest>();
    updateAssociations.add( new SSIContestApproversUpdateAssociation( expectedContest ) );

    try
    {
      SSIContest actualContest = ssiContestService.saveContest( expectedContest, valueBean, updateAssociations );
      assertEquals( actualContest.getId(), expectedContest.getId() );

    }
    catch( ServiceErrorException e )
    {
      fail();
    }

  }

  public void testSaveStepItUpContestPayout() throws ServiceErrorException
  {

    SSIContest detachedContest = buildContest( 100L, SSIContestType.lookup( SSIContestType.STEP_IT_UP ) );
    SSIContest attachSSIContest = buildContest( 1L, SSIContestType.lookup( SSIContestType.STEP_IT_UP ) );
    attachSSIContest.setContestBillCodes( new ArrayList<>() );
    attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.LIVE ) );
    detachedContest.setContestBillCodes( new ArrayList<>() );
    // List<UpdateAssociationRequest> updateAssociationRequests = new
    // ArrayList<UpdateAssociationRequest>();
    // updateAssociationRequests.add( new SSIContestLevelsUpdateAssociation( detachedContest ) );

    List<SSIContestParticipant> participants = new ArrayList<SSIContestParticipant>();
    participants.add( addParticipantOne() );
    participants.add( addParticipantTwo() );

    List<SSIContestParticipantValueBean> detachedContestParticipants = new ArrayList<SSIContestParticipantValueBean>();
    detachedContestParticipants.add( addParticipantValueBeanOne() );

    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( attachSSIContest ) );
    mockSsiContestDAO.expects( once() ).method( "getContestParticipants" ).will( returnValue( participants ) );
    mockSsiContestDAO.expects( once() ).method( "updateContestGoalPercentage" );

    SSIContest actualSSIContest = ssiContestService.savePayoutStepItUp( detachedContest, detachedContestParticipants );
    assertEquals( actualSSIContest.getId(), attachSSIContest.getId() );
  }

  public void testDeleteContestWithCorrestStatus()
  {
    SSIContest contest = new SSIContest()
    {
      public boolean isDeleteable()
      {
        return true;
      }
    };
    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( contest ) );
    mockSsiContestDAO.expects( once() ).method( "deleteContest" );
    mockCmAssetService.expects( once() ).method( "deleteCMAsset" );
    try
    {
      ssiContestService.deleteContest( 123L );
      mockSsiContestDAO.verify();
      mockCmAssetService.verify();
    }
    catch( Exception e )
    {
      fail();
    }

  }

  public void testDeleteContestWithInCorrestStatus()
  {
    SSIContest contest = new SSIContest()
    {
      public boolean isDeleteable()
      {
        return false;
      }
    };
    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( contest ) );
    try
    {
      ssiContestService.deleteContest( 123L );
      fail();
    }

    catch( ServiceErrorException e )
    {
      mockSsiContestDAO.verify();
      assertTrue( true );
    }

    catch( Exception e )
    {
      fail();
    }

  }

  public void testSavePayoutObjectivesContest() throws ServiceErrorException
  {
    SSIContest detachSSIContest = buildSSIContest( true );
    SSIContest attachSSIContest = buildContest( 1L, SSIContestType.lookup( SSIContestType.OBJECTIVES ) );
    attachSSIContest.setContestBillCodes( new ArrayList<>() );
    attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.LIVE ) );
    detachSSIContest.setContestBillCodes( new ArrayList<>() );
    List<SSIContestParticipant> participants = new ArrayList<SSIContestParticipant>();
    participants.add( addParticipantOne() );
    participants.add( addParticipantTwo() );
    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( attachSSIContest ) );
    mockSsiContestDAO.expects( once() ).method( "updateContestGoalPercentage" );
    mockGamificationDAO.expects( once() ).method( "getBadgeRuleById" ).will( returnValue( new BadgeRule() ) );

    SSIContest actualSSIContest = ssiContestService.savePayoutObjectives( detachSSIContest, 1L, new ArrayList<SSIContestParticipantValueBean>() );
    assertEquals( actualSSIContest.getId(), attachSSIContest.getId() );

  }

  public void testSaveDoThisGetThatContest() throws ServiceErrorException
  {
    SSIContest detachedContest = buildSSIContest( true );
    SSIContest attachSSIContest = buildContest( 1L, SSIContestType.lookup( SSIContestType.DO_THIS_GET_THAT ) );
    attachSSIContest.setContestBillCodes( new ArrayList<>() );
    attachSSIContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.LIVE ) );
    detachedContest.setContestBillCodes( new ArrayList<>() );
    List<SSIContestParticipant> participants = new ArrayList<SSIContestParticipant>();
    participants.add( addParticipantOne() );
    participants.add( addParticipantTwo() );
    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( attachSSIContest ) );
    mockSsiContestDAO.expects( once() ).method( "updateContestGoalPercentage" );

    SSIContest actualSSIContest = ssiContestService.savePayoutDoThisGetThat( detachedContest );
    assertEquals( actualSSIContest.getId(), attachSSIContest.getId() );

  }

  private Set<SSIContestActivity> getContestActivities()
  {
    Set<SSIContestActivity> ssiContestActivitySet = new HashSet<SSIContestActivity>();
    SSIContestActivity SSIContestActivity = new SSIContestActivity();
    SSIContestActivity.setId( 1L );
    SSIContestActivity.setSequenceNumber( 123 );
    SSIContestActivity.setVersion( 1L );
    ssiContestActivitySet.add( SSIContestActivity );
    return ssiContestActivitySet;
  }

  public void testGetContestParticipantsByContestId() throws ServiceErrorException
  {
    List<SSIContestParticipant> participants = new ArrayList<SSIContestParticipant>();
    participants.add( addParticipantOne() );
    participants.add( addParticipantTwo() );
    mockSsiContestDAO.expects( once() ).method( "getAllContestParticipantsByContestId" ).will( returnValue( participants ) );

    List<SSIContestParticipant> actualParticipants = ssiContestService.getAllContestParticipantsByContestId( 1L );
    if ( actualParticipants != null && actualParticipants.size() > 1 )
    {
      for ( SSIContestParticipant ssiContestParticipant : actualParticipants )
      {
        assertSame( 1L, ssiContestParticipant.getId() );
      }
    }
  }

  public void testUpdatePayout()
  {
    SSIContest detachSSIContest = buildSSIContest( true );
    Long contestId = 1L;
    List<SSIContestParticipant> participants = new ArrayList<SSIContestParticipant>();
    participants.add( addParticipantOne() );
    participants.add( addParticipantTwo() );

    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( detachSSIContest ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "getContestParticipants" ).will( returnValue( participants ) );

    participants = ssiContestService.updatePayout( detachSSIContest, contestId );
    
    assert participants.get( 0 ).getObjectiveAmount().equals( 1000D );
    assert participants.get( 1 ).getObjectiveAmount().equals( 1200D );
  }

  public void testUpdateLevelPayout()
  {

    SSIContest detachSSIContest = buildSSIContest( true );
    detachSSIContest.setIndividualBaselineType( SSIIndividualBaselineType.lookup( SSIIndividualBaselineType.PERCENTAGE_OVER_BASELINE_CODE ) );
    SSIContest attachSSIContest = buildContest( 1L, SSIContestType.lookup( SSIContestType.OBJECTIVES ) );
    Long contestId = 1L;
    List<SSIContestParticipant> participants = new ArrayList<SSIContestParticipant>();
    SSIContestParticipant participantOne = addParticipantOne();
    SSIContestParticipant participantTwo = addParticipantTwo();
    participants.add( participantOne );
    participants.add( participantTwo );
    List<SSIContestLevel> contestLevels = new ArrayList<>();
    contestLevels.add( buildContestLevel() );

    mockSsiContestDAO.expects( once() ).method( "getContestById" ).will( returnValue( attachSSIContest ) );
    mockSsiContestDAO.expects( atLeastOnce() ).method( "getContestLevelsByContestId" ).will( returnValue( contestLevels ) );
    mockSsiContestParticipantDAO.expects( once() ).method( "getContestParticipants" ).will( returnValue( participants ) );

    participants = ssiContestService.updateLevelPayout( detachSSIContest, contestId );
    
    assert participants.get( 0 ).getObjectiveAmount().equals( 1000D );
    assert participants.get( 1 ).getObjectiveAmount().equals( 1200D );
  }

  public void testGetContestListByCreator()
  {
    List<SSIContestListValueBean> contestList = new ArrayList<SSIContestListValueBean>();
    mockSsiContestDAO.expects( once() ).method( "getContestListByCreatorSuperViewer" ).will( returnValue( contestList ) );
    mockSsiContestDAO.expects( once() ).method( "getAwardThemNowContestSuperViewer" ).will( returnValue( new ArrayList<SSIContestListValueBean>() ) );
    mockCmAssetService.expects( atLeastOnce() ).method( "getString" ).will( returnValue( "a cm string" ) );
    final AtomicBoolean assetCodeTranslated = new AtomicBoolean( false );
    final AtomicBoolean statuLabelPopulated = new AtomicBoolean( false );
    final AtomicBoolean rolePopulated = new AtomicBoolean( false );

    SSIContestServiceImpl ssiContestService = new SSIContestServiceImpl()
    {
      protected List<SSIContestListValueBean> translateAssetCode( List<SSIContestListValueBean> contestList )
      {
        assetCodeTranslated.set( true );
        return contestList;
      }

      protected void populateStatusLabel( List<SSIContestListValueBean> contestList )
      {
        statuLabelPopulated.set( true );
      }

      protected void populateCreatorRoleAndUrl( List<SSIContestListValueBean> contestList, String role, String roleLabel, Long participantId )
      {
        rolePopulated.set( true );
      }
      
      protected Locale getUserLocale()
      {
        return Locale.US;
      }
    };
    ssiContestService.setSsiContestDAO( (SSIContestDAO)mockSsiContestDAO.proxy() );
    ssiContestService.setCmAssetService( (CMAssetService)mockCmAssetService.proxy() );
    ssiContestService.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );
    assertNotNull( ssiContestService.getContestListByCreator( 1L ) );
    mockSsiContestDAO.verify();
    assertTrue( assetCodeTranslated.get() && statuLabelPopulated.get() && rolePopulated.get() );
  }

  public void testIsContestCreator()
  {
    mockSsiContestDAO.expects( once() ).method( "fetchContestCreatorCount" ).will( returnValue( false ) );
    boolean isCreator = ssiContestService.isContestCreator( 75582L );
    assertEquals( isCreator, false );

    mockSsiContestDAO.expects( once() ).method( "fetchContestCreatorCount" ).will( returnValue( true ) );
    boolean isContCreator = ssiContestService.isContestCreator( 5582L );
    assertEquals( isContCreator, true );
  }

}
