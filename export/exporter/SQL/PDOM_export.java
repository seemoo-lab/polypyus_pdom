package SQL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IEnumerator;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IFunctionType;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IPointerType;
import org.eclipse.cdt.core.dom.ast.IScope;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPBase;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassScope;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassSpecialization;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassTemplate;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassTemplatePartialSpecialization;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPConstructor;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPField;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPFunctionType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPMethod;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPNamespaceScope;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPParameter;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPTemplateArgument;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPTemplateDefinition;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPTemplateInstance;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPTemplateParameter;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPUsingDeclaration;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPUsingDirective;
import org.eclipse.cdt.core.index.IIndexBinding;
import org.eclipse.cdt.internal.core.dom.parser.ProblemBinding;
import org.eclipse.cdt.internal.core.dom.parser.ProblemType;
import org.eclipse.cdt.internal.core.dom.parser.c.CArrayType;
import org.eclipse.cdt.internal.core.dom.parser.c.CBasicType;
import org.eclipse.cdt.internal.core.dom.parser.c.CFunctionType;
import org.eclipse.cdt.internal.core.dom.parser.c.CPointerType;
import org.eclipse.cdt.internal.core.dom.parser.c.CQualifierType;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPArrayType;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPBasicType;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPClassInstance;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPClassSpecializationScope;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPConstructorSpecialization;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPFieldSpecialization;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPFunctionType;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPMethodSpecialization;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPParameterPackType;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPParameterSpecialization;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPPointerType;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPQualifierType;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPReferenceType;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPTemplateNonTypeArgument;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPTemplateTypeArgument;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPTypedefSpecialization;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPUnknownBinding;
import org.eclipse.cdt.internal.core.dom.parser.cpp.ICPPEvaluation;
import org.eclipse.cdt.internal.core.dom.parser.cpp.semantics.EvalBinding;
import org.eclipse.cdt.internal.core.dom.parser.cpp.semantics.EvalFixed;
import org.eclipse.cdt.internal.core.dom.parser.cpp.semantics.TypeOfDependentExpression;
import org.eclipse.cdt.internal.core.index.IIndexScope;
import org.eclipse.cdt.internal.core.pdom.db.BTree;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMNamedNode;
import org.eclipse.cdt.internal.core.pdom.dom.PDOMNode;
import org.eclipse.cdt.internal.core.pdom.dom.c.PDOMCEnumeration;
import org.eclipse.cdt.internal.core.pdom.dom.c.PDOMCEnumerator;
import org.eclipse.cdt.internal.core.pdom.dom.c.PDOMCField;
import org.eclipse.cdt.internal.core.pdom.dom.c.PDOMCFunction;
import org.eclipse.cdt.internal.core.pdom.dom.c.PDOMCParameter;
import org.eclipse.cdt.internal.core.pdom.dom.c.PDOMCStructure;
import org.eclipse.cdt.internal.core.pdom.dom.c.PDOMCTypedef;
import org.eclipse.cdt.internal.core.pdom.dom.c.PDOMCVariable;
import org.eclipse.cdt.internal.core.pdom.dom.cpp.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.cdt.internal.core.index.IIndexCPPBindingConstants;

import run.BTreeNodeReader;

public class PDOM_export {
	
	private DBController controll;
	private int RECDEPTH;
	private PrintStream Exceptions_log;
	private HashMap<Integer, String> IDs = new HashMap<Integer,String>();
	private int mappedSymbols;
	
	public PDOM_export(DBController controller, int maxdepth) throws IOException
	{
		controll = controller;
		RECDEPTH = maxdepth;
		Exceptions_log = run.Run.Exceptions_logFile;
		mappedSymbols = 0;
		
		
	}
	
