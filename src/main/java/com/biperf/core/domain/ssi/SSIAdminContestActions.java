
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;

public class SSIAdminContestActions extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  public static final String EDIT_CREATOR = "Edit Creatror";
  public static final String CLOSE_CONTEST = "Close Contest";
  public static final String COPY_CONTEST = "Close Contest";
  public static final String CREATE_CONTEST = "Create Contest";
  public static final String EDIT_CONTEST = "Edit Contest";
  public static final String SAVE_AS_DRAFT = "Save As Draft";
  public static final String UPLOAD_CONTEST_PROGRESS_SPREADSHEET = "Upload Contest Progress Spreadsheet";
  public static final String UPDATE_CONTEST_PROGRESS_ONLINE = "upadte Contest Progress Online";
  public static final String ISSUE_PAYOUTS = "Issue Payouts";
  public static final String APPROVE_CONTEST = "Approve Contest";
  public static final String DENY_CONTEST = "Deny Contest";

  private Long userID;
  private Long contestID;
  private String action;
  private String description;

  public SSIAdminContestActions()
  {

  }

  public Long getUserID()
  {
    return userID;
  }

  public void setUserID( Long userID )
  {
    this.userID = userID;
  }

  public Long getContestID()
  {
    return contestID;
  }

  public void setContestID( Long contestID )
  {
    this.contestID = contestID;
  }

  public String getAction()
  {
    return action;
  }

  public void setAction( String action )
  {
    this.action = action;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof SSIAdminContestActions ) )
    {
      return false;
    }

    final SSIAdminContestActions other = (SSIAdminContestActions)object;

    if ( getId() == null )
    {
      if ( other.getId() != null )
      {
        return false;
      }
    }
    else if ( !getId().equals( other.getId() ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 0;
    result = prime * result + ( this.getId() != null ? this.getId().hashCode() : 0 );
    return result;
  }

}
