
package com.biperf.core.event.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.Environment;
import com.biw.event.streams.consume.config.CrossAccountConsumeConfiguration;

@Component( "consumerConfig" )
public class ConsumeConfigurationImpl extends CrossAccountConsumeConfiguration
{

  protected @Autowired SystemVariableService systemVariableService;

  public ConsumeConfigurationImpl()
  {
  }

  @Override
  public String streamName()
  {
    return systemVariableService.getPropertyByName( SystemVariableService.AWS_KINESIS_STREAMNAME ).getStringVal();
  }

  @Override
  public String region()
  {
    return systemVariableService.getPropertyByName( SystemVariableService.AWS_KINESIS_REGION ).getStringVal();
  }

  @Override
  public String applicationName()
  {
    // return systemVariableService.getContextName();
    String appName = systemVariableService.getContextName();
    String ctxName = systemVariableService.getContextName();
    String env = Environment.getEnvironmentSuffix();
    try
    {
      StringBuffer appStr = new StringBuffer();
      appStr.append( ctxName );
      appStr.append( "-" );
      appStr.append( env );
      appName = appStr.toString();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    return appName;
  }

  @Override
  public String roleArn()
  {
    String accountNo = systemVariableService.getPropertyByName( SystemVariableService.AWS_ACCOUNT_NO ).getStringVal();
    String env = Environment.getEnvironmentSuffix();
    String ctxName = systemVariableService.getContextName();
    String roleARNStr = null;
    try
    {
      StringBuffer roleARN = new StringBuffer();
      roleARN.append( "arn:aws:iam::" );
      roleARN.append( accountNo );
      roleARN.append( ":role/external-acct-" );
      roleARN.append( ctxName );
      roleARN.append( "-" );
      roleARN.append( env );
      roleARN.append( "-" );
      roleARN.append( "role" );
      roleARNStr = roleARN.toString();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    return roleARNStr;
  }

  @Override
  public String roleSessionName()
  {
    return systemVariableService.getContextName();
  }

}
