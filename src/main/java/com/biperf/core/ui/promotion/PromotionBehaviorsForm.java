/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionBehaviorsForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.PromotionBehaviorsValueBean;
import com.biperf.core.value.PromotionBehaviorsValueBeanComparator;
import com.objectpartners.cms.util.CmsResourceBundle;

/*
 * PromotionBehaviorsForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Oct
 * 6, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class PromotionBehaviorsForm extends BaseForm
{
  public static final String FORM_NAME = "promotionBehaviorsForm";
  public static final String CHECKED = "on";

  private Long promotionId;
  private Long version;
  private String method;
  private String expired;
  private String live;
  private String cmAssetCode;

  // Display properties
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionName;
  private String promotionStatus;

  private boolean active;
  private String newBehavior;
  private String[] behaviors;
  private List<PromotionBehaviorsValueBean> promoNominationBehaviorsVBList = new ArrayList<PromotionBehaviorsValueBean>();

  private boolean behaviorRequired;
  private boolean behaviorSelected;

  /** Name of behavior the remove button was clicked for */
  private String removeBehaviorCode;

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );
    if ( this.promoNominationBehaviorsVBList.size() == 0 )
    {
      promoNominationBehaviorsVBList = getEmptyNominationBehaviorsValueList( RequestUtils.getOptionalParamInt( request, "promoNominationBehaviorsVBListSize" ) );
    }

    this.removeBehaviorCode = null;
  }

  public int getPromoNominationBehaviorsVBListSize()
  {
    if ( this.promoNominationBehaviorsVBList != null )
    {
      return this.promoNominationBehaviorsVBList.size();
    }

    return 0;
  }

  private List<PromotionBehaviorsValueBean> getEmptyNominationBehaviorsValueList( int valueListCount )
  {
    List<PromotionBehaviorsValueBean> valueList = new ArrayList<PromotionBehaviorsValueBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      PromotionBehaviorsValueBean nominationTimePeriod = new PromotionBehaviorsValueBean();

      valueList.add( nominationTimePeriod );
    }

    return valueList;
  }

  public void load( Promotion promotion )
  {
    this.setRemoveBehaviorCode( "" );
    this.setNewBehavior( "" );
    this.setPromotionId( promotion.getId() );
    this.setCmAssetCode( promotion.getCmAssetCode() );
    this.setVersion( promotion.getVersion() );
    this.setExpired( String.valueOf( promotion.isExpired() ) );
    this.setLive( String.valueOf( promotion.isLive() ) );
    this.setPromotionName( promotion.getName() );
    this.setPromotionTypeName( promotion.getPromotionType().getName() );
    this.setPromotionStatus( promotion.getPromotionStatus().getCode() );
    this.setPromotionTypeCode( promotion.getPromotionType().getCode() );

    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
      this.setActive( recognitionPromotion.isBehaviorActive() );
      this.setBehaviorRequired( recognitionPromotion.isBehaviorRequired() );
      String[] behaviorTypes = new String[recognitionPromotion.getPromotionBehaviors().size()];
      int loopCount = 0;
      Iterator iter = recognitionPromotion.getPromotionBehaviors().iterator();
      while ( iter.hasNext() )
      {
        PromotionBehavior promoBehavior = (PromotionBehavior)iter.next();
        behaviorTypes[loopCount] = promoBehavior.getPromotionBehaviorType().getCode();
        loopCount++;
      }
      this.setBehaviors( behaviorTypes );
    }
    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      this.setActive( nominationPromotion.isBehaviorActive() );
      List<PromotionBehaviorsValueBean> promotionBehavioursVB = new ArrayList<PromotionBehaviorsValueBean>();
      List<PromoNominationBehaviorType> promoNominationBehaviorTypes = PromoNominationBehaviorType.getList();
      Set<PromotionBehavior> promoBehaviors = nominationPromotion.getPromotionBehaviors();

      if ( nominationPromotion != null && promoBehaviors != null && promoBehaviors.size() > 0 )
      {
        for ( PromotionBehavior promotionBehavior : nominationPromotion.getPromotionBehaviors() )
        {
          promotionBehavioursVB.add( new PromotionBehaviorsValueBean( CHECKED,
                                                                      promotionBehavior.getPromotionBehaviorType().getName(),
                                                                      promotionBehavior.getPromotionBehaviorType().getCode(),
                                                                      promotionBehavior.getBehaviorOrder() ) );
        }
      }

      for ( PromoNominationBehaviorType promoNominationBehaviorType : promoNominationBehaviorTypes )
      {
        PromotionBehaviorsValueBean promotionBehaviorsValueBean = new PromotionBehaviorsValueBean( promoNominationBehaviorType.getName(), promoNominationBehaviorType.getCode(), "" );

        if ( !promotionBehavioursVB.contains( promotionBehaviorsValueBean ) )
        {
          promotionBehavioursVB.add( promotionBehaviorsValueBean );
        }
      }
      Collections.sort( promotionBehavioursVB, new PromotionBehaviorsValueBeanComparator() );
      this.setPromoNominationBehaviorsVBList( promotionBehavioursVB );

      /*
       * if ( nominationPromotion.getCustomApproverOptions() != null &&
       * !nominationPromotion.getCustomApproverOptions().isEmpty() ) { for ( ApproverOption
       * ApproverOption : nominationPromotion.getCustomApproverOptions() ) { if ( "behavior".equals(
       * ApproverOption.getApproverType().getCode() ) ) { this.behaviorSelected = true; } } }
       */
      if ( getNominationPromotionService().isBehaviorBasedApproverTypeExist( promotion.getId() ) )
      {
        this.behaviorSelected = true;
      }

      if ( nominationPromotion.getAwardGroupType().equals( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ) )
          && nominationPromotion.getEvaluationType().equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
      {
        // set expired as true to gray out behavior section
        this.setExpired( "true" );
      }
    }
  }

  /*
   * public Promotion toDomainObject(boolean isRecognition) { AbstractRecognitionPromotion
   * promotion; if ( isRecognition ) { promotion = new RecognitionPromotion(); } else { promotion =
   * new NominationPromotion(); } }
   */
  public Promotion toDomain( boolean isRecognition )
  {
    // RecognitionPromotion promotion = new RecognitionPromotion();
    AbstractRecognitionPromotion promotion;

    if ( isRecognition )
    {
      promotion = new RecognitionPromotion();
      ( (RecognitionPromotion)promotion ).setBehaviorRequired( this.isBehaviorRequired() );
    }
    else
    {
      promotion = new NominationPromotion();
    }

    promotion.setId( getPromotionId() );
    promotion.setVersion( getVersion() );
    promotion.setName( getPromotionName() );

    promotion.setBehaviorActive( isActive() );
    if ( isRecognition )
    {
      String[] behaviors = getBehaviors();
      if ( behaviors != null )
      {
        for ( int i = 0; i < behaviors.length; i++ )
        {
          String behaviorTypeCode = behaviors[i];
          promotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( behaviorTypeCode ) );
        }
      }
    }
    else
    {
      if ( this.promoNominationBehaviorsVBList != null && this.promoNominationBehaviorsVBList.size() > 0 )
      {
        for ( PromotionBehaviorsValueBean promotionBehavior : this.promoNominationBehaviorsVBList )
        {
          if ( "on".equals( promotionBehavior.getChecked() ) )
          {
            promotion.addPromotionBehavior( PromoNominationBehaviorType.lookup( promotionBehavior.getPromoNominationBehaviorTypeCode() ), promotionBehavior.getBehaviorOrder() );
          }
        }
      }
    }
    return promotion;
  }

  /**
   * Validate the form before submitting Overridden from
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

    String behaviorsActive = request.getParameter( "active" );
    if ( "true".equals( behaviorsActive ) )
    {
      if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
      {
        String[] behaviorsList = request.getParameterValues( "behaviors" );
        if ( behaviorsList == null || behaviorsList.length == 0 )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.behaviors.ACTIVE_BEHAVIORS" ) ) );
        }
      }
      if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
      {
        int behaviorsListSize = this.promoNominationBehaviorsVBList.size();
        if ( behaviorsListSize == 0 )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.behaviors.ACTIVE_BEHAVIORS" ) ) );
          return errors;
        }
        for ( PromotionBehaviorsValueBean promotionBehaviorsValueBean : this.promoNominationBehaviorsVBList )
        {
          if ( CHECKED.equals( promotionBehaviorsValueBean.getChecked() ) )
          {
            if ( StringUtils.isEmpty( promotionBehaviorsValueBean.getBehaviorOrder() ) )
            {
              errors.add( ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.behaviors.BEHAVIORS_ORDER_REQUIRED" ) ) );
              return errors;
            }
            if ( promotionBehaviorsValueBean.getBehaviorOrder() == null || promotionBehaviorsValueBean.getBehaviorOrder().equals( "0" ) )
            {
              errors.add( "PromotionBehaviorsForm", new ActionMessage( "promotion.behaviors.INVALID_BEHAVIOR_ORDER" ) );
              return errors;
            }
          }
        }
        String valid = isValidBehaviorOrder( this.getPromoNominationBehaviorsVBList() );
        if ( !StringUtil.isNullOrEmpty( valid ) )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( valid, CmsResourceBundle.getCmsBundle().getString( "promotion.overview", "WIZARD" ) ) );
        }
      }
    }

    return errors;
  }

  // validating the order number for active behaviors- Srini
  public String isValidBehaviorOrder( List<PromotionBehaviorsValueBean> promotionBehaviorList )
  {
    int activeBehaviorSize = 0;
    List<Integer> order = new ArrayList<Integer>();
    Set<String> set = new HashSet<String>();
    List<String> duplicates = new ArrayList<String>();
    if ( promotionBehaviorList != null && promotionBehaviorList.size() > 0 )
    {
      // int counter = 0;
      for ( PromotionBehaviorsValueBean promoBehavior : promotionBehaviorList )
      {
        if ( StringUtils.isEmpty( promoBehavior.getChecked() ) && !StringUtils.isEmpty( promoBehavior.getBehaviorOrder() ) )
        {
          return "promotion.errors.NO_BEHAVIOR_ORDER";
        }
        else if ( !StringUtils.isEmpty( promoBehavior.getChecked() ) && StringUtils.isEmpty( promoBehavior.getBehaviorOrder() ) )
        {
          return "promotion.errors.NO_BEHAVIOR_ORDER";
        }
        else if ( !StringUtils.isEmpty( promoBehavior.getBehaviorOrder() ) && !StringUtils.isEmpty( promoBehavior.getChecked() ) )
        {
          activeBehaviorSize++;
          order.add( Integer.parseInt( promoBehavior.getBehaviorOrder() ) );

          if ( !set.add( promoBehavior.getBehaviorOrder() ) )
          {
            duplicates.add( promoBehavior.getBehaviorOrder() );
          }
        }
      }

      if ( order.size() > 0 )
      {
        Integer maxOrder = Collections.max( order );
        if ( maxOrder != activeBehaviorSize )
        {
          return "promotion.errors.INVALID_SORT_ORDER";
        }

        if ( duplicates.size() > 0 )
        {
          return "promotion.errors.INVALID_SORT_ORDER";
        }
      }
      else if ( activeBehaviorSize == 0 )
      {
        return "promotion.errors.SELECT_BEHAVIOR";
      }
    }
    return "";
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getNewBehavior()
  {
    return newBehavior;
  }

  public void setNewBehavior( String newBehavior )
  {
    this.newBehavior = newBehavior;
  }

  public String[] getBehaviors()
  {
    return behaviors;
  }

  public void setBehaviors( String[] behaviors )
  {
    this.behaviors = behaviors;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getExpired()
  {
    return expired;
  }

  public void setExpired( String expired )
  {
    this.expired = expired;
  }

  public String getLive()
  {
    return live;
  }

  public void setLive( String live )
  {
    this.live = live;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  /**
   * @return promotionStatus
   */
  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  /**
   * @param promotionStatus
   */
  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isNominationPromotion()
  {
    return promotionTypeCode.equals( PromotionType.NOMINATION );
  }

  public boolean isRecognitionPromotion()
  {
    return promotionTypeCode.equals( PromotionType.RECOGNITION );
  }

  public boolean isBehaviorRequired()
  {
    return behaviorRequired;
  }

  public void setBehaviorRequired( boolean behaviorRequired )
  {
    this.behaviorRequired = behaviorRequired;
  }

  public List<PromotionBehaviorsValueBean> getPromoNominationBehaviorsVBList()
  {
    return promoNominationBehaviorsVBList;
  }

  public void setPromoNominationBehaviorsVBList( List<PromotionBehaviorsValueBean> promoNominationBehaviorsVBList )
  {
    this.promoNominationBehaviorsVBList = promoNominationBehaviorsVBList;
  }

  public String getRemoveBehaviorCode()
  {
    return removeBehaviorCode;
  }

  public void setRemoveBehaviorCode( String removeBehaviorCode )
  {
    this.removeBehaviorCode = removeBehaviorCode;
  }

  public boolean isBehaviorSelected()
  {
    return behaviorSelected;
  }

  public void setBehaviorSelected( boolean behaviorSelected )
  {
    this.behaviorSelected = behaviorSelected;
  }

  // client customization start
  private NominationPromotionService getNominationPromotionService()
  {
    return (NominationPromotionService)BeanLocator.getBean( NominationPromotionService.BEAN_NAME );
  }
  // client customization end
}
