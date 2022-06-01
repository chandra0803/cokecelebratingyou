
package com.biperf.core.ui.productclaim;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class ClaimSubmittedBean implements Serializable
{
  public static final String KEY = "claimSubmittedBean";
  public Long claimId = null;
  public Long promotionId = null;
  public String claimNumber;
  public Date submittedDate;

  private ClaimSubmittedBean()
  {
  }

  public static ClaimSubmittedBean addToSession( HttpServletRequest request, Long claimId, Long promotionId, String claimNumber, Date submittedDate )
  {
    ClaimSubmittedBean bean = create( claimId, promotionId, claimNumber, submittedDate );
    request.getSession().setAttribute( KEY, bean );
    return bean;
  }

  private static ClaimSubmittedBean create( Long claimId, Long promotionId, String claimNumber, Date submittedDate )
  {
    ClaimSubmittedBean bean = new ClaimSubmittedBean();
    bean.setClaimId( claimId );
    bean.setPromotionId( promotionId );
    bean.setClaimNumber( claimNumber );
    bean.setSubmittedDate( submittedDate );

    return bean;
  }

  public static void moveToRequest( HttpServletRequest request )
  {
    // get it from session....
    ClaimSubmittedBean bean = (ClaimSubmittedBean)request.getSession().getAttribute( KEY );

    // add it to the request object...
    request.setAttribute( KEY, bean );

    // remove it from session....
    request.getSession().removeAttribute( KEY );
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getClaimNumber()
  {
    return claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
  }

  public Date getSubmittedDate()
  {
    return submittedDate;
  }

  public void setSubmittedDate( Date submittedDate )
  {
    this.submittedDate = submittedDate;
  }

}
