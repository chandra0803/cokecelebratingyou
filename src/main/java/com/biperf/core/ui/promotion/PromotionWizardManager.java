/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionWizardManager.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;

/**
 * PromotionWizardManager.
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
 * <td>crosenquest</td>
 * <td>Aug 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionWizardManager implements Serializable
{
  /**
   * Logger for this class
   */
  private static final Logger log = Logger.getLogger( PromotionWizardManager.class );

  public static final String SESSION_KEY = "PROMOTION_WIZARD_FORM_MANAGER";
  public static final String WORKFLOW_CONTAINER = "com.livinglogic.struts.workflow.WorkflowContainer.container";

  public static final String PROMOTION_TYPE_REQUEST_ATTR = "PROMOTION_TYPE_REQUEST_ATTR";

  private Promotion promotion = null;
  private PromotionType promotionType;
  private List promotionClaimFormStepElementValidations = null;
  private String pageNumber = "";

  public Promotion getPromotion()
  {
    return this.promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public List getPromotionClaimFormStepElementValidations()
  {
    return this.promotionClaimFormStepElementValidations;
  }

  public void setPromotionClaimFormStepElementValidations( List promotionClaimFormStepElementValidations )
  {
    this.promotionClaimFormStepElementValidations = promotionClaimFormStepElementValidations;
  }

  public PromotionType getPromotionType()
  {
    return this.promotionType;
  }

  /**
   * Return the struts module path for the promotion type code defined by the
   * the promotionTypeCode. 
   * @param promotionTypeCode
   */
  public static String getStrutsModulePath( String promotionTypeCode )
  {
    String moduleDir;

    if ( promotionTypeCode.equals( PromotionType.PRODUCT_CLAIM ) )
    {
      moduleDir = "promotionProductClaim";
    }
    else if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      moduleDir = "promotionRecognition";
    }
    else if ( promotionTypeCode.equals( PromotionType.QUIZ ) )
    {
      moduleDir = "promotionQuiz";
    }
    else if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      moduleDir = "promotionNomination";
    }
    else if ( promotionTypeCode.equals( PromotionType.SURVEY ) )
    {
      moduleDir = "promotionSurvey";
    }
    else if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) )
    {
      moduleDir = "promotionGoalQuest";
    }
    else if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
    {
      moduleDir = "promotionChallengepoint";
    }
    else if ( promotionTypeCode.equals( PromotionType.WELLNESS ) )
    {
      moduleDir = "promotionWellness";
    }
    else if ( promotionTypeCode.equals( PromotionType.THROWDOWN ) )
    {
      moduleDir = "promotionThrowDown";
    }
    else if ( promotionTypeCode.equals( PromotionType.BADGE ) )
    {
      moduleDir = "promotion";
    }
    else if ( promotionTypeCode.equals( PromotionType.ENGAGEMENT ) )
    {
      moduleDir = "promotionEngagement";
    }
    else if ( promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      moduleDir = "promotionSSI";
    }
    else
    {
      log.error( "Unhandled promotion code: " + promotionTypeCode );
      moduleDir = null;
    }
    return moduleDir;
  }

  /**
   * Return the struts module path for the promotion type code defined by the
   * Request attribute <code>PROMOTION_TYPE_REQUEST_ATTR</code>. 
   * @param request
   */
  public static String getStrutsModulePath( HttpServletRequest request )
  {
    // the attribute PROMOTION_TYPE_REQUEST_ATTR shosuld be set in each page's controller - see
    // PromotionBasicsController as an example.
    String promotionTypeCode = (String)request.getAttribute( PROMOTION_TYPE_REQUEST_ATTR );

    return getStrutsModulePath( promotionTypeCode );
  }

  public void setPromotionType( PromotionType promotionType )
  {
    this.promotionType = promotionType;
  }

  public String getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber( String pageNumber )
  {
    this.pageNumber = pageNumber;
  }

}
