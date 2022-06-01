/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/RecognitionClaimProcessingStrategy.java,v $
 */

package com.biperf.core.service.claim;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.activity.AbstractRecognitionActivity;
import com.biperf.core.domain.activity.MerchOrderActivity;
import com.biperf.core.domain.activity.RecognitionActivity;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.BudgetUsageOverAllocallatedException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.mobileapp.recognition.service.MobileNotificationService;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.EmailNotificationService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.PayoutCalculationFacts;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.service.promotion.engine.RecognitionFacts;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.TcccClientUtils;

/**
 * RecognitionClaimProcessingStrategy.
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
 * <td>Oct 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionClaimProcessingStrategy extends AbstractClaimProcessingStrategy
{
  private static final Log log = LogFactory.getLog( RecognitionClaimProcessingStrategy.class );

  private PromotionService promotionService;
  private EmailNotificationService emailNotificationService;
  private MailingService mailingService;
  private CountryService countryService;
  private MobileNotificationService mobileNotificationService;
  private UserService userService; 
  private CokeClientService cokeClientService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getPromotionPayoutType(com.biperf.core.domain.promotion.Promotion)
   * @param promotion
   * @return String promotion payout type
   */
  @Override
  public String getPromotionPayoutType( Promotion promotion )
  {
    return PromotionType.RECOGNITION;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getPayoutCalculationFacts(Approvable,
   *      com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.domain.participant.Participant)
   * @param approvable
   * @param promotion
   * @param participant
   * @return PayoutCalculationFacts
   */
  @Override
  protected PayoutCalculationFacts getPayoutCalculationFacts( Approvable approvable, Promotion promotion, Participant participant )
  {
    return new RecognitionFacts( (RecognitionClaim)approvable, participant );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#createActivitiesForApprovable(Approvable)
   * @param claim
   * @return List
   */
  protected List createActivitiesForApprovable( Approvable claim )
  {
    List activities = new ArrayList();

    RecognitionClaim recognitionClaim = (RecognitionClaim)claim;
    Promotion promotion = claim.getPromotion();

    List validParticipants = getValidParticipantsForApprovable( claim, promotion );

    // Add submitter activities
    Participant submitter = recognitionClaim.getSubmitter();
    /*
     * if the submitter is a valid submitter pax and there is at least one more valid pax involved
     * in the claim. For non-participant receivers there will be only one valid participant and this
     * is still valid.
     */
    if ( validParticipants.contains( submitter ) && validParticipants.size() > 1 )
    {
      AbstractRecognitionActivity activity = buildAbstractRecognitionActivity( promotion );

      activity.setClaim( recognitionClaim );
      activity.setPromotion( promotion );
      activity.setParticipant( submitter );
      activity.setNode( recognitionClaim.getNode() );

      // TODO What should this be???
      activity.setAwardQuantity( new Long( 0 ) );

      activity.setSubmissionDate( recognitionClaim.getSubmissionDate() );
      activity.setSubmitter( true );

      activities.add( activity );
    }

    // Add recipient activities
    Iterator claimRecipientIterator = recognitionClaim.getClaimRecipients().iterator();
    while ( claimRecipientIterator.hasNext() )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIterator.next();

      Participant recipient = claimRecipient.getRecipient();

      if ( recipient != null && validParticipants.contains( recipient ) && claimRecipient.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
      {
        AbstractRecognitionActivity activity = buildAbstractRecognitionActivity( promotion );

        activity.setClaim( recognitionClaim );
        activity.setPromotion( promotion );
        activity.setParticipant( recipient );
        activity.setNode( claimRecipient.getNode() );

        if ( ( (AbstractRecognitionPromotion)recognitionClaim.getPromotion() ).isAwardActive() || ( (RecognitionPromotion)recognitionClaim.getPromotion() ).isAllowPublicRecognitionPoints() )
        {
          if ( promotion.getAdihCashOption() )
          {
            //Client customization start
            String jobGrades = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_CASH_JOB_GRADE_AND_BELOW ).getStringVal();
            String[] jobGradesArray = jobGrades.split( "," );
            List<String> jobGradesList = new ArrayList<>( Arrays.asList( jobGradesArray ) );
            String userJobGrade = userService.getUserJobGradeCharValue( recipient.getId() );
            if ( !StringUtil.isNullOrEmpty( userJobGrade ) && ( jobGradesList.contains( userJobGrade ) ) )
            {
              activity.setAwardQuantity( claimRecipient.getAwardQuantity() );
            }
            else
            {
              Long usdCashAmt = TcccClientUtils.convertToUSD( cokeClientService, claimRecipient.getCustomCashAwardQuantity(), claimRecipient.getCashCurrencyCode() );
              Long points = TcccClientUtils.convertToPoints( cokeClientService, usdCashAmt, recipient.getId() );
              activity.setAwardQuantity( points );
              // Update claim recipient record as participant claimed the award.
              claimRecipient.setCashPaxClaimed( true );
              claimDAO.saveClaim( (Claim)claim );
            }
          }
          else
          {//Client customization end
            activity.setAwardQuantity( claimRecipient.getAwardQuantity() );
          }
        }
        else
        {
          activity.setAwardQuantity( new Long( 0 ) );
        }

        activity.setSubmissionDate( recognitionClaim.getSubmissionDate() );
        activity.setSubmitter( false );

        activities.add( activity );
      } // if
    } // while

    return activities;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getValidParticipantsForApprovable(Approvable,
   *      com.biperf.core.domain.promotion.Promotion)
   * @param approvable
   * @param promotion
   * @return List
   */
  @Override
  protected List getValidParticipantsForApprovable( Approvable approvable, Promotion promotion )
  {
    RecognitionClaim recognitionClaim = (RecognitionClaim)approvable;

    List validParticipants = new ArrayList();

    // if (promotionService.isParticipantMemberOfPromotionAudience(recognitionClaim.getSubmitter(),
    // promotion, true, recognitionClaim.getNode()))
    // {
    validParticipants.add( recognitionClaim.getSubmitter() );
    // }

    Iterator claimRecipientIterator = recognitionClaim.getClaimRecipients().iterator();
    while ( claimRecipientIterator.hasNext() )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIterator.next();
      Participant recipient = claimRecipient.getRecipient();
      // if (promotionService.isParticipantMemberOfPromotionAudience(recipient, promotion, false,
      // recognitionClaim.getNode()))
      // {
      if ( recipient != null )
      {
        validParticipants.add( recipient );
      }
      // }
    } // while

    return validParticipants;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#calculateBudget(java.util.Set,
   *      Approvable)
   * @param payoutCalculationResults
   * @param approvable
   */
  @Override
  protected void calculateBudget( Set payoutCalculationResults, Approvable approvable )
  {
    // Fix for bug#56006,55519 start
    calculateBudget( payoutCalculationResults, approvable, null );
    // Fix for bug#56006,55519 end
  }

  // Fix for bug#56006,55519 start
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#calculateBudget(java.util.Set,
   *      Approvable, BudgetSegment)
   * @param payoutCalculationResults
   * @param approvable
   * @param budgetSegment
   */
  @Override
  protected void calculateBudget( Set payoutCalculationResults, Approvable approvable, BudgetSegment budgetSegment )
  { // Fix for bug#56006,55519 end
    RecognitionPromotion promotion = (RecognitionPromotion)approvable.getPromotion();

    Claim claim = (Claim)approvable;
    // If promotion doesn't use budget, no calculation is necessary.
    if ( !promotion.isBudgetUsed() )
    {
      return;
    }

    // if no payout for this claim, no calculation is necessary.
    if ( payoutCalculationResults.isEmpty() )
    {
      return;
    }

    // Fix for bug#56006,55519 start
    Budget budget = null;
    if ( budgetSegment == null )
    {
      budget = promotionService.getRecognitionAvailableBudget( promotion.getId(), claim.getSubmitter().getId(), claim.getNode().getId(), null );
    }
    else
    {
      budget = promotionService.getRecognitionAvailableBudget( promotion.getId(), claim.getSubmitter().getId(), claim.getNode().getId(), budgetSegment );
    }
    // Fix for bug#56006,55519 end
    
    if ( budget == null )
    {
      throw new BeaconRuntimeException( "No budget found for Claim " + claim.getClaimNumber() );
    }

    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEDUCT ) );

    /*
     * This ReferenceVariableForClaimId which we set in budget is just a reference variable for
     * claimId which is used in GenericPostUpdateEventListener to save it in BudgetHistory table
     */
    budget.setReferenceVariableForClaimId( claim.getId() );

    BigDecimal currentBudget = budget.getCurrentValue();
    BigDecimal totalPayout = BigDecimal.ZERO;
    List journalList = new ArrayList();

    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
      Journal journal = payoutCalculationResult.getJournal();

      if ( journal != null )
      {
        // set budget on journal records as we go
        journal.setBudget( budget );
      }
      if ( isMerchandise( promotion ) )
      {
        Set activities = payoutCalculationResult.getContributingActivities();
        for ( Iterator activityIter = activities.iterator(); activityIter.hasNext(); )
        {
          MerchOrderActivity activity = (MerchOrderActivity)activityIter.next();
          if ( activity != null && activity.getMerchOrder() != null )
          {
            totalPayout = totalPayout.add( BigDecimal.valueOf( activity.getMerchOrder().getPoints() ) );
          }
        }
      }
      else
      {
        if ( journal != null )
        {
          if ( journal.getTransactionAmount() != null )
          {
            BudgetMaster budgetMaster = promotion.getBudgetMaster();
            BigDecimal calculatedBudgetValue = budgetMaster.isCentralBudget()
                ? BigDecimal.valueOf( journal.getTransactionAmount() )
                : calculateBudgetEquivalence( BigDecimal.valueOf( journal.getTransactionAmount().intValue() ), claim.getSubmitter(), journal.getParticipant() );
            totalPayout = totalPayout.add( calculatedBudgetValue );

            // Set the Budget Amount in Journal
            journal.setBudgetValue( calculatedBudgetValue );
          }
        }
        journalList.add( journal );
      }
    }

    // If the total payout falls within the budget, decrease the budget and mark budget on journal

    // Budget Internationalization - allow budget to go as low as -1 because of rounding
    // Fix for bug :54017
    // Removed +1 to allow the budget not to go below zero.
    if ( totalPayout.doubleValue() <= currentBudget.doubleValue() )
    {
      budget.setCurrentValue( currentBudget.subtract( totalPayout ) );
    }
    else
    {
      // Shouldn't get here. UI Layer should prevent.
      // Might want to handle this gracefully if this is a situation that occurs often.
      // BugFix 18327 Add one service error for exceeding the Budget limits Of an Approver for Pax
      // Budget Type
      log.error( "ERROR BudgetUsageOverAllocallatedException. budgetId=" + budget.getId() + " totalPayout=" + totalPayout + " currentBudget=" + currentBudget );
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.HARD_CAP_BUDGET_EXCEEDED_RECOGNITION, String.valueOf( currentBudget ) ) );
      throw new BeaconRuntimeException( new BudgetUsageOverAllocallatedException( serviceErrors ) );
    }
  }

  private BigDecimal calculateBudgetEquivalence( BigDecimal value, Participant submitter, Participant recipient )
  {
    if ( null != submitter.getPrimaryAddress() && null != recipient.getPrimaryAddress() )
    {
      BigDecimal usMediaValue = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
      BigDecimal recipientMediaValue = recipient.getPrimaryAddress().getAddress().getCountry().getBudgetMediaValue();
      value = BudgetUtils.applyMediaConversion( value, recipientMediaValue, usMediaValue );
    }
    return value;
  }

  /**
   * This postProcess implementation for Recognition will check to see if any awards were given for
   * this claim. If awards are not active on this promotion, need to send recognition email here,
   * otherwise JournalServiceImpl sends the recognition email AFTER the award has been deposited.
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#postProcess(java.util.Set,
   *      Approvable)
   * @param payoutCalculationResults
   * @param approvable
   * @throws ServiceErrorException 
   */
  @Override
  public void postProcess( Set payoutCalculationResults, Approvable approvable ) throws ServiceErrorException
  {
    try
    {
      RecognitionPromotion promotion = (RecognitionPromotion)approvable.getPromotion();
      RecognitionClaim recognitionClaim = (RecognitionClaim)approvable;

      if ( !promotion.isAwardActive()
          || ( promotion.isAwardActive() && promotion.getAwardType().isPointsAwardType() && promotion.getAwardAmountFixed() != null && promotion.getAwardAmountFixed().longValue() == 0 )
          || promotion.isCheersPromotion() ) //Client customizations for WIP #62128
      {

        if ( !promotion.isNoNotification() )
        {
          for ( ClaimRecipient claimRecipient : recognitionClaim.getClaimRecipients() )
          {
            if ( claimRecipient.getAwardQuantity() == null || claimRecipient.getAwardQuantity() == 0 )
            {
              if ( !recognitionClaim.isSkipStandardRecognitionEmail() )
              {
                if ( promotion.isIncludeCelebrations() )
                {
                  Mailing mailing = mailingService.buildRecognitionCelebrationMailing( recognitionClaim, null, false );

                  if ( mailing.getMailingRecipients().size() > 0 )
                  {
                    mailingService.submitMailing( mailing, null );
                  }
                }
                else if ( !promotion.isIncludeCelebrations() )
                {
                  //Client customization start
                  Mailing mailing = null;
                  if ( promotion.getAdihCashOption() )
                  {
                    Long usdCashAmt = TcccClientUtils.convertToUSD( cokeClientService, claimRecipient.getCustomCashAwardQuantity(), claimRecipient.getCashCurrencyCode() );
                    Long points = TcccClientUtils.convertToPoints( cokeClientService, usdCashAmt, claimRecipient.getRecipient().getId() );
                    mailing = mailingService.buildRecognitionMailingCustomOnlyPoints( recognitionClaim, String.valueOf( points ) );
                  }
                  else
                    mailing = mailingService.buildRecognitionMailing( recognitionClaim, null );
                  //Client customization end
                  if ( mailing.getMailingRecipients().size() > 0 )
                  {
                    mailingService.submitMailing( mailing, null );
                  }                  
                }
              }
            }
          }
        }
      }

    }
    catch( Exception ex )
    {
      log.error( "Error in postProcess ", ex );
      throw new ServiceErrorException( ex.getMessage(), ex );
    }

    try
    {
      RecognitionPromotion promotion = (RecognitionPromotion)approvable.getPromotion();
      RecognitionClaim recognitionClaim = (RecognitionClaim)approvable;

      // Check to see if notifications are turned off, if they are turned off, do not send push
      // notifications to mobile devices
      if ( !promotion.isNoNotification() && !recognitionClaim.isSkipStandardRecognitionEmail() )
      {
        // send mobile notifications
        mobileNotificationService.onRecognitionClaimSent( recognitionClaim.getId() );
      }
    }
    catch( Throwable t )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( "\n**********************************************************" );
      sb.append( "\n* ERROR in method RecognitionClaimProcessingStrategy.postProcess" );
      sb.append( "\n*********************mobileNotificationService*************************************" );
      sb.append( "\napprovable ID: " ).append( approvable.getId() );
      sb.append( "\nerror message: " ).append( t.getMessage() );
      sb.append( "\n**********************************************************" );
      sb.append( "\n* END ERROR" );
      sb.append( "\n**********************************************************" );

      log.error( sb.toString() );

    }
  }

  private AbstractRecognitionActivity buildAbstractRecognitionActivity( Promotion promotion )
  {
    if ( isMerchandise( (AbstractRecognitionPromotion)promotion ) )
    {
      return new MerchOrderActivity( GuidUtils.generateGuid() );
    }
    else
    {
      return new RecognitionActivity( GuidUtils.generateGuid() );
    }
  }

  private boolean isMerchandise( AbstractRecognitionPromotion promo )
  {
    return promo.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE );
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setEmailNotificationService( EmailNotificationService emailNotificationService )
  {
    this.emailNotificationService = emailNotificationService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  public void setMobileNotificationService( MobileNotificationService mobileNotificationService )
  {
    this.mobileNotificationService = mobileNotificationService;
  }
  
  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }


  public void setCokeClientService( CokeClientService cokeClientService )
  {
    this.cokeClientService = cokeClientService;
  }
}
