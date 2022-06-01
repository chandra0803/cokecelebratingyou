
package com.biperf.core.ui.recognition.state;

import java.io.Serializable;

public class CalculatorResult implements Serializable
{
  private Long criteriaId;
  private Long ratingId;
  private Long criteriaRating;

  public Long getCriteriaId()
  {
    return criteriaId;
  }

  public void setCriteriaId( Long criteriaId )
  {
    this.criteriaId = criteriaId;
  }

  public Long getRatingId()
  {
    return ratingId;
  }

  public void setRatingId( Long ratingId )
  {
    this.ratingId = ratingId;
  }

  public Long getCriteriaRating()
  {
    return criteriaRating;
  }

  public void setCriteriaRating( Long criteriaRating )
  {
    this.criteriaRating = criteriaRating;
  }
}
