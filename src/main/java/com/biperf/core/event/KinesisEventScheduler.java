
package com.biperf.core.event;

import static com.biperf.core.utils.Environment.ENV_DEV;
import static com.biperf.core.utils.Environment.getEnvironment;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.profile.internal.ProfileKeyConstants;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UrlReader;
import com.biw.event.streams.consume.Consumer;
import com.biw.event.streams.consume.EventProcessor;
import com.biw.event.streams.consume.config.ConsumeConfiguration;
import com.biw.event.streams.consume.impl.ConsumerImpl;

@Component( "kinesisEventScheduler" )
public class KinesisEventScheduler
{
  public static String BEAN_NAME = "kinesisEventScheduler";

  private static Log log = LogFactory.getLog( KinesisEventScheduler.class );
  Consumer<ConsumeConfiguration> consumer = new ConsumerImpl();
  public static final String UnixFileSeparator = "/";
  private static final String WindowsFileSeparator = "\\";
  private static final String DATA_DIR_PROPERTY = "appsecuredatadir";

  // for the kinesis consumer thread
  Thread consumerThread;

  /*
   * Start the Kinesis consumer
   */
  public void startKinesisConsumer() throws ServletException
  {

    if ( !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( "New version of Service Anniversary is NOT enabled" );
      return;
    }

    // Added this condition to avoid development environment avoid to consume kinesis events
    if ( isDevEnv() )
    {
      log.error( "Development environemnt won't consume kinesis events or app secure data dir not configured" );
      return;
    }

    try
    {
      log.error( "Initializing Kinesis Stream............" );
      PropertySetItem streamNameProp = getSystemVariableService().getPropertyByName( SystemVariableService.AWS_KINESIS_STREAMNAME );
      String streamName = null;
      PropertySetItem regionProp = getSystemVariableService().getPropertyByName( SystemVariableService.AWS_KINESIS_REGION );
      String region = null;
      PropertySetItem awsAcctNoProp = getSystemVariableService().getPropertyByName( SystemVariableService.AWS_ACCOUNT_NO );
      String awsAcctNo = null;

      String companyId = MeshServicesUtil.getCompanyId();

      if ( companyId == null || StringUtils.isBlank( companyId ) )
      {
        log.error( "Kinesis consumer not initialized due to Company information is not configured" );
        return;
      }

      if ( isNull( streamNameProp ) || isEmpty( streamNameProp.getStringVal() ) )
      {
        log.error( "Kinesis consumer not initialized due to Kinesis Stream Name not configured" );
        return;
      }
      else
      {
        streamName = streamNameProp.getStringVal();
      }

      if ( isNull( regionProp ) || isEmpty( regionProp.getStringVal() ) )
      {
        log.error( "Kinesis consumer not initialized due to Kinesis Region Name not configured" );
        return;
      }
      else
      {
        region = regionProp.getStringVal();
      }

      if ( isNull( awsAcctNoProp ) || isEmpty( awsAcctNoProp.getStringVal() ) )
      {
        log.error( "AWS account not configured" );
        return;
      }
      else
      {
        awsAcctNo = awsAcctNoProp.getStringVal();
      }

      ConsumeConfiguration consumeConfiguration = (ConsumeConfiguration)BeanLocator.getBean( "consumerConfig" );
      Set<EventProcessor> eventProcessors = BeanLocator.getBeans( EventProcessor.class );

      PropertySetItem awsKey = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.ELASTICSEARCH_CREDENTIALS_USERNAME );
      if ( isNull( awsKey ) || isEmpty( awsKey.getStringVal() ) )
      {
        log.error( "AWS access key is  not configured" );
        return;
      }

      PropertySetItem awsSecret = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.ELASTICSEARCH_CREDENTIALS_PASSWORD );
      if ( isNull( awsSecret ) || isEmpty( awsSecret.getStringVal() ) )
      {
        log.error( "AWS secter  not configured" );
        return;
      }

      if ( !AwsUtils.isAws() )
      {
        createAWSCredentialFile( awsKey.getStringVal(), awsSecret.getStringVal() );
      }

      try
      {
        log.error( "Initialize the Kinesis stream " + streamName + " in region " + region );

        consumerThread = new Thread( new KinesisThread( consumeConfiguration, eventProcessors ) );
        consumerThread.start();

        log.error( "Started Kinesis Consumer process" );

      }
      catch( Throwable e )
      {
        log.error( "Exception in Kinesis consumer configuration processRecords call", e );
        e.printStackTrace();
      }

