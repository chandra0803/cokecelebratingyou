/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/promotion/PublicRecognitionDAO.java,v $
 */

package com.biperf.core.dao.promotion;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.promotion.PublicRecognitionLike;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;

/**
 * 
 * @author veeramas
 * @since Jul 23, 2012
 * @version 1.0
 */
public interface PublicRecognitionDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "publicRecognitionDAO";

  /**
   * 
   * @param claimId
   * @return PublicRecognitionLike
   */
  public List<PublicRecognitionLike> getUserLikesByClaim( Long claimId );

  /**
   * 
   * @param savePublicRecognitionComment
   * @return PublicRecognitionComment
   */
  public PublicRecognitionComment savePublicRecognitionComment( PublicRecognitionComment savePublicRecognitionComment );

  /**
   * @param toDelete 
   */
  public void deletePublicRecognitionLike( PublicRecognitionLike toDelete );

  /**
   * 
   * @param savePublicRecognitionLike
   * @return PublicRecognitionLike
   */
  public PublicRecognitionLike savePublicRecognitionLike( PublicRecognitionLike savePublicRecognitionLike );

  /**
   * 
   * @param claimId
   * @return long
   */
  public long getLikeCountByClaimId( Long claimId );

  /**
   * 
   * @param claimId
   * @param userId
   * @return true if current user liked the given claim else false
   */
  public boolean isCurrentUserLikedClaim( Long claimId, Long userId );

  /**
   * 
   * @param  claimId
   * @return List<PublicRecognitionLike>
   */
  public List<PublicRecognitionLike> getLikedPaxListByClaimId( Long claimId, int pageNumber );

  public int getLikedPaxCount( Long claimId );

  public PublicRecognitionComment getPublicRecognitionCommentBy( Long publicRecogntionCommentId );

  public void deletePublicRecognitionComment( PublicRecognitionComment prc );

  public List<PublicRecognitionLike> getUserLikesByTeamId( Long teamId );

  public List<PublicRecognitionComment> getUserCommentsByTeam( Long teamId );

  public List<PublicRecognitionFormattedValueBean> getPublicRecognitionList( Long userId, String listType, int rowNumStart, int rowNumEnd, String listValue );

  /**
   * Return a list of public recognition formatted beans that are associated for the specified claim.
   *
   * @param claimId
   * @param userId
   * @return List<PublicRecognitionFormattedValueBean>
   */
  public List<PublicRecognitionFormattedValueBean> getPublicRecognitionClaimsByClaimId( Long claimId, Long userId );

  public List<PublicRecognitionFormattedValueBean> getDashboardRecognitionClaimsByType( Map<String, Object> queryParams );

  List<PublicRecognitionLike> getUserLikesByTeam( Long teamId );

  List<PublicRecognitionComment> getUserCommentsByTeamId( Long teamId );

}
