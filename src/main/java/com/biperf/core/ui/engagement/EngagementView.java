
package com.biperf.core.ui.engagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.value.EngagementDashboardValueBean;
import com.biperf.core.value.EngagementSummaryValueBean;

/**
 * 
 * EngagementView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementView
{
  protected static final String TEAM = "team";
  protected static final String USER = "user";

  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private EngagementKPMView engagement;

  public EngagementView()
  {
  }

  public EngagementView( String mode, Long userId, String timeframeType, int timeframeMonthId, int timeframeYear, EngagementSummaryValueBean engagementSummaryValueBean, Date startDate, Date endDate )
  {
    this.engagement = new EngagementKPMView( mode, userId, timeframeType, timeframeMonthId, timeframeYear, engagementSummaryValueBean, startDate, endDate );
  }

  public EngagementView( String mode,
                         Long userId,
                         String timeframeType,
                         int timeframeMonthId,
                         int timeframeYear,
                         EngagementDashboardValueBean engagementDashboardValueBean,
                         Date startDate,
                         Date endDate,
                         String selectedNodes,
                         boolean isDrillDown,
                         String nodeName )
  {
    this.engagement = new EngagementKPMView( mode, userId, timeframeType, timeframeMonthId, timeframeYear, engagementDashboardValueBean, startDate, endDate, selectedNodes, isDrillDown, nodeName );
  }

  public EngagementView( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public EngagementKPMView getEngagement()
  {
    return engagement;
  }

  public void setEngagement( EngagementKPMView engagement )
  {
    this.engagement = engagement;
  }

}
