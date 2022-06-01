
package com.biperf.core.ui.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.value.BudgetSegmentValueBean;
import com.biperf.core.value.client.TcccLevelPayoutValueBean;
import com.biperf.core.value.client.TcccLevelPayoutValueBeanComparator;

public class TcccLevelPayoutForm extends BaseForm
{
  public static final String FORM_NAME = "tcccLevelPayoutForm";

  private List<TcccLevelPayoutValueBean> payoutLevelList = new ArrayList<TcccLevelPayoutValueBean>();

  public List<TcccLevelPayoutValueBean> getPayoutLevelList()
  {
    if ( payoutLevelList == null )
    {
      payoutLevelList = new ArrayList<TcccLevelPayoutValueBean>();
    }
    return payoutLevelList;
  }

  public void setPayoutLevelList( List<TcccLevelPayoutValueBean> payoutLevelList )
  {
    this.payoutLevelList = payoutLevelList;
  }

  public TcccLevelPayoutValueBean getPayoutLeveBeanList( int index )
  {
    try
    {
      return (TcccLevelPayoutValueBean)payoutLevelList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public void load( List<TcccLevelPayoutValueBean> levelPayoutList, String promotionId )
  {
    TcccLevelPayoutValueBean levelPayoutVB = null;

    if ( levelPayoutList != null && levelPayoutList.size() > 0 )

    {
      for ( Iterator levelPayoutIter = levelPayoutList.iterator(); levelPayoutIter.hasNext(); )
      {
        levelPayoutVB = (TcccLevelPayoutValueBean)levelPayoutIter.next();
        payoutLevelList.add( levelPayoutVB );

      }
    }
    else
    {
      levelPayoutVB = new TcccLevelPayoutValueBean();
      levelPayoutVB.setPromotionId( promotionId );
      payoutLevelList.add( levelPayoutVB );
    }
    Collections.sort( payoutLevelList, new TcccLevelPayoutValueBeanComparator() );
  }

  public void addEmptyPayoutLevel()
  {
    TcccLevelPayoutValueBean levelPayout = new TcccLevelPayoutValueBean();
    levelPayout.setPromotionId( this.payoutLevelList.get( 0 ).getPromotionId() );
    this.payoutLevelList.add( levelPayout );
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // LevelPayout. If this is not done, the form wont initialize
    // properly.
    payoutLevelList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "payoutLevelListSize" ) );
  }

  private List<TcccLevelPayoutValueBean> getEmptyValueList( int valueListCount )
  {
    List<TcccLevelPayoutValueBean> valueList = new ArrayList<TcccLevelPayoutValueBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      TcccLevelPayoutValueBean levePayout = new TcccLevelPayoutValueBean();
      valueList.add( levePayout );
    }

    return valueList;
  }

  public int getPayoutLevelListSize()
  {
    if ( this.payoutLevelList != null )
    {
      return this.payoutLevelList.size();
    }
    return 0;
  }

}
