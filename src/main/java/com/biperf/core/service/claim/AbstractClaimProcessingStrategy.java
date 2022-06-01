/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/AbstractClaimProcessingStrategy.java,v $
 */

package com.biperf.core.service.claim;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionProcessingModeType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.domain.promotion.PostProcessPayoutCalculation;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.ClaimOnlinePostProcess;
import com.biperf.core.process.NominationAutoNotificationProcess;
import com.biperf.core.service.SAO;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PostProcessJobsService;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.engine.PayoutCalculationFacts;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.TcccClientUtils;

/**
 * AbstractClaimProcessingStrategy.
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
 * <td>Oct 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class AbstractClaimProcessingStrategy implements ClaimProcessingStrategy
{
  protected ActivityService activityService;
  protected ClaimDAO claimDAO;
  protected JournalService journalService;
  protected ProcessService processService;
  protected PromotionEngineService promotionEngineService;
  protected GamificationService gamificationService;
  protected PostProcessJobsService postProcessJobsService;  
 
  
  // Fix for bug#56006,55519 start
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimProcessingStrategy#processApprovable(Approvable)
   * @param approvable
   */
  public final void processApprovable( Approvable approvable, boolean deductBudget, BudgetSegment budgetSegment ) throws ServiceErrorException
  {
	    // Generate only activities for the claim when isLevelPayoutByApproverAvailable=1 for nomination.
	    /*Promotion p=approvable.getPromotion();
	    NominationPromotion np=null;
	    if(p.isNominationPromotion() )
	    {
	    	 np=(NominationPromotion)p;
	    }
		if(np !=null && np.isLevelPayoutByApproverAvailable())
		{
		    List activities = createActivitiesForApprovable( approvable );
		    activities = activityService.saveActivities( activities );
		}
	    else{*/
    // Generate activities for the claim.
    List activities = createActivitiesForApprovable( approvable );

    activities = activityService.saveActivities( activities );

    if ( isPromotionRealTime( approvable.getPromotion() ) && isProcessingTimePassed( approvable ) )
    {
      if ( approvable.getPromotion().isNominationPromotion() )
      {
        int exactApprovalRound;
        if ( approvable instanceof ClaimGroup )
        {
          ClaimGroup claimGroup = (ClaimGroup)approvable;
          exactApprovalRound = claimGroup.getApprovableItemApproversSize();
        }
        else
        {
          Claim claim = (Claim)approvable;
          exactApprovalRound = claim.getApprovableItemApproversSize();
        }

        if ( exactApprovalRound == 0 )
        {
          exactApprovalRound = approvable.getApprovalRound().intValue();
        }

        NominationPromotion nominationPromotion = (NominationPromotion)approvable.getPromotion();
        Claim claim1 = (Claim)approvable;        
        ApproverType approverType =claim1.getPromotion().getApproverType();
          
        for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
        {
        	
          if ( nominationPromotionLevel.getLevelIndex() != null && nominationPromotionLevel.getLevelIndex().intValue() == exactApprovalRound )
          {
            if ( !nominationPromotionLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.OTHER )
            	  && !approverType.getCode().equals( ApproverType.CUSTOM_APPROVERS ) )
            {
              calculateAndSavePayouts( approvable, deductBudget, budgetSegment );  
            }
            // Award is other. Still need to send notification, but there are no payouts to process
            else
            {
              if ( approvable.getPromotion().isOnlineEntry() )
              {
                if ( approvable instanceof ClaimGroup )
                {
                  for ( Object claim : ( (ClaimGroup)approvable ).getClaims() )
                  {
                    PostProcessJobs postProcessJobs = postProcessJobsService.createPostProcessJobs( new PostProcessJobs(), createPostProcessInputParameters( (Claim)claim, new LinkedHashSet<>() ) );
                    postProcessJobsService.schedulePostProcess( postProcessJobs, ClaimOnlinePostProcess.PROCESS_NAME, ClaimOnlinePostProcess.BEAN_NAME );
                    break;
                  }
                }
                else
                {
                  PostProcessJobs postProcessJobs = postProcessJobsService.createPostProcessJobs( new PostProcessJobs(), createPostProcessInputParameters( approvable, new LinkedHashSet<>() ) );
                  postProcessJobsService.schedulePostProcess( postProcessJobs, ClaimOnlinePostProcess.PROCESS_NAME, ClaimOnlinePostProcess.BEAN_NAME );
                }
              }
              else
              {
                postProcess( new LinkedHashSet<>(), approvable );
              }
            }
            break;
          }
        }

        if ( ClaimApproveUtils.isClaimApproved( approvable ) )
        {
          if ( approvable instanceof ClaimGroup )
          {
            for ( Object claim : ( (ClaimGroup)approvable ).getClaims() )
            {
              gamificationService.populateBadgePartcipant( (Claim)claim );
            }
          }
          else if ( !approvable.getPromotion().isProductClaimPromotion() )
          {
            gamificationService.populateBadgePartcipant( (Claim)approvable );
          }
        }
      }
      else
      {
        calculateAndSavePayouts( approvable, deductBudget, budgetSegment );
        if ( ClaimApproveUtils.isClaimApproved( approvable ) )
        {
          if ( approvable instanceof ClaimGroup )
          {
            for ( Object claim : ( (ClaimGroup)approvable ).getClaims() )
            {
              gamificationService.populateBadgePartcipant( (Claim)claim );
            }
          }
          else if ( !approvable.getPromotion().isProductClaimPromotion() )
          {
            gamificationService.populateBadgePartcipant( (Claim)approvable );
          }
        }
      }
    }
 // }
    // for deferred case, payout will be calculated
    // and persisted via various background Processes
  }
  // Fix for bug#56006,55519 end

  /**
   * For claims where a "notification/processing/payout" date is used, return false if the date 
   * exists and is in the future. Otherwise return true. 
   * @param approvable
   */
  private boolean isProcessingTimePassed( Approvable approvable )
  {
    boolean datePassed = true;
    if ( approvable.getPromotion().isNominationPromotion() )
    {
      Date now = new Date();

      Date processingDate = null;
      if ( approvable instanceof NominationClaim )
      {
        NominationClaim nominationClaim = (NominationClaim)approvable;
        Set<ClaimRecipient> claimRecipients = nominationClaim.getClaimRecipients();
        for ( ClaimRecipient claimRecipient : claimRecipients )
        {
          processingDate = claimRecipient.getNotificationDate();
        }
      }
      else
      {
        ClaimGroup claimGroup = (ClaimGroup)approvable;
        processingDate = claimGroup.getNotificationDate();
      }

      if ( processingDate != null )
      {
        // Add Notification process time if any so that if today is the notification day, we don't
        // send
        // until the time of day of the process is passed.
        Long timeOfDayMillis = processService.getTimeOfDayMillisOfFirstSchedule( NominationAutoNotificationProcess.BEAN_NAME );
        if ( timeOfDayMillis == null )
        {
          timeOfDayMillis = new Long( 0 );
        }

        Date processingDateWithTime = new Date( processingDate.getTime() + timeOfDayMillis.longValue() );
        if ( now.before( processingDateWithTime ) )
        {
          datePassed = false;
        }
      }
    }

    return datePassed;
  }

  // Fix for bug#56006,55519 start
  /**
   * @param claim
   * @return the payouts for the given promotion, as a <code>Set</code> of
   *         {@link com.biperf.core.service.promotion.engine.PayoutCalculationResult} objects.
   */
  public Set calculateAndSavePayouts( Approvable approvable, boolean deductBudget, BudgetSegment budgetSegment ) throws ServiceErrorException
  {
    // Fix for bug#56006,55519 end
    Set liveMasterAndChildPromotions = approvable.getPromotion().getLivePrimaryAndChildPromotions();

    Set savedPayouts = new LinkedHashSet();
    // For each promotion and each payee pax run through engine.
    for ( Iterator iterator = liveMasterAndChildPromotions.iterator(); iterator.hasNext(); )
    {
      Promotion promotion = (Promotion)iterator.next();

      String payoutType = getPromotionPayoutType( promotion );

      if ( promotion.isProductClaimPromotion() )
      {
        if ( !payoutType.equals( PromotionPayoutType.STACK_RANK ) && promotion.isChild() )
        {
          // First get valid participants
          List validParticipants = getValidParticipantsForApprovable( approvable, promotion );

          for ( Iterator iterator1 = validParticipants.iterator(); iterator1.hasNext(); )
          {
            Participant validParticipant = (Participant)iterator1.next();

            PayoutCalculationFacts facts = getPayoutCalculationFacts( approvable, promotion, validParticipant );

            // Keep track of all of the saved payouts for this claim
            savedPayouts.addAll( promotionEngineService.calculatePayoutAndSaveResults( facts, promotion, validParticipant, payoutType ) );
          }
        }
        if ( !payoutType.equals( PromotionPayoutType.STACK_RANK ) && promotion.isMaster() )
        {
          // First get valid participants
          List validParticipants = getValidParticipantsForApprovable( approvable, promotion );

          for ( Iterator iterator1 = validParticipants.iterator(); iterator1.hasNext(); )
          {
            Participant validParticipant = (Participant)iterator1.next();

            PayoutCalculationFacts facts = getPayoutCalculationFacts( approvable, promotion, validParticipant );

            // Keep track of all of the saved payouts for this claim
            savedPayouts.addAll( promotionEngineService.calculatePayoutAndSaveResults( facts, promotion, validParticipant, payoutType ) );
          }
        }
      }
      else
      {
        if ( !payoutType.equals( PromotionPayoutType.STACK_RANK ) )
        {
          // First get valid participants
          List validParticipants = getValidParticipantsForApprovable( approvable, promotion );

          for ( Iterator iterator1 = validParticipants.iterator(); iterator1.hasNext(); )
          {
            Participant validParticipant = (Participant)iterator1.next();

            PayoutCalculationFacts facts = getPayoutCalculationFacts( approvable, promotion, validParticipant );

            // Keep track of all of the saved payouts for this claim
            savedPayouts.addAll( promotionEngineService.calculatePayoutAndSaveResults( facts, promotion, validParticipant, payoutType ) );
          }
        }

      }
    }

    // Fix for bug#56006,55519 start
    if ( deductBudget )
    {
      if ( budgetSegment == null )
      {
        calculateBudget( savedPayouts, approvable );
      }
      else
      {
        calculateBudget( savedPayouts, approvable, budgetSegment );
      }
    }
   Claim claim1 = (Claim)approvable;
   if ( claim1 instanceof NominationClaim )
   {
     NominationClaim nomClaim = (NominationClaim)claim1;
   int exactApprovalRound = claim1.getApprovableItemApproversSize();
	  
	if(exactApprovalRound > 1){
		promotionEngineService.depositApprovedPayouts( savedPayouts );
	}
   }else{
	   promotionEngineService.depositApprovedPayouts( savedPayouts );
   }

    if ( approvable.getPromotion().isOnlineEntry() )
    {
      if ( approvable instanceof ClaimGroup )
      {
        for ( Object claim : ( (ClaimGroup)approvable ).getClaims() )
        {
          PostProcessJobs postProcessJobs = postProcessJobsService.createPostProcessJobs( new PostProcessJobs(), createPostProcessInputParameters( (Claim)claim, savedPayouts ) );
          postProcessJobsService.schedulePostProcess( postProcessJobs, ClaimOnlinePostProcess.PROCESS_NAME, ClaimOnlinePostProcess.BEAN_NAME );
          break;
        }
      }
      else
      { 
    	  
	  if (null != approvable && null != approvable.getPromotion()
	    		&& null != approvable.getPromotion().getApproverType()
	    		&& approvable.getPromotion().getApproverType().isActive()
				&& approvable.getPromotion().getApproverType().getCode().equals(ApproverType.SPECIFIC_APPROVERS))
	  	{
			sendRecognitionNotification( savedPayouts, approvable );
		} else {
			Claim claim = (Claim) approvable;
			if (claim instanceof NominationClaim) 
			{
				if( !ClaimApproveUtils.isNominationApprovalComplete(approvable)){
						PostProcessJobs postProcessJobs = postProcessJobsService.createPostProcessJobs(new PostProcessJobs(), createPostProcessInputParameters(approvable, savedPayouts));
						postProcessJobsService.schedulePostProcess(postProcessJobs,
								ClaimOnlinePostProcess.PROCESS_NAME, ClaimOnlinePostProcess.BEAN_NAME);				
				
			}
			} else {
				PostProcessJobs postProcessJobs = postProcessJobsService.createPostProcessJobs(new PostProcessJobs(), createPostProcessInputParameters(approvable, savedPayouts));
				postProcessJobsService.schedulePostProcess(postProcessJobs, ClaimOnlinePostProcess.PROCESS_NAME,
						ClaimOnlinePostProcess.BEAN_NAME);
			}
		}
      }
    }
    else
    {
      postProcess( savedPayouts, approvable );
    }

    return savedPayouts;
  }

  private LinkedHashMap<String, Object> createPostProcessInputParameters( Approvable claim, Set savedPayouts )
  {
    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
    linkedHashMap.put( "processName", ClaimOnlinePostProcess.PROCESS_NAME );
    linkedHashMap.put( "processBeanName", ClaimOnlinePostProcess.BEAN_NAME );
    linkedHashMap.put( "promotionType", claim.getPromotion().getPromotionType().getCode() );
    linkedHashMap.put( "claimId", claim.getId() );
    linkedHashMap.put( "payOutCalculationResult", buildSavedPayoutResult( savedPayouts ) );

    return linkedHashMap;
  }

  public Set buildSavedPayoutResult( Set savedPayouts )
  {
    Set payoutCalculationResults = new LinkedHashSet();
    if ( savedPayouts != null )
    {
      Iterator iter = savedPayouts.iterator();
      while ( iter.hasNext() )
      {
        PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
        PostProcessPayoutCalculation postProcessPayoutCal = new PostProcessPayoutCalculation();

        Long calculatedPayout = payoutCalculationResult.getCalculatedPayout();
        if ( calculatedPayout != null )
        {
          postProcessPayoutCal.setCalculatedPayout( calculatedPayout );
        }

        if ( payoutCalculationResult.getCalculatedCashPayout() != null )
        {
          postProcessPayoutCal.setCalculatedCashPayout( payoutCalculationResult.getCalculatedCashPayout() );
        }

        Long journalId = null;
        if ( payoutCalculationResult.getJournal() != null )
        {
          journalId = payoutCalculationResult.getJournal().getId();
          postProcessPayoutCal.setJournalId( journalId );
        }

        Long promotionPayoutGroupId = null;
        if ( payoutCalculationResult.getPromotionPayoutGroup() != null )
        {
          promotionPayoutGroupId = payoutCalculationResult.getPromotionPayoutGroup().getId();
          postProcessPayoutCal.setPromotionPayoutGroupId( promotionPayoutGroupId );
        }

        Long minimumQualifierStatusId = null;
        if ( payoutCalculationResult.getMinimumQualifierStatus() != null )
        {
          minimumQualifierStatusId = payoutCalculationResult.getMinimumQualifierStatus().getId();
          postProcessPayoutCal.setMinimumQualifierStatusId( minimumQualifierStatusId );
        }

        Long payoutCalculationAuditId = null;
        if ( payoutCalculationResult.getPayoutCalculationAudit() != null )
        {
          payoutCalculationAuditId = payoutCalculationResult.getPayoutCalculationAudit().getId();
          postProcessPayoutCal.setPayoutCalculationAuditId( payoutCalculationAuditId );
        }

        Long promoMerchProgramLevelId = null;
        if ( payoutCalculationResult.getPromoMerchProgramLevel() != null )
        {
          promoMerchProgramLevelId = payoutCalculationResult.getPromoMerchProgramLevel().getId();
          postProcessPayoutCal.setPromoMerchProgramLevelId( promoMerchProgramLevelId );
        }
        payoutCalculationResults.add( postProcessPayoutCal );
      }
    }

    return payoutCalculationResults;
  }

  private boolean isPromotionRealTime( Promotion promotion )
  {
    boolean realTime;

    if ( promotion.isProductClaimPromotion() )
    {
      ProductClaimPromotion promotionAsProductClaimPromotion = (ProductClaimPromotion)promotion;
      String promotionProcessingModeCode = promotionAsProductClaimPromotion.getPromotionProcessingMode().getCode();
      if ( promotionProcessingModeCode.equals( PromotionProcessingModeType.REAL_TIME ) )
      {
        realTime = true;
      }
      else if ( promotionProcessingModeCode.equals( PromotionProcessingModeType.BATCH ) )
      {
        realTime = false;
      }
      else
      {
        throw new BeaconRuntimeException( "Unknown promotion processing mode: " + promotionProcessingModeCode );
      }
    }
    else
    {
      // all other types are real-time
      realTime = true;
    }

    return realTime;
  }
  
  private void sendRecognitionNotification(Set payoutCalculationResults, Approvable approvable ) throws ServiceErrorException
  {

	    try
	    {
	      RecognitionPromotion promotion = (RecognitionPromotion)approvable.getPromotion();
	      RecognitionClaim recognitionClaim = (RecognitionClaim)approvable;

	      if ( !promotion.isAwardActive()
	          || promotion.isAwardActive() && promotion.getAwardType().isPointsAwardType()   )
	      {

	        if ( !promotion.isNoNotification() )
	        {
	          for ( ClaimRecipient claimRecipient : recognitionClaim.getClaimRecipients() )
	          {
	      
	                if ( promotion.isIncludeCelebrations() )
	                {
	                  Mailing mailing =  getMailingService().buildRecognitionCelebrationMailing( recognitionClaim, null, false );

	                  if ( mailing.getMailingRecipients().size() > 0 )
	                  {
	                	  getMailingService().submitMailing( mailing, null );
	                  }
	                }
	                else if ( !promotion.isIncludeCelebrations() )
	                {
	                  //Client customization start
	                  Mailing mailing = null;
	                  if ( promotion.getAdihCashOption() )
	                  {
	                    Long usdCashAmt = TcccClientUtils.convertToUSD( getCokeClientService(), claimRecipient.getCustomCashAwardQuantity(), claimRecipient.getCashCurrencyCode() );
	                    Long points = TcccClientUtils.convertToPoints( getCokeClientService(), usdCashAmt, claimRecipient.getRecipient().getId() );
	                    mailing =  getMailingService().buildRecognitionMailingCustomOnlyPoints( recognitionClaim, String.valueOf(  claimRecipient.getCustomCashAwardQuantity() ) );
	                  }
	                  else
	                    mailing =  getMailingService().buildRecognitionMailing( recognitionClaim, null );
	                  //Client customization end
	                  if ( mailing.getMailingRecipients().size() > 0 )
	                  {
	                    getMailingService().submitMailing( mailing, null );
	                  }                  
	                }
	              }	            
	        }
	      }

	    }
	    catch( Exception ex )
	    {
	    //  log.error( "Error in postProcess ", ex );
	      throw new ServiceErrorException( ex.getMessage(), ex );
	    }
 
	  
  }

  /**
   * @param approvable
   * @return List of Activities generated for a claim
   */
  protected abstract List createActivitiesForApprovable( Approvable approvable );

  /**
   * @param approvable
   * @param promotion
   * @return List of valid participants involved in the claim
   */
  protected abstract List getValidParticipantsForApprovable( Approvable approvable, Promotion promotion );

  /**
   * @param approvable
   * @param promotion
   * @param participant
   * @return PayoutCalculationFacts
   */
  protected abstract PayoutCalculationFacts getPayoutCalculationFacts( Approvable approvable, Promotion promotion, Participant participant );

  /**
   * Calculate budget for given payouts
   * 
   * @param payoutCalculationResults
   * @param approvable
   */
  protected void calculateBudget( Set payoutCalculationResults, Approvable approvable )
  {
    // Implement this if necessary in subclass
  }

  /**
   * Calculate budget for given payouts
   * 
   * @param payoutCalculationResults
   * @param approvable
   * @param budgetSegment
   */
  protected void calculateBudget( Set payoutCalculationResults, Approvable approvable, BudgetSegment budgetSegment )
  {
    // Implement this if necessary in subclass
  }

  public void setActivityService( ActivityService activityService )
  {
    this.activityService = activityService;
  }

  public void setPromotionEngineService( PromotionEngineService promotionEngineService )
  {
    this.promotionEngineService = promotionEngineService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  /**
   * @param claimDAO value for claimDAO property
   */
  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  public void setPostProcessJobsService( PostProcessJobsService postProcessJobsService )
  {
    this.postProcessJobsService = postProcessJobsService;
  }

 
 
  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  
  public MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }
  
  
  public CokeClientService getCokeClientService()
  {
    return (CokeClientService)getService( CokeClientService.BEAN_NAME );
  }
}
