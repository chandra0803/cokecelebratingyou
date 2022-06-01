
package com.biperf.core.orm.id;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.exception.JDBCExceptionHelper;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;

/** 
* The BIBatchSequenceGenerator is a class to more efficiently generate Long values used
* for IDs in hibernate domain objects via a sequence.  This class closely follows the
* generation methods of the other SequenceGenerator implementations.  The major difference
* is that the batchSize argument is used to make 1 call to an Oracle database to collect the
* batchSize number of IDs from the sequence.  This class then uses these in memory until they
* are depleted - in which case it asked for more values according to the batchSize.  In the base
* class, a database call is made for EACH insert to get the ID.  This class reduces network hops
* by a factor of the batchSize.
*
* @author Chris Siemback
* @version 1.0 Oct. 17, 2013
*/
public class BIBatchSequenceGenerator extends SequenceGenerator
{
  public static final String BATCH_SIZE = "batch_size";

  private int batchSize;
  private String generatedSequenceSql = null;
  private Deque<IntegralDataTypeHolder> idValues = null;

  public void configure( Type type, Properties params, Dialect d ) throws MappingException
  {
    super.configure( type, params, d );
    valiateBatchSize( params );
    batchSize = PropertiesHelper.getInt( BATCH_SIZE, params, 10 );
    generatedSequenceSql = d.getSequenceNextValString( getSequenceName() ) + " connect by level < " + batchSize;
    idValues = new ArrayDeque<IntegralDataTypeHolder>( batchSize );
  }

  public synchronized Serializable generate( final SessionImplementor session, Object obj )
  {
    return generateHolder( session ).makeValue();
  }

  protected IntegralDataTypeHolder generateHolder( final SessionImplementor session )
  {
    if ( idValues.isEmpty() )
    {
      populateValues( session );
    }
    return idValues.pollFirst();
  }

  private void populateValues( final SessionImplementor session )
  {
    try
    {
      PreparedStatement st = session.getBatcher().prepareSelectStatement( generatedSequenceSql );
      try
      {
        ResultSet rs = st.executeQuery();
        try
        {
          while ( rs.next() )
          {
            IntegralDataTypeHolder result = buildHolder();
            idValues.add( result.initialize( rs, 1 ) );
          }
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        session.getBatcher().closeStatement( st );
      }
    }
    catch( SQLException sqle )
    {
      throw JDBCExceptionHelper.convert( session.getFactory().getSQLExceptionConverter(), sqle, "could not get next sequence value", generatedSequenceSql );
    }
  }

  private void valiateBatchSize( Properties params ) throws MappingException
  {
    String value = params.getProperty( BATCH_SIZE );
    if ( null == value )
    {
      throw new MappingException( "Batch size is NULL!" );
    }
    if ( !StringUtils.isNumeric( value ) )
    {
      throw new MappingException( "Batch size [ " + value + " ] is not a value numeric" );
    }
    if ( Integer.parseInt( value ) < 2 )
    {
      throw new MappingException( "Batch size [ " + value + " ] is less than 2 - you are not using this ID Generator correctly - use the standard sequence generator" );
    }
  }
}
