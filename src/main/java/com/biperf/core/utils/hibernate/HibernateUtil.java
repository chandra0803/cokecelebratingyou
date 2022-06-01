/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils.hibernate;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.EntityKey;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.AssociationType;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;

import com.biperf.core.dao.QueryConstraint;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditUpdateInfo;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * HibernateUtil - Utility Class for performing common Hibernate operations.
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
 * <td>tom</td>
 * <td>Jun 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HibernateUtil
{

  /**
   * Standard Util Constructor, protected since only contains static methods.
   */
  protected HibernateUtil()
  {
    super();
  }

  /**
   * Perform a save or update or Shallow merge, depending on the current data state. A shallow merge
   * will not try to persist any children objects. We do not call the Hibernate merge - we call our
   * own merge which just copies values over. <br/> Notes: <br/>
   * <ul>
   * <li>Callers should use the returned object if they want to continue working on the object.</li>
   * <li>Detached objects should not have detached associated objects( collections, other domain
   * objects). Detached associated object will not be saved. </li>
   * </ul>
   * 
   * @param domainObject
   * @return BaseDomain - the persisted version of the domainObject param.
   */
  public static BaseDomain saveOrUpdateOrShallowMerge( BaseDomain domainObject )
  {
    return saveOrUpdateOrMerge( domainObject, false );
  }

  /**
   * Perform a save or update or Deep merge, depending on the current data state. A deep merge will
   * call a Hibernate merge, which will update collections according to cascading constraints <br/>
   * Notes: <br/>
   * <ul>
   * <li>Callers should use the returned object if they want to continue working on the object.</li>
   * <li>Detached objects should not have detached associated objects( collections, other domain
   * objects). Detached associated object will not be saved. </li>
   * </ul>
   * 
   * @param domainObject
   * @return BaseDomain - the persisted version of the domainObject param.
   */
  public static BaseDomain saveOrUpdateOrDeepMerge( BaseDomain domainObject )
  {
    return saveOrUpdateOrMerge( domainObject, true );
  }

  /**
   * Perform a save or update or merge, depending on the current data state. <br/> Notes: <br/>
   * <ul>
   * <li>Callers should use the returned object if they want to continue working on the object.</li>
   * <li>Detached objects should not have detached associated objects( collections, other domain
   * objects). Detached associated object will not be saved. </li>
   * </ul>
   * 
   * @param domainObject
   * @param isDeepMerge
   * @return BaseDomain - the persisted version of the domainObject param.
   */
  protected static BaseDomain saveOrUpdateOrMerge( BaseDomain domainObject, boolean isDeepMerge )
  {
    BaseDomain persistedObject;
    Session session = HibernateSessionManager.getSession();

    if ( domainObject.getId() == null )
    {
      // this is a new object...just save it
      session.save( domainObject );
      persistedObject = domainObject;
    }
    else
    {
      boolean sessionContainsThisObjectButHasADifferentReference = checkIfSessionContainsADifferentReference( session, domainObject );

      if ( sessionContainsThisObjectButHasADifferentReference )
      {

        if ( isDeepMerge )
        {
          persistedObject = (BaseDomain)session.merge( domainObject );
        }
        else
        {
          persistedObject = shallowMerge( domainObject, session );
          session.saveOrUpdate( persistedObject );
        }

      }
      else
      {
        session.saveOrUpdate( domainObject );
        persistedObject = domainObject;
      }
    }
    return persistedObject;
  }

  /**
   * Tests if the reference we have, is the EXACT same reference as what is in the Hibernate
   * session. (if it's not, we have to copy value over, otherwise we don't)
   * 
   * @param session
   * @param domainObject
   * @return boolean
   */
  private static boolean checkIfSessionContainsADifferentReference( Session session, BaseDomain domainObject )
  {
    final PersistenceContext persistenceContext = HibernateSessionManager.getPersistenceContext();

    EntityKey key = new EntityKey( domainObject.getId(), HibernateSessionManager.getEntityPersister( domainObject ), session.getEntityMode() );

    boolean hasObjectWithThisKeyInSession = persistenceContext.containsEntity( key );

    boolean sessionContainsThisObjectButHasADifferentReference = hasObjectWithThisKeyInSession && !session.contains( domainObject );

    return sessionContainsThisObjectButHasADifferentReference;
  }

  /**
   * Copy all "shallow" elements from sourceDomainObject to the version of the object in the
   * Hibernate session. Shallow object are all bean properties that aren't collections, maps, audit
   * information, or other DomainObjects.
   * 
   * @param sourceDomainObject
   * @param session
   * @return The session version of the domain object, with values merged in from
   *         sourceDomainObject.
   */
  private static BaseDomain shallowMerge( BaseDomain sourceDomainObject, Session session )
  {
    BaseDomain sessionObject = (BaseDomain)session.get( sourceDomainObject.getClass(), sourceDomainObject.getId() );
    EntityPersister persister = ( (SessionWrapper)session ).getEntityPersister( sessionObject );

    Type[] types = persister.getPropertyTypes();
    String[] propertyNames = persister.getPropertyNames();

    for ( int i = 0; i < types.length; i++ )
    {
      Type type = types[i];
      if ( ! ( type instanceof AssociationType || isAuditType( type ) || isHibernateBackRef( propertyNames[i] ) ) )
      {
        String propertyName = propertyNames[i];
        try
        {
          // Only try to set if property exists. THis is to handle cases like user.ssnDecrypted,
          // where for whatever
          // reason there is no getter or setter (Hibernate uses field access on user.ssnDecrypted)
          if ( propertyExistsInDomainObject( sourceDomainObject, propertyName ) )
          {

            Object sourceObjectPropertyValue = PropertyUtils.getSimpleProperty( sourceDomainObject, propertyName );
            PropertyUtils.setSimpleProperty( sessionObject, propertyName, sourceObjectPropertyValue );
          }
        }
        catch( Exception e )
        {
          throw new BeaconRuntimeException( "Exception copying bean property: " + propertyName + "for class: " + sourceDomainObject.getClass().getName(), e );
        }
      }
    }

    return sessionObject;
  }

  public static List getObjectList( QueryConstraint queryConstraint )
  {
    Criteria criteria = queryConstraint.buildCriteria();

    return criteria.list();
  }

  public static Object getUniqueResultObject( QueryConstraint queryConstraint )
  {
    Criteria criteria = queryConstraint.buildCriteria();

    return criteria.uniqueResult();
  }

  public static int getObjectListCount( QueryConstraint queryConstraint )
  {
    Criteria criteria = queryConstraint.buildCriteria();
    criteria.setProjection( Projections.rowCount() );
    List countAsList = criteria.list();

    if ( countAsList.size() != 1 )
    {
      throw new BeaconRuntimeException( "Criteria count query was called and should only return one " + "Integer result, but result count was: " + countAsList.size() );
    }
    return ( (Long)countAsList.get( 0 ) ).intValue();
  }

  private static boolean propertyExistsInDomainObject( BaseDomain sourceDomainObject, String propertyName )
  {
    return PropertyUtils.isReadable( sourceDomainObject, propertyName ) && PropertyUtils.isWriteable( sourceDomainObject, propertyName );
  }

  private static boolean isHibernateBackRef( String propertyName )
  {
    return propertyName.startsWith( "_" ) && propertyName.endsWith( "Backref" );
  }

  /**
   * Returns true if the Hibernate type is either of type AuditInfoType or AuditUpdateType
   * 
   * @param type
   */
  private static boolean isAuditType( Type type )
  {
    boolean isAuditType = false;
    if ( type instanceof CustomType )
    {
      Class customTypeReturnedClass = type.getReturnedClass();
      if ( customTypeReturnedClass.equals( AuditUpdateInfo.class ) || customTypeReturnedClass.equals( AuditCreateInfo.class ) )
      {
        isAuditType = true;
      }
    }

    return isAuditType;
  }

}
