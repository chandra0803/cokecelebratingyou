
package com.biperf.core.service.throwdown;

import java.util.List;

import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.domain.promotion.SmackTalkLike;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

public interface SmackTalkService extends SAO
{

  public static String BEAN_NAME = "smackTalkService";

  /**
   * 
   * @param smackTalkId
   * @return SmackTalkLike
   */
  public List<SmackTalkLike> getUserLikesByComments( Long smackTalkId );

  /**
   * 
   * @param matchId
   * @return SmackTalkComment
   */
  public List<SmackTalkComment> getSmackTalkPostsByMatch( Long matchId );

  /**
   * 
   * @param smackTalkId
   * @return SmackTalkComment
   */
  public List<SmackTalkComment> getUserCommentsBySmackTalkPost( Long smackTalkId );

  /**
   * 
   * @param saveSmackTalkComment
   * @return SmackTalkComment
   */
  public SmackTalkComment saveSmackTalkComment( SmackTalkComment saveSmackTalkComment );

  /**
   * 
   * @param saveSmackTalkLike
   * @return SmackTalkLike
   */
  public SmackTalkLike saveSmackTalkLike( SmackTalkLike saveSmackTalkLike );

  /**
   * 
   * @param smackTalkId
   * @return long
   */
  public long getLikeCountBySmackTalk( Long smackTalkId );

  /**
   * 
   * @param smackTalkId
   * @param userId
   * @return true if current user liked the smacktalk
   */
  public boolean isCurrentUserLikedSmackTalk( Long smackTalkId, Long userId );

  /**
   * 
   * @param matchId
   * @return SmackTalkComment
   */
  public List<SmackTalkComment> getSmackTalkPostsForMatch( Long[] matchIds );

  /**
   * 
   * @param  array of matchIds
   * @return List<PublicRecognitionComment>
   */
  public List<SmackTalkComment> getUserCommentsForSmackTalkPosts( Long[] smackTalkIds );

  /**
   * 
   * @param  array of matchIds
   * @return List<SmackTalkLike>
   */
  public List<SmackTalkLike> getUserLikesForComments( Long[] smackTalkIds );

  /**
   * 
   * @param  matchId
   * @return List<SmackTalkLike>
   */
  public List<SmackTalkLike> getLikedPaxListBySmackTalkId( Long smackTalkId, AssociationRequestCollection associationRequests, int pageNumber );

  public int getLikedPaxCount( Long smackTalkId );

  public void hideSmackTalkComment( Long smackTalkId );

  public SmackTalkComment getSmackTalkComment( Long smackTalkId );

}
