
package com.biperf.core.ui.managertoolkit;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.ParticipantEnrollmentSource;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.RegistrationService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.user.UserForm;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.UserManager;

public class ParticipantRosterMgmtAction extends BaseDispatchAction
{
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return list( actionMapping, actionForm, request, response );
  }

  public ActionForward rosterAdd( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm form = (UserForm)actionForm;
    ActionMessages errors = new ActionMessages();

    // Generate password
    setGeneratedPassword( form );

    Participant participant = form.toDomainObjectParticipant();

    // overriding the value what we get from form.toDomainObjectParticipant();
    participant.setEnrollmentSource( ParticipantEnrollmentSource.lookup( ParticipantEnrollmentSource.ROSTER ) );
    participant.setEnrollmentDate( new Date() );

    participant.setLanguageType( LanguageType.lookup( form.getLanguage() ) );
    if ( participant.getLanguageType() == null )
    {
      participant.setLanguageType( LanguageType.lookup( getSystemVariableService().getDefaultLanguage().getStringVal() ) );
    }
    if ( !StringUtils.isEmpty( form.getNodeId() ) )
    {
      Node node = getNodeService().getNodeById( new Long( form.getNodeId() ) );
      UserNode userNode = new UserNode();
      userNode.setNode( node );
      userNode.setHierarchyRoleType( HierarchyRoleType.lookup( form.getNodeRelationship() ) );
      userNode.setActive( Boolean.TRUE );
      userNode.setIsPrimary( Boolean.TRUE );
      participant.addUserNode( userNode );
    }
    try
    {
      participant = getParticipantService().createFullParticipant( participant );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( participant.getId() );
      if ( participant.getPrimaryEmailAddress() != null && participant.getPrimaryEmailAddress().getEmailAddr() != null && !participant.isWelcomeEmailSent() )
      {
        getRegistrationService().sendWelcomeEamiltoRosterMgmtPax( participant );
      }
      // Uncomment the below line to get the model confirmation for created pax
      // request.getSession().setAttribute( "participantCreated", true );
    }
    catch( ServiceErrorException e )
    {
      log.error( e );
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return mapping
   */
  @SuppressWarnings( "rawtypes" )
  public ActionForward list( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm userForm = (UserForm)request.getAttribute( UserForm.NAME );
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );

    // Set all nodes for which participant is manager/owner
    List nodeList = participant.getActiveManagerNodes();
    request.setAttribute( "nodeList", nodeList );

    if ( nodeList != null && nodeList.size() == 1 )
    {
      request.setAttribute( "node", nodeList.get( 0 ) );
    }

    // Set default selected node, if not already selected
    if ( StringUtils.isEmpty( userForm.getNodeId() ) )
    {
      if ( null != nodeList && !nodeList.isEmpty() )
      {
        Long defaultSelectedNode = participant.getPrimaryUserNode().getNode().getId();
        userForm.setNodeId( String.valueOf( defaultSelectedNode ) );
      }
    }
    // Set participant status list
    request.setAttribute( "participantStatusList", ParticipantStatus.getList() );

    // Set default status, if not already selected
    if ( StringUtils.isEmpty( userForm.getPaxStatus() ) )
    {
      String defaultStatus = ParticipantStatus.lookup( ParticipantStatus.ACTIVE ).getCode();
      userForm.setPaxStatus( defaultStatus );
    }
    if ( !StringUtils.isEmpty( userForm.getPaxStatus() ) )
    {
      if ( userForm.getPaxStatus().equalsIgnoreCase( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ).getCode() ) )
      {
        request.setAttribute( "listStatus", "active" );
      }
      else
      {
        request.setAttribute( "listStatus", "inactive" );
      }
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  @SuppressWarnings( "rawtypes" )
  public ActionForward prepareRosterEdit( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm form = (UserForm)actionForm;

    Participant participant = getParticipantService().getParticipantOverviewById( new Long( form.getUserId() ) );

    form.loadParticipant( participant );
    if ( participant.getLanguageType() == null )
    {
      participant.setLanguageType( LanguageType.lookup( getSystemVariableService().getDefaultLanguage().getStringVal() ) );
    }
    else
    {
      form.setLanguage( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    form.setWelcomeEmailSent( participant.getWelcomeEmailSent() );

    UserNode userNode = getUserService().getUserNodeByUserIdAndNodeId( new Long( form.getId() ), new Long( form.getNodeId() ) );
    form.setNodeRelationship( userNode.getHierarchyRoleType().getCode() );
    form.setRosterPaxuserId( new Long( form.getUserId() ) );

    // Replacing paxOverview call with specific associations: reusing already loaded object from
    // above
    ParticipantEmployer participantEmployer = null;
    List employmentsNonDomain = participant.getParticipantEmployers();
    Iterator employmentIterator = employmentsNonDomain.iterator();
    if ( employmentIterator.hasNext() )
    {
      participantEmployer = (ParticipantEmployer)employmentIterator.next();
    }

    if ( participantEmployer != null )
    {
      form.loadParticipantEmployer( participantEmployer );
    }
    return actionMapping.findForward( "display" );
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public ActionForward rosterEdit( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserForm form = (UserForm)actionForm;
    ActionMessages errors = new ActionMessages();

    form.setPasswordSystemGenerated( Boolean.FALSE );
    Participant participant = form.toDomainObjectParticipant();
    participant.setLanguageType( LanguageType.lookup( form.getLanguage() ) );
    participant.setId( form.getId() );

    if ( !StringUtils.isEmpty( form.getNodeId() ) )
    {
      Node node = getNodeService().getNodeById( new Long( form.getNodeId() ) );

      UserNode userNode = new UserNode();
      userNode.setNode( node );
      userNode.setHierarchyRoleType( HierarchyRoleType.lookup( form.getNodeRelationship() ) );
      userNode.setActive( Boolean.TRUE );

      participant.addUserNode( userNode );
    }
    try
    {
      getParticipantService().rosterUpdateParticipant( participant );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( participant.getId() );

      // Update User characteristics
      if ( null != participant.getUserCharacteristics() && !participant.getUserCharacteristics().isEmpty() )
      {
        List characteristicList = new ArrayList( participant.getUserCharacteristics() );

        Iterator iter = characteristicList.iterator();
        while ( iter.hasNext() )
        {
          UserCharacteristic userCharacteristic = (UserCharacteristic)iter.next();
          if ( userCharacteristic.getCharacteristicValue() != null && userCharacteristic.getCharacteristicValue().equals( "" ) )
          {
            userCharacteristic.setCharacteristicValue( "delete_option" );
          }
        }

        getUserService().updateUserCharacteristics( participant.getId(), characteristicList );
        getUserService().updateBankCharacteristics( participant.getId(), characteristicList );
      }
      if ( participant.getPrimaryEmailAddress() != null && participant.getPrimaryEmailAddress().getEmailAddr() != null && !participant.isWelcomeEmailSent() )
      {
        // need to set the password bug#53068
        String password = getUserService().generatePassword();
        participant.setPassword( password );
        if ( getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_FORCE_RESET ).getBooleanVal() )
        {
          participant.setForcePasswordChange( Boolean.TRUE.booleanValue() );
        }
        else
        {
          participant.setForcePasswordChange( Boolean.FALSE.booleanValue() );
        }

        getRegistrationService().sendWelcomeEamiltoRosterMgmtPax( participant );
      }
      // Uncomment the below line to get the model confirmation for the participant update
      // request.getSession().setAttribute( "participantUpdated", true );
    }
    catch( ServiceErrorException e )
    {
      log.error( e );
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  // public ActionForward fetchStatesByCountry( ActionMapping mapping, ActionForm actionForm,
  // HttpServletRequest request, HttpServletResponse response ) throws IOException
  // {
  // String countryCode = request.getParameter( "addressFormBean.countryCode" );
  // StatesView beans = new StatesView();
  // if ( !StringUtils.isEmpty( countryCode ) )
  // {
  // List states = StateType.getList( countryCode );
  // NameIdBean selectOne = new NameIdBean();
  // selectOne.setCode( "" );
  // selectOne.setName( CmsResourceBundle.getCmsBundle().getString( "system.general.SELECT_ONE" ) );
  // beans.getLocations().add( selectOne );
  // for ( Object object : states )
  // {
  // StateType stateType = (StateType)object;
  // NameIdBean bean = new NameIdBean();
  // bean.setCode( stateType.getCode() );
  // bean.setName( stateType.getName() );
  // beans.getLocations().add( bean );
  // }
  // }
  // super.writeAsJsonToResponse( beans, response );
  // return null;
  // }

  private void setGeneratedPassword( UserForm form )
  {
    String password = getUserService().generatePassword();
    form.setPassword2( password );
    form.setConfirmPassword2( password );
    form.setPasswordSystemGenerated( Boolean.TRUE );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private RegistrationService getRegistrationService()
  {
    return (RegistrationService)getService( RegistrationService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  // /**
  // * Generates the user Id as per the use-case. First letter of first name + complete last name +
  // random 4 digits
  // */
  // public ActionForward generateUserName( ActionMapping mapping, ActionForm form,
  // HttpServletRequest request, HttpServletResponse response ) throws IOException
  // {
  // String firstName = request.getParameter( "firstName" );
  // String lastName = request.getParameter( "lastName" );
  //
  // super.writeAsJsonToResponse( getUserService().generateUniqueUserIdBean(firstName, lastName),
  // response );
  // return null;
  // }
  //
  // public ActionForward validateUserName( ActionMapping mapping, ActionForm form,
  // HttpServletRequest request, HttpServletResponse response ) throws IOException
  // {
  // String userName = request.getParameter( "loginID" );
  // super.writeAsJsonToResponse( getMessageValidatingUserName( userName ), response );
  // return null;
  // }
  //
  // private WebErrorMessageList getMessageValidatingUserName( String userName )
  // {
  // boolean isError = getUserService().isUserNameInUse( userName );
  //
  // WebErrorMessageList msgs = new WebErrorMessageList();
  // WebErrorMessage msg = new WebErrorMessage( (isError ? "error" : "success")
  // , (isError ? "validationError" : null)
  // , null
  // , null
  // , null
  // , null );
  // Fields field = new Fields();
  // field.setName( "userName" );
  // field.setText( isError ? CmsResourceBundle.getCmsBundle().getString(
  // ServiceErrorMessageKeys.JSON_PARTICIPANT_DUPLICATE_USER_NAME ) : null );
  // msg.getFields().add( field );
  // msgs.getMessages().add( msg );
  // return msgs;
  // }
}
