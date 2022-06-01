/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.message.hibernate;

import java.util.List;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.message.MessageDAO;
import com.biperf.core.domain.message.Message;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * MessageDAOImpl.
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
 */
public class MessageDAOImpl extends BaseDAO implements MessageDAO
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.message.MessageDAO#getMessageById(java.lang.Long)
   * @param id
   * @return Message
   */
  public Message getMessageById( Long id )
  {

    return (Message)getSession().get( Message.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.message.MessageDAO#getMessageByName(java.lang.String)
   * @param name
   * @return Message
   */
  public Message getMessageByName( String name )
  {

    return (Message)getSession().getNamedQuery( "com.biperf.core.domain.message.MessageByName" ).setString( "name", name ).uniqueResult();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.message.MessageDAO#getMessageByCMAssetCode(java.lang.String)
   * @param cmAssetCode
   * @return Message
   */
  public Message getMessageByCMAssetCode( String cmAssetCode )
  {
    return (Message)getSession().getNamedQuery( "com.biperf.core.domain.message.MessageByCMAssetCode" ).setString( "cmAssetCode", cmAssetCode ).uniqueResult();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.message.MessageDAO#getAllActiveMessages
   * @return Message
   */
  public List getAllActiveMessages()
  {

    return getSession().getNamedQuery( "com.biperf.core.domain.message.MessagesByActiveStatus" ).list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.message.MessageDAO#getAllInactiveMessages
   * @return Message
   */
  public List getAllInactiveMessages()
  {

    return getSession().getNamedQuery( "com.biperf.core.domain.message.MessagesByInactiveStatus" ).list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.message.MessageDAO#getAll()
   * @return List
   */
  public List getAll()
  {

    return getSession().getNamedQuery( "com.biperf.core.domain.message.AllMessageList" ).list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.message.MessageDAO#saveMessage(com.biperf.core.domain.message.Message)
   * @param message
   * @return Message
   */
  public Message saveMessage( Message message )
  {
    return (Message)HibernateUtil.saveOrUpdateOrShallowMerge( message );
  }

  public List getAllActiveMessagesByModuleType( String code )
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.message.MessagesByActiveStatusModuleCode" ).setString( "code", code ).list();
  }

  public List getAllActiveMessagesByTypecode( String code )
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.message.getAllActiveMessagesByTypecode" ).setString( "code", code ).list();
  }

  public List getAllActiveTextMessagesBySMSGroupType( String code )
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.message.getAllActiveTextMessagesBySMSGroupType" ).setString( "code", code ).list();
  }
}
