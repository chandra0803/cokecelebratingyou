/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/productclaim/ProductClaimSubmissionAction.java,v $
 *
 */

package com.biperf.core.ui.productclaim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.claim.CustomerInformationBlock;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimFormStepAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.claim.ClaimElementForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.user.AddressFormBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.threads.CallableFactory;
import com.biperf.core.value.ClaimInfoBean;
import com.biperf.core.value.NodePreviewBean;
import com.biperf.core.value.ParticipantPreviewBean;
import com.biperf.core.value.ProductClaimBean;
import com.biperf.core.value.ProductClaimBean.ProductBean;
import com.biperf.core.value.ProductClaimBean.ProductBean.CharacteristicsValueBean;
import com.biperf.core.value.ProductClaimCharacteristicsPreviewBean;
import com.biperf.core.value.ProductClaimCharacteristicsPreviewBean.CharacteristicsTypeValuePreviewBean;
import com.biperf.core.value.ProductClaimPreviewBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.util.StringUtils;

/**
 * ProductClaimSubmissionAction.
 * 
 * @author arasi
 * @since Jul 29, 2013
 * @version 1.0
 */
public abstract class ProductClaimSubmissionAction extends BaseDispatchAction
{
  private static final String STATE_SESSION_KEY = ProductClaimSubmissionForm.class.getName() + ".SESSION_KEY";
  public static final String ADRESS_DELIMITER = "|";

