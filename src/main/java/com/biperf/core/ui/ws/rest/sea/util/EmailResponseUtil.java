
package com.biperf.core.ui.ws.rest.sea.util;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.ws.rest.sea.BaseService;
import com.biperf.core.ui.ws.rest.sea.dto.EmailMessageBody;
import com.biperf.core.ui.ws.rest.sea.dto.RecognitionRequestView;
import com.biperf.core.ui.ws.rest.sea.dto.RecognitionResponseView;

public class EmailResponseUtil extends BaseService
{
  private static final Log logger = LogFactory.getLog( EmailResponseUtil.class );

  private static String ONE_LINE_BREAK = "\r\n";
  private static String TWO_LINE_BREAK = "\r\n\r\n";
  private static String WHITE_SPACE = " ";
  private static String TAB = "\t";
  private static String TAB_BULLET = "\t- ";

  private void buildHeader( RecognitionRequestView request, RecognitionResponseView response ) 
  {
    if ( StringUtils.isEmpty( response.getLastName() ) )
    {
      response.getMessages().add( MessageService.SEA_EMAIL_DEAR_CUSTOMER_MESSAGE );
    }
    else
    {
      response.getMessages().add( MessageService.SEA_EMAIL_DEAR_MESSAGE );
    }
  }
  /*
   * This method should evaluate ALL the conditionals, if any don't pass, it is considered invalid.
   */
  private boolean isValid( RecognitionRequestView request, RecognitionResponseView response )
  {
    if( !isCorrectable( request, response ) )
      return false ;
    if( response.isBehaviorRequired() && response.isInvalidBehavior() )
      return false ;
    if( !response.isEmailEnabledPromotion() )
      return false ;
    if( !response.isEndTagFound() )
      return false ;
    if( response.isFixedPointsDoesntMatch() )
      return false ;
    if( response.isMissingBehavior() )
      return false ;
    if( response.isNoBudget() )
      return false ;
    if( response.isNotEnoughBudget() )
      return false ;
    if( response.isInvalidBehavior() )
      return false ;
    if( response.isPointsOutOfRange() )
      return false ;
    if( response.isMessageEmpty() )
      return false ;
    return true;
  }
  
  /*
   * This method will inspect the conditionals and determine if any of the "failures" are NOT CORRECTABLE.
   */
  private boolean isCorrectable( RecognitionRequestView request, RecognitionResponseView response )
  {
    if( response.isSenderNotFound() )
      return false ;
    if( !response.isEmailEnabledPromotion() )
      return false ;
    if( response.isSenderNotInAudience() )
      return false ;
    if( response.isSenderNotUnique() ) 
      return false ;
    if ( request.getRecipientEmails().size() == 1 && response.isSameSenderAndRecipient() )
      return false;
    if ( response.getValidRecipients().isEmpty() )
      return false;
    if ( response.isPointsAvailableToPromotion() && response.isPointsAvailableToEmail() && !response.getInvalidRecipients().isEmpty() )
      return false;
//    if( !response.getRecipientsNotInAudience().isEmpty() )
//      return false ;
//    if( !response.getRecipientsNotUnique().isEmpty() )
//      return false ;
    
    return true;
  }
  
  private void buildFooter( RecognitionRequestView request, RecognitionResponseView response ) 
  {
    //response.getMessages().add( MessageService.SEA_EMAIL_FOOTER );
    //response.getMessages().add( TWO_LINE_BREAK );
  }
  
  private void buildFooterWithPromotionName( RecognitionRequestView request, RecognitionResponseView response ) 
  {
    //buildFooter( request, response ) ;
    //response.getMessages().add( response.getPromotionName() );
  }
    
