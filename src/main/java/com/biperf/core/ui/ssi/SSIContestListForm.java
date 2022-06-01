
package com.biperf.core.ui.ssi;

import com.biperf.core.ui.BaseForm;

public class SSIContestListForm extends BaseForm
{

  private String initializationJson;
  private String id;
  private String contestJson;
  private String role;
  private Long userId;
  private Long activityId;
  private String sortedOn;
  private String sortedBy;
  private int page;
  private String filter;

  public String getContestJson()
  {
    return contestJson;
  }

  public void setContestJson( String contestJson )
  {
    this.contestJson = contestJson;
  }

  public String getInitializationJson()
  {
    return initializationJson;
  }

  public void setInitializationJson( String initializationJson )
  {
    this.initializationJson = initializationJson;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage( int page )
  {
    this.page = page;
  }

  public String getFilter()
  {
    return filter;
  }

  public void setFilter( String filter )
  {
    this.filter = filter;
  }

}
