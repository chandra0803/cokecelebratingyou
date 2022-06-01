
package com.biperf.core.service.reports;

import java.util.List;
import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * Service interface for behavior reports
 */
public interface BehaviorReportsService extends SAO
{
  public static String BEAN_NAME = "behaviorReportsService";

  public Map<String, Object> getBehaviorsTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getBehaviorsBarchartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getBehaviorsPiechartResults( Map<String, Object> reportParameters );

  public List<String> getBehaviorsByPromo( String promotionId, String locale );

  public Map getBehaviorReportExtract( Map<String, Object> reportParameters );

}
