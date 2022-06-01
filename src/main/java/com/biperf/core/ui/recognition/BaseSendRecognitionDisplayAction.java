
package com.biperf.core.ui.recognition;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.CustomerInformationBlock;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.claim.ClaimElementForm;
import com.biperf.core.ui.recognition.purl.PresetSearchFiltersBean;
import com.biperf.core.ui.recognition.state.RecipientBean;
import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.threads.CallableFactory;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.RecognitionBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.biperf.core.domain.WebErrorMessage;

public abstract class BaseSendRecognitionDisplayAction extends BaseRecognitionAction
{

  private static final Log logger = LogFactory.getLog( BaseSendRecognitionDisplayAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String mode = request.getParameter( "mode" );

    if ( !"edit".equalsIgnoreCase( mode ) && !"isBudgetAlert".equalsIgnoreCase( mode ) )
    {
      RecognitionStateManager.remove( request );
    }

    final SendRecognitionForm form = (SendRecognitionForm)actionForm;
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotEmpty( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      if ( Objects.nonNull( (String)clientStateMap.get( "reporteeId" ) ) )
      {
        Long reporteeId = new Long( (String)clientStateMap.get( "reporteeId" ) );
        User user = getUserService().getUserById( reporteeId );
        Participant manager = null;

        if ( Objects.nonNull( (Boolean)clientStateMap.get( "isRaFlow" ) ) && new Boolean( (boolean)clientStateMap.get( "isRaFlow" ) ) )
        {

          manager = getParticipantService().getParticipantById( UserManager.getUserId() );
          if ( user.isActive() && manager.isManager() )
          {
            setClaimRecipientFormBeansForPurl( form, reporteeId );
          }
        }
        else
        {
          setClaimRecipientFormBeansForPurl( form, reporteeId );
        }

      }

    }

    intializeFormSetUpJson( request, form );
    setPromotionType( form );
    request.getSession().setAttribute( "submitPromotionPath", mapping.getPath() );

    /*
     * if ( request.getParameter( "isBudgetAlert" ) != null && request.getParameter( "isBudgetAlert"
     * ).equals( "true" ) ) { RecognitionStateManager.addToRequest( getRecognitionForm( request ),
     * request ); }
     */

    if ( form.getClaimId() != null )
    {
      populateNomsData( form, request );
    }
    else
    {
      SendRecognitionForm formInSession = RecognitionStateManager.getFromSession( request );

      if ( formInSession != null )
      {
        formInSession.setInitializationJson( form.getInitializationJson() );
        formInSession.setPromotionType( form.getPromotionType() );
        formInSession.setRecognitionEdit( true );

        // When going back to edit, escape quotation marks so the FE ultimately renders them
        // correctly
        if ( "edit".equalsIgnoreCase( mode ) && formInSession.getComments() != null )
        {
          formInSession.setComments( formInSession.getComments().replace( "\"", "&amp;quot;" ) );
        }

        RecognitionStateManager.addToRequest( formInSession, request );
        return mapping.findForward( "success" );
      }
      if ( form != null && form.getComments() != null )
      {
        form.setComments( form.getComments().replace( "\"", "&amp;quot;" ) );
      }
      prepopulateRecogData( form, request );
    }
   // Client customization for WIP #39189 starts
    // clear out all the stored files
    SendRecognitionForm sendRecogForm = getRecognitionState( request );
    if ( sendRecogForm.getClaimUploads() == null || sendRecogForm.getClaimUploads().size() == 0 )
    {
      request.getSession().removeAttribute( CLAIM_FILES );
    }
    // Client customization for WIP #39189 ends

    return mapping.findForward( "success" );
  }

  @SuppressWarnings( "unchecked" )
  public void intializeFormSetUpJson( HttpServletRequest request, final SendRecognitionForm form )
  {
    final Long USER_ID = UserManager.getUserId();
    final boolean IS_USER_A_PARTICIPANT = UserManager.getUser().isParticipant();
    // Setting the eligiblePromotion session value as null to get the updated eligible promotion
    // list
    if ( request.getSession().getAttribute( "eligiblePromotions" ) != null )
    {
      request.getSession().setAttribute( "eligiblePromotions", null );
    }
    final List eligiblePromotion = getEligiblePromotions( request );
    final RecognitionStartBean startBean = new RecognitionStartBean();

    List<Callable<Object>> callables = new ArrayList<Callable<Object>>();

    callables.add( CallableFactory.createCallable( new Callable<Object>()
    {
      public Object call() throws Exception
      {
        getUserNodes( USER_ID, startBean );
        return null;
      }

    } ) );

    callables.add( CallableFactory.createCallable( new Callable<Object>()
    {
      public Object call() throws Exception
      {
        getRecognitionPromotions( USER_ID, IS_USER_A_PARTICIPANT, startBean, form, eligiblePromotion );
        return null;
      }

    } ) );

    try
    {
      getExecutorService().invokeAll( callables );
    }
    catch( InterruptedException ie )
    {
    }

    form.setInitializationJson( toJson( startBean ) );
  }

