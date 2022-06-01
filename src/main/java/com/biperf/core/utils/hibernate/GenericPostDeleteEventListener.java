/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/hibernate/GenericPostDeleteEventListener.java,v $
 */

package com.biperf.core.utils.hibernate;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.SystemVariableType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.system.OsPropertySetHistory;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserNodeHistory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * GenericPostDeleteEventListener.
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
 * <td>zahler</td>
 * <td>Dec 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GenericPostDeleteEventListener extends AbstractEventListener implements PostDeleteEventListener
{

  private static final Log logger = LogFactory.getLog( GenericPostDeleteEventListener.class );

  /**
   * Overridden from
   * 
   * @see org.hibernate.event.PostDeleteEventListener#onPostDelete(org.hibernate.event.PostDeleteEvent)
   * @param event
   */
  public void onPostDelete( PostDeleteEvent event )
  {
    processPromotionChangeEvent( event, event.getEntity() );// check for promotion updates for
                                                            // caching purposes

    if ( event.getEntity() instanceof UserNode )
    {
      onPostDeleteUserNode( event );
    }
    else if ( event.getEntity() instanceof PropertySetItem )
    {
      onPostDeletePropertySetItem( event );
    }
  }

  /**
   * Handle the post delete of a UserNode
   * 
   * @param event
   */
  private void onPostDeleteUserNode( PostDeleteEvent event )
  {
    if ( event.getDeletedState() != null )
    {
      UserNodeHistory userNodeHistory = new UserNodeHistory();
      userNodeHistory.setUser( (User)PostSaveEventUtils.getProperty( "user", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      userNodeHistory.setNode( (Node)PostSaveEventUtils.getProperty( "node", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      userNodeHistory.setActive( new Boolean( false ) ); // history records are always inactive per
      // bug #10776
      userNodeHistory.setHierarchyRoleType( (HierarchyRoleType)PostSaveEventUtils.getProperty( "hierarchyRoleType", event.getPersister().getPropertyNames(), event.getDeletedState() ) );

      // get the session and save the history record.
      Session session = HibernateSessionManager.getSession();
      session.saveOrUpdate( userNodeHistory );
    }
  }

  private void onPostDeletePropertySetItem( PostDeleteEvent event )
  {
    PropertySetItem propertySetItem = (PropertySetItem)event.getEntity();

    if ( event.getDeletedState() != null )
    {
      OsPropertySetHistory osPropertySetTracking = new OsPropertySetHistory();

      SystemVariableType SystemVariableType = (SystemVariableType)PostSaveEventUtils.getProperty( "type", event.getPersister().getPropertyNames(), event.getDeletedState() );
      AuditCreateInfo auditCreateInfo = (AuditCreateInfo)PostSaveEventUtils.getProperty( "auditCreateInfo", event.getPersister().getPropertyNames(), event.getDeletedState() );
      osPropertySetTracking.setType( SystemVariableType.lookup( SystemVariableType.getCode() ) );
      osPropertySetTracking.setAuditCreateInfo( auditCreateInfo );

      osPropertySetTracking.setEntityId( propertySetItem.getEntityId() ); // Non Editable field
      osPropertySetTracking.setEntityName( propertySetItem.getEntityName() ); 
      osPropertySetTracking.setKey( (String)PostSaveEventUtils.getProperty( "key", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      osPropertySetTracking.setBooleanVal( (boolean)PostSaveEventUtils.getProperty( "booleanVal", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      osPropertySetTracking.setDoubleVal( (double)PostSaveEventUtils.getProperty( "doubleVal", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      osPropertySetTracking.setDateVal( (Date)PostSaveEventUtils.getProperty( "dateVal", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      osPropertySetTracking.setStringVal( (String)PostSaveEventUtils.getProperty( "stringVal", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      osPropertySetTracking.setLongVal( (Long)PostSaveEventUtils.getProperty( "longVal", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      osPropertySetTracking.setIntVal( (Integer)PostSaveEventUtils.getProperty( "intVal", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      osPropertySetTracking.setEditable( (boolean)PostSaveEventUtils.getProperty( "editable", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      osPropertySetTracking.setGroupName( (String)PostSaveEventUtils.getProperty( "groupName", event.getPersister().getPropertyNames(), event.getDeletedState() ) );
      osPropertySetTracking.setViewable( (boolean)PostSaveEventUtils.getProperty( "viewable", event.getPersister().getPropertyNames(), event.getDeletedState() ) );

      Session session = HibernateSessionManager.getSession();
      session.saveOrUpdate( osPropertySetTracking );
    }

  }

}
