
package com.biperf.core.ui.purl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.DateUtils;

public class PurlMaintenanceForm extends BaseActionForm
{
  public static final String NAME = "purlMaintenanceForm";

  private static final int INVITE_DEFAULT_SIZE = 1000;

  private String method;

  private PurlRecipient recipient;
  private Date awardDate;
  private String[] selectedBox;
  private String data;

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    recipient = new PurlRecipient();
    selectedBox = new String[INVITE_DEFAULT_SIZE];
  }

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    return actionErrors;
  }

  public PurlRecipient getRecipient()
  {
    return recipient;
  }

  public void setRecipient( PurlRecipient recipient )
  {
    this.recipient = recipient;
  }

  public String[] getSelectedBox()
  {
    return selectedBox;
  }

  public void setSelectedBox( String[] selectedBox )
  {
    this.selectedBox = selectedBox;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  public String getStringAwardDate()
  {
    return DateUtils.toDisplayString( awardDate );
  }

  public void setStringAwardDate( String awardDate )
  {
    this.awardDate = DateUtils.toDate( awardDate );
  }

  public void setData( String data )
  {
    this.data = data;
  }

  public String getData()
  {
    return data;
  }
}
