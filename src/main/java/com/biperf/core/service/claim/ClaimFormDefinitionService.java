/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/claim/ClaimFormDefinitionService.java,v $
 */

package com.biperf.core.service.claim;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * ClaimFormService.
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
public interface ClaimFormDefinitionService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "claimFormDefinitionService";

  /**
   * Gets the ClaimForm by the id.
   * 
   * @param claimFormId
   * @return ClaimForm
   */
  public ClaimForm getClaimFormById( Long claimFormId );

  /**
   * Gets a list of all ClaimFormStepElement for a given claim form
   */
  public List<ClaimFormStepElement> getAllClaimFormStepElementsByClaimFormId( Long claimFormId );

  /**
   * Gets a list of ClaimFormStepElement objects by id.
   * 
   * @param claimFormId
   * @param type
   * @return List list of ClaimFormStepElement objects
   */
  public List getAllClaimFormStepElementsByClaimFormIdElementType( Long claimFormId, ClaimFormElementType type );

  /**
   * @param id
   * @param associationRequestCollection
   * @return ClaimForm
   */
  public ClaimForm getClaimFormByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Return a List of all ClaimForms.
   * 
   * @return List of ClaimForms
   */
  public List getAll();

  /**
   * Return a Map of Lists of ClaimForms. Each List has the types. (Template, UnderConstruction
   * etc...)
   * 
   * @return Map of ClaimForm Lists for all Types
   */
  public Map getFormLibraryMap();

  /**
   * Saves the ClaimForm to the database.
   * 
   * @param claimForm
   * @param createCmAsset
   * @return ClaimForm
   * @throws ServiceErrorException
   */
  public ClaimForm saveClaimForm( ClaimForm claimForm, boolean createCmAsset ) throws ServiceErrorException;

  /**
   * Delete the ClaimForm from the database.
   * 
   * @param claimFormId
   * @throws ServiceErrorException
   */
  public void deleteClaimForm( Long claimFormId ) throws ServiceErrorException;

  /**
   * Delete all the ClaimForms in the param list from the database.
   * 
   * @param claimFormIds
   * @throws ServiceErrorException
   */
  public void deleteClaimForms( List claimFormIds ) throws ServiceErrorException;

  /**
   * Copy the claimForm to a new claimForm.
   * 
   * @param claimFormIdToCopy
   * @param newFormName
   * @return ClaimForm (the copied ClaimForm)
   * @throws ServiceErrorException
   */
  public ClaimForm copyClaimForm( Long claimFormIdToCopy, String newFormName ) throws ServiceErrorException;

  /**
   * Verifies the ClaimFormStep is at a state that can be previewed or it will throw an exception.
   * 
   * @param claimFormStepId
   * @throws ServiceErrorException
   */
  public void preparePreviewClaimForm( Long claimFormStepId ) throws ServiceErrorException;

  /**
   * Get the Claim Form Step list for a claimForm by claimFormId.
   * 
   * @param claimFormId
   * @return List
   */
  public List getClaimFormSteps( Long claimFormId );

  /**
   * Get the ClaimFormStep for a claimForm by claimFormStepId.
   * 
   * @param claimFormStepId
   * @return ClaimFormStep
   */
  public ClaimFormStep getClaimFormStep( Long claimFormStepId );

  /**
   * Get the ClaimFormStep for a claimForm by claimFormStepId
   * 
   * @param claimFormStepId
   * @param associationRequestCollection
   * @return ClaimFormStep
   */
  public ClaimFormStep getClaimFormStepWithAssociations( Long claimFormStepId, AssociationRequestCollection associationRequestCollection );

  /**
   * Save the claimFormStep for the claimForm identified by the claimFormId param.
   * 
   * @param claimFormId
   * @param claimFormStep
   * @param claimFormStepName
   * @return ClaimFormStep
   * @throws ServiceErrorException
   */
  public ClaimFormStep saveClaimFormStep( Long claimFormId, ClaimFormStep claimFormStep, String claimFormStepName ) throws ServiceErrorException;

  /**
   * Adds a claimFormStepElement object, provided the form is either "Under Construction" or
   * "Completed".
   * 
   * @param claimFormId
   * @param claimFormStepId
   * @param element
   * @param data
   * @throws ServiceErrorException
   */
  public void addClaimFormStepElement( Long claimFormId, Long claimFormStepId, ClaimFormStepElement element, ClaimFormStepElementCMDataHolder data ) throws ServiceErrorException;

  /**
   * Updates a claimFormStepElement object, provided the form is either "Under Construction" or
   * "Completed".
   * 
   * @param claimFormId
   * @param claimFormStepId
   * @param element
   * @param data
   * @throws ServiceErrorException
   */
  public void updateClaimFormStepElement( Long claimFormId, Long claimFormStepId, ClaimFormStepElement element, ClaimFormStepElementCMDataHolder data ) throws ServiceErrorException;

  /**
   * Returns a claimFormStepElement associated to the elementId param.
   * 
   * @param elementId
   * @return ClaimFormStepElement
   */
  public ClaimFormStepElement getClaimFormStepElementById( Long elementId );

  /**
   * Deletes the list of claimFormStepElements
   * 
   * @param claimFormId
   * @param claimFormStepId
   * @param claimFormStepElementIds List of Long objects
   */
  public void deleteClaimFormStepElements( Long claimFormId, Long claimFormStepId, List claimFormStepElementIds );

  /**
   * Move the specified claimFormStep to the newIndex and shift all other ClaimFormSteps down.
   * 
   * @param claimFormId
   * @param claimFormStepId
   * @param newIndex
   */
  public void reorderClaimFormStep( Long claimFormId, Long claimFormStepId, int newIndex );

  /**
   * Move the specified claimFormStepElement to the newIndex and shift all other
   * ClaimFormStepElements down.
   * 
   * @param claimFormStepId
   * @param claimFormStepElementId
   * @param newIndex
   * @return ClaimFormStep
   */
  public ClaimFormStep reorderClaimFormStepElement( Long claimFormStepId, Long claimFormStepElementId, int newIndex );

  /**
   * Delete a list of claimFormStepIds for this form.
   * 
   * @param claimFormId
   * @param claimFormStepIds
   */
  public void deleteClaimFormSteps( Long claimFormId, List claimFormStepIds );

  /**
   * @param claimFormStepId
   * @param claimFormStepElementsToSave
   * @param claimFormStepElementsToDelete
   */
  public void saveCustomerInformationBlockElements( Long claimFormStepId, List claimFormStepElementsToSave, List claimFormStepElementsToDelete );

  /**
   * Get the ClaimFormStepElementCMDataHolder for the ClaimFormStepElement.
   * 
   * @param claimFormAsset
   * @param element
   * @return ClaimFormStepElementCMDataHolder
   */
  public ClaimFormStepElementCMDataHolder getClaimFormStepElementCMDataHolder( String claimFormAsset, ClaimFormStepElement element );

  /**
   * Selects all ClaimForms that are not under construction
   * 
   * @return List of claim form Steps
   */
  public List getAllClaimFormsNotUnderConstruction();

  /**
   * Selects all ClaimForms that are not under construction by module type
   * 
   * @return List of claim form Steps
   */
  public List getAllClaimFormsNotUnderConstructionByModuleType( String moduleType );

  /**
   * Updates the claim form status. Should be called whenever a promotion is
   * associated/disassociated from a claim form And whenever a promotion is deleted
   * 
   * @param claimForm
   */
  public void updateClaimFormStatus( Long claimForm );

} // end interface ClaimFormService
