/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ChallengepointPayoutProcess.java,v $
 */

package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.challengepoint.PendingChallengepointAwardSummary;
import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.MailingBatchHolder;

/**
 * ChallengepointPayoutProcess.
 * 
 * Calculates the Challengepoint Payouts and Issues them. Also Creates a .csv file of report data and email
 * it to the user who submitted the approval.
 * 
 * This process is launched internally by the ChallengepointProcessConfirmationController
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
 * <td>Prabu</td>
 * <td>Aug 5, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengepointPayoutProcess extends BaseProcessImpl
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name used to get an object of this class from the Spring application context.
   */
  public static final String BEAN_NAME = "challengepointPayoutProcess";

  private static final Log log = LogFactory.getLog( ChallengepointPayoutProcess.class );

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ********** Required Report Parameters *************
  private String promotionId; // will terminate process if null

  /**
   * Service to call calculate the goal quest payout process
   */
  private ChallengepointProgressService challengepointProgressService;

  private ChallengePointService challengePointService;

  private PromotionEngineService promotionEngineService;

  private PromotionService promotionService;
  private GoalQuestService goalQuestService;

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
      boolean challengepointPayOutSuccessful = false;
      ChallengePointPromotion promotion = null;
      try
      {
        // first get the promotion and update the flag
        promotion = (ChallengePointPromotion)promotionService.getPromotionById( getPromotionId( promotionId ) );
        // check if someone already ran the process, if true throw exception
        if ( promotion.isIssueAwardsRun() )
        {
          challengepointPayOutSuccessful = true;
          throw new BeaconRuntimeException( "The Award process has been already run by user id: " + promotion.getAuditUpdateInfo().getModifiedBy() );
        }

        if ( promotion.isAfterFinalProcessDate() )
        {
          // update issue awards run flag to true
          promotion.setIssueAwardsRun( true );
          promotion.setIssueAwardsRunDate( DateUtils.getCurrentDate() );
          promotionService.savePromotion( promotion );
        }

        // get the calculation results
        PendingChallengepointAwardSummary pendingChallengepointAwardSummary = challengepointProgressService.getAwardSummaryByLevels( getPromotionId( promotionId ) );
        List<ChallengepointPaxAwardValueBean> cpCalculationList = pendingChallengepointAwardSummary.getChallengepointCalculationResultList();

        promotion = (ChallengePointPromotion)pendingChallengepointAwardSummary.getPromotion();
        List<DepositProcessBean> depositProcessMerchList = new ArrayList<DepositProcessBean>();
        List<DepositProcessBean> depositProcessPointsList = new ArrayList<DepositProcessBean>();

        int totErrors = 0;

        MailingBatchHolder mailingBatchHolder = new MailingBatchHolder();
        if ( promotion != null && cpCalculationList != null )
        {
          // Check for Email batch enabled and then create a batch for each promotion.
          mailingBatchHolder.setPaxAchievedMailingBatch( applyBatch( BEAN_NAME + " <i>Achieved</i> " + promotion.getName() ) );
          mailingBatchHolder.setPaxNotAchievedMailingBatch( applyBatch( BEAN_NAME + " <i>NOT Achieved</i> " + promotion.getName() ) );

          mailingBatchHolder.setPartnerAchievedMailingBatch( applyBatch( BEAN_NAME + " <i>Partner Achieved</i> " + promotion.getName() ) );
          mailingBatchHolder.setPartnerNotAchievedMailingBatch( applyBatch( BEAN_NAME + " <i>Partner NOT Achieved</i> " + promotion.getName() ) );

          for ( Iterator<ChallengepointPaxAwardValueBean> it = cpCalculationList.iterator(); it.hasNext(); )
          {
            ChallengepointPaxAwardValueBean cpPaxAwardValueBean = it.next();
            try
            {
              // submit the calculation results to process the payouts
              DepositProcessBean depositProcessBean = promotionEngineService.processChallengepointPayoutCalculationResults( cpCalculationList, cpPaxAwardValueBean, promotion, mailingBatchHolder );

              if ( depositProcessBean != null )
              {
                if ( promotion.getChallengePointAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
                {
                  depositProcessPointsList.add( depositProcessBean );
                }
                else if ( promotion.getChallengePointAwardType().getCode().equals( ChallengePointAwardType.MERCHTRAVEL ) )
                {
                  if ( depositProcessBean.getJournalId() != null )
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
              totErrors++;
              challengepointPayOutSuccessful = false;
              StringBuffer failMessage = new StringBuffer( "Error Occurred while Challenge Point Payout Process calculation" + " flag for promotion id: " );
              failMessage.append( getPromotionId() ).append( " The participant id is: " );
              failMessage.append( cpPaxAwardValueBean.getParticipant().getId() );
              failMessage.append( " The error caused by: " );
              failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
              log.info( failMessage, e );
              addComment( failMessage.toString() );
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

        String batchComments = generateMailingBatchComments( mailingBatchHolder );
        // Generate Extract report and send email notification
        challengePointService.generateAndMailExtractReport( cpCalculationList, promotion, pendingChallengepointAwardSummary.getManagerOverrideResults(), batchComments );

        if ( totErrors == 0 )
        {
          addComment( "Challengepoint Payout calculation is successful for Promotion id: " + getPromotionId() );
          final String successMessage = "Challengepoint Process completed successfully" + " and extract report email sent to user id: " + UserManager.getUserId() + " " + batchComments;
          log.info( successMessage );
          addComment( successMessage );

        }
        else
        {
          addComment( "Challengepoint Payout calculation partially successful for Promotion id: " + getPromotionId() + " " + batchComments );
          try
          {
            if ( !challengepointPayOutSuccessful && promotion != null )
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
        StringBuffer failureMsg = new StringBuffer( "Error Occurred while processing the Challengepoint Payout Process." + " The error caused by: " );
        failureMsg.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
        try
        {
          if ( !challengepointPayOutSuccessful && promotion != null )
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

  private String generateMailingBatchComments( MailingBatchHolder mailingBatchHolder )
  {
    StringBuilder sb = new StringBuilder();

    String participantAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxAchievedMailingBatch() );
    sb.append( participantAchievedBatchEmailComments );

    String participantNotAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxNotAchievedMailingBatch() );
    sb.append( participantNotAchievedBatchEmailComments );

    String interimBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getChallengePointInterimPayoutBatch() );
    sb.append( interimBatchEmailComments );

    return sb.toString();
  }

  private Long getPromotionId( String promotionId )
  {
    if ( promotionId == null || promotionId.equals( "" ) )
    {
      return null;
    }

    return new Long( promotionId );
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

  public ChallengepointProgressService getChallengepointProgressService()
  {
    return challengepointProgressService;
  }

  public void setChallengepointProgressService( ChallengepointProgressService challengepointProgressService )
  {
    this.challengepointProgressService = challengepointProgressService;
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

  public ChallengePointService getChallengePointService()
  {
    return challengePointService;
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
  }

  public GoalQuestService getGoalQuestService()
  {
    return goalQuestService;
  }

  public void setGoalQuestService( GoalQuestService goalQuestService )
  {
    this.goalQuestService = goalQuestService;
  }

}
