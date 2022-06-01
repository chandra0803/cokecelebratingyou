
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionSSIApprovalLevelType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseController;

/**
 * 
 * PromotionSSIApprovalController.
 * 
 * @author chowdhur
 * @since Nov 3, 2014
 */
public class PromotionSSIApprovalController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionSSIApprovalForm promotionSSIApprovalForm = (PromotionSSIApprovalForm)request.getAttribute( "promotionSSIApprovalForm" );

    request.setAttribute( "approvalLevelTypesList", PromotionSSIApprovalLevelType.getList() );

    List<Audience> availableAudiences = getAudienceService().getAll();

    List<Audience> availableLvl1Audiences = getAvailableAudiences( promotionSSIApprovalForm.getApprovalLvl1Audiences(), new ArrayList<Audience>( availableAudiences ) );
    Collections.sort( availableLvl1Audiences, new AudienceComparator() );
    request.setAttribute( "availableLvl1Audiences", availableLvl1Audiences );

    List<Audience> availableLvl2Audiences = getAvailableAudiences( promotionSSIApprovalForm.getApprovalLvl2Audiences(), new ArrayList<Audience>( availableAudiences ) );
    Collections.sort( availableLvl2Audiences, new AudienceComparator() );
    request.setAttribute( "availableLvl2Audiences", availableLvl2Audiences );

    if ( ObjectUtils.equals( promotionSSIApprovalForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }
    request.setAttribute( "pageNumber", "5" );
  }

  public class AudienceComparator implements Comparator<Audience>
  {
    public int compare( Audience o1, Audience o2 )
    {
      return o1.getName().toLowerCase().compareTo( o2.getName().toLowerCase() );
    }
  }

  private List<Audience> getAvailableAudiences( List<PromotionAudienceFormBean> audiences, List<Audience> availableAudiences )
  {
    if ( audiences != null )
    {
      Iterator<Audience> audienceIterator = availableAudiences.iterator();
      while ( audienceIterator.hasNext() )
      {
        Audience audience = (Audience)audienceIterator.next();
        Iterator<PromotionAudienceFormBean> assignedIterator = audiences.iterator();
        while ( assignedIterator.hasNext() )
        {
          PromotionAudienceFormBean audienceBean = assignedIterator.next();
          if ( audienceBean.getAudienceId().equals( audience.getId() ) )
          {
            audienceIterator.remove();
          }
        }
      }
    }
    return availableAudiences;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }
}
