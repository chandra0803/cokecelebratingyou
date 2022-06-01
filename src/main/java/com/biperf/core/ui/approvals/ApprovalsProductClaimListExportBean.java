
package com.biperf.core.ui.approvals;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.MinimumQualifierStatusService;
import com.biperf.core.ui.BaseExportBean;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class ApprovalsProductClaimListExportBean extends BaseExportBean<Approvable>
{
  private static final String NEW_LINE = "\n";
  private static final String QUOTE = "\"";
  private static final String QUOTE_COMMA_QUOTE = "\",\"";
  private static final String COMMA = ",";

  @Override
  protected String buildCsvFileName()
  {
    return "product_claim_approvals.csv";
  }

  @Override
  protected String buildPdfFileName()
  {
    return "product_claim_approvals.pdf";
  }

  @Override
  protected String buildCsvHeader( Promotion promotion, Approvable exportItem )
  {
    Content content = CmsUtil.getContentFromReaderObject( ContentReaderManager.getContentReader().getContent( "claims.product.approval.details" ) );
    StringBuilder csvHeader = new StringBuilder();
    csvHeader.append( content.getContentDataMap().get( "CLAIM_NUMBER" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "CLAIM_DATE" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "SUBMITTER" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "SUBMITTER_MANAGER" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "P_ORG_NAME" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "PROMOTION" ) );
    csvHeader.append( COMMA );
    for ( ClaimFormStep claimFormStep : promotion.getClaimForm().getClaimFormSteps() )
    {
      for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
      {
        csvHeader.append( claimFormStepElement.getI18nLabel() );
        csvHeader.append( COMMA );
      }
    }
    csvHeader.append( content.getContentDataMap().get( "PRODUCT" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "CATEGORY" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "SUBCATEGORY" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "CHARACTERISTICS" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "QUANTITY" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "STATUS" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "AWARD" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "APPROVER" ) );
    csvHeader.append( COMMA );
    csvHeader.append( content.getContentDataMap().get( "TEAM_MEMBERS" ) );
    return csvHeader.toString();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected String buildCsvRow( Approvable exportItem )
  {
    ProductClaim productClaim = (ProductClaim)exportItem;
    StringBuffer csvRow = new StringBuffer();
    Set<ClaimProduct> claimProducts = productClaim.getClaimProducts();
    int claimProductSize = claimProducts.size();
    int i = 0;
    for ( ClaimProduct claimProduct : claimProducts )
    {
      csvRow.append( QUOTE );
      csvRow.append( productClaim.getClaimNumber() );
      csvRow.append( QUOTE_COMMA_QUOTE );
      csvRow.append( DateUtils.toDisplayString( productClaim.getSubmissionDate() ) );
      csvRow.append( QUOTE_COMMA_QUOTE );
      if ( productClaim.getSubmitter() != null )
      {
        csvRow.append( productClaim.getSubmitter().getNameLFMWithComma() );
        csvRow.append( QUOTE_COMMA_QUOTE );
        if ( productClaim.getSubmitter().getPrimaryUserNode() != null )
        {
          if ( productClaim.getSubmitter().getPrimaryUserNode().getNode() != null )
          {
            int count = 0;
            for ( Iterator iter = productClaim.getSubmitter().getPrimaryUserNode().getNode().getNodeManagersForUser( productClaim.getSubmitter() ).iterator(); iter.hasNext(); )
            {
              if ( count > 0 )
              {
                csvRow.append( COMMA );
              }
              User user = (User)iter.next();
              csvRow.append( user.getNameLFMWithComma() );
              count++;
            }
            csvRow.append( QUOTE_COMMA_QUOTE );
            csvRow.append( productClaim.getSubmitter().getPrimaryUserNode().getNode().getName() );
            csvRow.append( QUOTE_COMMA_QUOTE );
          }
          else
          {
            csvRow.append( QUOTE_COMMA_QUOTE );
            csvRow.append( QUOTE_COMMA_QUOTE );
          }
        }
        else
        {
          csvRow.append( QUOTE_COMMA_QUOTE );
          csvRow.append( QUOTE_COMMA_QUOTE );
        }
      }
      else
      {
        csvRow.append( QUOTE_COMMA_QUOTE );
        csvRow.append( QUOTE_COMMA_QUOTE );
        csvRow.append( QUOTE_COMMA_QUOTE );
      }
      if ( productClaim.getPromotion() != null )
      {
        csvRow.append( productClaim.getPromotion().getName() );
      }
      csvRow.append( QUOTE_COMMA_QUOTE );
      if ( exportItem.getPromotion() != null )
      {
        if ( exportItem.getPromotion().getClaimForm() != null )
        {
          if ( exportItem.getPromotion().getClaimForm().getClaimFormSteps() != null )
          {
            for ( ClaimFormStep claimFormStep : exportItem.getPromotion().getClaimForm().getClaimFormSteps() )
            {
              if ( claimFormStep.getClaimFormStepElements() != null )
              {
                for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
                {
                  if ( productClaim.getClaimElements() != null )
                  {
                    String value = "";
                    for ( ClaimElement claimElement : productClaim.getClaimElements() )
                    {
                      if ( claimElement.getClaimFormStepElement() != null )
                      {
                        if ( claimElement.getClaimFormStepElement().getId() != null )
                        {
                          if ( claimElement.getClaimFormStepElement().getId().equals( claimFormStepElement.getId() ) )
                          {
                            value = StringUtils.defaultString( claimElement.getValue() );
                            break;
                          }
                        }
                      }
                    }
                    csvRow.append( value );
                    csvRow.append( QUOTE_COMMA_QUOTE );
                  }
                }
              }
            }
          }
        }
      }
      if ( claimProduct.getProduct() != null )
      {
        csvRow.append( claimProduct.getProduct().getName() );
        csvRow.append( QUOTE_COMMA_QUOTE );
        csvRow.append( claimProduct.getProduct().getProductCategoryName() );
        csvRow.append( QUOTE_COMMA_QUOTE );
        csvRow.append( claimProduct.getProduct().getProductSubCategoryName() );
        csvRow.append( QUOTE_COMMA_QUOTE );
      }
      else
      {
        csvRow.append( QUOTE_COMMA_QUOTE );
        csvRow.append( QUOTE_COMMA_QUOTE );
        csvRow.append( QUOTE_COMMA_QUOTE );
      }
      if ( claimProduct.getClaimProductCharacteristics() != null )
      {
        for ( Iterator iter = claimProduct.getClaimProductCharacteristics().iterator(); iter.hasNext(); )
        {
          ClaimProductCharacteristic claimProductCharacteristic = (ClaimProductCharacteristic)iter.next();
          if ( claimProductCharacteristic.getProductCharacteristicType() != null )
          {
            csvRow.append( claimProductCharacteristic.getProductCharacteristicType().getCharacteristicName() + " " + claimProductCharacteristic.getValue() );
            csvRow.append( COMMA );
          }
        }
      }
      csvRow.append( QUOTE_COMMA_QUOTE );
      csvRow.append( claimProduct.getQuantity() );
      csvRow.append( QUOTE_COMMA_QUOTE );
      if ( claimProduct.getApprovalStatusType() != null )
      {
        csvRow.append( claimProduct.getApprovalStatusType().getName() );
      }
      csvRow.append( QUOTE_COMMA_QUOTE );

      if ( productClaim.getSubmitter() != null )
      {
        if ( claimProduct.getApprovalStatusType() != null )
        {
          if ( claimProduct.getApprovalStatusType().getCode() != null )
          {
            if ( claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
            {
              Long earnings = new Long( 0 );

              MinimumQualifierStatus minimumQualifierStatus = null;
              Long minQualifierStatusId = getClaimService().getMinQualifierId( productClaim.getId(), claimProduct.getProduct().getId() );
              if ( Objects.nonNull( minQualifierStatusId ) )
              {
                minimumQualifierStatus = getMinimumQualifierStatusService()
                    .getMinimumQualifierStatusById( getClaimService().getMinQualifierId( productClaim.getId(), claimProduct.getProduct().getId() ) );

                earnings = getClaimService().getEarningsForClaim( productClaim.getId(), productClaim.getSubmitter().getId() );

                if ( Objects.nonNull( minimumQualifierStatus ) && minimumQualifierStatus.isMinQualifierMet() )
                {
                  if ( earnings != null )
                  {
                    csvRow.append( earnings );
                  }
                }
                else
                {
                  csvRow.append( new Long( 0 ) );

                }
              }
              else
              {
                csvRow.append( new Long( 0 ) );

              }

            }
          }
        }
      }
      csvRow.append( QUOTE_COMMA_QUOTE );
      if ( claimProduct.getCurrentApproverUser() != null )
      {
        csvRow.append( claimProduct.getCurrentApproverUser().getNameLFMNoComma() );
      }
      csvRow.append( QUOTE_COMMA_QUOTE );
      if ( productClaim.getClaimParticipants() != null )
      {
        for ( ProductClaimParticipant productClaimParticipant : productClaim.getClaimParticipants() )
        {
          if ( productClaimParticipant.getParticipant() != null )
          {
            csvRow.append( productClaimParticipant.getParticipant().getNameLFMWithComma() );
          }
        }
      }
      csvRow.append( QUOTE );

      if ( i < claimProductSize - 1 )
      {
        csvRow.append( NEW_LINE );
      }
      i++;
    }
    return csvRow.toString();
  }

  /**
   * No PDF for Product Claims
   */
  protected String buildXMLString( Promotion promotion, List<Approvable> approvables )
  {
    return null;
  }

  protected String buildXMLStringRecognition( List<AbstractRecognitionClaim> approvables )
  {
    return null;
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)BeanLocator.getBean( ClaimService.BEAN_NAME );
  }

  private MinimumQualifierStatusService getMinimumQualifierStatusService()
  {
    return (MinimumQualifierStatusService)BeanLocator.getBean( MinimumQualifierStatusService.BEAN_NAME );
  }
}
