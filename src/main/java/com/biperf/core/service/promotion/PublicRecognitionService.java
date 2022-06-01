/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PublicRecognitionService.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.promotion.PublicRecognitionLike;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.publicrecognition.PublicRecognitionSet;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.TranslatedContent;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.translation.UnexpectedTranslationException;
import com.biperf.core.service.translation.UnsupportedTranslationException;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;

/**
 * 
 * @author veeramas
 * @since Jul 24, 2012
 * @version 1.0
 */
public interface PublicRecognitionService extends SAO
{
  /**
   * The key to an object that implements this interface in the Spring application context.
   */
  static final String BEAN_NAME = "publicRecognitionService";

  /**
   * Return a list of public recognition formatted beans that are associated for the specified claim.
   *
   * @param claimId
   * @param userId
   * @return List<PublicRecognitionFormattedValueBean>
   */
  public List<PublicRecognitionFormattedValueBean> getPublicRecognitionClaimsByClaimId( Long claimId, Long userId );

  /**
   * 
   * @param publicRecogComments
   * @return List<PublicRecognitionComment>
   */
  public List<PublicRecognitionComment> savePublicRecognitionComments( List<PublicRecognitionComment> publicRecogComments );

  /**
   * @param participantId
   * @param claimId 
   */
  public void likePublicRecognition( Long participantId, Long claimId );

  /**
   * @param participantId
   * @param claimId 
   */
  public void unlikePublicRecognition( Long participantId, Long claimId );

  /**
   * 
   * @param claimId
   * @return long
   */
  public long recognitionLikeCountByClaimId( Long claimId );

  /**
   * 
   * @param claimId
   * @return List<PublicRecognitionLike>
   */
  public List<PublicRecognitionLike> getLikedPaxListByClaimIdWithAssociations( Long claimId, AssociationRequestCollection associationRequests, int pageNumber );

  /**
   * 
   * @param claimId
   * @return count of likes
   */
  public int getLikedPaxCount( Long claimId );

  /**
   * Get Followers for logged-in participant
   * @param participantId
   * @return List<Participant>
   */
  public List<Participant> getParticipantsIAmFollowing( Long participantId );

  /**
   * Update the given claim to be hidden from public recognition
   * @param claimId
   */
  public void hidePublicRecognition( Long claimId );

  /**
   * To calculate the payout for public recognition budget
   * @param approvable
   */
  public void calculatePublicRecognitionBudget( Approvable approvable ) throws ServiceErrorException;

  /**
   * @param claim
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @param deductBudget
   * @return
   * @throws ServiceErrorException
   */
  public List<PublicRecognitionComment> savePublicRecognitionClaim( Claim claim,
                                                                    Long claimFormStepId,
                                                                    User approverUser,
                                                                    boolean forceAutoApprove,
                                                                    boolean deductBudget,
                                                                    Participant participant,
                                                                    String comment,
                                                                    Long points,
                                                                    RecognitionClaim recClaim )
      throws ServiceErrorException;

  /**
   * 
   * @param submission
   * @param points
   * @param participant
   * @param forceAutoApprove
   * @param deductBudget
   * @return
   * @throws ServiceErrorException
   */
  public RecognitionClaimSubmissionResponse savePublicRecognitionClaim( RecognitionClaimSubmission submission,
                                                                        Long points,
                                                                        Participant participant,
                                                                        boolean forceAutoApprove,
                                                                        boolean deductBudget,
                                                                        Long teamId )
      throws ServiceErrorException;

  public TranslatedContent getTranslatedPublicRecognitionCommentFor( Long publicRecognitionCommentId, Long userId ) throws UnsupportedTranslationException, UnexpectedTranslationException;

  public void deletePublicRecognitionComment( Long commentId, Long participantId ) throws ServiceErrorException;

  public List<PublicRecognitionLike> getUserLikesByTeam( Long teamId );

  public List<PublicRecognitionComment> getUserCommentsByTeam( Long teamId );

  public List<PublicRecognitionSet> getPublicRecognitionClaimsByTabType( Long userId, String tabType, int pageSize, int pageNumber, String listValue ) throws ServiceErrorException;

  public List<PublicRecognitionSet> getDashboardRecognitionClaimsByType( Map<String, Object> queryParams, int pageSize ) throws ServiceErrorException;

  public RecognitionClaim buildRecognitionClaim( Long claimId, Long points, String comment, RecognitionClaimSource recognitionClaimSource ) throws ServiceErrorException;
  
  public List<Long> submitRecognitionWithPubRecAddPoints( Participant sender, List<Participant> recipients, RecognitionPromotion promotion, String comments, String behavior, int points ) throws ServiceErrorException;
  
}
