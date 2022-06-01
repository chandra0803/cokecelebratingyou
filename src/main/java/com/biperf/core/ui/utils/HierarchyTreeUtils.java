
package com.biperf.core.ui.utils;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.hierarchy.Node;
import com.jenkov.prizetags.tree.impl.TreeNode;
import com.jenkov.prizetags.tree.itf.ITreeNode;
import com.objectpartners.cms.util.ContentReaderManager;

public class HierarchyTreeUtils
{
  /**
   * add the child nodes for the specified parent node to the tree
   * 
   * @param treeNode
   * @param node
   * @return ITreeNode
   */
  public static ITreeNode addChildren( ITreeNode treeNode, Node node )
  {
    if ( treeNode == null || node == null || node.getChildNodes() == null || node.getChildNodes().isEmpty() )
    {
      return null;
    }

    Iterator children = node.getChildNodes().iterator();
    while ( children.hasNext() )
    {
      Node childNode = (Node)children.next();

      ITreeNode childTreeNode = new TreeNode( String.valueOf( childNode.getId().longValue() ), childNode.getName(), treeNode );
      childTreeNode.setToolTip( ContentReaderManager.getText( childNode.getNodeType().getCmAssetCode(), childNode.getNodeType().getNameCmKey() ) );

      addChildren( childTreeNode, childNode );
    }

    return treeNode;
  }

  /**
   * adds the specified child node to its parent node in the list
   * 
   * @param nodeList
   * @param childNode
   */
  public static void addNodeToParent( List nodeList, Node childNode )
  {
    if ( childNode.getParentNode() == null )
    {
      // this is the root node
      return;
    }

    Iterator iter = nodeList.iterator();
    while ( iter.hasNext() )
    {
      Node currentNode = (Node)iter.next();

      if ( currentNode.equals( childNode.getParentNode() ) )
      {
        currentNode.addChildNode( childNode );
        return;
      }
    }
  }

  /**
   * builds the node list into a node hierarchy starting at the root node
   * 
   * @param nodeList
   * @return Node
   */
  public static Node buildNodeHierarchy( List nodeList )
  {
    Node rootNode = null;

    if ( nodeList == null )
    {
      return rootNode;
    }

    Node currentNode = null;
    Iterator nodeIter = nodeList.iterator();
    while ( nodeIter.hasNext() )
    {
      currentNode = (Node)nodeIter.next();

      if ( currentNode.getParentNode() == null )
      {
        rootNode = currentNode;
      }

      addNodeToParent( nodeList, currentNode );
    }

    return rootNode;
  }

}
