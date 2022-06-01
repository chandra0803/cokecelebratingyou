
package com.biperf.core.ui.diyquiz;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.PositionType;

/**
 * 
 * DIYQuizParticipantView.
 * 
 * @author kandhi
 * @since Jul 10, 2013
 * @version 1.0
 */
public class DIYQuizParticipantView
{
  private Long quizParticipantId; // Id in the DIY_QUIZ_PARTICIPANT table
  private Long id; // Participant Id
  private String firstName;
  private String lastName;
  private String organization;
  private String countryCode;
  private String countryName;
  private String avatarUrl;

  private String departmentName;
  private String jobName;

  public DIYQuizParticipantView()
  {

  }

  public DIYQuizParticipantView( Long quizParticipantId,
                                 Long id,
                                 String firstName,
                                 String lastName,
                                 String organization,
                                 String countryCode,
                                 String countryName,
                                 String avatarUrl,
                                 String departmentName,
                                 String jobName )
  {
    super();
    this.quizParticipantId = quizParticipantId;
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.organization = organization;
    this.countryCode = countryCode;
    this.countryName = countryName;
    this.avatarUrl = avatarUrl;

    if ( jobName != null )
    {
      DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET + PositionType.ASSET_ITEM_SUFFIX, jobName );
      if ( jobPositionItem != null )
      {
        this.jobName = jobPositionItem.getName();
      }
    }

    if ( departmentName != null )
    {
      DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET + DepartmentType.ASSET_ITEM_SUFFIX, departmentName );
      if ( departmentItem != null )
      {
        this.departmentName = departmentItem.getName();
      }
    }
  }

  public Long getQuizParticipantId()
  {
    return quizParticipantId;
  }

  public void setQuizParticipantId( Long quizParticipantId )
  {
    this.quizParticipantId = quizParticipantId;
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

  public String getOrganization()
  {
    return organization;
  }

  public void setOrganization( String organization )
  {
    this.organization = organization;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

}
