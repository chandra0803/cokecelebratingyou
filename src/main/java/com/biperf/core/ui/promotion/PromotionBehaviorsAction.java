/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionBehaviorsAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionBehaviorUpdateAssociation;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.PromotionBehaviorsValueBean;
import com.biperf.core.value.PromotionBehaviorsValueBeanComparator;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.exception.CmsLiveDataException;

/*
 * PromotionBehaviorsAction <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Oct
 * 10, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class PromotionBehaviorsAction extends PromotionBaseDispatchAction
{
  /** Log */
  public static final Log logger = LogFactory.getLog( PromotionBehaviorsAction.class );

  /**
   * RETURN_ACTION_URL_PARAM
   */
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  public static final String CHECKED = "on";

  /**
   * Method which is dispatched to when there is no value for specified request parameter included
   * in the request.
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
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
    ActionMessages errors = new ActionMessages();
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    PromotionBehaviorsForm behaviorsForm = (PromotionBehaviorsForm)form;

    Promotion promotion;
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );

      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BEHAVIORS ) );

      AbstractRecognitionPromotion attachedPromotion = (AbstractRecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );

      ( (AbstractRecognitionPromotion)promotion ).setPromotionBehaviors( attachedPromotion.getPromotionBehaviors() );
    }
    else
    {
      // promotion = new RecognitionPromotion();
      Long promotionId;
      if ( RequestUtils.containsAttribute( request, "promotionId" ) )
      {
        promotionId = RequestUtils.getRequiredAttributeLong( request, "promotionId" );
      }
      else
      {
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
            String promotionIdString = (String)clientStateMap.get( "promotionId" );
            promotionId = new Long( promotionIdString );
          }
          catch( ClassCastException e )
          {
            promotionId = (Long)clientStateMap.get( "promotionId" );
          }
          if ( promotionId == null )
          {
            errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "promotionId as part of clientState" ) );
            saveErrors( request, errors );
            return mapping.findForward( forwardTo );
          }
        }
        catch( InvalidClientStateException e )
        {
          throw new IllegalArgumentException( "request parameter clientState was missing" );
        }
      }
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BEHAVIORS ) );

      promotion = (AbstractRecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
    }

    behaviorsForm.load( promotion );

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

    PromotionBehaviorsForm behaviorsForm = (PromotionBehaviorsForm)form;

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
      return forward;
    }

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ) != null && request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      promotion = getPromotionService().getPromotionById( behaviorsForm.getPromotionId() );

    }

    try
    {
      PromotionBehaviorUpdateAssociation pbua = new PromotionBehaviorUpdateAssociation( behaviorsForm.toDomain( behaviorsForm.isRecognitionPromotion() ) );
      promotion = getPromotionService().savePromotion( behaviorsForm.getPromotionId(), pbua );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
    }

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
        if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          forward = mapping.findForward( "saveAndContinueApprovals" );
        }
        else
        {
          forward = getWizardNextPage( mapping, request, promotion );
        }
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }
    return forward;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addBehavior( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    PromotionBehaviorsForm behaviorsForm = (PromotionBehaviorsForm)form;

    CMAssetService service = (CMAssetService)getService( CMAssetService.BEAN_NAME );
    PromotionService promoService = (PromotionService)getService( PromotionService.BEAN_NAME );

    ActionMessages errors = new ActionMessages();
    String newBehavior = behaviorsForm.getNewBehavior();
    boolean behaviorExist = false;
    try
    {
      if ( newBehavior != null && newBehavior.length() > 0 )
      {

        // Duplicate behavior check
        List typeList;
        if ( behaviorsForm.isRecognitionPromotion() )
        {
          typeList = PromoRecognitionBehaviorType.getList();
          for ( int i = 0; i < typeList.size(); i++ )
          {
            PromoRecognitionBehaviorType type = (PromoRecognitionBehaviorType)typeList.get( i );
            if ( type.getName().equalsIgnoreCase( behaviorsForm.getNewBehavior() ) )
            {
              behaviorExist = true;
              errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.DUPLICATE_BEHAVIOR" ) ); // No
                                                                                                                                // entry
              // added
            }
          }
        }
        else
        {
          typeList = PromoNominationBehaviorType.getList();
          for ( int i = 0; i < typeList.size(); i++ )
          {
            PromoNominationBehaviorType type = (PromoNominationBehaviorType)typeList.get( i );
            if ( type.getName().equalsIgnoreCase( behaviorsForm.getNewBehavior() ) )
            {
              behaviorExist = true;
              errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.DUPLICATE_BEHAVIOR" ) ); // No
                                                                                                                                // entry
              // added
            }
          }
        }

        // Duplicate behavior check
        if ( !behaviorExist )
        {
          if ( behaviorsForm.isRecognitionPromotion() )
          {
            service.addPickListItem( "picklist.promo.recognition.behavior.items", behaviorsForm.getNewBehavior() );
          }
          else if ( behaviorsForm.isNominationPromotion() )
          {
            service.addPickListItem( "picklist.promo.nomination.behavior.items", behaviorsForm.getNewBehavior() );

          }
        }
      }
      else
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_NEW_BEHAVIOR" ) ); // No
                                                                                                                       // entry
                                                                                                                       // added
      }
    }
    catch( ServiceErrorException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_NEW_BEHAVIOR" ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {

      List typeList;
      if ( behaviorsForm.isRecognitionPromotion() )
      {
        String[] behaviorList = behaviorsForm.getBehaviors();
        String[] newList;
        if ( behaviorList != null && behaviorList.length > 0 )
        {
          newList = new String[behaviorList.length + 1];
          for ( int i = 0; i < behaviorList.length; i++ )
          {
            String behavior = behaviorList[i];
            newList[i] = behavior;
          }
        }
        else
        {
          newList = new String[1];
        }
        typeList = PromoRecognitionBehaviorType.getList();

        PromoRecognitionBehaviorType prbt = null;
        for ( int i = 0; i < typeList.size(); i++ )
        {
          PromoRecognitionBehaviorType type = (PromoRecognitionBehaviorType)typeList.get( i );
          if ( type.getName().equals( behaviorsForm.getNewBehavior() ) )
          {
            prbt = type;
          }
        }

        newList[newList.length - 1] = prbt.getCode();
        behaviorsForm.setBehaviors( newList );
      }
      else
      {
        List<PromotionBehaviorsValueBean> behaviorList = behaviorsForm.getPromoNominationBehaviorsVBList();
        List<PromotionBehaviorsValueBean> newList = behaviorsForm.getPromoNominationBehaviorsVBList();

        if ( behaviorList != null && behaviorList.size() > 0 )
        {
          newList = new ArrayList<PromotionBehaviorsValueBean>();
          for ( int i = 0; i < behaviorList.size(); i++ )
          {
            newList.add( behaviorList.get( i ) );
          }
        }
        else
        {
          newList = new ArrayList<PromotionBehaviorsValueBean>();
        }

        typeList = PromoNominationBehaviorType.getList();

        PromoNominationBehaviorType prbt = null;
        for ( int i = 0; i < typeList.size(); i++ )
        {
          PromoNominationBehaviorType type = (PromoNominationBehaviorType)typeList.get( i );
          if ( type.getName().equals( behaviorsForm.getNewBehavior() ) )
          {
            prbt = type;
          }
        }
        newList.add( new PromotionBehaviorsValueBean( CHECKED, prbt.getName(), prbt.getCode(), "" ) );
        Collections.sort( newList, new PromotionBehaviorsValueBeanComparator() );
        behaviorsForm.setPromoNominationBehaviorsVBList( newList );
      }
      behaviorsForm.setNewBehavior( "" );
    }
    return forward;
  }

  /**
   * Delete a behavior from the system.  Cannot delete a behavior if it is in use by another promotion.
   */
  public ActionForward removeBehavior( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    PromotionBehaviorsForm behaviorsForm = (PromotionBehaviorsForm)form;
    ActionMessages errors = new ActionMessages();

    CMAssetService cmService = (CMAssetService)getService( CMAssetService.BEAN_NAME );

    String codeToRemove = behaviorsForm.getRemoveBehaviorCode();

    // Make sure there's a behavior code to work on
    if ( StringUtil.isValid( codeToRemove ) )
    {
      // Get list of promotion IDs using the behavior - make sure current promotion is the only one,
      // if any
      List<Long> promoIdsUsingBehavior = getPromotionService().getPromotionIdsForBehavior( codeToRemove );

      if ( promoIdsUsingBehavior == null || promoIdsUsingBehavior.isEmpty() || promoIdsUsingBehavior.size() == 1 && promoIdsUsingBehavior.get( 0 ).equals( behaviorsForm.getPromotionId() ) )
      {
        // We're okay to remove the behavior - remove from promotion and CM
        if ( behaviorsForm.isNominationPromotion() )
        {
          NominationPromotion nomPromo = (NominationPromotion)behaviorsForm.toDomain( false );

          // Remove from promotion object
          Iterator<PromotionBehavior> behaviorIterator = nomPromo.getPromotionBehaviors().iterator();
          while ( behaviorIterator.hasNext() )
          {
            PromotionBehavior behavior = behaviorIterator.next();

            if ( behavior.getPromotionBehaviorType().getCode().equals( codeToRemove ) )
            {
              behaviorIterator.remove();
            }
          }

          // Save updated promotion object
          try
          {
            PromotionBehaviorUpdateAssociation pbua = new PromotionBehaviorUpdateAssociation( nomPromo );
            nomPromo = (NominationPromotion)getPromotionService().savePromotion( behaviorsForm.getPromotionId(), pbua );
          }
          catch( UniqueConstraintViolationException e )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
          }

          // Have changes in promotion object reflected in form
          List<PromotionBehaviorsValueBean> behaviorBeanList = behaviorsForm.getPromoNominationBehaviorsVBList();
          if ( behaviorBeanList != null )
          {
            Iterator<PromotionBehaviorsValueBean> behaviorBeanIterator = behaviorBeanList.iterator();
            while ( behaviorBeanIterator.hasNext() )
            {
              PromotionBehaviorsValueBean behaviorBean = behaviorBeanIterator.next();

              if ( codeToRemove.equals( behaviorBean.getPromoNominationBehaviorTypeCode() ) )
              {
                behaviorBeanIterator.remove();
              }
            }
          }

          // Remove the entry from CM
          List<Content> contents = cmService.getContentsForAsset( "picklist.promo.nomination.behavior.items" );
          for ( Iterator<Content> contentIter = contents.iterator(); contentIter.hasNext(); )
          {
            Content content = contentIter.next();

            if ( content.getContentDataMap().get( "CODE" ).equals( codeToRemove ) )
            {
              try
              {
                cmService.removeContent( "picklist.promo.nomination.behavior.items", content.getId() );
              }
              catch( CmsLiveDataException e )
              {
                errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "system.cmservice.errors.DELETE_ERR" ) );
              }
            }
          }
        }
      }
      else
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.BEHAVIOR_IN_USE" ) );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_REMOVE_BEHAVIOR" ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

}
