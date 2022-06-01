
package com.biperf.core.ui.ssi.view;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.ui.ssi.view.SSIContestApprovalStepItUpDetailView.StepItUpPaxView.StepItUpLevelAmountView;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestBaseLineTotalsValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author patelp
 * @since Feb 21, 2015
 * @version 1.0
 */

public class SSIContestApprovalStepItUpDetailView
{

  private static final String LEVEL = "Level";

  private String sortedOn;
  private String sortedBy;
  private long total;
  private int paxPerPage;
  private int currentPage;
  private Double baselineTotal;
  private List<StepItUpPaxView> results = new ArrayList<StepItUpPaxView>();
  private List<StepItUpPaxLevelView> levels = new ArrayList<StepItUpPaxLevelView>();

  public SSIContestApprovalStepItUpDetailView( List<SSIContestParticipant> contestParticipants,
                                               SSIContest contest,
                                               SSIContestBaseLineTotalsValueBean contestBaseLineTotalsValueBean,
                                               Integer participantsCount,
                                               Integer currentPage )
  {

    int precision = contest.getActivityMeasureType() != null ? SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() ) : 0;
    this.baselineTotal = contestBaseLineTotalsValueBean.getBaselineTotal();

    for ( SSIContestLevel contestLevel : contest.getContestLevels() )
    {
      StepItUpPaxLevelView paxLevel = new StepItUpPaxLevelView();
      paxLevel.setName( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), new Object[] { contestLevel.getSequenceNumber() } ) );
      paxLevel.setSortedOnName( LEVEL + contestLevel.getSequenceNumber() );
      if ( contest.getIndividualBaselineType().isPercentageOverBaseline() )
      {
        paxLevel.setBaseline( SSIContestUtil.getFormattedValue( contestLevel.getGoalAmount(), 0 ) + "%" );
      }
      else if ( contest.getActivityMeasureType().isCurrency() || contest.getActivityMeasureType().isUnit() )
      {
        paxLevel.setBaseline( SSIContestUtil.getFormattedValue( contestLevel.getGoalAmount(), precision ) );
      }
      if ( contest.getIndividualBaselineType().isCurrencyOverBaseline() )
      {
        paxLevel.setTotal( SSIContestUtil.getFormattedValue( contestBaseLineTotalsValueBean.getBaselineTotal() + participantsCount * contestLevel.getGoalAmount(), precision ) );
      }
      else if ( contest.getIndividualBaselineType().isPercentageOverBaseline() )
      {
        paxLevel.setTotal( SSIContestUtil.getFormattedValue( contestBaseLineTotalsValueBean.getBaselineTotal() + contestBaseLineTotalsValueBean.getBaselineTotal() / 100 * contestLevel.getGoalAmount(),
                                                             precision ) );
      }
      this.levels.add( paxLevel );
    }

    for ( SSIContestParticipant contestParticipant : contestParticipants )
    {
      StepItUpPaxView stepItUpPaxView = new StepItUpPaxView();
      stepItUpPaxView.setId( contestParticipant.getParticipant().getId() );
      stepItUpPaxView.setFirstName( contestParticipant.getParticipant().getFirstName() );
      stepItUpPaxView.setLastName( contestParticipant.getParticipant().getLastName() );
      stepItUpPaxView.setBaseline( contestParticipant.getStepItUpBaselineAmount() );
      for ( SSIContestLevel contestLevel : contest.getContestLevels() )
      {
        StepItUpLevelAmountView stepItUpLevelAmountView = stepItUpPaxView.getStepItUpLevelAmountViewInstance();
        stepItUpLevelAmountView.setName( LEVEL + " " + contestLevel.getSequenceNumber() );
        stepItUpLevelAmountView.setAmount( SSIContestUtil.getFormattedValue( getLevelAmount( contestParticipant.getStepItUpBaselineAmount(), contestLevel.getGoalAmount(), contest ), precision ) );
        stepItUpPaxView.getLevelAmounts().add( stepItUpLevelAmountView );
      }
      this.results.add( stepItUpPaxView );
      Collections.sort( this.getResults(), new SSIContestParticipantComparator() );
    }
    this.setTotal( participantsCount );
    this.setCurrentPage( currentPage );
    this.setPaxPerPage( SSIContestUtil.PAX_RECORDS_PER_PAGE );
  }

  class SSIContestParticipantComparator implements Comparator<StepItUpPaxView>
  {

    public int compare( StepItUpPaxView stepItUpPaxOne, StepItUpPaxView stepItUpPaxTwo )
    {
      int nameComp = stepItUpPaxOne.getLastName().compareToIgnoreCase( stepItUpPaxTwo.getLastName() );
      return nameComp == 0 ? stepItUpPaxOne.getFirstName().compareToIgnoreCase( stepItUpPaxTwo.getFirstName() ) : nameComp;

    }
  }

  private double getLevelAmount( Double baseLineAmount, Double goal, SSIContest contest )
  {
    if ( contest.getIndividualBaselineType().isCurrencyOverBaseline() )
    {
      return baseLineAmount + goal;
    }
    else if ( contest.getIndividualBaselineType().isPercentageOverBaseline() )
    {
      return baseLineAmount + baseLineAmount * goal / 100;
    }
    else
    {
      return 0;
    }
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

  public long getTotal()
  {
    return total;
  }

  public void setTotal( long total )
  {
    this.total = total;
  }

  public int getPaxPerPage()
  {
    return paxPerPage;
  }

  public void setPaxPerPage( int paxPerPage )
  {
    this.paxPerPage = paxPerPage;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public List<StepItUpPaxView> getResults()
  {
    return results;
  }

  public void setResults( List<StepItUpPaxView> results )
  {
    this.results = results;
  }

  public List<StepItUpPaxLevelView> getLevels()
  {
    return levels;
  }

  public void setLevels( List<StepItUpPaxLevelView> levels )
  {
    this.levels = levels;
  }

  public Double getBaselineTotal()
  {
    return baselineTotal;
  }

  public void setBaselineTotal( Double baselineTotal )
  {
    this.baselineTotal = baselineTotal;
  }

  class StepItUpPaxLevelView
  {
    private String name;
    private String sortedOnName;
    private String baseline;
    private String total;

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

    public String getSortedOnName()
    {
      return sortedOnName;
    }

    public void setSortedOnName( String sortedOnName )
    {
      this.sortedOnName = sortedOnName;
    }

    public String getBaseline()
    {
      return baseline;
    }

    public void setBaseline( String baseline )
    {
      this.baseline = baseline;
    }

    public String getTotal()
    {
      return total;
    }

    public void setTotal( String total )
    {
      this.total = total;
    }
  }

  class StepItUpPaxView
  {
    private Long id;
    private String firstName;
    private String lastName;
    private Double baseline;
    List<StepItUpLevelAmountView> levelAmounts = new ArrayList<StepItUpLevelAmountView>();

    public Long getId()
    {
      return id;
    }

    public StepItUpLevelAmountView getStepItUpLevelAmountViewInstance()
    {

      return new StepItUpLevelAmountView();
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getFirstName()
    {
      return firstName;
    }

    public void setFirstName( String firstName )
    {
      this.firstName = firstName;
    }

    public String getLastName()
    {
      return lastName;
    }

    public void setLastName( String lastName )
    {
      this.lastName = lastName;
    }

    public Double getBaseline()
    {
      return baseline;
    }

    public void setBaseline( Double baseline )
    {
      this.baseline = baseline;
    }

    public List<StepItUpLevelAmountView> getLevelAmounts()
    {
      return levelAmounts;
    }

    public void setLevelAmounts( List<StepItUpLevelAmountView> levelAmounts )
    {
      this.levelAmounts = levelAmounts;
    }

    class StepItUpLevelAmountView
    {
      private String name;
      private String amount;

      public String getName()
      {
        return name;
      }

      public void setName( String name )
      {
        this.name = name;
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

  }
}
