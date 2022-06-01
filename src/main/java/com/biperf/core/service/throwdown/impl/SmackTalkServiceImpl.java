
package com.biperf.core.service.throwdown.impl;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.throwdown.SmackTalkDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.domain.promotion.SmackTalkLike;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.throwdown.SmackTalkService;
import com.biperf.core.utils.UserManager;

public class SmackTalkServiceImpl implements SmackTalkService
{
  private SmackTalkDAO smackTalkDAO;

  public SmackTalkDAO getSmackTalkDAO()
  {
    return smackTalkDAO;
  }

  public void setSmackTalkDAO( SmackTalkDAO smackTalkDAO )
  {
    this.smackTalkDAO = smackTalkDAO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SmackTalkLike> getUserLikesByComments( Long smackTalkId )
  {
    return smackTalkDAO.getUserLikesByComments( smackTalkId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SmackTalkComment> getSmackTalkPostsByMatch( Long matchId )
  {
    return smackTalkDAO.getSmackTalkPostsByMatch( matchId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SmackTalkComment> getUserCommentsBySmackTalkPost( Long smackTalkId )
  {
    return smackTalkDAO.getUserCommentsBySmackTalkPost( smackTalkId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SmackTalkComment saveSmackTalkComment( SmackTalkComment saveSmackTalkComment )
  {
    return smackTalkDAO.saveSmackTalkComment( saveSmackTalkComment );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SmackTalkLike saveSmackTalkLike( SmackTalkLike saveSmackTalkLike )
  {
    saveSmackTalkLike.setLiked( true );
    return smackTalkDAO.saveSmackTalkLike( saveSmackTalkLike );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getLikeCountBySmackTalk( Long smackTalkId )
  {
    return smackTalkDAO.getLikeCountBySmackTalk( smackTalkId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCurrentUserLikedSmackTalk( Long smackTalkId, Long userId )
  {
    return smackTalkDAO.isCurrentUserLikedSmackTalk( smackTalkId, userId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SmackTalkComment> getSmackTalkPostsForMatch( Long[] matchIds )
  {
    return smackTalkDAO.getSmackTalkPostsForMatch( matchIds );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SmackTalkComment> getUserCommentsForSmackTalkPosts( Long[] smackTalkIds )
  {
    return smackTalkDAO.getUserCommentsForSmackTalkPosts( smackTalkIds );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SmackTalkLike> getUserLikesForComments( Long[] smackTalkIds )
  {
    return smackTalkDAO.getUserLikesForComments( smackTalkIds );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SmackTalkLike> getLikedPaxListBySmackTalkId( Long smackTalkId, AssociationRequestCollection associationRequests, int pageNumber )
  {
    List<SmackTalkLike> likers = smackTalkDAO.getLikedPaxListBySmackTalkId( smackTalkId, pageNumber );
    for ( Iterator<SmackTalkLike> iter = likers.iterator(); iter.hasNext(); )
    {
      Participant paxWhoLiked = iter.next().getUser();
      for ( Iterator iterator = associationRequests.iterator(); iterator.hasNext(); )
      {
        AssociationRequest req = (AssociationRequest)iterator.next();
        req.execute( paxWhoLiked );
      }
    }
    return likers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLikedPaxCount( Long smackTalkId )
  {
    int likedPax = smackTalkDAO.getLikedPaxCount( smackTalkId );
    if ( smackTalkDAO.isCurrentUserLikedSmackTalk( smackTalkId, UserManager.getUserId() ) )
    {
      return likedPax - 1;
    }
    else
    {
      return likedPax;
    }
  }

  /**
   * Update the smack talk comment to be hidden.
   * @param smackTalkId
   */
  @Override
  public void hideSmackTalkComment( Long smackTalkId )
  {
    SmackTalkComment smackTalkComment = smackTalkDAO.getSmackTalkComment( smackTalkId );
    smackTalkComment.setIsHidden( true );
    smackTalkDAO.saveSmackTalkComment( smackTalkComment );
  }

  public SmackTalkComment getSmackTalkComment( Long smackTalkId )
  {
    return smackTalkDAO.getSmackTalkComment( smackTalkId );
  }

}
