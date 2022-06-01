/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ParticipantEmploymentAction.java,v $
 *
 */

package com.biperf.core.ui.participant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.impl.PurlContributorAssociationRequest;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ParticipantEmploymentAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ParticipantEmploymentAction extends BaseDispatchAction
{
  /**
   * Displays a list of all User Addresses
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayList( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantEmploymentListForm participantEmployerListForm = (ParticipantEmploymentListForm)actionForm;

    // Put the user id onto the form

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      Long userId = null;
      try
      {
        userId = (Long)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        userId = new Long( (String)clientStateMap.get( "userId" ) );
      }
      if ( userId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
      participantEmployerListForm.setUserId( userId.longValue() );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  } // end displayList

  /**
   * prepare the ParticipantEmployment create page.
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantEmploymentForm participantEmploymentForm = (ParticipantEmploymentForm)actionForm;
    ParticipantEmployer participantEmployer = getParticipantService().getCurrentParticipantEmployer( new Long( participantEmploymentForm.getUserId() ) );
    if ( participantEmployer != null )
    {
      participantEmploymentForm.setPreviousTerminationDate( DateUtils.toDisplayString( participantEmployer.getTerminationDate() ) );
    }
    return actionMapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * prepare the ParticipantEmployment update page.
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantEmploymentForm participantEmploymentForm = (ParticipantEmploymentForm)actionForm;
    ParticipantEmployer participantEmployer = getParticipantService().getCurrentParticipantEmployer( new Long( participantEmploymentForm.getUserId() ) );
    participantEmploymentForm.load( participantEmployer );

    participantEmploymentForm.setMethod( "update" );

    return actionMapping.findForward( ActionConstants.EDIT_FORWARD );
  }

  /**
   * update the ParticipantEmployment record.
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    ParticipantEmploymentForm participantEmploymentForm = (ParticipantEmploymentForm)actionForm;
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant participant = getParticipantService().getParticipantByIdWithAssociations( new Long( participantEmploymentForm.getUserId() ), associationRequestCollection );

    ParticipantEmployer participantEmployer = participantEmploymentForm.toDomainObject();

    List employerinfo = participant.getParticipantEmployers();
    // for ( Iterator iter = employerinfo.iterator(); iter.hasNext(); )
    employerinfo.forEach( ( paxEmployer ) ->
    {
      if ( participantEmployer.getTerminationDate() != null )
      {
        List<PurlRecipient> purlRecipients = getPurlService().getPurlRecipientByUserId( participant.getId() );
        if ( null != purlRecipients && !purlRecipients.isEmpty() )
        {
          purlRecipients.forEach( ( purlRecipient ) ->
          {
            AssociationRequestCollection associationRequestCol = new AssociationRequestCollection();
            associationRequestCol.add( new PurlContributorAssociationRequest( PurlContributorAssociationRequest.PURL_RECIPIENT_CONTRIBUTORS ) );
            List<PurlContributor> pulrContributors = getPurlService().getAllPurlContributions( purlRecipient.getId(), associationRequestCol );

            // for ( PurlContributor purlContributor : pulrContributors )
            pulrContributors.forEach( ( purlContributor ) ->
            {
              purlContributor.setState( PurlContributorState.lookup( PurlContributorState.EXPIRED ) );
              getPurlService().savePurlContributor( purlContributor );
            } );
            purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.EXPIRED ) );
            getPurlService().savePurlRecipient( purlRecipient );
          } );
        }
      }
    } );

    try
    {
      getParticipantService().saveParticipantEmployer( new Long( participantEmploymentForm.getUserId() ), participantEmployer );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( participantEmploymentForm.getUserId() );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", new Long( participantEmploymentForm.getUserId() ) );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString, "method=displayList" } );
  }

  /**
   * create the ParticipantEmployment record.
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    ParticipantEmploymentForm participantEmploymentForm = (ParticipantEmploymentForm)actionForm;
    ParticipantEmployer participantEmployer = participantEmploymentForm.toDomainObject();

    ParticipantEmployer participantEmployerCurrent = getParticipantService().getCurrentParticipantEmployer( new Long( participantEmploymentForm.getUserId() ) );

    if ( participantEmployerCurrent != null )
    {
      if ( participantEmploymentForm.getPreviousTerminationDate() == null || participantEmploymentForm.getPreviousTerminationDate().length() == 0 )
      {
        errors.add( "PreviousTerminationDate", new ActionMessage( "system.errors.REQUIRED", "Previous Employment Termination Date" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
      }
      else if ( participantEmployerCurrent.getHireDate() != null && participantEmployerCurrent.getHireDate().after( DateUtils.toDate( participantEmploymentForm.getPreviousTerminationDate() ) ) )
      {
        errors.add( "PreviousTerminationDate", new ActionMessage( "participant.errors.TERMINATION_AFTER_HIRE" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
      } // EARLY EXIT
    }

    try
    {
      getParticipantService().saveParticipantEmployer( new Long( participantEmploymentForm.getUserId() ),
                                                       participantEmployer,
                                                       DateUtils.toDate( participantEmploymentForm.getPreviousTerminationDate() ) );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( participantEmploymentForm.getUserId() );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", new Long( participantEmploymentForm.getUserId() ) );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString, "method=displayList" } );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }
}
