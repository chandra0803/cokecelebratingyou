
package com.biperf.core.ui.recognition;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.gamification.BadgeDetails;

public class RecognitionSentBean implements Serializable
{
  public static final String KEY = "recognitionSentBean";
  public Long claimId = null;
  public Long promotionId = null;
  public String promotionType;
  public List<BadgeDetails> badgeDetails;
  private boolean isPurlRecognition;
  private boolean isEasyRecognition;
  private boolean isAllowManagerAward;

  private RecognitionSentBean()
  {
  }

  public static RecognitionSentBean addToSession( HttpServletRequest request,
                                                  Long claimId,
                                                  Long promotionId,
                                                  String promotionType,
                                                  List<BadgeDetails> badgeDetails,
                                                  boolean isPurlRecognition,
                                                  boolean isAllowManagerAward )
  {
    RecognitionSentBean bean = create( claimId, promotionId, promotionType, badgeDetails, isPurlRecognition, false, isAllowManagerAward );
    request.getSession().setAttribute( KEY, bean );
    return bean;
  }

  public static RecognitionSentBean addToRequest( HttpServletRequest request, Long claimId, Long promotionId, String promotionType, List<BadgeDetails> badgeDetails, boolean isPurlRecognition )
  {
    RecognitionSentBean bean = create( claimId, promotionId, promotionType, badgeDetails, isPurlRecognition, true, false );
    request.setAttribute( KEY, bean );
    return bean;
  }

  public static RecognitionSentBean create( Long claimId,
                                            Long promotionId,
                                            String promotionType,
                                            List<BadgeDetails> badgeDetails,
                                            boolean isPurlRecognition,
                                            boolean isEasyRecognition,
                                            boolean isAllowManagerAward )
  {
    RecognitionSentBean bean = new RecognitionSentBean();
    bean.setClaimId( claimId );
    bean.setPromotionId( promotionId );
    bean.setPromotionType( promotionType );
    bean.setBadgeDetails( badgeDetails );
    bean.setIsPurlRecognition( isPurlRecognition );
    bean.setIsEasyRecognition( isEasyRecognition );
    bean.setIsAllowManagerAward( isAllowManagerAward );

    return bean;
  }

  public static void moveToRequest( HttpServletRequest request )
  {
    // get it from session....
    RecognitionSentBean bean = (RecognitionSentBean)request.getSession().getAttribute( KEY );

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

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public List<BadgeDetails> getBadgeDetails()
  {
    return badgeDetails;
  }

  public void setBadgeDetails( List<BadgeDetails> badgeDetails )
  {
    this.badgeDetails = badgeDetails;
  }

  public void setIsPurlRecognition( boolean isPurlRecognition )
  {
    this.isPurlRecognition = isPurlRecognition;
  }

  public boolean getIsPurlRecognition()
  {
    return isPurlRecognition;
  }

  public boolean getIsEasyRecognition()
  {
    return isEasyRecognition;
  }

  private void setIsEasyRecognition( boolean isEasyRecognition )
  {
    this.isEasyRecognition = isEasyRecognition;
  }

  public boolean getIsAllowManagerAward()
  {
    return isAllowManagerAward;
  }

  public void setIsAllowManagerAward( boolean isAllowManagerAward )
  {
    this.isAllowManagerAward = isAllowManagerAward;
  }

}
