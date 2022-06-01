
package com.biperf.core.domain.report;

import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ReportParameterType;

public class ReportParameter extends BaseDomain

{
  private Report report;
  private String parameterName;
  private String parameterCmKey;
  private String databaseColumn;
  private ReportParameterType reportParameterType;
  private String listDefinition;
  private String parameterGroup;
  private int sequenceNum;
  private String collectionName;
  private String defaultValue;
  private boolean adminSelectOnly;
  private boolean hideShowAllOption;
  private boolean displayOnDashboard;
  private Set<ReportDashboardItemParam> reportDashboardItemParams = new HashSet<ReportDashboardItemParam>( 0 );

  public ReportParameter()
  {
    super();
  }

  public Report getReport()
  {
    return report;
  }

  public void setReport( Report report )
  {
    this.report = report;
  }

  public String getParameterName()
  {
    return parameterName;
  }

  public void setParameterName( String parameterName )
  {
    this.parameterName = parameterName;
  }

  public String getParameterCmKey()
  {
    return parameterCmKey;
  }

  public void setParameterCmKey( String parameterCmKey )
  {
    this.parameterCmKey = parameterCmKey;
  }

  public String getDatabaseColumn()
  {
    return databaseColumn;
  }

  public void setDatabaseColumn( String databaseColumn )
  {
    this.databaseColumn = databaseColumn;
  }

  public ReportParameterType getReportParameterType()
  {
    return reportParameterType;
  }

  public void setReportParameterType( ReportParameterType reportParameterType )
  {
    this.reportParameterType = reportParameterType;
  }

  public String getListDefinition()
  {
    return listDefinition;
  }

  public void setListDefinition( String listDefinition )
  {
    this.listDefinition = listDefinition;
  }

  public String getParameterGroup()
  {
    return parameterGroup;
  }

  public void setParameterGroup( String parameterGroup )
  {
    this.parameterGroup = parameterGroup;
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public String getCollectionName()
  {
    return collectionName;
  }

  public void setCollectionName( String collectionName )
  {
    this.collectionName = collectionName;
  }

  public String getDefaultValue()
  {
    return defaultValue;
  }

  public void setDefaultValue( String defaultValue )
  {
    this.defaultValue = defaultValue;
  }

  public void setAdminSelectOnly( boolean adminSelectOnly )
  {
    this.adminSelectOnly = adminSelectOnly;
  }

  public boolean isAdminSelectOnly()
  {
    return adminSelectOnly;
  }

  public void setHideShowAllOption( boolean hideShowAllOption )
  {
    this.hideShowAllOption = hideShowAllOption;
  }

  public boolean isHideShowAllOption()
  {
    return hideShowAllOption;
  }

  public Set<ReportDashboardItemParam> getReportDashboardItemParams()
  {
    return reportDashboardItemParams;
  }

  public void setReportDashboardItemParams( Set<ReportDashboardItemParam> reportDashboardItemParams )
  {
    this.reportDashboardItemParams = reportDashboardItemParams;
  }

  public void addReportDashboardItemParam( ReportDashboardItemParam reportDashboardItemParam )
  {
    reportDashboardItemParam.setReportParameter( this );
    this.reportDashboardItemParams.add( reportDashboardItemParam );
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( parameterName == null ? 0 : parameterName.hashCode() );
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
    ReportParameter other = (ReportParameter)obj;
    if ( parameterName == null )
    {
      if ( other.parameterName != null )
      {
        return false;
      }
    }
    else if ( !parameterName.equals( other.parameterName ) )
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

  public boolean isDisplayOnDashboard()
  {
    return displayOnDashboard;
  }

  public void setDisplayOnDashboard( boolean displayOnDashboard )
  {
    this.displayOnDashboard = displayOnDashboard;
  }

}
