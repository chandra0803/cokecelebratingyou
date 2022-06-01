/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/hibernate/AuditCreateType.java,v $
 */

package com.biperf.core.utils.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

import com.biperf.core.domain.AuditCreateInfo;

/**
 * Hibernate UserType for audit tracking. The create audit info is updated via the
 * HiberateAuditInterceptor.
 * <p>
 * Sample mapping using the AuditCreateType. Note that the column names have to be in the order
 * specified.
 * 
 * <pre>
 * 
 *  
 *   
 *    
 *     
 *      
 *       
 *        
 *         
 *          &lt;property name=&quot;auditCreateInfo&quot; type=&quot;com.biperf.core.utils.hibernate.AuditCreateType&quot;&gt;
 *            &lt;column name=&quot;DATE_CREATED&quot;/&gt;
 *            &lt;column name=&quot;CREATED_BY&quot;/&gt;
 *          &lt;/property&gt;
 *          
 *         
 *        
 *       
 *      
 *     
 *    
 *   
 *  
 * </pre>
 * 
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
 * <td>jdunne</td>
 * <td>Apr 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuditCreateType implements UserType
{
  private static final int[] SQL_TYPES = new int[] { Types.TIMESTAMP, Types.VARCHAR };

  /**
   * Return the array of sql types used by the AuditCreateType.
   * 
   * @return array of sql types
   */
  public int[] sqlTypes()
  {
    return SQL_TYPES;
  }

  /**
   * @return true
   */
  public boolean isMutable()
  {
    return true;
  }

  /**
   * @return AuditInfo.class
   */
  public Class returnedClass()
  {
    return AuditCreateInfo.class;
  }

  /**
   * Compare two AuditInfo classes.
   * 
   * @param x
   * @param y
   * @return true if objects are same or equal
   */
  public boolean equals( Object x, Object y )
  {
    if ( x == y )
    {
      return true;
    }
    if ( x == null || y == null )
    {
      return false;
    }
    AuditCreateInfo aix = (AuditCreateInfo)x;
    AuditCreateInfo aiy = (AuditCreateInfo)y;
    try
    {
      return StandardBasicTypes.TIMESTAMP.isEqual( aix.getDateCreated(), aiy.getDateCreated() )
          && StandardBasicTypes.STRING.isEqual( aix.getCreatedBy() == null ? "" : aix.getCreatedBy().toString(), aiy.getCreatedBy() == null ? "" : aiy.getCreatedBy().toString() );
    }
    catch( HibernateException e )
    {
      return false;
    }
  }

  /**
   * Deep copy of a AuditInfo object.
   * 
   * @param value
   * @return copy of object
   */
  public Object deepCopy( Object value )
  {
    if ( value == null )
    {
      return null;
    }
    AuditCreateInfo ai = (AuditCreateInfo)value;
    AuditCreateInfo result = new AuditCreateInfo();

    if ( ai.getDateCreated() == null )
    {
      result.setDateCreated( null );
    }
    else
    {
      result.setDateCreated( ai.getDateCreated() );
    }
    result.setCreatedBy( ai.getCreatedBy() );
    return result;
  }

  /**
   * Get the AuditInfo object from the ResultSet. Creates a new AuditInfo object.
   * 
   * @param rs
   * @param names
   * @param owner
   * @return object that represents the data
   * @throws HibernateException
   * @throws java.sql.SQLException
   */
  public Object nullSafeGet( ResultSet rs, String[] names, Object owner ) throws HibernateException, SQLException
  {

    // AuditInfo can't be null
    AuditCreateInfo ai = new AuditCreateInfo();
    ai.setDateCreated( (java.sql.Timestamp)StandardBasicTypes.TIMESTAMP.nullSafeGet( rs, names[0] ) );
    ai.setCreatedBy( (Long)StandardBasicTypes.LONG.nullSafeGet( rs, names[1] ) );
    return ai;
  }

  /**
   * Set the AuditInfo parameters on the prepared statement.
   * 
   * @param st
   * @param value
   * @param index
   * @throws HibernateException
   * @throws SQLException
   */
  public void nullSafeSet( PreparedStatement st, Object value, int index ) throws HibernateException, SQLException
  {

    // AuditInfo can't be null
    AuditCreateInfo ai = (AuditCreateInfo)value;
    StandardBasicTypes.TIMESTAMP.nullSafeSet( st, ai.getDateCreated(), index );
    StandardBasicTypes.LONG.nullSafeSet( st, ai.getCreatedBy(), index + 1 );
  }

  /**
   * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
   * @param x
   * @return hashcode
   * @throws HibernateException
   */
  public int hashCode( Object x ) throws HibernateException
  {
    return x.hashCode();
  }

  /**
   * Return a deepCopy of AuditCreateInfo object.
   * 
   * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
   * @param value
   * @return serializeable representation of object
   * @throws HibernateException
   */
  public Serializable disassemble( Object value ) throws HibernateException
  {
    return (AuditCreateInfo)deepCopy( value );
  }

  /**
   * Returns a deep copy of the cached object.
   * 
   * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
   * @param cached
   * @param owner
   * @return object from serializable representation
   * @throws HibernateException
   */
  public Object assemble( Serializable cached, Object owner ) throws HibernateException
  {
    return deepCopy( cached );
  }

  /**
   * Returns the target object. We don't want the Creation information to be changed.
   * 
   * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object,
   *      java.lang.Object)
   * @param original
   * @param target
   * @param owner
   * @return substitute object
   * @throws HibernateException
   */
  public Object replace( Object original, Object target, Object owner ) throws HibernateException
  {
    return target;
  }
}
