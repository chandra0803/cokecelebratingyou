/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCopyHolder;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionCopyForm.
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
 * <td>Jul 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionCopyForm extends BaseActionForm
{
  private String promotionId;
  private String newPromotionName;
  private String newParentPromotionName;
  private String newParentFamilyPromotionName;
  private String entireFamilySelected;
  private Set childPromotions;
  private List promotionCopies = new ArrayList();
  private int childPromotionCount;

  private String oldPromotionName;
  private String oldPromotionTypeName;
  private String oldPromotionTypeCode;

  /**
   * Load the form
   * 
   * @param promotion
   */
  public void load( Promotion promotion )
  {
    this.setOldPromotionName( promotion.getName() );
    this.setOldPromotionTypeName( promotion.getPromotionType().getName() );
    this.setOldPromotionTypeCode( promotion.getPromotionType().getCode() );
    if ( promotion.isProductClaimPromotion() )
    {
      ProductClaimPromotion pcPromotion = (ProductClaimPromotion)promotion;

      if ( pcPromotion.getChildPromotionCount() > 0 )
      {

        childPromotionCount = pcPromotion.getChildPromotionCount();

        Set children = pcPromotion.getChildPromotions();
        this.setChildPromotions( children );

        Iterator iter = pcPromotion.getChildPromotions().iterator();

        while ( iter.hasNext() )
        {
          Promotion child = (Promotion)iter.next();
          PromotionCopyHolder pch = new PromotionCopyHolder();
          pch.setCurrentName( child.getName() );
          pch.setNewName( "" );

          promotionCopies.add( pch );
        }
      }
    }
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    childPromotionCount = 0;

    int count = 0;
    if ( request.getParameter( "promotionCopiesCount" ) != null )
    {
      count = Integer.parseInt( request.getParameter( "promotionCopiesCount" ) );
    }

    for ( int i = 0; i < count; i++ )
    {
      promotionCopies.add( new PromotionCopyHolder() );
    }
  }

  /**
   * @return promotionId
   */
  public String getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return newPromotionName
   */
  public String getNewPromotionName()
  {
    return newPromotionName;
  }

  /**
   * @param newPromotionName
   */
  public void setNewPromotionName( String newPromotionName )
  {
    this.newPromotionName = newPromotionName;
  }

  /**
   * @return childPromotionCount
   */
  public int getChildPromotionCount()
  {
    return childPromotionCount;
  }

  /**
   * @param childPromotionCount
   */
  public void setChildPromotionCount( int childPromotionCount )
  {
    this.childPromotionCount = childPromotionCount;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    if ( childPromotionCount > 0 )
    {
      if ( entireFamilySelected == null || entireFamilySelected.equals( "" ) )
      {
        String fields = CmsResourceBundle.getCmsBundle().getString( "promotion.copy.PARENT_PROMOTION_ONLY" ) + " or ";
        fields += CmsResourceBundle.getCmsBundle().getString( "promotion.copy.ENTIRE_PROMOTION_FAMILY" );
        errors.add( "promoToCopy", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, fields ) );
      }
      else
      {
        // entire promotion family
        if ( entireFamilySelected != null && entireFamilySelected.equals( "true" ) )
        {
          if ( newParentFamilyPromotionName == null || newParentFamilyPromotionName.length() == 0 )
          {
            errors.add( "entireFamilySelected",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.copy.NEW_PARENT_PROMOTION_NAME" ) ) );
          }
          // To fix bug 21222
          if ( newPromotionName != null && newPromotionName.length() > 50 )
          {
            errors.add( "promotionName", new ActionMessage( "promotion.basics.errors.NAME_TOO_LONG" ) );
          }
          for ( int i = 0; i < promotionCopies.size(); i++ )
          {
            PromotionCopyHolder holder = (PromotionCopyHolder)promotionCopies.get( i );
            if ( holder.getNewName() == null || holder.getNewName().length() == 0 )
            {
              errors.add( "entireFamilySelected", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "New '" + holder.getCurrentName() + "' Promotion Name" ) );
            }
          }
          // parent promotion only
        }
        else if ( entireFamilySelected != null && entireFamilySelected.equals( "false" ) )
        {
          if ( newParentPromotionName == null || newParentPromotionName.length() == 0 )
          {
            errors.add( "notEntireFamilySelected",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.copy.NEW_PARENT_PROMOTION_NAME" ) ) );
          }
        }
      }
    }
    // parent promo with no children or child promo
    else
    {
      if ( newPromotionName == null || newPromotionName.length() == 0 )
      {
        errors.add( "newPromotionName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.copy.NEW_PROMOTION_NAME" ) ) );
      }
      if ( newPromotionName != null && newPromotionName.length() > 50 )
      {
        errors.add( "promotionName", new ActionMessage( "promotion.basics.errors.NAME_TOO_LONG" ) );
      }

    }
    return errors;
  }

  /**
   * @return newParentPromotionName
   */
  public String getNewParentPromotionName()
  {
    return newParentPromotionName;
  }

  /**
   * @param newParentPromotionName
   */
  public void setNewParentPromotionName( String newParentPromotionName )
  {
    this.newParentPromotionName = newParentPromotionName;
  }

  /**
   * @return childPromotions
   */
  public Set getChildPromotions()
  {
    return childPromotions;
  }

  /**
   * @param childPromotions
   */
  public void setChildPromotions( Set childPromotions )
  {
    this.childPromotions = childPromotions;
  }

  /**
   * @return promotionCopies
   */
  public List getPromotionCopies()
  {
    return promotionCopies;
  }

  /**
   * @param promotionCopies
   */
  public void setPromotionCopies( List promotionCopies )
  {
    this.promotionCopies = promotionCopies;
  }

  /**
   * @param index
   * @return PromotionCopyHolder
   */
  public PromotionCopyHolder getPromotionCopiesItem( int index )
  {
    return (PromotionCopyHolder)promotionCopies.get( index );
  }

  /**
   * @return int number of promotion copies
   */
  public int getPromotionCopiesCount()
  {
    if ( getPromotionCopies() == null )
    {
      return 0;
    }
    return getPromotionCopies().size();
  }

  /**
   * @return newParentFamilyPromotionName
   */
  public String getNewParentFamilyPromotionName()
  {
    return newParentFamilyPromotionName;
  }

  /**
   * @param newParentFamilyPromotionName
   */
  public void setNewParentFamilyPromotionName( String newParentFamilyPromotionName )
  {
    this.newParentFamilyPromotionName = newParentFamilyPromotionName;
  }

  /**
   * @return entireFamilySelected
   */
  public String getEntireFamilySelected()
  {
    return entireFamilySelected;
  }

  /**
   * @param entireFamilySelected
   */
  public void setEntireFamilySelected( String entireFamilySelected )
  {
    this.entireFamilySelected = entireFamilySelected;
  }

  public String getOldPromotionName()
  {
    return oldPromotionName;
  }

  public void setOldPromotionName( String oldPromotionName )
  {
    this.oldPromotionName = oldPromotionName;
  }

  public String getOldPromotionTypeName()
  {
    return oldPromotionTypeName;
  }

  public void setOldPromotionTypeName( String oldPromotionTypeName )
  {
    this.oldPromotionTypeName = oldPromotionTypeName;
  }

  public String getOldPromotionTypeCode()
  {
    return oldPromotionTypeCode;
  }

  public void setOldPromotionTypeCode( String oldPromotionTypeCode )
  {
    this.oldPromotionTypeCode = oldPromotionTypeCode;
  }

}
