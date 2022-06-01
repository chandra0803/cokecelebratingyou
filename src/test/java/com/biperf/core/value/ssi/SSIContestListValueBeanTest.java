
package com.biperf.core.value.ssi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.biperf.core.domain.enums.SSIContestStatus;

import junit.framework.TestCase;

public class SSIContestListValueBeanTest extends TestCase
{
  public void testSSIContestListValueBeanSorting()
  {
    List<SSIContestListValueBean> valueBeans = new ArrayList<SSIContestListValueBean>();
    valueBeans.add( new SSIContestListValueBean( "1", "livecontest2", SSIContestStatus.LIVE, null, null )
    {
      protected String getClientStatePassword()
      {
        return "123";
      }
    } );
    valueBeans.add( new SSIContestListValueBean( "2", "pendingcontest", SSIContestStatus.PENDING, null, null )
    {
      protected String getClientStatePassword()
      {
        return "123";
      }
    } );
    valueBeans.add( new SSIContestListValueBean( "3", "waitcontest", SSIContestStatus.WAITING_FOR_APPROVAL, null, null )
    {
      protected String getClientStatePassword()
      {
        return "123";
      }
    } );
    valueBeans.add( new SSIContestListValueBean( "4", "draftcontest", SSIContestStatus.DRAFT, null, null )
    {
      protected String getClientStatePassword()
      {
        return "123";
      }
    } );
    valueBeans.add( new SSIContestListValueBean( "5", "livecontest1", SSIContestStatus.LIVE, null, null )
    {
      protected String getClientStatePassword()
      {
        return "123";
      }
    } );
    Collections.sort( valueBeans );
    assertEquals( valueBeans.get( 0 ).getName(), "livecontest1" );
    assertEquals( valueBeans.get( 1 ).getName(), "livecontest2" );
    assertEquals( valueBeans.get( 2 ).getName(), "pendingcontest" );
    assertEquals( valueBeans.get( 3 ).getName(), "waitcontest" );
    assertEquals( valueBeans.get( 4 ).getName(), "draftcontest" );
  }

}
