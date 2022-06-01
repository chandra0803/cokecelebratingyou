
package com.biperf.core.value;

import com.biperf.core.domain.participant.Participant;

public class SmackTalkParticipantView
{
  private Long id;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private String title;
  private String countryCode;
  private String countryName;

  public SmackTalkParticipantView()
  {

  }

  public SmackTalkParticipantView( Participant participant )
  {
    this.id = participant.getId();
    this.firstName = participant.getFirstName();
    this.lastName = participant.getLastName();
    this.avatarUrl = participant.getAvatarSmallFullPath();
  }

  public SmackTalkParticipantView( Long id, String firstName, String lastName, String avatarUrl, String title, String countryCode, String countryName )
  {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
    this.title = title;
    this.countryCode = countryCode;
    this.countryName = countryName;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
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

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

}
