
package com.biperf.core.dao.throwdown;

import java.util.List;
import java.util.Set;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.domain.promotion.SmackTalkLike;

public interface SmackTalkDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "smackTalkDAO";

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
   * @param savePublicRecognitionComment
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
   * @return true if current user liked the given claim else false
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
   * @param  array of smackTalkIdIds
   * @return List<SmackTalkComment>
   */
  public List<SmackTalkComment> getUserCommentsForSmackTalkPosts( Long[] smackTalkIds );

  /**
   * 
   * @param  array of smackTalkIds
   * @return List<SmackTalkLike>
   */
  public List<SmackTalkLike> getUserLikesForComments( Long[] smackTalkIds );

  /**
   * 
   * @param  smackTalkId
   * @return List<SmackTalkLike>
   */
  public List<SmackTalkLike> getLikedPaxListBySmackTalkId( Long smackTalkId, int pageNumber );

  public int getLikedPaxCount( Long smackTalkId );

  public SmackTalkComment getSmackTalkComment( Long smackTalkId );

  public List<SmackTalkComment> getSmackTalkByTeam( Set<Long> teamIds, Integer roundNumber );

  public List<SmackTalkComment> getSmackTalkByPromotionAndRoundNumber( Long promotionId, Integer roundNumber );

  public List<SmackTalkComment> getSmackTalkByPromotionAndRoundIdAndTeam( Long promotionId, Long roundId, Long teamId );

  public List<SmackTalkComment> getSmackTalkByPromotionAndRoundIdAndUser( Long promotionId, Long roundId, Long userId );

}