  public void buildSubmitRecognitionMessageBodyContent( RecognitionRequestView request, RecognitionResponseView response )
  {
    buildHeader( request, response );
    if ( isValid( request, response ) ) // all good
    {
      if ( response.getInvalidRecipients().isEmpty() && response.getRecipientsNotInAudience().isEmpty() && response.getRecipientsNotUnique().isEmpty() )
      {
        // all recipients are valid
        addNotifyMessage( response );
      }
      else
      {
        response.getMessages().add( TWO_LINE_BREAK );
        response.getMessages().add( TAB_BULLET );
        // if points added in the email message
        if( response.isPointsAvailableToEmail() )
        {
          response.getMessages().add( MessageService.SEA_EMAIL_NON_FOUND_RECIPIENTS_MESSAGE );
          response.getMessages().add( ONE_LINE_BREAK );
          printRecipientInfoAsList( response, response.getInvalidRecipients() );
        }
        else
        {
          response.getMessages().add( MessageService.SEA_EMAIL_VALID_RECIPIENT_MESSAGE );
          response.getMessages().add( ONE_LINE_BREAK );
          printRecipientInfoAsList( response, response.getValidRecipients() );
        }
      }
      buildFooterWithPromotionName( request, response );
    }
    else if ( isCorrectable( request, response ) ) // construct the correctable email
    {
      buildCorrectableMessages( request, response, false );
    }
    else
    {
      buildNotCorrectableMessages( request, response );
    }
    buildEmail( request, response );
  }

  private void addNotifyMessage(RecognitionResponseView response)
  {
    response.getMessages().add( TWO_LINE_BREAK );
    response.getMessages().add( MessageService.SEA_EMAIL_BOX_RECEIVED_RECOGNITION_MESSAGE );
    response.getMessages().add( TWO_LINE_BREAK );
    response.getMessages().add( MessageService.SEA_EMAIL_NO_REPLY_RECEIVED_MESSAGE );
    response.getMessages().add( TWO_LINE_BREAK );
  }
  
  public void buildConfirmRecognitionMessageBodyContent( RecognitionRequestView request, RecognitionResponseView response )
  {
    buildHeader( request, response ) ;
    response.getMessages().add( TWO_LINE_BREAK );
    
    if( response.getCode()==1 )  // recognitions successfully sent (at least 1)
    {
      response.getMessages().add( MessageService.SEA_EMAIL_CONFIRMATION_TO_ISSUE_POINT_MESSAGE );  // send message telling user recognition(s) are posted!
    }
    else if ( response.getCode()==2 )// validation still failed even after response
    {
      response.getMessages().add( TWO_LINE_BREAK );
      response.getMessages().add( MessageService.SEA_EMAIL_NOT_CORRECTABLE_ERROR_SUFFIX );
    }
    else // unknown error
    {
      response.getMessages().add( TWO_LINE_BREAK );
      response.getMessages().add( MessageService.SEA_EMAIL_SYSTEM_DOWN );
    }
    
    response.getMessages().add( TWO_LINE_BREAK );
    buildFooterWithPromotionName( request, response ) ;
    buildEmail( request, response );
  }

  private void buildCorrectableMessages( RecognitionRequestView request, RecognitionResponseView response, boolean isConfirmationProcess )
  {
    response.getMessages().add( TWO_LINE_BREAK );
    response.getMessages().add( MessageService.SEA_EMAIL_CORRECTABLE_ERROR_PREFIX );
    response.getMessages().add( TWO_LINE_BREAK );
    
    // budget/points issues...
    invalidPoints( response, false ) ;
    // recipient issues
    invalidRecipientsMessage( request, response );
    // behavior issues..
    invalidBehaviorsMessage( request, response ) ;
    // end tag issues...
    invalidEndTag( request, response ) ;
    // check for an empty message...
    invalidMessage( request, response, isConfirmationProcess ) ;
    
    response.getMessages().add( MessageService.SEA_EMAIL_CORRECTABLE_ERROR_SUFFIX );
    response.getMessages().add( TWO_LINE_BREAK );
    // standard response time awarning
    response.getMessages().add( MessageService.SEA_EMAIL_NO_REPLY_RECEIVED_MESSAGE );
    response.getMessages().add( TWO_LINE_BREAK );
    // standard footer
    buildFooterWithPromotionName( request, response ) ;
  }
  
