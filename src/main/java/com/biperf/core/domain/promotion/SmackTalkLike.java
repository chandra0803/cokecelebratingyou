
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

@SuppressWarnings( "serial" )
public class SmackTalkLike extends BaseDomain
{
  private boolean liked;
  private Participant user;
  private SmackTalkComment smackTalkComment;

  public void setLiked( boolean liked )
  {
    this.liked = liked;
  }

  public boolean isLiked()
  {
    return liked;
  }

  public Participant getUser()
  {
    return user;
  }

  public void setUser( Participant user )
  {
    this.user = user;
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
    if ( ! ( obj instanceof SmackTalkLike ) )
    {
      return false;
    }

    final SmackTalkLike like = (SmackTalkLike)obj;

    if ( getId() != null ? !getId().equals( like.getId() ) : like.getId() != null )
    {
      return false;
    }

    if ( getUser() != null ? !getUser().equals( like.getUser() ) : like.getUser() != null )
    {
      return false;
    }

    if ( getSmackTalkComment() != null ? !getSmackTalkComment().equals( like.getSmackTalkComment() ) : like.getSmackTalkComment() != null )
    {
      return false;
    }

    return true;
  }

  public void setSmackTalkComment( SmackTalkComment smackTalkComment )
  {
    this.smackTalkComment = smackTalkComment;
  }

  public SmackTalkComment getSmackTalkComment()
  {
    return smackTalkComment;
  }

}
