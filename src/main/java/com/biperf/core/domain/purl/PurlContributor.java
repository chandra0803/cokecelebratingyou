
package com.biperf.core.domain.purl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.user.User;

public class PurlContributor extends BaseDomain
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private User user;
  private PurlRecipient purlRecipient;
  private String emailAddr;
  private PurlContributorState state;
  private String firstName;
  private String lastName;
  private PurlContributor invitedContributor;
  private List<PurlContributorMedia> medias = new ArrayList<PurlContributorMedia>();
  private List<PurlContributorComment> comments = new ArrayList<PurlContributorComment>();
  private String avatarUrl;
  private PurlMediaState avatarState;
  private boolean sendLater = false;
  private String displayAvatarUrl;
  private boolean defaultInvitee = false;

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public PurlRecipient getPurlRecipient()
  {
    return purlRecipient;
  }

  public void setPurlRecipient( PurlRecipient purlRecipient )
  {
    this.purlRecipient = purlRecipient;
  }

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
  }

  public PurlContributorState getState()
  {
    return state;
  }

  public void setState( PurlContributorState state )
  {
    this.state = state;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public PurlContributor getInvitedContributor()
  {
    return invitedContributor;
  }

  public void setInvitedContributor( PurlContributor invitedContributor )
  {
    this.invitedContributor = invitedContributor;
  }

  public String getNameLFMNoComma()
  {
    StringBuffer fullName = new StringBuffer();
    if ( lastName != null )
    {
      fullName.append( lastName ).append( " " );
    }
    if ( firstName != null )
    {
      fullName.append( firstName );
    }

    return fullName.toString();
  }

  public String getNameLFMWithComma()
  {
    StringBuffer fullName = new StringBuffer();
    if ( lastName != null )
    {
      fullName.append( lastName ).append( ", " );
    }
    if ( firstName != null )
    {
      fullName.append( firstName );
    }

    return fullName.toString();
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( this.getId() == null ? 0 : this.getId().hashCode() );
    result = prime * result + ( purlRecipient == null ? 0 : purlRecipient.hashCode() );
    result = prime * result + ( emailAddr == null ? 0 : emailAddr.hashCode() );
    result = prime * result + ( user == null ? 0 : user.hashCode() );
    result = prime * result + ( state == null ? 0 : state.hashCode() );
    result = prime * result + ( avatarUrl == null ? 0 : avatarUrl.hashCode() );
    result = prime * result + ( avatarState == null ? 0 : avatarState.hashCode() );
    result = prime * result + ( sendLater ? 0 : 1 );
    return result;
  }

  // pulled changes from G4 to G5 using bugzilla bug # 42978
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( ! ( obj instanceof PurlContributor ) )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    PurlContributor other = (PurlContributor)obj;
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
    if ( purlRecipient == null )
    {
      if ( other.purlRecipient != null )
      {
        return false;
      }
    }
    else if ( !purlRecipient.equals( other.purlRecipient ) )
    {
      return false;
    }

    if ( emailAddr == null )
    {
      if ( other.emailAddr != null )
      {
        return false;
      }
    }
    else if ( !emailAddr.equals( other.emailAddr ) )
    {
      return false;
    }

    if ( user == null )
    {
      if ( other.user != null )
      {
        return false;
      }
    }
    else if ( !user.equals( other.user ) )
    {
      return false;
    }
    if ( state == null )
    {
      if ( other.state != null )
      {
        return false;
      }
    }
    else if ( !state.equals( other.state ) )
    {
      return false;
    }
    if ( avatarUrl == null )
    {
      if ( other.avatarUrl != null )
      {
        return false;
      }
    }
    else if ( !avatarUrl.equals( other.avatarUrl ) )
    {
      return false;
    }
    if ( avatarState == null )
    {
      if ( other.avatarState != null )
      {
        return false;
      }
    }
    else if ( !avatarState.equals( other.avatarState ) )
    {
      return false;
    }
    if ( sendLater != other.sendLater )
    {
      return false;
    }
    return true;
  }

  public List<PurlContributorMedia> getMedias()
  {
    return medias;
  }

  public void setMedias( List<PurlContributorMedia> medias )
  {
    this.medias = medias;
  }

  public List<PurlContributorComment> getComments()
  {
    return comments;
  }

  public void setComments( List<PurlContributorComment> comments )
  {
    this.comments = comments;
  }

  public void addMedias( Set<PurlContributorMedia> medias )
  {
    for ( PurlContributorMedia purlContributorMedia : medias )
    {
      addMedia( purlContributorMedia );
    }
  }

  public void addMedia( PurlContributorMedia purlContributorMedia )
  {
    purlContributorMedia.setPurlContributor( this );
    medias.add( purlContributorMedia );
  }

  public void addComments( Set<PurlContributorComment> comments )
  {
    for ( PurlContributorComment purlContributorComment : comments )
    {
      addComment( purlContributorComment );
    }
  }

  public void addComment( PurlContributorComment purlContributorComment )
  {
    purlContributorComment.setPurlContributor( this );
    comments.add( purlContributorComment );
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public PurlMediaState getAvatarState()
  {
    return avatarState;
  }

  public void setAvatarState( PurlMediaState avatarState )
  {
    this.avatarState = avatarState;
  }

  public boolean isSendLater()
  {
    return sendLater;
  }

  public void setSendLater( boolean sendLater )
  {
    this.sendLater = sendLater;
  }

  public String getDisplayAvatarUrl()
  {
    return avatarUrl;
  }

  public void setDisplayAvatarUrl( String displayAvatarUrl )
  {
    this.displayAvatarUrl = displayAvatarUrl;
  }

  public boolean isDefaultInvitee()
  {
    return defaultInvitee;
  }

  public void setDefaultInvitee( boolean defaultInvitee )
  {
    this.defaultInvitee = defaultInvitee;
  }

}
