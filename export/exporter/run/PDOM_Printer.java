package run;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.cdt.core.index.IIndexLinkage;
import org.eclipse.cdt.internal.core.index.IIndexFragmentFile;
import org.eclipse.cdt.internal.core.pdom.PDOM;
import org.eclipse.cdt.internal.core.pdom.db.Database;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMLinkage;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMNamedNode;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMNode;
import org.eclipse.core.runtime.CoreException;

public class PDOM_Printer {
	
	//-----------------------Database_Constants------------------//
	public static final int INT_SIZE = 4;
	public static final int CHUNK_SIZE = 1024 * 4;
	public static final int OFFSET_IN_CHUNK_MASK= CHUNK_SIZE - 1;
	public static final int BLOCK_HEADER_SIZE= 2;
	public static final int BLOCK_SIZE_DELTA_BITS = 3;
	public static final int BLOCK_SIZE_DELTA= 1 << BLOCK_SIZE_DELTA_BITS;
	public static final int MIN_BLOCK_DELTAS = 2;	// a block must at least be 2 + 2*4 bytes to link the free blocks.
	public static final int MAX_BLOCK_DELTAS = CHUNK_SIZE / BLOCK_SIZE_DELTA;
	public static final int MAX_MALLOC_SIZE = MAX_BLOCK_DELTAS * BLOCK_SIZE_DELTA - BLOCK_HEADER_SIZE;
	public static final int PTR_SIZE = 4;  // size of a pointer in the database in bytes
	// The lower bound for TYPE_SIZE is 1 + PTR_SIZE, but a slightly larger space for types stored
	// inline produces in a slightly smaller overall database size.
	public static final int TYPE_SIZE = 2 + PTR_SIZE;  // size of a type in the database in bytes
	public static final int VALUE_SIZE = 1 + PTR_SIZE;  // size of a value in the database in bytes
	public static final int EVALUATION_SIZE = TYPE_SIZE;  // size of an evaluation in the database in bytes
	public static final int EXECUTION_SIZE = TYPE_SIZE;  // size of an execution in the database in bytes
	public static final int ARGUMENT_SIZE = TYPE_SIZE;  // size of a template argument in the database in bytes
	public static final long MAX_DB_SIZE= ((long) 1 << (Integer.SIZE + BLOCK_SIZE_DELTA_BITS));

	public static final int VERSION_OFFSET = 0;
	public static final int DATA_AREA = (CHUNK_SIZE / BLOCK_SIZE_DELTA - MIN_BLOCK_DELTAS + 2) * INT_SIZE;	
	
	//----------------------------------PDOM-Header-OFFSETS--------------------//
	
	public static final int LINKAGES = Database.DATA_AREA;
	public static final int FILE_INDEX = Database.DATA_AREA + 4;
	public static final int INDEX_OF_DEFECTIVE_FILES = Database.DATA_AREA + 8;
	public static final int INDEX_OF_FILES_WITH_UNRESOLVED_INCLUDES = Database.DATA_AREA + 12;
	public static final int PROPERTIES = Database.DATA_AREA + 16;
	public static final int TAG_INDEX = Database.DATA_AREA + 20;
	public static final int END= Database.DATA_AREA + 24;
	
	
	public static void printHeader(PDOM pdom) throws CoreException {
		System.out.println("header: PDOM: " + pdom.getPath().getName()); //$NON-NLS-1$
		System.out.println("\t version: " + pdom.getDB().getInt(VERSION_OFFSET)); //$NON-NLS-1$
		System.out.println("\t linkages: " + pdom.getDB().getRecPtr(LINKAGES)); //$NON-NLS-1$
		System.out.println("\t file Index: " + pdom.getDB().getRecPtr(FILE_INDEX)); //$NON-NLS-1$
		System.out.println("\t index of defective files: " + pdom.getDB().getRecPtr(INDEX_OF_DEFECTIVE_FILES)); //$NON-NLS-1$
		System.out.println("\t index of files with unresolved includes: " + pdom.getDB().getRecPtr(INDEX_OF_FILES_WITH_UNRESOLVED_INCLUDES)); //$NON-NLS-1$
		System.out.println("\t properties: " + pdom.getDB().getRecPtr(PROPERTIES)); //$NON-NLS-1$
		System.out.println("\t tag index: " + pdom.getDB().getRecPtr(TAG_INDEX));		 //$NON-NLS-1$
	}
	
