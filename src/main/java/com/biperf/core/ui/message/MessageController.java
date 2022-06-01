/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/message/MessageController.java,v $
 */

package com.biperf.core.ui.message;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.InsertFieldType;
import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.MessageStatusType;
import com.biperf.core.domain.enums.MessageType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.ui.BaseController;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsUtil;

/**
 * MessageController.
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
 * <td>Sep 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List localeItems = getCMAssetService().getSupportedLocales( true );
    request.setAttribute( "localeItems", localeItems );
    MessageForm messageForm = (MessageForm)request.getAttribute( "messageForm" );
    boolean displayStrongMailContent = false;

    Message message = getMessageService().getMessageById( new Long( messageForm.getMessageId() ) );
    if ( message != null && message.getCmAssetCode() != null )
    {
      if ( Arrays.asList( Message.STRONGMAIL_MESSAGE_ARRAY ).contains( message.getCmAssetCode() ) )
      {
        displayStrongMailContent = true;
      }

      for ( Iterator iter = localeItems.iterator(); iter.hasNext(); )
      {
        Content content = (Content)iter.next();
        if ( content != null )
        {
          String localeCode = (String)content.getContentDataMap().get( "CODE" );
          Locale locale = CmsUtil.getLocale( localeCode );
          String translatedText = getCMAssetService().getString( message.getCmAssetCode(), Message.CM_KEY_SUBJECT, locale, true );
          messageForm.getTranslatedSubjects().put( localeCode, translatedText );
          translatedText = getCMAssetService().getString( message.getCmAssetCode(), Message.CM_KEY_HTML_MSG, locale, true );
          messageForm.getTranslatedHtmlMsgs().put( localeCode, translatedText );
          translatedText = getCMAssetService().getString( message.getCmAssetCode(), Message.CM_KEY_PLAIN_TEXT_MSG, locale, true );
          messageForm.getTranslatedPlainTextMsgs().put( localeCode, translatedText );
          translatedText = getCMAssetService().getString( message.getCmAssetCode(), Message.CM_KEY_TEXT_MSG, locale, true );
          messageForm.getTranslatedTextMsgs().put( localeCode, translatedText );
          translatedText = getCMAssetService().getString( message.getCmAssetCode(), Message.CM_KEY_SM_SUBJECT, locale, true );
          messageForm.getTranslatedStrongMailSubjects().put( localeCode, translatedText );
          translatedText = getCMAssetService().getString( message.getCmAssetCode(), Message.CM_KEY_SM_MSG, locale, true );
          messageForm.getTranslatedStrongMailMsgs().put( localeCode, translatedText );
        }
      }
    }
    request.setAttribute( "messageStatusList", MessageStatusType.getList() );
    request.setAttribute( "moduleList", MessageModuleType.getList() );
    request.setAttribute( "insertFieldList", InsertFieldType.getList() );
    request.setAttribute( "messageTypeList", MessageType.getList() );
    request.setAttribute( "messageSMSGroupTypeList", MessageSMSGroupType.getPlateuOnlyList() );
    request.setAttribute( "DISPLAY_STRONGMAIL_CONTENT", displayStrongMailContent );
  }

  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

}
