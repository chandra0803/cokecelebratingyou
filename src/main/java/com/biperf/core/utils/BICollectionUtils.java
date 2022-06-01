
package com.biperf.core.utils;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BICollectionUtils
{
  public static <T> Stream<List<T>> batches( List<T> source, int batchSize )
  {
    if ( batchSize <= 0 )
    {
      throw new IllegalArgumentException( "length = " + batchSize );
    }
    int size = source.size();
    if ( size <= 0 )
    {
      return Stream.empty();
    }

    int fullChunks = ( size - 1 ) / batchSize;
    return IntStream.range( 0, fullChunks + 1 ).mapToObj( n -> source.subList( n * batchSize, n == fullChunks ? size : ( n + 1 ) * batchSize ) );
  }

}
