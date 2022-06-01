
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.value.ssi.SSIContestDescriptionValueBean;

/**
 * 
 * SSIContestDescriptionsView.
 * 
 * @author chowdhur
 * @since Nov 18, 2014
 */
public class SSIContestDescriptionsView
{
  private List<SSIContestDescriptionValueBean> descriptions;

  public List<SSIContestDescriptionValueBean> getDescriptions()
  {
    return descriptions;
  }

  public void setDescriptions( List<SSIContestDescriptionValueBean> descriptions )
  {
    this.descriptions = descriptions;
  }

}
