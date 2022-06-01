/**
 * 
 */

package com.biperf.core.dao.promotion;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.PromotionSweepstake;

/**
 * PromotionSweepstakeDAO.
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
 * <td>asondgeroth</td>
 * <td>Nov 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface PromotionSweepstakeDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "promotionSweepstakeDAO";

  /**
   * Get a PromotionSweepstake by Id.
   * 
   * @param id
   * @return PromotionSweepstake
   */
  public PromotionSweepstake getPromotionSweepstakeById( Long id );

  /**
   * Saves the PromotionSweepstake to the database.
   * 
   * @param promotionSweepstake
   * @return PromotionSweepstake
   */
  public PromotionSweepstake save( PromotionSweepstake promotionSweepstake );

  /**
   * Deletes the promotionSweepstake from the database.
   * 
   * @param promotionSweepstake
   */
  public void delete( PromotionSweepstake promotionSweepstake );

  /**
   * Retrieves all the promotionSweepstakes from the database.
   * 
   * @return List a of promotionSweepstakes
   */
  public List getAllPromotionSweepstakes();

  /**
   * Retrieves all the PromotionSweepstakes sorted by date from the database.
   * 
   * @return List of PromotionSweepstakes
   */
  public List getAllPromotionSweepstakesListSortedByDate();

  /**
   * Retrieves all the PromotionSweepstakes given promotionId sorted by date from the database.
   * 
   * @param promotionId
   * @return List of PromotionSweepstakes
   */
  public List getAllPromotionSweepstakesListByPromotionIdSortedByDate( Long promotionId );

  /* Bug # 34020 start */
  public int getPromotionSweepstakesNotProcessedCount( Long promotionId );

  public int getPromotionSweepstakesHistoryCount( Long promotionId );

  public List getPromotionSweepstakesByPromotionIdNotProcessed( Long promotionId );

  public List getAllPromotionSweepstakeWinnersByDrawingId( Long drawingId );
  /* Bug # 34020 End */
}
