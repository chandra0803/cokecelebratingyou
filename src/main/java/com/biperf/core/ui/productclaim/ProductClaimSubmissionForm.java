/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/productclaim/ProductClaimSubmissionForm.java,v $
 */

package com.biperf.core.ui.productclaim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.product.ProductCharacteristicService;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.CMMessage;
import com.biperf.core.ui.MethodMap;
import com.biperf.core.ui.claim.ClaimElementForm;
import com.biperf.core.ui.claim.ClaimParticipantForm;
import com.biperf.core.ui.claim.ClaimProductForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.CharacteristicValidationUtils;
import com.biperf.core.value.CharacteristicValueBean;
import com.biperf.core.value.ParticipantPreviewBean;
import com.biperf.core.value.ProductClaimCharacteristicsPreviewBean;
import com.biperf.core.value.ProductClaimPreviewBean;

/**
 * ProductClaimSubmissionForm <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductClaimSubmissionForm extends BaseForm implements Serializable
{
  public static final String FORM_NAME = "productClaimSubmissionForm";

  private List<CMMessage> errors;

  private String initializationJson = "";

  private Long promotionId;
  private String promotionName;
  private Long nodeId;
  private String orgUnitName;
  private String claimFormAsset;
  private Long claimFormId;
  private Long claimFormStepId;
  private Map<Long, List<ClaimElementForm>> claimElementForms;
  private ArrayList<ClaimElementForm> claimElement = new ArrayList<ClaimElementForm>();
  private MethodMap claimElementValue;
  private Long claimId;
  private String claimNumber;
  private Long submitterId;
  private Long proxyUserId;
  private String why;

  Map<Long, List<ClaimProductForm>> claimProductForms;
  private Map<Integer, ClaimProductForm> claimProducts = new HashMap<Integer, ClaimProductForm>();
  private MethodMap claimProductValue;

  Map<Long, List<ClaimParticipantForm>> claimParticipantForms;
  private Map<Integer, ClaimParticipantForm> claimParticipants = new HashMap<Integer, ClaimParticipantForm>();

  private ArrayList<ProductClaimPreviewBean> product = new ArrayList<ProductClaimPreviewBean>();
  private ArrayList<ParticipantPreviewBean> participants = new ArrayList<ParticipantPreviewBean>();

  // ---------------------------------------------------------------------------
  // Load and To Domain Object Methods
  // ---------------------------------------------------------------------------
  /**
   * 
   */
  public ProductClaimSubmissionForm()
  {
    super();
  }

  public List<CMMessage> getErrors()
  {
    return errors;
  }

  public void setErrors( List<CMMessage> errors )
  {
    this.errors = errors;
  }

  public String getInitializationJson()
  {
    return initializationJson;
  }

  public void setInitializationJson( String initializationJson )
  {
    this.initializationJson = initializationJson;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getOrgUnitName()
  {
    return orgUnitName;
  }

  public void setOrgUnitName( String orgUnitName )
  {
    this.orgUnitName = orgUnitName;
  }

  public String getClaimFormAsset()
  {
    return claimFormAsset;
  }

  public void setClaimFormAsset( String claimFormAsset )
  {
    this.claimFormAsset = claimFormAsset;
  }

  public Long getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( Long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public Long getClaimFormStepId()
  {
    return claimFormStepId;
  }

  public void setClaimFormStepId( Long claimFormStepId )
  {
    this.claimFormStepId = claimFormStepId;
  }

  public void addClaimElementForms( Long promotionId, List<ClaimElementForm> claimFormStepElements )
  {
    if ( claimElementForms == null )
    {
      claimElementForms = new HashMap<Long, List<ClaimElementForm>>();
    }
    claimElementForms.put( promotionId, claimFormStepElements );
  }

  public Map<Long, List<ClaimElementForm>> getClaimElementForms()
  {
    return claimElementForms;
  }

  public void setClaimElementForms( Map<Long, List<ClaimElementForm>> claimElementForms )
  {
    this.claimElementForms = claimElementForms;
  }

  public int getClaimElementCount()
  {
    if ( claimElement == null || claimElement.isEmpty() )
    {
      return 0;
    }
    return claimElement.size();
  }

  public List<ClaimElementForm> getClaimElements()
  {
    return claimElement;
  }

  public ClaimElementForm getClaimElement( int index )
  {
    while ( index >= claimElement.size() )
    {
      claimElement.add( new ClaimElementForm() );
    }
    return (ClaimElementForm)claimElement.get( index );
  }

  public MethodMap getClaimProductValue()
  {
    return claimProductValue;
  }

  public void setClaimProductValue( MethodMap claimProductValue )
  {
    this.claimProductValue = claimProductValue;
  }

  public void addClaimProductForms( Long promotionId, List<ClaimProductForm> claimProductForm )
  {
    if ( claimProductForms == null )
    {
      claimProductForms = new HashMap<Long, List<ClaimProductForm>>();
    }
    claimProductForms.put( promotionId, claimProductForm );
  }

  public Map<Long, List<ClaimProductForm>> getClaimProductForms()
  {
    return claimProductForms;
  }

  public void setClaimProductForms( Map<Long, List<ClaimProductForm>> claimProductForms )
  {
    this.claimProductForms = claimProductForms;
  }

  public Map<Integer, ClaimProductForm> getClaimProducts()
  {
    return claimProducts;
  }

  public void setClaimProducts( Map<Integer, ClaimProductForm> claimProducts )
  {
    this.claimProducts = claimProducts;
  }

  public Map<Long, List<ClaimParticipantForm>> getClaimParticipantForms()
  {
    return claimParticipantForms;
  }

  public void setClaimParticipantForms( Map<Long, List<ClaimParticipantForm>> claimParticipantForms )
  {
    this.claimParticipantForms = claimParticipantForms;
  }

  public Map<Integer, ClaimParticipantForm> getClaimParticipants()
  {
    return claimParticipants;
  }

  public void setClaimParticipants( Map<Integer, ClaimParticipantForm> claimParticipants )
  {
    this.claimParticipants = claimParticipants;
  }

  public List<ClaimProductForm> getClaimProductsList()
  {
    return new ArrayList<ClaimProductForm>( claimProducts.values() );
  }

  public int getProductCount()
  {
    if ( product == null || product.isEmpty() )
    {
      return 0;
    }
    return product.size();
  }

  public ProductClaimPreviewBean getProduct( int index )
  {
    while ( index >= product.size() )
    {
      product.add( new ProductClaimPreviewBean() );
    }
    return (ProductClaimPreviewBean)product.get( index );
  }

  public ArrayList<ProductClaimPreviewBean> getProducts()
  {
    return product;
  }

  public void setProducts( ProductClaimPreviewBean productBean )
  {
    this.product.add( productBean );
  }

  public int getParticipantsCount()
  {
    if ( participants == null || participants.isEmpty() )
    {
      return 0;
    }
    return participants.size();
  }

  public ParticipantPreviewBean getParticipant( int index )
  {
    while ( index >= participants.size() )
    {
      participants.add( new ParticipantPreviewBean() );
    }
    return (ParticipantPreviewBean)participants.get( index );
  }

  public void setParticipants( ParticipantPreviewBean participant )
  {
    this.participants.add( participant );
  }

  public List<ParticipantPreviewBean> getTeamMembers()
  {
    return participants;
  }

  public void setClaimElements( ClaimElementForm claimElement )
  {
    this.claimElement.add( claimElement );
  }

  public MethodMap getClaimElementValue()
  {
    return claimElementValue;
  }

  public void setClaimElementValue( MethodMap claimElementValue )
  {
    this.claimElementValue = claimElementValue;
  }

  /**
   * Converts a {@link ProductClaimSubmissionForm} object to a {@link Claim} object.
   * 
   * @return a {@link Claim} object based on this {@link ProductClaimSubmissionForm} object.
   */
  public ProductClaim toDomainObject( ProductClaim claim1, Promotion promotion, Node paxSelectedNode )
  {
    ProductClaimPromotion claimPromotion = (ProductClaimPromotion)promotion;
    ProductClaim claim = null;
    if ( claim1 == null )
    {
      claim = new ProductClaim();
    }
    else
    {
      claim = claim1;
    }
    claim.setPromotion( promotion );
    claim.setOpen( true );

    // Add ClaimElement objects to the Claim object.
    claim.getClaimElements().clear();
    ClaimFormDefinitionService claimFormDefinitionService = getClaimFormDefinitionService();
    for ( int i = 0; i < this.claimElement.size(); i++ )
    {
      // Add ClaimElement objects to the Claim object.
      ClaimElementForm claimElementForm = (ClaimElementForm)this.claimElement.get( i );
      claimElementForm.setClaimFormStepElement( claimFormDefinitionService.getClaimFormStepElementById( claimElementForm.getClaimFormStepElementId() ) );
      ClaimElement claimElement = claimElementForm.toDomainObject();
      claim.addClaimElement( claimElement );
    }

    // Add ClaimProduct objects to the Claim object.
    claim.getClaimProducts().clear();
    ProductService productService = getProductService();
    ProductCharacteristicService productCharacteristicService = getCharacteristicService();
    for ( int i = 0; i < product.size(); i++ )
    {
      ProductClaimPreviewBean productClaimPreviewBean = (ProductClaimPreviewBean)this.product.get( i );

      ClaimProductForm claimProductForm = new ClaimProductForm();
      claimProductForm.setProduct( productService.getProductById( productClaimPreviewBean.getProductid() ) );
      if ( claimPromotion.getDefaultQuantity() != null && claimPromotion.getDefaultQuantity().intValue() > 0 )
      {
        claimProductForm.setQuantity( String.valueOf( claimPromotion.getDefaultQuantity().intValue() ) );
      }
      else
      {
        claimProductForm.setQuantity( productClaimPreviewBean.getQuantity().toString() );
      }

      List claimProductCharacteristicValueList = new ArrayList();
      for ( int c = 0; c < productClaimPreviewBean.getCharacteristicsCount(); c++ )
      {
        ProductClaimCharacteristicsPreviewBean characteristicBean = productClaimPreviewBean.getCharacteristics( c );

        CharacteristicValueBean formBean = new CharacteristicValueBean();
        ProductCharacteristicType productCharacteristicType = (ProductCharacteristicType)productCharacteristicService.getCharacteristicById( characteristicBean.getId() );
        CharacteristicValidationUtils.loadCharacteristicValueBeanData( formBean, productCharacteristicType, null );
        formBean.setIsUnique( productCharacteristicType.getIsUnique() );
        formBean.setVersion( productCharacteristicType.getVersion() );
        formBean.setCharacteristicValue( characteristicBean.getValue() );
        formBean.setCharacteristicValues( characteristicBean.getValues() );

        claimProductCharacteristicValueList.add( formBean );
      }

      claimProductForm.setClaimProductCharacteristicValueList( claimProductCharacteristicValueList );
      ClaimProduct claimProduct = claimProductForm.toDomainObject();
      claim.addClaimProduct( claimProduct );
    }

    // Add ProductClaimParticipant objects to the Claim object
    claim.getClaimParticipants().clear();
    NodeService nodeService = getNodeService();
    ParticipantService participantService = getParticipantService();
    for ( int j = 0; j < participants.size(); j++ )
    {
      ParticipantPreviewBean participantPreviewBean = (ParticipantPreviewBean)this.participants.get( j );

      Node nodeValues = new Node();
      nodeValues = getUserService().getPrimaryUserNode( participantPreviewBean.getId() ).getNode();

      ClaimParticipantForm claimParticipantForm = new ClaimParticipantForm();
      claimParticipantForm.setParticipant( participantService.getParticipantById( participantPreviewBean.getId() ) );
      claimParticipantForm.setNode( nodeValues );

      if ( claimParticipantForm.getParticipant() != null )
      {
        ProductClaimParticipant productClaimParticipant = new ProductClaimParticipant();
        productClaimParticipant.setParticipant( claimParticipantForm.getParticipant() );
        Node node = claimParticipantForm.getNode();
        if ( node == null )
        {
          throw new BeaconRuntimeException( "no nodeId set for claim user, validation should have caught this" );
        }

        productClaimParticipant.setNode( node );
        productClaimParticipant.setPromotionTeamPosition( claimParticipantForm.getPromotionTeamPosition() );
        claim.addClaimParticipant( productClaimParticipant );
      }
    }

    // Add the selected Node object to the Claim object
    claim.setNode( paxSelectedNode );

    return claim;
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private static ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)BeanLocator.getBean( ClaimFormDefinitionService.BEAN_NAME );
  }

  private ProductService getProductService()
  {
    return (ProductService)BeanLocator.getBean( ProductService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  protected ProductCharacteristicService getCharacteristicService()
  {
    return (ProductCharacteristicService)BeanLocator.getBean( ProductCharacteristicService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getClaimNumber()
  {
    return claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
  }

  public Long getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  public Long getProxyUserId()
  {
    return proxyUserId;
  }

  public void setProxyUserId( Long proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  public String getWhy()
  {
    return why;
  }

  public void setWhy( String why )
  {
    this.why = why;
  }

}
