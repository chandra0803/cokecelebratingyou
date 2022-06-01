
package com.biperf.core.ui;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.exception.ExceptionView;
import com.biperf.core.exception.FieldValidation;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.PromotionMenuBean;
import com.objectpartners.cms.util.CmsResourceBundle;

@Component
public abstract class SpringBaseController
{
  public static final String ELIGIBLE_PROMOTIONS = "eligiblePromotions";

  @Autowired
  private MainContentService mainContentService;

  @SuppressWarnings( "unchecked" )
  public List<PromotionMenuBean> getEligiblePromotions( HttpServletRequest request )
  {
    if ( UserManager.getUser().isParticipant() )
    {
      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)request.getSession().getAttribute( ELIGIBLE_PROMOTIONS );
      if ( null != eligiblePromotions )
      {
        return eligiblePromotions;
      }
      else
      {
        eligiblePromotions = mainContentService.buildEligiblePromoList( UserManager.getUser() );
        request.getSession().setAttribute( ELIGIBLE_PROMOTIONS, eligiblePromotions );
        return eligiblePromotions;
      }
    }
    return new ArrayList<PromotionMenuBean>();
  }

  public List<WebErrorMessage> buildAppExceptionMessage()
  {
    return buildCustomExceptionMessage( "system.errors.USER_FRIENDLY_SYSTEM_ERROR_MESSAGE" );
  }

  public List<WebErrorMessage> buildCustomExceptionMessage( String text )
  {
    WebErrorMessageList messageList = new WebErrorMessageList();

    WebErrorMessage errorMessage = new WebErrorMessage();
    errorMessage.setSuccess( false );
    errorMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    errorMessage.setText( text );
    messageList.getMessages().add( errorMessage );
    return messageList.getMessages();
  }

  public List<WebErrorMessage> buildSuccessMessage( String text )
  {
    WebErrorMessageList messageList = new WebErrorMessageList();

    WebErrorMessage errorMessage = new WebErrorMessage();
    errorMessage.setSuccess( true );
    errorMessage.setType( WebResponseConstants.RESPONSE_TYPE_SUCCESS );
    errorMessage.setText( text );
    messageList.getMessages().add( errorMessage );
    return messageList.getMessages();
  }

  public String buildCMSMessage( String key )
  {
    return CmsResourceBundle.getCmsBundle().getString( key );
  }

  public String getUserFriendlySystemMsg()
  {
    return buildCMSMessage( "system.errors.USER_FRIENDLY_SYSTEM_ERROR_MESSAGE" );
  }

  protected <T extends ExceptionView> ResponseEntity<T> buildResponse( ExceptionView view, HttpStatus httpStatus )
  {
    return buildResponse( view, null, httpStatus );
  }

  @SuppressWarnings( "unchecked" )
  protected <T extends ExceptionView> ResponseEntity<T> buildResponse( ExceptionView view, List<String> messages, HttpStatus httpStatus )
  {
    view.setResponseCode( httpStatus.value() );
    // Not a neat solution but spring 4 has better implementation to this. Checking if http status
    // code is a 2XX series (200-299)
    if ( httpStatus.value() / 100 == 2 )
    {
      // For a success status, there will be only one message which is a success message
      if ( messages != null )
      {
        view.setResponseMessage( messages.get( 0 ) );
      }
    }
    else
    {
      List<FieldValidation> fieldValidations = new ArrayList<FieldValidation>();
      messages.stream().forEach( m -> fieldValidations.add( new FieldValidation( null, m ) ) );
      view.setFieldErrors( fieldValidations );
    }
    return (ResponseEntity<T>)new ResponseEntity<ExceptionView>( view, httpStatus );
  }
  
  protected PageRedirectMessage buildPageRedirect( String url )
  {
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
    WebErrorMessage message = new WebErrorMessage();
    message.setCommand( "redirect" );
    message.setType( "serverCommand" );
    message.setCode( "SUCCESS" );
    message.setUrl( url );
    messages.add( message );
    return new PageRedirectMessage( messages );
  }
  
  protected class PageRedirectMessage
  {
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

    public PageRedirectMessage( List<WebErrorMessage> messages )
    {
      this.messages = messages;
    }

    public List<WebErrorMessage> getMessages()
    {
      return messages;
    }
  }
}
