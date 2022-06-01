
package com.biperf.core.service.email.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.MockContentReader;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public class PersonalizationServiceImplTest extends BaseServiceTest
{

  private static final String BLANK1 = ""; // prepersonalization (mailing level)
  private static final String BLANK2 = null;
  private static final String BLANK3 = "";
  private static final String BLANK4 = ""; // personalization
  private static final String BLANK5 = null;
  private static final String BLANK6 = "";
  private static final String FUNKY = "~!@#$%^&*()_+{}|:";

  private static final String PREPROCESSED_SUBJECT = "Submitted Notification by ${firstName} - Test Subject";
  private static final String PREPROCESSED_PLAIN_TEXT = "Hello ${firstName} - You have successfully submitted a claim for promotion ${promotionName}.";
  private static final String PREPROCESSED_HTML = "<html><head>${promotionName} - Claim Submitted</head><body>Hello ${firstName},<p/> You have successfully submitted a "
      + "claim for promotion ${promotionName}.  Test <a href=\"$url\">url</a>" + "  Test blanks <${blank1}> <${blank2}> <$blank3> <${blank4}> <${blank5}> <$blank6>."
      + "  Funky data ${funkyData}.</body></html>";
  private static final String PREPROCESSED_TEXT = "SMS Hello ${firstName} - You have successfully submitted a claim for " + "promotion ${promotionName}.";

  private static final String PREPERSONALIZED_SUBJECT = "Submitted Notification by ${firstName} - Test Subject";
  private static final String PREPERSONALIZED_PLAIN_TEXT = "Hello ${firstName} - You have successfully submitted a claim for promotion New Test Promotion.";
  private static final String PREPERSONALIZED_HTML = "<html><head>New Test Promotion - Claim Submitted</head><body>Hello ${firstName},<p/> You have successfully submitted a "
      + "claim for promotion New Test Promotion.  Test <a href=\"http://localhost:7001/beacon/homePage.do?method=foofoo\">url</a>" + "  Test blanks <> <> <> <${blank4}> <${blank5}> <$blank6>."
      + "  Funky data ${funkyData}.</body></html>";
  private static final String PREPERSONALIZED_TEXT = "SMS Hello ${firstName} - You have successfully submitted a claim for promotion New Test Promotion.";

  private static final String PREPERSONALIZED_PLAIN_MESSAGE = "Hello ${firstName} - You have successfully submitted a claim for promotion .";
  private static final String PREPERSONALIZED_HTML_MESSAGE = "<html><head> - Claim Submitted</head><body>Hello ${firstName},<p/> You have successfully submitted a "
      + "claim for promotion .  Test <a href=\"http://localhost:7001/beacon/homePage.do?method=foofoo\">url</a> " + " Test blanks <> <> <> <${blank4}> <${blank5}> <$blank6>."
      + "  Funky data ${funkyData}.</body></html>";
  private static final String PREPERSONALIZED_MESSAGE = "SMS Hello ${firstName} - You have successfully submitted a claim for promotion .";

  private static final String PERSONALIZED_SUBJECT_MATTHEW = "Submitted Notification by Matthew - Test Subject";
  private static final String PERSONALIZED_PLAIN_TEXT_MATTHEW = "Hello Matthew - You have successfully submitted a claim for promotion New Test Promotion.";
  private static final String PERSONALIZED_HTML_MATTHEW = "<html><head>New Test Promotion - Claim Submitted</head><body>Hello Matthew,<p/> You have successfully submitted a "
      + "claim for promotion New Test Promotion.  Test <a href=\"http://localhost:7001/beacon/homePage.do?method=foofoo\">url</a>" + "  Test blanks <> <> <> <> <> <>."
      + "  Funky data ${funkyData}.</body></html>";
  private static final String PERSONALIZED_TEXT_MATTHEW = "SMS Hello Matthew - You have successfully submitted a claim for promotion New Test Promotion.";

  private static final String PREPERSONALIZED_ARRAY_TEST = "";
  private static final String PERSONALIZED_ARRAY_TEST = "";

  private static final String CM_ASSETCODE = "message_data.message.10000831";

  private PersonalizationServiceImpl personalizationService = new PersonalizationServiceImpl();

  private ContentReader oldContentReader = null;

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed.
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    oldContentReader = ContentReaderManager.getContentReader();

  }

  public void testPersonalize()
  {

    MailingRecipient mailingRecipient = buildMailingRecipient( 1, "Matthew", "Shepard", "en", 1 );

    MailingRecipientData blank1Data = new MailingRecipientData();
    blank1Data.setKey( "blank4" );
    blank1Data.setValue( BLANK4 );
    mailingRecipient.addMailingRecipientData( blank1Data );
    MailingRecipientData blank2Data = new MailingRecipientData();
    blank2Data.setKey( "blank5" );
    blank2Data.setValue( BLANK5 );
    mailingRecipient.addMailingRecipientData( blank2Data );
    MailingRecipientData blank3Data = new MailingRecipientData();
    blank3Data.setKey( "blank6" );
    blank3Data.setValue( BLANK6 );
    mailingRecipient.addMailingRecipientData( blank3Data );
    MailingRecipientData funkyData = new MailingRecipientData();
    funkyData.setKey( "funky" );
    funkyData.setValue( FUNKY );
    mailingRecipient.addMailingRecipientData( funkyData );

    assertEquals( PERSONALIZED_SUBJECT_MATTHEW, personalizationService.personalize( mailingRecipient, PREPERSONALIZED_SUBJECT ) );
    assertEquals( PERSONALIZED_HTML_MATTHEW, personalizationService.personalize( mailingRecipient, PREPERSONALIZED_HTML ) );
    assertEquals( PERSONALIZED_PLAIN_TEXT_MATTHEW, personalizationService.personalize( mailingRecipient, PREPERSONALIZED_PLAIN_TEXT ) );
    assertEquals( PERSONALIZED_TEXT_MATTHEW, personalizationService.personalize( mailingRecipient, PREPERSONALIZED_TEXT ) );

    assertEquals( PERSONALIZED_ARRAY_TEST, personalizationService.personalize( mailingRecipient, PREPERSONALIZED_ARRAY_TEST ) );

  }

  public void testPrePersonalize()
  {
    HashMap objectMap = new HashMap();

    // build non recipient-specific lookup objects
    Promotion promotion = buildPromotion();
    objectMap.put( "promotionName", promotion.getName() );
    objectMap.put( "url", "http://localhost:7001/beacon/homePage.do?method=foofoo" );
    objectMap.put( "funky2", FUNKY );
    objectMap.put( "blank1", BLANK1 );
    objectMap.put( "blank2", BLANK2 );
    objectMap.put( "blank3", BLANK3 );

    Content content = new Content();
    content.addContentData( Message.CM_KEY_SUBJECT, PREPROCESSED_SUBJECT );
    content.addContentData( Message.CM_KEY_PLAIN_TEXT_MSG, PREPROCESSED_PLAIN_TEXT );
    content.addContentData( Message.CM_KEY_HTML_MSG, PREPROCESSED_HTML );
    content.addContentData( Message.CM_KEY_TEXT_MSG, PREPROCESSED_TEXT );

    MockContentReader mockContentReader = new MockContentReader();
    mockContentReader.addContent( CM_ASSETCODE, content );
    ContentReaderManager.setContentReader( mockContentReader );

    Mailing processedMailing = null;

    try
    {
      processedMailing = personalizationService.preProcessMailing( buildMailing(), objectMap );
    }
    catch( Exception e )
    {
      // won't be any mailing recipient locale errors
    }

    // only one locale
    assertEquals( 1, processedMailing.getMailingMessageLocales().size() );
    MailingMessageLocale mailingMessageLocale = (MailingMessageLocale)processedMailing.getMailingMessageLocales().iterator().next();

    assertEquals( PREPERSONALIZED_SUBJECT, mailingMessageLocale.getSubject() );
    assertEquals( PREPERSONALIZED_PLAIN_MESSAGE, mailingMessageLocale.getPlainMessage() );
    assertEquals( PREPERSONALIZED_HTML_MESSAGE, mailingMessageLocale.getHtmlMessage() );
    assertEquals( PREPERSONALIZED_MESSAGE, mailingMessageLocale.getTextMessage() );

  }

  private Promotion buildPromotion()
  {
    Promotion promotion = new ProductClaimPromotion();
    promotion.setName( "New Test Promotion" );
    return promotion;
  }

  private Mailing buildMailing()
  {
    Mailing mailing = new Mailing();
    mailing.setMailingRecipients( buildMailingRecipients() );
    mailing.setMailingMessageLocales( new HashSet() );
    mailing.setMessage( buildMessage( 1 ) );

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( "testmessage@test.com" );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );

    return mailing;
  }

  private Message buildMessage( long id )
  {
    Message message = new Message();
    message.setId( new Long( id ) );
    message.setCmAssetCode( CM_ASSETCODE );

    return message;
  }

  private Set buildMailingRecipients()
  {
    Set mailingRecipients = new HashSet();

    mailingRecipients.add( buildMailingRecipient( 1, "Matthew", "Shepard", "en", 1 ) );
    mailingRecipients.add( buildMailingRecipient( 2, "Glynn", "Pooke", "en", 2 ) );
    mailingRecipients.add( buildMailingRecipient( 3, "Robert", "Presswood", "en", 3 ) );
    mailingRecipients.add( buildMailingRecipient( 4, "Vanessa", "Martin", "en", 4 ) );

    return mailingRecipients;
  }

  private Set buildMailingRecipientDataSet( String firstName, String lastName )
  {
    Set mailingRecipientDataSet = new HashSet();
    MailingRecipientData mailingRecipientData1 = new MailingRecipientData();
    mailingRecipientData1.setKey( "firstName" );
    mailingRecipientData1.setValue( firstName );
    mailingRecipientDataSet.add( mailingRecipientData1 );

    MailingRecipientData mailingRecipientData2 = new MailingRecipientData();
    mailingRecipientData2.setKey( "lastName" );
    mailingRecipientData2.setValue( lastName );
    mailingRecipientDataSet.add( mailingRecipientData2 );

    MailingRecipientData mailingRecipientData3 = new MailingRecipientData();
    mailingRecipientData3.setKey( "promotionName" );
    mailingRecipientData3.setValue( "New Test Promotion" );

    return mailingRecipientDataSet;
  }

  private MailingRecipient buildMailingRecipient( long id, String firstName, String lastName, String locale, int randomValue )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    mailingRecipient.setUser( buildUser( id, firstName, lastName ) );
    mailingRecipient.setGuid( String.valueOf( new Date().getTime() + randomValue ) );
    mailingRecipient.setLocale( locale );
    mailingRecipient.setMailingRecipientDataSet( buildMailingRecipientDataSet( firstName, lastName ) );

    return mailingRecipient;
  }

  private User buildUser( long id, String firstName, String secondName )
  {
    User user = new User();
    user.setId( new Long( id ) );
    user.setFirstName( firstName );
    user.setLastName( secondName );

    return user;
  }

  @Override
  public void tearDown() throws Exception
  {
    super.tearDown();
    // restore the content reader for incontainer tests.
    if ( oldContentReader != null )
    {
      ContentReaderManager.setContentReader( oldContentReader );
    }
  }

}