	public static void printLinkages(PDOM pdom, boolean IndexList, boolean nestedBList, boolean MacroL ) throws CoreException{
		System.out.println("Linkages: "); //$NON-NLS-1$
		
		IIndexLinkage[] links = pdom.getLinkages();
		for(int i = 0; i < links.length;i++) {
			System.out.println("\t ID: " + links[i].getLinkageName()); //$NON-NLS-1$
						
			ArrayList<Long> index_List = new ArrayList<Long>();
			ArrayList<Long> NestedBinding_List = new ArrayList<Long>();
			ArrayList<Long> MacroList = new ArrayList<Long>();
				
			PDOMLinkage link = pdom.getLinkage(links[i].getLinkageID());
			new BTreeNodeReader(pdom.getDB().getRecPtr(link.getIndex().rootPointer), link.getIndex(), index_List, pdom, new HashMap<Long,String>());
			new BTreeNodeReader(pdom.getDB().getRecPtr(link.getNestedBindingsIndex().rootPointer), link.getNestedBindingsIndex(), NestedBinding_List, pdom, new HashMap<Long,String>());
			new BTreeNodeReader(pdom.getDB().getRecPtr(link.getNestedBindingsIndex().rootPointer), link.getNestedBindingsIndex(), MacroList, pdom, new HashMap<Long,String>());
			
			
			if(IndexList) {
				System.out.println("\t index BTree:"); //$NON-NLS-1$
				index_List.forEach(x -> {
					try {
						PDOMNamedNode node = (PDOMNamedNode) link.getNode(x, PDOMNode.getNodeType(link.getDB(), x));
						if(node != null) {
							System.out.println("\t\t"+ + node.getNodeType() + node ); //$NON-NLS-1$
						}							
					} catch (NullPointerException | CoreException e) {					
						//System.err.println(e.getMessage());

					}
				});
			}
			
			if(nestedBList) {
				System.out.println("\t nested binding BTree: ");		 //$NON-NLS-1$
				NestedBinding_List.forEach(x -> {
					try {
						PDOMNamedNode node = (PDOMNamedNode) link.getNode(x, PDOMNode.getNodeType(link.getDB(), x));
						if(node != null)
							System.out.println("\t\t" + node.getNodeType() + " " + node ); //$NON-NLS-1$ //$NON-NLS-2$
					} catch (NullPointerException | CoreException e) {
						//System.err.println(e.getMessage());
					}
				});
			}
						
			if(MacroL) {
				System.out.println("\t macro Btree root: "); //$NON-NLS-1$
				MacroList.forEach(x -> {
					try {
						PDOMNamedNode node = (PDOMNamedNode) link.getNode(x, PDOMNode.getNodeType(link.getDB(), x));
						if(node != null)
							System.out.println("\t\t"+ + node.getNodeType() + node ); //$NON-NLS-1$
					} catch (NullPointerException | CoreException e) {					
						//System.err.println(e.getMessage());
					}
				});
			}			
		}
		
	}
	
		
	public static void print_File_Index(PDOM pdom) throws CoreException {
		IIndexFragmentFile[] files = pdom.getAllFiles();
		System.out.println("files: "); //$NON-NLS-1$
		for(int i = 0; i < files.length; i++) {
			
			try {
				System.out.println("\t" + files[i].toDebugString()); //$NON-NLS-1$
			}catch(Exception exc) {
				
			}
			
		}		
	}

	public static void printHelp() {
		System.out.println("PDOM_Export.java -P {path_pdom_file} -{options}\n" + //$NON-NLS-1$
				"-P {pdom_path} path of pdom file\n" + //$NON-NLS-1$
				"-D {database_path} path of database folder\n" + //$NON-NLS-1$
				"-S {symbol_path} path of symbol list\n" + //$NON-NLS-1$
				"-o overwrite database\n"+ //$NON-NLS-1$
				"-f print files\n" + //$NON-NLS-1$
				"-H print header\n" + //$NON-NLS-1$
				"-l print linkages\n" + //$NON-NLS-1$
				"-h print help \n"); //$NON-NLS-1$		
	}
	

}
