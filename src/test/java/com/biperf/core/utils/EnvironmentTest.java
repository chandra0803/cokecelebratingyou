
package com.biperf.core.utils;

import java.net.Proxy;

import org.apache.commons.lang3.StringUtils;

import junit.framework.TestCase;

public class EnvironmentTest extends TestCase
{

  public static final String ENV_PROPERTY_NAME = "environment.name";
  public static final String PROXY_HOST = "bi.http.proxyHost";
  public static final String PROXY_PORT = "bi.http.proxyPort";
  public static final String ENV_TMP_DIR_NAME = "appdatadir";
  public static final String ENVIRONMENT_TYPE = "environment.type";
  public static final String ENV_SERVER_INSTANCE_NAME = "com.sun.aas.instanceName";

  // Fails if the environment is unknown
  public void testGetEnvironment()
  {
    System.setProperty( ENV_PROPERTY_NAME, Environment.ENV_DEV );
    assertTrue( "Environment name doesn't match", Environment.ENV_DEV.equals( Environment.getEnvironment() ) );

    System.setProperty( ENV_PROPERTY_NAME, Environment.ENV_QA );
    assertTrue( "Environment name doesn't match", Environment.ENV_QA.equals( Environment.getEnvironment() ) );

    System.setProperty( ENV_PROPERTY_NAME, Environment.ENV_PRE );
    assertTrue( "Environment name doesn't match", Environment.ENV_PRE.equals( Environment.getEnvironment() ) );

    System.setProperty( ENV_PROPERTY_NAME, Environment.ENV_PROD );
    assertTrue( "Environment name doesn't match", Environment.ENV_PROD.equals( Environment.getEnvironment() ) );
  }

  // Checks if environemnt is ctech
  public void testIsCtech()
  {
    System.setProperty( ENV_PROPERTY_NAME, Environment.ENV_DEV );
    assertFalse( "Environment Ctech check failed", Environment.isCtech() );

    System.setProperty( ENV_PROPERTY_NAME, Environment.ENV_QA );
    assertTrue( "Environment Ctech check failed", Environment.isCtech() );

    System.setProperty( ENV_PROPERTY_NAME, Environment.ENV_PRE );
    assertTrue( "Environment Ctech check failed", Environment.isCtech() );

    System.setProperty( ENV_PROPERTY_NAME, Environment.ENV_PROD );
    assertTrue( "Environment Ctech check failed", Environment.isCtech() );
  }

  public void testGetProxyHost()
  {
    System.setProperty( PROXY_HOST, "proxyuser.biperf.com" );
    assertTrue( "Proxy host cannot be empty", !StringUtils.isEmpty( Environment.getProxy() ) );
  }

  public void testGetProxyPort()
  {
    System.setProperty( PROXY_PORT, "8080" );
    assertTrue( "Proxy port cannot be empty", Environment.getProxyPort() != 0 );
  }

  public void testGetHostServer()
  {
    System.setProperty( ENV_SERVER_INSTANCE_NAME, "test" );
    assertTrue( "Host Server cannot be empty", !StringUtils.isEmpty( Environment.getHostServer() ) );
  }

  public void testGetTmpDir()
  {
    System.setProperty( ENV_TMP_DIR_NAME, "/dev/appdatadir" );
    assertTrue( "Temp Directory cannot be empty", !StringUtils.isEmpty( Environment.getTmpDir() ) );
  }

  public void testBuildProxy()
  {
    System.setProperty( ENVIRONMENT_TYPE, "aws" );
    assertTrue( "Proxy should be direct for aws environment", Environment.buildProxy().type().equals( Proxy.Type.DIRECT ) );

    System.setProperty( ENVIRONMENT_TYPE, "bius" );
    System.setProperty( PROXY_HOST, "proxyuser.biperf.com" );
    System.setProperty( PROXY_PORT, "8080" );
    assertTrue( "Proxy should be http for bius environment", Environment.buildProxy().type().equals( Proxy.Type.HTTP ) );

  }
}
