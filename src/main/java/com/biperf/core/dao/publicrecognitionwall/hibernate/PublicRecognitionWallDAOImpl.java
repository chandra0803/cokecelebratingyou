
package com.biperf.core.dao.publicrecognitionwall.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.publicrecognitionwall.CallPrcPublicRecognitionWallList;
import com.biperf.core.dao.publicrecognitionwall.PublicRecognitionWallDAO;
import com.biperf.core.value.publicrecognitionwall.PublicRecognitionWallBean;

/**
 * 
 * @author gandreds
 * @since Apr 8, 2016
 * 
 */
@Repository
public class PublicRecognitionWallDAOImpl extends BaseDAO implements PublicRecognitionWallDAO
{
  @Autowired
  private DataSource dataSource;

  @PostConstruct
  private void initialize()
  {
    setDataSource( dataSource );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<PublicRecognitionWallBean> getPublicRecognitionWallByPageCount( final int pageCount )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "pageCount", pageCount );
    CallPrcPublicRecognitionWallList procedure = new CallPrcPublicRecognitionWallList( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( inParams );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      return null;
    }
    List<PublicRecognitionWallBean> publicRecognitionWallBeanList = (List<PublicRecognitionWallBean>)outParams.get( "p_out_result_set" );
    return publicRecognitionWallBeanList;
  }

}
