
package com.biperf.core.domain.report;

import com.biperf.core.domain.BaseDomain;

public class ReportDashboardItemParam extends BaseDomain

{
  private ReportDashboardItem reportDashboardItem;
  private ReportParameter reportParameter;
  private String value;
  private Boolean autoUpdate = false;

  public ReportDashboardItemParam()
  {
    super();
  }

  public ReportDashboardItem getReportDashboardItem()
  {
    return reportDashboardItem;
  }

  public void setReportDashboardItem( ReportDashboardItem reportDashboardItem )
  {
    this.reportDashboardItem = reportDashboardItem;
  }

  public ReportParameter getReportParameter()
  {
    return reportParameter;
  }

  public void setReportParameter( ReportParameter reportParameter )
  {
    this.reportParameter = reportParameter;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  @Override
  public int hashCode()
  {
    int result = 0;

    result += this.getReportDashboardItem() != null ? this.getReportDashboardItem().hashCode() : 0;
    result += this.getReportParameter() != null ? this.getReportParameter().hashCode() * 13 : 0;

    return result;
    /*
     * final int prime = 31; int result = 1; result = prime * result + ( ( reportDashboardItem ==
     * null ) ? 0 : reportDashboardItem.hashCode() ); result = prime * result + ( ( reportParameter
     * == null ) ? 0 : reportParameter.hashCode() ); return result;
     */
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
    ReportDashboardItemParam other = (ReportDashboardItemParam)obj;
    if ( reportDashboardItem == null )
    {
      if ( other.reportDashboardItem != null )
      {
        return false;
      }
    }
    else if ( !reportDashboardItem.equals( other.reportDashboardItem ) )
    {
      return false;
    }
    if ( reportParameter == null )
    {
      if ( other.reportParameter != null )
      {
        return false;
      }
    }
    else if ( !reportParameter.equals( other.reportParameter ) )
    {
      return false;
    }
    return true;
  }

  public Boolean getAutoUpdate()
  {
    return autoUpdate;
  }

  public void setAutoUpdate( Boolean autoUpdate )
  {
    this.autoUpdate = autoUpdate;
  }
}
