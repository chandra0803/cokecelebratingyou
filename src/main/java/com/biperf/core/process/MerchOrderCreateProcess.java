
package com.biperf.core.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.awardbanq.databeans.account.IssuedOMGiftCode;
import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.GiftCodesWebServiceException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.awardbanq.GiftCodes;
import com.biperf.core.service.awardbanq.GiftcodesResponseValueObject;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.goalquest.impl.PaxGoalAssociationRequest;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.util.StringUtils;

public class MerchOrderCreateProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( MerchOrderCreateProcess.class );

  public static final String BEAN_NAME = "merchOrderCreateProcess";
  public static final String PROCESS_NAME = "MerchOrder Create Process";

  private PromotionEngineService promotionEngineService;
  private MerchOrderService merchOrderService;
  private PromotionService promotionService;
  private PaxGoalService paxGoalService;
  private MerchLevelService merchLevelService;
  private ClaimService claimService;
  private ActivityService activityService;
  private JournalService journalService;
  private ProcessService processService;

  private List<DepositProcessBean> depositProcessMerchList;
  private Long promotionId;
  private int retry = 0; // Bug 66870
  private String isRetriable; // Bug 66870

  @SuppressWarnings( "unused" )
  public void onExecute()
  {
    boolean retriableAllowed = false; // Bug 66870
    if ( !StringUtils.isEmpty( isRetriable ) )
    {
      retriableAllowed = new Boolean( isRetriable );
    }

    Promotion promotion = promotionService.getPromotionById( promotionId );

    String programId = "";
    MerchLevel omLevel = null;
    if ( !depositProcessMerchList.isEmpty() )
    {
      for ( Iterator<DepositProcessBean> iter = depositProcessMerchList.iterator(); iter.hasNext(); )
      {
        List<GiftCodes> giftCodes = new ArrayList<GiftCodes>();
        DepositProcessBean depositProcessBean = iter.next();
        try
        {
          MerchOrder merchOrder = merchOrderService.getMerchOrderById( depositProcessBean.getMerchOrderId() );
          Participant participant = merchOrder.getParticipant();
          Long valueOfGiftCode = depositProcessBean.getValueOfGiftCode();
          if ( StringUtils.isEmpty( merchOrder.getGiftCode() ) )
          {
            programId = depositProcessBean.getProgramId();
            GiftcodesResponseValueObject giftCodeVO = null;
            String batchId = merchOrder.getBatchId().toString();
            if ( promotion instanceof GoalQuestPromotion )
            {
              GoalQuestPromotion promo = (GoalQuestPromotion)promotion;
              programId = promo.getProgramId();
              PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participant.getId() );

              GoalLevel goalLevel = (GoalLevel)paxGoal.getGoalLevel();
              BigDecimal awardAmount = goalLevel.getAward();
              valueOfGiftCode = awardAmount.longValue();

            }
            if ( promotion.isGoalQuestOrChallengePointPromotion() && !participantService.isPaxOptedOutOfAwards( participant.getId() ) )
            {
              giftCodeVO = promotionEngineService.retrieveGiftCodesForMerchandiseOrTravelWebService( programId, participant, valueOfGiftCode, merchOrder.getBatchId().toString() );
              giftCodes.addAll( giftCodeVO.getGiftCodes() );
            }
            else if ( promotion.isRecognitionPromotion() )
            {
              boolean isLevel = ( (RecognitionPromotion)promotion ).getAwardStructure().equals( MerchGiftCodeType.LEVEL );
              IssuedOMGiftCode giftCode = null;
              if ( merchOrder.getPromoMerchProgramLevel() != null )
              {
                omLevel = merchLevelService.getMerchLevelData( merchOrder.getPromoMerchProgramLevel(), true, !isLevel );
                programId = merchOrder.getPromoMerchProgramLevel().getPromoMerchCountry().getProgramId();
                // Call to this method will generate a new giftcode.
                // This should be done only for Level based promotion
                if ( isLevel )
                {
                  giftCodeVO = promotionEngineService.retrieveGiftCodesForMerchandiseOrTravelWebService( programId, participant, omLevel.getMaxValue(), batchId );
                  if ( giftCodeVO != null )
                  {
                    for ( GiftCodes giftCodeObj : giftCodeVO.getGiftCodes() )
                    {
                      if ( log.isDebugEnabled() )
                      {
                        log.debug( "giftCode is : " + giftCodeObj.getGiftCode() );
                      }
                    }
                  }
                  giftCodes.addAll( giftCodeVO.getGiftCodes() );

                  merchOrder.setPoints( omLevel.getMaxValue() );
                  RecognitionClaim claim = (RecognitionClaim)getClaimService().getClaimById( merchOrder.getClaim().getId() );

                  Budget budget = null;
                  if ( promotion.isBudgetUsed() )
                  {
                    budget = promotionService.getAvailableBudget( promotion.getId(), claim.getSubmitter().getId(), claim.getNode().getId() );

                    if ( budget != null )
                    {
                      budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEDUCT ) );
                      budget.setReferenceVariableForClaimId( claim.getId() );
                      BigDecimal totalPayout = BigDecimal.ZERO;
                      totalPayout = totalPayout.add( BigDecimal.valueOf( omLevel.getMaxValue() ) );
                      BigDecimal currentBudget = budget.getCurrentValue();
                      if ( totalPayout.doubleValue() <= currentBudget.doubleValue() )
                      {
                        budget.setCurrentValue( currentBudget.subtract( totalPayout ) );
                      }
                    }
                  }
                }
              }
            }
            merchOrderService.processGiftCodes( merchOrder, giftCodes, true, depositProcessBean, omLevel );
            if ( merchOrder != null && merchOrder.getClaim() != null )
            {
              RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
              Mailing mailing = null;
              if ( recognitionPromotion != null && recognitionPromotion.isIncludeCelebrations() )
              {
                mailing = merchOrderService.buildMerchCelebrationMailing( merchOrder.getClaim().getId() );
              }
              else if ( recognitionPromotion != null && recognitionPromotion.isIncludePurl() )
              {
                RecognitionClaim claim = (RecognitionClaim)getClaimService().getClaimById( merchOrder.getClaim().getId() );
                if ( !claim.isSkipStandardRecognitionEmail() )
                {
                  mailing = merchOrderService.buildMerchRecognitionMailing( merchOrder.getClaim().getId() );
                }
              }
              else if ( recognitionPromotion != null && !recognitionPromotion.isIncludePurl() )
              {
                mailing = merchOrderService.buildMerchRecognitionMailing( merchOrder.getClaim().getId() );
              }
              if ( mailing != null && mailing.getMailingRecipients().size() > 0 )
              {
                Set<MailingRecipient> mailingRecipients = mailing.getMailingRecipients();
                for ( Iterator iter1 = mailingRecipients.iterator(); iter1.hasNext(); )
                {
                  MailingRecipient mr = (MailingRecipient)iter1.next();
                  if ( !mr.isValidRecipient() )
                  {
                    iter1.remove();
                  }
                  mailing.setMailingRecipients( mailingRecipients );
                }
                mailingService.submitMailing( mailing, null );
              }

            }
            else if ( merchOrder.getClaim() == null )
            {
              Activity activity = activityService.getActivityForMerchOrderId( merchOrder.getId() );

              if ( activity != null )
              {
                Set<ActivityJournal> journal = activity.getActivityJournals();
                for ( Iterator iter1 = journal.iterator(); iter1.hasNext(); )
                {
                  ActivityJournal journalObj = (ActivityJournal)iter1.next();
                  boolean isfromSweepstakes = journalService.isJournalFromSweepstakes( journalObj.getJournal() );
                  if ( isfromSweepstakes )
                  {
                    Mailing mailing = journalService.buildSweepstakeMailing( journalObj.getJournal() );
                    mailingService.submitMailing( mailing, null );
                  }
                }
              }
            }
            log.error( "Giftcode generated successfully for merch order id :  " + depositProcessBean.getMerchOrderId() );
            addComment( "Giftcode generated successfully for merch order id:  " + depositProcessBean.getMerchOrderId() );
          }
          else
          {
            log.error( "Gift code already exists for merch order id " + depositProcessBean.getMerchOrderId() );
            addComment( "Gift code already exists for merch order id " + depositProcessBean.getMerchOrderId() );
          }
        }
        catch( GiftCodesWebServiceException gce ) // Bug 66870
        {
          log.error( "A GiftCodesWebServiceException occurred while retrieving giftcodes for merch order id " + depositProcessBean.getMerchOrderId() + " (process invocation ID = "
              + getProcessInvocationId() + ")", gce );
          addComment( "A GiftCodesWebServiceException occurred while retrieving giftcodes for merch order id " + depositProcessBean.getMerchOrderId()
              + " See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
          if ( retriableAllowed && isRetriable() )
          {
            submitRetry( depositProcessBean );
          }
        }
        catch( Exception e )
        {
          log.error( "An exception occurred while retrieving giftcodes for merch order id " + depositProcessBean.getMerchOrderId() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
          addComment( "An exception occurred while retrieving giftcodes for merch order id " + depositProcessBean.getMerchOrderId() + " See the log file for additional information.  "
              + "(process invocation ID = " + getProcessInvocationId() + ")" );
        }
      } // iterator
    } // merchList
    // TODO need to unit test
    else
    {
      List<MerchOrder> merchOrders = promotionService.getMerchOrdersToGenerateGiftCodeByPromotionIdAndUserId( promotionId, null );
      for ( Iterator<MerchOrder> iter = merchOrders.iterator(); iter.hasNext(); )
      {
        List<GiftCodes> giftCodes = new ArrayList<GiftCodes>();
        MerchOrder merchOrder = iter.next();
        GiftcodesResponseValueObject giftCodeVO = null;
        String batchId = merchOrder.getBatchId().toString();

        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          programId = ( (GoalQuestPromotion)promotion ).getProgramId();
          AssociationRequestCollection arCollection = new AssociationRequestCollection();
          arCollection.add( new PaxGoalAssociationRequest( PaxGoalAssociationRequest.GOAL_LEVEL ) );
          List<PaxGoal> paxGoalList = paxGoalService.getPaxGoalByPromotionId( promotionId, arCollection );
          for ( Iterator<PaxGoal> paxGoalIter = paxGoalList.iterator(); paxGoalIter.hasNext(); )
          {
            PaxGoal paxGoal = paxGoalIter.next();
            if ( paxGoal.getParticipant().getId().equals( merchOrder.getParticipant().getId() ) )
            {
              BigDecimal giftCodeAward = ( (GoalLevel)paxGoal.getGoalLevel() ).getAward();
              giftCodeVO = promotionEngineService.retrieveGiftCodesForMerchandiseOrTravelWebService( programId, paxGoal.getParticipant(), giftCodeAward.longValue(), batchId );
              giftCodes.addAll( giftCodeVO.getGiftCodes() );
              merchOrderService.processGiftCodes( merchOrder, giftCodes, true, null, null );
            }
          }
        }
        else if ( promotion.isRecognitionPromotion() )
        {
          omLevel = merchLevelService.getMerchLevelData( merchOrder.getPromoMerchProgramLevel(), true, ! ( (RecognitionPromotion)promotion ).getAwardStructure().equals( MerchGiftCodeType.LEVEL ) );
          programId = merchOrder.getPromoMerchProgramLevel().getPromoMerchCountry().getProgramId();
          giftCodeVO = promotionEngineService.retrieveGiftCodesForMerchandiseOrTravelWebService( programId, merchOrder.getParticipant(), omLevel.getMaxValue(), batchId );
          giftCodes.addAll( giftCodeVO.getGiftCodes() );
          merchOrderService.processGiftCodes( merchOrder, giftCodes, ( (RecognitionPromotion)promotion ).getAwardStructure().equals( MerchGiftCodeType.LEVEL ), null, null );
        }
      }
    }
  }

  private void submitRetry( DepositProcessBean depositProcessBean ) // Bug 66870
  {

    Process process = processService.createOrLoadSystemProcess( MerchOrderCreateProcess.PROCESS_NAME, MerchOrderCreateProcess.BEAN_NAME );

    List<DepositProcessBean> depositProcessMerchList = new ArrayList<DepositProcessBean>();

    depositProcessMerchList.add( depositProcessBean );

    int nextRetry = retry + 1;
    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "depositProcessMerchList", depositProcessMerchList );
    parameterValueMap.put( "promotionId", promotionId );
    parameterValueMap.put( "retry", Integer.toString( nextRetry ) );

    processService.launchProcess( process, parameterValueMap, UserManager.getUserId(), new Long( getRetryDelay() ) );

    addComment( "Launching retry #" + nextRetry + " for merch order id " + depositProcessBean.getMerchOrderId() );
  }

  private static long getRetryDelay()
  {
    long retryDelay = 600;

    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );

    PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_DEPOSIT_RETRY_DELAY );
    if ( property != null )
    {
      retryDelay = property.getLongVal();
    }

    return retryDelay * 1000;
  }

  public boolean isRetriable()
  {
    boolean isRetriable = false;
    int maxRetryCount = getMaxRetryCount();
    isRetriable = retry + 1 <= maxRetryCount;
    if ( isRetriable )
    {
      isRetriable = true;
    }

    return isRetriable;
  }

  private static int getMaxRetryCount()
  {
    int maxRetryCount = 3;

    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );

    PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_DEPOSIT_RETRY_COUNT );

    if ( property != null )
    {
      maxRetryCount = property.getIntVal();
    }

    return maxRetryCount;
  }

  public void setDepositProcessMerchList( List<DepositProcessBean> depositProcessMerchList )
  {
    this.depositProcessMerchList = depositProcessMerchList;
  }

  public void setPromotionEngineService( PromotionEngineService promotionEngineService )
  {
    this.promotionEngineService = promotionEngineService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public ClaimService getClaimService()
  {
    return claimService;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public ActivityService getActivityService()
  {
    return activityService;
  }

  public void setActivityService( ActivityService activityService )
  {
    this.activityService = activityService;
  }

  public JournalService getJournalService()
  {
    return journalService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public int getRetry()
  {
    return retry;
  }

  public void setRetry( int retry )
  {
    this.retry = retry;
  }

  public String getIsRetriable()
  {
    return isRetriable;
  }

  public void setIsRetriable( String isRetriable )
  {
    this.isRetriable = isRetriable;
  }

}
