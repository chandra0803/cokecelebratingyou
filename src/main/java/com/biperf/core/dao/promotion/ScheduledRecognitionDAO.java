
package com.biperf.core.dao.promotion;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.ScheduledRecognition;

public interface ScheduledRecognitionDAO extends DAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "scheduledRecognitionDAO";

  public ScheduledRecognition saveScheduledRecognition( ScheduledRecognition scheduledRecognition );

  public ScheduledRecognition getScheduledRecognitionById( Long id );

  public ScheduledRecognition getScheduledRecognitionByTriggerName( String triggerName );

  public List<Long> getScheduledRecognitionIdsForRetryProcess();

  public List<ScheduledRecognition> getScheduledRecognitionsByDeliveryDate( Date deliveryDate );
}
