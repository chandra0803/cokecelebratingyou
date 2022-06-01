/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/message/impl/MessageServiceImpl.java,v $
 */

package com.biperf.core.service.message.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.biperf.core.dao.message.MessageDAO;
import com.biperf.core.domain.enums.MessageStatusType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * MessageServiceImpl.
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
 * <td>robinsra</td>
 * <td>Sep 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageServiceImpl implements MessageService
{
  private MessageDAO messageDAO;
  private CMAssetService cmAssetService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.message.MessageService#getMessageById(java.lang.Long)
   * @param id
   * @return Message
   */
  public Message getMessageById( Long id )
  {
    return this.messageDAO.getMessageById( id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.message.MessageService#getMessageByCMAssetCode(java.lang.String)
   * @param cmAssetCode
   * @return Message
   */
  public Message getMessageByCMAssetCode( String cmAssetCode )
  {
    return messageDAO.getMessageByCMAssetCode( cmAssetCode );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.message.MessageService#getAll()
   * @return List
   */
  public List getAll()
  {
    return this.messageDAO.getAll();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.message.MessageService#getAllMessagesByStatus(java.lang.String)
   * @param statusCode
   * @return List of all messages for the status
   */
  public List getAllMessagesByStatus( String statusCode )
  {
    List messageList = null;
    if ( statusCode.equals( "all" ) )
    {
      messageList = getAll();
    }
    else if ( statusCode.equals( MessageStatusType.ACTIVE ) )
    {
      messageList = getAllActiveMessages();
    }
    else if ( statusCode.equals( MessageStatusType.INACTIVE ) )
    {
      messageList = getAllInactiveMessages();
    }
    return messageList;
  }

  /**
   * Gets a list of all message by status Overridden from
   * 
   * @see com.biperf.core.service.message.MessageService#getAllActiveMessages()
   * @return list
   */
  public List getAllActiveMessages()
  {
    return this.messageDAO.getAllActiveMessages();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.message.MessageService#getAllActiveTextMessages()
   * @return List of active messages that contain a text message
   */
  public List getAllActiveTextMessages()
  {
    List allActiveTextMsgs = new ArrayList();
    List allActiveMessages = getAllActiveMessages();
    Iterator msgIterator = allActiveMessages.iterator();
    while ( msgIterator.hasNext() )
    {
      Message message = (Message)msgIterator.next();
      String textMsg = nullCheckAndTrim( message.getI18nTextBody() );
      if ( !textMsg.equals( "" ) )
      {
        allActiveTextMsgs.add( message );
      }
    }
    return allActiveTextMsgs;
  }

  private String nullCheckAndTrim( String checkString )
  {
    if ( checkString == null )
    {
      checkString = "";
    }
    else
    {
      checkString = checkString.trim();
    }
    return checkString;
  }

  /**
   * Gets a list of all message by status Overridden from
   * 
   * @see com.biperf.core.service.message.MessageService#getAllInactiveMessages()
   * @return list
   */
  public List getAllInactiveMessages()
  {
    return this.messageDAO.getAllInactiveMessages();
  }

  /*
   * Checks to see if the message is a duplicate record based on the message name @param message
   * @return boolean
   */
  private boolean isDuplicateName( Message message )
  {
    // Check to see if the message already exists in the database with the same name.
    // calls getMessageByName to get a message record
    Message dbMessageByName = this.messageDAO.getMessageByName( message.getName() );

    if ( dbMessageByName != null )
    {
      // if we found a record in the database with the given name,
      // and our message ID is null, we are trying to insert a duplicate record.
      if ( message.getId() == null )
      {
        return true;
      }

      // if we found a record in the database with the given name, but the
      // ID's are not equal, we are trying to update to a message that already
      // exists.
      if ( dbMessageByName.getId().compareTo( message.getId() ) != 0 )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Set the MessageDAO through IoC
   * 
   * @param messageDAO
   */
  public void setMessageDAO( MessageDAO messageDAO )
  {
    this.messageDAO = messageDAO;
  }

  /**
   * Set the CMAssetService through IoC
   * 
   * @param cmAssetService
   */
  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.message.MessageService#saveMessage(com.biperf.core.domain.message.Message,
   *      java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   * @param message
   * @param subject
   * @param htmlMsg
   * @param plainTextMsg
   * @param textMsg
   * @return Message
   * @throws ServiceErrorException
   */
  public Message saveMessage( Message message, String subject, String htmlMsg, String plainTextMsg, String textMsg, String strongMailSubject, String strongMailMsg ) throws ServiceErrorException
  {
    return saveMessage( message, subject, htmlMsg, plainTextMsg, textMsg, strongMailSubject, strongMailMsg, ContentReaderManager.getCurrentLocale() );
  }

  public Message saveMessage( Message message, String subject, String htmlMsg, String plainTextMsg, String textMsg, String strongMailSubject, String strongMailMsg, Locale locale )
      throws ServiceErrorException
  {

    List serviceErrors = new ArrayList();

    // Check if there is a duplicate message name
    if ( isDuplicateName( message ) )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.MESSAGE_DUPLICATE_NAME );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors );
    }

    // Check if text message exceeds 100 characters
    if ( textMsg != null && textMsg.length() > 160 )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.MESSAGE_TEXT_TOO_LONG );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors );
    }

    if ( message.getId() == null )
    {
      // ---------------------
      // New message
      // --------------------
      // generate unique asset code
      message.setCmAssetCode( cmAssetService.getUniqueAssetCode( Message.CM_ASSET_CODE_PREFIX ) );
    }

    saveMessageCmText( message, subject, htmlMsg, plainTextMsg, textMsg, strongMailSubject, strongMailMsg, locale );

    // ----------------------------------
    // Save Message (Create or Update)
    // -----------------------------------
    message = this.messageDAO.saveMessage( message );

    return message;

  }

  public void saveMessageCmText( Message message, String subject, String htmlMsg, String plainTextMsg, String textMsg, String strongMailSubject, String strongMailMsg, Locale locale )
      throws ServiceErrorException
  {
    List cmDataElements = prepareCmDataElements( subject, htmlMsg, plainTextMsg, textMsg, strongMailSubject, strongMailMsg );

    // ------------------------------------
    // Create or Update CM Asset and Keys
    // -------------------------------------
    cmAssetService.createOrUpdateAsset( Message.CM_SECTION_CODE, Message.CM_ASSET_TYPE_NAME, message.getName() + Message.CM_ASSET_NAME_SUFFIX, message.getCmAssetCode(), cmDataElements, locale, null );
  }

  private List prepareCmDataElements( String subject, String htmlMsg, String plainTextMsg, String textMsg, String strongMailSubject, String strongMailMsg )
  {
    List cmDataElements = new ArrayList();
    CMDataElement cmDataElement;

    // ---------------
    // Setup CM Keys
    // ---------------
    // Subject
    cmDataElement = new CMDataElement( Message.CM_ASSET_TYPE_ITEM_NAME_SUBJECT, Message.CM_KEY_SUBJECT, subject, false, DataTypeEnum.TEMPLATESTRING );
    cmDataElements.add( cmDataElement );

    // htmlMsg
    cmDataElement = new CMDataElement( Message.CM_ASSET_TYPE_ITEM_NAME_HTML_MSG, Message.CM_KEY_HTML_MSG, htmlMsg, false, DataTypeEnum.TEMPLATEHTML );
    cmDataElements.add( cmDataElement );

    // plainTextMsg
    cmDataElement = new CMDataElement( Message.CM_ASSET_TYPE_ITEM_NAME_PLAIN_TEXT_MSG, Message.CM_KEY_PLAIN_TEXT_MSG, plainTextMsg, false, DataTypeEnum.TEMPLATESTRING );
    cmDataElements.add( cmDataElement );

    // textMsg
    cmDataElement = new CMDataElement( Message.CM_ASSET_TYPE_ITEM_NAME_TEXT_MSG, Message.CM_KEY_TEXT_MSG, textMsg, false, DataTypeEnum.TEMPLATESTRING );
    cmDataElements.add( cmDataElement );

    // strongMailSubject
    cmDataElement = new CMDataElement( Message.CM_ASSET_TYPE_ITEM_NAME_SM_SUBJECT, Message.CM_KEY_SM_SUBJECT, strongMailSubject, false, DataTypeEnum.TEMPLATESTRING );
    cmDataElements.add( cmDataElement );

    // strongMailMsg
    cmDataElement = new CMDataElement( Message.CM_ASSET_TYPE_ITEM_NAME_SM_MSG, Message.CM_KEY_SM_MSG, strongMailMsg, false, DataTypeEnum.TEMPLATESTRING );
    cmDataElements.add( cmDataElement );

    return cmDataElements;
  }

  public List getAllActiveMessagesByModuleType( String code )
  {
    return this.messageDAO.getAllActiveMessagesByModuleType( code );
  }

  public List getAllActiveMessagesByTypecode( String code )
  {

    return this.messageDAO.getAllActiveMessagesByTypecode( code );

  }

  /**
  * Overridden from
  * 
  * @see com.biperf.core.service.message.MessageService#getAllActiveTextMessages()
  * @return List of active messages that contain a text message
  */
  public List getAllActiveTextMessagesBySMSGroupType( String messageSMSGroupTypeCode )
  {

    return this.messageDAO.getAllActiveTextMessagesBySMSGroupType( messageSMSGroupTypeCode );
  }

}
