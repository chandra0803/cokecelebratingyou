
package com.biperf.core.ui.reports;

import java.util.Arrays;
import java.util.List;

public class ReportParameterInfo implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;
  private static final String SHOW_ALL = "show_all";
  public static final String NOMINATION_COMMENT_AVAILABLE = "nomCommentAvailable";

  private Long id;
  private String name;
  private String value;
  private Boolean autoUpdate;
  private String[] values;
  private String type;
  private String cmKey;
  private String parameterGroup;
  private String listDefinition;
  private String collectionName;
  private boolean adminSelectOnly;
  private boolean hideShowAllOption;
  private boolean showOnDashboard;
  private Boolean nodeAndBelow = true;
  private String reportCode;

  public String getCollectionName()
  {
    return collectionName;
  }

  public void setCollectionName( String collectionName )
  {
    this.collectionName = collectionName;
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

  public String getListDefinition()
  {
    return listDefinition;
  }

  public void setListDefinition( String listDefinition )
  {
    this.listDefinition = listDefinition;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public Boolean getAutoUpdate()
  {
    return autoUpdate;
  }

  public void setAutoUpdate( Boolean autoUpdate )
  {
    this.autoUpdate = autoUpdate;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getCmKey()
  {
    return cmKey;
  }

  public void setCmKey( String cmKey )
  {
    this.cmKey = cmKey;
  }

  public String getParameterGroup()
  {
    return parameterGroup;
  }

  public void setParameterGroup( String parameterGroup )
  {
    this.parameterGroup = parameterGroup;
  }

  public String[] getValues()
  {
    return values;
  }

  public void setValues( String[] values )
  {
    this.values = values;
  }

  public boolean isShowOnDashboard()
  {
    return showOnDashboard;
  }

  public void setShowOnDashboard( boolean showOnDashboard )
  {
    this.showOnDashboard = showOnDashboard;
  }

  public String getParameterValue()
  {
    String paramValue = null;
    if ( values != null && values.length > 0 )
    {
      List<String> valuesList = (List)Arrays.asList( values );
      if ( valuesList.contains( SHOW_ALL ) )
      {
        paramValue = null;
      }
      else
      { // For multi-select tags
        for ( String arrValue : values )
        {

          if ( arrValue != null && !SHOW_ALL.equals( arrValue ) )
          {
            if ( paramValue != null )
            {
              paramValue = paramValue + ",";
            }
            else
            {
              paramValue = "";
            }
            paramValue = paramValue + arrValue;
          }
        }
      }

    }
    else if ( !SHOW_ALL.equals( value ) )
    {
      paramValue = value;
    }
    return paramValue;
  }

  public Boolean getNodeAndBelow()
  {
    return nodeAndBelow;
  }

  public void setNodeAndBelow( Boolean nodeAndBelow )
  {
    this.nodeAndBelow = nodeAndBelow;
  }

  public String getReportCode()
  {
    return reportCode;
  }

  public void setReportCode( String reportCode )
  {
    this.reportCode = reportCode;
  }

}
