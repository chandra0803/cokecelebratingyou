
package com.biperf.core.ui.mobilerecogapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.mobileapp.recognition.service.EligibleRecognitionPromotion;
import com.biperf.core.mobileapp.recognition.service.RecognitionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.RecognitionBean;

public class PromotionsListAction extends BaseSendRecognitionAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final PromotionsListForm form = (PromotionsListForm)actionForm;

    final Long USER_ID = UserManager.getUserId();
    final boolean IS_USER_A_PARTICIPANT = UserManager.getUser().isParticipant();
    String lastName = UserManager.getUser().getLastName();
    // Setting the eligiblePromotion session value as null to get the updated eligible promotion
    // list
    if ( request.getSession().getAttribute( "eligiblePromotions" ) != null )
    {
      request.getSession().setAttribute( "eligiblePromotions", null );
    }
    final List<PromotionMenuBean> eligiblePromotion = getEligiblePromotions( request );
    final PromotionsListBean promotionsListBean = new PromotionsListBean();
    Long recipeintId = null;
    // this need to be changed if it is form attribute
    String recId = request.getParameter( "recipientId" );
    if ( null != recId )
    {
      recipeintId = new Long( request.getParameter( "recipientId" ) );
    }

    List<EligibleRecognitionPromotion> erps = getRecognitionService().getMobileRecognitionSubmissionList( USER_ID, eligiblePromotion, IS_USER_A_PARTICIPANT, recipeintId );
    Collections.sort( erps, getPromotionBeanComparator() );
    promotionsListBean.setEligibleRecognitionPromotions( erps );
    getUserNodes( USER_ID, promotionsListBean );
    populateSocialMediaSharingOptions( promotionsListBean );

    int celebrationCount = 0;
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      celebrationCount = getServiceAnniversaryService().getUpcomingCelebrationCount();
    }
    else
    {
      celebrationCount = getPurlService().getUpcomingPurlRecipientsCount();
    }
    promotionsListBean.setCelebrationCount( celebrationCount );

    writeAsJsonToResponse( promotionsListBean, response );

    // Need to set locale and primaryCountryCode for UserManager object. These are
    // consumed by some service methods down the chain.
    Locale locale = UserManager.getLocale();

    return null;
  }

  /* default */ void getUserNodes( Long userId, PromotionsListBean bean )
  {
    Set<UserNode> userNodes = getUserService().getUserNodes( userId );
    List<UserNode> userNodesList = null;
    if ( userNodes != null && !userNodes.isEmpty() )
    {
      userNodesList = new ArrayList<>( userNodes );
    }

    bean.setUserNodes( userNodesList );
  }

  /* default */ void getPromotions( Long userId, boolean isUserAParticipant, PromotionsListBean promotionsListBean, List<PromotionMenuBean> eligiblePromotions )
  {
    List<PromotionMenuBean> validPromotions = new ArrayList<>();

    for ( PromotionMenuBean promoBean : eligiblePromotions )
    {
      if ( promoBean.isCanSubmit() )
      {
        validPromotions.add( promoBean );
      }
    }

    List<RecognitionBean> allRecognitionPromotions = getPromotionService().getRecognitionSubmissionList( userId, validPromotions, isUserAParticipant );
    List<RecognitionBean> recognitionPromotions = new ArrayList<>();
    for ( RecognitionBean b : allRecognitionPromotions )
    {
      if ( b.isRegularRecognitionPromotion() )
      {
        recognitionPromotions.add( b );
      }
    }

    promotionsListBean.setRecognitionBeans( recognitionPromotions );
  }

  protected void populateSocialMediaSharingOptions( PromotionsListBean promotionsListBean )
  {
    SystemVariableService svs = getSystemVariableService();
    promotionsListBean.setShareFacebook( getBooleanPropertyValue( svs, SystemVariableService.PUBLIC_RECOG_ALLOW_FACEBOOK ) );
    promotionsListBean.setShareTwitter( getBooleanPropertyValue( svs, SystemVariableService.PUBLIC_RECOG_ALLOW_TWITTER ) );
    promotionsListBean.setShareLinkedIn( getBooleanPropertyValue( svs, SystemVariableService.PUBLIC_RECOG_ALLOW_LINKED_IN ) );
  }

  private Comparator<EligibleRecognitionPromotion> getPromotionBeanComparator()
  {
    return new Comparator<EligibleRecognitionPromotion>()
    {
      @Override
      public int compare( EligibleRecognitionPromotion one, EligibleRecognitionPromotion two )
      {
        // first, all promotions with points go to the top
        if ( one.isAwardAvailable() && !two.isAwardAvailable() )
        {
          return -1;
        }
        if ( !one.isAwardAvailable() && two.isAwardAvailable() )
        {
          return 1;
        }

        // if they both have points then check the budget end dates
        if ( one.isAwardAvailable() && two.isAwardAvailable() )
        {
          // check for existence of budgets first
          if ( one.getBudgetInfo() != null && two.getBudgetInfo() != null )
          {
            // ok, so both have budgets, but drop budgets with zero value down the list.
            if ( one.getBudgetInfo().getCurrentValue() > 0 && two.getBudgetInfo().getCurrentValue() == 0 )
            {
              return -1;
            }
            if ( one.getBudgetInfo().getCurrentValue() == 0 && two.getBudgetInfo().getCurrentValue() > 0 )
            {
              return 1;
            }

            // both have budgets (and the budgets are non-zero) so show the earliest ending budget
            // first
            // first check if either of the budgets have no end date
            if ( one.getBudgetInfo().hasEndDate() && !two.getBudgetInfo().hasEndDate() )
            {
              return -1;
            }
            if ( !one.getBudgetInfo().hasEndDate() && two.getBudgetInfo().hasEndDate() )
            {
              return 1;
            }

            if ( one.getBudgetInfo().hasEndDate() && two.getBudgetInfo().hasEndDate() )
            {
              // both have budget end dates, so show the earliest ending one first
              if ( one.getBudgetInfo().getDaysRemaining() < two.getBudgetInfo().getDaysRemaining() )
              {
                return -1;
              }
              if ( one.getBudgetInfo().getDaysRemaining() > two.getBudgetInfo().getDaysRemaining() )
              {
                return 1;
              }

              // both budgets have the same daysRemaining so sort by promotion end date, then name
              if ( one.endsBefore( two ) )
              {
                return -1;
              }
              else if ( two.endsBefore( one ) )
              {
                return 1;
              }
              else
              {
                // sort alphabetically
                return one.getName().toLowerCase().trim().compareTo( two.getName().toLowerCase().trim() );
              }
            }

          }

          // at least one doesn't have a budget, so show the one with the budget first
          if ( one.getBudgetInfo() != null && two.getBudgetInfo() == null )
          {
            return -1;
          }
          if ( one.getBudgetInfo() == null && two.getBudgetInfo() != null )
          {
            return 1;
          }

        }

        // if we made it this far neither has an award
        // so start by sorting by daysRemaining in the promotion
        if ( one.endsBefore( two ) )
        {
          return -1;
        }
        else if ( two.endsBefore( one ) )
        {
          return 1;
        }
        else
        {
          // same end dates so sort alphabetically
          return one.getName().toLowerCase().trim().compareTo( two.getName().toLowerCase().trim() );
        }
      }

    };
  }

  private RecognitionService getRecognitionService()
  {
    return (RecognitionService)BeanLocator.getBean( RecognitionService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  private ServiceAnniversaryService getServiceAnniversaryService()
  {
    return (ServiceAnniversaryService)getService( ServiceAnniversaryService.BEAN_NAME );
  }

  private boolean getBooleanPropertyValue( SystemVariableService systemVariableService, String propertyName )
  {
    PropertySetItem property = systemVariableService.getPropertyByName( propertyName );
    return property != null ? property.getBooleanVal() : false;
  }
}
