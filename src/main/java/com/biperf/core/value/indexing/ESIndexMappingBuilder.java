
package com.biperf.core.value.indexing;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;

public interface ESIndexMappingBuilder
{

  public XContentBuilder buildJsonSchema() throws IOException;

  public XContentBuilder buildJsonData( Searchable searchable );

  public Object buildId( Searchable searchable ) throws IOException;
  
  public String getJsonSchema() throws IOException;
  
  public String getJsonData( Searchable indexData ) throws IOException;

}
