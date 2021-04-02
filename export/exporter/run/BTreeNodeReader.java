package run;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.cdt.internal.core.pdom.PDOM;
import org.eclipse.cdt.internal.core.pdom.db.BTree;
import org.eclipse.cdt.internal.core.pdom.db.Database;
import org.eclipse.core.runtime.CoreException;


public class BTreeNodeReader {
	
	public long record;
	public ArrayList<Long> records;
	final public BTree tree;
	public PDOM pdom;
	HashMap<Long, String> visited;
	
	/**
	 * Reads a PDOM Btree Index. 
	 * @param record is the offset to the first node (root offset)
	 * @param tree	BTree to read
	 * @param records_List the reader saves all records (offsets) to all nodes of the tree in this list
	 * @param pdom PDOM
	 * @param visited saves all visited nodes in a hashmap (need this here for recursion)
	 */
	public BTreeNodeReader(long record, BTree tree,ArrayList<Long> records_List, PDOM pdom, HashMap<Long, String> visited)
	{		
		this.tree = tree; 
		this.pdom = pdom;
		this.record = record;
		
		records = records_List;

		setChildren();	
		setRecords();
			
	}
	
	
	protected final long getRecord(long node, int index) throws CoreException {
		return pdom.getDB().getRecPtr(node + index * Database.INT_SIZE);
	}


	protected final long getChild(long node, int index) throws CoreException {
		return pdom.getDB().getRecPtr(node + tree.OFFSET_CHILDREN + index * Database.INT_SIZE);
	}
	
	private void setChildren(){
		
		try {
		int index = 0;
		long node = tree.db.getRecPtr(record + tree.OFFSET_CHILDREN + index * Database.INT_SIZE);
		while(node != 0 && index < tree.MAX_CHILDREN)
		{
			new BTreeNodeReader(node, this.tree, this.records, this.pdom, this.visited);
			index++;
			node = tree.db.getRecPtr(record + tree.OFFSET_CHILDREN + index * Database.INT_SIZE);
		}
		}catch(Exception Exc) {
			Run.Exceptions_logFile.println(Exc.getMessage());
		}
		
	}

	private void setRecords(){

		try {
		int index = 0;
		long rec = tree.db.getRecPtr(record + index * Database.INT_SIZE);
		while(rec != 0 && index < tree.MAX_RECORDS)
		{
			records.add(rec);
			index++;
			rec = tree.db.getRecPtr(record + index * Database.INT_SIZE);
			
		}
		}catch(Exception exc)
		{
			Run.Exceptions_logFile.println(exc.getMessage());
		}
	}
	
	
	

}
