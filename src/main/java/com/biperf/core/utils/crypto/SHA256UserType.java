/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/crypto/MD5UserType.java,v $
 */

package com.biperf.core.utils.crypto;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

/**
 * SHA256UserType.
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
 * <td>hamza</td>
 * <td>Mar 21, 2016</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SHA256UserType implements UserType
{
  private static final int[] SQL_TYPES = new int[] { Types.VARCHAR };

  /**
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[],
   *      java.lang.Object)
   * @param rs
   * @param names
   * @param owner
   * @return Object
   * @throws HibernateException
   * @throws SQLException
   */
  public Object nullSafeGet( ResultSet rs, String[] names, Object owner ) throws HibernateException, SQLException
  {
    // return the encrypted password
    return (String)StandardBasicTypes.STRING.nullSafeGet( rs, names[0] );
  }

  /**
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object,
   *      int)
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
      /*
       * This check is to avoid encrypting MD5 passwords to SHA when user enters a wrong password.
       * We leave it alone when wrong passwords are entered. When actual value of password is
       * updated, it gets encrypted in ELSE block
       */
      if ( ( (String)value ).startsWith( "{MD5}" ) )
      {
        StandardBasicTypes.STRING.nullSafeSet( st, (String)value, index );
      }
      else
      {
        StandardBasicTypes.STRING.nullSafeSet( st, new SHA256Hash().encryptDefault( (String)value ), index );
      }
    }
  }

  /**
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
   * @param x
   * @return int
   * @throws HibernateException
   */
  public int hashCode( Object x ) throws HibernateException
  {

    return x.hashCode();
  }

  /**
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#sqlTypes()
   * @return int[]
   */
  public int[] sqlTypes()
  {
    return SQL_TYPES;
  }

  /**
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#isMutable()
   * @return boolean
   */
  public boolean isMutable()
  {
    return true;
  }

  /**
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#returnedClass()
   * @return Class
   */
  public Class returnedClass()
  {
    return String.class;
  }

  /**
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
   * @param x
   * @param y
   * @return boolean
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
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
   * @param value
   * @return Object
   */
  public Object deepCopy( Object value )
  {
    if ( value == null )
    {
      return null;
    }
    return new String( (String)value );
  }

  /**
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
   * @param value
   * @return Serializable
   * @throws HibernateException
   */
  public Serializable disassemble( Object value ) throws HibernateException
  {
    return (String)deepCopy( value );
  }

  /**
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
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
   * Overridden from
   * 
   * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object,
   *      java.lang.Object)
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

}
