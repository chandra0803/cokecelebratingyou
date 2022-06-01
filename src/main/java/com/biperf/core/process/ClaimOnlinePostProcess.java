
package com.biperf.core.process;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.domain.promotion.PostProcessPayoutCalculation;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.audit.PayoutCalculationAuditService;
import com.biperf.core.service.claim.ClaimProcessingStrategy;
import com.biperf.core.service.claim.ClaimProcessingStrategyFactory;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.MinimumQualifierStatusService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.promotion.PostProcessJobsService;
import com.biperf.core.service.promotion.PromotionPayoutService;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;

/**
 * ClaimOnlinePostProcess.
 * Added for bug 3136. The post process is called only for online transactions. 
 * See AbstractClaimProcessingStrategy calculateAndSavePayouts method for more info.
 * 
 * Rewritten for bug 54379 to pass in postProcessJobsId instead of the claim object.
 * 
 * @author kandhi
 * @since May 21, 2013
 * @version 1.0
 */
public class ClaimOnlinePostProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( ClaimOnlinePostProcess.class );

  public static final String BEAN_NAME = "claimOnlinePostProcess";
  public static final String PROCESS_NAME = "Claim Online Post Process";

  private ClaimProcessingStrategyFactory claimProcessingStrategyFactory;
  private ClaimService claimService;
  private JournalService journalService;
  private PromotionPayoutService promotionPayoutService;
  private MinimumQualifierStatusService minimumQualifierStatusService;
  private PayoutCalculationAuditService payoutCalculationAuditService;
  private MerchLevelService merchLevelService;
  private PostProcessJobsService postProcessJobsService;

  // properties set from jobDataMap
  private String postProcessJobsId;

  @Override
  protected void onExecute()
  {
    try
    {
      PostProcessJobs postProcessJobs = postProcessJobsService.getPostProcessJobsById( new Long( postProcessJobsId ) );

      if ( !postProcessJobs.isFired() )
      {
        Set payoutCalculationResults = new LinkedHashSet();
        Approvable claim = (Approvable)claimService.getClaimForPostProcess( postProcessJobs.getClaimId() );

        if ( postProcessJobs.getPayOutCalculationResult() != null )
        {
          payoutCalculationResults = buildPayoutCalculationResult( postProcessJobs.getPayOutCalculationResult() );
        }
        ClaimProcessingStrategy claimProcessingStrategy = claimProcessingStrategyFactory.getClaimProcessingStrategy( claim.getPromotion().getPromotionType() );
        claimProcessingStrategy.postProcess( payoutCalculationResults, claim );

        postProcessJobs.setFired( true );
        postProcessJobs.setFiredDate( new Date() );
        postProcessJobsService.savePostProcessJobs( postProcessJobs );

        addComment( "postProcessJobsId : " + postProcessJobsId + " claimId : " + claim.getId() + " processed successfully" );
      }
      else
      {
        addComment( "postProcessJobsId : " + postProcessJobsId + " has already been processed" );
      }

    }
    catch( ServiceErrorException se )
    {
      addComment( "se Exception occurred on postProcessJobsId: " + postProcessJobsId + " " + se.getMessage() );
      log.error( "se Exception", se );
    }
    catch( Exception e )
    {
      addComment( "ex Exception occurred on postProcessJobsId: " + postProcessJobsId + " " + e.getMessage() );
      log.error( "ex Exception", e );
    }
  }

  private Set buildPayoutCalculationResult( Set postProcessPayoutCalculationList )
  {
    Set payoutCalculationResults = new LinkedHashSet();
    if ( postProcessPayoutCalculationList != null )
    {
      Iterator iter = postProcessPayoutCalculationList.iterator();
      while ( iter.hasNext() )
      {
        PostProcessPayoutCalculation postProcessPayoutCalculation = (PostProcessPayoutCalculation)iter.next();

        PayoutCalculationResult payoutCalculationResult = new PayoutCalculationResult();
        if ( postProcessPayoutCalculation.getJournalId() != null )
        {
          payoutCalculationResult.setJournal( journalService.getJournalById( postProcessPayoutCalculation.getJournalId() ) );
        }
        if ( postProcessPayoutCalculation.getMinimumQualifierStatusId() != null )
        {
          payoutCalculationResult.setMinimumQualifierStatus( minimumQualifierStatusService.getMinimumQualifierStatus( postProcessPayoutCalculation.getMinimumQualifierStatusId() ) );
        }
        if ( postProcessPayoutCalculation.getCalculatedPayout() != null )
        {
          payoutCalculationResult.setCalculatedPayout( postProcessPayoutCalculation.getCalculatedPayout() );
        }
        if ( postProcessPayoutCalculation.getCalculatedCashPayout() != null )
        {
          payoutCalculationResult.setCalculatedCashPayout( postProcessPayoutCalculation.getCalculatedCashPayout() );
        }
        if ( postProcessPayoutCalculation.getPromoMerchProgramLevelId() != null )
        {
          payoutCalculationResult.setPromoMerchProgramLevel( merchLevelService.getPromoMerchProgramLevelById( postProcessPayoutCalculation.getPromoMerchProgramLevelId() ) );
        }
        if ( postProcessPayoutCalculation.getPayoutCalculationAuditId() != null )
        {
          payoutCalculationResult.setPayoutCalculationAudit( payoutCalculationAuditService.getPayoutCalculationAuditById( postProcessPayoutCalculation.getPayoutCalculationAuditId() ) );
        }
        if ( postProcessPayoutCalculation.getPromotionPayoutGroupId() != null )
        {
          payoutCalculationResult.setPromotionPayoutGroup( promotionPayoutService.getPromotionPayoutGroupById( postProcessPayoutCalculation.getPromotionPayoutGroupId() ) );
        }

        payoutCalculationResults.add( payoutCalculationResult );
      }
    }
    return payoutCalculationResults;
  }

  public String getPostProcessJobsId()
  {
    return postProcessJobsId;
  }

  public void setPostProcessJobsId( String postProcessJobsId )
  {
    this.postProcessJobsId = postProcessJobsId;
  }

  public void setClaimProcessingStrategyFactory( ClaimProcessingStrategyFactory claimProcessingStrategyFactory )
  {
    this.claimProcessingStrategyFactory = claimProcessingStrategyFactory;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setPromotionPayoutService( PromotionPayoutService promotionPayoutService )
  {
    this.promotionPayoutService = promotionPayoutService;
  }

  public void setMinimumQualifierStatusService( MinimumQualifierStatusService minimumQualifierStatusService )
  {
    this.minimumQualifierStatusService = minimumQualifierStatusService;
  }

  public void setPayoutCalculationAuditService( PayoutCalculationAuditService payoutCalculationAuditService )
  {
    this.payoutCalculationAuditService = payoutCalculationAuditService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  public void setPostProcessJobsService( PostProcessJobsService postProcessJobsService )
  {
    this.postProcessJobsService = postProcessJobsService;
  }
}
