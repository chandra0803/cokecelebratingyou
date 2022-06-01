/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/util/ServiceError.java,v $
 */

package com.biperf.core.service.util;

import org.apache.commons.lang3.StringUtils;

/**
 * ServiceError.
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
 * <td>tennant</td>
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ServiceError
{
  private String key = "";
  private String arg1 = "";
  private String arg2 = "";
  private String arg3 = "";
  private String arg4 = "";
  private String[] args = new String[0];

  /**
   * @param key password_must_be_mixed
   */
  public ServiceError( String key )
  {
    this.key = key;
    args = new String[0];
  }

  public ServiceError( String key, String arg1 )
  {
    this( key );

    if ( arg1 != null )
    {
      this.arg1 = arg1;
      args = new String[] { arg1 };
    }
  }

  public ServiceError( String key, String arg1, String arg2 )
  {
    this( key, arg1 );

    if ( arg2 != null )
    {
      this.arg2 = arg2;
      args = new String[] { arg1, arg2 };
    }
  }

  public ServiceError( String key, String arg1, String arg2, String arg3 )
  {
    this( key, arg1, arg2 );

    if ( arg3 != null )
    {
      this.arg3 = arg3;
      args = new String[] { arg1, arg2, arg3 };
    }
  }

  public ServiceError( String key, String arg1, String arg2, String arg3, String arg4 )
  {
    this( key, arg1, arg2 );

    if ( arg3 != null )
    {
      this.arg3 = arg3;
      args = new String[] { arg1, arg2, arg3 };
    }

    if ( arg4 != null )
    {
      this.arg4 = arg4;
      args = new String[] { arg1, arg2, arg3, arg4 };
    }
  }

  public String getArg1()
  {
    return arg1;
  }

  public String getArg2()
  {
    return arg2;
  }

  public String getArg3()
  {
    return arg3;
  }

  public String getArg4()
  {
    return arg4;
  }

  public String getKey()
  {
    return key;
  }

  public String[] getArgs()
  {
    // if args is set return args else check args1 to args3 before returning the string array
    if ( args != null && args.length > 0 )
    {
      return args;
    }
    if ( !StringUtils.isEmpty( arg1 ) )
    {
      args = new String[] { getArg1() };
    }
    if ( !StringUtils.isEmpty( arg2 ) )
    {
      args = new String[] { getArg1(), getArg2() };
    }
    if ( !StringUtils.isEmpty( arg3 ) )
    {
      args = new String[] { getArg1(), getArg2(), getArg3() };
    }
    if ( !StringUtils.isEmpty( arg4 ) )
    {
      args = new String[] { getArg1(), getArg2(), getArg3(), getArg4() };
    }
    return args;
  }

  /**
   * @return number of arguments
   */
  public int getNumberOfArgs()
  {
    if ( args != null )
    {
      return args.length;
    }
    return 0;
  }

  public String toString()
  {
    StringBuffer toString = new StringBuffer( "key: " ).append( getKey() ).append( ", arg0: " );
    for ( int i = 0; i < args.length; i++ )
    {
      toString.append( args[i] );
      if ( i < args.length - 1 )
      {
        toString.append( ", arg" ).append( i + 1 ).append( ": " );
      }
    }
    return toString.toString();
  }

  public void setArg1( String arg1 )
  {
    this.arg1 = arg1;
    args = new String[] { arg1 };
  }

  public void setArg2( String arg2 )
  {
    this.arg2 = arg2;
    args = new String[] { getArg1(), arg2 };
  }

  public void setArg3( String arg3 )
  {
    this.arg3 = arg3;
    args = new String[] { getArg1(), getArg2(), arg3 };
  }

  public void setArg4( String arg4 )
  {
    this.arg4 = arg4;
    args = new String[] { getArg1(), getArg2(), getArg3(), arg4 };
  }

  public void setArgs( String[] args )
  {
    this.args = args;
  }
}
