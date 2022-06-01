
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SSIContestApprovalAudienceType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel1Audience;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel2Audience;
import com.biperf.core.service.SAO;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * PromotionSSIApprovalForm.
 * 
 * @author chowdhur
 * @since Nov 3, 2014
 */
public class PromotionSSIApprovalForm extends BaseActionForm
{
  private static final int LEVEL1 = 1;
  private static final int LEVEL2 = 2;

  private boolean requireContestApproval;
  private int contestApprovalLevels;
  private int daysToApproveOnSubmission;

  private String method;
  private String returnActionUrl;
  private Long promotionId;
  private String promotionName;
  private String promotionTypeCode;
  private String promotionTypeName;
  private String promotionStatus;

  private String approvalLvl1AudienceId;
  private String approvalLvl1AudienceType;
  private List<PromotionAudienceFormBean> approvalLvl1Audiences = new ArrayList<PromotionAudienceFormBean>();

  private String approvalLvl2AudienceId;
  private String approvalLvl2AudienceType;
  private List<PromotionAudienceFormBean> approvalLvl2Audiences = new ArrayList<PromotionAudienceFormBean>();

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    approvalLvl1Audiences = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "approvalLvl1AudienceCount" ) );
    approvalLvl2Audiences = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "approvalLvl2AudienceCount" ) );
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    if ( requireContestApproval )
    {
      if ( contestApprovalLevels != LEVEL1 && contestApprovalLevels != LEVEL2 )
      {
        errors.add( "contestApprovalLevels",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.approvals.LEVEL_OPTIONS" ) ) );
      }
      if ( daysToApproveOnSubmission <= 0 )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( "promotion.ssi.approvals.INVALID_DAYS_ERROR", CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.approvals.DAYS_TO_APPROVE" ) ) );
      }
      if ( contestApprovalLevels == LEVEL1 && approvalLvl1Audiences.isEmpty() )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.ssi.approvals.LEVEL1_AUDIENCE_ERROR" ) );
      }
      if ( contestApprovalLevels == LEVEL2 )
      {
        if ( approvalLvl1Audiences.isEmpty() )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.ssi.approvals.LEVEL1_AUDIENCE_ERROR" ) );
        }
        if ( approvalLvl2Audiences.isEmpty() )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.ssi.approvals.LEVEL2_AUDIENCE_ERROR" ) );
        }
      }
    }
    if ( errors == null )
    {
      errors = new ActionErrors();
    }
    return errors;
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
      // create an empty PromotionAudienceFormBean
      PromotionAudienceFormBean promoAudienceBean = new PromotionAudienceFormBean();
      valueList.add( promoAudienceBean );
    }

    return valueList;
  }

  /**
   * Populate the form from the domain object
   * @param promotion
   */
  public void load( Promotion promotion )
  {
    SSIPromotion ssiPromotion = (SSIPromotion)promotion;
    this.promotionName = ssiPromotion.getName();
    this.promotionTypeCode = ssiPromotion.getPromotionType().getCode();
    this.promotionTypeName = ssiPromotion.getPromotionType().getName();
    this.requireContestApproval = ssiPromotion.getRequireContestApproval();
    this.contestApprovalLevels = ssiPromotion.getContestApprovalLevels();
    this.daysToApproveOnSubmission = ssiPromotion.getDaysToApproveOnSubmission();
    if ( ssiPromotion.getContestApprovalLevel1AudienceType() != null )
    {
      this.approvalLvl1AudienceType = ssiPromotion.getContestApprovalLevel1AudienceType().getCode();
    }
    if ( ssiPromotion.getContestApprovalLevel2AudienceType() != null )
    {
      this.approvalLvl2AudienceType = ssiPromotion.getContestApprovalLevel2AudienceType().getCode();
    }
    loadApprovalLvl1Audiences( ssiPromotion.getContestApprovalLevel1Audiences() );
    loadApprovalLvl2Audiences( ssiPromotion.getContestApprovalLevel2Audiences() );
  }

  /**
   * Load approval audiences
   * @param audienceList
   */
  private void loadApprovalLvl1Audiences( Set<SSIPromotionContestApprovalLevel1Audience> audienceList )
  {
    PromotionAudience promotionAudience = null;
    Iterator<SSIPromotionContestApprovalLevel1Audience> audienceIter = null;
    if ( audienceList != null )
    {
      audienceIter = audienceList.iterator();
      while ( audienceIter.hasNext() )
      {
        promotionAudience = (PromotionAudience)audienceIter.next();
        PromotionAudienceFormBean audienceBean = new PromotionAudienceFormBean();
        audienceBean.setId( promotionAudience.getId() );
        audienceBean.setAudienceId( promotionAudience.getAudience().getId() );
        audienceBean.setName( promotionAudience.getAudience().getName() );
        audienceBean.setSize( getNbrOfPaxsInCriteriaAudience( promotionAudience.getAudience() ) );
        audienceBean.setAudienceType( promotionAudience.getAudience().getAudienceType().getCode() );
        audienceBean.setVersion( promotionAudience.getAudience().getVersion() );
        this.approvalLvl1Audiences.add( audienceBean );
      }
    }
  }

  /**
   * Load approval audiences
   * @param audienceList
   */
  private void loadApprovalLvl2Audiences( Set<SSIPromotionContestApprovalLevel2Audience> audienceList )
  {
    PromotionAudience promotionAudience = null;
    Iterator<SSIPromotionContestApprovalLevel2Audience> audienceIter = null;
    if ( audienceList != null )
    {
      audienceIter = audienceList.iterator();
      while ( audienceIter.hasNext() )
      {
        promotionAudience = (PromotionAudience)audienceIter.next();
        PromotionAudienceFormBean audienceBean = new PromotionAudienceFormBean();
        audienceBean.setId( promotionAudience.getId() );
        audienceBean.setAudienceId( promotionAudience.getAudience().getId() );
        audienceBean.setName( promotionAudience.getAudience().getName() );
        audienceBean.setSize( getNbrOfPaxsInCriteriaAudience( promotionAudience.getAudience() ) );
        audienceBean.setAudienceType( promotionAudience.getAudience().getAudienceType().getCode() );
        audienceBean.setVersion( promotionAudience.getAudience().getVersion() );
        this.approvalLvl2Audiences.add( audienceBean );
      }
    }
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
   * Gets a HierarchyService
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
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
   * Populate the domain object for the save
   * @param promotion
   * @return
   */
  public SSIPromotion toDomainObject( SSIPromotion ssiPromotion )
  {
    ssiPromotion.setRequireContestApproval( this.requireContestApproval );

    if ( requireContestApproval )
    {
      ssiPromotion.setRequireContestApproval( requireContestApproval );
      ssiPromotion.setContestApprovalLevels( this.contestApprovalLevels );
      ssiPromotion.setDaysToApproveOnSubmission( this.daysToApproveOnSubmission );
      if ( contestApprovalLevels == 1 )
      {
        ssiPromotion.setContestApprovalLevel1AudienceType( SSIContestApprovalAudienceType.lookup( SSIContestApprovalAudienceType.SPECIFY_AUDIENCE_CODE ) );
      }
      else if ( contestApprovalLevels == 2 )
      {
        ssiPromotion.setContestApprovalLevel2AudienceType( SSIContestApprovalAudienceType.lookup( SSIContestApprovalAudienceType.SPECIFY_AUDIENCE_CODE ) );
      }
    }
    else
    {
      ssiPromotion.setRequireContestApproval( false );
      ssiPromotion.setContestApprovalLevels( 0 );
      ssiPromotion.setDaysToApproveOnSubmission( 0 );

      ssiPromotion.setContestApprovalLevel1AudienceType( null );
      ssiPromotion.setContestApprovalLevel2AudienceType( null );

    }
    addContestApprovalLevel1Audiences( ssiPromotion );
    addContestApprovalLevel2Audiences( ssiPromotion );
    return ssiPromotion;
  }

  /**
   * Populate the contest approval level 1 audiences in the promotion
   * @param promotion
   */
  private void addContestApprovalLevel1Audiences( SSIPromotion promotion )
  {
    Set audienceSet = new LinkedHashSet();
    // if ( !StringUtil.isNullOrEmpty( approvalLvl1AudienceType ) &&
    // getApprovalLvl1AudienceType().equals( SSIContestApprovalAudienceType.SPECIFY_AUDIENCE_CODE )
    // )
    {
      Iterator<PromotionAudienceFormBean> audienceIter = getApprovalLvl1Audiences().iterator();
      if ( audienceIter != null )
      {
        while ( audienceIter.hasNext() )
        {
          PromotionAudienceFormBean audienceBean = audienceIter.next();

          SSIPromotionContestApprovalLevel1Audience promoAudience = new SSIPromotionContestApprovalLevel1Audience();

          Audience audience = null;
          if ( audienceBean.getAudienceType().equals( AudienceType.SPECIFIC_PAX_TYPE ) )
          {
            audience = new PaxAudience();
          }
          else
          {
            audience = new CriteriaAudience();
          }

          audience.setId( audienceBean.getAudienceId() );
          audience.setName( audienceBean.getName() );

          Long formPromoAudienceId = audienceBean.getId();
          Long promoAudienceId;
          if ( formPromoAudienceId == null || formPromoAudienceId.equals( new Long( 0 ) ) )
          {
            promoAudienceId = null;
          }
          else
          {
            promoAudienceId = formPromoAudienceId;
          }

          if ( promoAudience != null )
          {
            promoAudience.setId( promoAudienceId );
            promoAudience.setAudience( audience );
            promoAudience.setPromotion( promotion );
            audienceSet.add( promoAudience );
          }
        }
      }
    }
    /*
     * else { promotion.getContestApprovalLevel1Audiences().clear(); }
     */
    promotion.setContestApprovalLevel1Audiences( audienceSet );
  }

  /**
   * Populate the contest approval level 1 audiences in the promotion
   * @param promotion
   */
  private void addContestApprovalLevel2Audiences( SSIPromotion promotion )
  {
    Set audienceSet = new LinkedHashSet();
    if ( contestApprovalLevels == 2 )
    {
      Iterator<PromotionAudienceFormBean> audienceIter = getApprovalLvl2Audiences().iterator();
      if ( audienceIter != null )
      {
        while ( audienceIter.hasNext() )
        {
          PromotionAudienceFormBean audienceBean = audienceIter.next();

          SSIPromotionContestApprovalLevel2Audience promoAudience = new SSIPromotionContestApprovalLevel2Audience();

          Audience audience = null;
          if ( audienceBean.getAudienceType().equals( AudienceType.SPECIFIC_PAX_TYPE ) )
          {
            audience = new PaxAudience();
          }
          else
          {
            audience = new CriteriaAudience();
          }

          audience.setId( audienceBean.getAudienceId() );
          audience.setName( audienceBean.getName() );

          Long formPromoAudienceId = audienceBean.getId();
          Long promoAudienceId;
          if ( formPromoAudienceId == null || formPromoAudienceId.equals( new Long( 0 ) ) )
          {
            promoAudienceId = null;
          }
          else
          {
            promoAudienceId = formPromoAudienceId;
          }

          if ( promoAudience != null )
          {
            promoAudience.setId( promoAudienceId );
            promoAudience.setAudience( audience );
            promoAudience.setPromotion( promotion );
            audienceSet.add( promoAudience );
          }
        }
      }
    }
    /*
     * else { promotion.getContestApprovalLevel2Audiences().clear(); }
     */
    promotion.setContestApprovalLevel2Audiences( audienceSet );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   *
   * @return Promotion
   */
  public Promotion toDomainObject()
  {
    SSIPromotion ssiPromotion = new SSIPromotion();
    ssiPromotion.setPromotionType( PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) );
    if ( ssiPromotion != null )
    {
      toDomainObject( ssiPromotion );
    }
    return ssiPromotion;
  }

  /**
   * removes any items that have been selected to be removed from the list
   *
   * @param promoAudienceType
   */
  public void removeApprovalLvl1Items()
  {
    Iterator it = getApprovalLvl1Audiences().iterator();
    while ( it.hasNext() )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)it.next();
      if ( audienceFormBean.isRemoved() )
      {
        it.remove();
      }
    }
  }

  /**
   * removes any items that have been selected to be removed from the list
   *
   * @param promoAudienceType
   */
  public void removeApprovalLvl2Items()
  {
    Iterator it = getApprovalLvl2Audiences().iterator();
    while ( it.hasNext() )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)it.next();
      if ( audienceFormBean.isRemoved() )
      {
        it.remove();
      }
    }
  }

  /**
   * adds an audience item to the list
   *
   * @param audience
   * @param promoAudienceType
   */
  public void addLvl1Audience( Audience audience )
  {
    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );
    approvalLvl1Audiences.add( audienceFormBean );
  }

  /**
   * adds an audience item to the list
   *
   * @param audience
   * @param promoAudienceType
   */
  public void addLvl2Audience( Audience audience )
  {
    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );
    approvalLvl2Audiences.add( audienceFormBean );
  }

  /**
   * Accessor for the number of PromotionAudienceFormBean objects in the list.
   *
   * @return int
   */
  public int getApprovalLvl1AudienceCount()
  {
    if ( approvalLvl1Audiences == null )
    {
      return 0;
    }
    return approvalLvl1Audiences.size();
  }

  public int getApprovalLvl2AudienceCount()
  {
    if ( approvalLvl2Audiences == null )
    {
      return 0;
    }
    return approvalLvl2Audiences.size();
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of PromotionAudienceFormBean from the value list
   */
  public PromotionAudienceFormBean getContestApprovalLvl1Audiences( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)approvalLvl1Audiences.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of PromotionAudienceFormBean from the value list
   */
  public PromotionAudienceFormBean getContestApprovalLvl2Audiences( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)approvalLvl2Audiences.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
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

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getApprovalLvl1AudienceId()
  {
    return approvalLvl1AudienceId;
  }

  public void setApprovalLvl1AudienceId( String approvalLvl1AudienceId )
  {
    this.approvalLvl1AudienceId = approvalLvl1AudienceId;
  }

  public String getApprovalLvl1AudienceType()
  {
    return approvalLvl1AudienceType;
  }

  public void setApprovalLvl1AudienceType( String approvalLvl1AudienceType )
  {
    this.approvalLvl1AudienceType = approvalLvl1AudienceType;
  }

  public List<PromotionAudienceFormBean> getApprovalLvl1Audiences()
  {
    return approvalLvl1Audiences;
  }

  public void setApprovalLvl1Audiences( List<PromotionAudienceFormBean> approvalLvl1Audiences )
  {
    this.approvalLvl1Audiences = approvalLvl1Audiences;
  }

  public List<PromotionAudienceFormBean> getApprovalLvl2Audiences()
  {
    return approvalLvl2Audiences;
  }

  public void setApprovalLvl2Audiences( List<PromotionAudienceFormBean> approvalLvl2Audiences )
  {
    this.approvalLvl2Audiences = approvalLvl2Audiences;
  }

  public boolean isRequireContestApproval()
  {
    return requireContestApproval;
  }

  public void setRequireContestApproval( boolean requireContestApproval )
  {
    this.requireContestApproval = requireContestApproval;
  }

  public int getContestApprovalLevels()
  {
    return contestApprovalLevels;
  }

  public void setContestApprovalLevels( int contestApprovalLevels )
  {
    this.contestApprovalLevels = contestApprovalLevels;
  }

  public String getApprovalLvl2AudienceId()
  {
    return approvalLvl2AudienceId;
  }

  public void setApprovalLvl2AudienceId( String approvalLvl2AudienceId )
  {
    this.approvalLvl2AudienceId = approvalLvl2AudienceId;
  }

  public String getApprovalLvl2AudienceType()
  {
    return approvalLvl2AudienceType;
  }

  public void setApprovalLvl2AudienceType( String approvalLvl2AudienceType )
  {
    this.approvalLvl2AudienceType = approvalLvl2AudienceType;
  }

  public int getDaysToApproveOnSubmission()
  {
    return daysToApproveOnSubmission;
  }

  public void setDaysToApproveOnSubmission( int daysToApproveOnSubmission )
  {
    this.daysToApproveOnSubmission = daysToApproveOnSubmission;
  }

}