  // ---------------------------------------------------------------------------
  // Claim Methods
  // ---------------------------------------------------------------------------

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return showClaim( mapping, form, request, response );
    }
  }

  public ActionForward newClaim( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    request.getSession().removeAttribute( STATE_SESSION_KEY );
    ProductClaimSubmissionForm productClaimSubmissionForm = new ProductClaimSubmissionForm();

    if ( ClientStateUtils.getClientStateMap( request ) != null )
    {
      String promotionId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" );
      if ( promotionId != null )
      {
        productClaimSubmissionForm.setPromotionId( new Long( promotionId ) );
      }
    }
    return showClaim( mapping, productClaimSubmissionForm, request, response );
  }

  public ActionForward showClaim( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final ProductClaimSubmissionForm productClaimSubmissionForm = (ProductClaimSubmissionForm)form;

    final Long USER_ID = UserManager.getUserId();
    final boolean IS_USER_A_PARTICIPANT = UserManager.getUser().isParticipant();
    final List eligiblePromotion = getEligiblePromotions( request );
    final ProductClaimStartBean startBean = new ProductClaimStartBean();

    if ( productClaimSubmissionForm.getClaimId() != null )
    {
      ActionMessages errors = new ActionMessages();
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

        // ---------------------------------------------------
        // Remove any 'old' claim submission from the session
        // ---------------------------------------------------
        request.getSession().removeAttribute( STATE_SESSION_KEY );

        // --------------------------------------
        // Get the ClaimId of the request
        // --------------------------------------
        final Long claimId = (Long)clientStateMap.get( "claimId" );
        if ( claimId == null )
        {
          errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimId as part of clientState" ) );
          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.FAIL_UPDATE );
        }
        request.getSession().setAttribute( "claimId", claimId );

        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
        final ProductClaim claim = (ProductClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
        productClaimSubmissionForm.setPromotionId( claim.getPromotion().getId() );

        ArrayList<ParticipantPreviewBean> participants = new ArrayList<ParticipantPreviewBean>();
        Map<Integer, NodePreviewBean> nodes = new HashMap<Integer, NodePreviewBean>();
        int paxCount = 0;
        int productCount = 0;
        int nodeCount = 0;
        int pcharCount = 0;
        int charCount = 0;

        for ( ProductClaimParticipant productClaimParticipant : claim.getClaimParticipants() )
        {
          ParticipantPreviewBean participantPreviewBean = new ParticipantPreviewBean();
          participantPreviewBean.setId( productClaimParticipant.getParticipant().getId() );
          participantPreviewBean.setAllowPublicInformation( productClaimParticipant.getParticipant().isAllowPublicInformation() );
          participantPreviewBean.setAllowPublicRecognition( productClaimParticipant.getParticipant().isAllowPublicRecognition() );
          participantPreviewBean.setAvatarUrl( productClaimParticipant.getParticipant().getAvatarSmallFullPath() );
          participantPreviewBean.setDepartmentName( productClaimParticipant.getParticipant().getDepartmentType() );
          participantPreviewBean.setFirstName( productClaimParticipant.getParticipant().getFirstName() );
          participantPreviewBean.setLastName( productClaimParticipant.getParticipant().getLastName() );
          participantPreviewBean.setJobName( productClaimParticipant.getParticipant().getPaxJobName() );
          for ( UserNode userNode : productClaimParticipant.getParticipant().getUserNodes() )
          {
            NodePreviewBean nodePreviewBean = new NodePreviewBean();
            nodePreviewBean.setId( userNode.getNode().getId() );
            nodePreviewBean.setName( userNode.getNode().getName() );
            // participantPreviewBean.setNodeValues( nodePreviewBean );
            // nodes.put( nodeCount, nodePreviewBean );
            nodeCount++;
          }
          participantPreviewBean.setNodes( nodes );
          participantPreviewBean.setOrgName( productClaimParticipant.getParticipant().getPaxOrgName() );
          participantPreviewBean.setProfileUrl( productClaimParticipant.getParticipant().getAvatarOriginalFullPath() );

          paxCount++;

          productClaimSubmissionForm.setParticipants( participantPreviewBean );
        }

        for ( Iterator iter = claim.getClaimProducts().iterator(); iter.hasNext(); )
        {
          ProductClaimPreviewBean productClaimPreviewBean = new ProductClaimPreviewBean();
          ClaimProduct product = (ClaimProduct)iter.next();

          productClaimPreviewBean.setProductid( product.getProduct().getId() );
          productClaimPreviewBean.setName( product.getProduct().getName() );
          productClaimPreviewBean.setCategory( product.getProduct().getProductCategoryName() );
          productClaimPreviewBean.setSubcategory( product.getProduct().getProductSubCategoryName() );
          productClaimPreviewBean.setQuantity( (long)product.getQuantity() );

          for ( Iterator charIter = product.getClaimProductCharacteristics().iterator(); charIter.hasNext(); )
          {
            ProductClaimCharacteristicsPreviewBean productClaimCharacteristicsPreviewBean = new ProductClaimCharacteristicsPreviewBean();
            ClaimProductCharacteristic claimProductCharacteristic = (ClaimProductCharacteristic)charIter.next();
            if ( claimProductCharacteristic.getProductCharacteristicType() != null )
            {
              productClaimCharacteristicsPreviewBean.setId( claimProductCharacteristic.getProductCharacteristicType().getId() );
              productClaimCharacteristicsPreviewBean.setDateEnd( claimProductCharacteristic.getProductCharacteristicType().getDisplayDateEnd() );
              productClaimCharacteristicsPreviewBean.setDateStart( claimProductCharacteristic.getProductCharacteristicType().getDisplayDateStart() );
              if ( claimProductCharacteristic.getProductCharacteristicType().getMaxValue() != null )
              {
                productClaimCharacteristicsPreviewBean.setMax( claimProductCharacteristic.getProductCharacteristicType().getMaxValue().longValue() );
              }
              productClaimCharacteristicsPreviewBean.setMaxSize( claimProductCharacteristic.getProductCharacteristicType().getMaxSize() );
              if ( claimProductCharacteristic.getProductCharacteristicType().getMinValue() != null )
              {
                productClaimCharacteristicsPreviewBean.setMin( claimProductCharacteristic.getProductCharacteristicType().getMinValue().longValue() );
              }
              productClaimCharacteristicsPreviewBean.setName( claimProductCharacteristic.getProductCharacteristicType().getCharacteristicName() );
              productClaimCharacteristicsPreviewBean.setValue( claimProductCharacteristic.getValue() );

              if ( claimProductCharacteristic.getProductCharacteristicType().getPlName() != null )
              {
                if ( !DynaPickListType.getList( claimProductCharacteristic.getProductCharacteristicType().getPlName() ).isEmpty() )
                {
                  int index = 0;
                  for ( Iterator listIter = DynaPickListType.getList( claimProductCharacteristic.getProductCharacteristicType().getPlName() ).iterator(); listIter.hasNext(); )
                  {
                    CharacteristicsTypeValuePreviewBean characteristicsTypeValuePreviewBean = new CharacteristicsTypeValuePreviewBean();
                    DynaPickListType dynaPickListType = (DynaPickListType)listIter.next();
                    characteristicsTypeValuePreviewBean.setId( dynaPickListType.getCode() );
                    characteristicsTypeValuePreviewBean.setName( dynaPickListType.getName() );
                    index++;
                  }
                }
              }
              productClaimCharacteristicsPreviewBean.setRequired( claimProductCharacteristic.getProductCharacteristicType().getIsRequired() );
              if ( claimProductCharacteristic.getProductCharacteristicType().getCharacteristicDataType() != null )
              {
                productClaimCharacteristicsPreviewBean.setType( claimProductCharacteristic.getProductCharacteristicType().getCharacteristicDataType().getName() );
              }
              productClaimCharacteristicsPreviewBean.setUnique( claimProductCharacteristic.getProductCharacteristicType().getIsUnique() );
            }
            productClaimPreviewBean.setCharacteristicsValues( productClaimCharacteristicsPreviewBean );
            pcharCount++;
          }
          productClaimSubmissionForm.setProducts( productClaimPreviewBean );
          productCount++;
        }

        ArrayList<ClaimElementForm> claimElements = new ArrayList<ClaimElementForm>();

        for ( ClaimElement claimElement : claim.getClaimElements() )
        {
          ClaimElementForm claimElementForm = new ClaimElementForm();

          claimElementForm.setValue( claimElement.getValue() );
          claimElementForm.setClaimElementId( claimElement.getId() );
          claimElementForm.setClaimFormStepElement( claimElement.getClaimFormStepElement() );
          claimElementForm.setClaimFormStepElementId( claimElement.getClaimFormStepElement().getId() );
          claimElementForm.setPickListItems( claimElement.getPickListItems() );
          AddressFormBean addressFormBean = new AddressFormBean();
          if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isAddressBlock() )
          {
            if ( !StringUtils.isEmpty( claimElement.getValue() ) )
            {
              String addressValue = claimElement.getValue();
              addressFormBean.setCountryCode( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
              addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
              addressFormBean.setAddr1( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
              addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
              addressFormBean.setAddr2( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
              addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
              addressFormBean.setAddr3( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
              addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );

              addressFormBean.setCity( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
              addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
              addressFormBean.setStateTypeCode( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
              addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
              addressFormBean.setPostalCode( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
            }
            claimElementForm.setMainAddressFormBean( addressFormBean );
          }
          claimElements.add( claimElementForm );
          productClaimSubmissionForm.setClaimElements( claimElementForm );
          charCount++;
        }

        Long selectedNode = null;
        try
        {
          selectedNode = (Long)clientStateMap.get( "selectedNode" );
        }
        catch( ClassCastException cce )
        {
          String selNode = (String)clientStateMap.get( "selectedNode" );
          selectedNode = new Long( selNode );
        }

        final Node theSelectedNode = getNodeService().getNodeById( selectedNode );

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
            getProductClaimPromotions( USER_ID, IS_USER_A_PARTICIPANT, startBean, productClaimSubmissionForm, eligiblePromotion );
            return null;
          }

        } ) );

        callables.add( CallableFactory.createCallable( new Callable<Object>()
        {
          public Object call() throws Exception
          {
            getClaimInfo( claim, theSelectedNode, startBean );
            return null;
          }

        } ) );

        try
        {
          getExecutorService().invokeAll( callables );
        }
        catch( InterruptedException ie )
        {
          // log.error( "\n\nERROR in " + getClass().getName() +
          // " when calling executorService.invokeAll: " + ie.toString() );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
      productClaimSubmissionForm.setInitializationJson( toJson( startBean ) );
      request.getSession().setAttribute( STATE_SESSION_KEY, productClaimSubmissionForm );
    }

    else
    {
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
          getProductClaimPromotions( USER_ID, IS_USER_A_PARTICIPANT, startBean, productClaimSubmissionForm, eligiblePromotion );
          return null;
        }

      } ) );

      try
      {
        getExecutorService().invokeAll( callables );
      }
      catch( InterruptedException ie )
      {
        // log.error( "\n\nERROR in " + getClass().getName() +
        // " when calling executorService.invokeAll: " + ie.toString() );
      }
      productClaimSubmissionForm.setInitializationJson( toJson( startBean ) );
    }
    ProductClaimStateManager.addToRequest( getProductClaimState( request ), request );

    request.setAttribute( "productClaimSubmissionForm", productClaimSubmissionForm );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  protected abstract ProductClaimSubmissionForm getProductClaimState( HttpServletRequest request );

  public abstract ProductClaimSubmissionForm getProductClaimForm( HttpServletRequest request );

  private void getUserNodes( Long userId, ProductClaimStartBean startBean )
  {
    Set<UserNode> userNodes = getUserService().getUserNodes( userId );
    List<UserNode> userNodesList = null;
    if ( userNodes != null && !userNodes.isEmpty() )
    {
      userNodesList = new ArrayList<UserNode>( userNodes );
    }
    startBean.setUserNodes( userNodesList );
  }

  private void getProductClaimPromotions( Long userId, boolean isUserAParticipant, ProductClaimStartBean startBean, ProductClaimSubmissionForm form, List eligiblePromotion )
  {
    List<ProductClaimBean> promotions = getPromotions( userId, isUserAParticipant, eligiblePromotion );

    for ( ProductClaimBean promotion : promotions )
    {
      setClaimFormElements( promotion, form );
    }

    startBean.setProductClaimPromotions( promotions );
  }

  private void getClaimInfo( ProductClaim claim, Node theSelectedNode, ProductClaimStartBean startBean )
  {
    ClaimInfoBean claimBean = new ClaimInfoBean( claim, theSelectedNode );
    startBean.setClaimInfo( claimBean );
    // SelectedNodeBean selectedNodeBean = claimBean.SelectedNodeBean( theSelectedNode );
  }

  protected List<ProductClaimBean> getPromotions( Long userId, boolean isUserAParticipant, List eligiblePromotions )
  {
    PromotionService promotionService = getPromotionService();
    List<PromotionMenuBean> validPromotions = new ArrayList<PromotionMenuBean>();
    for ( int i = 0; i < eligiblePromotions.size(); i++ )
    {
      PromotionMenuBean promoBean = (PromotionMenuBean)eligiblePromotions.get( i );
      if ( promoBean.isCanSubmit() && promoBean.getPromotion().isProductClaimPromotion() )
      {
        validPromotions.add( promoBean );
      }
    }

    List<ProductClaimPromotion> claimSubmissionsList = new ArrayList<ProductClaimPromotion>();

    Iterator<PromotionMenuBean> it = validPromotions.iterator();
    while ( it.hasNext() )
    {
      PromotionMenuBean promotionMenuBean = it.next();

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.TEAM_POSITIONS ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
      associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_PARTICIPANTS ) );
      Promotion promotion = promotionService.getPromotionByIdWithAssociations( promotionMenuBean.getPromotion().getId(), associationRequestCollection );
      claimSubmissionsList.add( (ProductClaimPromotion)promotion );
    }

    List<ProductClaimBean> beans = new ArrayList<ProductClaimBean>( claimSubmissionsList.size() );
    for ( ProductClaimPromotion claimPromotion : claimSubmissionsList )
    {
      // get the existing one in the eligible list
      Long promotionId = claimPromotion.getId();
      boolean isViewWebRules = false;
      for ( PromotionMenuBean eligiblePromotion : validPromotions )
      {
        if ( promotionId.equals( eligiblePromotion.getPromotion().getId() ) )
        {
          isViewWebRules = eligiblePromotion.isCanViewRules();
          break;
        }
      }
      if ( claimPromotion.isWebRulesActive() && isViewWebRules )
      {
        beans.add( new ProductClaimBean( claimPromotion, claimPromotion.getWebRulesText() ) );
      }
      else
      {
        beans.add( new ProductClaimBean( claimPromotion ) );
      }
    }

    // Alpha sort product characteristics - Bug 52008

    for ( ProductClaimBean productClaimBean : beans )
    {
      if ( productClaimBean.getProducts() != null )
      {
        for ( ProductBean productBean : productClaimBean.getProducts() )
        {
          Collections.sort( productBean.getCharacteristics(), new ProductCharacteristicsComparator() );
        }
      }
    }

    // End Bug 52008

    return beans;
  }

  public class ProductCharacteristicsComparator implements Comparator<CharacteristicsValueBean>
  {
    public int compare( CharacteristicsValueBean o1, CharacteristicsValueBean o2 )
    {
      return ( (Comparable<String>)o1.getName() ).compareTo( o2.getName() );
    }
  }

  private void setClaimFormElements( ProductClaimBean promotion, ProductClaimSubmissionForm form )
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

  /**
   * This method is used for preview of the form step elements in the admin
   * Realized this code 
   */
  public ActionForward preparePreview( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ProductClaimSubmissionForm productClaimSubmissionForm = (ProductClaimSubmissionForm)form;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }

      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      Long claimFormStepId = (Long)clientStateMap.get( "claimFormStepId" );
      Long claimFormId = (Long)clientStateMap.get( "claimFormId" );
      String whyFlag = (String)clientStateMap.get( "why" );
      if ( claimFormStepId != null && claimFormId != null )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ClaimFormStepAssociationRequest() );

        ClaimFormStep claimFormStep = getClaimFormService().getClaimFormStepWithAssociations( claimFormStepId, associationRequestCollection );
        List<ClaimElementForm> claimElementForms = new ArrayList<ClaimElementForm>();
        productClaimSubmissionForm.setClaimFormStepId( claimFormStep.getId() );
        productClaimSubmissionForm.setWhy( whyFlag );
        for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
        {
          ClaimElement claimElement = new ClaimElement();
          claimElement.setClaimFormStepElement( claimFormStepElement );

          ClaimElementForm claimElementForm = new ClaimElementForm();
          claimElementForm.setClaimFormAssetCode( claimFormStep.getClaimForm().getCmAssetCode() );
          claimElementForm.setClaimFormId( claimFormId );
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

        if ( !claimElementForms.isEmpty() )
        {
          productClaimSubmissionForm.addClaimElementForms( 1L, claimElementForms );
        }
      }
      else
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimFormId and claimFormStepId as part of clientState" ) );
        saveErrors( request, errors );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Claim service.
   * 
   * @return a reference to the Claim service.
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  /**
   * Returns a reference to the User service.
   * 
   * @return a reference to the User service.
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  protected ExecutorService getExecutorService()
  {
    return (ExecutorService)BeanLocator.getBean( "executorService" );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

}
