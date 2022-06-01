/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/Environment.java,v $
 */

package com.biperf.core.utils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.util.StringUtils;

/**
 * Environment.
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
 * <td>Brian Repko</td>
 * <td>Jun 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
abstract public class Environment
{
  public static final String ENV_TMP_DIR_NAME = "appdatadir";
  public static final String ENV_PROPERTY_NAME = "environment.name";
  public static final String ENV_SERVER_INSTANCE_NAME = "com.sun.aas.instanceName";

  public static final String ENV_UNKNOWN = "unknown";
  public static final String ENV_DEV = "dev";
  public static final String ENV_QA = "qa";
  public static final String ENV_PRE = "preprod";
  public static final String ENV_PROD = "prod";

  private static final List<String> ENV_CTECH = Arrays.asList( new String[] { ENV_QA, ENV_PRE, ENV_PROD } );

  private static final List<String> ENV_ALL = Arrays.asList( new String[] { ENV_DEV, ENV_QA, ENV_PRE, ENV_PROD } );
  /**
   *  JVM system level property
   */
  public static final String PROXY_HOST = "bi.http.proxyHost";
  /**
   *  JVM system level property
   */
  public static final String PROXY_PORT = "bi.http.proxyPort";
  public static final String DEFAULT_PROXY_PORT = "8080";

  /**
   * Returns a string representing the BI environment that you are running in based on the system
   * property environment.name
   * 
   * @return dev, qa, preprod, prod or unknown if no system property is set or does not match a
   *         known value
   */
  public static String getEnvironment()
  {
    String env = System.getProperty( ENV_PROPERTY_NAME );
    if ( env == null || env.trim().equals( "" ) )
    {
      return ENV_UNKNOWN;
    }
    env = env.toLowerCase();
    return ENV_ALL.contains( env ) ? env : ENV_UNKNOWN;
  }

  /**
   * @return true if your environment is qa, preprod or prod
   */
  public static boolean isCtech()
  {
    return ENV_CTECH.contains( getEnvironment() );
  }

  /**
   * Get the Proxy host for the current environment (qa, preprod, prod)
   * 
   * @return String
   */
  public static String getProxy()
  {
    // read JVM system level property
    String proxy = System.getProperty( PROXY_HOST );

    return proxy;
  }

  /**
   * Get the Proxy port for the current environment (qa, preprod, prod)
   * 
   * @return int
   */
  public static int getProxyPort()
  {
    // read JVM system level property
    int proxyPort = Integer.parseInt( System.getProperty( PROXY_PORT, DEFAULT_PROXY_PORT ) );

    return proxyPort;
  }

  public static String getHostServer()
  {
    return System.getProperty( ENV_SERVER_INSTANCE_NAME );
  }

  public static String getTmpDir()
  {
    return System.getProperty( ENV_TMP_DIR_NAME );
  }

  public static Proxy buildProxy()
  {
    if ( AwsUtils.isAws() ) // no proxy
    {
      return Proxy.NO_PROXY;
    }
    return new Proxy( Proxy.Type.HTTP, new InetSocketAddress( Environment.getProxy(), Environment.getProxyPort() ) );
  }

  public static String getEnvironmentSuffix()
  {
    String currentEnvironment = getEnvironment();
    if ( StringUtils.isNullOrEmpty( currentEnvironment ) )
    {
      throw new IllegalArgumentException( " Environment cannot be empty to get the stream name" );
    }
    else if ( ENV_DEV.equalsIgnoreCase( currentEnvironment ) )
    {
      return "dev";
    }
    else if ( ENV_QA.equalsIgnoreCase( currentEnvironment ) )
    {
      return "qa";
    }
    else if ( ENV_PRE.equalsIgnoreCase( currentEnvironment ) )
    {
      return "pprd";
    }
    else if ( ENV_PROD.equalsIgnoreCase( currentEnvironment ) )
    {
      return "prod";
    }
    else if ( ENV_UNKNOWN.equalsIgnoreCase( currentEnvironment ) )
    {
      return currentEnvironment;
    }
    return "";
  }

}
