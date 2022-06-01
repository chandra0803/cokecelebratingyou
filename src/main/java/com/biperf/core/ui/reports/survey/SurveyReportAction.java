
package com.biperf.core.ui.reports.survey;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.reports.SurveyReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;

public abstract class SurveyReportAction extends BaseReportsAction
{
  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( SurveyReportAction.class );

  protected SurveyReportsService getSurveyReportsService()
  {
    return (SurveyReportsService)getService( SurveyReportsService.BEAN_NAME );
  }

  @Override
  protected String[] getColumnHeaders( Map<String, Object> reportParameters )
  {
    return null;
  }

}
