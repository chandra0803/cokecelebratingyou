/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/hibernate/GenericPostUpdateEventListener.java,v $
 */

package com.biperf.core.utils.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetHistory;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.SystemVariableType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.system.OsPropertySetHistory;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserNodeHistory;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.HibernateSessionManager;
import com.biw.hc.core.service.HCServices;

/**
 * GenericPostUpdateEventListener.
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
 * <td>sedey</td>
 * <td>May 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>    
 * </table>
 * 
 * 
 */
public class GenericPostUpdateEventListener extends AbstractEventListener implements PostUpdateEventListener, Serializable
{

  private static final Log logger = LogFactory.getLog( GenericPostUpdateEventListener.class );

  /**
   * Overridden from     
   * 
   * @see org.hibernate.event.PostUpdateEventListener#onPostUpdate(org.hibernate.event.PostUpdateEvent)
   * @param event
   */
  public void onPostUpdate( PostUpdateEvent event )
  {

    processPromotionChangeEvent( event, event.getEntity() );// check for
                                                            // promotion
                                                            // updates for
                                                            // caching
                                                            // purposes

    // If the entity from this event is an instance of Budget then process a
    // BudgetHistory record.
    if ( event.getEntity() instanceof Budget )
    {
      onPostUpdateBudget( event );
    }
    else if ( event.getEntity() instanceof UserNode )
    {
      onPostUpdateUserNode( event );
    }
    else if ( event.getEntity() instanceof User )
    {
      onPostUpdateUser( event );
    }
    else if ( event.getEntity() instanceof PropertySetItem )
    {
      onPostUpdatePropertySetItem( event );
    }

  }

  /**
   * Handle the post update of a UserNode
   * 
   * @param event
   */
  private void onPostUpdateUserNode( PostUpdateEvent event )
  {
    // Per business rule, only create a history record if the node values
    // change.
    if ( event.getOldState() != null
    /*
     * && ( isPropertyModified( "user", event.getPersister().getPropertyNames(), event.getState(),
     * event.getOldState() ) || isPropertyModified( "node", event.getPersister().getPropertyNames(),
     * event.getState(), event.getOldState() ) )
     */
    )
    {
      UserNodeHistory userNodeHistory = new UserNodeHistory();
      User user = (User)PostSaveEventUtils.getProperty( "user", event.getPersister().getPropertyNames(), event.getOldState() );
      userNodeHistory.setUser( user );
      userNodeHistory.setNode( (Node)PostSaveEventUtils.getProperty( "node", event.getPersister().getPropertyNames(), event.getOldState() ) );
      userNodeHistory.setActive( new Boolean( false ) ); // history records
                                                         // are always
                                                         // inactive per
      // bug #10776
      userNodeHistory.setHierarchyRoleType( (HierarchyRoleType)PostSaveEventUtils.getProperty( "hierarchyRoleType", event.getPersister().getPropertyNames(), event.getOldState() ) );

      Session session = HibernateSessionManager.getSession();
      session.saveOrUpdate( userNodeHistory );

      if ( user.isParticipant() )
      {
        indexParticipant( user.getId() );
      }
    }
  }

  private void onPostUpdateUser( PostUpdateEvent event )
  {
    User user = (User)event.getEntity();
    Long userId = user.getId();

    if ( user.isParticipant() )
    {
      // Elastic search index

      if ( PostSaveEventUtils
          .isModified( event,
                       "firstName",
                       "lastName",
                       "avatarOriginal",
                       "active",
                       "optOutAwards",
                       "userName",
                       "userType",
                       "titleType",
                       "suffixType",
                       "languageType",
                       "genderType",
                       "status",
                       "terminationDate",
                       "positionType",
                       "departmentType",
                       "termsAcceptedDate",
                       "optOutOfAwardsDate" ) )
      {
        indexParticipant( userId );
      }

      // Honeycomb account sync
      if ( isHCServicesDefined() )
      {
        if ( PostSaveEventUtils.isModified( event, AccountSyncUpdateFields.SYNC_ON_UPDATE_FIELDS ) )
        {
          getHCServices().syncParticipantDetails( Arrays.asList( userId ) );
        }
      }
    }

  }

