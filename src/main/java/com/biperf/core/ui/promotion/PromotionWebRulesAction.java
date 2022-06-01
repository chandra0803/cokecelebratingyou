/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.ManagerWebRulesAudienceType;
import com.biperf.core.domain.enums.PartnerWebRulesAudienceType;
import com.biperf.core.domain.enums.WebRulesAudienceType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionManagerWebRulesAudience;
import com.biperf.core.domain.promotion.PromotionPartnerWebRulesAudience;
import com.biperf.core.domain.promotion.PromotionWebRulesAudience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * PromotionWebRulesAction.
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
 * <td>asondgeroth</td>
 * <td>Jul 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionWebRulesAction extends PromotionBaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( PromotionBasicsAction.class );
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionWebRulesForm promoWebRulesForm = (PromotionWebRulesForm)form;

    Promotion promotion = null;

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );

      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_PARTNERS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );

      Promotion attachedPromotion = getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );

      promotion.setPromotionWebRulesAudiences( attachedPromotion.getPromotionWebRulesAudiences() );

      if ( promotion.isGoalQuestOrChallengePointPromotion() )
      {
        ( (GoalQuestPromotion)promotion ).setPromotionManagerWebRulesAudience( ( (GoalQuestPromotion)attachedPromotion ).getPromotionManagerWebRulesAudience() );
        ( (GoalQuestPromotion)promotion ).setPromotionPartnerWebRulesAudience( ( (GoalQuestPromotion)attachedPromotion ).getPromotionPartnerWebRulesAudience() );
      }
    }
    // NORMAL MODE
    else
    {
      String promotionId = promoWebRulesForm.getPromotionId();
      if ( promotionId != null && promotionId.length() > 0 )
      {
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_PARTNERS ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );

        promotion = getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), promoAssociationRequestCollection );
      }
    }

    if ( promotion != null )
    {
      promoWebRulesForm.load( promotion );
    }

    // iterate PromotionWebRulesAudiences and set nbr of pax for criteria based audience
    Set promotionAudienceSetWithPaxSize = new LinkedHashSet();
    if ( promotion != null && promotion.getPromotionWebRulesAudiences() != null )
    {
      Iterator promotionAudienceIterator = promotion.getPromotionWebRulesAudiences().iterator();
      while ( promotionAudienceIterator.hasNext() )
      {
        PromotionWebRulesAudience promoWebRulesAudience = (PromotionWebRulesAudience)promotionAudienceIterator.next();
        Audience audience = promoWebRulesAudience.getAudience();
        audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
        promotionAudienceSetWithPaxSize.add( promoWebRulesAudience );
      }
    }
    request.getSession().setAttribute( "sessionAudienceList", new LinkedHashSet( promotionAudienceSetWithPaxSize ) );

    if ( promotion != null && promotion.isGoalQuestOrChallengePointPromotion() )
    {
      // iterate PromotionManagerWebRulesAudiences and set nbr of pax for criteria based audience
      Set promotionManagerAudienceSetWithPaxSize = new LinkedHashSet();
      if ( ( (GoalQuestPromotion)promotion ).getPromotionManagerWebRulesAudience() != null )
      {
        Iterator promotionManagerAudienceIterator = ( (GoalQuestPromotion)promotion ).getPromotionManagerWebRulesAudience().iterator();
        while ( promotionManagerAudienceIterator.hasNext() )
        {
          PromotionManagerWebRulesAudience promoManagerWebRulesAudience = (PromotionManagerWebRulesAudience)promotionManagerAudienceIterator.next();
          Audience audience = promoManagerWebRulesAudience.getAudience();
          audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
          promotionManagerAudienceSetWithPaxSize.add( promoManagerWebRulesAudience );
        }
      }
      request.getSession().setAttribute( "sessionManagerAudienceList", new LinkedHashSet( promotionManagerAudienceSetWithPaxSize ) );

      // iterate PromotionPartnerWebRulesAudiences and set nbr of pax for criteria based audience
      Set promotionPartnerAudienceSetWithPaxSize = new LinkedHashSet();
      if ( ( (GoalQuestPromotion)promotion ).getPromotionPartnerWebRulesAudience() != null )
      {
        Iterator promotionPartnerAudienceIterator = ( (GoalQuestPromotion)promotion ).getPromotionPartnerWebRulesAudience().iterator();
        while ( promotionPartnerAudienceIterator.hasNext() )
        {
          PromotionPartnerWebRulesAudience promoPartnerWebRulesAudience = (PromotionPartnerWebRulesAudience)promotionPartnerAudienceIterator.next();
          Audience audience = promoPartnerWebRulesAudience.getAudience();
          audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
          promotionPartnerAudienceSetWithPaxSize.add( promoPartnerWebRulesAudience );
        }
      }
      request.getSession().setAttribute( "sessionPartnerAudienceList", new LinkedHashSet( promotionPartnerAudienceSetWithPaxSize ) );
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionWebRulesForm promoWebRulesForm = (PromotionWebRulesForm)form;

    Promotion promotion = null;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        forward = getCancelForward( mapping, request );
      }
      request.getSession().removeAttribute( "sessionAudienceList" );
      return forward;
    }

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    // NORMAL MODE
    else
    {
      String promotionId = promoWebRulesForm.getPromotionId();
      if ( promotionId != null && promotionId.length() > 0 )
      {
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_PARTNERS ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );

        promotion = getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), promoAssociationRequestCollection );
      }
    }

    // Check the webRulesAudience for validity and add/remove them to/from the promotion if the web
    // rules are active
    if ( promoWebRulesForm.isActive() )
    {
      if ( promoWebRulesForm.getAudience().equals( WebRulesAudienceType.CREATE_AUDIENCE_CODE ) )
      {
        Set sessionAudienceSet = (LinkedHashSet)request.getSession().getAttribute( "sessionAudienceList" );
        // To fix 20250 display error message if audience not selected
        if ( sessionAudienceSet == null || sessionAudienceSet.size() < 1 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.webrules.errors.AUDIENCE_NOT_FOUND" ) );
        }
        else
        {
          Set promotionAudienceSet = promotion.getPromotionWebRulesAudiences();
          if ( promotionAudienceSet != null && promotionAudienceSet.size() > 0 )
          {
            Iterator promotionAudienceIterator = promotion.getPromotionWebRulesAudiences().iterator();
            while ( promotionAudienceIterator.hasNext() )
            {
              PromotionWebRulesAudience promoWebRulesAudience = (PromotionWebRulesAudience)promotionAudienceIterator.next();
              if ( !sessionAudienceSet.contains( promoWebRulesAudience ) )
              {
                promotionAudienceIterator.remove();
              }
            }
          }
          Iterator sessionAudienceIterator = sessionAudienceSet.iterator();
          while ( sessionAudienceIterator.hasNext() )
          {
            PromotionWebRulesAudience sessionPromoWebRulesAudience = (PromotionWebRulesAudience)sessionAudienceIterator.next();
            promotion.addPromotionWebRulesAudience( sessionPromoWebRulesAudience );
          }
        }
      }

      if ( promotion.isGoalQuestOrChallengePointPromotion() )
      {
        if ( promoWebRulesForm.getManagerAudience().equals( ManagerWebRulesAudienceType.CREATE_AUDIENCE_CODE ) )
        {
          Set sessionManagerAudienceSet = (LinkedHashSet)request.getSession().getAttribute( "sessionManagerAudienceList" );
          // To fix 20250 display error message if audience not selected
          if ( sessionManagerAudienceSet == null || sessionManagerAudienceSet.size() < 1 )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.webrules.errors.AUDIENCE_NOT_FOUND" ) );
          }
          else
          {
            Set promotionManagerAudienceSet = ( (GoalQuestPromotion)promotion ).getPromotionManagerWebRulesAudience();
            if ( promotionManagerAudienceSet != null && promotionManagerAudienceSet.size() > 0 )
            {
              Iterator promotionManagerAudienceIterator = ( (GoalQuestPromotion)promotion ).getPromotionManagerWebRulesAudience().iterator();
              while ( promotionManagerAudienceIterator.hasNext() )
              {
                PromotionManagerWebRulesAudience promoManagerWebRulesAudience = (PromotionManagerWebRulesAudience)promotionManagerAudienceIterator.next();
                if ( !sessionManagerAudienceSet.contains( promoManagerWebRulesAudience ) )
                {
                  promotionManagerAudienceIterator.remove();
                }
              }
            }
            Iterator sessionManagerAudienceIterator = sessionManagerAudienceSet.iterator();
            while ( sessionManagerAudienceIterator.hasNext() )
            {
              PromotionManagerWebRulesAudience sessionPromoManagerWebRulesAudience = (PromotionManagerWebRulesAudience)sessionManagerAudienceIterator.next();
              ( (GoalQuestPromotion)promotion ).addPromotionManagerWebRulesAudience( sessionPromoManagerWebRulesAudience );
            }
          }
        }
        if ( promoWebRulesForm.isPartnerAvailable( promotion ) )
        {
          if ( promoWebRulesForm.getPartnerAudience().equals( PartnerWebRulesAudienceType.CREATE_AUDIENCE_CODE ) )
          {
            Set sessionPartnerAudienceSet = (LinkedHashSet)request.getSession().getAttribute( "sessionPartnerAudienceList" );
            // To fix 20250 display error message if audience not selected
            if ( sessionPartnerAudienceSet == null || sessionPartnerAudienceSet.size() < 1 )
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.webrules.errors.AUDIENCE_NOT_FOUND" ) );
            }
            else
            {
              Set promotionPartnerAudienceSet = ( (GoalQuestPromotion)promotion ).getPromotionPartnerWebRulesAudience();
              if ( promotionPartnerAudienceSet != null && promotionPartnerAudienceSet.size() > 0 )
              {
                Iterator promotionPartnerAudienceIterator = ( (GoalQuestPromotion)promotion ).getPromotionPartnerWebRulesAudience().iterator();
                while ( promotionPartnerAudienceIterator.hasNext() )
                {
                  PromotionPartnerWebRulesAudience promotionPartnerWebRulesAudience = (PromotionPartnerWebRulesAudience)promotionPartnerAudienceIterator.next();
                  if ( !sessionPartnerAudienceSet.contains( promotionPartnerWebRulesAudience ) )
                  {
                    promotionPartnerAudienceIterator.remove();
                  }
                }
              }
              Iterator sessionPartnerAudienceIterator = sessionPartnerAudienceSet.iterator();
              while ( sessionPartnerAudienceIterator.hasNext() )
              {
                PromotionPartnerWebRulesAudience sessionPromoPartnerWebRulesAudience = (PromotionPartnerWebRulesAudience)sessionPartnerAudienceIterator.next();
                ( (GoalQuestPromotion)promotion ).addPromotionPartnerWebRulesAudience( sessionPromoPartnerWebRulesAudience );
              }
            }
          }
        }
      }
    }

    // Put the appropriate information onto the promotion based on the user's web rules
    // configuration
    if ( promotion != null )
    {
      promoWebRulesForm.buildPromotionWebRules( promotion, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    try
    {
      if ( promoWebRulesForm.isActive() )
      {
        promotion = getPromotionService().saveWebRulesCmText( promotion, promoWebRulesForm.getWebRulesText() );

        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          promotion = getPromotionService().saveWebRulesManagerCmText( promotion, promoWebRulesForm.getManagerWebRulesText() );
          if ( promoWebRulesForm.isPartnerAvailable( promotion ) )
          {
            promotion = getPromotionService().saveWebRulesPartnerCmText( promotion, promoWebRulesForm.getPartnerWebRulesText() );
          }
        }
      }
      promotion = getPromotionService().savePromotion( promotion );
    }
    catch( ServiceErrorException e1 )
    {
      logger.error( e1.getMessage(), e1 );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }

    getAudienceService().clearPromoEligibilityCache();

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      if ( isWizardMode( request ) )
      {
        setPromotionInWizardManager( request, promotion );
        forward = getWizardNextPage( mapping, request, promotion );
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
      request.getSession().removeAttribute( "sessionAudienceList" );
      request.getSession().removeAttribute( "sessionGqManagerAudienceList" );
      request.getSession().removeAttribute( "sessionGqPartnerAudienceList" );
    }

    return forward;

  }

  /**
   * Iterates over the current list of promotionWebRules audiences and removes them from the form.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removeAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionWebRulesForm promoWebRulesForm = (PromotionWebRulesForm)form;

    if ( null != promoWebRulesForm.getDeletePromotionWebRulesAudience() && promoWebRulesForm.getDeletePromotionWebRulesAudience().length > 0 )
    {

      // Get the list of names for the audiences being deleted.
      List deletedWebRulesAudienceNames = ArrayUtil.stringArrayToList( promoWebRulesForm.getDeletePromotionWebRulesAudience() );

      Set sessionAudienceList = (LinkedHashSet)request.getSession().getAttribute( "sessionAudienceList" );
      // Get the iterator of existing audiences on the promotionWebRulesForm
      Iterator audienceIterator = sessionAudienceList.iterator();

      Set setOfWebRulesAudienceToRemove = new LinkedHashSet();

      // Iterate over the webRulesAudience
      while ( audienceIterator.hasNext() )
      {
        PromotionWebRulesAudience promotionWebRulesAudience = (PromotionWebRulesAudience)audienceIterator.next();

        if ( deletedWebRulesAudienceNames.contains( promotionWebRulesAudience.getAudience().getName() ) )
        {
          setOfWebRulesAudienceToRemove.add( promotionWebRulesAudience );
        }
      }

      sessionAudienceList.removeAll( setOfWebRulesAudienceToRemove );

    }

    return forward;

  }

  public ActionForward removeManagerAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success" );

    PromotionWebRulesForm promoWebRulesForm = (PromotionWebRulesForm)form;

    if ( null != promoWebRulesForm.getDeletePromotionManagerWebRulesAudience() && promoWebRulesForm.getDeletePromotionManagerWebRulesAudience().length > 0 )
    {

      // Get the list of names for the audiences being deleted.
      List deletedManagerWebRulesAudienceNames = ArrayUtil.stringArrayToList( promoWebRulesForm.getDeletePromotionManagerWebRulesAudience() );

      Set sessionManagerAudienceList = (LinkedHashSet)request.getSession().getAttribute( "sessionManagerAudienceList" );
      // Get the iterator of existing audiences on the promotionWebRulesForm
      Iterator managerAudienceIterator = sessionManagerAudienceList.iterator();

      Set setOfWebRulesManagerAudienceToRemove = new LinkedHashSet();

      // Iterate over the webRulesAudience
      while ( managerAudienceIterator.hasNext() )
      {
        PromotionManagerWebRulesAudience promotionManagerWebRulesAudience = (PromotionManagerWebRulesAudience)managerAudienceIterator.next();

        if ( deletedManagerWebRulesAudienceNames.contains( promotionManagerWebRulesAudience.getAudience().getName() ) )
        {
          setOfWebRulesManagerAudienceToRemove.add( promotionManagerWebRulesAudience );
        }
      }
      sessionManagerAudienceList.removeAll( setOfWebRulesManagerAudienceToRemove );
    }
    return forward;
  }

  public ActionForward removePartnerAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success" );

    PromotionWebRulesForm promoWebRulesForm = (PromotionWebRulesForm)form;

    if ( null != promoWebRulesForm.getDeletePromotionPartnerWebRulesAudience() && promoWebRulesForm.getDeletePromotionPartnerWebRulesAudience().length > 0 )
    {

      // Get the list of names for the audiences being deleted.
      List deletedPartnerWebRulesAudienceNames = ArrayUtil.stringArrayToList( promoWebRulesForm.getDeletePromotionPartnerWebRulesAudience() );

      Set sessionPartnerAudienceList = (LinkedHashSet)request.getSession().getAttribute( "sessionPartnerAudienceList" );
      // Get the iterator of existing audiences on the promotionWebRulesForm
      Iterator partnerAudienceIterator = sessionPartnerAudienceList.iterator();

      Set setOfWebRulesPartnerAudienceToRemove = new LinkedHashSet();

      // Iterate over the webRulesAudience
      while ( partnerAudienceIterator.hasNext() )
      {
        PromotionPartnerWebRulesAudience promotionPartnerWebRulesAudience = (PromotionPartnerWebRulesAudience)partnerAudienceIterator.next();

        if ( deletedPartnerWebRulesAudienceNames.contains( promotionPartnerWebRulesAudience.getAudience().getName() ) )
        {
          setOfWebRulesPartnerAudienceToRemove.add( promotionPartnerWebRulesAudience );
        }
      }
      sessionPartnerAudienceList.removeAll( setOfWebRulesPartnerAudienceToRemove );
    }
    return forward;
  }

  /**
   * Adds a promotionWebRulesAudience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionWebRulesForm promotionWebRulesForm = (PromotionWebRulesForm)form;

    addPromotionWebRulesAudience( request, promotionWebRulesForm, new Long( promotionWebRulesForm.getAudienceId() ) );
    promotionWebRulesForm.setAudienceId( "" );

    return mapping.findForward( "success" );
  }

  /**
   * Manages adding a PromotionWebRulesAudience to the form in preparation for saving.
   * 
   * @param request
   * @param audienceId
   * @param promotionWebRulesForm
   */
  private void addPromotionWebRulesAudience( HttpServletRequest request, PromotionWebRulesForm promotionWebRulesForm, Long audienceId )
  {
    // Build the audience
    Audience audience = getAudienceService().getAudienceById( audienceId, null );
    audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );

    Promotion promotion = null;

    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      promotion = getPromotionService().getPromotionById( new Long( promotionWebRulesForm.getPromotionId() ) );
    }

    // Build a new promotionWebRulesAudience
    PromotionWebRulesAudience promotionWebRulesAudience = new PromotionWebRulesAudience();
    promotionWebRulesAudience.setAudience( audience );
    promotionWebRulesAudience.setPromotion( promotion );

    Set promoWebRulesAudienceSet = (LinkedHashSet)request.getSession().getAttribute( "sessionAudienceList" );
    if ( promoWebRulesAudienceSet == null )
    {
      promoWebRulesAudienceSet = new LinkedHashSet();
    }

    promoWebRulesAudienceSet.add( promotionWebRulesAudience );

    request.getSession().setAttribute( "sessionAudienceList", promoWebRulesAudienceSet );
  }

  public ActionForward addManagerAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionWebRulesForm promotionWebRulesForm = (PromotionWebRulesForm)form;

    addPromotionManagerWebRulesAudience( request, promotionWebRulesForm, new Long( promotionWebRulesForm.getManagerAudienceId() ) );

    promotionWebRulesForm.setManagerAudienceId( "" );

    return mapping.findForward( "success" );

  }

  private void addPromotionManagerWebRulesAudience( HttpServletRequest request, PromotionWebRulesForm promotionWebRulesForm, Long audienceId )
  {
    // Build the audience
    Audience audience = getAudienceService().getAudienceById( audienceId, null );
    audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );

    Promotion promotion = null;

    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      promotion = getPromotionService().getPromotionById( new Long( promotionWebRulesForm.getPromotionId() ) );
    }

    AssociationRequestCollection goalAssociationRequestCollection = new AssociationRequestCollection();
    goalAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS ) );

    Promotion attachedGoalPromotion = getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), goalAssociationRequestCollection );

    // Build a new promotionManagerWebRulesAudience
    PromotionManagerWebRulesAudience promotionManagerWebRulesAudience = new PromotionManagerWebRulesAudience();
    promotionManagerWebRulesAudience.setAudience( audience );
    promotionManagerWebRulesAudience.setPromotion( attachedGoalPromotion );

    Set promoManagerWebRulesAudienceSet = (LinkedHashSet)request.getSession().getAttribute( "sessionManagerAudienceList" );
    if ( promoManagerWebRulesAudienceSet == null )
    {
      promoManagerWebRulesAudienceSet = new LinkedHashSet();
    }

    promoManagerWebRulesAudienceSet.add( promotionManagerWebRulesAudience );

    request.getSession().setAttribute( "sessionManagerAudienceList", promoManagerWebRulesAudienceSet );
  }

  public ActionForward addPartnerAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionWebRulesForm promotionWebRulesForm = (PromotionWebRulesForm)form;

    addPromotionPartnerWebRulesAudience( request, promotionWebRulesForm, new Long( promotionWebRulesForm.getPartnerAudienceId() ) );

    promotionWebRulesForm.setPartnerAudienceId( "" );

    return mapping.findForward( "success" );

  }

  private void addPromotionPartnerWebRulesAudience( HttpServletRequest request, PromotionWebRulesForm promotionWebRulesForm, Long audienceId )
  {
    // Build the audience
    Audience audience = getAudienceService().getAudienceById( audienceId, null );
    audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );

    Promotion promotion = null;
    Promotion promo = null;

    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      promotion = getPromotionService().getPromotionById( new Long( promotionWebRulesForm.getPromotionId() ) );
    }

    AssociationRequestCollection goalAssociationRequestCollection = new AssociationRequestCollection();
    goalAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_PARTNERS ) );

    Promotion attachedGoalPromotion = getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), goalAssociationRequestCollection );

    // Build a new promotionPartnerWebRulesAudience
    PromotionPartnerWebRulesAudience promotionPartnerWebRulesAudience = new PromotionPartnerWebRulesAudience();
    promotionPartnerWebRulesAudience.setAudience( audience );
    promotionPartnerWebRulesAudience.setPromotion( attachedGoalPromotion );

    Set promoPartnerWebRulesAudienceSet = (LinkedHashSet)request.getSession().getAttribute( "sessionPartnerAudienceList" );
    if ( promoPartnerWebRulesAudienceSet == null )
    {
      promoPartnerWebRulesAudienceSet = new LinkedHashSet();
    }

    promoPartnerWebRulesAudienceSet.add( promotionPartnerWebRulesAudience );

    request.getSession().setAttribute( "sessionPartnerAudienceList", promoPartnerWebRulesAudienceSet );
  }

  private int getNbrOfPaxsInCriteriaAudience( Audience audience )
  {
    int nbrOfPaxInCriteriaAudience = 0;

    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );

    nbrOfPaxInCriteriaAudience = paxFormattedValueList.size();

    return nbrOfPaxInCriteriaAudience;
  }

  /**
   * Makes a request to the Audience builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionWebRulesForm promotionWebRulesForm = (PromotionWebRulesForm)form;

    String promotionId = promotionWebRulesForm.getPromotionId();

    ActionForward returnForward = mapping.findForward( "audienceLookup" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap, true );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );

    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( PromotionWebRulesForm.SESSION_KEY, promotionWebRulesForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );

    return forward;
  }

  /**
   * Handles the return from the audience builder. This will look for the AudienceId on the request,
   * load the audience and the promotion and build a new PromotionWebRulesAudience which is set onto
   * the form in preparation to saving the webRules.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    PromotionWebRulesForm promotionWebRulesForm = (PromotionWebRulesForm)form;
    // Get the promotionWebRulesForm from the session as it was placed there before we called out to
    // build
    // the audience.
    PromotionWebRulesForm sessionPromotionWebRulesForm = (PromotionWebRulesForm)request.getSession().getAttribute( PromotionWebRulesForm.SESSION_KEY );

    if ( sessionPromotionWebRulesForm != null )
    {
      try
      {
        BeanUtils.copyProperties( promotionWebRulesForm, sessionPromotionWebRulesForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    // Get the newly created audienceId from the request.
    Long audienceId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      try
      {
        String audienceIdString = (String)clientStateMap.get( "audienceId" );
        if ( audienceIdString != null && !audienceIdString.equals( "" ) )
        {
          audienceId = new Long( audienceIdString );
        }
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

    }
    catch( InvalidClientStateException e )
    {
      // audienceId is optional - do nothing
      // throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addPromotionWebRulesAudience( request, promotionWebRulesForm, audienceId );
    }

    return forward;
  }

  public ActionForward prepareManagerAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionWebRulesForm promotionWebRulesForm = (PromotionWebRulesForm)form;

    String promotionId = promotionWebRulesForm.getPromotionId();

    ActionForward returnForward = mapping.findForward( "managerAudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap, true );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );

    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( PromotionWebRulesForm.SESSION_KEY, promotionWebRulesForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );

    return forward;
  }

  public ActionForward preparePartnerAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PromotionWebRulesForm promotionWebRulesForm = (PromotionWebRulesForm)form;

    String promotionId = promotionWebRulesForm.getPromotionId();

    ActionForward returnForward = mapping.findForward( "partnerAudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap, true );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );

    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( PromotionWebRulesForm.SESSION_KEY, promotionWebRulesForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );

    return forward;
  }

  public ActionForward returnManagerAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    PromotionWebRulesForm promotionWebRulesForm = (PromotionWebRulesForm)form;
    // Get the promotionWebRulesForm from the session as it was placed there before we called out to
    // build
    // the audience.
    PromotionWebRulesForm sessionPromotionWebRulesForm = (PromotionWebRulesForm)request.getSession().getAttribute( PromotionWebRulesForm.SESSION_KEY );

    if ( sessionPromotionWebRulesForm != null )
    {
      try
      {
        BeanUtils.copyProperties( promotionWebRulesForm, sessionPromotionWebRulesForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    // Get the newly created audienceId from the request.
    Long audienceId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      try
      {
        String audienceIdString = (String)clientStateMap.get( "audienceId" );
        if ( audienceIdString != null && !audienceIdString.equals( "" ) )
        {
          audienceId = new Long( audienceIdString );
        }
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

    }
    catch( InvalidClientStateException e )
    {
      // audienceId is optional - do nothing
      // throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addPromotionManagerWebRulesAudience( request, promotionWebRulesForm, audienceId );
    }

    return forward;
  }

  public ActionForward returnPartnerAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    PromotionWebRulesForm promotionWebRulesForm = (PromotionWebRulesForm)form;
    // Get the promotionWebRulesForm from the session as it was placed there before we called out to
    // build
    // the audience.
    PromotionWebRulesForm sessionPromotionWebRulesForm = (PromotionWebRulesForm)request.getSession().getAttribute( PromotionWebRulesForm.SESSION_KEY );

    if ( sessionPromotionWebRulesForm != null )
    {
      try
      {
        BeanUtils.copyProperties( promotionWebRulesForm, sessionPromotionWebRulesForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    // Get the newly created audienceId from the request.
    Long audienceId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      try
      {
        String audienceIdString = (String)clientStateMap.get( "audienceId" );
        if ( audienceIdString != null && !audienceIdString.equals( "" ) )
        {
          audienceId = new Long( audienceIdString );
        }
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

    }
    catch( InvalidClientStateException e )
    {
      // audienceId is optional - do nothing
      // throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addPromotionPartnerWebRulesAudience( request, promotionWebRulesForm, audienceId );
    }
    return forward;
  }

  /**
   * Get the audienceService.
   * 
   * @return AudienceService.
   */
  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  /**
   * Gets an AudienceService
   * 
   * @return AudienceService
   */
  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  /**
   * Gets a HierarchyService
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }
}
