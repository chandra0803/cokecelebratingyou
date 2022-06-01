/**
 * 
 */

package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * @author poddutur
 *
 */
public interface HierarchyExportReportDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "hierarchyExportReportDAO";

  public Map getHierarchyExportReportExtractResults( Map<String, Object> reportParameters );

}
