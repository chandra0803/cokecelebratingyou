
package com.biperf.core.ui.claim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.recognition.state.RecipientBean;

public class ManagerAddPointsForm extends BaseForm implements Serializable
{

  public static final String SESSION_KEY = "managerAddPointsState";

  private ParticipantInfoView recipientInfo;
  private ParticipantInfoView submitterInfo;
  private RecognitionDetailBean detailBean;
  private String nodeBudgetJson;

  private Long promotionId;
  private String promotionName;
  private Long submitterNodeId;
  private Long mgrPromotionId;
  private String mgrPromotionName;

  // award information
  private String awardType;
  private Long awardFixed;
  private Long awardMin;
  private Long awardMax;

  private String comment;

  private Map<Integer, RecipientBean> claimRecipientFormBeans = new HashMap<Integer, RecipientBean>();

  public ParticipantInfoView getRecipientInfo()
  {
    return recipientInfo;
  }

  public void setRecipientInfo( ParticipantInfoView recipientInfo )
  {
    this.recipientInfo = recipientInfo;
  }

  public ParticipantInfoView getSubmitterInfo()
  {
    return submitterInfo;
  }

  public void setSubmitterInfo( ParticipantInfoView submitterInfo )
  {
    this.submitterInfo = submitterInfo;
  }

  public RecognitionDetailBean getDetailBean()
  {
    return detailBean;
  }

  public void setDetailBean( RecognitionDetailBean detailBean )
  {
    this.detailBean = detailBean;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getSubmitterNodeId()
  {
    return submitterNodeId;
  }

  public void setSubmitterNodeId( Long submitterNodeId )
  {
    this.submitterNodeId = submitterNodeId;
  }

  public String getNodeBudgetJson()
  {
    return nodeBudgetJson;
  }

  public void setNodeBudgetJson( String nodeBudgetJson )
  {
    this.nodeBudgetJson = nodeBudgetJson;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public Long getAwardFixed()
  {
    return awardFixed;
  }

  public void setAwardFixed( Long awardFixed )
  {
    this.awardFixed = awardFixed;
  }

  public Long getAwardMin()
  {
    return awardMin;
  }

  public void setAwardMin( Long awardMin )
  {
    this.awardMin = awardMin;
  }

  public Long getAwardMax()
  {
    return awardMax;
  }

  public void setAwardMax( Long awardMax )
  {
    this.awardMax = awardMax;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public int getClaimRecipientFormBeansCount()
  {
    if ( claimRecipientFormBeans == null || claimRecipientFormBeans.isEmpty() )
    {
      return 0;
    }
    return claimRecipientFormBeans.size();
  }

  public RecipientBean getClaimRecipientFormBeans( int index )
  {
    RecipientBean bean = claimRecipientFormBeans.get( index );
    if ( bean == null )
    {
      bean = new RecipientBean();
      claimRecipientFormBeans.put( index, bean );
    }
    return bean;
  }

  public List<RecipientBean> getRecipients()
  {
    return new ArrayList<RecipientBean>( claimRecipientFormBeans.values() );
  }

  public RecipientBean getRecipientBean()
  {
    if ( claimRecipientFormBeans != null && !claimRecipientFormBeans.isEmpty() )
    {
      return claimRecipientFormBeans.get( 0 );
    }
    return null;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Long getMgrPromotionId()
  {
    return mgrPromotionId;
  }

  public void setMgrPromotionId( Long mgrPromotionId )
  {
    this.mgrPromotionId = mgrPromotionId;
  }

  public String getMgrPromotionName()
  {
    return mgrPromotionName;
  }

  public void setMgrPromotionName( String mgrPromotionName )
  {
    this.mgrPromotionName = mgrPromotionName;
  }

}
