
package com.biperf.core.service.journal.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.activity.SweepstakesActivity;
import com.biperf.core.domain.activity.SweepstakesMerchLevelActivity;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.journal.JournalBillCode;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.AwardBanQDepositRetryProcess;
import com.biperf.core.process.JournalEntryOnlinePostProcess;
import com.biperf.core.process.JournalSweepstakesMailingProcess;
import com.biperf.core.process.PointsDepositProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.journal.DepositBillingCodeStrategy;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PostProcessJobsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.ProxyUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;

/**
 * JournalServiceImpl.
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
 * <td>jenniget</td>
 * <td>Sep 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class JournalServiceImpl implements JournalService
{
  private static final Log logger = LogFactory.getLog( JournalServiceImpl.class );

  private AwardBanQServiceFactory awardBanQServiceFactory;
  private MailingService mailingService;
  private MessageService messageService;
  private ProcessService processService;
  private CountryService countryService;
  private JournalDAO journalDAO;
  private ClaimDAO claimDAO;
  private DepositBillingCodeStrategy billingCodeStrategy;
  private SystemVariableService systemVariableService = null;
  private PostProcessJobsService postProcessJobsService;

  public void deleteJournal( Journal journal )
  {
    journalDAO.deleteJournal( journal );
  }

  public Journal saveJournalEntry( Journal journalEntry )
  {
    return getJournalDAO().saveJournalEntry( journalEntry );
  }

  public Journal saveAndDepositJournalEntry( Journal journalEntry ) throws ServiceErrorException
  {
    return saveAndDepositJournalEntry( journalEntry, false );
  }

  public Journal saveAndDepositJournalEntry( Journal journalEntry, boolean isRetriable ) throws ServiceErrorException
  {
    // persist journal entry so it gets an id assigned to it to be used by banq
    journalEntry = getJournalDAO().saveJournalEntry( journalEntry );

    if ( isSuspended( journalEntry ) )
    {
      journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.SUSPENDED ) );
    }

    if ( canDeposit( journalEntry ) )
    {
      boolean isFirstAttempt = true;
      depositJournalEntry( journalEntry, isRetriable, isFirstAttempt );
    }

    if ( journalEntry.getJournalStatusType().getCode().equals( JournalStatusType.POST ) )
    {
      // bug 73458 added postDepositStep for AwardBanQDepositRetryProcess to send mailing if deposit
      // successful
      postDepositStep( journalEntry );
    } // if POSTED

    return journalEntry;
  }

  public void claimJournalPostProcess( Journal journalEntry ) throws ServiceErrorException
  {
    try
    {
      if ( journalEntry.getPromotion().isRecognitionPromotion() )
      {
        AbstractRecognitionPromotion recognitionPromotion = (AbstractRecognitionPromotion)journalEntry.getPromotion();
        if ( !recognitionPromotion.isNoNotification() )
        {
          if ( recognitionPromotion.getAwardType().isPointsAwardType() )
          {
            Mailing mailing = buildRecognitionMailing( journalEntry );
            if ( mailing != null )
            {
              mailingService.submitMailing( mailing, null );
            }
          }
          // TODO: Celebration Manager Email
          /*
           * Mailing mailing = buildRecognitionMailing( journalEntry ); if ( mailing != null ) {
           * mailingService.submitMailing( mailing, null ); }
           */
        }
      }
      else if ( journalEntry.getPromotion().isQuizPromotion() )
      {
        Mailing mailing = buildQuizMailing( journalEntry );
        if ( mailing != null )
        {
          mailingService.submitMailing( mailing, null );
        }
      }
      else if ( journalEntry.getPromotion().isProductClaimPromotion() )
      {
        Mailing mailing = buildProductClaimMailing( journalEntry );
        if ( mailing != null )
        {
          mailingService.submitMailing( mailing, null );
        }
      }
      else if ( journalEntry.getPromotion().isNominationPromotion() )
      {
        List mailings = buildNominationMailing( journalEntry );
        for ( Iterator iter = mailings.iterator(); iter.hasNext(); )
        {
          Mailing mailing = (Mailing)iter.next();
          mailingService.submitMailing( mailing, null );
        }
      }
    }
    catch( Exception ex )
    {
      logger.error( "Error in postProcess ", ex );
      throw new ServiceErrorException( ex.getMessage(), ex );
    }
  }

  public boolean depositJournalEntry( Journal journalEntry, boolean isRetriable ) throws ServiceErrorException
  {
    boolean isFirstAttempt = false;
    // persist journal entry so it gets an id assigned to it to be used by banq
    journalEntry = getJournalDAO().saveJournalEntry( journalEntry );
    return depositJournalEntry( journalEntry, isRetriable, isFirstAttempt );
  }

  public void excecuteOnReversal( Long journalId )
  {
    journalDAO.excecuteOnReversal( journalId );
  }

  public boolean depositJournalEntry( Journal journalEntry, boolean isRetriable, boolean isFirstAttempt ) throws ServiceErrorException
  {
    boolean deposited = false;

    try
    {
      if ( journalEntry.getPromotion() != null && billingCodeStrategy != null )
      {
        billingCodeStrategy.setJournalBillingCodes( journalEntry );
      }

      if ( journalEntry.getParticipant().getOptOutAwards() )
      {
        journalEntry.setTransactionAmount( new Long( 0 ) );
      }

      // Post the transaction to the AwardBanQ only if Amount is not equal to Zero
      if ( !journalEntry.getTransactionAmount().equals( new Long( 0 ) ) )
      {
        // deposit
        awardBanQServiceFactory.getAwardBanQService().deposit( journalEntry );
      }

      // successfully deposited
      deposited = true;
      journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );

      try // bug 73458
      {
        // Don't send notification for a nomination reversal
        if ( journalEntry.getPromotion() != null && !journalEntry.isFileLoadDeposit() && ! ( journalEntry.getPromotion().isNominationPromotion() && journalEntry.getTransactionType().isReverse() ) )
        {
          Mailing mailing = buildDepositMailing( journalEntry );
          mailingService.submitMailing( mailing, null );
        }
      }
      catch( Exception e ) // bug 73458
      {
        // capture the Exception so the journal remains in POST status
        logger.error( "An exception occurred while sending deposit notice sms mailing. " + "(journal ID = " + journalEntry.getId() + ")", e );
      }
    }
    catch( ServiceErrorException e )
    {
      logger.error( e.getServiceErrorsCMText() );
      logger.error( "Error while trying to deposit to bank; retrying: " + isRetriable, e );

      // need to catch this exception, but it won't cause a rollback. Instead, it
      // should set the journal status back to approved and send an email to an admin.
      if ( isFirstAttempt )
      {
        journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );
      }

      // either retry or report
      if ( !isRetriable )
      {
        reportDepositFailure( journalEntry, e );
        throw e;
      }
      else if ( isFirstAttempt && !AwardBanQDepositRetryProcess.launchDepositRetryProcess( journalEntry ) )
      {
        reportDepositFailure( journalEntry, e );
        throw e;
      }
    }

    journalEntry = getJournalDAO().saveJournalEntry( journalEntry );

    return deposited;
  }

  /**
   * Build a mailing to be sent out for bank deposits.
   * 
   * @param journal
   * @return Mailing
   */
  private Mailing buildDepositMailing( Journal journal )
  {
    Mailing mailing = new Mailing();

    Message message = messageService.getMessageByCMAssetCode( MessageService.DEPOSIT_NOTICE_MESSAGE_CM_ASSET_CODE );

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setMessage( message );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( "Deposit Mailbox" );
    mailing.setSendSMSOnly( true );

    MailingRecipient mailingRecipient = new MailingRecipient();

    Participant participant = journal.getParticipant();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    if ( participant.getLanguageType() != null )
    {
      mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    Map dataMap = new HashMap();
    dataMap.put( "firstName", participant.getFirstName() );
    dataMap.put( "lastName", participant.getLastName() );
    dataMap.put( "programName", journal.getPromotion().getName() );

    if ( journal.getTransactionAmount() != null )
    {
      if ( journal.getTransactionAmount().longValue() > 1 )
      {
        dataMap.put( "manyAwardAmount", "TRUE" );
      }
      String awardAmount = journal.getTransactionAmount().toString();

      awardAmount = NumberFormatUtil.getUserLocaleBasedNumberFormat( journal.getTransactionAmount(), LocaleUtils.getLocale( mailingRecipient.getLocale().toString() ) );
      dataMap.put( "awardAmount", String.valueOf( awardAmount ) );
    }

    dataMap.put( "mediaType", getDepositMediaType( journal ) );

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    mailing.addMailingRecipient( mailingRecipient );

    return mailing;
  }

  /**
   * Get the award type for the journal for the deposit process
   */
  private String getDepositMediaType( Journal journal )
  {
    if ( journal.getPromotion().getAwardType() != null )
    {
      return journal.getPromotion().getAwardType().getAbbr();
    }
    else if ( journal.getAwardPayoutType() != null )
    {
      return journal.getAwardPayoutType().getAbbr();
    }
    // This case could possibly come up for a nomination reversal, but those do not send mailings
    else
    {
      return null;
    }
  }

  private void reportDepositFailure( Journal journalEntry, ServiceErrorException e )
  {
    Participant pax = journalEntry.getParticipant();
    String paxNameAndUserName = pax.getFirstName() + " " + pax.getLastName() + " (" + pax.getUserName() + ")";

    StringBuffer buf = new StringBuffer();
    buf.append( "An Error Occurred while trying to make a deposit for " );
    buf.append( paxNameAndUserName ).append( " in awardBanQ.  " );
    buf.append( "Journal #" ).append( journalEntry.getId().toString() );
    buf.append( " could not be deposited.  " );
    buf.append( "Errors: " ).append( e.getServiceErrorsCMText() ).append( "<br/><br/>" );

    String message = buf.toString();
    mailingService.submitSystemMailing( "Bank Deposit Failure for " + paxNameAndUserName, message, message );
  }

  /**
   * Gets the Journal by Id Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#getJournalById(java.lang.Long)
   * @param id
   * @return Journal
   */
  public Journal getJournalById( Long id )
  {
    return this.journalDAO.getJournalById( id );
  }

  /**
   * Gets the Journal by Id with associations Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#getJournalById(java.lang.Long,
   *      AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return Journal
   */
  public Journal getJournalById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Journal journal = this.journalDAO.getJournalById( id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( journal );
    }
    return journal;
  }

  /**
   * Gets the journal by ID Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#getJournalsByClaimIdAndUserId(java.lang.Long,
   *      java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param claimId
   * @param userId
   * @param journalAssociationRequest
   * @return List
   */
  public List getJournalsByClaimIdAndUserId( Long claimId, Long userId, AssociationRequestCollection journalAssociationRequest )
  {
    List journals;

    journals = this.journalDAO.getJournalsByClaimIdAndUserId( claimId, userId );

    for ( Iterator iter = journals.iterator(); iter.hasNext(); )
    {
      Journal journal = (Journal)iter.next();

      if ( journalAssociationRequest != null )
      {
        journalAssociationRequest.process( journal );
      }
    }

    return journals;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#approveJournal(java.lang.Long,
   *      java.lang.String, java.lang.String)
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal approveJournal( Long journalId, String comments, String reasonCode ) throws ServiceErrorException
  {
    Journal journal = getJournalById( journalId );

    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );
    journal.setComments( comments );
    journal.setReasonType( PromotionApprovalOptionReasonType.lookup( reasonCode ) );

    // TODO Don't automatically deposit, check to see if promotion is batch,
    // if it is don't deposit, just save.

    return saveAndDepositJournalEntry( journal );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#denyJournal(java.lang.Long,
   *      java.lang.String, java.lang.String)
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal denyJournal( Long journalId, String comments, String reasonCode )
  {
    Journal journal = getJournalById( journalId );

    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.DENY ) );
    journal.setComments( comments );
    journal.setReasonType( PromotionApprovalOptionReasonType.lookup( reasonCode ) );

    return saveJournalEntry( journal );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#postJournal(java.lang.Long,
   *      java.lang.String, java.lang.String)
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal postJournal( Long journalId, String comments, String reasonCode ) throws ServiceErrorException
  {
    Journal journal = getJournalById( journalId );

    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
    journal.setComments( comments );
    journal.setReasonType( PromotionApprovalOptionReasonType.lookup( reasonCode ) );

    return postJournal( journal );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#postJournal(java.lang.Long,
   *      java.lang.String, java.lang.String)
   * @param journal
   * @return Journal
   */
  public Journal postJournal( Journal journal ) throws ServiceErrorException
  {
    return saveAndDepositJournalEntry( journal );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#reverseJournal(java.lang.Long,
   *      java.lang.String, java.lang.String)
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal reverseJournal( Long journalId, String comments, String reasonCode ) throws ServiceErrorException
  {
    AssociationRequestCollection assocs = new AssociationRequestCollection();
    assocs.add( new JournalAssociationRequest( JournalAssociationRequest.ALL ) );
    Journal journal = getJournalById( journalId, assocs );
    Participant participant = journal.getParticipant();
    // Reverse transaction is a negative deposit of the orginal Transaction Amount
    long reverseTransactionAmount = journal.getTransactionAmount().longValue() * -1;
    Journal journalEntry = new Journal();
    journalEntry.setTransactionAmount( new Long( reverseTransactionAmount ) );
    journalEntry.setParticipant( participant );
    journalEntry.setPromotion( journal.getPromotion() );
    journalEntry.setGuid( GuidUtils.generateGuid() );
    journalEntry.setBudget( journal.getBudget() );
    journalEntry.setAccountNumber( participant.getAwardBanqNumber() );
    journalEntry.setTransactionDate( new Date() );
    journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );
    journalEntry.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.REVERSE ) );
    journalEntry.setTransactionDescription( "Reverse of Journal: " + journal.getId() );
    journalEntry.setComments( comments );
    journalEntry.setReasonType( PromotionApprovalOptionReasonType.lookup( reasonCode ) );
    journalEntry.setAwardPayoutType( journal.getAwardPayoutType() );

    if ( journal.getBillCodes().size() > 0 )
    {
      // Set Bill Codes
      Set<JournalBillCode> journalBillCodes = new HashSet<JournalBillCode>();
      JournalBillCode journalBillCode = journal.getBillCodes().iterator().next();
      JournalBillCode journalEntryBillCode = new JournalBillCode();
      journalEntryBillCode.setJournal( journalBillCode.getJournal() );
      journalEntryBillCode.setBillCode1( journalBillCode.getBillCode1() );
      journalEntryBillCode.setBillCode2( journalBillCode.getBillCode2() );
      journalEntryBillCode.setBillCode3( journalBillCode.getBillCode3() );
      journalEntryBillCode.setBillCode4( journalBillCode.getBillCode4() );
      journalEntryBillCode.setBillCode5( journalBillCode.getBillCode5() );
      journalEntryBillCode.setBillCode6( journalBillCode.getBillCode6() );
      journalEntryBillCode.setBillCode7( journalBillCode.getBillCode7() );
      journalEntryBillCode.setBillCode8( journalBillCode.getBillCode8() );
      journalEntryBillCode.setBillCode9( journalBillCode.getBillCode9() );
      journalEntryBillCode.setBillCode10( journalBillCode.getBillCode10() );
      journalBillCodes.add( journalEntryBillCode );
      journalEntry.setBillCodes( journalBillCodes );
    }
    journal = saveAndDepositJournalEntry( journalEntry );
    journal = adjustBudgetForJournal( journal );

    return journal;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#voidJournal(java.lang.Long,
   *      java.lang.String, java.lang.String)
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal voidJournal( Long journalId, String comments, String reasonCode )
  {
    Journal journal = getJournalById( journalId );

    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.VOID ) );
    journal.setComments( comments );
    journal.setReasonType( PromotionApprovalOptionReasonType.lookup( reasonCode ) );

    return saveJournalEntry( journal );
  }

  public JournalDAO getJournalDAO()
  {
    return journalDAO;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setJournalDAO( JournalDAO journalDAO )
  {
    this.journalDAO = journalDAO;
  }

  /**
   * Returns a list of journals that meet the specified criteria.
   * 
   * @param queryConstraint specifies the criteria that the returned journals meet.
   * @return a list of journals that meet the specified criteria, as a <code>List</code> of
   *         {@link Journal} objects.
   */
  public List<Journal> getJournalList( JournalQueryConstraint queryConstraint )
  {
    return journalDAO.getJournalList( queryConstraint );
  }

  /**
   * Returns a list of journals that meet the specified criteria.
   * 
   * @param queryConstraint specifies the criteria that the returned journals meet.
   * @param journalAssociationRequestCollection
   * @return a list of journals that meet the specified criteria, as a <code>List</code> of
   *         {@link Journal} objects.
   */
  public List<Journal> getJournalList( JournalQueryConstraint queryConstraint, AssociationRequestCollection journalAssociationRequestCollection )
  {
    List<Journal> journalList = journalDAO.getJournalList( queryConstraint );

    for ( Journal journal : journalList )
    {
      if ( journalAssociationRequestCollection != null )
      {
        journalAssociationRequestCollection.process( journal );
      }
    }
    return journalList;
  }

  /**
   * Decrease the budget by the amount in the Journal record (taking into account the country
   * equivalence)
   * 
   * @param journal
   * @return Journal
   */
  private Journal adjustBudgetForJournal( Journal journal )
  {
    Budget budget = journal.getBudget();

    // If there is no Budget set, there is nothing to do.
    if ( budget == null )
    {
      return journal;
    }

    BigDecimal awardQuantity = getAwardQuantityEquivalent( journal );

    budget.setCurrentValue( budget.getCurrentValue().subtract( awardQuantity ) );

    // Set the Budget Amount in Journal
    journal.setBudgetValue( awardQuantity );

    return journal;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#getAwardQuantityEquivalent(com.biperf.core.domain.journal.Journal,
   *      java.util.Map)
   * @param journal
   * @param countryMediaRatioMap
   * @return double
   */
  public BigDecimal getAwardQuantityEquivalent( Journal journal )
  {
    UserAddress primaryAddress = journal.getParticipant().getPrimaryAddress();
    BigDecimal US_MEDIA_VALUE = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
    BigDecimal RECIPIENT_MEDIA_VALUE = null;

    if ( primaryAddress != null )
    {
      RECIPIENT_MEDIA_VALUE = primaryAddress.getAddress().getCountry().getBudgetMediaValue();
    }

    return BudgetUtils.applyMediaConversion( BigDecimal.valueOf( journal.getTransactionAmount() ), RECIPIENT_MEDIA_VALUE, US_MEDIA_VALUE );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.JournalService#getTotalEarningsByMediaTypeAndUserId(java.lang.Long,
   *      java.lang.String)
   * @param userId
   * @param mediaType
   * @return Long totalEarnings
   */
  public Long getTotalEarningsByMediaTypeAndUserId( Long userId, String mediaType )
  {
    return journalDAO.getTotalEarningsByMediaTypeAndUserId( userId, mediaType );
  }

  /**
   * Builds a Mailing for a recogntion claim.
   * 
   * @param journal
   * @return Mailing
   */
  private Mailing buildRecognitionMailing( Journal journal )
  {
    RecognitionClaim claim = (RecognitionClaim)getClaimByJournal( journal );

    for ( Iterator iter = journal.getActivityJournals().iterator(); iter.hasNext(); )
    {
      ActivityJournal activityJournal = (ActivityJournal)iter.next();
      Activity activity = activityJournal.getActivity();
    }
    Mailing mailing = null;
    if ( !claim.isSkipStandardRecognitionEmail() )
    {
      RecognitionPromotion promotion = (RecognitionPromotion)claim.getPromotion();
      if ( promotion.isIncludeCelebrations() )
      {
        mailing = mailingService.buildRecognitionCelebrationMailing( claim, journal.getParticipant(), false );
      }
      else
      {
    	if ( promotion.getAdihCashOption() )
            mailing = mailingService.buildRecognitionMailingCustomOnlyPoints( claim, String.valueOf( journal.getTransactionAmount() ) );
        else
        	mailing = mailingService.buildRecognitionMailing( claim, journal.getParticipant() );
      }
    }
    return mailing;
  }

  /**
   * Builds Winner, Winner's Manager and Winner's Nominator Mailings for a nomination.
   * 
   * @param journal
   * @return Mailing
   */
  private List buildNominationMailing( Journal journal )
  {
    NominationClaim claim = (NominationClaim)getClaimByJournal( journal );

    // Bug # 37498 - added userId
    List mailings = mailingService.buildNominationMailing( claim, journal.getParticipant().getId() );

    return mailings;
  }

  /**
   * Builds a Mailing for a quiz.
   * 
   * @param journal
   * @return Mailing
   */
  private Mailing buildQuizMailing( Journal journal )
  {
    QuizClaim claim = (QuizClaim)getClaimByJournal( journal );
    Long depositedPoints = null;
    boolean isCalculationSuccessful = false;

    for ( Iterator iter = journal.getActivityJournals().iterator(); iter.hasNext(); )
    {
      ActivityJournal activityJournal = (ActivityJournal)iter.next();
      Activity activity = activityJournal.getActivity();
      if ( activityJournal.getJournal().getBudget() != null )
      {
        depositedPoints = activityJournal.getJournal().getTransactionAmount();
        isCalculationSuccessful = true;
      }
    }
    Mailing mailing = mailingService.buildQuizMailing( claim, isCalculationSuccessful, depositedPoints );

    return mailing;
  }

  /**
   * Builds a Mailing for a sweepstakes.
   * 
   * @param journal
   * @return Mailing
   */
  public Mailing buildSweepstakeMailing( Journal journal )
  {
    Mailing mailing = new Mailing();

    // Claim claim = getClaimByJournal( journal );

    Message message = messageService.getMessageByCMAssetCode( MessageService.SWEEPSTAKES_MESSAGE_CM_ASSET_CODE );

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setMessage( message );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( "Some Sweepstakes Mailbox" );

    MailingRecipient mailingRecipient = buildSweepstakesMailingRecipient( journal );

    mailing.addMailingRecipient( mailingRecipient );

    return mailing;
  }

  /**
   * Build the mailing for the product claim.
   * 
   * @param journal
   * @return Mailing
   */
  private Mailing buildProductClaimMailing( Journal journal )
  {
    Mailing mailing = new Mailing();
    MailingRecipient mailingRecipient = null;

    ProductClaim claim = (ProductClaim)getClaimByJournal( journal );

    // If the claim has multiple products, figure out the last product on this claim
    // so that we can send 1 mailing when the journal used is tied to the last product
    Long totalClaimAmount = new Long( 0 );
    boolean claimHasMultipleProducts = false;
    Set claimProducts = claim.getClaimProducts();
    Long lastProductId = new Long( 0 );
    if ( claimProducts.size() > 1 )
    {
      claimHasMultipleProducts = true;

      for ( Iterator iter = claimProducts.iterator(); iter.hasNext(); )
      {
        ClaimProduct claimProduct = (ClaimProduct)iter.next();
        lastProductId = claimProduct.getProduct().getId();
      }
    }

    boolean managerOverride = false;
    for ( Iterator iter = journal.getActivityJournals().iterator(); iter.hasNext(); )
    {
      ActivityJournal activityJournal = (ActivityJournal)iter.next();
      Activity activity = activityJournal.getActivity();

      // Figure out the total earnings for a claim that has multiple products on it
      if ( claimHasMultipleProducts && activity instanceof SalesActivity )
      {
        SalesActivity productClaimActivity = (SalesActivity)activity;
        if ( productClaimActivity.getProduct().getId().equals( lastProductId ) )
        {
          totalClaimAmount = journalDAO.getEarningsByClaimIdAndUserId( claim.getId(), journal.getPromotion().getId(), journal.getParticipant().getId() );
        }
        else
        {
          return null; // EARLY EXIT do not send emails unless this journal is tied to the LAST
                       // product on the claim
        }
      }

      if ( activity instanceof ManagerOverrideActivity )
      {
        managerOverride = true;
        break;
      }

    }

    Message message = null;
    if ( managerOverride )
    {
      message = messageService.getMessageByCMAssetCode( MessageService.MANAGER_OVERRIDE_MESSAGE_CM_ASSET_CODE );
    }
    else
    {
      message = messageService.getMessageByCMAssetCode( MessageService.PRODUCT_CLAIM_MESSAGE_CM_ASSET_CODE );
    }

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setMessage( message );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( "Some Claim Mailbox" );

    // Send email on the last product on the claim
    if ( claimHasMultipleProducts )
    {
      mailingRecipient = mailingService
          .buildMailingRecipientForProductClaimEmail( claim, journal.getParticipant(), totalClaimAmount, journal.getProcessStartDate(), journal.getProcessEndDate(), journal.getPromotion() );
    }
    else
    {
      mailingRecipient = mailingService.buildMailingRecipientForProductClaimEmail( claim,
                                                                                   journal.getParticipant(),
                                                                                   journal.getTransactionAmount(),
                                                                                   journal.getProcessStartDate(),
                                                                                   journal.getProcessEndDate(),
                                                                                   journal.getPromotion() );
    }

    mailing.addMailingRecipient( mailingRecipient );

    return mailing;
  }

  /**
   * @param journal
   * @return Claim
   */
  private Claim getClaimByJournal( Journal journal )
  {
    Claim claim = null;
    // Get the Activity for the participant of the journal
    for ( Iterator iter = journal.getActivityJournals().iterator(); iter.hasNext(); )
    {
      ActivityJournal activityJournal = (ActivityJournal)iter.next();
      Activity activity = activityJournal.getActivity();
      if ( activity.getParticipant().equals( journal.getParticipant() ) )
      {
        if ( activity.getClaim() != null )
        {
          claim = claimDAO.getClaimById( activity.getClaim().getId() );
          // Insure we don't have proxied version
          claim = (Claim)ProxyUtil.deproxy( claim );
        }
        else if ( activity instanceof NominationActivity )
        {
          NominationActivity nominationActivity = (NominationActivity)activity;
          if ( nominationActivity != null )
          {
            for ( Iterator iter1 = nominationActivity.getClaimGroup().getClaims().iterator(); iter1.hasNext(); )
            {
              NominationClaim nominationClaim = (NominationClaim)iter1.next();
              claim = claimDAO.getClaimById( nominationClaim.getId() );
              // Insure we don't have proxied version
              claim = (Claim)ProxyUtil.deproxy( claim );
            }
          }
        }
        break;
      }
    }

    return claim;
  }

  /**
   * @param journal
   * @return boolean
   */
  public boolean isJournalFromSweepstakes( Journal journal )
  {
    boolean isJournalFromSweepstakes = false;

    for ( Iterator iter = journal.getActivityJournals().iterator(); iter.hasNext(); )
    {
      ActivityJournal activityJournal = (ActivityJournal)iter.next();
      Activity activity = activityJournal.getActivity();
      if ( activity.getParticipant().equals( journal.getParticipant() ) && activity instanceof SweepstakesActivity )
      {
        isJournalFromSweepstakes = true;
        break;
      }
    }

    return isJournalFromSweepstakes;
  }

  /**
   * @param journal
   * @return MailingRecipient
   */
  private MailingRecipient buildSweepstakesMailingRecipient( Journal journal )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    Participant participant = journal.getParticipant();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    if ( participant.getLanguageType() != null )
    {
      mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    Map dataMap = new HashMap();
    dataMap.put( "firstName", participant.getFirstName() );
    dataMap.put( "lastName", participant.getLastName() );
    dataMap.put( "programName", journal.getPromotion().getName() );

    if ( journal.getTransactionAmount().longValue() > 1 )
    {
      dataMap.put( "manyAwardAmount", "TRUE" );
    }
    String awardAmount = journal.getTransactionAmount().toString();
    if ( journal.getTransactionAmount() != null )
    {
      awardAmount = NumberFormatUtil.getUserLocaleBasedNumberFormat( journal.getTransactionAmount(), LocaleUtils.getLocale( mailingRecipient.getLocale().toString() ) );
    }
    dataMap.put( "awardAmount", String.valueOf( awardAmount ) );

    // Try award type from promotion. May be null in nominations, so use award type from journal
    PromotionAwardsType awardType = journal.getPromotion().getAwardType();
    if ( awardType == null )
    {
      awardType = journal.getAwardPayoutType();
    }
    dataMap.put( "mediaType", awardType.getAbbr() );

    if ( journal.getPromotion().isRecognitionPromotion() && journal.getPromotion().getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.MERCHANDISE ) ) )
    {
      dataMap.put( "phoneNumber", systemVariableService.getPropertyByName( SystemVariableService.MERCHLINQ_ORDER_PHONE ).getStringVal() );
      MerchOrder merchOrder = getSweepstakesMerchOrderFromJournal( journal );
      if ( merchOrder != null )
      {
        String link = buildThankqOnlineRedeemLink( journal, merchOrder );
        dataMap.put( "siteLink", link );
        dataMap.put( "giftCodeOn", "true" );
        dataMap.put( "giftCode", merchOrder.getFullGiftCode() );
        dataMap.put( "referenceNumber", merchOrder.getReferenceNumber() );
        dataMap.put( "levelLabel", merchOrder.getPromoMerchProgramLevel().getDisplayLevelName() );
      }

    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  private String buildThankqOnlineRedeemLink( Journal journal, MerchOrder merchOrder )
  {
    if ( merchOrder == null )
    {
      return null;
    }

    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/merchLevelShopping.do?method=shopOnline&gc="
        + merchOrder.getFullGiftCode();
  }

  /**
   * @param journal
   * @return
   */
  private MerchOrder getSweepstakesMerchOrderFromJournal( Journal journal )
  {
    MerchOrder merchOrder = null;

    // TODO: For Recognition Submission, just collect the activity with a MerchOrder, like
    // SweepstakesMerchLevelActivity
    for ( Iterator iter = journal.getActivityJournals().iterator(); iter.hasNext(); )
    {
      ActivityJournal activityJournal = (ActivityJournal)iter.next();
      Activity activity = activityJournal.getActivity();
      if ( activity.getParticipant().equals( journal.getParticipant() ) && activity instanceof SweepstakesMerchLevelActivity )
      {
        SweepstakesMerchLevelActivity sweepstakesMerchLevelActivity = (SweepstakesMerchLevelActivity)activity;
        merchOrder = sweepstakesMerchLevelActivity.getMerchOrder();
        break;
      }
    }
    return merchOrder;
  }

  /**
   * Utility method to check if this Journal is depositable.
   * 
   * @param journalEntry
   * @return boolean
   */
  private boolean canDeposit( Journal journalEntry )
  {
    boolean canDeposit = true;

    // Only make a bank deposit if award type is perqs
    PromotionAwardsType awardType = null;
    if ( journalEntry.getPromotion().getAwardType() != null )
    {
      awardType = journalEntry.getPromotion().getAwardType();
    }
    else if ( journalEntry.getAwardPayoutType() != null )
    {
      awardType = journalEntry.getAwardPayoutType();
    }

    if ( awardType == null || !awardType.getCode().equals( PromotionAwardsType.POINTS ) )
    {
      canDeposit = false;
    }

    if ( canDeposit && isSuspended( journalEntry ) )
    {
      canDeposit = false;
    }

    // GQ and CP Merchandise promotion has manager override payout as points
    if ( journalEntry.getPromotion() != null && journalEntry.getPromotion().isGoalQuestOrChallengePointPromotion() && awardType != null
        && awardType.getCode().equals( PromotionAwardsType.MERCHANDISE ) )
    {
      canDeposit = true;
    }

    return canDeposit;
  }

  /**
   * Check to see if the journal should be suspended.
   * 
   * @param journalEntry
   * @return boolean
   */
  private boolean isSuspended( Journal journalEntry )
  {
    boolean isSuspended = false;

    ParticipantSuspensionStatus suspensionStatus = journalEntry.getParticipant().getSuspensionStatus();
    if ( suspensionStatus != null )
    {
      String suspensionStatusCode = suspensionStatus.getCode();
      if ( suspensionStatusCode.equals( ParticipantSuspensionStatus.SUSPEND_DEPOSITS ) || suspensionStatusCode.equals( ParticipantSuspensionStatus.SUSPEND_ALL ) )
      {
        isSuspended = true;
      }
    }

    return isSuspended;
  }

  // grabs values for post_process_jobs table from Journal object. May need to pass in trigger name
  // and group as well
  private Map<String, Object> createPostProcessInputParameters( Journal journal )
  {
    Map<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
    linkedHashMap.put( "processName", JournalEntryOnlinePostProcess.PROCESS_NAME );
    linkedHashMap.put( "processBeanName", JournalEntryOnlinePostProcess.BEAN_NAME );
    linkedHashMap.put( "promotionType", journal.getPromotion().getPromotionType().getCode() );
    linkedHashMap.put( "journalId", journal.getId() );

    return linkedHashMap;
  }

  /**
   * Overridden from @see com.biperf.core.service.journal.JournalService#getReverseJournalsByJournalId(java.lang.Long, java.lang.Long)
   * @param journalId
   * @param promotionId
   * @return count value of int
   */
  public int getReverseJournalsByJournalId( Long journalId, Long promotionId )
  {
    return journalDAO.getReverseJournalsByJournalId( journalId, promotionId );
  }

  /**
   *  
   * @see com.biperf.core.dao.journal.JournalDAO#isParticipantPayoutComplete(java.lang.Long,
   *      java.lang.Long)
   * @param userId
   * @param promotionId
   * @return boolean payout completed or not
   */
  public boolean isParticipantPayoutComplete( Long userId, Long promotionId )
  {
    return journalDAO.isParticipantPayoutComplete( userId, promotionId );
  }

  @Override
  public Long getJournalIdForReversedClaim( Long claimId, Long userId, Long approvalRound )
  {
    return journalDAO.getJournalIdForReversedClaim( claimId, userId, approvalRound );
  }

  /**
   * Get a list of userIds from Journal where awardType matches value passed and transaction date is
   * within a given range
   * 
   * @param awardType
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getUserIdsByAwardTypeWithinRange( String awardType, String startDate, String endDate )
  {
    return journalDAO.getUserIdsByAwardTypeWithinRange( awardType, startDate, endDate );
  }

  public Long getAwardAmountByClaimIdByUserId( Long claimId, Long userId )
  {
    return journalDAO.getAwardAmountByClaimIdByUserId( claimId, userId );
  }

  /**
   * Added method for bug 73458. Journal will be saved. Deposit and mailing will be processed in the 
   * background by Quartz process.
   */
  public void saveAndLaunchPointsDepositProcess( Journal journalEntry, boolean retryEnabled ) throws ServiceErrorException
  {

    journalEntry = getJournalDAO().saveJournalEntry( journalEntry );

    DepositProcessBean depositProcessBean = new DepositProcessBean();
    depositProcessBean.setJournalId( journalEntry.getId() );

    List depositProcessPointsList = new ArrayList<DepositProcessBean>();
    depositProcessPointsList.add( depositProcessBean );

    // deposit
    // send the reminder emails via a separate process
    LinkedHashMap<String, Object> paramValueMap = new LinkedHashMap<String, Object>();
    paramValueMap.put( "depositProcessPointsList", depositProcessPointsList );
    paramValueMap.put( "promotionId", journalEntry.getPromotion().getId() );
    if ( retryEnabled )
    {
      paramValueMap.put( "isRetriable", String.valueOf( Boolean.TRUE ) );
    }
    else
    {
      paramValueMap.put( "isRetriable", String.valueOf( Boolean.FALSE ) );
    }

    Process process = processService.createOrLoadSystemProcess( PointsDepositProcess.PROCESS_NAME, PointsDepositProcess.BEAN_NAME );
    processService.launchProcess( process, paramValueMap, UserManager.getUserId() );

  }

  /**
   * Added for bug 73458. This method will also be called by AwardBanQDepositRetryProcess
   * to send mailing if deposit is successful.
   */
  public void postDepositStep( Journal journalEntry ) throws ServiceErrorException
  {
    try
    {
      if ( journalEntry.getJournalStatusType().getCode().equals( JournalStatusType.POST ) )
      {
        if ( isJournalFromSweepstakes( journalEntry ) )
        {

          if ( ! ( journalEntry.getPromotion().isRecognitionPromotion() && journalEntry.getPromotion().getAwardType().isMerchandiseAwardType() ) )
          {
            // always schedule the mailing as separate process. That way if the mailing fails it
            // does not rollback the journal post status.
            JournalSweepstakesMailingProcess.launchJournalSweepsMailingProcess( journalEntry.getId() );
          }

        }
        else if ( getClaimByJournal( journalEntry ) != null )
        {
          // always schedule the mailing as separate process. That way if the mailing fails it does
          // not rollback the journal post status.
          boolean isJournalExists = postProcessJobsService.isJournalIdExists( journalEntry.getId() );
          if ( !isJournalExists )
          {
            // Insert into new post process jobs table
            PostProcessJobs postProcessJobs = postProcessJobsService.createPostProcessJobs( new PostProcessJobs(), createPostProcessInputParameters( journalEntry ) );
            postProcessJobsService.schedulePostProcess( postProcessJobs, JournalEntryOnlinePostProcess.PROCESS_NAME, JournalEntryOnlinePostProcess.BEAN_NAME );
          }
        }
      }
    }
    catch( Exception e )
    {
      // bug 73458 capture the Exception so the journal remains in POST status
      logger.error( "An exception occurred during postDepositStep. " + "(journal ID = " + journalEntry.getId() + ")", e );
    }
  }

  /**
   * Added for bug 73458. This method is called by PointsDepositProcess.
   * Get the journal object in the service layer to prevent participant object 
   * update during deposit because of journal.hbm.xml cascade="save-update" on participant. 
   */
  public boolean processPointsDepositJournal( Long journalId, boolean retriable ) throws ServiceErrorException
  {
    boolean success = false;

    Journal journal = this.getJournalById( journalId );
    Journal journalSaved = this.saveAndDepositJournalEntry( journal, retriable );
    
    if ( journalSaved.getJournalStatusType().getCode().equals( JournalStatusType.POST ) )
    {
      success = true;
    }

    return success;
  }
  
  /**
   * Added for bug 73458. This method is called by AwardBanQDepositRetryProcess.
   * Get the journal object in the service layer to prevent participant object 
   * update during deposit because of journal.hbm.xml cascade="save-update" on participant. 
   */
  public boolean processDepositRetryJournal( Long journalId, boolean retriable ) throws ServiceErrorException
  {
    boolean success = false;
    Journal journalEntry = this.getJournalById( journalId );
    success = this.depositJournalEntry( journalEntry, retriable );
    
    if ( success )
    {
      this.postDepositStep( journalEntry );
    }
    return success;
  }
  
  public void sendCustomRecognizedReceivedEmail( Claim claim )
  {
    RecognitionClaim recogClaim = (RecognitionClaim)claim;
    Mailing mailing = mailingService.buildRecognitionMailingCustom( recogClaim, recogClaim.getClaimRecipients().iterator().next().getRecipient() );
    mailingService.submitMailing( mailing, null );
  }

  
  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  public void setDepositBillingCodeStrategy( DepositBillingCodeStrategy billingCodeStrategy )
  {
    this.billingCodeStrategy = billingCodeStrategy;
  }

  /**
   * Set SystemVariableService
   * 
   * @param systemVariableService
   */
  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public void setPostProcessJobsService( PostProcessJobsService postProcessJobsService )
  {
    this.postProcessJobsService = postProcessJobsService;
  }

}
