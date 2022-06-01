
package com.biperf.core.dao.awardgenerator;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.awardgenerator.AwardGenBatch;
import com.biperf.core.domain.awardgenerator.AwardGenParticipant;
import com.biperf.core.value.AwardGeneratorManagerPaxBean;

/**
 * AwardGenBatchDAO
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
 * <td>Jul 22, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface AwardGenBatchDAO extends DAO
{
  /**
   * Save the awardGenBatch to the database.
   * 
   * @param awardGenBatch
   * @return AwardGenBatch
   */
  public AwardGenBatch save( AwardGenBatch awardGenBatch );

  /**
   * Gets the awardGenBatch by the id.
   * 
   * @param id
   * @return AwardGenBatch
   */
  public AwardGenBatch getAwardGenBatchById( Long id );

  public List<AwardGenBatch> getAllBatchesByAwardGenId( Long awardGenId );

  public List getAllAwardGenBatchs();

  public boolean isBatchExist( Long awardGenId, String batchStartDate, String batchEndDate );

  public List<AwardGeneratorManagerPaxBean> getPaxListByMgrAndBatchId( Long userId, Long batchId );

  public void dismissAlertForAwardGenManager( AwardGenParticipant awardGenParticipant );

  public AwardGenParticipant getAwardGenParticipantById( Long awardGenPaxId );

  public List<Long> getAllManagersByBatchId( Long batchId );

  public Map<String, Object> generateAndSaveBatch( Map<String, Object> awardGenParams );

  public Map<String, Object> generateValuesForEmail( Long batchId );

}
