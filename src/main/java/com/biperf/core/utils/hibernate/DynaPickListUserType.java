/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/hibernate/DynaPickListUserType.java,v $
 */

package com.biperf.core.utils.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.EnhancedUserType;

import com.biperf.core.domain.enums.DynaPickListType;

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
 *            &lt;property name=&quot;emailType&quot; column=&quot;email_type&quot;&gt;
 *              &lt;type name=&quot;com.biperf.core.utils.hibernate.PickListUserType&quot;&gt;
 *                &lt;param name=&quot;lookupClass&quot;&gt;com.biperf.core.domain.enums.EmailAddressType&lt;/param&gt;
 *                &lt;param name=&quot;dbType&quot;&gt;String&lt;/param&gt;
 *              &lt;/type&gt;
 *            &lt;/property&gt;
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
 * <td>sedey</td>
 * <td>Apr 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class DynaPickListUserType implements EnhancedUserType
{

  // private static final Log LOG = LogFactory.getLog( DynaPickListUserType.class );
  // private static final String DB_TYPE_STRING = "String";
  // private static final String DB_TYPE_INTEGER = "Integer";

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
    return DynaPickListType.class;
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
    // same as toXMLString
    return toXMLString( value );
  }

  /**
   * @param value
   * @return ((PickListItem)value).getCode();
   */
  public String toXMLString( Object value )
  {
    return ( (DynaPickListType)value ).getPickListAssetCode() + ":" + ( (DynaPickListType)value ).getCode();
  }

  /**
   * Lookup PickListItem concrete class from value.
   * 
   * @param xmlValue
   * @return PickListItem
   */
  public Object fromXMLString( String xmlValue )
  {
    // xmlValue=picklist.emailtype:hom
    // parse out picklist name and code
    String asset = xmlValue.substring( 0, xmlValue.indexOf( ":" ) );
    String code = xmlValue.substring( xmlValue.indexOf( ":" ) + 1 );
    return DynaPickListType.lookup( asset, code );
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
    String code = (String)StandardBasicTypes.STRING.nullSafeGet( rs, names[0] );
    if ( code == null )
    {
      return null;
    }
    return fromXMLString( code );
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
      StandardBasicTypes.STRING.nullSafeSet( st, toXMLString( value ), index );
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
    return (DynaPickListType)deepCopy( value );
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
    return original;
  }

}
