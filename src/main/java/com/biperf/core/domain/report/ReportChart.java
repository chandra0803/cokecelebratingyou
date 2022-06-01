
package com.biperf.core.domain.report;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ReportChartType;

public class ReportChart extends BaseDomain
{
  private String caption;
  private String subCaption;
  private String chartDataUrl;

  private Boolean showLabels;
  private Boolean enableSmartLabels;
  private Boolean showLegend;
  private Boolean showPercentValues;
  private String xAxisLabel;
  private String yAxisLabel;
  private int sequenceNum;
  private Report report;
  private ReportChartType chartType;
  private int displayLimit;
  private boolean includedInPlateau;

  public ReportChart()
  {
    super();
  }

  public String getCaption()
  {
    return caption;
  }

  public void setCaption( String caption )
  {
    this.caption = caption;
  }

  public String getSubCaption()
  {
    return subCaption;
  }

  public void setSubCaption( String subCaption )
  {
    this.subCaption = subCaption;
  }

  public Boolean getEnableSmartLabels()
  {
    return enableSmartLabels;
  }

  public void setEnableSmartLabels( Boolean enableSmartLabels )
  {
    this.enableSmartLabels = enableSmartLabels;
  }

  public String getChartDataUrl()
  {
    return chartDataUrl;
  }

  public void setChartDataUrl( String chartDataUrl )
  {
    this.chartDataUrl = chartDataUrl;
  }

  public Boolean getShowLabels()
  {
    return showLabels;
  }

  public void setShowLabels( Boolean showLabels )
  {
    this.showLabels = showLabels;
  }

  public Boolean getShowLegend()
  {
    return showLegend;
  }

  public void setShowLegend( Boolean showLegend )
  {
    this.showLegend = showLegend;
  }

  public void setShowPercentValues( Boolean showPercentValues )
  {
    this.showPercentValues = showPercentValues;
  }

  public Boolean getShowPercentValues()
  {
    return showPercentValues;
  }

  public String getxAxisLabel()
  {
    return xAxisLabel;
  }

  public void setxAxisLabel( String xAxisLabel )
  {
    this.xAxisLabel = xAxisLabel;
  }

  public String getyAxisLabel()
  {
    return yAxisLabel;
  }

  public void setyAxisLabel( String yAxisLabel )
  {
    this.yAxisLabel = yAxisLabel;
  }

  public Report getReport()
  {
    return report;
  }

  public void setReport( Report report )
  {
    this.report = report;
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public ReportChartType getChartType()
  {
    return chartType;
  }

  public void setChartType( ReportChartType chartType )
  {
    this.chartType = chartType;
  }

  public int getDisplayLimit()
  {
    return displayLimit;
  }

  public void setDisplayLimit( int displayLimit )
  {
    this.displayLimit = displayLimit;
  }

  public boolean isIncludedInPlateau()
  {
    return includedInPlateau;
  }

  public void setIncludedInPlateau( boolean includedInPlateau )
  {
    this.includedInPlateau = includedInPlateau;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( caption == null ? 0 : caption.hashCode() );
    result = prime * result + ( report == null ? 0 : report.hashCode() );
    result = prime * result + sequenceNum;
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    ReportChart other = (ReportChart)obj;
    if ( caption == null )
    {
      if ( other.caption != null )
      {
        return false;
      }
    }
    else if ( !caption.equals( other.caption ) )
    {
      return false;
    }
    if ( report == null )
    {
      if ( other.report != null )
      {
        return false;
      }
    }
    else if ( !report.equals( other.report ) )
    {
      return false;
    }
    if ( sequenceNum != other.sequenceNum )
    {
      return false;
    }
    return true;
  }

}
