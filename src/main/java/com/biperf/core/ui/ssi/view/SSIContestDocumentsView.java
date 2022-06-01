
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.value.ssi.SSIContestDocumentValueBean;

/**
 * 
 * SSIContestDocumentsView.
 * 
 * @author chowdhur
 * @since Nov 18, 2014
 */
public class SSIContestDocumentsView
{
  private List<SSIContestDocumentValueBean> documents;

  public List<SSIContestDocumentValueBean> getDocuments()
  {
    return documents;
  }

  public void setDocuments( List<SSIContestDocumentValueBean> documents )
  {
    this.documents = documents;
  }

}
