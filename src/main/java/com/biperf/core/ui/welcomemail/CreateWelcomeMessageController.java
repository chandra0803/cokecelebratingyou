
package com.biperf.core.ui.welcomemail;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.InsertFieldType;
import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;

/**
 * 
 * CreateWelcomeMessageController.
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
 * <td>Ramesh Kunasekaran</td>
 * <td>Sep 18, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class CreateWelcomeMessageController extends BaseController
{

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    WelcomeMessageForm welcomeMessageForm = (WelcomeMessageForm)request.getAttribute( "welcomeMessageForm" );
    List allAudiences = getAudienceService().getAll();
    List selectedAudiences = welcomeMessageForm.getAudienceList();
    request.setAttribute( "availableAudiences", getAvailableAudiences( selectedAudiences, allAudiences ) );
    request.setAttribute( "insertFieldList", InsertFieldType.getList() );
    List messageList = getMessageList();
    request.setAttribute( "messageList", messageList );
    request.setAttribute( "messageListSize", new Integer( messageList.size() ) );

  }

  /**
   * 
   * @param audiences
   * @param availableAudiences
   * @return list
   */
  private List getAvailableAudiences( List audiences, List availableAudiences )
  {
    if ( audiences != null )
    {
      Iterator audienceIterator = availableAudiences.iterator();
      while ( audienceIterator.hasNext() )
      {
        Audience audience = (Audience)audienceIterator.next();
        Iterator assignedIterator = audiences.iterator();
        while ( assignedIterator.hasNext() )
        {
          PromotionAudienceFormBean audienceBean = (PromotionAudienceFormBean)assignedIterator.next();
          if ( audienceBean.getAudienceId().equals( audience.getId() ) )
          {
            audienceIterator.remove();
          }
        }
      }
    }
    return availableAudiences;
  }

  /**
   * Returns a list of messages sorted by message Module type.
   * 
   * @return a list of messages, as a <code>List</code> of {@link Message} objects.
   */
  private List getMessageList()
  {
    List messageList = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.WELCOME_MESSAGE );
    if ( CollectionUtils.isEmpty( messageList ) )
    {
      Message noMessage = new Message();
      noMessage.setId( new Long( -1 ) );
      noMessage.setName( "No Notification" );
      messageList.add( noMessage );
    }
    if ( messageList != null )
    {
      Collections.sort( messageList, new Comparator()
      {
        public int compare( Object object, Object object1 )
        {
          Message message1 = (Message)object;
          Message message2 = (Message)object1;

          return message1.getName().compareTo( message2.getName() );
        }
      } );

    }

    return messageList;
  }

  /**
   * @return AudienceService
   */
  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  /**
   * Returns the message service.
   * 
   * @return a reference to the message service.
   */
  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }
}
