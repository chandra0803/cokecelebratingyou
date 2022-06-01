/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/process/ProcessDAO.java,v $
 */

package com.biperf.core.dao.process;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.process.Process;
import com.biperf.core.service.AssociationRequestCollection;

/**
 * ProcessDAO.
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
 * <td>leep</td>
 * <td>November 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ProcessDAO extends DAO
{

  public static final String BEAN_NAME = "processDAO";

  /**
   * Get the process from the database by the id.
   * 
   * @param id
   * @return Process
   */
  public Process getProcessById( Long id );

  /**
   * Get the process from the database by the id.
   * 
   * @param id
   * @return Process
   */
  public Process getProcessByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the process from the database by the id.
   * 
   * @param status
   * @return List
   */
  public List getProcessListByStatus( String status );

  /**
   * Get processes by status.
   * 
   * @param status
   * @param associationRequestCollection
   * @return List
   */
  public List getProcessListByStatus( String status, AssociationRequestCollection associationRequestCollection, String processType );

  /**
   * Saves the process to the database.
   * 
   * @param process
   * @return Process
   */
  public Process save( Process process );

  /**
   * Get the process with the given processName, or null if none exists
   * 
   * @param processName
   */
  public Process getProcessByName( String processName );

  public Process getProcessByBeanName( String processBeanName );

  /**
   * Given a specially defined namedQueryName (for example see Promotion.hbm.xml -
   * com.biperf.core.domain.promotion.processParameterValueChoices.batchModePromotions). This method
   * is used to provide a list of process parameters value choices.
   * 
   * @param namedQueryName
   * @return List of {@link com.biperf.core.value.FormattedValueBean} objects.
   */
  public List getProcessParameterValueChoices( String namedQueryName );

  /**
   * Done as a seperate method (rather than just updating process) so we can (mostly) avoid
   * optimistic locking, since we really want "last one wins"
   */
  public void updateLastExecutedDate( Date lastExecutedTime, Long processId );

  public List getAllCurrentlyExecutingProcesses();

  public void clearSecondLevelCache();

  // Client customizations for wip #23129 starts
  public List getClientGiftCodeSweepPromotions();

  public List getClientGiftCodeSweepBean( Long promoId );
  // Client customizations for wip #23129 ends
}
