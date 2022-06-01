/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.approvals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.biperf.core.value.client.ApproverLevelTypeBean;
import com.biperf.core.value.client.LevelPayoutDataBean;


/**
 * 
 * @author poddutur
 * @since Apr 19, 2016
 */
// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class NominationsApprovalPageTableDataView
{
  NominationsApprovalPageTableData nominationsApprovalPageTableData = new NominationsApprovalPageTableData();

  @JsonProperty( "tabularData" )
  public NominationsApprovalPageTableData getNominationsApprovalPageTableData()
  {
    return nominationsApprovalPageTableData;
  }

  public void setNominationsApprovalPageTableData( NominationsApprovalPageTableData nominationsApprovalPageTableData )
  {
    this.nominationsApprovalPageTableData = nominationsApprovalPageTableData;
  }

  public static class NominationsApprovalPageTableData
  {
    @JsonProperty( "sortedOn" )
    private String sortedOn;
    @JsonProperty( "sortedBy" )
    private String sortedBy;
    @JsonProperty( "totalRows" )
    private int totalRows;
    @JsonProperty( "rowsPerPage" )
    private int rowsPerPage;
    @JsonProperty( "currentPage" )
    private int currentPage;
    @JsonProperty( "totalPages" )
    private int totalPages;
    @JsonProperty( "isMore" )
    private boolean isMore;
 // Client customization for WIP #56492 starts
    @JsonProperty( "isCustomApprovalType" )
    private boolean isCustomApprovalType;
 // Client customization for WIP #56492 ends
    @JsonProperty( "isCumulativeNomination" )
    private boolean isCumulativeNomination;
    @JsonProperty( "awardType" )
    private String awardType;
    @JsonProperty( "awardMin" )
    private BigDecimal awardMin;
    @JsonProperty( "awardMax" )
    private BigDecimal awardMax;
    @JsonProperty( "previousLevelName" )
    private String previousLevelName;
    @JsonProperty( "nextLevelName" )
    private String nextLevelName;
    @JsonProperty( "meta" )
    private MetaView metaView = new MetaView();
    private List<Result> results = new ArrayList<Result>();
    @JsonProperty( "budgetBalance" )
    private String budgetBalance;
    @JsonProperty( "previousApproverCount" )
    private int previousApproverCount;
    @JsonProperty( "nextApproverCount" )
    private int nextApproverCount;
    @JsonProperty( "pendingNominations" )
    private int pendingNominations;
    @JsonProperty( "budgetPeriod" )
    private String budgetPeriodName;
    @JsonProperty( "lastBudgetRequestDate" )
    private String lastBudgetRequestDate;
    @JsonProperty( "potentialAwardExceeded" )
    private boolean potentialAwardExceeded;
    @JsonProperty( "payoutDescription" )
    private String payoutDescription;
    @JsonProperty( "excelUrl" )
    private String excelUrl;
    @JsonProperty( "pdfUrl" )
    private String pdfUrl;
    private List<PreviousApprover> previousApprovers = new ArrayList<PreviousApprover>();
    private List<NextApprover> nextApprovers = new ArrayList<NextApprover>();
    @JsonProperty( "isCustomPayoutType" )
    private boolean isCustomPayoutType;

    public String getSortedOn()
    {
      return sortedOn;
    }

    public void setSortedOn( String sortedOn )
    {
      this.sortedOn = sortedOn;
    }

    public String getSortedBy()
    {
      return sortedBy;
    }

    public void setSortedBy( String sortedBy )
    {
      this.sortedBy = sortedBy;
    }

    public int getTotalRows()
    {
      return totalRows;
    }

    public void setTotalRows( int totalRows )
    {
      this.totalRows = totalRows;
    }

    public int getRowsPerPage()
    {
      return rowsPerPage;
    }

    public void setRowsPerPage( int rowsPerPage )
    {
      this.rowsPerPage = rowsPerPage;
    }

    public int getCurrentPage()
    {
      return currentPage;
    }

    public void setCurrentPage( int currentPage )
    {
      this.currentPage = currentPage;
    }

    public int getTotalPages()
    {
      return totalPages;
    }

    public void setTotalPages( int totalPages )
    {
      this.totalPages = totalPages;
    }

    public boolean isMore()
    {
      return isMore;
    }

    public void setMore( boolean isMore )
    {
      this.isMore = isMore;
    }
    
 // Client customization for WIP #56492 starts
    public boolean isCustomApprovalType()
    {
      return isCustomApprovalType;
    }

    public void setCustomApprovalType( boolean isCustomApprovalType )
    {
      this.isCustomApprovalType = isCustomApprovalType;
    }
 // Client customization for WIP #56492 ends

    public boolean isCumulativeNomination()
    {
      return isCumulativeNomination;
    }

    public void setCumulativeNomination( boolean isCumulativeNomination )
    {
      this.isCumulativeNomination = isCumulativeNomination;
    }

    public String getAwardType()
    {
      return awardType;
    }

    public void setAwardType( String awardType )
    {
      this.awardType = awardType;
    }

    public BigDecimal getAwardMin()
    {
      return awardMin;
    }

    public void setAwardMin( BigDecimal awardMin )
    {
      this.awardMin = awardMin;
    }

    public BigDecimal getAwardMax()
    {
      return awardMax;
    }

    public void setAwardMax( BigDecimal awardMax )
    {
      this.awardMax = awardMax;
    }

    public String getBudgetBalance()
    {
      return budgetBalance;
    }

    public void setBudgetBalance( String budgetBalance )
    {
      this.budgetBalance = budgetBalance;
    }

    public String getPayoutDescription()
    {
      return payoutDescription;
    }

    public void setPayoutDescription( String payoutDescription )
    {
      this.payoutDescription = payoutDescription;
    }

    public String getPreviousLevelName()
    {
      return previousLevelName;
    }

    public void setPreviousLevelName( String previousLevelName )
    {
      this.previousLevelName = previousLevelName;
    }

    public String getNextLevelName()
    {
      return nextLevelName;
    }

    public void setNextLevelName( String nextLevelName )
    {
      this.nextLevelName = nextLevelName;
    }

    public String getBudgetPeriodName()
    {
      return budgetPeriodName;
    }

    public void setBudgetPeriodName( String budgetPeriodName )
    {
      this.budgetPeriodName = budgetPeriodName;
    }

    public String getLastBudgetRequestDate()
    {
      return lastBudgetRequestDate;
    }

    public void setLastBudgetRequestDate( String lastBudgetRequestDate )
    {
      this.lastBudgetRequestDate = lastBudgetRequestDate;
    }

    public boolean isPotentialAwardExceeded()
    {
      return potentialAwardExceeded;
    }

    public void setPotentialAwardExceeded( boolean potentialAwardExceeded )
    {
      this.potentialAwardExceeded = potentialAwardExceeded;
    }

    public MetaView getMetaView()
    {
      return metaView;
    }

    public void setMetaView( MetaView metaView )
    {
      this.metaView = metaView;
    }

    public String getExcelUrl()
    {
      return excelUrl;
    }

    public void setExcelUrl( String excelUrl )
    {
      this.excelUrl = excelUrl;
    }

    public String getPdfUrl()
    {
      return pdfUrl;
    }

    public void setPdfUrl( String pdfUrl )
    {
      this.pdfUrl = pdfUrl;
    }

    @JsonProperty( "results" )
    public List<Result> getResults()
    {
      return results;
    }

    public void setResults( List<Result> results )
    {
      this.results = results;
    }

    public int getPreviousApproverCount()
    {
      return previousApproverCount;
    }

    public void setPreviousApproverCount( int previousApproverCount )
    {
      this.previousApproverCount = previousApproverCount;
    }

    public int getNextApproverCount()
    {
      return nextApproverCount;
    }

    public void setNextApproverCount( int nextApproverCount )
    {
      this.nextApproverCount = nextApproverCount;
    }

    public int getPendingNominations()
    {
      return pendingNominations;
    }

    public void setPendingNominations( int pendingNominations )
    {
      this.pendingNominations = pendingNominations;
    }

    @JsonProperty( "previousApprovers" )
    public List<PreviousApprover> getPreviousApprovers()
    {
      return previousApprovers;
    }

    public void setPreviousApprovers( List<PreviousApprover> previousApprovers )
    {
      this.previousApprovers = previousApprovers;
    }

    @JsonProperty( "nextApprovers" )
    public List<NextApprover> getNextApprovers()
    {
      return nextApprovers;
    }

    public void setNextApprovers( List<NextApprover> nextApprovers )
    {
      this.nextApprovers = nextApprovers;
    }

    /**
	 * @return the isCustomPayoutType
	 */
	public boolean isCustomPayoutType() {
		return isCustomPayoutType;
	}

	/**
	 * @param isCustomPayoutType the isCustomPayoutType to set
	 */
	public void setCustomPayoutType(boolean isCustomPayoutType) {
		this.isCustomPayoutType = isCustomPayoutType;
	}

	public static class MetaView
    {
      private List<Column> columns = new ArrayList<Column>();

      @JsonProperty( "columns" )
      public List<Column> getColumns()
      {
        return columns;
      }

      public void setColumns( List<Column> columns )
      {
        this.columns = columns;
      }

      public static class Column
      {
        @JsonProperty( "id" )
        private Long id;
        @JsonProperty( "name" )
        private String name;
        @JsonProperty( "displayName" )
        private String displayName;
        @JsonProperty( "sortable" )
        private boolean sortable;

        public Long getId()
        {
          return id;
        }

        public void setId( Long id )
        {
          this.id = id;
        }

        public String getName()
        {
          return name;
        }

        public void setName( String name )
        {
          this.name = name;
        }

        public String getDisplayName()
        {
          return displayName;
        }

        public void setDisplayName( String displayName )
        {
          this.displayName = displayName;
        }

        public boolean isSortable()
        {
          return sortable;
        }

        public void setSortable( boolean sortable )
        {
          this.sortable = sortable;
        }
      }
    }

    public static class PreviousApprover
    {
      @JsonProperty( "id" )
      private Long id;
      @JsonProperty( "name" )
      private String name;

      public Long getId()
      {
        return id;
      }

      public void setId( Long id )
      {
        this.id = id;
      }

      public String getName()
      {
        return name;
      }

      public void setName( String name )
      {
        this.name = name;
      }
    }

    public static class NextApprover
    {
      @JsonProperty( "id" )
      private Long id;
      @JsonProperty( "name" )
      private String name;

      public Long getId()
      {
        return id;
      }

      public void setId( Long id )
      {
        this.id = id;
      }

      public String getName()
      {
        return name;
      }

      public void setName( String name )
      {
        this.name = name;
      }
    }

    public static class Result
    {
      @JsonProperty( "claimId" )
      private String claimId;
      @JsonProperty( "claimGroupId" )
      private Long claimGroupId;
      @JsonProperty( "index" )
      private int index;
      @JsonProperty( "paxId" )
      private Long paxId;
      @JsonProperty( "teamId" )
      private Long teamId;
      @JsonProperty( "nominee" )
      private String nominee;
      @JsonProperty( "avatarUrl" )
      private String avatarUrl;
      @JsonProperty( "countryCode" )
      private String countryCode;
      @JsonProperty( "orgName" )
      private String orgName;
      @JsonProperty( "jobName" )
      private String jobName;
      @JsonProperty( "departmentName" )
      private String derpartmentName;
      @JsonProperty( "isPastWinner" )
      private boolean isPastWinner;
      @JsonProperty( "pastWinnerMaxLimit" )
      private boolean pastWinnerMaxLimit;
      @JsonProperty( "recentTimePeriodWon" )
      private String recentTimePeriodWon;
      @JsonProperty( "mostRecentTimeDate" )
      private String mostRecentTimeDate;
      @JsonProperty( "isTeam" )
      private boolean isTeam;
      @JsonProperty( "teamMemberCount" )
      private int teamMemberCount;
      @JsonProperty( "dateSubmitted" )
      private String dateSubmitted;
      @JsonProperty( "nominator" )
      private String nominator;
      @JsonProperty( "nominatorId" )
      private Long nominatorId;
      @JsonProperty( "award" )
      private String award;
      @JsonProperty( "status" )
      private String status;
      @JsonProperty( "eCardImg" )
      private String eCardImg;
      @JsonProperty( "videoUrl" )
      private String videoUrl;
      @JsonProperty( "videoImg" )
      private String videoImg;
      @JsonProperty( "teamAward" )
      private BigDecimal teamAward;
      @JsonProperty( "reason" )
      private String reason;
      @JsonProperty( "submitterLangId" )
      private Long submitterLangId;
      @JsonProperty( "moreInfo" )
      private String moreInfo;
      @JsonProperty( "attachmentUrl" )
      private String attachmentUrl;
      @JsonProperty( "attachmentName" )
      private String attachmentName;
      @JsonProperty( "eCertUrl" )
      private String eCertUrl;
      @JsonProperty( "contextPath" )
      private String contextPath;
      @JsonProperty( "deinalReason" )
      private String deinalReason;
      @JsonProperty( "winnerMessage" )
      private String winnerMessage;
      @JsonProperty( "moreinfoMessage" )
      private String moreinfoMessage;
      @JsonProperty( "nominatorCount" )
      private int nominatorCount;
      @JsonProperty( "notificationDate" )
      private String notificationDate;
      @JsonProperty( "levelName" )
      private String levelName;
      @JsonProperty( "optOutAwards" )
      private boolean optOutAwards;
      private List<TeamMemeber> teamMembers = new ArrayList<TeamMemeber>();
      private List<Behavior> behaviors = new ArrayList<Behavior>();
      private List<CustomField> customFields = new ArrayList<CustomField>();
      private List<TimePeriodViewBean> timePeriods = new ArrayList<TimePeriodViewBean>();
      
      // Client customization for WIP #39189 starts
      private List<Attachment> attachments = new ArrayList<Attachment>();
      public List<Attachment> getAttachments()
      {
        return attachments;
      }

      public void setAttachments( List<Attachment> attachments )
      {
        this.attachments = attachments;
      }
      //END
      
  
   // Client customization for WIP #56492 starts
      private List<ApproverLevelTypeBean> levels = new ArrayList<ApproverLevelTypeBean>();
      
      @JsonProperty( "levels" )
      public List<ApproverLevelTypeBean> getLevels()
      {
        return levels;
      }

      public void setLevels( List<ApproverLevelTypeBean> levels )
      {
        this.levels = levels;
      }
   // Client customization for WIP #56492 ends

      public String getClaimId()
      {
        return claimId;
      }

      public void setClaimId( String claimId )
      {
        this.claimId = claimId;
      }

      public Long getClaimGroupId()
      {
        return claimGroupId;
      }

      public void setClaimGroupId( Long claimGroupId )
      {
        this.claimGroupId = claimGroupId;
      }

      public int getIndex()
      {
        return index;
      }

      public void setIndex( int index )
      {
        this.index = index;
      }

      public String getNominee()
      {
        return nominee;
      }

      public void setNominee( String nominee )
      {
        this.nominee = nominee;
      }

      public String getAvatarUrl()
      {
        return avatarUrl;
      }

      public void setAvatarUrl( String avatarUrl )
      {
        this.avatarUrl = avatarUrl;
      }

      public String getCountryCode()
      {
        return countryCode;
      }

      public void setCountryCode( String countryCode )
      {
        this.countryCode = countryCode;
      }

      public String getOrgName()
      {
        return orgName;
      }

      public void setOrgName( String orgName )
      {
        this.orgName = orgName;
      }

      public String getJobName()
      {
        return jobName;
      }

      public void setJobName( String jobName )
      {
        this.jobName = jobName;
      }

      public String getDerpartmentName()
      {
        return derpartmentName;
      }

      public void setDerpartmentName( String derpartmentName )
      {
        this.derpartmentName = derpartmentName;
      }

      public boolean isPastWinner()
      {
        return isPastWinner;
      }

      public void setPastWinner( boolean isPastWinner )
      {
        this.isPastWinner = isPastWinner;
      }

      public boolean isPastWinnerMaxLimit()
      {
        return pastWinnerMaxLimit;
      }

      public void setPastWinnerMaxLimit( boolean pastWinnerMaxLimit )
      {
        this.pastWinnerMaxLimit = pastWinnerMaxLimit;
      }

      public String getRecentTimePeriodWon()
      {
        return recentTimePeriodWon;
      }

      public void setRecentTimePeriodWon( String recentTimePeriodWon )
      {
        this.recentTimePeriodWon = recentTimePeriodWon;
      }

      public String getMostRecentTimeDate()
      {
        return mostRecentTimeDate;
      }

      public void setMostRecentTimeDate( String mostRecentTimeDate )
      {
        this.mostRecentTimeDate = mostRecentTimeDate;
      }

      public boolean isTeam()
      {
        return isTeam;
      }

      public void setTeam( boolean isTeam )
      {
        this.isTeam = isTeam;
      }

      public int getTeamMemberCount()
      {
        return teamMemberCount;
      }

      public void setTeamMemberCount( int teamMemberCount )
      {
        this.teamMemberCount = teamMemberCount;
      }

      public String getDateSubmitted()
      {
        return dateSubmitted;
      }

      public void setDateSubmitted( String dateSubmitted )
      {
        this.dateSubmitted = dateSubmitted;
      }

      public String getNominator()
      {
        return nominator;
      }

      public void setNominator( String nominator )
      {
        this.nominator = nominator;
      }

      public Long getNominatorId()
      {
        return nominatorId;
      }

      public void setNominatorId( Long nominatorId )
      {
        this.nominatorId = nominatorId;
      }

      public String getAward()
      {
        return award;
      }

      public void setAward( String award )
      {
        this.award = award;
      }

      public String getStatus()
      {
        return status;
      }

      public void setStatus( String status )
      {
        this.status = status;
      }

      public String geteCardImg()
      {
        return eCardImg;
      }

      public void seteCardImg( String eCardImg )
      {
        this.eCardImg = eCardImg;
      }

      public String getVideoUrl()
      {
        return videoUrl;
      }

      public void setVideoUrl( String videoUrl )
      {
        this.videoUrl = videoUrl;
      }

      public String getVideoImg()
      {
        return videoImg;
      }

      public void setVideoImg( String videoImg )
      {
        this.videoImg = videoImg;
      }

      public BigDecimal getTeamAward()
      {
        return teamAward;
      }

      public void setTeamAward( BigDecimal teamAward )
      {
        this.teamAward = teamAward;
      }

      public Long getPaxId()
      {
        return paxId;
      }

      public void setPaxId( Long paxId )
      {
        this.paxId = paxId;
      }

      public Long getTeamId()
      {
        return teamId;
      }

      public void setTeamId( Long teamId )
      {
        this.teamId = teamId;
      }

      public String getReason()
      {
        return reason;
      }

      public void setReason( String reason )
      {
        this.reason = reason;
      }

      public Long getSubmitterLangId()
      {
        return submitterLangId;
      }

      public void setSubmitterLangId( Long submitterLangId )
      {
        this.submitterLangId = submitterLangId;
      }

      public String getMoreInfo()
      {
        return moreInfo;
      }

      public void setMoreInfo( String moreInfo )
      {
        this.moreInfo = moreInfo;
      }

      public String getAttachmentUrl()
      {
        return attachmentUrl;
      }

      public void setAttachmentUrl( String attachmentUrl )
      {
        this.attachmentUrl = attachmentUrl;
      }

      public String getAttachmentName()
      {
        return attachmentName;
      }

      public void setAttachmentName( String attachmentName )
      {
        this.attachmentName = attachmentName;
      }

      public String geteCertUrl()
      {
        return eCertUrl;
      }

      public void seteCertUrl( String eCertUrl )
      {
        this.eCertUrl = eCertUrl;
      }

      public String getDeinalReason()
      {
        return deinalReason;
      }

      public void setDeinalReason( String deinalReason )
      {
        this.deinalReason = deinalReason;
      }

      public String getWinnerMessage()
      {
        return winnerMessage;
      }

      public void setWinnerMessage( String winnerMessage )
      {
        this.winnerMessage = winnerMessage;
      }

      public String getMoreinfoMessage()
      {
        return moreinfoMessage;
      }

      public void setMoreinfoMessage( String moreinfoMessage )
      {
        this.moreinfoMessage = moreinfoMessage;
      }

      public int getNominatorCount()
      {
        return nominatorCount;
      }

      public void setNominatorCount( int nominatorCount )
      {
        this.nominatorCount = nominatorCount;
      }

      public String getNotificationDate()
      {
        return notificationDate;
      }

      public void setNotificationDate( String notificationDate )
      {
        this.notificationDate = notificationDate;
      }

      public String getLevelName()
      {
        return levelName;
      }

      public void setLevelName( String levelName )
      {
        this.levelName = levelName;
      }
      
      public boolean isOptOutAwards()
      {
        return optOutAwards;
      }
      
      public void setOptOutAwards( boolean optOutAwards )
      {
        this.optOutAwards = optOutAwards;
      }

      @JsonProperty( "teamMembers" )
      public List<TeamMemeber> getTeamMembers()
      {
        return teamMembers;
      }

      public void setTeamMembers( List<TeamMemeber> teamMembers )
      {
        this.teamMembers = teamMembers;
      }

      @JsonProperty( "behaviors" )
      public List<Behavior> getBehaviors()
      {
        return behaviors;
      }

      public void setBehaviors( List<Behavior> behaviors )
      {
        this.behaviors = behaviors;
      }

      @JsonProperty( "customFields" )
      public List<CustomField> getCustomFields()
      {
        return customFields;
      }

      public void setCustomFields( List<CustomField> customFields )
      {
        this.customFields = customFields;
      }

      @JsonProperty( "timePeriods" )
      public List<TimePeriodViewBean> getTimePeriods()
      {
        return timePeriods;
      }

      public void setTimePeriods( List<TimePeriodViewBean> timePeriods )
      {
        this.timePeriods = timePeriods;
      }
      

      public static class TeamMemeber
      {
        @JsonProperty( "paxId" )
        private Long id;
        @JsonProperty( "name" )
        private String name;
        @JsonProperty( "countryCode" )
        private String countryCode;
        @JsonProperty( "orgName" )
        private String orgName;
        @JsonProperty( "jobName" )
        private String jobName;
        @JsonProperty( "award" )
        private BigDecimal award;
        @JsonProperty( "departmentName" )
        private String departmentName;
        @JsonProperty( "optOutAwards" )
        private boolean optOutAwards;

        public Long getId()
        {
          return id;
        }

        public void setId( Long id )
        {
          this.id = id;
        }

        public String getName()
        {
          return name;
        }

        public void setName( String name )
        {
          this.name = name;
        }

        public String getCountryCode()
        {
          return countryCode;
        }

        public void setCountryCode( String countryCode )
        {
          this.countryCode = countryCode;
        }

        public String getOrgName()
        {
          return orgName;
        }

        public void setOrgName( String orgName )
        {
          this.orgName = orgName;
        }

        public String getJobName()
        {
          return jobName;
        }

        public void setJobName( String jobName )
        {
          this.jobName = jobName;
        }

        public BigDecimal getAward()
        {
          return award;
        }

        public void setAward( BigDecimal award )
        {
          this.award = award;
        }

        public String getDepartmentName()
        {
          return departmentName;
        }

        public void setDepartmentName( String departmentName )
        {
          this.departmentName = departmentName;
        }
        
        public boolean isOptOutAwards()
        {
          return optOutAwards;
        }
        
        public void setOptOutAwards( boolean optOutAwards )
        {
          this.optOutAwards = optOutAwards;
        }

      }
      
      // Client customization for WIP #39189 starts
      public static class Attachment
      {
        @JsonProperty( "attachmentUrl" )
        private String attachmentUrl;
        @JsonProperty( "attachmentName" )
        private String attachmentName;

        public String getAttachmentUrl()
        {
          return attachmentUrl;
        }

        public void setAttachmentUrl( String attachmentUrl )
        {
          this.attachmentUrl = attachmentUrl;
        }

        public String getAttachmentName()
        {
          return attachmentName;
        }

        public void setAttachmentName( String attachmentName )
        {
          this.attachmentName = attachmentName;
        }

      }
      // Client customization for WIP #39189 ends

      public static class Behavior
      {
        @JsonProperty( "name" )
        private String name;
        @JsonProperty( "img" )
        private String img;

        public String getName()
        {
          return name;
        }

        public void setName( String name )
        {
          this.name = name;
        }

        public String getImg()
        {
          return img;
        }

        public void setImg( String img )
        {
          this.img = img;
        }
      }

      public static class CustomField
      {
        @JsonProperty( "id" )
        private Long id;
        @JsonProperty( "name" )
        private String name;
        @JsonProperty( "description" )
        private String description;

        public Long getId()
        {
          return id;
        }

        public void setId( Long id )
        {
          this.id = id;
        }

        public String getName()
        {
          return name;
        }

        public void setName( String name )
        {
          this.name = name;
        }

        public String getDescription()
        {
          return description;
        }

        public void setDescription( String description )
        {
          this.description = description;
        }

      }

      public static class TimePeriodViewBean
      {
        @JsonProperty( "id" )
        private Long id;
        @JsonProperty( "name" )
        private String name;
        @JsonProperty( "maxWinsllowed" )
        private Long maxWinsllowed;
        @JsonProperty( "noOfWinnners" )
        private Long noOfWinnners;

        public Long getId()
        {
          return id;
        }

        public void setId( Long id )
        {
          this.id = id;
        }

        public String getName()
        {
          return name;
        }

        public void setName( String name )
        {
          this.name = name;
        }

        public Long getMaxWinsllowed()
        {
          return maxWinsllowed;
        }

        public void setMaxWinsllowed( Long maxWinsllowed )
        {
          this.maxWinsllowed = maxWinsllowed;
        }

        public Long getNoOfWinnners()
        {
          return noOfWinnners;
        }

        public void setNoOfWinnners( Long noOfWinnners )
        {
          this.noOfWinnners = noOfWinnners;
        }
      }

      public String getContextPath()
      {
        return contextPath;
      }

      public void setContextPath( String contextPath )
      {
        this.contextPath = contextPath;
      }

    }

  }

}
