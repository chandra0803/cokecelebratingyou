
package com.biperf.core.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.email.EmailCredentials;
import com.biperf.core.service.email.EmailService;
import com.sun.mail.smtp.SMTPTransport;

public class CheckEmailServer
{
  private static final Log logger = LogFactory.getLog( CheckEmailServer.class );

  /**
   * Connect to the first available server from the list. If no server is
   * available, return null.
   * 
   * @param serverList
   *            The list of server(s) that will be checked to see if it's available.
   *            
   * @return String of the first available server or null if none of the server is unavailable.
   * 
   * @exception IOException
   *                if the application throws an IOException
   */
  public static String connectedTo( String serverList ) throws IOException
  {
    // Remove spaces from the comma separated list
    String noSpaceServer = serverList.replaceAll( "\\s", "" );
    String[] listOfServer = noSpaceServer.split( "\\," );
    Socket server = null;

    for ( int i = 0; i < listOfServer.length; i++ )
    {
      // for AWS, there is no list - so just attempt to send the email
      if ( AwsUtils.isAws() )
      {
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "AWS SMTP Connecting to: " + listOfServer[i] );
        }
        return listOfServer[i];
      }
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Attempting to connect to: " + listOfServer[i] );
      }
      server = new Socket();
      SocketAddress socketAddress = new InetSocketAddress( listOfServer[i], 25 );

      // Attempt to connect to the server
      try
      {
        server.connect( socketAddress, 1000 );
        server.close();

        Properties properties = new Properties();
        properties.setProperty( "mail.smtp.host", listOfServer[i] );

        Session session = Session.getDefaultInstance( properties );
        SMTPTransport transport = new SMTPTransport( session, null );

        // Connect to the server and issue the HELO command. Return
        // the server name, if the response code is 250
        try
        {
          if ( AwsUtils.isAws() )
          {
            EmailCredentials credentials = getEmailService().getSMTPEmailCredentials();
            transport.connect( listOfServer[i], credentials.getUsername(), credentials.getPassword() );
          }
          else
          {
            transport.connect( listOfServer[i], null, null );
          }

          transport.issueCommand( "HELO " + listOfServer[i], 250 );

          // Check return code to make sure the server is available
          if ( transport.getLastReturnCode() == 250 )
          {
            transport.close();
            return listOfServer[i];
          }

          transport.close();
        }
        catch( MessagingException e1 )
        {
          logger.error( "Unable to connect to " + listOfServer[i] + " " + e1.getMessage(), e1 );
        }
      }
      catch( IOException e )
      {
        logger.error( "Unable to connect to " + listOfServer[i] + " " + e.getMessage(), e );
      }
    }

    return null;
  }

  private static EmailService getEmailService()
  {
    return (EmailService)BeanLocator.getBean( EmailService.BEAN_NAME );
  }
}
