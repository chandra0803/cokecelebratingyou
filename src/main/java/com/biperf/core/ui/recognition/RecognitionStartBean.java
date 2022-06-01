
package com.biperf.core.ui.recognition;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.user.UserNode;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.RecognitionBean;

public class RecognitionStartBean
{
  private List<RecognitionBean> promotions;
  private List<NodeBean> nodes;
  // Client customizations for wip #26532 starts
  private List<String> purlAllowOutsideDomains;
  //  Client customizations for wip #26532 ends
  
  public RecognitionStartBean()
  {
  }

  public RecognitionStartBean( List<RecognitionBean> recognitionPromotions, List<UserNode> userNodes, List<String> allowedDomains )
  {
    // promotions
    setRecognitionPromotions( recognitionPromotions );

    // nodes
    setUserNodes( userNodes );
    
    // Client customizations for wip #26532 starts
    // acceptable domains
    setPurlAllowOutsideDomains( allowedDomains );
    // Client customizations for wip #26532 ends
  }

  public List<RecognitionBean> getPromotions()
  {
    return promotions;
  }

  public List<NodeBean> getNodes()
  {
    return nodes;
  }

  public final void setUserNodes( List<UserNode> userNodes )
  {
    if ( userNodes != null && !userNodes.isEmpty() )
    {
      nodes = new ArrayList<NodeBean>( userNodes.size() );
      for ( UserNode userNode : userNodes )
      {
        nodes.add( new NodeBean( userNode ) );
      }
    }
  }

  public final void setRecognitionPromotions( List<RecognitionBean> recognitionPromotions )
  {
    if ( recognitionPromotions != null && !recognitionPromotions.isEmpty() )
    {
      promotions = new ArrayList<RecognitionBean>();

      for ( RecognitionBean p : recognitionPromotions )
      {
        promotions.add( p );
      }
    }
  }

  // Client customizations for wip #26532 starts
  public List<String> getPurlAllowOutsideDomains()
  {
    return purlAllowOutsideDomains;
  }

  public void setPurlAllowOutsideDomains( List<String> purlAllowOutsideDomains )
  {
    this.purlAllowOutsideDomains = purlAllowOutsideDomains;
  }
  // Client customizations for wip #26532 ends
  public static class NodeBean extends NameableBean
  {
    public NodeBean( UserNode userNode )
    {
      super( userNode.getNode().getId(), userNode.getNode().getName() );
    }
  }
}
