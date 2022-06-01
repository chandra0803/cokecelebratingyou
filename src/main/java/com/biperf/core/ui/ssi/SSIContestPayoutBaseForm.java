
package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;
import org.apache.struts.action.ActionErrors;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestBillCode;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.ssi.view.SSIContestBillCodeView;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.StringUtil;

/**
 * SSIContestPayoutBaseForm.
 * 
 * @author dudam
 * @since May 26, 2015
 * @version 1.0
 */
public class SSIContestPayoutBaseForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String billTo;
  private boolean billCodeReuired;

  private List<SSIContestBillCodeView> billCodes;

  public String getBillTo()
  {
    return billTo;
  }

  public void setBillTo( String billTo )
  {
    this.billTo = billTo;
  }

  public boolean isBillCodeReuired()
  {
    return billCodeReuired;
  }

  public void setBillCodeReuired( boolean billCodeReuired )
  {
    this.billCodeReuired = billCodeReuired;
  }

  protected void validateBillCode( ActionErrors actionErrors )
  {
  }

  @SuppressWarnings( "unchecked" )
  public List<SSIContestBillCodeView> getBillCodes()
  {
    if ( billCodes == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new SSIContestBillCodeView();
        }
      };
      billCodes = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return billCodes;
  }

  public void populateBillCodes( SSIContest ssiContest )
  {
    SSIPromotion ssiPromotion = getSSIPromotionService().getLiveSSIPromotion();
    if ( Objects.isNull( billCodes ) )
    {
      ssiContest.setContestBillCodes( new ArrayList<SSIContestBillCode>() );
      return;
    }

    SSIContestBillCode billCodeDomain;
    List<SSIContestBillCode> billCodesList = new ArrayList<SSIContestBillCode>();
    for ( SSIContestBillCodeView view : this.billCodes )
    {
      if ( Objects.isNull( view.getIndexAsLong() ) )
      {
        continue;
      }

      billCodeDomain = new SSIContestBillCode();
      billCodeDomain.setBillCode( view.getBillCodeDomainName( ssiPromotion.getId() ) );
      billCodeDomain.setCustomValue( view.getCustomValue() );
      billCodeDomain.setSortOrder( view.getIndexAsLong() );
      billCodeDomain.setTrackBillCodeBy( StringUtil.isNullOrEmpty( view.getTrackBy() ) ? view.getTrackBy() : view.getTrackBy().toLowerCase() );
      billCodeDomain.setSsiContest( ssiContest );
      billCodesList.add( billCodeDomain );

    }
    ssiContest.setContestBillCodes( billCodesList );
  }

  private SSIPromotionService getSSIPromotionService()
  {
    return (SSIPromotionService)BeanLocator.getBean( SSIPromotionService.BEAN_NAME );
  }

}
