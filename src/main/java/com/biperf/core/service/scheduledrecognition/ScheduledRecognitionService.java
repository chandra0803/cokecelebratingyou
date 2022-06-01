/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/scheduledrecognition/ScheduledRecognitionService.java,v $
 */

package com.biperf.core.service.scheduledrecognition;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.biperf.core.domain.promotion.ScheduledRecognition;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

public interface ScheduledRecognitionService extends SAO
{
  /**
   * BEAN_NAME for referencing in tests and spring config files.
   */
  public final String BEAN_NAME = "scheduledRecognitionService";

  public ScheduledRecognition createScheduledRecognition( ScheduledRecognition scheduledRecognition, LinkedHashMap inputParameters, Date deliveryDate );

  public void rescheduleRecognition( long scheduledRecognitionId, Date newTimeToRun ) throws ServiceErrorException;

  public void deleteScheduledRecognition( long scheduledRecognitionId ) throws ServiceErrorException;

  public void updateScheduleRecognitionWithClaimId( String triggerName, Long claimid ) throws ServiceErrorException;

  public ScheduledRecognition getScheduledRecognitionById( Long id );

  public void scheduleDelaySendRecognitionProcess( ScheduledRecognition scheduledRecognition, Date deliveryDate, Long timeGap );

  public List<ScheduledRecognition> getScheduledRecognitionsByDeliveryDate( Date deliveryDate );

  public List<Long> getScheduledRecognitionIdsForRetryProcess();
}
