
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.SmackTalkSet;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.UserManager;

public class SmackTalkMainView extends BaseJsonView
{
  private static final long serialVersionUID = 1L;

  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  public List<SmackTalkSet> smackTalkSets = new ArrayList<SmackTalkSet>();
  private SmackTalkView smackTalk;
  private String tabType;
  private boolean isDefault;
  private String smackTalkUrl;

  public SmackTalkMainView()
  {
  }

  public SmackTalkMainView( List<SmackTalkSet> smackTalkSets )
  {
    for ( SmackTalkSet prs : smackTalkSets )
    {
      SmackTalkSet set = new SmackTalkSet( prs.getNameId(), prs.getName(), prs.getDesc(), prs.getTotalCount(), prs.getIsDefault() );
      if ( prs.getCommentBeans() != null && !prs.getCommentBeans().isEmpty() )
      {
        for ( SmackTalkCommentViewBean commentBean : prs.getCommentBeans() )
        {
          ThrowdownPromotion promotion = commentBean.getMatchBean().getPromotion();
          SmackTalkView smackView = new SmackTalkView();
          smackView.setPromotionName( promotion.getName() );
          smackView.setPromotionType( PromotionType.THROWDOWN );
          smackView.setMatchId( commentBean.getMatchBean().getMatchId() );
          smackView.setIsMyMatch( commentBean.getMatchBean().isMine() );
          smackView.setDetail( false );
          smackView.setCommenterMain( commentBean.getCommenter() );
          smackView.setComments( commentBean.getCommentsPerPost() );
          smackView.setComment( commentBean.getComment() );
          smackView.setId( commentBean.getId() );
          smackView.setLiked( commentBean.getLiked() );
          smackView.setNumLikers( commentBean.getNumLikers() );
          smackView.setHidden( commentBean.getHidden() );
          smackView.setTime( commentBean.getRelativePostCreatedDate() );

          if ( commentBean.getCommenter().getId().equals( UserManager.getUserId() ) )
          {
            smackView.setMine( true );
          }
          Map<String, Long> paramMap = new HashMap<String, Long>();
          paramMap.put( "matchId", commentBean.getMatchBean().getMatchId() );
          smackView.setSmackTalkPageDetailUrl( commentBean.getMatchBean().getMatchUrl() );

          if ( !commentBean.getMatchBean().getPrimaryTeam().getTeam().isShadowPlayer() )
          {
            Participant pax = commentBean.getMatchBean().getPrimaryTeam().getTeam().getParticipant();
            smackView.setPlayer1( new SmackTalkParticipantView( pax.getId(), pax.getFirstName(), pax.getLastName(), pax.getAvatarSmallFullPath(), null, null, null ) );
          }
          else
          {
            smackView.setPlayer1( new SmackTalkParticipantView( null, promotion.getTeamUnavailableResolverType().getName(), "", null, null, null, null ) );

          }
          if ( !commentBean.getMatchBean().getSecondaryTeam().getTeam().isShadowPlayer() )
          {
            Participant pax = commentBean.getMatchBean().getSecondaryTeam().getTeam().getParticipant();
            smackView.setPlayer2( new SmackTalkParticipantView( pax.getId(), pax.getFirstName(), pax.getLastName(), pax.getAvatarSmallFullPath(), null, null, null ) );
          }
          else
          {
            smackView.setPlayer2( new SmackTalkParticipantView( null, promotion.getTeamUnavailableResolverType().getName(), "", null, null, null, null ) );
          }
          set.getSmackTalks().add( smackView );

        } // for
      } // set if
      this.smackTalkSets.add( set );
    } // set for
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public void setTabType( String tabType )
  {
    this.tabType = tabType;
  }

  public String getTabType()
  {
    return tabType;
  }

  public void setDefault( boolean isDefault )
  {
    this.isDefault = isDefault;
  }

  public boolean isDefault()
  {
    return isDefault;
  }

  public void setSmackTalk( SmackTalkView smackTalk )
  {
    this.smackTalk = smackTalk;
  }

  public SmackTalkView getSmackTalk()
  {
    return smackTalk;
  }

  public void setSmackTalkUrl( String smackTalkUrl )
  {
    this.smackTalkUrl = smackTalkUrl;
  }

  public String getSmackTalkUrl()
  {
    return smackTalkUrl;
  }
}
