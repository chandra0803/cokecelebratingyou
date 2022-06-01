/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/claim/hibernate/ClaimFormDAOImplTest.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.claim.HierarchyUniqueConstraintEnum;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.domain.enums.NumberFieldInputFormatType;
import com.biperf.core.domain.enums.TextFieldInputFormatType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * ClaimFormDAOImplTest.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormDAOImplTest extends BaseDAOTest
{

  /** LOG */
  private static final Log LOG = LogFactory.getLog( ClaimFormDAOImplTest.class );

  /**
   * Tests saving and getting all the ClaimForm records saved.
   */
  public void testGetAll()
  {

    ClaimFormDAO claimFormDAO = getClaimFormDAO();
    int count = 0;
    count = claimFormDAO.getAll().size();

    List expectedClaimForms = new ArrayList();

    ClaimForm expectedClaimForm1 = new ClaimForm();
    expectedClaimForm1.setName( "Test Claim Form" + System.currentTimeMillis() );
    expectedClaimForm1.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform" + System.currentTimeMillis() );
    expectedClaimForm1.setDescription( "a description of the claim form" );
    expectedClaimForm1.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) );
    expectedClaimForm1.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    expectedClaimForms.add( expectedClaimForm1 );
    claimFormDAO.saveClaimForm( expectedClaimForm1 );

    ClaimForm expectedClaimForm2 = new ClaimForm();
    expectedClaimForm2.setName( "Test Claim Form2" + System.currentTimeMillis() );
    expectedClaimForm2.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform2" + System.currentTimeMillis() );
    expectedClaimForm2.setDescription( "a description of the claim form" );
    expectedClaimForm2.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) );
    expectedClaimForm2.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    expectedClaimForms.add( expectedClaimForm2 );
    claimFormDAO.saveClaimForm( expectedClaimForm2 );

    List actualClaimForms = claimFormDAO.getAll();

    assertEquals( "List of claim forms are not the same size.", expectedClaimForms.size() + count, actualClaimForms.size() );

  }

  /**
   * Tests saving or updating the ClaimForm. This needs to fetch the claimForm by Id so it is also
   * testing ClaimFormDAO.getClaimFormById(Long id).
   */
  public void testSaveGetDeleteClaimFormById()
  {

    ClaimFormDAO claimFormDAO = getClaimFormDAO();

    ClaimForm expectedClaimForm = new ClaimForm();
    expectedClaimForm.setName( "Test Claim Form" + System.currentTimeMillis() );
    expectedClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform" + System.currentTimeMillis() );
    expectedClaimForm.setDescription( "a description of the claim form" );
    expectedClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) );
    expectedClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // Add a few claimFormSteps to this claimForm before saving.
    ClaimFormStep claimFormStep1 = buildStaticClaimFormStep();
    expectedClaimForm.addClaimFormStep( claimFormStep1 );

    ClaimFormStep claimFormStep2 = buildStaticClaimFormStep();
    expectedClaimForm.addClaimFormStep( claimFormStep2 );

    ClaimFormStepEmailNotification claimFormStepEmailNotification = new ClaimFormStepEmailNotification();
    claimFormStepEmailNotification.setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType.getDefaultItem() );

    claimFormStep2.addClaimFormStepEmailNotification( claimFormStepEmailNotification );

    ClaimFormStep claimFormStep3 = buildStaticClaimFormStep();
    expectedClaimForm.addClaimFormStep( claimFormStep3 );

    // Add Claim Form
    claimFormDAO.saveClaimForm( expectedClaimForm );
    HibernateSessionManager.getSession().flush();

    ClaimForm actualClaimForm = claimFormDAO.getClaimFormById( expectedClaimForm.getId() );

    assertEquals( "ClaimForm not equals", expectedClaimForm, actualClaimForm );

    // Get the ClaimFormStepById
    ClaimFormStep actualClaimFormStep = claimFormDAO.getClaimFormStepById( claimFormStep3.getId() );
    assertEquals( "Actual ClaimFormStep was not equal to expected.", claimFormStep3, actualClaimFormStep );

    // Update the ClaimForm
    expectedClaimForm.setName( "testFormName" + System.currentTimeMillis() );
    claimFormDAO.saveClaimForm( expectedClaimForm );
    HibernateSessionManager.getSession().flush();

    // get Updated ClaimForm from the database(by name)
    actualClaimForm = claimFormDAO.getClaimFormByName( expectedClaimForm.getName() );
    assertEquals( "Updated ClaimForm not equals", expectedClaimForm, actualClaimForm );

    // Delete
    claimFormDAO.deleteClaimForm( actualClaimForm );
    HibernateSessionManager.getSession().flush();

    ClaimForm checkClaimForm = claimFormDAO.getClaimFormByName( expectedClaimForm.getName() );

    if ( checkClaimForm != null )
    {
      assertEquals( "ClaimForm still there after delete", expectedClaimForm, checkClaimForm );
    }
  }

  /**
   * Build a static claimFormStep.
   * 
   * @return ClaimFormStep
   */
  public static ClaimFormStep buildStaticClaimFormStep()
  {
    ClaimFormStep claimFormStep = new ClaimFormStep();
    claimFormStep.setCmKeyFragment( String.valueOf( Math.random() % 29930291 ) );
    claimFormStep.setSalesRequired( true );
    return claimFormStep;
  }

  /**
   * Tests saving and updating a claimFormStep.
   */
  public void testSaveClaimFormStep()
  {

    // Get the claimFormDAO
    ClaimFormDAO claimFormDAO = getClaimFormDAO();

    // Create and Save a new ClaimForm
    ClaimForm createdClaimForm = new ClaimForm();
    createdClaimForm.setName( "Test Claim Form" + System.currentTimeMillis() );
    createdClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform" + System.currentTimeMillis() );
    createdClaimForm.setDescription( "a description of the claim form" );
    createdClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) );
    createdClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    claimFormDAO.saveClaimForm( createdClaimForm );

    // Add a claimFormStep to this claimForm before saving.
    ClaimFormStep createdClaimFormStep = new ClaimFormStep();
    createdClaimFormStep.setSalesRequired( false );
    // Add the newly created claimFormStep to the claimForm
    createdClaimForm.addClaimFormStep( createdClaimFormStep );

    // Create an emailNotificationType and add it to the ClaimFormStep
    ClaimFormStepEmailNotification createdClaimFormStepEmailNotification = new ClaimFormStepEmailNotification();
    createdClaimFormStepEmailNotification.setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType.getDefaultItem() );
    createdClaimFormStep.addClaimFormStepEmailNotification( createdClaimFormStepEmailNotification );
    createdClaimFormStep.setCmKeyFragment( "1234" );
    // Save the claimFormStep
    claimFormDAO.saveClaimFormStep( createdClaimFormStep );

    // Get the claimFormStep by the id.
    ClaimFormStep actualClaimFormStep = claimFormDAO.getClaimFormStepById( createdClaimFormStep.getId() );

    // Assert they are equal.
    assertEquals( "ClaimFormSteps were not equals as expected", actualClaimFormStep, createdClaimFormStep );

    // Get the claimFormStepEmailNotificationById
    ClaimFormStepEmailNotification actualClaimFormStepEmailNotification = claimFormDAO.getClaimFormStepEmailNotificationById( createdClaimFormStepEmailNotification.getId() );
    // Assert they are equal.
    assertEquals( "ClaimFormStepEmailNotification were not equals as expected", actualClaimFormStepEmailNotification, createdClaimFormStepEmailNotification );

    // Flush the session
    HibernateSessionManager.getSession().flush();
    HibernateSessionManager.getSession().clear();

    // Get the claimFormStep from the claimForm just configured and saved.
    // This claimForm and its children will have their ids populated.
    ClaimFormStep expectedClaimFormStep = claimFormDAO.getClaimFormStepById( createdClaimFormStep.getId() );

    // Replicate the expectedClaimFormStep in an object detached from the session and attempt to
    // save it.
    ClaimFormStep updatedClaimFormStep = new ClaimFormStep();
    updatedClaimFormStep.setClaimForm( expectedClaimFormStep.getClaimForm() );
    updatedClaimFormStep.setId( expectedClaimFormStep.getId() );
    // updatedClaimFormStep.setName(expectedClaimFormStep.getName());
    updatedClaimFormStep.setCmKeyFragment( "1234" );
    updatedClaimFormStep.setSalesRequired( expectedClaimFormStep.isSalesRequired() );
    updatedClaimFormStep.setClaimFormStepElements( expectedClaimFormStep.getClaimFormStepElements() );
    updatedClaimFormStep.setClaimFormStepEmailNotifications( expectedClaimFormStep.getClaimFormStepEmailNotifications() );
    updatedClaimFormStep.setVersion( expectedClaimFormStep.getVersion() );
    updatedClaimFormStep.setAuditCreateInfo( expectedClaimFormStep.getAuditCreateInfo() );
    updatedClaimFormStep.setAuditUpdateInfo( expectedClaimFormStep.getAuditUpdateInfo() );

    // Add a new emailNotificationType onto the claimFormStep
    ClaimFormStepEmailNotification claimFormStepEmailNotificationOther1 = new ClaimFormStepEmailNotification();
    claimFormStepEmailNotificationOther1.setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType.lookup( "other2" ) );
    updatedClaimFormStep.addClaimFormStepEmailNotification( claimFormStepEmailNotificationOther1 );

    // Add a new emailNotificationType onto the claimFormStep
    ClaimFormStepEmailNotification claimFormStepEmailNotificationDenied = new ClaimFormStepEmailNotification();
    claimFormStepEmailNotificationDenied.setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType.lookup( "denied" ) );
    updatedClaimFormStep.addClaimFormStepEmailNotification( claimFormStepEmailNotificationDenied );

    int indexOfStep = createdClaimForm.getClaimFormSteps().indexOf( updatedClaimFormStep );

    if ( indexOfStep == -1 )
    {
      fail( "This isn't a new claimFormStep and should be updating an already existing ClaimFormStep" );
    }
    else
    {
      createdClaimForm.addClaimFormStep( indexOfStep, updatedClaimFormStep );
    }

    // Flush the session
    HibernateSessionManager.getSession().flush();
    HibernateSessionManager.getSession().clear();

    // Save the updated claimFormStep.
    claimFormDAO.saveClaimFormStep( updatedClaimFormStep );

    HibernateSessionManager.getSession().clear();

    actualClaimFormStep = claimFormDAO.getClaimFormStepById( updatedClaimFormStep.getId() );

    // --------------------------
    // Get All Claim Form Steps
    // ---------------------------
    assertEquals( "Actual updated claimFormStep wasn't equal to what was expected", updatedClaimFormStep, actualClaimFormStep );
  }

  /**
   * Test saving and deleting a claimFormStep element.
   */
  public void testSaveAndDeleteClaimFormStepElement()
  {

    ClaimFormDAO claimFormDAO = getClaimFormDAO();

    ClaimForm expectedClaimForm = new ClaimForm();
    expectedClaimForm.setName( "Test Claim Form" + System.currentTimeMillis() );
    expectedClaimForm.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform" + System.currentTimeMillis() );
    expectedClaimForm.setDescription( "a description of the claim form" );
    expectedClaimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) );
    expectedClaimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    // Add Claim Form
    ClaimForm savedClaimForm = claimFormDAO.saveClaimForm( expectedClaimForm );
    HibernateSessionManager.getSession().flush();

    // Add a claimFormStep to this claimForm before saving.
    ClaimFormStep claimFormStep1 = new ClaimFormStep();
    // claimFormStep1.setName("testNAME1");
    claimFormStep1.setCmKeyFragment( "11111" );
    claimFormStep1.setSalesRequired( false );
    claimFormStep1.setClaimForm( savedClaimForm );
    savedClaimForm.addClaimFormStep( claimFormStep1 );

    // Add Claim Form
    ClaimForm actualClaimForm = claimFormDAO.saveClaimForm( savedClaimForm );
    HibernateSessionManager.getSession().flush();

    // Create Node
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    Node node = new Node();
    node.setName( "testNAME" + uniqueName );
    node.setDescription( "testDESCRIPTION" );
    node.setPath( "testPATH" );
    node.setParentNode( null );

    node.setNodeType( nodeType );
    node.setHierarchy( hierarchy );

    nodeDAO.saveNode( node );

    HibernateSessionManager.getSession().flush();

    ClaimFormStep claimFormStep = (ClaimFormStep)actualClaimForm.getClaimFormSteps().get( 0 );
    ClaimFormStepElement element = new ClaimFormStepElement();
    element.setCmKeyFragment( "1234444" );
    element.setClaimFormElementType( ClaimFormElementType.lookup( ClaimFormElementType.TEXT_FIELD ) );
    element.setNumberFieldInputFormat( NumberFieldInputFormatType.lookup( NumberFieldInputFormatType.WHOLE ) );
    element.setTextFieldInputFormat( TextFieldInputFormatType.lookup( TextFieldInputFormatType.NORMAL_TEXT ) );
    element.setUniqueness( HierarchyUniqueConstraintEnum.NONE );
    claimFormStep.addClaimFormStepElement( element );

    claimFormDAO.saveClaimForm( actualClaimForm );

    element.setSequenceNumber( new Integer( 0 ) );

    flushAndClearSession();

    LOG.debug( "ELEMENT:ID BEFORE- " + element.getId() );
    flushAndClearSession();
    LOG.debug( "ELEMENT:ID AFTER - " + element.getId() );
    ClaimFormStepElement savedElement = claimFormDAO.getClaimFormStepElementById( element.getId() );
    assertDomainObjectEquals( element, savedElement );

    // Test would fail without next flushAndClearSession, not sure why....
    flushAndClearSession();
    claimFormDAO.deleteClaimFormStepElement( element.getId() );
    flushAndClearSession();

    ClaimFormStepElement deletedElement = claimFormDAO.getClaimFormStepElementById( element.getId() );
    assertNull( deletedElement );
  }

  /**
   * Test the copy claim form
   * 
   * @throws CloneNotSupportedException
   */
  public void testCopyClaimForm() throws CloneNotSupportedException
  {
    ClaimFormDAO claimFormDAO = getClaimFormDAO();

    ClaimForm claimForm = buildClaimFormDomainObjectWithStepsAndElements();
    ClaimForm savedClaimForm = claimFormDAO.saveClaimForm( claimForm );

    HibernateSessionManager.getSession().flush();
    HibernateSessionManager.getSession().clear();

    ClaimForm newClaimForm = new ClaimForm( savedClaimForm );

    newClaimForm.setName( "NEWNAME" + String.valueOf( Math.random() % 29930291 ) );
    newClaimForm.setCmAssetCode( "NEWCMASSET" + String.valueOf( Math.random() % 29930291 ) );

    newClaimForm = claimFormDAO.saveClaimForm( newClaimForm );

    HibernateSessionManager.getSession().flush();

    assertFalse( newClaimForm.getName().equals( claimForm.getName() ) );
    ClaimFormStep oldStep = (ClaimFormStep)claimForm.getClaimFormSteps().get( 0 );
    ClaimFormStep newStep = (ClaimFormStep)newClaimForm.getClaimFormSteps().get( 0 );
    assertNotNull( oldStep.getId() );
    assertNotNull( newStep.getId() );
    assertTrue( oldStep.getId().longValue() != newStep.getId().longValue() );
    // assertTrue(oldStep.getName().equals(newStep.getName()) );
    // TODO - how to copy CM names?

    ClaimFormStepElement oldElement = (ClaimFormStepElement) ( (ClaimFormStep)claimForm.getClaimFormSteps().get( 0 ) ).getClaimFormStepElements().get( 0 );
    ClaimFormStepElement newElement = (ClaimFormStepElement) ( (ClaimFormStep)newClaimForm.getClaimFormSteps().get( 0 ) ).getClaimFormStepElements().get( 0 );
    assertNotNull( oldElement.getId() );
    assertNotNull( newElement.getId() );
    assertTrue( oldElement.getId().longValue() != newElement.getId().longValue() );
    assertTrue( oldElement.getDescription().equals( newElement.getDescription() ) );
  }

  /**
   * @return ClaimForm with children
   */
  public static ClaimForm buildClaimFormDomainObjectWithStepsAndElements()
  {
    ClaimForm claimForm = getClaimFormDomainObject();

    ClaimFormStep claimFormStep1 = getClaimFormStepDomainObject();
    ClaimFormStep claimFormStep2 = getClaimFormStepDomainObject();
    ClaimFormStepElement claimFormStepElement1 = getClaimFormStepElementDomainObject();
    ClaimFormStepElement claimFormStepElement2 = getClaimFormStepElementDomainObject();
    ClaimFormStepElement claimFormStepElement3 = getClaimFormStepElementDomainObject();

    claimFormStep1.addClaimFormStepElement( claimFormStepElement1 );
    claimFormStep1.addClaimFormStepElement( claimFormStepElement2 );

    claimFormStep2.addClaimFormStepElement( claimFormStepElement3 );
    claimForm.addClaimFormStep( claimFormStep1 );
    claimForm.addClaimFormStep( claimFormStep2 );

    return claimForm;
  }

  /**
   * Test the saving of a ClaimForm with an empty step list when the persisted ClaimForm has steps.
   * In this case the steps should not be deleted on the final flush.
   */
  public void testUpdateClaimFormWithEmptySteps()
  {
    ClaimForm claimForm = getClaimFormDomainObject();
    ClaimFormStep expectedClaimFormStep = getClaimFormStepDomainObject();
    claimForm.addClaimFormStep( expectedClaimFormStep );
    getClaimFormDAO().saveClaimForm( claimForm );

    flushAndClearSession();

    // No we create an identical claim form minus the steps
    ClaimForm claimFormCopy = new ClaimForm();
    claimFormCopy.setId( claimForm.getId() );
    claimFormCopy.setVersion( claimForm.getVersion() );
    claimFormCopy.setName( claimForm.getName() );
    claimFormCopy.setCmAssetCode( claimForm.getCmAssetCode() );
    claimFormCopy.getAuditCreateInfo().setCreatedBy( claimForm.getAuditCreateInfo().getCreatedBy() );
    claimFormCopy.getAuditCreateInfo().setDateCreated( claimForm.getAuditCreateInfo().getDateCreated() );

    // put the persisted claim form back in the session.
    ClaimForm dbClaimForm = getClaimFormDAO().getClaimFormById( claimForm.getId() );
    assertNotNull( dbClaimForm );

    // save the modified claim form
    getClaimFormDAO().updateClaimForm( claimFormCopy );

    flushAndClearSession();

    // no we check to be sure that the steps did not get deleted.
    List actualSteps = getClaimFormDAO().getClaimFormById( claimForm.getId() ).getClaimFormSteps();
    assertTrue( actualSteps.contains( expectedClaimFormStep ) );
  }

  /**
   * Tests getting all ClaimForms not under construction.
   */
  public void testGetAllClaimFormsNotUnderConstruction()
  {

    ClaimFormDAO claimFormDAO = getClaimFormDAO();
    int totalCount = 0;
    List claimForms = claimFormDAO.getAll();
    totalCount = claimForms.size();

    System.out.println( "totalCount = " + totalCount );

    int notUnderConstructionCount = 0;

    for ( int i = 0; i < claimForms.size(); i++ )
    {
      if ( ! ( (ClaimForm)claimForms.get( i ) ).getClaimFormStatusType().getCode().equals( "undrconstr" ) && ! ( (ClaimForm)claimForms.get( i ) ).getClaimFormStatusType().getCode().equals( "tmpl" ) )
      {
        notUnderConstructionCount++;
      }
    }

    int actualNotUnderConstructionCount = claimFormDAO.getAllClaimFormsNotUnderConstruction().size();

    System.out.println( "notUnderConstructionCount = " + notUnderConstructionCount );
    System.out.println( "actualNotUnderConstructionCount = " + actualNotUnderConstructionCount );

    assertEquals( "List of claim forms are not the same size.", notUnderConstructionCount, actualNotUnderConstructionCount );

    List expectedClaimForms = new ArrayList();

    ClaimForm expectedClaimForm1 = new ClaimForm();
    expectedClaimForm1.setName( "Test Claim Form Under Contruction" );
    expectedClaimForm1.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform_uc" );
    expectedClaimForm1.setDescription( "a description of the claim form" );
    expectedClaimForm1.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) );
    expectedClaimForm1.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    expectedClaimForms.add( expectedClaimForm1 );
    claimFormDAO.saveClaimForm( expectedClaimForm1 );

    ClaimForm expectedClaimForm2 = new ClaimForm();
    expectedClaimForm2.setName( "Test Claim Form NOT Under Contruction" );
    expectedClaimForm2.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform_nuc" );
    expectedClaimForm2.setDescription( "a description of the claim form" );
    expectedClaimForm2.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) );
    expectedClaimForm2.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
    expectedClaimForms.add( expectedClaimForm2 );
    claimFormDAO.saveClaimForm( expectedClaimForm2 );

    actualNotUnderConstructionCount = claimFormDAO.getAllClaimFormsNotUnderConstruction().size();

    assertEquals( "List of claim forms are not the same size.", notUnderConstructionCount + 1, actualNotUnderConstructionCount );

  }

  /**
   * Tests getting all ClaimForms not under construction.
   */
  public void testGetAllClaimFormsNotUnderConstructionByModuleType()
  {

    ClaimFormDAO claimFormDAO = getClaimFormDAO();
    int totalCount = 0;
    List claimForms = claimFormDAO.getAll();
    totalCount = claimForms.size();

    System.out.println( "totalCount = " + totalCount );

    int notUnderConstructionCount = 0;

    for ( int i = 0; i < claimForms.size(); i++ )
    {
      if ( ! ( (ClaimForm)claimForms.get( i ) ).getClaimFormStatusType().getCode().equals( "undrconstr" ) && ! ( (ClaimForm)claimForms.get( i ) ).getClaimFormStatusType().getCode().equals( "tmpl" )
          && ( (ClaimForm)claimForms.get( i ) ).getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) )
      {
        notUnderConstructionCount++;
      }
    }

    int actualNotUnderConstructionCount = claimFormDAO.getAllClaimFormsNotUnderConstructionByModuleType( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ).size();

    System.out.println( "notUnderConstructionCount = " + notUnderConstructionCount );
    System.out.println( "actualNotUnderConstructionCount = " + actualNotUnderConstructionCount );

    assertEquals( "List of claim forms are not the same size.", notUnderConstructionCount, actualNotUnderConstructionCount );

    List expectedClaimForms = new ArrayList();

    ClaimForm expectedClaimForm1 = new ClaimForm();
    expectedClaimForm1.setName( "Test Claim Form Under Contruction (Rec)" );
    expectedClaimForm1.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform_uc_rec" );
    expectedClaimForm1.setDescription( "a description of the claim form" );
    expectedClaimForm1.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_RECOGNITION ) );
    expectedClaimForm1.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    expectedClaimForms.add( expectedClaimForm1 );
    claimFormDAO.saveClaimForm( expectedClaimForm1 );

    ClaimForm expectedClaimForm2 = new ClaimForm();
    expectedClaimForm2.setName( "Test Claim Form Under Contruction (Prd)" );
    expectedClaimForm2.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform_uc_prd" );
    expectedClaimForm2.setDescription( "a description of the claim form" );
    expectedClaimForm2.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) );
    expectedClaimForm2.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    expectedClaimForms.add( expectedClaimForm2 );
    claimFormDAO.saveClaimForm( expectedClaimForm2 );

    ClaimForm expectedClaimForm3 = new ClaimForm();
    expectedClaimForm3.setName( "Test Claim Form NOT Under Contruction (Rec)" );
    expectedClaimForm3.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform_nuc_rec" );
    expectedClaimForm3.setDescription( "a description of the claim form" );
    expectedClaimForm3.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_RECOGNITION ) );
    expectedClaimForm3.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
    expectedClaimForms.add( expectedClaimForm3 );
    claimFormDAO.saveClaimForm( expectedClaimForm3 );

    ClaimForm expectedClaimForm4 = new ClaimForm();
    expectedClaimForm4.setName( "Test Claim Form NOT Under Contruction (Prd)" );
    expectedClaimForm4.setCmAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX + "testclaimform_nuc_prd" );
    expectedClaimForm4.setDescription( "a description of the claim form" );
    expectedClaimForm4.setClaimFormModuleType( ClaimFormModuleType.lookup( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) );
    expectedClaimForm4.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
    expectedClaimForms.add( expectedClaimForm4 );
    claimFormDAO.saveClaimForm( expectedClaimForm4 );

    actualNotUnderConstructionCount = claimFormDAO.getAllClaimFormsNotUnderConstructionByModuleType( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ).size();

    assertEquals( "List of claim forms are not the same size.", notUnderConstructionCount + 1, actualNotUnderConstructionCount );

  }

  /**
   * @return ClaimForm Random Domain object
   */
  private static ClaimForm getClaimFormDomainObject()
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" + uniqueName );
    claimForm.setCmAssetCode( "claimform.asset" + uniqueName );
    claimForm.setDescription( "Description of the test claim form" + uniqueName );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    return claimForm;
  }

  /**
   * @return ClaimFormStep Random Domain object
   */
  private static ClaimFormStep getClaimFormStepDomainObject()
  {
    ClaimFormStep claimFormStep = new ClaimFormStep();
    claimFormStep.setCmKeyFragment( String.valueOf( Math.random() % 29930291 ) );
    claimFormStep.setSalesRequired( true );

    return claimFormStep;
  }

  /**
   * @return ClaimFormStepElement Random Domain object
   */
  private static ClaimFormStepElement getClaimFormStepElementDomainObject()
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
    claimFormStepElement.setClaimFormElementType( ClaimFormElementType.lookup( "copy" ) );
    claimFormStepElement.setCmKeyFragment( "1xecec2de4eed" + uniqueName );
    claimFormStepElement.setDescription( "Description of the test claim form" + uniqueName );

    return claimFormStepElement;
  }

  /**
   * Get the ClaimFormDAO.
   * 
   * @return ClaimFormDAO
   */
  private ClaimFormDAO getClaimFormDAO()
  {
    return (ClaimFormDAO)ApplicationContextFactory.getApplicationContext().getBean( "claimFormDAO" );
  }

  /**
   * Get the NodeDAO from the beanFactory.
   * 
   * @return NodeDAO
   */
  private NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeDAO" );
  }

} // end ClaimFormDAOImplTest
