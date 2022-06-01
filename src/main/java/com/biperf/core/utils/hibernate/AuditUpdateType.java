/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/hibernate/AuditUpdateType.java,v $
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

import com.biperf.core.domain.AuditUpdateInfo;

/**
 * Hibernate UserType for audit tracking. The update audit info is updated via the
 * HiberateAuditInterceptor.
 * <p>
 * Sample mapping using the AuditUpdateType. Note that the column names have to be in the order
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
 *          &lt;property name=&quot;auditUpdateInfo&quot; type=&quot;com.biperf.core.utils.hibernate.AuditUpdateType&quot;&gt;
 *            &lt;column name=&quot;DATE_MODIFIED&quot;/&gt;
 *            &lt;column name=&quot;MODIFIED_BY&quot;/&gt;
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
 * <td>Apr 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuditUpdateType implements UserType
{
  private static final int[] SQL_TYPES = new int[] { Types.TIMESTAMP, Types.VARCHAR };

  /**
   * Return the array of sql types used by the AuditUpdateType.
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
    return AuditUpdateInfo.class;
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
    AuditUpdateInfo aix = (AuditUpdateInfo)x;
    AuditUpdateInfo aiy = (AuditUpdateInfo)y;
    try
    {
      return StandardBasicTypes.TIMESTAMP.isEqual( aix.getDateModified(), aiy.getDateModified() )
          && StandardBasicTypes.STRING.isEqual( aix.getModifiedBy() == null ? "" : aix.getModifiedBy().toString(), aiy.getModifiedBy() == null ? "" : aiy.getModifiedBy().toString() );
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
    AuditUpdateInfo ai = (AuditUpdateInfo)value;
    AuditUpdateInfo result = new AuditUpdateInfo();

    if ( ai.getDateModified() == null )
    {
      result.setDateModified( null );
    }
    else
    {
      result.setDateModified( ai.getDateModified() );
    }

    result.setModifiedBy( ai.getModifiedBy() );
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
    AuditUpdateInfo ai = new AuditUpdateInfo();
    ai.setDateModified( (java.sql.Timestamp)StandardBasicTypes.TIMESTAMP.nullSafeGet( rs, names[0] ) );
    ai.setModifiedBy( (Long)StandardBasicTypes.LONG.nullSafeGet( rs, names[1] ) );
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
    AuditUpdateInfo ai = (AuditUpdateInfo)value;
    StandardBasicTypes.TIMESTAMP.nullSafeSet( st, ai.getDateModified(), index );
    StandardBasicTypes.LONG.nullSafeSet( st, ai.getModifiedBy(), index + 1 );
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
   * Returns a deepCopy of the AuditUpdateInfo
   * 
   * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
   * @param value
   * @return serializeable representation of object
   * @throws HibernateException
   */
  public Serializable disassemble( Object value ) throws HibernateException
  {
    return (AuditUpdateInfo)deepCopy( value );
  }

  /**
   * Returns a deepCopy of the cached object
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
   * Return the original so the values will be copied during merge.
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
    return deepCopy( original );
  }
}
