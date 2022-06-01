
package com.biperf.core.ui.promotion;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.ssi.SSIPromotionContestApprovalAudienceUpdateAssociation;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * 
 * PromotionSSIApprovalAction.
 * 
 * @author chowdhur
 * @since Nov 3, 2014
 */
public class PromotionSSIApprovalAction extends PromotionBaseDispatchAction
{

  private static final String PROMOTION_ERRORS_UNIQUE_CONSTRAINT = "promotion.errors.UNIQUE_CONSTRAINT";

  private static final Log logger = LogFactory.getLog( PromotionSSIApprovalAction.class );

  private static String RETURN_ACTION_URL_PARAM = "returnActionUrl";
  public static String SESSION_CONTEST_APPROVAL_AUDIENCE_FORM = "sessionContestApprovalAudienceForm";

  /**
  * Overridden from
  * 
  * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
  *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
  *      javax.servlet.http.HttpServletResponse)
  * @param mapping
  * @param actionForm
  * @param request
  * @param response
  * @return ActionForward
  */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;

    Promotion promotion = null;
    promotionSSIApprovalForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    // WIZARD MODE
    if ( isWizardMode( request ) )
    {
      promotion = getWizardPromotion( request );
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_1_AUDIENCE ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_2_AUDIENCE ) );
      promotion = getPromotionService().getPromotionByIdWithAssociations( promotionSSIApprovalForm.getPromotionId(), promoAssociationRequestCollection );
    }
    else
    {
      Long promotionId = promotionSSIApprovalForm.getPromotionId();

      if ( promotionId != null )
      {
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_1_AUDIENCE ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_2_AUDIENCE ) );
        promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
      }
      else
      {
        throw new IllegalArgumentException( "promotionId is null" );
      }
    }
    if ( promotion != null )
    {
      promotionSSIApprovalForm.load( promotion );
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.WIZARD_SAVE_AND_CONTINUE_ATTRIBUTE );
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;
    ActionMessages errors = new ActionMessages();
    Promotion promotion = null;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        forward = getCancelForward( mapping, request );
      }
      return forward;
    }

    // WIZARD MODE
    if ( isWizardMode( request ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_1_AUDIENCE ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_2_AUDIENCE ) );
      promotion = getPromotionService().getPromotionByIdWithAssociations( promotionSSIApprovalForm.getPromotionId(), promoAssociationRequestCollection );
    }
    try
    {
      SSIPromotion ssiPromotion = (SSIPromotion)promotion;
      ssiPromotion = promotionSSIApprovalForm.toDomainObject( ssiPromotion );
      Long promotionId = promotionSSIApprovalForm.getPromotionId();

      if ( promotionId != null && promotionId > 0 )
      {
        SSIPromotionContestApprovalAudienceUpdateAssociation ssiPromotionContestApprovalAudienceUpdateAssociation = new SSIPromotionContestApprovalAudienceUpdateAssociation( ssiPromotion );
        promotion = getPromotionService().savePromotion( promotionId, ssiPromotionContestApprovalAudienceUpdateAssociation );
      }
      else
      {
        promotion = getPromotionService().savePromotion( ssiPromotion );
      }
    }
    catch( ConstraintViolationException e )
    {
      logger.error( e.getMessage(), e );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( PROMOTION_ERRORS_UNIQUE_CONSTRAINT ) );
    }
    catch( UniqueConstraintViolationException e )
    {
      logger.error( e.getMessage(), e );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( PROMOTION_ERRORS_UNIQUE_CONSTRAINT ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      if ( isWizardMode( request ) )
      {
        setPromotionInWizardManager( request, promotion );

        forward = getWizardNextPage( mapping, request, promotion );
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }
    return forward;
  }

  /**
   * display participant list popup
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayPaxListPopup( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    Long audienceId = null;
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
      try
      {
        audienceId = new Long( (String)clientStateMap.get( "audienceId" ) );
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

      if ( audienceId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "audienceId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( "paxListPopup" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Audience audience = getAudienceService().getAudienceById( audienceId, null );
    List paxFormattedValueList = getPaxsInCriteriaAudience( audience );

    request.setAttribute( "paxFormattedValueList", paxFormattedValueList );
    request.setAttribute( "paxFormattedValueListSize", String.valueOf( paxFormattedValueList.size() ) );
    return mapping.findForward( "paxListPopup" );
  }

  /**
   * removes any Claim Approval audiences selected
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward removeContestApprovalLvl1Audience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;
    // Remove audiences from the secondary audience list.
    promotionSSIApprovalForm.removeApprovalLvl1Items();
    return forward;
  }

  /**
   * removes any Claim Approval audiences selected
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward removeContestApprovalLvl2Audience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;
    // Remove audiences from the secondary audience list.
    promotionSSIApprovalForm.removeApprovalLvl2Items();
    return forward;
  }

  // TODO: Check
  /**
   * Makes a request to the Audience builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareContestApprovalLvl1AudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;

    Long promotionId = promotionSSIApprovalForm.getPromotionId();

    ActionForward returnForward = mapping.findForward( "contestApprovalLvl1AudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );
    Map parmMap = new HashMap();
    parmMap.put( "saveAudienceReturnUrl", returnUrl );
    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;
    queryString2 = ClientStateUtils.generateEncodedLink( "", "", parmMap );
    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_CONTEST_APPROVAL_AUDIENCE_FORM, promotionSSIApprovalForm );

    return ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );
  }

  /**
   * Handles the return from the audience builder. This will look for the AudienceId on the request,
   * load the audience and the promotion and build a new PromotionWebRulesAudience which is set onto
   * the form in preparation to saving the webRules.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */

  public ActionForward returnContestApprovalLvl1AudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionSSIApprovalForm sessionPromotionSSIApprovalForm = (PromotionSSIApprovalForm)request.getSession().getAttribute( SESSION_CONTEST_APPROVAL_AUDIENCE_FORM );

    if ( sessionPromotionSSIApprovalForm != null )
    {
      String formAudienceType = PromotionAudienceForm.PRIMARY;
      extractAndAddLvl1PromotionAudience( request, promotionSSIApprovalForm, sessionPromotionSSIApprovalForm, formAudienceType );
    }

    request.getSession().removeAttribute( SESSION_CONTEST_APPROVAL_AUDIENCE_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void extractAndAddLvl1PromotionAudience( HttpServletRequest request,
                                                   PromotionSSIApprovalForm promotionSSIApprovalForm,
                                                   PromotionSSIApprovalForm sessionPromoAudienceForm,
                                                   String formAudienceType )
  {
    try
    {
      BeanUtils.copyProperties( promotionSSIApprovalForm, sessionPromoAudienceForm );
    }
    catch( Exception e )
    {
      logger.info( "returnCategoryLookup: Copy Properties failed." );
    }
    Long audienceId = null;
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
      audienceId = (Long)clientStateMap.get( "audienceId" );
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since this is an option parameter
    }

    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addContestApprovalLvl1Audience( promotionSSIApprovalForm, request, formAudienceType, audienceId );
    }
  }

  private void addContestApprovalLvl1Audience( PromotionSSIApprovalForm form, HttpServletRequest request, String promoAudienceType, Long audienceId )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    if ( AudienceType.SPECIFIC_PAX_TYPE.equals( form.getApprovalLvl1AudienceType() ) || audience instanceof CriteriaAudience )
    {
      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      Long primaryHierarchyId = primaryHierarchy.getId();

      Set audiences = new LinkedHashSet();
      audiences.add( audience );
      List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );
      audience.setSize( paxFormattedValueList.size() );
    }

    // add the audience to the list
    form.addLvl1Audience( audience );
  }

  /**
   * Makes a request to the Audience builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareContestApprovalLvl2AudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;

    Long promotionId = promotionSSIApprovalForm.getPromotionId();

    ActionForward returnForward = mapping.findForward( "contestApprovalLvl2AudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );
    Map parmMap = new HashMap();
    parmMap.put( "saveAudienceReturnUrl", returnUrl );
    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;
    queryString2 = ClientStateUtils.generateEncodedLink( "", "", parmMap );
    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_CONTEST_APPROVAL_AUDIENCE_FORM, promotionSSIApprovalForm );

    return ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );
  }

  /**
   * Handles the return from the audience builder. This will look for the AudienceId on the request,
   * load the audience and the promotion and build a new PromotionWebRulesAudience which is set onto
   * the form in preparation to saving the webRules.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */

  public ActionForward returnContestApprovalLvl2AudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionSSIApprovalForm sessionPromotionSSIApprovalForm = (PromotionSSIApprovalForm)request.getSession().getAttribute( SESSION_CONTEST_APPROVAL_AUDIENCE_FORM );

    if ( sessionPromotionSSIApprovalForm != null )
    {
      String formAudienceType = PromotionAudienceForm.PRIMARY;
      extractAndAddLvl2PromotionAudience( request, promotionSSIApprovalForm, sessionPromotionSSIApprovalForm, formAudienceType );
    }

    request.getSession().removeAttribute( SESSION_CONTEST_APPROVAL_AUDIENCE_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void extractAndAddLvl2PromotionAudience( HttpServletRequest request,
                                                   PromotionSSIApprovalForm promotionSSIApprovalForm,
                                                   PromotionSSIApprovalForm sessionPromoAudienceForm,
                                                   String formAudienceType )
  {
    try
    {
      BeanUtils.copyProperties( promotionSSIApprovalForm, sessionPromoAudienceForm );
    }
    catch( Exception e )
    {
      logger.info( "returnCategoryLookup: Copy Properties failed." );
    }
    Long audienceId = null;
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
      audienceId = (Long)clientStateMap.get( "audienceId" );
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since this is an option parameter
    }

    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addContestApprovalLvl2Audience( promotionSSIApprovalForm, request, formAudienceType, audienceId );
    }
  }

  private void addContestApprovalLvl2Audience( PromotionSSIApprovalForm form, HttpServletRequest request, String promoAudienceType, Long audienceId )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    if ( AudienceType.SPECIFIC_PAX_TYPE.equals( form.getApprovalLvl1AudienceType() ) || audience instanceof CriteriaAudience )
    {
      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      Long primaryHierarchyId = primaryHierarchy.getId();

      Set audiences = new LinkedHashSet();
      audiences.add( audience );
      List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );
      audience.setSize( paxFormattedValueList.size() );
    }

    // add the audience to the list
    form.addLvl2Audience( audience );
  }

  /**
   * add team audience
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addContestApprovalLvl1Audience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;

    // If there was no audience selected, then return an error
    if ( promotionSSIApprovalForm.getApprovalLvl1AudienceId() == null || promotionSSIApprovalForm.getApprovalLvl1AudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    addPromotionLvl1Audience( promotionSSIApprovalForm, request, new Long( promotionSSIApprovalForm.getApprovalLvl1AudienceId() ) );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * add team audience
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addContestApprovalLvl2Audience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)form;

    // If there was no audience selected, then return an error
    if ( promotionSSIApprovalForm.getApprovalLvl2AudienceId() == null || promotionSSIApprovalForm.getApprovalLvl2AudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    addPromotionLvl2Audience( promotionSSIApprovalForm, request, new Long( promotionSSIApprovalForm.getApprovalLvl2AudienceId() ) );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void addPromotionLvl1Audience( PromotionSSIApprovalForm form, HttpServletRequest request, Long audienceId )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    // add the audience to the list
    form.addLvl1Audience( audience );
  }

  private void addPromotionLvl2Audience( PromotionSSIApprovalForm form, HttpServletRequest request, Long audienceId )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    // add the audience to the list
    form.addLvl2Audience( audience );
  }

  private List getPaxsInCriteriaAudience( Audience audience )
  {
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    return getListBuilderService().searchParticipants( audiences, primaryHierarchyId, false, null, true );
  }

  /**
   * Gets an AudienceService
   * 
   * @return AudienceService
   */
  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  /**
   * Gets a HierarchyService
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }
}
