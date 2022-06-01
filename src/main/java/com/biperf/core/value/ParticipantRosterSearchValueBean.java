
package com.biperf.core.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ParticipantRosterSearchValueBean implements Serializable
{
  private Long id;
  private String lastName;
  private String firstName;
  private String middleName;
  private String emailAddress;
  private String userName;
  private String enrollmentDate;
  private String terminationDate;
  private HierarchyRoleType hierarchyRoleType;
  private String jobPosition;
  private Long participantCount;
  private Date userLoggedInTime;
  private String role;

  public ParticipantRosterSearchValueBean()
  {
    super();
  }

  public ParticipantRosterSearchValueBean( Long id, String lastName, String firstName, String middleName, String emailAddress, String userName, String enrollmentDate )
  {
    this();

    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
    this.middleName = middleName;
    this.emailAddress = emailAddress;
    this.userName = userName;
    this.enrollmentDate = enrollmentDate;
  }

  public ParticipantRosterSearchValueBean( Long id,
                                           String lastName,
                                           String firstName,
                                           String middleName,
                                           String emailAddress,
                                           String userName,
                                           Date enrollmentDate,
                                           Participant participant,
                                           UserNode userNode )// , Timestamp lastLoginDate)
  {
    this( id, lastName, firstName, middleName, emailAddress, userName, DateUtils.toDisplayString( enrollmentDate ) );
    this.hierarchyRoleType = userNode.getHierarchyRoleType();
    if ( participant.getParticipantEmployers() != null && participant.getParticipantEmployers().size() > 0 )
    {
      this.jobPosition = this.paxEmployerjobPosition( participant.getParticipantEmployers() );
      this.terminationDate = this.paxEmployeTerminationDate( participant.getParticipantEmployers() );
    }
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
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

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public HierarchyRoleType getHierarchyRoleType()
  {
    return hierarchyRoleType;
  }

  public void setHierarchyRoleType( HierarchyRoleType hierarchyRoleType )
  {
    this.hierarchyRoleType = hierarchyRoleType;
  }

  public Long getParticipantCount()
  {
    return participantCount;
  }

  public void setParticipantCount( Long participantCount )
  {
    this.participantCount = participantCount;
  }

  public String getNameLFM()
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
    if ( middleName != null )
    {
      fullName.append( " " ).append( middleName ).append( "." );
    }

    return fullName.toString();
  }

  public String getEnrollmentDate()
  {
    return enrollmentDate;
  }

  public void setEnrollmentDate( String enrollmentDate )
  {
    this.enrollmentDate = enrollmentDate;
  }

  public String getTerminationDate()
  {
    return terminationDate;
  }

  public void setTerminationDate( String terminationDate )
  {
    this.terminationDate = terminationDate;
  }

  public String getRole()
  {
    HierarchyRoleType type = HierarchyRoleType.lookup( this.role );
    if ( type != null )
    {
      return type.getName();
    }
    else
    {
      return "";
    }
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  public Date getUserLoggedInTime()
  {
    return userLoggedInTime;
  }

  public void setUserLoggedInTime( Date userLoggedInTime )
  {
    this.userLoggedInTime = userLoggedInTime;
  }

  public String getDisplayLastLogin()
  {
    if ( this.userLoggedInTime != null )
    {
      return DateUtils.toDisplayString( this.userLoggedInTime );
    }
    else
    {
      return CmsResourceBundle.getCmsBundle().getString( "participant.roster.management.modify.NEVER_LOGGED" );
    }
  }

  public String paxEmployeTerminationDate( List<ParticipantEmployer> parEmpList )
  {
    final StringBuffer buf = new StringBuffer();
    for ( ParticipantEmployer participantEmployer : parEmpList )
    {
      if ( participantEmployer != null )
      {
        if ( participantEmployer.getTerminationDate() != null )
        {
          buf.append( DateUtils.toDisplayString( participantEmployer.getTerminationDate() ) );
          break;
        }
      }
    }
    return buf.toString();
  }

  public String paxEmployerjobPosition( List<ParticipantEmployer> parEmpList )
  {
    final StringBuffer buf = new StringBuffer();
    for ( ParticipantEmployer participantEmployer : parEmpList )
    {
      if ( participantEmployer != null )
      {
        if ( participantEmployer.getPositionType() != null )
        {
          PickListValueBean pickListPositionValueBean = getUserService().getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                                                                                     participantEmployer.getParticipant().getLanguageType() == null
                                                                                                         ? UserManager.getDefaultLocale().toString()
                                                                                                         : participantEmployer.getParticipant().getLanguageType().getCode(),
                                                                                                     participantEmployer.getPositionType() );
          buf.append( pickListPositionValueBean.getName() );
          break;
        }
      }
    }
    return buf.toString();
  }

  public String getJobPosition()
  {
    PositionType type = PositionType.lookup( this.jobPosition );
    if ( type != null )
    {
      return type.getName();
    }
    else
    {
      return "";
    }
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }
}
