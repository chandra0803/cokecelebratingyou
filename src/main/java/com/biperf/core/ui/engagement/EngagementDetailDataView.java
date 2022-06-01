
package com.biperf.core.ui.engagement;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.value.EngagementSiteVisitsLoginValueBean;

/**
 * 
 * EngagementDetailDataView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementDetailDataView
{

  private EngagementRecognitionSentView recSent;
  private EngagementRecognitionReceivedView recRecv;
  private EngagementUniqueRecognitionReceivedView paxRecTo;
  private EngagementUniqueRecognitionSentView paxRecBy;
  private EngagementSiteVisitsView visits;
  private EngagementRecognitionByPromotionView byPromo;
  private List<EngagementRecognitionByBehaviorView> byBehavior;
  private String recognitionsUrl;
  private int count;
  private String chart;
  private String detailUrl;
  private List<EngagementSiteVisitsLoginView> logins;
  private String chartUrl;
  private EngagementChartUrlParamsView chartUrlParams;

  public EngagementDetailDataView( int count, String detailUrl, String chartUrl, EngagementChartUrlParamsView chartUrlParams )
  {
    super();
    this.count = count;
    this.detailUrl = detailUrl;
    this.chartUrl = chartUrl;
    this.chartUrlParams = chartUrlParams;
  }

  public EngagementDetailDataView( EngagementRecognitionByPromotionView byPromo, List<EngagementRecognitionByBehaviorView> byBehavior, String recognitionsUrl )
  {
    super();
    this.byPromo = byPromo;
    this.byBehavior = byBehavior;
    this.recognitionsUrl = recognitionsUrl;
  }

  public EngagementDetailDataView( List<EngagementSiteVisitsLoginValueBean> engagementSiteVisitsLoginValueBeanList )
  {
    super();
    this.logins = populateSiteVisitsLoginView( engagementSiteVisitsLoginValueBeanList );
  }

  public EngagementDetailDataView( EngagementRecognitionSentView recSent,
                                   EngagementRecognitionReceivedView recRecv,
                                   EngagementUniqueRecognitionReceivedView paxRecTo,
                                   EngagementUniqueRecognitionSentView paxRecBy,
                                   EngagementSiteVisitsView visits )
  {
    super();
    this.recSent = recSent;
    this.recRecv = recRecv;
    this.paxRecTo = paxRecTo;
    this.paxRecBy = paxRecBy;
    this.visits = visits;
  }

  private List<EngagementSiteVisitsLoginView> populateSiteVisitsLoginView( List<EngagementSiteVisitsLoginValueBean> engagementSiteVisitsLoginValueBeanList )
  {
    logins = new ArrayList<EngagementSiteVisitsLoginView>();
    if ( engagementSiteVisitsLoginValueBeanList != null )
    {

      for ( EngagementSiteVisitsLoginValueBean engagementSiteVisitsLoginValueBean : engagementSiteVisitsLoginValueBeanList )
      {
        EngagementSiteVisitsLoginView engagementSiteVisitsLoginView = new EngagementSiteVisitsLoginView( engagementSiteVisitsLoginValueBean.getDate(),
                                                                                                         engagementSiteVisitsLoginValueBean.getTime(),
                                                                                                         engagementSiteVisitsLoginValueBean.getTimeZoneId(),
                                                                                                         engagementSiteVisitsLoginValueBean.getLocaleTime() );
        logins.add( engagementSiteVisitsLoginView );
      }
    }
    return logins;
  }

  public String getChartUrl()
  {
    return chartUrl;
  }

  public void setChartUrl( String chartUrl )
  {
    this.chartUrl = chartUrl;
  }

  public EngagementChartUrlParamsView getChartUrlParams()
  {
    return chartUrlParams;
  }

  public void setChartUrlParams( EngagementChartUrlParamsView chartUrlParams )
  {
    this.chartUrlParams = chartUrlParams;
  }

  public EngagementRecognitionSentView getRecSent()
  {
    return recSent;
  }

  public void setRecSent( EngagementRecognitionSentView recSent )
  {
    this.recSent = recSent;
  }

  public EngagementRecognitionReceivedView getRecRecv()
  {
    return recRecv;
  }

  public void setRecRecv( EngagementRecognitionReceivedView recRecv )
  {
    this.recRecv = recRecv;
  }

  public EngagementUniqueRecognitionReceivedView getPaxRecTo()
  {
    return paxRecTo;
  }

  public void setPaxRecTo( EngagementUniqueRecognitionReceivedView paxRecTo )
  {
    this.paxRecTo = paxRecTo;
  }

  public EngagementUniqueRecognitionSentView getPaxRecBy()
  {
    return paxRecBy;
  }

  public void setPaxRecBy( EngagementUniqueRecognitionSentView paxRecBy )
  {
    this.paxRecBy = paxRecBy;
  }

  public EngagementSiteVisitsView getVisits()
  {
    return visits;
  }

  public void setVisits( EngagementSiteVisitsView visits )
  {
    this.visits = visits;
  }

  public EngagementRecognitionByPromotionView getByPromo()
  {
    return byPromo;
  }

  public void setByPromo( EngagementRecognitionByPromotionView byPromo )
  {
    this.byPromo = byPromo;
  }

  public List<EngagementRecognitionByBehaviorView> getByBehavior()
  {
    return byBehavior;
  }

  public void setByBehavior( List<EngagementRecognitionByBehaviorView> byBehavior )
  {
    this.byBehavior = byBehavior;
  }

  public String getRecognitionsUrl()
  {
    return recognitionsUrl;
  }

  public void setRecognitionsUrl( String recognitionsUrl )
  {
    this.recognitionsUrl = recognitionsUrl;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count )
  {
    this.count = count;
  }

  public String getChart()
  {
    return chart;
  }

  public void setChart( String chart )
  {
    this.chart = chart;
  }

  public String getDetailUrl()
  {
    return detailUrl;
  }

  public void setDetailUrl( String detailUrl )
  {
    this.detailUrl = detailUrl;
  }

  public List<EngagementSiteVisitsLoginView> getLogins()
  {
    return logins;
  }

  public void setLogins( List<EngagementSiteVisitsLoginView> logins )
  {
    this.logins = logins;
  }

}
