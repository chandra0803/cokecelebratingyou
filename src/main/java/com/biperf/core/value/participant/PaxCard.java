
package com.biperf.core.value.participant;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PromotionMenuBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( Include.NON_NULL )
public class PaxCard
{

  @JsonProperty( "id" )
  private Long paxId;
  @JsonProperty( "lastName" )
  private String lastName;
  @JsonProperty( "firstName" )
  private String firstName;
  @JsonProperty( "avatarUrl" )
  private String avatarUrl;
  //
  @JsonProperty( "organization" )
  private String organization;
  @JsonProperty( "departmentName" )
  private String departmentName;
  @JsonProperty( "jobName" )
  private String jobName;
  @JsonProperty( "participantUrl" )
  private String participantUrl;
  //
  @JsonProperty( "promotions" )
  private List<PromotionBean> promotions;

  @JsonProperty( "follow" )
  private boolean follow = false;

  @JsonProperty( "primaryNodeId" )
  private Long primaryNodeId;

  @JsonProperty( "allNodeIds" )
  private List<Long> allNodeIds;

  @JsonProperty( "purlData" )
  private List<PurlData> purlData;

  @JsonProperty( "optOutAwards" )
  private boolean optOutAwards = false;

  @JsonProperty( "countryCode" )
  private String countryCode;

  @JsonProperty( "nodes" )
  private List<NameableBean> nodes;

  @JsonProperty( "nodeId" )
  private Long nodeId;
  
  //Client customization start
  @JsonProperty( "currency" )
  private String currency;
  
  @JsonProperty( "awardMax" )
  private Long awardMax;
  
  @JsonProperty( "awardMin" )
  private Long awardMin;
  //Client customization end
  
  public PaxCard()
  {
  }

  public PaxCard( PaxIndexData indexData, List<PromotionBean> promotions )
  {
    this.paxId = indexData.getUserId();
    this.lastName = indexData.getLastname();
    this.firstName = indexData.getFirstname();
    this.promotions = promotions;
    this.primaryNodeId = indexData.getPrimaryNodeId();
    this.allNodeIds = indexData.getAllNodeIds();
    this.optOutAwards = indexData.isOptOutAwards();
  }

  public Long getPaxId()
  {
    return paxId;
  }

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public List<PromotionBean> getPromotions()
  {
    return promotions;
  }

