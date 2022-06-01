
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class SmackTalkCommentViewBean
{
  private Long id;
  private Long matchId;
  private String comment;
  private SmackTalkParticipantView commenter;
  private List<SmackTalkCommentViewBean> commentsPerPost = new ArrayList<SmackTalkCommentViewBean>();
  private boolean liked;
  private long numLikers;
  private boolean hidden;
  private ThrowdownMatchBean matchBean;
  private String relativePostCreatedDate;
  private AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
  private boolean mine;

  public SmackTalkCommentViewBean()
  {

  }

  public SmackTalkCommentViewBean( SmackTalkComment stc )
  {
    this.matchId = stc.getMatch().getId();
    this.commenter = new SmackTalkParticipantView( stc.getUser() );
    this.id = stc.getId();
    this.comment = stc.getComments();
    this.commentsPerPost = stc.getCommentView();
    this.liked = stc.isLiked();
    this.numLikers = stc.getNumLikers();
    this.mine = stc.isMine();
  }

  public Long getMatchId()
  {
    return matchId;
  }

  public void setMatchId( Long matchId )
  {
    this.matchId = matchId;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public SmackTalkParticipantView getCommenter()
  {
    return commenter;
  }

  public void setCommenter( SmackTalkParticipantView commenter )
  {
    this.commenter = commenter;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getId()
  {
    return id;
  }

  public void setCommentsPerPost( List<SmackTalkCommentViewBean> commentsPerPost )
  {
    this.commentsPerPost = commentsPerPost;
  }

  public List<SmackTalkCommentViewBean> getCommentsPerPost()
  {
    return commentsPerPost;
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

  public void setMatchBean( ThrowdownMatchBean matchBean )
  {
    this.matchBean = matchBean;
  }

  public ThrowdownMatchBean getMatchBean()
  {
    return matchBean;
  }

  @JsonIgnore
  public String getRelativePostCreatedDate()
  {
    relativePostCreatedDate = DateUtils.toRelativeTimeLapsed( getAuditCreateInfo().getDateCreated() );
    return relativePostCreatedDate.replaceFirst( "A", "a" );
  }

  public void setAuditCreateInfo( AuditCreateInfo auditCreateInfo )
  {
    this.auditCreateInfo = auditCreateInfo;
  }

  public AuditCreateInfo getAuditCreateInfo()
  {
    return auditCreateInfo;
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

}
