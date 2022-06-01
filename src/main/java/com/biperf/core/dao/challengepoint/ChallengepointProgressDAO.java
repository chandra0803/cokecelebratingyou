
package com.biperf.core.dao.challengepoint;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.service.AssociationRequestCollection;

/**
 * ChallengepointProgressDAO.
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
public interface ChallengepointProgressDAO extends DAO
{
  public static final String BEAN_NAME = "challengepointProgressDAO";

  /**
   * Deletes the specified goalquest pax activity.
   *
   * @param challengepointProgress  the ChallengepointProgress to delete.
   */
  public void deleteChallengepointProgress( ChallengepointProgress challengepointProgress );

  /**
   * Save the goalQuestParticipantActivity.
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

  /**
  * Get a list of ChallengepointProgress by promotion id and user id.
  * 
  * @param promotionId
  * @param userId 
  * @param associationRequestCollection
  * @return list of ChallengepointProgress objects
  */
  public List<ChallengepointProgress> getChallengepointProgressByPromotionId( Long promotionId, AssociationRequestCollection associationRequestCollection );
}
