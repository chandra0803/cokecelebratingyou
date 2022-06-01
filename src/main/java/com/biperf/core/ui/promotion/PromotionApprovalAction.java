/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionApprovalAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.PromotionApprovalParticipantBean;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionParticipantApprover;
import com.biperf.core.domain.promotion.PromotionParticipantSubmitter;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromotionApprovalUpdateAssociation;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.node.NodeSearchAction;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.CustomApproverValueBean;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.UserValueBean;
import com.biperf.core.value.nomination.NominationApproverValueBean;

/**
 * PromotionApprovalAction.
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
 * <td>wadzinsk</td>
 * <td>Jul 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovalAction extends PromotionBaseDispatchAction
{
  private static final String SESSION_PROMO_APPROVAL_FORM = "SESSION_PROMO_APPROVAL_FORM";

  private static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

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

    PromotionApprovalForm promoApprovalForm = (PromotionApprovalForm)form;

    Promotion promotion = null;

    promoApprovalForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );

      // load all the approval participants
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_PARTICIPANTS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CUSTOM_APPROVER_OPTIONS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOM_DEFAULT_APPROVER ) );
      Promotion attachedPromotion = getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );

      // update the promotion object with the approval options
      promotion.setPromotionApprovalOptions( attachedPromotion.getPromotionApprovalOptions() );

      // update the promotion object with the approval participants
      promotion.setPromotionParticipantApprovers( attachedPromotion.getPromotionParticipantApprovers() );
      promotion.setPromotionParticipantSubmitters( attachedPromotion.getPromotionParticipantSubmitters() );
      // Prevent null pointer error in the form on customApproverOptions during save method
      setPromotionInWizardManager( request, attachedPromotion );

    }
    else
    {
      String promotionId = promoApprovalForm.getPromotionId();

      if ( promotionId != null && promotionId.length() > 0 )
      {
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_PARTICIPANTS ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CUSTOM_APPROVER_OPTIONS ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOM_DEFAULT_APPROVER ) );

        promotion = getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), promoAssociationRequestCollection );
      }
      else
      {
        throw new IllegalArgumentException( "promotionId is null" );
      }
    }

    promoApprovalForm.load( promotion );
    characteristicListForEachLevel( promoApprovalForm, true );
    populateNomApproverList( promoApprovalForm );
    return mapping.findForward( forwardTo );
  }

  /**
   * Used for approver type and approval type drop-downs.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward redisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionApprovalForm promoApprovalForm = (PromotionApprovalForm)form;
    if ( promoApprovalForm.getApprovalType() != null && !promoApprovalForm.getApprovalType().equals( ApprovalType.lookup( ApprovalType.CONDITIONAL_PAX_BASED ).getCode() ) )
    {
      promoApprovalForm.setParticipantSubmitterList( null );
    }
    if( (promoApprovalForm.getApproverType()!= null && !promoApprovalForm.getApproverType().equals( "specific_approv" )) || 
            (!promoApprovalForm.getApprovalType().equals( ApprovalType.lookup( ApprovalType.CONDITIONAL_PAX_BASED ).getCode() )
            && !promoApprovalForm.getApprovalType().equals( ApprovalType.lookup( ApprovalType.AUTOMATIC_DELAYED ).getCode() )
            && !promoApprovalForm.getApprovalType().equals( ApprovalType.lookup( ApprovalType.CONDITIONAL_NTH_BASED ).getCode() )
            && !promoApprovalForm.getApprovalType().equals( ApprovalType.lookup( ApprovalType.MANUAL ).getCode() )
            && !promoApprovalForm.getApprovalType().equals( ApprovalType.lookup( ApprovalType.COKE_CUSTOM ).getCode() )////customization wip 42702
            ) )
    {
      promoApprovalForm.setParticipantApproverList( null );
    }

    if ( promoApprovalForm.getApprovalNodeLevels() != null && !promoApprovalForm.getApprovalNodeLevels().isEmpty() )
    {
      ActionMessages errors = new ActionMessages();
      int level = 0;
      try
      {
        level = Integer.parseInt( promoApprovalForm.getApprovalNodeLevels() );
      }
      catch( NumberFormatException e )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.approvals.errors.APPROVAL_LEVEL_INVALID" ) );
        saveErrors( request, errors );
        promoApprovalForm.getCustomApproverValueBeanListAsList().clear();
      }
      if ( level <= 0 || level > 5 )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.approvals.errors.CUSTOM_INVALID_RANGE" ) );
        saveErrors( request, errors );
        promoApprovalForm.getCustomApproverValueBeanListAsList().clear();
      }
      else if ( promoApprovalForm.getCustomApproverValueBeanListAsList() == null || promoApprovalForm.getCustomApproverValueBeanListAsList().size() == 0
          || promoApprovalForm.getCustomApproverValueBeanListAsList().size() != level )
      {
        promoApprovalForm.getCustomApproverValueBeanListAsList().clear();
        promoApprovalForm.setCustomApproverValueBeanListAsList( promoApprovalForm.getEmptyValueList( level ) );
      }

      if ( promoApprovalForm.getApprovalNodeLevels() != null && !promoApprovalForm.getApprovalNodeLevels().isEmpty() )
      {
        for ( Iterator<CustomApproverValueBean> iter = promoApprovalForm.getCustomApproverValueBeanListAsList().iterator(); iter.hasNext(); )
        {
          CustomApproverValueBean bean = iter.next();
          bean.getCharacteristics().clear();
          bean.getCharacteristics().addAll( promoApprovalForm.getCharacteristics() );
        }
      }
      if ( promoApprovalForm.getCustomApproverTypes() != null && !promoApprovalForm.isBehaviorActive() )
      {
        for ( Iterator<CustomApproverType> iter = promoApprovalForm.getCustomApproverTypes().listIterator(); iter.hasNext(); )
        {
          CustomApproverType customApproverType = iter.next();
          if ( "behavior".equals( customApproverType.getCode() ) )
          {
            iter.remove();
          }
        }
      }
    }

    populateNomApproverList( promoApprovalForm );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward redisplayList( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionApprovalForm promoApprovalForm = (PromotionApprovalForm)actionForm;

    if ( promoApprovalForm.getCustomApproverTypes() != null && !promoApprovalForm.isBehaviorActive() )
    {
      for ( Iterator<CustomApproverType> iter = promoApprovalForm.getCustomApproverTypes().listIterator(); iter.hasNext(); )
      {
        CustomApproverType customApproverType = iter.next();
        if ( "behavior".equals( customApproverType.getCode() ) )
        {
          iter.remove();
        }
      }
    }

    characteristicListForEachLevel( promoApprovalForm, false );
    promoApprovalForm.setReDisplay( true );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void characteristicListForEachLevel( PromotionApprovalForm promoApprovalForm, boolean noReload )
  {
    if ( !noReload )
    {
      if ( promoApprovalForm.getApprovalNodeLevels() != null && !promoApprovalForm.getApprovalNodeLevels().isEmpty() )
      {
        for ( Iterator<CustomApproverValueBean> iter = promoApprovalForm.getCustomApproverValueBeanListAsList().iterator(); iter.hasNext(); )
        {
          CustomApproverValueBean bean = iter.next();
          bean.getCharacteristics().addAll( promoApprovalForm.getCharacteristics() );
        }
      }
    }
    for ( Iterator<CustomApproverValueBean> iter = promoApprovalForm.getCustomApproverValueBeanListAsList().iterator(); iter.hasNext(); )
    {
      CustomApproverValueBean bean = iter.next();

      if ( "characteristic".equals( bean.getCustomApproverTypeValue() ) )
      {
        for ( Iterator<CustomApproverValueBean> innerIter = promoApprovalForm.getCustomApproverValueBeanListAsList().iterator(); innerIter.hasNext(); )
        {
          CustomApproverValueBean innerBean = innerIter.next();
          if ( !bean.getLevel().equals( innerBean.getLevel() ) )
          {
            Iterator<Characteristic> charIterator = getUserCharacteristicService().getAllCharacteristics().iterator();
            // innerBean.getCharacteristics().clear();
            while ( charIterator.hasNext() )
            {
              Characteristic charType = charIterator.next();
              if ( charType.getId().equals( bean.getCharacteristicId() ) )
              {
                innerBean.getCharacteristics().remove( charType );
              }
              /*
               * else { innerBean.getCharacteristics().add(charType); }
               */
            }
          }
        }
      }
    }
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionApprovalForm promoApprovalForm = (PromotionApprovalForm)form;

    ActionForward forward = mapping.findForward( ActionConstants.WIZARD_SAVE_AND_EXIT_ATTRIBUTE );

    ActionMessages errors = new ActionMessages();

    Promotion promotion = null;

    // If the promotion has a parent, then all fields are read-only and there is nothing to save
    if ( promoApprovalForm.isHasParent() )
    {
      if ( isWizardMode( request ) )
      {
        promotion = getWizardPromotion( request );
        return forward = getWizardNextPage( mapping, request, promotion );
      }

      // TODO: do we ever get this condition ???
      promotion = new ProductClaimPromotion();
      promotion.setId( new Long( promoApprovalForm.getPromotionId() ) );
      return forward = saveAndExit( mapping, request, promotion );
    }
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
    populateNomApproverList( promoApprovalForm );
    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );

      promotion = promoApprovalForm.toDomainObject( promotion );
    }
    else
    {
      // update promotion with form data
      promotion = promoApprovalForm.toDomainObject();
    }

    // Set many-to-one objects by loading association
    if ( !StringUtils.isBlank( promoApprovalForm.getApproverNodeId() ) )
    {
      Node approverNode = getNodeService().getNodeById( new Long( promoApprovalForm.getApproverNodeId() ) );
      promotion.setApproverNode( approverNode );
    }
    else
    {
      promotion.setApproverNode( null );
    }

    if ( !StringUtils.isBlank( promoApprovalForm.getApprovalConditionalClaimFormStepElementId() ) )
    {
      ClaimFormStepElement claimFormStepElement = getClaimFormDefinitionService().getClaimFormStepElementById( new Long( promoApprovalForm.getApprovalConditionalClaimFormStepElementId() ) );
      promotion.setApprovalConditionalAmountField( claimFormStepElement );

    }
    else
    {
      promotion.setApprovalConditionalAmountField( null );
    }

    // load Approvers (not for nominations)
    if ( !PromotionType.NOMINATION.equals( promoApprovalForm.getPromotionTypeCode() ) && promoApprovalForm.getApproverType() != null
        && promoApprovalForm.getApproverType().equals( "specific_approv" ) )
    {
      if ( promoApprovalForm.getParticipantApproverListCount() > 0 )
      {

        Iterator approverIterator = promoApprovalForm.getParticipantApproverList().iterator();
        while ( approverIterator.hasNext() )
        {
          PromotionApprovalParticipantBean approver = (PromotionApprovalParticipantBean)approverIterator.next();

          PromotionParticipantApprover approvalParticipant = new PromotionParticipantApprover();

          approvalParticipant.setId( approver.getId() );
          approvalParticipant.setVersion( approver.getVersion() );

          Participant participant = getParticipantService().getParticipantById( approver.getParticipantId() );
          approvalParticipant.setParticipant( participant );

          promotion.addPromotionParticipantApprover( approvalParticipant );
        }
      }
    }

    // load Submitters
    if ( promoApprovalForm.getApprovalType().equals( "cond_pax" ) )
    {
      if ( promoApprovalForm.getParticipantSubmitterListCount() > 0 )
      {
        Iterator submitterIterator = promoApprovalForm.getParticipantSubmitterList().iterator();
        while ( submitterIterator.hasNext() )
        {
          PromotionApprovalParticipantBean submitter = (PromotionApprovalParticipantBean)submitterIterator.next();

          PromotionParticipantSubmitter submitterParticipant = new PromotionParticipantSubmitter();

          submitterParticipant.setId( submitter.getId() );
          submitterParticipant.setVersion( submitter.getVersion() );

          Participant participant = getParticipantService().getParticipantById( submitter.getParticipantId() );
          submitterParticipant.setParticipant( participant );

          promotion.addPromotionParticipantSubmitter( submitterParticipant );
        }
      }
    }

    PromotionApprovalUpdateAssociation promoApprovalUpdateAssociation = new PromotionApprovalUpdateAssociation( promotion );

    try
    {
      promotion = getPromotionService().savePromotion( new Long( promoApprovalForm.getPromotionId() ), promoApprovalUpdateAssociation );
    }
    catch( UniqueConstraintViolationException e )
    {
      throw new BeaconRuntimeException( "This call shouldn't change any unique fields, so must be software bug.", e );
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

        if ( promotion.isRecognitionPromotion() )
        {
          if ( isSaveAndExit( request ) )
          {
            forward = saveAndExit( mapping, request, promotion );
          }
          else if ( ! ( (RecognitionPromotion)promotion ).isIncludePurl() )
          {
            forward = getWizardNextPage( mapping, request, promotion );
          }
          else
          {
            forward = mapping.findForward( "skipPublicRecog" );
          }
        }

        else
        {
          forward = getWizardNextPage( mapping, request, promotion );
        }
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }

    return forward;

  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward prepareNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    PromotionApprovalForm promotionApprovalForm = (PromotionApprovalForm)form;

    String returnUrl = RequestUtils.getRequiredParamString( request, "nodeLookupReturnUrl" );

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PROMO_APPROVAL_FORM, promotionApprovalForm );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/hierarchy/nodeLookup.do?" + "method=displaySearchWithinPrimary&" + NodeSearchAction.RETURN_ACTION_URL_PARAM + returnUrl );

    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    PromotionApprovalForm promotionApprovalForm = (PromotionApprovalForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionApprovalForm sessionPromotionApprovalForm = (PromotionApprovalForm)request.getSession().getAttribute( SESSION_PROMO_APPROVAL_FORM );

    if ( sessionPromotionApprovalForm != null )
    {
      try
      {
        BeanUtils.copyProperties( promotionApprovalForm, sessionPromotionApprovalForm );
      }
      catch( Exception e )
      {
        throw new BeaconRuntimeException( "Copy Properties failed.", e );
      }
    }

    // Set the nodeId, nodeName, and lookedUpNodeName on the form.
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
      String nodeId = (String)clientStateMap.get( "nodeId" );
      if ( nodeId != null )
      {
        promotionApprovalForm.setApproverNodeId( nodeId );
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing as this is an optional parameter
    }
    promotionApprovalForm.setApproverNodeName( RequestUtils.getOptionalParamString( request, "nodeName" ) );
    // clean up the session
    request.getSession().removeAttribute( SESSION_PROMO_APPROVAL_FORM );

    return redisplay( mapping, form, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward prepareApproverLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    // String index = request.getParameterMap().get("customApproverValueBeanListCount")[0];
    /*
     * String answer = null; Map m = request.getParameterMap(); Set s = m.entrySet(); Integer
     * sequenceNum = 0; for ( Iterator iterator = s.iterator(); iterator.hasNext(); ) {
     * Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>)iterator.next(); String key
     * = entry.getKey(); String[] value = null; if ( key.contains( ".level" ) ) { value =
     * entry.getValue(); answer = value[0].toString(); } }
     */
    prepareParticipantLookup( mapping, form, request, response );
    // request.getParameterMap()(arg0)("level");customApproverValueBeanListCount

    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnApproverLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return returnParticipantLookup( mapping, form, request, response, PromotionParticipantApprover.PROMO_PARTICIPANT_TYPE );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  // TODO: This method is a copy-paste of prepareSubmitterLookup. Even though they do the same thing
  // discussion pointed to the possibility of needing to do two different things.
  public ActionForward prepareSubmitterLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    prepareParticipantLookup( mapping, form, request, response );

    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  private ActionForward prepareParticipantLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    PromotionApprovalForm promoApprovalForm = (PromotionApprovalForm)form;

    String returnUrl = RequestUtils.getRequiredParamString( request, "returnUrl" );

    if ( PromotionType.NOMINATION.equals( promoApprovalForm.getPromotionTypeCode() ) )
    {
      String levelId = RequestUtils.getRequiredParamString( request, "levelId" );
      returnUrl = returnUrl + "&levelId=" + levelId;

      promoApprovalForm.setLevelId( new Long( levelId ) );
    }

    // Put the form in the session to be reloaded when coming back from participant lookup.
    request.getSession().setAttribute( SESSION_PROMO_APPROVAL_FORM, promoApprovalForm );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/participant/listBuilderPaxDisplay.do?" + ListBuilderAction.AUDIENCE_MEMBERS_LOOKUP_RETURN_URL_PARAM + returnUrl );

    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnSubmitterLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return returnParticipantLookup( mapping, form, request, response, PromotionParticipantSubmitter.PROMO_PARTICIPANT_TYPE );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  private ActionForward returnParticipantLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String type )
  {
    PromotionApprovalForm promotionApprovalForm = (PromotionApprovalForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionApprovalForm sessionPromotionApprovalForm = (PromotionApprovalForm)request.getSession().getAttribute( SESSION_PROMO_APPROVAL_FORM );

    if ( sessionPromotionApprovalForm != null )
    {
      try
      {
        BeanUtils.copyProperties( promotionApprovalForm, sessionPromotionApprovalForm );
      }
      catch( Exception e )
      {
        throw new BeaconRuntimeException( "Copy Properties failed.", e );
      }
    }

    // get the participant ids returned from the list builder search
    List participants = (List)request.getSession().getAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );

    if ( participants != null )
    {
      for ( Iterator iter = participants.iterator(); iter.hasNext(); )
      {
        FormattedValueBean participantBean = (FormattedValueBean)iter.next();
        if ( promotionApprovalForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
        {
          PromotionApprovalParticipantBean bean = new PromotionApprovalParticipantBean();
          CustomApproverValueBean valueBean = promotionApprovalForm.getCustomApproverValueBeanListAsList().get( promotionApprovalForm.getLevelId().intValue() - 1 );
          // TODO possible cleam up check if this service call is needed since the search
          // returns first name, last name, job type, and department already as a formatted String.
          Participant participant = getParticipantService().getParticipantById( participantBean.getId() );
          bean.setParticipantId( participant.getId() );
          bean.setFirstName( participant.getFirstName() );
          bean.setLastName( participant.getLastName() );
          bean.setPromotionId( new Long( promotionApprovalForm.getPromotionId() ) );
          bean.setLevelId( sessionPromotionApprovalForm != null ? sessionPromotionApprovalForm.getLevelId() : null );
          bean.setParticipantType( PromotionParticipantApprover.PROMO_PARTICIPANT_TYPE );
          valueBean.setParticipantBean( bean );
          valueBean.getApproverListAsList().add( bean );
          promotionApprovalForm.getCustomApproverValueBeanListAsList().set( promotionApprovalForm.getLevelId().intValue() - 1, valueBean );
        }
        else
        {
          PromotionApprovalParticipantBean bean = new PromotionApprovalParticipantBean();
          // TODO possible cleam up check if this service call is needed since the search
          // returns first name, last name, job type, and department already as a formatted String.
          Participant participant = getParticipantService().getParticipantById( participantBean.getId() );
          bean.setParticipantId( participant.getId() );
          bean.setFirstName( participant.getFirstName() );
          bean.setLastName( participant.getLastName() );
          bean.setPromotionId( new Long( promotionApprovalForm.getPromotionId() ) );
          bean.setLevelId( sessionPromotionApprovalForm != null ? sessionPromotionApprovalForm.getLevelId() : null );

          if ( PromotionParticipantApprover.PROMO_PARTICIPANT_TYPE.equals( type ) )
          {
            bean.setParticipantType( PromotionParticipantApprover.PROMO_PARTICIPANT_TYPE );
            promotionApprovalForm.addPromotionParticipantApprover( bean );
          }
          else
          {
            bean.setParticipantType( PromotionParticipantSubmitter.PROMO_PARTICIPANT_TYPE );
            promotionApprovalForm.addPromotionParticipantSubmitter( bean );
          }
        }

      }
    }

    // clean up the session
    request.getSession().removeAttribute( SESSION_PROMO_APPROVAL_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );

    return redisplay( mapping, form, request, response );
  }

  /**
   * Remove the approvers selected
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removeApprovers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    PromotionApprovalForm promotionApprovalForm = (PromotionApprovalForm)form;

    return removeParticipants( mapping, form, request, response, promotionApprovalForm.getParticipantApproverList() );
  }

  /**
   * Remove the custom specific approvers selected
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removeCustomApprovers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionApprovalForm promotionApprovalForm = (PromotionApprovalForm)form;

    String levelId = RequestUtils.getRequiredParamString( request, "levelId" );
    Long level = new Long( levelId );

    List<PromotionApprovalParticipantBean> approverList = new ArrayList<>();

    if ( promotionApprovalForm.getCustomApproverValueBeanListAsList() != null )
    {
      // I'm hesitant to assume that the level index will always match the list index - going to
      // look for it instead
      for ( int i = 0; i < promotionApprovalForm.getCustomApproverValueBeanListAsList().size(); ++i )
      {
        if ( level.equals( promotionApprovalForm.getCustomApproverValueBeanListAsList().get( i ).getLevel() ) )
        {
          approverList = promotionApprovalForm.getCustomApproverValueBeanListAsList().get( i ).getApproverListAsList();
        }
      }
    }

    return removeParticipants( mapping, form, request, response, approverList );
  }

  /**
   * Remove the submitters selected
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removeSubmitters( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionApprovalForm promotionApprovalForm = (PromotionApprovalForm)form;

    return removeParticipants( mapping, form, request, response, promotionApprovalForm.getParticipantSubmitterList() );
  }

  /**
   * Remove the participants selected
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  private ActionForward removeParticipants( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, List participantList )
  {
    PromotionApprovalForm promotionApprovalForm = (PromotionApprovalForm)form;
    ActionMessages errors = new ActionMessages();
    characteristicListForEachLevel( promotionApprovalForm, false );

    Iterator participantIterator = participantList.iterator();

    while ( participantIterator.hasNext() )
    {
      PromotionApprovalParticipantBean participantBean = (PromotionApprovalParticipantBean)participantIterator.next();
      if ( participantBean.getRemove() != null && participantBean.getRemove().equals( "Y" ) )
      {
        participantIterator.remove();
      }
    }

    ActionForward actionForward;
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = mapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    else
    {
      actionForward = mapping.findForward( "success_recognition" );
      if ( promotionApprovalForm.getPromotionTypeCode().equals( PromotionType.PRODUCT_CLAIM ) )
      {
        actionForward = mapping.findForward( "success_product" );
      }
    }

    return actionForward;
  }

  /**
   * Search for a person to be the default approver. Search is by last name.
   */
  public ActionForward searchDefaultApprover( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionApprovalForm promotionApprovalForm = (PromotionApprovalForm)form;
    characteristicListForEachLevel( promotionApprovalForm, false );

    // Get search results based on last name only.
    ParticipantSearchCriteria searchCriteria = new ParticipantSearchCriteria();
    searchCriteria.setLastName( promotionApprovalForm.getDefaultApproverSearchLastName() );
    searchCriteria.setSortByLastNameFirstName( true );
    List<Participant> resultList = getParticipantService().searchParticipant( searchCriteria, true );

    // I'm not sure but I get the feel request attributes are usually light-weight, so change from
    // participant to lighter value bean object?
    List<UserValueBean> beanResultList = new ArrayList<UserValueBean>( resultList.size() );
    for ( Participant p : resultList )
    {
      UserValueBean bean = new UserValueBean();
      bean.setId( p.getId() );
      bean.setFirstName( p.getFirstName() );
      bean.setLastName( p.getLastName() );
      beanResultList.add( bean );
    }

    request.setAttribute( "defaultApproverSearchResults", beanResultList );
    request.setAttribute( "defaultApproverSearchResultsCount", beanResultList.size() );

    return forward;
  }

  /**
   * Continue or exit without saving
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionFoward
   */
  public ActionForward continueOrExit( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    Promotion promotion = getWizardPromotion( request );
    setPromotionInWizardManager( request, promotion );

    ActionForward forward = getWizardNextPage( mapping, request, promotion );

    return forward;
  }

  /**
   * Back to behavior Action
   * 
   * @param request
   * @param mapping
   * @param actionForm
   * @param response
   * @return ActionForward
   */
  public ActionForward backToSweepStakes( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.WIZARD_BACK_TO_SWEEPSTAKES );
  }

  /**
   * Get the claimService from the beanFactory
   * 
   * @return ClaimService
   */
  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  /**
   * Get the NodeService from the beanFactory locator.
   * 
   * @return NodeService
   */
  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  /**
   * Get the ParticipantService from the beanFactory locator.
   * 
   * @return ParticipantService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the PromotionParticipantService from the beanFactory locator.
   * 
   * @return PromotionParticipantService
   */
  private PromotionParticipantService getPromotionParticipantService()
  {
    return (PromotionParticipantService)getService( PromotionParticipantService.BEAN_NAME );
  }

  /**
   * Get the PromotionParticipantService from the beanFactory locator.
   * 
   * @return PromotionParticipantService
   */
  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  private void populateNomApproverList( PromotionApprovalForm form )
  {
    PromotionService promotionService = (PromotionService)getService( PromotionService.BEAN_NAME );
    List<CustomApproverValueBean> customApproverValueBeanListAsList = form.getCustomApproverValueBeanListAsList();

    for ( CustomApproverValueBean bean : customApproverValueBeanListAsList )
    {
      if ( !bean.eligibleForViewApproverModal() )
      {
        continue;
      }

      List<NominationApproverValueBean> approverList = promotionService.getCustomApproverList( bean.getLevel().intValue(), Long.valueOf( form.getPromotionId() ) );
      bean.setNomApproverList( approverList );
    }

  }
}
