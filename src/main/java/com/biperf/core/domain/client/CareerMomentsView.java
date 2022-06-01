/**
 * 
 */

package com.biperf.core.domain.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
@JsonInclude( value = Include.NON_NULL )
public class CareerMomentsView
{
  private Long id;
  private Long recipientId;
  private String recipientAvatar;
  private int totalRecords;
  private String lastName;
  private String firstName;
  private String avatarUrl = null;
  private String departmentType;
  private String recType;
  private String divisionName;
  private String positionType;
  private String celebrationDate;
  private String contributeUrl;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public String getRecipientAvatar()
  {
    return recipientAvatar;
  }

  public void setRecipientAvatar( String recipientAvatar )
  {
    this.recipientAvatar = recipientAvatar;
  }

  public int getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( int totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getDepartmentType()
  {
    return departmentType;
  }

  public void setDepartmentType( String departmentType )
  {
    this.departmentType = departmentType;
  }

  public String getRecType()
  {
    return recType;
  }

  public void setRecType( String recType )
  {
    this.recType = recType;
  }

  public String getDivisionName()
  {
    return divisionName;
  }

  public void setDivisionName( String divisionName )
  {
    this.divisionName = divisionName;
  }

  public String getPositionType()
  {
    return positionType;
  }

  public void setPositionType( String positionType )
  {
    this.positionType = positionType;
  }

  public String getCelebrationDate()
  {
    return celebrationDate;
  }

  public void setCelebrationDate( String celebrationDate )
  {
    this.celebrationDate = celebrationDate;
  }

  public String getContributeUrl()
  {
    return contributeUrl;
  }

  public void setContributeUrl( String contributeUrl )
  {
    this.contributeUrl = contributeUrl;
  }

}
