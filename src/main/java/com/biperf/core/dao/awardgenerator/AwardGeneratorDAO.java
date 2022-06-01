
package com.biperf.core.dao.awardgenerator;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.awardgenerator.AwardGenAward;
import com.biperf.core.domain.awardgenerator.AwardGenerator;
import com.biperf.core.value.AwardGeneratorManagerReminderBean;

/**
 * AwardGeneratorDAO
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
 * <td>chowdhur</td>
 * <td>Jul 09, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface AwardGeneratorDAO extends DAO
{
  /**
   * Manages deleting an awardGenerator from the database.
   * 
   * @param awardGenerator
   */
  public void deleteAwardGenerator( AwardGenerator awardGenerator );

  /**
   * Save the awardGenerator to the database.
   * 
   * @param awardGenerator
   * @return AwardGenerator
   */
  public AwardGenerator saveAwardGenerator( AwardGenerator awardGenerator );

  /**
   * Gets the awardGenerator by the id.
   * 
   * @param id
   * @return AwardGenerator
   */
  public AwardGenerator getAwardGeneratorById( Long id );

  /**
   * Gets a list of all awardGenerators in the database. If
   * startDate is null, Current Date is taken as startDate.
   * if EndDate is null, then there is no endDate.
   * 
   * @return List
   */
  public List getAllActiveAwardGenerators();

  /**
   * 
   * @param name
   * @return
   */
  public AwardGenerator getAwardGeneratorByName( String name );

  /**
   * 
   * @param awardGenId
   * @return
   */
  public List<String> getAllYearsByAwardGenId( Long awardGenId );

  /**
   * 
   * @param awardGenId
   * @return
   */
  public List<String> getAllDaysByAwardGenId( Long awardGenId );

  /**
   * 
   * @param awardGenId
   * @param years
   * @return
   */
  public List<AwardGenAward> getAllAwardsByAwardGenIdAndYears( Long awardGenId, String years );

  public List<AwardGenAward> getAllAwardsByAwardGenIdAndDays( Long awardGenId, String days );

  /**
   * 
   * @param participantId
   * @param eligiblePromotions
   * @return
   */
  public List<AwardGeneratorManagerReminderBean> getAwardGeneratorManagerRemindersList( Long participantId );

  public AwardGenAward getAwardGenAwardById( Long id );

  public void deleteAwardGenAward( AwardGenAward awardGenAward );
}