      log.error( "Exit from Kinesis Consumer Initialization" );
    }
    catch( Exception e )
    {
      log.error( "Exception in Kinesis consumer configuration", e );
      e.printStackTrace();
    }
  }

  /**
   * Returns the status of Kinesis consumer Scheduler thread
   * 
   * @return
   */
  public boolean kinesisSchedulerStatus()
  {
    if ( consumerThread != null )
    {
      return consumerThread.isAlive();
    }
    return false;
  }

  private boolean createAWSCredentialFile( String key, String secret )
  {
    log.info( "creating AWS Credential File" );
    String fileName = "";
    String failureMsg = "";
    File extractFile = null;

    try
    {
      fileName = "credentials";
      String credentialLocation = getAppSecureDataLocation();

      FileExtractUtils.createDirIfNeeded( credentialLocation );

      // Writes a file with the aws credentials.
      extractFile = new File( credentialLocation, fileName );

      // Build failure message just in case.
      failureMsg = "An exception occurred while attempting to write file for aws credential ";

      boolean success = false;
      if ( !extractFile.exists() )
      {
        success = extractFile.createNewFile();
      }
      else
      {
        success = true;
      }

      if ( success )
      {
        BufferedWriter writer = new BufferedWriter( new FileWriter( extractFile ) );

        // default profile.
        writer.write( "[default]" );
        writer.newLine();
        writer.write( ProfileKeyConstants.AWS_ACCESS_KEY_ID );
        writer.write( " = " );
        writer.write( key );
        writer.newLine();
        writer.write( ProfileKeyConstants.AWS_SECRET_ACCESS_KEY );
        writer.write( " = " );
        writer.write( secret );

        writer.close();
      }
      else
      {
        log.error( failureMsg );
        return false;
      }
    }
    catch( IOException e )
    {
      log.error( failureMsg, e );
      return false;
    }
    catch( Exception e )
    {
      log.error( failureMsg, e );
      return false;
    }
    return true;
  }

  public String getAppSecureDataLocation()
  {
    String appSecureLocation = null;

    // On Windows this is \ On Unix this is /
    String currentSystemFileSeparator = File.separator;

    // user defined path where the csv file should be saved. i.e. /tmp/ on Unix
    appSecureLocation = System.getProperty( DATA_DIR_PROPERTY );

    // make sure the user defined directory works with the current system
    if ( !StringUtils.isBlank( appSecureLocation ) )
    {
      // e.g. Developers running on localhosts on Windows
      // but the system variable specifies an Unix file separator
      if ( appSecureLocation.indexOf( appSecureLocation ) >= 0 && currentSystemFileSeparator.equals( WindowsFileSeparator ) )
      {
        appSecureLocation = appSecureLocation.replace( '/', '\\' );
      }
      // e.g. QA, PPRD, PROD - CTECH environments running on Unix
      // but the system variable specifies a Windows file separator
      if ( appSecureLocation.indexOf( WindowsFileSeparator ) >= 0 && currentSystemFileSeparator.equals( UnixFileSeparator ) )
      {
        appSecureLocation = appSecureLocation.replace( '\\', '/' );
      }
    }

    appSecureLocation = appSecureLocation + currentSystemFileSeparator + ".aws";

    return appSecureLocation;
  }

  private class KinesisThread implements Runnable
  {
    ConsumeConfiguration consumeConfiguration;
    Set<EventProcessor> eventProcessors;

    public KinesisThread( ConsumeConfiguration consumeConfiguration, Set<EventProcessor> eventProcessors )
    {
      this.consumeConfiguration = consumeConfiguration;
      this.eventProcessors = eventProcessors;
    }

    @Override
    public void run()
    {
      try
      {
        consumer.processRecords( consumeConfiguration, eventProcessors );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
    }
  }

  private boolean isDevEnv()
  {
    try
    {
      String envUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      if ( ENV_DEV.equalsIgnoreCase( getEnvironment() ) || !new UrlReader().useProxy( envUrl ) )
      {
        return true;
      }
    }
    catch( Exception e )
    {
      return false;
    }

    return false;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }
}
