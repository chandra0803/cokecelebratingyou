
package com.biperf.core.domain.leaderboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

public class LeaderBoard extends BaseDomain
{

  private static final long serialVersionUID = 1L;
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private String name;
  private String activityTitle;
  private Date activityDate;
  private Date displayEndDate;
  private String sortOrder;
  private String rulescmAsset;
  private User user;
  private Date startDate;
  private Date endDate;
  private String status;
  private String notifyMessage;

  private Set<LeaderBoardParticipant> participants = new LinkedHashSet();
  private List<LeaderBoardParticipant> sortParticipants = new ArrayList<LeaderBoardParticipant>();

  // These are not persistent in db. These transient variables are used to build the json object.
  private Date asOfDate;
  private Boolean editableByUser;
  private String rulescmAssetText;

  @JsonIgnore
  private boolean displayMode = false;

  // This obj is not presisted, it is used for front end json obj
  private LeaderBoardActionUrls leaderBoardActionUrls = new LeaderBoardActionUrls();

  // DB saved status
  public static final String LEADERBOARD_UNDER_CONSTR = "U";
  public static final String LEADERBOARD_COMPLETE = "C";
  public static final String LEADERBOARD_EXPIRED = "D";

  // Used for reference
  public static final String COMPLETED = "completed";
  public static final String LIVE = "live";
  public static final String EXPIRED = "expired";
  public static final String ALL_NOT_EXPIRED = "allNotExpired";

  public static final String LEADERBOARD_RULES_SECTION_CODE = "leaderboard";
  public static final String LEADERBOARD_RULES_CMASSET_TYPE_NAME = "Name Type";
  public static final String LEADERBOARD_RULES_CMASSET_TYPE_KEY = "HTML_KEY";
  public static final String LEADERBOARD_RULES_CMASSET_NAME = "LeaderBoard Rule Text";
  public static final String LEADERBOARD_RULES_CMASSET_PREFIX = "leaderboard.rulestext.";

  public static final String LEADERBOARD_ACTIVE = "active";
  public static final String LEADERBOARD_PENDING = "pending";
  public static final String LEADERBOARD_ARCHIVED = "archived";

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
  * Get  LeaderBoard StartDate
  * 
  * @return Date
  */
  @JsonIgnore
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * Set LeaderBoard StartDate
   * 
   * @param Date
   */
  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  /**
   * Get  LeaderBoard EndDate
   * 
   * @return Date
   */
  @JsonIgnore
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * Set LeaderBoard EndDate
   * 
   * @param Date
   */
  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  /**
   * Get  LeaderBoard Status
   * 
   * @return String
   */
  @JsonIgnore
  public String getStatus()
  {
    return status;
  }

  /**
   * Set LeaderBoard Status
   * 
   * @param String
   */
  public void setStatus( String status )
  {
    this.status = status;
  }

  @JsonIgnore
  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getActivityTitle()
  {
    return activityTitle;
  }

  public void setActivityTitle( String activityTitle )
  {
    this.activityTitle = activityTitle;
  }

  @JsonIgnore
  public Date getDisplayEndDate()
  {
    return displayEndDate;
  }

  public void setDisplayEndDate( Date displayEndDate )
  {
    this.displayEndDate = displayEndDate;
  }

  @JsonProperty( "activitySortOrder" )
  public String getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder( String sortOrder )
  {
    this.sortOrder = sortOrder;
  }

  @JsonIgnore
  public String getRulescmAsset()
  {
    return rulescmAsset;
  }

  public void setRulescmAsset( String rulescmAsset )
  {
    this.rulescmAsset = rulescmAsset;
  }

  @JsonIgnore
  public Set<LeaderBoardParticipant> getParticipants()
  {
    return participants;
  }

  public void setParticipants( Set<LeaderBoardParticipant> participants )
  {
    this.participants = participants;
  }

  @JsonProperty( "leaders" )
  public List<LeaderBoardParticipant> getSortParticipants()
  {
    return this.sortParticipants;
  }

  public void setSortParticipants( List<LeaderBoardParticipant> sortParticipants )
  {
    this.sortParticipants = sortParticipants;
  }

