/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/process/ProcessService.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.process;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.domain.promotion.ScheduledRecognition;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * ProcessService.
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
 * <td>sharma</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ProcessService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "processService";

  public static final String RUN_BY_USER_ID_PARAM_NAME = "runByUserId";

  public static final String PROCESS_BEAN_NAME = "processBeanName";

  public static final String PROCESS_BEAN_JOB_NAME_PREFIX = "ProcessBeanJob-";

  public static final String PROCESS_BEAN_JOB_GROUP_PREFIX = "ProcessBeanJobGroup-";

  public static final String PROCESS_BEAN_TRIGGER_NAME_PREFIX = "ProcessBeanTriggerName-";

  public static final String PROCESS_BEAN_TRIGGER_GROUP_PREFIX = "ProcessBeanTriggerGroup-";

  public Process getProcessById( Long id );

  public Process getProcessById( Long id, AssociationRequestCollection associationRequestCollection );

  public Process getProcessByName( String name );

  public Process getProcessByBeanName( String name );

  public List getProcessListByStatus( String status );

  public List getProcessListByStatus( String status, AssociationRequestCollection associationRequestCollection, String processType );

  public Process save( Process process );

  public Process createOrLoadSystemProcess( String processName, String processBeanName );

  public void launchProcess( Process process, Map processParameterMap, Long runByUserId );

  public void launchProcess( Process process, Map processParameterMap, Long runByUserId, Long waitMillis );

  public void scheduleProcess( Process process, ProcessSchedule processSchedule, Map processParameterMap, Long runByUserId );

  public void removeProcessSchedule( Long processId, String scheduleName );

  public List getSchedules( Long processId );

  public void interruptJob( Long processId );

  public List getProcessJobBeanNames();

  public void testListTriggers();

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
   * Done as a seperate method so we can avoid optimistic locking, since we really want "last one
   * wins"
   * 
   * @param date
   * @param processId
   */
  public void updateLastExecutedTime( Date date, Long processId );

  /**
   * Get the "time of day" millisecond value for the first schedule found for the process
   * associated with the given processBeanName. If no schedule exists or the first schedule
   * has no "time of day", null will be returned.
   * 
   * @param processBeanName
   * @return Long   * 
   */
  public Long getTimeOfDayMillisOfFirstSchedule( String processBeanName );

  /**
   * Get the "time of day" as Date value for the first schedule date found for the process
   * associated with the given processBeanName. If no schedule exists or the first schedule
   * has no "time of day", null will be returned.
   * 
   * @param processBeanName
   * @return Long
   */
  public Date getTimeOfDayOfFirstScheduleDate( String processBeanName );

  public List getCurrentlyExecutingProcessess();

  public void interruptProcess( Long processId );

  public void interruptAllProcesses();

  public ScheduledRecognition populateScheduleRecognitionTriggerName( ScheduledRecognition scheduledRecognition, Process process );

  public PostProcessJobs populatePostProcessTriggerName( PostProcessJobs postProcessJobs, Process process );

  public ScheduledRecognition scheduleRecognitionProcess( ScheduledRecognition scheduledRecognition, Process process, ProcessSchedule processSchedule, Map processParameterMap, Long runByUserId );

  public PostProcessJobs schedulePostProcess( PostProcessJobs postProcessJobs, Process process, ProcessSchedule processSchedule, Map processParameterMap, Long runByUserId );

  public void clearHibernateSecondLevelCache();
  // Client customizations for wip #23129 starts
  public List getClientGiftCodeSweepPromotions();

  public List getClientGiftCodeSweepBean( Long promotionId );
  // Client customizations for wip #23129 ends  
}
