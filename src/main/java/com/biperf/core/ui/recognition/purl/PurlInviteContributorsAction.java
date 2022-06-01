
package com.biperf.core.ui.recognition.purl;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.user.ParticipantNameComparator;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.purl.impl.PurlRecipientAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.CMMessage;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.recognition.SendRecognitionForm;
import com.biperf.core.ui.recognition.state.PurlContributorBean;
import com.biperf.core.ui.recognition.state.RecipientBean;
import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PickListValueBean;
import com.biperf.core.value.PurlContributorInviteValue;
import com.biperf.core.value.PurlContributorValueBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PurlInviteContributorsAction extends BaseRecognitionAction
{
  private static final Log logger = LogFactory.getLog( PurlInviteContributorsAction.class );

  @SuppressWarnings( { "rawtypes" } )
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SendRecognitionForm sendRecognitionForm = (SendRecognitionForm)form;

    Map clientStateMap = getClientState( request );
    Long purlRecipientId = Long.valueOf( clientStateMap.get( "purlRecipientId" ).toString() );
    String purlReturnUrl = (String)clientStateMap.get( "purlReturnUrl" );

    if ( !getPurlService().isValidForInvitation( purlRecipientId ) )
    {
      return mapping.findForward( "invalid" );
    }
    else
    {
      populateSendRecognitionForm( purlRecipientId, purlReturnUrl, sendRecognitionForm );
      RecognitionStateManager.addToRequest( sendRecognitionForm, request );
      // Client customizations for WIP #26532 starts
      boolean isAllowPurlOutsideInvites = getParticipantService().isAllowePurlOutsideInvites( sendRecognitionForm.getPurlRecipient().getUser().getId() );
      if ( isAllowPurlOutsideInvites )
      {
        request.setAttribute( "allowedDomains", "" );
      }
      else
      {
        // set the system default allowed domains if the user doesn't want to invite to outsiders
        String allowedDomains = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_ACCEPTABLE_DOMAINS ).getStringVal();
        String output = "\"" + StringUtils.join( StringUtils.split( allowedDomains, "," ), "\",\"" ) + "\"";
        request.setAttribute( "allowedDomains", output );
      }
      // Client customizations for WIP #26532 ends
      return mapping.findForward( "success" );
    }
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SendRecognitionForm sendRecognitionForm = (SendRecognitionForm)form;
    Long purlRecipientId = sendRecognitionForm.getPurlRecipientId();
    String purlReturnUrl = sendRecognitionForm.getPurlReturnUrl();

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "PurlRecipientId : " + purlRecipientId );
    }

    List<PurlContributorInviteValue> inviteList = new ArrayList<PurlContributorInviteValue>();
    for ( PurlContributorBean purlContributorBean : sendRecognitionForm.getContributors() )
    {
      inviteList.add( populateInvite( purlContributorBean ) );
    }

    // Validate invite list
    List<CMMessage> errors = new ArrayList<CMMessage>();
    if ( inviteList.isEmpty() )
    {
      errors.add( new CMMessage( "purl.invitation.detail", "CONTRIBUTOR_LIST_EMPTY" ) );
    }

    if ( !errors.isEmpty() )
    {
      sendRecognitionForm.setErrors( errors );
      populateSendRecognitionForm( purlRecipientId, purlReturnUrl, sendRecognitionForm );
      RecognitionStateManager.addToRequest( sendRecognitionForm, request );
      return mapping.findForward( "failure_no_invites" );
    }
    else
    {
      inviteList = getPurlService().sendContributorInvitationByManager( purlRecipientId, inviteList, false );
      Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
      // clientStateParameterMap.put( "invites", inviteList ); to avoid buffer overflow exception
      // with request header
      for ( Iterator<PurlContributorInviteValue> iter = inviteList.iterator(); iter.hasNext(); )
      {
        PurlContributorInviteValue invite = iter.next();
        if ( invite.getFirstName().equals( UserManager.getUser().getFirstName() ) && invite.getLastName().equals( UserManager.getUser().getLastName() ) )
        {
          iter.remove();
          break;
        }
      }
      request.getSession().setAttribute( "invites", inviteList );
      clientStateParameterMap.put( "purlListUrl", purlReturnUrl );
      return ActionUtils.forwardWithParameters( mapping, "success_invites_sent", new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ) } );
    }
  }

  private PurlContributorInviteValue populateInvite( PurlContributorBean purlContributorBean )
  {
    PurlContributorInviteValue invite = new PurlContributorInviteValue();

    invite.setFirstName( purlContributorBean.getFirstName() );
    invite.setLastName( purlContributorBean.getLastName() );
    invite.setOrgName( purlContributorBean.getOrgName() );
    invite.setJobName( purlContributorBean.getJobName() );
    invite.setDepartment( purlContributorBean.getDepartmentName() );
    invite.setCountryName( purlContributorBean.getCountryName() );
    invite.setCountryCode( purlContributorBean.getCountryCode() );

    if ( StringUtils.isNotBlank( purlContributorBean.getEmail() ) )
    {
      invite.setEmailAddr( purlContributorBean.getEmail() );
    }
    else
    {
      invite.setPaxId( Long.valueOf( purlContributorBean.getId() ) );
    }

    return invite;
  }

  @SuppressWarnings( "unchecked" )
  private SendRecognitionForm populateSendRecognitionForm( Long purlRecipientId, String purlReturnUrl, SendRecognitionForm sendRecognitionForm )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_ADDRESS ) );
    arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_EMAIL ) );
    arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_NODE ) );
    arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR ) );
    arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR_USER_ADDRESS ) );
    PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( purlRecipientId, arc );

    sendRecognitionForm.setPromotionId( purlRecipient.getPromotion().getId() );
    sendRecognitionForm.setPromotionType( purlRecipient.getPromotion().getPromotionType().getCode() );
    sendRecognitionForm.setNodeId( purlRecipient.getNode().getId() );
    sendRecognitionForm.setRecipientId( purlRecipient.getUser().getId() );
    sendRecognitionForm.setPurlRecipientId( purlRecipient.getId() );
    sendRecognitionForm.setPurlRecipient( purlRecipient );
    sendRecognitionForm.setPurlReturnUrl( purlReturnUrl );
    populateRecipientInfo( purlRecipient, sendRecognitionForm );
    populateContributorInfo( purlRecipient, sendRecognitionForm );

    return sendRecognitionForm;
  }

  private void populateRecipientInfo( PurlRecipient purlRecipient, SendRecognitionForm state )
  {
    RecipientBean recipientBean = state.getClaimRecipientFormBeans( 0 );

    recipientBean.setUserId( purlRecipient.getUser().getId() );
    recipientBean.setFirstName( purlRecipient.getUser().getFirstName() );
    recipientBean.setLastName( purlRecipient.getUser().getLastName() );
    recipientBean.setCountryCode( purlRecipient.getUser().getPrimaryCountryCode() );
    recipientBean.setCountryName( purlRecipient.getUser().getPrimaryCountryName() );
    /* Client customizations for WIP #26532 starts */
    recipientBean.setPurlAllowOutsideDomains( getParticipantService().isAllowePurlOutsideInvites( purlRecipient.getUser().getId() ) );
    /* Client customizations for WIP #26532 ends */
    List<Node> childNodes = getNodeService().getNodeAndNodesBelow( purlRecipient.getNode().getId() );
    PresetSearchFiltersBean bean = new PresetSearchFiltersBean( childNodes,
                                                                CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.ADD_TEAM_MEMBERS" ),
                                                                CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.SELECT_TEAM" ) );
    ObjectMapper mapper = new ObjectMapper();
    Writer writer = new StringWriter();
    try
    {
      mapper.writeValue( writer, bean );
    }
    catch( Throwable t )
    {
      log.error( "\n\n\nERROR!!!\n\n\n" + t.getMessage() );
    }
    state.setContributorTeamsSearchFilters( writer.toString() );

    if ( purlRecipient.getUser().getPrimaryEmailAddress() != null )
    {
      recipientBean.setEmailAddr( purlRecipient.getUser().getPrimaryEmailAddress().getEmailAddr() );
    }
  }

  private void populateContributorInfo( PurlRecipient purlRecipient, SendRecognitionForm state )
  {
    List<PurlContributorBean> contributorInfo = null;

    if ( purlRecipient.getContributorInvited() > 0 )
    {
      state.setPreselectedContributorsLocked( true );
      contributorInfo = getExistingContributorBeans( purlRecipient, state );
    }
    else
    {
      contributorInfo = getPreSelectedContributorBeans( purlRecipient, state );
    }

    if ( purlRecipient.getContributorInvited() > 0 && contributorsAddedByManager( UserManager.getUserId(), purlRecipient.getId() ) == 0 )
    {
      contributorInfo.addAll( getPreSelectedContributorBeans( purlRecipient, state ) );
    }

    int recipientCount = getPurlService().getPurlRecipientsCountForAutoInvite( purlRecipient.getPromotion().getId(), purlRecipient.getId() );

    if ( recipientCount == 1 )
    {
      state.setPreselectedContributorsLocked( false );
    }

    state.populateContributorMap( contributorInfo );
  }

  private int contributorsAddedByManager( Long managerId, Long purlRecipientId )
  {
    List<PurlContributor> addedByManager = null;
    addedByManager = getPurlService().getPurlContributors( managerId, purlRecipientId );
    return addedByManager != null && addedByManager.size() > 0 ? addedByManager.size() : 0;
  }

  private List<PurlContributorBean> getExistingContributorBeans( PurlRecipient purlRecipient, SendRecognitionForm state )
  {
    List<PurlContributorValueBean> purlContributorValueBeanList = getParticipantService().getAllExistingContributors( purlRecipient.getUser().getId() );

    List<PurlContributorBean> purlContributorBeanList = new ArrayList<PurlContributorBean>();
    PurlContributorBean purlContributorBean = null;

    for ( PurlContributorValueBean purlContributorValueBean : purlContributorValueBeanList )
    {
      purlContributorBean = new PurlContributorBean();
      Boolean contribType = purlContributorValueBean.getContribType().equals( "other" );

      if ( contribType && purlContributorValueBean.getEmail() != null || !contribType )
      {
        purlContributorBean.setId( purlContributorValueBean.getId() );
        purlContributorBean.setCountryCode( purlContributorValueBean.getCountryCode() );
        purlContributorBean.setCountryName( purlContributorValueBean.getCountryName() );
        purlContributorBean.setFirstName( purlContributorValueBean.getFirstName() );
        purlContributorBean.setLastName( purlContributorValueBean.getLastName() );
        purlContributorBean.setEmail( purlContributorValueBean.getEmail() );
        purlContributorBean.setOrgName( purlContributorValueBean.getOrgName() );
        purlContributorBean.setDepartmentName( purlContributorValueBean.getDepartmentName() );
        purlContributorBean.setJobName( purlContributorValueBean.getJobName() );
        purlContributorBean.setContribType( purlContributorValueBean.getContribType() );
        purlContributorBean.setSourceType( purlContributorValueBean.getSourceType() );
        purlContributorBean.setInvitationSentDate( purlContributorValueBean.getInvitationSentDate() );

        purlContributorBeanList.add( purlContributorBean );
      }
    }

    return purlContributorBeanList;
  }

  @SuppressWarnings( "unchecked" )
  private List<PurlContributorBean> getPreSelectedContributorBeans( PurlRecipient purlRecipient, SendRecognitionForm state )
  {
    List<Participant> preSelectedContributors = getParticipantService().getAllPreSelectedContributors( purlRecipient.getUser().getId() );

    Collections.sort( preSelectedContributors, new ParticipantNameComparator() );

    List<PurlContributorBean> purlContributorBeans = new ArrayList<PurlContributorBean>();
    for ( Participant participant : preSelectedContributors )
    {
      PurlContributorBean contributorBean = createPurlContributorBean( participant, "preselected" );
      purlContributorBeans.add( contributorBean );
    }

    return purlContributorBeans;
  }

  private PurlContributorBean createPurlContributorBean( User user, String contributionType )
  {
    PurlContributorBean contributorBean = new PurlContributorBean();
    contributorBean.setContribType( contributionType );

    if ( user != null )
    {
      contributorBean.setId( user.getId().toString() );
      contributorBean.setFirstName( user.getFirstName() );
      contributorBean.setLastName( user.getLastName() );
      contributorBean.setCountryName( user.getPrimaryCountryName() );
      contributorBean.setCountryCode( user.getPrimaryCountryCode() );
      for ( Object obj : user.getUserNodes() )
      {
        // check if it is primary node
        if ( obj instanceof UserNode && ( (UserNode)obj ).getIsPrimary() )
        {
          contributorBean.setOrgName( ( (UserNode)obj ).getNode().getName() );
          break;
        }
      }

      if ( user instanceof Participant )
      {
        Participant pax = (Participant)user;
        PickListValueBean pickListDeptValueBean = getUserService()
            .getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                         pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                         pax.getDepartmentType() );
        PickListValueBean pickListPositionValueBean = getUserService()
            .getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                         pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                         pax.getPositionType() );
        if ( !StringUtils.isEmpty( pax.getDepartmentType() ) )
        {
          contributorBean.setDepartmentName( pickListDeptValueBean.getName() );
        }
        if ( !StringUtils.isEmpty( pax.getPositionType() ) )
        {
          contributorBean.setJobName( pickListPositionValueBean.getName() );
        }
        if ( pax.getSourceType() != null )
        {
          contributorBean.setSourceType( pax.getSourceType() );
        }
      }
    }

    return contributorBean;
  }

  @SuppressWarnings( "rawtypes" )
  private Map getClientState( HttpServletRequest request ) throws InvalidClientStateException
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    return ClientStateSerializer.deserialize( clientState, password );
  }
}
