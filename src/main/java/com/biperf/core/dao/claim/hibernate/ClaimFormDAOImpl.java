/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/ClaimFormDAOImpl.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * ClaimFormDAOImpl.
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
public class ClaimFormDAOImpl extends BaseDAO implements ClaimFormDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#deleteClaimForm(com.biperf.core.domain.claim.ClaimForm)
   * @param claimForm
   */
  public void deleteClaimForm( ClaimForm claimForm )
  {
    getSession().delete( claimForm );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.claim.AllClaimFormList" ).list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getClaimFormById(java.lang.Long)
   * @param id
   * @return ClaimForm
   */
  public ClaimForm getClaimFormById( Long id )
  {
    return (ClaimForm)getSession().get( ClaimForm.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getClaimFormByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return ClaimForm
   */
  public ClaimForm getClaimFormByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    ClaimForm claimForm = (ClaimForm)getSession().get( ClaimForm.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( claimForm );
    }

    return claimForm;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getClaimFormByName(java.lang.String)
   * @param formName
   * @return ClaimForm
   */
  public ClaimForm getClaimFormByName( String formName )
  {
    // TODO - Currently we will do a toUpperCase on this query.
    // and the Hbm file also does an upper on the column.
    // This was 'ok'ed' because there should not be a large number of rows in this DB table.
    // In the future we may want to do it differently.
    return (ClaimForm)getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimFormByName" ).setString( "formName", formName.toUpperCase() ).uniqueResult();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#saveClaimForm(com.biperf.core.domain.claim.ClaimForm)
   * @param claimForm
   * @return claimForm
   * @throws ConstraintViolationException
   */
  public ClaimForm saveClaimForm( ClaimForm claimForm ) throws ConstraintViolationException
  {
    claimForm = (ClaimForm)HibernateUtil.saveOrUpdateOrShallowMerge( claimForm );
    // Note: need to do the flush to get the Constraint Violation to bubble up.
    getSession().flush();
    return claimForm;
  } // end saveClaimForm

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#updateClaimForm(com.biperf.core.domain.claim.ClaimForm)
   * @param claimForm
   * @return ClaimForm
   * @throws ConstraintViolationException
   */
  public ClaimForm updateClaimForm( ClaimForm claimForm ) throws ConstraintViolationException
  {
    claimForm = (ClaimForm)HibernateUtil.saveOrUpdateOrShallowMerge( claimForm );
    // Note: need to do the flush to get the Constraint Violation to bubble up.
    getSession().flush();
    return claimForm;
  }

  /**
   * Get the ClaimFormStep by the id. Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getClaimFormStepById(java.lang.Long)
   * @param claimFormStepId
   * @return ClaimFormStep
   */
  public ClaimFormStep getClaimFormStepById( Long claimFormStepId )
  {
    return (ClaimFormStep)getSession().get( ClaimFormStep.class, claimFormStepId );
  }

  /**
   * Get the list of all ClaimFormSteps associated to the claimFormId.
   */
  @Override
  public List<ClaimFormStepElement> getAllClaimFormStepElementsByClaimFormId( Long claimFormId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimFormStepElementsByClaimFormId" );
    query.setLong( "claimFormId", claimFormId.longValue() );

    return query.list();
  }

  /**
   * Get the list of numeric ClaimFormSteps associated to the claimFormId.
   * 
   * @param claimFormId
   * @param type
   * @return List
   */
  public List getAllClaimFormStepElementsByClaimFormIdElementType( Long claimFormId, ClaimFormElementType type )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimFormStepElementsByClaimFormIdElementType" );
    query.setLong( "claimFormId", claimFormId.longValue() );
    query.setString( "elementTypeCode", type.getCode() );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getClaimFormStepByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return ClaimFormStep
   */
  public ClaimFormStep getClaimFormStepByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    ClaimFormStep claimFormStep = (ClaimFormStep)getSession().get( ClaimFormStep.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( claimFormStep );
    }

    return claimFormStep;
  }

  /**
   * Gets a claim form step element by id. Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getClaimFormStepElementById(java.lang.Long)
   * @param id
   * @return ClaimFormStepElement
   */
  public ClaimFormStepElement getClaimFormStepElementById( Long id )
  {
    return (ClaimFormStepElement)getSession().get( ClaimFormStepElement.class, id );
  }

  /**
   * Save ClaimFormStep Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#saveClaimFormStep(com.biperf.core.domain.claim.ClaimFormStep)
   * @param claimFormStep
   * @return claimFormStep
   */
  public ClaimFormStep saveClaimFormStep( ClaimFormStep claimFormStep )
  {
    getSession().saveOrUpdate( claimFormStep );
    return claimFormStep;
  }

  /**
   * Does a physical delete on a claim form step element
   * 
   * @param id
   */
  public void deleteClaimFormStepElement( Long id )
  {
    ClaimFormStepElement element = getClaimFormStepElementById( id );
    // Need to remove it from ClaimFormStepElements instead of deleting
    // it from session that way this association will be terminated too.
    // Otherwise Cascade will try to do saveOrUpdate on the deleted object
    element.getClaimFormStep().getClaimFormStepElements().remove( element );
    // getSession().delete( element );
  }

  /**
   * Does a session.merge to update an element which is already persistent
   * 
   * @param element
   * @return ClaimFormStepElement
   */
  public ClaimFormStepElement updateClaimFormStepElement( ClaimFormStepElement element )
  {
    return (ClaimFormStepElement)HibernateUtil.saveOrUpdateOrShallowMerge( element );
  }

  /**
   * Get the list of claimFormStepEmailNotifications associated to the claimFormStepId param.
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getClaimFormStepElementsByClaimFormStepId(java.lang.Long)
   * @param claimFormStepId
   * @return List
   */
  public List getClaimFormStepEmailNotificationsByClaimFormStepById( Long claimFormStepId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimFormStepEmailNotificationsByClaimFormStepId" );

    query.setParameter( "claimFormStepId", claimFormStepId );

    return query.list();
  }

  /**
   * Get a single claimFormStepEmailNotification by the ID param. Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getClaimFormStepEmailNotificationById(java.lang.Long)
   * @param id
   * @return ClaimFormStepEmailNotification
   */
  public ClaimFormStepEmailNotification getClaimFormStepEmailNotificationById( Long id )
  {
    return (ClaimFormStepEmailNotification)getSession().get( ClaimFormStepEmailNotification.class, id );
  }

  /**
   * Save a claimFormStep for the claimForm associated to the claimFormId param. Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#saveClaimFormStep(java.lang.Long,
   *      com.biperf.core.domain.claim.ClaimFormStep)
   * @param claimFormId
   * @param managedClaimFormStep
   * @return ClaimFormStep
   */
  public ClaimFormStep saveClaimFormStep( Long claimFormId, ClaimFormStep managedClaimFormStep )
  {

    ClaimForm claimForm = this.getClaimFormById( claimFormId );

    // Check to see if this is a new or updated step
    if ( null != managedClaimFormStep.getId() && managedClaimFormStep.getId().longValue() != 0 )
    {

      managedClaimFormStep.setClaimForm( claimForm );

      // Get the elements for the updated claimFormStep - this assumes we are only modifying the
      // step, and not the elements
      // todo we should make this an update association...

      for ( int i = 0; i < claimForm.getClaimFormSteps().size(); i++ )
      {
        ClaimFormStep claimFormStep = (ClaimFormStep)claimForm.getClaimFormSteps().get( i );
        if ( claimFormStep.getId().equals( managedClaimFormStep.getId() ) )
        {
          // deleteClaimFormStepEmailNotification();
          claimFormStep.getClaimFormStepEmailNotifications().clear();
          getSession().flush();
          claimFormStep.setSalesRequired( managedClaimFormStep.isSalesRequired() );
          Iterator claimFormStepEmailIterator = managedClaimFormStep.getClaimFormStepEmailNotifications().iterator();
          while ( claimFormStepEmailIterator.hasNext() )
          {
            ClaimFormStepEmailNotification claimFormStepEmailNotification = (ClaimFormStepEmailNotification)claimFormStepEmailIterator.next();
            ClaimFormStepEmailNotification notificationToAdd = new ClaimFormStepEmailNotification();
            notificationToAdd.setClaimFormStepEmailNotificationType( claimFormStepEmailNotification.getClaimFormStepEmailNotificationType() );
            claimFormStep.addClaimFormStepEmailNotification( notificationToAdd );
          }
        }
      }
    }
    else
    {

      // This is a new step
      claimForm.addClaimFormStep( managedClaimFormStep );

      // Saving the claimFormStep could result in a unique constraint violation (claimFormStep.name)
      this.saveClaimFormStep( managedClaimFormStep );

    }

    return managedClaimFormStep;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getAllClaimFormsNotUnderConstruction()
   * @return List of claim form Steps
   */
  public List getAllClaimFormsNotUnderConstruction()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimFormsNotUnderConstruction" );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.ClaimFormDAO#getAllClaimFormsNotUnderConstructionByModuleType(
   *      String moduleType )
   * @param moduleType
   * @return List of claim form Steps
   */
  public List getAllClaimFormsNotUnderConstructionByModuleType( String moduleType )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.ClaimFormsNotUnderConstructionByModuleType" ).setString( "moduleType", moduleType );

    return query.list();
  }
} // end ClaimFormDAOImpl
