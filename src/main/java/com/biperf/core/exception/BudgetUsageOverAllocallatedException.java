
package com.biperf.core.exception;

import java.util.List;

public class BudgetUsageOverAllocallatedException extends ServiceErrorException
{

  public BudgetUsageOverAllocallatedException( List serviceErrors )
  {
    super( serviceErrors );
  }

}
