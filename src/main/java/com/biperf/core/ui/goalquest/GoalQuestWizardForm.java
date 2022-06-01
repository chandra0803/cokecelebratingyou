
package com.biperf.core.ui.goalquest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.value.ChallengepointPaxValueBean;

@SuppressWarnings( "serial" )
public class GoalQuestWizardForm extends BaseForm
{
  private boolean isNew = true;
  private Long userId = null;

  private int maxPartnersInput = 0;
  private Long promotionId = null;
  private Long selectedGoalId = null;
  // selected product info
  private String selectedProductId = null;
  private String selectedProductName = null;
  private String selectedProductImgUrl = "http://placehold.it/280x280";

  private String method;
  private String activeStep = "stepOverview";

  private String promotionType;

  private GoalQuestPromotion promotion;
  private ChallengepointPaxValueBean challengepointPaxValueBean;

  // This is either the currently selected Partners or the default/pre-selected
  private Map<Integer, ParticipantInfoView> partners = new HashMap<Integer, ParticipantInfoView>();
  private Map<Integer, ParticipantInfoView> preselectedPartners = new HashMap<Integer, ParticipantInfoView>();

  public boolean uaConnected;
  public String uaOAuthUrl;
  public boolean uaEnabled;
  public boolean showUAStepInput;
  private String uaLogOutUrl;

  public boolean getIsPartnersSelected()
  {
    return preselectedPartners.size() > 0;
  }

  public void setIsPartnersSelected( boolean value )
  {
    // don't do anything here, the getter is based on the selected partner list size
  }

  public String getPromotionName()
  {
    return promotion.getPromoNameFromCM();
  }

  public boolean isNew()
  {
    return isNew;
  }

  public void setNew( boolean isNew )
  {
    this.isNew = isNew;
  }

  public String getOverview()
  {
    return promotion.getOverviewDetailsText();
  }

  public String getObjectiveFromCM()
  {
    return promotion.getObjectiveFromCM();
  }

  public String getRules()
  {
    return promotion.getWebRulesText();
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public GoalQuestPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( GoalQuestPromotion promotion )
  {
    this.promotion = promotion;
  }

  public Date getPromotionStartDate()
  {
    return promotion.getSubmissionStartDate(); // DateUtils.toDisplayString(
                                               // promotion.getSubmissionStartDate() );
  }

  public Date getPromotionEndDate()
  {
    return promotion.getSubmissionEndDate(); // DateUtils.toDisplayString(
                                             // promotion.getSubmissionEndDate() );
  }

  public Date getGoalSelectionStartDate()
  {
    return promotion.getGoalCollectionStartDate(); // DateUtils.toDisplayString(
                                                   // promotion.getGoalCollectionStartDate() );
  }

  public Date getGoalSelectionEndDate()
  {
    return promotion.getGoalCollectionEndDate();// DateUtils.toDisplayString(
                                                // promotion.getGoalCollectionEndDate() );
  }

  public boolean getIsPartnersEnabled()
  {
    return null != promotion.getPartnerAudienceType();
  }

  public String getActiveStep()
  {
    return activeStep;
  }

  public void setActiveStep( String activeStep )
  {
    this.activeStep = activeStep;
  }

  public String getAwardType()
  {
    if ( promotion.isGoalQuestPromotion() )
    {
      return promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) ? "points" : "plateau";
    }
    else
    {
      return ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode().equals( ChallengePointAwardType.POINTS ) ? "points" : "plateau";
    }
  }

  public Long getSelectedGoalId()
  {
    return selectedGoalId;
  }

  public void setSelectedGoalId( Long selectedGoalId )
  {
    this.selectedGoalId = selectedGoalId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public int getPartnersCount()
  {
    if ( preselectedPartners == null || preselectedPartners.isEmpty() )
    {
      return 0;
    }
    return preselectedPartners.size();
  }

  public String getSelectedProductId()
  {
    return selectedProductId;
  }

  public List<ParticipantInfoView> getPreselectedPartners()
  {
    return new ArrayList<ParticipantInfoView>( preselectedPartners.values() );
  }

  public List<ParticipantInfoView> getPartnersAsList()
  {
    return new ArrayList<ParticipantInfoView>( partners.values() );
  }

  public ParticipantInfoView getPartners( int index )
  {
    ParticipantInfoView bean = partners.get( index );
    if ( bean == null )
    {
      bean = new ParticipantInfoView();
      partners.put( index, bean );
    }
    return bean;
  }

  public void setPreselectedPartners( List<? extends ParticipantInfoView> selectedPartners )
  {
    this.preselectedPartners.clear();
    if ( selectedPartners != null )
    {
      for ( int i = 0; i < selectedPartners.size(); i++ )
      {
        this.preselectedPartners.put( i, selectedPartners.get( i ) );
      }
    }
  }

  public void setSelectedProductId( String selectedProductId )
  {
    this.selectedProductId = selectedProductId;
  }

  public boolean getIsProductOnly()
  {
    if ( getPromotion().getMerchGiftCodeType() != null && getPromotion().getMerchGiftCodeType().getCode().equals( MerchGiftCodeType.PRODUCT ) )
    {
      return true;
    }
    return false;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public int getMaxPartnersInput()
  {
    return maxPartnersInput;
  }

  public void setMaxPartnersInput( int maxPartnersInput )
  {
    this.maxPartnersInput = maxPartnersInput;
  }

  public String getSelectedProductName()
  {
    return selectedProductName;
  }

  public void setSelectedProductName( String selectedProductName )
  {
    this.selectedProductName = selectedProductName;
  }

  public String getSelectedProductImgUrl()
  {
    return selectedProductImgUrl;
  }

  public void setSelectedProductImgUrl( String selectedProductImgUrl )
  {
    this.selectedProductImgUrl = selectedProductImgUrl;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public ChallengepointPaxValueBean getChallengepointPaxValueBean()
  {
    return challengepointPaxValueBean;
  }

  public void setChallengepointPaxValueBean( ChallengepointPaxValueBean challengepointPaxValueBean )
  {
    this.challengepointPaxValueBean = challengepointPaxValueBean;
  }

  public boolean isUaConnected()
  {
    return uaConnected;
  }

  public void setUaConnected( boolean uaConnected )
  {
    this.uaConnected = uaConnected;
  }

  public String getUaOAuthUrl()
  {
    return uaOAuthUrl;
  }

  public void setUaOAuthUrl( String uaOAuthUrl )
  {
    this.uaOAuthUrl = uaOAuthUrl;
  }

  public boolean isUaEnabled()
  {
    return uaEnabled;
  }

  public void setUaEnabled( boolean uaEnabled )
  {
    this.uaEnabled = uaEnabled;
  }

  public boolean isShowUAStepInput()
  {
    return uaEnabled;
  }

  public void setShowUAStepInput( boolean showUAStepInput )
  {
    this.showUAStepInput = showUAStepInput;
  }

  public String getUaLogOutUrl()
  {
    return uaLogOutUrl;
  }

  public void setUaLogOutUrl( String uaLogOutUrl )
  {
    this.uaLogOutUrl = uaLogOutUrl;
  }
}
