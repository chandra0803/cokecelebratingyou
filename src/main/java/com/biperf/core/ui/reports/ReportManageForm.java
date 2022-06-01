
package com.biperf.core.ui.reports;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.ReportCategoryType;
import com.biperf.core.ui.BaseForm;

public class ReportManageForm extends BaseForm
{
  String name;
  String reportCode;
  private String cmAssetCode;
  ReportCategoryType categoryType;
  String url;
  String description;
  boolean active;
  String module;
  boolean forceParameters;
  boolean exportOnly;
  private String method;
  private String[] updateValues;

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    return actionErrors;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getReportCode()
  {
    return reportCode;
  }

  public void setReportCode( String reportCode )
  {
    this.reportCode = reportCode;
  }

  public ReportCategoryType getCategoryType()
  {
    return categoryType;
  }

  public void setCategoryType( ReportCategoryType categoryType )
  {
    this.categoryType = categoryType;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getModule()
  {
    return module;
  }

  public void setModule( String module )
  {
    this.module = module;
  }

  public boolean isForceParameters()
  {
    return forceParameters;
  }

  public void setForceParameters( boolean forceParameters )
  {
    this.forceParameters = forceParameters;
  }

  public boolean isExportOnly()
  {
    return exportOnly;
  }

  public void setExportOnly( boolean exportOnly )
  {
    this.exportOnly = exportOnly;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMethod()
  {
    return method;
  }

  public void setUpdateValues( String[] updateValues )
  {
    this.updateValues = updateValues;
  }

  public String[] getUpdateValues()
  {
    return updateValues;
  }
}
