
package com.biperf.core.ui.recognition.easy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.search.ParticipantAutoCompleteSearchAction;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.UserManager;

public class RecipientSearchAction extends ParticipantAutoCompleteSearchAction
{

  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return super.generatePaxSearchView( mapping, actionForm, request, response );
  }

  @Override
  protected List<Participant> searchParticipants( HttpServletRequest request )
  {
    ParticipantSearchCriteria criteria = getSearchCriteria( request );

    List<String> paxs = getParticipantService().searchCriteriaAutoComplete( criteria );

    // now, use what would show in the autocomplete (smith becomes Smith) and search on that
    List<Participant> allResults = new ArrayList<Participant>();
    if ( paxs != null && !paxs.isEmpty() )
    {
      // search using each item that would otherwise appear in the drop down autocomplete
      for ( String result : paxs )
      {
        criteria.setLastName( result );

        // search again!
        List<Participant> results = getParticipantService().searchParticipants( criteria );

        for ( Participant pax : results )
        {
          if ( !pax.getId().equals( UserManager.getUserId() ) )
          {
            allResults.add( pax );
          }
        }
      }
    }

    return allResults;
  }

  @Override
  protected BigDecimal getBudgetConversionRatio( Long receiverId, Long submitterId, Long promotionId )
  {
    boolean calculateConversionRatio = false;
    if ( promotionId != null )
    {
      Promotion promotion = getPromotionService().getPromotionById( promotionId );
      calculateConversionRatio = promotion.getBudgetMaster() != null && !promotion.getBudgetMaster().isCentralBudget();
    }
    return calculateConversionRatio ? BudgetUtils.getBudgetConversionRatio( getUserService(), receiverId, submitterId ) : BigDecimal.ONE;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
