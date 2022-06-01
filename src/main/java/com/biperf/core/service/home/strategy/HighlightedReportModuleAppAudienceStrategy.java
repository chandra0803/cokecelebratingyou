
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.report.ReportDashboardItem;

public class HighlightedReportModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( !participant.isParticipant() )
    {
      return false;
    }

    ReportDashboardItem reportDashboardItem = getDashboardReportsService().getUserDashboardItem( participant.getId(), 0 );

    if ( reportDashboardItem != null )
    {
      return true;
    }

    return false;
  }
}
