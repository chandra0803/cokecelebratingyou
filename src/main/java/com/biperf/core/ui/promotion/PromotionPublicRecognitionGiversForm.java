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
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
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

public class PromotionPublicRecognitionGiversForm extends BaseActionForm
{
  private static final PromotionPublicRecogGiversComparator promotionPublicRecogGiversComparator = new PromotionPublicRecogGiversComparator();

  private String method;

  // Promotion display information
  private Long promotionId;
  private String promotionName;
  private String promotionTypeCode;
  private String promotionTypeName;
  private String budgetMasterName;
  private List giverList;

  // Budget Segment fields
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
    // PromotionPublicRecogGiversFormBean. If this is not done, the form wont initialize
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
      // create an empty PromotionPublicRecogGiversFormBean
      PromotionPublicRecogGiversFormBean giverBean = new PromotionPublicRecogGiversFormBean();
      valueList.add( giverBean );
    }

    return valueList;
  }

  public void loadBudgetSegmentList( RecognitionPromotion promotion )
  {
    if ( promotion.getPublicRecogBudgetMaster().getBudgetSegments() != null && promotion.getPublicRecogBudgetMaster().getBudgetSegments().size() > 0 )
    {
      for ( BudgetSegment budgetSegment : promotion.getPublicRecogBudgetMaster().getBudgetSegments() )
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

  public void loadBudgetSegmentList( NominationPromotion promotion )
  {
    if ( promotion.getPublicRecogBudgetMaster().getBudgetSegments() != null && promotion.getPublicRecogBudgetMaster().getBudgetSegments().size() > 0 )
    {
      for ( BudgetSegment budgetSegment : promotion.getPublicRecogBudgetMaster().getBudgetSegments() )
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
  public void load( Promotion promotion, Set usersOrNodes, boolean isNodes )
  {
    // Promotion display information
    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.budgetMasterName = null;

    // Fields that depend on type of promotion
    if ( promotion.isRecognitionPromotion() )
    {
      budgetMasterName = ( (RecognitionPromotion)promotion ).getPublicRecogBudgetMaster().getBudgetMasterName();
    }
    else if ( promotion.isNominationPromotion() )
    {
      budgetMasterName = ( (NominationPromotion)promotion ).getPublicRecogBudgetMaster().getBudgetMasterName();
    }

    for ( Iterator iter = usersOrNodes.iterator(); iter.hasNext(); )
    {
      if ( isNodes )
      {
        PromotionPublicRecogGiversFormBean bean = new PromotionPublicRecogGiversFormBean();
        Node node = (Node)iter.next();
        bean.setId( node.getId().toString() );
        bean.setName( node.getName() );
        giverList.add( bean );
      }
      else
      {
        PromotionPublicRecogGiversFormBean bean = new PromotionPublicRecogGiversFormBean();
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
      Collections.sort( giverList, promotionPublicRecogGiversComparator );
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
    // Note: This method does not appear to be called. However it could cause issues if needed for
    // nominations
    return toDomainObject( new RecognitionPromotion() );
  }

  /**
   * Copy values from the form to the domain object. 
   * This method is overloaded for Recognition promotions
   * 
   * @param promotion
   * @return Promotion
   */
  public RecognitionPromotion toDomainObject( RecognitionPromotion recPromo )
  {
    for ( Iterator iter = getGiverList().iterator(); iter.hasNext(); )
    {
      PromotionPublicRecogGiversFormBean bean = (PromotionPublicRecogGiversFormBean)iter.next();
      Budget budget = new Budget();
      budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
      budget.setCurrentValue( new BigDecimal( bean.getBudget() ) );
      budget.setOriginalValue( new BigDecimal( bean.getBudget() ) );
      budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

      String budgetTypeCode = recPromo.getPublicRecogBudgetMaster().getBudgetType().getCode();
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

      for ( Iterator<BudgetSegment> iterBudgetSegment = recPromo.getPublicRecogBudgetMaster().getBudgetSegments().iterator(); iterBudgetSegment.hasNext(); )
      {
        BudgetSegment budgetSegment = (BudgetSegment)iterBudgetSegment.next();
        if ( budgetSegment.getId().equals( this.budgetSegmentId ) )
        {
          budget.setBudgetSegment( budgetSegment );
          budgetSegment.addBudget( budget );
          recPromo.getPublicRecogBudgetMaster().getBudgetSegments().add( budgetSegment );
        }
      }

    }

    return recPromo;
  }

  /**
   * Copy values from the form to the domain object. 
   * This method is overloaded for Nomination promotions
   * 
   * @param promotion
   * @return Promotion
   */
  public NominationPromotion toDomainObject( NominationPromotion nomPromo )
  {
    for ( Iterator iter = getGiverList().iterator(); iter.hasNext(); )
    {
      PromotionPublicRecogGiversFormBean bean = (PromotionPublicRecogGiversFormBean)iter.next();
      Budget budget = new Budget();
      budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
      budget.setCurrentValue( new BigDecimal( bean.getBudget() ) );
      budget.setOriginalValue( new BigDecimal( bean.getBudget() ) );
      budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

      String budgetTypeCode = nomPromo.getPublicRecogBudgetMaster().getBudgetType().getCode();
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

      for ( Iterator<BudgetSegment> iterBudgetSegment = nomPromo.getPublicRecogBudgetMaster().getBudgetSegments().iterator(); iterBudgetSegment.hasNext(); )
      {
        BudgetSegment budgetSegment = (BudgetSegment)iterBudgetSegment.next();
        if ( budgetSegment.getId().equals( this.budgetSegmentId ) )
        {
          budget.setBudgetSegment( budgetSegment );
          budgetSegment.addBudget( budget );
          nomPromo.getPublicRecogBudgetMaster().getBudgetSegments().add( budgetSegment );
        }
      }

    }

    return nomPromo;
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
  public PromotionPublicRecogGiversFormBean getGiverList( int index )
  {
    try
    {
      return (PromotionPublicRecogGiversFormBean)giverList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
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
      PromotionPublicRecogGiversFormBean bean = (PromotionPublicRecogGiversFormBean)iter.next();
      if ( bean.getBudget() == null || bean.getBudget().trim().equals( "" ) )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "promotion.public.recognition", "BUDGET" ) ) );
      }
      else
      {
        int value = 0;
        try
        {
          value = Integer.parseInt( bean.getBudget() );
          if ( value < 0 )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "promotion.notification.errors.SUB_ZERO", CmsResourceBundle.getCmsBundle().getString( "promotion.public.recognition", "BUDGET" ) ) );
          }
        }
        catch( NumberFormatException e )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.NONNUMERIC", CmsResourceBundle.getCmsBundle().getString( "promotion.public.recognition", "BUDGET" ) ) );
        }
      }
    }
    return errors;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public void setBudgetSegmentList( List<BudgetSegment> budgetSegmentList )
  {
    this.budgetSegmentList = budgetSegmentList;
  }

  public List<BudgetSegment> getBudgetSegmentList()
  {
    return budgetSegmentList;
  }
}
