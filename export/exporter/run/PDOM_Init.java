package run;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.cdt.core.index.IIndexLinkage;
import org.eclipse.cdt.internal.core.pdom.PDOM;
import org.eclipse.cdt.internal.core.pdom.dom.IPDOMLinkageFactory;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMLinkage;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMNamedNode;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMNode;
import org.eclipse.cdt.internal.core.pdom.dom.c.PDOMCLinkageFactory;
import org.eclipse.cdt.internal.core.pdom.dom.cpp.PDOMCPPLinkageFactory;

public class PDOM_Init {
	
	/**
	 * Reads index
	 * @param pdom
	 * @throws Exception
	 */
	public static HashMap<String, PDOMNamedNode> init_PDOMSymbols(PDOM pdom ) throws Exception{
		
		 HashMap<String, PDOMNamedNode> allNodes = new  HashMap<String, PDOMNamedNode>();
		 
		 IIndexLinkage[] links = pdom.getLinkages();
		 for(int i = 0; i < links.length;i++) {
					
			PDOMLinkage link = pdom.getLinkage(links[i].getLinkageID());
			
			ArrayList<Long> index_List = new ArrayList<Long>();
			ArrayList<Long> NestedBinding_List = new ArrayList<Long>();
				
			new BTreeNodeReader(pdom.getDB().getRecPtr(link.getIndex().rootPointer), link.getIndex(), index_List, pdom, new HashMap<Long,String>());
			new BTreeNodeReader(pdom.getDB().getRecPtr(link.getNestedBindingsIndex().rootPointer), link.getNestedBindingsIndex(), NestedBinding_List, pdom, new HashMap<Long,String>());
			
			NestedBinding_List.forEach(x -> {
				try {
					PDOMNamedNode node = (PDOMNamedNode) link.getNode(x, PDOMNode.getNodeType(link.getDB(), x));
					allNodes.put(new String(node.getNameCharArray()), node);
				} catch (Exception e) {
					Run.Exceptions_logFile.println(e.getMessage());
				}
			});
			
			index_List.forEach(x -> {
				try {
					PDOMNamedNode node = (PDOMNamedNode) link.getNode(x, PDOMNode.getNodeType(link.getDB(), x));
					allNodes.put(new String(node.getNameCharArray()), node);
				} catch (Exception e) {					
					Run.Exceptions_logFile.println(e.getMessage());

				}
			});		
		}
		
		return allNodes;
	}
	
	public static HashMap<String, IPDOMLinkageFactory> getinkageFactoryMap () {
		HashMap<String, IPDOMLinkageFactory> Map = new HashMap<String, IPDOMLinkageFactory>();
		Map.put("C", new PDOMCLinkageFactory()); //$NON-NLS-1$
		Map.put("C++", new PDOMCPPLinkageFactory()); //$NON-NLS-1$
		return Map;		
	}

}
