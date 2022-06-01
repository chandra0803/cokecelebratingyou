
package com.biperf.core.ui.goalquest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.challengepoint.ChallengepointReviewProgress;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.GoalQuestReviewProgress;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.goalquest.view.AwardProductView;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;

@SuppressWarnings( "serial" )
public class GoalQuestDetailsForm extends BaseForm
{
  private GoalQuestPromotion promotion;
  private Long paxGoalId;
  private PaxGoal paxGoal;

  // partner info
  private boolean partnersEnabled = false;
  private boolean isPartner = false;
  private List<ParticipantPartner> partners = new ArrayList<ParticipantPartner>();

  // selected product info
  private AwardProductView productView = null;
  private String selectedProductId = null;
  private String selectedProductName = null;
  private String selectedProductImgUrl = "http://placehold.it/280x280";

  // progress
  private boolean goalAchieved = false;
  private List<GoalQuestReviewProgress> gqAutoProgressList = new ArrayList<GoalQuestReviewProgress>();
  private List<ChallengepointReviewProgress> cpAutoProgressList = new ArrayList<ChallengepointReviewProgress>();
  private GoalQuestReviewProgress gqProgress;// the latest
  private ChallengepointReviewProgress cpProgress;// the latest
  private String progressDate = null;

  private String calculatedAchievementAmount;
  private String partnerPayout;

  public GoalQuestReviewProgress getGqProgress()
  {
    return gqProgress;
  }

  public void setGqProgress( GoalQuestReviewProgress gqProgress )
  {
    this.gqProgress = gqProgress;
  }

  public ChallengepointReviewProgress getCpProgress()
  {
    return cpProgress;
  }

  public void setCpProgress( ChallengepointReviewProgress cpProgress )
  {
    this.cpProgress = cpProgress;
  }

  public List<GoalQuestReviewProgress> getGqAutoProgressList()
  {
    return gqAutoProgressList;
  }

  public void setGqAutoProgressList( List<GoalQuestReviewProgress> gqAutoProgressList )
  {
    this.gqAutoProgressList = gqAutoProgressList;
  }

  public String getProgressDate()
  {
    return progressDate;
  }

  public void setProgressDate( String date )
  {
    this.progressDate = date;
  }

  public boolean isPartner()
  {
    if ( isPartnersEnabled() && this.isPartner && getOwner() != null && !getOwner().getId().equals( UserManager.getUserId() ) )
    {
      return true;
    }
    return false;
  }

  public void setPartner( boolean isPartner )
  {
    this.isPartner = isPartner;
  }

  public Participant getOwner()
  {
    if ( paxGoal != null )
    {
      return paxGoal.getParticipant();
    }
    return null;
  }

  public List<ParticipantPartner> getPartners()
  {
    return partners;
  }

  public void setPartners( List<ParticipantPartner> partners )
  {
    this.partners = partners;
  }

  public PaxGoal getPaxGoal()
  {
    return paxGoal;
  }

  public String getLevelId()
  {
    if ( null != paxGoal && paxGoal.getGoalLevel() != null )
    {
      return paxGoal.getGoalLevel().getId().toString();
    }
    return "";
  }

  public void setPaxGoal( PaxGoal paxGoal )
  {
    this.paxGoal = paxGoal;
  }

  public String getPromotionName()
  {
    return promotion.getPromoNameFromCM();
  }

  public boolean isChallengePointPromotion()
  {
    return promotion.isChallengePointPromotion();
  }

  public boolean isPartnersEnabled()
  {
    return partnersEnabled;
  }

  public Long getPaxGoalId()
  {
    return paxGoalId;
  }

  public void setPaxGoalId( Long paxGoalId )
  {
    this.paxGoalId = paxGoalId;
  }

  public void setPartnersEnabled( boolean partnersEnabled )
  {
    this.partnersEnabled = partnersEnabled;
  }

  public boolean isGoalQuestPromotion()
  {
    return promotion.isGoalQuestPromotion();
  }

  public boolean isUserAbleToSelectSpecificProduct()
  {
    return promotion.getMerchGiftCodeType() != null && promotion.getMerchGiftCodeType().getCode().equals( MerchGiftCodeType.PRODUCT );
  }

  public boolean isUserAbleToChangeProducts()
  {
    return promotion.getSubmissionEndDate().after( new Date() );
  }

  public AwardProductView getProductView()
  {
    return productView;
  }

  public void setProductView( AwardProductView productView )
  {
    this.productView = productView;
  }

  public String getOverview()
  {
    return promotion.getOverviewDetailsText();
  }

  public String getRules()
  {
    return promotion.getWebRulesText();
  }

  public GoalQuestPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( GoalQuestPromotion promotion )
  {
    this.promotion = promotion;
  }

  public String getPromotionStartDate()
  {
    return DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
  }

  public String getPromotionEndDate()
  {
    return DateUtils.toDisplayString( promotion.getSubmissionEndDate() );
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

  public String getAwardType()
  {
    return promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) ? "points" : "plateau";
  }

  public boolean isGoalAchieved()
  {
    return goalAchieved;
  }

  public void setGoalAchieved( boolean truth )
  {
    this.goalAchieved = truth;
  }

  public boolean getIsProductOnly()
  {
    if ( getPromotion().getMerchGiftCodeType() != null && getPromotion().getMerchGiftCodeType().getCode().equals( MerchGiftCodeType.PRODUCT ) )
    {
      return true;
    }
    return false;
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

  public List<ChallengepointReviewProgress> getCpAutoProgressList()
  {
    return cpAutoProgressList;
  }

  public void setCpAutoProgressList( List<ChallengepointReviewProgress> cpAutoProgressList )
  {
    this.cpAutoProgressList = cpAutoProgressList;
  }

  public void setCalculatedAchievementAmount( String calculatedAchievementAmount )
  {
    this.calculatedAchievementAmount = calculatedAchievementAmount;
  }

  public String getCalculatedAchievementAmount()
  {
    return calculatedAchievementAmount;
  }

  public void setPartnerPayout( String partnerPayout )
  {
    this.partnerPayout = partnerPayout;
  }

  public String getPartnerPayout()
  {
    return partnerPayout;
  }

  public String getLocaleBasedGqPercentToGoal()
  {
    if ( this.getGqProgress() != null && this.getGqProgress().getPercentToGoal() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getGqProgress().getPercentToGoal(), promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getLocaleBasedCpPercentToGoal()
  {
    if ( this.getCpProgress() != null && this.getCpProgress().getPercentToGoal() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getCpProgress().getPercentToGoal(), promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

}
