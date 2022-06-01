
package com.biperf.core.ui.nomination;

import java.util.List;

import com.biperf.core.ui.nomination.NominationsPromotionListViewBean.NodeViewBean;
import com.biperf.core.value.nomination.NominationSubmitDataAttachmentValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataBehaviorValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataColorSettingValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataDrawSettingsValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataECardValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.nomination.NominationsPromotionListValueBean;
import com.biperf.core.value.nomination.NominationsPromotionListValueBean.NodeValueBean;

public class NominationsSubmitDataViewMapper
{
  private final NominationsPromotionListValueBean vb;
  private final NominationsPromotionListViewBean view = new NominationsPromotionListViewBean();

  public NominationsSubmitDataViewMapper( NominationsPromotionListValueBean valueBean )
  {
    this.vb = valueBean;
  }

  public NominationsPromotionListViewBean getViewBean()
  {

    view.setTotalEligiblePromotionCount( this.vb.getTotalEligiblePromotionCount() );

    populatePromoHeaderInfo();
    populateNodeView();
    populateBehaviours();
    populateDrawTolSetting();
    populateEcards();
    
    // Client customization for WIP #39189 starts
    populateNominationLinks();
    // Client customization for WIP #39189 ends

    return view;

  }

  private void populateEcards()
  {
    List<NominationSubmitDataECardValueBean> vb = this.vb.getPromotion().geteCards();

    List<NominationSubmitDataECardViewBean> view = this.view.getPromotion().geteCards();

    for ( NominationSubmitDataECardValueBean v : vb )
    {
      view.add( new NominationSubmitDataECardViewBean( v.getId(), v.getName(), v.getSmallImage(), v.getLargeImage(), v.isCanEdit(), v.getCardType(), v.isTranslatable(), v.getLocale() ) );
    }

  }

  private void populateDrawTolSetting()
  {
    NominationSubmitDataDrawSettingsValueBean vbSettings = this.vb.getPromotion().getDrawToolSettings();
    NominationSubmitDataDrawSettingsViewBean viewSettings = this.view.getPromotion().getDrawToolSettings();

    viewSettings.setCanDraw( vbSettings.isCanDraw() );
    viewSettings.setCanUpload( vbSettings.isCanUpload() );
    viewSettings.setSizes( vbSettings.getSizes() );

    for ( NominationSubmitDataColorSettingValueBean color : vbSettings.getColors() )
    {
      viewSettings.getColors().add( new NominationSubmitDataColorSettingViewBean( color.getHex(), color.getTitle() ) );
    }

  }

  private void populateBehaviours()
  {
    List<NominationSubmitDataBehaviorValueBean> behaviors = this.vb.getPromotion().getBehaviors();
    List<NominationSubmitDataBehaviorViewBean> behavioursView = this.view.getPromotion().getBehaviors();

    for ( NominationSubmitDataBehaviorValueBean behaviour : behaviors )
    {
      behavioursView.add( new NominationSubmitDataBehaviorViewBean( behaviour.getId(), behaviour.getName(), behaviour.getImage(), behaviour.isSelected(), behaviour.getPosition() ) );
    }
  }
  
//Client customization for WIP #39189 starts
 private void populateNominationLinks()
 {
   List<NominationSubmitDataAttachmentValueBean> attachmentValueBeans = this.vb.getPromotion().getNominationLinks();
   List<NominationSubmitDataAttachmentViewBean> attachmentViews = this.view.getPromotion().getNominationLinks();

   for ( NominationSubmitDataAttachmentValueBean attachmentValueBean : attachmentValueBeans )
   {
     attachmentViews.add( new NominationSubmitDataAttachmentViewBean( attachmentValueBean.getLinkid(),
                                                                      attachmentValueBean.getNominationLink(),
                                                                      attachmentValueBean.getNominationUrl(),
                                                                      attachmentValueBean.getFileName() ) );
   }
   this.view.getPromotion().setUpdatedDocCount( this.vb.getPromotion().getUpdatedDocCount() );
 }
 // Client customization for WIP #39189 ends


