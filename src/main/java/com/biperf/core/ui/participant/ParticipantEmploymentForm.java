/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/ParticipantEmploymentForm.java,v $
 *
 */

package com.biperf.core.ui.participant;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ParticipantEmploymentForm <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ParticipantEmploymentForm extends BaseForm
{
  public static final String UPDATE_FORM_NAME = "participantEmploymentUpdateForm";
  public static final String CREATE_FORM_NAME = "participantEmploymentCreateForm";

  private long userId;
  private String employerId;
  private String position;
  private String department;
  private String hireDate = DateUtils.displayDateFormatMask;
  private String terminationDate = DateUtils.displayDateFormatMask;
  private String previousTerminationDate = DateUtils.displayDateFormatMask;
  private String method;
  private long dateCreated;
  private String createdBy;

  public long getUserId()
  {
    return userId;
  }

  public void setUserId( long userId )
  {
    this.userId = userId;
  }

  public String getEmployerId()
  {
    return employerId;
  }

  public void setEmployerId( String employerId )
  {
    this.employerId = employerId;
  }

  public String getPosition()
  {
    return position;
  }

  public void setPosition( String position )
  {
    this.position = position;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getHireDate()
  {
    return hireDate;
  }

  public void setHireDate( String hireDate )
  {
    this.hireDate = hireDate;
  }

  public String getTerminationDate()
  {
    return terminationDate;
  }

  public void setTerminationDate( String terminationDate )
  {
    this.terminationDate = terminationDate;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public String getPreviousTerminationDate()
  {
    return previousTerminationDate;
  }

  public void setPreviousTerminationDate( String previousTerminationDate )
  {
    this.previousTerminationDate = previousTerminationDate;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );
    Participant participant = null;

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    Date formatDate = null;
    if ( hireDate != null && hireDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( hireDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( hireDate ) )
      {
        actionErrors.add( "hireDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "participant.participant.HIRE_DATE" ) ) );

      }
    }
    if ( terminationDate != null && terminationDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( terminationDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( terminationDate ) )
      {
        actionErrors.add( "terminationDate",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "participant.participant.TERMINATION_DATE" ) ) );

      }
    }
    if ( previousTerminationDate != null && previousTerminationDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( previousTerminationDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( previousTerminationDate ) )
      {
        actionErrors.add( "previousTerminationDate",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "participant.employment.history.PREV_TERM_DATE" ) ) );

      }
    }

    if ( DateUtils.toDate( hireDate ) != null && DateUtils.toDate( previousTerminationDate ) != null && DateUtils.toDate( hireDate ).after( DateUtils.toDate( previousTerminationDate ) ) )
    {
      actionErrors.add( "hireDate", new ActionMessage( "participant.errors.START_DATE_AFTER_END_DATE" ) );
    }

    if ( DateUtils.toDate( hireDate ) != null && DateUtils.toDate( terminationDate ) != null && DateUtils.toDate( hireDate ).after( DateUtils.toDate( terminationDate ) ) )
    {
      actionErrors.add( "hireDate", new ActionMessage( "participant.errors.START_DATE_AFTER_END_DATE" ) );
    }

    if ( DateUtils.toDate( terminationDate ) != null && DateUtils.toDate( terminationDate ).after( DateUtils.getCurrentDate() ) )
    {
      actionErrors.add( "TerminationDate", new ActionMessage( "participant.errors.TERMINATION_DATE_AFTER_CURRENT" ) );
    }
    // BugFix 17271
    // if the termination Date Removed , validate node relationship rules since they will all be
    // reactivated
    if ( this.userId > 0 )
    {
      participant = getParticipantService().getParticipantById( new Long( this.userId ) );
    }

    if ( this.getMethod() != null && this.getMethod().equalsIgnoreCase( "update" ) )
    {
      if ( participant != null && participant.getStatus() != null && !participant.isActive().booleanValue() && ( terminationDate == null || StringUtils.isEmpty( terminationDate ) ) )
      {
        validateNodeOwnerRule( actionErrors );
      }
    }
    if ( this.getMethod() != null && this.getMethod().equalsIgnoreCase( "create" ) )
    {
      if ( participant != null && participant.getStatus() != null && !participant.isActive().booleanValue() && ( terminationDate == null || StringUtils.isEmpty( terminationDate ) ) )
      {
        validateNodeOwnerRule( actionErrors );
      }
      if ( participant != null && participant.getStatus() != null && !participant.isActive().booleanValue() && DateUtils.toDate( terminationDate ) != null
          && DateUtils.toDate( terminationDate ).compareTo( DateUtils.getCurrentDate() ) <= 0 )
      {
        validateNodeOwnerRule( actionErrors );
      }

    }

    return actionErrors;
  }

  /**
   * Load the form with the domain object value;
   * 
   * @param participantEmployer
   */
  public void load( ParticipantEmployer participantEmployer )
  {
    this.userId = participantEmployer.getParticipant().getId().longValue();
    this.employerId = participantEmployer.getEmployer().getId().toString();
    if ( participantEmployer.getPositionType() != null )
    {
      this.position = participantEmployer.getPositionType();
    }
    if ( participantEmployer.getDepartmentType() != null )
    {
      this.department = participantEmployer.getDepartmentType();
    }
    this.hireDate = participantEmployer.getHireDate() != null ? DateUtils.toDisplayString( participantEmployer.getHireDate() ) : DateUtils.displayDateFormatMask;
    this.terminationDate = participantEmployer.getTerminationDate() != null ? DateUtils.toDisplayString( participantEmployer.getTerminationDate() ) : DateUtils.displayDateFormatMask;
    this.dateCreated = participantEmployer.getAuditCreateInfo().getDateCreated().getTime();
    this.createdBy = participantEmployer.getAuditCreateInfo().getCreatedBy().toString();
  }

  /**
   * Builds a domain object from the form.
   * 
   * @return ParticipantEmployer
   */
  public ParticipantEmployer toDomainObject()
  {
    ParticipantEmployer participantEmployer = new ParticipantEmployer();

    Employer employer = new Employer();
    employer.setId( new Long( this.employerId ) );
    participantEmployer.setEmployer( employer );

    Participant participant = new Participant();
    participant.setId( new Long( this.userId ) );
    participantEmployer.setParticipant( participant );

    participantEmployer.setDepartmentType( this.department );
    participantEmployer.setPositionType( this.position );
    participantEmployer.setHireDate( DateUtils.toDate( this.hireDate ) );
    participantEmployer.setTerminationDate( DateUtils.toDate( this.terminationDate ) );

    // Note: only set these if they are not empty. When a new record is created, createdBy and
    // dateCreated are empty in the
    // form. The constructor for ParticipantEmployer sets these fields for new objects.
    if ( !StringUtils.isEmpty( this.createdBy ) )
    {
      participantEmployer.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
      participantEmployer.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    }

    return participantEmployer;
  }

  // BugFix 17271 starts
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private void validateNodeOwnerRule( ActionErrors actionErrors )
  {

    for ( Iterator iter = getUserService().getUserNodes( new Long( this.userId ) ).iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( userNode.getHierarchyRoleType().getCode().equals( HierarchyRoleType.OWNER ) && getNodeService().isOwnerAlreadyInUserNode( getUserId(), userNode.getNode().getId() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.errors.NODE_ALREADY_HAS_OWNER_DETAILED", userNode.getNode().getName() ) );
      }
    }

  }
  // BugFix 17271 ends
}
