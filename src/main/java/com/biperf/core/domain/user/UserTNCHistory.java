
package com.biperf.core.domain.user;

import java.sql.Timestamp;

import com.biperf.core.domain.BaseDomain;

public class UserTNCHistory extends BaseDomain
{

  private static final long serialVersionUID = 1L;

  public static final String OPT_OUT_OF_AWARDS_ON = "OptOutOfAwards - ON";
  public static final String OPT_OUT_OF_AWARDS_OFF = "OptOutOfAwards - OFF";
  public static final String OPT_OUT_OF_PROGRAM_ON = "OptOutOfProgram - ON";
  public static final String OPT_OUT_OF_PROGRAM_OFF = "OptOutOfProgram - OFF";

  private User user;
  private String tncAction;
  private Timestamp historyDateCreated;
  private Long historyCreatedBy;

  public Timestamp getHistoryDateCreated()
  {
    return historyDateCreated;
  }

  public void setHistoryDateCreated( Timestamp historyDateCreated )
  {
    this.historyDateCreated = historyDateCreated;
  }

  public Long getHistoryCreatedBy()
  {
    return historyCreatedBy;
  }

  public void setHistoryCreatedBy( Long historyCreatedBy )
  {
    this.historyCreatedBy = historyCreatedBy;
  }

  public UserTNCHistory()
  {

  }

  public User getUser()
  {
    return user;
  }

  public String getTncAction()
  {
    return tncAction;
  }

  public void setTncAction( String tncAction )
  {
    this.tncAction = tncAction;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof UserTNCHistory ) )
    {
      return false;
    }

    final UserTNCHistory other = (UserTNCHistory)object;

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
