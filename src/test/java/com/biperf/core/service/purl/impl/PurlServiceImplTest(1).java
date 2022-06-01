
package com.biperf.core.service.purl.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
import org.jmock.Mock;
import org.junit.Test;

import com.biperf.core.builders.BuilderUtil;
import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.dao.purl.PurlContributorCommentDAO;
import com.biperf.core.dao.purl.PurlContributorDAO;
import com.biperf.core.dao.purl.PurlContributorMediaDAO;
import com.biperf.core.dao.purl.PurlRecipientDAO;
import com.biperf.core.domain.activity.MerchOrderActivity;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.AddressMethodType;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.PurlContributorCommentStatus;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorMediaType;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlContributorMedia;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.purl.PurlContributorCommentValidationException;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.PurlMediaUploadValue;

public class PurlServiceImplTest extends BaseServiceTest
{
  public PurlServiceImplTest( String test )
  {
    super( test );
  }

  private PurlServiceImpl purlService = new PurlServiceImpl();

  private Mock mockPurlRecipientDAO = null;
  private Mock mockPurlContributorDAO = null;
  private Mock mockPurlContributorCommentDAO = null;
  private Mock mockPurlContributorMediaDAO = null;
  private Mock mockActivityDAO = null;
  private Mock mockMerchOrderDAO = null;

  private Mock mockParticipantService = null;
  private Mock mockNodeService = null;
  private Mock mockSystemVariableService = null;
  private Mock mockClaimService = null;
  private Mock mockMailingService = null;
  private Mock mockUserService = null;
  private Mock mockRoleService = null;
  private Mock mockCmAssetService = null;

  protected void setUp() throws Exception
  {
    super.setUp();

    mockPurlRecipientDAO = new Mock( PurlRecipientDAO.class );
    purlService.setPurlRecipientDAO( (PurlRecipientDAO)mockPurlRecipientDAO.proxy() );

    mockPurlContributorDAO = new Mock( PurlContributorDAO.class );
    purlService.setPurlContributorDAO( (PurlContributorDAO)mockPurlContributorDAO.proxy() );

    mockPurlContributorCommentDAO = new Mock( PurlContributorCommentDAO.class );
    purlService.setPurlContributorCommentDAO( (PurlContributorCommentDAO)mockPurlContributorCommentDAO.proxy() );

    mockPurlContributorMediaDAO = new Mock( PurlContributorMediaDAO.class );
    purlService.setPurlContributorMediaDAO( (PurlContributorMediaDAO)mockPurlContributorMediaDAO.proxy() );

    mockActivityDAO = new Mock( ActivityDAO.class );
    purlService.setActivityDAO( (ActivityDAO)mockActivityDAO.proxy() );

    mockMerchOrderDAO = new Mock( MerchOrderDAO.class );
    purlService.setMerchOrderDAO( (MerchOrderDAO)mockMerchOrderDAO.proxy() );

    mockParticipantService = new Mock( ParticipantService.class );
    purlService.setParticipantService( (ParticipantService)mockParticipantService.proxy() );

    mockNodeService = new Mock( NodeService.class );
    purlService.setNodeService( (NodeService)mockNodeService.proxy() );

    mockSystemVariableService = new Mock( SystemVariableService.class );
    purlService.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );

    mockClaimService = new Mock( ClaimService.class );
    purlService.setClaimService( (ClaimService)mockClaimService.proxy() );

    mockMailingService = new Mock( MailingService.class );
    purlService.setMailingService( (MailingService)mockMailingService.proxy() );

    mockRoleService = new Mock( RoleService.class );
    purlService.setRoleService( (RoleService)mockRoleService.proxy() );

    mockUserService = new Mock( UserService.class );
    purlService.setUserService( (UserService)mockUserService.proxy() );

