
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HtmlUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.ContentReaderManager;

@JsonInclude( value = Include.NON_NULL )
public class ThrowdownStackRanking extends BaseJsonView
{
  private static final long serialVersionUID = 1L;
  public static final int DETAIL_PAGE_SIZE = 100;
  public static final int TILE_PAGE_SIZE = 25;
  public static final int NUMBER_OF_TOPPERS = 3;

  private Node node;
  private ThrowdownPromotion promotion;
  private Date progressEndDate;
  private boolean payoutIssued;
  private boolean showLeaders;
  private String rules = null;
  private String activitySortOrder = "DSC";
  private String editableByUser = "false";
  private List<ThrowdownStackRankingParticipant> badgeHolders = new ArrayList<ThrowdownStackRankingParticipant>();
  private List<ThrowdownStackRankingParticipant> leaders = new ArrayList<ThrowdownStackRankingParticipant>();
  private ThrowdownStackRankingParticipant titleUser;
  private int totalNumberLeaders;
  private int currentPage;
  private boolean progressLoaded;
  private StackStandingNode standingNode;

  @JsonIgnore
  public boolean isDisplayProgress()
  {
    return promotion.isDisplayTeamProgress();
  }

  @JsonIgnore
  public Long getPromotionId()
  {
    return promotion.getId();
  }

  @JsonProperty( "name" )
  public String getName()
  {
    return promotion.getPromoNameFromCM();
  }

  @JsonProperty( "overview" )
  public String getPromotionOverviewFormatted()
  {
    return HtmlUtils.removeFormatting( promotion.getOverviewDetailsText() );
  }

  @JsonProperty( "promotionOverview" )
  public String getPromotionOverview()
  {
    return promotion.getOverviewDetailsText();
  }

  @JsonProperty( "startDate" )
  public String getPromotionStartDate()
  {
    return DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
  }

  @JsonProperty( "endDate" )
  public String getPromotionEndDate()
  {
    return DateUtils.toDisplayString( promotion.getSubmissionEndDate() );
  }

  @JsonProperty( "displayEndDate" )
  public String getPromotionDisplayEndDate()
  {
    return DateUtils.toDisplayString( promotion.getTileDisplayStartDate() );
  }

  @JsonIgnore
  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  @JsonIgnore
  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  public void addLeader( ThrowdownStackRankingParticipant leader )
  {
    this.leaders.add( leader );
  }

  @JsonProperty( "activityTitle" )
  public String getActivityTitle()
  {
    return promotion.getBaseUnitText();
  }

  @JsonProperty( "activityPosition" )
  public String getActivityPosition()
  {
    return promotion.getBaseUnitPosition().getCode();
  }

  @JsonProperty( "progressEndDate" )
  public String getProgressEndDate()
  {
    return progressEndDate != null ? DateUtils.toDisplayString( progressEndDate ) : null;
  }

  @JsonIgnore
  public void setProgressEndDate( Date progressEndDate )
  {
    this.progressEndDate = progressEndDate;
  }

  @JsonProperty( "isProgressLoaded" )
  public boolean getProgressLoaded()
  {
    return progressLoaded;
  }

  @JsonIgnore
  public void setProgressLoaded( boolean progressLoaded )
  {
    this.progressLoaded = progressLoaded;
  }

  @JsonProperty( "id" )
  public Long getNodeId()
  {
    return node != null ? node.getId() : -1;
  }

  public String getNodeTypeName()
  {
    return node != null ? node.getNodeType().getI18nName() : ContentReaderManager.getText( "system.general", "ALL" );
  }

  public Long getNodeTypeId()
  {
    return node != null ? node.getNodeType().getId() : -1;
  }

  @JsonProperty( "leadersPerPage" )
  public Integer getLeadersPerPage()
  {
    return DETAIL_PAGE_SIZE;
  }

  @JsonIgnore
  public Node getNode()
  {
    return node;
  }

  @JsonIgnore
  public void setNode( Node node )
  {
    this.node = node;
  }

  @JsonIgnore
  public boolean isPayoutIssued()
  {
    return payoutIssued;
  }

  @JsonIgnore
  public void setPayoutIssued( boolean payoutIssued )
  {
    this.payoutIssued = payoutIssued;
  }

  @JsonIgnore
  public boolean isShowLeaders()
  {
    return showLeaders;
  }

  @JsonIgnore
  public void setShowLeaders( boolean showLeaders )
  {
    this.showLeaders = showLeaders;
  }

  @JsonProperty( "rules" )
  public String getRules()
  {
    return rules;
  }

  @JsonIgnore
  public void setRules( String rules )
  {
    this.rules = rules;
  }

  @JsonProperty( "activitySortOrder" )
  public String getActivitySortOrder()
  {
    return activitySortOrder;
  }

  @JsonIgnore
  public void setActivitySortOrder( String activitySortOrder )
  {
    this.activitySortOrder = activitySortOrder;
  }

  @JsonProperty( "editableByUser" )
  public String getEditableByUser()
  {
    return editableByUser;
  }

  @JsonIgnore
  public void setEditableByUser( String editableByUser )
  {
    this.editableByUser = editableByUser;
  }

  @JsonProperty( "badgeHolders" )
  public List<ThrowdownStackRankingParticipant> getBadgeHolders()
  {
    return badgeHolders;
  }

  @JsonIgnore
  public void setBadgeHolders( List<ThrowdownStackRankingParticipant> badgeHolders )
  {
    this.badgeHolders = badgeHolders;
  }

  @JsonProperty( "leaders" )
  public List<ThrowdownStackRankingParticipant> getLeaders()
  {
    return leaders;
  }

  @JsonIgnore
  public void setLeaders( List<ThrowdownStackRankingParticipant> leaders )
  {
    this.leaders = leaders;
  }

  public void addBadgeHolder( ThrowdownStackRankingParticipant badgeHolder )
  {
    this.badgeHolders.add( badgeHolder );
  }

  @JsonProperty( "totalNumberLeaders" )
  public int getTotalNumberLeaders()
  {
    return totalNumberLeaders;
  }

  @JsonIgnore
  public void setTotalNumberLeaders( int totalNumberLeaders )
  {
    this.totalNumberLeaders = totalNumberLeaders;
  }

  @JsonProperty( "currentPage" )
  public int getCurrentPage()
  {
    return currentPage;
  }

  @JsonIgnore
  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  @JsonIgnore
  public boolean isHierarchyRanking()
  {
    return node == null;
  }

  @JsonProperty( "titleUser" )
  public ThrowdownStackRankingParticipant getTitleUser()
  {
    return titleUser;
  }

  @JsonIgnore
  public void setTitleUser( ThrowdownStackRankingParticipant titleUser )
  {
    this.titleUser = titleUser;
  }

  @JsonIgnore
  public StackStandingNode getStandingNode()
  {
    return standingNode;
  }

  @JsonIgnore
  public void setStandingNode( StackStandingNode standingNode )
  {
    this.standingNode = standingNode;
  }

}
