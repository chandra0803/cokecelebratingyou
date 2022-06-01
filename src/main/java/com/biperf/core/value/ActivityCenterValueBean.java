
package com.biperf.core.value;

import java.io.Serializable;
import java.util.Date;

import com.biperf.core.domain.enums.ActivityType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;

public class ActivityCenterValueBean implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Promotion promotion;
  private ActivityType activityType;
  private boolean showProgramRules = false;
  private boolean showOnlyProgramRules = false;
  private boolean showGiverActivityTracker = false;
  private boolean showReceiverActivityTracker = false;
  private boolean showGiverPromoLevelText = false;
  private boolean showReceiverPromoLevelText = false;
  private boolean showPromoToReceiver = false;
  private boolean showPromoToGiver = false;
  private boolean goalSelectable = false;
  private boolean goalUpdate = false;
  private boolean viewProgress = false;
  private boolean viewResults = false;
  private boolean cpSelectable = false;
  private boolean cpUpdate = false;
  private boolean viewBasicAwards = false;
  private boolean takeQuiz = false;
  private boolean resumeQuiz = false;
  private boolean quizCompleted = false;
  private boolean showBudgetApproval = false;
  private boolean showBudgetTracker = false;
  private boolean showBudgetPromo = false;
  private boolean showUpdatePurlPromo = false;
  private long numberOfSubmissions = 0;
  private long numberOfApprovables = 0;
  private long numberOfApproved = 0;
  private long numberOfDays = 0;
  private Date lastUpdated;
  private String goalSelectionEnddate;
  private String cpSelectionEndDate;
  private String promotionEndDate;
  private String onlineShoppingUrl;
  private long claimId;
  private int balance;
  private String giverCmAssetKey;
  private String receiverCmAssetKey;
  private String giverCmAssetCode;
  private String receiverCmAssetCode;
  private long numberOfContributors;
  private String wellnessUrl;
  private boolean showBudgetAllocator;
  private Long nodeId;

  public ActivityCenterValueBean()
  {
  }

  public ActivityType getActivityType()
  {
    return activityType;
  }

  public void setActivityType( ActivityType activityType )
  {
    this.activityType = activityType;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public boolean isPurlEnabled()
  {
    if ( null != promotion && promotion.isRecognitionPromotion() )
    {
      return ( (RecognitionPromotion)promotion ).isIncludePurl();
    }
    return false;
  }

  public boolean isShowProgramRules()
  {
    return showProgramRules;
  }

  public void setShowProgramRules( boolean showProgramRules )
  {
    this.showProgramRules = showProgramRules;
  }

  public long getNumberOfSubmissions()
  {
    return numberOfSubmissions;
  }

  public void setNumberOfSubmissions( long numberOfSubmissions )
  {
    this.numberOfSubmissions = numberOfSubmissions;
  }

  public void setNumberOfApprovables( long numberOfApprovables )
  {
    this.numberOfApprovables = numberOfApprovables;
  }

  public long getNumberOfApprovables()
  {
    return numberOfApprovables;
  }

  public Date getLastUpdated()
  {
    return lastUpdated;
  }

  public void setLastUpdated( Date lastUpdated )
  {
    this.lastUpdated = lastUpdated;
  }

  public boolean isGoalSelectable()
  {
    return goalSelectable;
  }

  public void setGoalSelectable( boolean goalSelectable )
  {
    this.goalSelectable = goalSelectable;
  }

  public boolean isGoalUpdate()
  {
    return goalUpdate;
  }

  public void setGoalUpdate( boolean goalUpdate )
  {
    this.goalUpdate = goalUpdate;
  }

  public boolean isViewProgress()
  {
    return viewProgress;
  }

  public void setViewProgress( boolean viewProgress )
  {
    this.viewProgress = viewProgress;
  }

  public boolean isViewResults()
  {
    return viewResults;
  }

  public void setViewResults( boolean viewResults )
  {
    this.viewResults = viewResults;
  }

  public boolean isCpSelectable()
  {
    return cpSelectable;
  }

  public void setCpSelectable( boolean cpSelectable )
  {
    this.cpSelectable = cpSelectable;
  }

  public boolean isCpUpdate()
  {
    return cpUpdate;
  }

  public void setCpUpdate( boolean cpUpdate )
  {
    this.cpUpdate = cpUpdate;
  }

  public boolean isShowBudgetApproval()
  {
    return showBudgetApproval;
  }

  public void setShowBudgetApproval( boolean showBudgetApproval )
  {
    this.showBudgetApproval = showBudgetApproval;
  }

  public String getOnlineShoppingUrl()
  {
    return onlineShoppingUrl;
  }

  public void setOnlineShoppingUrl( String onlineShoppingUrl )
  {
    this.onlineShoppingUrl = onlineShoppingUrl;
  }

  public boolean isShowOnlyProgramRules()
  {
    return showOnlyProgramRules;
  }

  public void setShowOnlyProgramRules( boolean showOnlyProgramRules )
  {
    this.showOnlyProgramRules = showOnlyProgramRules;
  }

  public String getGoalSelectionEnddate()
  {
    return goalSelectionEnddate;
  }

  public void setGoalSelectionEnddate( String goalSelectionEnddate )
  {
    this.goalSelectionEnddate = goalSelectionEnddate;
  }

  public String getCpSelectionEndDate()
  {
    return cpSelectionEndDate;
  }

  public void setCpSelectionEndDate( String cpSelectionEndDate )
  {
    this.cpSelectionEndDate = cpSelectionEndDate;
  }

  public int getBalance()
  {
    return balance;
  }

  public void setBalance( int balance )
  {
    this.balance = balance;
  }

  public boolean isShowBudgetTracker()
  {
    return showBudgetTracker;
  }

  public void setShowBudgetTracker( boolean showBudgetTracker )
  {
    this.showBudgetTracker = showBudgetTracker;
  }

  public boolean isShowBudgetPromo()
  {
    return showBudgetPromo;
  }

  public void setShowBudgetPromo( boolean showBudgetPromo )
  {
    this.showBudgetPromo = showBudgetPromo;
  }

  public String getPromotionEndDate()
  {
    return promotionEndDate;
  }

  public void setPromotionEndDate( String promotionEndDate )
  {
    this.promotionEndDate = promotionEndDate;
  }

  public long getNumberOfDays()
  {
    return numberOfDays;
  }

  public void setNumberOfDays( long numberOfDays )
  {
    this.numberOfDays = numberOfDays;
  }

  public boolean isTakeQuiz()
  {
    return takeQuiz;
  }

  public void setTakeQuiz( boolean takeQuiz )
  {
    this.takeQuiz = takeQuiz;
  }

  public boolean isResumeQuiz()
  {
    return resumeQuiz;
  }

  public void setResumeQuiz( boolean resumeQuiz )
  {
    this.resumeQuiz = resumeQuiz;
  }

  public boolean isQuizCompleted()
  {
    return quizCompleted;
  }

  public void setQuizCompleted( boolean quizCompleted )
  {
    this.quizCompleted = quizCompleted;
  }

  public long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( long claimId )
  {
    this.claimId = claimId;
  }

  public boolean isShowGiverActivityTracker()
  {
    return showGiverActivityTracker;
  }

  public void setShowGiverActivityTracker( boolean showGiverActivityTracker )
  {
    this.showGiverActivityTracker = showGiverActivityTracker;
  }

  public boolean isShowReceiverActivityTracker()
  {
    return showReceiverActivityTracker;
  }

  public void setShowReceiverActivityTracker( boolean showReceiverActivityTracker )
  {
    this.showReceiverActivityTracker = showReceiverActivityTracker;
  }

  public boolean isShowGiverPromoLevelText()
  {
    return showGiverPromoLevelText;
  }

  public void setShowGiverPromoLevelText( boolean showGiverPromoLevelText )
  {
    this.showGiverPromoLevelText = showGiverPromoLevelText;
  }

  public boolean isShowReceiverPromoLevelText()
  {
    return showReceiverPromoLevelText;
  }

  public void setShowReceiverPromoLevelText( boolean showReceiverPromoLevelText )
  {
    this.showReceiverPromoLevelText = showReceiverPromoLevelText;
  }

  public String getGiverCmAssetKey()
  {
    return giverCmAssetKey;
  }

  public void setGiverCmAssetKey( String giverCmAssetKey )
  {
    this.giverCmAssetKey = giverCmAssetKey;
  }

  public String getReceiverCmAssetKey()
  {
    return receiverCmAssetKey;
  }

  public void setReceiverCmAssetKey( String receiverCmAssetKey )
  {
    this.receiverCmAssetKey = receiverCmAssetKey;
  }

  public String getGiverCmAssetCode()
  {
    return giverCmAssetCode;
  }

  public void setGiverCmAssetCode( String giverCmAssetCode )
  {
    this.giverCmAssetCode = giverCmAssetCode;
  }

  public String getReceiverCmAssetCode()
  {
    return receiverCmAssetCode;
  }

  public void setReceiverCmAssetCode( String receiverCmAssetCode )
  {
    this.receiverCmAssetCode = receiverCmAssetCode;
  }

  public boolean isShowPromoToReceiver()
  {
    return showPromoToReceiver;
  }

  public void setShowPromoToReceiver( boolean showPromoToReceiver )
  {
    this.showPromoToReceiver = showPromoToReceiver;
  }

  public boolean isShowPromoToGiver()
  {
    return showPromoToGiver;
  }

  public void setShowPromoToGiver( boolean showPromoToGiver )
  {
    this.showPromoToGiver = showPromoToGiver;
  }

  public boolean isViewBasicAwards()
  {
    return viewBasicAwards;
  }

  public void setViewBasicAwards( boolean viewBasicAwards )
  {
    this.viewBasicAwards = viewBasicAwards;
  }

  public boolean isShowUpdatePurlPromo()
  {
    return showUpdatePurlPromo;
  }

  public void setShowUpdatePurlPromo( boolean showUpdatePurlPromo )
  {
    this.showUpdatePurlPromo = showUpdatePurlPromo;
  }

  public long getNumberOfContributors()
  {
    return numberOfContributors;
  }

  public void setNumberOfContributors( long numberOfContributors )
  {
    this.numberOfContributors = numberOfContributors;
  }

  public String getWellnessUrl()
  {
    return wellnessUrl;
  }

  public void setWellnessUrl( String wellnessUrl )
  {
    this.wellnessUrl = wellnessUrl;
  }

  public void setShowBudgetAllocator( boolean showBudgetAllocator )
  {
    this.showBudgetAllocator = showBudgetAllocator;
  }

  public boolean isShowBudgetAllocator()
  {
    return showBudgetAllocator;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public long getNumberOfApproved()
  {
    return numberOfApproved;
  }

  public void setNumberOfApproved( long numberOfApproved )
  {
    this.numberOfApproved = numberOfApproved;
  }

}