  /**
   * Handle the post update of a system variable
   * 
   * @param event
   */
  private void onPostUpdatePropertySetItem( PostUpdateEvent event )
  {
    PropertySetItem propertySetItem = (PropertySetItem)event.getEntity();
    if ( event.getOldState() != null )
    {
      OsPropertySetHistory osPropertySetTracking = new OsPropertySetHistory();

      SystemVariableType SystemVariableType = (SystemVariableType)PostSaveEventUtils.getProperty( "type", event.getPersister().getPropertyNames(), event.getOldState() );
      AuditCreateInfo auditCreateInfo = (AuditCreateInfo)PostSaveEventUtils.getProperty( "auditCreateInfo", event.getPersister().getPropertyNames(), event.getOldState() );
      osPropertySetTracking.setType( SystemVariableType.lookup( SystemVariableType.getCode() ) );
      osPropertySetTracking.setAuditCreateInfo( auditCreateInfo );

      osPropertySetTracking.setEntityId( propertySetItem.getEntityId() ); // Non Editable field
      osPropertySetTracking.setEntityName( propertySetItem.getEntityName() ); // Entity name -
                                                                              // Primary key in
                                                                              // os_propertyset
                                                                              // table and non
                                                                              // editable field

      osPropertySetTracking.setDateVal( (Date)PostSaveEventUtils.getProperty( "dateVal", event.getPersister().getPropertyNames(), event.getOldState() ) );
      osPropertySetTracking.setStringVal( (String)PostSaveEventUtils.getProperty( "stringVal", event.getPersister().getPropertyNames(), event.getOldState() ) );

      osPropertySetTracking.setKey( (String)PostSaveEventUtils.getProperty( "key", event.getPersister().getPropertyNames(), event.getOldState() ) );
      if ( Objects.isNull( PostSaveEventUtils.getProperty( "booleanVal", event.getPersister().getPropertyNames(), event.getOldState() ) ) )
      {
        osPropertySetTracking.setBooleanVal( null );
      }
      else
      {
        osPropertySetTracking.setBooleanVal( (boolean)PostSaveEventUtils.getProperty( "booleanVal", event.getPersister().getPropertyNames(), event.getOldState() ) );
      }

      if ( Objects.isNull( PostSaveEventUtils.getProperty( "doubleVal", event.getPersister().getPropertyNames(), event.getOldState() ) ) )
      {
        osPropertySetTracking.setDoubleVal( null );
      }
      else
      {
        osPropertySetTracking.setDoubleVal( (double)PostSaveEventUtils.getProperty( "doubleVal", event.getPersister().getPropertyNames(), event.getOldState() ) );
      }
      if ( Objects.isNull( PostSaveEventUtils.getProperty( "longVal", event.getPersister().getPropertyNames(), event.getOldState() ) ) )
      {
        osPropertySetTracking.setLongVal( null );
      }
      else
      {
        osPropertySetTracking.setLongVal( (Long)PostSaveEventUtils.getProperty( "longVal", event.getPersister().getPropertyNames(), event.getOldState() ) );
      }
      if ( Objects.isNull( PostSaveEventUtils.getProperty( "intVal", event.getPersister().getPropertyNames(), event.getOldState() ) ) )
      {
        osPropertySetTracking.setIntVal( null );
      }
      else
      {
        osPropertySetTracking.setIntVal( (Integer)PostSaveEventUtils.getProperty( "intVal", event.getPersister().getPropertyNames(), event.getOldState() ) );
      }
      osPropertySetTracking.setEditable( (boolean)PostSaveEventUtils.getProperty( "editable", event.getPersister().getPropertyNames(), event.getOldState() ) );
      osPropertySetTracking.setGroupName( (String)PostSaveEventUtils.getProperty( "groupName", event.getPersister().getPropertyNames(), event.getOldState() ) );

      osPropertySetTracking.setViewable( (boolean)PostSaveEventUtils.getProperty( "viewable", event.getPersister().getPropertyNames(), event.getOldState() ) );

      Session session = HibernateSessionManager.getSession();
      session.saveOrUpdate( osPropertySetTracking );
    }

  }

  /**
   * Handle the post update of a budget
   * 
   * @param event
   */
  private void onPostUpdateBudget( PostUpdateEvent event )
  {
    Budget budget = (Budget)event.getEntity();
    try
    {
      // Per business rule, only create a history record if the origional
      // or current values change.
      if ( event.getOldState() != null && ( PostSaveEventUtils.isPropertyModified( "originalValue", event.getPersister().getPropertyNames(), event.getState(), event.getOldState() )
          || PostSaveEventUtils.isPropertyModified( "currentValue", event.getPersister().getPropertyNames(), event.getState(), event.getOldState() ) ) )
      {
        BudgetHistory history = new BudgetHistory();
        history.setBudgetId( budget.getId() );
        history.setOriginalValueBeforeTransaction( (BigDecimal)PostSaveEventUtils.getProperty( "originalValue", event.getPersister().getPropertyNames(), event.getOldState() ) );
        history.setOriginalValueAfterTransaction( budget.getOriginalValue() );
        history.setCurrentValueBeforeTransaction( (BigDecimal)PostSaveEventUtils.getProperty( "currentValue", event.getPersister().getPropertyNames(), event.getOldState() ) );
        history.setCurrentValueAfterTransaction( budget.getCurrentValue() );
        history.setActionType( budget.getActionType() );
        history.setClaimId( budget.getReferenceVariableForClaimId() );
        history.setFromBudgetId( budget.getFromBudget() );

        // get the session and save the history record.
        Session session = HibernateSessionManager.getSession();
        session.saveOrUpdate( history );
      }
    }
    catch( ConstraintViolationException e )
    {
      logger.debug( "Constraint Violation: " + e.getConstraintName() );
      // Do nothing..We are trying to insert duplicate row here.
    }
  }

  private void indexParticipant( Long userId )
  {
    if ( isAutoCompleteServiceDefined() )
    {
      getAutoCompleteService().indexParticipants( Arrays.asList( userId ) );
    }
  }

  private static boolean isAutoCompleteServiceDefined()
  {
    return BeanLocator.hasBean( AutoCompleteService.BEAN_NAME );
  }

  private static AutoCompleteService getAutoCompleteService()
  {
    return (AutoCompleteService)BeanLocator.getBean( AutoCompleteService.BEAN_NAME );
  }

  private static boolean isHCServicesDefined()
  {
    return BeanLocator.hasBean( HCServices.BEAN_NAME );
  }

  private static HCServices getHCServices()
  {
    return (HCServices)BeanLocator.getBean( HCServices.BEAN_NAME );
  }
}
