
package com.biperf.core.ui.reports.badge;

import com.biperf.core.service.reports.BadgeReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;

/**
 * 
 * BadgeActivityReportAction.
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
 * <td>kandhi</td>
 * <td>Nov 29, 2012</td>
 * <td>1.0</td>
 * <td>Initial version</td>
 * </tr>
 * </table>
 * 
 */
public abstract class BadgeActivityReportAction extends BaseReportsAction
{

  protected static final String[] EXTRACT_ALL_COLUMN_NAMES = { "TRANSACTION_DATE",
                                                               "LOGIN_ID",
                                                               "FIRST_NAME",
                                                               "MIDDLE_NAME",
                                                               "LAST_NAME",
                                                               "COUNTRY",
                                                               "PRIMARY_ORG_UNIT",
                                                               "DEPARTMENT",
                                                               "JOB_TITLE",
                                                               "PROMOTION_NAME",
                                                               "BADGE_NAME",
                                                               "PAX_CHAR1",
                                                               "PAX_CHAR2",
                                                               "PAX_CHAR3",
                                                               "PAX_CHAR4",
                                                               "PAX_CHAR5",
                                                               "PAX_CHAR6",
                                                               "PAX_CHAR7",
                                                               "PAX_CHAR8",
                                                               "PAX_CHAR9",
                                                               "PAX_CHAR10",
                                                               "PAX_CHAR11",
                                                               "PAX_CHAR12",
                                                               "PAX_CHAR13",
                                                               "PAX_CHAR14",
                                                               "PAX_CHAR15",
 															  "PAX_CHAR16",
 															  "PAX_CHAR17",
 															  "PAX_CHAR18",
 															  "PAX_CHAR19",
 															  "PAX_CHAR20",
 															  "PAX_CHAR21",
 															  "PAX_CHAR22",
 															  "PAX_CHAR23",
 															  "PAX_CHAR24",
 															  "PAX_CHAR25",
 															  "PAX_CHAR26",
 															  "PAX_CHAR27",
 															  "PAX_CHAR28",
 															  "PAX_CHAR29",
 															  "PAX_CHAR30",
 															  "PAX_CHAR31",
 															  "PAX_CHAR32",
 															  "PAX_CHAR33",
 															  "PAX_CHAR34",
 															  "PAX_CHAR35"};


  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.badgeactivity.extract";
  }

  protected BadgeReportsService getBadgeReportsService()
  {
    return (BadgeReportsService)getService( BadgeReportsService.BEAN_NAME );
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

}