    mockCmAssetService = new Mock( CMAssetService.class );
    purlService.setCmassetService( (CMAssetService)mockCmAssetService.proxy() );
  }

  private static PurlRecipient buildPurlRecipient()
  {
    PurlRecipient purlRecipient = new PurlRecipient();
    purlRecipient.setId( new Long( 1 ) );
    purlRecipient.setInvitationStartDate( new Date() );
    purlRecipient.setAwardDate( new Date( System.currentTimeMillis() + 30 * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY ) );
    purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.INVITATION ) );

    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "PromoName" );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date( System.currentTimeMillis() + 30 * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY ) );
    promotion.setIncludePurl( true );
    purlRecipient.setPromotion( promotion );

    Participant participant = new Participant();
    participant.setId( new Long( 1 ) );
    participant.setUserName( "uName" );
    participant.setFirstName( "fName" );
    participant.setLastName( "lName" );
    participant.setActive( Boolean.TRUE );
    purlRecipient.setUser( participant );

    Node node = new Node();
    node.setId( new Long( 1 ) );
    node.setName( "NodeName" );
    purlRecipient.setNode( node );

    return purlRecipient;
  }

  private static PurlRecipient buildPurlRecipientAwardAmount()
  {
    PurlRecipient purlRecipient = buildPurlRecipient();
    purlRecipient.setAwardAmount( new BigDecimal( 100 ) );
    return purlRecipient;
  }

  private static PurlRecipient buildPurlRecipientAwardLevel()
  {
    PurlRecipient purlRecipient = buildPurlRecipient();

    Country country = new Country();
    country.setId( new Long( 1 ) );
    country.setCountryCode( "cc" );
    country.setCountryName( "CountryName" );
    country.setCampaignNbr( "CampaignNbr" );
    country.setCampaignPassword( "CampaignPass" );
    country.setAwardbanqAbbrev( "abq" );
    country.setDisplayTravelAward( Boolean.FALSE );
    country.setTimeZoneId( TimeZoneId.lookup( TimeZone.getDefault().getID() ) );
    country.setAddressMethod( AddressMethodType.lookup( AddressMethodType.NORTH_AMERICAN ) );
    country.setStatus( CountryStatusType.lookup( CountryStatusType.ACTIVE ) );
    country.setDateStatus( new Date() );

    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setId( new Long( 1 ) );
    promoMerchCountry.setPromotion( purlRecipient.getPromotion() );
    promoMerchCountry.setCountry( country );

    PromoMerchProgramLevel awardLevel = new PromoMerchProgramLevel();
    awardLevel.setId( new Long( 1 ) );
    awardLevel.setPromoMerchCountry( promoMerchCountry );
    awardLevel.setLevelName( "level_name" );
    awardLevel.setCmAssetKey( "cm.asset.key" );
    awardLevel.setMinValue( 10 );
    awardLevel.setMaxValue( 100 );
    awardLevel.setOrdinalPosition( 1 );
    awardLevel.setProgramId( "12345" );

    purlRecipient.setAwardLevel( awardLevel );
    return purlRecipient;
  }

  private static PurlContributor buildPurlContributor()
  {
    PurlContributor purlContributor = new PurlContributor();
    purlContributor.setId( new Long( 1 ) );
    purlContributor.setFirstName( "fName" );
    purlContributor.setLastName( "lName" );
    purlContributor.setState( PurlContributorState.lookup( PurlContributorState.INVITATION ) );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    purlContributor.setPurlRecipient( purlRecipient );

    return purlContributor;
  }

  private static PurlContributor buildPaxPurlContributor()
  {
    PurlContributor purlContributor = buildPurlContributor();

    Participant participant = new Participant();
    participant.setId( new Long( 1 ) );
    participant.setUserName( "uName" );
    participant.setFirstName( "fName" );
    participant.setLastName( "lName" );
    participant.setActive( Boolean.TRUE );
    participant.setLanguageType( LanguageType.lookup( LanguageType.ENGLISH ) );
    purlContributor.setUser( participant );

    return purlContributor;
  }

  private static PurlContributor buildNonPaxPurlContributor()
  {
    PurlContributor purlContributor = buildPurlContributor();
    purlContributor.setEmailAddr( "email@address.com" );
    return purlContributor;
  }

  private static PurlContributorComment buildPurlContributorComment()
  {
    PurlContributorComment comment = new PurlContributorComment();
    comment.setId( new Long( 1 ) );
    comment.setComments( "Comments" );
    comment.setStatus( PurlContributorCommentStatus.lookup( PurlContributorCommentStatus.ACTIVE ) );
    return comment;
  }

  private static PurlContributorMedia buildPurlContributorPhoto()
  {
    PurlContributorMedia media = new PurlContributorMedia();
    media.setId( new Long( 1 ) );
    media.setCaption( "Caption" );
    media.setUrl( "path/to/photo" );
    media.setUrlThumb( "path/to/photo/thumb" );
    media.setState( PurlMediaState.lookup( PurlMediaState.POSTED ) );
    media.setStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
    media.setType( PurlContributorMediaType.lookup( PurlContributorMediaType.PICTURE ) );
    return media;
  }

  private static PurlContributorMedia buildPurlContributorVideo()
  {
    PurlContributorMedia media = new PurlContributorMedia();
    media.setId( new Long( 1 ) );
    media.setCaption( "Caption" );
    media.setUrl( "url.to.video" );
    media.setState( PurlMediaState.lookup( PurlMediaState.POSTED ) );
    media.setStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
    media.setType( PurlContributorMediaType.lookup( PurlContributorMediaType.VIDEO_URL ) );
    return media;
  }

  public void testGetPurlRecipientWithAwardAmountById()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientById" ).with( same( purlRecipient.getId() ) ).will( returnValue( purlRecipient ) );

    PurlRecipient existingPurlRecipient = purlService.getPurlRecipientById( purlRecipient.getId() );
    assertNotNull( existingPurlRecipient.getAwardAmount() );
    assertNull( existingPurlRecipient.getAwardLevel() );

    mockPurlRecipientDAO.verify();
  }

  public void testGetPurlRecipientWithAwardLevelById()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardLevel();

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientById" ).with( same( purlRecipient.getId() ) ).will( returnValue( purlRecipient ) );

    PurlRecipient existingPurlRecipient = purlService.getPurlRecipientById( purlRecipient.getId() );
    assertNull( existingPurlRecipient.getAwardAmount() );
    assertNotNull( existingPurlRecipient.getAwardLevel() );

    mockPurlRecipientDAO.verify();
  }

  public void testSavePurlRecipientWithAwardAmount()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();

    mockPurlRecipientDAO.expects( once() ).method( "save" ).with( same( purlRecipient ) ).will( returnValue( purlRecipient ) );

    PurlRecipient savedPurlRecipient = purlService.savePurlRecipient( purlRecipient );
    assertNotNull( savedPurlRecipient.getAwardAmount() );
    assertNull( savedPurlRecipient.getAwardLevel() );

    mockPurlRecipientDAO.verify();
  }

  public void testSavePurlRecipientWithAwardLevel()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardLevel();

    mockPurlRecipientDAO.expects( once() ).method( "save" ).with( same( purlRecipient ) ).will( returnValue( purlRecipient ) );

    PurlRecipient savedPurlRecipient = purlService.savePurlRecipient( purlRecipient );
    assertNull( savedPurlRecipient.getAwardAmount() );
    assertNotNull( savedPurlRecipient.getAwardLevel() );

    mockPurlRecipientDAO.verify();
  }

  public void testDeletePurlRecipient()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();

    mockPurlRecipientDAO.expects( once() ).method( "delete" ).with( same( purlRecipient ) );

    purlService.deletePurlRecipient( purlRecipient );

    mockPurlRecipientDAO.verify();
  }

  public void testGetPaxPurlContributorById()
  {
    PurlContributor purlContributor = buildPaxPurlContributor();

    mockPurlContributorDAO.expects( once() ).method( "getPurlContributorById" ).with( same( purlContributor.getId() ) ).will( returnValue( purlContributor ) );

    PurlContributor existingPurlContributor = purlService.getPurlContributorById( purlContributor.getId() );
    assertNotNull( existingPurlContributor.getUser() );
    assertNull( existingPurlContributor.getEmailAddr() );

    mockPurlContributorDAO.verify();
  }

  public void testGetNonPaxPurlContributorById()
  {
    PurlContributor purlContributor = buildNonPaxPurlContributor();

    mockPurlContributorDAO.expects( once() ).method( "getPurlContributorById" ).with( same( purlContributor.getId() ) ).will( returnValue( purlContributor ) );

    PurlContributor existingPurlContributor = purlService.getPurlContributorById( purlContributor.getId() );
    assertNull( existingPurlContributor.getUser() );
    assertNotNull( existingPurlContributor.getEmailAddr() );

    mockPurlContributorDAO.verify();
  }

  public void testSavePaxPurlContributor()
  {
    PurlContributor purlContributor = buildPaxPurlContributor();

    mockPurlContributorDAO.expects( once() ).method( "save" ).with( same( purlContributor ) ).will( returnValue( purlContributor ) );

    PurlContributor savedPurlContributor = purlService.savePurlContributor( purlContributor );
    assertNotNull( savedPurlContributor.getUser() );
    assertNull( savedPurlContributor.getEmailAddr() );

    mockPurlContributorDAO.verify();
  }

  public void testSaveNonPaxPurlContributor()
  {
    PurlContributor purlContributor = buildNonPaxPurlContributor();

    mockPurlContributorDAO.expects( once() ).method( "save" ).with( same( purlContributor ) ).will( returnValue( purlContributor ) );

    PurlContributor savedPurlContributor = purlService.savePurlContributor( purlContributor );
    assertNull( savedPurlContributor.getUser() );
    assertNotNull( savedPurlContributor.getEmailAddr() );

    mockPurlContributorDAO.verify();
  }

  public void testDeletePurlContributor()
  {
    PurlContributor purlContributor = buildPaxPurlContributor();

    mockPurlContributorDAO.expects( once() ).method( "delete" ).with( same( purlContributor ) );

    purlService.deletePurlContributor( purlContributor );

    mockPurlContributorDAO.verify();
  }

  public void testGetPurlContributorByUserId()
  {
    Long userId = new Long( 1 );
    Long purlRecipientId = new Long( 1 );

    PurlContributor purlContributor = buildPaxPurlContributor();
    List<PurlContributor> list = new ArrayList<PurlContributor>();
    list.add( purlContributor );

    mockPurlContributorDAO.expects( once() ).method( "getContributors" ).will( returnValue( list ) );

    PurlContributor existingPurlContributor = purlService.getPurlContributorByUserId( userId, purlRecipientId );
    assertNotNull( existingPurlContributor );

    mockPurlContributorDAO.verify();
  }

  public void testGetAllPurlInvitationsForManager()
  {
    Long promotionId = new Long( 1 );
    Long managerId = new Long( 2 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockNodeService.expects( once() ).method( "findOwnerNodeIds" ).with( same( managerId ) ).will( returnValue( new ArrayList<Long>() ) );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    Participant nodeOwner = new Participant();
    nodeOwner.setId( managerId );
    nodeOwner.setUserName( "uName" );
    nodeOwner.setFirstName( "fName" );
    nodeOwner.setLastName( "lName" );
    nodeOwner.setActive( Boolean.TRUE );

    mockParticipantService.expects( once() ).method( "getNodeOwner" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( null ) );

    mockNodeService.expects( once() ).method( "getNodeOwnerByLevel" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( nodeOwner ) );

    List<PurlRecipient> purlRecipients = purlService.getAllPurlInvitationsForManager( managerId, promotionId );
    assertTrue( !purlRecipients.isEmpty() );

    mockPurlRecipientDAO.verify();
    mockParticipantService.verify();
    mockNodeService.verify();
  }

  public void testGetAllPendingPurlInvitationsForPromotion()
  {
    Long promotionId = new Long( 1 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockPurlContributorDAO.expects( once() ).method( "getContributors" ).withAnyArguments().will( returnValue( new ArrayList() ) );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    Participant nodeOwner = new Participant();
    nodeOwner.setId( new Long( 1 ) );
    nodeOwner.setUserName( "uName" );
    nodeOwner.setFirstName( "fName" );
    nodeOwner.setLastName( "lName" );
    nodeOwner.setActive( Boolean.TRUE );

    List<PurlRecipient> purlRecipients = purlService.getAllPendingPurlInvitations( promotionId );
    assertTrue( !purlRecipients.isEmpty() );

    mockPurlRecipientDAO.verify();
  }

  public void testGetAllPendingPurlInvitationsForManager()
  {
    Long promotionId = new Long( 1 );
    Long managerId = new Long( 2 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockPurlContributorDAO.expects( once() ).method( "getContributors" ).withAnyArguments().will( returnValue( new ArrayList() ) );

    mockNodeService.expects( once() ).method( "findOwnerNodeIds" ).with( same( managerId ) ).will( returnValue( new ArrayList<Long>() ) );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    Participant nodeOwner = new Participant();
    nodeOwner.setId( managerId );
    nodeOwner.setUserName( "uName" );
    nodeOwner.setFirstName( "fName" );
    nodeOwner.setLastName( "lName" );
    nodeOwner.setActive( Boolean.TRUE );

    mockParticipantService.expects( once() ).method( "getNodeOwner" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( null ) );

    mockNodeService.expects( once() ).method( "getNodeOwnerByLevel" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( nodeOwner ) );

    List<PurlRecipient> purlRecipients = purlService.getAllPendingPurlInvitationsForManager( managerId, promotionId );
    assertTrue( !purlRecipients.isEmpty() );

    mockPurlRecipientDAO.verify();
    mockParticipantService.verify();
    mockNodeService.verify();
  }

  public void testGetAllCurrentPurlInvitationsForManager()
  {
    Long promotionId = new Long( 1 );
    Long managerId = new Long( 2 );

    PurlContributor purlContributor = buildPaxPurlContributor();
    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    purlRecipient.addContributor( purlContributor );
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockNodeService.expects( once() ).method( "findOwnerNodeIds" ).with( same( managerId ) ).will( returnValue( new ArrayList<Long>() ) );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    Participant nodeOwner = new Participant();
    nodeOwner.setId( managerId );
    nodeOwner.setUserName( "uName" );
    nodeOwner.setFirstName( "fName" );
    nodeOwner.setLastName( "lName" );
    nodeOwner.setActive( Boolean.TRUE );

    mockParticipantService.expects( once() ).method( "getNodeOwner" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( null ) );

    mockNodeService.expects( once() ).method( "getNodeOwnerByLevel" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( nodeOwner ) );

    List<PurlRecipient> purlRecipients = purlService.getAllCurrentPurlInvitationsForManager( managerId, promotionId );
    assertTrue( !purlRecipients.isEmpty() );

    mockPurlRecipientDAO.verify();
    mockParticipantService.verify();
    mockNodeService.verify();
  }

  public void testGetAllPurlContributions()
  {
    Long userId = new Long( 1 );
    Long promotionId = new Long( 1 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    PurlContributor purlContributor = buildPaxPurlContributor();
    purlContributor.setPurlRecipient( purlRecipient );
    List<PurlContributor> list = new ArrayList<PurlContributor>();
    list.add( purlContributor );

    mockPurlContributorDAO.expects( once() ).method( "getContributors" ).will( returnValue( list ) );

    List<PurlContributor> purlContributors = purlService.getAllPurlContributions( userId, promotionId, true );
    assertTrue( !purlContributors.isEmpty() );

    mockPurlContributorDAO.verify();
  }

  public void testGetAllPendingPurlContributionsForPromotion()
  {
    Long promotionId = new Long( 1 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    PurlContributor purlContributor = buildPaxPurlContributor();
    purlContributor.setPurlRecipient( purlRecipient );
    List<PurlContributor> list = new ArrayList<PurlContributor>();
    list.add( purlContributor );

    mockPurlContributorDAO.expects( once() ).method( "getContributors" ).will( returnValue( list ) );

    List<PurlContributor> purlContributors = purlService.getAllPendingPurlContributions( promotionId );
    assertTrue( !purlContributors.isEmpty() );

    mockPurlContributorDAO.verify();
  }

  public void testGetAllPendingPurlContributions()
  {
    Long userId = new Long( 1 );
    Long promotionId = new Long( 1 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    PurlContributor purlContributor = buildPaxPurlContributor();
    purlContributor.setPurlRecipient( purlRecipient );
    List<PurlContributor> list = new ArrayList<PurlContributor>();
    list.add( purlContributor );

    mockPurlContributorDAO.expects( once() ).method( "getContributors" ).will( returnValue( list ) );

    List<PurlContributor> purlContributors = purlService.getAllPendingPurlContributions( userId, promotionId );
    assertTrue( !purlContributors.isEmpty() );

    mockPurlContributorDAO.verify();
  }

  public void testGetAllCurrentPurlContributions()
  {
    Long userId = new Long( 1 );
    Long promotionId = new Long( 1 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    PurlContributorComment purlContributorComment = buildPurlContributorComment();
    PurlContributor purlContributor = buildPaxPurlContributor();
    purlContributor.setPurlRecipient( purlRecipient );
    purlContributor.addComment( purlContributorComment );
    List<PurlContributor> list = new ArrayList<PurlContributor>();
    list.add( purlContributor );

    mockPurlContributorDAO.expects( once() ).method( "getContributors" ).will( returnValue( list ) );

    List<PurlContributor> purlContributors = purlService.getAllCurrentPurlContributions( userId, promotionId, false );
    assertTrue( !purlContributors.isEmpty() );

    mockPurlContributorDAO.verify();
  }

  public void testGetPurlRecipientByUserId()
  {
    PurlRecipient purlRecipient = buildPurlRecipient();
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientByUserId" ).with( same( purlRecipient.getUser().getId() ) ).will( returnValue( list ) );

    List<PurlRecipient> purlRecipients = purlService.getPurlRecipientByUserId( purlRecipient.getUser().getId() );
    assertTrue( !purlRecipients.isEmpty() );

    mockPurlRecipientDAO.verify();

  }

  public void testGetComments()
  {
    Long purlRecipientId = new Long( 1 );

    PurlContributorComment purlContributorComment = buildPurlContributorComment();
    List<PurlContributorComment> list = new ArrayList<PurlContributorComment>();
    list.add( purlContributorComment );

    mockPurlContributorCommentDAO.expects( once() ).method( "getComments" ).will( returnValue( list ) );

    List<PurlContributorComment> purlContributorComments = purlService.getComments( purlRecipientId );
    assertTrue( !purlContributorComments.isEmpty() );

    mockPurlContributorCommentDAO.verify();
  }

  public void testGetPhotos()
  {
    Long purlRecipientId = new Long( 1 );

    PurlContributorMedia purlContributorMedia = buildPurlContributorPhoto();
    List<PurlContributorMedia> list = new ArrayList<PurlContributorMedia>();
    list.add( purlContributorMedia );

    mockPurlContributorMediaDAO.expects( once() ).method( "getMediaUploads" ).will( returnValue( list ) );

    List<PurlContributorMedia> purlContributorPhotos = purlService.getPhotoUploads( purlRecipientId );
    assertTrue( !purlContributorPhotos.isEmpty() );

    mockPurlContributorMediaDAO.verify();
  }

  public void testGetVideos()
  {
    Long purlRecipientId = new Long( 1 );

    PurlContributorMedia purlContributorMedia = buildPurlContributorVideo();
    List<PurlContributorMedia> list = new ArrayList<PurlContributorMedia>();
    list.add( purlContributorMedia );

    mockPurlContributorMediaDAO.expects( once() ).method( "getMediaUploads" ).will( returnValue( list ) );

    List<PurlContributorMedia> purlContributorVideos = purlService.getVideoUploads( purlRecipientId );
    assertTrue( !purlContributorVideos.isEmpty() );

    mockPurlContributorMediaDAO.verify();
  }

  public void testGetPurlContributorMediaById()
  {
    PurlContributorMedia purlContributorMedia = buildPurlContributorVideo();

    mockPurlContributorMediaDAO.expects( once() ).method( "getPurlContributorMediaById" ).with( same( purlContributorMedia.getId() ) ).will( returnValue( purlContributorMedia ) );

    PurlContributorMedia existingPurlContributorMedia = purlService.getPurlContributorMediaById( purlContributorMedia.getId() );
    assertNotNull( existingPurlContributorMedia );

    mockPurlContributorMediaDAO.verify();
  }

  public void testGetPurlContributorCommentById()
  {
    PurlContributorComment purlContributorComment = buildPurlContributorComment();

    mockPurlContributorCommentDAO.expects( once() ).method( "getPurlContributorCommentById" ).with( same( purlContributorComment.getId() ) ).will( returnValue( purlContributorComment ) );

    PurlContributorComment existingPurlContributorComment = purlService.getPurlContributorCommentById( purlContributorComment.getId() );
    assertNotNull( existingPurlContributorComment );

    mockPurlContributorCommentDAO.verify();
  }

  public void testSavePurlContributorMedia()
  {
    PurlContributorMedia purlContributorMedia = buildPurlContributorVideo();

    mockPurlContributorMediaDAO.expects( once() ).method( "save" ).with( same( purlContributorMedia ) ).will( returnValue( purlContributorMedia ) );

    PurlContributorMedia savedMedia = purlService.savePurlContributorMedia( purlContributorMedia );
    assertNotNull( savedMedia );

    mockPurlContributorMediaDAO.verify();
  }

  public void testSavePurlContributorComment()
  {
    PurlContributorComment purlContributorComment = buildPurlContributorComment();

    mockPurlContributorCommentDAO.expects( once() ).method( "save" ).with( same( purlContributorComment ) ).will( returnValue( purlContributorComment ) );

    PurlContributorComment savedComment = purlService.savePurlContributorComment( purlContributorComment );
    assertNotNull( savedComment );

    mockPurlContributorCommentDAO.verify();
  }

  public void testGetAllCompletedPurlRecipients()
  {
    Long promotionId = new Long( 1 );
    Long userId = new Long( 1 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.COMPLETE ) );
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    PropertySetItem systemVariableDaysToExpire = new PropertySetItem();
    systemVariableDaysToExpire.setIntVal( 10 );
    systemVariableDaysToExpire.setKey( SystemVariableService.PURL_DAYS_TO_EXP );
    systemVariableDaysToExpire.setEntityName( SystemVariableService.PURL_DAYS_TO_EXP );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PURL_DAYS_TO_EXP ) ).will( returnValue( systemVariableDaysToExpire ) );

    List<PurlRecipient> purlRecipients = purlService.getAllCompletedPurlRecipients( userId, promotionId );
    assertTrue( !purlRecipients.isEmpty() );

    mockPurlRecipientDAO.verify();
    mockSystemVariableService.verify();
  }

  public void testGetAllPendingPurlRecipients()
  {
    Long promotionId = new Long( 1 );
    Long userId = new Long( 1 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.RECOGNITION ) );
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    PropertySetItem systemVariableDaysToExpire = new PropertySetItem();
    systemVariableDaysToExpire.setIntVal( 10 );
    systemVariableDaysToExpire.setKey( SystemVariableService.PURL_DAYS_TO_EXP );
    systemVariableDaysToExpire.setEntityName( SystemVariableService.PURL_DAYS_TO_EXP );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PURL_DAYS_TO_EXP ) ).will( returnValue( systemVariableDaysToExpire ) );

    List<PurlRecipient> purlRecipients = purlService.getAllCompletedPurlRecipients( userId, promotionId );
    assertTrue( !purlRecipients.isEmpty() );

    mockPurlRecipientDAO.verify();
    mockSystemVariableService.verify();
  }

  public void testGetAllCurrentPurlRecipients()
  {
    Long promotionId = new Long( 1 );
    Long userId = new Long( 1 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.COMPLETE ) );
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    PropertySetItem systemVariableDaysToExpire = new PropertySetItem();
    systemVariableDaysToExpire.setIntVal( 10 );
    systemVariableDaysToExpire.setKey( SystemVariableService.PURL_DAYS_TO_EXP );
    systemVariableDaysToExpire.setEntityName( SystemVariableService.PURL_DAYS_TO_EXP );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PURL_DAYS_TO_EXP ) ).will( returnValue( systemVariableDaysToExpire ) );

    List<PurlRecipient> purlRecipients = purlService.getAllCompletedPurlRecipients( userId, promotionId );
    assertTrue( !purlRecipients.isEmpty() );

    mockPurlRecipientDAO.verify();
    mockSystemVariableService.verify();
  }

  public void testGetGiftcodeForRecipient()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    RecognitionClaim claim = new RecognitionClaim();
    claim.setId( new Long( 1 ) );
    purlRecipient.setClaim( claim );

    MerchOrder merchOrder = new MerchOrder();
    merchOrder.setId( new Long( 1 ) );
    merchOrder.setGiftCode( "12345678" );
    merchOrder.setGiftCodeKey( "12345678" );

    MerchOrderActivity activity = new MerchOrderActivity();
    activity.setMerchOrder( merchOrder );

    List<MerchOrderActivity> activities = new ArrayList<MerchOrderActivity>();
    activities.add( activity );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientById" ).will( returnValue( purlRecipient ) );

    mockActivityDAO.expects( once() ).method( "getMerchOrderActivitiesByClaimAndUserId" ).will( returnValue( activities ) );

    mockMerchOrderDAO.expects( once() ).method( "getMerchOrderById" ).will( returnValue( merchOrder ) );

    String giftCode = purlService.getGiftcodeForRecipient( purlRecipient.getId() );
    assertNotNull( giftCode );

    mockPurlRecipientDAO.verify();
    mockActivityDAO.verify();
    mockMerchOrderDAO.verify();
  }

  public void testUpdatePurlContributorName()
  {
    PurlContributor purlContributor = buildPaxPurlContributor();

    mockPurlContributorDAO.expects( once() ).method( "getPurlContributorById" ).with( same( purlContributor.getId() ) ).will( returnValue( purlContributor ) );

    mockPurlContributorDAO.expects( once() ).method( "save" ).with( same( purlContributor ) ).will( returnValue( purlContributor ) );

    PurlContributor updatedPurlContributor = purlService.updatePurlContributorName( purlContributor );
    assertEquals( updatedPurlContributor.getFirstName(), purlContributor.getFirstName() );
    assertEquals( updatedPurlContributor.getLastName(), purlContributor.getLastName() );

    mockPurlContributorDAO.verify();
  }

  public void testGetAllPurlRecipientsForAwardIssuance()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    List<PurlRecipient> purlRecipients = purlService.getAllPurlRecipientsForAwardIssuance();
    assertTrue( !purlRecipients.isEmpty() );

    mockPurlRecipientDAO.verify();
  }

  public void testGetAllPurlRecipientsToExpire()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    PropertySetItem systemVariableDaysToExpire = new PropertySetItem();
    systemVariableDaysToExpire.setIntVal( 60 );
    systemVariableDaysToExpire.setKey( SystemVariableService.PURL_DAYS_TO_EXP );
    systemVariableDaysToExpire.setEntityName( SystemVariableService.PURL_DAYS_TO_EXP );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PURL_DAYS_TO_EXP ) ).will( returnValue( systemVariableDaysToExpire ) );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    List<PurlRecipient> purlRecipients = purlService.getAllPurlRecipientsToExpire();
    assertTrue( !purlRecipients.isEmpty() );

    mockSystemVariableService.verify();
    mockPurlRecipientDAO.verify();
  }

  public void testGetAllPurlRecipientsToArchive()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    PropertySetItem systemVariableDaysToExpire = new PropertySetItem();
    systemVariableDaysToExpire.setIntVal( 60 );
    systemVariableDaysToExpire.setKey( SystemVariableService.PURL_DAYS_TO_EXP );
    systemVariableDaysToExpire.setEntityName( SystemVariableService.PURL_DAYS_TO_EXP );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PURL_DAYS_TO_EXP ) ).will( returnValue( systemVariableDaysToExpire ) );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    List<PurlRecipient> purlRecipients = purlService.getAllPurlRecipientsToArchive();
    assertTrue( !purlRecipients.isEmpty() );

    mockSystemVariableService.verify();
    mockPurlRecipientDAO.verify();
  }

  public void testGetEligiblePurlPromotionsForInvitation()
  {
    Long userId = new Long( 2 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockNodeService.expects( once() ).method( "findOwnerNodeIds" ).with( same( userId ) ).will( returnValue( new ArrayList<Long>() ) );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    Participant nodeOwner = new Participant();
    nodeOwner.setId( new Long( 2 ) );
    nodeOwner.setUserName( "uName" );
    nodeOwner.setFirstName( "fName" );
    nodeOwner.setLastName( "lName" );
    nodeOwner.setActive( Boolean.TRUE );

    mockParticipantService.expects( once() ).method( "getNodeOwner" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( null ) );

    mockNodeService.expects( once() ).method( "getNodeOwnerByLevel" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( nodeOwner ) );

    List<Promotion> eligiblePromotions = purlService.getEligiblePurlPromotionsForInvitation( userId );
    assertTrue( !eligiblePromotions.isEmpty() );

    mockPurlRecipientDAO.verify();
    mockParticipantService.verify();
    mockNodeService.verify();
  }

  public void testGetEligiblePurlPromotionsForContributor()
  {
    Long userId = new Long( 1 );

    PurlContributor purlContributor = buildPaxPurlContributor();
    List<PurlContributor> list = new ArrayList<PurlContributor>();
    list.add( purlContributor );

    mockPurlContributorDAO.expects( once() ).method( "getContributors" ).will( returnValue( list ) );

    List<Promotion> eligiblePromotions = purlService.getEligiblePurlPromotionsForContributor( userId );
    assertTrue( !eligiblePromotions.isEmpty() );

    mockPurlContributorDAO.verify();
  }

  public void testGetEligiblePurlPromotionsForRecipient()
  {
    Long userId = new Long( 1 );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    List<PurlRecipient> list = new ArrayList<PurlRecipient>();
    list.add( purlRecipient );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientList" ).will( returnValue( list ) );

    List<Promotion> eligiblePromotions = purlService.getEligiblePurlPromotionsForRecipient( userId );
    assertTrue( !eligiblePromotions.isEmpty() );

    mockPurlRecipientDAO.verify();
  }

  public void testIsValidForContribution()
  {
    PurlContributor purlContributor = buildPaxPurlContributor();

    mockPurlContributorDAO.expects( once() ).method( "getPurlContributorById" ).with( same( purlContributor.getId() ) ).will( returnValue( purlContributor ) );

    boolean status = purlService.isValidForContribution( purlContributor.getId() );
    assertTrue( status );

    mockPurlContributorDAO.verify();
  }

  public void testGetNodeOwnerForPurlRecipient()
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientById" ).with( same( purlRecipient.getId() ) ).will( returnValue( purlRecipient ) );

    Participant nodeOwner = new Participant();
    nodeOwner.setId( new Long( 2 ) );
    nodeOwner.setUserName( "uName" );
    nodeOwner.setFirstName( "fName" );
    nodeOwner.setLastName( "lName" );
    nodeOwner.setActive( Boolean.TRUE );

    mockParticipantService.expects( once() ).method( "getNodeOwner" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( null ) );

    mockNodeService.expects( once() ).method( "getNodeOwnerByLevel" ).with( same( purlRecipient.getNode().getId() ) ).will( returnValue( nodeOwner ) );

    Participant nodeOwnerForPurlRecipient = purlService.getNodeOwnerForPurlRecipient( purlRecipient.getId() );
    assertNotNull( nodeOwnerForPurlRecipient );

    mockPurlRecipientDAO.verify();
    mockParticipantService.verify();
    mockNodeService.verify();
  }

  public void testProcessAwardStandard() throws ServiceErrorException
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardLevel();
    purlRecipient.setSubmitter( BuilderUtil.buildParticipant() );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientById" ).with( same( purlRecipient.getId() ) ).will( returnValue( purlRecipient ) );

    Participant nodeOwner = new Participant();
    nodeOwner.setId( new Long( 1 ) );
    nodeOwner.setUserName( "uName" );
    nodeOwner.setFirstName( "fName" );
    nodeOwner.setLastName( "lName" );
    nodeOwner.setActive( Boolean.TRUE );

    mockUserService.expects( once() ).method( "getPreferredLanguageFor" ).withAnyArguments().will( returnValue( LanguageType.lookup( LanguageType.ENGLISH ) ) );

    RecognitionClaim claim = new RecognitionClaim();
    claim.setId( new Long( 1 ) );

    mockClaimService.expects( once() ).method( "getNextTeamId" ).withNoArguments().will( returnValue( new Long( 1000 ) ) );

    mockClaimService.expects( once() ).method( "saveClaim" );

    mockPurlRecipientDAO.expects( once() ).method( "save" ).with( same( purlRecipient ) ).will( returnValue( purlRecipient ) );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientById" ).with( same( purlRecipient.getId() ) ).will( returnValue( purlRecipient ) );

    mockPurlRecipientDAO.expects( once() ).method( "save" ).with( same( purlRecipient ) ).will( returnValue( purlRecipient ) );

    purlService.processAward( purlRecipient.getId() );

    mockPurlRecipientDAO.verify();
    mockParticipantService.verify();
    mockNodeService.verify();
  }

  public void testProcessAwardPurl() throws ServiceErrorException
  {
    PurlContributorComment purlContributorComment = buildPurlContributorComment();

    PurlContributor purlContributor = buildPaxPurlContributor();
    purlContributor.addComment( purlContributorComment );

    PurlRecipient purlRecipient = buildPurlRecipientAwardAmount();
    purlRecipient.addContributor( purlContributor );
    purlRecipient.setSubmitter( BuilderUtil.buildParticipant() );

    PromotionNotificationType promotionNotification = new PromotionNotificationType();
    promotionNotification.setNotificationMessageId( 123 );
    promotionNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION ) );

    List promotionNotifications = new ArrayList();
    promotionNotifications.add( promotionNotification );

    purlRecipient.getPromotion().setPromotionNotifications( promotionNotifications );

    mockUserService.expects( once() ).method( "getPreferredLanguageFor" ).withAnyArguments().will( returnValue( LanguageType.lookup( LanguageType.ENGLISH ) ) );

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientById" ).with( same( purlRecipient.getId() ) ).will( returnValue( purlRecipient ) );

    Participant nodeOwner = new Participant();
    nodeOwner.setId( new Long( 1 ) );
    nodeOwner.setUserName( "uName" );
    nodeOwner.setFirstName( "fName" );
    nodeOwner.setLastName( "lName" );
    nodeOwner.setActive( Boolean.TRUE );

    RecognitionClaim claim = new RecognitionClaim();
    claim.setId( new Long( 1 ) );

    mockClaimService.expects( once() ).method( "getNextTeamId" ).withNoArguments().will( returnValue( new Long( 1000 ) ) );

    mockClaimService.expects( once() ).method( "saveClaim" );

    mockPurlRecipientDAO.expects( once() ).method( "save" ).with( same( purlRecipient ) ).will( returnValue( purlRecipient ) );

    mockPurlRecipientDAO.expects( once() ).method( "save" ).with( same( purlRecipient ) ).will( returnValue( purlRecipient ) );

    mockPurlContributorDAO.expects( once() ).method( "getPurlContributorById" ).with( same( purlContributor.getId() ) ).will( returnValue( purlContributor ) );

    mockPurlContributorDAO.expects( once() ).method( "save" ).with( same( purlContributor ) ).will( returnValue( purlContributor ) );

    Mailing mailing = new Mailing();
    mockMailingService.expects( once() ).method( "buildPurlRecipientInvitationMailing" ).will( returnValue( mailing ) );
    mockMailingService.expects( once() ).method( "submitMailing" );

    purlService.processAward( purlRecipient.getId() );

    mockPurlRecipientDAO.verify();
    mockParticipantService.verify();
    mockNodeService.verify();
    mockClaimService.verify();
    mockPurlContributorDAO.verify();
    mockMailingService.verify();
  }

  public void testProcessExpirePurl() throws ServiceErrorException
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardLevel();

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientById" ).with( same( purlRecipient.getId() ) ).will( returnValue( purlRecipient ) );

    mockPurlRecipientDAO.expects( once() ).method( "save" ).with( same( purlRecipient ) ).will( returnValue( purlRecipient ) );

    purlService.processExpirePurl( purlRecipient.getId() );

    mockPurlRecipientDAO.verify();
  }

  public void testProcessArchivePurl() throws ServiceErrorException
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardLevel();

    mockPurlRecipientDAO.expects( once() ).method( "getPurlRecipientById" ).with( same( purlRecipient.getId() ) ).will( returnValue( purlRecipient ) );

    mockPurlRecipientDAO.expects( once() ).method( "save" ).with( same( purlRecipient ) ).will( returnValue( purlRecipient ) );

    purlService.processExpirePurl( purlRecipient.getId() );

    mockPurlRecipientDAO.verify();
  }

  public void testSendThankyouToContributors() throws ServiceErrorException
  {
    PurlRecipient purlRecipient = buildPurlRecipientAwardLevel();

    PurlContributorComment purlContributorComment = buildPurlContributorComment();
    PurlContributor purlContributor = buildNonPaxPurlContributor();
    purlContributor.addComment( purlContributorComment );
    List<PurlContributor> list = new ArrayList<PurlContributor>();
    list.add( purlContributor );

    mockPurlContributorDAO.expects( once() ).method( "getContributors" ).will( returnValue( list ) );

    PropertySetItem systemVariableEmailAddr = new PropertySetItem();
    systemVariableEmailAddr.setStringVal( "system@domain.com" );
    systemVariableEmailAddr.setKey( SystemVariableService.SYSTEM_EMAIL_ADDRESS );
    systemVariableEmailAddr.setEntityName( SystemVariableService.SYSTEM_EMAIL_ADDRESS );

    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.SYSTEM_EMAIL_ADDRESS ) ).will( returnValue( systemVariableEmailAddr ) );

    Mailing mailing = new Mailing();
    mockMailingService.expects( once() ).method( "submitMailing" ).will( returnValue( mailing ) );

    purlService.sendThankyouToContributors( purlRecipient.getId(), "subject", "comments" );

    mockPurlContributorDAO.verify();
    mockSystemVariableService.verify();
    mockMailingService.verify();
  }

  public void testPostCommentForContributor()
  {

    try
    {
      PurlContributorComment purlContributorComment = buildPurlContributorComment();
      PurlContributor purlContributor = buildPaxPurlContributor();
      mockParticipantService.expects( once() ).method( "getParticipantById" ).withAnyArguments().will( returnValue( purlContributor.getUser() ) );
      mockPurlRecipientDAO.expects( once() ).method( "save" ).withAnyArguments();
      mockPurlContributorDAO.expects( once() ).method( "getPurlContributorById" ).will( returnValue( purlContributor ) );
      mockPurlContributorDAO.expects( once() ).method( "save" ).with( same( purlContributor ) ).will( returnValue( purlContributor ) );
      mockUserService.expects( once() ).method( "getPreferredLanguageFor" ).will( returnValue( purlContributor.getUser().getLanguageType() ) );
      mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PURL_COMMENT_SIZE_LIMIT ) ).will( returnValue( BuilderUtil.buildPropertySetItem() ) );
      PurlContributorComment savedComment = purlService.postCommentForContributor( purlContributor.getId(), purlContributorComment );
      assertNotNull( savedComment );
      mockPurlContributorDAO.verify();
    }
    catch( Exception e )
    {
      fail( e.getMessage() );
    }
  }

  @Test
  public void testPostInvalidLengthCommentForContributor()
  {
    try
    {
      PurlContributorComment purlContributorComment = buildPurlContributorComment();
      purlContributorComment.setComments( RandomStringUtils.random( 501 ) );
      PurlContributor purlContributor = buildPaxPurlContributor();

      PropertySetItem prop = new PropertySetItem();
      prop.setIntVal( 500 );

      mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.PURL_COMMENT_SIZE_LIMIT ) ).will( returnValue( prop ) );
      mockCmAssetService.expects( once() ).method( "getTextFromCmsResourceBundle" ).will( returnValue( "error message" ) );

      purlService.postCommentForContributor( purlContributor.getId(), purlContributorComment );
      fail( "Message length check failed to throw exception" );
    }
    catch( PurlContributorCommentValidationException e )
    {
      mockSystemVariableService.verify();
      // success!
    }
    catch( ServiceErrorException e )
    {
      fail();
    }
  }

  public void testPostMediaForContributor()
  {
    PurlMediaUploadValue mediaVideoUrl = new PurlMediaUploadValue();
    mediaVideoUrl.setMedia( PurlContributorMediaType.VIDEO_URL );
    mediaVideoUrl.setCaption( "caption" );
    mediaVideoUrl.setFull( "url/to/video" );

    PurlContributor purlContributor = buildPaxPurlContributor();

    mockPurlContributorDAO.expects( once() ).method( "getPurlContributorById" ).with( same( purlContributor.getId() ) ).will( returnValue( purlContributor ) );

    mockPurlContributorDAO.expects( once() ).method( "save" ).with( same( purlContributor ) ).will( returnValue( purlContributor ) );

    PurlMediaUploadValue resultMedia = purlService.postMediaForContributor( purlContributor.getId(), mediaVideoUrl );
    assertNotNull( resultMedia );

    mockPurlContributorDAO.verify();
  }

  /*
   * public void testGetUpcomingPurlRecipients() { { Long promotionId = new Long( 1 ); PurlRecipient
   * purlRecipient = buildPurlRecipient(); purlRecipient.setState( PurlRecipientState.lookup(
   * PurlRecipientState.CONTRIBUTION ) ); List<PurlRecipient> list = new ArrayList<PurlRecipient>();
   * list.add( purlRecipient ); AuthenticatedUser user = new AuthenticatedUser(); user.setUserId(
   * new Long( 2 ) ); user.setUsername( "u2Name" ); UserManager.setUser( user ); int count = 100;
   * String sortBy = "awardDate"; int startIndex = 0; mockPurlRecipientDAO.expects( once() ).method(
   * "getUpcomingPurlRecipients" ).with( same( promotionId ), same( count ), same( sortBy ), same(
   * startIndex ) ).will( returnValue( list ) ); List<PurlRecipient> purlRecipients =
   * purlService.getUpcomingPurlRecipients( promotionId, count, sortBy, startIndex ); assertTrue(
   * !purlRecipients.isEmpty() ); assertEquals( 1, purlRecipients.size() );
   * mockPurlRecipientDAO.verify(); } }
   *//**
    * Test to remove the logged in user from the PURL recipients list
    *//*
       * public void testGetUpcomingPurlRecipientsRemoveSelf() { { List<PurlRecipient> list = new
       * ArrayList<PurlRecipient>(); Long promotionId = new Long( 1 ); PurlRecipient purlRecipient =
       * buildPurlRecipient(); purlRecipient.setState( PurlRecipientState.lookup(
       * PurlRecipientState.CONTRIBUTION ) ); list.add( purlRecipient ); PurlRecipient
       * purlRecipient2 = buildPurlRecipient(); Participant participant = new Participant();
       * participant.setId( new Long( 2 ) ); participant.setUserName( "u2Name" );
       * participant.setFirstName( "f2Name" ); participant.setLastName( "l2Name" );
       * participant.setActive( Boolean.TRUE ); purlRecipient2.setUser( participant );
       * purlRecipient2.setState( PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) );
       * list.add( purlRecipient2 ); AuthenticatedUser user = new AuthenticatedUser();
       * user.setUserId( new Long( 2 ) ); user.setUsername( "u2Name" ); UserManager.setUser( user );
       * int count = 100; String sortBy = "awardDate"; int startIndex = 0;
       * mockPurlRecipientDAO.expects( once() ).method( "getUpcomingPurlRecipients" ).with( same(
       * promotionId ), same( count ), same( sortBy ), same( startIndex ) ).will( returnValue( list
       * ) ); List<PurlRecipient> purlRecipients = purlService.getUpcomingPurlRecipients(
       * promotionId, count, sortBy, startIndex ); assertTrue( !purlRecipients.isEmpty() );
       * assertEquals( 1, purlRecipients.size() ); mockPurlRecipientDAO.verify(); } }
       */

  public void testSendPurlContributionEmail()
  {

    Role role = new Role();
    role.setId( new Long( 6 ) );
    role.setActive( Boolean.valueOf( true ) );
    role.setHelpText( "BI_ADMIN" );

    List<Long> userIds = new ArrayList<Long>();
    userIds.add( new Long( 1 ) );

    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "ServiceTestUserName" );
    user.setFirstName( "ServiceTestTestFirstName" );
    user.setLastName( "ServiceTestLastName" );
    user.setMasterUserId( new Long( 1 ) );
    user.setPassword( "ServiceTestPassword" );
    user.setActive( Boolean.TRUE );

    mockParticipantService.expects( once() ).method( "getNodeOwner" ).will( returnValue( null ) );
    mockRoleService.expects( once() ).method( "getRoleByCode" ).will( returnValue( role ) );
    mockRoleService.expects( once() ).method( "getBiAdminUserIdsByRoleId" ).will( returnValue( userIds ) );
    mockUserService.expects( atLeastOnce() ).method( "getUserById" ).will( returnValue( user ) );

    Mailing mailing = new Mailing();
    mockMailingService.expects( once() ).method( "buildPurlContributorNotificationMailing" ).will( returnValue( mailing ) );
    mockMailingService.expects( once() ).method( "submitMailing" );

    PromotionNotificationType promotionNotificationOne = new PromotionNotificationType();
    promotionNotificationOne.setNotificationMessageId( 1 );
    promotionNotificationOne.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION ) );

    List<PromotionNotificationType> promotionNotifications = new ArrayList<>();
    promotionNotifications.add( promotionNotificationOne );

    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "PromoName" );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date( System.currentTimeMillis() + 30 * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY ) );
    promotion.setIncludePurl( true );

    promotion.setPromotionNotifications( promotionNotifications );

    PurlRecipient purlRecipient = buildPurlRecipient();
    PurlContributor purlContributorObj = buildPaxPurlContributor();
    PurlRecipient purlRecipientInternal = buildPurlRecipient();
    purlRecipientInternal.setPromotion( promotion );

    purlContributorObj.setPurlRecipient( purlRecipientInternal );
    Set<PurlContributor> purContributorSet = new HashSet<>();
    purContributorSet.add( purlContributorObj );
    purlRecipient.setContributors( purContributorSet );

    PurlContributor purlContributor = buildPaxPurlContributor();
    purlService.sendPurlContributionEmail( purlRecipient, purlContributor, new Long( 1 ), false );
    mockMailingService.verify();

  }

}
