/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionApprovalForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.CustomApproverRoutingType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.enums.PromotionApprovalOptionType;
import com.biperf.core.domain.enums.PromotionApprovalParticipantBean;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.ScoreBy;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Approver;
import com.biperf.core.domain.promotion.ApproverCriteria;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionApprovalOption;
import com.biperf.core.domain.promotion.PromotionApprovalOptionReason;
import com.biperf.core.domain.promotion.PromotionApprovalParticipant;
import com.biperf.core.domain.promotion.PromotionParticipantApprover;
import com.biperf.core.domain.promotion.PromotionParticipantSubmitter;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromotionParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.CustomApproverValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionApprovalForm.
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
 * <td>wadzinsk</td>
 * <td>Jul 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovalForm extends BaseActionForm
{
  private String promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionEndDate;
  private String promotionStatus;
  private String claimFormId;
  private String returnActionUrl;
  private String approvalType;
  private String approverType;
  private String approvalAutoDelayDays;
  private String approvalConditionalClaimCount;
  private String approvalConditionalAmountOperator;
  private String approvalConditionalAmount;
  private String approvalConditionalClaimFormStepElementId;
  private String approverNodeId;
  private String approverNodeName;
  private String approvalNodeLevels;
  private String approvalStartDate;
  private String approvalEndDate;
  private String approvalHierarchyId;
  private String approvalNodeTypeId;
  private String[] promotionApprovalOptions;
  private String[] heldPromotionApprovalOptionReasons;
  private String[] deniedPromotionApprovalOptionReasons;
  private String method;
  private List participantSubmitterList;
  private List participantApproverList;
  private boolean hasParent;
  private boolean isRecognitionPromotion;
  private boolean hasChildren;
  private String evaluationType;
  private String awardGroupMethod;
  private String nomPublicationDate;
  private boolean behaviorActive;
  private boolean approvalLevelPayout; //Client customization for WIP #58122
  private List<CustomApproverValueBean> customApproverValueBeanList = new ArrayList<CustomApproverValueBean>();
  private List<CustomApproverType> customApproverTypes = new ArrayList<CustomApproverType>( CustomApproverType.getList() );
  private List<Characteristic> characteristics = new ArrayList<Characteristic>( getUserCharacteristicService().getAllCharacteristics() );
  private List<CustomApproverRoutingType> customApproverRoutingList = new ArrayList<CustomApproverRoutingType>( CustomApproverRoutingType.getList() );

  private String scoreBy;

  private Long version;

  private boolean promotionLive;

  private Long levelId;

  private SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

  // Default approver section
  private Long defaultApproverId; // Current approver
  private String defaultApproverSearchLastName; // Last-name search field
  private Long selectedDefaultApproverUserId; // Selection from the search results

  // Transient flag so we know which custom approver types to use
  private boolean cumulativeNomination;
  private boolean reDisplay = false;

  public List<CustomApproverValueBean> getCustomApproverValueBeanListAsList()
  {
    return customApproverValueBeanList;
  }

  public void setCustomApproverValueBeanListAsList( List<CustomApproverValueBean> customApproverValueBeanList )
  {
    this.customApproverValueBeanList = customApproverValueBeanList;
  }

  public CustomApproverValueBean getCustomApproverValueBean( int index )
  {
    while ( customApproverValueBeanList.size() <= index )
    {
      customApproverValueBeanList.add( new CustomApproverValueBean() );
    }
    return customApproverValueBeanList.get( index );
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
    super.reset( mapping, request );

    promotionApprovalOptions = request.getParameterValues( "promotionApprovalOptions" );
    heldPromotionApprovalOptionReasons = request.getParameterValues( "heldPromotionApprovalOptionReasons" );
    deniedPromotionApprovalOptionReasons = request.getParameterValues( "deniedPromotionApprovalOptionReasons" );

    int approverListCount = RequestUtils.getOptionalParamInt( request, "participantApproverListCount" );
    int submitterListCount = RequestUtils.getOptionalParamInt( request, "participantSubmitterListCount" );

    participantApproverList = getEmptyPromotionApprovalParticipantBeanList( approverListCount );
    participantSubmitterList = getEmptyPromotionApprovalParticipantBeanList( submitterListCount );

    this.customApproverValueBeanList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "customApproverValueBeanListCount" ) );

    this.promotionTypeCode = RequestUtils.getOptionalParamString( request, "promotionTypeCode" );
    this.behaviorActive = RequestUtils.getOptionalParamBoolean( request, "behaviorActive" );
    this.cumulativeNomination = RequestUtils.getOptionalParamBoolean( request, "cumulativeNomination" );

    if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      if ( cumulativeNomination )
      {
        customApproverTypes.clear();
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.SPECIFIC_APPROVERS ) );
      }
      if ( !behaviorActive )
      {
        customApproverTypes.clear();
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.AWARD ) );
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.CHARACTERISTIC ) );
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.SPECIFIC_APPROVERS ) );
      }
      if ( cumulativeNomination && !behaviorActive )
      {
        customApproverTypes.clear();
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.SPECIFIC_APPROVERS ) );
      }
    }

  }

  public List<CustomApproverValueBean> getEmptyValueList( int valueListCount )
  {
    List<CustomApproverValueBean> valueList = new ArrayList<CustomApproverValueBean>();
    // List<CustomApproverType> approvers = new
    // ArrayList<CustomApproverType>(CustomApproverType.getList());

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty BudgetReallocationValueBean
      CustomApproverValueBean giverBean = new CustomApproverValueBean();
      giverBean.setLevel( (long) ( i + 1 ) );
      valueList.add( giverBean );
    }

    return valueList;
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Promotion
   */
  public Promotion toDomainObject()
  {
    Promotion promotion = null;

    // Create a new Promotion since one was not passed in
    if ( promotionTypeCode.equals( PromotionType.PRODUCT_CLAIM ) )
    {
      promotion = new ProductClaimPromotion();
    }
    else if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      promotion = new RecognitionPromotion();
    }
    else if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      promotion = new NominationPromotion();
    }

    if ( promotion != null )
    {
      return toDomainObject( promotion );
    }
    return promotion;
  }

  /**
   * Copy values from the form to the domain object.
   * 
   * @param promotion
   * @return Promotion
   */
  public Promotion toDomainObject( Promotion promotion )
  {
    promotion.setId( new Long( promotionId ) );
    promotion.setName( promotionName );
    promotion.setApprovalType( approvalType != null ? ApprovalType.lookup( approvalType ) : null );
    promotion.setApproverType( approverType != null ? ApproverType.lookup( approverType ) : null );
    promotion.setApprovalAutoDelayDays( !StringUtils.isBlank( approvalAutoDelayDays ) ? new Integer( approvalAutoDelayDays ) : null );
    promotion.setApprovalConditionalClaimCount( !StringUtils.isBlank( approvalConditionalClaimCount ) ? new Integer( approvalConditionalClaimCount ) : null );
    promotion.setApprovalConditionalAmountOperator( approvalConditionalAmountOperator != null ? ApprovalConditionalAmmountOperatorType.lookup( approvalConditionalAmountOperator ) : null );
    promotion.setApprovalConditionalAmount( !StringUtils.isBlank( approvalConditionalAmount ) ? new Double( approvalConditionalAmount ) : null );

    promotion.setApprovalNodeLevels( !StringUtils.isBlank( approvalNodeLevels ) ? new Integer( approvalNodeLevels ) : null );

    promotion.setApprovalHierarchy( approvalHierarchyId != null ? getHierarchyService().getById( new Long( approvalHierarchyId ) ) : null );
    promotion.setApprovalNodeType( approvalNodeTypeId != null ? getNodeTypeService().getNodeTypeById( new Long( approvalNodeTypeId ) ) : null );

    promotion.setApprovalStartDate( DateUtils.toDate( approvalStartDate ) );
    promotion.setApprovalEndDate( DateUtils.toDate( approvalEndDate ) );
    promotion.setVersion( version );

    // Reset the promotionApprovalOptions and repopulate them based on the selections made
    promotion.setPromotionApprovalOptions( new LinkedHashSet() );

    String[] tempArray = null;
    if ( promotionApprovalOptions == null || promotionApprovalOptions.length == 0 )
    {
      promotionApprovalOptions = new String[1];
      tempArray = new String[1];
    }
    else
    {
      tempArray = new String[promotionApprovalOptions.length + 1];
    }

    for ( int i = 0; i < tempArray.length; i++ )
    {
      String option = "denied";
      if ( i < tempArray.length - 1 )
      {
        option = promotionApprovalOptions[i];
      }
      tempArray[i] = option;
    }
    promotionApprovalOptions = tempArray;

    if ( !promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {

      // If there are promotionApprovalOptions selected, build all the detached
      // PromotionApprovalOption and PromotionApprovalOptionReason objects and add them to the
      // detached promotion.
      if ( promotionApprovalOptions != null && promotionApprovalOptions.length > 0 )
      {
        for ( int i = 0; i < promotionApprovalOptions.length; i++ )
        {
          String optionCode = promotionApprovalOptions[i];
          String reasonCode = "";
          PromotionApprovalOption promoApprovalOption = new PromotionApprovalOption();
          promoApprovalOption.setPromotionApprovalOptionType( PromotionApprovalOptionType.lookup( optionCode ) );
          // Set the promotion at this point because it needs to be there when
          // addPromotionApprovalOptionReason
          // gets called, otherwise the PromotionApprovalOption object will have a null promotion
          // object inside of the PromotionApprovalOptionReason object and will cause problems
          // when saving.
          promoApprovalOption.setPromotion( promotion );
          if ( optionCode.equals( "held" ) )
          {
            if ( heldPromotionApprovalOptionReasons != null && heldPromotionApprovalOptionReasons.length > 0 )
            {
              for ( int j = 0; j < heldPromotionApprovalOptionReasons.length; j++ )
              {
                reasonCode = heldPromotionApprovalOptionReasons[j];
                PromotionApprovalOptionReason promoApprovalOptionReason = new PromotionApprovalOptionReason();
                promoApprovalOptionReason.setPromotionApprovalOptionReasonType( PromotionApprovalOptionReasonType.lookup( reasonCode ) );
                promoApprovalOption.addPromotionApprovalOptionReason( promoApprovalOptionReason );
              }
            }
          }
          if ( optionCode.equals( "denied" ) )
          {
            if ( deniedPromotionApprovalOptionReasons != null && deniedPromotionApprovalOptionReasons.length > 0 )
            {
              for ( int k = 0; k < deniedPromotionApprovalOptionReasons.length; k++ )
              {
                reasonCode = deniedPromotionApprovalOptionReasons[k];
                PromotionApprovalOptionReason promoApprovalOptionReason = new PromotionApprovalOptionReason();
                promoApprovalOptionReason.setPromotionApprovalOptionReasonType( PromotionApprovalOptionReasonType.lookup( reasonCode ) );
                promoApprovalOption.addPromotionApprovalOptionReason( promoApprovalOptionReason );
              }
            }
          }
          promotion.addPromotionApprovalOption( promoApprovalOption );
        }
      }

      // Pending and Approved are always added so add them here
      PromotionApprovalOption approvedPromoApprovalOption = new PromotionApprovalOption();
      approvedPromoApprovalOption.setPromotionApprovalOptionType( PromotionApprovalOptionType.lookup( PromotionApprovalOptionType.APPROVED ) );
      approvedPromoApprovalOption.setPromotion( promotion );
      promotion.addPromotionApprovalOption( approvedPromoApprovalOption );
    }

    PromotionApprovalOption pendingPromoApprovalOption = new PromotionApprovalOption();
    pendingPromoApprovalOption.setPromotionApprovalOptionType( PromotionApprovalOptionType.lookup( PromotionApprovalOptionType.PENDING ) );
    pendingPromoApprovalOption.setPromotion( promotion );
    promotion.addPromotionApprovalOption( pendingPromoApprovalOption );

    if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      NominationPromotion nomPromo = (NominationPromotion)promotion;

      promotion.setPromotionType( PromotionType.lookup( getPromotionTypeCode() ) );

      // Winner, NonWinner and Expired are always added so add them here
      PromotionApprovalOption winnerPromoApprovalOption = new PromotionApprovalOption();
      PromotionApprovalOption nonWinnerPromoApprovalOption = new PromotionApprovalOption();
      PromotionApprovalOption expiredPromoApprovalOption = new PromotionApprovalOption();

      winnerPromoApprovalOption.setPromotionApprovalOptionType( PromotionApprovalOptionType.lookup( PromotionApprovalOptionType.WINNER ) );
      winnerPromoApprovalOption.setPromotion( promotion );
      nonWinnerPromoApprovalOption.setPromotionApprovalOptionType( PromotionApprovalOptionType.lookup( PromotionApprovalOptionType.NONWINNER ) );
      nonWinnerPromoApprovalOption.setPromotion( promotion );
      expiredPromoApprovalOption.setPromotionApprovalOptionType( PromotionApprovalOptionType.lookup( PromotionApprovalOptionType.EXPIRED ) );
      expiredPromoApprovalOption.setPromotion( promotion );

      promotion.addPromotionApprovalOption( winnerPromoApprovalOption );
      promotion.addPromotionApprovalOption( nonWinnerPromoApprovalOption );
      promotion.addPromotionApprovalOption( expiredPromoApprovalOption );

      // Custom approvers to domain objects
      if ( approverType != null && ApproverType.CUSTOM_APPROVERS.equals( approverType ) )
      {
        nomPromo.setApprovalNodeLevels( Integer.parseInt( approvalNodeLevels ) );
        nomPromo.getCustomApproverOptions().clear();

        List<CustomApproverValueBean> customApproverValueBeanList = getCustomApproverValueBeanListAsList();
        for ( int i = 0; i < customApproverValueBeanList.size(); ++i )
        {
          CustomApproverValueBean customApproverVB = customApproverValueBeanList.get( i );
          ApproverOption option = new ApproverOption();

          option.setApprovalLevel( customApproverVB.getLevel() );
          option.setApproverType( CustomApproverType.lookup( customApproverVB.getCustomApproverTypeValue() ) );
          option.setSequenceNum( i );

          // Set characteristic only when dealing with that type
          if ( option.getApproverType() != null && CustomApproverType.CHARACTERISTIC.equals( option.getApproverType().getCode() ) )
          {
            option.setCharacteristicId( customApproverVB.getCharacteristicId() );
            if ( customApproverVB.getCustomApproverRoutingTypeValue() == null )
            {
              option.setApproverRoutingType( populateApprovalRoutingValue( nomPromo, customApproverVB ) );
            }
            else
            {
              option.setApproverRoutingType( CustomApproverRoutingType.lookup( customApproverVB.getCustomApproverRoutingTypeValue() ) );
            }
          }

          // For the specific approver type, build up domain objects for participants
          if ( option.getApproverType() != null && CustomApproverType.SPECIFIC_APPROVERS.equals( option.getApproverType().getCode() ) )
          {
            Iterator<PromotionApprovalParticipantBean> approverParticipantIterator = customApproverVB.getApproverListAsList().iterator();
            while ( approverParticipantIterator.hasNext() )
            {
              PromotionApprovalParticipantBean participantBean = approverParticipantIterator.next();
              ApproverCriteria criteria = new ApproverCriteria();
              Approver approver = new Approver();

              Participant participant = getParticipantService().getParticipantById( participantBean.getParticipantId() );
              approver.setParticipant( participant );

              criteria.addApprover( approver );
              option.addApproverCriteria( criteria );
            }
          }
          if ( customApproverVB.getNomApproverList() != null && customApproverVB.getNomApproverList().size() > 0 && !reDisplay )
          {
            customApproverVB.getNomApproverList().forEach( ( participantBean ) ->
            {
              ApproverCriteria criteria = new ApproverCriteria();
              Approver approver = new Approver();

              Participant participant = getParticipantService().getParticipantByUserName( participantBean.getUsername() );
              approver.setParticipant( participant );

              criteria.addApprover( approver );
              if ( CustomApproverType.AWARD.equalsIgnoreCase( participantBean.getApproverType() ) )
              {
                if ( participantBean.getApproverValue() != null && participantBean.getApproverValue().contains( "-" ) )
                {
                  String[] splitValue = participantBean.getApproverValue().split( "-" );
                  criteria.setMinVal( Integer.valueOf( splitValue[0].trim() ) );
                  criteria.setMaxVal( Integer.valueOf( splitValue[1].trim() ) );
                }
                else
                {
                  criteria.setMinVal( Integer.valueOf( participantBean.getApproverValue() ) );
                }
              }
              else
              {
                criteria.setApproverValue( participantBean.getApproverValue() );
              }
              option.addApproverCriteria( criteria );
              option.setId( Long.valueOf( customApproverVB.getApproverOptionId() ) );
            } );
          }

          nomPromo.addCustomApproverOptions( option );
        }
      }
      else
      {
        nomPromo.getCustomApproverOptions().clear();
      }

      // Default approver
      Participant defaultApprover = null;

      // New approver selection
      if ( selectedDefaultApproverUserId != null && selectedDefaultApproverUserId != 0 )
      {
        defaultApprover = getParticipantService().getParticipantById( selectedDefaultApproverUserId );
      }
      // Use existing selection
      else
      {
        defaultApprover = getParticipantService().getParticipantById( defaultApproverId );
      }

      nomPromo.setDefaultApprover( defaultApprover );
    }
    else
    {
      promotion.setPromotionType( PromotionType.lookup( getPromotionTypeCode() ) );
    }

    return promotion;
  }

  /**
   * Load the form with the domain object value;
   * 
   * @param promotion
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public void load( Promotion promotion )
  {
    promotionId = promotion.getId().toString();
    promotionName = promotion.getName();
    promotionTypeName = promotion.getPromotionType().getName();
    promotionTypeCode = promotion.getPromotionType().getCode();
    promotionEndDate = DateUtils.toDisplayString( promotion.getSubmissionEndDate() );
    promotionStatus = promotion.getPromotionStatus().getCode();
    claimFormId = promotion.getClaimForm().getId().toString();
    promotionLive = promotion.isLive();

    if ( promotion.getScoreBy() != null )
    {
      scoreBy = promotion.getScoreBy().getCode();
    }

    if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      approvalType = promotion.getApprovalType() != null ? promotion.getApprovalType().getCode() : ApprovalType.AUTOMATIC_IMMEDIATE;
    }
    else if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
        //customization starts wip 56492 starts
        boolean isLevelSelectionByApprover = ( (NominationPromotion)promotion ).isLevelSelectionByApprover();
        approvalLevelPayout = ( (NominationPromotion)promotion ).isLevelPayoutByApproverAvailable();//customization starts wip 58122
        if ( isLevelSelectionByApprover )
        {
          approvalType = ApprovalType.COKE_CUSTOM;
        }
        else
        {
          approvalType = promotion.getApprovalType() != null ? promotion.getApprovalType().getCode() : ApprovalType.MANUAL;
        }
        //customization end wip 56492 ends
        // approvalType = ApprovalType.MANUAL;
      NominationPromotion nomPromotion = (NominationPromotion)promotion;
      awardGroupMethod = nomPromotion.getAwardGroupType().getCode();
      evaluationType = nomPromotion.getEvaluationType().getCode();
      if ( nomPromotion.isPublicationDateActive() )
      {
        nomPublicationDate = DateUtils.toDisplayString( nomPromotion.getPublicationDate() );
      }
      if ( nomPromotion.isBehaviorActive() )
      {
        behaviorActive = true;
      }

      if ( nomPromotion.isCumulative() )
      {
        cumulativeNomination = true;
      }

      // Default Approver fields
      if ( nomPromotion.getDefaultApprover() != null )
      {
        this.defaultApproverId = nomPromotion.getDefaultApprover().getId();
      }
    }
    else
    {

      approvalType = promotion.getApprovalType() != null ? promotion.getApprovalType().getCode() : "auto_approve";
    }
    approverType = promotion.getApproverType() != null ? promotion.getApproverType().getCode() : null;
    approvalAutoDelayDays = promotion.getApprovalAutoDelayDays() != null ? promotion.getApprovalAutoDelayDays().toString() : null;
    approvalConditionalClaimCount = promotion.getApprovalConditionalClaimCount() != null ? promotion.getApprovalConditionalClaimCount().toString() : null;

    ApprovalConditionalAmmountOperatorType approvalConditionalAmountOperatorType = promotion.getApprovalConditionalAmountOperator();
    approvalConditionalAmountOperator = approvalConditionalAmountOperatorType != null ? approvalConditionalAmountOperatorType.getCode() : null;
    if ( promotion.getApprovalConditionalAmountField() != null && promotion.getApprovalConditionalAmountField().getId() != null )
    {
      approvalConditionalClaimFormStepElementId = promotion.getApprovalConditionalAmountField().getId().toString();
    }
    else
    {
      approvalConditionalClaimFormStepElementId = null;
    }

    approvalConditionalAmount = promotion.getApprovalConditionalAmount() != null ? promotion.getApprovalConditionalAmount().toString() : null;

    approvalStartDate = promotion.getApprovalStartDate() != null ? DateUtils.toDisplayString( promotion.getApprovalStartDate() ) : null;
    approvalEndDate = promotion.getApprovalEndDate() != null ? DateUtils.toDisplayString( promotion.getApprovalEndDate() ) : null;

    approvalNodeLevels = promotion.getApprovalNodeLevels() != null ? promotion.getApprovalNodeLevels().toString() : null;

    if ( promotion.getApproverNode() != null && promotion.getApproverNode().getId() != null )
    {
      approverNodeId = promotion.getApproverNode().getId().toString();
      approverNodeName = promotion.getApproverNode().getName();
    }
    else
    {
      approverNodeId = null;
      approverNodeName = null;
    }

    Hierarchy approvalHierarchy = promotion.getApprovalHierarchy();
    if ( approvalHierarchy != null && approvalHierarchy.getId() != null )
    {
      approvalHierarchyId = approvalHierarchy.getId().toString();
    }
    else
    {
      approvalHierarchyId = null;
    }

    NodeType approvalNodeType = promotion.getApprovalNodeType();
    if ( approvalNodeType != null && approvalNodeType.getId() != null )
    {
      approvalNodeTypeId = approvalNodeType.getId().toString();
    }
    else
    {
      approvalNodeTypeId = null;
    }

    version = promotion.getVersion();

    if ( promotion.getPromotionApprovalOptions() != null && promotion.getPromotionApprovalOptions().size() > 0 )
    {
      Iterator it = promotion.getPromotionApprovalOptions().iterator();
      int i = 0;
      promotionApprovalOptions = new String[promotion.getPromotionApprovalOptions().size()];
      while ( it.hasNext() )
      {
        int j = 0;
        PromotionApprovalOption promoApprovalOption = (PromotionApprovalOption)it.next();
        promotionApprovalOptions[i] = promoApprovalOption.getPromotionApprovalOptionType().getCode();

        String[] reasons = new String[promoApprovalOption.getPromotionApprovalOptionReasons().size()];

        Iterator reasonIt = promoApprovalOption.getPromotionApprovalOptionReasons().iterator();
        while ( reasonIt.hasNext() )
        {
          PromotionApprovalOptionReason promoApprovalOptionReason = (PromotionApprovalOptionReason)reasonIt.next();
          reasons[j] = promoApprovalOptionReason.getPromotionApprovalOptionReasonType().getCode();

          j++;
        }

        if ( promoApprovalOption.getPromotionApprovalOptionType().getCode().equals( "held" ) )
        {
          heldPromotionApprovalOptionReasons = reasons;
        }
        if ( promoApprovalOption.getPromotionApprovalOptionType().getCode().equals( "denied" ) )
        {
          deniedPromotionApprovalOptionReasons = reasons;
        }

        i++;
      }
    }

    List approvers = promotion.getPromotionParticipantApprovers() != null ? promotion.getPromotionParticipantApprovers() : new ArrayList();

    Iterator approverIterator = approvers.iterator();
    while ( approverIterator.hasNext() )
    {
      PromotionParticipantApprover approver = (PromotionParticipantApprover)approverIterator.next();
      if ( approver != null )
      {
        participantApproverList.add( toValueObject( approver ) );
      }
    }

    List submitters = promotion.getPromotionParticipantSubmitters() != null ? promotion.getPromotionParticipantSubmitters() : new ArrayList();
    Iterator submitterIterator = submitters.iterator();
    while ( submitterIterator.hasNext() )
    {
      PromotionParticipantSubmitter submitter = (PromotionParticipantSubmitter)submitterIterator.next();
      if ( submitter != null )
      {
        participantSubmitterList.add( toValueObject( submitter ) );
      }
    }

    hasParent = promotion.hasParent();
    hasChildren = promotion.getChildrenCount() > 0;

    isRecognitionPromotion = promotion.isRecognitionPromotion();

    if ( promotion.isNominationPromotion() && getApprovalNodeLevels() != null )
    {
      int level = Integer.parseInt( getApprovalNodeLevels() );
      NominationPromotion nomPromotion = (NominationPromotion)promotion;

      if ( level == nomPromotion.getApprovalNodeLevels() )
      {
        if ( nomPromotion.getCustomApproverOptions() != null && nomPromotion.getCustomApproverOptions().size() > 0 )
        {
          // Ordered via approvalLevel. Change to list implementation and sort to guarantee order.
          List<ApproverOption> optionList = new ArrayList<>( nomPromotion.getCustomApproverOptions() );
          Collections.sort( optionList );
          Iterator customApproverIterator = optionList.iterator();
          while ( customApproverIterator.hasNext() )
          {
            ApproverOption approverOption = (ApproverOption)customApproverIterator.next();
            CustomApproverValueBean approverValueBean = new CustomApproverValueBean();
            approverValueBean.setApproverOptionId( approverOption.getId().toString() );
            approverValueBean.setCustomApproverTypeValue( approverOption.getApproverType().getCode() );
            approverValueBean.setLevel( approverOption.getApprovalLevel() );
            approverValueBean.setCharacteristicId( approverOption.getCharacteristicId() );
            if ( approverOption.getApproverRoutingType() != null )
            {
              approverValueBean.setCustomApproverRoutingTypeValue( approverOption.getApproverRoutingType().getCode() );
            }
            approverValueBean.setCharacteristics( getUserCharacteristicService().getAllCharacteristics() );
            approverValueBean.setSequenceNum( approverOption.getSequenceNum() );

            // Add approver participants to value beans
            if ( approverOption.getApproverCriteria() != null && approverOption.getApproverCriteria().size() > 0 )
            {
              Iterator<ApproverCriteria> approverCriteriaIterator = approverOption.getApproverCriteria().iterator();
              while ( approverCriteriaIterator.hasNext() )
              {
                ApproverCriteria approverCriteria = approverCriteriaIterator.next();

                if ( approverCriteria.getApprovers() != null && approverCriteria.getApprovers().size() > 0 )
                {
                  Iterator<Approver> approverIt = approverCriteria.getApprovers().iterator();
                  while ( approverIt.hasNext() )
                  {
                    Approver approver = approverIt.next();

                    PromotionApprovalParticipantBean bean = new PromotionApprovalParticipantBean();
                    bean.setFirstName( approver.getParticipant().getFirstName() );
                    bean.setLastName( approver.getParticipant().getLastName() );
                    bean.setLevelId( approverOption.getApprovalLevel() );
                    bean.setParticipantId( approver.getParticipant().getId() );
                    bean.setPromotionId( nomPromotion.getId() );
                    approverValueBean.getApproverListAsList().add( bean );
                  }
                }
              }
            }
            customApproverValueBeanList.add( approverValueBean );
          }
        }
        else
        {

          this.customApproverValueBeanList.clear();
          this.customApproverValueBeanList = getEmptyValueList( level );
        }
      }
    }

    // If the nomination promotion is cumulative approval, only certain custom approver type options
    // will show
    // Need to make sure the list is reduced on first page load, even if no existing data
    if ( promotion.isNominationPromotion() )
    {
      if ( isCumulativeNomination() )
      {
        customApproverTypes.clear();
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.SPECIFIC_APPROVERS ) );
      }
      if ( !isBehaviorActive() )
      {
        customApproverTypes.clear();
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.AWARD ) );
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.CHARACTERISTIC ) );
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.SPECIFIC_APPROVERS ) );
      }
      if ( isCumulativeNomination() && !isBehaviorActive() )
      {
        customApproverTypes.clear();
        customApproverTypes.add( CustomApproverType.lookup( CustomApproverType.SPECIFIC_APPROVERS ) );
      }
    }
  }

  /**
   * Load the form with the domain object value;
   * 
   * @return PromotionApprovalParticipantBean
   * @param participant
   */
  public PromotionApprovalParticipantBean toValueObject( PromotionApprovalParticipant participant )
  {
    PromotionApprovalParticipantBean bean = new PromotionApprovalParticipantBean();
    if ( participant instanceof PromotionParticipantApprover )
    {
      bean.setParticipantType( PromotionParticipantApprover.PROMO_PARTICIPANT_TYPE );
    }

    if ( participant instanceof PromotionParticipantSubmitter )
    {
      bean.setParticipantType( PromotionParticipantSubmitter.PROMO_PARTICIPANT_TYPE );
    }

    bean.setId( participant.getId() );
    bean.setPromotionId( participant.getPromotion().getId() );

    Participant pax = participant.getParticipant();
    bean.setParticipantId( pax.getId() );
    bean.setFirstName( pax.getFirstName() );
    bean.setLastName( pax.getLastName() );

    bean.setVersion( participant.getVersion() );

    return bean;
  }

  public PromotionApprovalParticipantBean toPromotionApprovalParticipantBean( PromotionApprovalParticipant participant )
  {
    PromotionApprovalParticipantBean bean = new PromotionApprovalParticipantBean();

    if ( participant instanceof PromotionParticipantApprover )
    {
      bean.setParticipantType( PromotionParticipantApprover.PROMO_PARTICIPANT_TYPE );
    }

    if ( participant instanceof PromotionParticipantSubmitter )
    {
      bean.setParticipantType( PromotionParticipantSubmitter.PROMO_PARTICIPANT_TYPE );
    }

    bean.setId( participant.getId() );
    bean.setPromotionId( participant.getPromotion().getId() );

    Participant pax = participant.getParticipant();
    bean.setParticipantId( pax.getId() );
    bean.setFirstName( pax.getFirstName() );
    bean.setLastName( pax.getLastName() );

    bean.setVersion( participant.getVersion() );

    return bean;
  }

  /**
   * @return value of approvalAutoDelayDays property
   */
  public String getApprovalAutoDelayDays()
  {
    return approvalAutoDelayDays;
  }

  /**
   * @param approvalAutoDelayDays value for approvalAutoDelayDays property
   */
  public void setApprovalAutoDelayDays( String approvalAutoDelayDays )
  {
    this.approvalAutoDelayDays = approvalAutoDelayDays;
  }

  /**
   * @return value of approvalConditionalAmount property
   */
  public String getApprovalConditionalAmount()
  {
    return approvalConditionalAmount;
  }

  /**
   * @param approvalConditionalAmount value for approvalConditionalAmount property
   */
  public void setApprovalConditionalAmount( String approvalConditionalAmount )
  {
    this.approvalConditionalAmount = approvalConditionalAmount;
  }

  /**
   * @return value of approvalConditionalAmountOperator property
   */
  public String getApprovalConditionalAmountOperator()
  {
    return approvalConditionalAmountOperator;
  }

  /**
   * @param approvalConditionalAmountOperator value for approvalConditionalAmountOperator property
   */
  public void setApprovalConditionalAmountOperator( String approvalConditionalAmountOperator )
  {
    this.approvalConditionalAmountOperator = approvalConditionalAmountOperator;
  }

  /**
   * @return value of approvalConditionalClaimCount property
   */
  public String getApprovalConditionalClaimCount()
  {
    return approvalConditionalClaimCount;
  }

  /**
   * @param approvalConditionalClaimCount value for approvalConditionalClaimCount property
   */
  public void setApprovalConditionalClaimCount( String approvalConditionalClaimCount )
  {
    this.approvalConditionalClaimCount = approvalConditionalClaimCount;
  }

  /**
   * @return value of approvalNodeLevels property
   */
  public String getApprovalNodeLevels()
  {
    return approvalNodeLevels;
  }

  /**
   * @param approvalNodeLevels value for approvalNodeLevels property
   */
  public void setApprovalNodeLevels( String approvalNodeLevels )
  {
    this.approvalNodeLevels = approvalNodeLevels;
  }

  /**
   * @return value of approvalType property
   */
  public String getApprovalType()
  {
    return approvalType;
  }

  /**
   * @param approvalType value for approvalType property
   */
  public void setApprovalType( String approvalType )
  {
    this.approvalType = approvalType;
  }

  /**
   * @return value of approverType property
   */
  public String getApproverType()
  {
    return approverType;
  }

  /**
   * @param approverType value for approverType property
   */
  public void setApproverType( String approverType )
  {
    this.approverType = approverType;
  }

  /**
   * @return value of promotionId property
   */
  public String getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId value for promotionId property
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return value of returnActionUrl property
   */
  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  /**
   * @param returnActionUrl value for returnActionUrl property
   */
  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  /**
   * @return value of version property
   */
  public Long getVersion()
  {
    return version;
  }

  /**
   * @param version value for version property
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * @return value of approvalEndDate property
   */
  public String getApprovalEndDate()
  {
    return approvalEndDate;
  }

  /**
   * @param approvalEndDate value for approvalEndDate property
   */
  public void setApprovalEndDate( String approvalEndDate )
  {
    this.approvalEndDate = approvalEndDate;
  }

  /**
   * @return value of approvalStartDate property
   */
  public String getApprovalStartDate()
  {
    return approvalStartDate;
  }

  /**
   * @param approvalStartDate value for approvalStartDate property
   */
  public void setApprovalStartDate( String approvalStartDate )
  {
    this.approvalStartDate = approvalStartDate;
  }

  /**
   * @return value of approvalConditionalClaimFormStepElementId property
   */
  public String getApprovalConditionalClaimFormStepElementId()
  {
    return approvalConditionalClaimFormStepElementId;
  }

  /**
   * @param approvalConditionalClaimFormStepElementId value for
   *          approvalConditionalClaimFormStepElementId property
   */
  public void setApprovalConditionalClaimFormStepElementId( String approvalConditionalClaimFormStepElementId )
  {
    this.approvalConditionalClaimFormStepElementId = approvalConditionalClaimFormStepElementId;
  }

  /**
   * @return value of approverNodeId property
   */
  public String getApproverNodeId()
  {
    return approverNodeId;
  }

  /**
   * @param approverNodeId value for approverNodeId property
   */
  public void setApproverNodeId( String approverNodeId )
  {
    this.approverNodeId = approverNodeId;
  }

  /**
   * @return value of approverNodeName property
   */
  public String getApproverNodeName()
  {
    return approverNodeName;
  }

  /**
   * @param approverNodeName value for approverNodeName property
   */
  public void setApproverNodeName( String approverNodeName )
  {
    this.approverNodeName = approverNodeName;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param mapping the mapping used to select this instance.
   * @param request the servlet request we are processing.
   * @return <code>ActionErrors</code> object that encapsulates any validation errors.
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    // If the promotion has a parent then all fields are read-only and there is nothing to validate
    if ( hasParent )
    {
      return null;
    }

    ActionErrors actionErrors = super.validate( mapping, request );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    // ***** Validate startDate *****/
    // Only if the promotion is not auto_approve or not auto_delayed do we need to validate
    // approvalStartDate
    if ( !approvalType.equals( ApprovalType.AUTOMATIC_IMMEDIATE ) && !approvalType.equals( ApprovalType.AUTOMATIC_DELAYED ) )
    {
      // Make sure its not empty
      if ( StringUtils.isEmpty( approvalStartDate ) )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.APPROVAL_START_DATE" ) ) );
      }
      else
      {
        // Now validate the date
        try
        {
          sdf.parse( approvalStartDate );

          // For nomination, if publication date active, confirm publication date isn't before
          // approval start date
          if ( !StringUtils.isBlank( nomPublicationDate ) )
          {
            try
            {
              Date approvalStartDateDate = sdf.parse( approvalStartDate );
              Date nomPublicationDateDate = sdf.parse( nomPublicationDate );
              if ( nomPublicationDateDate.before( approvalStartDateDate ) )
              {
                actionErrors
                    .add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "promotion.approvals.errors.DATE_BEFORE_PUBLICATION_DATE", CmsResourceBundle.getCmsBundle().getString( "promotion.basics.START" ), nomPublicationDate ) );
              }
            }
            catch( ParseException e )
            {
              actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.START" ) ) );
            }
          }
        }
        catch( ParseException e )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.START" ) ) );
        }
      }

      // confirm approval end date not empty if publication date is active
      if ( !StringUtils.isBlank( nomPublicationDate ) && StringUtils.isEmpty( approvalEndDate ) )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.END" ) ) );
      }

      if ( !StringUtils.isBlank( approvalEndDate ) )
      {
        try
        {
          Date approvalEndDateDate = sdf.parse( approvalEndDate );
          Date approvalStartDateDate = sdf.parse( approvalStartDate );
          // Confirm end date isn't before start date
          if ( approvalEndDateDate.before( approvalStartDateDate ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_START_DATE ) );
          }
          // Can't set approval end date if submission end date is not set
          if ( promotionEndDate.equals( "" ) && !approvalEndDate.equals( "" ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.CANNOT_SET_END_DATE" ) );
          }
          else
          {
            // Confirm approval end date isn't before submission end date
            if ( approvalEndDateDate.before( DateUtils.toDate( promotionEndDate ) ) )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.INVALID_END_DATE", promotionEndDate ) );
            }
          }
        }
        catch( ParseException e )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_START_DATE ) );
        }

        // For nomination, if there is a publication date, then publication date must be after
        // approval end date
        if ( !StringUtils.isBlank( nomPublicationDate ) )
        {
          // confirm publication date is after approval end date
          try
          {
            Date approvalEndDateDate = sdf.parse( approvalEndDate );
            Date nomPublicationDateDate = sdf.parse( nomPublicationDate );
            if ( !nomPublicationDateDate.after( approvalEndDateDate ) )
            {
              actionErrors
                  .add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "promotion.approvals.errors.DATE_BEFORE_PUBLICATION_DATE", CmsResourceBundle.getCmsBundle().getString( "promotion.basics.END" ), nomPublicationDate ) );
            }
          }
          catch( ParseException e )
          {
            actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.END" ) ) );
          }
        }
      } // END approvalEndDate validations
      try
      {
        Date approvalStartDateVar = sdf.parse( approvalStartDate );
        Date promotionStartDate = getPromotionService().getPromotionById( new Long( this.getPromotionId() ) ).getSubmissionStartDate();
        if ( approvalStartDateVar.before( promotionStartDate ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.INVALID_APPROVAL_START_DATE" ) );
        }
      }
      catch( ParseException e )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.APPROVAL_START_DATE" ) ) );
      }

    }

    // ***** Validate approvalAutoDelayDays *****/
    // Only if the promotion is auto_delayed do we need to validate approvalAutoDelayDays
    if ( approvalType.equals( ApprovalType.AUTOMATIC_DELAYED ) )
    {
      int apprvlAutoDelayDays = RequestUtils.getOptionalParamInt( request, "approvalAutoDelayDays" );
      // Make sure its not empty
      if ( apprvlAutoDelayDays <= 0 )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage( "promotion.approvals.errors.INVALID_RANGE", CmsResourceBundle.getCmsBundle().getString( "promotion.approvals.APPROVAL_NUMBER_OF_DAYS" ) ) );
      }
    }

    // ***** Validate approvalConditionalClaimCount *****/
    // Only if the promotion is cond_nth do we need to validate approvalConditionalClaimCount
    if ( approvalType.equals( ApprovalType.CONDITIONAL_NTH_BASED ) )
    {
      int apprvlConditionalClaimCount = RequestUtils.getOptionalParamInt( request, "approvalConditionalClaimCount" );
      // Make sure its not empty
      if ( apprvlConditionalClaimCount <= 0 )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage( "promotion.approvals.errors.INVALID_RANGE", CmsResourceBundle.getCmsBundle().getString( "promotion.approvals.APPROVAL_REVIEW_EVERY" ) ) );
      }
    }

    // ***** Validate approvalConditionalAmount *****/
    if ( approvalType.equals( ApprovalType.CONDITIONAL_AMOUNT_BASED ) )
    {
      if ( StringUtils.isEmpty( approvalConditionalClaimFormStepElementId ) )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage( "promotion.approvals.errors.CONDITIONAL_AMOUNT_FIELD", CmsResourceBundle.getCmsBundle().getString( "promotion.approvals.CONDITIONAL_AMOUNT_FIELD" ) ) );
      }

      if ( StringUtils.isEmpty( approvalConditionalAmountOperator ) )
      {
        actionErrors
            .add( ActionMessages.GLOBAL_MESSAGE,
                  new ActionMessage( "promotion.approvals.errors.CONDITIONAL_AMOUNT_OPERATOR", CmsResourceBundle.getCmsBundle().getString( "promotion.approvals.CONDITIONAL_AMOUNT_OPERATOR" ) ) );
      }

      if ( StringUtils.isEmpty( approvalConditionalAmount ) )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                          new ActionMessage( "promotion.approvals.errors.CONDITIONAL_AMOUNT", CmsResourceBundle.getCmsBundle().getString( "promotion.approvals.CONDITIONAL_AMOUNT" ) ) );
      }
      else
      {
        double amount = RequestUtils.getOptionalParamDouble( request, "approvalConditionalAmount" );

        if ( amount <= 0 )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage( "promotion.approvals.errors.CONDITIONAL_AMOUNT", CmsResourceBundle.getCmsBundle().getString( "promotion.approvals.CONDITIONAL_AMOUNT" ) ) );
        }
      }
    }

    // ***** Validate approvalConditionalParticipant *****/
    if ( approvalType.equals( ApprovalType.CONDITIONAL_PAX_BASED ) )
    {
      if ( participantSubmitterList.size() < 1 )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.SUBMITTERS_REQUIRED" ) );

      }
    }

    // ***** Calculator approver check ****/
    // checking for calculator approver type...
    if ( scoreBy != null )
    {
      if ( !approvalType.equals( ApprovalType.MANUAL ) && scoreBy.length() != 0 )
      {
        if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) || promotionTypeCode.equals( PromotionType.NOMINATION ) )
        {
          // if scoreBy is approver .. then other than manual type is not allowed...
          if ( scoreBy.equals( ScoreBy.APPROVER ) )
          {
            // Approvers can have only approval type Manual .. other types are invalid..
            actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.APPROVERS_ONLY_MANUAL" ) );
          }
        }
      }
    }
    // ***** Validate approvalNodeLevels *****/
    // Only if the promotion is node_owner do we need to validate approvalNodeLevels
    if ( null != this.approverType && !approverType.equals( "" ) )
    {

      if ( approverType.equals( ApproverType.SPECIFIC_APPROVERS ) )
      {
        if ( participantApproverList.size() < 1 )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.APPROVERS_REQUIRED" ) );

        }
        else
        {
          Iterator approvers = participantApproverList.iterator();

          // If there are submitters, then we need to make sure no pax are listed as
          // both a submitter and an approver
          ArrayList submitterPaxIdList = new ArrayList();
          if ( participantSubmitterList.size() > 0 )
          {

            Iterator submitterIterator = participantSubmitterList.iterator();
            while ( submitterIterator.hasNext() )
            {
              PromotionApprovalParticipantBean submitter = (PromotionApprovalParticipantBean)submitterIterator.next();
              submitterPaxIdList.add( submitter.getParticipantId() );
            }
          }

          while ( approvers.hasNext() )
          {
            PromotionApprovalParticipantBean approver = (PromotionApprovalParticipantBean)approvers.next();

            if ( submitterPaxIdList.contains( approver.getParticipantId() ) )
            {

              actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.PAX_IS_SUBMITTER_AND_APPROVER" ) );

              break;
            }

          }
        }
      }

      if ( approverType.equals( ApproverType.NODE_OWNER_BY_TYPE ) )
      {
        // Validate approval hierarchy ID.
        if ( StringUtils.isEmpty( approvalHierarchyId ) )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.APPROVAL_HIERARCHY" ) );
        }

        // Validate approval node type ID.
        if ( StringUtils.isEmpty( approvalNodeTypeId ) )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.APPROVAL_NODE_TYPE" ) );
        }
      }

      if ( approverType.equals( ApproverType.NODE_OWNER_BY_LEVEL ) || approverType.equals( ApproverType.NODE_OWNER_BY_TYPE ) || approverType.equals( ApproverType.NOMINATOR_NODE_OWNER_BY_LEVEL )
          || approverType.equals( ApproverType.NOMINATOR_NODE_OWNER_BY_TYPE ) || approverType.equals( ApproverType.NOMINEE_NODE_OWNER_BY_LEVEL )
          || approverType.equals( ApproverType.NOMINEE_NODE_OWNER_BY_TYPE ) )
      {
        // Validate approval node levels.
        int apprvlNodeLevels = RequestUtils.getOptionalParamInt( request, "approvalNodeLevels" );
        if ( apprvlNodeLevels <= 0 || apprvlNodeLevels > 5 )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.CUSTOM_INVALID_RANGE" ) );
        }
      }
      // Custom Approver Validation
      if ( approverType.equals( ApproverType.CUSTOM_APPROVERS ) )
      {
        int custApprovalLevels = RequestUtils.getOptionalParamInt( request, "approvalNodeLevels" );
        if ( custApprovalLevels <= 0 || custApprovalLevels > 5 )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.CUSTOM_INVALID_RANGE" ) );
        }

        if ( approvalNodeLevels.isEmpty() )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.CUSTOM_APPROVAL_LEVEL_REQUIRED" ) );
        }

        boolean isBehaviorApproverTypeExist = false;
        boolean isAwardApproverTypeExist = false;
        boolean isAwardErrorFound = false;

        for ( CustomApproverValueBean approverValueBean : getCustomApproverValueBeanListAsList() )
        {
          if ( CustomApproverType.BEHAVIOR.equals( approverValueBean.getCustomApproverTypeValue() ) )
          {
            if ( !isBehaviorApproverTypeExist )
            {
              isBehaviorApproverTypeExist = true;
            }
            else
            {
              actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.CUSTOM_DUPLICATE_BEHAVIOR" ) );
            }
          }

          if ( CustomApproverType.AWARD.equals( approverValueBean.getCustomApproverTypeValue() ) )
          {
            if ( !isAwardApproverTypeExist )
            {
              isAwardApproverTypeExist = true;
            }
            else
            {
              if ( !isAwardErrorFound )
              {
                actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.CUSTOM_DUPLICATE_AWARD" ) );
                isAwardErrorFound = true;
              }
            }
          }

          if ( CustomApproverType.CHARACTERISTIC.equals( approverValueBean.getCustomApproverTypeValue() ) )
          {
            if ( approverValueBean.getCharacteristicId() == 0 )
            {
              actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.CUSTOM_CHARACTERISTIC_REQUIRED" ) );
            }
            if ( approverValueBean.getCustomApproverRoutingTypeValue() != null && approverValueBean.getCustomApproverRoutingTypeValue().equalsIgnoreCase( "select" ) )
            {
              actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.APPROVER_ROUTINGTYPE_REQ" ) );
            }
          }

          if ( CustomApproverType.SPECIFIC_APPROVERS.equals( approverValueBean.getCustomApproverTypeValue() ) )
          {
            if ( approverValueBean.getApproverListAsList().size() < 1 )
            {
              actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.APPROVERS_REQUIRED" ) );

            }
          }
        }

      }
    }
    else
    {
      if ( !approvalType.equals( ApprovalType.AUTOMATIC_IMMEDIATE ) )
      {
        actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.APPROVER_REQUIRED" ) );
      }
    }

    // Make sure there are approval options selected
    if ( promotionApprovalOptions != null && promotionApprovalOptions.length > 0 )
    {
      for ( int i = 0; i < promotionApprovalOptions.length; i++ )
      {
        if ( promotionApprovalOptions[i].equals( "held" ) )
        {
          if ( heldPromotionApprovalOptionReasons == null || heldPromotionApprovalOptionReasons.length < 1 )
          {
            actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.EMPTY_HELD_REASON" ) );
          }
        }
        else if ( promotionApprovalOptions[i].equals( "denied" ) )
        {
          if ( ( deniedPromotionApprovalOptionReasons == null || deniedPromotionApprovalOptionReasons.length < 1 ) && !approvalType.equals( ApprovalType.AUTOMATIC_IMMEDIATE ) )
          {
            actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.approvals.errors.EMPTY_DENIED_REASON" ) );
          }
        }
      }
    }

    // Validate default approver section
    if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      // Check if there is a new default approver selection
      if ( selectedDefaultApproverUserId != null && selectedDefaultApproverUserId != 0 )
      {
        // Make sure the selection actually leads to a participant
        if ( getParticipantService().getParticipantById( selectedDefaultApproverUserId ) == null )
        {
          actionErrors.add( "defaultApprover", new ActionMessage( "promotion.approvals.errors.INVALID_DEFAULT_APPROVER" ) );
        }
      }
      // Fall back to existing selection
      else
      {
        // Check that there actually is an existing selection
        if ( defaultApproverId == null || defaultApproverId == 0 )
        {
          actionErrors.add( "defaultApprover",
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.approvals.DEFAULT_APPROVER" ) ) );
        }
        // Check that the existing selection actually leads to a participant
        else if ( getParticipantService().getParticipantById( defaultApproverId ) == null )
        {
          actionErrors.add( "defaultApprover", new ActionMessage( "promotion.approvals.errors.INVALID_DEFAULT_APPROVER" ) );
        }
      }
    }

    // This is for the wizard
    request.setAttribute( "participantSubmitterList", getParticipantSubmitterList() );
    request.setAttribute( "participantApproverList", getParticipantApproverList() );

    for ( Iterator<CustomApproverValueBean> iter = getCustomApproverValueBeanListAsList().iterator(); iter.hasNext(); )
    {
      CustomApproverValueBean bean = iter.next();
      bean.getCharacteristics().addAll( getCharacteristics() );
    }

    return actionErrors;
  }

  private CustomApproverRoutingType populateApprovalRoutingValue( NominationPromotion promotion, CustomApproverValueBean customApproverVB )
  {
    if ( promotion.getAwardGroupType() != null && promotion.isTeam() )
    {
      return CustomApproverRoutingType.lookup( CustomApproverRoutingType.BYNOMINATOR );
    }
    else
    {
      if ( customApproverVB.getCustomApproverRoutingTypeValue() == null )
      {
        List<CustomApproverValueBean> customApproverValueBeanList = getCustomApproverValueBeanListAsList();
        for ( int i = 0; i < customApproverValueBeanList.size(); ++i )
        {
          CustomApproverValueBean customApprover = customApproverValueBeanList.get( i );
          if ( customApprover.getCustomApproverRoutingTypeValue() != null )
          {
            return CustomApproverRoutingType.lookup( customApprover.getCustomApproverRoutingTypeValue() );
          }
        }
      }
      else if ( customApproverVB.getCustomApproverRoutingTypeValue() != null )
      {
        return CustomApproverRoutingType.lookup( customApproverVB.getCustomApproverRoutingTypeValue() );
      }
      return CustomApproverRoutingType.lookup( CustomApproverRoutingType.BYNOMINATOR );
    }
  }

  /**
   * @return promotionApprovalOptions
   */
  public String[] getPromotionApprovalOptions()
  {
    return promotionApprovalOptions;
  }

  /**
   * @param promotionApprovalOptions
   */
  public void setPromotionApprovalOptions( String[] promotionApprovalOptions )
  {
    this.promotionApprovalOptions = promotionApprovalOptions;
  }

  /**
   * @return deniedPromotionApprovalOptionReasons
   */
  public String[] getDeniedPromotionApprovalOptionReasons()
  {
    return deniedPromotionApprovalOptionReasons;
  }

  /**
   * @param deniedPromotionApprovalOptionReasons
   */
  public void setDeniedPromotionApprovalOptionReasons( String[] deniedPromotionApprovalOptionReasons )
  {
    this.deniedPromotionApprovalOptionReasons = deniedPromotionApprovalOptionReasons;
  }

  /**
   * @return heldPromotionApprovalOptionReasons
   */
  public String[] getHeldPromotionApprovalOptionReasons()
  {
    return heldPromotionApprovalOptionReasons;
  }

  /**
   * @param heldPromotionApprovalOptionReasons
   */
  public void setHeldPromotionApprovalOptionReasons( String[] heldPromotionApprovalOptionReasons )
  {
    this.heldPromotionApprovalOptionReasons = heldPromotionApprovalOptionReasons;
  }

  /**
   * @return promotionName
   */
  public String getPromotionName()
  {
    return promotionName;
  }

  /**
   * @param promotionName
   */
  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  /**
   * @return method
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return participantApproverList
   */
  public List getParticipantApproverList()
  {
    return participantApproverList;
  }

  /**
   * @param participantApproverList
   */
  public void setParticipantApproverList( List participantApproverList )
  {
    this.participantApproverList = participantApproverList;
  }

  /**
   * @return participantSubmitterList
   */
  public List getParticipantSubmitterList()
  {
    return participantSubmitterList;
  }

  /**
   * @param participantSubmitterList
   */
  public void setParticipantSubmitterList( List participantSubmitterList )
  {
    this.participantSubmitterList = participantSubmitterList;
  }

  /**
   * Accessor for the number of PromotionParticipantApprover objects in the list.
   * 
   * @return int
   */
  public int getParticipantApproverListCount()
  {
    if ( participantApproverList == null )
    {
      return 0;
    }

    return participantApproverList.size();
  }

  /**
   * Accessor for the number of PromotionParticipantSubmitter objects in the list.
   * 
   * @return int
   */
  public int getParticipantSubmitterListCount()
  {
    if ( participantSubmitterList == null )
    {
      return 0;
    }

    return participantSubmitterList.size();
  }

  /**
   * adds a PromotionApprovalParticipantBean to the participantSubmitterList list
   * 
   * @param promotionParticipantSubmitter
   */
  public void addPromotionParticipantSubmitter( PromotionApprovalParticipantBean promotionParticipantSubmitter )
  {
    participantSubmitterList.add( promotionParticipantSubmitter );
  }

  /**
   * adds a PromotionApprovalParticipantBean to the participantApproverList list
   * 
   * @param promotionParticipantApprover
   */
  public void addPromotionParticipantApprover( PromotionApprovalParticipantBean promotionParticipantApprover )
  {
    participantApproverList.add( promotionParticipantApprover );
  }

  /**
   * resets the value list with empty PromotionApprovalParticipantBeans
   * 
   * @param listCount
   * @return List
   */
  private List getEmptyPromotionApprovalParticipantBeanList( int listCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < listCount; i++ )
    {
      PromotionApprovalParticipantBean participant = new PromotionApprovalParticipantBean();
      valueList.add( participant );
    }

    return valueList;
  }

  /**
   * @return promotionTypeName
   */
  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  /**
   * @param promotionTypeName
   */
  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  /**
   * @return claimFormId
   */
  public String getClaimFormId()
  {
    return claimFormId;
  }

  /**
   * @param claimFormId
   */
  public void setClaimFormId( String claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  /**
   * @return true if promotion is Live
   */
  public boolean isPromotionLive()
  {
    return promotionLive;
  }

  /**
   * @param promotionLive
   */
  public void setPromotionLive( boolean promotionLive )
  {
    this.promotionLive = promotionLive;
  }

  /**
   * @return promotionEndDate
   */
  public String getPromotionEndDate()
  {
    return promotionEndDate;
  }

  /**
   * @param promotionEndDate
   */
  public void setPromotionEndDate( String promotionEndDate )
  {
    this.promotionEndDate = promotionEndDate;
  }

  /**
   * @return true if promotion has parent promotion
   */
  public boolean isHasParent()
  {
    return hasParent;
  }

  /**
   * @param hasParent
   */
  public void setHasParent( boolean hasParent )
  {
    this.hasParent = hasParent;
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

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public boolean isHasChildren()
  {
    return hasChildren;
  }

  public void setHasChildren( boolean hasChildren )
  {
    this.hasChildren = hasChildren;
  }

  public boolean isRecognitionPromotion()
  {
    return isRecognitionPromotion;
  }

  public void setRecognitionPromotion( boolean isRecognitionPromotion )
  {
    this.isRecognitionPromotion = isRecognitionPromotion;
  }

  public String getApprovalHierarchyId()
  {
    return approvalHierarchyId;
  }

  public void setApprovalHierarchyId( String approvalHierarchyId )
  {
    this.approvalHierarchyId = approvalHierarchyId;
  }

  public String getApprovalNodeTypeId()
  {
    return approvalNodeTypeId;
  }

  public void setApprovalNodeTypeId( String approvalNodeTypeId )
  {
    this.approvalNodeTypeId = approvalNodeTypeId;
  }

  /**
   * Returns the hierarchy service.
   * 
   * @return a reference to the hierarchy service.
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)BeanLocator.getBean( HierarchyService.BEAN_NAME );
  }

  /**
   * Returns the node type service.
   * 
   * @return a reference to the node type service.
   */
  private NodeTypeService getNodeTypeService()
  {
    return (NodeTypeService)BeanLocator.getBean( NodeTypeService.BEAN_NAME );
  }

  /**
   * Returns the participant service.
   * 
   * @return a reference to the participant service.
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  public String getAwardGroupMethod()
  {
    return awardGroupMethod;
  }

  public void setAwardGroupMethod( String awardGroupMethod )
  {
    this.awardGroupMethod = awardGroupMethod;
  }

  public String getEvaluationType()
  {
    return evaluationType;
  }

  public void setEvaluationType( String evaluationType )
  {
    this.evaluationType = evaluationType;
  }

  public String getScoreBy()
  {
    return scoreBy;
  }

  public void setScoreBy( String scoreBy )
  {
    this.scoreBy = scoreBy;
  }

  public String getNomPublicationDate()
  {
    return nomPublicationDate;
  }

  public void setNomPublicationDate( String nomPublicationDate )
  {
    this.nomPublicationDate = nomPublicationDate;
  }

  public void addCustomApproverValueBeanList( CustomApproverValueBean customApproverValueBean )
  {
    customApproverValueBeanList.add( customApproverValueBean );
  }

  public int getCustomApproverValueBeanListCount()
  {
    if ( customApproverValueBeanList != null && customApproverValueBeanList.size() > 0 )
    {
      return customApproverValueBeanList.size();
    }

    return 0;

  }

  private PromotionParticipantService getPromotionParticipantService()
  {
    return (PromotionParticipantService)BeanLocator.getBean( PromotionParticipantService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)BeanLocator.getBean( UserCharacteristicService.BEAN_NAME );
  }

  public Long getLevelId()
  {
    return levelId;
  }

  public void setLevelId( Long levelId )
  {
    this.levelId = levelId;
  }

  public boolean isCumulativeNomination()
  {
    return cumulativeNomination;
  }

  public void setCumulativeNomination( boolean cumulativeNomination )
  {
    this.cumulativeNomination = cumulativeNomination;
  }

  public List<CustomApproverType> getCustomApproverTypes()
  {
    return customApproverTypes;
  }

  public void setCustomApproverTypes( List<CustomApproverType> customApproverTypes )
  {
    this.customApproverTypes = customApproverTypes;
  }

  public List<Characteristic> getCharacteristics()
  {
    return characteristics;
  }

  public void setCharacteristics( List<Characteristic> characteristics )
  {
    this.characteristics = characteristics;
  }

  public boolean isBehaviorActive()
  {
    return behaviorActive;
  }

  public void setBehaviorActive( boolean behaviorActive )
  {
    this.behaviorActive = behaviorActive;
  }

  public Long getDefaultApproverId()
  {
    return defaultApproverId;
  }

  public void setDefaultApproverId( Long defaultApproverId )
  {
    this.defaultApproverId = defaultApproverId;
  }

  public String getDefaultApproverSearchLastName()
  {
    return defaultApproverSearchLastName;
  }

  public void setDefaultApproverSearchLastName( String defaultApproverSearchLastName )
  {
    this.defaultApproverSearchLastName = defaultApproverSearchLastName;
  }

  public Long getSelectedDefaultApproverUserId()
  {
    return selectedDefaultApproverUserId;
  }

  public void setSelectedDefaultApproverUserId( Long selectedDefaultApproverUserId )
  {
    this.selectedDefaultApproverUserId = selectedDefaultApproverUserId;
  }

  public List<CustomApproverRoutingType> getCustomApproverRoutingList()
  {
    return customApproverRoutingList;
  }

  public void setCustomApproverRoutingList( List<CustomApproverRoutingType> customApproverRoutingList )
  {
    this.customApproverRoutingList = customApproverRoutingList;
  }

  public boolean isReDisplay()
  {
    return reDisplay;
  }

  public void setReDisplay( boolean reDisplay )
  {
    this.reDisplay = reDisplay;
  }
  //Client customization for WIP #58122 starts
  public boolean isApprovalLevelPayout()
  {
    return approvalLevelPayout;
  }

  public void setApprovalLevelPayout( boolean approvalLevelPayout )
  {
    this.approvalLevelPayout = approvalLevelPayout;
  }
  //Client customization for WIP #58122 ends

}
