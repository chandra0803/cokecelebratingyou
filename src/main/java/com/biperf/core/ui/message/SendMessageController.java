/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/message/SendMessageController.java,v $
 */

package com.biperf.core.ui.message;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.enums.InsertFieldType;
import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;

/**
 * SendMessageController.
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
 * <td>zahler</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SendMessageController extends BaseController
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
    SendMessageForm sendMessageForm = (SendMessageForm)request.getAttribute( "sendMessageForm" );

    PromotionQueryConstraint constraint = new PromotionQueryConstraint();
    if ( sendMessageForm.getModuleCode() != null && sendMessageForm.getModuleCode().equals( MessageModuleType.PRODUCT_CLAIM ) )
    {
      constraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) } );
    }
    else if ( sendMessageForm.getModuleCode() != null && sendMessageForm.getModuleCode().equals( MessageModuleType.QUIZ ) )
    {
      constraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.QUIZ ) } );
    }
    else if ( sendMessageForm.getModuleCode() != null && sendMessageForm.getModuleCode().equals( MessageModuleType.RECOGNITION ) )
    {
      constraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.RECOGNITION ) } );
    }
    constraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    request.setAttribute( "promotionList", getPromotionService().getPromotionList( constraint ) );

    List allAudiences = getAudienceService().getAll();
    List selectedAudiences = sendMessageForm.getAudienceAsList();

    request.setAttribute( "availableAudiences", getAvailableAudiences( selectedAudiences, allAudiences ) );
    request.setAttribute( "insertFieldList", InsertFieldType.getList() );
  }

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
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * @return AudienceService
   */
  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

}
