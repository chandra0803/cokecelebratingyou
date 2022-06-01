/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.BudgetSegmentComparator;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionAwardGiversForm.
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
 * <td>jenniget</td>
 * <td>Oct 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAwardGiversForm extends BaseActionForm
{
  private static final PromotionAwardGiversComparator promotionAwardGiversComparator = new PromotionAwardGiversComparator();

  private String method;

  // Promotion display information
  private Long promotionId;
  private String promotionName;
  private String promotionTypeCode;
  private String promotionTypeName;
  private String budgetMasterName;
  private List giverList;
  private List<BudgetSegment> budgetSegmentList = new ArrayList<BudgetSegment>();
  private Long budgetSegmentId;

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // PromotionECardFormBeans. If this is not done, the form wont initialize
    // properly.
    giverList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "giverListCount" ) );
  }

  /**
   * resets the notificationList with empty PromotionNotificationForm beans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionAwardGiversFormBean
      PromotionAwardGiversFormBean giverBean = new PromotionAwardGiversFormBean();
      valueList.add( giverBean );
    }

    return valueList;
  }

  @SuppressWarnings( "unchecked" )
  public void loadBudgetSegmentList( RecognitionPromotion promotion )
  {
    if ( promotion.getBudgetMaster() != null && promotion.getBudgetMaster().getBudgetSegments() != null && promotion.getBudgetMaster().getBudgetSegments().size() > 0 )
    {
      for ( BudgetSegment budgetSegment : promotion.getBudgetMaster().getBudgetSegments() )
      {
        if ( budgetSegment.isCurrentOrFuture() )
        {
          budgetSegmentList.add( budgetSegment );
        }
      }
      if ( budgetSegmentList != null && !budgetSegmentList.isEmpty() )
      {
        Collections.sort( budgetSegmentList, new BudgetSegmentComparator() );
      }
    }
  }

  /**
   * Load the form with the domain object value;
   * 
   * @param promotion
   */
  public void load( RecognitionPromotion promotion, Set usersOrNodes, boolean isNodes )
  {
    // Promotion display information
    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.budgetMasterName = promotion.getBudgetMaster().getBudgetMasterName();

    for ( Iterator iter = usersOrNodes.iterator(); iter.hasNext(); )
    {
      if ( isNodes )
      {
        PromotionAwardGiversFormBean bean = new PromotionAwardGiversFormBean();
        Node node = (Node)iter.next();
        bean.setId( node.getId().toString() );
        bean.setName( node.getName() );
        giverList.add( bean );
      }
      else
      {
        PromotionAwardGiversFormBean bean = new PromotionAwardGiversFormBean();
        User user = (User)iter.next();
        bean.setId( user.getId().toString() );
        bean.setName( user.getLastName() + ", " + user.getFirstName() );
        if ( user.getMiddleName() != null )
        {
          bean.setName( bean.getName() + " " + user.getMiddleName() );
        }
        giverList.add( bean );
      }
    }
    if ( giverList != null && !giverList.isEmpty() )
    {
      // sort list by name
      Collections.sort( giverList, promotionAwardGiversComparator );
    }
  }

  public void load( RecognitionPromotion promotion )
  {
    // Promotion display information
    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionTypeName = promotion.getPromotionType().getName();
    if ( promotion.getBudgetMaster() != null )
    {
      this.budgetMasterName = promotion.getBudgetMaster().getBudgetMasterName();
    }
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return RecognitionPromotion
   */
  public RecognitionPromotion toDomainObject()
  {
    // Create a new Promotion since one was not passed in
    return toDomainObject( new RecognitionPromotion() );
  }

  /**
   * Copy values from the form to the domain object.
   * 
   * @param promotion
   * @return Promotion
   */
  public RecognitionPromotion toDomainObject( RecognitionPromotion promotion )
  {
    for ( Iterator iter = getGiverList().iterator(); iter.hasNext(); )
    {
      PromotionAwardGiversFormBean bean = (PromotionAwardGiversFormBean)iter.next();
      Budget budget = new Budget();
      budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
      budget.setCurrentValue( new BigDecimal( bean.getBudget() ) );
      budget.setOriginalValue( new BigDecimal( bean.getBudget() ) );
      budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

      String budgetTypeCode = promotion.getBudgetMaster().getBudgetType().getCode();
      if ( budgetTypeCode.equals( BudgetType.NODE_BUDGET_TYPE ) )
      {
        Node node = getNodeService().getNodeById( new Long( Long.parseLong( bean.getId() ) ) );
        budget.setNode( node );
      }
      else if ( budgetTypeCode.equals( BudgetType.PAX_BUDGET_TYPE ) )
      {
        User user = getUserService().getUserById( new Long( Long.parseLong( bean.getId() ) ) );
        budget.setUser( user );
      }
      else
      {
        throw new BeaconRuntimeException( "Unknown Budget Type Code: " + budgetTypeCode );
      }

      for ( Iterator<BudgetSegment> iterBudgetSegment = promotion.getBudgetMaster().getBudgetSegments().iterator(); iterBudgetSegment.hasNext(); )
      {
        BudgetSegment budgetSegment = (BudgetSegment)iterBudgetSegment.next();
        if ( budgetSegment.getId().equals( this.budgetSegmentId ) )
        {
          budget.setBudgetSegment( budgetSegment );
          budgetSegment.addBudget( budget );
          promotion.getBudgetMaster().getBudgetSegments().add( budgetSegment );
        }
      }

    }

    return promotion;
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getBudgetMasterName()
  {
    return budgetMasterName;
  }

  public void setBudgetMasterName( String budgetMasterName )
  {
    this.budgetMasterName = budgetMasterName;
  }

  public List getGiverList()
  {
    return giverList;
  }

  public void setGiverList( List giverList )
  {
    this.giverList = giverList;
  }

  /**
   * Accessor for the number of PromotionECardFormBean objects in the list.
   * 
   * @return int
   */
  public int getGiverListCount()
  {
    if ( giverList == null )
    {
      return 0;
    }

    return giverList.size();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionECardFormBean from the value list
   */
  public PromotionAwardGiversFormBean getGiverList( int index )
  {
    try
    {
      return (PromotionAwardGiversFormBean)giverList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public void setBudgetSegmentList( List<BudgetSegment> budgetSegmentList )
  {
    this.budgetSegmentList = budgetSegmentList;
  }

  public List<BudgetSegment> getBudgetSegmentList()
  {
    return budgetSegmentList;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param mapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }
    for ( Iterator iter = getGiverList().iterator(); iter.hasNext(); )
    {
      PromotionAwardGiversFormBean bean = (PromotionAwardGiversFormBean)iter.next();
      if ( bean.getBudget() == null || bean.getBudget().trim().equals( "" ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.awards", "BUDGET" ) ) );
      }
      else
      {
        int value = 0;
        try
        {
          value = Integer.parseInt( bean.getBudget() );
          if ( value < 0 )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.SUB_ZERO", CmsResourceBundle.getCmsBundle().getString( "promotion.awards", "BUDGET" ) ) );
          }
        }
        catch( NumberFormatException e )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.NONNUMERIC", CmsResourceBundle.getCmsBundle().getString( "promotion.awards", "BUDGET" ) ) );
        }
      }
    }
    return errors;
  }
}
