
package com.biperf.core.ui.mobilerecogapp;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.user.UserNode;
import com.biperf.core.mobileapp.recognition.service.EligibleRecognitionPromotion;
import com.biperf.core.ui.recognition.RecognitionStartBean;
import com.biperf.core.value.RecognitionBean;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PromotionsListBean
{
  private List<RecognitionBean> recognitionBeans;

  private List<RecognitionStartBean.NodeBean> nodes;

  private List<PromotionBean> promotionBeans;

  private List<EligibleRecognitionPromotion> eligibleRecognitionPromotions;

  private Long primaryNodeId;

  // social media sharing
  private final SocialMediaSharingOptions socialMediaSharingOptions = new SocialMediaSharingOptions();

  // EZ Thanks
  private int celebrationCount;

  // social media sharing
  public List<EligibleRecognitionPromotion> getEligibleRecognitionPromotions()
  {
    return eligibleRecognitionPromotions;
  }

  public void setEligibleRecognitionPromotions( List<EligibleRecognitionPromotion> eligibleRecognitionPromotions )
  {
    this.eligibleRecognitionPromotions = eligibleRecognitionPromotions;
  }

  @JsonIgnore
  public List<RecognitionBean> getRecognitionBeans()
  {
    return recognitionBeans;
  }

  public void setRecognitionBeans( List<RecognitionBean> recognitionBeans )
  {
    this.recognitionBeans = recognitionBeans;
  }

  public List<PromotionBean> getPromotionBeans()
  {
    return promotionBeans;
  }

  public void setPromotionBeans( List<PromotionBean> promotionBeans )
  {
    this.promotionBeans = promotionBeans;
  }

  @JsonIgnore
  public List<RecognitionStartBean.NodeBean> getNodes()
  {
    return nodes;
  }

  public Long getPrimaryNodeId()
  {
    return primaryNodeId;
  }

  public final void setUserNodes( List<UserNode> userNodes )
  {
    if ( userNodes != null && !userNodes.isEmpty() )
    {
      nodes = new ArrayList<>( userNodes.size() );
      for ( UserNode userNode : userNodes )
      {
        if ( userNode.getIsPrimary() )
        {
          this.primaryNodeId = userNode.getNode().getId();
        }
        nodes.add( new RecognitionStartBean.NodeBean( userNode ) );
      }
    }
  }

  public void setShareFacebook( boolean shareFacebook )
  {
    socialMediaSharingOptions.shareFacebook = shareFacebook;
  }

  public void setShareTwitter( boolean shareTwitter )
  {
    socialMediaSharingOptions.shareTwitter = shareTwitter;
  }

  public void setShareLinkedIn( boolean shareLinkedIn )
  {
    socialMediaSharingOptions.shareLinkedIn = shareLinkedIn;
  }

  public SocialMediaSharingOptions getSocialMediaSharingOptions()
  {
    return socialMediaSharingOptions;
  }

  public int getCelebrationCount()
  {
    return celebrationCount;
  }

  public void setCelebrationCount( int celebrationCount )
  {
    this.celebrationCount = celebrationCount;
  }

  public static class SocialMediaSharingOptions
  {
    private boolean shareFacebook;
    private boolean shareTwitter;
    private boolean shareLinkedIn;

    public boolean isShareFacebook()
    {
      return shareFacebook;
    }

    public boolean isShareTwitter()
    {
      return shareTwitter;
    }

    public boolean isShareLinkedIn()
    {
      return shareLinkedIn;
    }
  }

}
