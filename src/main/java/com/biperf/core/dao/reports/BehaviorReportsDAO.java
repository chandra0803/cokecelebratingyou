
package com.biperf.core.dao.reports;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * Behavior reports DAO interface
 */
public interface BehaviorReportsDAO extends DAO
{
  public static final String BEAN_NAME = "behaviorReportsDAO";

  public Map<String, Object> getBehaviorsTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getBehaviorsPiechartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getBehaviorsBarchartResults( Map<String, Object> reportParameters );

  public List<String> getBehaviorsByPromo( String promotionId, String locale );

  public Map getBehaviorReportExtract( Map<String, Object> reportParameters );

}
