
package com.biperf.core.domain.promotion;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.SmackTalkCommentViewBean;
import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings( "serial" )
public class SmackTalkComment extends BaseDomain
{
  private List<SmackTalkComment> userComments;
  private List<SmackTalkCommentViewBean> commentView = new ArrayList<SmackTalkCommentViewBean>();
  private Participant user;
  private Match match;
  private boolean isHidden;
  private SmackTalkComment parent;
  private long numLikers;
  private boolean isLiked;
  private String comments;
  private Long parentSmackTalkId;
  private String relativePostCreatedDate;
  private boolean isMine;

  @JsonIgnore
  public Participant getUser()
  {
    return user;
  }

  @JsonIgnore
  public void setUser( Participant user )
  {
    this.user = user;
  }

  @JsonIgnore
  public Match getMatch()
  {
    return match;
  }

  @JsonIgnore
  public void setMatch( Match match )
  {
    this.match = match;
  }

  public List<SmackTalkCommentViewBean> getCommentView()
  {
    return commentView;
  }

  public void setComments( List<SmackTalkCommentViewBean> comments )
  {
    this.commentView = comments;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( getId() == null ? 0 : getId().hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( ! ( obj instanceof SmackTalkComment ) )
    {
      return false;
    }

    final SmackTalkComment comment = (SmackTalkComment)obj;

    if ( getId() != null ? !getId().equals( comment.getId() ) : comment.getId() != null )
    {
      return false;
    }

    if ( getUser() != null ? !getUser().equals( comment.getUser() ) : comment.getUser() != null )
    {
      return false;
    }
    if ( getMatch() != null ? !getMatch().equals( comment.getMatch() ) : comment.getMatch() != null )
    {
      return false;
    }

    return true;
  }

  public void setIsHidden( boolean isHidden )
  {
    this.isHidden = isHidden;
  }

  public boolean getIsHidden()
  {
    return isHidden;
  }

  @JsonIgnore
  public void setParent( SmackTalkComment parent )
  {
    this.parent = parent;
  }

  @JsonIgnore
  public SmackTalkComment getParent()
  {
    return parent;
  }

  public long getNumLikers()
  {
    return numLikers;
  }

  public void setNumLikers( long numLikers )
  {
    this.numLikers = numLikers;
  }

  public boolean isLiked()
  {
    return isLiked;
  }

  public void setLiked( boolean isLiked )
  {
    this.isLiked = isLiked;
  }

  public void setUserComments( List<SmackTalkComment> userComments )
  {
    this.userComments = userComments;
  }

  public List<SmackTalkComment> getUserComments()
  {
    return userComments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getComments()
  {
    return comments;
  }

  public Long getParentSmackTalkId()
  {
    return parentSmackTalkId;
  }

  public void setParentSmackTalkId( Long parentSmackTalkId )
  {
    this.parentSmackTalkId = parentSmackTalkId;
  }

  public String getRelativePostCreatedDate()
  {
    relativePostCreatedDate = DateUtils.toRelativeTimeLapsed( getAuditCreateInfo().getDateCreated() );
    return relativePostCreatedDate;
  }

  public void setMine( boolean isMine )
  {
    this.isMine = isMine;
  }

  public boolean isMine()
  {
    return isMine;
  }

}
