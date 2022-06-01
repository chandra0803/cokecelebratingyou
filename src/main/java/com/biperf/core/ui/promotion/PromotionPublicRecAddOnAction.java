
package com.biperf.core.ui.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.PublicRecognitionAudienceType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPublicRecognitionAudience;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.NonUniqueDataServiceErrorException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PublicRecognitionAddOnUpdateAssociation;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.BudgetSegmentValueBean;
import com.biperf.core.value.FormattedValueBean;

public class PromotionPublicRecAddOnAction extends PromotionBaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( PromotionPublicRecAddOnAction.class );
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  private static final String SESSION_PUBLIC_RECOG_FORM = "SESSION_PUBLIC_RECOG_FORM";

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

    PromotionPublicRecAddOnForm recogForm = (PromotionPublicRecAddOnForm)form;

    Promotion promotion = null;

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );

      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_PUBLIC_RECOGNITION_AUDIENCE ) );

      Promotion attachedPromotion = getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );

      if ( promotion.isRecognitionPromotion() )
      {
        RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
        recPromo.setPromotionPublicRecognitionAudiences( ( (RecognitionPromotion)attachedPromotion ).getPromotionPublicRecognitionAudiences() );
      }
      else if ( promotion.isNominationPromotion() )
      {
        NominationPromotion nomPromo = (NominationPromotion)promotion;
        nomPromo.setPromotionPublicRecognitionAudiences( ( (NominationPromotion)attachedPromotion ).getPromotionPublicRecognitionAudiences() );
      }

    }
    // NORMAL MODE
    else
    {
      String promotionId = recogForm.getPromotionId().toString();
      if ( promotionId != null && promotionId.length() > 0 )
      {
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_PUBLIC_RECOGNITION_AUDIENCE ) );
        promotion = getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), promoAssociationRequestCollection );
      }
    }

    if ( promotion != null )
    {
      recogForm.load( promotion );
    }

    Set<PromotionPublicRecognitionAudience> setFromPromo = null;
    Set<PromotionPublicRecognitionAudience> promotionPublicRecogAudienceSetWithPaxSize = new LinkedHashSet<>();

    if ( promotion.isRecognitionPromotion() )
    {
      setFromPromo = ( (RecognitionPromotion)promotion ).getPromotionPublicRecognitionAudiences();
    }
    else if ( promotion.isNominationPromotion() )
    {
      setFromPromo = ( (NominationPromotion)promotion ).getPromotionPublicRecognitionAudiences();
    }

    if ( promotion != null && setFromPromo != null )
    {
      Iterator<PromotionPublicRecognitionAudience> publicRecognitionAudienceIterator = setFromPromo.iterator();
      while ( publicRecognitionAudienceIterator.hasNext() )
      {
        PromotionPublicRecognitionAudience publicRecognitionAudience = publicRecognitionAudienceIterator.next();
        Audience audience = publicRecognitionAudience.getAudience();
        audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
        promotionPublicRecogAudienceSetWithPaxSize.add( publicRecognitionAudience );
      }
    }
    request.getSession().setAttribute( "sessionPubliRecogAudienceList", new LinkedHashSet<>( promotionPublicRecogAudienceSetWithPaxSize ) );

    return mapping.findForward( forwardTo );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionPublicRecAddOnForm recogForm = (PromotionPublicRecAddOnForm)form;
    String bgMasterId = request.getParameter( "budgetMasterId" );

    if ( bgMasterId != null && !bgMasterId.equals( "" ) )
    {
      recogForm.setBudgetMasterId( new Long( bgMasterId ) );
    }
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
      request.getSession().removeAttribute( "sessionPubliRecogAudienceList" );
      return forward;
    }
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_PUBLIC_REC_BUDGET_MASTER ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_PUBLIC_RECOGNITION_AUDIENCE ) );

      promotion = getPromotionService().getPromotionByIdWithAssociations( recogForm.getPromotionId(), associationRequestCollection );
    }

    if ( recogForm.isAllowPublicRecognitionPoints() )
    {
      if ( recogForm.getAudience().equals( PublicRecognitionAudienceType.CREATE_AUDIENCE_CODE ) )
      {
        Set sessionPublicRecogAudienceSet = (LinkedHashSet)request.getSession().getAttribute( "sessionPubliRecogAudienceList" );

        if ( sessionPublicRecogAudienceSet == null || sessionPublicRecogAudienceSet.size() < 1 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.public.recognition.errors.AUDIENCE_NOT_FOUND" ) );
        }
        else
        {
          Set publicRecognitionAudienceSet = null;
          if ( promotion.isRecognitionPromotion() )
          {
            publicRecognitionAudienceSet = ( (RecognitionPromotion)promotion ).getPromotionPublicRecognitionAudiences();
          }
          else if ( promotion.isNominationPromotion() )
          {
            publicRecognitionAudienceSet = ( (NominationPromotion)promotion ).getPromotionPublicRecognitionAudiences();
          }

          if ( publicRecognitionAudienceSet != null && publicRecognitionAudienceSet.size() > 0 )
          {
            Iterator promoPublicRecogAudienceIterator = publicRecognitionAudienceSet.iterator();
            while ( promoPublicRecogAudienceIterator.hasNext() )
            {
              PromotionPublicRecognitionAudience promotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)promoPublicRecogAudienceIterator.next();
              if ( !sessionPublicRecogAudienceSet.contains( promotionPublicRecognitionAudience ) )
              {
                promoPublicRecogAudienceIterator.remove();
              }
            }
          }
          Iterator sessionAudienceIterator = sessionPublicRecogAudienceSet.iterator();
          while ( sessionAudienceIterator.hasNext() )
          {
            PromotionPublicRecognitionAudience sessionPromotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)sessionAudienceIterator.next();

            if ( promotion.isRecognitionPromotion() )
            {
              ( (RecognitionPromotion)promotion ).addPromotionPublicRecognitionAudience( sessionPromotionPublicRecognitionAudience );
            }
            else if ( promotion.isNominationPromotion() )
            {
              ( (NominationPromotion)promotion ).addPromotionPublicRecognitionAudience( sessionPromotionPublicRecognitionAudience );
            }
          }
        }
      }
    }
    recogForm.buildPublicRecogAudience( promotion, errors );

    if ( recogForm.isCreateNewBudgetMaster() )
    {
      BudgetMaster budgetMaster = recogForm.getNewBudgetMaster();

      // segment logic
      budgetMaster.getBudgetSegments().clear();
      for ( Iterator<BudgetSegmentValueBean> iter = recogForm.getBudgetSegmentVBList().iterator(); iter.hasNext(); )
      {
        BudgetSegmentValueBean budgetSegmentVB = (BudgetSegmentValueBean)iter.next();

        // build budget segment obj
        BudgetSegment budgetSegment = new BudgetSegment();
        budgetSegment = recogForm.populateBudgetSegment( budgetSegmentVB );

        if ( budgetMaster.getBudgetType().isCentralBudgetType() )
        {
          Budget budget = new Budget();

          budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
          BigDecimal originalValueLocal = new BigDecimal( budgetSegmentVB.getOriginalValue() );
          budget.setOriginalValue( originalValueLocal );
          budget.setCurrentValue( originalValueLocal );
          budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

          budgetSegment.getBudgets().clear();
          budgetSegment.addBudget( budget );
        }
        budgetMaster.addBudgetSegment( budgetSegment );
      }

      if ( budgetMaster.getBudgetType().isCentralBudgetType() )
      {
        budgetMaster.setFinalPayoutRule( BudgetFinalPayoutRule.lookup( recogForm.getFinalPayoutRule() ) );
      }

      if ( errors.isEmpty() )
      {
        try
        {
          budgetMaster = getBudgetMasterService().saveBudgetMaster( budgetMaster );

          if ( promotion.isRecognitionPromotion() )
          {
            ( (RecognitionPromotion)promotion ).setPublicRecogBudgetMaster( budgetMaster );
          }
          else if ( promotion.isNominationPromotion() )
          {
            ( (NominationPromotion)promotion ).setPublicRecogBudgetMaster( budgetMaster );
          }
        }
        catch( NonUniqueDataServiceErrorException e )
        {
          // If the name is not unique, the send back the error instead of
          // continuing on
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.public.recognition.errors.BUDGET_NAME_EXISTS" ) );
        }
        catch( ServiceErrorException se )
        {
          ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( se.getServiceErrors(), errors );
        }
        catch( Exception e )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.public.recognition.errors.GENERIC_ERROR" ) );
        }
      }
    }

    if ( recogForm.isUseExistingBudgetMaster() )
    {
      Long budgetMasterId = recogForm.getBudgetMasterId();
      if ( null == budgetMasterId )
      {
        budgetMasterId = recogForm.getHiddenBudgetMasterId();
      }
      if ( budgetMasterId.longValue() == 0 )
      {
        if ( request.getParameter( "budgetMasterId" ) != null )
        {
          budgetMasterId = new Long( request.getParameter( "budgetMasterId" ) );
          recogForm.setBudgetMasterId( budgetMasterId );
        }
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    try
    {
      promotion = recogForm.toDomainObject( promotion );

      PublicRecognitionAddOnUpdateAssociation publicRecognitionAddOnUpdateAssociation = new PublicRecognitionAddOnUpdateAssociation( promotion );

      List<UpdateAssociationRequest> updateAssociations = new ArrayList<>();

      updateAssociations.add( publicRecognitionAddOnUpdateAssociation );

      promotion = getPromotionService().savePromotion( recogForm.getPromotionId(), updateAssociations );
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

        forward = getWizardNextPage( mapping, request, promotion );
      }

      if ( !recogForm.isAllowPublicRecognitionPoints() || recogForm.getBudgetOption().equals( PromotionPublicRecAddOnForm.BUDGET_NONE )
          || recogForm.getBudgetType().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
      {
        if ( isWizardMode( request ) )
        {
          forward = getWizardNextPage( mapping, request, promotion );
        }
        else
        {
          forward = saveAndExit( mapping, request, promotion );
        }
      }
      else if ( recogForm.getBudgetOption().equals( PromotionPublicRecAddOnForm.BUDGET_EXISTING ) && getPublicRecogBudgetMaster( promotion ).getBudgetType().isCentralBudgetType() )
      {
        if ( isWizardMode( request ) )
        {
          forward = getWizardNextPage( mapping, request, promotion );
        }
        else
        {
          forward = saveAndExit( mapping, request, promotion );
        }
      }
      else
      {
        forward = mapping.findForward( "saveAndContinueGiversBudget" );
      }
    }
    request.getSession().removeAttribute( "sessionPubliRecogAudienceList" );

    return forward;
  }

  /**
   * Helper function for splitting by promotion type
   */
  private BudgetMaster getPublicRecogBudgetMaster( Promotion promotion )
  {
    if ( promotion.isRecognitionPromotion() )
    {
      return ( (RecognitionPromotion)promotion ).getPublicRecogBudgetMaster();
    }
    else if ( promotion.isNominationPromotion() )
    {
      return ( (NominationPromotion)promotion ).getPublicRecogBudgetMaster();
    }
    else
    {
      throw new RuntimeException( "No conditional case implemented for provided promotion" );
    }
  }

  public ActionForward removePublicRecognitionAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionPublicRecAddOnForm promotionPublicRecAddOnForm = (PromotionPublicRecAddOnForm)form;

    if ( null != promotionPublicRecAddOnForm.getDeletePublicRecognitionAudience() && promotionPublicRecAddOnForm.getDeletePublicRecognitionAudience().length > 0 )
    {

      // Get the list of names for the audiences being deleted.
      List deletedPublicRecogAudienceNames = ArrayUtil.stringArrayToList( promotionPublicRecAddOnForm.getDeletePublicRecognitionAudience() );

      Set sessionPublicRecogAudienceList = (LinkedHashSet)request.getSession().getAttribute( "sessionPubliRecogAudienceList" );
      // Get the iterator of existing audiences on the promotionWebRulesForm
      Iterator audienceIterator = sessionPublicRecogAudienceList.iterator();

      Set setOfPublicRecognitionAudienceToRemove = new LinkedHashSet();

      // Iterate over the webRulesAudience
      while ( audienceIterator.hasNext() )
      {
        PromotionPublicRecognitionAudience promotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)audienceIterator.next();

        if ( deletedPublicRecogAudienceNames.contains( promotionPublicRecognitionAudience.getAudience().getName() ) )
        {
          setOfPublicRecognitionAudienceToRemove.add( promotionPublicRecognitionAudience );
        }
      }

      sessionPublicRecogAudienceList.removeAll( setOfPublicRecognitionAudienceToRemove );

    }
    return forward;
  }

  public ActionForward addAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionPublicRecAddOnForm promotionPublicRecAddOnForm = (PromotionPublicRecAddOnForm)form;

    addPublicRecognitionAudience( request, promotionPublicRecAddOnForm, new Long( promotionPublicRecAddOnForm.getAudienceId() ) );
    promotionPublicRecAddOnForm.setAudienceId( "" );

    return mapping.findForward( "success" );
  }

  private void addPublicRecognitionAudience( HttpServletRequest request, PromotionPublicRecAddOnForm promotionPublicRecAddOnForm, Long audienceId )
  {
    Audience audience = getAudienceService().getAudienceById( audienceId, null );
    audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );

    Promotion promotion = null;

    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      promotion = getPromotionService().getPromotionById( new Long( promotionPublicRecAddOnForm.getPromotionId() ) );
    }

    // Build a new promotionPublicRecognitionAudience
    PromotionPublicRecognitionAudience promotionPublicRecognitionAudience = new PromotionPublicRecognitionAudience();
    promotionPublicRecognitionAudience.setAudience( audience );
    promotionPublicRecognitionAudience.setPromotion( promotion );

    Set promoPublicRecogAudienceSet = (LinkedHashSet)request.getSession().getAttribute( "sessionPubliRecogAudienceList" );
    if ( promoPublicRecogAudienceSet == null )
    {
      promoPublicRecogAudienceSet = new LinkedHashSet();
    }

    promoPublicRecogAudienceSet.add( promotionPublicRecognitionAudience );

    request.getSession().setAttribute( "sessionPubliRecogAudienceList", promoPublicRecogAudienceSet );
  }

  private int getNbrOfPaxsInCriteriaAudience( Audience audience )
  {
    int nbrOfPaxInCriteriaAudience = 0;

    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );

    nbrOfPaxInCriteriaAudience = paxFormattedValueList.size();

    return nbrOfPaxInCriteriaAudience;
  }

  public ActionForward preparePublicRecogAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionPublicRecAddOnForm promotionPublicRecAddOnForm = (PromotionPublicRecAddOnForm)form;

    String promotionId = promotionPublicRecAddOnForm.getPromotionId().toString();

    ActionForward returnForward = mapping.findForward( "addPublicRecognitionAudience" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap, true );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );

    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( PromotionPublicRecAddOnForm.SESSION_KEY, promotionPublicRecAddOnForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );

    return forward;
  }

  public ActionForward returnPublicRecAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionPublicRecAddOnForm promotionPublicRecAddOnForm = (PromotionPublicRecAddOnForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionPublicRecAddOnForm sessionPublicRecogForm = (PromotionPublicRecAddOnForm)request.getSession().getAttribute( PromotionPublicRecAddOnForm.SESSION_KEY );

    if ( sessionPublicRecogForm != null )
    {
      try
      {
        BeanUtils.copyProperties( promotionPublicRecAddOnForm, sessionPublicRecogForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
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

      try
      {
        String audienceIdString = (String)clientStateMap.get( "audienceId" );
        if ( audienceIdString != null && !audienceIdString.equals( "" ) )
        {
          audienceId = new Long( audienceIdString );
        }
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

    }
    catch( InvalidClientStateException e )
    {
      // audienceId is optional - do nothing
      // throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addPublicRecognitionAudience( request, promotionPublicRecAddOnForm, audienceId );
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward returnApproverLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionPublicRecAddOnForm promotionPublicRecAddOnForm = (PromotionPublicRecAddOnForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionPublicRecAddOnForm sessionPromotionPublicRecAddOnForm = (PromotionPublicRecAddOnForm)request.getSession().getAttribute( SESSION_PUBLIC_RECOG_FORM );
    request.getSession().removeAttribute( SESSION_PUBLIC_RECOG_FORM );

    if ( sessionPromotionPublicRecAddOnForm != null )
    {
      List participants = (List)request.getSession().getAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );

      if ( participants != null )
      {
        Iterator participantIter = participants.iterator();
        if ( participantIter.hasNext() )
        {
          FormattedValueBean participantBean = (FormattedValueBean)participantIter.next();
          sessionPromotionPublicRecAddOnForm.setBudgetApproverId( participantBean.getId() );
          sessionPromotionPublicRecAddOnForm.setBudgetApproverName( participantBean.getLastName() + ", " + participantBean.getFirstName() );
        }
      }

      try
      {
        BeanUtils.copyProperties( promotionPublicRecAddOnForm, sessionPromotionPublicRecAddOnForm );
      }
      catch( IllegalArgumentException iae )
      {
        // no audience returned
      }
      catch( Exception e )
      {
        log.info( "returnApproverLookup: Copy Properties failed." );
      }
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Makes a request to the Approver builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareApproverLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionPublicRecAddOnForm promotionPublicRecAddOnForm = (PromotionPublicRecAddOnForm)form;

    // Put the form in the session to be reloaded when coming back from
    // approver lookup.
    request.getSession().setAttribute( SESSION_PUBLIC_RECOG_FORM, promotionPublicRecAddOnForm );

    String returnUrl = RequestUtils.getRequiredParamString( request, "approverLookupReturnUrl" );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/participant/listBuilderPaxDisplay.do?" + ListBuilderAction.AUDIENCE_MEMBERS_LOOKUP_RETURN_URL_PARAM + "=" + returnUrl
        + "&singleResult=true" );

    return null;
  }

  public ActionForward redisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward addAnotherSegment( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success_add_another" );
    ActionMessages errors = new ActionMessages();
    PromotionPublicRecAddOnForm promotionPublicRecAddOnForm = (PromotionPublicRecAddOnForm)actionForm;

    promotionPublicRecAddOnForm.addEmptyBudgetSegment();

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( "failure_add_another" );
    }

    return forward;
  }

  public ActionForward removeBudgetSegment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionPublicRecAddOnForm promotionPublicRecAddOnForm = (PromotionPublicRecAddOnForm)form;
    promotionPublicRecAddOnForm.getBudgetSegmentVBList().remove( promotionPublicRecAddOnForm.getBudgetSegmentVBListSize() - 1 );
    return forward;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)BeanLocator.getBean( BudgetMasterService.BEAN_NAME );
  } // end getBudgetMasterService

  /**
   * Get the UserService from the beanFactory locator.
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

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

  protected static PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }

  protected static MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

}
