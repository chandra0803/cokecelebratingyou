
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestPaginationValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIPaxContestActivityHistoryView
{
  private TabularData tabularData;
  private String sortedOn;
  private String sortedBy;
  private int total;
  private int perPage;
  private int current;

  public SSIPaxContestActivityHistoryView()
  {

  }

  public SSIPaxContestActivityHistoryView( List<SSIContestPaxClaim> paxClaims,
                                           String totalAmt,
                                           SSIContestParticipant contestParticipant,
                                           String siteUrl,
                                           SSIContest contest,
                                           Long particpantId,
                                           boolean fromDrilldown )
  {
    if ( paxClaims != null )
    {
      tabularData = new TabularData( paxClaims, totalAmt, contestParticipant, siteUrl, contest, particpantId, fromDrilldown );
    }
  }

  public void addPaginationParams( SSIContestPaginationValueBean paginationParams, int total )
  {
    this.current = paginationParams.getCurrentPage();
    this.perPage = paginationParams.getPageSize();
    this.sortedBy = paginationParams.getSortedBy();
    this.sortedOn = paginationParams.getSortedOn();
    this.total = total;
  }

  public TabularData getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( TabularData tabularData )
  {
    this.tabularData = tabularData;
  }

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

  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  public int getPerPage()
  {
    return perPage;
  }

  public void setPerPage( int perPage )
  {
    this.perPage = perPage;
  }

  public int getCurrent()
  {
    return current;
  }

  public void setCurrent( int current )
  {
    this.current = current;
  }

  class TabularData
  {
    private Meta meta;
    private List<Result> results;

    public TabularData()
    {

    }

    public TabularData( List<SSIContestPaxClaim> paxClaims, String totalAmt, SSIContestParticipant contestParticipant, String siteUrl, SSIContest contest, Long particpantId, boolean fromDrilldown )
    {
      meta = new Meta( totalAmt );
      results = new ArrayList<Result>();
      for ( SSIContestPaxClaim paxClaim : paxClaims )
      {
        results.add( new Result( paxClaim, contestParticipant, siteUrl, contest, particpantId, fromDrilldown ) );
      }
    }

    public Meta getMeta()
    {
      return meta;
    }

    public void setMeta( Meta meta )
    {
      this.meta = meta;
    }

    public List<Result> getResults()
    {
      return results;
    }

    public void setResults( List<Result> results )
    {
      this.results = results;
    }
  }

  class Meta
  {
    private List<Column> columns;
    private boolean footerActive;

    public Meta()
    {

    }

    public Meta( String totalAmt )
    {
      columns = new ArrayList<Column>();
      columns.add( new Column( 1,
                               "claimNumber",
                               CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.CLAIM_NUMBER" ),
                               true,
                               CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.TOTAL" ) ) );
      columns.add( new Column( 2, "submissionDate", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.DATE_SUBMITTED" ), true, "" ) );
      columns.add( new Column( 3, "activity", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.ACTIVITY" ), false, "" ) );
      columns.add( new Column( 4, "status", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.CLAIM_STATUS" ), true, "" ) );
      columns.add( new Column( 5, "claimAmountQuantity", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.AMOUNT" ), true, totalAmt ) );
      this.footerActive = true;
    }

    public List<Column> getColumns()
    {
      return columns;
    }

    public void setColumns( List<Column> columns )
    {
      this.columns = columns;
    }

    public boolean isFooterActive()
    {
      return footerActive;
    }

    public void setFooterActive( boolean footerActive )
    {
      this.footerActive = footerActive;
    }

  }

  class Result
  {
    private String claimNumber;
    private String claimDetailUrl;
    private String date;
    private String status;
    private String activity;
    private String amount;

    public Result()
    {

    }

    public Result( SSIContestPaxClaim paxClaim, SSIContestParticipant contestParticipant, String siteUrl, SSIContest contest, Long particpantId, boolean fromDrillDown )
    {
      int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
      this.claimNumber = paxClaim.getClaimNumber();
      this.claimDetailUrl = buildClaimDetailUrl( paxClaim, siteUrl, particpantId, fromDrillDown );
      this.date = DateUtils.toDisplayString( paxClaim.getSubmissionDate() );
      this.activity = SSIContestUtil.getActivityDescription( paxClaim, contestParticipant, contest );
      this.status = paxClaim.getStatus().getName();
      this.amount = SSIContestUtil.getFormattedValue( paxClaim.getClaimAmountQuantity(), precision );
    }

    private String buildClaimDetailUrl( SSIContestPaxClaim paxClaim, String siteUrl, Long particpantId, boolean fromDrillDown )
    {
      Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
      clientStateParameterMap.put( SSIContestUtil.CLAIM_ID, paxClaim.getId().toString() );
      if ( fromDrillDown )
      {
        clientStateParameterMap.put( SSIContestUtil.USER_ID, particpantId.toString() );
      }
      return ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_VIEW_CLAIM_DETAILS, clientStateParameterMap );
    }

    public String getClaimNumber()
    {
      return claimNumber;
    }

    public void setClaimNumber( String claimNumber )
    {
      this.claimNumber = claimNumber;
    }

    public String getClaimDetailUrl()
    {
      return claimDetailUrl;
    }

    public void setClaimDetailUrl( String claimDetailUrl )
    {
      this.claimDetailUrl = claimDetailUrl;
    }

    public String getDate()
    {
      return date;
    }

    public void setDate( String date )
    {
      this.date = date;
    }

    public String getStatus()
    {
      return status;
    }

    public void setStatus( String status )
    {
      this.status = status;
    }

    public String getActivity()
    {
      return activity;
    }

    public void setActivity( String activity )
    {
      this.activity = activity;
    }

    public String getAmount()
    {
      return amount;
    }

    public void setAmount( String amount )
    {
      this.amount = amount;
    }

  }

  class Column
  {
    private int id;
    private String name;
    private String displayName;
    private boolean sortable;
    private String footerDisplayText;

    public Column()
    {

    }

    public Column( int id, String name, String displayName, boolean sortable, String footerDisplayText )
    {
      this.id = id;
      this.name = name;
      this.displayName = displayName;
      this.sortable = sortable;
      this.footerDisplayText = footerDisplayText;
    }

    public int getId()
    {
      return id;
    }

    public void setId( int id )
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

    public String getFooterDisplayText()
    {
      return footerDisplayText;
    }

    public void setFooterDisplayText( String footerDisplayText )
    {
      this.footerDisplayText = footerDisplayText;
    }

  }

}
