
package com.biperf.core.service.email.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.service.email.PersonalizationService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.util.StringUtils;

/**
 * PersonalizationService
 * 
 *
 */
public class PersonalizationServiceImpl implements PersonalizationService
{

  private SystemVariableService systemVariableService;

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  private static final Log logger = LogFactory.getLog( PersonalizationServiceImpl.class );

  /**
   * Process mailing level data
   * 
   * @param mailing
   * @param objectMap This is the map of objects that will be used to populate the mailing level
   *          dynamic elements in the message. Format for the key is $<keyName>. Value can be
   *          String or a vector of values
   * @return Mailing
   */
  public Mailing preProcessMailing( Mailing mailing, Map objectMap )
  {

    try
    {
      Set locales = new HashSet();
      String canAddGeneralData = null;
      Set userEmailAddress = new HashSet();

      if ( objectMap != null && objectMap.get( "canAddGeneralData" ) != null )
      {
        canAddGeneralData = "true";
      }
      if ( !mailing.getMailingType().getCode().equals( MailingType.EMAIL_WIZARD )
          && ! ( mailing.getMailingType().getCode().equals( MailingType.EMAIL_WIZARD_PREVIEW ) && StringUtils.isEmpty( canAddGeneralData ) ) )
      {
        Set mailingRecipients = mailing.getMailingRecipients();
        Iterator mailingRecipientsIter = mailingRecipients.iterator();

        while ( mailingRecipientsIter.hasNext() )
        {
          MailingRecipient mailingRecipient = (MailingRecipient)mailingRecipientsIter.next();
          if ( StringUtils.isEmpty( mailingRecipient.getLocale() ) )
          {
            mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
          }
          locales.add( mailingRecipient.getLocale() );
        }
      }

      return loadMessageData( objectMap, mailing, locales );
    }
    catch( Exception e )
    {
      logger.error( "An error occured method preProcessMailing." + "For mailingId: " + mailing.getId(), e );
      return null;
    }

  }

  /**
   * Personalize input string (target) given a mailingRecipient which has recipient specific data.
   * 
   * @param mailingRecipient
   * @param targetString
   * @return String
   */
  public String personalize( MailingRecipient mailingRecipient, String targetString )
  {
    return personalize( mailingRecipient, targetString, false );
  }

  public String personalize( MailingRecipient mailingRecipient, String targetString, boolean maskSecureFields )
  {
    if ( targetString == null )
    {
      return "";
    }

    Set mailingRecipientDataSet = mailingRecipient.getMailingRecipientDataSet();
    Map objectMap = new HashMap();

    Iterator mailingRecipientDataSetIter = mailingRecipientDataSet.iterator();
    while ( mailingRecipientDataSetIter.hasNext() )
    {

      MailingRecipientData mailingRecipientData = (MailingRecipientData)mailingRecipientDataSetIter.next();

      String key = mailingRecipientData.getKey();

      if ( key != null )
      {

        int firstBracketIndex = key.lastIndexOf( "[" );
        int lastBracketIndex = key.lastIndexOf( "]" );
        int lastCharacterPosition = key.length() - 1;

        if ( firstBracketIndex > -1 && lastBracketIndex > -1 && lastBracketIndex > firstBracketIndex && lastBracketIndex == lastCharacterPosition )
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
              treeMap.put( new Integer( index ), mailingRecipientData.getValue() );
            }
            else
            {
              TreeMap treeMap = new TreeMap();
              treeMap.put( new Integer( index ), mailingRecipientData.getValue() );
              objectMap.put( realKey, treeMap );
            }
          }
        }

        String mailingRecipientDataValue = mailingRecipientData.getValue();

        if ( mailingRecipientDataValue == null )
        {
          mailingRecipientDataValue = "";
        }