	public void mapAllSymbols(HashMap<String, PDOMNamedNode> allNodes, boolean Debug) throws Exception{
		int counter = 0;
		
		
		for (Entry<String, PDOMNamedNode> entry : allNodes.entrySet()) {
			counter = counter +1;
			try {
				
				try {
					System.out.println("Done: " + counter + "/" + allNodes.size() + "(exported symbols: " + this.mappedSymbols + ")" + " current symbol: " + entry.getValue()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				}catch(NullPointerException exc) {
					System.out.println("Done: " + counter + "/" + allNodes.size() + "(exported symbols: " + this.mappedSymbols + ")" + " current symbol: " + entry.getKey()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				}
				
				
				if(entry.getValue().getLinkage().getLinkageName() == "C")//$NON-NLS-1$
					this.mapCNode(entry.getValue(), 0, 0,"", Debug);	 //$NON-NLS-1$									
				else
					this.mapCPPNode(entry.getValue(), 0, 0,"", Debug); //$NON-NLS-1$				
					
			}catch(Exception exc) {
				if(exc.getMessage().equals("[SQLITE_BUSY]  The database file is locked (database is locked)"))
					throw new Exception(exc.getMessage() + "\n");
				Exceptions_log.println(entry.getKey());
				exc.printStackTrace(Exceptions_log);
			}
			
		}
				
	}
	
	
	public void mapSymbolsFromFile(String Path, HashMap<String, PDOMNamedNode> allNodes, boolean Debug){
		double counter = 0 ;
		double Found = 0;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(Path));
			String line = reader.readLine();
			while (line != null) {
					if(allNodes.get(line) != null) {
						try {
							if(allNodes.get(line).getLinkage().getLinkageName() == "C")  //$NON-NLS-1$
								this.mapCNode(allNodes.get(line), 0, 0,"", Debug);	 //$NON-NLS-1$								
							else 
								this.mapCPPNode(allNodes.get(line), 0, 0,"", Debug);	 //$NON-NLS-1$
															
						}catch(Exception exc) {
							Exceptions_log.println(line);
							exc.printStackTrace(Exceptions_log);
						}
						Found++; 
					}					
			counter++;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			Exceptions_log.println(e.getMessage());
		}
		
		System.out.println("Symbols in file: " + counter + " found in PDOM bindingsIndex: " + Found); //$NON-NLS-1$ //$NON-NLS-2$ 
	
	}
	
	@SuppressWarnings("unused")
	private static void printTap(int t) {
		for(int i = 0; i < t;i++)
			System.out.print("\t");  //$NON-NLS-1$
	}
	
	
	private int getuniqueID() {
		Random rand = new Random();
		int ID = Math.abs(rand.nextInt());
		
		while(this.IDs.containsKey(ID))
			ID = Math.abs(rand.nextInt());
		
		this.IDs.put(ID, "value"); //$NON-NLS-1$
		return ID;
	}
	
	
	private void mapCPPNode(Object node, int depth, int owner,String ownerVariable, boolean debug) throws SQLException{
		
		if(node == null || depth == this.RECDEPTH)
			return;	
		
		this.mappedSymbols = this.mappedSymbols + 1 ;	
		
		if(node instanceof  PDOMNode) {
			PDOMNode pdomNode = (PDOMNode) node;
			int nodeType = pdomNode.getNodeType();
						
			switch (nodeType) {
			case IIndexCPPBindingConstants.CPPVARIABLE:
				mapPDOMCPPVariable(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPFUNCTION:
				mapPDOMCPPFunction(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPPARAMETER:
				mapPDOMCPPParameter(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPCLASSTYPE:
				mapPDOMCPPClassType(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPFIELD:
				mapPDOMCPPField(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_CONSTRUCTOR:
				mapPDOMCPPConstructor(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPMETHOD:
				mapPDOMCPPMethod(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPNAMESPACE:
				mapPDOMCPPNamespace(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPNAMESPACEALIAS:
				mapPDOMCPPNamespaceAlias(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_USING_DECLARATION:
				mapPDOMCPPUsingDeclaration(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPENUMERATION:
				mapPDOMCPPEnumeration(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPENUMERATOR:
				mapPDOMCPPEnumerator(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPPTYPEDEF:
				mapPDOMCPPTypedef(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_FUNCTION_TEMPLATE:
				mapPDOMCPPFunctionTemplate(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_METHOD_TEMPLATE:
				mapPDOMCPPMethodTemplate(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_CONSTRUCTOR_TEMPLATE:
				mapPDOMCPPConstructorTemplate(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_CLASS_TEMPLATE:
				mapPDOMCPPClassTemplate(node,depth,owner,ownerVariable,debug);	
				break;
			case IIndexCPPBindingConstants.CPP_CLASS_TEMPLATE_PARTIAL_SPEC:
				mapPDOMCPPClassTemplatePartialSpecialization(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_CLASS_TEMPLATE_PARTIAL_SPEC_SPEC:
				Exceptions_log.println("Missing case: CPP_CLASS_TEMPLATE_PARTIAL_SPEC_SPEC"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_FUNCTION_INSTANCE:
				mapPDOMCPPFunctionInstance(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_METHOD_INSTANCE:
				mapPDOMCPPMethodInstance(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_CONSTRUCTOR_INSTANCE:
				Exceptions_log.println("Missing case: CPP_CONSTRUCTOR_INSTANCE"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_CLASS_INSTANCE:
				mapPDOMCPPClassInstance(node,depth,owner,ownerVariable,debug);	
				break;
			case IIndexCPPBindingConstants.CPP_TEMPLATE_TYPE_PARAMETER:
				mapPDOMCPPTemplateTypeParameter(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_TEMPLATE_TEMPLATE_PARAMETER:
				mapPDOMCPPTemplateTemplateParameter(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_TEMPLATE_NON_TYPE_PARAMETER:
				mapPDOMCPPTemplateNonTypeParameter(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_FIELD_SPECIALIZATION:
				mapPDOMCPPFieldSpecialization(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_FUNCTION_SPECIALIZATION:
			Exceptions_log.println("Missing case: CPP_CONSTRUCTOR_INSTANCE"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_METHOD_SPECIALIZATION:
				mapPDOMCPPMethodSpecialization(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_PARAMETER_SPECIALIZATION:
				mapPDOMCPPParameterSpecialization(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_CONSTRUCTOR_SPECIALIZATION:
				Exceptions_log.println("Missing case: CPP_CONSTRUCTOR_SPECIALIZATION"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_CLASS_SPECIALIZATION:
				mapPDOMCPPClassSpecialization(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_FUNCTION_TEMPLATE_SPECIALIZATION:
				Exceptions_log.println("Missing case: CPP_FUNCTION_TEMPLATE_SPECIALIZATION"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_METHOD_TEMPLATE_SPECIALIZATION:
				mapPDOMCPPMethodTemplateSpecialization(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_CONSTRUCTOR_TEMPLATE_SPECIALIZATION:
				Exceptions_log.println("Missing case: CPP_CONSTRUCTOR_TEMPLATE_SPECIALIZATION"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_CLASS_TEMPLATE_SPECIALIZATION:
				Exceptions_log.println("Missing case: CPP_CLASS_TEMPLATE_SPECIALIZATION"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_TYPEDEF_SPECIALIZATION:
				mapPDOMCPPTypedefSpecialization(node,depth,owner,ownerVariable,debug);
				break;
			case IIndexCPPBindingConstants.CPP_USING_DECLARATION_SPECIALIZATION:
				Exceptions_log.println("Missing case: CPP_USING_DECLARATION_SPECIALIZATION"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_ALIAS_TEMPLATE:
				mapPDOMCPPAliasTemplate(node,depth,owner,ownerVariable,debug);	
				break;
			case IIndexCPPBindingConstants.CPP_ALIAS_TEMPLATE_INSTANCE:
				Exceptions_log.println("Missing case: CPP_ALIAS_TEMPLATE_INSTANCE"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_ENUMERATION_SPECIALIZATION:
				Exceptions_log.println("Missing case: CPP_ENUMERATION_SPECIALIZATION"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_ENUMERATOR_SPECIALIZATION:
				Exceptions_log.println("Missing case: CPP_ENUMERATOR_SPECIALIZATION"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_VARIABLE_TEMPLATE:
				Exceptions_log.println("Missing case: CPP_VARIABLE_TEMPLATE"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_FIELD_TEMPLATE:
				Exceptions_log.println("Missing case: CPP_FIELD_TEMPLATE"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_VARIABLE_INSTANCE:
				Exceptions_log.println("Missing case: CPP_VARIABLE_INSTANCE"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_FIELD_INSTANCE:
				Exceptions_log.println("Missing case: CPP_FIELD_INSTANCE"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_VARIABLE_TEMPLATE_PARTIAL_SPECIALIZATION:
				Exceptions_log.println("Missing case: CPP_VARIABLE_TEMPLATE_PARTIAL_SPECIALIZATION"); //$NON-NLS-1$
				break;
			case IIndexCPPBindingConstants.CPP_FIELD_TEMPLATE_PARTIAL_SPECIALIZATION:
				Exceptions_log.println("Missing case: CPP_FIELD_TEMPLATE_PARTIAL_SPECIALIZATION"); //$NON-NLS-1$
				break;
			default:
				Exceptions_log.println("Missing case in PDOMNode: " + node.getClass()); //$NON-NLS-1$
			}
		}
		else if(node instanceof CPPUnknownBinding) {
			
			if(node instanceof PDOMCPPDeferredClassInstance) {
				mapPDOMCPPDeferredClassInstance(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof PDOMCPPUnknownMemberClassInstance){	
				mapPDOMCPPUnknownMemberClassInstance(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof PDOMCPPUnknownMemberClass){
				mapPDOMCPPUnknownMemberClass(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof PDOMCPPUnknownMethod){	
				mapPDOMCPPUnknownMethod(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof TypeOfDependentExpression){
				mapTypeOfDependentExpression(node,depth,owner,ownerVariable,debug);	
			}else {
				Exceptions_log.println("Missing case in   CPPUnknownBinding: " + node); //$NON-NLS-1$
			}
				
		}else {		
			
			if(node instanceof PDOMCPPClassScope){
				mapPDOMCPPClassScope(node,depth,owner,ownerVariable,debug);			
			}else if(node instanceof PDOMCPPBase){
				mapPDOMCPPBase(node,depth,owner,ownerVariable,debug);			
			}else if(node instanceof PDOMCPPClassSpecializationScope){
				mapPDOMCPPClassSpecializationScope(node,depth,owner,ownerVariable,debug);			
			}else if(node instanceof PDOMCPPGlobalScope){
				mapPDOMCPPGlobalScope(node,depth,owner,ownerVariable,debug);
				
				//------------------CPPTypes-----------------------//
			}else if(node instanceof CPPArrayType){
				mapCPPArrayType(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPBasicType){
				mapCPPBasicType(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPClassInstance){
				mapCPPClassInstance(node,depth,owner,ownerVariable,debug);	
			}else if(node instanceof CPPClassSpecializationScope){
				mapCPPClassSpecializationScope(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPConstructorSpecialization){
				mapCPPConstructorSpecialization(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPFieldSpecialization){
				mapCPPFieldSpecialization(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPFunctionType){
				mapCPPFunctionType(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPMethodSpecialization){
				mapCPPMethodSpecialization(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPParameterPackType){
				mapCPPParameterPackType(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPParameterSpecialization){
				mapCPPParameterSpecialization(node,depth,owner,ownerVariable,debug);		
			}else if(node instanceof CPPPointerType){
				mapCPPPointerType(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPQualifierType){
				mapCPPQualifierType(node,depth,owner,ownerVariable,debug);	
			}else if(node instanceof CPPReferenceType){
				mapCPPReferenceType(node,depth,owner,ownerVariable,debug);		
			}else if(node instanceof CPPTemplateTypeArgument){
				mapCPPTemplateTypeArgument(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof CPPTemplateNonTypeArgument){
				mapCPPTemplateNonTypeArgument(node,depth,owner,ownerVariable,debug);		
			}else if(node instanceof CPPTypedefSpecialization){
				mapCPPTypedefSpecialization(node,depth,owner,ownerVariable,debug);
				
				//------------------ProblemTypes-----------------------//
			}else if(node instanceof ProblemType){
				mapProblemType(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof ProblemBinding){
				mapProblemBinding(node,depth,owner,ownerVariable,debug);
			}else if(node instanceof PDOMCPPUnknownScope || node instanceof EvalBinding || node instanceof EvalFixed){
				//ignore
			}else {
				Exceptions_log.println("Missing case in Default " + node.getClass()); //$NON-NLS-1$
			}
			
		}	
	}
	


	private void mapCNode(Object node, int depth, int owner,String ownerVariable, boolean debug) throws SQLException {
		
		if(node == null || depth == this.RECDEPTH)
			return;
		
		this.mappedSymbols = this.mappedSymbols +1;
		
		if(node instanceof PDOMCEnumeration) {
			mapPDOMCEnumeration(node, depth, owner,ownerVariable,debug);
		}else if (node instanceof PDOMCEnumerator) {
			mapPDOMCEnumerator(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof PDOMCField) { //extends PDOMCVariable
			mapPDOMCField(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof PDOMCVariable) {		
			mapPDOMCVariable(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof PDOMCFunction) {
			mapPDOMCFunction(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof PDOMCParameter) {
			mapPDOMCParameter(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof PDOMCStructure) {
			mapPDOMCStructure(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof PDOMCTypedef) {
			mapPDOMCTypedef(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof CArrayType) {
			mapCArrayType(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof CBasicType) {
			mapCBasicType(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof CFunctionType) {
			mapCFunctionType(node, depth, owner,ownerVariable,debug);			
		}else if(node instanceof CPointerType) {
			mapCPointerType(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof CQualifierType) {		
			mapCQualifierType(node, depth, owner,ownerVariable,debug);
		}else if(node instanceof ProblemType) {
			mapProblemType(node, depth, owner,ownerVariable,debug);
		}else {
			String nodeClass = ""; //$NON-NLS-1$
			try { nodeClass = node.getClass().getSimpleName();}catch(NullPointerException exc) {}
			Exceptions_log.println("Missing Node in CCase: " + nodeClass + ":" + node); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}	
	
	private void mapPDOMCPPAliasTemplate(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPAliasTemplate cppnode = (PDOMCPPAliasTemplate) node;			
		
		int ID = (int) cppnode.getRecord();
		String 						name = null;
		IPDOMCPPTemplateParameter[] templateParameters  = null;
		IType 						templateType = null;			
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{templateParameters  = cppnode.getTemplateParameters();}catch(NullPointerException exc) { }
		try{templateType = cppnode.getType();}catch(NullPointerException exc) { }
			
		String templateTypeTable = ""; //$NON-NLS-1$
		try{templateTypeTable = templateType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int templateTypeTableSubId = 0;		
		try {templateTypeTableSubId = (int) ((PDOMNode) templateType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		
		if(controll.InsertPDOMCPPAliasTemplate(ID, name,owner,ownerVariable,templateTypeTable,templateTypeTableSubId, debug))
			return;
					
		if(templateParameters != null)
		for(int i = 0; i < templateParameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateParameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIPDOMCPPTemplateParameter(ArrayID, ID, "templateParameters", templateParameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateParameters[i], depth+1, ArrayID,"templateParameters", debug); //$NON-NLS-1$
		}			
					
		mapCPPNode(templateType, depth+1, ID,"templateType", debug);	 //$NON-NLS-1$
	}
		
	private void mapPDOMCPPClassInstance(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {	

		PDOMCPPClassInstance cppnode = (PDOMCPPClassInstance) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;			
		ICPPBase[] 				bases = null;
		ICPPClassScope			compositeScope = null;
		ICPPConstructor[] 		constructors = null;
		ICPPField[] 			declaredFields = null;
		ICPPMethod[] 			declaredMethods = null;
		IField[] 				fields = null;
		ICPPMethod[] 			methods = null;
		ICPPClassType[]		 	nestedClasses = null;
		ICPPClassType 			specializedBinding = null;
		ICPPUsingDeclaration[] 	usingDeclarations = null;	
		ICPPTemplateArgument[] 	templateArguments = null;
		ICPPTemplateDefinition 	templateDefinition = null;
		int 					isFinal = 0;
		int 					isAnonymous = 0;	
		int 					isExplicitSpecialization = 0;
			
		try{name = cppnode.getName();}catch(NullPointerException exc) { }	
		try{bases = cppnode.getBases();}catch(NullPointerException exc) { }	
		try{compositeScope = cppnode.getCompositeScope();}catch(NullPointerException exc) { }	
		try{constructors = cppnode.getConstructors();}catch(NullPointerException exc) { }	
		try{declaredFields = cppnode.getDeclaredFields();}catch(NullPointerException exc) { }	
		try{declaredMethods = cppnode.getAllDeclaredMethods();}catch(NullPointerException exc) { }	
		try{fields = cppnode.getFields();}catch(NullPointerException exc) { }	
		try{methods = cppnode.getMethods();}catch(NullPointerException exc) { }	
		try{nestedClasses = cppnode.getNestedClasses();}catch(NullPointerException exc) { }	
		try{specializedBinding = cppnode.getSpecializedBinding();}catch(NullPointerException exc) { }	
		try{usingDeclarations = cppnode.getUsingDeclarations();}catch(NullPointerException exc) { }				
		try{templateArguments  = cppnode.getTemplateArguments();}catch(NullPointerException exc) { }	
		try{templateDefinition = cppnode.getTemplateDefinition();}catch(NullPointerException exc) { }	
		try{isFinal = cppnode.isFinal()? 1:0;}catch(NullPointerException exc) { }	
		try{isAnonymous = cppnode.isAnonymous()? 1:0;	}catch(NullPointerException exc) { }
		try{isExplicitSpecialization = cppnode.isExplicitSpecialization()?1:0;}catch(NullPointerException exc) { }	
		
		String compositeScopeTable = ""; //$NON-NLS-1$
		try{compositeScopeTable = compositeScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String specializedBindingTable = ""; //$NON-NLS-1$
		try{specializedBindingTable = specializedBinding.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String templateDefinitionTable = ""; //$NON-NLS-1$
		try{templateDefinitionTable = templateDefinition.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		
		int compositeScopeTableSubId = 0;	
		int specializedBindingTableSubId = 0;	
		int templateDefinitionTableSubId = 0;	
		try {compositeScopeTableSubId = (int) ((PDOMNode) compositeScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		try {specializedBindingTableSubId = (int) ((PDOMNode) specializedBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		try {templateDefinitionTableSubId = (int) ((PDOMNode) templateDefinition).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPClassInstance(ID, name,owner,ownerVariable,
				compositeScopeTable,compositeScopeTableSubId,
				specializedBindingTable,specializedBindingTableSubId,
				templateDefinitionTable,templateDefinitionTableSubId,
				isFinal, isAnonymous,isExplicitSpecialization,debug
				))
			return;
		
		if(bases != null)
		for(int i = 0; i < bases.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) bases[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPBase(ArrayID, ID, "bases", bases[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(bases[i], depth+1, ArrayID,"bases", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(compositeScope, depth+1, ID,"compositeScope", debug);			 //$NON-NLS-1$
		
		if(constructors != null)
		for(int i = 0; i < constructors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constructors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constructors[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(constructors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}
		
		
		if(declaredFields != null)
		for(int i = 0; i < declaredFields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredFields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPField(ArrayID, ID, "declaredFields", declaredFields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredFields[i], depth+1, ArrayID,"declaredFields", debug); //$NON-NLS-1$
		}
		
		if(declaredMethods != null)
		for(int i = 0; i < declaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "declaredMethods", declaredMethods[i].getClass().getSimpleName(), tableRefSubId,i,debug); //$NON-NLS-1$
			mapCPPNode(declaredMethods[i], depth+1, ArrayID,"declaredMethods", debug); //$NON-NLS-1$
		}
		
		if(fields != null)
		for(int i = 0; i < fields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) fields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIField(ArrayID, ID, "fields", fields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(fields[i], depth+1, ArrayID,"fields", debug); //$NON-NLS-1$
		}
		
		if(methods != null)
		for(int i = 0; i < methods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) methods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "methods", methods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(methods[i], depth+1, ArrayID,"methods", debug); //$NON-NLS-1$
		}
			
		if(nestedClasses != null)
		for(int i = 0; i < nestedClasses.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) nestedClasses[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassType(ArrayID, ID, "nestedClasses", nestedClasses[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(nestedClasses[i], depth+1, ArrayID,"nestedClasses", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(specializedBinding, depth+1, ID,"specializedBinding", debug); //$NON-NLS-1$
			
		if(usingDeclarations != null)
		for(int i = 0; i < usingDeclarations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) usingDeclarations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPUsingDeclaration(ArrayID, ID, "usingDeclarations", usingDeclarations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(usingDeclarations[i], depth+1, ArrayID,"usingDeclarations", debug); //$NON-NLS-1$
		}
		
		if(templateArguments != null)
		for(int i = 0; i < templateArguments.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateArguments[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateArgument(ArrayID, ID, "templateArguments", templateArguments[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateArguments[i], depth+1, ArrayID,"templateArguments", debug); //$NON-NLS-1$
		}
					
		mapCPPNode(templateDefinition, depth+1, ID,"templateDefinition", debug);		 //$NON-NLS-1$			
	}
	
	private void mapPDOMCPPClassType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {	
		
		PDOMCPPClassType cppnode = (PDOMCPPClassType) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPBase[] 				bases = null;
		ICPPConstructor[] 		constructors = null;
		ICPPClassScope 			compositeScope = null;
		ICPPField[] 			declaredFields = null;
		ICPPMethod[] 			declaredMethods = null;
		ICPPMethod[] allDeclaredMethods = null;
		IField[] 				fields = null;
		ICPPMethod[] 			methods = null;
		ICPPClassType[] 		nestedClasses = null;
		ICPPUsingDeclaration[] 	usingDeclarations = null;
		int 					isFinal = 0;
		int 					isAnonymous = 0;
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }	
		try{bases = cppnode.getBases();	}catch(NullPointerException exc) { }	
		try{constructors = cppnode.getConstructors();	}catch(NullPointerException exc) { }	
		try{compositeScope = cppnode.getCompositeScope();	}catch(NullPointerException exc) { }	
		try{declaredFields = cppnode.getDeclaredFields();	}catch(NullPointerException exc) { }	
		try{declaredMethods = cppnode.getDeclaredMethods();	}catch(NullPointerException exc) { }	
		try{allDeclaredMethods = cppnode.getAllDeclaredMethods();		}catch(NullPointerException exc) { }	
		try{fields = cppnode.getFields();	}catch(NullPointerException exc) { }	
		try{methods = cppnode.getMethods();	}catch(NullPointerException exc) { }	
		try{nestedClasses = cppnode.getNestedClasses();	}catch(NullPointerException exc) { }	
		try{usingDeclarations = cppnode.getUsingDeclarations();	}catch(NullPointerException exc) { }	
		try{isFinal = cppnode.isFinal()? 1:0;	}catch(NullPointerException exc) { }	
		try{isAnonymous = cppnode.isAnonymous()? 1:0;		}catch(NullPointerException exc) { }
		
		String compositeScopeTable = ""; //$NON-NLS-1$
		try{compositeScopeTable = compositeScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int compositeScopeTableSubId = 0;	
		try {compositeScopeTableSubId = (int) ((PDOMNode) compositeScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPClassType(ID, name,owner,ownerVariable,
				compositeScopeTable,compositeScopeTableSubId,
				isFinal, isAnonymous,debug))
			return;		
		
		if(bases != null)
		for(int i = 0; i < bases.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) bases[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPBase(ArrayID, ID, "bases", bases[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(bases[i], depth+1, ArrayID,"bases", debug); //$NON-NLS-1$
		}
					
		if(constructors != null)
		for(int i = 0; i < constructors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constructors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constructors[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(constructors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(compositeScope, depth+1, ID,"compositeScope", debug); //$NON-NLS-1$

		if(declaredFields != null)
		for(int i = 0; i < declaredFields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredFields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPField(ArrayID, ID, "declaredFields", declaredFields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredFields[i], depth+1, ArrayID,"declaredFields", debug); //$NON-NLS-1$
		}
		
		if(declaredMethods != null)
		for(int i = 0; i < declaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "declaredMethods", declaredMethods[i].getClass().getSimpleName(), tableRefSubId,i,debug); //$NON-NLS-1$
			mapCPPNode(declaredMethods[i], depth+1, ArrayID,"declaredMethods", debug); //$NON-NLS-1$
		}
		
		if(allDeclaredMethods != null)
		for(int i = 0; i < allDeclaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allDeclaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "allDeclaredMethods", allDeclaredMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allDeclaredMethods[i], depth+1, ArrayID,"allDeclaredMethods", debug); //$NON-NLS-1$
		}
		
		if(fields != null)
		for(int i = 0; i < fields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) fields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIField(ArrayID, ID, "fields", fields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(fields[i], depth+1, ArrayID,"fields", debug); //$NON-NLS-1$
		}
		
		if(methods != null)
		for(int i = 0; i < methods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) methods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "methods", methods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(methods[i], depth+1, ArrayID,"methods", debug); //$NON-NLS-1$
		}
		
		if(nestedClasses != null)
		for(int i = 0; i < nestedClasses.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) nestedClasses[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassType(ArrayID, ID, "nestedClasses", nestedClasses[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(nestedClasses[i], depth+1, ArrayID,"nestedClasses", debug); //$NON-NLS-1$
		}
			
		if(usingDeclarations != null)
		for(int i = 0; i < usingDeclarations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) usingDeclarations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPUsingDeclaration(ArrayID, ID, "usingDeclarations", usingDeclarations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(usingDeclarations[i], depth+1, ArrayID,"usingDeclarations", debug); //$NON-NLS-1$
		}		
	}
	
	private void mapPDOMCPPClassSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPClassSpecialization cppnode = (PDOMCPPClassSpecialization) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPBase[] 				bases = null;
		ICPPClassScope			compositeScope = null;
		ICPPConstructor[] 		constructors = null;
		ICPPField[] 			declaredFields = null;
		ICPPMethod[] 			declaredMethods = null;
		ICPPMethod[] 			allDeclaredmethods = null;
		IField[] 				fields = null;
		ICPPMethod[] 			methods = null;
		ICPPClassType[]		 	nestedClasses = null;
		ICPPClassType 			specializedBinding = null;
		ICPPUsingDeclaration[] 	usingDeclarations = null;
		int 					isFinal = 0;
		int 					isAnonymous = 0;
		
		try {name = cppnode.getName();}catch(NullPointerException exc) { }
		try {bases = cppnode.getBases();}catch(NullPointerException exc) { }
		try {compositeScope = cppnode.getCompositeScope();}catch(NullPointerException exc) { }
		try {constructors = cppnode.getConstructors();}catch(NullPointerException exc) { }
		try {declaredFields = cppnode.getDeclaredFields();}catch(NullPointerException exc) { }
		try {declaredMethods = cppnode.getDeclaredMethods();}catch(NullPointerException exc) { }
		try {fields = cppnode.getFields();}catch(NullPointerException exc) { }
		try {methods = cppnode.getMethods();}catch(NullPointerException exc) { }
		try {allDeclaredmethods = cppnode.getAllDeclaredMethods();}catch(NullPointerException exc) { }
		try {nestedClasses = cppnode.getNestedClasses();}catch(NullPointerException exc) { }
		try {specializedBinding = cppnode.getSpecializedBinding();}catch(NullPointerException exc) { }
		try {usingDeclarations = cppnode.getUsingDeclarations();}catch(NullPointerException exc) { }
		try {isFinal = cppnode.isFinal()? 1:0;}catch(NullPointerException exc) { }
		try {isAnonymous = cppnode.isAnonymous()? 1:0;}catch(NullPointerException exc) { }
		
		String compositeScopeTable = ""; //$NON-NLS-1$
		try{compositeScopeTable = compositeScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String specializedBindingTable = ""; //$NON-NLS-1$
		try{specializedBindingTable = specializedBinding.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int compositeScopeTableSubId = 0;	
		try {compositeScopeTableSubId = (int) ((PDOMNode) compositeScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int specializedBindingTableSubId = 0;	
		try {specializedBindingTableSubId = (int) ((PDOMNode) specializedBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		
		if(controll.InsertPDOMCPPClassSpecialization(ID, name,owner,ownerVariable,
				compositeScopeTable,compositeScopeTableSubId,
				specializedBindingTable,specializedBindingTableSubId,
				isFinal,isAnonymous,debug))
			return;
		
		if(bases != null)
		for(int i = 0; i < bases.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) bases[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPBase(ArrayID, ID, "bases", bases[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(bases[i], depth+1, ArrayID,"bases", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(compositeScope, depth+1, ID,"compositeScope", debug); //$NON-NLS-1$
		
		if(constructors != null)
		for(int i = 0; i < constructors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constructors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constructors[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(constructors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}
		
		if(declaredFields != null)
		for(int i = 0; i < declaredFields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredFields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPField(ArrayID, ID, "declaredFields", declaredFields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredFields[i], depth+1, ArrayID,"declaredFields", debug); //$NON-NLS-1$
		}
		
		if(declaredMethods != null)
		for(int i = 0; i < declaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "declaredMethods", declaredMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredMethods[i], depth+1, ArrayID,"declaredMethods", debug); //$NON-NLS-1$
		}
		
		if(fields != null)
		for(int i = 0; i < fields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) fields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIField(ArrayID, ID, "fields", fields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(fields[i], depth+1, ArrayID,"fields", debug); //$NON-NLS-1$
		}
		
		if(methods != null)
		for(int i = 0; i < methods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) methods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "methods", methods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(methods[i], depth+1, ArrayID,"methods", debug); //$NON-NLS-1$
		}
		
		if(allDeclaredmethods != null)
		for(int i = 0; i < allDeclaredmethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allDeclaredmethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "allDeclaredmethods", allDeclaredmethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allDeclaredmethods[i], depth+1, ArrayID,"allDeclaredmethods", debug); //$NON-NLS-1$
		}
		
		if(nestedClasses != null)			
		for(int i = 0; i < nestedClasses.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) nestedClasses[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassType(ArrayID, ID, "nestedClasses", nestedClasses[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(nestedClasses[i], depth+1, ArrayID,"nestedClasses", debug); //$NON-NLS-1$
		}
			
		mapCPPNode(specializedBinding, depth+1, ID,"specializedBinding", debug); //$NON-NLS-1$
		 
		if(usingDeclarations != null)
		for(int i = 0; i < usingDeclarations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) usingDeclarations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPUsingDeclaration(ArrayID, ID, "usingDeclarations", usingDeclarations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(usingDeclarations[i], depth+1, ArrayID,"usingDeclarations", debug); //$NON-NLS-1$
		}						
	}
	
	private void mapPDOMCPPClassTemplate(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPClassTemplate cppnode = (PDOMCPPClassTemplate) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPBase[] 									bases = null; //Basisklasse
		ICPPConstructor[] 							constructors = null;
		ICPPClassScope 								compositeScope = null;
		ICPPField[] 								declaredFields = null;
		ICPPMethod[] 								declaredMethods = null;
		ICPPMethod[] 								allDeclaredMethods = null;
		IField[] 									fields = null;
		ICPPMethod[] 								methods = null;
		ICPPClassType[] 							nestedClasses = null;
		ICPPTemplateInstance[] 						allInstances = null;
		ICPPClassTemplatePartialSpecialization[] 	partialSpecializations = null;
		ICPPTemplateParameter[] templateParameters = null;
		ICPPUsingDeclaration[] 						usingDeclarations = null;
		int isAnonymous = 0;
		int isFinal =0;
		
		try {name = cppnode.getName();}catch(NullPointerException exc) { }
		try {bases = cppnode.getBases();}catch(NullPointerException exc) { }
		try {compositeScope = cppnode.getCompositeScope();}catch(NullPointerException exc) { }
		try {constructors = cppnode.getConstructors();}catch(NullPointerException exc) { }
		try {declaredFields = cppnode.getDeclaredFields();}catch(NullPointerException exc) { }
		try {declaredMethods = cppnode.getDeclaredMethods();}catch(NullPointerException exc) { }
		try {allDeclaredMethods = cppnode.getAllDeclaredMethods();}catch(NullPointerException exc) { }
		try {fields = cppnode.getFields();}catch(NullPointerException exc) { }
		try {methods = cppnode.getMethods();}catch(NullPointerException exc) { }
		try {nestedClasses = cppnode.getNestedClasses();}catch(NullPointerException exc) { }
		try {allInstances = cppnode.getAllInstances();}catch(NullPointerException exc) { }
		try {partialSpecializations = cppnode.getPartialSpecializations();}catch(NullPointerException exc) { }
		try {templateParameters = cppnode.getTemplateParameters();}catch(NullPointerException exc) { }
		try {usingDeclarations = cppnode.getUsingDeclarations();}catch(NullPointerException exc) { }
		try {isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) { }
		try {isAnonymous = cppnode.isAnonymous()?1:0;}catch(NullPointerException exc) { }

		String compositeScopeTable = ""; //$NON-NLS-1$
		try{compositeScopeTable = compositeScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int compositeScopeTableSubId = 0;	
		try {compositeScopeTableSubId = (int) ((PDOMNode) compositeScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPClassTemplate(ID, name,owner,ownerVariable,
				compositeScopeTable,compositeScopeTableSubId,isFinal,isAnonymous,debug
				))
			return;
		
		if(bases != null)
		for(int i = 0; i < bases.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) bases[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPBase(ArrayID, ID, "bases", bases[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(bases[i], depth+1, ArrayID,"bases", debug); //$NON-NLS-1$
		}
		
		if(constructors != null)
		for(int i = 0; i < constructors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constructors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constructors[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(constructors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(compositeScope, depth+1, ID,"compositeScope", debug); //$NON-NLS-1$
		
		if(declaredFields != null)
		for(int i = 0; i < declaredFields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredFields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPField(ArrayID, ID, "declaredFields", declaredFields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredFields[i], depth+1, ArrayID,"declaredFields", debug); //$NON-NLS-1$
		}
		
		if(declaredMethods != null)
		for(int i = 0; i < declaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "declaredMethods", declaredMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredMethods[i], depth+1, ArrayID,"declaredMethods", debug); //$NON-NLS-1$
		}
		
		if(allDeclaredMethods != null)
		for(int i = 0; i < allDeclaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allDeclaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "allDeclaredMethods", allDeclaredMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allDeclaredMethods[i], depth+1, ArrayID,"allDeclaredMethods", debug); //$NON-NLS-1$
		}
		
		if(fields != null)
		for(int i = 0; i < fields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) fields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIField(ArrayID, ID, "fields", fields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(fields[i], depth+1, ArrayID,"fields", debug); //$NON-NLS-1$
		}
		
		if(methods != null)
		for(int i = 0; i < methods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) methods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "methods", methods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(methods[i], depth+1, ArrayID,"methods", debug); //$NON-NLS-1$
		}
		
		if(nestedClasses != null)			
		for(int i = 0; i < nestedClasses.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) nestedClasses[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassType(ArrayID, ID, "nestedClasses", nestedClasses[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(nestedClasses[i], depth+1, ArrayID,"nestedClasses", debug); //$NON-NLS-1$
		}
			
		if(allInstances != null)
		for(int i = 0; i < allInstances.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allInstances[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateInstance(ArrayID, ID, "allInstances", allInstances[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allInstances[i], depth+1, ArrayID,"allInstances", debug); //$NON-NLS-1$
		}
		
		if(partialSpecializations != null)
		for(int i = 0; i < partialSpecializations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) partialSpecializations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassTemplatePartialSpecialization(ArrayID, ID, "partialSpecializations", partialSpecializations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(partialSpecializations[i], depth+1, ArrayID,"partialSpecializations", debug); //$NON-NLS-1$
		}
		
		if(templateParameters != null)
		for(int i = 0; i < templateParameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateParameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "templateParameters", templateParameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateParameters[i], depth+1, ArrayID,"templateParameters", debug); //$NON-NLS-1$
		}
				
		if(usingDeclarations != null)
		for(int i = 0; i < usingDeclarations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) usingDeclarations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPUsingDeclaration(ArrayID, ID, "usingDeclarations", usingDeclarations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(usingDeclarations[i], depth+1, ArrayID,"usingDeclarations", debug); //$NON-NLS-1$
		}				
	}
	
	private void mapPDOMCPPClassTemplatePartialSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPClassTemplatePartialSpecialization cppnode = (PDOMCPPClassTemplatePartialSpecialization) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPBase[] 									bases = null;
		ICPPConstructor[] 							constructors = null;
		ICPPClassScope 								compositeScope = null;
		ICPPField[] 								declaredFields = null;
		ICPPMethod[] 								declaredMethods = null;
		ICPPMethod[] 								allDeclaredMethods = null;
		IField[] 									fields = null;
		ICPPMethod[] 								methods = null;
		ICPPClassType[] 							nestedClasses = null;
		ICPPTemplateInstance[] 						allInstances = null;
		ICPPClassTemplatePartialSpecialization[] 	partialSpecializations = null;
		ICPPUsingDeclaration[] 						usingDeclarations = null;		
		ICPPTemplateArgument[] 						templateArguments = null;
		ICPPTemplateParameter[] templateParameters = null;
		ICPPClassTemplate primaryClassTemplate = null;
		ICPPTemplateDefinition primaryTemplate = null;
		int isAnonymous = 0;
		int isFinal = 0;		

		try {name = cppnode.getName();}catch(NullPointerException exc) { }
		try {bases = cppnode.getBases();}catch(NullPointerException exc) { }
		try {compositeScope = cppnode.getCompositeScope();}catch(NullPointerException exc) { }
		try {constructors = cppnode.getConstructors();}catch(NullPointerException exc) { }
		try {declaredFields = cppnode.getDeclaredFields();}catch(NullPointerException exc) { }
		try {declaredMethods = cppnode.getDeclaredMethods();}catch(NullPointerException exc) { }
		try {allDeclaredMethods = cppnode.getAllDeclaredMethods();}catch(NullPointerException exc) { }
		try {fields = cppnode.getFields();}catch(NullPointerException exc) { }
		try {methods = cppnode.getMethods();}catch(NullPointerException exc) { }
		try {nestedClasses = cppnode.getNestedClasses();}catch(NullPointerException exc) { }
		try {allInstances = cppnode.getAllInstances();}catch(NullPointerException exc) { }
		try {partialSpecializations = cppnode.getPartialSpecializations();}catch(NullPointerException exc) { }
		try {usingDeclarations = cppnode.getUsingDeclarations();}catch(NullPointerException exc) { }
		try {templateArguments = cppnode.getTemplateArguments();}catch(NullPointerException exc) { }		
		try {templateParameters = cppnode.getTemplateParameters();}catch(NullPointerException exc) { }		
		try {primaryClassTemplate = cppnode.getPrimaryClassTemplate();}catch(NullPointerException exc) { }
		try {primaryTemplate = cppnode.getPrimaryTemplate();}catch(NullPointerException exc) { }		
		try {isAnonymous = cppnode.isAnonymous()?1:0;}catch(NullPointerException exc) { }
		try {isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) { }
		
		String compositeScopeTable = ""; //$NON-NLS-1$
		try{compositeScopeTable = compositeScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String primaryClassTemplateTable = ""; //$NON-NLS-1$
		try{primaryClassTemplateTable = primaryClassTemplate.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String primaryTemplateTable = ""; //$NON-NLS-1$
		try{primaryTemplateTable = primaryTemplate.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int compositeScopeTableSubId = 0;	
		try {compositeScopeTableSubId = (int) ((PDOMNode) compositeScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int primaryClassTemplateTableSubId = 0;	
		try {primaryClassTemplateTableSubId = (int) ((PDOMNode) primaryClassTemplate).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int primaryTemplateTableSubId = 0;	
		try {primaryTemplateTableSubId = (int) ((PDOMNode) primaryTemplate).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPClassTemplatePartialSpecialization(ID, name,owner,ownerVariable,
				compositeScopeTable, compositeScopeTableSubId,primaryClassTemplateTable,primaryClassTemplateTableSubId,primaryTemplateTable,primaryTemplateTableSubId,isAnonymous,isFinal, debug))
			return;
		
		if(bases != null)
		for(int i = 0; i < bases.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) bases[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPBase(ArrayID, ID, "bases", bases[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(bases[i], depth+1, ID,"bases", debug); //$NON-NLS-1$
		}
			
		if(constructors != null)
		for(int i = 0; i < constructors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constructors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constructors[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCPPNode(constructors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}
			
		mapCPPNode(compositeScope, depth+1, ID,"compositeScope", debug); //$NON-NLS-1$
			
		if(declaredFields != null)
		for(int i = 0; i < declaredFields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredFields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPField(ArrayID, ID, "declaredFields", declaredFields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredFields[i], depth+1, ArrayID,"declaredFields", debug); //$NON-NLS-1$
		}
			
		if(declaredMethods != null)
		for(int i = 0; i < declaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "declaredMethods", declaredMethods[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCPPNode(declaredMethods[i], depth+1, ArrayID,"declaredMethods", debug); //$NON-NLS-1$
		}
		
		if(allDeclaredMethods != null)
		for(int i = 0; i < allDeclaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allDeclaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "allDeclaredMethods", allDeclaredMethods[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCPPNode(allDeclaredMethods[i], depth+1, ArrayID,"allDeclaredMethods", debug); //$NON-NLS-1$
		}
			
		if(fields != null)
		for(int i = 0; i < fields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) fields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIField(ArrayID, ID, "fields", fields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(fields[i], depth+1, ArrayID,"fields", debug); //$NON-NLS-1$
		}
			
		if(methods != null)
		for(int i = 0; i < methods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) methods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "methods", methods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(methods[i], depth+1, ArrayID,"methods", debug); //$NON-NLS-1$
		}
			
		if(nestedClasses != null)			
		for(int i = 0; i < nestedClasses.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) nestedClasses[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassType(ArrayID, ID, "nestedClasses", nestedClasses[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(nestedClasses[i], depth+1, ArrayID,"nestedClasses", debug); //$NON-NLS-1$
		}
				
		if(allInstances != null)
		for(int i = 0; i < allInstances.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allInstances[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateInstance(ArrayID, ID, "allInstances", allInstances[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCPPNode(allInstances[i], depth+1, ArrayID,"allInstances", debug); //$NON-NLS-1$
		}
			
		if(partialSpecializations != null)
		for(int i = 0; i < partialSpecializations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) partialSpecializations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassTemplatePartialSpecialization(ArrayID, ID, "partialSpecializations", partialSpecializations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(partialSpecializations[i], depth+1, ArrayID,"partialSpecializations", debug); //$NON-NLS-1$
		}
						
		if(usingDeclarations != null)
		for(int i = 0; i < usingDeclarations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) usingDeclarations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPUsingDeclaration(ArrayID, ID, "usingDeclarations", usingDeclarations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(usingDeclarations[i], depth+1, ArrayID,"usingDeclarations", debug); //$NON-NLS-1$
		}

		if(templateArguments != null)
		for(int i = 0; i < templateArguments.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateArguments[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateArgument(ArrayID, ID, "templateArguments", templateArguments[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateArguments[i], depth+1, ArrayID,"templateArguments", debug); //$NON-NLS-1$
		}
		
		if(templateParameters != null)
		for(int i = 0; i < templateParameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateParameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "templateParameters", templateParameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateParameters[i], depth+1, ArrayID,"templateParameters", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(primaryClassTemplate, depth+1, ID,"primaryClassTemplate", debug); //$NON-NLS-1$
		
		mapCPPNode(primaryTemplate, depth+1, ID,"primaryTemplate", debug); //$NON-NLS-1$
		
	}
	
	private void mapPDOMCPPConstructor(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPConstructor cppnode = (PDOMCPPConstructor) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		ICPPFunctionType declaredType = null;
		IType[] exceptionSpecification = null;
		IScope functionScope = null;
		int hasParameterPack = 0;
		int 				requiredArgumentCount = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int 				isDestructor = 0;
		int 				isExplicit  = 0;
		int 				isFinal  = 0;
		int 				isImplicit  = 0;
		int 				isOverride  = 0;
		int 				isPureVirtual = 0;
		int 				isVirtual  = 0;		
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredType = cppnode.getDeclaredType();}catch(NullPointerException |  ClassCastException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{isDestructor = cppnode.isDestructor()?1:0;}catch(NullPointerException exc) { }
		try{isExplicit = cppnode.isExplicit()?1:0;}catch(NullPointerException exc) { }
		try{isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) { }
		try{isImplicit = cppnode.isImplicit()?1:0;}catch(NullPointerException exc) { }
		try{isOverride = cppnode.isOverride()?1:0;}catch(NullPointerException exc) { }
		try{isPureVirtual = cppnode.isPureVirtual()?1:0;}catch(NullPointerException exc) { }
		try{isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) { }		
		
		String declaredTypeTable = ""; //$NON-NLS-1$
		try{declaredTypeTable = declaredType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int declaredTypeTableSubId = 0;	
		try {declaredTypeTableSubId = (int) ((PDOMNode) declaredType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;	
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPConstructor(ID, name,owner,ownerVariable,
				typeTable,typeTableSubId,declaredTypeTable,declaredTypeTableSubId,functionScopeTable,functionScopeTableSubId,hasParameterPack,
				requiredArgumentCount,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
				isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual,debug))
			return;		
		
		mapCPPNode(declaredType, depth+1, ID,"declaredType", debug); //$NON-NLS-1$
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}
		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}					
	}
	
	private void mapPDOMCPPDeferredClassInstance(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		controll.InsertPDOMCPPDeferredClassInstance(owner, debug);	
	}

	private void mapPDOMCPPEnumeration(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		PDOMCPPEnumeration cppnode = (PDOMCPPEnumeration) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		IEnumerator[] 	enumerators = null;
		IType 			fixedType = null;
		long minValue = 0;
		long maxValue = 0;
		
		try{ name = cppnode.getName();}catch(NullPointerException exc) { }
		try{ enumerators = cppnode.getEnumerators();}catch(NullPointerException exc) { }
		try{ fixedType = cppnode.getFixedType();}catch(NullPointerException exc) { }
		try{ minValue = cppnode.getMinValue();}catch(NullPointerException exc) { }
		try{ maxValue = cppnode.getMaxValue();}catch(NullPointerException exc) { }
		
		String fixedTypeTable = ""; //$NON-NLS-1$
		try{fixedTypeTable = fixedType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int fixedTypeTableSubId = 0;	
		try {fixedTypeTableSubId = (int) ((PDOMNode) fixedType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPEnumeration(ID, name,owner,ownerVariable,
				fixedTypeTable,fixedTypeTableSubId,minValue,maxValue,debug))
			return;		
		
		if(enumerators != null)
		for(int i = 0; i < enumerators.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) enumerators[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIEnumerator(ArrayID, ID, "enumerators", enumerators[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(enumerators[i], depth+1, ArrayID,"enumerators", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(fixedType, depth+1, ID,"fixedType", debug); //$NON-NLS-1$		
	}
	
	@SuppressWarnings({ "deprecation"})
	private void mapPDOMCPPEnumerator(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPEnumerator cppnode = (PDOMCPPEnumerator) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		IType 	internalType = null;
		IType 	type = null;
		long 	value = 0;
		
		try{ name = cppnode.getName();}catch(NullPointerException exc) { }
		try{ internalType = cppnode.getInternalType();}catch(NullPointerException exc) { }
		try{ type = cppnode.getType();}catch(NullPointerException exc) { }
		try{ value = cppnode.getValue().numericalValue();}catch(NullPointerException exc) { }
		
		String internalTypeTable = ""; //$NON-NLS-1$
		try{internalTypeTable = internalType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int internalTypeTableSubId = 0;	
		try {internalTypeTableSubId = (int) ((PDOMNode) internalType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPEnumerator(ID, name,owner,ownerVariable,
				internalTypeTable,internalTypeTableSubId,
				typeTable,typeTableSubId,
				value,debug))
			return;
											
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		mapCPPNode(internalType, depth+1, ID,"internalType", debug); //$NON-NLS-1$		
	}
	
	@SuppressWarnings({ "deprecation"})
	private void mapPDOMCPPField(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPField cppnode = (PDOMCPPField) node;

		int ID = (int) cppnode.getRecord();
		String name = null;
		long 	value = 0;
		IType 	type = null;
		ICPPClassType 		classOwner = null;
		int 				fielsPosition = 0;	
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isMutable = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{value = cppnode.getInitialValue().numericalValue();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{classOwner = cppnode.getClassOwner();}catch(NullPointerException exc) { }
		try{fielsPosition = cppnode.getFieldPosition();	}catch(NullPointerException exc) { }
		
		String classOwnerTable = ""; //$NON-NLS-1$
		try{classOwnerTable = classOwner.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int classOwnerTableSubId = 0;	
		try {classOwnerTableSubId = (int) ((PDOMNode) classOwner).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPField(ID, name,owner,ownerVariable,
				value, 
				typeTable,typeTableSubId,
				classOwnerTable,classOwnerTableSubId,
				fielsPosition,
				isAuto, isConstExpr, isExtern, isExternC, isMutable, isRegister, isStatic,debug))
			return;
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		mapCPPNode(classOwner, depth+1, ID,"classOwner",debug); //$NON-NLS-1$		
	}
	
	@SuppressWarnings({"deprecation" })
	private void mapPDOMCPPFieldSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
	
		PDOMCPPFieldSpecialization cppnode = (PDOMCPPFieldSpecialization) node;

		int ID = (int) cppnode.getRecord();
		String name = null;
		long 	initialValue = 0;
		IType 	type = null;
		ICPPClassType classOwner = null;
		int 	fielsPosition = 0;	
		int 	isAuto = 0;
		int 	isConstExpr = 0;
		int 	isExtern = 0;
		int 	isExternC = 0;
		int 	isMutable = 0;
		int 	isRegister = 0;
		int 	isStatic = 0;			
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{initialValue = cppnode.getInitialValue().numericalValue();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{classOwner = cppnode.getClassOwner();}catch(NullPointerException exc) { }
		try{fielsPosition = cppnode.getFieldPosition();	}catch(NullPointerException exc) { }
		
		String classOwnerTable = ""; //$NON-NLS-1$
		try{classOwnerTable = classOwner.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int classOwnerTableSubId = 0;	
		try {classOwnerTableSubId = (int) ((PDOMNode) classOwner).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPFieldSpecialization(ID, name,owner,ownerVariable,
				initialValue, 
				typeTable,typeTableSubId,
				classOwnerTable,classOwnerTableSubId,
				fielsPosition,
				isAuto, isConstExpr, isExtern, isExternC, isMutable, isRegister, isStatic,debug))
			return;
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		mapCPPNode(classOwner, depth+1, ID,"classOwner",debug); //$NON-NLS-1$				
	}
	
	private void mapPDOMCPPFunction(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPFunction cppnode = (PDOMCPPFunction) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		ICPPFunctionType declaredType = null;
		IType[] exceptionSpecification = null;
		IScope functionScope = null;
		int hasParameterPack = 0;
		int 				requiredArgumentCount = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredType = cppnode.getDeclaredType();}catch(NullPointerException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }

		String declaredTypeTable = ""; //$NON-NLS-1$
		try{declaredTypeTable = declaredType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int declaredTypeTableSubId = 0;	
		try {declaredTypeTableSubId = (int) ((PDOMNode) declaredType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;	
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPFunction(ID, name,owner,ownerVariable,
				typeTable,typeTableSubId,declaredTypeTable,declaredTypeTableSubId,functionScopeTable,functionScopeTableSubId,hasParameterPack,
				requiredArgumentCount,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,debug))
			return;
		
		mapCPPNode(declaredType, depth+1, ID,"declaredType", debug); //$NON-NLS-1$
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}

		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}		
	}
	
	private void mapPDOMCPPFunctionInstance(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPFunctionInstance cppnode = (PDOMCPPFunctionInstance) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType declaredType = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		IScope				functionScope = null;
		IType[] exceptionSpecification = null;
		ICPPTemplateArgument[] templateArguments = null; 
		ICPPTemplateDefinition templateDefinition = null;
		int 				requiredArgumentCount = 0;
		int isExplicitSpecialization = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int hasParameterPack = 0;
		int takesVarArgs = 0;
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredType = cppnode.getDeclaredType();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }
		try{takesVarArgs = cppnode.takesVarArgs()?1:0;}catch(NullPointerException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{templateArguments = cppnode.getTemplateArguments(); }catch(NullPointerException exc) { }
		try{templateDefinition = cppnode.getTemplateDefinition();}catch(NullPointerException exc) { }
		try{isExplicitSpecialization = cppnode.isExplicitSpecialization()?1:0;}catch(NullPointerException exc) { }
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String templateDefinitionTable = ""; //$NON-NLS-1$
		try{templateDefinitionTable = templateDefinition.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String declaredTypeTable = ""; //$NON-NLS-1$
		try{declaredTypeTable = declaredType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;	
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int templateDefinitionTableSubId = 0;	
		try {templateDefinitionTableSubId = (int) ((PDOMNode) templateDefinition).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int declaredTypeTableSubId = 0;	
		try {declaredTypeTableSubId = (int) ((PDOMNode) declaredType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPFunctionInstance(ID, name,owner,ownerVariable,
				declaredTypeTable,declaredTypeTableSubId,
				typeTable,typeTableSubId,
				functionScopeTable,functionScopeTableSubId,
				templateDefinitionTable,templateDefinitionTableSubId,
				requiredArgumentCount,isExplicitSpecialization,
				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,hasParameterPack,takesVarArgs,debug))
			return;		
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		
		mapCPPNode(declaredType, depth+1, ID,"declaredType", debug); //$NON-NLS-1$
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}
		
		if(templateArguments != null)
		for(int i = 0; i < templateArguments.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateArguments[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateArgument(ArrayID, ID, "templateArguments", templateArguments[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateArguments[i], depth+1, ArrayID,"templateArguments", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(templateDefinition, depth+1, ID,"templateDefinition", debug); //$NON-NLS-1$		
	}
	
	private void mapPDOMCPPMethod(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPMethod cppnode = (PDOMCPPMethod) node;

		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		ICPPFunctionType declaredType = null;
		IType[] exceptionSpecification = null;
		IScope functionScope = null;
		int hasParameterPack = 0;
		int 				requiredArgumentCount = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int 				isDestructor = 0;
		int 				isExplicit  = 0;
		int 				isFinal  = 0;
		int 				isImplicit  = 0;
		int 				isOverride  = 0;
		int 				isPureVirtual = 0;
		int 				isVirtual  = 0;		
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredType = cppnode.getDeclaredType();}catch(NullPointerException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{isDestructor = cppnode.isDestructor()?1:0;}catch(NullPointerException exc) { }
		try{isExplicit = cppnode.isExplicit()?1:0;}catch(NullPointerException exc) { }
		try{isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) { }
		try{isImplicit = cppnode.isImplicit()?1:0;}catch(NullPointerException exc) { }
		try{isOverride = cppnode.isOverride()?1:0;}catch(NullPointerException exc) { }
		try{isPureVirtual = cppnode.isPureVirtual()?1:0;}catch(NullPointerException exc) { }
		try{isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) { }	
		
		String declaredTypeTable = ""; //$NON-NLS-1$
		try{declaredTypeTable = declaredType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int declaredTypeTableSubId = 0;	
		try {declaredTypeTableSubId = (int) ((PDOMNode) declaredType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;	
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPMethod(ID, name,owner,ownerVariable,
				typeTable,typeTableSubId,
				declaredTypeTable,declaredTypeTableSubId,
				functionScopeTable,functionScopeTableSubId,
				hasParameterPack,
				requiredArgumentCount,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
				isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual,debug))
			return;
		
		
		mapCPPNode(declaredType, depth+1, ID,"declaredType", debug); //$NON-NLS-1$
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}
		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}		
	}
	
	private void mapPDOMCPPMethodInstance(Object node, int depth, int owner, String ownerVariable,boolean debug) throws SQLException {
		
		PDOMCPPMethodInstance cppnode = (PDOMCPPMethodInstance) node;
					
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType declaredType = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		IScope				functionScope = null;
		IType[] exceptionSpecification = null;
		ICPPTemplateArgument[] templateArguments = null; 
		ICPPTemplateDefinition templateDefinition = null;
		int 				requiredArgumentCount = 0;
		int isExplicitSpecialization = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int hasParameterPack = 0;
		int takesVarArgs = 0;			
		int 				isDestructor = 0;
		int 				isExplicit  = 0;
		int 				isFinal  = 0;
		int 				isImplicit  = 0;
		int 				isOverride  = 0;
		int 				isPureVirtual = 0;
		int 				isVirtual  = 0;

		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredType = cppnode.getDeclaredType();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }
		try{takesVarArgs = cppnode.takesVarArgs()?1:0;}catch(NullPointerException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{templateArguments = cppnode.getTemplateArguments(); }catch(NullPointerException exc) { }
		try{templateDefinition = cppnode.getTemplateDefinition();}catch(NullPointerException exc) { }
		try{isExplicitSpecialization = cppnode.isExplicitSpecialization()?1:0;}catch(NullPointerException exc) { }
		try{isDestructor = cppnode.isDestructor()?1:0;}catch(NullPointerException exc) { }
		try{isExplicit = cppnode.isExplicit()?1:0;}catch(NullPointerException exc) { }
		try{isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) { }
		try{isImplicit = cppnode.isImplicit()?1:0;}catch(NullPointerException exc) { }
		try{isOverride = cppnode.isOverride()?1:0;}catch(NullPointerException exc) { }
		try{isPureVirtual = cppnode.isPureVirtual()?1:0;}catch(NullPointerException exc) { }
		try{isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) { }
				
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String templateDefinitionTable = ""; //$NON-NLS-1$
		try{templateDefinitionTable = templateDefinition.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String declaredTypeTable = ""; //$NON-NLS-1$
		try{declaredTypeTable = declaredType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;	
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int templateDefinitionTableSubId = 0;	
		try {templateDefinitionTableSubId = (int) ((PDOMNode) templateDefinition).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int declaredTypeTableSubId = 0;	
		try {declaredTypeTableSubId = (int) ((PDOMNode) declaredType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
						
		if(controll.InsertPDOMCPPMethodInstance(ID, name,owner,ownerVariable,
				declaredTypeTable,declaredTypeTableSubId,
				typeTable,typeTableSubId,
				functionScopeTable,functionScopeTableSubId,
				templateDefinitionTable,templateDefinitionTableSubId,
				requiredArgumentCount,isExplicitSpecialization,
				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,hasParameterPack,takesVarArgs,
				isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual, debug))
			return;
									
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
				
		mapCPPNode(declaredType, depth+1, ID,"declaredType", debug); //$NON-NLS-1$
				
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}
				
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
				
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}
				
		if(templateArguments != null)
		for(int i = 0; i < templateArguments.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateArguments[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateArgument(ArrayID, ID, "templateArguments", templateArguments[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateArguments[i], depth+1, ArrayID,"templateArguments", debug); //$NON-NLS-1$
		}
				
		mapCPPNode(templateDefinition, depth+1, ID,"templateDefinition", debug); //$NON-NLS-1$
	}
	
	private void mapPDOMCPPMethodSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPMethodSpecialization cppnode = (PDOMCPPMethodSpecialization) node;

		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		ICPPFunctionType declaredType = null;
		IType[] exceptionSpecification = null;
		IScope functionScope = null;
		int hasParameterPack = 0;
		int 				requiredArgumentCount = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int 				isDestructor = 0;
		int 				isExplicit  = 0;
		int 				isFinal  = 0;
		int 				isImplicit  = 0;
		int 				isOverride  = 0;
		int 				isPureVirtual = 0;
		int 				isVirtual  = 0;		
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredType = cppnode.getDeclaredType();}catch(NullPointerException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{isDestructor = cppnode.isDestructor()?1:0;}catch(NullPointerException exc) { }
		try{isExplicit = cppnode.isExplicit()?1:0;}catch(NullPointerException exc) { }
		try{isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) { }
		try{isImplicit = cppnode.isImplicit()?1:0;}catch(NullPointerException exc) { }
		try{isOverride = cppnode.isOverride()?1:0;}catch(NullPointerException exc) { }
		try{isPureVirtual = cppnode.isPureVirtual()?1:0;}catch(NullPointerException exc) { }
		try{isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) { }		
		
		String declaredTypeTable = ""; //$NON-NLS-1$
		try{declaredTypeTable = declaredType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int declaredTypeTableSubId = 0;	
		try {declaredTypeTableSubId = (int) ((PDOMNode) declaredType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;	
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPMethodSpecialization(ID, name,owner,ownerVariable,
				typeTable,declaredTypeTableSubId,
				declaredTypeTable,functionScopeTableSubId,
				functionScopeTable,typeTableSubId,
				hasParameterPack, requiredArgumentCount,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
				isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual,debug))
			return;		
		
		mapCPPNode(declaredType, depth+1, ID,"declaredType", debug); //$NON-NLS-1$
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}
		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}		
	}
	
	private void mapPDOMCPPMethodTemplateSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPMethodTemplateSpecialization cppnode = (PDOMCPPMethodTemplateSpecialization) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		ICPPFunctionType declaredType = null;
		IType[] exceptionSpecification = null;
		IScope functionScope = null;
		ICPPTemplateInstance[] allInstances = null; //
		ICPPTemplateParameter[] templateParameters = null; //
		int hasParameterPack = 0;
		int 				requiredArgumentCount = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int 				isDestructor = 0;
		int 				isExplicit  = 0;
		int 				isFinal  = 0;
		int 				isImplicit  = 0;
		int 				isOverride  = 0;
		int 				isPureVirtual = 0;
		int 				isVirtual  = 0;		
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredType = cppnode.getDeclaredType();}catch(NullPointerException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{allInstances = cppnode.getAllInstances();}catch(NullPointerException exc) { }
		try{templateParameters = cppnode.getTemplateParameters();}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{isDestructor = cppnode.isDestructor()?1:0;}catch(NullPointerException exc) { }
		try{isExplicit = cppnode.isExplicit()?1:0;}catch(NullPointerException exc) { }
		try{isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) { }
		try{isImplicit = cppnode.isImplicit()?1:0;}catch(NullPointerException exc) { }
		try{isOverride = cppnode.isOverride()?1:0;}catch(NullPointerException exc) { }
		try{isPureVirtual = cppnode.isPureVirtual()?1:0;}catch(NullPointerException exc) { }
		try{isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) { }		
		
		String declaredTypeTable = ""; //$NON-NLS-1$
		try{declaredTypeTable = declaredType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int declaredTypeTableSubId = 0;	
		try {declaredTypeTableSubId = (int) ((PDOMNode) declaredType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;	
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPMethodTemplateSpecialization(ID, name,owner,ownerVariable,
				typeTable,typeTableSubId,
				declaredTypeTable,declaredTypeTableSubId,
				functionScopeTable,functionScopeTableSubId,
				hasParameterPack,requiredArgumentCount,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
				isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual,debug))
			return;		
		
		mapCPPNode(declaredType, depth+1, ID,"declaredType", debug); //$NON-NLS-1$
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}
		
		if(allInstances != null)
		for(int i = 0; i < allInstances.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allInstances[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateInstance(ArrayID, ID, "allInstances", allInstances[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allInstances[i], depth+1, ArrayID,"allInstances", debug); //$NON-NLS-1$
		}
		
		if(templateParameters != null)
		for(int i = 0; i < templateParameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateParameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "templateParameters", templateParameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateParameters[i], depth+1, ArrayID,"templateParameters", debug); //$NON-NLS-1$
		}

		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}		
	}
	
	private void mapPDOMCPPNamespace(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPNamespace cppnode = (PDOMCPPNamespace) node;
		
		int ID = (int) cppnode.getRecord();
		String name =  null;
		BTree 					indexTree = null;
		ICPPNamespaceScope[] 	inlineNamespaces = null;
		IBinding[] 				memberBindings = null;
		ICPPNamespaceScope 		namespaceScope = null;
		IIndexBinding 			scopeBinding = null;
		ICPPUsingDirective[] 	usingDirective = null;
		
		try{name = cppnode.getName();}catch(NullPointerException exc) {}
		try{indexTree = cppnode.getIndex();}catch(NullPointerException | CoreException exc) {} 
		try{inlineNamespaces = cppnode.getInlineNamespaces();}catch(NullPointerException exc) {}
		try{memberBindings = cppnode.getMemberBindings();}catch(NullPointerException exc) {}
		try{namespaceScope = cppnode.getNamespaceScope();}catch(NullPointerException exc) {}
		try{scopeBinding = cppnode.getScopeBinding();}catch(NullPointerException exc) {}
		try{usingDirective = cppnode.getUsingDirectives();}catch(NullPointerException exc) {}
		
		String namespaceScopeTable = ""; //$NON-NLS-1$
		try{namespaceScopeTable = namespaceScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String scopeBindingTable = ""; //$NON-NLS-1$
		try{scopeBindingTable = scopeBinding.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int namespaceScopeTableSubId = 0;	
		try {namespaceScopeTableSubId = (int) ((PDOMNode) namespaceScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int scopeBindingTableSubId = 0;	
		try {scopeBindingTableSubId = (int) ((PDOMNode) scopeBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPNamespace(ID, name,owner,ownerVariable,
				namespaceScopeTable,namespaceScopeTableSubId,
				scopeBindingTable,scopeBindingTableSubId,
				debug))
			return;
		
		ArrayList<Long> indexList = new ArrayList<Long>();			
		try {new BTreeNodeReader(cppnode.getPDOM().getDB().getRecPtr(indexTree.rootPointer), indexTree, indexList, cppnode.getPDOM(), new HashMap<Long,String>());
		} catch (CoreException e1) {   }
		indexList.forEach(x -> {
			try {
				Object newNode = cppnode.getLinkage().getNode(x, PDOMNode.getNodeType(cppnode.getLinkage().getDB(), x));
				int tableRefSubId = 0;		
				try {tableRefSubId = (int) ((PDOMNode) newNode).getRecord();}catch(NullPointerException | ClassCastException exc) { }
				controll.InsertBtreeIndex(ID, "BTree", newNode.getClass().getSimpleName(), tableRefSubId,debug); //$NON-NLS-1$
				mapCPPNode(newNode, depth+1, ID,"BTree", debug); //$NON-NLS-1$

			} catch (Exception e) {
				System.err.println(e.getMessage() + "BTREE INSERT!!!"); //$NON-NLS-1$
			}
		});
			
		if(inlineNamespaces != null)
		for(int i = 0; i < inlineNamespaces.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) inlineNamespaces[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPNamespaceScope(ArrayID, ID, "inlineNamespaces", inlineNamespaces[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(inlineNamespaces[i], depth+1, ArrayID,"inlineNamespaces", debug); //$NON-NLS-1$
		}
		
		if(memberBindings != null)
		for(int i = 0; i < memberBindings.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) memberBindings[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIBinding(ArrayID, ID, "memberBindings", memberBindings[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(memberBindings[i], depth+1, ArrayID,"memberBindings", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(namespaceScope, depth+1, ID,"namespaceScope", debug); //$NON-NLS-1$

		mapCPPNode(scopeBinding, depth+1, ID,"scopeBinding", debug); //$NON-NLS-1$

		if(usingDirective != null)
		for(int i = 0; i < usingDirective.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) usingDirective[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPUsingDeclaration(ArrayID, ID, "usingDirective", usingDirective[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCPPNode(usingDirective[i], depth+1, ArrayID,"usingDirective", debug); //$NON-NLS-1$
		}		
	}
	
	private void mapPDOMCPPNamespaceAlias(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPNamespaceAlias cppnode = (PDOMCPPNamespaceAlias) node;
		
		int ID = (int) cppnode.getRecord();
		String name =null;
		IBinding 				bindings = null;
		IBinding[] 				memberBindings = null;
		ICPPNamespaceScope 		namespaceScope = null;
		int 					isInline = 0;
		
		try{name = cppnode.getName();}catch(NullPointerException exc) {}
		try{bindings = cppnode.getBinding();}catch(NullPointerException exc) {}
		try{memberBindings = cppnode.getMemberBindings();}catch(NullPointerException exc) {}
		try{namespaceScope = cppnode.getNamespaceScope();}catch(NullPointerException exc) {}
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) {}
	
		String namespaceScopeTable = ""; //$NON-NLS-1$
		try{namespaceScopeTable = namespaceScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String bindingsTable = ""; //$NON-NLS-1$
		try{bindingsTable = bindings.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int namespaceScopeTableSubId = 0;	
		try {namespaceScopeTableSubId = (int) ((PDOMNode) namespaceScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int bindingsTableSubId = 0;	
		try {bindingsTableSubId = (int) ((PDOMNode) bindings).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPNamespaceAlias(ID, name,owner,ownerVariable,
				bindingsTable,bindingsTableSubId,
				namespaceScopeTable,namespaceScopeTableSubId,
				isInline,debug))
			return;
			
		mapCPPNode(bindings, depth+1, ID,"bindings", debug); //$NON-NLS-1$

		if(memberBindings != null)
		for(int i = 0; i < memberBindings.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) memberBindings[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIBinding(ArrayID, ID, "memberBindings", memberBindings[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(memberBindings[i], depth+1, ArrayID,"memberBindings", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(namespaceScope, depth+1, ID,"namespaceScope", debug); //$NON-NLS-1$		
	}
	
	@SuppressWarnings("deprecation")
	private void mapPDOMCPPParameter(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPParameter cppnode = (PDOMCPPParameter) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		long defaultValue = 0;
		IIndexScope scope = null;
		IType type = null;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int					isGloballyQualified = 0;
		int 				isMutable = 0;
		int 				isParameterPack = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
			
		try{ name = cppnode.getName();}catch(NullPointerException exc) {}
		try{ defaultValue = cppnode.getDefaultValue().numericalValue();}catch(NullPointerException exc) {}
		try{ scope = cppnode.getScope();}catch(NullPointerException exc) {}
		try{ type = cppnode.getType();}catch(NullPointerException exc) {}
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) {}
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) {}
		try{isGloballyQualified = cppnode.isGloballyQualified()?1:0;}catch(NullPointerException exc) {}
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) {}
		try{isParameterPack = cppnode.isParameterPack()?1:0;}catch(NullPointerException exc) {}
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		
		String scopeTable = ""; //$NON-NLS-1$
		try{scopeTable = scope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int scopeTableSubId = 0;	
		try {scopeTableSubId = (int) ((PDOMNode) scope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
				
		if(controll.InsertPDOMCPPParameter(ID, name,owner,ownerVariable,
				defaultValue,
				typeTable,typeTableSubId,
				scopeTable,scopeTableSubId,
				isAuto,isConstExpr,isExtern,isExternC,isGloballyQualified,isMutable,isParameterPack,isRegister,isStatic,debug))
			return;
										
		mapCPPNode(scope, depth+1, ID,"scope", debug);	 //$NON-NLS-1$
		mapCPPNode(type, depth+1, ID,"type", debug);	 //$NON-NLS-1$	
	}
	
	@SuppressWarnings("deprecation")
	private void mapPDOMCPPParameterSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPParameterSpecialization cppnode = (PDOMCPPParameterSpecialization) node;	
	
		int ID = (int) cppnode.getRecord();
		String name = null;
		long defaultValue = 0;
		IIndexScope scope = null;
		IType type = null;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isMutable = 0;
		int 				isParameterPack = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		
		try{ name = cppnode.getName();}catch(NullPointerException exc) {}
		try{ defaultValue = cppnode.getDefaultValue().numericalValue();}catch(NullPointerException exc) {}
		try{ scope = cppnode.getScope();}catch(NullPointerException exc) {}
		try{ type = cppnode.getType();}catch(NullPointerException exc) {}
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) {}
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) {}
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) {}
		try{isParameterPack = cppnode.isParameterPack()?1:0;}catch(NullPointerException exc) {}
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		
		String scopeTable = ""; //$NON-NLS-1$
		try{scopeTable = scope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int scopeTableSubId = 0;	
		try {scopeTableSubId = (int) ((PDOMNode) scope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPParameterSpecialization(ID, name,owner,ownerVariable,
				defaultValue,
				typeTable,typeTableSubId,
				scopeTable,scopeTableSubId,
				isAuto,isConstExpr,isExtern,isExternC,0,isMutable,isParameterPack,isRegister,isStatic,debug))
			return;
										
		mapCPPNode(scope, depth+1, ID,"scope", debug);	 //$NON-NLS-1$
		mapCPPNode(type, depth+1, ID,"type", debug);	 //$NON-NLS-1$		
	}
	
	private void mapPDOMCPPTemplateTypeParameter(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {

		PDOMCPPTemplateTypeParameter cppnode = (PDOMCPPTemplateTypeParameter) node;
						
		int ID = (int) cppnode.getRecord();
		String name = null;
		IType defaultType = null;
		ICPPTemplateArgument defaultValue = null;
		int parameterPosition = 0;
		int isParameterPack = 0;
		
		try{ name = cppnode.getName();}catch(NullPointerException exc) {}
		try{ defaultType = cppnode.getDefault();}catch(NullPointerException exc) {}
		try{ defaultValue = cppnode.getDefaultValue();}catch(NullPointerException exc) {}
		try{ parameterPosition = cppnode.getParameterPosition();}catch(NullPointerException exc) {}
		try{ isParameterPack = cppnode.isParameterPack()?1:0;}catch(NullPointerException exc) {}
		
		String defaultTypeTable = ""; //$NON-NLS-1$
		try{defaultTypeTable = defaultType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String defaultValueTable = ""; //$NON-NLS-1$
		try{defaultValueTable = defaultValue.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int defaultTypeTableSubId = 0;	
		try {defaultTypeTableSubId = (int) ((PDOMNode) defaultType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int defaultValueTableSubId = 0;	
		try {defaultValueTableSubId = (int) ((PDOMNode) defaultValue).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPTemplateTypeParameter(ID, name,owner,ownerVariable,
				defaultTypeTable,defaultTypeTableSubId,
				defaultValueTable,defaultValueTableSubId,
				parameterPosition,isParameterPack,
				debug))
			return;
		
		mapCPPNode(defaultType, depth+1, ID,"defaultType", debug);	 //$NON-NLS-1$
		mapCPPNode(defaultValue, depth+1,ID, "defaultValue", debug);	 //$NON-NLS-1$		
	}
	
	@SuppressWarnings("deprecation")
	private void mapPDOMCPPTemplateNonTypeParameter(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
	
		PDOMCPPTemplateNonTypeParameter cppnode = (PDOMCPPTemplateNonTypeParameter) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		IType type = null;
		long initValue = 0;
		ICPPTemplateArgument defaultValue = null;
		int parameterPosition = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isMutable = 0;
		int 				isParameterPack = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
				
		try{name = cppnode.getName();}catch(NullPointerException exc) {}
		try{type = cppnode.getType();}catch(NullPointerException exc) {}
		try{initValue = cppnode.getInitialValue().numericalValue();}catch(NullPointerException exc) {}
		try{defaultValue = cppnode.getDefaultValue();}catch(NullPointerException exc) {}
		try{parameterPosition = cppnode.getParameterPosition();}catch(NullPointerException exc) {}
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) {}
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) {}
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) {}
		try{isParameterPack = cppnode.isParameterPack()?1:0;}catch(NullPointerException exc) {}
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String defaultValueTable = ""; //$NON-NLS-1$
		try{defaultValueTable = defaultValue.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int defaultValueTableSubId = 0;	
		try {defaultValueTableSubId = (int) ((PDOMNode) defaultValue).getRecord();}catch(NullPointerException | ClassCastException exc) { }
				
		if(controll.InsertPDOMCPPTemplateNonTypeParameter(ID, name,owner,ownerVariable,
				initValue,
				typeTable,typeTableSubId,
				defaultValueTable,defaultValueTableSubId,
				parameterPosition,isAuto,isConstExpr,isExtern,isExternC,isMutable,isParameterPack,isRegister,isStatic,debug))
			return;
	
		mapCPPNode(type, depth+1, ID,"type", debug);	 //$NON-NLS-1$
		mapCPPNode(defaultValue, depth+1,ID, "defaultValue", debug);	 //$NON-NLS-1$		
	}
		
	private void mapPDOMCPPTemplateTemplateParameter(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
	
		PDOMCPPTemplateTemplateParameter cppnode = (PDOMCPPTemplateTemplateParameter) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;

		ICPPBase[] 									bases = null;
		ICPPConstructor[] 							constructors = null;
		IScope compositeScope = null;
		ICPPField[] 								declaredFields = null;
		ICPPMethod[] 								declaredMethods = null;
		ICPPMethod[] 								allDeclaredMethods = null;
		IType defaultType = null;
		ICPPTemplateArgument defaultValue = null;
		IField[] 									fields = null;
		ICPPMethod[] 								methods = null;
		ICPPClassType[] 							nestedClasses = null;
		ICPPClassTemplatePartialSpecialization[] 	partialSpecializations = null;
		IPDOMCPPTemplateParameter[] 				templateParameters = null;
		ICPPUsingDeclaration[] 						usingDeclarations = null;
		int 				parameterPosition = 0;
		int 				isAnonymous = 0;
		int 				isFinal = 0;
		int 				isParameterPack = 0;
		
		try{ name = cppnode.getName();}catch(NullPointerException exc) {}
		try{bases = cppnode.getBases();}catch(NullPointerException exc) {}
		try{constructors = cppnode.getConstructors();}catch(NullPointerException exc) {}
		try{declaredFields = cppnode.getDeclaredFields();}catch(NullPointerException exc) {}
		try{declaredMethods = cppnode.getDeclaredMethods();}catch(NullPointerException exc) {}
		try{allDeclaredMethods = cppnode.getAllDeclaredMethods();}catch(NullPointerException exc) {}
		try{fields = cppnode.getFields();}catch(NullPointerException exc) {}
		try{methods = cppnode.getMethods();}catch(NullPointerException exc) {}
		try{nestedClasses = cppnode.getNestedClasses();}catch(NullPointerException exc) {}
		try{partialSpecializations = cppnode.getPartialSpecializations();}catch(NullPointerException exc) {}
		try{templateParameters = cppnode.getTemplateParameters();}catch(NullPointerException exc) {}
		try{usingDeclarations = cppnode.getUsingDeclarations();}catch(NullPointerException exc) {}
		try{parameterPosition = cppnode.getParameterPosition();}catch(NullPointerException exc) {}
		try{isAnonymous = cppnode.isAnonymous()?1:0;}catch(NullPointerException exc) {}
		try{isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) {}
		try{isParameterPack = cppnode.isParameterPack()?1:0;}catch(NullPointerException exc) {}		
		try{compositeScope = cppnode.getCompositeScope();}catch(NullPointerException exc) {}
		try{defaultType = cppnode.getDefault();}catch(NullPointerException exc) {}
		try{defaultValue = cppnode.getDefaultValue();}catch(NullPointerException exc) {}
				
		String compositeScopeTable = ""; //$NON-NLS-1$
		try{compositeScopeTable = compositeScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String defaultTypeTable = ""; //$NON-NLS-1$
		try{defaultTypeTable = defaultType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String defaultValueTable = ""; //$NON-NLS-1$
		try{defaultValueTable = defaultValue.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int compositeScopeTableSubId = 0;	
		try {compositeScopeTableSubId = (int) ((PDOMNode) compositeScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int defaultTypeTableSubId = 0;	
		try {defaultTypeTableSubId = (int) ((PDOMNode) defaultType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int defaultValueTableSubId = 0;	
		try {defaultValueTableSubId = (int) ((PDOMNode) defaultValue).getRecord();}catch(NullPointerException | ClassCastException exc) { }
				
		if(controll.InsertPDOMCPPTemplateTemplateParameter(ID, name,owner,ownerVariable,
				compositeScopeTable,compositeScopeTableSubId,
				defaultTypeTable,defaultTypeTableSubId,
				defaultValueTable,defaultValueTableSubId,
				parameterPosition,isAnonymous,isFinal,isParameterPack,debug))
			return;
						
		if(allDeclaredMethods != null)
		for(int i = 0; i < allDeclaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allDeclaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "allDeclaredMethods", allDeclaredMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allDeclaredMethods[i], depth+1, ArrayID,"allDeclaredMethods", debug); //$NON-NLS-1$
		}
		
		if(bases != null)
		for(int i = 0; i < bases.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) bases[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPBase(ArrayID, ID, "bases", bases[i].getClass().getSimpleName(), tableRefSubId,i,debug); //$NON-NLS-1$
			mapCPPNode(bases[i], depth+1, ArrayID,"bases", debug); //$NON-NLS-1$
		}
		
		if(constructors != null)
		for(int i = 0; i < constructors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constructors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constructors[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(constructors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}
		
		
		mapCPPNode(compositeScope, depth+1, ID,"compositeScope", debug);	 //$NON-NLS-1$
		
		if(declaredFields != null)
		for(int i = 0; i < declaredFields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredFields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPField(ArrayID, ID, "declaredFields", declaredFields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredFields[i], depth+1, ArrayID,"declaredFields", debug); //$NON-NLS-1$
		}
		
		if(declaredMethods != null)
		for(int i = 0; i < declaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "declaredMethods", declaredMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredMethods[i], depth+1, ArrayID,"declaredMethods", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(defaultType, depth+1, ID,"defaultType", debug); //$NON-NLS-1$
		
		mapCPPNode(defaultValue, depth+1, ID,"defaultValue", debug); //$NON-NLS-1$
		
		if(fields != null)
		for(int i = 0; i < fields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) fields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIField(ArrayID, ID, "fields", fields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(fields[i], depth+1, ArrayID,"fields", debug); //$NON-NLS-1$
		}
		
		if(methods != null)
		for(int i = 0; i < methods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) methods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "methods", methods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(methods[i], depth+1, ArrayID,"methods", debug); //$NON-NLS-1$
		}
		
		if(nestedClasses != null)
		for(int i = 0; i < nestedClasses.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) nestedClasses[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassType(ArrayID, ID, "nestedClasses", nestedClasses[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCPPNode(nestedClasses[i], depth+1, ArrayID,"nestedClasses", debug); //$NON-NLS-1$
		}
		
		if(partialSpecializations != null)
		for(int i = 0; i < partialSpecializations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) partialSpecializations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassTemplatePartialSpecialization(ArrayID, ID, "partialSpecializations", partialSpecializations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(partialSpecializations[i], depth+1, ArrayID,"partialSpecializations", debug); //$NON-NLS-1$
		}
		
		if(templateParameters != null)
		for(int i = 0; i < templateParameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateParameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIPDOMCPPTemplateParameter(ArrayID, ID, "templateParameters", templateParameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateParameters[i], depth+1, ArrayID,"templateParameters", debug); //$NON-NLS-1$
		}
		
		if(usingDeclarations != null)
		for(int i = 0; i < usingDeclarations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) usingDeclarations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPUsingDeclaration(ArrayID, ID, "usingDeclarations", usingDeclarations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(usingDeclarations[i], depth+1, ArrayID,"usingDeclarations", debug); //$NON-NLS-1$
		}
	}
		
	private void mapPDOMCPPTypedef(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPTypedef cppnode = (PDOMCPPTypedef) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		IType type = null;		
		
		try {name = cppnode.getName();}catch(NullPointerException exc) {}
		try {type = cppnode.getType();	}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPTypedef(ID, name,owner,ownerVariable,typeTable,typeTableSubId,debug))
			return;
		
		mapCPPNode(type, depth+1, ID,"type", debug);	 //$NON-NLS-1$		
	}
		
	private void mapPDOMCPPTypedefSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
	
		PDOMCPPTypedefSpecialization cppnode = (PDOMCPPTypedefSpecialization) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		IType type = null;
		
		try {name = cppnode.getName();}catch(NullPointerException exc) {}
		try {type = cppnode.getType();	}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
					
		if(controll.InsertPDOMCPPTypedefSpecialization(ID, name,owner,ownerVariable,typeTable,typeTableSubId,debug))
			return;
		
		mapCPPNode(type, depth+1, ID,"type", debug);	 //$NON-NLS-1$		
	}
	
	private void mapPDOMCPPUnknownMemberClass(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		PDOMCPPUnknownMemberClass cppnode = (PDOMCPPUnknownMemberClass) node;
		controll.InsertPDOMCPPUnknownMemberClass(cppnode.getKey(), debug);
	}
	
	private void mapPDOMCPPUnknownMemberClassInstance(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {	
		PDOMCPPUnknownMemberClassInstance cppnode = (PDOMCPPUnknownMemberClassInstance) node;			
		controll.InsertPDOMCPPUnknownMemberClassInstance(cppnode.getKey(), debug);
	}
	
	private void mapPDOMCPPUnknownMethod(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {		
		controll.InsertPDOMCPPUnknownMethod(0, debug);	
	}
	
	private void mapPDOMCPPUsingDeclaration(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
	
		PDOMCPPUsingDeclaration cppnode = (PDOMCPPUsingDeclaration) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		IBinding[] delegates = null;	
		
		try { name = cppnode.getName();}catch(NullPointerException exc) {}
		try {delegates = cppnode.getDelegates();}catch(NullPointerException exc) {}
				
		if(controll.InsertPDOMCPPUsingDeclaration(ID, name,owner,ownerVariable,debug))
			return;
		
		if(delegates != null)
		for(int i = 0; i < delegates.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) delegates[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIBinding(ArrayID, ID, "delegates", delegates[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(delegates[i], depth+1, ArrayID,"delegates", debug); //$NON-NLS-1$
		}			
	}
	
	@SuppressWarnings("deprecation")
	private void mapPDOMCPPVariable(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPVariable cppnode = (PDOMCPPVariable) node;
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		long 	value = 0;
		IType 	type = null;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isMutable = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		
		try {name = cppnode.getName();}catch(NullPointerException exc) {}
		try {value = cppnode.getInitialValue().numericalValue();}catch(NullPointerException exc) {}
		try {type = cppnode.getType();}catch(NullPointerException exc) {}
		try {isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try {isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) {}
		try {isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try {isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) {}
		try {isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) {}
		try {isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try {isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) {}
	
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }

		if(controll.InsertPDOMCPPVariable(ID, name,owner,ownerVariable,
				value,typeTable,typeTableSubId,
				isAuto,isConstExpr,isExtern,isExternC,isMutable,isRegister,isStatic,debug))
			return;
		
		mapCPPNode(type, depth+1, ID,"type", debug);		 //$NON-NLS-1$		
	}
	
	private void mapCPPBasicType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		int ID = this.getuniqueID();
		String name = node.toString();
		
		if(controll.InsertCPPBasicType(ID, name,owner,ownerVariable,debug))
			return;	
		
	}
	
	private void mapProblemBinding(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		int ID = this.getuniqueID();
		
		if(controll.InsertProblemType(ID,debug))
			return;	
	}
	
	@SuppressWarnings({ "deprecation"})
	private void mapCPPFunctionType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPFunctionType cppnode = (CPPFunctionType) node;
		
		int ID = this.getuniqueID();
		IType[] parameterTypes = null;
		IType returnType = null;
		IPointerType thisType = null;
		int hasRefQualifier = 0;
		int isConst = 0;
		int isRValueReference = 0;
		int isVolatile = 0;
		int takesVarArgs = 0;
		
		try {parameterTypes = cppnode.getParameterTypes();}catch(NullPointerException exc) {}
		try {returnType = cppnode.getReturnType();}catch(NullPointerException exc) {}
		try {thisType = cppnode.getThisType();}catch(NullPointerException exc) {}
		try { hasRefQualifier = cppnode.hasRefQualifier()?1:0;}catch(NullPointerException exc) {}
		try { isConst = cppnode.isConst()?1:0;}catch(NullPointerException exc) {}
		try { isRValueReference = cppnode.isRValueReference()?1:0;}catch(NullPointerException exc) {}
		try { isVolatile = cppnode.isVolatile()?1:0;}catch(NullPointerException exc) {}
		try { takesVarArgs = cppnode.takesVarArgs()?1:0;}catch(NullPointerException exc) {}
		
		String returnTypeTable = ""; //$NON-NLS-1$
		try{returnTypeTable = returnType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String thistypeTable = ""; //$NON-NLS-1$
		try{thistypeTable = thisType.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int returnTypeTableSubId = 0;	
		try {returnTypeTableSubId = (int) ((PDOMNode) returnType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int thistypeTableSubId = 0;	
		try {thistypeTableSubId = (int) ((PDOMNode) thisType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPFunctionType(ID, "",owner,ownerVariable, //$NON-NLS-1$
				returnTypeTable,returnTypeTableSubId,
				thistypeTable,thistypeTableSubId,
				hasRefQualifier,isConst,isRValueReference,isVolatile,takesVarArgs,debug))
			return;	
				
		if(parameterTypes != null)
		for(int i = 0; i < parameterTypes.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameterTypes[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "parameterTypes", parameterTypes[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameterTypes[i], depth+1, ArrayID,"parameterTypes", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(returnType, depth+1, ID,"returnType", debug); //$NON-NLS-1$
		
		mapCPPNode(thisType, depth+1, ID,"thisType", debug);			 //$NON-NLS-1$		
	}
	
	private void mapCPPPointerType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPPointerType cppnode = (CPPPointerType) node;
		
		int ID = this.getuniqueID();
		IType type = null;
		int isConst = 0;
		int isRestrict = 0;
		int isVolatile =0;
		
		try { type = cppnode.getType();}catch(NullPointerException exc) {}
		try { isConst = cppnode.isConst()?1:0;}catch(NullPointerException exc) {}
		try { isRestrict = cppnode.isRestrict()?1:0;}catch(NullPointerException exc) {}
		try { isVolatile = cppnode.isVolatile()?1:0;}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { 	}
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPPointerType(ID, "",owner,ownerVariable,typeTable,typeTableSubId, //$NON-NLS-1$
				isConst,isRestrict,isVolatile,debug))
			return;

		mapCPPNode(type, depth+1, ID,"type", debug);	 //$NON-NLS-1$		
	}
	
	@SuppressWarnings("deprecation")
	private void mapCPPArrayType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		CPPArrayType cppnode = (CPPArrayType) node;
		
		int ID = this.getuniqueID();
		IType type = null;
		long size = 0;			
		
		try { type = cppnode.getType();}catch(NullPointerException exc) {}
		try { size =  cppnode.getSize().numericalValue();}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { 	}
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPArrayType(ID, "",owner,ownerVariable,typeTable,typeTableSubId,size,debug)) //$NON-NLS-1$
			return;
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$		
	}
	
	private void mapCPPQualifierType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
	
		CPPQualifierType cppnode = (CPPQualifierType) node;
		
		int ID = this.getuniqueID();
		IType type = null;
		int isConst = 0;
		int isVolatile = 0;
		
		try { type = cppnode.getType();}catch(NullPointerException exc) {}
		try { isConst = cppnode.isConst()?1:0;}catch(NullPointerException exc) {}
		try { isVolatile = cppnode.isVolatile()?1:0;}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPQualifierType(ID, "",owner,ownerVariable,typeTable,typeTableSubId, //$NON-NLS-1$
				isConst,isVolatile,debug))
			return;

		mapCPPNode(type, depth+1, ID,"type", debug);	 //$NON-NLS-1$						
	}
	
	private void mapCPPReferenceType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPReferenceType cppnode = (CPPReferenceType) node;
		
		int ID = this.getuniqueID();
		IType type = null;
		int isRValueReference = 0;			
		
		try { type = cppnode.getType();}catch(NullPointerException exc) {}
		try { isRValueReference = cppnode.isRValueReference()?1:0;}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPReferenceType(ID, "",owner,ownerVariable,typeTable,typeTableSubId, //$NON-NLS-1$
				isRValueReference,debug))
			return;

		mapCPPNode(type, depth+1, ID,"type", debug);	 //$NON-NLS-1$			
	}
	
	private void mapCPPParameterPackType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPParameterPackType cppnode = (CPPParameterPackType) node;	
		
		int ID = this.getuniqueID();
		IType type = null;
		
		try { type = cppnode.getType();}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPParameterPackType(ID, "",owner,ownerVariable,typeTable,typeTableSubId, //$NON-NLS-1$
				debug))
			return;

		mapCPPNode(type, depth+1, ID,"type", debug);	 //$NON-NLS-1$	
	}
	
	@SuppressWarnings({"deprecation" })
	private void mapCPPTemplateTypeArgument(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPTemplateTypeArgument cppnode = (CPPTemplateTypeArgument) node;	
		
		int ID = this.getuniqueID();
		ICPPTemplateArgument expansionPattern = null;
		ICPPEvaluation nonTypeEvaluation = null;
		long nonTypeValue = 0;
		IType typeOfNonTypeValue = null;
		IType typeValue = null;
		
		try { expansionPattern = cppnode.getExpansionPattern();}catch(NullPointerException exc) {}
		try { nonTypeEvaluation = cppnode.getNonTypeEvaluation();}catch(NullPointerException exc) {}
		try { nonTypeValue = cppnode.getNonTypeValue().numericalValue();}catch(NullPointerException exc) {}
		try { typeOfNonTypeValue = cppnode.getTypeOfNonTypeValue();}catch(NullPointerException exc) {}
		try { typeValue = cppnode.getTypeValue();}catch(NullPointerException exc) {}
		
		String expansionPatternTable = ""; //$NON-NLS-1$
		try{expansionPatternTable = expansionPattern.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String nonTypeEvaluationTable = ""; //$NON-NLS-1$
		try{nonTypeEvaluationTable = nonTypeEvaluation.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String typeOfNonTypeValueTable = ""; //$NON-NLS-1$
		try{typeOfNonTypeValueTable = typeOfNonTypeValue.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String typeValueTable = ""; //$NON-NLS-1$
		try{typeValueTable = typeValue.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int expansionPatternTableSubId = 0;	
		try {expansionPatternTableSubId = (int) ((PDOMNode) expansionPattern).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int nonTypeEvaluationTableSubId = 0;	
		try {nonTypeEvaluationTableSubId = (int) ((PDOMNode) nonTypeEvaluation).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeOfNonTypeValueTableSubId = 0;	
		try {typeOfNonTypeValueTableSubId = (int) ((PDOMNode) typeOfNonTypeValue).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeValueTableSubId = 0;	
		try {typeValueTableSubId = (int) ((PDOMNode) typeValue).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPTemplateTypeArgument(ID,owner,ownerVariable,
				nonTypeValue,
				expansionPatternTable,expansionPatternTableSubId,
				nonTypeEvaluationTable,nonTypeEvaluationTableSubId,
				typeOfNonTypeValueTable,typeOfNonTypeValueTableSubId,
				typeValueTable,typeValueTableSubId, debug))
			return;
			
		mapCPPNode(expansionPattern, depth+1, ID,"expansionPattern", debug); //$NON-NLS-1$
		
		mapCPPNode(nonTypeEvaluation, depth+1, ID,"nonTypeEvaluation", debug); //$NON-NLS-1$
		
		mapCPPNode(typeOfNonTypeValue, depth+1, ID,"typeOfNonTypeValue", debug); //$NON-NLS-1$
		
		mapCPPNode(typeValue, depth+1, ID,"typeValue", debug);	 //$NON-NLS-1$		
	}
	
	private void mapCPPConstructorSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPConstructorSpecialization cppnode = (CPPConstructorSpecialization) node;	
		
		int 				ID = this.getuniqueID();
		ICPPFunctionType 	declaredType = null;
		IType[] 			exceptionSpecification = null;
		IScope 				functionScope = null;
		ICPPParameter[] 	parameters = null;
		int 				requiredArgumentCount = 0;
		ICPPFunctionType 	type = null;
		ICPPClassType		classOwner = null;
		ICPPMethod 			specializedBinding = null;
		int 				hasParameterPack = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int 				takesVarArgs = 0;
		int 				isDestructor = 0;
		int 				isExplicit = 0;
		int 				isFinal = 0;
		int 				isImplicit = 0;
		int 				isOverride = 0;
		int 				isPureVirtual = 0;
		int 				isVirtual = 0;	
					
		try {declaredType = cppnode.getDeclaredType();}catch(NullPointerException exc) {}
		try {exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) {}
		try {functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) {}
		try {parameters = cppnode.getParameters();}catch(NullPointerException exc) {}
		try {requiredArgumentCount = cppnode.getRequiredArgumentCount();}catch(NullPointerException exc) {}
		try {type = cppnode.getType();}catch(NullPointerException exc) {}
		try {classOwner = cppnode.getClassOwner();}catch(NullPointerException exc) {}
		try {specializedBinding = cppnode.getSpecializedBinding();}catch(NullPointerException exc) {}
		try {hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) {}
		try {isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try {isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) {}
		try {isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) {}
		try {isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try {isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) {}
		try {isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) {}
		try {isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) {}
		try {isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) {}
		try {isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try {isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		try {takesVarArgs = cppnode.takesVarArgs()?1:0;}catch(NullPointerException exc) {}
		try {isDestructor = cppnode.isDestructor()?1:0;}catch(NullPointerException exc) {}
		try {isExplicit = cppnode.isExplicit()?1:0;}catch(NullPointerException exc) {}
		try {isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) {}
		try {isImplicit = cppnode.isImplicit()?1:0;}catch(NullPointerException exc) {}
		try {isOverride = cppnode.isOverride()?1:0;}catch(NullPointerException exc) {}
		try {isPureVirtual = cppnode.isPureVirtual()?1:0;}catch(NullPointerException exc) {}
		try {isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) {}		
		
		String declaredTypeTable = ""; //$NON-NLS-1$
		try{declaredTypeTable = declaredType.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String classOwnerTable = ""; //$NON-NLS-1$
		try{classOwnerTable = classOwner.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String specializedBindingTable = ""; //$NON-NLS-1$
		try{specializedBindingTable = specializedBinding.getClass().getSimpleName();}catch(NullPointerException exc) {}		
		
		int declaredTypeTableSubId = 0;	
		try {declaredTypeTableSubId = (int) ((PDOMNode) declaredType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;	
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int classOwnerTableSubId = 0;	
		try {classOwnerTableSubId = (int) ((PDOMNode) classOwner).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int specializedBindingTableSubId = 0;	
		try {specializedBindingTableSubId = (int) ((PDOMNode) specializedBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPConstructorSpecialization(ID,"",owner,ownerVariable, //$NON-NLS-1$
				declaredTypeTable,declaredTypeTableSubId,
				functionScopeTable,functionScopeTableSubId,
				typeTable,typeTableSubId,
				classOwnerTable,classOwnerTableSubId,
				specializedBindingTable,specializedBindingTableSubId,
				requiredArgumentCount,hasParameterPack,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
				isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual,takesVarArgs, debug))
			return;
		
		mapCPPNode(declaredType, depth+1, ID,"declaredType", debug); //$NON-NLS-1$
		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(), tableRefSubId,i,debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameter", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameter", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		
		mapCPPNode(classOwner, depth+1, ID,"classOwner", debug); //$NON-NLS-1$
		
		mapCPPNode(specializedBinding, depth+1, ID, "specializedBinding", debug); //$NON-NLS-1$		
	}
	
	private void mapCPPMethodSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPMethodSpecialization cppnode = (CPPMethodSpecialization) node;
		
		int 				ID = this.getuniqueID();
		ICPPFunctionType 	declaredType = null;
		IType[] 			exceptionSpecification = null;
		IScope 				functionScope = null;
		ICPPParameter[] 	parameters = null;
		int 				requiredArgumentCount = 0;
		ICPPFunctionType 	type = null;
		ICPPClassType		classOwner = null;
		ICPPMethod 			specializedBinding = null;
		int 				hasParameterPack = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int 				takesVarArgs = 0;
		int 				isDestructor = 0;
		int 				isExplicit = 0;
		int 				isFinal = 0;
		int 				isImplicit = 0;
		int 				isOverride = 0;
		int 				isPureVirtual = 0;
		int 				isVirtual = 0;		
					
		try {declaredType = cppnode.getDeclaredType();}catch(NullPointerException exc) {}
		try {exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) {}
		try {functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) {}
		try {parameters = cppnode.getParameters();}catch(NullPointerException exc) {}
		try {requiredArgumentCount = cppnode.getRequiredArgumentCount();}catch(NullPointerException exc) {}
		try {type = cppnode.getType();}catch(NullPointerException exc) {}
		try {classOwner = cppnode.getClassOwner();}catch(NullPointerException exc) {}
		try {specializedBinding = cppnode.getSpecializedBinding();}catch(NullPointerException exc) {}
		try {hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) {}
		try {isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try {isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) {}
		try {isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) {}
		try {isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try {isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) {}
		try {isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) {}
		try {isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) {}
		try {isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) {}
		try {isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try {isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		try {takesVarArgs = cppnode.takesVarArgs()?1:0;}catch(NullPointerException exc) {}
		try {isDestructor = cppnode.isDestructor()?1:0;}catch(NullPointerException exc) {}
		try {isExplicit = cppnode.isExplicit()?1:0;}catch(NullPointerException exc) {}
		try {isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) {}
		try {isImplicit = cppnode.isImplicit()?1:0;}catch(NullPointerException exc) {}
		try {isOverride = cppnode.isOverride()?1:0;}catch(NullPointerException exc) {}
		try {isPureVirtual = cppnode.isPureVirtual()?1:0;}catch(NullPointerException exc) {}
		try {isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) {}		
		
		String declaredTypeTable = ""; //$NON-NLS-1$
		try{declaredTypeTable = declaredType.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String classOwnerTable = ""; //$NON-NLS-1$
		try{classOwnerTable = classOwner.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String specializedBindingTable = ""; //$NON-NLS-1$
		try{specializedBindingTable = specializedBinding.getClass().getSimpleName();}catch(NullPointerException exc) {}	
		
		int declaredTypeTableSubId = 0;	
		try {declaredTypeTableSubId = (int) ((PDOMNode) declaredType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;	
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int classOwnerTableSubId = 0;	
		try {classOwnerTableSubId = (int) ((PDOMNode) classOwner).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int specializedBindingTableSubId = 0;	
		try {specializedBindingTableSubId = (int) ((PDOMNode) specializedBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPMethodSpecialization(ID,"",owner,ownerVariable, //$NON-NLS-1$
				declaredTypeTable,declaredTypeTableSubId,
				functionScopeTable,functionScopeTableSubId,
				typeTable,typeTableSubId,
				classOwnerTable,classOwnerTableSubId,
				specializedBindingTable,specializedBindingTableSubId,
				requiredArgumentCount,hasParameterPack,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
				isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual,takesVarArgs, debug))
			return;
		
		mapCPPNode(declaredType, depth+1, ID,"declaredType", debug); //$NON-NLS-1$
		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(), tableRefSubId,i,debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameter", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameter", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		
		mapCPPNode(classOwner, depth+1, ID,"classOwner", debug); //$NON-NLS-1$
		
		mapCPPNode(specializedBinding, depth+1, ID, "specializedBinding", debug); //$NON-NLS-1$		
	}
	
	@SuppressWarnings("deprecation")
	private void mapCPPFieldSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPFieldSpecialization cppnode = (CPPFieldSpecialization) node;	
		
		int 		ID = this.getuniqueID();
		String 		name = null;
		long 		initialValue = 0;
		IType 		type = null;
		int 		isAuto = 0;
		int 		isConstExpr = 0;
		int 		isExtern = 0;
		int 		isExternC = 0;
		int 		isMutable = 0;
		int 		isRegister = 0;
		int 		isStatic = 0;
		int fieldPosition = 0;
		int visibility =  0;		
		
		try { name = cppnode.getName();}catch(NullPointerException exc) {}
		try { initialValue = cppnode.getInitialValue().numericalValue();}catch(NullPointerException exc) {}
		try { type = cppnode.getType();}catch(NullPointerException exc) {}
		try { isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try { isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) {}
		try { isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try { isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) {}
		try { isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) {}
		try { isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try { isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		try { fieldPosition = cppnode.getFieldPosition();}catch(NullPointerException exc) {}
		try { visibility = cppnode.getVisibility();}catch(NullPointerException exc) {}

		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPFieldSpecialization(ID,name,owner,ownerVariable, 
				initialValue,typeTable,typeTableSubId,
				isAuto,isConstExpr,isExtern,isExternC,isMutable,isRegister,isStatic,fieldPosition,visibility,debug))
			return;
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$7
	}
	
	private void mapTypeOfDependentExpression(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {		
		//pass
	}
	
	@SuppressWarnings("deprecation")
	private void mapCPPParameterSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPParameterSpecialization cppnode = (CPPParameterSpecialization) node;	
		
		int 				ID = this.getuniqueID();
		long 				defaultValue = 0;
		long 				initialValue = 0;
		IType 				type = null;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isMutable = 0;
		int 				isParameterPack = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		
		try { defaultValue = cppnode.getDefaultValue().numericalValue();}catch(NullPointerException exc) {}
		try { initialValue = cppnode.getInitialValue().numericalValue();}catch(NullPointerException exc) {}
		try { type = cppnode.getType();}catch(NullPointerException exc) {}
		try { isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try { isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) {}
		try { isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try { isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) {}
		try { isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) {}
		try { isParameterPack = cppnode.isParameterPack()?1:0;}catch(NullPointerException exc) {}
		try { isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try { isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typeTableSubId = 0;	
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPParameterSpecialization(ID,"",owner,ownerVariable, //$NON-NLS-1$
				defaultValue,initialValue,typeTable,typeTableSubId,
				isAuto,isConstExpr,isExtern,isExternC,isMutable,isParameterPack,isRegister,isStatic,debug))
			return;
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$			
	}
	
	private void mapCPPClassInstance(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPClassInstance cppnode = (CPPClassInstance) node;	
		
		int 						ID = this.getuniqueID();
		String 						name = null;
		ICPPBase[] 					bases = null;
		ICPPConstructor[] 			constructors = null;
		ICPPClassScope 				compositeScope = null;
		ICPPField[] 				declaredFields = null;
		ICPPMethod[] 				declaredMethods = null;
		ICPPMethod[] 				allDeclaredMethods = null;
		IField[] 					fields = null;
		ICPPMethod[] 				methods = null;
		ICPPClassType[] 			nestedClasses = null;
		ICPPClassType				specializedBinding = null;
		ICPPUsingDeclaration[] 		usingDeclarations = null;
		ICPPTemplateArgument[] 		templateArguments = null;
		ICPPTemplateDefinition		templateDefinition = null;
		int 						isAnonym = 0;
		int 						isFinal = 0;
		int							isExplicitSpecialization = 0;		
		
		try { name = cppnode.getName();}catch(NullPointerException exc) {}
		try { bases = cppnode.getBases();}catch(NullPointerException exc) {}
		try { constructors = cppnode.getConstructors();}catch(NullPointerException exc) {}
		try { compositeScope = cppnode.getCompositeScope();}catch(NullPointerException exc) {}
		try { declaredFields = cppnode.getDeclaredFields();}catch(NullPointerException exc) {}
		try { declaredMethods = cppnode.getDeclaredMethods();}catch(NullPointerException exc) {}
		try { allDeclaredMethods = cppnode.getAllDeclaredMethods();}catch(NullPointerException exc) {}
		try { fields = cppnode.getFields();}catch(NullPointerException exc) {}
		try { methods = cppnode.getMethods();}catch(NullPointerException exc) {}
		try { nestedClasses = cppnode.getNestedClasses();}catch(NullPointerException exc) {}
		try { specializedBinding = cppnode.getSpecializedBinding();}catch(NullPointerException exc) {}
		try { usingDeclarations = cppnode.getUsingDeclarations();}catch(NullPointerException exc) {}
		try { isAnonym = cppnode.isAnonymous()?1:0;}catch(NullPointerException exc) {}
		try { isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) {}
		try { templateArguments = cppnode.getTemplateArguments();}catch(NullPointerException exc) {}
		try { templateDefinition = cppnode.getTemplateDefinition();}catch(NullPointerException exc) {}
		try { isExplicitSpecialization = cppnode.isExplicitSpecialization()?1:0;}catch(NullPointerException exc) {}
		
		String compositeScopeTable = ""; //$NON-NLS-1$
		try{compositeScopeTable = compositeScope.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String specializedBindingTable = ""; //$NON-NLS-1$
		try{specializedBindingTable = specializedBinding.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String templateDefinitionTable = ""; //$NON-NLS-1$
		try{templateDefinitionTable = templateDefinition.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int compositeScopeTableSubId = 0;	
		try {compositeScopeTableSubId = (int) ((PDOMNode) compositeScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int specializedBindingTableSubIdSubId = 0;	
		try {specializedBindingTableSubIdSubId = (int) ((PDOMNode) specializedBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int templateDefinitionTableSubId = 0;	
		try {templateDefinitionTableSubId = (int) ((PDOMNode) templateDefinition).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPClassInstance(ID,name,owner,ownerVariable,
				compositeScopeTable,compositeScopeTableSubId,
				specializedBindingTable,specializedBindingTableSubIdSubId,
				templateDefinitionTable,templateDefinitionTableSubId,
				isAnonym,isFinal,isExplicitSpecialization,debug))
			return;
		
		if(allDeclaredMethods != null)
		for(int i = 0; i < allDeclaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allDeclaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "allDeclaredMethods", allDeclaredMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allDeclaredMethods[i], depth+1, ArrayID,"allDeclaredMethods", debug); //$NON-NLS-1$
		}
		
		if(bases != null)
		for(int i = 0; i < bases.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) bases[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPBase(ArrayID, ID, "bases", bases[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(bases[i], depth+1, ArrayID,"bases", debug); //$NON-NLS-1$
		}
		
		if(constructors != null)
		for(int i = 0; i < constructors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constructors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constructors[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(constructors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}

		mapCPPNode(compositeScope, depth+1, ID,"compositeScope", debug); //$NON-NLS-1$
		
		if(declaredFields != null)
		for(int i = 0; i < declaredFields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredFields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPField(ArrayID, ID, "declaredFields", declaredFields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredFields[i], depth+1, ArrayID,"declaredFields", debug); //$NON-NLS-1$
		}
		
		if(declaredMethods != null)
		for(int i = 0; i < declaredMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) declaredMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "declaredMethods", declaredMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(declaredMethods[i], depth+1, ArrayID,"declaredMethods", debug); //$NON-NLS-1$
		}
		
		if(fields != null)
		for(int i = 0; i < fields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) fields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIField(ArrayID, ID, "fields", fields[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(fields[i], depth+1, ArrayID,"fields", debug); //$NON-NLS-1$
		}
		
		if(methods != null)
		for(int i = 0; i < methods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) methods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "methods", methods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(methods[i], depth+1, ArrayID,"methods", debug); //$NON-NLS-1$
		}
		
		if(nestedClasses != null)
		for(int i = 0; i < nestedClasses.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) nestedClasses[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPClassType(ArrayID, ID, "nestedClasses", nestedClasses[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(nestedClasses[i], depth+1, ArrayID,"nestedClasses", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(specializedBinding, depth+1, ID,"specializedBinding", debug); //$NON-NLS-1$
		
		if(usingDeclarations != null)
		for(int i = 0; i < usingDeclarations.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) usingDeclarations[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPUsingDeclaration(ArrayID, ID, "usingDeclarations", usingDeclarations[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(usingDeclarations[i], depth+1, ArrayID,"usingDeclarations", debug); //$NON-NLS-1$
		}
	
		if(templateArguments != null)
		for(int i = 0; i < templateArguments.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateArguments[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateArgument(ArrayID, ID, "templateArguments", templateArguments[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateArguments[i], depth+1, ArrayID,"templateArguments", debug); //$NON-NLS-1$
		}
	
		mapCPPNode(templateDefinition, depth+1, ID,"templateDefinition", debug);//$NON-NLS-1$		
	}
		
	private void mapCPPTypedefSpecialization(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPTypedefSpecialization cppnode = (CPPTypedefSpecialization) node;	
		
		int 	ID = this.getuniqueID();
		String 	name = cppnode.getName();
		IType typ = cppnode.getType();
		
		String typTable = ""; //$NON-NLS-1$
		try{typTable = typ.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typTableSubId = 0;	
		try {typTableSubId = (int) ((PDOMNode) typ).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPTypedefSpecialization(ID,name,owner,ownerVariable,typTable,typTableSubId,debug))
			return;
		
		mapCPPNode(typ, depth+1, ID,"typ", debug); //$NON-NLS-1$	
	}
		
	@SuppressWarnings("deprecation")
	private void mapCPPClassSpecializationScope(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		CPPClassSpecializationScope cppnode = (CPPClassSpecializationScope) node;	
		
		int 						ID = this.getuniqueID();
		String name = ""; //$NON-NLS-1$
		ICPPClassSpecialization classType = null;
		ICPPConstructor[] constructors = null;
		ICPPMethod[] implicitMethods = null;
		
		try { classType = cppnode.getClassType();}catch(NullPointerException exc) {}
		try { name = cppnode.getScopeName().toCharArray().toString();}catch(NullPointerException exc) {}
		try { constructors = cppnode.getConstructors();}catch(NullPointerException exc) {}
		try { implicitMethods = cppnode.getImplicitMethods();}catch(NullPointerException exc) {}
		
		String classTypeTable = ""; //$NON-NLS-1$
		try{classTypeTable = classType.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int classTypeTableSubId = 0;	
		try {classTypeTableSubId = (int) ((PDOMNode) classType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPPClassSpecializationScope(ID,name,owner,ownerVariable, 
				classTypeTable,classTypeTableSubId,debug))
			return;
		
		mapCPPNode(classType, depth+1, ID,"classType", debug); //$NON-NLS-1$
		
		if(constructors != null)
		for(int i = 0; i < constructors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constructors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constructors[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(constructors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}
	
		if(implicitMethods != null)
		for(int i = 0; i < implicitMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) implicitMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "implicitMethods", implicitMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(implicitMethods[i], depth+1, ArrayID,"implicitMethods", debug); //$NON-NLS-1$
		}		
	}
	
	private void mapPDOMCPPClassScope(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPClassScope cppnode = (PDOMCPPClassScope) node;
		
		int 						ID = this.getuniqueID();
		String scopeName = ""; //$NON-NLS-1$
		ICPPClassType classType= null;
		ICPPConstructor[] constrcutors = null;
		ICPPMethod[] implicitMethods = null;
		IIndexBinding scopeBinding = null;
		
		try { classType = cppnode.getClassType();}catch(NullPointerException exc) {}
		try { constrcutors = cppnode.getConstructors();}catch(NullPointerException exc) {}
		try { implicitMethods = cppnode.getImplicitMethods();}catch(NullPointerException exc) {}
		try { scopeBinding = cppnode.getScopeBinding();}catch(NullPointerException exc) {}
		try { scopeName = cppnode.getScopeName().toString();}catch(NullPointerException exc) {}		
		
		String classTypeTable = ""; //$NON-NLS-1$
		try{classTypeTable = classType.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String scopeBindingsTable = ""; //$NON-NLS-1$
		try{scopeBindingsTable = scopeBinding.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int classTypeTableSubId = 0;	
		try {classTypeTableSubId = (int) ((PDOMNode) classType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int scopeBindingsTableSubId = 0;	
		try {scopeBindingsTableSubId = (int) ((PDOMNode) scopeBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		
		if(controll.InsertPDOMCPPClassScope(ID,scopeName,owner,ownerVariable,
				classTypeTable,classTypeTableSubId,
				scopeBindingsTable,scopeBindingsTableSubId,
				debug))
			return;
		
		mapCPPNode(classType, depth+1, ID,"classType", debug); //$NON-NLS-1$
		
		if(constrcutors != null)
		for(int i = 0; i < constrcutors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constrcutors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constrcutors[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(constrcutors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}
		
		if(implicitMethods != null)
		for(int i = 0; i < implicitMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) implicitMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "implicitMethods", implicitMethods[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCPPNode(implicitMethods[i], depth+1, ArrayID,"implicitMethods", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(scopeBinding, depth+1, ID,"scopeBinding", debug); //$NON-NLS-1$		
	}
	
	private void mapPDOMCPPBase(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPBase cppnode = (PDOMCPPBase) node;
		
		int ID = (int) cppnode.getRecord();		
		IBinding baseClass = null;
		IType baseClassType = null;
		String classDefinitionName = ""; //$NON-NLS-1$
		int isInheritedConstructorsSource = 0;
		int isVirtual = 0;
		
		try { baseClass = cppnode.getBaseClass();}catch(NullPointerException exc) {}
		try { baseClassType= cppnode.getBaseClassType();}catch(NullPointerException exc) {}
		try { classDefinitionName = cppnode.getClassDefinitionName().toString();}catch(NullPointerException exc) {}
		try { isInheritedConstructorsSource = cppnode.isInheritedConstructorsSource()?1:0;}catch(NullPointerException exc) {}
		try { isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) {}
		
		String baseClassTable = ""; //$NON-NLS-1$
		try{baseClassTable = baseClass.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String baseClassTypeTable = ""; //$NON-NLS-1$
		try{baseClassTypeTable = baseClassType.getClass().getSimpleName();}catch(NullPointerException exc) {}	
		
		int baseClassTableSubId = 0;	
		try {baseClassTableSubId = (int) ((PDOMNode) baseClass).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int baseClassTypeTableSubId = 0;	
		try {baseClassTypeTableSubId = (int) ((PDOMNode) baseClassType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPBase(ID,classDefinitionName,owner,ownerVariable,
				baseClassTable,baseClassTableSubId,
				baseClassTypeTable,baseClassTypeTableSubId,isInheritedConstructorsSource,isVirtual,
				debug))
			return;
			
		mapCPPNode(baseClass, depth+1, ID,"baseClass", debug); //$NON-NLS-1$
		
		mapCPPNode(baseClassType, depth+1, ID,"baseClassType", debug); //$NON-NLS-1$			
	}
	
	private void mapPDOMCPPClassSpecializationScope(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCPPClassSpecializationScope cppnode = (PDOMCPPClassSpecializationScope) node;
		
		int ID = getuniqueID();		
		ICPPClassSpecialization classType = null;
		ICPPConstructor[] constructors = null;
		ICPPMethod[] implicitMethods = null;
		ICPPClassType originalClassType = null;
		String scopeName = ""; //$NON-NLS-1$
		IIndexBinding scopeBinding = null;
		
		try { classType = cppnode.getClassType();}catch(NullPointerException exc) {}
		try { constructors = cppnode.getConstructors();}catch(NullPointerException exc) {}
		try { implicitMethods = cppnode.getImplicitMethods();}catch(NullPointerException exc) {}
		try { originalClassType = cppnode.getOriginalClassType();}catch(NullPointerException exc) {}
		try { scopeName = cppnode.getScopeName().toString();}catch(NullPointerException exc) {}
		try { scopeBinding = cppnode.getScopeBinding();}catch(NullPointerException exc) {}
		
		String classTypeTable = ""; //$NON-NLS-1$
		try{classTypeTable = classType.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String originalClassTypeTable = ""; //$NON-NLS-1$
		try{originalClassTypeTable = originalClassType.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String scopeBindingTable = ""; //$NON-NLS-1$
		try{scopeBindingTable = scopeBinding.getClass().getSimpleName();}catch(NullPointerException exc) {}	
		
		int classTypeTableSubId = 0;	
		try {classTypeTableSubId = (int) ((PDOMNode) classType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int originalClassTypeTableSubId = 0;	
		try {originalClassTypeTableSubId = (int) ((PDOMNode) originalClassType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int scopeBindingTableSubId = 0;	
		try {scopeBindingTableSubId = (int) ((PDOMNode) scopeBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPClassSpecializationScope(ID,scopeName,owner,ownerVariable,
				classTypeTable,classTypeTableSubId,
				originalClassTypeTable,originalClassTypeTableSubId,scopeBindingTable,scopeBindingTableSubId,
				debug))
			return;
		
		mapCPPNode(classType, depth+1, ID,"classType", debug); //$NON-NLS-1$
		
		if(constructors != null)
		for(int i = 0; i < constructors.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) constructors[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPConstructor(ArrayID, ID, "constructors", constructors[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(constructors[i], depth+1, ArrayID,"constructors", debug); //$NON-NLS-1$
		}
		
		if(implicitMethods != null)
		for(int i = 0; i < implicitMethods.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) implicitMethods[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPMethod(ArrayID, ID, "implicitMethods", implicitMethods[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(implicitMethods[i], depth+1, ArrayID,"implicitMethods", debug); //$NON-NLS-1$
		}
		
		mapCPPNode(originalClassType, depth+1, ID,"originalClassType", debug); //$NON-NLS-1$
		
		mapCPPNode(scopeBinding, depth+1, ID,"scopeBindings", debug); //$NON-NLS-1$		
	}
			
	private void mapPDOMCPPGlobalScope(Object node, int depth, int owner, String ownerVariable, boolean debug)throws SQLException {	
		
		PDOMCPPGlobalScope cppnode = (PDOMCPPGlobalScope) node;
		
		int ID = getuniqueID();		
		IIndexBinding scopeBinding = null;
		String scopeName = ""; //$NON-NLS-1$
		ICPPNamespaceScope[] inlineNamespaces = null;
		ICPPUsingDirective[] usingDirectives = null;
		
		try { scopeName = cppnode.getScopeName().toString();}catch(NullPointerException exc) {}
		try { scopeBinding = cppnode.getScopeBinding();}catch(NullPointerException exc) {}
		try { inlineNamespaces = cppnode.getInlineNamespaces();}catch(NullPointerException exc) {}
		try { usingDirectives = cppnode.getUsingDirectives();}catch(NullPointerException exc) {}
		
		String scopeBindingTable = ""; //$NON-NLS-1$
		try{scopeBindingTable = scopeBinding.getClass().getSimpleName();}catch(NullPointerException exc) {}	
		
		int scopeBindingTableSubId = 0;		
		try {scopeBindingTableSubId = (int) ((PDOMNode) scopeBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPGlobalScope(ID,scopeName,owner,ownerVariable,scopeBindingTable,scopeBindingTableSubId,
				debug))
			return;
		
		mapCPPNode(scopeBinding, depth+1, ID,"scopeBinding", debug); //$NON-NLS-1$
		
		if(inlineNamespaces != null)
		for(int i = 0; i < inlineNamespaces.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) inlineNamespaces[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPNamespaceScope(ArrayID, ID, "inlineNamespaces", inlineNamespaces[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(inlineNamespaces[i], depth+1, ArrayID,"inlineNamespaces", debug); //$NON-NLS-1$
		}
		
		if(usingDirectives != null)
		for(int i = 0; i < usingDirectives.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) usingDirectives[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPUsingDeclaration(ArrayID, ID, "usingDirectives", usingDirectives[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(usingDirectives[i], depth+1, ArrayID,"usingDirectives", debug); //$NON-NLS-1$
		}		
	}
	
	@SuppressWarnings("deprecation")
	private void mapCPPTemplateNonTypeArgument(Object node, int depth, int owner, String ownerVariable, boolean debug)throws SQLException {	
	
		CPPTemplateNonTypeArgument cppnode = (CPPTemplateNonTypeArgument) node;
		
		int ID = getuniqueID();
		ICPPTemplateArgument expansionPattern = null;
		ICPPEvaluation nonTypeEvaluation = null;
		long nonTypeValue = 0;
		IType originalTypeValue = null;
		IType typeOfNonTypeValue = null;
		IType typeValue = null;
		int isNonTypeValue = 0;
		int isPackExpansion = 0;
		int isTypeValue = 0;
		
		try { expansionPattern = cppnode.getExpansionPattern();}catch(NullPointerException exc) {}
		try { nonTypeEvaluation = cppnode.getNonTypeEvaluation();}catch(NullPointerException exc) {}
		try { nonTypeValue = cppnode.getNonTypeValue().numericalValue();}catch(NullPointerException exc) {}
		try { originalTypeValue = cppnode.getOriginalTypeValue();}catch(NullPointerException exc) {}
		try { typeOfNonTypeValue = cppnode.getTypeOfNonTypeValue();}catch(NullPointerException exc) {}
		try { typeValue = cppnode.getTypeValue();}catch(NullPointerException exc) {}
		try { isNonTypeValue = cppnode.isNonTypeValue()?1:0;}catch(NullPointerException exc) {}
		try { isPackExpansion = cppnode.isPackExpansion()?1:0;}catch(NullPointerException exc) {}
		try { isTypeValue = cppnode.isTypeValue()?1:0;}catch(NullPointerException exc) {}		
		
		String expansionPatternTable = ""; //$NON-NLS-1$
		try{expansionPatternTable = expansionPattern.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String nonTypeEvaluationTable = ""; //$NON-NLS-1$
		try{nonTypeEvaluationTable = nonTypeEvaluation.getClass().getSimpleName();}catch(NullPointerException exc) {}			
		String originalTypeValueTable = ""; //$NON-NLS-1$
		try{originalTypeValueTable = originalTypeValue.getClass().getSimpleName();}catch(NullPointerException exc) {}	
		String typeOfNonTypeValueTable = ""; //$NON-NLS-1$
		try{typeOfNonTypeValueTable = typeOfNonTypeValue.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String typeValueTable = ""; //$NON-NLS-1$
		try{typeValueTable = typeValue.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int expansionPatternTableSubId = 0;		
		try {expansionPatternTableSubId = (int) ((PDOMNode) expansionPattern).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int nonTypeEvaluationTableSubId = 0;		
		try {nonTypeEvaluationTableSubId = (int) ((PDOMNode) nonTypeEvaluation).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int originalTypeValueTableeSubId = 0;		
		try {originalTypeValueTableeSubId = (int) ((PDOMNode) originalTypeValue).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeOfNonTypeValueTableSubId = 0;		
		try {typeOfNonTypeValueTableSubId = (int) ((PDOMNode) typeOfNonTypeValue).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int typeValueTableSubId = 0;		
		try {typeValueTableSubId = (int) ((PDOMNode) typeValue).getRecord();}catch(NullPointerException | ClassCastException exc) { 	}		
		
		
		if(controll.InsertCPPTemplateNonTypeArgument(ID,"",owner,ownerVariable,nonTypeValue, //$NON-NLS-1$
				expansionPatternTable, expansionPatternTableSubId,
				nonTypeEvaluationTable,nonTypeEvaluationTableSubId,
				originalTypeValueTable,originalTypeValueTableeSubId,
				typeOfNonTypeValueTable,typeOfNonTypeValueTableSubId,
				typeValueTable,typeValueTableSubId,
				isNonTypeValue,
				isPackExpansion,isTypeValue,
				debug))
			return;
		
		mapCPPNode(expansionPattern, depth+1, ID,"expansionPattern", debug); //$NON-NLS-1$
		
		mapCPPNode(nonTypeEvaluation, depth+1, ID,"nonTypeEvaluation", debug); //$NON-NLS-1$
		
		mapCPPNode(originalTypeValue, depth+1, ID,"originalTypeValue", debug); //$NON-NLS-1$
		
		mapCPPNode(typeOfNonTypeValue, depth+1, ID,"typeOfNonTypeValue", debug); //$NON-NLS-1$
		
		mapCPPNode(typeValue, depth+1, ID,"typeValue", debug); //$NON-NLS-1$			
	}
	
	private void mapPDOMCPPConstructorTemplate(Object node, int depth, int owner, String ownerVariable,boolean debug) throws SQLException {
			
		PDOMCPPConstructorTemplate cppnode = (PDOMCPPConstructorTemplate) node;				
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType declaredTyp = null;
		IType[] exceptionSpecification = null;
		IScope functionScope = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		ICPPTemplateInstance[] allInstances = null; //--//
		IPDOMCPPTemplateParameter[] templateParameters = null; //--//
		int 				requiredArgumentCount = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int hasParameterPack  = 0;
		int isDestructor = 0;
		int isExplicit = 0;
		int isFinal = 0;
		int isImplicit = 0;
		int isOverride = 0;
		int isPureVirtual = 0;
		int isVirtual = 0;
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredTyp = cppnode.getDeclaredType();}catch(NullPointerException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{allInstances = cppnode.getAllInstances();}catch(NullPointerException exc) { }
		try{templateParameters = cppnode.getTemplateParameters();}catch(NullPointerException exc) { }		
		try{requiredArgumentCount = cppnode.getRequiredArgumentCount();}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }
		try{isDestructor = cppnode.isDestructor()?1:0;}catch(NullPointerException exc) { }
		try{isExplicit = cppnode.isExplicit()?1:0;}catch(NullPointerException exc) { }
		try{isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) { }
		try{isImplicit = cppnode.isImplicit()?1:0;}catch(NullPointerException exc) { }
		try{isOverride = cppnode.isOverride()?1:0;}catch(NullPointerException exc) { }
		try{isPureVirtual = cppnode.isPureVirtual()?1:0;}catch(NullPointerException exc) { }
		try{isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) { }
		
		String funcTypeTable = ""; //$NON-NLS-1$
		try{funcTypeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String declaredTypTable = ""; //$NON-NLS-1$
		try{declaredTypTable = declaredTyp.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int funcTypeTableSubId = 0;		
		try {funcTypeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int declaredTypTableSubId = 0;		
		try {declaredTypTableSubId = (int) ((PDOMNode) declaredTyp).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;		
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPConstructorTemplate(ID, name,owner,ownerVariable,
				funcTypeTable,funcTypeTableSubId,
				declaredTypTable,declaredTypTableSubId,
				functionScopeTable,functionScopeTableSubId,
				hasParameterPack,
				requiredArgumentCount,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
				isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual,debug))
			return;
		
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		mapCPPNode(declaredTyp, depth+1, ID,"declaredTyp", debug); //$NON-NLS-1$		
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$		
		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}		
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}		

		if(allInstances != null)
		for(int i = 0; i < allInstances.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allInstances[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateInstance(ArrayID, ID, "allInstances", allInstances[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allInstances[i], depth+1, ArrayID,"allInstances", debug); //$NON-NLS-1$
		}		

		if(templateParameters != null)
		for(int i = 0; i < templateParameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateParameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIPDOMCPPTemplateParameter(ArrayID, ID, "templateParameters", templateParameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateParameters[i], depth+1, ArrayID,"templateParameters", debug); //$NON-NLS-1$
		}		
	}

	private void mapPDOMCPPMethodTemplate(Object node, int depth, int owner, String ownerVariable,boolean debug) throws SQLException {
		
		PDOMCPPMethodTemplate cppnode = (PDOMCPPMethodTemplate) node;				
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType declaredTyp = null;
		IType[] exceptionSpecification = null;
		IScope functionScope = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		ICPPTemplateInstance[] allInstances = null; //--//
		IPDOMCPPTemplateParameter[] templateParameters = null; //--//
		int 				requiredArgumentCount = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int hasParameterPack  = 0;
		int isDestructor = 0;
		int isExplicit = 0;
		int isFinal = 0;
		int isImplicit = 0;
		int isOverride = 0;
		int isPureVirtual = 0;
		int isVirtual = 0;
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredTyp = cppnode.getDeclaredType();}catch(NullPointerException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{allInstances = cppnode.getAllInstances();}catch(NullPointerException exc) { }
		try{templateParameters = cppnode.getTemplateParameters();}catch(NullPointerException exc) { }		
		try{requiredArgumentCount = cppnode.getRequiredArgumentCount();}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }
		try{isDestructor = cppnode.isDestructor()?1:0;}catch(NullPointerException exc) { }
		try{isExplicit = cppnode.isExplicit()?1:0;}catch(NullPointerException exc) { }
		try{isFinal = cppnode.isFinal()?1:0;}catch(NullPointerException exc) { }
		try{isImplicit = cppnode.isImplicit()?1:0;}catch(NullPointerException exc) { }
		try{isOverride = cppnode.isOverride()?1:0;}catch(NullPointerException exc) { }
		try{isPureVirtual = cppnode.isPureVirtual()?1:0;}catch(NullPointerException exc) { }
		try{isVirtual = cppnode.isVirtual()?1:0;}catch(NullPointerException exc) { }
		
		String funcTypeTable = ""; //$NON-NLS-1$
		try{funcTypeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String declaredTypTable = ""; //$NON-NLS-1$
		try{declaredTypTable = declaredTyp.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int funcTypeTableSubId = 0;		
		try {funcTypeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int declaredTypTableSubId = 0;		
		try {declaredTypTableSubId = (int) ((PDOMNode) declaredTyp).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;		
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPMethodTemplate(ID, name,owner,ownerVariable,
				funcTypeTable,funcTypeTableSubId,
				declaredTypTable,declaredTypTableSubId,
				functionScopeTable,functionScopeTableSubId,
				hasParameterPack,
				requiredArgumentCount,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
				isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual,debug))
			return;		
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		mapCPPNode(declaredTyp, depth+1, ID,"declaredTyp", debug); //$NON-NLS-1$		
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$		
		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}
		
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}
		

		if(allInstances != null)
		for(int i = 0; i < allInstances.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allInstances[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateInstance(ArrayID, ID, "allInstances", allInstances[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allInstances[i], depth+1, ArrayID,"allInstances", debug); //$NON-NLS-1$
		}
		

		if(templateParameters != null)
		for(int i = 0; i < templateParameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateParameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIPDOMCPPTemplateParameter(ArrayID, ID, "templateParameters", templateParameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateParameters[i], depth+1, ArrayID,"templateParameters", debug); //$NON-NLS-1$
		}		
	}

	private void mapPDOMCPPFunctionTemplate(Object node, int depth, int owner, String ownerVariable,boolean debug) throws SQLException {
		
		PDOMCPPFunctionTemplate cppnode = (PDOMCPPFunctionTemplate) node;	
		
		int ID = (int) cppnode.getRecord();
		String name = null;
		ICPPFunctionType declaredTyp = null;
		IType[] exceptionSpecification = null;
		IScope functionScope = null;
		ICPPFunctionType 	type = null;			
		ICPPParameter[] 	parameters = null;
		ICPPTemplateInstance[] allInstances = null; //--//
		IPDOMCPPTemplateParameter[] templateParameters = null; //--//
		int 				requiredArgumentCount = 0;
		int 				isAuto = 0;
		int 				isConstExpr = 0;
		int 				isDeleted = 0;
		int 				isExtern = 0;
		int 				isExternC = 0;
		int 				isInline = 0;
		int 				isMutable = 0;
		int 				isNoReturn = 0;
		int 				isRegister = 0;
		int 				isStatic = 0;
		int hasParameterPack  = 0;		
		
		try{name = cppnode.getName();}catch(NullPointerException exc) { }
		try{declaredTyp = cppnode.getDeclaredType();}catch(NullPointerException exc) { }
		try{exceptionSpecification = cppnode.getExceptionSpecification();}catch(NullPointerException exc) { }
		try{functionScope = cppnode.getFunctionScope();}catch(NullPointerException exc) { }
		try{type = cppnode.getType();}catch(NullPointerException exc) { }
		try{parameters = cppnode.getParameters();}catch(NullPointerException exc) { }
		try{allInstances = cppnode.getAllInstances();}catch(NullPointerException exc) { }
		try{templateParameters = cppnode.getTemplateParameters();}catch(NullPointerException exc) { }		
		try{requiredArgumentCount = cppnode.getRequiredArgumentCount();}catch(NullPointerException exc) { }
		try{isAuto = cppnode.isAuto()?1:0;}catch(NullPointerException exc) { }
		try{isConstExpr = cppnode.isConstexpr()?1:0;}catch(NullPointerException exc) { }
		try{isDeleted = cppnode.isDeleted()?1:0;}catch(NullPointerException exc) { }
		try{isExtern = cppnode.isExtern()?1:0;}catch(NullPointerException exc) { }
		try{isExternC = cppnode.isExternC()?1:0;}catch(NullPointerException exc) { }
		try{isInline = cppnode.isInline()?1:0;}catch(NullPointerException exc) { }
		try{isMutable = cppnode.isMutable()?1:0;}catch(NullPointerException exc) { }
		try{isNoReturn = cppnode.isNoReturn()?1:0;}catch(NullPointerException exc) { }
		try{isRegister = cppnode.isRegister()?1:0;}catch(NullPointerException exc) { }
		try{isStatic = cppnode.isStatic()?1:0;}catch(NullPointerException exc) { }
		try{hasParameterPack = cppnode.hasParameterPack()?1:0;}catch(NullPointerException exc) { }

		String funcTypeTable = ""; //$NON-NLS-1$
		try{funcTypeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String declaredTypTable = ""; //$NON-NLS-1$
		try{declaredTypTable = declaredTyp.getClass().getSimpleName();}catch(NullPointerException exc) { }
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) { }
		
		int funcTypeTableSubId = 0;		
		try {funcTypeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int declaredTypTableSubId = 0;		
		try {declaredTypTableSubId = (int) ((PDOMNode) declaredTyp).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		int functionScopeTableSubId = 0;		
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCPPFunctionTemplate(ID, name,owner,ownerVariable,
				funcTypeTable,funcTypeTableSubId,
				declaredTypTable,declaredTypTableSubId,
				functionScopeTable,functionScopeTableSubId,
				hasParameterPack,
				requiredArgumentCount,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,debug))
			return;		
		
		mapCPPNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		mapCPPNode(declaredTyp, depth+1, ID,"declaredTyp", debug); //$NON-NLS-1$		
		
		mapCPPNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$		
		
		if(exceptionSpecification != null)
		for(int i = 0; i < exceptionSpecification.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) exceptionSpecification[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "exceptionSpecification", exceptionSpecification[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(exceptionSpecification[i], depth+1, ArrayID,"exceptionSpecification", debug); //$NON-NLS-1$
		}
		
		
		if(parameters != null)
		for(int i = 0; i < parameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) parameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPParameter(ArrayID, ID, "parameters", parameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(parameters[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}
		

		if(allInstances != null)
		for(int i = 0; i < allInstances.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) allInstances[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertICPPTemplateInstance(ArrayID, ID, "allInstances", allInstances[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(allInstances[i], depth+1, ArrayID,"allInstances", debug); //$NON-NLS-1$
		}
		

		if(templateParameters != null)
		for(int i = 0; i < templateParameters.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;		
			try {tableRefSubId = (int) ((PDOMNode) templateParameters[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIPDOMCPPTemplateParameter(ArrayID, ID, "templateParameters", templateParameters[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCPPNode(templateParameters[i], depth+1, ArrayID,"templateParameters", debug); //$NON-NLS-1$
		}		
	}
	
	private void mapPDOMCEnumeration(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCEnumeration cnode = (PDOMCEnumeration) node;	
		
		int 						ID = (int) cnode.getRecord();
		String name = ""; //$NON-NLS-1$
		IEnumerator[] enumerators = null;
		long minValue = 0;
		long maxValue = 0;
						
		try { name = cnode.getName();}catch(NullPointerException exc) {}
		try { enumerators = cnode.getEnumerators();}catch(NullPointerException exc) {}
		try { minValue = cnode.getMinValue();}catch(NullPointerException exc) {}
		try { maxValue = cnode.getMaxValue();}catch(NullPointerException exc) {}		
				
		if(controll.InsertPDOMCEnumeration(ID,name,owner,ownerVariable,minValue,maxValue,
				debug))
			return;
		
		if(enumerators != null)
		for(int i = 0; i < enumerators.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;
			try {tableRefSubId = (int) ((PDOMNode) enumerators[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIEnumerator(ArrayID, ID, "enumerators", enumerators[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCNode(enumerators[i], depth+1, ArrayID,"enumerators", debug); //$NON-NLS-1$
		}	
	}

	@SuppressWarnings("deprecation")
	private void mapPDOMCEnumerator(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		
		PDOMCEnumerator cnode = (PDOMCEnumerator) node;
		
		int 						ID = (int) cnode.getRecord();
		String name = ""; //$NON-NLS-1$
		IType typ = null;
		long value = 0;
		
		try { name = cnode.getName();}catch(NullPointerException exc) {}
		try { typ = cnode.getType();}catch(NullPointerException exc) {}
		try { value = cnode.getValue().numericalValue();}catch(NullPointerException exc) {}
		
		String TypeTable = ""; //$NON-NLS-1$
		try{TypeTable = typ.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typeTableSubId = 0;
		try {typeTableSubId = (int) ((PDOMNode) typ).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCEnumerator(ID,name,owner,ownerVariable,TypeTable,typeTableSubId,value,
				debug))
			return;
		
		mapCNode(typ, depth+1, ID,"type", debug); //$NON-NLS-1$		
	}
	
	@SuppressWarnings("deprecation")
	private void mapPDOMCField(Object node,int depth, int owner, String ownerVariable, boolean debug)throws SQLException {
		
		PDOMCField cnode = (PDOMCField) node;
		
		int ID = (int) cnode.getRecord();
		String name = ""; //$NON-NLS-1$
		long initialValue = 0;
		IType typ = null;
		ICompositeType compositeTypOwner = null;
		int isAuto = 0;
		int isExtern = 0;
		int isRegister = 0;
		int isStatic = 0;
		
		try { name = cnode.getName();}catch(NullPointerException exc) {}
		try { initialValue = cnode.getInitialValue().numericalValue();}catch(NullPointerException exc) {}
		try { typ = cnode.getType();}catch(NullPointerException exc) {}
		try { compositeTypOwner = cnode.getCompositeTypeOwner();}catch(NullPointerException exc) {}
		try { isAuto = cnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try { isExtern = cnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try { isRegister = cnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try { isStatic = cnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		
		String TypeTable = ""; //$NON-NLS-1$
		try{TypeTable = typ.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String compositeTypOwnereTable = ""; //$NON-NLS-1$
		try{compositeTypOwnereTable = compositeTypOwner.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int compositeTypOwnereTableSubId = 0;
		int typTableSubId = 0;
		try {compositeTypOwnereTableSubId = (int) ((PDOMNode) compositeTypOwner).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		try {typTableSubId = (int) ((PDOMNode) typ).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCField(ID,name,owner,ownerVariable,initialValue,TypeTable,typTableSubId,compositeTypOwnereTable,compositeTypOwnereTableSubId,
				isAuto,isExtern,isRegister,isStatic,
				debug))
			return;
		
		mapCNode(typ, depth+1, ID,"type", debug); //$NON-NLS-1$		
		
		mapCNode(compositeTypOwner, depth+1, ID,"compositeTypeOwner", debug); //$NON-NLS-1$
	}

	private void mapPDOMCFunction(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException{
		
		PDOMCFunction cnode = (PDOMCFunction) node;
		
		int 						ID = (int) cnode.getRecord();
		String name = ""; //$NON-NLS-1$
		IScope functionScope = null;
		IParameter[] parameter = null;
		IFunctionType type = null;
		int isAuto = 0;
		int isExtern = 0;
		int isInline = 0;
		int isNoReturn = 0;
		int isRegister = 0;
		int isStatic = 0;
		int takesVarArgs = 0;
		
		try { name = cnode.getName();}catch(NullPointerException exc) {}
		try { functionScope = cnode.getFunctionScope();}catch(NullPointerException exc) {}
		try { parameter = cnode.getParameters();}catch(NullPointerException exc) {}
		try { type = cnode.getType();}catch(NullPointerException exc) {}
		try { isAuto = cnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try { isExtern = cnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try { isInline = cnode.isInline()?1:0;}catch(NullPointerException exc) {}
		try { isNoReturn = cnode.isNoReturn()?1:0;}catch(NullPointerException exc) {}
		try { isRegister = cnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try { isStatic = cnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		try { takesVarArgs = cnode.takesVarArgs()?1:0;}catch(NullPointerException exc) {}
		
		String functionScopeTable = ""; //$NON-NLS-1$
		try{functionScopeTable = functionScope.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int functionScopeTableSubId = 0;
		int typeTableSubId = 0;
		
		try {functionScopeTableSubId = (int) ((PDOMNode) functionScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCFunction(ID,name,owner,ownerVariable,functionScopeTable,functionScopeTableSubId,typeTable,typeTableSubId,
				isAuto,isExtern,isInline,isNoReturn,isRegister,isStatic,takesVarArgs,
				debug))
			return;
		
		mapCNode(functionScope, depth+1, ID,"functionScope", debug); //$NON-NLS-1$
		
		if(parameter != null)
		for(int i = 0; i < parameter.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;
			try {tableRefSubId = (int) ((PDOMNode) parameter[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIParameter(ArrayID, ID, "parameters", parameter[i].getClass().getSimpleName(),tableRefSubId,i, debug); //$NON-NLS-1$
			mapCNode(parameter[i], depth+1, ArrayID,"parameters", debug); //$NON-NLS-1$
		}
				
		mapCNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
	}
	
	@SuppressWarnings("deprecation")
	private void mapPDOMCParameter(Object node, int depth, int owner, String ownerVariable, boolean debug)throws SQLException {
		
		PDOMCParameter cnode = (PDOMCParameter) node;
		
		int 						ID = (int) cnode.getRecord();
		String name = ""; //$NON-NLS-1$
		long initialValue = 0;
		IIndexScope scope = null;
		IType type = null;
		int hasDeclaration = 0;
		int hasDefinition = 0;
		int isAuto = 0;
		int isExtern = 0;
		int isRegister = 0;
		int isStatic = 0;
		
		try { name = cnode.getName();}catch(NullPointerException exc) {}
		try { initialValue = cnode.getInitialValue().numericalValue();}catch(NullPointerException exc) {}
		try { scope = cnode.getScope();}catch(NullPointerException exc) {}
		try { type = cnode.getType();}catch(NullPointerException exc) {}
		try { hasDeclaration = cnode.hasDeclaration()?1:0;}catch(NullPointerException | CoreException e) {}
		try { hasDefinition = cnode.hasDefinition()?1:0;}catch(NullPointerException | CoreException e) {}
		try { isAuto = cnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try { isExtern = cnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try { isRegister = cnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try { isStatic = cnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		
		String scopeTable = ""; //$NON-NLS-1$
		try{scopeTable = scope.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String typTable = ""; //$NON-NLS-1$
		try{typTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int scopeTableSubId = 0;
		int typeTableSubId = 0;
		
		try {scopeTableSubId = (int) ((PDOMNode) scope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCParameter(ID,name,owner,ownerVariable,initialValue,scopeTable,scopeTableSubId,typTable,typeTableSubId,hasDeclaration,hasDefinition,
				isAuto,isExtern,isRegister,isStatic,
				debug))
			return;
		
		mapCNode(scope, depth+1, ID,"scope", debug); //$NON-NLS-1$
		
		mapCNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
	}

	private void mapPDOMCStructure(Object node, int depth, int owner, String ownerVariable, boolean debug)throws SQLException {
		
		PDOMCStructure cnode = (PDOMCStructure) node;
		
		int 						ID = (int) cnode.getRecord();
		String name = ""; //$NON-NLS-1$
		IScope compositeScope = null;
		ICompositeType compositeType = null;
		IField[] fields = null;
		IIndexBinding scopeBinding = null;
		int isAnonymous = 0;
		
		try { name = cnode.getName();}catch(NullPointerException exc) {}
		try { compositeScope = cnode.getCompositeScope();}catch(NullPointerException exc) {}
		try { compositeType = cnode.getCompositeType();}catch(NullPointerException exc) {}
		try { fields = cnode.getFields();}catch(NullPointerException exc) {}
		try { scopeBinding = cnode.getScopeBinding();}catch(NullPointerException exc) {}
		try { isAnonymous = cnode.isAnonymous()?1:0;}catch(NullPointerException exc) {}
		
		String compositeScopeTable = ""; //$NON-NLS-1$
		try{compositeScopeTable = compositeScope.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String compositeTypTable = ""; //$NON-NLS-1$
		try{compositeTypTable = compositeType.getClass().getSimpleName();}catch(NullPointerException exc) {}
		String scopeBindingTable = ""; //$NON-NLS-1$
		try{scopeBindingTable = scopeBinding.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int compositeScopeSubId = 0;
		int compositeTypeSubId = 0;
		int scopeBindingTSubId = 0;
		
		try {compositeScopeSubId = (int) ((PDOMNode) compositeScope).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		try {compositeTypeSubId = (int) ((PDOMNode) compositeType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		try {scopeBindingTSubId = (int) ((PDOMNode) scopeBinding).getRecord();}catch(NullPointerException | ClassCastException exc) { }		
		
		if(controll.InsertPDOMCStructure(ID,name,owner,ownerVariable,compositeScopeTable,compositeScopeSubId,compositeTypTable,compositeTypeSubId,scopeBindingTable,scopeBindingTSubId,isAnonymous,
				debug))
			return;
		
		mapCNode(compositeScope, depth+1, ID,"compositeScope", debug); //$NON-NLS-1$
		
		mapCNode(compositeType, depth+1, ID,"compositeType", debug); //$NON-NLS-1$
		
		if(fields != null)
		for(int i = 0; i < fields.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;
			try {tableRefSubId = (int) ((PDOMNode) fields[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }	
			controll.InsertIField(ArrayID, ID, "fields", fields[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCNode(fields[i], depth+1, ArrayID,"fields", debug); //$NON-NLS-1$
		}
		
		mapCNode(scopeBinding, depth+1, ID,"scopeBinding", debug); //$NON-NLS-1$
	}

	private void mapPDOMCTypedef(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException{
		
		PDOMCTypedef cnode = (PDOMCTypedef) node;
		
		int 						ID = (int) cnode.getRecord();
		String name = ""; //$NON-NLS-1$
		IType typ = null;
		
		try { name = cnode.getName();}catch(NullPointerException exc) {}
		try { typ = cnode.getType();}catch(NullPointerException exc) {}
		
		String typTable = ""; //$NON-NLS-1$
		try{typTable = typ.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typTableSubId = 0;
		try {typTableSubId = (int) ((PDOMNode) typ).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCTypedef(ID,name,owner,ownerVariable,typTable,typTableSubId,
				debug))
			return;
		
		mapCNode(typ, depth+1, ID,"type", debug); //$NON-NLS-1$
	}

	@SuppressWarnings("deprecation")
	private void mapPDOMCVariable(Object node, int depth, int owner, String ownerVariable, boolean debug)throws SQLException {
		
		PDOMCVariable cnode = (PDOMCVariable) node;
		
		int ID = (int) cnode.getRecord();
		String name = ""; //$NON-NLS-1$
		long initialValue = 0;
		IType type = null;
		int isAuto = 0;
		int isExtern = 0;
		int isRegister = 0;
		int isStatic = 0;
		
		try { name = cnode.getName();}catch(NullPointerException exc) {}
		try { initialValue = cnode.getInitialValue().numericalValue();}catch(NullPointerException exc) {}
		try { type = cnode.getType();}catch(NullPointerException exc) {}
		try { isAuto = cnode.isAuto()?1:0;}catch(NullPointerException exc) {}
		try { isExtern = cnode.isExtern()?1:0;}catch(NullPointerException exc) {}
		try { isRegister = cnode.isRegister()?1:0;}catch(NullPointerException exc) {}
		try { isStatic = cnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		
		String typTable = ""; //$NON-NLS-1$
		try{typTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typeTableSubId = 0;

		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertPDOMCVariable(ID,name,owner,ownerVariable,initialValue,typTable,typeTableSubId,isAuto,isExtern,isRegister,isStatic,
				debug))
			return;
		
		mapCNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
	}

	@SuppressWarnings({ "deprecation" })
	private void mapCArrayType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException{
		
		CArrayType cnode = (CArrayType) node;
		
		int ID = getuniqueID();
		long size = 0;
		IType typ = null;
		int hasSize = 0;
		int isConst = 0;
		int isRestrict = 0;
		int isStatic = 0;
		int isVariableLength = 0;
		int isVolatile = 0;
		
		try { size = cnode.getSize().numericalValue();}catch(NullPointerException exc) {}
		try { typ = cnode.getType();}catch(NullPointerException exc) {}
		try { hasSize = cnode.hasSize()?1:0;}catch(NullPointerException exc) {}
		try { isConst = cnode.isConst()?1:0;}catch(NullPointerException exc) {}
		try { isRestrict = cnode.isRestrict()?1:0;}catch(NullPointerException exc) {}
		try { isStatic = cnode.isStatic()?1:0;}catch(NullPointerException exc) {}
		try { isVariableLength = cnode.isVariableLength()?1:0;}catch(NullPointerException exc) {}
		try { isVolatile = cnode.isVolatile()?1:0;}catch(NullPointerException exc) {}		
		
		String typTable = ""; //$NON-NLS-1$
		try{typTable = typ.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typTableSubId = 0;
		try {typTableSubId = (int) ((PDOMNode) typ).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCArrayType(ID,"",owner,ownerVariable,typTable,typTableSubId,size,hasSize,isConst,isRestrict,isStatic,isVariableLength,isVolatile, //$NON-NLS-1$
				debug))
			return;
		
		mapCNode(typ, depth+1, ID,"type", debug); //$NON-NLS-1$
	}

	private void mapCBasicType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException{
		
		CBasicType cnode = (CBasicType) node;
		
		int ID = getuniqueID();
		String name = cnode.toString();
		
		if(controll.InsertCBasicType(ID,name,owner,ownerVariable,
				debug))
			return;		
	}

	private void mapCFunctionType(Object node, int depth, int owner, String ownerVariable, boolean debug)throws SQLException {
		
		CFunctionType cnode = (CFunctionType) node;
		
		int ID = getuniqueID();
		IType[] parameterTypes = null;
		IType returnType = null;
		int takesVarArgs = 0;
		
		try { parameterTypes = cnode.getParameterTypes();}catch(NullPointerException exc) {}
		try { returnType = cnode.getReturnType();}catch(NullPointerException exc) {}
		try { takesVarArgs = cnode.takesVarArgs()?1:0;}catch(NullPointerException exc) {}
		
		String returnTypeTable = ""; //$NON-NLS-1$
		try{returnTypeTable = returnType.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int returnTypeTableSubId = 0;
		try {returnTypeTableSubId = (int) ((PDOMNode) returnType).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCFunctionType(ID,"",owner,ownerVariable,returnTypeTable,returnTypeTableSubId,takesVarArgs, //$NON-NLS-1$
				debug))
			return;
		
		if(parameterTypes != null)
		for(int i = 0; i < parameterTypes.length;i++) {
			int ArrayID = getuniqueID();
			int tableRefSubId = 0;
			try {tableRefSubId = (int) ((PDOMNode) parameterTypes[i]).getRecord();}catch(NullPointerException | ClassCastException exc) { }
			controll.InsertIType(ArrayID, ID, "parameterTypes", parameterTypes[i].getClass().getSimpleName(),tableRefSubId, i,debug); //$NON-NLS-1$
			mapCNode(parameterTypes[i], depth+1, ArrayID,"parameterTypes", debug); //$NON-NLS-1$
		}
				
		mapCNode(returnType, depth+1, ID,"returnType", debug); //$NON-NLS-1$
	}

	private void mapCPointerType(Object node, int depth, int owner, String ownerVariable, boolean debug)throws SQLException {
		
		CPointerType cnode = (CPointerType) node;
		
		int ID = getuniqueID();
		IType type = null;
		int isConst = 0;
		int isRestrict = 0;
		int isVolatile = 0;
		
		try { type = cnode.getType();}catch(NullPointerException exc) {}
		try { isConst = cnode.isConst()?1:0;}catch(NullPointerException exc) {}
		try { isRestrict = cnode.isRestrict()?1:0;}catch(NullPointerException exc) {}
		try { isVolatile = cnode.isVolatile()?1:0;}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typeTableSubId = 0;
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCPointerType(ID,"",owner,ownerVariable,typeTable,typeTableSubId,isConst,isRestrict,isVolatile, //$NON-NLS-1$
				debug))
			return;
		
		mapCNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
	}

	private void mapCQualifierType(Object node, int depth, int owner, String ownerVariable, boolean debug)throws SQLException {
		
		CQualifierType cnode = (CQualifierType) node;
		
		int ID = getuniqueID();
		IType type = null;
		int isConst = 0;
		int isRestrict = 0;
		int isVolatile = 0;
		
		try { type = cnode.getType();}catch(NullPointerException exc) {}
		try { isConst = cnode.isConst()?1:0;}catch(NullPointerException exc) {}
		try { isRestrict = cnode.isRestrict()?1:0;}catch(NullPointerException exc) {}
		try { isVolatile = cnode.isVolatile()?1:0;}catch(NullPointerException exc) {}
		
		String typeTable = ""; //$NON-NLS-1$
		try{typeTable = type.getClass().getSimpleName();}catch(NullPointerException exc) {}
		
		int typeTableSubId = 0;
		try {typeTableSubId = (int) ((PDOMNode) type).getRecord();}catch(NullPointerException | ClassCastException exc) { }
		
		if(controll.InsertCQualifierType(ID,"",owner,ownerVariable,typeTable,typeTableSubId,isConst,isRestrict,isVolatile, //$NON-NLS-1$
				debug))
			return;
		
		mapCNode(type, depth+1, ID,"type", debug); //$NON-NLS-1$
		
	}

	private void mapProblemType(Object node, int depth, int owner, String ownerVariable, boolean debug) throws SQLException {
		controll.InsertProblemType(getuniqueID(), debug);		
	}

	
}
