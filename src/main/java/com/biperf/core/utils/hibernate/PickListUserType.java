/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/hibernate/PickListUserType.java,v $
 */

package com.biperf.core.utils.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.exception.BeaconRuntimeException;

/**
 * Hibernate UserType for type safe enum support from PickLists. Picklists are defined in the
 * Content Manager.
 * <p>
 * Sample mapping using the PickListUserType. Note the param name "lookupClass". This specifies
 * which concrete PickListItem class to use for the type safe enum.
 * <p>
 * The property dbType is optional and defaults to String. Valid settings for dbType is "String" or
 * "Integer".
 * <p>
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
 *           &lt;property name=&quot;emailType&quot; column=&quot;email_type&quot;&gt;
 *             &lt;type name=&quot;com.biperf.core.utils.hibernate.PickListUserType&quot;&gt;
 *               &lt;param name=&quot;lookupClass&quot;&gt;com.biperf.core.domain.enums.EmailAddressType&lt;/param&gt;
 *               &lt;param name=&quot;dbType&quot;&gt;String&lt;/param&gt;
 *             &lt;/type&gt;
 *           &lt;/property&gt;
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
 * <td>Apr 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PickListUserType implements EnhancedUserType, ParameterizedType, Serializable
{

  private static final Log LOG = LogFactory.getLog( PickListUserType.class );
  private static final String DB_TYPE_STRING = "String";
  private static final String DB_TYPE_INTEGER = "Integer";

  private String lookupClass;
  private String dbType = DB_TYPE_STRING;

  private static final int[] SQL_TYPES = new int[] { Types.VARCHAR };

  /**
   * @return single element array with only Types.VARCHAR
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
   * @return PickListItem.class
   */
  public Class returnedClass()
  {
    return PickListItem.class;
  }

  /**
   * @param x
   * @param y
   * @return true iff x = y
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
    return x.equals( y );
  }

  /**
   * @param value
   * @return PicklistItem.getCode()
   */
  public String objectToSQLString( Object value )
  {
    return ( (PickListItem)value ).getCode();
  }

  /**
   * @param value
   * @return ((PickListItem)value).getCode();
   */
  public String toXMLString( Object value )
  {
    return ( (PickListItem)value ).getCode();
  }

  /**
   * Lookup PickListItem concrete class from value.
   * 
   * @param xmlValue
   * @return PickListItem
   */
  public Object fromXMLString( String xmlValue )
  {
    return PickListItem.getPickListFactory().getPickListItem( getPickListClass(), xmlValue );
  }

  /**
   * @param x
   * @return int
   * @throws HibernateException
   */
  public int hashCode( Object x ) throws HibernateException
  {
    return x.hashCode();
  }

  /**
   * Get the concrete PickListItem instance from the code.
   * 
   * @param rs
   * @param names
   * @param owner
   * @return Object
   * @throws HibernateException
   * @throws SQLException
   */
  public Object nullSafeGet( ResultSet rs, String[] names, Object owner ) throws HibernateException, SQLException
  {
    String code = null;
    if ( DB_TYPE_STRING.equals( dbType ) )
    {
      code = (String)StandardBasicTypes.STRING.nullSafeGet( rs, names[0] );
    }
    else if ( DB_TYPE_INTEGER.equals( dbType ) )
    {
      code = StandardBasicTypes.INTEGER.nullSafeGet( rs, names[0] ).toString();
    }
    if ( code == null )
    {
      return null;
    }
    return PickListItem.getPickListFactory().getPickListItem( getPickListClass(), (String)StandardBasicTypes.STRING.nullSafeGet( rs, names[0] ) );
  }

  /**
   * Set the PickListItem.code on the prepared statement.
   * 
   * @param st
   * @param value
   * @param index
   * @throws HibernateException
   * @throws SQLException
   */
  public void nullSafeSet( PreparedStatement st, Object value, int index ) throws HibernateException, SQLException
  {
    if ( value == null )
    {
      StandardBasicTypes.STRING.nullSafeSet( st, null, index );
    }
    else
    {
      PickListItem item = (PickListItem)value;
      if ( DB_TYPE_STRING.equals( dbType ) )
      {
        StandardBasicTypes.STRING.nullSafeSet( st, item.getCode(), index );
      }
      else if ( DB_TYPE_INTEGER.equals( dbType ) )
      {
        StandardBasicTypes.INTEGER.nullSafeSet( st, new Integer( item.getCode() ), index );
      }
    }
  }

  /**
   * @param value
   * @return Object
   * @throws HibernateException
   */
  public Object deepCopy( Object value ) throws HibernateException
  {
    if ( value == null )
    {
      return null;
    }
    return value;
  }

  /**
   * Returns a deepCopy of the PickListItem
   * 
   * @param value
   * @return Serializable
   * @throws HibernateException
   */
  public Serializable disassemble( Object value ) throws HibernateException
  {
    return (PickListItem)deepCopy( value );
  }

  /**
   * Returns a deepCopy of the cached object.
   * 
   * @param cached
   * @param owner
   * @return Object
   * @throws HibernateException
   */
  public Object assemble( Serializable cached, Object owner ) throws HibernateException
  {
    return deepCopy( cached );
  }

  /**
   * Return original. Need the types to be copied during merge.
   * 
   * @param original
   * @param target
   * @param owner
   * @return Object
   * @throws HibernateException
   */
  public Object replace( Object original, Object target, Object owner ) throws HibernateException
  {
    return deepCopy( original );
  }

  /**
   * Set parameters passed in from the mapping file. Currently the only parameter used is named
   * "lookupClass". This must be the fully qualified concrete PickListItem class.
   * 
   * @param parameters
   */
  public void setParameterValues( Properties parameters )
  {
    lookupClass = parameters.getProperty( "lookupClass" );
    if ( parameters.getProperty( "dbType" ) != null )
    {
      dbType = parameters.getProperty( "dbType" );
    }
  }

  /**
   * Lookup the concrete PickListItem subclass using the lookupClass parameter set from the
   * setParameterValues method.
   * 
   * @return PickLIstItem.class()
   */
  private Class getPickListClass()
  {
    Thread t = Thread.currentThread();
    ClassLoader cl = t.getContextClassLoader();
    Class pickListClass = null;
    try
    {
      pickListClass = cl.loadClass( lookupClass );
    }
    catch( ClassNotFoundException e )
    {
      LOG.error( "Cannot find lookupClass : " + lookupClass, e );
      throw new BeaconRuntimeException( "Cannot find lookupClass : " + lookupClass, e );
    }
    return pickListClass;
  }
}
