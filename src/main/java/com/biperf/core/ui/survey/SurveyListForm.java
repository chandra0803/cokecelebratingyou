
package com.biperf.core.ui.survey;

import com.biperf.core.ui.BaseForm;

public class SurveyListForm extends BaseForm
{
  private String method;
  private String surveyFormId;
  private String[] deleteUnderConstructionIds;
  private String[] deleteCompletedIds;

  public String[] getDeleteCompletedIds()
  {
    return deleteCompletedIds;
  }

  public void setDeleteCompletedIds( String[] deleteCompletedIds )
  {
    this.deleteCompletedIds = deleteCompletedIds;
  }

  public String[] getDeleteUnderConstructionIds()
  {
    return deleteUnderConstructionIds;
  }

  public void setDeleteUnderConstructionIds( String[] deleteUnderConstructionIds )
  {
    this.deleteUnderConstructionIds = deleteUnderConstructionIds;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getSurveyFormId()
  {
    return surveyFormId;
  }

  public void setSurveyFormId( String surveyFormId )
  {
    this.surveyFormId = surveyFormId;
  }

}