  @JsonIgnore
  public Date getAsOfDate()
  {
    return asOfDate;
  }

  public void setAsOfDate( Date asOfDate )
  {
    this.asOfDate = asOfDate;
  }

  public Boolean getEditableByUser()
  {
    return editableByUser;
  }

  public void setEditableByUser( Boolean editableByUser )
  {
    this.editableByUser = editableByUser;
  }

  /**
   * @return lastUpdateDate in String
   */
  @JsonIgnore
  public String getDisplayLastUpdatedDate()
  {
    return DateUtils.toDisplayString( this.getAuditUpdateInfo().getDateModified() );
  }

  /**
   * @return startDate in String
   */
  @JsonProperty( "startDate" )
  public String getDisplayUIStartDate()
  {
    return DateUtils.toDisplayString( getStartDate() );
  }

  /**
   * @return endDate in String
   */
  @JsonProperty( "endDate" )
  public String getDisplayUIEndDate()
  {
    return DateUtils.toDisplayString( getEndDate() );
  }

  /*
   * @return displayEndDate in String
   */
  @JsonProperty( "displayEndDate" )
  public String getDisplayUIDisplayEndDate()
  {
    return DateUtils.toDisplayString( getDisplayEndDate() );
  }

  /*
   * @return displayAsOfDate in String
   */
  @JsonProperty( "activityDate" )
  public String getDisplayUIAsOfDate()
  {
    return DateUtils.toDisplayString( getAsOfDate() );
  }

  @JsonProperty( "rules" )
  public String getRulesTextFromCM()
  {
    String rulesText = null;
    if ( this.rulescmAsset != null )
    {
      rulesText = CmsResourceBundle.getCmsBundle().getString( this.rulescmAsset, LeaderBoard.LEADERBOARD_RULES_CMASSET_TYPE_KEY );
    }

    // if(this.displayMode)
    // rulesText=HtmlUtils.removeFormatting( rulesText );

    return rulesText;
  }

  @JsonProperty( "urls" )
  public LeaderBoardActionUrls getLeaderBoardActionUrls()
  {
    return leaderBoardActionUrls;
  }

  public void setLeaderBoardActionUrls( LeaderBoardActionUrls leaderBoardActionUrls )
  {
    this.leaderBoardActionUrls = leaderBoardActionUrls;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  @JsonIgnore
  public String getNotifyMessage()
  {
    return notifyMessage;
  }

  public void setNotifyMessage( String notifyMessage )
  {
    this.notifyMessage = notifyMessage;
  }

  public Date getActivityDate()
  {
    return activityDate;
  }

  public void setActivityDate( Date activityDate )
  {
    this.activityDate = activityDate;
  }

  @JsonIgnore
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "LeaderBoard [" );
    buf.append( "{UserId=" ).append( this.getStartDate() ).append( "}, " );
    buf.append( "{Name=" ).append( this.getEndDate() ).append( "}, " );
    buf.append( "{ActivityTitle=" ).append( this.getStatus() ).append( "}" );
    buf.append( "]" );
    return buf.toString();
  }

  @JsonIgnore
  public String getRulescmAssetText()
  {
    return rulescmAssetText;
  }

  public void setRulescmAssetText( String rulescmAssetText )
  {
    this.rulescmAssetText = rulescmAssetText;
  }

  public boolean isDisplayMode()
  {
    return displayMode;
  }

  public void setDisplayMode( boolean displayMode )
  {
    this.displayMode = displayMode;
  }

  @Override
  @JsonIgnore
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( name == null ? 0 : name.hashCode() );
    result = prime * result + ( rulescmAsset == null ? 0 : rulescmAsset.hashCode() );
    return result;
  }

  @Override
  @JsonIgnore
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
    LeaderBoard other = (LeaderBoard)obj;
    if ( name == null )
    {
      if ( other.name != null )
      {
        return false;
      }
    }
    else if ( !name.equals( other.name ) )
    {
      return false;
    }
    if ( rulescmAsset == null )
    {
      if ( other.rulescmAsset != null )
      {
        return false;
      }
    }
    else if ( !rulescmAsset.equals( other.rulescmAsset ) )
    {
      return false;
    }
    return true;
  }

}
