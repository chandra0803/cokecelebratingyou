/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/ProductClaimImportStrategy.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.FileImportApprovalType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ProductClaimImportFieldRecord;
import com.biperf.core.domain.fileload.ProductClaimImportProductRecord;
import com.biperf.core.domain.fileload.ProductClaimImportRecord;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCharacteristic;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.SalesPayoutStrategyUtil;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;

/*
 * ProductImportStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>jenniget</td> <td>Jan
 * 31, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ProductClaimImportStrategy extends ImportStrategy
{
  /**
   * Logger for this class
   */
  private static final Logger log = Logger.getLogger( ProductClaimImportStrategy.class );

  private ClaimService claimService;
  private ParticipantService participantService;
  private NodeService nodeService;
  private HierarchyService hierarchyService;
  private PromotionService promotionService;
  private ProductService productService;

  private ProductClaim createClaim( ProductClaimImportRecord productClaimRecord, ProductClaimPromotion promotion, List errors )
  {
    ProductClaim claim = new ProductClaim();

    claim.setSubmissionDate( new Date() );

    claim.setSubmitter( participantService.getParticipantById( productClaimRecord.getSubmitterUserId() ) );

    if ( productClaimRecord.getTrackToNodeId() != null )
    {
      claim.setNode( nodeService.getNodeById( productClaimRecord.getTrackToNodeId() ) );

      // confirm user is in node and node is in primary hierarchy
      UserNode userNodeForTrackToNode = claim.getSubmitter().getUserNodeByNodeId( claim.getNode().getId() );
      if ( userNodeForTrackToNode == null )
      {
        errors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_TRACK_TO_NODE_NOT_IN_USERS_NODE ) );
      }
      else
      {
        // make sure node exists in primary hierarchy
        if ( !userNodeForTrackToNode.getNode().getHierarchy().equals( hierarchyService.getPrimaryHierarchy() ) )
        {
          errors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_TRACK_TO_NODE_NOT_IN_PRIMARY_HIERARCHY ) );
        }
      }
    }
    else
    {
      if ( claim.getSubmitter().getUserNodesCount() != 1 )
      {
        // TrackToNode is required when submitter associated with more than 1 node
        errors.add( new ServiceError( "system.errors.REQUIRED", "Node Name" ) );
      }
      else
      {
        claim.getSubmitter().getUserNodes();
        for ( Iterator iter = claim.getSubmitter().getUserNodes().iterator(); iter.hasNext(); )
        {
          Node node = ( (UserNode)iter.next() ).getNode();

          // make sure node exists in primary hierarchy
          if ( node.getHierarchy().equals( hierarchyService.getPrimaryHierarchy() ) )
          {
            claim.setNode( node );
          }
          else
          {
            errors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_SUBMITTER_NOT_PART_OF_PRIMARY_HIERARCHY ) );
          }
        }
      }
    }

    claim.setPromotion( promotion );

    populateClaimElements( productClaimRecord, promotion, claim, errors );

    populateClaimProducts( productClaimRecord, claim, errors );

    return claim;
  }

  private void populateClaimProducts( ProductClaimImportRecord productClaimRecord, ProductClaim claim, List errors )
  {
    for ( Iterator iter = productClaimRecord.getProductClaimImportProductRecords().iterator(); iter.hasNext(); )
    {
      ProductClaimImportProductRecord productClaimImportProductRecord = (ProductClaimImportProductRecord)iter.next();
      ClaimProduct claimProduct = new ClaimProduct();

      Product product = productService.getProductById( productClaimImportProductRecord.getProductId() );

      claimProduct.setProduct( product );
      claimProduct.setQuantity( productClaimImportProductRecord.getSoldQuantity() == null ? 0 : productClaimImportProductRecord.getSoldQuantity().intValue() );
      claimProduct.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );

      populateClaimProductCharacteristics( productClaimImportProductRecord, claimProduct, product, errors );

      claim.addClaimProduct( claimProduct );
    }
  }

  private void populateClaimProductCharacteristics( ProductClaimImportProductRecord productClaimImportProductRecord, ClaimProduct claimProduct, Product product, List errors )
  {
    Map productCharacteristicsKeyedById = createMapKeyedByCharacteristicId( product.getProductCharacteristics() );

    populateClaimProductCharacteristic( claimProduct,
                                        productCharacteristicsKeyedById,
                                        productClaimImportProductRecord.getProductCharacteristicId1(),
                                        productClaimImportProductRecord.getProductCharacteristicName1(),
                                        productClaimImportProductRecord.getProductCharacteristicValue1(),
                                        errors );

    populateClaimProductCharacteristic( claimProduct,
                                        productCharacteristicsKeyedById,
                                        productClaimImportProductRecord.getProductCharacteristicId2(),
                                        productClaimImportProductRecord.getProductCharacteristicName2(),
                                        productClaimImportProductRecord.getProductCharacteristicValue2(),
                                        errors );

    populateClaimProductCharacteristic( claimProduct,
                                        productCharacteristicsKeyedById,
                                        productClaimImportProductRecord.getProductCharacteristicId3(),
                                        productClaimImportProductRecord.getProductCharacteristicName3(),
                                        productClaimImportProductRecord.getProductCharacteristicValue3(),
                                        errors );

    populateClaimProductCharacteristic( claimProduct,
                                        productCharacteristicsKeyedById,
                                        productClaimImportProductRecord.getProductCharacteristicId4(),
                                        productClaimImportProductRecord.getProductCharacteristicName4(),
                                        productClaimImportProductRecord.getProductCharacteristicValue4(),
                                        errors );

    populateClaimProductCharacteristic( claimProduct,
                                        productCharacteristicsKeyedById,
                                        productClaimImportProductRecord.getProductCharacteristicId5(),
                                        productClaimImportProductRecord.getProductCharacteristicName5(),
                                        productClaimImportProductRecord.getProductCharacteristicValue5(),
                                        errors );

    // Fill in remaining product chars with empty values
    Map remainingProductCharacteristicsKeyedById = productCharacteristicsKeyedById;
    for ( Iterator iterator = remainingProductCharacteristicsKeyedById.values().iterator(); iterator.hasNext(); )
    {
      ProductCharacteristicType productCharacteristic = (ProductCharacteristicType)iterator.next();
      ClaimProductCharacteristic claimProductCharacteristic = new ClaimProductCharacteristic();
      claimProductCharacteristic.setProductCharacteristicType( productCharacteristic );
      claimProduct.addClaimProductCharacteristics( claimProductCharacteristic );
    }
  }

  private void populateClaimProductCharacteristic( ClaimProduct claimProduct,
                                                   Map productCharacteristicsKeyedById,
                                                   Long productCharacteristicId,
                                                   String productCharacteristicName,
                                                   String productCharacteristicValue,
                                                   List errors )
  {
    if ( productCharacteristicId == null )
    {
      // no product char value given, so don't create a claim prod char.
      return;
    }

    ProductCharacteristicType productCharacteristic = (ProductCharacteristicType)productCharacteristicsKeyedById.get( productCharacteristicId );
    if ( productCharacteristic != null )
    {
      ClaimProductCharacteristic claimProductCharacteristic = new ClaimProductCharacteristic();
      claimProductCharacteristic.setProductCharacteristicType( productCharacteristic );
      claimProductCharacteristic.setValue( productCharacteristicValue );
      claimProduct.addClaimProductCharacteristics( claimProductCharacteristic );

      // Remove so we know which product chars remain after record is processed.
      productCharacteristicsKeyedById.remove( productCharacteristicId );
    }
    else
    {
      // Characteristic record for a characteristic that is not for the given product
      errors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_PRODUCT_CHARACTERISTIC_NOT_VALID_FOR_PRODUCT, productCharacteristicName ) );
    }
  }

  private void populateClaimElements( ProductClaimImportRecord productClaimRecord, Promotion promotion, ProductClaim claim, List errors )
  {
    List allClaimFormStepElements = new ArrayList();

    List claimFormSteps = promotion.getClaimForm().getClaimFormSteps();
    for ( Iterator iter = claimFormSteps.iterator(); iter.hasNext(); )
    {
      ClaimFormStep claimFormStep = (ClaimFormStep)iter.next();
      allClaimFormStepElements.addAll( claimFormStep.getClaimFormStepElements() );
    }

    Map allClaimFormStepElementsKeyedByName = createMapKeyedByName( allClaimFormStepElements );

    Set productClaimImportFieldRecords = productClaimRecord.getProductClaimImportFieldRecords();
    // Build a ClaimElement for each import field record
    for ( Iterator iter = productClaimImportFieldRecords.iterator(); iter.hasNext(); )
    {
      ProductClaimImportFieldRecord importFieldRecord = (ProductClaimImportFieldRecord)iter.next();

      ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)allClaimFormStepElementsKeyedByName.get( importFieldRecord.getClaimFormStepElementName() );
      if ( claimFormStepElement != null )
      {
        ClaimElement claimElement = new ClaimElement();
        claimElement.setClaimFormStepElement( claimFormStepElement );
        claimElement.setValue( importFieldRecord.getClaimFormStepElementValue() );

        claim.addClaimElement( claimElement );

        // Remove used element so we know which weren't used
        allClaimFormStepElementsKeyedByName.remove( importFieldRecord.getClaimFormStepElementName() );
      }
      else
      {
        // ClaimFormElement record for an element that is not part of the claim form for the
        // selected promotion
        errors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_FORM_ELEMENT_NOT_PART_OF_FORM_FOR_PROMOTION, importFieldRecord.getClaimFormStepElementName() ) );
      }
    }

    // Add empty claim elements for each element not supplied in import
    Map remainingClaimFormStepElementsKeyedByName = allClaimFormStepElementsKeyedByName;
    for ( Iterator iterator = remainingClaimFormStepElementsKeyedByName.values().iterator(); iterator.hasNext(); )
    {
      ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)iterator.next();
      ClaimElement claimElement = new ClaimElement();
      claimElement.setClaimFormStepElement( claimFormStepElement );
      claim.addClaimElement( claimElement );
    }
  }

  private Map createMapKeyedByName( List allClaimFormStepElements )
  {
    Map elementsKeyedByName = new HashMap();
    if ( allClaimFormStepElements != null )
    {
      for ( Iterator iter = allClaimFormStepElements.iterator(); iter.hasNext(); )
      {
        ClaimFormStepElement element = (ClaimFormStepElement)iter.next();
        elementsKeyedByName.put( element.getI18nLabel(), element );
      }
    }

    return elementsKeyedByName;
  }

  private Map createMapKeyedByCharacteristicId( Set productCharacteristics )
  {
    Map elementsKeyedById = new HashMap();
    if ( productCharacteristics != null )
    {
      for ( Iterator iter = productCharacteristics.iterator(); iter.hasNext(); )
      {
        ProductCharacteristic prodChar = (ProductCharacteristic)iter.next();
        elementsKeyedById.put( prodChar.getProductCharacteristicType().getId(), prodChar.getProductCharacteristicType() );
      }
    }

    return elementsKeyedById;
  }

  /**
   * Verifies the specified import file records.
   * 
   * @param importFile the import file to import.
   * @param records the records to import.
   */
  public void verifyImportFile( ImportFile importFile, List records )
  {
    // start out with the number of current records with errors
    int importRecordErrorCount = importFile.getImportRecordErrorCount();

    long counter = 0;
    log.info( "processed record count: " + counter );
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      ProductClaimImportRecord record = (ProductClaimImportRecord)iterator.next();
      boolean hasNoErrors = record.getImportRecordErrors().isEmpty();
      boolean foundError = false;

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = validateProductClaimImportRecord( record, (ProductClaimPromotion)importFile.getPromotion() );

      if ( !errors.isEmpty() )
      {
        foundError = true;
        createAndAddImportRecordErrors( importFile, record, errors );
      }

      if ( foundError && hasNoErrors )
      {
        importRecordErrorCount++;
      }
    }
    log.info( "Verify: total processed record count: " + counter );

    importFile.setImportRecordErrorCount( importRecordErrorCount );
  }

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the records to import.
   * @param justForPaxRightNow
   * @throws ServiceErrorException
   */
  public void verifyImportFile( ImportFile importFile, List records, boolean justForPaxRightNow ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    verifyImportFile( importFile, records );
  }

  protected Collection validateProductClaimImportRecord( ProductClaimImportRecord productClaimRecord, ProductClaimPromotion promotion )
  {
    List errors = new ArrayList();

    ProductClaim claim = createClaim( productClaimRecord, promotion, errors );
    if ( errors.isEmpty() )
    {
      // NOTE: submitter exists check already done by load procedure

      // Confirm user can submit against the promotion
      if ( !promotionService.isPromotionClaimableByParticipant( promotion.getId(), claim.getSubmitter() ) )
      {
        errors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_SUBMITTER_INELIGIBLE_FOR_PROMOTION ) );
      }

      // Validate that products are applicable for this promotion
      for ( Iterator iter = claim.getClaimProducts().iterator(); iter.hasNext(); )
      {
        boolean productApplicable = false;
        ClaimProduct claimProduct = (ClaimProduct)iter.next();
        for ( Iterator iterator = promotion.getPromotionPayoutGroups().iterator(); iterator.hasNext(); )
        {
          PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)iterator.next();

          for ( Iterator iterator1 = promotionPayoutGroup.getPromotionPayouts().iterator(); iterator1.hasNext(); )
          {
            PromotionPayout promotionPayout = (PromotionPayout)iterator1.next();

            if ( SalesPayoutStrategyUtil.isProductSoldApplicableToPromotionPayout( claimProduct.getProduct(), promotionPayout ) )
            {
              productApplicable = true;
              break;
            }
          }
        }

        if ( !productApplicable )
        {
          errors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_PRODUCT_NOT_ELIGIBLE_FOR_PROMOTION, claimProduct.getProduct().getName() ) );
        }
      }

      // validate claim form elements and product chars
      errors.addAll( claimService.validateClaim( claim ) );
    }

    return errors;
  }

  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    long counter = 0;
    log.info( "processed record count: " + counter );

    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      ProductClaimImportRecord productClaimRecord = (ProductClaimImportRecord)iterator.next();

      // guard clause - only process this record if there are no errors with it
      if ( !productClaimRecord.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      try
      {
        List errors = new ArrayList();
        Claim claim = createClaim( productClaimRecord, (ProductClaimPromotion)importFile.getPromotion(), errors );
        // we shouldn't ever get one of these since we validated previously, but log just in case
        for ( Iterator iter = errors.iterator(); iter.hasNext(); )
        {
          ServiceError e = (ServiceError)iter.next();
          log.error( e.toString() );
        }
        boolean forceAutoApprove;
        if ( importFile.getFileImportApprovalType().getCode().equals( FileImportApprovalType.MANUAL ) )
        {
          forceAutoApprove = false;
        }
        else
        {
          forceAutoApprove = true;
        }
        claimService.saveClaim( claim, null, null, forceAutoApprove, true );
      }
      catch( ServiceErrorException e )
      {
        // we shouldn't ever get one of these since we validated previously, but log just in case
        StringBuffer errorsText = new StringBuffer();
        errorsText.append( e.toString() ).append( "\nService Errors:\n" );
        for ( Iterator iter = e.getServiceErrorsCMText().iterator(); iter.hasNext(); )
        {
          String errorLine = (String)iter.next();
          errorsText.append( errorLine ).append( "\n" );
        }
        log.error( errorsText.toString(), e );
      }
    }
    log.info( "Import: total processed record count: " + counter );
  }

  /**
   * @param claimService value for claimService property
   */
  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  /**
   * @param participantService value for participantService property
   */
  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  /**
   * @param nodeService value for nodeService property
   */
  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  /**
   * @param hierarchyService value for hierarchyService property
   */
  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  /**
   * @param promotionService value for promotionService property
   */
  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  /**
   * @param productService value for productService property
   */
  public void setProductService( ProductService productService )
  {
    this.productService = productService;
  }
}
