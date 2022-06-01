/**
 * 
 */

package com.biperf.core.service.publicrecognition.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jmock.Mock;
import org.jmock.core.Constraint;

import com.biperf.core.dao.promotion.PublicRecognitionDAO;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PublicRecognitionTabType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.promotion.PublicRecognitionLike;
import com.biperf.core.domain.publicrecognition.PublicRecognitionSet;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.impl.PublicRecognitionServiceImpl;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;

/**
 * @author poddutur
 *
 */
public class PublicRecognitionServiceImplTest extends BaseServiceTest
{
  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public PublicRecognitionServiceImplTest( String test )
  {
    super( test );
  }

  /** PublicRecognitionService */
  private PublicRecognitionServiceImpl publicRecognitionServiceImpl = new PublicRecognitionServiceImpl();

  /** mockForumTopicDAO */
  private Mock mockPublicRecognitionDAO;
  private Mock mockParticipantService;
  private Mock mockClaimService;
  private Mock mockUserService;
  private Mock mockPromotionService;
  private Mock mockBudgetService;
  private Mock mockSystemVariableService;

  private static final String CLIENT_NAME = "G5 Test";
  private static final String PUBLIC_RECOG_DEFAULT_TAB_NAME = "global";
  private static final String[] PUBLIC_RECOG_ACTIVE_TABS = { "mine", "followed", "team", "global" };

