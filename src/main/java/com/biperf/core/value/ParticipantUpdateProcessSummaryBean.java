
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.exception.ServiceErrorException;

/**
 * ParticipantUpdateProcessSummaryBean. 
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
 * <td>kunaseka</td>
 * <td>Jun 22, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ParticipantUpdateProcessSummaryBean implements Serializable
{
  private int totalCount = 0;
  private int successCount = 0;
  private int failureCount = 0;
  private List errorSummary = new ArrayList();

  private class ErrorSummary
  {
    ServiceErrorException error;
    String errorMessage;

    public ServiceErrorException getError()
    {
      return error;
    }

    public void setError( ServiceErrorException error )
    {
      this.error = error;
    }

    public String getErrorMessage()
    {
      return errorMessage;
    }

    public void setErrorMessage( String errorMessage )
    {
      this.errorMessage = errorMessage;
    }
  }

  private ErrorSummary getErrorSummary( int index )
  {
    return (ErrorSummary)this.errorSummary.get( index );
  }

  public int getTotalCount()
  {
    return this.totalCount;
  }

  public int getSuccessCount()
  {
    return this.successCount;
  }

  public int getFailureCount()
  {
    return this.failureCount;
  }

  public int getErrorCount()
  {
    return errorSummary.size();
  }

  public ServiceErrorException getError( int index )
  {
    return index < getErrorCount() ? getErrorSummary( index ).getError() : null;
  }

  public String getErrorMessage( int index )
  {
    return index < getErrorCount() ? getErrorSummary( index ).getErrorMessage() : null;
  }

  public void incTotalCount()
  {
    ++totalCount;
  }

  public void incSuccessCount()
  {
    ++successCount;
  }

  public void incFailureCount()
  {
    ++failureCount;
  }

  public void addError( ServiceErrorException error, String errorMessage )
  {
    ErrorSummary summary = new ErrorSummary();
    summary.setError( error );
    summary.setErrorMessage( errorMessage );
    this.errorSummary.add( summary );
  }
}
