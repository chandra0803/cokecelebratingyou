
package com.biperf.core.ui.user;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.EncryptionService;
import com.biperf.core.service.system.SystemVariableService;

@Controller
@RequestMapping( "/account" )
public class AccountLockController extends BaseUserController
{

  private static final Log log = LogFactory.getLog( AccountLockController.class );

  private static String ENCRYPTION_PREFIX = "{AES}";
  public static final String LOGIN = "login";
  public static final String STATIC_PAGE = "static";

  private @Autowired SystemVariableService systemVariableService;
  private @Autowired UserService userService;
  private @Autowired EncryptionService encryptionService;

  @RequestMapping( value = "/lock.action", method = RequestMethod.GET )
  public void lockAccount( @RequestParam( "key" ) String encryptedValue, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    try
    {
      String value = encryptionService.getDecryptedValue( ENCRYPTION_PREFIX + encryptedValue );
      Long userId = Long.parseLong( value );
      boolean accountLocked = userService.IsUserAccountLocked( userId );
      if ( accountLocked )
      {
        response.sendRedirect( buildRedirect( accountLocked ) );
      }
      else
      {
        response.sendRedirect( accountLockConfirm( userId ) );
      }
    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }
  }

  @RequestMapping( value = "/lockAccountConfirm.action", method = RequestMethod.GET )
  public void lockAccountConfirmed( @RequestParam( "key" ) String encryptedValue, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    try
    {
      String value = encryptionService.getDecryptedValue( ENCRYPTION_PREFIX + encryptedValue );
      Long userId = Long.parseLong( value );
      userService.lockUserAccount( userId );
    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }
    response.sendRedirect( buildRedirectAccountLock() );
  }

  private String buildRedirect( boolean accountLocked )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    sb.append( "/login.do?accountLock=true&key=true&isEmail=true" );
    return sb.toString();
  }

  private String accountLockConfirm( Long userId )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    String encryptedId = encryptionService.getEncryptedValue( String.valueOf( userId ) );
    sb.append( "/login.do?accountLockConfirm=true&key=" + encryptedId.replace( "{AES}", "" ) );
    return sb.toString();
  }

  private String buildRedirectAccountLock()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    sb.append( "/login.do?accountLock=true&key=false&isEmail=true" );
    return sb.toString();
  }
}
