/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/email/impl/MailingServiceImplTest.java,v $
 */

package com.biperf.core.service.email.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.easymock.classextension.EasyMock;

import com.biperf.core.builders.BuilderUtil;
import com.biperf.core.dao.mailing.MailingDAO;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.enums.UserTokenStatusType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.EmailUserToken;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.process.MailingProcess;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.email.PersonalizationService;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.security.EncryptionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * MailingServiceImplTest
 * 
 *
 */
public class MailingServiceImplTest extends BaseServiceTest
{

  private static final String SENDER_ADDRESS = "notification@biworldwide.com";
  private static final String SUBJECT_PREFIX = "";

  private static final String RESEND_SUBJ_NOT_PERSONALIZED = "Resent subject for $firstName";
  private static final String RESEND_BODY_NOT_PERSONALIZED = "Resent message for $firstName";
  private static final String RESEND_SMS_NOT_PERSONALIZED = "Resent SMS for $firstName";
  private static final String RESEND_SUBJ_PERSONALIZED = "Resent subject for James";
  private static final String RESEND_BODY_PERSONALIZED = "Resent message for James";
  private static final String RESEND_SMS_PERSONALIZED = "Resent SMS for James";

  private static final String PREPERSONALIZED_SUBJECT = "Submitted Notification by $firstName - Test Subject";
  private static final String PREPERSONALIZED_PLAIN = "Hello $firstName - You have successfully submitted a claim for promotion New Test Promotion";
  private static final String PREPERSONALIZED_HTML = "<html><head>New Test Promotion - Claim Submitted</head><body>Hello $firstName,<p/> You have successfully submitted a claim for promotion New Test Promotion</body></html>";
  private static final String PREPERSONALIZED_SMS = "SMS Hello $firstName - You have successfully submitted a claim for promotion New Test Promotion";

  private static final String INCENTIVE_PERSONAL_DISPLAY_NAME = "Incentive System Notification Service";

  private static final String EMAIL_WRAPPER_HEADER = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" /><meta name=\"robots\" content=\"none\" /><title></title><style type=\"text/css\">${css}</style></head><body><div id=\"EmailBox\"><div id=\"Box\"><div id=\"Title\"></div><div id=\"Main\">";
  private static final String EMAIL_WRAPPER_FOOTER = "</div></div></div></body></html>";
  private static final String SITE_URL_PREFIX = "http://localhost:7001/pentag ";
  // private static final String URLSHORTNER_URL="http://recog.co/yourls-api.php";
  // private static final String URLSHORTNER_SIGNATURE="0b9f20a71a";

  private MailingServiceImpl mailingService;

  private MailingDAO mailingDAOMock;

  private SystemVariableService systemVariableServiceMock;

  private CommLogService commLogServiceMock;

  private MessageService messageServiceMock;

  private ProcessService processServiceMock;

  private PersonalizationService personalizationServiceMock;

  private EmailService emailServiceMock;

  private DesignThemeService designThemeServiceMock;

  // private MailingBatchDAO mailingBatchDAOMock;

  private MockPickListFactory mockFactory = new MockPickListFactory();

  private ContentReader oldContentReader = null;

  UserService userServiceMock;

  private EncryptionService encryptionServiceMock;
  
  private CMAssetService cmAssetServiceMock;

