/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/claim/impl/ClaimFormDefinitionServiceImplTest.java,v $
 */

package com.biperf.core.service.claim.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.jmock.Mock;

import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.claim.hibernate.ClaimFormDAOImplTest;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.claim.ClaimFormStepElementCMDataHolder;
import com.biperf.core.service.cms.CMAssetService;

/**
 * ClaimFormServiceImplTest.
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
 * <td>robinsra</td>
 * <td>Jun 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormDefinitionServiceImplTest extends BaseServiceTest
{
  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public ClaimFormDefinitionServiceImplTest( String test )
  {
    super( test );
  }

  /**
   * claimFormServiceImplementation
   */
  private ClaimFormDefinitionServiceImpl claimFormService = new ClaimFormDefinitionServiceImpl();

  /**
   * mocks
   */
  private Mock mockClaimFormDAO = null;
  private Mock mockCMAssetService = null;

  /**
   * cmConstants for Claim Form
   */
  // private Mock mockClaimFormDefinitionService = null;
  /**
   * Setup this test case. Overridden from
   * 
   * @throws Exception
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    mockClaimFormDAO = new Mock( ClaimFormDAO.class );
    claimFormService.setClaimFormDAO( (ClaimFormDAO)mockClaimFormDAO.proxy() );

    // mockClaimFormDefinitionService = new Mock( ClaimFormDefinitionService.class );

    mockCMAssetService = new Mock( CMAssetService.class );
    claimFormService.setCmAssetService( (CMAssetService)mockCMAssetService.proxy() );
  }

  /**
   * Test getting the ClaimForm by id.
   */
  public void testGetClaimFormById()
  {
    // Get the test ClaimForm.
    ClaimForm claimForm = new ClaimForm();
    claimForm.setId( new Long( 1 ) );
    claimForm.setName( "A Test Form" );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.TEMPLATE ) );

    // ClaimFormDAO expected to call getClaimFormById once with the ClaimFormId which will return
    // the ClaimForm expected
    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( claimForm.getId() ) ).will( returnValue( claimForm ) );

    ClaimForm actualClaimForm = claimFormService.getClaimFormById( claimForm.getId() );
    assertEquals( "Actual claimForm wasn't equal to what was expected", claimForm, actualClaimForm );

    mockClaimFormDAO.verify();
  } // end testGetClaimFormById

  /**
   * Tests getting all of the Claim Forms
   */
  public void testGetAll()
  {
    List claimFormList = new ArrayList();
    claimFormList.add( new ClaimForm() );
    claimFormList.add( new ClaimForm() );
    mockClaimFormDAO.expects( once() ).method( "getAll" ).will( returnValue( claimFormList ) );

    List returnedClaimFormList = claimFormService.getAll();
    assertTrue( returnedClaimFormList.size() == 2 );
  } // end testGetAll

  /**
   * Test adding the ClaimForm through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveClaimFormInsert() throws ServiceErrorException
  {
    // Create the test ClaimForm.
    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    ClaimForm savedClaimForm = new ClaimForm();
    savedClaimForm.setId( new Long( 1 ) );
    savedClaimForm.setName( "A Test Form" );
    savedClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    savedClaimForm.setDescription( "Description of the test claim form" );
    savedClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    savedClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // find the claim form by Name
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( claimForm.getName() ) ).will( returnValue( null ) );

    // get a unique asset name
    mockCMAssetService.expects( once() ).method( "getUniqueAssetCode" ).with( same( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX ) ).will( returnValue( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1234" ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // insert a new claimForm
    mockClaimFormDAO.expects( once() ).method( "saveClaimForm" ).with( same( claimForm ) ).will( returnValue( savedClaimForm ) );

    // test Adding with the ClaimFormService.saveClaimForm
    ClaimForm returnedClaimForm = claimFormService.saveClaimForm( claimForm, true );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    assertEquals( "Actual returned Claim Form wasn't equal to what was expected", savedClaimForm, returnedClaimForm );
    mockClaimFormDAO.verify();
  } // end testSaveClaimFormInsert

  /**
   * Test adding duplicate claimForm through the service.
   */
  public void testSaveClaimFormInsertConstraintViolation()
  {
    // Create the test Claim Form.
    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // Create the Claim Form that is already in the DB.
    ClaimForm dbClaimForm = new ClaimForm();
    dbClaimForm.setName( "A Test Form" );
    dbClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de24eed" );
    dbClaimForm.setDescription( "Description of the test claim form" );
    dbClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    dbClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // insert a new claim form
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( claimForm.getName() ) ).will( returnValue( dbClaimForm ) );

    try
    {
      claimFormService.saveClaimForm( claimForm, true );
    }
    catch( ServiceErrorException se )
    {
      // good - should get exception
      return;
    }

    fail( "Should have thrown ServiceErrorException" );
  }

  /**
   * Test adding, then updating the ClaimForm through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveClaimFormUpdate() throws ServiceErrorException
  {
    // Create the test ClaimForm.
    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    ClaimForm savedClaimForm = new ClaimForm();
    savedClaimForm.setId( new Long( 1 ) );
    savedClaimForm.setName( "A Test Form" );
    savedClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    savedClaimForm.setDescription( "Description of the test claim form" );
    savedClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    savedClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // find the claim form by Name
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( claimForm.getName() ) ).will( returnValue( null ) );

    // get a unique asset name
    mockCMAssetService.expects( once() ).method( "getUniqueAssetCode" ).with( same( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX ) ).will( returnValue( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1234" ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // insert a new claimForm
    mockClaimFormDAO.expects( once() ).method( "saveClaimForm" ).with( same( claimForm ) ).will( returnValue( savedClaimForm ) );

    // test Adding the ClaimFormService.saveClaimForm
    ClaimForm returnedClaimForm = claimFormService.saveClaimForm( claimForm, true );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    assertEquals( "Actual returned Claim Form wasn't equal to what was expected", savedClaimForm, returnedClaimForm );
    mockClaimFormDAO.verify();

    // ---------------------
    // Update the Claim Form
    // ---------------------
    returnedClaimForm.setName( "Updated Form Name" );

    // find the claim form by Name
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( returnedClaimForm.getName() ) ).will( returnValue( null ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( returnedClaimForm.getId() ) ).will( returnValue( returnedClaimForm ) );

    // update a new claimForm
    mockClaimFormDAO.expects( once() ).method( "updateClaimForm" ).with( same( returnedClaimForm ) ).will( returnValue( returnedClaimForm ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    ClaimForm returnedUpdatedClaimForm = claimFormService.saveClaimForm( returnedClaimForm, true );
    assertEquals( "Actual returned Claim Form wasn't equal to what was expected", returnedUpdatedClaimForm, returnedClaimForm );
    mockClaimFormDAO.verify();

  } // end testSaveClaimFormUpdate

  /**
   * Test adding, then updating the ClaimForm with bad asset through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveClaimFormUpdateWithBadAsset() throws ServiceErrorException
  {
    // Create the test ClaimForm.
    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    ClaimForm savedClaimForm = new ClaimForm();
    savedClaimForm.setId( new Long( 1 ) );
    savedClaimForm.setName( "A Test Form" );
    savedClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    savedClaimForm.setDescription( "Description of the test claim form" );
    savedClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    savedClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // find the claim form by Name
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( claimForm.getName() ) ).will( returnValue( null ) );

    // get a unique asset name
    mockCMAssetService.expects( once() ).method( "getUniqueAssetCode" ).with( same( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX ) ).will( returnValue( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1234" ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // insert a new claimForm
    mockClaimFormDAO.expects( once() ).method( "saveClaimForm" ).with( same( claimForm ) ).will( returnValue( savedClaimForm ) );

    // test Adding the ClaimFormService.saveClaimForm
    ClaimForm returnedClaimForm = claimFormService.saveClaimForm( claimForm, true );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    assertEquals( "Actual returned Claim Form wasn't equal to what was expected", savedClaimForm, returnedClaimForm );
    mockClaimFormDAO.verify();

    // ---------------------
    // Update the Claim Form
    // ---------------------
    returnedClaimForm.setName( "Updated Form Name" );
    returnedClaimForm.setCmAssetCode( "somethingbad" );
    savedClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );

    // find the claim form by Name
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( returnedClaimForm.getName() ) ).will( returnValue( null ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( savedClaimForm.getId() ) ).will( returnValue( savedClaimForm ) );

    try
    {
      claimFormService.saveClaimForm( returnedClaimForm, true );
    }
    catch( ServiceErrorException se )
    {
      // good - should get exception
      return;
    }

    fail( "Should have thrown ServiceErrorException" );

  } // end testSaveClaimFormUpdateWithBadAsset

  /**
   * Test adding, then updating the ClaimForm with an invalid status to update through the service.
   * 
   * @throws ServiceErrorException
   */
  // in the Save Claim Form (when updating)
  public void testSaveClaimFormUpdateStatusTests() throws ServiceErrorException
  {
    // Create the test ClaimForm.
    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    ClaimForm savedClaimForm = new ClaimForm();
    savedClaimForm.setId( new Long( 1 ) );
    savedClaimForm.setName( "A Test Form" );
    savedClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    savedClaimForm.setDescription( "Description of the test claim form" );
    savedClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    savedClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // find the claim form by Name
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( claimForm.getName() ) ).will( returnValue( null ) );

    // get a unique asset name
    mockCMAssetService.expects( once() ).method( "getUniqueAssetCode" ).with( same( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX ) ).will( returnValue( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1234" ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // insert a new claimForm
    mockClaimFormDAO.expects( once() ).method( "saveClaimForm" ).with( same( claimForm ) ).will( returnValue( savedClaimForm ) );

    // test Adding the ClaimFormService.saveClaimForm
    ClaimForm returnedClaimForm = claimFormService.saveClaimForm( claimForm, true );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    assertEquals( "Actual returned Claim Form wasn't equal to what was expected", savedClaimForm, returnedClaimForm );
    mockClaimFormDAO.verify();

    // -----------------------------------------------------------
    // Update the Claim Form - move it to Assigned (should work)
    // -----------------------------------------------------------
    returnedClaimForm.setName( "Updated Form Name" );
    returnedClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );

    ClaimForm savedDBClaimForm = new ClaimForm();
    savedDBClaimForm.setId( new Long( 1 ) );
    savedDBClaimForm.setName( "A Test Form" );
    savedDBClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    savedDBClaimForm.setDescription( "Description of the test claim form" );
    savedDBClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    savedDBClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // find the claim form by Name
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( returnedClaimForm.getName() ) ).will( returnValue( null ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( returnedClaimForm.getId() ) ).will( returnValue( savedDBClaimForm ) );

    // update a new claimForm
    mockClaimFormDAO.expects( once() ).method( "updateClaimForm" ).with( same( returnedClaimForm ) ).will( returnValue( returnedClaimForm ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    ClaimForm returnedUpdatedClaimForm = claimFormService.saveClaimForm( returnedClaimForm, true );
    assertEquals( "Actual returned Claim Form wasn't equal to what was expected", returnedUpdatedClaimForm, returnedClaimForm );
    mockClaimFormDAO.verify();
    // ---------------------
    // Update the Claim Form
    // (now it is at an assigned status - it should get back a Service Exception)
    // ---------------------
    returnedClaimForm.setName( "Updated Form Name" );

    // find the claim form by Name
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( returnedClaimForm.getName() ) ).will( returnValue( null ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( returnedClaimForm.getId() ) ).will( returnValue( returnedClaimForm ) );
    try
    {
      claimFormService.saveClaimForm( returnedClaimForm, true );
    }
    catch( ServiceErrorException se )
    {
      // good - should get exception
      return;
    }

    fail( "Should have thrown ServiceErrorException" );

  } // end testSaveClaimFormUpdateWithBadAsset

  /**
   * Test deleting the ClaimForm through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testDeleteClaimForm() throws ServiceErrorException
  {
    // Create the test Claim Form.
    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( claimForm.getId() ) ).will( returnValue( claimForm ) );

    mockClaimFormDAO.expects( once() ).method( "deleteClaimForm" ).with( same( claimForm ) );

    // test the ClaimFormService.deleteClaimForm
    claimFormService.deleteClaimForm( claimForm.getId() );

    // Verify the mockDAO deletes.
    mockClaimFormDAO.verify();
  }

  /**
   * Test deleting the ClaimForm through the service with a status that should not delete.
   */
  public void testDeleteClaimFormWithInvalidStatus()
  {
    // Create the test Claim Form.
    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( claimForm.getId() ) ).will( returnValue( claimForm ) );

    // test the ClaimFormService.deleteClaimForm
    try
    {
      claimFormService.deleteClaimForm( claimForm.getId() );
    }
    catch( ServiceErrorException se )
    {
      // Good - Should get exception
      return;
    }

    fail( "Should have gotten a ServiceErrorException" );
  }

  /**
   * Test Adding a ClaimFormStep.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveOrUpdateClaimFormStep() throws ServiceErrorException
  {
    // -----------------------------
    // Create the test ClaimForm.
    // -----------------------------
    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    ClaimForm savedClaimForm = new ClaimForm();
    savedClaimForm.setId( new Long( 1 ) );
    savedClaimForm.setName( "A Test Form" );
    savedClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    savedClaimForm.setDescription( "Description of the test claim form" );
    savedClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    savedClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // find the claim form by Name
    mockClaimFormDAO.expects( once() ).method( "getClaimFormByName" ).with( same( claimForm.getName() ) ).will( returnValue( null ) );

    // get a unique asset name
    mockCMAssetService.expects( once() ).method( "getUniqueAssetCode" ).with( same( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX ) ).will( returnValue( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1234" ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // insert a new claimForm
    mockClaimFormDAO.expects( once() ).method( "saveClaimForm" ).with( same( claimForm ) ).will( returnValue( savedClaimForm ) );

    // test Adding with the ClaimFormService.saveClaimForm
    ClaimForm returnedClaimForm = claimFormService.saveClaimForm( claimForm, true );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    assertEquals( "Actual returned Claim Form wasn't equal to what was expected", savedClaimForm, returnedClaimForm );
    mockClaimFormDAO.verify();

    // ---------------------------
    // Create Claim Form Step
    // ---------------------------
    ClaimFormStep claimFormStep = new ClaimFormStep();
    claimFormStep.setCmKeyFragment( String.valueOf( Math.random() % 29930291 ) );
    claimFormStep.setSalesRequired( true );

    // Create Email Notification and attach to step
    ClaimFormStepEmailNotification claimFormStepEmailNotification = new ClaimFormStepEmailNotification();
    claimFormStepEmailNotification.setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType.getDefaultItem() );

    claimFormStep.addClaimFormStepEmailNotification( claimFormStepEmailNotification );

    // ------------------
    // Add ClaimFormStep
    // --------------------
    // find the claim form by ID
    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( returnedClaimForm.getId() ) ).will( returnValue( returnedClaimForm ) );

    mockCMAssetService.expects( once() ).method( "getUniqueKeyFragment" ).will( returnValue( "112233" ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // save the claimForm
    mockClaimFormDAO.expects( once() ).method( "saveClaimFormStep" ).with( same( claimFormStep ) ).will( returnValue( claimFormStep ) );

    // Add the Claim Form Step
    ClaimFormStep returnedClaimFormStep = claimFormService.saveClaimFormStep( returnedClaimForm.getId(), claimFormStep, "A Testing Step" + String.valueOf( Math.random() % 29930291 ) );
    mockClaimFormDAO.verify();
    assertTrue( returnedClaimFormStep.getClaimFormStepEmailNotifications().size() == 1 );

    // --------
    // Update
    // --------
    // Change the ClaimFormStep then save it again.
    returnedClaimFormStep.setId( new Long( 2443234 ) );

    // find the claim form by ID
    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( returnedClaimForm.getId() ) ).will( returnValue( returnedClaimForm ) );

    // Get the elements associated to this updated claimFormStep
    // mockClaimFormDAO.expects( once() ).method( "getClaimFormStepElementsByClaimFormStepId"
    // ).with( same( returnedClaimFormStep.getId() ) ).will( returnValue( new ArrayList() ) );

    // save the claimForm
    mockClaimFormDAO.expects( once() ).method( "saveClaimFormStep" ).with( same( returnedClaimForm.getId() ), same( returnedClaimFormStep ) ).will( returnValue( returnedClaimFormStep ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // Add the Claim Form Step
    ClaimFormStep updatedClaimFormStep = claimFormService.saveClaimFormStep( returnedClaimForm.getId(), returnedClaimFormStep, "TEST UPDATED NAME" );

    mockClaimFormDAO.verify();
    assertTrue( updatedClaimFormStep.getClaimFormStepEmailNotifications().size() == 1 );
    // TODO - Verify CM name?
    // assertTrue( updatedClaimFormStep.getName().equals("TEST UPDATED NAME") );

    // -------------------
    // Get Steps
    // ----------------
    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( returnedClaimForm.getId() ) ).will( returnValue( returnedClaimForm ) );

    List claimFormSteps = claimFormService.getClaimFormSteps( returnedClaimForm.getId() );
    assertTrue( claimFormSteps.size() == 1 );
    // --------------------------------
    // Get individual Claim Form Step
    // ---------------------------------
    mockClaimFormDAO.expects( once() ).method( "getClaimFormStepById" ).with( same( returnedClaimFormStep.getId() ) ).will( returnValue( returnedClaimFormStep ) );
    ClaimFormStep actualClaimFormStep = claimFormService.getClaimFormStep( returnedClaimFormStep.getId() );

    assertEquals( "Actual claimFormStep wasn't equal to what was expected", returnedClaimFormStep, actualClaimFormStep );
  }

  /**
   * Tests getting all of the Claim Forms
   * 
   * @throws ServiceErrorException
   */
  public void testDeleteClaimForms() throws ServiceErrorException
  {
    Long claimFormId1 = new Long( 1 );
    Long claimFormId2 = new Long( 2 );

    List claimFormIdDeleteList = new ArrayList();

    ClaimForm claimForm1 = getClaimFormDomainObject();
    claimForm1.setId( claimFormId1 );
    claimFormIdDeleteList.add( claimFormId1 );

    ClaimForm claimForm2 = getClaimFormDomainObject();
    claimForm2.setId( claimFormId2 );
    claimFormIdDeleteList.add( claimFormId2 );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( claimForm1.getId() ) ).will( returnValue( claimForm1 ) );
    mockClaimFormDAO.expects( once() ).method( "deleteClaimForm" ).with( same( claimForm1 ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( claimForm2.getId() ) ).will( returnValue( claimForm2 ) );
    mockClaimFormDAO.expects( once() ).method( "deleteClaimForm" ).with( same( claimForm2 ) );

    claimFormService.deleteClaimForms( claimFormIdDeleteList );

    mockClaimFormDAO.verify();
  } // end testDeleteClaimForms

  /**
   * Test the getFormLibraryMap method
   */
  public void testGetFormLibraryMap()
  {
    List allClaimForms = new ArrayList();

    // Should be:
    // 2 UnderContruction
    // 1 Assigned
    // 3 Completed
    // 0 Templates
    ClaimForm claimFormUnderConstruction1 = getClaimFormDomainObject();
    claimFormUnderConstruction1.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    allClaimForms.add( claimFormUnderConstruction1 );

    ClaimForm claimFormUnderConstruction2 = getClaimFormDomainObject();
    claimFormUnderConstruction2.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    allClaimForms.add( claimFormUnderConstruction2 );

    ClaimForm claimFormAssigned1 = getClaimFormDomainObject();
    claimFormAssigned1.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );
    allClaimForms.add( claimFormAssigned1 );

    ClaimForm claimFormCompleted1 = getClaimFormDomainObject();
    claimFormCompleted1.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
    allClaimForms.add( claimFormCompleted1 );

    ClaimForm claimFormCompleted2 = getClaimFormDomainObject();
    claimFormCompleted2.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
    allClaimForms.add( claimFormCompleted2 );

    ClaimForm claimFormCompleted3 = getClaimFormDomainObject();
    claimFormCompleted3.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
    allClaimForms.add( claimFormCompleted3 );

    mockClaimFormDAO.expects( once() ).method( "getAll" ).will( returnValue( allClaimForms ) );

    Map formLibraryMap = claimFormService.getFormLibraryMap();

    assertEquals( 2, ( (List)formLibraryMap.get( ClaimFormStatusType.UNDER_CONSTRUCTION ) ).size() );
    assertEquals( 1, ( (List)formLibraryMap.get( ClaimFormStatusType.ASSIGNED ) ).size() );
    assertEquals( 3, ( (List)formLibraryMap.get( ClaimFormStatusType.COMPLETED ) ).size() );
    assertEquals( 0, ( (List)formLibraryMap.get( ClaimFormStatusType.TEMPLATE ) ).size() );
  }

  /**
   * Test reorderClaimFormStepElement
   */
  public void testReorderClaimFormStepElement()
  {
    int reorderToPosition = 2;

    ClaimFormStepElement claimFormStepElementToReorder = getClaimFormStepElementDomainObject();
    ClaimFormStep claimFormStep = getClaimFormStepDomainObject();

    // Elements with differentId
    ClaimFormStepElement claimFormStepElement2 = getClaimFormStepElementDomainObject();
    claimFormStepElement2.setId( new Long( 2 ) );
    ClaimFormStepElement claimFormStepElement3 = getClaimFormStepElementDomainObject();
    claimFormStepElement3.setId( new Long( 3 ) );
    ClaimFormStepElement claimFormStepElement4 = getClaimFormStepElementDomainObject();
    claimFormStepElement4.setId( new Long( 4 ) );

    claimFormStep.addClaimFormStepElement( claimFormStepElementToReorder );
    claimFormStep.addClaimFormStepElement( claimFormStepElement2 );
    claimFormStep.addClaimFormStepElement( claimFormStepElement3 );
    claimFormStep.addClaimFormStepElement( claimFormStepElement4 );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormStepElementById" ).with( same( claimFormStepElementToReorder.getId() ) ).will( returnValue( claimFormStepElementToReorder ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormStepById" ).with( same( claimFormStep.getId() ) ).will( returnValue( claimFormStep ) );

    mockClaimFormDAO.expects( once() ).method( "saveClaimFormStep" ).with( same( claimFormStep ) ).will( returnValue( claimFormStep ) );

    ClaimFormStep savedStep = claimFormService.reorderClaimFormStepElement( claimFormStep.getId(), claimFormStepElementToReorder.getId(), reorderToPosition );

    assertEquals( ( (ClaimFormStepElement)savedStep.getClaimFormStepElements().get( reorderToPosition ) ).getId(), claimFormStepElementToReorder.getId() );

  }

  /**
   * @return ClaimForm Random Domain object
   */
  private ClaimForm getClaimFormDomainObject()
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    ClaimForm claimForm = new ClaimForm();
    claimForm.setId( new Long( 1 ) );
    claimForm.setName( "A Test Form" + uniqueName );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    claimForm.setDescription( "Description of the test claim form" + uniqueName );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    return claimForm;
  }

  /**
   * @return ClaimFormStep Random Domain object
   */
  private ClaimFormStep getClaimFormStepDomainObject()
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    ClaimFormStep claimFormStep = new ClaimFormStep();
    claimFormStep.setId( new Long( 1 ) );
    claimFormStep.setCmKeyFragment( "FRAG" + uniqueName );
    claimFormStep.setSalesRequired( false );

    return claimFormStep;
  }

  /**
   * @return ClaimFormStepElement Random Domain object
   */
  private ClaimFormStepElement getClaimFormStepElementDomainObject()
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
    claimFormStepElement.setId( new Long( 1 ) );
    claimFormStepElement.setCmKeyFragment( "KEY" + uniqueName );
    claimFormStepElement.setDescription( "DESC" + uniqueName );
    claimFormStepElement.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );

    return claimFormStepElement;
  }

  /**
   * Test adding a claimFormStepElement.
   * 
   * @throws Exception
   */
  public void testClaimFormStepElementAdd() throws Exception
  {
    Long claimFormId = new Long( 1 );
    Long stepId = new Long( 2 );
    ClaimFormStepElement element = new ClaimFormStepElement();
    element.setId( new Long( 3 ) );

    ClaimFormStep step1 = new ClaimFormStep();
    step1.setId( new Long( 2 ) );
    List steps = new ArrayList();
    steps.add( step1 );

    ClaimForm claimForm = new ClaimForm()
    {
      public boolean isEditable()
      {
        return true;
      }
    };
    claimForm.setName( "Test form" );
    claimForm.setClaimFormSteps( steps );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.getDefaultItem() );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );
    step1.setClaimForm( claimForm );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( claimFormId ) ).will( returnValue( claimForm ) );

    ClaimFormStepElementCMDataHolder data = new ClaimFormStepElementCMDataHolder();

    mockCMAssetService.expects( once() ).method( "getUniqueKeyFragment" ).will( returnValue( "1234" ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    claimFormService.addClaimFormStepElement( claimFormId, stepId, element, data );

    assertEquals( step1.getClaimFormStepElements().size(), 1 );
    assertSame( element.getId(), ( (ClaimFormStepElement)step1.getClaimFormStepElements().get( 0 ) ).getId() );

  }

  /**
   * Test adding a claimFormStepElement to a form which is not editable.
   * 
   * @throws Exception
   */
  public void testClaimFormStepElementAddWithUneditableForm() throws Exception
  {
    Long claimFormId = new Long( 1 );
    Long stepId = new Long( 2 );
    ClaimFormStepElement element = new ClaimFormStepElement();
    element.setId( new Long( 3 ) );

    ClaimFormStep step1 = new ClaimFormStep();
    step1.setId( new Long( 2 ) );
    List steps = new ArrayList();
    steps.add( step1 );

    ClaimForm claimForm = new ClaimForm()
    {
      public boolean isEditable()
      {
        return false;
      }
    };

    claimForm.setClaimFormSteps( steps );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( claimFormId ) ).will( returnValue( claimForm ) );

    try
    {
      claimFormService.addClaimFormStepElement( claimFormId, stepId, element, null );
      fail( "Should have thrown an exception" );
    }
    catch( BeaconRuntimeException e )
    {
      // nothing, success here
    }

  }

  /**
   * TODO remove if not being used.
   * 
   * @return ClaimFormStep Random Domain object private ClaimFormStep getClaimFormStepDomainObject() {
   *         String uniqueName = String.valueOf( Math.random() % 29930291 ); ClaimFormStep
   *         claimFormStep = new ClaimFormStep(); claimFormStep.setId( new Long( 1 ) );
   *         claimFormStep.setName( "ClaimFormStepName" + uniqueName );
   *         claimFormStep.setSalesRequired( true ); return claimFormStep; }
   */

  /**
   * TODO remove if not being used. private ClaimFormStepElement
   * getClaimFormStepElementDomainObject() { String uniqueName = String.valueOf( Math.random() %
   * 29930291 ); ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
   * claimFormStepElement.setId( new Long( 1 ) ); claimFormStepElement.setClaimFormElementType(
   * ClaimFormElementType.lookup( "copy" ) ); //
   * claimFormStepElement.setCmKey(ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + uniqueName);
   * claimFormStepElement.setDescription( "Description of the test claim form" + uniqueName );
   * return claimFormStepElement; }
   */

  /**
   * @throws ServiceErrorException
   */
  public void testClaimFormStepElementUpdate() throws ServiceErrorException
  {
    Long claimFormId = new Long( 1 );
    Long stepId = new Long( 2 );
    ClaimFormStepElement element = new ClaimFormStepElement();
    element.setId( new Long( 3 ) );
    element.setCmKeyFragment( "" + System.currentTimeMillis() );

    ClaimFormStep step1 = new ClaimFormStep();
    step1.setId( new Long( 2 ) );
    List steps = new ArrayList();
    steps.add( step1 );

    ClaimForm claimForm = new ClaimForm()
    {
      public boolean isEditable()
      {
        return true;
      }
    };

    claimForm.setClaimFormSteps( steps );
    claimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "1xecec2de4eed" );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( claimFormId ) ).will( returnValue( claimForm ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormStepElementById" ).with( same( element.getId() ) ).will( returnValue( element ) );

    mockClaimFormDAO.expects( once() ).method( "updateClaimFormStepElement" ).with( same( element ) ).will( returnValue( element ) );

    mockCMAssetService.expects( once() ).method( "createOrUpdateAsset" );

    claimFormService.updateClaimFormStepElement( claimFormId, stepId, element, new ClaimFormStepElementCMDataHolder() );

    mockClaimFormDAO.verify();
  }

  /**
   * Test getting the claimFormStepElement by the assigned ID.
   */
  public void testGetElementById()
  {

    Long elementId = new Long( 1 );
    ClaimFormStepElement element = new ClaimFormStepElement();
    element.setClaimFormStep( new ClaimFormStep() );
    element.getClaimFormStep().setClaimForm( new ClaimForm() );
    mockClaimFormDAO.expects( once() ).method( "getClaimFormStepElementById" ).with( same( elementId ) ).will( returnValue( element ) );

    ClaimFormStepElement found = claimFormService.getClaimFormStepElementById( elementId );
    assertSame( found, element );
    mockClaimFormDAO.verify();

  }

  public void testDeleteClaimFormStepElements()
  {

    ClaimForm form = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();

    ClaimFormStep step = (ClaimFormStep)form.getClaimFormSteps().get( 0 );
    step.setId( new Long( 4 ) );
    ClaimFormStepElement elementToDelete = (ClaimFormStepElement)step.getClaimFormStepElements().get( 0 );
    ClaimFormStepElement elementToKeep = (ClaimFormStepElement)step.getClaimFormStepElements().get( 1 );

    elementToDelete.setId( new Long( 10 ) );
    elementToKeep.setId( new Long( 50 ) );
    List elementIds = new ArrayList();
    elementIds.add( elementToDelete.getId() );

    assertEquals( 2, step.getClaimFormStepElements().size() ); // just be sure things are setup ok

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( form.getId() ) ).will( returnValue( form ) );

    claimFormService.deleteClaimFormStepElements( form.getId(), ( (ClaimFormStep)form.getClaimFormSteps().get( 0 ) ).getId(), elementIds );

    assertEquals( 1, step.getClaimFormStepElements().size() );
    ClaimFormStepElement leftOverElement = (ClaimFormStepElement)step.getClaimFormStepElements().get( 0 );
    assertFalse( leftOverElement.getId().equals( elementToDelete.getId() ) );// be sure the one we
    // deleted is not
    // there anymore
  }

  /**
   * Tests getting all ClaimForms not under construction.
   */
  public void testGetAllClaimFormsNotUnderConstruction()
  {

    List claimFormList = new ArrayList();
    claimFormList.add( new ClaimForm() );
    claimFormList.add( new ClaimForm() );
    mockClaimFormDAO.expects( once() ).method( "getAllClaimFormsNotUnderConstruction" ).will( returnValue( claimFormList ) );

    List returnedClaimFormList = claimFormService.getAllClaimFormsNotUnderConstruction();
    assertTrue( returnedClaimFormList.size() == 2 );

  }

  public void testClaimFormStatusNoPromotions()
  {

    ClaimForm claimForm = new ClaimForm();
    claimForm.setId( new Long( 1337 ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormById" ).with( same( claimForm.getId() ) ).will( returnValue( claimForm ) );
    // no promotions
    claimFormService.updateClaimFormStatus( claimForm.getId() );

    assertTrue( claimForm.getClaimFormStatusType().isCompleted() );

  }

  public void testClaimFormServiceOnePromotion()
  {

    ClaimForm claimForm = new ClaimForm();
    claimForm.setId( new Long( 1337 ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );

    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 122 ) );
    promotion.setClaimForm( claimForm );

    claimForm.addPromotion( promotion );

    mockClaimFormDAO.expects( atLeastOnce() ).method( "getClaimFormById" ).with( same( claimForm.getId() ) ).will( returnValue( claimForm ) );

    claimFormService.updateClaimFormStatus( claimForm.getId() );

    assertTrue( claimForm.getClaimFormStatusType().isAssigned() );

    claimForm.setPromotions( new LinkedHashSet() );
    promotion.setClaimForm( null );

    claimFormService.updateClaimFormStatus( claimForm.getId() );

    assertTrue( claimForm.getClaimFormStatusType().isCompleted() );

  }

  /**
   * Test calling preparePreviewClaimForm with a claim form that contains a claim form, step, and
   * elements
   */
  public void testPreparePreviewClaimForm()
  {
    // Setup a ClaimForm that has both a step and an element
    ClaimForm claimForm = getClaimFormDomainObject();
    ClaimFormStep claimFormStep = getClaimFormStepDomainObject();
    claimForm.addClaimFormStep( claimFormStep );
    ClaimFormStepElement claimFormStepElement = getClaimFormStepElementDomainObject();
    claimFormStep.addClaimFormStepElement( claimFormStepElement );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormStepByIdWithAssociations" ).will( returnValue( claimFormStep ) );

    try
    {
      claimFormService.preparePreviewClaimForm( claimFormStep.getId() );
    }
    catch( ServiceErrorException se )
    {
      fail( "Should not have thrown a Service Error Exception here. " + se.toString() );
    }

  } // end testGetClaimFormById

  /**
   * Test calling preparePreviewClaimForm with a claim form that contains only a claim form - No
   * step, and No elements
   */
  public void testPreparePreviewClaimFormNoSteps()
  {
    // Setup a ClaimForm that has both a step and an element
    // ClaimForm claimForm = getClaimFormDomainObject();

    mockClaimFormDAO.expects( once() ).method( "getClaimFormStepByIdWithAssociations" ).will( returnValue( null ) );

    try
    {
      claimFormService.preparePreviewClaimForm( new Long( 1 ) );
      fail( "Should have gotten a service error exception" );
    }
    catch( ServiceErrorException se )
    {
      // Good - should get exception
    }

  } // end testGetClaimFormById

  /**
   * Test calling preparePreviewClaimForm with a claim form that contains only a claim form and a
   * step - No elements
   */
  public void testPreparePreviewClaimFormNoElements()
  {
    // Setup a ClaimForm that has only a step and no element
    ClaimForm claimForm = getClaimFormDomainObject();
    ClaimFormStep claimFormStep = getClaimFormStepDomainObject();
    claimForm.addClaimFormStep( claimFormStep );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormStepByIdWithAssociations" ).will( returnValue( claimFormStep ) );
    try
    {
      claimFormService.preparePreviewClaimForm( new Long( 1 ) );
      fail( "Should have gotten a service error exception" );
    }
    catch( ServiceErrorException se )
    {
      // Good - should get exception
    }
  } // end testGetClaimFormById

} // end ClaimFormServiceImplTest
