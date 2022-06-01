/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/ExpiredPromotionListController.java,v $
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
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * Implements the controller for the ExpiredPromotionList page.
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
 * <td>June 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ExpiredPromotionListController extends BaseController
{
  /**
   * Tiles controller for the PromotionList page
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) Overridden from
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // use a set so we don't have to worry about duplicate promotions getting added
    Set expiredSet = new LinkedHashSet();
    // get the list of all expired promotions
    List promotionList = new ArrayList();

    if ( RequestUtils.containsParam( request, "promotionType" ) )
    {
      String promotionTypeCode = RequestUtils.getRequiredParamString( request, "promotionType" );

      if ( promotionTypeCode.equals( "all" ) )
      {
        promotionList = getPromotionService().getAllExpired();
      }
      else
      {
        promotionList = getPromotionService().getAllExpiredByType( PromotionType.lookup( promotionTypeCode ) );
      }
    }
    else
    {
      promotionList = getPromotionService().getAllExpired();
    }

    if ( promotionList != null && promotionList.size() > 0 )
    {
      Iterator it = promotionList.iterator();
      while ( it.hasNext() )
      {
        Promotion promotion = (Promotion)it.next();

        if ( promotion.isProductClaimPromotion() )
        {
          ProductClaimPromotion pcPromotion = (ProductClaimPromotion)promotion;
          // 1. if Parent Has Child(ren), parent must appear first then its children
          if ( pcPromotion.getChildrenCount() > 0 )
          {
            expiredSet.add( promotion ); // add expired parent

            // find its children in the List
            Iterator itForChildren = promotionList.iterator();

            while ( itForChildren.hasNext() )
            {
              Promotion childPromo = (Promotion)itForChildren.next();
              if ( childPromo.isProductClaimPromotion() )
              {
                ProductClaimPromotion child = (ProductClaimPromotion)childPromo;
                Promotion parent = child.getParentPromotion();
                if ( parent != null && parent.getId() != null && parent.getId().equals( promotion.getId() ) )
                {
                  if ( child.isExpired() )
                  {
                    expiredSet.add( child ); // add expired child
                  }
                }
              }
            }
          }
          // 2. Child Has Parent
          else if ( pcPromotion.getParentPromotion() != null )
          {
            Promotion parentPromotion = pcPromotion.getParentPromotion();
            if ( parentPromotion.isExpired() ) // if parent is also Expired
            {
              expiredSet.add( parentPromotion ); // add expired parent
              expiredSet.add( promotion ); // and then add expired child
            }
            else
            {
              expiredSet.add( promotion ); // just add expired child
            }
          }
          // 3. Plain Old Promotion (i.e. not a parent, has no children)
          else
          {
            expiredSet.add( promotion ); // add plain old promotion
          }
        }
        // Bug Fix for 17515. The Form List was from 2 different collection set(claimForm and quiz)
        // form,hence was not sorted properly.
        // The fix is a work around process where the quiz Set is also got in the claimForm set and
        // then sorted from a single collection
        // Set since the list is from 2 different collection set(Form Library and quiz library) form
        // is not sorted properly
        else if ( promotion.isQuizPromotion() )
        {
          promotion.setClaimForm( new ClaimForm() );
          promotion.getClaimForm().setName( ( (QuizPromotion)promotion ).getQuiz().getName() );
          expiredSet.add( promotion );
        }
        // 3. Plain Old Promotion (i.e. not a parent, has no children)
        else
        {
          expiredSet.add( promotion ); // add plain old promotion
        }

        // 1. Child Has Parent
        /*
         * if ( promotion.getParentPromotion() != null ) { Promotion parentPromotion =
         * promotion.getParentPromotion(); if ( parentPromotion.isExpired() ) //if parent is also
         * Under Construction { expiredSet.add( parentPromotion ); //add under construction parent
         * expiredSet.add( promotion ); //and then add under construction child } else {
         * expiredSet.add( promotion ); //just add under construction child } } //Plain Old
         * Promotion (i.e. not a parent, has no children) else { expiredSet.add( promotion ); //add
         * plain old promotion }
         */
      }
    }
    request.setAttribute( "expiredList", expiredSet );
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
