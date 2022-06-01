
package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;

import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.FileImportApprovalType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.domain.fileload.ProductClaimImportProductRecord;
import com.biperf.core.domain.fileload.ProductClaimImportRecord;
import com.biperf.core.domain.fileload.ProductImportRecord;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCharacteristic;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.product.ProductService;
import com.biperf.core.service.promotion.PromotionService;

/**
 * ProductImportStrategyTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th></th>
 * </tr>
 * <tr>
 * <td>Travis Jenniges</td>
 * <td>Feb 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductClaimImportStrategyTest extends BaseServiceTest
{
  private static final Long PRODUCT_CHAR1_ID = new Long( 1 );
  private static final String PRODUCT_CHAR1_NAME = "prodchar1";
  private static final Long PRODUCT1_ID = PRODUCT_CHAR1_ID;
  private static final String PRODUCT1_NAME = "product1";
  private static final Long ELEMENT1_ID = new Long( 1 );

  private IMocksControl control;

  private ProductService productServiceMock;
  private ParticipantService participantServiceMock;
  private NodeService nodeServiceMock;
  private PromotionService promotionServiceMock;
  private HierarchyService hierarchyServiceMock;
  private ClaimService claimServiceMock;

  private Product product1;

  private ProductClaimImportStrategy importStrategyUnderTest;

  public void setUp() throws Exception
  {
    super.setUp();

    // Initialize the mock controller
    control = EasyMock.createControl();

    // Initialize the mocked objects
    importStrategyUnderTest = new ProductClaimImportStrategy();
    productServiceMock = control.createMock( ProductService.class );
    participantServiceMock = control.createMock( ParticipantService.class );
    nodeServiceMock = control.createMock( NodeService.class );
    promotionServiceMock = control.createMock( PromotionService.class );
    hierarchyServiceMock = control.createMock( HierarchyService.class );
    claimServiceMock = control.createMock( ClaimService.class );

    // Set necessary attributes of the productClaimImportStrategy we are testing
    importStrategyUnderTest.setProductService( productServiceMock );
    importStrategyUnderTest.setParticipantService( participantServiceMock );
    importStrategyUnderTest.setNodeService( nodeServiceMock );
    importStrategyUnderTest.setPromotionService( promotionServiceMock );
    importStrategyUnderTest.setHierarchyService( hierarchyServiceMock );
    importStrategyUnderTest.setClaimService( claimServiceMock );

    // Initialize test product
    product1 = ProductDAOImplTest.buildStaticProductDomainObject( PRODUCT1_NAME, ProductDAOImplTest.getProductCategoryDomainObject( "cat12" ) );
    product1.setProductCharacteristics( new LinkedHashSet() );

    ProductCharacteristic testProductCharacteristic = ProductDAOImplTest.getTestProductCharacteristic();
    testProductCharacteristic.getProductCharacteristicType().setCharacteristicName( PRODUCT_CHAR1_NAME );
    testProductCharacteristic.getProductCharacteristicType().setId( PRODUCT_CHAR1_ID );
    product1.addProductCharacteristic( testProductCharacteristic );
  }

  public void testVerifyCleanImportFile()
  {
    ImportFile file = buildCleanProductClaimImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );

    assertFalse( file.getProductClaimImportRecords().isEmpty() );
    assertTrue( file.getImportRecordErrors().isEmpty() );

    List<ProductClaimImportRecord> records = new ArrayList( file.getProductClaimImportRecords() );

    Hierarchy primaryHierarchy = new Hierarchy();
    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( "submitter" );
    Node submitterNode = NodeDAOImplTest.buildUniqueNode( "node" );
    submitterNode.setId( new Long( 1 ) );
    submitterNode.setHierarchy( primaryHierarchy );
    submitter.addNode( submitterNode, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );

    ProductClaimPromotion promotion = createPromotion();
    file.setPromotion( promotion );
    Long nullPromoId = null;

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( submitter );
    EasyMock.expect( nodeServiceMock.getNodeById( null ) ).andReturn( submitterNode );
    EasyMock.expect( productServiceMock.getProductById( PRODUCT1_ID ) ).andReturn( product1 );
    EasyMock.expect( promotionServiceMock.isPromotionClaimableByParticipant( nullPromoId, submitter ) ).andReturn( true );
    EasyMock.expect( hierarchyServiceMock.getPrimaryHierarchy() ).andReturn( primaryHierarchy );
    EasyMock.expect( claimServiceMock.validateClaim( new ProductClaim() ) ).andReturn( new ArrayList() );
    control.replay();
    importStrategyUnderTest.verifyImportFile( file, records );

    assertTrue( file.getImportRecordErrors().isEmpty() );
  }

  public void testImportCleanImportFile() throws Exception
  {
    ImportFile file = buildCleanProductClaimImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );

    assertFalse( file.getProductClaimImportRecords().isEmpty() );
    assertTrue( file.getImportRecordErrors().isEmpty() );

    Hierarchy primaryHierarchy = new Hierarchy();

    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( "submitter" );
    Node submitterNode = NodeDAOImplTest.buildUniqueNode( "node" );
    submitterNode.setId( new Long( 1 ) );
    submitterNode.setHierarchy( primaryHierarchy );
    submitter.addNode( submitterNode, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );

    ProductClaimPromotion promotion = createPromotion();
    file.setPromotion( promotion );

    // ** test Import
    control.reset();
    Long nullPromoId = null;
    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( submitter );
    EasyMock.expect( nodeServiceMock.getNodeById( null ) ).andReturn( submitterNode );
    EasyMock.expect( productServiceMock.getProductById( PRODUCT1_ID ) ).andReturn( product1 );
    EasyMock.expect( promotionServiceMock.isPromotionClaimableByParticipant( nullPromoId, submitter ) ).andReturn( true );
    EasyMock.expect( hierarchyServiceMock.getPrimaryHierarchy() ).andReturn( primaryHierarchy );
    EasyMock.expect( claimServiceMock.validateClaim( new ProductClaim() ) ).andReturn( new ArrayList() );
    EasyMock.expect( claimServiceMock.saveClaim( new ProductClaim(), null, null, false, true ) ).andReturn( new ProductClaim() );

    control.replay();

    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORT_IN_PROCESS ) );
    file.setFileImportApprovalType( FileImportApprovalType.lookup( FileImportApprovalType.MANUAL ) );
    importStrategyUnderTest.importImportFile( file, new ArrayList( file.getProductClaimImportRecords() ) );

    assertTrue( file.getImportRecordErrors().isEmpty() );

  }

  // public void testImportCleanImportFile()
  // {
  // ImportFile file = buildCleanProductImportFile();
  // file.setStatus( (ImportFileStatusType)MockPickListFactory
  // .getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFIED ) );
  // assertTrue( file.getImportRecordErrors().isEmpty() );
  //
  // try
  // {
  // for ( int i = 0; i < file.getProductImportRecords().size(); i++ )
  // {
  // productServiceControl.expectAndReturn( productServiceMock.save( null, new Product() ),
  // new Product() );
  // if ( i == 0 )
  // {
  // ProductCategory category = new ProductCategory();
  // category.setName( "Name" );
  // productCategoryServiceControl.expectAndReturn( productCategoryServiceMock
  // .saveProductCategory( category, null ), category );
  // }
  // }
  // productServiceControl.replay();
  // productCategoryServiceControl.replay();
  // importStrategyUnderTest.importImportFile( file,
  // new ArrayList( file.getProductImportRecords() ) );
  // productServiceControl.verify();
  // productCategoryServiceControl.verify();
  // }
  // catch( ServiceErrorException e )
  // {
  // fail( "" + e );
  // }
  // catch( UniqueConstraintViolationException e )
  // {
  // fail( "" + e );
  // }
  //
  // assertTrue( file.getImportRecordErrors().isEmpty() );
  // }

  public static ImportFile buildCleanProductClaimImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.PRODUCT_CLAIM ) );
    Set records = new LinkedHashSet();
    file.setProductClaimImportRecords( records );

    ProductClaimImportRecord record1 = new ProductClaimImportRecord();
    record1.setActionType( ProductImportRecord.ADD_ACTION_TYPE );

    // claim elements
    LinkedHashSet productClaimImportFieldRecords = new LinkedHashSet();
    record1.setProductClaimImportFieldRecords( productClaimImportFieldRecords );

    // claim products
    LinkedHashSet productClaimImportProductRecords = new LinkedHashSet();
    record1.setProductClaimImportProductRecords( productClaimImportProductRecords );

    ProductClaimImportProductRecord claimImportProductRecord = new ProductClaimImportProductRecord();
    claimImportProductRecord.setProductId( PRODUCT1_ID );
    claimImportProductRecord.setProductName( PRODUCT1_NAME );
    claimImportProductRecord.setSoldQuantity( new Integer( 2 ) );
    claimImportProductRecord.setProductCharacteristicId1( PRODUCT_CHAR1_ID );
    claimImportProductRecord.setProductCharacteristicName1( PRODUCT_CHAR1_NAME );
    claimImportProductRecord.setProductCharacteristicValue1( "prodchar-value1" );
    productClaimImportProductRecords.add( claimImportProductRecord );

    records.add( record1 );

    return file;
  }

  private ProductClaimPromotion createPromotion()
  {
    ProductClaimPromotion promotion = new ProductClaimPromotion();
    promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    promotion.setSubmissionStartDate( new Date() );

    // Add claim form step element
    ClaimForm claimForm = new ClaimForm();
    claimForm.setCmAssetCode( "123456" );
    promotion.setClaimForm( claimForm );

    ClaimFormStep claimFormStep = new ClaimFormStep();
    claimForm.addClaimFormStep( claimFormStep );

    ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
    claimFormStepElement.setId( ELEMENT1_ID );
    claimFormStep.addClaimFormStepElement( claimFormStepElement );

    PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();
    PromotionPayout promotionPayout = new PromotionPayout();
    promotionPayout.setProduct( product1 );
    promotionPayoutGroup.addPromotionPayout( promotionPayout );
    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    return promotion;
  }

  public static void addErrors( ImportFile file )
  {
    Set errors = new HashSet( 1 );
    errors.add( new ImportRecordError() );
    for ( Iterator iter = file.getProductImportRecords().iterator(); iter.hasNext(); )
    {
      ProductImportRecord record = (ProductImportRecord)iter.next();
      record.setImportRecordErrors( errors );
    }
  }
}