/**
 * 
 */

package com.biperf.core.service.promotion;

import java.util.Date;
import java.util.List;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.SAO;

/**
 * PromotionSweepstakeService.
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
 *
 */
public interface PromotionSweepstakeService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "promotionSweepstakeService";

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
   * Retrieves the most recently ended PromotionSweepstake
   * 
   * @return promotionSweepstake
   */
  public PromotionSweepstake getMostRecentlyEndedPromotionSweepstake( Long promotionId );

  /**
   * Retrieves all the PromotionSweepstakes sorted by date from the database.
   * 
   * @return List of PromotionSweepstakes
   */
  public List getAllPromotionSweepstakesListSortedByDate();

  /**
   * Removes pending winners (actually just the flip of a flag on the database)
   * 
   * @param winnerFormBeans List of PendingWinnerFormBeans representing winners to be removed
   * @param promotionId Long id of Sweepstake to be processed
   */
  public Promotion removeWinners( List winnerFormBeans, Long promotionId ) throws UniqueConstraintViolationException;

  /**
   * Removes pending winners (actually just the flip of a flag on the database) and replaces them
   * with new ones
   * 
   * @param winnerFormBeans List of PendingWinnerFormBeans representing winners to be replaced
   * @param promotionId Long id of Sweepstake to be processed
   * @param winnersType 
   * @return Promotion
   * @throws UniqueConstraintViolationException 
   * @throws ServiceErrorException 
   */
  public Promotion replaceWinners( List winnerFormBeans, Long promotionId, List winnersType, int giversDisplayed, int receiversDisplayed )
      throws UniqueConstraintViolationException, ServiceErrorException;

  /**
   * Marks sweepstake as processed and deposits points to each winner's account.
   * 
   * @param sweepstakeId id of Sweepstake to be processed
   */
  public Long processAward( Long sweepstakeId ) throws UniqueConstraintViolationException, ServiceErrorExceptionWithRollback;

  /**
   * Creates a list of winners for a specified promotion for a given timeframe. Only participants
   * who were/are qualified during the specified timeframe.
   * 
   * @param promotionId
   * @param startDate
   * @param endDate
   * @return List 
   * @throws UniqueConstraintViolationException 
   * @throws ServiceErrorException 
   */
  public List createWinnersList( Long promotionId, Date startDate, Date endDate ) throws UniqueConstraintViolationException, ServiceErrorException;

  /**
   * Creates a list of winners for a specified promotion for a given sweepstake. Only participants
   * who were/are qualified during the specified within the sweepstake's startDate and endDate.
   * 
   * @param promotionId
   * @param sweepstake
   * @return List
   * @throws UniqueConstraintViolationException 
   * @throws ServiceErrorException 
   */
  public List createWinnersList( Long promotionId, PromotionSweepstake sweepstake ) throws UniqueConstraintViolationException, ServiceErrorException;

  /* Bug # 34020 start */
  public int getPromotionSweepstakesNotProcessedCount( Long promotionId );

  public int getPromotionSweepstakesHistoryCount( Long promotionId );

  public List getPromotionSweepstakesByPromotionIdNotProcessed( Long promotionId );

  public List getAllPromotionSweepstakeWinnersByDrawingId( Long drawingId );

  /* Bug # 34020 end */
  public Promotion scheduleProcessAward( Long promotionId ) throws UniqueConstraintViolationException, ServiceErrorExceptionWithRollback;
}