  /**
   * Test setup Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  @Override
  public void setUp() throws Exception
  {
    super.setUp();

    mailingDAOMock = EasyMock.createMock( MailingDAO.class );
    systemVariableServiceMock = EasyMock.createMock( SystemVariableService.class );
    commLogServiceMock = EasyMock.createMock( CommLogService.class );
    messageServiceMock = EasyMock.createMock( MessageService.class );
    processServiceMock = EasyMock.createNiceMock( ProcessService.class );
    personalizationServiceMock = EasyMock.createMock( PersonalizationService.class );
    emailServiceMock = EasyMock.createMock( EmailService.class );
    designThemeServiceMock = EasyMock.createMock( DesignThemeService.class );
    userServiceMock = EasyMock.createMock( UserService.class );
    encryptionServiceMock = EasyMock.createMock( EncryptionService.class );
    cmAssetServiceMock = EasyMock.createMock( CMAssetService.class );

    // emailServiceMock.setMailSender( mailSenderMock );

    oldContentReader = ContentReaderManager.getContentReader();

    mailingService = new MailingServiceImpl()
    {
      @Override
      protected UserService getUserService()
      {
        return userServiceMock;
      }

      @Override
      protected String getTextMessageString( String cmKey )
      {
        return "${programName}: Your account was just activated. Didn't do it ? Take action to protect your account. Lock it and get help: ${accountLockShortUrl}. TXT STOP 2end";
      }
    };
    mailingService.setSystemVariableService( systemVariableServiceMock );
    mailingService.setCommLogService( commLogServiceMock );
    mailingService.setProcessService( processServiceMock );
    mailingService.setPersonalizationService( personalizationServiceMock );
    mailingService.setMailingDAO( mailingDAOMock );
    mailingService.setEmailService( emailServiceMock );
    mailingService.setMessageService( messageServiceMock );
    mailingService.setDesignThemeService( designThemeServiceMock );
    mailingService.setEncryptionService( encryptionServiceMock );
    mailingService.setCmAssetService( cmAssetServiceMock );

  }

  @Override
  public void tearDown()
  {
    // restore the content reader for incontainer tests.
    if ( oldContentReader != null )
    {
      ContentReaderManager.setContentReader( oldContentReader );
    }
  }

  public void testSubmitMailing()
  {

    // SETUP TEST DATA
    Mailing mailing = buildMailing( null, (MailingType)MockPickListFactory.getMockPickListItem( MailingType.class, MailingType.EMAIL_WIZARD ), new Date().getTime() );

    Map<Object, Object> objectMap = new HashMap<>();
    Promotion promotion = buildPromotion();
    objectMap.put( "promotion", promotion.getName() );

    // mailing is exactly the same but has additional data
    String guid = GuidUtils.generateGuid();
    Mailing prePersonalizedMailing = buildMailing( null, (MailingType)MockPickListFactory.getMockPickListItem( MailingType.class, MailingType.EMAIL_WIZARD ), new Date().getTime() );
    Iterator prePersonalizedMailingRecipientsIter = prePersonalizedMailing.getMailingRecipients().iterator();
    while ( prePersonalizedMailingRecipientsIter.hasNext() )
    {
      MailingRecipient prePersonalizedMailingRecipient = (MailingRecipient)prePersonalizedMailingRecipientsIter.next();

      Iterator mailingRecipientsIter = mailing.getMailingRecipients().iterator();
      while ( mailingRecipientsIter.hasNext() )
      {
        MailingRecipient mailingRecipient = (MailingRecipient)mailingRecipientsIter.next();
        if ( prePersonalizedMailingRecipient.getId().equals( mailingRecipient.getId() ) )
        {
          prePersonalizedMailingRecipient.setGuid( mailingRecipient.getGuid() );
          addPrePersonalizedMailingRecipientData( prePersonalizedMailingRecipient );
          break;
        }
      }
    }
    prePersonalizedMailing.setGuid( guid );

    // mailing exactly the same but with id - need them seperate though
    Mailing prePersonalizedMailingWithId = buildMailing( new Long( 1 ), (MailingType)MockPickListFactory.getMockPickListItem( MailingType.class, MailingType.EMAIL_WIZARD ), new Date().getTime() );
    Iterator prePersonalizedMailingWithIdRecipientsIter = prePersonalizedMailing.getMailingRecipients().iterator();
    while ( prePersonalizedMailingWithIdRecipientsIter.hasNext() )
    {
      MailingRecipient prePersonalizedMailingRecipient = (MailingRecipient)prePersonalizedMailingWithIdRecipientsIter.next();

      Iterator mailingRecipientsIter = mailing.getMailingRecipients().iterator();
      while ( mailingRecipientsIter.hasNext() )
      {
        MailingRecipient mailingRecipient = (MailingRecipient)mailingRecipientsIter.next();
        if ( prePersonalizedMailingRecipient.getId().equals( mailingRecipient.getId() ) )
        {
          prePersonalizedMailingRecipient.setGuid( mailingRecipient.getGuid() );
          addPrePersonalizedMailingRecipientData( prePersonalizedMailingRecipient );
          break;
        }
      }
    }
    prePersonalizedMailingWithId.setGuid( guid );

    // SETUP MOCK OBJECTS
    PropertySetItem systemVariableIncentivePersonalDisplayName = new PropertySetItem();
    systemVariableIncentivePersonalDisplayName.setStringVal( INCENTIVE_PERSONAL_DISPLAY_NAME );
    systemVariableIncentivePersonalDisplayName.setKey( SystemVariableService.INCENTIVE_PERSONAL_DISPLAY_NAME );
    systemVariableIncentivePersonalDisplayName.setEntityName( SystemVariableService.INCENTIVE_PERSONAL_DISPLAY_NAME );

    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.INCENTIVE_PERSONAL_DISPLAY_NAME ) ).andReturn( systemVariableIncentivePersonalDisplayName );
    EasyMock.replay( systemVariableServiceMock );

    EasyMock.expect( messageServiceMock.getMessageByCMAssetCode( mailing.getMessage().getCmAssetCode() ) ).andReturn( mailing.getMessage() );
    EasyMock.replay( messageServiceMock );

    EasyMock.expect( personalizationServiceMock.preProcessMailing( mailing, objectMap ) ).andReturn( prePersonalizedMailing );
    EasyMock.replay( personalizationServiceMock );

    EasyMock.expect( mailingDAOMock.saveMailing( prePersonalizedMailing ) ).andReturn( prePersonalizedMailingWithId );
    EasyMock.replay( mailingDAOMock );

    Process process = new Process();
    process.setId( new Long( 9999 ) );
    process.setName( MailingProcess.PROCESS_NAME );
    process.setProcessBeanName( MailingProcess.BEAN_NAME );
    process.setProcessStatusType( (ProcessStatusType)mockFactory.getPickListItem( ProcessStatusType.class, ProcessStatusType.ACTIVE ) );

    EasyMock.expect( processServiceMock.createOrLoadSystemProcess( MailingProcess.PROCESS_NAME, MailingProcess.BEAN_NAME ) ).andReturn( process );

    Map<Object, Object> parameterValueMap = new LinkedHashMap<>();
    parameterValueMap.put( "mailingId", new String[] { prePersonalizedMailingWithId.getId().toString() } );

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( mailing.getDeliveryDate() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );

    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processServiceMock.scheduleProcess( process, processSchedule, parameterValueMap, null );
    EasyMock.expectLastCall().once();

    EasyMock.replay( processServiceMock );

    // TEST
    boolean noExceptions = true;
    try
    {
      mailingService.submitMailing( mailing, objectMap );
    }
    catch( Exception e )
    {
      noExceptions = false;
    }

    assertTrue( noExceptions );

  }

  public void testReSubmitMailing()
  {

    // BUILD UP TEST DATA
    User user = buildUser( 99, "James", "Jablonski" );
    String mailingRecipientGuid = GuidUtils.generateGuid();
    long time = new Date().getTime();

    // previously saved mailing
    Mailing previouslySavedMailing = new Mailing();
    previouslySavedMailing.setId( new Long( 1 ) );

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setUser( user );
    mailingRecipient.setLocale( "en" );
    mailingRecipient.setGuid( mailingRecipientGuid );
    mailingRecipient.setPreviewEmailAddress( user.getPrimaryEmailAddress().getEmailAddr() );

    MailingMessageLocale previouslySavedMailingMessageLocale = new MailingMessageLocale();
    previouslySavedMailingMessageLocale.setSubject( RESEND_SUBJ_NOT_PERSONALIZED );
    previouslySavedMailingMessageLocale.setPlainMessage( RESEND_BODY_NOT_PERSONALIZED );
    previouslySavedMailingMessageLocale.setTextMessage( RESEND_SMS_NOT_PERSONALIZED );
    previouslySavedMailingMessageLocale.setLocale( "en" );
    previouslySavedMailing.getMailingMessageLocales().add( previouslySavedMailingMessageLocale );

    previouslySavedMailing.setGuid( GuidUtils.generateGuid() );
    previouslySavedMailing.addMailingRecipient( mailingRecipient );
    previouslySavedMailing.setMailingType( MailingType.lookup( MailingType.RESEND ) );
    previouslySavedMailing.setDeliveryDate( new Timestamp( time - 1000000 ) );
    previouslySavedMailing.setSender( "Sender" );

    // SETUP MOCKS
    personalizationServiceMock.personalize( mailingRecipient, RESEND_SUBJ_NOT_PERSONALIZED );
    EasyMock.expectLastCall().andReturn( RESEND_SUBJ_PERSONALIZED );

    personalizationServiceMock.personalize( mailingRecipient, RESEND_BODY_NOT_PERSONALIZED );
    EasyMock.expectLastCall().andReturn( RESEND_BODY_PERSONALIZED );

    personalizationServiceMock.personalize( mailingRecipient, RESEND_SMS_NOT_PERSONALIZED );
    EasyMock.expectLastCall().andReturn( RESEND_SMS_PERSONALIZED );
    EasyMock.replay( personalizationServiceMock );

    Mailing mailing = new Mailing();
    mailing.setId( new Long( 2 ) );

    String mailingGuid = GuidUtils.generateGuid();

    MailingMessageLocale mailingMessageLocale = new MailingMessageLocale();
    mailingMessageLocale.setSubject( RESEND_SUBJ_PERSONALIZED );
    mailingMessageLocale.setPlainMessage( RESEND_BODY_PERSONALIZED );
    mailingMessageLocale.setTextMessage( RESEND_SMS_PERSONALIZED );
    mailingMessageLocale.setLocale( "en" );
    mailing.getMailingMessageLocales().add( mailingMessageLocale );

    mailing.setGuid( mailingGuid );
    mailing.addMailingRecipient( mailingRecipient );
    mailing.setMailingType( MailingType.lookup( MailingType.RESEND ) );
    mailing.setDeliveryDate( new Timestamp( time ) );

    Mailing savedMailing = new Mailing();
    savedMailing.setId( new Long( 2 ) );

    savedMailing.getMailingMessageLocales().add( mailingMessageLocale );

    mailing.setGuid( mailingGuid );
    mailing.addMailingRecipient( mailingRecipient );
    mailing.setMailingType( MailingType.lookup( MailingType.RESEND ) );
    mailing.setDeliveryDate( new Timestamp( time ) );

    Process process = new Process();
    process.setId( new Long( 9999 ) );
    process.setName( MailingProcess.PROCESS_NAME );
    process.setProcessBeanName( MailingProcess.BEAN_NAME );
    process.setProcessStatusType( (ProcessStatusType)mockFactory.getPickListItem( ProcessStatusType.class, ProcessStatusType.ACTIVE ) );

    EasyMock.expect( processServiceMock.createOrLoadSystemProcess( MailingProcess.PROCESS_NAME, MailingProcess.BEAN_NAME ) ).andReturn( process );

    Map<Object, Object> parameterValueMap = new LinkedHashMap<>();
    parameterValueMap.put( "mailingId", new String[] { savedMailing.getId().toString() } );

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( mailing.getDeliveryDate() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );

    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processServiceMock.scheduleProcess( process, processSchedule, parameterValueMap, null );
    EasyMock.expectLastCall().once();

    EasyMock.replay( processServiceMock );

    PropertySetItem systemVariableSystemEmailAddress = new PropertySetItem();
    systemVariableSystemEmailAddress.setStringVal( SENDER_ADDRESS );
    systemVariableSystemEmailAddress.setKey( SystemVariableService.SYSTEM_EMAIL_ADDRESS );
    systemVariableSystemEmailAddress.setEntityName( SystemVariableService.SYSTEM_EMAIL_ADDRESS );

    PropertySetItem systemVariableDefaultLanguage = new PropertySetItem();
    systemVariableDefaultLanguage.setStringVal( "en_us" );
    systemVariableDefaultLanguage.setKey( SystemVariableService.DEFAULT_LANGUAGE );
    systemVariableDefaultLanguage.setEntityName( SystemVariableService.DEFAULT_LANGUAGE );

    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ) ).andReturn( systemVariableSystemEmailAddress );
    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS_ADDL ) ).andReturn( systemVariableSystemEmailAddress );
    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.SYSTEM_PERSONAL_DISPLAY_NAME ) ).andReturn( systemVariableSystemEmailAddress );
    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.CLIENT_PREFIX ) ).andReturn( systemVariableSystemEmailAddress ).anyTimes();
    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.CLIENT_NAME ) ).andReturn( systemVariableSystemEmailAddress ).anyTimes();
    EasyMock.expect( systemVariableServiceMock.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ) ).andReturn( systemVariableDefaultLanguage ).anyTimes();
    EasyMock.replay( systemVariableServiceMock );

    EasyMock.expect( designThemeServiceMock.getDefaultDesignTheme() ).andReturn( "designTheme" );
    EasyMock.expect( designThemeServiceMock.getSkinContentByKey( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( "designTheme" );
    EasyMock.expect( designThemeServiceMock.getSkinContentByKey( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( "designTheme" );
    EasyMock.replay( designThemeServiceMock );
    
    boolean noExceptions = true;
    try
    {
      mailingService.reSubmitMailing( previouslySavedMailing, user, null, false );
    }
    catch( Exception e )
    {
      noExceptions = false;
      e.printStackTrace();
    }

    assertTrue( noExceptions );

  }

  public void testbuildPAXForgotLoginIDNotification()
  {
    mockMailingServices();
    Mailing mailing = mailingService.buildPAXForgotLoginIDNotification( 1L, new PaxContactType(), null );

    assertNotNull( mailing );
    assertTrue( CollectionUtils.isNotEmpty( mailing.getMailingRecipients() ) );
  }

  public void testbuildPAXForgotLoginIDSentNotification()
  {
    mockMailingServices();
    User user = BuilderUtil.buildUser();
    UserEmailAddress email = BuilderUtil.buildUserEmailAddress();
    user.addUserEmailAddress( email );
    email.setUser( user );
    Mailing mailing = mailingService.buildPaxForgotLoginIDSentNotification( email, true, true );
    assertNotNull( mailing );
    assertTrue( CollectionUtils.isNotEmpty( mailing.getMailingRecipients() ) );
  }

  public void testbuildPAXPasswordChangeNotification()
  {
    PropertySetItem propertySetItem = BuilderUtil.buildPropertySetItem();
    expect( systemVariableServiceMock.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ) ).andReturn( propertySetItem );
    expect( encryptionServiceMock.getEncryptedValue( anyObject() ) ).andReturn( new String( "Testing Encrypted String" ) );
    expect( userServiceMock.getUserById( anyObject() ) ).andReturn( buildUser( 1L, "firstName", "lastName" ) );
    mockMailingServices();
    replay( encryptionServiceMock );
    Mailing mailing = mailingService.buildAccountOrPasswordChangeNotification( 1L, true, MessageService.PASSWORD_CHANGE );

    assertNotNull( mailing );
    assertTrue( CollectionUtils.isNotEmpty( mailing.getMailingRecipients() ) );
  }

  public void testBuildTextMessageForAccountActivationNotification()
  {
    PropertySetItem propertySetItem = BuilderUtil.buildPropertySetItem();
    expect( encryptionServiceMock.getEncryptedValue( anyObject() ) ).andReturn( new String( "Testing Encrypted String" ) );
    expect( systemVariableServiceMock.getPropertyByName( anyObject() ) ).andReturn( propertySetItem );
    expect( systemVariableServiceMock.getPropertyByNameAndEnvironment( anyObject() ) ).andReturn( propertySetItem );
    expect( systemVariableServiceMock.getPropertyByNameAndEnvironment( anyObject() ) ).andReturn( propertySetItem );
    expect( systemVariableServiceMock.getPropertyByNameAndEnvironment( anyObject() ) ).andReturn( propertySetItem );
    replay( encryptionServiceMock, systemVariableServiceMock );
    String textMessage = mailingService.buildAccountOrPasswordChangeText( buildUser( 99, "James", "Jablonski" ), MessageService.ACCOUNT_ACTIVATED );
    assertNotNull( textMessage );
  }

  public void testbuildPAXForgotPasswordNotification()
  {

    mockMailingServices();
    PaxContactType paxContact = new PaxContactType();
    paxContact.setContactType( ContactType.EMAIL );
    Mailing mailing = mailingService.buildPAXForgotPasswordNotification( 1L, paxContact, buildUserToken().getToken() );
    assertNotNull( mailing );
    assertTrue( CollectionUtils.isNotEmpty( mailing.getMailingRecipients() ) );

  }

  public void testBuildSSIContestEditNotification()
  {
    mockMailingServices();
    expect( cmAssetServiceMock.getString( anyObject(), EasyMock.eq( SSIContest.CONTEST_CMASSET_NAME ), anyObject(), EasyMock.eq( true ) ) ).andReturn( "contest-name" );
    Mailing mailing = mailingService.buildSSIContestEditNotification( new SSIContest(), "test.case@test.com" );
    assertNotNull( mailing );
    assertTrue( CollectionUtils.isNotEmpty( mailing.getMailingRecipients() ) );
  }

  public static UserToken buildUserToken()
  {

    UserToken userToken = new EmailUserToken();
    userToken.setStatus( UserTokenStatusType.lookup( UserTokenStatusType.ISSUED ) );
    userToken.setUnencryptedTokenValue( "A123B123" );
    userToken.setToken( new SHA256Hash().encryptDefault( "A123B123" ) );

    return userToken;
  }

  // Mocks mailing service objects needed for mailing service
  private void mockMailingServices()
  {
    expect( messageServiceMock.getMessageByCMAssetCode( anyObject() ) ).andReturn( buildMessage( 1L ) );
    expect( userServiceMock.getUserById( anyObject() ) ).andReturn( buildUser( 1L, "firstName", "lastName" ) );
    expect( systemVariableServiceMock.getPropertyByName( anyObject() ) ).andReturn( new PropertySetItem() );
    expect( systemVariableServiceMock.getPropertyByNameAndEnvironment( anyObject() ) ).andReturn( new PropertySetItem() );
    expect( systemVariableServiceMock.getPropertyByName( anyObject() ) ).andReturn( new PropertySetItem() );
    replay( messageServiceMock, userServiceMock, systemVariableServiceMock );
  }

  private Promotion buildPromotion()
  {
    Promotion promotion = new ProductClaimPromotion();
    promotion.setName( "New Test Promotion" );
    return promotion;
  }

  private Mailing buildMailing( Long id, MailingType mailingType, long time )
  {
    Mailing mailing = new Mailing();
    mailing.setMailingType( mailingType );
    mailing.setId( id );
    mailing.setMailingRecipients( buildMailingRecipients() );
    mailing.getMailingMessageLocales().add( buildPrePersonalizedMessageLocale() );
    mailing.setMessage( buildMessage( 1 ) );

    mailing.setDeliveryDate( new Timestamp( time ) );
    mailing.setSender( "testmessage@test.com" );

    return mailing;
  }

  private Message buildMessage( long id )
  {
    Message message = new Message();
    message.setId( new Long( id ) );
    message.setCmAssetCode( "message_data.message.10000831" );

    return message;
  }

  private Set<MailingRecipient> buildMailingRecipients()
  {
    Set<MailingRecipient> mailingRecipients = new HashSet<>();

    mailingRecipients.add( buildMailingRecipient( 1, "Matthew", "Shepard", "en", 1 ) );
    mailingRecipients.add( buildMailingRecipient( 2, "Glynn", "Pooke", "en", 2 ) );
    mailingRecipients.add( buildMailingRecipient( 3, "Robert", "Presswood", "en", 3 ) );
    mailingRecipients.add( buildMailingRecipient( 4, "Vanessa", "Martin", "en", 4 ) );

    return mailingRecipients;
  }

  private MailingRecipient buildMailingRecipient( long id, String firstName, String secondName, String locale, int semiRandomValue )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    mailingRecipient.setId( new Long( id ) );
    mailingRecipient.setUser( buildUser( id, firstName, secondName ) );
    mailingRecipient.setGuid( String.valueOf( new Date().getTime() + semiRandomValue ) );
    mailingRecipient.setLocale( locale );

    return mailingRecipient;
  }

  private MailingRecipient addPrePersonalizedMailingRecipientData( MailingRecipient mailingRecipient )
  {
    String firstName = mailingRecipient.getUser().getFirstName();
    MailingRecipientData mailingRecipientDataFirstName = new MailingRecipientData();
    MailingRecipientData mailingRecipientDataLastName = new MailingRecipientData();

    if ( firstName.equals( "Matthew" ) )
    {
      mailingRecipientDataFirstName.setKey( "{user:firstName" );
      mailingRecipientDataFirstName.setValue( "Matthew" );
      mailingRecipientDataLastName.setKey( "{user:lastName" );
      mailingRecipientDataLastName.setValue( "Shepard" );

    }
    else if ( firstName.equals( "Glynn" ) )
    {
      mailingRecipientDataFirstName.setKey( "{user:firstName" );
      mailingRecipientDataFirstName.setValue( "Glynn" );
      mailingRecipientDataLastName.setKey( "{user:lastName" );
      mailingRecipientDataLastName.setValue( "Pooke" );

    }
    else if ( firstName.equals( "Robert" ) )
    {
      mailingRecipientDataFirstName.setKey( "{user:firstName" );
      mailingRecipientDataFirstName.setValue( "Robert" );
      mailingRecipientDataLastName.setKey( "{user:lastName" );
      mailingRecipientDataLastName.setValue( "Presswood" );

    }
    else if ( firstName.equals( "Vanessa" ) )
    {
      mailingRecipientDataFirstName.setKey( "{user:firstName" );
      mailingRecipientDataFirstName.setValue( "Vanessa" );
      mailingRecipientDataLastName.setKey( "{user:lastName" );
      mailingRecipientDataLastName.setValue( "Martin" );
    }

    mailingRecipient.getMailingRecipientDataSet().add( mailingRecipient );

    return mailingRecipient;
  }

  private User buildUser( long id, String firstName, String lastName )
  {
    User user = new User();
    user.setId( new Long( id ) );
    user.setFirstName( firstName );
    user.setLastName( lastName );
    user.setLanguageType( (LanguageType)MockPickListFactory.getMockPickListItem( LanguageType.class, LanguageType.ENGLISH ) );
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( firstName + "." + lastName + "@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.HOME ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setIsPrimary( new Boolean( true ) );
    userEmailAddress.setUser( user );
    user.getUserEmailAddresses().add( userEmailAddress );

    return user;
  }

  private MailingMessageLocale buildPrePersonalizedMessageLocale()
  {
    MailingMessageLocale mailingMessageLocale = new MailingMessageLocale();

    mailingMessageLocale.setId( new Long( 1 ) );
    mailingMessageLocale.setLocale( "en" );
    mailingMessageLocale.setSubject( PREPERSONALIZED_SUBJECT );
    mailingMessageLocale.setPlainMessage( PREPERSONALIZED_PLAIN );
    mailingMessageLocale.setHtmlMessage( PREPERSONALIZED_HTML );
    mailingMessageLocale.setTextMessage( PREPERSONALIZED_SMS );

    return mailingMessageLocale;

  }

  public MailingServiceImpl getMailingService()
  {
    return mailingService;
  }

  /*
   * It is never used private void testMailingBatch() { MailingBatch mailingBatch =
   * getMailingService().getMailingBatch( new Long (0) ); getMailingService().updateMailingBatch(
   * new Long (0) ); getMailingService().getMailingsForBatchId( new Long (0) ); }
   */

}
