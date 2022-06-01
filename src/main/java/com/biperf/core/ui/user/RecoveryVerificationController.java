
package com.biperf.core.ui.user;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.exception.ExceptionView;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

@Controller
@RequestMapping( "/recoveryverification/v1" )
public class RecoveryVerificationController extends BaseUserController
{

  private @Autowired UserService userService;


  /**
   * Sends a notification with a verification code
   */
  @RequestMapping( value = "/sendRecoveryCode.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<ExceptionView> sendRecoveryCode( @RequestBody RecoveryVerificationModel model )
      throws ServiceErrorException
  {
    try
    {
      Long userId = UserManager.getUserId();
      ContactType contactType = buildContactType( model );
      String contactValue = userService.sendRecoveryVerificationMessage( userId, contactType, model.getEmailOrPhone() );

      List<String> messages = new ArrayList<>();
      String message = "";
      if ( ContactType.EMAIL.equals( contactType ) )
      {
        message = buildCMSMessage( "profile.preference.tab.VERIFICATION_SENT_EMAIL" );
      }
      else if ( ContactType.PHONE.equals( contactType ) )
      {
        message = buildCMSMessage( "profile.preference.tab.VERIFICATION_SENT_PHONE" );
      }
      
      if ( UserManager.isUserDelegateOrLaunchedAs() )
      {
        if ( ContactType.EMAIL.equals( contactType ) )
        {
          contactValue = StringUtil.maskEmailAddress( contactValue );
        }
        else if ( ContactType.PHONE.equals( contactType ) )
        {
          contactValue = StringUtil.maskPhoneNumber( contactValue );
        }
      }
      
      message = MessageFormat.format( message, contactValue );
      messages.add( message );

      return buildResponse( new ExceptionView(), messages, HttpStatus.OK );
    }
    catch( ServiceErrorException e )
    {
      return buildResponse( new ExceptionView(), e.getServiceErrorsCMText(), HttpStatus.BAD_REQUEST );
    }
  }
  
  /**
   * Submit a verification code to verify a recovery contact method
   */
  @RequestMapping( value = "/verifyRecoveryCode.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<ExceptionView> sendVerificationCode( @RequestBody RecoveryVerificationModel model )
      throws ServiceErrorException
  {
    try
    {
      Long userId = UserManager.getUserId();
      ContactType contactType = buildContactType( model );

      boolean verifySuccess = userService.verifyRecoveryCode( userId, model.getToken(), contactType );

      if ( verifySuccess )
      {
        List<String> messages = new ArrayList<>();
        messages.add( buildCMSMessage( "profile.preference.tab.RECOVERY_VERIFICATION_SUCCESS" ) );
        return buildResponse( new ExceptionView(), messages, HttpStatus.OK );
      }
      else
      {
        List<String> messages = new ArrayList<>();
        messages.add( buildCMSMessage( "profile.preference.tab.RECOVERY_VERIFICATION_FAILED" ) );
        return buildResponse( new ExceptionView(), messages, HttpStatus.BAD_REQUEST );
      }
    }
    catch( ServiceErrorException e )
    {
      return buildResponse( new ExceptionView(), e.getServiceErrorsCMText(), HttpStatus.BAD_REQUEST );
    }
  }
  
  private ContactType buildContactType( RecoveryVerificationModel model )
  {
    return model.getContactType().equalsIgnoreCase( ContactType.PHONE.toString() ) ? ContactType.PHONE : ContactType.EMAIL;
  }

}