  @SuppressWarnings( "unchecked" )
  private void populateNomsData( SendRecognitionForm form, HttpServletRequest request )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_PARTICIPANTS ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.USER_NODES ) );
    Claim claim = getClaimService().getClaimByIdWithAssociations( form.getClaimId(), associationRequestCollection );
    NominationClaim nomClaim = (NominationClaim)claim;
    form.setNodeId( nomClaim.getNode().getId() );

    Set<ClaimRecipient> claimRecipients = nomClaim.getClaimRecipients();
    int i = 0;

    for ( ClaimRecipient claimRecipient : claimRecipients )
    {
      RecipientBean claimRecipientFormBeans = form.getClaimRecipientFormBeans( i );
      claimRecipientFormBeans.setUserId( claimRecipient.getRecipient().getId() );
      i = i + 1;
    }

    prepopulateRecogData( form, request );

  }

  public void populateClaimId( SendRecognitionForm submitForm, HttpServletRequest request )
  {

    Long promotionId = null;
    Long claimId = null;

    String clientState = request.getParameter( "clientState" );
    if ( clientState != null && clientState.trim().length() > 0 )
    {
      String cryptoPass = request.getParameter( "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }

      try
      {
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        if ( clientStateMap.get( "promotionId" ) != null )
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        if ( clientStateMap.get( "claimId" ) != null )
        {
          claimId = (Long)clientStateMap.get( "claimId" );
        }
      }
      catch( InvalidClientStateException e )
      {
        // Never Enter This Block
      }
    }
    else
    {
      if ( request.getParameter( "claimId" ) != null )
      {
        claimId = Long.valueOf( request.getParameter( "claimId" ) );
      }
      if ( request.getParameter( "promoId" ) != null )
      {
        promotionId = Long.valueOf( request.getParameter( "promoId" ) );
      }
    }
    submitForm.setPromotionId( promotionId );
    submitForm.setClaimId( claimId );
  }

  protected abstract SendRecognitionForm getRecognitionState( HttpServletRequest request );

  protected abstract void setPromotionType( SendRecognitionForm form );

  public abstract SendRecognitionForm getRecognitionForm( HttpServletRequest request );

  private void getUserNodes( Long userId, RecognitionStartBean startBean )
  {
    Set<UserNode> userNodes = getUserService().getUserNodes( userId );
    List<UserNode> userNodesList = new ArrayList<UserNode>();
    // Client customization for WIP #41645 starts
    boolean hasManagerPrimary = false;
    if ( userNodes != null  && !userNodes.isEmpty() )
    {    	
      if ( userNodes.size() > 1 )
      {
    	for ( UserNode userNode : userNodes )
        {
        	if ( userNode.getIsMgrPrimary() )
        	{
        		userNodesList.add( userNode ); 
        		hasManagerPrimary = true;
        	}
        }
    	if ( !hasManagerPrimary )
    	{
    		 userNodesList.addAll( userNodes ); 
    	}
      }
      else
      {
    	 userNodesList.addAll( userNodes );  
      }
    }    
    // Client customization for WIP #41645 ends
    
 /*   if ( userNodes != null && !userNodes.isEmpty() )

    {
      for ( UserNode userNode : userNodes )
      {
        if ( !userNode.getNode().isDeleted() )
        {
          userNodesList.add( userNode );
        }
      }
    }*/
    startBean.setUserNodes( userNodesList );
  }

  private void getRecognitionPromotions( Long userId, boolean isUserAParticipant, RecognitionStartBean startBean, SendRecognitionForm form, List eligiblePromotion )
  {
    List<RecognitionBean> promotions = getPromotions( userId, isUserAParticipant, eligiblePromotion );

    for ( RecognitionBean promotion : promotions )
    {
      setClaimFormElements( promotion, form );
    }

    startBean.setRecognitionPromotions( promotions );

  }

  // Client customizations for wip #26532 starts
  private void getAllowedDomains( boolean isUserAParticipant, RecognitionStartBean startBean )
  {
    if ( isUserAParticipant )
    {
      // set the system default allowed domains
      String allowedDomains = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_ACCEPTABLE_DOMAINS ).getStringVal();
      startBean.setPurlAllowOutsideDomains( Arrays.asList( StringUtils.split( allowedDomains, "," ) ) );
    }
  }
  // Client customizations for wip #26532 ends
  
  protected List<RecognitionBean> getPromotions( Long userId, boolean isUserAParticipant, List eligiblePromotions )
  {
    List<PromotionMenuBean> validPromotions = new ArrayList<PromotionMenuBean>();
    for ( int i = 0; i < eligiblePromotions.size(); i++ )
    {
      PromotionMenuBean promoBean = (PromotionMenuBean)eligiblePromotions.get( i );
      if ( promoBean.isCanSubmit() )
      {
        validPromotions.add( promoBean );
      }
    }
    return getPromotionService().getRecognitionSubmissionList( userId, validPromotions, isUserAParticipant );
  }

  private void setClaimFormElements( RecognitionBean promotion, SendRecognitionForm form )
  {
    if ( !promotion.isClaimFormUsed() || promotion.getClaimForm() == null )
    {
      return;
    }

    ClaimForm claimForm = promotion.getClaimForm();
    if ( claimForm == null || !claimForm.hasCustomFormElements() )
    {
      return;
    }

    form.setClaimFormAsset( claimForm.getCmAssetCode() );
    form.setClaimFormId( claimForm.getId() );

    List<ClaimFormStep> claimFormSteps = claimForm.getClaimFormSteps();
    List<ClaimElementForm> claimElementForms = new ArrayList<ClaimElementForm>();
    for ( ClaimFormStep claimFormStep : claimFormSteps )
    {
      form.setClaimFormStepId( claimFormStep.getId() );

      for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
      {
        ClaimElement claimElement = new ClaimElement();
        claimElement.setClaimFormStepElement( claimFormStepElement );

        ClaimElementForm claimElementForm = new ClaimElementForm();
        claimElementForm.setClaimFormAssetCode( claimForm.getCmAssetCode() );
        claimElementForm.setClaimFormId( claimForm.getId() );
        claimElementForm.load( claimElement );

        ClaimFormElementType claimFormElementType = claimFormStepElement.getClaimFormElementType();
        if ( claimFormElementType.isSelectField() || claimFormElementType.isMultiSelectField() )
        {
          claimElementForm.setPickList( DynaPickListType.getList( claimFormStepElement.getSelectionPickListName() ) );
        }

        if ( claimFormElementType.isAddressBlock() )
        {
          if ( CustomerInformationBlock.MAIN_ADDRESS_1_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() )
              || CustomerInformationBlock.ADDITIONAL_ADDRESS_2_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() ) )
          {
            // this is to take care of address1
            claimElementForm.getMainAddressFormBean()
                .setRequiredPostalCode( getCountryService().getCountryByCode( claimElementForm.getMainAddressFormBean().getCountryCode() ).getRequirePostalCode() );
          }
        }

        claimElementForms.add( claimElementForm );
      }

    }

    if ( !claimElementForms.isEmpty() )
    {
      form.addClaimElementForms( promotion.getId(), claimElementForms );
    }

  }

  @SuppressWarnings( "unchecked" )
  private void populateRecipientInfo( SendRecognitionForm state, Promotion promotion )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
    arc.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    arc.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );

    if ( CollectionUtils.isNotEmpty( state.getRecipients() ) )
    {
      for ( int i = 0; i < state.getRecipients().size(); i++ )
      {
        RecipientBean recipientBean = state.getClaimRecipientFormBeans( i );
        Long recipientId = recipientBean.getUserId();
        Long recipientPrimaryNodeId = null;
        Participant pax = getParticipantService().getParticipantByIdWithAssociations( recipientId, arc );

        String nodes = "";

        Set<UserNode> userNodes = pax.getUserNodes();
        List<NameableBean> beans = new ArrayList<NameableBean>();

        for ( UserNode userNode : userNodes )
        {
          Node node = userNode.getNode();
          if ( userNode.getIsPrimary() )
          {
            recipientPrimaryNodeId = node.getId();

          }

          if ( !node.isDeleted() )
          {
            beans.add( new NameableBean( node.getId(), node.getName() ) );
          }
        }

        nodes = toJson( beans ).toString();
        if ( StringUtils.isEmpty( nodes ) )
        {
          nodes = toJson( new ArrayList<NameableBean>() ).toString();
        }

        recipientBean.setNodeId( recipientPrimaryNodeId );
        recipientBean.setUserId( pax.getId() );
        recipientBean.setFirstName( pax.getFirstName() );
        recipientBean.setLastName( pax.getLastName() );
        recipientBean.setCountryCode( pax.getPrimaryCountryCode() );
        recipientBean.setCountryName( pax.getPrimaryCountryName() );
        recipientBean.setJobName( pax.getPaxJobName() );
        recipientBean.setDepartmentName( pax.getPaxDeptName() );
        recipientBean.setNodes( nodes );
        recipientBean.setOptOutAwards( pax.getOptOutAwards() );
        recipientBean.setAvatarUrl( pax.getAvatarSmall() );

        if ( Objects.nonNull( promotion ) )
        {
          boolean calculateBudgetRatio = promotion.getBudgetMaster() != null && !promotion.getBudgetMaster().isCentralBudget();
          recipientBean.setCountryRatio( calculateBudgetRatio ? BudgetUtils.getBudgetConversionRatio( getUserService(), recipientId, UserManager.getUserId() ).doubleValue() : 1 );
        }

        if ( pax.getPrimaryEmailAddress() != null )
        {
          recipientBean.setEmailAddr( pax.getPrimaryEmailAddress().getEmailAddr() );
        }

        if ( Objects.nonNull( promotion ) && promotion.isAbstractRecognitionPromotion() )
        {
          AbstractRecognitionPromotion arp = (AbstractRecognitionPromotion)promotion;
          if ( arp.isAwardActive() && arp.isAwardAmountTypeFixed() )
          {
            if ( pax.getOptOutAwards() )
            {
              recipientBean.setAwardQuantity( 0L );
            }
            else
            {
              recipientBean.setAwardQuantity( arp.getAwardAmountFixed() );
            }
          }
        }
        // Client customizations for wip #26532 starts
        recipientBean.setPurlAllowOutsideDomains( pax.isAllowSharePurlToOutsiders() );
        // Client customizations for wip #26532 ends

        // Client customizations for wip #42701 starts
        recipientBean.setCurrency( getUserService().getUserCurrencyCharValue( pax.getId() ) );
        // Client customizations for wip #42701 ends
        /* coke customization start */
        //boolean optedOut = getParticipantService().isOptedOut( pax.getId() );
        //recipientBean.setOptinout( optedOut );
        
        if(promotion!=null && promotion.getAdihCashOption())
        {
          recipientBean.setAwardMin( 0L );
          recipientBean.setAwardMax( promotion.getAdihCashMaxAward() );
        }
        /* coke customization end */

      }
    }

  }

  private void populatePurlContributorSearchFilter( SendRecognitionForm state, Promotion promotion )
  {
    if ( Objects.isNull( promotion ) )
    {
      return;
    }

    if ( promotion instanceof RecognitionPromotion )
    {
      RecognitionPromotion rp = (RecognitionPromotion)promotion;
      if ( rp.isIncludePurl() && state.getClaimRecipientFormBeansCount() == 1 && state.getClaimRecipientFormBeans( 0 ).getNodeId() != null )
      {
        List<Node> childNodes = getNodeService().getNodeAndNodesBelow( state.getClaimRecipientFormBeans( 0 ).getNodeId() );
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
          if ( logger.isDebugEnabled() )
          {
            logger.debug( "\n\n\nERROR!!!\n\n\n" + t.getMessage() );
          }
        }
        state.setContributorTeamsSearchFilters( writer.toString() );
      }
    }
  }

  public void prepopulateRecogData( SendRecognitionForm state, HttpServletRequest request )
  {
    populatePrimaryNodeId( state );
    Long promotionId = state.getPromotionId();
    Promotion promotion = null;

    if ( Objects.nonNull( promotionId ) )
    {
      promotion = getPromotionService().getPromotionById( promotionId );
      state.setPromotionType( promotion.getPromotionType().getCode() );
    }

    populateRecipientInfo( state, promotion );
    populatePurlContributorSearchFilter( state, promotion );
    RecognitionStateManager.addToRequest( state, request );

  }

  public void populatePrimaryNodeId( SendRecognitionForm state )
  {
    Long userId = UserManager.getUserId();

    Set<UserNode> userNodes = getUserService().getUserNodes( userId );

    if ( userNodes.size() == 1 )
    {
      state.setNodeId( getUserService().getPrimaryUserNode( userId ).getNode().getId() );
    }
  }

  private void setClaimRecipientFormBeansForPurl( SendRecognitionForm form, Long reporteeId )
  {
    RecipientBean recipientBean = new RecipientBean();
    recipientBean.setUserId( reporteeId );
    form.setClaimRecipientFormBeansForPurl( 0, recipientBean );
  }
}
