
package com.biperf.core.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.value.client.CheersPointValueBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class PublicRecognitionCheersListView
{
  private List<WebErrorMessage> messages;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private String department;
  private String orgName;
  private List<CheersPointValueBean> points = new ArrayList<CheersPointValueBean>();

  public void mapRecipient( Participant recipient )
  {
    this.firstName = recipient.getFirstName();
    this.lastName = recipient.getLastName();
    this.avatarUrl = recipient.getAvatarSmallFullPath();
    this.department = recipient.getPaxDeptName();
    this.orgName = recipient.getPaxOrgName();
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public List<CheersPointValueBean> getPoints()
  {
    return points;
  }

  public void setPoints( List<CheersPointValueBean> points )
  {
    this.points = points;
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

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

}
