
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.value.ssi.SSIContestNameValueBean;

/**
 * 
 * SSIContestNamesView.
 * 
 * @author chowdhur
 * @since Nov 18, 2014
 */
public class SSIContestNamesView
{
  private List<SSIContestNameValueBean> names;

  public List<SSIContestNameValueBean> getNames()
  {
    return names;
  }

  public void setNames( List<SSIContestNameValueBean> names )
  {
    this.names = names;
  }

}
