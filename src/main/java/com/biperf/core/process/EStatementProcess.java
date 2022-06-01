/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/EStatementProcess.java,v $
 */

package com.biperf.core.process;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.AccountTransactionComparator;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.AccountSummary;
import com.biperf.core.domain.participant.AccountTransaction;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This process is to send monthly electronic statements to all participants.
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
 * <td>sathish</td>
 * <td>Nov 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EStatementProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( EStatementProcess.class );

  public static final String BEAN_NAME = "eStatementProcess";

  protected static final String CM_ASSET_CODE = "message_data.message.12544";

  private AwardBanQServiceFactory awardBanQServiceFactory;
  private String userId;
  private String password;
  private String startDate; // will default to 01/01/2000 if null
  private String endDate; // will default to 12/31/2100 if null
  private String campaignNumber;
  int successCount = 0;
  int failureCount = 0;

  private String sendOnlyPaxWithPoints;
  private Boolean isValidUserNamePassword = true; // for bug 66352

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.
   * BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    AssociationRequestCollection paxAssociationRequestCollection = new AssociationRequestCollection();
    if ( sendOnlyPaxWithPoints.equals( "no" ) ) // address and phone are needed for new enrollments
    {
      paxAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
      paxAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );
    }
    paxAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );

    boolean done = false;
    // bug fix:35971
    int pageNumber = 1;
    int recordsPerPage = 25;
    int totalRecords = 0;
    if ( startDate == null || startDate.equals( "" ) )
    {
      startDate = DateUtils.toDisplayString( DateUtils.getFirstDayOfPreviousMonth() );
    }
    if ( endDate == null || endDate.equals( "" ) )
    {
      endDate = DateUtils.toDisplayString( DateUtils.getLastDayOfPreviousMonth() );
    }
    Message message = messageService.getMessageByCMAssetCode( CM_ASSET_CODE );
    String paxStatementLink = getSystemVariableService().getPropertyByNameAndEnvironment( "site.url" ).getStringVal() + PageConstants.PARTICIPANT_STATEMENTS;
    String paxPrefLink = getSystemVariableService().getPropertyByNameAndEnvironment( "site.url" ).getStringVal() + PageConstants.PARTICIPANT_PREFERENCES;

    Long startingUserId = systemVariableService.getPropertyByName( SystemVariableService.ESTATEMENT_STARTING_USER_ID ).getLongVal();
    log.error( "*****ESTATEMENTPROCESS campaignNumber=" + campaignNumber + " START startingUserId=" + startingUserId );

    // while ( !done )
    // {
    if ( sendOnlyPaxWithPoints.equals( "yes" ) || sendOnlyPaxWithPoints.equals( "no" ) )
    {
      List<Long> activePaxIds = participantService.getAllActivePaxIdsInCampaignForEstatements( campaignNumber, sendOnlyPaxWithPoints, startingUserId );

      if ( activePaxIds != null && !activePaxIds.isEmpty() )
      {
        log.error( " : " + activePaxIds.size() + " pax ids to be processed." );

        try
        {

          for ( Long paxId : activePaxIds )
          {
            if ( isValidUserNamePassword )
            {

              // List eStatementParticipants = participantService.
              // getAllActivePaxWithCommunicationPreferenceInCampaign( campaignNumber,
              // ParticipantPreferenceCommunicationsType
              // .lookup( ParticipantPreferenceCommunicationsType.E_STATEMENTS ),
              // paxAssociationRequestCollection , pageNumber, recordsPerPage);
              Participant pax = participantService.getParticipantByIdWithAssociations( paxId, paxAssociationRequestCollection );
              List<Participant> eStatementParticipants = new ArrayList<Participant>();
              eStatementParticipants.add( pax );

              log.error( "ESTATEMENTPROCESS campaignNumber=" + campaignNumber + " paxId=" + pax.getId() + " start" );
              if ( eStatementParticipants != null && eStatementParticipants.size() > 0 )
              {
                // Bug 66119
                /*
                 * if ( sendOnlyPaxWithPoints.equals( "no" ) && ( pax.getAwardBanqNumber() == null
                 * || pax.getAwardBanqNumber().trim().equals( "" )) ) { log.error(
                 * "ESTATEMENTPROCESS campaignNumber="+campaignNumber+" paxId="+pax.getId() +
                 * " enrollParticipantsInAwardBanQ" ); enrollParticipantsInAwardBanQ(
                 * eStatementParticipants ); }
                 */

                createEStatementMailings( eStatementParticipants, message, paxStatementLink, paxPrefLink );

                // if( eStatementParticipants.size() < recordsPerPage )
                // {
                // done = true;
                // }
                totalRecords += eStatementParticipants.size();
                // if(HibernateSessionManager.getSession()!=null)
                // {
                // HibernateSessionManager.getSession().flush();
                // HibernateSessionManager.getSession().clear();
                // }

              }
              log.error( "ESTATEMENTPROCESS campaignNumber=" + campaignNumber + " paxId=" + pax.getId() + " finish" );
              // else
              // {
              // done = true;
              // }

              // pageNumber++;
            }
          } // for

        }
        catch( Exception e )
        {
          log.error( "Error occurred in the while loop. " + e.getMessage(), e );
        }
      } // activePaxIds
      // } //while

      addComment( "The eStatement process has completed. Total e-statments sent : " + successCount + ". Failure records are  : " + failureCount );
      // String summaryString = "The eStatement process has completed. Total e-statments sent : "
      // +successCount + ". Failure records are : " + failureCount;
      sendSummaryMessage( successCount, failureCount );

      if ( totalRecords == 0 )
      {
        addComment( "Could not run eStatement Process because there are no participants set up for eStatements." );
      }
      log.error( "*****ESTATEMENTPROCESS campaignNumber=" + campaignNumber + " FINISH startingUserId=" + startingUserId );
    }
    else
    {
      addComment( "Statements did not process. picklist.yesno values need to be yes/no for sendOnlyPaxWithPoints parameter." );
    }
    isValidUserNamePassword = true; // reseting to initial state as it is singleton bean
  }

  private void enrollParticipantsInAwardBanQ( List eStatementParticipants ) throws JsonGenerationException, JsonMappingException, IOException
  {
    AwardBanQService service = getAwardBanQServiceFactory().getAwardBanQService();
    Iterator eStatementPaxIter = eStatementParticipants.iterator();
    while ( eStatementPaxIter.hasNext() )
    {
      Participant participant = (Participant)eStatementPaxIter.next();
      try
      {
        service.enrollParticipantInAwardBanQWebService( participant );
      }
      catch( ServiceErrorException e )
      {
        // nop for now
        log.error( e.getMessage(), e );
        addComment( "Could not enroll participant to awardbanq for " + participant.getFirstName() + " " + participant.getLastName() + " (" + participant.getUserName() + ") because "
            + e.getMessage() );
      }
    }
  }

  protected void createEStatementMailings( List eStatementParticipants, final Message message, final String paxStatementLink, final String paxPrefLink )
  {
    Iterator eStatementPaxIter = eStatementParticipants.iterator();
    while ( eStatementPaxIter.hasNext() && isValidUserNamePassword )
    {
      Mailing eStatementMailing = new Mailing();
      AccountSummary accountSummary = null;

      Participant participant = (Participant)eStatementPaxIter.next();
      boolean okToSendMail = true;
      MailingRecipient mailingRecipient = new MailingRecipient();
      mailingRecipient.setGuid( GuidUtils.generateGuid() );
      mailingRecipient.setUser( participant );
      mailingRecipient.setMailing( eStatementMailing );
      if ( participant.getLanguageType() != null )
      {
        mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
      }
      else
      {
        mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
      }

      try
      {
        Long[] paxIds = { participant.getId() };
        Map summaries = getAwardBanQServiceFactory().getAwardBanQService()
            .getMultipleAccountSummariesByParticipantIdAndDateRange( paxIds, DateUtils.toDate( startDate ), DateUtils.toDate( endDate ), userId, password );
        accountSummary = (AccountSummary)summaries.get( participant.getId() );

        // verify includeAllUsers flag. when includeAllUsers=no, skip users with
        // i) ending balance is zero OR
        // ii) no activity in the selected date range
        if ( sendOnlyPaxWithPoints.equals( "yes" ) )
        {
          okToSendMail = false;

          if ( accountSummary != null && ( accountSummary.getAccountTransactions() != null && accountSummary.getAccountTransactions().size() > 0 || accountSummary.getEndingBalance() > 0 ) )
          {
            okToSendMail = true;
          }
        }

      }
      // These exceptions are thrown when validating the participant
      catch( ServiceErrorException seewr )
      {
        failureCount++;
        log.error( "Exception encounterd getting statement from awardbanq: " + seewr.getServiceErrorsCMText().get( 0 ) );
        addComment( "Could not get eStatement for " + participant.getFirstName() + " " + participant.getLastName() + " (" + participant.getUserName() + ") because "
            + seewr.getServiceErrorsCMText().get( 0 ) );
        okToSendMail = false;
      }
      // Anything else is caught here
      catch( Exception e )
      {
        failureCount++;
        log.error( "Exception encountered getting statement from awardbanq: " + e.getMessage() );
        okToSendMail = false;
        if ( e.getMessage() != null && !e.getMessage().equals( "" ) )
        {
          // if the message contains the below string that means it is an invalid user/password
          // combination and we should break out of the process as no statement data will
          // be retrieved.
          // added for bug 66352
          if ( e.getMessage().contains( "Invalid userid-password" ) )
          {
            isValidUserNamePassword = false;
          }
          if ( e.getMessage().indexOf( "[-42]" ) != -1 )
          {
            addComment( "Could not run eStatement Process for " + participant.getFirstName() + " " + participant.getLastName() + " (" + participant.getUserName() + ") because " + e.getMessage() );
            break;
          }

          addComment( "Could not get eStatement for " + participant.getFirstName() + " " + participant.getLastName() + " (" + participant.getUserName() + ") because " + e.getMessage() );
        }
      }
      // Only prepare and send the message for this pax if there are no exceptions getting
      // their statement.

      if ( okToSendMail )
      {
        MailingRecipientData month = new MailingRecipientData();
        month.setKey( "month" );
        month.setValue( DateUtils.getPreviousMonthAsString( new Date() ) );
        mailingRecipient.addMailingRecipientData( month );

        MailingRecipientData year = new MailingRecipientData();
        year.setKey( "year" );
        year.setValue( DateUtils.getYearOfPreviousMonthAsString( new Date() ) );
        mailingRecipient.addMailingRecipientData( year );

        MailingRecipientData firstName = new MailingRecipientData();
        firstName.setKey( "firstName" );
        firstName.setValue( participant.getFirstName() );
        mailingRecipient.addMailingRecipientData( firstName );

        MailingRecipientData lastName = new MailingRecipientData();
        lastName.setKey( "lastName" );
        lastName.setValue( participant.getLastName() );
        mailingRecipient.addMailingRecipientData( lastName );

        String beginningBalanceString = null;
        String earnedThisPeriodString = null;
        String redeemedThisPeriodString = null;
        String adjustmentsThisPeriodString = null;
        String endingBalanceString = null;
        long accountTransactionsSize = 0;

        if ( accountSummary != null )
        {
          beginningBalanceString = new Long( accountSummary.getBeginningBalance() ).toString();
          earnedThisPeriodString = new Long( accountSummary.getEarnedThisPeriod() ).toString();
          redeemedThisPeriodString = new Long( accountSummary.getRedeemedThisPeriod() ).toString();
          adjustmentsThisPeriodString = new Long( accountSummary.getAdjustmentsThisPeriod() ).toString();
          endingBalanceString = new Long( accountSummary.getEndingBalance() ).toString();
        }
        else
        {
          beginningBalanceString = "0";
          earnedThisPeriodString = "0";
          redeemedThisPeriodString = "0";
          adjustmentsThisPeriodString = "0";
          endingBalanceString = "0";
        }

        if ( accountSummary != null && accountSummary.getAccountTransactions() != null )
        {
          accountTransactionsSize = accountSummary.getAccountTransactions().size();
        }
        else
        {
          accountTransactionsSize = 0;
        }

        MailingRecipientData beginningBalance = new MailingRecipientData();
        beginningBalance.setKey( "beginningBalance" );
        beginningBalance.setValue( beginningBalanceString );
        mailingRecipient.addMailingRecipientData( beginningBalance );

        MailingRecipientData earnedThisPeriod = new MailingRecipientData();
        earnedThisPeriod.setKey( "earnedThisPeriod" );
        earnedThisPeriod.setValue( earnedThisPeriodString );
        mailingRecipient.addMailingRecipientData( earnedThisPeriod );

        MailingRecipientData redeemedThisPeriod = new MailingRecipientData();
        redeemedThisPeriod.setKey( "redeemedThisPeriod" );
        redeemedThisPeriod.setValue( redeemedThisPeriodString );
        mailingRecipient.addMailingRecipientData( redeemedThisPeriod );

        MailingRecipientData adjustmentsThisPeriod = new MailingRecipientData();
        adjustmentsThisPeriod.setKey( "adjustmentsThisPeriod" );
        adjustmentsThisPeriod.setValue( adjustmentsThisPeriodString );
        mailingRecipient.addMailingRecipientData( adjustmentsThisPeriod );

        MailingRecipientData endingBalance = new MailingRecipientData();
        endingBalance.setKey( "endingBalance" );
        endingBalance.setValue( endingBalanceString );
        mailingRecipient.addMailingRecipientData( endingBalance );

        MailingRecipientData noActivity = new MailingRecipientData();
        noActivity.setKey( "noActivity" );
        if ( accountTransactionsSize <= 0 )
        {
          noActivity.setValue( "true" );
        }

        mailingRecipient.addMailingRecipientData( noActivity );

        List transactions = new ArrayList();

        for ( int counter = 0; counter < accountTransactionsSize; counter++ )
        {
          AccountTransaction accountTransaction = (AccountTransaction)accountSummary.getAccountTransactions().get( counter );
          // String tdate = DateUtils.toDisplayString( accountTransaction.getTransactionDate(),
          // LocaleUtils.getLocale(mailingRecipient.getLocale()) );
          // accountTransaction.setTransactionDate( DateUtils.toDate( tdate ) );
          accountTransaction.setTransactionDate( accountTransaction.getTransactionDate() );

          transactions.add( accountTransaction );
          Collections.sort( transactions, new AccountTransactionComparator() );
        }

        Iterator it = transactions.iterator();

        for ( int counter = transactions.size(); it.hasNext(); counter-- )
        {
          AccountTransaction at = (AccountTransaction)it.next();
          MailingRecipientData transactionDate = new MailingRecipientData();
          transactionDate.setKey( "transactionDate[" + counter + "]" );
          // transactionDate.setValue( at.getFormattedTransactionDate() );
          transactionDate.setValue( DateUtils.toDisplayString( at.getTransactionDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
          mailingRecipient.addMailingRecipientData( transactionDate );

          MailingRecipientData transactionDescription = new MailingRecipientData();
          transactionDescription.setKey( "transactionDescription[" + counter + "]" );
          transactionDescription.setValue( at.getTransactionDescription() );
          mailingRecipient.addMailingRecipientData( transactionDescription );

          MailingRecipientData transactionAmount = new MailingRecipientData();
          transactionAmount.setKey( "transactionAmount[" + counter + "]" );
          transactionAmount.setValue( new Long( at.getTransactionAmount() ).toString() );
          mailingRecipient.addMailingRecipientData( transactionAmount );
        }

        MailingRecipientData paidStart = new MailingRecipientData();
        paidStart.setKey( "paidStart" );
        paidStart.setValue( DateUtils.toDisplayString( DateUtils.toDate( startDate ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
        // paidStart.setValue( DateUtils.toDisplayString( DateUtils.getFirstDayOfPreviousMonth() )
        // );
        mailingRecipient.addMailingRecipientData( paidStart );

        MailingRecipientData paidEnd = new MailingRecipientData();
        paidEnd.setKey( "paidEnd" );
        paidEnd.setValue( DateUtils.toDisplayString( DateUtils.toDate( endDate ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
        // paidEnd.setValue( DateUtils.toDisplayString( DateUtils.getLastDayOfPreviousMonth() ) );
        mailingRecipient.addMailingRecipientData( paidEnd );

        MailingRecipientData myAccountLink = new MailingRecipientData();
        myAccountLink.setKey( "myAccountLink" );
        myAccountLink.setValue( paxStatementLink );
        mailingRecipient.addMailingRecipientData( myAccountLink );

        MailingRecipientData myPreferencesLink = new MailingRecipientData();
        myPreferencesLink.setKey( "myPreferencesLink" );
        myPreferencesLink.setValue( paxPrefLink );
        mailingRecipient.addMailingRecipientData( myPreferencesLink );

        eStatementMailing.addMailingRecipient( mailingRecipient );

        eStatementMailing.setGuid( GuidUtils.generateGuid() );
        eStatementMailing.setSender( "Estatement for " + mailingRecipient.getUser().getNameLFMWithComma() );
        eStatementMailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        eStatementMailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
        eStatementMailing.setMessage( message );

        this.mailingService.submitMailing( eStatementMailing, null );
        successCount++;
      }

    }

  }

  /**
   * Composes and sends the summary e-mail to the "run by" user.
   */
  private void sendSummaryMessage( int successCount, int failCount )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "successCount", new Integer( successCount ) );
    objectMap.put( "failCount", new Integer( failCount ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeEstatement( MessageService.ESTATEMENT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      addComment( "eStatement process summary email sent to user id: " + UserManager.getUserId() );
    }
    catch( Exception e )
    {

    }
  }

  private Mailing composeEstatement( String cmAssetCode, String mailingType )
  {

    Mailing mailing = composeMail();

    mailing.setMailingType( MailingType.lookup( mailingType ) );

    Message message = messageService.getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );
    return mailing;
  }

  /**
   * Creates a new mailing object and add Guid, Sender and Delivery Date to it
   * 
   * @return a partially assembled mailing object
   */
  protected Mailing composeMail()
  {
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    mailing.setDeliveryDate( deliveryDate );

    return mailing;

  }

  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    return awardBanQServiceFactory;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getCampaignNumber()
  {
    return campaignNumber;
  }

  public void setCampaignNumber( String campaignNumber )
  {
    this.campaignNumber = campaignNumber;
  }

  public String getSendOnlyPaxWithPoints()
  {
    return sendOnlyPaxWithPoints;
  }

  public void setSendOnlyPaxWithPoints( String sendOnlyPaxWithPoints )
  {
    this.sendOnlyPaxWithPoints = sendOnlyPaxWithPoints;
  }

}
