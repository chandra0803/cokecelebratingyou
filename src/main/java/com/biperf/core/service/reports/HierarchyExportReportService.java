/**
 * 
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * @author poddutur
 *
 */
public interface HierarchyExportReportService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "hierarchyExportReportService";

  @SuppressWarnings( "rawtypes" )
  public Map getHierarchyExportReportExtract( Map<String, Object> reportParameters );

}