        objectMap.put( mailingRecipientData.getKey(), mailingRecipientDataValue );

      }
    }
    return processMessage( objectMap, "personalization - recipient specific data", targetString, maskSecureFields );
  }

  /**
   * Load the dynamic message data for
   * 
   * @param objectMap
   * @param mailing
   * @param locales
   * @return Mailing
   */
  private Mailing loadMessageData( Map objectMap, Mailing mailing, Set locales )
  {

    try
    {
      if ( mailing.getMailingType().getCode().equals( MailingType.EMAIL_WIZARD_PREVIEW ) && objectMap == null )
      {
        return mailing;
      }

      // E-mail wizard is seperated out because personalization needs to happen
      // but is not loaded from message (like other mailings)!
      if ( mailing.getMailingType().getCode().equals( MailingType.EMAIL_WIZARD ) )
      {

        MailingMessageLocale adHocMailingMessageLocale = (MailingMessageLocale)mailing.getMailingMessageLocales().iterator().next();

        // need to run pre-personalization process on the ad-hoc messages
        adHocMailingMessageLocale.setSubject( loadMailingLevelData( objectMap, adHocMailingMessageLocale.getSubject() ) );

        adHocMailingMessageLocale.setHtmlMessage( loadMailingLevelData( objectMap, adHocMailingMessageLocale.getHtmlMessage() ) );

        adHocMailingMessageLocale.setPlainMessage( loadMailingLevelData( objectMap, adHocMailingMessageLocale.getPlainMessage() ) );

        adHocMailingMessageLocale.setTextMessage( loadMailingLevelData( objectMap, adHocMailingMessageLocale.getTextMessage() ) );

      }
      else if ( objectMap == null )
      {
        // iterate over locales and build general data
        Iterator localesIter = locales.iterator();

        while ( localesIter.hasNext() )
        {
          String locale = (String)localesIter.next();

          MailingMessageLocale mailingMessageLocale = new MailingMessageLocale();
          mailingMessageLocale.setLocale( locale );
          Message message = mailing.getMessage();

          mailingMessageLocale.setSubject( message.getI18nSubject( locale ) );

          mailingMessageLocale.setHtmlMessage( message.getI18nHtmlBody( locale ) );

          mailingMessageLocale.setPlainMessage( message.getI18nPlainTextBody( locale ) );

          mailingMessageLocale.setTextMessage( message.getI18nTextBody( locale ) );

          mailing.addMailingMessageLocale( mailingMessageLocale );
        }

      }
      else
      {

        // iterate over locales and build general data
        Iterator localesIter = locales.iterator();
        while ( localesIter.hasNext() )
        {
          String locale = (String)localesIter.next();

          MailingMessageLocale mailingMessageLocale = new MailingMessageLocale();
          mailingMessageLocale.setLocale( locale );
          Message message = mailing.getMessage();

          mailingMessageLocale.setSubject( loadMailingLevelData( objectMap, message.getI18nSubject( locale ) ) );

          mailingMessageLocale.setHtmlMessage( loadMailingLevelData( objectMap, message.getI18nHtmlBody( locale ) ) );

          mailingMessageLocale.setPlainMessage( loadMailingLevelData( objectMap, message.getI18nPlainTextBody( locale ) ) );

          mailingMessageLocale.setTextMessage( loadMailingLevelData( objectMap, message.getI18nTextBody( locale ) ) );
          if ( objectMap != null && objectMap.get( "canAddGeneralData" ) != null )
          {
            refreshMailingMessageLocales( mailing, mailingMessageLocale );

          }
          else
          {
            mailing.addMailingMessageLocale( mailingMessageLocale );
          }
        }
      }
      return mailing;
    }
    catch( Exception e )
    {
      logger.error( "An error occured method loadMessageData." + "For mailingId: " + mailing.getId(), e );
      return null;
    }

  }

  /**
   * Load non specific participant message data
   * 
   * @param objectMap Objects containing data to load from - no participant specific included
   * @param targetString
   * @return String
   */
  private String loadMailingLevelData( Map objectMap, String targetString )
  {
    if ( targetString == null )
    {
      return "";
    }

    String result = targetString;

    if ( objectMap != null )
    {

      Set keySet = objectMap.keySet();

      Iterator keySetIter = keySet.iterator();
      while ( keySetIter.hasNext() )
      {
        String pattern = (String)keySetIter.next();
        Object value = objectMap.get( pattern );
        if ( value == null )
        {
          value = new String( "" );
        }
        result = this.replaceForPattern( pattern, value.toString(), result );

      }

    }
    return result;

  }

  /**
   * Process message using velocity
   * 
   * @param objectMap Objects containing data to load from
   * @param logString
   * @param targetString
   * @return String
   */
  public String processMessage( Map objectMap, String logString, String targetString )
  {
    return processMessage( objectMap, logString, targetString, false );
  }

  public String processMessage( Map objectMap, String logString, String targetString, boolean maskSecureFields )
  {

    if ( maskSecureFields )
    {
      maskSecureFields( objectMap );
    }

    Context velocityContext = new VelocityContext( objectMap );
    StringWriter stringWriter = new StringWriter();

    try
    {
      /*
       * Velocity.init(); Velocity.setProperty( "counter.initial.value", "0" ); //
       * Velocity.mergeTemplate("template", "UTF-8", velocityContext, stringWriter); if (
       * Velocity.evaluate( velocityContext, stringWriter, logString, targetString ) ) { return
       * stringWriter.toString(); }
       */

      VelocityEngine ve = new VelocityEngine();

      ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.SimpleLog4JLogSystem" );

      ve.setProperty( "runtime.log.logsystem.log4j.category", PersonalizationServiceImpl.class.getName() );

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

  private void maskSecureFields( Map objectMap )
  {
    objectMap.put( "password", "********" );
    objectMap.put( "giftCode", "********" );
  }

  private String replaceForPattern( String pattern, String value, String targetString )
  {

    String result = null;
    if ( value.contains( "$" ) )
    {
      value = new StringBuffer( value ).insert( value.indexOf( "$" ), "\\" ).toString();
    }

    // check first pattern
    Pattern compiledPattern = Pattern.compile( "\\$\\{" + pattern + "\\}" );
    Matcher matcher = compiledPattern.matcher( targetString );
    if ( matcher.find() )
    {
      result = matcher.replaceAll( value );
    }
    else
    {
      compiledPattern = Pattern.compile( "\\$" + pattern );
      matcher = compiledPattern.matcher( targetString );
      result = matcher.replaceAll( value );
    }

    if ( result == null )
    {
      return targetString;
    }

    return result;

  }

  /**
   * @param mailing
   * @param mailingMessageLocale
   * @return Mailing
   */
  public Mailing refreshMailingMessageLocales( Mailing mailing, MailingMessageLocale mailingMessageLocale )
  {
    mailingMessageLocale.setMailing( mailing );
    if ( mailing.getMailingMessageLocales() != null )
    {
      if ( mailing.getMailingMessageLocales().contains( mailingMessageLocale ) )
      {
        mailing.getMailingMessageLocales().remove( mailingMessageLocale );
        mailing.getMailingMessageLocales().add( mailingMessageLocale );
      }
    }

    return mailing;
  }

  /**
   * Bug # 51917
   * Since the injection is by lookup-method as defined in application context spring will
   * inject the bean. This method will be overridden by spring while creating the bean.
   * return null
   */
  public UserService getUserService()
  {
    return null;
  }

}
