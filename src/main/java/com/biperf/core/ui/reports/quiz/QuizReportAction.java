/**
 * 
 */

package com.biperf.core.ui.reports.quiz;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.reports.QuizReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.utils.StringUtil;

/**
 * @author poddutur
 *
 */
public abstract class QuizReportAction extends BaseReportsAction
{
  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( QuizReportAction.class );

  protected QuizReportsService getQuizReportsService()
  {
    return (QuizReportsService)getService( QuizReportsService.BEAN_NAME );
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.quizzes.extract";
  }

  protected void filterDIYQuizzes( Map<String, Object> reportParameters )
  {
    String promotionId = (String)reportParameters.get( "promotionId" );
    String diyQuizIdParam = null;
    String promotionIdParam = null;
    if ( promotionId != null )
    {
      String[] promotionIds = promotionId.split( "," );
      for ( String promotionItem : promotionIds )
      {
        if ( promotionItem != null && !promotionItem.isEmpty() && Long.valueOf( promotionItem ) < 0 )
        {
          if ( StringUtil.isEmpty( diyQuizIdParam ) )
          {
            diyQuizIdParam = "";
          }
          else
          {
            diyQuizIdParam = diyQuizIdParam + ",";
          }
          diyQuizIdParam = diyQuizIdParam + Long.valueOf( promotionItem ) * -1;
        }
        else
        {
          if ( StringUtil.isEmpty( promotionIdParam ) )
          {
            promotionIdParam = "";
          }
          else
          {
            promotionIdParam = promotionIdParam + ",";
          }
          promotionIdParam = promotionIdParam + promotionItem;
        }
      }
    }
    reportParameters.put( "promotionId", promotionIdParam );
    reportParameters.put( "diyQuizId", diyQuizIdParam );
  }

}
