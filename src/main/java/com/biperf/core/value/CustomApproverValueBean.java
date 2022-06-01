
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.CustomApproverRoutingType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.PromotionApprovalParticipantBean;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.value.nomination.NominationApproverValueBean;

public class CustomApproverValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Long level;
  private String customApproverTypeValue;
  private String customApproverRoutingTypeValue;
  private Long characteristicId;
  private List<Characteristic> characteristics = new ArrayList<Characteristic>();
  private List<CustomApproverRoutingType> customApproverRoutingList = new ArrayList<CustomApproverRoutingType>( CustomApproverRoutingType.getList() );
  private String approverOptionId;
  private PromotionApprovalParticipantBean participantBean = new PromotionApprovalParticipantBean();
  private List<PromotionApprovalParticipantBean> approverList = new ArrayList<PromotionApprovalParticipantBean>();
  private String viewApproverLink;
  private int sequenceNum;

  private List<NominationApproverValueBean> nomApproverList = new ArrayList<NominationApproverValueBean>();

  public Long getLevel()
  {
    return level;
  }

  public void setLevel( Long level )
  {
    this.level = level;
  }

  public String getCustomApproverTypeValue()
  {
    return customApproverTypeValue;
  }

  public void setCustomApproverTypeValue( String customApproverTypeValue )
  {
    this.customApproverTypeValue = customApproverTypeValue;
  }

  public String getApproverOptionId()
  {
    return approverOptionId;
  }

  public void setApproverOptionId( String approverOptionId )
  {
    this.approverOptionId = approverOptionId;
  }

  public List<Characteristic> getCharacteristics()
  {
    return characteristics;
  }

  public void setCharacteristics( List<Characteristic> characteristics )
  {
    this.characteristics = characteristics;
  }

  public Long getCharacteristicId()
  {
    return characteristicId;
  }

  public void setCharacteristicId( Long characteristicId )
  {
    this.characteristicId = characteristicId;
  }

  public List<PromotionApprovalParticipantBean> getApproverListAsList()
  {
    return approverList;
  }

  public void setApproverListAsList( List<PromotionApprovalParticipantBean> approverList )
  {
    this.approverList = approverList;
  }

  public PromotionApprovalParticipantBean getApprover( int index )
  {
    while ( approverList.size() <= index )
    {
      approverList.add( new PromotionApprovalParticipantBean() );
    }

    return approverList.get( index );
  }

  public PromotionApprovalParticipantBean getParticipantBean()
  {
    return participantBean;
  }

  public void setParticipantBean( PromotionApprovalParticipantBean participantBean )
  {
    this.participantBean = participantBean;
  }

  public String getViewApproverLink()
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( "promotionId", getApproverOptionId() );

    String stringVal1 = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String val2 = "/nomination/viewApprovers.do";

    /*
     * this.viewApproverLink = ClientStateUtils.generateEncodedLink( stringVal1, val2, paramMap );
     */

    this.viewApproverLink = stringVal1 + val2 + "?optionId=" + this.approverOptionId;

    return this.viewApproverLink;

  }

  private SystemVariableService getSystemVariableService()
  {

    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  public void setViewApproverLink( String viewApproverLink )
  {
    this.viewApproverLink = viewApproverLink;
  }

  public List<NominationApproverValueBean> getNomApproverList()
  {
    return nomApproverList;
  }

  public void setNomApproverList( List<NominationApproverValueBean> nomApproverList )
  {
    this.nomApproverList = nomApproverList;
  }

  public boolean eligibleForViewApproverModal()
  {
    return CustomApproverType.eligibleForViewApproverModal.contains( this.customApproverTypeValue ) && !StringUtils.isEmpty( this.approverOptionId );
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public String getCustomApproverRoutingTypeValue()
  {
    return customApproverRoutingTypeValue;
  }

  public void setCustomApproverRoutingTypeValue( String customApproverRoutingTypeValue )
  {
    this.customApproverRoutingTypeValue = customApproverRoutingTypeValue;
  }

  public List<CustomApproverRoutingType> getCustomApproverRoutingList()
  {
    return customApproverRoutingList;
  }

  public void setCustomApproverRoutingList( List<CustomApproverRoutingType> customApproverRoutingList )
  {
    this.customApproverRoutingList = customApproverRoutingList;
  }

}
