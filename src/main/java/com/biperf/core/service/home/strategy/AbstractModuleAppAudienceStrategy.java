
package com.biperf.core.service.home.strategy;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.diycommunications.ParticipantDIYCommunicationsService;
import com.biperf.core.service.engagement.EngagementService;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.reports.DashboardReportsService;
import com.biperf.core.service.rewardoffering.RewardOfferingsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

public abstract class AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private MainContentService mainContentService;
  private UserService userService;
  private RewardOfferingsService rewardOfferingsService;
  private PromotionService promotionService;
  private ParticipantService participantService;
  private AudienceService audienceService;
  private SystemVariableService systemVariableService;
  private ClaimService claimService;
  private EngagementService engagementService;
  private ClaimGroupService claimGroupService;
  private CMAssetService cmAssetService;
  private ParticipantDIYCommunicationsService participantDIYCommunicationsService;
  private DashboardReportsService dashboardReportsService;
  private MerchOrderService merchOrderService;

  @SuppressWarnings( "unchecked" )
  protected List<PromotionMenuBean> getEligiblePromotions( Map<String, Object> parameterMap )
  {
    if ( null != parameterMap.get( "eligiblePromotions" ) )
    {
      return (List<PromotionMenuBean>)parameterMap.get( "eligiblePromotions" );
    }
    else
    {
      List<PromotionMenuBean> promotions = (List<PromotionMenuBean>)mainContentService.buildEligiblePromoList( UserManager.getUser() );
      parameterMap.put( "eligiblePromotions", promotions );
      return promotions;
    }
  }

  protected boolean isDateWithinTileDisplayDate( Promotion promotion )
  {
    Date now = new Date();
    return now.after( promotion.getTileDisplayStartDate() ) && now.before( promotion.getTileDisplayEndDate() );
  }

  public MainContentService getMainContentService()
  {
    return mainContentService;
  }

  public void setMainContentService( MainContentService mainContentService )
  {
    this.mainContentService = mainContentService;
  }

  public FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)BeanLocator.getBean( FilterAppSetupService.BEAN_NAME );
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public RewardOfferingsService getRewardOfferingsService()
  {
    return rewardOfferingsService;
  }

  public void setRewardOfferingsService( RewardOfferingsService rewardOfferingsService )
  {
    this.rewardOfferingsService = rewardOfferingsService;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public ClaimService getClaimService()
  {
    return claimService;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setParticipantDIYCommunicationsService( ParticipantDIYCommunicationsService participantDIYCommunicationsService )
  {
    this.participantDIYCommunicationsService = participantDIYCommunicationsService;
  }

  public ParticipantDIYCommunicationsService getParticipantDIYCommunicationsService()
  {
    return participantDIYCommunicationsService;
  }

  public CMAssetService getCmAssetService()
  {
    return cmAssetService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public ClaimGroupService getClaimGroupService()
  {
    return claimGroupService;
  }

  public void setClaimGroupService( ClaimGroupService claimGroupService )
  {
    this.claimGroupService = claimGroupService;
  }

  public EngagementService getEngagementService()
  {
    return engagementService;
  }

  public void setEngagementService( EngagementService engagementService )
  {
    this.engagementService = engagementService;
  }

  public DashboardReportsService getDashboardReportsService()
  {
    return dashboardReportsService;
  }

  public void setDashboardReportsService( DashboardReportsService dashboardReportsService )
  {
    this.dashboardReportsService = dashboardReportsService;
  }

  public MerchOrderService getMerchOrderService()
  {
    return merchOrderService;
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

}