  public void setPromotions( List<PromotionBean> promotions )
  {
    this.promotions = promotions;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public boolean isOptOutAwards()
  {
    return optOutAwards;
  }

  public void setOptOutAwards( boolean optOutAwards )
  {
    this.optOutAwards = optOutAwards;
  }

  @JsonInclude( Include.NON_NULL )
  public static class PromotionBean
  {

    @JsonProperty( "id" )
    private Long promotionId;
    @JsonProperty( "name" )
    private String promotionName;
    @JsonProperty( "type" )
    private String promotionType;

    @JsonIgnore
    private boolean canReceive;

    @JsonProperty( "attributes" )
    private PromotionAttributes attributes;

    public PromotionBean( Promotion promotion, PromotionMenuBean menuBean )
    {
      this.promotionId = promotion.getId();
      this.promotionName = promotion.getName();
      this.promotionType = promotion.getPromotionType().getCode();
      this.canReceive = menuBean.isCanReceive();

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

    public String getPromotionType()
    {
      return promotionType;
    }

    public void setPromotionType( String promotionType )
    {
      this.promotionType = promotionType;
    }

    public boolean isCanReceive()
    {
      return canReceive;
    }

    public void setCanReceive( boolean canReceive )
    {
      this.canReceive = canReceive;
    }

    public PromotionAttributes getAttributes()
    {
      return attributes;
    }

    public void setAttributes( PromotionAttributes attributes )
    {
      this.attributes = attributes;
    }

  }

  public static class PurlData
  {
    @JsonProperty( "anniversary" )
    private String anniversary;
    @JsonProperty( "contributeUrl" )
    private String contributeUrl;
    @JsonProperty( "id" )
    private Long id;
    @JsonProperty( "viewUrl" )
    private String viewUrl;
    @JsonProperty( "promotion" )
    private String promotion;
    @JsonProperty( "expirationDate" )
    private String expirationDate;
    @JsonProperty( "isToday" )
    private boolean isToday = false;
    @JsonProperty( "timeLeft" )
    private String timeLeft;

    public PurlData()
    {
    }

    public PurlData( String anniversary, String contributeUrl )
    {
      this.anniversary = anniversary;
      this.contributeUrl = contributeUrl;
    }

    public String getAnniversary()
    {
      return anniversary;
    }

    public void setAnniversary( String anniversary )
    {
      this.anniversary = anniversary;
    }

    public String getContributeUrl()
    {
      return contributeUrl;
    }

    public void setContributeUrl( String contributeUrl )
    {
      this.contributeUrl = contributeUrl;
    }

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getViewUrl()
    {
      return viewUrl;
    }

    public void setViewUrl( String viewUrl )
    {
      this.viewUrl = viewUrl;
    }

    public String getPromotion()
    {
      return promotion;
    }

    public void setPromotion( String promotion )
    {
      this.promotion = promotion;
    }

    public String getExpirationDate()
    {
      return expirationDate;
    }

    public void setExpirationDate( String expirationDate )
    {
      this.expirationDate = expirationDate;
    }

    public boolean isToday()
    {
      return isToday;
    }

    public void setToday( boolean isToday )
    {
      this.isToday = isToday;
    }

    public String getTimeLeft()
    {
      return timeLeft;
    }

    public void setTimeLeft( String timeLeft )
    {
      this.timeLeft = timeLeft;
    }

  }

  public String getOrganization()
  {
    return organization;
  }

  public void setOrganization( String organization )
  {
    this.organization = organization;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

  public String getParticipantUrl()
  {
    return participantUrl;
  }

  public void setParticipantUrl( String participantUrl )
  {
    this.participantUrl = participantUrl;
  }

  public boolean isFollow()
  {
    return follow;
  }

  public void setFollow( boolean follow )
  {
    this.follow = follow;
  }

  public static class PromotionAttributes
  {

    private boolean easy;
    private boolean commentsActive;
    private boolean ecardsActive;
    private boolean cheers; //Client customizations for WIP #62128

    public PromotionAttributes( boolean easy, boolean commentsActive, boolean ecardsActive )
    {
      this.easy = easy;
      this.commentsActive = commentsActive;
      this.ecardsActive = ecardsActive;
    }

    public boolean isCommentsActive()
    {
      return commentsActive;
    }

    @JsonProperty( value = "isEasy" )
    public boolean isEasy()
    {
      return easy;
    }

    public boolean isEcardsActive()
    {
      return ecardsActive;
    }
    
    @JsonProperty( value = "isCheers" )
    public boolean isCheers()
    {
      return cheers;
    }

    public void setCheers( boolean cheers )
    {
      this.cheers = cheers;
    }
    
  }

  public Long getPrimaryNodeId()
  {
    return primaryNodeId;
  }

  public void setPrimaryNodeId( Long primaryNodeId )
  {
    this.primaryNodeId = primaryNodeId;
  }

  public List<Long> getAllNodeIds()
  {
    return allNodeIds;
  }

  public void setAllNodeIds( List<Long> allNodeIds )
  {
    this.allNodeIds = allNodeIds;
  }

  public List<PurlData> getPurlData()
  {
    if ( purlData == null )
    {
      purlData = new ArrayList<PurlData>();
    }
    return purlData;
  }

  public void setPurlData( List<PurlData> purlData )
  {
    this.purlData = purlData;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public List<NameableBean> getNodes()
  {
    return nodes;
  }

  public void setNodes( List<NameableBean> nodes )
  {
    this.nodes = nodes;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  //Client customization start
  public String getCurrency()
  {
    return currency;
  }

  public void setCurrency( String currency )
  {
    this.currency = currency;
  }

  public Long getAwardMax()
  {
    return awardMax;
  }

  public void setAwardMax( Long awardMax )
  {
    this.awardMax = awardMax;
  }

  public Long getAwardMin()
  {
    return awardMin;
  }

  public void setAwardMin( Long awardMin )
  {
    this.awardMin = awardMin;
  }
  //Client customization end

}
