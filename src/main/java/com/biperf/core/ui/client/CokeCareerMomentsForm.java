package com.biperf.core.ui.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.client.CareerMomentsView;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.value.client.ClientCommentRequestValueBean;

public class CokeCareerMomentsForm extends BaseActionForm
{
  private static final long serialVersionUID = 3566909959097561092L;
  private FormFile fileAsset;
  private String data;
  private String tabType;
  private String cmId;
  private String cmPastPresentSelect;
  private String cmType;
  private String name;
  private String currentPage;
  private String listValue;
  private List<CareerMomentsView> dataView = new ArrayList<CareerMomentsView>();
  
  public String getCmId()
  {
    return cmId;
  }

  public void setCmId( String cmId )
  {
    this.cmId = cmId;
  }

  public String getCmPastPresentSelect()
  {
    return cmPastPresentSelect;
  }

  public void setCmPastPresentSelect( String cmPastPresentSelect )
  {
    this.cmPastPresentSelect = cmPastPresentSelect;
  }

  public String getCmType()
  {
    return cmType;
  }

  public void setCmType( String cmType )
  {
    this.cmType = cmType;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( String currentPage )
  {
    this.currentPage = currentPage;
  }

  public String getTabType()
  {
    return tabType;
  }

  public void setTabType( String tabType )
  {
    this.tabType = tabType;
  }

  public FormFile getFileAsset()
  {
    return fileAsset;
  }

  public void setFileAsset( FormFile fileAsset )
  {
    this.fileAsset = fileAsset;
  }

  public String getData()
  {
    return data;
  }

  public void setData( String data )
  {
    this.data = data;
  }

  public String getListValue()
  {
    return listValue;
  }

  public void setListValue( String listValue )
  {
    this.listValue = listValue;
  }

  public List<CareerMomentsView> getDataView()
  {
    return dataView;
  }

  public void setDataView( List<CareerMomentsView> dataView )
  {
    this.dataView = dataView;
  }
  

}
