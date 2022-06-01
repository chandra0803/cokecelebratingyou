/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/impl/ClaimFormDefinitionServiceImpl.java,v $
 */

package com.biperf.core.service.claim.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimFormStepAssociationRequest;
import com.biperf.core.service.claim.ClaimFormStepElementCMDataHolder;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.HibernateSessionManager;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ClaimFormServiceImpl. <p/> <b>Change History:</b><br>
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
public class ClaimFormDefinitionServiceImpl implements ClaimFormDefinitionService
{
  /**
   * ClaimFormDAO *
   */
  private ClaimFormDAO claimFormDAO;
  private CMAssetService cmAssetService;
  private static final String ASSET_TYPE_NAME_PREFIX_CLAIM_FORM = "_ClaimFormData_";

  /**
   * Set the ClaimFormDAO through IoC
   * 
   * @param claimFormDAO
   */
  public void setClaimFormDAO( ClaimFormDAO claimFormDAO )
  {
    this.claimFormDAO = claimFormDAO;
  }

  /**
   * Set the CMAssetService through IoC
   * 
   * @param cmAssetService
   */
  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getClaimFormById(java.lang.Long)
   * @param claimFormId
   * @return ClaimForm
   */
  public ClaimForm getClaimFormById( Long claimFormId )
  {
    ClaimForm claimForm = this.claimFormDAO.getClaimFormById( claimFormId );
    return claimForm;
  }

  @Override
  public List<ClaimFormStepElement> getAllClaimFormStepElementsByClaimFormId( Long claimFormId )
  {
    List<ClaimFormStepElement> claimFormStepElements = this.claimFormDAO.getAllClaimFormStepElementsByClaimFormId( claimFormId );
    if ( claimFormStepElements != null && claimFormStepElements.size() > 0 )
    {
      Iterator<ClaimFormStepElement> iter = claimFormStepElements.iterator();
      while ( iter.hasNext() )
      {
        ClaimFormStepElement element = iter.next();
        Hibernate.initialize( element.getClaimFormStep() );
      }
    }
    return claimFormStepElements;
  }

