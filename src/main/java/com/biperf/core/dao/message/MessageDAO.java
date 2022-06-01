/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.message;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.message.Message;

/**
 * MessageDAO.
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
 */
public interface MessageDAO extends DAO
{
  /**
   * Bean Name=messageDAO
   */
  public static final String BEAN_NAME = "messageDAO";

  /**
   * Get the Message by id.
   * 
   * @param id
   * @return Message
   */
  public Message getMessageById( Long id );

  /**
   * Get the Message by name.
   * 
   * @param name
   * @return Message
   */
  public Message getMessageByName( String name );

  /**
   * @param cmAssetCode
   * @return Message
   */
  public Message getMessageByCMAssetCode( String cmAssetCode );

  /**
   * Get all active Messages.
   * 
   * @return Message
   */
  public List getAllActiveMessages();

  /**
   * Get all active messages by MessageModuleType
   * 
   * @param code
   * @return List of all active messages for the given module type code.
   */
  public List getAllActiveMessagesByModuleType( String code );

  public List getAllActiveMessagesByTypecode( String code );

  /**
   * Get all inactive Messages.
   * 
   * @return Message
   */
  public List getAllInactiveMessages();

  /**
   * Get All Message records.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Save or update the Message.
   * 
   * @param message
   * @return Message
   */
  public Message saveMessage( Message message );

  public List getAllActiveTextMessagesBySMSGroupType( String messageSMSGroupTypeCode );
}
