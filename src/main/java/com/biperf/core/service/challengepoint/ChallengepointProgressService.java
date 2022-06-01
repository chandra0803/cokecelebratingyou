
package com.biperf.core.service.challengepoint;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.domain.challengepoint.PendingChallengepointAwardSummary;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ChallengepointPaxValueBean;

/**
 * ChallengepointProgressService.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>reddy</td>
 * <td>Jul 17, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ChallengepointProgressService extends SAO
{

  public static final String BEAN_NAME = "challengepointProgressService";

  /**
   * Deletes the specified goalquest pax activity.
   *
   * @param challengepointProgress  the ChallengepointProgress to delete.
   */
  public void deleteChallengepointProgress( ChallengepointProgress challengepointProgress );

  /**
   * Save the challengepointProgress.
   * 
   * @param challengepointProgress
   * @param emailFlag 
   * @return ChallengepointProgress
   */
  // public ChallengepointProgress saveChallengepointProgress( ChallengepointProgress
  // challengepointProgress,boolean emailFlag );
  /**
   * Save the challengepointProgress with email notification send or flag.
   * 
   * @param challengepointProgress
   * @return ChallengepointProgress
   */
  public ChallengepointProgress saveChallengepointProgress( ChallengepointProgress challengepointProgress );

  /**
   * Get the ChallengepointProgress entry by id
   * 
   * @param id
   * @return ChallengepointProgress
   */
  public ChallengepointProgress getChallengepointProgressById( Long id );

  /**
   * Get a list of ChallengepointProgress by promotion id and user id.
   * 
   * @param promotionId
   * @param userId 
   * @param associationRequestCollection
   * @return list of ChallengepointProgress objects
   */
  public List<ChallengepointProgress> getChallengepointProgressByPromotionIdAndUserId( Long promotionId, Long userId, AssociationRequestCollection associationRequestCollection );

  public Map<String, Object> getChallengepointProgressByPromotionIdAndUserId( Long promotionId, Long userId ) throws ServiceErrorException;

  /**
   * get list of partipant award summary for a promotion id
   */
  public PendingChallengepointAwardSummary getAwardSummaryByLevels( Long promotionId ) throws ServiceErrorException;

  public ChallengepointPaxValueBean getChallengepointProgressSummary( Long promotionId, Long userId ) throws ServiceErrorException;

  /**
   * @param userId
   * @return List of all ChallengePointPromotionObjects with PaxValueBeans present
   *         for selected promotions
   * @throws ServiceErrorException 
   */
  public List<ChallengepointPaxValueBean> getAllLiveChallengePointPromotionsWithProgressByUserId( Long userId ) throws ServiceErrorException;

  public Date getProgressLastSubmissionDate( Long promotionId );

  /**
   *  isParticipantPayoutComplete()
   *  date : May 23 2011
   */
  public boolean isParticipantPayoutComplete( Long userId, Long promotionId );

}
