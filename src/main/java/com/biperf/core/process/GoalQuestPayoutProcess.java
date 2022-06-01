/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/GoalQuestPayoutProcess.java,v $
 */

package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.PendingGoalQuestAwardSummary;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.MailingBatchHolder;

/**
 * GoalQuestPayoutProcess.
 * 
 * Calculates the Goal Quest Payouts and Issues them. Also Creates a .csv file of report data and email
 * it to the user who submitted the approval.
 * 
 * This process is launched internally by the GoalQuestProcessConfirmationController
 *  
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
 * <td>Satish Viswanathan</td>
 * <td>Jan 31, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestPayoutProcess extends BaseProcessImpl
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name used to get an object of this class from the Spring application context.
   */
  public static final String BEAN_NAME = "goalQuestPayoutProcess";

  private static final Log log = LogFactory.getLog( GoalQuestPayoutProcess.class );

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ********** Required Report Parameters *************
  private String promotionId; // will terminate process if null

  /**
   * Service to call calculate the goal quest payout process
   */
  private GoalQuestService goalQuestService;

  private PromotionEngineService promotionEngineService;

  private PromotionService promotionService;

  private MailingBatch paxAchievedMailingBatch;
  private MailingBatch paxNotAchievedMailingBatch;
  private ProcessService processService;

  // ---------------------------------------------------------------------------
  // Process Methods
  // ---------------------------------------------------------------------------

  /**
   * Create report data extract file and email the file to the user, if there is data.
   * If there is no data, email the user informing the extract returns no data.
   * 
   */
  protected void onExecute()
  {
    // If required parameters are missing, write invocation log and stop.
    if ( isRequiredProcessParametersMissing() )
    {
      String msg = new String( "Required Parameters missing while executing " + BEAN_NAME + " is null. Process invocation ID = " + getProcessInvocationId() );
      log.warn( msg );
      addComment( msg );
    }
    else
    {
      boolean goalQuestPayOutSuccessful = false;
      GoalQuestPromotion promotion = null;
      try
      {
        // first get the promotion and update the flag
        promotion = (GoalQuestPromotion)promotionService.getPromotionById( getPromotionId( promotionId ) );
        // check if someone already ran the process, if true throw exception
        if ( promotion.isIssueAwardsRun() )
        {
          goalQuestPayOutSuccessful = true;
          throw new BeaconRuntimeException( "The Award process has been already run by user id: " + promotion.getAuditUpdateInfo().getModifiedBy() );
        }
        // update issue awards run flag to true
        promotion.setIssueAwardsRun( true );
        promotion.setIssueAwardsRunDate( DateUtils.getCurrentDate() );
        promotionService.savePromotion( promotion );

        // get the calculation results
        PendingGoalQuestAwardSummary pendingGoalQuestAwardSummary = goalQuestService.getGoalQuestAwardSummaryByPromotionId( getPromotionId( promotionId ) );
        List<GoalCalculationResult> goalCalculationList = pendingGoalQuestAwardSummary.getGoalCalculationResultList();

        int achieved = 0;
        int notAchieved = 0;
        int totErrors = 0;
        MailingBatchHolder mailBatchHolder = new MailingBatchHolder();

        if ( goalCalculationList != null && goalCalculationList.size() > 0 )
        {
          promotion = (GoalQuestPromotion)pendingGoalQuestAwardSummary.getPromotion();
          // Check for Email batch enabled and then create a batch for each promotion.
          paxAchievedMailingBatch = applyBatch( BEAN_NAME + " <i>Achieved</i> " + promotion.getName() );
          paxNotAchievedMailingBatch = applyBatch( BEAN_NAME + " <i>Not Achieved</i> " + promotion.getName() );
          mailBatchHolder.setPaxAchievedMailingBatch( paxAchievedMailingBatch );
          mailBatchHolder.setPaxNotAchievedMailingBatch( paxNotAchievedMailingBatch );

          // Check for Survey is exists for this promotion.
          // Making it non transactional way to implement the process..
          // Also find out achiever and non achiever have been selected for this

          goalQuestPayOutSuccessful = true;
          List<DepositProcessBean> depositProcessPointsList = new ArrayList<DepositProcessBean>();
          List<DepositProcessBean> depositProcessMerchList = new ArrayList<DepositProcessBean>();
          for ( Iterator<GoalCalculationResult> it = goalCalculationList.iterator(); it.hasNext(); )
          {
            GoalCalculationResult goalCalculationResult = (GoalCalculationResult)it.next();

            try
            {
              DepositProcessBean depositProcessBean = promotionEngineService.processGoalQuestPayoutCalculationResults( goalCalculationResult, promotion, mailBatchHolder );

              if ( depositProcessBean != null )
              {
                if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
                {
                  depositProcessPointsList.add( depositProcessBean );
                }
                else if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) || promotion.getAwardType().getCode().equals( PromotionAwardsType.TRAVEL_AWARD ) )
                {
                  if ( goalCalculationResult.isOwner() && depositProcessBean.getJournalId() != null )
                  {
                    depositProcessPointsList.add( depositProcessBean );
                  }
                  else
                  {
                    depositProcessMerchList.add( depositProcessBean );
                  }
                }
              }
            }
            catch( Exception e )
            {
              goalQuestPayOutSuccessful = false;
              totErrors++;
              StringBuffer failMessage = new StringBuffer( "Error Occurred while Goalquest Payout Process calculation" + " flag for promotion id: " );
              failMessage.append( getPromotionId() ).append( " The participant id is: " );
              if ( goalCalculationResult.getReciever() != null )
              {
                failMessage.append( goalCalculationResult.getReciever().getId() );
              }
              else if ( goalCalculationResult.getNodeOwner() != null )
              {
                failMessage.append( goalCalculationResult.getNodeOwner().getId() );
              }
              failMessage.append( " The error caused by: " );
              failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
              log.info( failMessage, e );
              addComment( failMessage.toString() );
            }

            if ( goalCalculationResult.isAchieved() )
            {
              achieved++;
            }
            else
            {
              notAchieved++;
            }
          }
          if ( depositProcessPointsList.size() > 0 )
          {
            goalQuestService.getJournalsDeposited( depositProcessPointsList, promotion.getId() );
          }
          if ( depositProcessMerchList.size() > 0 )
          {
            goalQuestService.createMerchOrders( depositProcessMerchList, promotion.getId() );
          }
        }

        addComment( "Emails have been sent to " + achieved + " Achievers" );
        addComment( "Emails have been sent to " + notAchieved + " Non-Achievers" );
        addComment( "Total Number of Errors " + totErrors );

        String batchComments = generateMailingBatchComments( mailBatchHolder );
        // Generate Extract report and send email notification
        goalQuestService.generateAndMailExtractReport( goalCalculationList, promotion, batchComments );

        if ( totErrors == 0 )
        {
          addComment( "Goal Quest Payout calculation is successful for Promotion id: " + getPromotionId() );
          final String successMessage = "Goal Quest Process completed successfully" + " and extract report email sent to user id: " + UserManager.getUserId();
          log.info( successMessage );
          addComment( successMessage );
          logMailingBatchHolderComments( mailBatchHolder );
        }
        else
        {
          addComment( "Goal Quest Payout calculation partially successful for Promotion id: " + getPromotionId() + batchComments );
          try
          {
            if ( !goalQuestPayOutSuccessful && promotion != null )
            {
              // set issue awards run flag to false and update promotion
              promotion.setIssueAwardsRun( false );
              promotion.setIssueAwardsRunDate( null );
              promotionService.savePromotion( promotion );
            }
          }
          catch( Exception e )
          {
            StringBuffer failMessage = new StringBuffer( "Error Occurred while updating the Promotion Issue Awards Run" + " flag for promotion id: " );
            failMessage.append( getPromotionId() ).append( "The error caused by: " );
            failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
            log.error( failMessage, e );
            addComment( failMessage.toString() );
          }
        }

      }
      catch( Exception e )
      {
        StringBuffer failureMsg = new StringBuffer( "Error Occurred while processing the GoalQuest Process." + " The error caused by: " );
        failureMsg.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
        try
        {
          if ( !goalQuestPayOutSuccessful && promotion != null )
          {
            // set issue awards run flag to false and update promotion
            promotion.setIssueAwardsRun( false );
            promotion.setIssueAwardsRunDate( null );
            promotionService.savePromotion( promotion );
          }
        }
        catch( Exception ex )
        {
          StringBuffer failMessage = new StringBuffer( "Error Occurred while updating the Promotion Issue Awards Run" + " flag for promotion id: " );
          failMessage.append( getPromotionId() ).append( "The error caused by: " );
          failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
          log.error( failMessage, e );
          addComment( failMessage.toString() );
        }
        log.error( failureMsg, e );
        addComment( failureMsg.toString() );
      }
    }
  } // END onExecute()

  private Long getPromotionId( String promotionId )
  {
    if ( promotionId == null || promotionId.equals( "" ) )
    {
      return null;
    }

    return new Long( promotionId );
  }

  private String generateMailingBatchComments( MailingBatchHolder mailingBatchHolder )
  {
    StringBuilder sb = new StringBuilder();

    String participantAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxAchievedMailingBatch() );
    sb.append( participantAchievedBatchEmailComments );

    String participantNotAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxNotAchievedMailingBatch() );
    sb.append( participantNotAchievedBatchEmailComments );

    String partnerAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerAchievedMailingBatch() );
    sb.append( partnerAchievedBatchEmailComments );

    String partnerAchievedNoPayoutBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerAchievedNoPayoutMailingBatch() );
    sb.append( partnerAchievedNoPayoutBatchEmailComments );

    String partnerNotAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerNotAchievedMailingBatch() );
    sb.append( partnerNotAchievedBatchEmailComments );

    return sb.toString();
  }

  /**
   * Not all Process Parameters for this process are required.
   * 
   * @return true if all required parameters are found, else false
   */
  private boolean isRequiredProcessParametersMissing()
  {
    // If required parameters are missing, write invocation comments and stop
    if ( promotionId == null )
    {
      return true;
    }
    return false;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public GoalQuestService getGoalQuestService()
  {
    return goalQuestService;
  }

  public void setGoalQuestService( GoalQuestService goalQuestService )
  {
    this.goalQuestService = goalQuestService;
  }

  public PromotionEngineService getPromotionEngineService()
  {
    return promotionEngineService;
  }

  public void setPromotionEngineService( PromotionEngineService promotionEngineService )
  {
    this.promotionEngineService = promotionEngineService;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public ProcessService getProcessService()
  {
    return processService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

}
