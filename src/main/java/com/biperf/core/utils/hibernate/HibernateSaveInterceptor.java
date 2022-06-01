/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils.hibernate;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.ui.utils.RosterUtil;

/**
 * TODO Javadoc for GenericSaveEventListener.
 * 
 * @author esakkimu
 * @since Dec 4, 2019
 */
public class HibernateSaveInterceptor extends EmptyInterceptor
{
  private static final Log logger = LogFactory.getLog( HibernateSaveInterceptor.class );

  // To do - Code Refractoring is required for hard coded values for propertyNames
  @Override
  public boolean onSave( Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types )
  {
    if ( entity instanceof User )
    {
      addRosterUserIdForUser( entity, state, propertyNames );
    }

    if ( entity instanceof Hierarchy )
    {
      addRosterHierarchyIdForHierarchy( entity, state, propertyNames );
    }

    if ( entity instanceof Node )
    {
      addRosterNodeIdForNode( entity, state, propertyNames );
    }

    if ( entity instanceof Audience )
    {
      addRosterAudienceIdForAudience( entity, state, propertyNames );
    }

    if ( entity instanceof UserPhone )
    {
      addRosterPhoneIdForUserPhone( entity, state, propertyNames );
    }

    if ( entity instanceof UserEmailAddress )
    {
      addRosterEmailIdForUserEmailAddress( entity, state, propertyNames );
    }

    if ( entity instanceof UserAddress )
    {
      addRosterAddressIdForUserAddress( entity, state, propertyNames );
    }

    if ( entity instanceof UserDevice )
    {
      addRosterDeviceIdForUserDevice( entity, state, propertyNames );
    }

    if ( entity instanceof Characteristic )
    {
      addRosterCharacteristicIdForCharacteristic( entity, state, propertyNames );
    }

    if ( entity instanceof UserCharacteristic )
    {
      addRosterUserCharIdForUser( entity, state, propertyNames );
    }

    if ( entity instanceof NodeCharacteristic )
    {
      addRosterNodeCharIdForNode( entity, state, propertyNames );
    }
    return true;
  }

  private void addRosterUserIdForUser( Object entity, Object[] state, String[] propertyNames )
  {
    User user = null;

    if ( entity instanceof User )
    {
      user = (User)entity;
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterUserId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }

    if ( Objects.nonNull( user.getUserPhones() ) )
    {
      addRosterPhoneIdForUserPhone( entity, state, propertyNames );
    }

    if ( Objects.nonNull( user.getUserEmailAddresses() ) )
    {
      addRosterEmailIdForUserEmailAddress( entity, state, propertyNames );
    }

    if ( Objects.nonNull( user.getUserAddresses() ) )
    {
      addRosterAddressIdForUserAddress( entity, state, propertyNames );
    }

    if ( Objects.nonNull( user.getUserCharacteristics() ) )
    {
      addRosterUserCharIdForUser( entity, state, propertyNames );
    }

  }

  private void addRosterHierarchyIdForHierarchy( Object entity, Object[] state, String[] propertyNames )
  {
    if ( entity instanceof Hierarchy )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterHierarchyId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }

  }

  private void addRosterNodeIdForNode( Object entity, Object[] state, String[] propertyNames )
  {
    Node node = null;
    if ( entity instanceof Node )
    {
      node = (Node)entity;
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterNodeId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }
    if ( Objects.nonNull( node.getNodeCharacteristics() ) )
    {
      addRosterNodeCharIdForNode( entity, state, propertyNames );
    }
  }

  private void addRosterAudienceIdForAudience( Object entity, Object[] state, String[] propertyNames )
  {
    if ( entity instanceof Audience )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterAudienceId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }
  }

  private void addRosterPhoneIdForUserPhone( Object entity, Object[] state, String[] propertyNames )
  {
    if ( entity instanceof UserPhone )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterPhoneId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }
  }

  private void addRosterEmailIdForUserEmailAddress( Object entity, Object[] state, String[] propertyNames )
  {
    if ( entity instanceof UserEmailAddress )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterEmailId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }
  }

  private void addRosterAddressIdForUserAddress( Object entity, Object[] state, String[] propertyNames )
  {
    if ( entity instanceof UserAddress )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterAddressId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }
  }

  private void addRosterDeviceIdForUserDevice( Object entity, Object[] state, String[] propertyNames )
  {
    if ( entity instanceof UserDevice )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterDeviceId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }
  }

  private void addRosterCharacteristicIdForCharacteristic( Object entity, Object[] state, String[] propertyNames )
  {
    if ( entity instanceof Characteristic )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterCharacteristicId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }
  }

  private void addRosterUserCharIdForUser( Object entity, Object[] state, String[] propertyNames )
  {
    if ( entity instanceof UserCharacteristic )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterUserCharId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }
  }

  private void addRosterNodeCharIdForNode( Object entity, Object[] state, String[] propertyNames )
  {
    if ( entity instanceof NodeCharacteristic )
    {
      for ( int i = 0; i < propertyNames.length; i++ )
      {
        if ( "rosterNodeCharId".equals( propertyNames[i] ) && Objects.isNull( state[i] ) )
        {
          state[i] = RosterUtil.getRandomUUId();
        }
      }
    }
  }
}
