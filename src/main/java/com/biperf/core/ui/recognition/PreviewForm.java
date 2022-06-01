
package com.biperf.core.ui.recognition;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.NumberFormatUtil;

public class PreviewForm extends BaseForm
{
  private String promotionName;
  private String promotionType;
  private Long cardId;
  private String cardUrl;
  private String selectedBehavior = "";

  private long availableBudget;
  private long thisIssuance;
  private long remainingBudget;
  private boolean displayRoundingDisclaimer = false;

  private boolean awardActive;
  private boolean budgetActive;
  private boolean showAwardColumn;
  private List<ClaimElement> claimElements = new ArrayList<ClaimElement>();

  private PromotionAwardsType awardType;

  private boolean purlEnabled;
  private boolean publicRecognitionEnabled;
  private boolean privateRecognitionEnabled;
  
  // Client customizations for WIP #42701 starts
  private boolean showCurrencyColumn;
  // Client customizations for WIP #42701 ends
  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public Long getCardId()
  {
    return cardId;
  }

  public void setCardId( Long cardId )
  {
    this.cardId = cardId;
  }

  public String getCardUrl()
  {
    return cardUrl;
  }

  public void setCardUrl( String cardUrl )
  {
    this.cardUrl = cardUrl;
  }

  public String getSelectedBehavior()
  {
    return selectedBehavior;
  }

  public void setSelectedBehavior( String selectedBehavior )
  {
    this.selectedBehavior = selectedBehavior;
  }

  public long getAvailableBudget()
  {
    return availableBudget;
  }

  public void setAvailableBudget( long availableBudget )
  {
    this.availableBudget = availableBudget;
  }

  public long getThisIssuance()
  {
    return thisIssuance;
  }

  public void setThisIssuance( long thisIssuance )
  {
    this.thisIssuance = thisIssuance;
  }

  public long getRemainingBudget()
  {
    return remainingBudget;
  }

  public void setRemainingBudget( long remainingBudget )
  {
    this.remainingBudget = remainingBudget;
  }

  public void setDisplayRoundingDisclaimer( boolean displayRoundingDisclaimer )
  {
    this.displayRoundingDisclaimer = displayRoundingDisclaimer;
  }

  public boolean isDisplayRoundingDisclaimer()
  {
    return displayRoundingDisclaimer;
  }

  public boolean isAwardActive()
  {
    return awardActive;
  }

  public void setAwardActive( boolean awardActive )
  {
    this.awardActive = awardActive;
  }

  public boolean isBudgetActive()
  {
    return budgetActive;
  }

  public void setBudgetActive( boolean budgetActive )
  {
    this.budgetActive = budgetActive;
  }

  public List<ClaimElement> getClaimElements()
  {
    return claimElements;
  }

  public void setClaimElements( List<ClaimElement> claimElements )
  {
    this.claimElements = claimElements;
  }

  public boolean isMerchandiseAward()
  {
    return awardType != null && awardType.isMerchandiseAwardType();
  }

  public boolean isPointsAward()
  {
    return awardType != null && awardType.isPointsAwardType();
  }

  public void setAwardType( PromotionAwardsType awardType )
  {
    this.awardType = awardType;
  }

  public void setShowAwardColumn( boolean showAwardColumn )
  {
    this.showAwardColumn = showAwardColumn;
  }

  public boolean isShowAwardColumn()
  {
    return showAwardColumn;
  }

  public boolean isPurlEnabled()
  {
    return purlEnabled;
  }

  public void setPurlEnabled( boolean purlEnabled )
  {
    this.purlEnabled = purlEnabled;
  }

  public String getDisplayAvailableBudget()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( this.availableBudget );
  }

  public String getDisplayThisIssuance()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( this.thisIssuance );
  }

  public String getDisplayRemainingBudget()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( this.remainingBudget );
  }

  public boolean isPublicRecognitionEnabled()
  {
    return publicRecognitionEnabled;
  }

  public void setPublicRecognitionEnabled( boolean publicRecognitionEnabled )
  {
    this.publicRecognitionEnabled = publicRecognitionEnabled;
  }

  public boolean isPrivateRecognitionEnabled()
  {
    return privateRecognitionEnabled;
  }

  public void setPrivateRecognitionEnabled( boolean privateRecognitionEnabled )
  {
    this.privateRecognitionEnabled = privateRecognitionEnabled;
  }


  // Client customizations for WIP #42701 starts
  public boolean isShowCurrencyColumn()
  {
    return showCurrencyColumn;
  }

  public void setShowCurrencyColumn( boolean showCurrencyColumn )
  {
    this.showCurrencyColumn = showCurrencyColumn;
  }
  // Client customizations for WIP #42701 ends
}
