/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionListController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

/**
 * Implements the controller for the PromotionList page.
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
 * <td>June 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionListController extends BaseController
{
  /**
   * Tiles controller for the PromotionList page Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // use a set so we don't have to worry about duplicate promotions getting added
    Set completeSet = new LinkedHashSet();
    Set underConstructionSet = new LinkedHashSet();
    Set liveSet = new LinkedHashSet();

    // TODO look for the request param which defines the promotions with a specific type to be
    // displayed.
    List promotionList = new ArrayList();

    if ( RequestUtils.containsParam( request, "promotionType" ) )
    {
      String promotionTypeCode = RequestUtils.getRequiredParamString( request, "promotionType" );

      if ( promotionTypeCode.equals( "all" ) )
      {
        promotionList = getPromotionService().getAllNonExpired();
      }
      else
      {
        promotionList = getPromotionService().getAllNonExpiredByType( PromotionType.lookup( promotionTypeCode ) );
      }
    }
    else
    {
      promotionList = getPromotionService().getAllNonExpired();
    }

    Iterator it = promotionList.iterator();

    ProductClaimPromotion pcPromotion;

    while ( it.hasNext() )
    {
      Promotion promotion = (Promotion)it.next();

      // ******** Remove the purl promotion. **********
      if ( promotion instanceof RecognitionPromotion )
      {
        if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          RecognitionPromotion recPromotion = (RecognitionPromotion)promotion;
          if ( recPromotion.isIncludePurl() || recPromotion.isIncludeCelebrations() )
          {
            continue;
          }
        }
      }

      // ********** Under Construction *************************
      if ( promotion.isUnderConstruction() )
      {
        if ( promotion.isProductClaimPromotion() )
        {
          pcPromotion = (ProductClaimPromotion)promotion;

          // 1. if Parent Has Child(ren), parent must appear first then its children
          if ( pcPromotion.getChildrenCount() > 0 )
          {
            underConstructionSet.add( promotion ); // add under construction parent

            // find its children in the List
            Iterator itForChildren = promotionList.iterator();

            while ( itForChildren.hasNext() )
            {
              Promotion promo = (Promotion)itForChildren.next();
              if ( promo.getPromotionType().equals( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) ) )
              {
                ProductClaimPromotion child = (ProductClaimPromotion)promo;
                ProductClaimPromotion parent = child.getParentPromotion();
                if ( parent != null && parent.getId() != null && parent.getId().equals( promotion.getId() ) )
                {
                  if ( child.isUnderConstruction() )
                  {
                    underConstructionSet.add( child ); // add under construction child
                  }
                }
              }
            }
          }
          // 3. Plain Old Promotion (i.e. not a parent, has no children)
          else if ( !promotion.hasParent() )
          {
            underConstructionSet.add( promotion ); // add plain old promotion
          }
        }
        // Bug Fix for 17515. The Form List was from 2 different collection set(claimForm and quiz)
        // form,hence was not sorted properly.
        // The fix is a work around process where the quiz Set is also got in the claimForm set and
        // then sorted from a single collection
        // Set since the list is from 2 different collection set(Form Library and quiz library) form
        // is not sorted properly
        if ( promotion.isQuizPromotion() )
        {
          promotion.setClaimForm( new ClaimForm() );
          promotion.getClaimForm().setName( ( (QuizPromotion)promotion ).getQuiz().getName() );
          underConstructionSet.add( promotion );
        }
        // 3. Plain Old Promotion (i.e. not a parent, has no children)
        else if ( !promotion.hasParent() )
        {
          underConstructionSet.add( promotion ); // add plain old promotion
        }
      }
      // *********** Complete *********************************
      else if ( promotion.isComplete() )
      {
        if ( promotion.isProductClaimPromotion() )
        {
          pcPromotion = (ProductClaimPromotion)promotion;

          // 1. if Parent Has Child(ren), parent must appear first then its children
          if ( pcPromotion.getChildrenCount() > 0 )
          {
            completeSet.add( promotion ); // add complete parent

            // find its children in the List
            Iterator itForChildren = promotionList.iterator();

            while ( itForChildren.hasNext() )
            {
              Promotion promo = (Promotion)itForChildren.next();
              if ( promo.getPromotionType().equals( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) ) )
              {
                ProductClaimPromotion child = (ProductClaimPromotion)promo;
                ProductClaimPromotion parent = child.getParentPromotion();
                if ( parent != null && parent.getId() != null && parent.getId().equals( promotion.getId() ) )
                {
                  if ( child.isComplete() )
                  {
                    completeSet.add( child ); // add complete child
                  }
                }
              }
            }
          }
          // 3. Plain Old Promotion (i.e. not a parent, has no children)
          else if ( !promotion.hasParent() )
          {
            completeSet.add( promotion ); // add plain old promotion
          }
        }
        // Bug Fix for 17515. The Form List was from 2 different collection set(claimForm and quiz)
        // form,hence was not sorted properly.
        // The fix is a work around process where the quiz Set is also got in the claimForm set and
        // then sorted from a single collection
        // Set since the list is from 2 different collection set(Form Library and quiz library) form
        // is not sorted properly
        if ( promotion.isQuizPromotion() )
        {
          promotion.setClaimForm( new ClaimForm() );
          promotion.getClaimForm().setName( ( (QuizPromotion)promotion ).getQuiz().getName() );
          completeSet.add( promotion );
        }
        // 3. Plain Old Promotion (i.e. not a parent, has no children)
        else if ( !promotion.hasParent() )
        {
          completeSet.add( promotion ); // add plain old promotion
        }
      }
      // ************ Live *************************************************
      else if ( promotion.isLive() )
      {
        if ( promotion.isProductClaimPromotion() )
        {
          pcPromotion = (ProductClaimPromotion)promotion;

          // 1. if Parent Has Child(ren), parent must appear first then its children
          if ( pcPromotion.getChildrenCount() > 0 )
          {
            liveSet.add( promotion ); // add live parent

            // find its children in the List
            Iterator itForChildren = promotionList.iterator();

            while ( itForChildren.hasNext() )
            {
              Promotion promo = (Promotion)itForChildren.next();
              if ( promo.getPromotionType().equals( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) ) )
              {
                ProductClaimPromotion child = (ProductClaimPromotion)promo;
                ProductClaimPromotion parent = child.getParentPromotion();
                if ( parent != null && parent.getId() != null && parent.getId().equals( promotion.getId() ) )
                {
                  if ( child.isLive() )
                  {
                    liveSet.add( child ); // add live child
                  }
                }
              }
            }
          }
          // 3. Plain Old Promotion (i.e. not a parent, has no children)
          else if ( !promotion.hasParent() )
          {
            liveSet.add( promotion ); // add plain old promotion
          }
        }
        // Bug Fix for 17515. The Form List was from 2 different collection set(claimForm and quiz)
        // form,hence was not sorted properly.
        // The fix is a work around process where the quiz Set is also got in the claimForm set and
        // then sorted from a single collection
        // Set since the list is from 2 different collection set(Form Library and quiz library) form
        // is not sorted properly
        if ( promotion.isQuizPromotion() )
        {
          promotion.setClaimForm( new ClaimForm() );
          promotion.getClaimForm().setName( ( (QuizPromotion)promotion ).getQuiz().getName() );
          liveSet.add( promotion );
        }
        // 3. Plain Old Promotion (i.e. not a parent, has no children)
        else if ( !promotion.hasParent() )
        {
          liveSet.add( promotion ); // add plain old promotion
        }
      }
    }

    request.setAttribute( "underConstructionSet", underConstructionSet );
    request.setAttribute( "completeSet", completeSet );
    request.setAttribute( "liveSet", liveSet );
    request.setAttribute( "promotionTypeList", PromotionType.getPromotionList() );
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
}
