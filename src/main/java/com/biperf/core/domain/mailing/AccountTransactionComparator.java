
package com.biperf.core.domain.mailing;

import java.util.Comparator;

import com.biperf.core.domain.participant.AccountTransaction;

public class AccountTransactionComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof AccountTransaction ) || ! ( o2 instanceof AccountTransaction ) )
    {
      throw new ClassCastException( "Object is not a accountTransaction domain object!" );
    }
    AccountTransaction event1 = (AccountTransaction)o1;
    AccountTransaction event2 = (AccountTransaction)o2;

    if ( event1 != null && event2 != null )
    {
      return new Long( event1.getTransactionDate().getTime() ).compareTo( new Long( event2.getTransactionDate().getTime() ) );
    }
    return 0;
  }
}
