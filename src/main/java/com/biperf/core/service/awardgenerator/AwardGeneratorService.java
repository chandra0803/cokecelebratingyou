
package com.biperf.core.service.awardgenerator;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.awardgenerator.AwardGenAward;
import com.biperf.core.domain.awardgenerator.AwardGenBatch;
import com.biperf.core.domain.awardgenerator.AwardGenerator;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.AwardGenPlateauFormBean;
import com.biperf.core.value.AwardGeneratorManagerPaxBean;
import com.biperf.core.value.AwardGeneratorManagerReminderBean;
import com.biperf.core.value.FormattedValueBean;

/**
 * AwardGeneratorService
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
 * <td>Jul 08, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public interface AwardGeneratorService extends SAO
{

  /**
   * Static final for the BEAN_NAME for use in the applicationContext.xml
   */
  public static final String BEAN_NAME = "awardGeneratorService";

  /**
   * Manages deleting an awardGenerator from the database.
   * 
   * @param awardGenerator
   */
  public void deleteAwardGenerator( AwardGenerator awardGenerator ) throws ServiceErrorException;

  /**
   * Manages deleting an awardGenerator from the database.
   * 
   * @param awardGenerator
   */
  public void deleteAwardGenerator( Long awardGeneratorId ) throws ServiceErrorException;

  /**
   * Deletes list of AwardGenerators
   * @param awardGeneratorIdList
   * @throws ServiceErrorException
   */
  public void deleteAwardGenerators( List awardGeneratorIdList ) throws ServiceErrorException;

  /**
   * Save the awardGenerator to the database.
   * 
   * @param awardGenerator
   * @return AwardGenerator
   */
  public AwardGenerator saveAwardGenerator( AwardGenerator awardGenerator ) throws ServiceErrorException;

  /**
   * Gets the awardGenerator by the id.
   * 
   * @param id
   * @return AwardGenerator
   */
  public AwardGenerator getAwardGeneratorById( Long id ) throws ServiceErrorException;

  /**
   * Gets a list of all awardGenerators in the database. If
   * startDate is null, Current Date is taken as startDate.
   * if EndDate is null, then there is no endDate.
   * 
   * @return List
   */
  public List getAllActiveAwardGenerators() throws ServiceErrorException;

  public AwardGenerator getAwardGeneratorByName( String name );

  public List<String> getAllYearsByAwardGenId( Long awardGenId );

  public List<String> getAllDaysByAwardGenId( Long awardGenId );

  public List<AwardGenAward> getAllAwardsByAwardGenIdAndYears( Long awardGenId, String years );

  public List<AwardGenAward> getAllAwardsByAwardGenIdAndDays( Long awardGenId, String days );

  public AwardGenBatch saveAwardGenBatch( AwardGenBatch awardGenBatch ) throws ServiceErrorException;

  public List<AwardGenBatch> getAllBatchesByAwardGenId( Long awardGenId );

  public List<FormattedValueBean> getAwardGenBatches( Long awardGenId );

  public List<FormattedValueBean> getExaminerList();

  public AwardGenBatch getAwardGenBatchById( Long awardGenBatchId );

  public Map<String, Object> generateAndSaveBatch( Map<String, Object> awardGenParams );

  public boolean isBatchExist( Long awardGenId, String batchStartDate, String batchEndDate );

  public void generateAndSendEmailExtract( Long batchId ) throws ServiceErrorException;

  public List<AwardGeneratorManagerReminderBean> getAwardGeneratorManagerRemindersList( Long participantId );

  public List<AwardGeneratorManagerPaxBean> getPaxListByMgrAndBatchId( Long userId, Long batchId );

  public void dismissAlertForAwardGenManager( Long userId, Long batchId );

  public List<Long> getAllManagersByBatchId( Long batchId );

  public void deleteAwardGenAwards( List<AwardGenAward> awardGenAwards ) throws ServiceErrorException;

  public void deleteAwardGenPlateauAwards( List<AwardGenPlateauFormBean> plateauValuesFormBeans, boolean awardActive ) throws ServiceErrorException;

}
