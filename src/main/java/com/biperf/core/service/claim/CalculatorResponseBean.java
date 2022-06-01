
package com.biperf.core.service.claim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
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
 * <td>sedey</td>
 * <td>June, 2 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorResponseBean implements Serializable
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private List ratingList = new ArrayList();
  private Long calculatorResponseId;
  private Long criterionId;
  private String criterionDesc;
  private String selectedRating;
  private String weightValue;
  private String scoreValue;

  public CalculatorResponseBean()
  {
  }

  public CalculatorResponseBean( Long criteriaId, Long ratingId )
  {
    this.criterionId = criteriaId;
    this.selectedRating = ratingId.toString();
  }

  public String getCriterionDesc()
  {
    return criterionDesc;
  }

  public void setCriterionDesc( String criterionDesc )
  {
    this.criterionDesc = criterionDesc;
  }

  public List getRatingList()
  {
    return ratingList;
  }

  public void setRatingList( List ratingList )
  {
    this.ratingList = ratingList;
  }

  public int getRatingListCount()
  {
    if ( ratingList == null )
    {
      return 0;
    }
    return ratingList.size();
  }

  public String getSelectedRating()
  {
    return selectedRating;
  }

  public void setSelectedRating( String selectedRating )
  {
    this.selectedRating = selectedRating;
  }

  public String getWeightValue()
  {
    return weightValue;
  }

  public void setWeightValue( String weightValue )
  {
    this.weightValue = weightValue;
  }

  public Long getCriterionId()
  {
    return criterionId;
  }

  public void setCriterionId( Long criterionId )
  {
    this.criterionId = criterionId;
  }

  public String getScoreValue()
  {
    return scoreValue;
  }

  public void setScoreValue( String scoreValue )
  {
    this.scoreValue = scoreValue;
  }

  public Long getCalculatorResponseId()
  {
    return calculatorResponseId;
  }

  public void setCalculatorResponseId( Long calculatorResponseId )
  {
    this.calculatorResponseId = calculatorResponseId;
  }

}
