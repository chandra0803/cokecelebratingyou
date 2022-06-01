
package com.biperf.core.value.approvals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;

/**
 * Value bean for nominations approval tabular data
 * 
 * @author corneliu
 * @since Apr 22, 2016
 */
public class NominationsApprovalTabularDataBean
{
  private List<NominationsApprovalResultBean> results;

  @SuppressWarnings( "unchecked" )
  public List<NominationsApprovalResultBean> getResults()
  {
    if ( results == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationsApprovalResultBean();
        }
      };

      results = ListUtils.lazyList( new ArrayList<>(), factory );
    }

    return results;
  }

  public void setResults( List<NominationsApprovalResultBean> results )
  {
    this.results = results;
  }

}
