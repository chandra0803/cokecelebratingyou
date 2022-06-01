/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/mailing/hibernate/MailingDAOImplTest.java,v $
 */

package com.biperf.core.dao.mailing.hibernate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.mailing.MailingDAO;
import com.biperf.core.dao.message.MessageDAO;
import com.biperf.core.dao.message.hibernate.MessageDAOImplTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.GuidUtils;

/**
 * MailingDAOImplTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>zahler</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MailingDAOImplTest extends BaseDAOTest
{

  public static String ONE_HUNDRED = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

  public static String ONE_HUNDRED_RETURNS = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

  public static String FIVE_HUNDRED = ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED;

  public static String ONE_THOUSAND_RETURNS = ONE_HUNDRED_RETURNS + ONE_HUNDRED_RETURNS + ONE_HUNDRED_RETURNS + ONE_HUNDRED_RETURNS + ONE_HUNDRED_RETURNS + ONE_HUNDRED_RETURNS + ONE_HUNDRED_RETURNS
      + ONE_HUNDRED_RETURNS + ONE_HUNDRED_RETURNS + ONE_HUNDRED_RETURNS;

  public static String ONE_THOUSAND = ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED + ONE_HUNDRED;

  public static String FIVE_THOUSAND = ONE_THOUSAND + ONE_THOUSAND + ONE_THOUSAND + ONE_THOUSAND + ONE_THOUSAND;

  public static String TEN_THOUSAND = FIVE_THOUSAND + FIVE_THOUSAND;

  public void testSaveAndGetMailingById()
  {
    MailingDAO mailingDAO = getMailingDAO();

    Mailing expectedMailing = buildMailing( getUniqueString(), "Some test here" );
    mailingDAO.saveMailing( expectedMailing );
    flushAndClearSession();

    Mailing actualMailing = mailingDAO.getMailingById( expectedMailing.getId() );
    assertEquals( "Mailings are not equal", expectedMailing, actualMailing );
    assertEquals( "Mailing Recipients are not equal", expectedMailing.getMailingRecipients(), actualMailing.getMailingRecipients() );
    assertEquals( "Mailing Message Locales are not equal", expectedMailing.getMailingMessageLocales(), actualMailing.getMailingMessageLocales() );

    MailingRecipient mailingRecipient = (MailingRecipient)actualMailing.getMailingRecipients().iterator().next();
    assertEquals( "Locale not found", mailingRecipient.getLocale(), mailingRecipient.getMailingMessageLocale().getLocale() );
    flushAndClearSession();

    // // TEST - BIG TEST 500
    // expectedMailing = buildMailing( getUniqueString(), true, FIVE_HUNDRED);
    // boolean exception = false;
    // try {
    // mailingDAO.saveMailing( expectedMailing );
    // flushAndClearSession();
    // } catch (Exception e) {
    // exception = true;
    // }
    //
    // assertTrue("Failed 500", exception == false);
    //
    // actualMailing = mailingDAO.getMailingById( expectedMailing.getId() );
    // assertEquals( "Mailings are not equal", expectedMailing, actualMailing );
    // assertEquals( "Mailing Recipients are not equal", expectedMailing.getMailingRecipients(),
    // actualMailing.getMailingRecipients() );
    // assertEquals( "Mailing Message Locales are not equal",
    // expectedMailing.getMailingMessageLocales(), actualMailing.getMailingMessageLocales() );
    //
    // mailingRecipient = (MailingRecipient) actualMailing.getMailingRecipients().iterator().next();
    // assertEquals( "Locale not found", mailingRecipient.getLocale(),
    // mailingRecipient.getMailingMessageLocale().getLocale() );
    // flushAndClearSession();
    //
    // // TEST - BIG TEST 1K
    // expectedMailing = buildMailing( getUniqueString(), true, ONE_THOUSAND);
    // exception = false;
    // try {
    // mailingDAO.saveMailing( expectedMailing );
    // flushAndClearSession();
    // } catch (Exception e) {
    // exception = true;
    // }
    //
    // assertTrue("Failed 1k", exception == false);
    //
    // actualMailing = mailingDAO.getMailingById( expectedMailing.getId() );
    // assertEquals( "Mailings are not equal", expectedMailing, actualMailing );
    // assertEquals( "Mailing Recipients are not equal", expectedMailing.getMailingRecipients(),
    // actualMailing.getMailingRecipients() );
    // assertEquals( "Mailing Message Locales are not equal",
    // expectedMailing.getMailingMessageLocales(), actualMailing.getMailingMessageLocales() );
    //
    // mailingRecipient = (MailingRecipient) actualMailing.getMailingRecipients().iterator().next();
    // assertEquals( "Locale not found", mailingRecipient.getLocale(),
    // mailingRecipient.getMailingMessageLocale().getLocale() );
    // flushAndClearSession();
    //
    // // TEST - BIG TEST NOT SO SPECIAL CHARACTERS 1K
    // expectedMailing = buildMailing( getUniqueString(), true, ONE_THOUSAND);
    // exception = false;
    // try {
    // mailingDAO.saveMailing( expectedMailing );
    // flushAndClearSession();
    // } catch (Exception e) {
    // exception = true;
    // }
    //
    // assertTrue("Failed not so special characters 1k", exception == false);
    //
    //
    // actualMailing = mailingDAO.getMailingById( expectedMailing.getId() );
    // assertEquals( "Mailings are not equal", expectedMailing, actualMailing );
    // assertEquals( "Mailing Recipients are not equal", expectedMailing.getMailingRecipients(),
    // actualMailing.getMailingRecipients() );
    // assertEquals( "Mailing Message Locales are not equal",
    // expectedMailing.getMailingMessageLocales(), actualMailing.getMailingMessageLocales() );
    //
    // mailingRecipient = (MailingRecipient) actualMailing.getMailingRecipients().iterator().next();
    // assertEquals( "Locale not found", mailingRecipient.getLocale(),
    // mailingRecipient.getMailingMessageLocale().getLocale() );
    // flushAndClearSession();
    //
    // // TEST - BIG TEST NOT SO SPECIAL CHARACTERS 2K
    // expectedMailing = buildMailing( getUniqueString(), true, ONE_THOUSAND + ONE_THOUSAND);
    // exception = false;
    // try {
    // mailingDAO.saveMailing( expectedMailing );
    // flushAndClearSession();
    // } catch (Exception e) {
    // exception = true;
    // }
    //
    // assertTrue("Failed not so special characters 2k", exception == false);
    //
    // actualMailing = mailingDAO.getMailingById( expectedMailing.getId() );
    // assertEquals( "Mailings are not equal", expectedMailing, actualMailing );
    // assertEquals( "Mailing Recipients are not equal", expectedMailing.getMailingRecipients(),
    // actualMailing.getMailingRecipients() );
    // assertEquals( "Mailing Message Locales are not equal",
    // expectedMailing.getMailingMessageLocales(), actualMailing.getMailingMessageLocales() );
    //
    // mailingRecipient = (MailingRecipient) actualMailing.getMailingRecipients().iterator().next();
    // assertEquals( "Locale not found", mailingRecipient.getLocale(),
    // mailingRecipient.getMailingMessageLocale().getLocale() );
    // flushAndClearSession();
    //
    // // TEST - BIG TEST 5K
    // expectedMailing = buildMailing( getUniqueString(), true, FIVE_THOUSAND);
    // exception = false;
    // try {
    // mailingDAO.saveMailing( expectedMailing );
    // flushAndClearSession();
    // } catch (Exception e) {
    // exception = true;
    // }
    //
    // assertTrue("Failed 5k", exception == false);
    //
    //
    // actualMailing = mailingDAO.getMailingById( expectedMailing.getId() );
    // assertEquals( "Mailings are not equal", expectedMailing, actualMailing );
    // assertEquals( "Mailing Recipients are not equal", expectedMailing.getMailingRecipients(),
    // actualMailing.getMailingRecipients() );
    // assertEquals( "Mailing Message Locales are not equal",
    // expectedMailing.getMailingMessageLocales(), actualMailing.getMailingMessageLocales() );
    //
    // mailingRecipient = (MailingRecipient) actualMailing.getMailingRecipients().iterator().next();
    // assertEquals( "Locale not found", mailingRecipient.getLocale(),
    // mailingRecipient.getMailingMessageLocale().getLocale() );
    // flushAndClearSession();
    //
    //
    // // TEST - BIG TEST 10K
    // expectedMailing = buildMailing( getUniqueString(), true, TEN_THOUSAND);
    // exception = false;
    // try {
    // mailingDAO.saveMailing( expectedMailing );
    // flushAndClearSession();
    // } catch (Exception e) {
    // exception = true;
    // }
    //
    // assertTrue("Failed 10k", exception == false);
    //
    //
    // actualMailing = mailingDAO.getMailingById( expectedMailing.getId() );
    // assertEquals( "Mailings are not equal", expectedMailing, actualMailing );
    // assertEquals( "Mailing Recipients are not equal", expectedMailing.getMailingRecipients(),
    // actualMailing.getMailingRecipients() );
    // assertEquals( "Mailing Message Locales are not equal",
    // expectedMailing.getMailingMessageLocales(), actualMailing.getMailingMessageLocales() );
    //
    // mailingRecipient = (MailingRecipient) actualMailing.getMailingRecipients().iterator().next();
    // assertEquals( "Locale not found", mailingRecipient.getLocale(),
    // mailingRecipient.getMailingMessageLocale().getLocale() );
    // flushAndClearSession();
  }

  public void testGetUsersWhoReceivedMessage()
  {
    Mailing expectedMailing1 = buildMailing( getUniqueString(), "Some text here" );
    getMailingDAO().saveMailing( expectedMailing1 );
    flushAndClearSession();

    Mailing expectedMailing2 = buildMailing( getUniqueString(), "Some text here 2" );
    getMailingDAO().saveMailing( expectedMailing2 );
    flushAndClearSession();

    List userList = getMailingDAO().getUsersWhoReceivedMessage( expectedMailing1.getMessage().getId() );

    assertTrue( "UserList should be 1 ", userList.size() == 1 );

  }

  public static Mailing buildMailing( String uniqueString, String mailingLocaleText )
  {
    // Save the message object
    Message message = getMessageDAO().saveMessage( MessageDAOImplTest.buildMessage( uniqueString ) );
    flushAndClearSession();

    Mailing mailing = new Mailing();

    String locale1 = "en";
    String locale2 = "es";

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( "JoeSender" + uniqueString );
    mailing.setMessage( message );
    mailing.addMailingRecipient( buildMailingRecipient( uniqueString, locale1 ) );

    mailing.addMailingMessageLocale( buildMailingMessageLocale( locale1, mailingLocaleText ) );
    mailing.addMailingMessageLocale( buildMailingMessageLocale( locale2, mailingLocaleText ) );

    return mailing;
  }

  public static MailingRecipient buildMailingRecipient( String uniqueString, String locale )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    getParticipantDAO().saveParticipant( pax );
    flushAndClearSession();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setDateSent( new Timestamp( new Date().getTime() ) );
    mailingRecipient.setUser( pax );
    mailingRecipient.setPreviewEmailAddress( "emailAddress" );
    mailingRecipient.setLocale( locale );
    mailingRecipient.addMailingRecipientData( buildMailingRecipientData( uniqueString ) );

    return mailingRecipient;
  }

  public static MailingMessageLocale buildMailingMessageLocale( String locale, String mailingLocaleText )
  {
    MailingMessageLocale messageMessageLocale = new MailingMessageLocale();

    messageMessageLocale.setLocale( locale );
    messageMessageLocale.setHtmlMessage( mailingLocaleText );
    messageMessageLocale.setPlainMessage( mailingLocaleText );
    messageMessageLocale.setTextMessage( "Text Message" );
    messageMessageLocale.setSubject( mailingLocaleText );

    return messageMessageLocale;
  }

  public static MailingRecipientData buildMailingRecipientData( String uniqueString )
  {
    MailingRecipientData mailingRecipientData = new MailingRecipientData();

    mailingRecipientData.setKey( "key" + uniqueString );
    mailingRecipientData.setValue( "value" + uniqueString );

    return mailingRecipientData;
  }

  /**
   * Returns a {@link MailingDAO} object.
   * 
   * @return a {@link MailingDAO} object.
   */
  private MailingDAO getMailingDAO()
  {
    return (MailingDAO)ApplicationContextFactory.getApplicationContext().getBean( MailingDAO.BEAN_NAME );
  }

  /**
   * Get the MessageDAO.
   * 
   * @return MessageDAO
   */
  private static MessageDAO getMessageDAO()
  {
    return (MessageDAO)ApplicationContextFactory.getApplicationContext().getBean( "messageDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   * 
   * @return ParticipantDAO
   */
  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( "participantDAO" );
  }
}
