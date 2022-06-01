/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/goalquest/GoalQuestPromotionAction.java,v $
 */

package com.biperf.core.ui.goalquest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

/**
 * GoalQuestPromotionAction.
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
 * <td>sedey</td>
 * <td>April 16, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestPromotionAction extends BaseGoalQuestAction
{
  @SuppressWarnings( "unchecked" )
  public ActionForward selectGoal( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Long promotionId = buildPromotionId( request );
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_GOAL_LEVELS ) );
    GoalQuestPromotion promotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

    GoalQuestWizardForm wizard = (GoalQuestWizardForm)form;
    wizard.setPromotion( promotion );

    return mapping.findForward( "selectGoal" );
  }

  public ActionForward showManagerGoalQuestRules( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    // slap the type in the request so the controller knows which type to fetch
    String type = PromotionType.GOALQUEST;
    request.setAttribute( "promotionType", type );

    List<PromotionMenuBean> promotionMenuBeans = getEligiblePromotions( request );
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    List<PromotionMenuBean> goalquestPromotionMenuBeans = getGoalQuestService().getManagerPromotionMenuBeans( promotionMenuBeans, participant, type );
    List<ManagerGoalquestViewBean> honeycombViewBeans = getGoalQuestService().getHoneycombManagerPrograms( participant.getHoneycombUserId() );
    
    // Deduplication. Goalquest has several entries, 'Manager', 'Multi-level Manager', etc.
    // We ignore this, since we're only showing the name and dates
    honeycombViewBeans = deduplicateHCViewBeans( honeycombViewBeans );

    // If there is only one program and it is on honeycomb, sso there.
    // (The tile's link will lead there instead of this action, but just in case, let's handle it.)
    if ( goalquestPromotionMenuBeans.isEmpty() && honeycombViewBeans.size() == 1 )
    {
      ManagerGoalquestViewBean viewBean = honeycombViewBeans.get( 0 );
      String programId = viewBean.getPromotionId().toString();
      String honeycombSSOLink = getGoalQuestService().buildGoalquestSSOLink( viewBean.getRole(), programId, null );
      response.sendRedirect( honeycombSSOLink );
      return null;
    }
    // Single program setup on the G platform. Go directly to detail page.
    else if ( goalquestPromotionMenuBeans.size() == 1 && honeycombViewBeans.isEmpty() )
    {
      GoalQuestPromotion promotion = (GoalQuestPromotion)goalquestPromotionMenuBeans.get( 0 ).getPromotion();
      request.setAttribute( "promotionMenuBean", goalquestPromotionMenuBeans.get( 0 ) );
      request.setAttribute( "rules", promotion.getManagerWebRulesFromCM() );
      return mapping.findForward( "singlePromotion" );
    }
    // List page.
    else
    {
      List<ManagerGoalquestViewBean> goalquestPromotionViewBeans = goalquestPromotionMenuBeans.stream().map( this::buildManagerGoalquestViewBean ).collect( Collectors.toList() );
      goalquestPromotionViewBeans.addAll( honeycombViewBeans );
      request.setAttribute( "promotionViewBeans", goalquestPromotionViewBeans );
      return mapping.findForward( "multiplePromotionsList" );
    }
  }

  /** Deduplicate by name, start date, and end date. */
  private List<ManagerGoalquestViewBean> deduplicateHCViewBeans( List<ManagerGoalquestViewBean> honeycombViewBeans )
  {
    List<ManagerGoalquestViewBean> distinctList = new ArrayList<>( honeycombViewBeans.size() );
    Iterator<ManagerGoalquestViewBean> iterator = honeycombViewBeans.iterator();
    while ( iterator.hasNext() )
    {
      ManagerGoalquestViewBean iteratorBean = iterator.next();
      boolean foundDuplicate = false;
      for ( ManagerGoalquestViewBean searchBean : distinctList )
      {
        if ( iteratorBean.getPromotionName().equals( searchBean.getPromotionName() )
            && iteratorBean.getStartDate() != null && iteratorBean.getStartDate().equals( searchBean.getStartDate() )
            && iteratorBean.getEndDate() != null && iteratorBean.getEndDate().equals( searchBean.getEndDate() ) )
        {
          foundDuplicate = true;
          break;
        }
      }
      if ( !foundDuplicate )
      {
        distinctList.add( iteratorBean );
      }
    }
    return distinctList;
  }

  public ActionForward showManagerChallengePointRules( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    // slap the type in the request so the controller knows which type to fetch
    String type = PromotionType.CHALLENGE_POINT;
    request.setAttribute( "promotionType", type );

    List<PromotionMenuBean> promotionMenuBeans = getEligiblePromotions( request );
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    List<PromotionMenuBean> goalquestPromotionMenuBeans = getGoalQuestService().getManagerPromotionMenuBeans( promotionMenuBeans, participant, type );

    // forward to detail page if there are only 1 set of rules
    if ( goalquestPromotionMenuBeans.size() > 1 )
    {
      List<ManagerGoalquestViewBean> goalquestPromotionViewBeans = goalquestPromotionMenuBeans.stream().map( this::buildManagerGoalquestViewBean ).collect( Collectors.toList() );
      request.setAttribute( "promotionViewBeans", goalquestPromotionViewBeans );
      return mapping.findForward( "multiplePromotionsList" );
    }
    else if ( goalquestPromotionMenuBeans.size() == 1 && goalquestPromotionMenuBeans.get( 0 ).getPromotion().isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion promotion = (GoalQuestPromotion)goalquestPromotionMenuBeans.get( 0 ).getPromotion();
      request.setAttribute( "promotionMenuBean", goalquestPromotionMenuBeans.get( 0 ) );
      request.setAttribute( "rules", promotion.getManagerWebRulesFromCM() );
    }
    return mapping.findForward( "singlePromotion" );
  }
  
  private ManagerGoalquestViewBean buildManagerGoalquestViewBean( PromotionMenuBean menuBean )
  {
    ManagerGoalquestViewBean viewBean = new ManagerGoalquestViewBean();
    viewBean.setPromotionId( menuBean.getPromotion().getId() );
    viewBean.setPromotionName( menuBean.getPromotion().getPromoNameFromCM() );
    viewBean.setStartDate( DateUtils.toDisplayDateString( menuBean.getPromotion().getSubmissionStartDate(), UserManager.getLocale() ) );
    viewBean.setEndDate( DateUtils.toDisplayDateString( menuBean.getPromotion().getSubmissionEndDate(), UserManager.getLocale() ) );
    viewBean.setObjectiveText( ( (GoalQuestPromotion)menuBean.getPromotion() ).getObjectiveFromCM() );
    viewBean.setRulesText( ( (GoalQuestPromotion)menuBean.getPromotion() ).getManagerWebRulesFromCM() );
    Map<String, Object> detailsLinkParamsMap = new HashMap<>();
    detailsLinkParamsMap.put( "promotionId", menuBean.getPromotion().getId() );
    viewBean.setDetailsLink( ClientStateUtils.generateEncodedLink( "", "goalquestShowRules.do?method=showManagerRulesDetail", detailsLinkParamsMap ) );
    viewBean.setHoneycombProgram( false );
    return viewBean;
  }
  
  public ActionForward showManagerRulesDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    if ( !getParticipantService().getParticipantById( UserManager.getUserId() ).isManager() )
    {
      return showRulesDetail( mapping, form, request, response );
    }
    GoalQuestPromotion promotion = (GoalQuestPromotion)getPromotionMenuBeanForRules( request ).getPromotion();
    request.setAttribute( "rules", promotion.getManagerWebRulesFromCM() );
    return mapping.findForward( "singlePromotion" );
  }

  public ActionForward showRulesDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    request.setAttribute( "rules", getRulesContent( request ) );
    return mapping.findForward( "singlePromotion" );
  }

  private String getRulesContent( HttpServletRequest request )
  {
    PromotionMenuBean promotionMenuBean = getPromotionMenuBeanForRules( request );
    GoalQuestPromotion promotion = (GoalQuestPromotion)promotionMenuBean.getPromotion();

    String showPartnerRules = buildIsPartner( request );

    if ( "true".equals( showPartnerRules ) )
    {
      return promotion.getPartnerWebRulesFromCM();
    }
    else
    {
      return promotion.getWebRulesText();
    }

  }

  private PromotionMenuBean getPromotionMenuBeanForRules( HttpServletRequest request )
  {
    Long promotionId = buildPromotionId( request );
    for ( PromotionMenuBean promotionMenuBean : getEligiblePromotions( request ) )
    {
      if ( promotionMenuBean.getPromotion().getId().equals( promotionId ) )
      {
        request.setAttribute( "promotionMenuBean", promotionMenuBean );
        return promotionMenuBean;
      }
    }
    return null;
  }
}
