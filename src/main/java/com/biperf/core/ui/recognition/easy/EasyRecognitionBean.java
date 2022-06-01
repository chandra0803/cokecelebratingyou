
package com.biperf.core.ui.recognition.easy;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.RecognitionBean;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EasyRecognitionBean
{
  private List<String> messages = new ArrayList<String>();
  private List<ParticipantInfoView> recipientInfo = new ArrayList<ParticipantInfoView>();
  private List<PromotionBean> promotions;
  private List<NodeBean> nodes;
  private List<NodeBean> recipientNodes;

  public void setRecipientInfo( Participant pax )
  {
    recipientInfo.add( new ParticipantInfoView( pax ) );
  }

  public List<ParticipantInfoView> getRecipientInfo()
  {
    return recipientInfo;
  }

  public List<NodeBean> getNodes()
  {
    return nodes;
  }

  public List<NodeBean> getRecipientNodes()
  {
    return recipientNodes;
  }

  public final void setPromotions( List<RecognitionBean> recognitionPromotions )
  {
    if ( recognitionPromotions != null && !recognitionPromotions.isEmpty() )
    {
      promotions = new ArrayList<PromotionBean>( recognitionPromotions.size() );
      for ( RecognitionBean p : recognitionPromotions )
      {
        promotions.add( new PromotionBean( p ) );
      }
    }
  }

  public List<PromotionBean> getPromotions()
  {
    return promotions;
  }

  public final void setUserNodes( List<UserNode> userNodes )
  {
    if ( userNodes != null && !userNodes.isEmpty() )
    {
      nodes = new ArrayList<NodeBean>( userNodes.size() );
      for ( UserNode userNode : userNodes )
      {
        nodes.add( new NodeBean( userNode ) );
      }
    }
  }

  public final void setRecipientNodes( List<UserNode> userNodes )
  {
    if ( userNodes != null && !userNodes.isEmpty() )
    {
      recipientNodes = new ArrayList<NodeBean>( userNodes.size() );
      for ( UserNode userNode : userNodes )
      {
        recipientNodes.add( new NodeBean( userNode ) );
      }
    }
  }

  public static class NodeBean extends NameableBean
  {
    public NodeBean( UserNode userNode )
    {
      super( userNode.getNode().getId(), userNode.getNode().getName() );
    }
  }

  public static class PromotionBean extends NameableBean
  {
    private PromotionAttributes attributes;

    public PromotionBean( RecognitionBean promotion )
    {
      super( promotion.getId(), promotion.getName() );
      boolean commentsActive = true;
      if ( promotion.isPurlPromotion() || promotion.isNomination() )
      {
        commentsActive = false;
      }
      attributes = new PromotionAttributes( promotion.isEasy(), commentsActive, promotion.isEcardsActive() );
    }

    public PromotionAttributes getAttributes()
    {
      return attributes;
    }
  }

  public static class PromotionAttributes
  {
    private boolean easy;
    private boolean commentsActive;
    private boolean ecardsActive;

    public PromotionAttributes( boolean easy, boolean commentsActive, boolean ecardsActive )
    {
      this.easy = easy;
      this.commentsActive = commentsActive;
      this.ecardsActive = ecardsActive;
    }

    public boolean isCommentsActive()
    {
      return commentsActive;
    }

    @JsonProperty( value = "isEasy" )
    public boolean isEasy()
    {
      return easy;
    }

    public boolean isEcardsActive()
    {
      return ecardsActive;
    }

  }
}