  /**
   * Setup the test. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockPublicRecognitionDAO = new Mock( PublicRecognitionDAO.class );
    mockParticipantService = new Mock( ParticipantService.class );
    mockClaimService = new Mock( ClaimService.class );
    mockUserService = new Mock( UserService.class );
    mockPromotionService = new Mock( PromotionService.class );
    mockSystemVariableService = new Mock( SystemVariableService.class );
    mockBudgetService = new Mock( BudgetMasterService.class );
    publicRecognitionServiceImpl.setParticipantService( (ParticipantService)mockParticipantService.proxy() );
    publicRecognitionServiceImpl.setClaimService( (ClaimService)mockClaimService.proxy() );
    publicRecognitionServiceImpl.setUserService( (UserService)mockUserService.proxy() );
    publicRecognitionServiceImpl.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );
    publicRecognitionServiceImpl.setPromotionService( (PromotionService)mockPromotionService.proxy() );
    publicRecognitionServiceImpl.setBudgetService( (BudgetMasterService)mockBudgetService.proxy() );
    publicRecognitionServiceImpl.setPublicRecognitionDAO( (PublicRecognitionDAO)mockPublicRecognitionDAO.proxy() );
  }

  public void testLikePublicRecognitionCount()
  {
    Long claimId = (long)10315;
    long expectedLikeCount = 0;

    mockPublicRecognitionDAO.expects( once() ).method( "getLikeCountByClaimId" ).with( same( claimId ) ).will( returnValue( expectedLikeCount ) );

    long actualLikeCount = publicRecognitionServiceImpl.recognitionLikeCountByClaimId( claimId );

    assertEquals( "Like count doesnot matches what was expected ", expectedLikeCount, actualLikeCount );
  }

  public void testLikePublicRecognition()
  {
    Long participantId = (long)60020;
    Long claimId = (long)10000;
    Participant user = new Participant();
    AbstractRecognitionClaim claim = new RecognitionClaim();
    user.setId( participantId );
    claim.setId( claimId );

    mockParticipantService.expects( once() ).method( "getParticipantById" ).with( same( participantId ) ).will( returnValue( user ) );

    mockClaimService.expects( once() ).method( "getClaimById" ).with( same( claimId ) ).will( returnValue( claim ) );

    PublicRecognitionLike publicRecogLike = new PublicRecognitionLike();
    publicRecogLike.setClaim( claim );
    publicRecogLike.setUser( user );
    publicRecogLike.setIsLiked( true );

    mockPublicRecognitionDAO.expects( once() ).method( "savePublicRecognitionLike" ).with( eq( publicRecogLike ) );

    publicRecognitionServiceImpl.likePublicRecognition( participantId, claimId );

    mockPublicRecognitionDAO.verify();
  }

  public void testUnLikePublicRecognition()
  {
    Long participantId = (long)60020;
    Long claimId = (long)10315;
    Participant participant = new Participant();
    participant.setId( participantId );
    List<PublicRecognitionLike> allLikesForClaim = new ArrayList<PublicRecognitionLike>();
    PublicRecognitionLike like = new PublicRecognitionLike();
    like.setUser( participant );
    allLikesForClaim.add( like );

    mockPublicRecognitionDAO.expects( once() ).method( "getUserLikesByClaim" ).with( same( claimId ) ).will( returnValue( allLikesForClaim ) );

    mockPublicRecognitionDAO.expects( once() ).method( "deletePublicRecognitionLike" ).with( same( like ) );

    publicRecognitionServiceImpl.unlikePublicRecognition( participantId, claimId );

    mockPublicRecognitionDAO.verify();
  }

  public void testSavePublicRecognitionComments()
  {
    List<PublicRecognitionComment> commentsToSave = new ArrayList<PublicRecognitionComment>();
    String comment = "Test Save Public Recognition Comment";
    Participant participant = new Participant();
    participant.setId( (long)60020 );
    AbstractRecognitionClaim claim = new RecognitionClaim();
    claim.setId( (long)10000 );
    claim.setTeamId( null );

    PublicRecognitionComment publicRecogComment = new PublicRecognitionComment();
    publicRecogComment.setComments( comment );
    publicRecogComment.setClaim( claim );
    publicRecogComment.setTeamId( claim.getTeamId() );
    publicRecogComment.setUser( participant );
    commentsToSave.add( publicRecogComment );
    LanguageType commentsLanguageType = null;

    mockUserService.expects( once() ).method( "getPreferredLanguageFor" ).with( same( publicRecogComment.getUser().getId() ) ).will( returnValue( commentsLanguageType ) );

    mockPublicRecognitionDAO.expects( once() ).method( "savePublicRecognitionComment" ).with( same( publicRecogComment ) ).will( returnValue( publicRecogComment ) );

    publicRecognitionServiceImpl.savePublicRecognitionComments( commentsToSave );

    mockPublicRecognitionDAO.verify();
  }

  public void testGetPublicRecognitionClaimsByTabType() throws ServiceErrorException
  {
    Long userId = (long)60020;
    Participant user = new Participant();
    String listType = PublicRecognitionTabType.lookup( PublicRecognitionTabType.GLOBAL_TAB ).getCode();
    int pageNumber = 1;
    int pageSize = 10;
    int rowNumStart = ( ( pageNumber - 1 ) * pageSize ) + 1;
    int rowNumEnd = rowNumStart + ( pageSize - 1 );
    String listValue = StringUtils.EMPTY;

    PropertySetItem systemVariableClientName = new PropertySetItem();

    systemVariableClientName.setStringVal( CLIENT_NAME );
    systemVariableClientName.setKey( SystemVariableService.CLIENT_NAME );
    systemVariableClientName.setEntityName( SystemVariableService.CLIENT_NAME );

    PropertySetItem systemVariablePubRecogDefaultName = new PropertySetItem();

    systemVariablePubRecogDefaultName.setStringVal( PUBLIC_RECOG_DEFAULT_TAB_NAME );
    systemVariablePubRecogDefaultName.setKey( SystemVariableService.PUBLIC_RECOG_DEFAULT_TAB_NAME );
    systemVariablePubRecogDefaultName.setEntityName( SystemVariableService.PUBLIC_RECOG_DEFAULT_TAB_NAME );

    PropertySetItem systemVariableActiveTab = new PropertySetItem();

    systemVariableActiveTab.setStringVal( PUBLIC_RECOG_ACTIVE_TABS.toString() );
    systemVariableActiveTab.setKey( SystemVariableService.PUBLIC_RECOG_ACTIVE_TABS );
    systemVariableActiveTab.setEntityName( SystemVariableService.PUBLIC_RECOG_ACTIVE_TABS );

    List<PublicRecognitionFormattedValueBean> claims = new ArrayList<PublicRecognitionFormattedValueBean>();
    List<PublicRecognitionSet> recognitionSetValueList = new ArrayList<PublicRecognitionSet>();

    Constraint[] constraint = new Constraint[] { same( userId ), same( listType ), same( rowNumStart ), same( rowNumEnd ), same( listValue ) };

    mockPublicRecognitionDAO.expects( once() ).method( "getPublicRecognitionList" ).with( constraint ).will( returnValue( claims ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.CLIENT_NAME ) ).will( returnValue( systemVariableClientName ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PUBLIC_RECOG_DEFAULT_TAB_NAME ) )
        .will( returnValue( systemVariablePubRecogDefaultName ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PUBLIC_RECOG_ACTIVE_TABS ) ).will( returnValue( systemVariableActiveTab ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PUBLIC_RECOG_ACTIVE_TABS ) ).will( returnValue( systemVariableActiveTab ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PUBLIC_RECOG_ACTIVE_TABS ) ).will( returnValue( systemVariableActiveTab ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PUBLIC_RECOG_ACTIVE_TABS ) ).will( returnValue( systemVariableActiveTab ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PUBLIC_RECOG_ACTIVE_TABS ) ).will( returnValue( systemVariableActiveTab ) );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PUBLIC_RECOG_ACTIVE_TABS ) ).will( returnValue( systemVariableActiveTab ) );

    mockParticipantService.expects( once() ).method( "getParticipantById" ).with( same( userId ) ).will( returnValue( user ) );

    recognitionSetValueList = publicRecognitionServiceImpl.getPublicRecognitionClaimsByTabType( userId, listType, pageSize, pageNumber, listValue );

    assertEquals( "List Sizes matches what was expected ", 1, recognitionSetValueList.size() );

    mockPublicRecognitionDAO.verify();
  }

}
