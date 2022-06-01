
package com.biperf.core.service.claim;

import com.biperf.core.utils.StringUtil;

public class PurlContributor
{
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private Long contributorId;
  private String avatarUrl;
  private String contribType;
  private boolean defaultInvitee = false;

  public PurlContributor( String userId, String firstName, String lastName, String avatarUrl, String email, String contribType, boolean defaultInvitee )
  {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
    this.email = email;
    this.contribType = contribType;
    this.defaultInvitee = defaultInvitee;

    if ( "other".equals( contribType ) )
    {
      this.userId = email;
    }
  }

  public boolean isDefaultInvitee()
  {
    return defaultInvitee;
  }

  public void setDefaultInvitee( boolean defaultInvitee )
  {
    this.defaultInvitee = defaultInvitee;
  }

  public boolean isPreselectedContribType()
  {
    return "preselected".equals( contribType );
  }

  public boolean isAdditionalContribType()
  {
    return "additional".equals( contribType );
  }

  public boolean isOtherContribType()
  {
    return "other".equals( contribType );
  }

  public String getEmail()
  {
    return email;
  }

  public Long getContributorId()
  {
    return contributorId;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getUserId()
  {
    return userId;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof PurlContributor ) )
    {
      return false;
    }

    PurlContributor purlContributor = (PurlContributor)object;

    if ( userId != null ? !userId.equals( purlContributor.getUserId() ) : purlContributor.getUserId() != null )
    {
      return false;
    }

    if ( contributorId != null ? !contributorId.equals( purlContributor.getContributorId() ) : purlContributor.getContributorId() != null )
    {
      return false;
    }

    return true;

  } // end equals

  public int hashCode()
  {
    int result = 0;

    result += userId != null ? userId.hashCode() : 0;
    result += contribType != null ? contribType.hashCode() : 13;

    return result;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toProcessString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( this.getUserId() ).append( "," );
    buf.append( this.getFirstName() ).append( "," );
    buf.append( this.getLastName() ).append( "," );
    buf.append( this.getAvatarUrl() ).append( "," );
    if ( this.getEmail() != null && !this.getEmail().equals( "" ) )
    {
      buf.append( this.getEmail() ).append( "," );
    }
    else
    {
      buf.append( "" ).append( "," );
    }
    buf.append( StringUtil.isEmpty( this.contribType ) ? null : this.contribType ).append( "," );
    buf.append( this.defaultInvitee );
    return buf.toString();
  }

}
