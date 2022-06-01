/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/Attic/AdminTestEstatementEmailProcess.java,v $
 */

package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.PageConstants;

/**
 * AdminTestEstatementEmailProcess is the process to generate estatement process for the given receipient
 * This process created for "EmailTestChangeRequestMay042015.doc"
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
 * <td>Venkatesh Dudam</td>
 * <td>July 7, 2015</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class AdminTestEstatementEmailProcess extends EStatementProcess
{

  private static final Log log = LogFactory.getLog( AdminTestEstatementEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test E Statement Email Process";
  public static final String BEAN_NAME = "adminTestEstatementEmailProcess";

  private String recipientUserName;
  private String recipientLocale;

  public AdminTestEstatementEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    log.debug( "Starting Admin Test E Statement Email Process for User Name: " + recipientUserName );
    try
    {
      Participant recipient = participantService.getParticipantByUserName( recipientUserName );
      if ( recipient != null )
      {
        Mailing mailing = new Mailing();
        Message message = messageService.getMessageByCMAssetCode( CM_ASSET_CODE );
        MailingRecipient mailingRecipient = buildMailingRecipient( recipient, mailing );
        Map dataMap = buildDataMap( mailingRecipient, recipient, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        mailing.addMailingRecipient( mailingRecipient );
        mailing.setGuid( GuidUtils.generateGuid() );
        mailing.setSender( "Estatement for " + mailingRecipient.getUser().getNameLFMWithComma() );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
        mailing.setMessage( message );
        this.mailingService.submitMailing( mailing, dataMap );
      }
      else
      {
        addComment( "User name " + recipientUserName + " not available in the system to launch Admin Test E Statement Email Process." );
      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing Admin Test E Statement Email Process with Username: " + recipientUserName );
    }
  }

  private MailingRecipient buildMailingRecipient( Participant recipient, Mailing mailing )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( recipient );
    mailingRecipient.setLocale( recipientLocale );
    mailingRecipient.setMailing( mailing );
    return mailingRecipient;
  }

  private Map buildDataMap( MailingRecipient mailingRecipient, Participant recipient, Locale locale )
  {
    // Mock Data for Testing
    Map dataMap = new HashMap();
    dataMap.put( "month", DateUtils.getPreviousMonthAsString( new Date() ) );
    dataMap.put( "year", DateUtils.getYearOfPreviousMonthAsString( new Date() ) );
    dataMap.put( "firstName", recipient.getFirstName() );
    dataMap.put( "lastName", recipient.getLastName() );
    dataMap.put( "beginningBalance", "10,000" );
    dataMap.put( "earnedThisPeriod", "1,000" );
    dataMap.put( "redeemedThisPeriod", "-200" );
    dataMap.put( "adjustmentsThisPeriod", "0" );
    dataMap.put( "endingBalance", "10,800" );

    Date transaction1Date = DateUtils.getDateAfterNumberOfDays( new Date(), -2 );
    MailingRecipientData transactionDate1 = new MailingRecipientData();
    transactionDate1.setKey( "transactionDate[" + 1 + "]" );
    transactionDate1.setValue( DateUtils.toDisplayString( transaction1Date, locale ) );
    mailingRecipient.addMailingRecipientData( transactionDate1 );

    MailingRecipientData transactionDescription1 = new MailingRecipientData();
    transactionDescription1.setKey( "transactionDescription[" + 1 + "]" );
    transactionDescription1.setValue( "Peer to Peer Recognition FY 2015" );
    mailingRecipient.addMailingRecipientData( transactionDescription1 );

    MailingRecipientData transactionAmount1 = new MailingRecipientData();
    transactionAmount1.setKey( "transactionAmount[" + 1 + "]" );
    transactionAmount1.setValue( "300" );
    mailingRecipient.addMailingRecipientData( transactionAmount1 );

    Date transaction2Date = DateUtils.getDateAfterNumberOfDays( new Date(), -15 );
    MailingRecipientData transactionDate2 = new MailingRecipientData();
    transactionDate2.setKey( "transactionDate[" + 2 + "]" );
    transactionDate2.setValue( DateUtils.toDisplayString( transaction2Date, locale ) );
    mailingRecipient.addMailingRecipientData( transactionDate2 );

    MailingRecipientData transactionDescription2 = new MailingRecipientData();
    transactionDescription2.setKey( "transactionDescription[" + 2 + "]" );
    transactionDescription2.setValue( "MERCH WITHDRAWAL for order S047623231" );
    mailingRecipient.addMailingRecipientData( transactionDescription2 );

    MailingRecipientData transactionAmount2 = new MailingRecipientData();
    transactionAmount2.setKey( "transactionAmount[" + 2 + "]" );
    transactionAmount2.setValue( "-100" );
    mailingRecipient.addMailingRecipientData( transactionAmount2 );

    Date transaction3Date = DateUtils.getDateAfterNumberOfDays( new Date(), -20 );
    MailingRecipientData transactionDate3 = new MailingRecipientData();
    transactionDate3.setKey( "transactionDate[" + 3 + "]" );
    transactionDate3.setValue( DateUtils.toDisplayString( transaction3Date, locale ) );
    mailingRecipient.addMailingRecipientData( transactionDate3 );

    MailingRecipientData transactionDescription3 = new MailingRecipientData();
    transactionDescription3.setKey( "transactionDescription[" + 3 + "]" );
    transactionDescription3.setValue( "Manager Discretionary FY 2015" );
    mailingRecipient.addMailingRecipientData( transactionDescription3 );

    MailingRecipientData transactionAmount3 = new MailingRecipientData();
    transactionAmount3.setKey( "transactionAmount[" + 3 + "]" );
    transactionAmount3.setValue( "200" );
    mailingRecipient.addMailingRecipientData( transactionAmount3 );

    Date transaction4Date = DateUtils.getDateAfterNumberOfDays( new Date(), -25 );
    MailingRecipientData transactionDate4 = new MailingRecipientData();
    transactionDate4.setKey( "transactionDate[" + 4 + "]" );
    transactionDate4.setValue( DateUtils.toDisplayString( transaction4Date, locale ) );
    mailingRecipient.addMailingRecipientData( transactionDate4 );

    MailingRecipientData transactionDescription4 = new MailingRecipientData();
    transactionDescription4.setKey( "transactionDescription[" + 4 + "]" );
    transactionDescription4.setValue( "AR Company Wide Recognition" );
    mailingRecipient.addMailingRecipientData( transactionDescription4 );

    MailingRecipientData transactionAmount4 = new MailingRecipientData();
    transactionAmount4.setKey( "transactionAmount[" + 4 + "]" );
    transactionAmount4.setValue( "200" );
    mailingRecipient.addMailingRecipientData( transactionAmount4 );

    Date transaction5Date = DateUtils.getDateAfterNumberOfDays( new Date(), -30 );
    MailingRecipientData transactionDate5 = new MailingRecipientData();
    transactionDate5.setKey( "transactionDate[" + 5 + "]" );
    transactionDate5.setValue( DateUtils.toDisplayString( transaction5Date, locale ) );
    mailingRecipient.addMailingRecipientData( transactionDate5 );

    MailingRecipientData transactionDescription5 = new MailingRecipientData();
    transactionDescription5.setKey( "transactionDescription[" + 5 + "]" );
    transactionDescription5.setValue( "MERCH WITHDRAWAL for order S71226573" );
    mailingRecipient.addMailingRecipientData( transactionDescription5 );

    MailingRecipientData transactionAmount5 = new MailingRecipientData();
    transactionAmount5.setKey( "transactionAmount[" + 5 + "]" );
    transactionAmount5.setValue( "-100" );
    mailingRecipient.addMailingRecipientData( transactionAmount5 );

    Date transaction6Date = DateUtils.getDateAfterNumberOfDays( new Date(), -60 );
    MailingRecipientData transactionDate6 = new MailingRecipientData();
    transactionDate6.setKey( "transactionDate[" + 6 + "]" );
    transactionDate6.setValue( DateUtils.toDisplayString( transaction6Date, locale ) );
    mailingRecipient.addMailingRecipientData( transactionDate6 );

    MailingRecipientData transactionDescription6 = new MailingRecipientData();
    transactionDescription6.setKey( "transactionDescription[" + 6 + "]" );
    transactionDescription6.setValue( "2015 Frequent Fitness Program" );
    mailingRecipient.addMailingRecipientData( transactionDescription6 );

    MailingRecipientData transactionAmount6 = new MailingRecipientData();
    transactionAmount6.setKey( "transactionAmount[" + 6 + "]" );
    transactionAmount6.setValue( "300" );
    mailingRecipient.addMailingRecipientData( transactionAmount6 );

    Date startDate = DateUtils.getDateAfterNumberOfDays( new Date(), -200 );
    Date endDate = DateUtils.getDateAfterNumberOfDays( new Date(), 0 );

    dataMap.put( "paidStart", DateUtils.toDisplayString( startDate, locale ) );
    dataMap.put( "paidEnd", DateUtils.toDisplayString( endDate, locale ) );
    dataMap.put( "myAccountLink", getSystemVariableService().getPropertyByNameAndEnvironment( "site.url" ).getStringVal() + PageConstants.PARTICIPANT_STATEMENTS );
    dataMap.put( "myPreferencesLink", getSystemVariableService().getPropertyByNameAndEnvironment( "site.url" ).getStringVal() + PageConstants.PARTICIPANT_PREFERENCES );

    return dataMap;
  }

  public String getRecipientUserName()
  {
    return recipientUserName;
  }

  public void setRecipientUserName( String recipientUserName )
  {
    this.recipientUserName = recipientUserName;
  }

  public String getRecipientLocale()
  {
    return recipientLocale;
  }

  public void setRecipientLocale( String recipientLocale )
  {
    this.recipientLocale = recipientLocale;
  }

}
