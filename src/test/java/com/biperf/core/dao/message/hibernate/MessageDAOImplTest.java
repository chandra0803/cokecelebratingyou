/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.message.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.message.MessageDAO;
import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.MessageStatusType;
import com.biperf.core.domain.enums.MessageType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * MessageDAOImplTest.
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
 * <td>tcheng</td>
 * <td>September 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds an active {@link Message} object.
   * 
   * @param uniqueString used to make the returned message unique.
   * @return a {@link Message} object.
   */
  public static Message buildMessage( String uniqueString )
  {
    Message message = new Message();

    message.setName( "MessageTest" + uniqueString );
    message.setModuleCode( MessageModuleType.lookup( MessageModuleType.PRODUCT_CLAIM ) );
    message.setStatusCode( MessageStatusType.lookup( "act" ) );
    message.setCmAssetCode( "CMAsset" + uniqueString );
    message.setDateLastSent( new Date() );
    message.setMessageTypeCode( MessageType.lookup( MessageType.GENERAL ) );

    return message;
  }

  /**
   * Builds an inactive {@link Message} object.
   * 
   * @param uniqueString used to make the returned message unique.
   * @return a {@link Message} object.
   */
  public static Message buildInactiveMessage( String uniqueString )
  {
    Message message = new Message();

    message.setName( "InactiveMessageTest" + uniqueString );
    message.setModuleCode( MessageModuleType.lookup( MessageModuleType.PRODUCT_CLAIM ) );
    message.setStatusCode( MessageStatusType.lookup( "ina" ) );
    message.setDateLastSent( new Date() );
    message.setCmAssetCode( "CMAsset" + uniqueString );
    message.setMessageTypeCode( MessageType.lookup( MessageType.GENERAL ) );

    return message;
  }

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests saving or updating the Message. This needs to fetch the Message by Id so it is also
   * testing MessageDAO.getMessageById(Long id).
   */
  public void testSaveAndGetMessageById()
  {
    MessageDAO messageDAO = getMessageDAO();

    // Create a Message object
    Message expectedMessage = buildMessage( getUniqueString() );

    // Save the message object
    messageDAO.saveMessage( expectedMessage );

    flushAndClearSession();

    // ---- 1. Try to fetch the record that was just saved using the getMessageById
    Message actualMessageById = messageDAO.getMessageById( expectedMessage.getId() );

    // Check that the message fetched matches the one that was created.
    assertEquals( "Message not equals", expectedMessage, actualMessageById );

    // ---- 2. Try to update the message
    expectedMessage.setName( "testUpdatedName" );

    // Save the updated message
    messageDAO.saveMessage( expectedMessage );

    flushAndClearSession();

    // ---- 3. Try to get the updated Message from the database using getMessageByCMAssetCode
    Message actualMessageByName = messageDAO.getMessageByCMAssetCode( expectedMessage.getCmAssetCode() );

    // Check that each of the message selects equals the updated message
    assertEquals( "Updated Message not equals by name", expectedMessage, actualMessageByName );

  }

  /**
   * Tests saving and getting all the message records saved.
   */
  public void testGetAll()
  {
    MessageDAO messageDAO = getMessageDAO();

    // Get a count of the messages in the database
    int count = messageDAO.getAll().size();

    List expectedMessages = new ArrayList();

    // Create a message record to add to the list
    Message expectedMessage1 = buildMessage( getUniqueString() );
    expectedMessages.add( expectedMessage1 );
    messageDAO.saveMessage( expectedMessage1 );

    // Create another message record to add to the list
    Message expectedMessage2 = buildMessage( getUniqueString() );
    expectedMessages.add( expectedMessage2 );
    messageDAO.saveMessage( expectedMessage2 );

    List actualMessages = messageDAO.getAll();

    assertEquals( "List of messages aren't the same size.", expectedMessages.size() + count, actualMessages.size() );

  }

  /**
   * Tests saving and getting all the message records by status code
   */
  public void testGetAllByStatusCode()
  {
    MessageDAO messageDAO = getMessageDAO();

    // Get a count of the messages in the database
    int count = messageDAO.getAll().size();

    List expectedMessages = new ArrayList();

    // Create an active message record to add to the list
    Message expectedMessage1 = buildMessage( getUniqueString() );
    expectedMessages.add( expectedMessage1 );
    messageDAO.saveMessage( expectedMessage1 );

    // Create another active message record to add to the list
    Message expectedMessage2 = buildMessage( getUniqueString() );
    expectedMessages.add( expectedMessage2 );
    messageDAO.saveMessage( expectedMessage2 );

    // Create an inactive message record to add to the list
    Message expectedMessage3 = buildInactiveMessage( getUniqueString() );
    expectedMessages.add( expectedMessage3 );
    messageDAO.saveMessage( expectedMessage3 );

    List actualActiveMessages = messageDAO.getAllActiveMessages();
    List actualInactiveMessages = messageDAO.getAllInactiveMessages();

    assertEquals( "List of messages aren't the same size.", expectedMessages.size() + count, actualActiveMessages.size() + actualInactiveMessages.size() );

  }

  /**
   * Tests get message by name
   */
  public void testGetMessageByCMAssetCode()
  {
    MessageDAO messageDAO = getMessageDAO();

    // Create a Message object with a specific name
    String nameOfMessage = getUniqueString();
    Message expectedMessage = buildMessage( nameOfMessage );

    // Save the named message object
    messageDAO.saveMessage( expectedMessage );

    flushAndClearSession();

    // Try to fetch the record that was just saved using the getMessageByName
    Message actualMessageByName = messageDAO.getMessageByCMAssetCode( expectedMessage.getCmAssetCode() );

    // Check that the message fetched matches the one that was created.
    assertEquals( "Message not equals", expectedMessage, actualMessageByName );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Get the MessageDAO.
   * 
   * @return MessageDAO
   */
  private MessageDAO getMessageDAO()
  {
    return (MessageDAO)ApplicationContextFactory.getApplicationContext().getBean( "messageDAO" );
  }
}
