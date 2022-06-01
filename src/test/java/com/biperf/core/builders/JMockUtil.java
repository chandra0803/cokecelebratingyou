package com.biperf.core.builders;

import org.jmock.core.Invocation;
import org.jmock.core.InvocationMatcher;

public class JMockUtil
{
  
  public static InvocationMatcher anyTimes()
  {
    return new InvocationMatcher()
    {
      
      @Override
      public StringBuffer describeTo( StringBuffer buffer )
      {
        return buffer;
      }
      
      @Override
      public void verify()
      {
      }
      
      @Override
      public boolean matches( Invocation invocation )
      {
        return true;
      }
      
      @Override
      public void invoked( Invocation invocation )
      {
      }
      
      @Override
      public boolean hasDescription()
      {
        return false;
      }
    };
  }

}
