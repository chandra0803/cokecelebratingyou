/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsClaimsListAction.java,v $
 */

package com.biperf.core.ui.approvals;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.claim.hibernate.ApproverSeekingClaimQueryConstraint;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.Claims;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.Claims.MetaContent;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.Claims.MetaContent.Columns;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.Claims.MetaContent.ProductStatus;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.Claims.MetaContent.ProductStatus.Reason;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.Claims.Results;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.Claims.Results.Product;
import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.ProductClaimApprovalStats;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.ListPageInfo;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ProductClaimStatusCountsBean;
import com.biperf.core.value.PromotionApprovableValue;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ApprovalsClaimsListAction.
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
 * <td>zahler</td>
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsClaimsListAction extends BaseDispatchAction
{
  public static final String ATTR_PROMOTION_CLAIMS_VALUE_LIST = "promotionClaimsValueList";
  private static final Long APPROVAL_LIST_PAGE_SIZE = 20L;

  protected ActionForward cancelled( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException, IOException
  {
    ApprovalsClaimsListForm claimProductApprovalListForm = (ApprovalsClaimsListForm)form;
    claimProductApprovalListForm.getClaimProductApprovalFormByClaimProductIdString().clear();
    final ProductClaimApprovalBean approvalBean = new ProductClaimApprovalBean();
    ProductClaimsPromotion productClaimsPromotion = new ProductClaimsPromotion();
    ProductClaimsParameters productClaimsParameters = new ProductClaimsParameters();
    Boolean saveOccurred = false;
    int pageNumber;
    if ( request.getParameter( "pageNumber" ) != null )
    {
      pageNumber = Integer.parseInt( request.getParameter( "pageNumber" ) );
    }
    else
    {
      pageNumber = 1;
    }

    int claimsPerPage = 20;
    int rowNumStart = ( pageNumber - 1 ) * claimsPerPage;
    int rowNumEnd = claimsPerPage;

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      if ( (Boolean)clientStateMap.get( "saveOccurred" ) != null )
      {
        saveOccurred = (Boolean)clientStateMap.get( "saveOccurred" );
      }
    }

    if ( request.getParameter( "startDate" ) != null && request.getParameter( "endDate" ) != null && request.getParameter( "promotionId" ) != null && request.getParameter( "claimStatus" ) != null )
    {
      claimProductApprovalListForm.setStartDate( request.getParameter( "startDate" ) );
      claimProductApprovalListForm.setEndDate( request.getParameter( "endDate" ) );
      claimProductApprovalListForm.setPromotionId( request.getParameter( "promotionId" ) );
      if ( request.getParameter( "claimStatus" ).equals( "open" ) )
      {
        claimProductApprovalListForm.setOpen( true );
      }
      else
      {
        claimProductApprovalListForm.setOpen( false );
      }
    }
    String sortedOn = "number";
    String sortedBy = "desc";
    if ( request.getParameter( "sortedOn" ) != null && request.getParameter( "sortedBy" ) != null )
    {
      sortedOn = request.getParameter( "sortedOn" );
      sortedBy = request.getParameter( "sortedBy" );
    }
    if ( claimProductApprovalListForm.getClaimStatus() != null )
    {
      claimProductApprovalListForm.setOpen( claimProductApprovalListForm.getClaimStatus().equals( "open" ) ? true : false );
    }
    claimProductApprovalListForm.setSortedOn( sortedOn );
    claimProductApprovalListForm.setSortedBy( sortedBy );
    claimProductApprovalListForm.setRowNumStart( rowNumStart );
    claimProductApprovalListForm.setRowNumEnd( rowNumEnd );

    if ( saveOccurred )
    {
      request.setAttribute( "saveOccurred", Boolean.TRUE );
    }

    String timeZoneID = getUserService().getUserTimeZone( UserManager.getUserId() );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );

    ApproverSeekingClaimQueryConstraint claimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );
    claimQueryConstraint.setApprovableUserId( UserManager.getUserId() );
    claimQueryConstraint.setEndDate( DateUtils.toDate( claimProductApprovalListForm.getEndDate() ) );
    claimQueryConstraint.setStartDate( DateUtils.toDate( claimProductApprovalListForm.getStartDate() ) );
    claimQueryConstraint.setOpen( claimProductApprovalListForm.isOpen() );
    claimQueryConstraint.setExpired( false );
    claimQueryConstraint.setClaimPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    if ( StringUtils.isNotBlank( claimProductApprovalListForm.getPromotionId() ) )
    {
      claimQueryConstraint.setIncludedPromotionIds( new Long[] { Long.parseLong( claimProductApprovalListForm.getPromotionId() ) } );
    }

    int promotionClaimsCnt = getClaimService().getClaimListCount( claimQueryConstraint );
    List promotionClaimsValueList = buildApprovablesList( claimProductApprovalListForm, true );

    for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
    {
      PromotionApprovableValue promotionApprovableValue = (PromotionApprovableValue)iter.next();
      List approvablesList = promotionApprovableValue.getApprovables();
      Claims claims = new Claims();
      MetaContent metaContent = new MetaContent();
      List<Results> resultList = new ArrayList<Results>();
      List<Columns> columnList = new ArrayList<Columns>();

      metaContent.setSortedOn( sortedOn );
      metaContent.setSortedBy( sortedBy );
      metaContent.setMaxRows( promotionClaimsCnt );
      metaContent.setRowsPerPage( claimsPerPage );
      metaContent.setPageNumber( pageNumber );
      metaContent.setExportUrl( RequestUtils.getBaseURI( request ) + "/claim/approvalsClaimsListMaintain.do?method=extractAsCsv" );

      Columns column1 = new Columns();
      column1.setName( "number" );
      column1.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.CLAIM_NUMBER" ) );
      column1.setSortable( true );
      columnList.add( column1 );
      Columns column2 = new Columns();
      column2.setName( "date" );
      column2.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.DATE" ) );
      column2.setSortable( true );
      columnList.add( column2 );
      Columns column3 = new Columns();
      column3.setName( "submitter" );
      column3.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.SUBMITTER" ) );
      column3.setSortable( true );
      columnList.add( column3 );
      Columns column4 = new Columns();
      column4.setName( "approver" );
      column4.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.APPROVER" ) );
      column4.setSortable( false );
      columnList.add( column4 );
      Columns column5 = new Columns();
      column5.setName( "products" );
      column5.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.ITEM" ) );
      column5.setSortable( false );
      columnList.add( column5 );
      Columns column6 = new Columns();
      column6.setName( "status" );
      column6.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.STATUS" ) );
      column6.setSortable( false );
      columnList.add( column6 );
      List<ProductStatus> statusesList = new ArrayList<ProductStatus>();

      if ( !promotionApprovableValue.getPromotion().getApprovalOptionTypes().isEmpty() )
      {
        for ( Iterator iterApprovStats = promotionApprovableValue.getPromotion().getApprovalOptionTypes().iterator(); iterApprovStats.hasNext(); )
        {
          ApprovalStatusType approvalStatusType = (ApprovalStatusType)iterApprovStats.next();
          ProductStatus productStatus = new ProductStatus();

          if ( approvalStatusType.getCode().equals( ApprovalStatusType.PENDING ) )
          {
            productStatus.setValue( approvalStatusType.getCode() );
            productStatus.setText( CmsResourceBundle.getCmsBundle().getString( "claims.submission.PENDING" ) );
            productStatus.setReasonsList( null );
          }
          else if ( approvalStatusType.getCode().equals( ApprovalStatusType.APPROVED ) )
          {
            productStatus.setValue( approvalStatusType.getCode() );
            productStatus.setText( CmsResourceBundle.getCmsBundle().getString( "claims.submission.APPROVE" ) );
            productStatus.setReasonsList( null );
          }
          else if ( approvalStatusType.getCode().equals( ApprovalStatusType.DENIED ) )
          {
            productStatus.setValue( approvalStatusType.getCode() );
            productStatus.setText( CmsResourceBundle.getCmsBundle().getString( "claims.submission.DENY" ) );
            List<Reason> deniedReasonslist = new ArrayList<Reason>();
            for ( Iterator iterDenyReason = promotionApprovableValue.getPromotion().getDeniedReasonCodeTypes().iterator(); iterDenyReason.hasNext(); )
            {
              PromotionApprovalOptionReasonType denyReasonType = (PromotionApprovalOptionReasonType)iterDenyReason.next();
              Reason denyReason = new Reason();
              denyReason.setReasonValue( denyReasonType.getCode() );
              denyReason.setReasonText( denyReasonType.getName() );
              deniedReasonslist.add( denyReason );
            }
            productStatus.setReasonsList( deniedReasonslist );
          }
          else if ( approvalStatusType.getCode().equals( ApprovalStatusType.HOLD ) )
          {
            productStatus.setValue( approvalStatusType.getCode() );
            productStatus.setText( CmsResourceBundle.getCmsBundle().getString( "claims.submission.HOLD" ) );
            List<Reason> holdReasonslist = new ArrayList<Reason>();
            for ( Iterator iterHoldReason = promotionApprovableValue.getPromotion().getHeldReasonCodeTypes().iterator(); iterHoldReason.hasNext(); )
            {
              PromotionApprovalOptionReasonType holdReasonType = (PromotionApprovalOptionReasonType)iterHoldReason.next();
              Reason holdReason = new Reason();
              holdReason.setReasonValue( holdReasonType.getCode() );
              holdReason.setReasonText( holdReasonType.getName() );
              holdReasonslist.add( holdReason );
            }
            productStatus.setReasonsList( holdReasonslist );
          }
          if ( productStatus.getText() != null )
          {
            if ( productStatus.getText().equals( CmsResourceBundle.getCmsBundle().getString( "claims.submission.PENDING" ) ) )
            {
              statusesList.add( 0, productStatus );
            }
            else
            {
              statusesList.add( productStatus );
            }
          }
        }
      }
      metaContent.setColumnList( columnList );
      metaContent.setStatusesList( statusesList );

      Timestamp claimDate = null;

      for ( Iterator iterApprov = approvablesList.iterator(); iterApprov.hasNext(); )
      {
        ProductClaim claim = (ProductClaim)iterApprov.next();

        Results results = new Results();
        results.setId( claim.getId() );
        results.setNumber( claim.getClaimNumber() );
        if ( claimDate != null )
        {
          if ( claimDate.before( claim.getAuditCreateInfo().getDateCreated() ) )
          {
            claimDate = claim.getAuditCreateInfo().getDateCreated();
          }
        }
        else
        {
          claimDate = claim.getAuditCreateInfo().getDateCreated();
        }
        results.setDate( DateUtils.getStringFromTimeStamp( claim.getAuditCreateInfo().getDateCreated() ) );
        results.setSubmitter( claim.getSubmitter().getNameLFMWithComma() );
        List<Product> productsList = new ArrayList<Product>();
        for ( Iterator pIter = claim.getClaimProducts().iterator(); pIter.hasNext(); )
        {
          ClaimProduct claimProduct = (ClaimProduct)pIter.next();
          if ( claimProduct != null )
          {
            Product product = new Product();
            product.setClaimItemId( claimProduct.getId() );
            product.setProductName( claimProduct.getProduct().getName() );
            product.setStatus( claimProduct.getApprovalStatusType().getCode() );
            if ( claimProduct.getPromotionApprovalOptionReasonType() != null )
            {
              product.setStatusReason( claimProduct.getPromotionApprovalOptionReasonType().getName() );
            }
            if ( claimProduct.getCurrentClaimItemApprover() != null && !claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.PENDING ) )
            {
              if ( claimProduct.getCurrentClaimItemApprover().getApproverUser() != null )
              {
                product.setApprover( claimProduct.getCurrentClaimItemApprover().getApproverUser().getNameLFMWithComma() );
              }
              column4.setSortable( true );
            }
            else if ( claimProduct.getCurrentClaimItemApprover() == null && !claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.PENDING ) )
            {
              product.setApprover( CmsResourceBundle.getCmsBundle().getString( "home.navmenu.admin.SYSTEM" ) );
              column4.setSortable( true );
            }
            productsList.add( product );
          }
        }
        if ( productsList.size() > 1 && sortedOn.equals( "approver" ) && sortedBy.equals( "asc" ) )
        {
          Collections.sort( productsList, new ProductApproverAscendingComparator() );
        }
        if ( productsList.size() > 1 && sortedOn.equals( "approver" ) && sortedBy.equals( "desc" ) )
        {
          Collections.sort( productsList, new ProductApproverDescendingComparator() );
        }
        results.setProductsList( productsList );
        resultList.add( results );
      }

      if ( sortedOn.equals( "approver" ) && sortedBy.equals( "asc" ) )
      {
        Collections.sort( resultList, new ProductResultsAscendingComparator() );
      }
      if ( sortedOn.equals( "approver" ) && sortedBy.equals( "desc" ) )
      {
        Collections.sort( resultList, new ProductResultsDescendingComparator() );
      }
      claims.setMetaContent( metaContent );
      claims.setResultList( resultList );

      Promotion promotion = promotionApprovableValue.getPromotion();
      ProductClaimApprovalStats productClaimApprovalStats1 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats2 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats3 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats4 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats5 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats6 = new ProductClaimApprovalStats();

      List<ProductClaimStatusCountsBean> productClaimStatusCountsList = getClaimService().getProductClaimStatusCount( promotion.getId() );

      for ( ProductClaimStatusCountsBean productClaimStatusCountsBean : productClaimStatusCountsList )
      {
        productClaimApprovalStats1.setCount( productClaimStatusCountsBean.getClaimsSubmitted() );
        productClaimApprovalStats2.setCount( productClaimStatusCountsBean.getClaimsPending() );
        productClaimApprovalStats3.setCount( productClaimStatusCountsBean.getProductsSubmitted() );
        productClaimApprovalStats4.setCount( productClaimStatusCountsBean.getProductsApproved() );
        productClaimApprovalStats5.setCount( productClaimStatusCountsBean.getProductsDenied() );
        productClaimApprovalStats6.setCount( productClaimStatusCountsBean.getProductsPending() );
      }
      List<ProductClaimApprovalStats> statsList = new ArrayList<ProductClaimApprovalStats>();

      productClaimApprovalStats1.setType( "claims" );
      productClaimApprovalStats1.setStatus( "submitted" );
      productClaimApprovalStats1.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.CLAIMS_SUBMITTED" ) );
      statsList.add( productClaimApprovalStats1 );

      productClaimApprovalStats2.setType( "claims" );
      productClaimApprovalStats2.setStatus( "pending" );
      productClaimApprovalStats2.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.CLAIMS_PENDING" ) );
      statsList.add( productClaimApprovalStats2 );

      productClaimApprovalStats3.setType( "products" );
      productClaimApprovalStats3.setStatus( "submitted" );
      productClaimApprovalStats3.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.PRODUCTS_SUBMITTED" ) );
      statsList.add( productClaimApprovalStats3 );

      productClaimApprovalStats4.setType( "products" );
      productClaimApprovalStats4.setStatus( "approved" );
      productClaimApprovalStats4.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.PRODUCTS_APPROVED" ) );
      statsList.add( productClaimApprovalStats4 );

      productClaimApprovalStats5.setType( "products" );
      productClaimApprovalStats5.setStatus( "denied" );
      productClaimApprovalStats5.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.PRODUCTS_DENIED" ) );
      statsList.add( productClaimApprovalStats5 );

      productClaimApprovalStats6.setType( "products" );
      productClaimApprovalStats6.setStatus( "pending" );
      productClaimApprovalStats6.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.PRODUCTS_PENDING" ) );
      statsList.add( productClaimApprovalStats6 );

      productClaimsPromotion.setId( promotion.getId() );
      productClaimsPromotion.setName( promotion.getName() );
      productClaimsPromotion.setTimestamp( DateUtils.toDisplayString( claimDate ) );
      productClaimsPromotion.setStatsList( statsList );
      productClaimsPromotion.setClaims( claims );

    }

    if ( StringUtils.isNotBlank( claimProductApprovalListForm.getPromotionId() ) )
    {
      productClaimsParameters.setPromotionId( Long.parseLong( claimProductApprovalListForm.getPromotionId() ) );
    }

    if ( claimProductApprovalListForm.isOpen() )
    {
      productClaimsParameters.setClaimStatus( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.OPEN" ) );
    }
    else
    {
      productClaimsParameters.setClaimStatus( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.CLOSED" ) );
    }
    Calendar cal = Calendar.getInstance();
    cal.add( Calendar.MONTH, -3 );
    productClaimsParameters.setStartDate( DateUtils.toDisplayString( cal.getTime() ) );
    productClaimsParameters.setEndDate( DateUtils.toDisplayString( DateUtils.getCurrentDate() ) );

    approvalBean.setProductClaimsParameters( productClaimsParameters );
    approvalBean.setProductClaimsPromotion( productClaimsPromotion );

    request.setAttribute( ATTR_PROMOTION_CLAIMS_VALUE_LIST, promotionClaimsValueList );

    claimProductApprovalListForm.load( promotionClaimsValueList );

    request.setAttribute( "claimProductApprovalListForm", claimProductApprovalListForm );
    if ( StringUtils.isBlank( claimProductApprovalListForm.getPromotionId() ) && !Objects.isNull( approvalBean ) && !Objects.isNull( approvalBean.getProductClaimsPromotion() ) )
    {
      claimProductApprovalListForm.setPromotionId( approvalBean.getProductClaimsPromotion().getId().toString() );
    }
    claimProductApprovalListForm.setInitializationJson( toJson( approvalBean ) );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward showActivity( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException, IOException
  {
    ApprovalsClaimsListForm claimProductApprovalListForm = (ApprovalsClaimsListForm)form;
    claimProductApprovalListForm.getClaimProductApprovalFormByClaimProductIdString().clear();
    ClaimsJsonApprovalBean claimsJsonBean = new ClaimsJsonApprovalBean();
    final ProductClaimApprovalBean approvalBean = new ProductClaimApprovalBean();
    ProductClaimsPromotion productClaimsPromotion = new ProductClaimsPromotion();
    ProductClaimsParameters productClaimsParameters = new ProductClaimsParameters();
    Boolean saveOccurred = false;
    int pageNumber;
    if ( request.getParameter( "pageNumber" ) != null )
    {
      pageNumber = Integer.parseInt( request.getParameter( "pageNumber" ) );
    }
    else
    {
      pageNumber = 1;
    }

    int claimsPerPage = 20;
    int rowNumStart = ( pageNumber - 1 ) * claimsPerPage;
    int rowNumEnd = claimsPerPage;

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      if ( (Boolean)clientStateMap.get( "saveOccurred" ) != null )
      {
        saveOccurred = (Boolean)clientStateMap.get( "saveOccurred" );
      }
    }

    if ( request.getParameter( "startDate" ) != null && request.getParameter( "endDate" ) != null && request.getParameter( "promotionId" ) != null && request.getParameter( "claimStatus" ) != null )
    {
      claimProductApprovalListForm.setStartDate( request.getParameter( "startDate" ) );
      claimProductApprovalListForm.setEndDate( request.getParameter( "endDate" ) );
      claimProductApprovalListForm.setPromotionId( request.getParameter( "promotionId" ) );
      if ( request.getParameter( "claimStatus" ).equals( "open" ) )
      {
        claimProductApprovalListForm.setOpen( true );
      }
      else
      {
        claimProductApprovalListForm.setOpen( false );
      }
    }

    String sortedOn = "number";
    String sortedBy = "desc";
    if ( request.getParameter( "sortedOn" ) != null && request.getParameter( "sortedBy" ) != null )
    {
      sortedOn = request.getParameter( "sortedOn" );
      sortedBy = request.getParameter( "sortedBy" );
    }
    if ( claimProductApprovalListForm.getClaimStatus() != null )
    {
      claimProductApprovalListForm.setOpen( claimProductApprovalListForm.getClaimStatus().equals( "open" ) ? true : false );
    }
    claimProductApprovalListForm.setSortedOn( sortedOn );
    claimProductApprovalListForm.setSortedBy( sortedBy );
    claimProductApprovalListForm.setRowNumStart( rowNumStart );
    claimProductApprovalListForm.setRowNumEnd( rowNumEnd );

    if ( saveOccurred )
    {
      request.setAttribute( "saveOccurred", Boolean.TRUE );
    }

    String timeZoneID = getUserService().getUserTimeZone( UserManager.getUserId() );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );

    ApproverSeekingClaimQueryConstraint claimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );
    claimQueryConstraint.setApprovableUserId( UserManager.getUserId() );
    claimQueryConstraint.setEndDate( DateUtils.toDate( claimProductApprovalListForm.getEndDate() ) );
    claimQueryConstraint.setStartDate( DateUtils.toDate( claimProductApprovalListForm.getStartDate() ) );
    claimQueryConstraint.setOpen( claimProductApprovalListForm.isOpen() );
    claimQueryConstraint.setExpired( false );
    claimQueryConstraint.setClaimPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    claimQueryConstraint.setIncludedPromotionIds( new Long[] { Long.parseLong( claimProductApprovalListForm.getPromotionId() ) } );

    int promotionClaimsCnt = getClaimService().getClaimListCount( claimQueryConstraint );

    List promotionClaimsValueList = buildApprovablesList( claimProductApprovalListForm, true );

    for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
    {
      PromotionApprovableValue promotionApprovableValue = (PromotionApprovableValue)iter.next();
      List approvablesList = promotionApprovableValue.getApprovables();
      Claims claims = new Claims();
      MetaContent metaContent = new MetaContent();
      List<Results> resultList = new ArrayList<Results>();
      List<Columns> columnList = new ArrayList<Columns>();

      metaContent.setSortedOn( sortedOn );
      metaContent.setSortedBy( sortedBy );
      metaContent.setMaxRows( promotionClaimsCnt );
      metaContent.setRowsPerPage( claimsPerPage );
      metaContent.setPageNumber( pageNumber );
      metaContent.setExportUrl( RequestUtils.getBaseURI( request ) + "/claim/approvalsClaimsListMaintain.do?method=extractAsCsv" );

      Columns column1 = new Columns();
      column1.setName( "number" );
      column1.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.CLAIM_NUMBER" ) );
      column1.setSortable( true );
      columnList.add( column1 );
      Columns column2 = new Columns();
      column2.setName( "date" );
      column2.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.DATE" ) );
      column2.setSortable( true );
      columnList.add( column2 );
      Columns column3 = new Columns();
      column3.setName( "submitter" );
      column3.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.SUBMITTER" ) );
      column3.setSortable( true );
      columnList.add( column3 );
      Columns column4 = new Columns();
      column4.setName( "approver" );
      column4.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.APPROVER" ) );
      column4.setSortable( false );
      columnList.add( column4 );
      Columns column5 = new Columns();
      column5.setName( "products" );
      column5.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.ITEM" ) );
      column5.setSortable( false );
      columnList.add( column5 );
      Columns column6 = new Columns();
      column6.setName( "status" );
      column6.setText( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.STATUS" ) );
      column6.setSortable( false );
      columnList.add( column6 );

      List<ProductStatus> statusesList = new ArrayList<ProductStatus>();

      if ( !promotionApprovableValue.getPromotion().getApprovalOptionTypes().isEmpty() )
      {
        for ( Iterator iterApprovStats = promotionApprovableValue.getPromotion().getApprovalOptionTypes().iterator(); iterApprovStats.hasNext(); )
        {
          ApprovalStatusType approvalStatusType = (ApprovalStatusType)iterApprovStats.next();
          ProductStatus productStatus = new ProductStatus();

          if ( approvalStatusType.getCode().equals( ApprovalStatusType.PENDING ) )
          {
            productStatus.setValue( approvalStatusType.getCode() );
            productStatus.setText( CmsResourceBundle.getCmsBundle().getString( "claims.submission.PENDING" ) );
            productStatus.setReasonsList( null );
          }
          else if ( approvalStatusType.getCode().equals( ApprovalStatusType.APPROVED ) )
          {
            productStatus.setValue( approvalStatusType.getCode() );
            productStatus.setText( CmsResourceBundle.getCmsBundle().getString( "claims.submission.APPROVE" ) );
            productStatus.setReasonsList( null );
          }
          else if ( approvalStatusType.getCode().equals( ApprovalStatusType.DENIED ) )
          {
            productStatus.setValue( approvalStatusType.getCode() );
            productStatus.setText( CmsResourceBundle.getCmsBundle().getString( "claims.submission.DENY" ) );
            List<Reason> deniedReasonslist = new ArrayList<Reason>();
            for ( Iterator iterDenyReason = promotionApprovableValue.getPromotion().getDeniedReasonCodeTypes().iterator(); iterDenyReason.hasNext(); )
            {
              PromotionApprovalOptionReasonType denyReasonType = (PromotionApprovalOptionReasonType)iterDenyReason.next();
              Reason denyReason = new Reason();
              denyReason.setReasonValue( denyReasonType.getCode() );
              denyReason.setReasonText( denyReasonType.getName() );
              deniedReasonslist.add( denyReason );
            }
            productStatus.setReasonsList( deniedReasonslist );
          }
          else if ( approvalStatusType.getCode().equals( ApprovalStatusType.HOLD ) )
          {
            productStatus.setValue( approvalStatusType.getCode() );
            productStatus.setText( CmsResourceBundle.getCmsBundle().getString( "claims.submission.HOLD" ) );
            List<Reason> holdReasonslist = new ArrayList<Reason>();
            for ( Iterator iterHoldReason = promotionApprovableValue.getPromotion().getHeldReasonCodeTypes().iterator(); iterHoldReason.hasNext(); )
            {
              PromotionApprovalOptionReasonType holdReasonType = (PromotionApprovalOptionReasonType)iterHoldReason.next();
              Reason holdReason = new Reason();
              holdReason.setReasonValue( holdReasonType.getCode() );
              holdReason.setReasonText( holdReasonType.getName() );
              holdReasonslist.add( holdReason );
            }
            productStatus.setReasonsList( holdReasonslist );
          }
          if ( productStatus.getText() != null )
          {
            if ( productStatus.getText().equals( CmsResourceBundle.getCmsBundle().getString( "claims.submission.PENDING" ) ) )
            {
              statusesList.add( 0, productStatus );
            }
            else
            {
              statusesList.add( productStatus );
            }
          }
        }
      }
      metaContent.setColumnList( columnList );
      metaContent.setStatusesList( statusesList );

      Timestamp claimDate = null;
      for ( Iterator iterApprov = approvablesList.iterator(); iterApprov.hasNext(); )
      {
        ProductClaim claim = (ProductClaim)iterApprov.next();

        Results results = new Results();
        results.setId( claim.getId() );
        results.setNumber( claim.getClaimNumber() );
        if ( claimDate != null )
        {
          if ( claimDate.before( claim.getAuditCreateInfo().getDateCreated() ) )
          {
            claimDate = claim.getAuditCreateInfo().getDateCreated();
          }
        }
        else
        {
          claimDate = claim.getAuditCreateInfo().getDateCreated();
        }
        results.setDate( DateUtils.getStringFromTimeStamp( claim.getAuditCreateInfo().getDateCreated() ) );
        results.setSubmitter( claim.getSubmitter().getNameLFMWithComma() );
        List<Product> productsList = new ArrayList<Product>();
        for ( Iterator pIter = claim.getClaimProducts().iterator(); pIter.hasNext(); )
        {
          ClaimProduct claimProduct = (ClaimProduct)pIter.next();
          if ( claimProduct != null )
          {
            Product product = new Product();
            product.setClaimItemId( claimProduct.getId() );
            product.setProductName( claimProduct.getProduct().getName() );
            product.setStatus( claimProduct.getApprovalStatusType().getCode() );
            if ( claimProduct.getPromotionApprovalOptionReasonType() != null )
            {
              product.setStatusReason( claimProduct.getPromotionApprovalOptionReasonType().getName() );
            }
            if ( claimProduct.getCurrentClaimItemApprover() != null && !claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.PENDING ) )
            {
              if ( claimProduct.getCurrentClaimItemApprover().getApproverUser() != null )
              {
                product.setApprover( claimProduct.getCurrentClaimItemApprover().getApproverUser().getNameLFMWithComma() );
              }
              column4.setSortable( true );
            }
            else if ( claimProduct.getCurrentClaimItemApprover() == null && !claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.PENDING ) )
            {
              product.setApprover( CmsResourceBundle.getCmsBundle().getString( "home.navmenu.admin.SYSTEM" ) );
              column4.setSortable( true );
            }
            productsList.add( product );
          }
        }
        if ( productsList.size() > 1 && sortedOn.equals( "approver" ) && sortedBy.equals( "asc" ) )
        {
          Collections.sort( productsList, new ProductApproverAscendingComparator() );
        }
        if ( productsList.size() > 1 && sortedOn.equals( "approver" ) && sortedBy.equals( "desc" ) )
        {
          Collections.sort( productsList, new ProductApproverDescendingComparator() );
        }
        results.setProductsList( productsList );
        resultList.add( results );
      }

      if ( sortedOn.equals( "approver" ) && sortedBy.equals( "asc" ) )
      {
        Collections.sort( resultList, new ProductResultsAscendingComparator() );
      }
      if ( sortedOn.equals( "approver" ) && sortedBy.equals( "desc" ) )
      {
        Collections.sort( resultList, new ProductResultsDescendingComparator() );
      }

      claims.setMetaContent( metaContent );
      claims.setResultList( resultList );

      Promotion promotion = promotionApprovableValue.getPromotion();
      ProductClaimApprovalStats productClaimApprovalStats1 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats2 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats3 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats4 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats5 = new ProductClaimApprovalStats();
      ProductClaimApprovalStats productClaimApprovalStats6 = new ProductClaimApprovalStats();

      List<ProductClaimStatusCountsBean> productClaimStatusCountsList = getClaimService().getProductClaimStatusCount( promotion.getId() );

      for ( ProductClaimStatusCountsBean productClaimStatusCountsBean : productClaimStatusCountsList )
      {
        productClaimApprovalStats1.setCount( productClaimStatusCountsBean.getClaimsSubmitted() );
        productClaimApprovalStats2.setCount( productClaimStatusCountsBean.getClaimsPending() );
        productClaimApprovalStats3.setCount( productClaimStatusCountsBean.getProductsSubmitted() );
        productClaimApprovalStats4.setCount( productClaimStatusCountsBean.getProductsApproved() );
        productClaimApprovalStats5.setCount( productClaimStatusCountsBean.getProductsDenied() );
        productClaimApprovalStats6.setCount( productClaimStatusCountsBean.getProductsPending() );
      }
      List<ProductClaimApprovalStats> statsList = new ArrayList<ProductClaimApprovalStats>();

      productClaimApprovalStats1.setType( "claims" );
      productClaimApprovalStats1.setStatus( "submitted" );
      productClaimApprovalStats1.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.CLAIMS_SUBMITTED" ) );
      statsList.add( productClaimApprovalStats1 );

      productClaimApprovalStats2.setType( "claims" );
      productClaimApprovalStats2.setStatus( "pending" );
      productClaimApprovalStats2.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.CLAIMS_PENDING" ) );
      statsList.add( productClaimApprovalStats2 );

      productClaimApprovalStats3.setType( "products" );
      productClaimApprovalStats3.setStatus( "submitted" );
      productClaimApprovalStats3.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.PRODUCTS_SUBMITTED" ) );
      statsList.add( productClaimApprovalStats3 );

      productClaimApprovalStats4.setType( "products" );
      productClaimApprovalStats4.setStatus( "approved" );
      productClaimApprovalStats4.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.PRODUCTS_APPROVED" ) );
      statsList.add( productClaimApprovalStats4 );

      productClaimApprovalStats5.setType( "products" );
      productClaimApprovalStats5.setStatus( "denied" );
      productClaimApprovalStats5.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.PRODUCTS_DENIED" ) );
      statsList.add( productClaimApprovalStats5 );

      productClaimApprovalStats6.setType( "products" );
      productClaimApprovalStats6.setStatus( "pending" );
      productClaimApprovalStats6.setName( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.PRODUCTS_PENDING" ) );
      statsList.add( productClaimApprovalStats6 );

      productClaimsPromotion.setId( promotion.getId() );
      productClaimsPromotion.setName( promotion.getName() );
      productClaimsPromotion.setTimestamp( DateUtils.toDisplayString( claimDate ) );
      productClaimsPromotion.setStatsList( statsList );
      productClaimsPromotion.setClaims( claims );
    }

    productClaimsParameters.setPromotionId( Long.parseLong( claimProductApprovalListForm.getPromotionId() ) );
    if ( claimProductApprovalListForm.isOpen() )
    {
      productClaimsParameters.setClaimStatus( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.OPEN" ) );
    }
    else
    {
      productClaimsParameters.setClaimStatus( CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.CLOSED" ) );
    }
    productClaimsParameters.setStartDate( claimProductApprovalListForm.getStartDate() );
    productClaimsParameters.setEndDate( claimProductApprovalListForm.getEndDate() );

    approvalBean.setProductClaimsParameters( productClaimsParameters );
    approvalBean.setProductClaimsPromotion( productClaimsPromotion );
    claimsJsonBean.setProductClaimApprovalBean( approvalBean );

    request.setAttribute( ATTR_PROMOTION_CLAIMS_VALUE_LIST, promotionClaimsValueList );

    claimProductApprovalListForm.load( promotionClaimsValueList );

    request.setAttribute( "claimProductApprovalListForm", claimProductApprovalListForm );

    super.writeAsJsonToResponse( claimsJsonBean, response );
    return null;
  }

  /**
   * Prepares anything necessary before displaying the update screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws InvalidClientStateException 
   * @throws IOException 
   */
  public ActionForward saveApprovals( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException, IOException
  {
    ApprovalsClaimsListForm claimProductApprovalListForm = (ApprovalsClaimsListForm)form;

    List promotionClaimsValueList = buildApprovablesList( claimProductApprovalListForm, true );

    List claims = new ArrayList();
    for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
    {
      PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next();
      claims.addAll( promotionClaimsValue.getApprovables() );
    }
    User approver = getUserService().getUserById( UserManager.getUserId() );

    claimProductApprovalListForm.populateClaimProductDomainObjects( promotionClaimsValueList, approver );

    try
    {
      getClaimService().saveClaims( claims, null, approver, false );
      request.setAttribute( "saveOccurred", Boolean.TRUE );
    }
    catch( ServiceErrorException e )
    {
      // TODO: add error snippet to jsp
      ActionMessages errors = new ActionMessages();
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    // reload form
    return prepareUpdate( mapping, form, request, response );
  }

  public static List<PromotionApprovableValue> buildApprovablesList( ApprovalsClaimsListForm claimProductApprovalListForm, boolean isPaginated )
  {
    boolean includeNodeAssociation = false;

    if ( claimProductApprovalListForm.getMethod() != null && claimProductApprovalListForm.getMethod().equalsIgnoreCase( "extractAsCsv" ) )
    {
      includeNodeAssociation = true;
    }

    List<PromotionApprovableValue> approvableList = buildPromotionClaimsValueList( claimProductApprovalListForm );

    if ( isPaginated )
    {
      ListPageInfo<PromotionApprovableValue> listPageInfo = new ListPageInfo<PromotionApprovableValue>( approvableList,
                                                                                                        APPROVAL_LIST_PAGE_SIZE,
                                                                                                        claimProductApprovalListForm.getRequestedPage() == null
                                                                                                            ? ListPageInfo.DEFAULT_INITIAL_PAGE
                                                                                                            : claimProductApprovalListForm.getRequestedPage() );
      claimProductApprovalListForm.setListPageInfo( listPageInfo );
      claimProductApprovalListForm.setRequestedPage( 1L );
      approvableList = listPageInfo.getCurrentPageList();
    }

    return approvableList;
  }

  public static List<PromotionApprovableValue> buildPromotionClaimsValueList( ApprovalsClaimsListForm claimProductApprovalListForm )
  {
    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );

    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );

    Long promotionId = StringUtils.isBlank( claimProductApprovalListForm.getPromotionId() ) ? null : new Long( claimProductApprovalListForm.getPromotionId() );

    Date startDate = DateUtils.toDate( claimProductApprovalListForm.getStartDate() );
    Date endDate = DateUtils.toDate( claimProductApprovalListForm.getEndDate() );
    List<PromotionApprovableValue> promotionClaimsValueList = getClaimService().getProductClaimsForApprovalByUser( UserManager.getUserId(),
                                                                                                                   promotionId != null ? new Long[] { promotionId } : null,
                                                                                                                   Boolean.valueOf( claimProductApprovalListForm.isOpen() ),
                                                                                                                   startDate,
                                                                                                                   endDate,
                                                                                                                   PromotionType.lookup( PromotionType.PRODUCT_CLAIM ),
                                                                                                                   claimAssociationRequestCollection,
                                                                                                                   promotionAssociationRequestCollection,
                                                                                                                   Boolean.FALSE,
                                                                                                                   claimProductApprovalListForm.getSortedOn(),
                                                                                                                   claimProductApprovalListForm.getSortedBy(),
                                                                                                                   claimProductApprovalListForm.getRowNumStart(),
                                                                                                                   claimProductApprovalListForm.getRowNumEnd() );

    PropertyComparator.sort( promotionClaimsValueList, new MutableSortDefinition( "promotion.approvalEndDate", true, true ) );

    return promotionClaimsValueList;
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward extractAsCsv( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );

    Long promotionId = Long.valueOf( ( (ApprovalsClaimsListForm)form ).getPromotionId() );

    ApprovalsProductClaimListExportBean exportBean = new ApprovalsProductClaimListExportBean();
    exportBean.setExportList( extractApprovables( buildApprovablesList( (ApprovalsClaimsListForm)form, false ) ) );
    exportBean.setNodeService( getNodeService() );
    exportBean.extractAsCsv( getPromotionService().getPromotionByIdWithAssociations( promotionId, arc ), response );
    return null;
  }

  @SuppressWarnings( "unchecked" )
  private static List<Approvable> extractApprovables( List<PromotionApprovableValue> promotionApprovableValueList )
  {
    ArrayList<Approvable> approvables = new ArrayList<Approvable>();

    for ( PromotionApprovableValue promotionApprovableValue : promotionApprovableValueList )
    {
      approvables.addAll( promotionApprovableValue.getApprovables() );
    }

    return approvables;
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private static NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
