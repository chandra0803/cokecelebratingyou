/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/budget/BudgetMaster.java,v $
 */

package com.biperf.core.domain.budget;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.cxf.common.util.StringUtils;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * BudgetMaster domain object which represents a budget master within the Beacon application.
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
 * <td>Jason</td>
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMaster extends BaseDomain
{

  private static final long serialVersionUID = 3256726164994536240L;

  private BudgetType budgetType;
  private BudgetOverrideableType overrideableType;
  private BudgetFinalPayoutRule finalPayoutRule;
  private BudgetMasterAwardType awardType;

  private String budgetName;
  private String cmAssetCode;
  private String nameCmKey;
  private boolean multiPromotion;
  private boolean active;

  private Set<BudgetSegment> budgetSegments = new LinkedHashSet<BudgetSegment>();
  private Set promotions = new LinkedHashSet(); // Two promotion sets,
  private Set<Promotion> cashPromotions = new LinkedHashSet<>(); // split by whether added as
                                                                 // award_budget_master or
                                                                 // cash_budget_master
  private Date startDate;
  private Date endDate;

  private boolean allowAdditionalTransferrees;

  public BudgetMaster()
  {
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "MasterBudget [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{budgetType=" ).append( null != this.getBudgetType() ? this.getBudgetType().getCode() : "" ).append( "}, " );
    buf.append( "{budgetName=" ).append( this.getBudgetName() ).append( "}, " );
    buf.append( "{cmAssetCode=" ).append( this.getCmAssetCode() ).append( "}, " );
    buf.append( "{nameCmKey=" ).append( this.getNameCmKey() ).append( "}, " );
    buf.append( "{overrideableType=" ).append( null != this.getOverrideableType() ? this.getOverrideableType().getCode() : "" ).append( "}, " );
    buf.append( "{multiPromotion=" ).append( this.isMultiPromotion() ).append( "}, " );
    buf.append( "{active=" ).append( this.isActive() ).append( "}, " );
    buf.append( "{finalPayoutRule=" ).append( null != this.getFinalPayoutRule() ? this.getFinalPayoutRule().getCode() : "" ).append( "}" );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof BudgetMaster ) )
    {
      return false;
    }

    final BudgetMaster master = (BudgetMaster)o;

    if ( cmAssetCode != null ? !cmAssetCode.equals( master.getCmAssetCode() ) : master.getCmAssetCode() != null )
    {
      return false;
    }
    if ( nameCmKey != null ? !nameCmKey.equals( master.getNameCmKey() ) : master.getNameCmKey() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = cmAssetCode != null ? cmAssetCode.hashCode() : 0;
    result = 29 * result + ( nameCmKey != null ? nameCmKey.hashCode() : 0 );
    return result;
  }

  /**
   * Add a Budget to budgets
   * 
   * @param budget
   */
  public void addBudgetSegment( BudgetSegment budgetSegment )
  {
    budgetSegment.setBudgetMaster( this );
    this.budgetSegments.add( budgetSegment );
  }

  public String getNameCmKey()
  {
    return nameCmKey;
  }

  public void setNameCmKey( String nameCmKey )
  {
    this.nameCmKey = nameCmKey;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getBudgetName()
  {
    if ( budgetName == null )
    {
      return getBudgetMasterName();
    }
    return budgetName;
  }

  public void setBudgetName( String budgetName )
  {
    this.budgetName = budgetName;
  }

  public BudgetType getBudgetType()
  {
    return budgetType;
  }

  public void setBudgetType( BudgetType budgetType )
  {
    this.budgetType = budgetType;
  }

  public boolean isMultiPromotion()
  {
    return multiPromotion;
  }

  public void setMultiPromotion( boolean multiPromotion )
  {
    this.multiPromotion = multiPromotion;
  }

  public BudgetOverrideableType getOverrideableType()
  {
    return overrideableType;
  }

  public void setOverrideableType( BudgetOverrideableType overrideableType )
  {
    this.overrideableType = overrideableType;
  }

  public Set<BudgetSegment> getBudgetSegments()
  {
    return budgetSegments;
  }

  public void setBudgetSegments( Set<BudgetSegment> budgetSegments )
  {
    this.budgetSegments = budgetSegments;
  }

  public BudgetSegment getCurrentBudgetSegment( String paxTimeZoneId )
  {
    if ( budgetSegments.size() > 0 )
    {
      Date currentDate = DateUtils.getCurrentDate();
      if ( !StringUtils.isEmpty( paxTimeZoneId ) )
      {
        currentDate = DateUtils.applyTimeZone( new Date(), paxTimeZoneId );
      }
      Iterator<BudgetSegment> iter = budgetSegments.iterator();
      while ( iter.hasNext() )
      {
        BudgetSegment budgetSegment = (BudgetSegment)iter.next();
        if ( budgetSegment.getStatus() && DateUtils.isDateBetween( currentDate, budgetSegment.getStartDate(), budgetSegment.getEndDate() ) )
        {
          return budgetSegment;
        }
      }
      return null;
    }
    else
    {
      throw new BeaconRuntimeException( "No Time Period(Budget Segment) found under budget master : " + budgetName );
    }
  }

  // to fix 18290
  public boolean isActive()
  {
    return this.active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getBudgetMasterName()
  {
    return ContentReaderManager.getText( this.cmAssetCode, this.nameCmKey );
  }

  public Set getPromotions()
  {
    return promotions;
  }

  public void setPromotions( Set promotions )
  {
    this.promotions = promotions;
  }

  public Set<Promotion> getCashPromotions()
  {
    return cashPromotions;
  }

  public void setCashPromotions( Set<Promotion> cashPromotions )
  {
    this.cashPromotions = cashPromotions;
  }

  /**
   * Returns true if this budget master represents a central budget; returns false otherwise.
   *
   * @return true if this budget master represents a central budget; return false otherwise.
   */
  public boolean isCentralBudget()
  {
    return budgetType.getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE );
  }

  /**
  * Returns true if this budget master represents a node budget; returns false otherwise.
  *
  * @return true if this budget master represents a node budget; return false otherwise.
  */
  public boolean isNodeBudget()
  {
    return budgetType.getCode().equals( BudgetType.NODE_BUDGET_TYPE );
  }

  /**
  * Returns true if this budget master represents a participant budget; returns false otherwise.
  *
  * @return true if this budget master represents a participant budget; return false otherwise.
  */
  public boolean isParticipantBudget()
  {
    return budgetType.getCode().equals( BudgetType.PAX_BUDGET_TYPE );
  }

  public BudgetFinalPayoutRule getFinalPayoutRule()
  {
    return finalPayoutRule;
  }

  public void setFinalPayoutRule( BudgetFinalPayoutRule finalPayoutRule )
  {
    this.finalPayoutRule = finalPayoutRule;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public BudgetMasterAwardType getAwardType()
  {
    return awardType;
  }

  public void setAwardType( BudgetMasterAwardType awardType )
  {
    this.awardType = awardType;
  }

  public boolean isAllowAdditionalTransferrees()
  {
    return allowAdditionalTransferrees;
  }

  public void setAllowAdditionalTransferrees( boolean allowAdditionalTransferrees )
  {
    this.allowAdditionalTransferrees = allowAdditionalTransferrees;
  }

}
