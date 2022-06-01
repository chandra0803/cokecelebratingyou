
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class SmackTalkView
{
  private Long id;
  private String smackTalkPageDetailUrl;
  private SmackTalkParticipantView player1 = new SmackTalkParticipantView();
  private SmackTalkParticipantView player2 = new SmackTalkParticipantView();
  private SmackTalkParticipantView commenterMain;
  private boolean isMyMatch;
  private String comment;
  private String promotionType;
  private String promotionName;
  private String time;
  private boolean detail;
  private Long matchId;
  private boolean liked;
  private long numLikers;
  private boolean hidden;
  private boolean mine;
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

  private List<SmackTalkCommentViewBean> comments = new ArrayList<SmackTalkCommentViewBean>();

  private String avatarUrl = null;

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getId()
  {
    return id;
  }

  public void setSmackTalkPageDetailUrl( String smackTalkPageDetailUrl )
  {
    this.smackTalkPageDetailUrl = smackTalkPageDetailUrl;
  }

  public String getSmackTalkPageDetailUrl()
  {
    return smackTalkPageDetailUrl;
  }

  public void setIsMyMatch( boolean isMyMatch )
  {
    this.isMyMatch = isMyMatch;
  }

  public boolean getIsMyMatch()
  {
    return isMyMatch;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public String getComment()
  {
    return comment;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setTime( String time )
  {
    this.time = time;
  }

  public String getTime()
  {
    return time;
  }

  public void setComments( List<SmackTalkCommentViewBean> comments )
  {
    this.comments = comments;
  }

  public List<SmackTalkCommentViewBean> getComments()
  {
    return comments;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setCommenterMain( SmackTalkParticipantView commenter )
  {
    this.commenterMain = commenter;
  }

  @JsonProperty( "commenterMain" )
  public SmackTalkParticipantView getCommenterMain()
  {
    return commenterMain;
  }

  public void setPlayer1( SmackTalkParticipantView player1 )
  {
    this.player1 = player1;
  }

  @JsonProperty( "player1" )
  public SmackTalkParticipantView getPlayer1()
  {
    return player1;
  }

  public void setPlayer2( SmackTalkParticipantView player2 )
  {
    this.player2 = player2;
  }

  @JsonProperty( "player2" )
  public SmackTalkParticipantView getPlayer2()
  {
    return player2;
  }

  public void setDetail( boolean detail )
  {
    this.detail = detail;
  }

  @JsonProperty( "isDetail" )
  public boolean getDetail()
  {
    return detail;
  }

  public void setMatchId( Long matchId )
  {
    this.matchId = matchId;
  }

  public Long getMatchId()
  {
    return matchId;
  }

  public void setLiked( boolean liked )
  {
    this.liked = liked;
  }

  @JsonProperty( "isLiked" )
  public boolean getLiked()
  {
    return liked;
  }

  public void setNumLikers( long numLikers )
  {
    this.numLikers = numLikers;
  }

  public long getNumLikers()
  {
    return numLikers;
  }

  public void setHidden( boolean hidden )
  {
    this.hidden = hidden;
  }

  @JsonProperty( "isHidden" )
  public boolean getHidden()
  {
    return hidden;
  }

  public void setMine( boolean mine )
  {
    this.mine = mine;
  }

  @JsonProperty( "isMine" )
  public boolean getMine()
  {
    return mine;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

}
