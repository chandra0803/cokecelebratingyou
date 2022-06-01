/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/enrollment/EnrollmentReportAction.java,v $
 *
 */

package com.biperf.core.ui.reports.enrollment;

import java.util.Map;

import com.biperf.core.service.reports.EnrollmentReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;

/**
 * EnrollmentReportAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 14, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public abstract class EnrollmentReportAction extends BaseReportsAction
{
 	  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { 
		  												"FIRST_NAME",
	                                                    "MIDDLE_INIT",
	                                                    "LAST_NAME",
	                                                    "USER_NAME",
	                                                    "LANGUAGE_PREF",
	                                                    "PRIMARY_EMAIL_TYPE",
	                                                    "PRIMARY_EMAIL",
	                                                    "PRIMARY_PHONE_TYPE",
	                                                    "PRIMARY_PHONE",
	                                                    "COUNTRY",
	                                                    "PARTICIPANT_STATUS",
	                                                    "NODE_NAME",
	                                                    "ORG_ROLE",
	                                                    "JOB_POSITION",
	                                                    "DEPARTMENT",
	                                                    "ENROLLMENT_DATE",
	                                                    "ENROLLMENT_SOURCE",
	                                                    "BIRTH_DATE",
	                                                    "TITLE",
	                                                    "SUFFIX",
	                                                    "GENDER",
	                                                    "PRIMARY_ADDRESS_TYPE",
	                                                    "ADDRESS_1",
	                                                    "ADDRESS_2",
	                                                    "ADDRESS_3",
	                                                    "CITY",
	                                                    "STATE",
	                                                    "POSTAL_CODE",
	                                                    "EMPLOYER",
	                                                    "PAX_CHAR_1",
	                                                    "PAX_CHAR_2",
	                                                    "PAX_CHAR_3",
	                                                    "PAX_CHAR_4",
	                                                    "PAX_CHAR_5",
	                                                    "PAX_CHAR_6",
	                                                    "PAX_CHAR_7",
	                                                    "PAX_CHAR_8",
	                                                    "PAX_CHAR_9",
	                                                    "PAX_CHAR_10",
	                                                    "PAX_CHAR_11",
	                                                    "PAX_CHAR_12",
	                                                    "PAX_CHAR_13",
	                                                    "PAX_CHAR_14",
	                                                    "PAX_CHAR_15",
	                                                    "PAX_CHAR_16",
	                                                    "PAX_CHAR_17",
	                                                    "PAX_CHAR_18",
	                                                    "PAX_CHAR_19",
	                                                    "PAX_CHAR_20",
	                                                    "PAX_CHAR_21",
	                                                    "PAX_CHAR_22",
	                                                    "PAX_CHAR_23",
	                                                    "PAX_CHAR_24",
	                                                    "PAX_CHAR_25",
	                                                    "PAX_CHAR_26",
	                                                    "PAX_CHAR_27",
	                                                    "PAX_CHAR_28",
	                                                    "PAX_CHAR_29",
	                                                    "PAX_CHAR_30",
	                                                    "PAX_CHAR_31",
	                                                    "PAX_CHAR_32",
	                                                    "PAX_CHAR_33",
	                                                    "PAX_CHAR_34",
	                                                    "PAX_CHAR_35"};

  protected EnrollmentReportsService getEnrollmentReportsService()
  {
    return (EnrollmentReportsService)getService( EnrollmentReportsService.BEAN_NAME );
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.enrollment.extract";
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getEnrollmentReportsService().getEnrollmentExtractResults( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.ENROLLMENT_REPORT;
  }
}
