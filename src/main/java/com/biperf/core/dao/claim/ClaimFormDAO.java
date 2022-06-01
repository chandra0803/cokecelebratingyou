/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/claim/ClaimFormDAO.java,v $
 */

package com.biperf.core.dao.claim;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.service.AssociationRequestCollection;

/**
 * ClaimFormDAO.
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
 */
public interface ClaimFormDAO extends DAO
{

  public static final String BEAN_NAME = "claimFormDAO";

  /**
   * Save the claimFormStep.
   * 
   * @param claimFormStep
   * @return ClaimFormStep
   */
  public ClaimFormStep saveClaimFormStep( ClaimFormStep claimFormStep );

  /**
   * Get the ClaimForm by id.
   * 
   * @param id
   * @return ClaimForm
   */
  public ClaimForm getClaimFormById( Long id );

  /**
   * Get the ClaimFormStepEmailNotification by id.
   * 
   * @param id
   * @return ClaimFormStepEmailNotification
   */
  public ClaimFormStepEmailNotification getClaimFormStepEmailNotificationById( Long id );

  /**
   * @param id
   * @param associationRequestCollection
   * @return ClaimForm with Associations
   */
  public ClaimForm getClaimFormByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the ClaimForm by name.
   * 
   * @param formName
   * @return ClaimForm
   */
  public ClaimForm getClaimFormByName( String formName );

  /**
   * Get All ClaimForm records.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Save or update the ClaimForm.
   * 
   * @param claimForm
   * @return ClaimForm throws ConstraintViolationException
   * @throws ConstraintViolationException
   */
  public ClaimForm saveClaimForm( ClaimForm claimForm ) throws ConstraintViolationException;

  /**
   * Delete the ClaimForm.
   * 
   * @param claimForm
   */
  public void deleteClaimForm( ClaimForm claimForm );

  /**
   * Update the claimForm record. Implementations should take into account the Object tree and
   * update reference to this child in the parent. Hibernate's session.merge will take care of this.
   * 
   * @param claimForm
   * @return ClaimForm throws ConstraintViolationException
   * @throws ConstraintViolationException
   */
  public ClaimForm updateClaimForm( ClaimForm claimForm ) throws ConstraintViolationException;

  /**
   * Get the ClaimFormStep by its id.
   * 
   * @param claimFormStepId
   * @return ClaimFormStep
   */
  public ClaimFormStep getClaimFormStepById( Long claimFormStepId );

  /**
   * Get the list of all ClaimFormSteps associated to the claimFormId.
   */
  public List<ClaimFormStepElement> getAllClaimFormStepElementsByClaimFormId( Long claimFormId );

  /**
   * Get the list of numeric ClaimFormSteps associated to the claimFormId.
   * 
   * @param claimFormId
   * @param type
   * @return List
   */
  public List getAllClaimFormStepElementsByClaimFormIdElementType( Long claimFormId, ClaimFormElementType type );

  /**
   * @param id
   * @param associationRequestCollection
   * @return ClaimFormStep with Associations
   */
  public ClaimFormStep getClaimFormStepByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets a claim form step element by ID
   * 
   * @param id
   * @return ClaimFormStepElement
   */
  public ClaimFormStepElement getClaimFormStepElementById( Long id );

  /**
   * Does a physical delete on a claim form step element
   * 
   * @param id
   */
  public void deleteClaimFormStepElement( Long id );

  /**
   * updates an existing claimformstepElement
   * 
   * @param element
   * @return ClaimFormStepElement
   */
  public ClaimFormStepElement updateClaimFormStepElement( ClaimFormStepElement element );

  /**
   * Save a claimFormStep for the claimForm associated to the claimFormId param.
   * 
   * @param claimFormId
   * @param managedClaimFormStep
   * @return ClaimFormStep
   */
  public ClaimFormStep saveClaimFormStep( Long claimFormId, ClaimFormStep managedClaimFormStep );

  /**
   * Selects all ClaimForms that are not under construction
   * 
   * @return List of claim form Steps
   */
  public List getAllClaimFormsNotUnderConstruction();

  /**
   * Selects all ClaimForms that are not under construction by moduleType
   * 
   * @return List of claim form Steps
   */
  public List getAllClaimFormsNotUnderConstructionByModuleType( String moduleType );

} // end ClaimFormDAO