  private void populatePromoHeaderInfo()
  {
    NominationSubmitDataPromotionViewBean promotion = this.view.getPromotion();

    NominationSubmitDataPromotionValueBean vb = this.vb.getPromotion();

    promotion.setId( vb.getId() );
    promotion.setName( vb.getName() );
    promotion.setRulesText( vb.getRulesText() );
    promotion.setIndividualOrTeam( vb.getIndividualOrTeam() );
    promotion.setNominatingType( vb.getNominatingType() );
    promotion.setAwardsActive( vb.isAwardsActive() );
    promotion.setBehaviorsActive( vb.isBehaviorsActive() );
    promotion.seteCardsActive( vb.iseCardsActive() );
    promotion.setCustomFieldsActive( vb.isCustomFieldsActive() );
    promotion.setMaxParticipants( vb.getMaxParticipants() );
    promotion.setAwardType( vb.getAwardType() );
    promotion.setAwardMin( vb.getAwardMin() );
    promotion.setAwardMax( vb.getAwardMax() );
    promotion.setAwardFixed( vb.getAwardFixed() );
    promotion.setAwardQuantity( vb.getAwardQuantity() );
    promotion.setCurrentStep( vb.getCurrentStep() );
    promotion.setClaimId( vb.getClaimId() );
    promotion.setDefaultWhyActive( vb.getDefaultWhyActive() );
    promotion.setCustomBeforeDefault( vb.getCustomBeforeDefault() );
    promotion.setAttachmentActive( vb.getAttachmentActive() );
    promotion.setWhyTabLabel( vb.getWhyTabLabel() );
    promotion.setTotalPromotionCount( vb.getTotalPromotionCount() );
    promotion.setCurrencyLabel( vb.getCurrencyLabel() );
    promotion.setCmAssetCode( vb.getCmAssetCode() );
    promotion.setWebRulesCmKey( vb.getWebRulesCmKey() );
    promotion.setPromoNameAssetCode( vb.getPromoNameAssetCode() );
    promotion.set_comment( vb.get_comment() );
    promotion.setCardCanEdit( vb.isCardCanEdit() );
    promotion.setCardData( vb.getCardData() );
    promotion.setOwnCardName( vb.getOwnCardName() );
    promotion.setCardType( vb.getCardType() );
    promotion.setCardId( vb.getCardId() );
    promotion.setCardUrl( vb.getCardUrl() );
    promotion.setDrawingData( vb.getDrawingData() );
    promotion.setNomineeType( vb.getNomineeType() );
    promotion.setTeamName( vb.getTeamName() );
    promotion.setSaveTeamAsGroup( vb.isSaveTeamAsGroup() );
    promotion.setGroupName( vb.getGroupName() );
    promotion.setIsEditMode( vb.isEditMode() );
    promotion.setComments( vb.getComments() );
    promotion.setPrivateNomination( vb.isPrivateNomination() );
    promotion.setRecommendedAward( vb.isRecommendedAward() );
    promotion.setFileName( vb.getFileName() );
    promotion.setAddAttachment( vb.isAddAttachment() );
    promotion.setMoreThanOneBehavioursAllowed( vb.isMoreThanOneBehavioursAllowed() );
    promotion.setMaxBehaviorsAllowed( vb.getMaxBehaviorsAllowed() );
    promotion.setRecipientName( vb.getRecipientName() );
   // Client customization for WIP #39189 starts
    promotion.setMinDocsAllowed( vb.getMinDocsAllowed() );
    promotion.setMinLinksAllowed( vb.getMinLinksAllowed() );
    promotion.setMaxDocsAllowed( vb.getMaxDocsAllowed() );
    promotion.setMaxLinksAllowed( vb.getMaxLinksAllowed() );
    // Client customization for WIP #39189 ends
    
    // Client customizations for WIP #59418
    promotion.setTeamNameCopyBlock( vb.getTeamNameCopyBlock() );

  }

  private void populateNodeView()
  {
    List<NodeValueBean> nodesVB = this.vb.getNodes();

    for ( NodeValueBean v : nodesVB )
    {
      view.getNodes().add( new NodeViewBean( v.getId(), v.getName() ) );
    }

  }

}
