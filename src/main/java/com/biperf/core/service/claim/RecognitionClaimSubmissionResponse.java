
package com.biperf.core.service.claim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biperf.core.service.util.ServiceError;

public class RecognitionClaimSubmissionResponse
{
  private List<ServiceError> serviceErrors;
  private Long claimId;

  public boolean isSuccess()
  {
    return serviceErrors == null || serviceErrors.isEmpty();
  }

  public void addErrors( Collection<ServiceError> errors )
  {
    if ( errors == null || errors.isEmpty() )
    {
      return;
    }

    if ( serviceErrors == null )
    {
      serviceErrors = new ArrayList<ServiceError>();
    }
    serviceErrors.addAll( errors );
  }

  public List<ServiceError> getErrors()
  {
    return serviceErrors;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

}
