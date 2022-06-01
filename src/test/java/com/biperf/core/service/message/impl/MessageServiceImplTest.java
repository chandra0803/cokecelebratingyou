/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.message.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;

import com.biperf.core.dao.message.MessageDAO;
import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.MessageStatusType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.cms.CMAssetService;

/**
 * MessageServiceImplTest.
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
 * <td>September 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageServiceImplTest extends BaseServiceTest
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public MessageServiceImplTest( String test )
  {
    super( test );
  }

  /** messageServiceImplementation */
  private MessageServiceImpl messageService = new MessageServiceImpl();

  /** mocks */
  private Mock mockMessageDAO = null;
  private Mock mockCmAssetService = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    mockMessageDAO = new Mock( MessageDAO.class );
    mockCmAssetService = new Mock( CMAssetService.class );

    messageService.setMessageDAO( (MessageDAO)mockMessageDAO.proxy() );
    messageService.setCmAssetService( (CMAssetService)mockCmAssetService.proxy() );
  }

  /**
   * Test getting the Message by id.
   */
  public void testGetMessageById()
  {
    // Get the test Message.
    Message message = new Message();
    message.setId( new Long( 1 ) );
    message.setName( "TestServiceMessage" );

    // MessageDAO expected to call getMessageById once with the MessageId which will return the
    // Message expected
    mockMessageDAO.expects( once() ).method( "getMessageById" ).with( same( message.getId() ) ).will( returnValue( message ) );

    messageService.getMessageById( message.getId() );

    mockMessageDAO.verify();
  }

  /**
   * Test getting a Message object by message name.
   */
  public void testGetMessageByCMAssetCode()
  {
    // Get the test Message.
    Message message = new Message();
    message.setId( new Long( 1 ) );
    message.setName( "TestServiceMessage" );
    message.setCmAssetCode( "message.something.something" );

    mockMessageDAO.expects( once() ).method( "getMessageByCMAssetCode" ).with( same( message.getCmAssetCode() ) ).will( returnValue( message ) );

    messageService.getMessageByCMAssetCode( message.getCmAssetCode() );

    mockMessageDAO.verify();
  }

  /**
   * Test adding the Message through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveMessageInsert() throws ServiceErrorException
  {
    // Create a Message object to simulate the object being passed in.
    Message message = new Message();
    message.setName( "TestServiceMessage" );
    message.setModuleCode( MessageModuleType.lookup( "quiz" ) );
    message.setStatusCode( MessageStatusType.lookup( MessageStatusType.ACTIVE ) );

    // Create a Message object to simulate what will get stored in the database.
    Message savedMessage = new Message();
    savedMessage.setId( new Long( 1 ) );
    savedMessage.setName( "TestServiceMessage" );
    savedMessage.setModuleCode( MessageModuleType.lookup( "quiz" ) );
    savedMessage.setStatusCode( MessageStatusType.lookup( MessageStatusType.ACTIVE ) );

    // Mock Message object for getMessageByName Call.
    mockMessageDAO.expects( once() ).method( "getMessageByName" ).with( same( message.getName() ) ).will( returnValue( null ) );

    // Mock object for saveMessage Call.
    mockMessageDAO.expects( once() ).method( "saveMessage" ).with( same( message ) ).will( returnValue( savedMessage ) );

    mockCmAssetService.expects( once() ).method( "getUniqueAssetCode" ).will( returnValue( "unique.asset.name" + System.currentTimeMillis() ) );
    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // Test the MessageService.saveMessage
    messageService.saveMessage( message, "subject", "html msg", "plainText msg", "text msg", "strongmail subject", "strongmail msg" );

    // Test the MessageService.getAll()
    List messageList = new ArrayList();
    messageList.add( new Message() );
    messageList.add( new Message() );
    mockMessageDAO.expects( once() ).method( "getAll" ).will( returnValue( messageList ) );
    List returnedMessageList = messageService.getAll();
    assertTrue( returnedMessageList.size() == 2 );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockMessageDAO.verify();

  }

  /**
   * Test getting all active Messages through the service.
   */
  public void testGetAllActiveTextMessages()
  {
    List messageList = new ArrayList();
    Message message1 = new Message();
    message1.setId( new Long( 1 ) );
    message1.setName( "TestServiceMessage" );
    message1.setModuleCode( MessageModuleType.lookup( "quiz" ) );
    message1.setStatusCode( MessageStatusType.lookup( MessageStatusType.ACTIVE ) );
    message1.setCmAssetCode( "myAssetCode" );
    messageList.add( message1 );

    Message message2 = new Message();
    message2.setId( new Long( 2 ) );
    message2.setName( "TestServiceMessage2" );
    message2.setModuleCode( MessageModuleType.lookup( "quiz" ) );
    message2.setStatusCode( MessageStatusType.lookup( MessageStatusType.ACTIVE ) );
    message2.setCmAssetCode( "myAssetCode2" );
    messageList.add( message2 );

    mockMessageDAO.expects( once() ).method( "getAllActiveMessages" ).will( returnValue( messageList ) );

    // TODO - at some point can we change mockContentReader to change what it is returning on the
    // fly.
    // For this test, it would be valuable to set some return values to ""
    // to ensure the lists are different sizes.
    List returnedActiveTextMessageList = messageService.getAllActiveTextMessages();
    // There are TWO messages returned by the list, but none have TEXT_MSG data
    assertTrue( returnedActiveTextMessageList.size() == 0 );
  }

}