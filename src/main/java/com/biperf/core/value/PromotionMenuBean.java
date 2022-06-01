
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.RecognitionBean.BehaviorBean;

public class PromotionMenuBean implements java.io.Serializable
{
  private Promotion promotion;
  private boolean canSubmit = false;
  private boolean canViewRules = false;
  private Boolean canReceive = false;
  private boolean canViewAwards = false;
  private boolean canShop = false;
  private String recognizeSomeoneLink;
  private String browseAwardsLink;
  private String programRulesLink;
  private String shopLink;
  private boolean isPartner = false;
  private Boolean inSecondaryAudience;
  private String purlActivityLink;
  private boolean purlPromotion;
  private boolean viewTile = false;
  private String formPromotionId;
  private String formPromotionName;
  
  /*customization start */
  private List<BehaviorBean> behaviors = new ArrayList<BehaviorBean>();
  /*customization end */
  
//Client customization start WIP#25589
  private boolean utilizeParentBudgets;  
  
  public boolean isUtilizeParentBudgets()
  {
    return utilizeParentBudgets;
  }

  public void setUtilizeParentBudgets( boolean utilizeParentBudgets )
  {
    this.utilizeParentBudgets = utilizeParentBudgets;
  }
  //Client customization end WIP#25589

  public String getFormPromotionName()
  {
    return formPromotionName;
  }

  public void setFormPromotionName( String formPromotionName )
  {
    this.formPromotionName = formPromotionName;
  }

  public String getFormPromotionId()
  {
    return formPromotionId;
  }

  public void setFormPromotionId( String formPromotionId )
  {
    this.formPromotionId = formPromotionId;
  }

  public boolean isPurlPromotion()
  {
    return purlPromotion;
  }

  public void setPurlPromotion( boolean purlPromotion )
  {
    this.purlPromotion = purlPromotion;
  }

  public String getPurlActivityLink()
  {
    return purlActivityLink;
  }

  public void setPurlActivityLink( String purlActivityLink )
  {
    this.purlActivityLink = purlActivityLink;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public boolean isCanSubmit()
  {
    return canSubmit;
  }

  public void setCanSubmit( boolean canSubmit )
  {
    this.canSubmit = canSubmit;
  }

  public boolean isCanViewRules()
  {
    return canViewRules;
  }

  public void setCanViewRules( boolean canViewRules )
  {
    this.canViewRules = canViewRules;
  }

  public String getRecognizeSomeoneLink()
  {
    return recognizeSomeoneLink;
  }

  public void setRecognizeSomeoneLink( String recognizeSomeoneLink )
  {
    this.recognizeSomeoneLink = recognizeSomeoneLink;
  }

  public String getBrowseAwardsLink()
  {
    return browseAwardsLink;
  }

  public void setBrowseAwardsLink( String browseAwardsLink )
  {
    this.browseAwardsLink = browseAwardsLink;
  }

  public String getProgramRulesLink()
  {
    return programRulesLink;
  }

  public void setProgramRulesLink( String programRulesLink )
  {
    this.programRulesLink = programRulesLink;
  }

  public boolean isCanReceive()
  {
    if ( canReceive == null )
    {
      MainContentService contentService = (MainContentService)BeanLocator.getBean( MainContentService.BEAN_NAME );
      ParticipantService paxService = (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
      canReceive = new Boolean( contentService.checkIfReceivable( promotion, paxService.getParticipantById( UserManager.getUserId() ) ) );
    }
    return canReceive.booleanValue();
  }

  public void setCanReceive( Boolean canReceive )
  {
    this.canReceive = canReceive;
  }

  public boolean isPartner()
  {
    return isPartner;
  }

  public void setPartner( boolean isPartner )
  {
    this.isPartner = isPartner;
  }

  public String getShopLink()
  {
    return shopLink;
  }

  public void setShopLink( String shopLink )
  {
    this.shopLink = shopLink;
  }

  public boolean isCanViewAwards()
  {
    return canViewAwards;
  }

  public void setCanViewAwards( boolean canViewAwards )
  {
    this.canViewAwards = canViewAwards;
  }

  public boolean isCanShop()
  {
    return canShop;
  }

  public void setCanShop( boolean canShop )
  {
    this.canShop = canShop;
  }

  public Boolean getInSecondaryAudience()
  {
    return inSecondaryAudience;
  }

  public void setInSecondaryAudience( Boolean inSecondaryAudience )
  {
    this.inSecondaryAudience = inSecondaryAudience;
  }

  public boolean isViewTile()
  {
    return viewTile;
  }

  public void setViewTile( boolean viewTile )
  {
    this.viewTile = viewTile;
  }

  public List<BehaviorBean> getBehaviors()
  {
    return behaviors;
  }

  public void setBehaviors( List<BehaviorBean> behaviors )
  {
    this.behaviors = behaviors;
  }
}
