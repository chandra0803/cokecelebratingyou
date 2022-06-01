
package com.biperf.core.ui.ssi;

import java.sql.Timestamp;
import java.util.Date;

import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.ssi.SSIAdminContestActions;

public class SSIContestAdminView
{
  private Long id;
  private String contestName;
  private String creatorName;
  private SSIContestStatus contestStatus;
  private SSIContestType contestType;
  private Date contestStartDate;
  private Timestamp dateModified;
  private Date contestEndDate;
  private boolean canClose = false;
  private boolean canShowLaunchAsButton = false;
  private boolean canShowEditedAdminIndicator = false;
  private String ssiAdminNameWithLastUpdated;
  private SSIAdminContestActions ssiAdminContestActions;

  public String getSsiAdminNameWithLastUpdated()
  {
    return ssiAdminNameWithLastUpdated;
  }

  public void setSsiAdminNameWithLastUpdated( String ssiAdminNameWithLastUpdated )
  {
    this.ssiAdminNameWithLastUpdated = ssiAdminNameWithLastUpdated;
  }

  public SSIAdminContestActions getSsiAdminContestActions()
  {
    return ssiAdminContestActions;
  }

  public void setSsiAdminContestActions( SSIAdminContestActions ssiAdminContestActions )
  {
    this.ssiAdminContestActions = ssiAdminContestActions;
  }

  public boolean isCanShowEditedAdminIndicator()
  {
    return canShowEditedAdminIndicator;
  }

  public void setCanShowEditedAdminIndicator( boolean canShowEditedAdminIndicator )
  {
    this.canShowEditedAdminIndicator = canShowEditedAdminIndicator;
  }

  public boolean isCanShowLaunchAsButton()
  {
    return canShowLaunchAsButton;
  }

  public void setCanShowLaunchAsButton( boolean canShowLaunchAsButton )
  {
    this.canShowLaunchAsButton = canShowLaunchAsButton;
  }

  public boolean isCanClose()
  {
    return canClose;
  }

  public void setCanClose( boolean canClose )
  {
    this.canClose = canClose;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Date getContestStartDate()
  {
    return contestStartDate;
  }

  public void setContestStartDate( Date contestStartDate )
  {
    this.contestStartDate = contestStartDate;
  }

  public String getContestName()
  {
    return contestName;
  }

  public void setContestName( String contestName )
  {
    this.contestName = contestName;
  }

  public String getCreatorName()
  {
    return creatorName;
  }

  public void setCreatorName( String creatorName )
  {
    this.creatorName = creatorName;
  }

  public SSIContestStatus getContestStatus()
  {
    return contestStatus;
  }

  public void setContestStatus( SSIContestStatus contestStatus )
  {
    this.contestStatus = contestStatus;
  }

  public Timestamp getDateModified()
  {
    return dateModified;
  }

  public void setDateModified( Timestamp dateModified )
  {
    this.dateModified = dateModified;
  }

  public Date getContestEndDate()
  {
    return contestEndDate;
  }

  public void setContestEndDate( Date contestEndDate )
  {
    this.contestEndDate = contestEndDate;
  }

  public SSIContestType getContestType()
  {
    return contestType;
  }

  public void setContestType( SSIContestType contestType )
  {
    this.contestType = contestType;
  }
}
