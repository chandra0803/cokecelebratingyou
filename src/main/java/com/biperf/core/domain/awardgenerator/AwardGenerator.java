
package com.biperf.core.domain.awardgenerator;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;

/**
 * AwardGenerator
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
 * <td>chowdhur</td>
 * <td>Jul 08, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGenerator extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String name;
  private boolean active;
  private String examinerField;
  private boolean notifyManager;
  private Integer numberOfDaysForAlert;
  private Promotion promotion;

  private Set<AwardGenAward> awardGenAwards = new LinkedHashSet<AwardGenAward>();
  private Set<AwardGenBatch> awardGenBatches = new LinkedHashSet<AwardGenBatch>();

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Default Constructor
   */
  public AwardGenerator()
  {
    super();
  }

  /**
   * @param id
   */
  public AwardGenerator( Long id )
  {
    super( id );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public String getExaminerField()
  {
    return examinerField;
  }

  public void setExaminerField( String examinerField )
  {
    this.examinerField = examinerField;
  }

  public boolean isNotifyManager()
  {
    return notifyManager;
  }

  public void setNotifyManager( boolean notifyManager )
  {
    this.notifyManager = notifyManager;
  }

  public Integer getNumberOfDaysForAlert()
  {
    return numberOfDaysForAlert;
  }

  public void setNumberOfDaysForAlert( Integer numberOfDaysForAlert )
  {
    this.numberOfDaysForAlert = numberOfDaysForAlert;
  }

  public Set<AwardGenAward> getAwardGenAwards()
  {
    return awardGenAwards;
  }

  public void setAwardGenAwards( Set<AwardGenAward> awardGenAwards )
  {
    this.awardGenAwards = awardGenAwards;
  }

  public void addAwardGenAward( AwardGenAward awardGenAward )
  {
    awardGenAward.setAwardGen( this );
    this.awardGenAwards.add( awardGenAward );
  }

  public Set<AwardGenBatch> getAwardGenBatches()
  {
    return awardGenBatches;
  }

  public void setAwardGenBatches( Set<AwardGenBatch> awardGenBatches )
  {
    this.awardGenBatches = awardGenBatches;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "AwardGenerator [name=" + name + ", promotion=" + promotion + ", examinerField=" + examinerField + ", notifyManager=" + notifyManager + ", numberOfDaysForAlert=" + numberOfDaysForAlert
        + ", active=" + active + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( examinerField == null ? 0 : examinerField.hashCode() );
    result = prime * result + ( name == null ? 0 : name.hashCode() );
    result = prime * result + ( notifyManager ? 1231 : 1237 );
    result = prime * result + ( numberOfDaysForAlert == null ? 0 : numberOfDaysForAlert.hashCode() );
    result = prime * result + ( promotion == null ? 0 : promotion.hashCode() );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    AwardGenerator other = (AwardGenerator)obj;
    if ( examinerField == null )
    {
      if ( other.examinerField != null )
      {
        return false;
      }
    }
    else if ( !examinerField.equals( other.examinerField ) )
    {
      return false;
    }
    if ( name == null )
    {
      if ( other.name != null )
      {
        return false;
      }
    }
    else if ( !name.equals( other.name ) )
    {
      return false;
    }
    if ( notifyManager != other.notifyManager )
    {
      return false;
    }
    if ( numberOfDaysForAlert == null )
    {
      if ( other.numberOfDaysForAlert != null )
      {
        return false;
      }
    }
    else if ( !numberOfDaysForAlert.equals( other.numberOfDaysForAlert ) )
    {
      return false;
    }
    if ( promotion == null )
    {
      if ( other.promotion != null )
      {
        return false;
      }
    }
    else if ( !promotion.equals( other.promotion ) )
    {
      return false;
    }
    return true;
  }

}
