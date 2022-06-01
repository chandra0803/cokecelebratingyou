/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/proxy/ProxyDetailAction.java,v $ */

package com.biperf.core.ui.proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyUsersView;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.proxy.ProxyAssociationRequest;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.proxy.ProxyUpdateAssociation;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;

/**
 * Action class for Proxy Detail operations.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyDetailAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( ProxyDetailAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    Proxy proxy = null;
    Participant participant = null;
    Long userId = null;
    Long proxyId = null;

    ProxyDetailForm proxyDetailForm = (ProxyDetailForm)form;

    proxyDetailForm.setMethod( "save" );

    if ( !StringUtils.isEmpty( proxyDetailForm.getMainUserId() ) )
    {
      userId = new Long( proxyDetailForm.getMainUserId() );
    }
    else if ( userId == null || userId.longValue() == 0 )
    { // BugFix 18747..Try To get the userId from Client State..
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
        String mainUsrId = (String)clientStateMap.get( "mainUserId" );
        if ( mainUsrId != null )
        {
          userId = new Long( mainUsrId );
        }
      }
      catch( InvalidClientStateException e )
      {
        // do nothing as this is an optional parameter
      }
    }
    else
    {
      userId = UserManager.getUserId();
    }

    if ( !StringUtils.isEmpty( proxyDetailForm.getProxyId() ) )
    {
      proxyId = new Long( proxyDetailForm.getProxyId() );
    }

    if ( proxyId != null && proxyId.longValue() != 0 )
    {
      AssociationRequestCollection proxyAssociationRequestCollection = new AssociationRequestCollection();
      proxyAssociationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.ALL ) );

      proxy = getProxyService().getProxyByIdWithAssociations( proxyId, proxyAssociationRequestCollection );
    }

    if ( userId != null && userId.longValue() != 0 )
    {
      AssociationRequestCollection userAssociationRequestCollection = new AssociationRequestCollection();

      userAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );

      participant = getParticipantService().getParticipantByIdWithAssociations( userId, userAssociationRequestCollection );
      // BugFix 17752 Pax Employers has not hydrated in the above service call so use this method.
      participant = getParticipantService().getParticipantOverviewById( participant.getId() );

    }

    List recognitionPromoList = getRecognitionPromos( participant );
    List productClaimPromoList = getProductClaimPromos( participant );
    List nominationPromoList = getNominationPromos( participant );

    proxyDetailForm.load( proxy, participant, recognitionPromoList, productClaimPromoList, nominationPromoList );
    return mapping.findForward( forwardTo );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException 
   */
  public ActionForward save( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ProxyDetailForm proxyDetailForm = (ProxyDetailForm)actionForm;
    Long mainUserId = UserManager.getUserId();
    String proxyUserId = null;
    Long proxyId = null;
    ProxyUsersView bean = null;
    Proxy proxy = null;
    try
    {
      if ( !StringUtils.isEmpty( proxyDetailForm.getProxyId() ) )
      {
        proxyId = new Long( proxyDetailForm.getProxyId() );
        proxyDetailForm.setProxyId( proxyId.toString() );
      }
      if ( !StringUtils.isEmpty( proxyDetailForm.getId() ) )
      {
        proxyUserId = proxyDetailForm.getId();
        proxyDetailForm.setProxyUserId( proxyUserId );
        proxyDetailForm.setMainUserId( mainUserId.toString() );
      }
      ProxyUpdateAssociation proxyUpdateAssociation = new ProxyUpdateAssociation( proxyDetailForm.toDomainObject() );
      proxy = getProxyService().saveProxy( proxyId, proxyUpdateAssociation );
    }
    catch( ServiceErrorException se )
    {

    }
    bean = getProxyService().getCreatedProxyView( proxy );
    super.writeAsJsonToResponse( bean, response );
    return null;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException 
   */
  public ActionForward saveAdmin( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    final String METHOD_NAME = "save";
    Long userId = null;
    Long dupProxyCount = new Long( 0 );
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }
    ActionMessages errors = new ActionMessages();
    ProxyDetailForm proxyDetailForm = (ProxyDetailForm)actionForm;
    String forwardAction = "";
    Proxy proxy = null;
    try
    {
      Long proxyId = null;
      if ( !StringUtils.isEmpty( proxyDetailForm.getProxyId() ) )
      {
        proxyId = new Long( proxyDetailForm.getProxyId() );
      }
      if ( !StringUtils.isEmpty( proxyDetailForm.getMainUserId() ) )
      {
        userId = new Long( proxyDetailForm.getMainUserId() );
      }
      else if ( userId == null || userId.longValue() == 0 )
      {
        String mainUsrId = getMainUserIdFromRequest( request );
        if ( !StringUtils.isEmpty( mainUsrId ) )
        {
          userId = new Long( mainUsrId );
        }
      }
      if ( userId == null || userId.longValue() == 0 )
      {
        userId = UserManager.getUserId();
      }

      if ( UserManager.getUserId().equals( new Long( proxyDetailForm.getProxyUserId() ) ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "proxy.detail.errors.PROXY_ADD_SELF" ) );
      }

      ProxyUpdateAssociation proxyUpdateAssociation = new ProxyUpdateAssociation( proxyDetailForm.toDomainObject() );
      // BugFix 17784, validate against duplicate Proxy names of an pax.
      if ( proxyId == null )
      {
        dupProxyCount = getProxyService().getDuplicateProxyUserCount( userId, new Long( proxyDetailForm.getProxyUserId() ) );
      }
      if ( dupProxyCount.longValue() > 0 )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "proxy.detail.errors.DUPLICATE_PROXY_ERROR" ) );
      }
      else
      {
        proxy = getProxyService().saveProxy( proxyId, proxyUpdateAssociation );
      }
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      forwardAction = ActionConstants.SUCCESS_FORWARD;
    }
    Map clientStateParameterMap = new HashMap();
    String queryString = null;
    if ( proxy != null && proxy.getUser() != null )
    {
      clientStateParameterMap.put( "userId", proxy.getUser().getId() );
    }
    queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    queryString += "&includeCancel=" + proxyDetailForm.isShowCancel();
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString } );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   */
  public ActionForward prepareNew( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ProxyDetailForm proxyDetailForm = (ProxyDetailForm)form;
    Participant participant = getParticipant( UserManager.getUserId() );
    proxyDetailForm.load( null, participant, getRecognitionPromos( participant ), getProductClaimPromos( participant ), getNominationPromos( participant ) );
    request.setAttribute( "proxyDetailForm", proxyDetailForm );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   */
  public ActionForward prepareEdit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ProxyDetailForm proxyDetailForm = (ProxyDetailForm)form;
    Participant proxyParticipant = null;
    Proxy proxy = null;
    if ( StringUtil.isEmpty( proxyDetailForm.getProxyId() ) )
    {
      proxyParticipant = getParticipant( Long.parseLong( proxyDetailForm.getId() ) );
      proxy = getProxyByProxyUser( proxyParticipant.getId() );
    }
    else
    {
      proxy = getProxyById( Long.parseLong( proxyDetailForm.getProxyId() ) );
      proxyParticipant = proxy.getProxyUser();
    }
    proxyDetailForm.load( proxy, proxyParticipant, getRecognitionPromos( proxy.getUser() ), getProductClaimPromos( proxy.getUser() ), getNominationPromos( proxy.getUser() ) );
    request.setAttribute( "proxyDetailForm", proxyDetailForm );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   */
  public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String delegateUserId = request.getParameter( "participantId" );
    if ( !StringUtils.isEmpty( delegateUserId ) )
    {
      Proxy proxy = getProxyService().getProxyByUserAndProxyUser( UserManager.getUserId(), Long.parseLong( delegateUserId ) );
      if ( proxy != null )
      {
        try
        {
          getProxyService().deleteProxy( proxy.getId() );
        }
        catch( ServiceErrorException e )
        {
          logger.error( "<<<<<<<<<<Error>>>>>>>>>>" + e );
        }
      }
    }
    ProxyUsersView bean = new ProxyUsersView();
    super.writeAsJsonToResponse( bean, response );
    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   */
  public ActionForward fetchDelegates( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    AssociationRequestCollection associationRequestCollection = getAssociationRequestCollection();
    List<Proxy> proxies = getProxyService().getProxiesByUserIdWithAssociation( UserManager.getUserId(), associationRequestCollection );
    ProxyUsersView bean = new ProxyUsersView( proxies );
    super.writeAsJsonToResponse( bean, response );
    return null;
  }

  private Participant getParticipant( Long participantId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    return getParticipantService().getParticipantByIdWithAssociations( participantId, associationRequestCollection );
  }

  private Proxy getProxyByProxyUser( Long proxyParticipantId )
  {
    AssociationRequestCollection associationRequestCollection = getAssociationRequestCollection();
    Proxy proxy = getProxyService().getProxyByUserAndProxyUserWithAssociations( UserManager.getUserId(), proxyParticipantId, associationRequestCollection );
    return proxy;
  }

  private Proxy getProxyById( Long proxyId )
  {
    AssociationRequestCollection associationRequestCollection = getAssociationRequestCollection();
    Proxy proxy = getProxyService().getProxyByIdWithAssociations( proxyId, associationRequestCollection );
    return proxy;
  }

  private AssociationRequestCollection getAssociationRequestCollection()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_MODULE ) );
    associationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_MODULE_PROMOTION ) );
    associationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_USER_ADDRESS ) );
    associationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_USER_EMPLOYER_INFO ) );
    return associationRequestCollection;
  }

  private List getRecognitionPromos( Participant participant )
  {
    return getProxyService().getRecognitionPromotionsByPax( participant );
  }

  private List getProductClaimPromos( Participant participant )
  {
    return getProxyService().getProductClaimPromotionsByPax( participant );
  }

  private List getNominationPromos( Participant participant )
  {
    return getProxyService().getNominationPromotionsByPax( participant );
  }

  private String getMainUserIdFromRequest( HttpServletRequest request )
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    Map clientStateMap = new HashMap();
    try
    {
      clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    }
    catch( InvalidClientStateException e )
    {
      logger.error( "<<<<<ERROR>>>>>" + e );
    }
    return (String)clientStateMap.get( "mainUserId" );
  }

  private ProxyService getProxyService()
  {
    return (ProxyService)getService( ProxyService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