  /**
   * Overridden from.
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getClaimFormById(java.lang.Long)
   * @param claimFormId
   * @param type
   * @return List list of ClaimFormStepElement objects
   */
  public List getAllClaimFormStepElementsByClaimFormIdElementType( Long claimFormId, ClaimFormElementType type )
  {
    List claimFormStepElements = this.claimFormDAO.getAllClaimFormStepElementsByClaimFormIdElementType( claimFormId, type );
    if ( claimFormStepElements != null && claimFormStepElements.size() > 0 )
    {
      Iterator iter = claimFormStepElements.iterator();
      while ( iter.hasNext() )
      {
        ClaimFormStepElement element = (ClaimFormStepElement)iter.next();
        Hibernate.initialize( element.getClaimFormStep() );
      }
    }
    return claimFormStepElements;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getClaimFormById(java.lang.Long)
   * @param id
   * @param associationRequestCollection
   * @return ClaimForm
   */
  public ClaimForm getClaimFormByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    ClaimForm claimForm = this.claimFormDAO.getClaimFormByIdWithAssociations( id, associationRequestCollection );
    return claimForm;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getAll()
   * @return List
   */
  public List getAll()
  {
    return this.claimFormDAO.getAll();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#saveClaimForm(com.biperf.core.domain.claim.ClaimForm,
   *      boolean)
   * @param claimFormToSave
   * @param createCmAsset
   * @return ClaimForm
   * @throws ServiceErrorException
   */
  public ClaimForm saveClaimForm( ClaimForm claimFormToSave, boolean createCmAsset ) throws ServiceErrorException
  {
    // --------------------------------------------------------------
    // Check to see if the claimForm already exists in the database
    // ---------------------------------------------------------------
    ClaimForm dbClaimForm = this.claimFormDAO.getClaimFormByName( claimFormToSave.getName() );
    if ( dbClaimForm != null )
    {
      // if we found a record in the database with the given form Name,
      // and our claimFormToSave ID is null (trying to add a new one),
      // we are trying to insert a duplicate record.
      if ( claimFormToSave.getId() == null )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_DUPLICATE_ERR );
      }

      // if we found a record in the database with the given formName,
      // but the ids are not equal (trying to update), we are trying to update to a
      // formName that already exists so throw a Duplicate Exception
      if ( dbClaimForm.getId().compareTo( claimFormToSave.getId() ) != 0 )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_DUPLICATE_ERR );
      }
    }

    try
    {
      if ( claimFormToSave.getId() == null )
      {
        // --------
        // ADDING
        // --------
        // Setup the CM pieces
        // Create Unique Asset
        String newAssetName = cmAssetService.getUniqueAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX );
        claimFormToSave.setCmAssetCode( newAssetName );

        // While adding, default claim form status to "under construction"
        claimFormToSave.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

        dbClaimForm = this.claimFormDAO.saveClaimForm( claimFormToSave );
      }
      else
      {
        // --------
        // Updating
        // --------
        ClaimForm dbCurrentClaimForm = this.claimFormDAO.getClaimFormById( claimFormToSave.getId() );
        if ( !dbCurrentClaimForm.isEditable() )
        {
          // Cannot edit the form
          throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_UNEDITABLE_ERR );
        }

        dbClaimForm = this.claimFormDAO.updateClaimForm( claimFormToSave );
      }

      if ( createCmAsset )
      {
        List cmDataElements = new ArrayList( 3 );
        cmDataElements.add( new CMDataElement( "Claim Form Name", "NAME", claimFormToSave.getName(), true ) );
        cmDataElements.add( new CMDataElement( "Claim Form Description", "DESCRIPTION", claimFormToSave.getDescription(), false ) );
        cmDataElements.add( new CMDataElement( "Claim Form Module Type", "TYPE", claimFormToSave.getClaimFormModuleType().getName(), false ) );

        cmAssetService.createOrUpdateAsset( ClaimForm.CM_CLAIM_FORM_SECTION,
                                            getClaimFormAssetName( claimFormToSave.getCmAssetCode() ),
                                            claimFormToSave.getName(),
                                            claimFormToSave.getCmAssetCode(),
                                            cmDataElements );
      }
    }
    catch( ConstraintViolationException cve )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_DUPLICATE_ERR, cve );
    }

    return dbClaimForm;
  } // end saveClaimForm

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#deleteClaimForm(java.lang.Long)
   * @param claimFormId
   * @throws ServiceErrorException
   */
  public void deleteClaimForm( Long claimFormId ) throws ServiceErrorException
  {
    // Check to see if the claimForm has any budgets.
    ClaimForm dbClaimForm = this.claimFormDAO.getClaimFormById( claimFormId );
    if ( dbClaimForm != null )
    {
      if ( !dbClaimForm.isDeleteable() )
      {
        // TODO - When CM can handle delete, delete the CM asset
        if ( dbClaimForm.getClaimFormStatusType().isUnderConstruction() )
        {
          // The claim form is at a status that we cannot delete
          throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_DELETE_STATUS_ERR );
        }
        else
        {
          // The claim form is linked to only expired promotions
          throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_DELETE_LINKED_ERR );
        }
      }
    }

    this.claimFormDAO.deleteClaimForm( dbClaimForm );

  } // end deleteClaimForm

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getClaimFormSteps(java.lang.Long)
   * @param claimFormId
   * @return List of ClaimFormSteps
   */
  public List getClaimFormSteps( Long claimFormId )
  {
    ClaimForm claimForm = claimFormDAO.getClaimFormById( claimFormId );

    List claimFormSteps = claimForm.getClaimFormSteps();

    Hibernate.initialize( claimFormSteps );

    return claimFormSteps;
  } // end getClaimFormSteps

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getClaimFormStep(java.lang.Long)
   * @param claimFormStepId
   * @return ClaimFormStep
   */
  public ClaimFormStep getClaimFormStep( Long claimFormStepId )
  {
    ClaimFormStep claimFormStep = claimFormDAO.getClaimFormStepById( claimFormStepId );

    return claimFormStep;
  }

  /**
   * Save or Update the changes made to the claimFormStep. Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#saveClaimFormStep(java.lang.Long,com.biperf.core.domain.claim.ClaimFormStep,String)
   * @param claimFormId
   * @param managedClaimFormStep
   * @param claimFormStepName
   * @return ClaimFormStep
   * @throws ServiceErrorException
   */
  public ClaimFormStep saveClaimFormStep( Long claimFormId, ClaimFormStep managedClaimFormStep, String claimFormStepName ) throws ServiceErrorException
  {

    ClaimForm claimForm = this.claimFormDAO.getClaimFormById( claimFormId );
    // CMType cmType = null;

    if ( !claimForm.isEditable() )
    {
      // Cannot edit the steps
      throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_STEP_UNEDITABLE_ERR );
    }

    // Perform uniqueness check
    List existingClaimFormSteps = claimForm.getClaimFormSteps();
    for ( Iterator iter = existingClaimFormSteps.iterator(); iter.hasNext(); )
    {
      ClaimFormStep existingClaimFormStep = (ClaimFormStep)iter.next();
      String existingClaimFormStepName = ContentReaderManager.getText( claimForm.getCmAssetCode(), existingClaimFormStep.getCmKeyForName() );
      if ( claimFormStepName.equals( existingClaimFormStepName ) )
      {
        // match found
        // ignore if id is same since we are updating
        if ( !existingClaimFormStep.getId().equals( managedClaimFormStep.getId() ) )
        {
          throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_STEP_DUPLICATE_ERR );
        }
      }
    }

    // Saving the claimFormStep could result in a unique constraint on the name within the
    // claimForm.
    try
    {
      // Check to see if this is a new or updated step
      if ( null != managedClaimFormStep.getId() && managedClaimFormStep.getId().longValue() != 0 )
      {
        // ---------------
        // update step
        // ---------------
        // CM Integration
        CMDataElement cmDataElement = new CMDataElement( "Claim Form Step Name", managedClaimFormStep.getCmKeyForName(), claimFormStepName, false );

        cmAssetService.createOrUpdateAsset( ClaimForm.CM_CLAIM_FORM_SECTION, getClaimFormAssetName( claimForm.getCmAssetCode() ), claimForm.getName(), claimForm.getCmAssetCode(), cmDataElement );

        this.claimFormDAO.saveClaimFormStep( claimFormId, managedClaimFormStep );
      }
      else
      {
        // -----------
        // new step
        // -----------
        // CM Integration
        String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
        managedClaimFormStep.setCmKeyFragment( cmKeyFragment );
        CMDataElement cmDataElement = new CMDataElement( "Claim Form Step Name", managedClaimFormStep.getCmKeyForName(), claimFormStepName, false );

        cmAssetService.createOrUpdateAsset( ClaimForm.CM_CLAIM_FORM_SECTION, getClaimFormAssetName( claimForm.getCmAssetCode() ), claimForm.getName(), claimForm.getCmAssetCode(), cmDataElement );

        // add the new step
        claimForm.addClaimFormStep( managedClaimFormStep );

        this.claimFormDAO.saveClaimFormStep( managedClaimFormStep );
      }
    }
    catch( Exception e )
    {
      HibernateSessionManager.getSession().clear();
      throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_STEP_DUPLICATE_ERR, e );
    }

    return managedClaimFormStep;

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getFormLibraryMap()
   * @return Map
   */
  public Map getFormLibraryMap()
  {
    Map formLibraryMap = new HashMap();
    List allClaimForms = this.claimFormDAO.getAll();

    List formTemplates = new ArrayList();
    List underConstructionForms = new ArrayList();
    List completedForms = new ArrayList();
    List assignedForms = new ArrayList();

    Iterator iter = allClaimForms.iterator();
    while ( iter.hasNext() )
    {
      ClaimForm claimForm = (ClaimForm)iter.next();

      if ( claimForm.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.TEMPLATE ) )
      {
        formTemplates.add( claimForm );
      }
      else if ( claimForm.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.UNDER_CONSTRUCTION ) )
      {
        underConstructionForms.add( claimForm );
      }
      else if ( claimForm.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.COMPLETED ) )
      {
        completedForms.add( claimForm );
      }
      else if ( claimForm.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.ASSIGNED ) )
      {
        assignedForms.add( claimForm );
      }
    }

    formLibraryMap.put( ClaimFormStatusType.TEMPLATE, formTemplates );
    formLibraryMap.put( ClaimFormStatusType.UNDER_CONSTRUCTION, underConstructionForms );
    formLibraryMap.put( ClaimFormStatusType.COMPLETED, completedForms );
    formLibraryMap.put( ClaimFormStatusType.ASSIGNED, assignedForms );

    return formLibraryMap;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#deleteClaimForms(java.util.List)
   * @param claimFormIds
   * @throws ServiceErrorException
   */
  public void deleteClaimForms( List claimFormIds ) throws ServiceErrorException
  {
    Iterator iter = claimFormIds.iterator();
    while ( iter.hasNext() )
    {
      Long claimFormId = (Long)iter.next();

      this.deleteClaimForm( claimFormId );
    }
  }

  /**
   * Adds a claimFormStepElement to the given Step.
   * 
   * @param claimFormId
   * @param claimFormStepId
   * @param element
   * @param data
   * @throws ServiceErrorException
   */
  public void addClaimFormStepElement( Long claimFormId, Long claimFormStepId, ClaimFormStepElement element, ClaimFormStepElementCMDataHolder data ) throws ServiceErrorException
  {
    ClaimForm form = getClaimFormById( claimFormId );
    // CMType cmType = null;

    // This should not normally happen, unless someone is trying to URL hack
    if ( !form.isEditable() )
    {
      throw new BeaconRuntimeException( "Attempt to edit an uneditable form" );
    }

    // Perform uniqueness check
    validateClaimStepElementUniqueness( claimFormStepId, element, data, form );

    // ---------------
    // CM Integration
    // ---------------
    String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
    element.setCmKeyFragment( cmKeyFragment );
    List cmDataElements = new ArrayList();
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_ELEMENT_LABEL_DESC_SUFFIX, element.getCmKeyForElementLabel(), data.getElementLabel() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_HEADING_DESC_SUFFIX, element.getCmKeyForHeading(), data.getHeading() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_COPY_BLOCK_DESC_SUFFIX, element.getCmKeyForCopyBlock(), data.getCopyBlock(), DataTypeEnum.HTML );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_LINK_NAME_DESC_SUFFIX, element.getCmKeyForLinkName(), data.getLinkName() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_LABEL_TRUE_DESC_SUFFIX, element.getCmKeyForLabelTrue(), data.getLabelTrue() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_LABEL_FALSE_DESC_SUFFIX, element.getCmKeyForLabelFalse(), data.getLabelFalse() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_BUTTON_LABEL_DESC_SUFFIX, element.getCmKeyForButton(), data.getButtonLabel() );
    cmAssetService.createOrUpdateAsset( ClaimForm.CM_CLAIM_FORM_SECTION, getClaimFormAssetName( form.getCmAssetCode() ), form.getName(), form.getCmAssetCode(), cmDataElements );

    // end cm integration

    ClaimFormStep step = (ClaimFormStep)ClaimFormStep.getBusinessObjectWithId( form.getClaimFormSteps(), claimFormStepId );

    // Claim form for ssi can have only one attachment claim element
    if ( step.getClaimForm().getClaimFormModuleType().isSsi() )
    {
      for ( ClaimFormStepElement stepElement : step.getClaimFormStepElements() )
      {
        if ( element.getClaimFormElementType().isFile() && stepElement.getClaimFormElementType().isFile() )
        {
          throw new ServiceErrorException( "ssi_contest.claims.ONLY_ONE_ATTACHMENT" );
        }
      }
    }

    // Only one step element can be marked as the why field at a time
    if ( element.isWhyField() )
    {
      List<ClaimFormStepElement> siblingStepElements = getAllClaimFormStepElementsByClaimFormId( claimFormId );
      long numWhyFieldElements = siblingStepElements.stream().filter( ( el ) -> el.isWhyField() ).count();
      if ( numWhyFieldElements > 0 )
      {
        throw new ServiceErrorException( "claims.form.step.element.MULTIPLE_WHY_FIELDS" );
      }
    }

    step.addClaimFormStepElement( element );
  }

  private void validateClaimStepElementUniqueness( Long claimFormStepId, ClaimFormStepElement element, ClaimFormStepElementCMDataHolder data, ClaimForm form ) throws ServiceErrorException
  {
    List existingClaimFormSteps = form.getClaimFormSteps();
    for ( Iterator iter = existingClaimFormSteps.iterator(); iter.hasNext(); )
    {
      ClaimFormStep existingClaimFormStep = (ClaimFormStep)iter.next();
      // find matching step id
      if ( existingClaimFormStep.getId().equals( claimFormStepId ) )
      {
        List existingClaimFormStepElements = existingClaimFormStep.getClaimFormStepElements();

        Iterator iter1 = existingClaimFormStepElements.iterator();
        if ( iter1.hasNext() )
        {
          ClaimFormStepElement existingClaimFormStepElement = (ClaimFormStepElement)iter1.next();
          String existingClaimFormStepElementName = ContentReaderManager.getText( form.getCmAssetCode(), existingClaimFormStepElement.getCmKeyForElementLabel() );
          if ( data.getElementLabel().equals( existingClaimFormStepElementName ) )
          {
            // name match found
            // ignore if id is same since we are updating
            if ( !existingClaimFormStepElement.getId().equals( element.getId() ) )
            {
              throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_STEP_ELEMENT_DUPLICATE_ERR );
            }
          }
        }
      }
    }
  }

  /**
   * Process the cmElements.
   * 
   * @param cmDataElements
   * @param label
   * @param key
   * @param data
   */
  private void addCMMetaData( List cmDataElements, String label, String key, String data )
  {
    if ( StringUtils.isNotEmpty( data ) )
    {
      cmDataElements.add( new CMDataElement( label, key, data ) );
    }
  }

  /**
   * Process the cmElements.
   * 
   * @param cmDataElements
   * @param label
   * @param key
   * @param data
   * @param dataType
   */
  private void addCMMetaData( List cmDataElements, String label, String key, String data, DataTypeEnum dataType )
  {
    if ( StringUtils.isNotEmpty( data ) )
    {
      cmDataElements.add( new CMDataElement( label, key, data, false, dataType ) );
    }
  }

  /**
   * Get the ClaimFormStepElementCMDataHolder for the ClaimFormStepElement.
   * 
   * @param claimFormAsset
   * @param element
   * @return ClaimFormStepElementCMDataHolder
   */
  public ClaimFormStepElementCMDataHolder getClaimFormStepElementCMDataHolder( String claimFormAsset, ClaimFormStepElement element )
  {
    ClaimFormStepElementCMDataHolder cmDataHolder = new ClaimFormStepElementCMDataHolder();
    cmDataHolder.setElementLabel( ContentReaderManager.getText( claimFormAsset, element.getCmKeyForElementLabel() ) );
    cmDataHolder.setHeading( ContentReaderManager.getText( claimFormAsset, element.getCmKeyForHeading() ) );
    cmDataHolder.setCopyBlock( ContentReaderManager.getText( claimFormAsset, element.getCmKeyForCopyBlock() ) );
    cmDataHolder.setLinkName( ContentReaderManager.getText( claimFormAsset, element.getCmKeyForLinkName() ) );
    cmDataHolder.setLabelTrue( ContentReaderManager.getText( claimFormAsset, element.getCmKeyForLabelTrue() ) );
    cmDataHolder.setLabelFalse( ContentReaderManager.getText( claimFormAsset, element.getCmKeyForLabelFalse() ) );
    cmDataHolder.setButtonLabel( ContentReaderManager.getText( claimFormAsset, element.getCmKeyForButton() ) );

    return cmDataHolder;
  }

  /**
   * modifies a claimFormStepElement for the given Step and form
   * 
   * @param claimFormId
   * @param claimFormStepId
   * @param element
   * @param data
   * @throws ServiceErrorException
   */
  public void updateClaimFormStepElement( Long claimFormId, Long claimFormStepId, ClaimFormStepElement element, ClaimFormStepElementCMDataHolder data ) throws ServiceErrorException
  {
    ClaimForm form = getClaimFormById( claimFormId );

    // This should not normally happen, unless someone is trying to URL hack
    if ( !form.isEditable() )
    {
      throw new BeaconRuntimeException( "Attempt to edit an uneditable form" );
    }

    validateClaimStepElementUniqueness( claimFormStepId, element, data, form );

    ClaimFormStepElement existingElement = claimFormDAO.getClaimFormStepElementById( element.getId() );
    // we don't need to do anything with the existingElement - we just want to load it in session so
    // we can merge.
    existingElement.getId();

    claimFormDAO.updateClaimFormStepElement( element );

    // ----------------
    // CM Integration
    // ----------------
    List cmDataElements = new ArrayList();
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_ELEMENT_LABEL_DESC_SUFFIX, element.getCmKeyForElementLabel(), data.getElementLabel() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_HEADING_DESC_SUFFIX, element.getCmKeyForHeading(), data.getHeading() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_COPY_BLOCK_DESC_SUFFIX, element.getCmKeyForCopyBlock(), data.getCopyBlock(), DataTypeEnum.HTML );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_LINK_NAME_DESC_SUFFIX, element.getCmKeyForLinkName(), data.getLinkName() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_LABEL_TRUE_DESC_SUFFIX, element.getCmKeyForLabelTrue(), data.getLabelTrue() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_LABEL_FALSE_DESC_SUFFIX, element.getCmKeyForLabelFalse(), data.getLabelFalse() );
    addCMMetaData( cmDataElements, data.getElementLabel() + ClaimFormStepElement.CM_CFSE_BUTTON_LABEL_DESC_SUFFIX, element.getCmKeyForButton(), data.getButtonLabel() );
    cmAssetService.createOrUpdateAsset( ClaimForm.CM_CLAIM_FORM_SECTION, getClaimFormAssetName( form.getCmAssetCode() ), form.getName(), form.getCmAssetCode(), cmDataElements );

    // End CM Stuff
  }

  public ClaimFormStepElement getClaimFormStepElementById( Long elementId )
  {
    ClaimFormStepElement element = claimFormDAO.getClaimFormStepElementById( elementId );
    // hydrate the object
    element.getClaimFormStep().getClaimForm();
    return element;
  }

  /**
   * Deletes the list of claimFormStepElements
   * 
   * @param claimFormId
   * @param claimFormStepId
   * @param claimFormStepElementIds
   */
  public void deleteClaimFormStepElements( Long claimFormId, Long claimFormStepId, List claimFormStepElementIds )
  {
    ClaimForm form = getClaimFormById( claimFormId );
    ClaimFormStep step = (ClaimFormStep)BaseDomain.getBusinessObjectWithId( form.getClaimFormSteps(), claimFormStepId );
    List elements = step.getClaimFormStepElements();
    for ( int i = 0; i < claimFormStepElementIds.size(); i++ )
    {
      Long elementId = (Long)claimFormStepElementIds.get( i );
      ClaimFormStepElement element = (ClaimFormStepElement)BaseDomain.getBusinessObjectWithId( elements, elementId );
      elements.remove( element );
      // TODO once CMAsset has delete, put that in here.
    }

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#copyClaimForm(java.lang.Long,
   *      java.lang.String)
   * @param claimFormIdToCopy
   * @param newFormName
   * @return ClaimForm (The copied ClaimForm)
   * @throws ServiceErrorException
   */
  public ClaimForm copyClaimForm( Long claimFormIdToCopy, String newFormName ) throws ServiceErrorException
  {
    ClaimForm copiedClaimForm;
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimFormAssociationRequest( ClaimFormAssociationRequest.ALL ) );

    ClaimForm savedClaimForm = claimFormDAO.getClaimFormByIdWithAssociations( claimFormIdToCopy, associationRequestCollection );

    if ( savedClaimForm != null )
    {
      Hibernate.initialize( savedClaimForm.getClaimFormSteps() );
    }
    if ( savedClaimForm == null )
    {
      return null;
    }

    ClaimForm claimFormCopy;
    try
    {
      claimFormCopy = new ClaimForm( savedClaimForm );
    }
    catch( CloneNotSupportedException e )
    {
      throw new ServiceErrorException( "Claim (id: " + savedClaimForm.getId() + ") failed cloning", e );
    }

    claimFormCopy.setName( newFormName );
    claimFormCopy.setCmAssetCode( cmAssetService.getUniqueAssetCode( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX ) );

    try
    {
      copiedClaimForm = saveClaimForm( claimFormCopy, false );
    }
    catch( ConstraintViolationException cve )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_DUPLICATE_ERR, cve );
    }

    // saveClaimForm method creates the CMType for us...now we have to copy all the asset data...
    Map supercedingData = new HashMap();
    supercedingData.put( "NAME", newFormName );
    cmAssetService.copyCMAsset( savedClaimForm.getCmAssetCode(), copiedClaimForm.getCmAssetCode(), newFormName, supercedingData, true, getClaimFormAssetName( copiedClaimForm.getCmAssetCode() ) );

    return copiedClaimForm;
  }

  /**
   * Move the specified claimFormStep to the newIndex and shift all other ClaimFormSteps down.
   * Overridden from
   * 
   * @param claimFormId
   * @param claimFormStepId
   * @param newIndex
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#reorderClaimFormStep(Long, Long,
   *      int)
   */
  public void reorderClaimFormStep( Long claimFormId, Long claimFormStepId, int newIndex )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimFormAssociationRequest( ClaimFormAssociationRequest.STEPS ) );

    ClaimFormStep claimFormStep = null;
    ClaimForm claimForm = claimFormDAO.getClaimFormByIdWithAssociations( claimFormId, associationRequestCollection );
    Iterator claimFormStepsIterator = claimForm.getClaimFormSteps().iterator();
    while ( claimFormStepsIterator.hasNext() )
    {
      ClaimFormStep step = (ClaimFormStep)claimFormStepsIterator.next();
      if ( step.getId().equals( claimFormStepId ) )
      {
        claimFormStep = step;
      }
    }

    // This should not normally happen, unless someone is trying to URL hack
    if ( claimFormStep == null )
    {
      throw new BeaconRuntimeException( "Cannot find claimFormStep by id. " );
    }

    claimForm.getClaimFormSteps().remove( claimFormStep );
    claimForm.getClaimFormSteps().add( newIndex, claimFormStep );
    claimFormDAO.saveClaimForm( claimForm );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#reorderClaimFormStepElement(java.lang.Long,java.lang.Long,
   *      int)
   * @param claimFormStepId
   * @param claimFormStepElementId
   * @param newIndex
   * @return ClaimFormStep
   */
  public ClaimFormStep reorderClaimFormStepElement( Long claimFormStepId, Long claimFormStepElementId, int newIndex )
  {
    ClaimFormStepElement claimFormStepElement = claimFormDAO.getClaimFormStepElementById( claimFormStepElementId );

    ClaimFormStep claimFormStep = claimFormDAO.getClaimFormStepById( claimFormStepId );

    claimFormStep.getClaimFormStepElements().remove( claimFormStepElement );

    if ( newIndex < claimFormStep.getClaimFormStepElements().size() )
    {
      claimFormStep.getClaimFormStepElements().add( newIndex, claimFormStepElement );
    }
    else
    {
      claimFormStep.getClaimFormStepElements().add( claimFormStepElement );
    }

    return claimFormDAO.saveClaimFormStep( claimFormStep );

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getClaimFormStepWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param claimFormStepId
   * @param associationRequestCollection
   * @return ClaimFormStep
   */
  public ClaimFormStep getClaimFormStepWithAssociations( Long claimFormStepId, AssociationRequestCollection associationRequestCollection )
  {
    ClaimFormStep claimFormStep = this.claimFormDAO.getClaimFormStepByIdWithAssociations( claimFormStepId, associationRequestCollection );
    return claimFormStep;
  }

  /**
   * Delete a list of claimFormStepIds for this form.
   * 
   * @param claimFormId
   * @param claimFormStepIds
   */
  public void deleteClaimFormSteps( Long claimFormId, List claimFormStepIds )
  {
    ClaimForm form = getClaimFormById( claimFormId );
    List steps = form.getClaimFormSteps();
    // if all claimform steps are removed from a completed form then it should be moved back to
    // Under Construction
    if ( claimFormStepIds.size() == steps.size() )
    {
      form.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    }
    for ( int i = 0; i < claimFormStepIds.size(); i++ )
    {
      Long claimFormStepId = (Long)claimFormStepIds.get( i );
      ClaimFormStep claimFormStep = (ClaimFormStep)BaseDomain.getBusinessObjectWithId( steps, claimFormStepId );
      steps.remove( claimFormStep );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#saveCustomerInformationBlockElements(java.lang.Long,
   *      java.util.List, java.util.List)
   * @param claimFormStepId
   * @param claimFormStepElementsToSave
   * @param claimFormStepElementsToDelete
   */
  public void saveCustomerInformationBlockElements( Long claimFormStepId, List claimFormStepElementsToSave, List claimFormStepElementsToDelete )
  {
    try
    {
      List cmDataElements = new ArrayList();
      ClaimFormStep claimFormStep = claimFormDAO.getClaimFormStepById( claimFormStepId );

      ClaimForm form = claimFormStep.getClaimForm();

      Iterator iterToSave = claimFormStepElementsToSave.iterator();
      while ( iterToSave.hasNext() )
      {
        ClaimFormStepElement claimFormStepElementToSave = (ClaimFormStepElement)iterToSave.next();

        boolean found = false;
        Iterator savedClaimFormStepElementIterator = claimFormStep.getClaimFormStepElements().iterator();
        while ( savedClaimFormStepElementIterator.hasNext() )
        {
          ClaimFormStepElement savedClaimFormStepElement = (ClaimFormStepElement)savedClaimFormStepElementIterator.next();

          if ( savedClaimFormStepElement.equals( claimFormStepElementToSave ) )
          {
            found = true;
            savedClaimFormStepElement.setRequired( claimFormStepElementToSave.isRequired() );
            break;
          }
        }

        if ( !found )
        {
          claimFormStep.addClaimFormStepElement( claimFormStepElementToSave );

          String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
          claimFormStepElementToSave.setCmKeyFragment( cmKeyFragment );

          cmDataElements
              .add( new CMDataElement( claimFormStepElementToSave.getDescription() + " CIB STEP", claimFormStepElementToSave.getCmKeyForElementLabel(), claimFormStepElementToSave.getDescription() ) );
        }
      } // while

      if ( cmDataElements.size() > 0 )
      {
        cmAssetService.createOrUpdateAsset( ClaimForm.CM_CLAIM_FORM_SECTION, getClaimFormAssetName( form.getCmAssetCode() ), form.getName(), form.getCmAssetCode(), cmDataElements );
      }

      // Loop through and see if any of the records are contained in the delete list
      Iterator savedClaimFormStepElementIterator = claimFormStep.getClaimFormStepElements().iterator();
      while ( savedClaimFormStepElementIterator.hasNext() )
      {
        ClaimFormStepElement savedClaimFormStepElement = (ClaimFormStepElement)savedClaimFormStepElementIterator.next();

        if ( claimFormStepElementsToDelete.contains( savedClaimFormStepElement ) )
        {
          savedClaimFormStepElementIterator.remove();
        }
      }

      claimFormDAO.saveClaimFormStep( claimFormStep );
    }
    catch( ServiceErrorException e )
    {
      throw new BeaconRuntimeException( e );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getAllClaimFormsNotUnderConstruction()
   * @return List
   */
  public List getAllClaimFormsNotUnderConstruction()
  {
    return this.claimFormDAO.getAllClaimFormsNotUnderConstruction();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#getAllClaimFormsNotUnderConstructionByModuleType(String
   *      moduleType)
   * @return List
   */
  public List getAllClaimFormsNotUnderConstructionByModuleType( String moduleType )
  {
    return this.claimFormDAO.getAllClaimFormsNotUnderConstructionByModuleType( moduleType );
  }

  public void updateClaimFormStatus( Long claimFormId )
  {
    ClaimForm claimForm = getClaimFormById( claimFormId );

    if ( claimForm.getPromotions().isEmpty() )
    {
      if ( claimForm.getClaimFormStatusType().isAssigned() )
      {
        claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
      }
    }
    else
    { // per Change Request dated 7/29, set status to completed if linked to only expired promotions
      boolean expiredOnly = true;
      for ( Iterator iter = claimForm.getPromotions().iterator(); iter.hasNext(); )
      {
        Promotion promo = (Promotion)iter.next();
        if ( !promo.isExpired() )
        {
          // found a promotion that is not expired
          expiredOnly = false;
          break;
        }
      }
      if ( expiredOnly )
      {
        claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
      }
      else
      {
        claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );
      }
    }

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimFormDefinitionService#preparePreviewClaimForm(java.lang.Long)
   *      Verifies the ClaimFormStep is at a state that can be previewed or it will throw an
   *      exception.
   * @param claimFormStepId
   * @throws ServiceErrorException
   */
  public void preparePreviewClaimForm( Long claimFormStepId ) throws ServiceErrorException
  {

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimFormStepAssociationRequest() );

    ClaimFormStep claimFormStep = this.claimFormDAO.getClaimFormStepByIdWithAssociations( claimFormStepId, associationRequestCollection );
    if ( claimFormStep == null )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_PREVIEW_FORM_NO_STEPS_ERR );
    }

    // verify there is at least 1 element for the step
    List claimFormStepElements = claimFormStep.getClaimFormStepElements();
    if ( claimFormStepElements == null || claimFormStepElements.size() < 1 )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.CLAIM_FORM_PREVIEW_FORM_NO_ELEMENTS_ERR );
    }

    // Set the claimFormStepId
    claimFormStepId = claimFormStep.getId();
  }

  /**
   * Get a unique asset type name based on the unique asset code. We have to use the same asset type
   * name each time we update the asset for a claim form.
   * 
   * @param assetCode
   * @return String
   */
  private String getClaimFormAssetName( String assetCode )
  {
    // string passed in is "claim_form_data.claim_form.xyz"
    // return string is "_ClaimFormData_xyz"
    if ( StringUtils.isEmpty( assetCode ) || !assetCode.startsWith( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX ) )
    {
      throw new BeaconRuntimeException( "Claim Form asset code does not start with " + ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX );
    }
    return ASSET_TYPE_NAME_PREFIX_CLAIM_FORM + assetCode.substring( ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX.length() );
  }

} // end class ClaimFormServiceImpl