  private void buildNotCorrectableMessages( RecognitionRequestView request, RecognitionResponseView response )
  {
    response.getMessages().add( TWO_LINE_BREAK );
    response.getMessages().add( MessageService.SEA_EMAIL_NOT_CORRECTABLE_ERROR_PREFIX );
    response.getMessages().add( TWO_LINE_BREAK );
    if( !response.isEmailEnabledPromotion() )
    {
      response.getMessages().add( MessageService.SEA_NO_EMAIL_ENABLED_PROMOTION );
      response.getMessages().add( TWO_LINE_BREAK );
      return ;
    }
    // sender issues
    invalidSenderMessage( response );
    // recipient issues
    invalidRecipientsMessage( request, response );
    // footer
    // response.getMessages().add( TWO_LINE_BREAK ) ;
    response.getMessages().add( MessageService.SEA_EMAIL_NOT_CORRECTABLE_ERROR_SUFFIX ) ;
    response.getMessages().add( TWO_LINE_BREAK ) ;
  }
  
  // non-correctable
  private void invalidSenderMessage( RecognitionResponseView response )
  {
    if ( response.isSenderNotFound() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_EMAIL_INVALID_MESSAGE );
      response.getMessages().add( TWO_LINE_BREAK );
    }
    else if ( response.isSenderNotUnique() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_EMAIL_FOR_MULTIPLE_PERSON_MESSAGE );
      response.getMessages().add( TWO_LINE_BREAK );
    }
    else if ( response.isSenderNotInAudience() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_EMAIL_INELIGIBLE_FOR_ISSUE_PROMO_MESSAGE );
      response.getMessages().add( TWO_LINE_BREAK );
    }
  }
  
  private void invalidEndTag( RecognitionRequestView request, RecognitionResponseView response )
  {
    if( !response.isEndTagFound() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_EMAIL_MISSING_END_TAG );
      response.getMessages().add( TWO_LINE_BREAK );
    }
  }
  
  private void invalidMessage( RecognitionRequestView request, RecognitionResponseView response, boolean confirmProcess )
  {
    if( response.isMessageEmpty() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_EMPTY_BODY_MESSAGE );
      response.getMessages().add( TWO_LINE_BREAK );
    }
  }

  private void invalidRecipientsMessage( RecognitionRequestView request, RecognitionResponseView response )
  {
    // no email recipient available
    if ( request.getRecipientEmails().size() == 0 )
    {
      response.getMessages().add( TAB_BULLET );
      response.getMessages().add( MessageService.SEA_EMAIL_NO_RECIPIENT_INCLUDED );
      response.getMessages().add( ONE_LINE_BREAK );
    }
    // one email recipient a
    else if ( request.getRecipientEmails().size() == 1 )
    {
      // self recognition not allowed
      if ( request.getRecipientEmails().iterator().next().equalsIgnoreCase( request.getSenderEmail() ) )
      {
        response.getMessages().add( TAB_BULLET );
        response.getMessages().add( MessageService.SEA_EMAIL_SELF_RECOGNITION_NOT_ALLOWED );
        response.getMessages().add( ONE_LINE_BREAK );
      }
      // message for not unique
      else if ( !response.getRecipientsNotUnique().isEmpty() )
      {
        response.getMessages().add( TAB_BULLET );
        response.getMessages().add( MessageService.SEA_EMAIL_RECIPIENT_NOT_UNIQUE );
        response.getMessages().add( ONE_LINE_BREAK );
        printRecipientInfoAsList( response, response.getRecipientsNotUnique() );
      }
      // message for not in audience
      else if ( !response.getRecipientsNotInAudience().isEmpty() )
      {
        response.getMessages().add( TAB_BULLET );
        response.getMessages().add( MessageService.SEA_EMAIL_INELIGIBLE_TO_RECEIVE_RECOGNITION_MESSAGE );
        response.getMessages().add( ONE_LINE_BREAK );
        printRecipientInfoAsList( response, response.getRecipientsNotInAudience() );
      }
      // message for invalid/not found email addresses
      else if ( !response.getInvalidRecipients().isEmpty() )
      {
        response.getMessages().add( TAB_BULLET );
        response.getMessages().add( MessageService.SEA_EMAIL_NON_FOUND_RECIPIENTS_MESSAGE );
        response.getMessages().add( ONE_LINE_BREAK );
        printRecipientInfoAsList( response, response.getInvalidRecipients() );
      }
    }
    else
    {
      // message for not unique
      if ( !response.getRecipientsNotUnique().isEmpty() )
      {
        response.getMessages().add( TAB_BULLET );
        response.getMessages().add( MessageService.SEA_EMAIL_RECIPIENT_NOT_UNIQUE );
        response.getMessages().add( ONE_LINE_BREAK );
        printRecipientInfoAsList( response, response.getRecipientsNotUnique() );
      }
      
      // points available in recognition
      if ( response.isPointsAvailableToPromotion() && response.isPointsAvailableToEmail() )
      {
        // points available for recognition
        // message for not in audience
        if ( !response.getRecipientsNotInAudience().isEmpty() )
        {
          response.getMessages().add( TAB_BULLET );
          response.getMessages().add( MessageService.SEA_EMAIL_INELIGIBLE_TO_RECEIVE_RECOGNITION_MESSAGE );
          response.getMessages().add( ONE_LINE_BREAK );
          printRecipientInfoAsList( response, response.getRecipientsNotInAudience() );
        }
        // message for invalid/not found email addresses
        if ( !response.getInvalidRecipients().isEmpty() )
        {
          response.getMessages().add( TAB_BULLET );
          response.getMessages().add( MessageService.SEA_EMAIL_NON_FOUND_RECIPIENTS_MESSAGE );
          response.getMessages().add( ONE_LINE_BREAK );
          printRecipientInfoAsList( response, response.getInvalidRecipients() );
        }
      }
    }
  }

  private void printRecipientInfoAsList( RecognitionResponseView response, java.util.List<String> recipients )
  {
    for ( String recipient : recipients )
    {
      response.getMessages().add( TAB + TAB + recipient + ONE_LINE_BREAK );
    }
    response.getMessages().add( TWO_LINE_BREAK );
  }
  
  private void invalidPoints( RecognitionResponseView response, boolean confirmProcess )
  {
    if ( response.isNotEnoughBudget() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_EMAIL_NOT_ENOUGH_POINT_MESSAGE );
      response.getMessages().add( WHITE_SPACE );
      response.getMessages().add( MessageService.SEA_EMAIL_POINT_BALANCE_MESSAGE );
      response.getMessages().add( TWO_LINE_BREAK );
      //adjustPointsMessage( response, confirmProcess ) ;
    } 
    if ( response.isFixedPointsDoesntMatch() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_EMAIL_SEND_FIXEDPOINT );
      response.getMessages().add( TWO_LINE_BREAK );
      //adjustPointsMessage( response, confirmProcess ) ;
    }
    if ( response.isPointsOutOfRange() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_EMAIL_OUTOF_POINT_RANGE );
      response.getMessages().add( TWO_LINE_BREAK );
      //adjustPointsMessage( response, confirmProcess ) ;    
    }
    if ( response.isNoBudget() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_EMAIL_OUT_OF_BUDGET_PROMOTION_MESSAGE );
      response.getMessages().add( TWO_LINE_BREAK );
    }
    // points not enabled for public recog promo and sender entered points in email
    if ( !response.isPointsAvailableToPromotion() && response.isPointsAvailableToEmail() && !confirmProcess )
    {
      response.getMessages().add( TAB_BULLET );
      response.getMessages().add( MessageService.SEA_EMAIL_NO_PUBLIC_RECOGNITION );
      response.getMessages().add( TWO_LINE_BREAK );
    }
  }

  private void adjustPointsMessage( RecognitionResponseView response, boolean confirmProcess )
  {
    response.getMessages().add( WHITE_SPACE );
    if ( !confirmProcess )
    {
      response.getMessages().add( MessageService.SEA_EMAIL_STILL_WISH_ADJUST_POINTS );
      response.getMessages().add( TWO_LINE_BREAK );
    }
  }
  
  private void invalidBehaviorsMessage( RecognitionRequestView request, RecognitionResponseView response )
  {
    if ( response.isMissingBehavior() )
    {
      response.getMessages().add( TAB_BULLET ) ;
      response.getMessages().add( MessageService.SEA_SYSTEM_REQUIRE_BEHAVIOUR );
      response.getMessages().add( WHITE_SPACE );
      response.getMessages().add( buildValidBehaviorsList( response ) ) ;
      response.getMessages().add( TWO_LINE_BREAK );
    }
    if ( response.isInvalidBehavior() )
    {
      if ( response.getValidBehaviors().size() > 1 )
      {
        response.getMessages().add( TAB_BULLET ) ;
        response.getMessages().add( MessageService.SEA_EMAIL_INVALID_HASHTAG_MESSAGE );
        response.getMessages().add( buildValidBehaviorsList( response ) ) ;
        response.getMessages().add( TWO_LINE_BREAK );
      }
      else
      {
        response.getMessages().add( TAB_BULLET ) ;
        response.getMessages().add( MessageService.SEA_BEHAVIOR_INACTIVE );
        response.getMessages().add( ONE_LINE_BREAK );
      }
    }
  }
  
  // Don't touch this method. If edited, please also modify HashTagParser.java line number 54 in SEA. 
  private String buildValidBehaviorsList( RecognitionResponseView response )
  {
    StringBuilder sb = new StringBuilder() ;
    sb.append( "  \n\r"+TAB+TAB+"|" ) ;
    for ( int i=0; i<response.getValidBehaviors().size();i++ )
    {
      sb.append( "#" + response.getValidBehaviors().get( i ) ) ;
      if( i<( response.getValidBehaviors().size()-1 ) )
      {
        sb.append( ", " ) ;
      }
    }
    sb.append( "|" ) ;
    return sb.toString() ;
  }
  
  @SuppressWarnings( "rawtypes" )
  private void buildEmail( RecognitionRequestView request, RecognitionResponseView response )
  {
    StringBuilder plainText = new StringBuilder();
    StringBuilder htmlText = new StringBuilder();
    Map map = buildKeys( request, response );
    
    for ( String message : response.getMessages() )
    {
      Message cmMessage = ( !ONE_LINE_BREAK.equals( message ) && !TWO_LINE_BREAK.equals( message ) && !WHITE_SPACE.equals( message ) ) ? getMessageService().getMessageByCMAssetCode( message ) : null;
      plainText.append( cmMessage != null ? cmMessage.getI18nPlainTextBody() : message );
      htmlText.append( cmMessage != null ? cmMessage.getI18nHtmlBody() : message );
    }
    
    EmailMessageBody emailMessageBody = new EmailMessageBody();
    
    emailMessageBody.setPlainText( personalize( map, plainText.toString() ) );
    emailMessageBody.setHtmlText( personalize( map, htmlText.toString() ) );
    response.setEmailMessageBody( emailMessageBody );
  }

  @SuppressWarnings( "rawtypes" )
  private Map buildKeys( RecognitionRequestView request, RecognitionResponseView response )
  {
    Map<String, String> map = new HashMap<>();

    map.put( "firstName", response.getFirstName() );
    map.put( "lastName", response.getLastName() );
    map.put( "programName", response.getProgramName() );
    map.put( "budgetPromotionName", response.getBudgetPromotionName() );
    map.put( "programEmail", response.getProgramEmailAddress() );
    map.put( "promotionName", response.getPromotionName() );
    map.put( "daysToAbandoned", Integer.toString( getSystemVariableService().getPropertyByName( SystemVariableService.SEA_DAYS_TO_ABANDONED ).getIntVal() ) );
    map.put( "points", Integer.toString( request.getPoints() ) );
    map.put( "senderEmail", request.getSenderEmail() );
    if ( request.getRecipientEmails() != null && request.getRecipientEmails().size() > 0 )
    {
      map.put( "recipientEmail", (String)request.getRecipientEmails().iterator().next() );
    }
    map.put( "validRecipientsSize", response.getValidRecipients() != null & response.getValidRecipients().size() > 0 ? Integer.toString( response.getValidRecipients().size() ) : "" );
    map.put( "fixedPoints", Integer.toString( response.getFixedPoints() ) );
    map.put( "pointRangeMin", Integer.toString( response.getPointRangeMin() ) );
    map.put( "pointRangeMax", Integer.toString( response.getPointRangeMax() ) );
    
    // media type bug fix.
    String pointsAvailable = Integer.toString( response.getPointsAvailable() ) + " " + PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getName();
    map.put( "pointsAvailable", pointsAvailable );
    
    map.put( "now", new SimpleDateFormat( "EEEE MMM dd, yyyy hh:mm a" ).format( new Date() ) );
    map.put( "behavior", request.getBehaviorHashTag().getName() );

    return map;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private String personalize( Map map, String targetString )
  {
    if ( targetString == null )
    {
      return "";
    }

    Map objectMap = new HashMap();
    Iterator it = map.entrySet().iterator();

    while ( it.hasNext() )
    {
      Map.Entry pairs = (Map.Entry)it.next();
      String key = (String)pairs.getKey();

      if ( key != null )
      {
        int firstBracketIndex = key.lastIndexOf( "[" );
        int lastBracketIndex = key.lastIndexOf( "]" );
        int lastCharacterPosition = ( key.length() - 1 );

        if ( ( firstBracketIndex > -1 ) && ( lastBracketIndex > -1 ) && ( lastBracketIndex > firstBracketIndex ) && ( lastBracketIndex == lastCharacterPosition ) )
        {
          int index = 0;
          boolean validIndex = true;

          try
          {
            index = Integer.parseInt( key.substring( firstBracketIndex + 1, lastBracketIndex ) );
          }
          catch( NumberFormatException numberFormatException )
          {
            validIndex = false;
          }

          if ( validIndex )
          {
            String realKey = key.substring( 0, firstBracketIndex );
            if ( objectMap.containsKey( realKey ) )
            {
              TreeMap treeMap = (TreeMap)objectMap.get( realKey );
              treeMap.put( new Integer( index ), pairs.getValue() );
            }
            else
            {
              TreeMap treeMap = new TreeMap();
              treeMap.put( new Integer( index ), pairs.getValue() );
              objectMap.put( realKey, treeMap );
            }
          }
        }

        String value = (String)pairs.getValue();

        if ( value == null )
        {
          value = "";
        }
        objectMap.put( pairs.getKey(), value );
      }
    }
    return processMessage( objectMap, "SEA Email Response", targetString );
  }

  @SuppressWarnings( "rawtypes" )
  private String processMessage( Map objectMap, String logString, String targetString )
  {
    Context velocityContext = new VelocityContext( objectMap );
    StringWriter stringWriter = new StringWriter();

    try
    {
      VelocityEngine ve = new VelocityEngine();
      ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.SimpleLog4JLogSystem" );
      ve.setProperty( "runtime.log.logsystem.log4j.category", EmailResponseUtil.class.getName() );
      ve.init();

      if ( ve.evaluate( velocityContext, stringWriter, logString, targetString ) )
      {
        return stringWriter.toString();
      }
    }
    catch( Exception e )
    {
      logger.error( "Error while evaluating Velocity text : " + targetString );
      return targetString;
    }

    return targetString;

  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

}
