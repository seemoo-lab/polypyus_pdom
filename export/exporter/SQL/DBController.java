package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBController {
    
    private Connection connection;
    private String DB_PATH;
    private HashMap<Integer, String> inDatabase;

    
    public DBController(String Path){
    	this.DB_PATH = Path;
    	this.initDBConnection();
    	this.inDatabase = new HashMap<Integer,String>();	
    }
    
        
    private void initDBConnection() {
        
       try {
            Class.forName("org.sqlite.JDBC"); //$NON-NLS-1$
       } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des JDBC-Treibers"); //$NON-NLS-1$
            e.printStackTrace();
       }
                
        try {
            if (connection != null)
                return;
            System.out.println("Creating Connection to Database..."); //$NON-NLS-1$
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH); //$NON-NLS-1$
            if (!connection.isClosed()) {
            	 System.out.println("...Connection established"); //$NON-NLS-1$  
            	 PreparedStatement ps = connection.prepareStatement("CREATE TABLE \"TEST\" ('ID' INT);"); //$NON-NLS-1$
            	 ps.execute();
            	 ps = connection.prepareStatement("DROP TABLE \"TEST\";");//$NON-NLS-1$
            	 ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
            
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
			public void run() {
                try {
                    if (!connection.isClosed() && connection != null) {
                        connection.close();
                        if (connection.isClosed())
                            System.out.println("Connection to Database closed"); //$NON-NLS-1$
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void closeConnection() throws SQLException {
    	this.connection.close();
    }
    
    public static DBController createNewDatabase(String Path, boolean dropTable) throws SQLException {
    	
    	String url = "jdbc:sqlite:" + Path; //$NON-NLS-1$
    	
    	Connection conn = DriverManager.getConnection(url);    	
        conn.getMetaData();  
        PreparedStatement ps;
                
               if(dropTable) {
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CArrayType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CBasicType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CFunctionType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPointerType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CQualifierType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCEnumeration\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCEnumerator\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCField\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCFunction\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCParameter\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCStructure\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCTypedef\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCVariable\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ProblemTypeC\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"TypeParameter\";" ); //$NON-NLS-1$
	            	ps.execute(); 
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"BTreeIndex\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPArrayType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPBasicType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPClassInstance\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPClassSpecializationScope\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPConstructorSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPFieldSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPFunctionType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPMethodSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPParameterPackType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPParameterSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPPointerType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPQualifierType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPReferenceType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPTemplateTypeArgument\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPTypedefSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"IBinding\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPBase\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPClassTemplatePartialSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPClassType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPConstructor\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPField\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPMethod\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPNamespaceScope\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPParameter\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPTemplateArgument\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPTemplateInstance\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ICPPUsingDeclaration\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"IEnumerator\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"IField\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"IPDOMCPPTemplateParameter\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"IType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPAliasTemplate\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPClassInstance\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPClassSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPClassTemplate\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPClassTemplatePartialSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPClassType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPConstructor\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPDeferredClassInstance\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPEnumeration\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPEnumerator\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPField\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPFieldSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPFunction\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPFunctionInstance\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPMethod\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPMethodSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPMethodInstance\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPMethodTemplateSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPNamespace\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPNamespaceAlias\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPParameter\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPParameterSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPTemplateNonTypeParameter\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPTemplateTemplateParameter\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPTemplateTypeParameter\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPFunctionTemplate\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPMethodTemplate\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPConstructorTemplate\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPTypedef\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPTypedefSpecialization\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPUnknownMemberClass\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPUnknownMemberClassInstance\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPUnknownMember\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPUnknownMethod\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPUsingDeclaration\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPVariable\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ProblemBinding\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"ProblemType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"TypeOfDependentExpression\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	//------//
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPBase\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCStructure\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPClassScope\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPClassSpecializationScope\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCField\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCEnumerator\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCEnumeration\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCFunction\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"IParameter\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CFunctionType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCVariable\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCParameter\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CBasicType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCTypedef\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPointerType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CArrayType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CQualifierType\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"PDOMCPPGlobalScope\";" ); //$NON-NLS-1$
	            	ps.execute();
	            	ps = conn.prepareStatement("DROP TABLE IF EXISTS \"CPPTemplateNonTypeArgument\";" ); //$NON-NLS-1$
	            	ps.execute();	            	
               }
               
               try {
            	   
            	   ps = conn.prepareStatement("SELECT * FROM `BTreeIndex`;"); //$NON-NLS-1$
                   ps.execute();  
                   System.out.println("Database exists."); //$NON-NLS-1$
            	   
               }catch(SQLException e){
             
               
                ps = conn.prepareStatement("CREATE TABLE \"CPPArrayType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT,`TypeTable` TEXT,`TypeTableSubId` INTEGER, `Size` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPBasicType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPClassInstance\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `CompositeScopeTable` TEXT,`CompositeScopeTableSubId` INTEGER, `SpecializedBindingTable` TEXT,`SpecializedBindingTableSubId` INTEGER, `TemplateDefinitionTable` TEXT,`TemplateDefinitionTableSubId` INTEGER, `isAnonym` INTEGER, `isFinal` INTEGER, `isExplicitSpecialization` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPClassSpecializationScope\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `ClassTypeTable` TEXT,`ClassTypeTableSubId` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPConstructorSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER,  `FunctionScopeTable` TEXT,`FunctionScopeTableSubId` INTEGER,  `TypeTable` TEXT,`TypeTableSubId` INTEGER,  `ClassOwnerTable` TEXT,`ClassOwnerTableSubId` INTEGER,  `SpecializedBindingTable` TEXT,`SpecializedBindingTableSubId` INTEGER,  `requiredArgumentCount` INTEGER, `hasParameterPack` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `isDestructor` INTEGER, `isExplicit` INTEGER, `isFinal` INTEGER, `isImplicit` INTEGER, `isOverride` INTEGER, `isPureVirtual` INTEGER, `isVirtual` INTEGER, `takesVarArgs` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPFieldSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `InitialValue` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isMutable` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `fieldPosition` INTEGER, `visibility` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPFunctionType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `ReturnTypeTable` TEXT,`ReturnTypeTableSubId` INTEGER, `ThisTypeTable` TEXT,`ThisTypeTableSubId` INTEGER, `hasRefQualifier` INTEGER, `isConst` INTEGER, `isRValueReference` INTEGER, `isVolatile` INTEGER, `takesVarArgs` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPMethodSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `FunctionScopeTable` TEXT,`FunctionScopeTableSubId` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `ClassOwnerTable` TEXT,`ClassOwnerTableSubId` INTEGER, `SpecializedBindingTable` TEXT,`SpecializedBindingTableSubId` INTEGER, `requiredArgumentCount` INTEGER, `hasParameterPack` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `isDestructor` INTEGER, `isExplicit` INTEGER, `isFinal` INTEGER, `isImplicit` INTEGER, `isOverride` INTEGER, `isPureVirtual` INTEGER, `isVirtual` INTEGER, `takesVarArgs` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPParameterPackType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT, `TypeTableSubId` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPParameterSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `DefaultValue` INTEGER, `InitialValue` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isMutable` INTEGER, `isParameterPack` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPPointerType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `isConst` INTEGER, `isRestrict` INTEGER, `isVolatile` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPQualifierType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `isConst` INTEGER, `isVolatile` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPReferenceType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `isRValueReference` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPTemplateTypeArgument\" ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `NonTypeValue` INTEGER, `ExpansionPatternTable` TEXT,`ExpansionPatternTableSubId` INTEGER, `NonTypeEvaluationTable` TEXT,`NonTypeEvaluationTableSubId` INTEGER, `TypeOfNonTypeValueTable` TEXT,`TypeOfNonTypeValueTableSubId` INTEGER, `TypeValueTable` TEXT ,`TypeValueTableSubId` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPTemplateNonTypeArgument\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `NonTypeValue` INTEGER, `ExpansionPatternTable` TEXT,`ExpansionPatternTableSubId` INTEGER, `NonTypeEvaluationTable` TEXT,`NonTypeEvaluationTableSubId` INTEGER, `OriginalTypeValueTable` TEXT,`OriginalTypeValueTableSubId` INTEGER,`TypeOfNonTypeValueTable` TEXT,`TypeOfNonTypeValueTableSubId` INTEGER, `TypeValueTable` TEXT,`TypeValueTableSubId` INTEGER, `isNonTypeValue` INTEGER, `isPackExpansion` INTEGER, `isTypeValue` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPPTypedefSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"IBinding\" ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPBase`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPClassTemplatePartialSpecialization`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPClassType` ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPConstructor`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPField`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPMethod`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPNamespaceScope`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPParameter`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPTemplateArgument`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPTemplateInstance`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ICPPUsingDeclaration`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `IEnumerator`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER,  `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `IField`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER,  `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `IParameter`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `IPDOMCPPTemplateParameter`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `IType`  ( `ID` INTEGER, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT,`TableRefSubId` INTEGER, `ArrayPos` INTEGER, PRIMARY KEY(`ID`))"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"BTreeIndex\" ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT, `Owner` INTEGER, `OwnerVariable` TEXT, `TableRef` TEXT, `TableRefSubId` INTEGER)"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPAliasTemplate\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TemplateTypeTable` TEXT,`TemplateTypeTableSubId` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPBase\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `BaseClassTable` TEXT,`BaseClassTableSubId` INTEGER, `BaseClassTypeTable` TEXT,`BaseClassTypeTableSubId` INTEGER, `isInheritedConstructorsSource` INTEGER, `isVirtual` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPClassInstance\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `CompositeScopeTable` TEXT,`CompositeScopeTableSubId` INTEGER, `SpecializedBindingTable` TEXT,`SpecializedBindingTableSubId` INTEGER, `TemplateDefinitionTable` TEXT,`TemplateDefinitionTableSubId` INTEGER, `isFinal` INTEGER, `isAnonymous` INTEGER, `isExplicitSpecialization` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPClassSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `CompositeScopeTable` TEXT,`CompositeScopeTableSubId` INTEGER,  `SpecializedBindingTable` TEXT,`SpecializedBindingTableSubId` INTEGER,  `isFinal` INTEGER, `isAnonymous` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPClassTemplate\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `CompositeScopeTable` TEXT,`CompositeScopeTableSubId` INTEGER,`isAnonymous` INTEGER,`isFinal` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPClassTemplatePartialSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `CompositeScopeTable` TEXT,`CompositeScopeTableSubId` INTEGER, `PrimaryClassTemplate` TEXT,`PrimaryClassTemplateSubId` INTEGER, `PrimaryTemplate` TEXT,`PrimaryTemplateSubId` INTEGER, `isAnonymous` INTEGER,`isFinal` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPClassType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `CompositeScopeTable` TEXT, `CompositeScopeTableSubId` INTEGER, `isFinal` INTEGER, `isAnonymous` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPConstructor\" (`ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `FunctionScope` TEXT, `FunctionScopeSubId` INTEGER, `hasParameterPack` INTEGER,`requiredArgumentCount` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `isDestructor` INTEGER, `isExplicit` INTEGER, `isFinal` INTEGER, `isImplicit` INTEGER, `isOverride` INTEGER, `isPureVirtual` INTEGER, `isVirtual` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPClassScope\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `ClassTypeTable` TEXT,`ClassTypeTableSubId` INTEGER, `ScopeBindingTable` TEXT, `ScopeBindingTableSubId` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPClassSpecializationScope\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `ClassTypeTable` TEXT,`ClassTypeTableSubId` INTEGER, `OriginalClassTypeTable` TEXT,`OriginalClassTypeTableSubId` INTEGER, `ScopeBindingTable` TEXT,`ScopeBindingTableSubId` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPDeferredClassInstance\" ( `ID` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPEnumeration\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `MinValue` INTEGER, `MaxValue` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPEnumerator\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `InternalTypeTable` TEXT,`InternalTypeTableSubId` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `Value` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPField\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `Value` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `ClassOwnerTable` TEXT,`ClassOwnerTableSubId` INTEGER,  `fieldPosition` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isMutable` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPFieldSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `Value` INTEGER, `TypeTable` TEXT, `TypeTableSubId` INTEGER, `ClassOwnerTable` TEXT,`ClassOwnerTableSubId` INTEGER, `fieldPosition` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isMutable` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPFunction\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `FunctionScope` TEXT,`FunctionScopeSubId` INTEGER,  `hasParameterPack` INTEGER,`requiredArgumentCount` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPFunctionInstance\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `TypeTable` TEXT, `TypeTableSubId` INTEGER, `FunctionScopeTable` TEXT,`FunctionScopeTableSubId` INTEGER,  `TemplateDefinitionTable` TEXT, `TemplateDefinitionTableSubId` INTEGER, `requiredArgumentCount` INTEGER, `isExplicitSpecialization` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER,`hasParameterPack` INTEGER,`takesVarArgs` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPGlobalScope\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `ScopeBindingTable` TEXT,`ScopeBindingTableSubId` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPMethod\" (`ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `FunctionScope` TEXT, `FunctionScopeSubId` INTEGER,`hasParameterPack` INTEGER,`requiredArgumentCount` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `isDestructor` INTEGER, `isExplicit` INTEGER, `isFinal` INTEGER, `isImplicit` INTEGER, `isOverride` INTEGER, `isPureVirtual` INTEGER, `isVirtual` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();                
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPMethodInstance\"  ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `TypeTable` TEXT, `TypeTableSubId` INTEGER, `FunctionScopeTable` TEXT, `FunctionScopeTableSubId` INTEGER,`TemplateDefinitionTable` TEXT, `TemplateDefinitionTableSubId` INTEGER, `requiredArgumentCount` INTEGER, `isExplicitSpecialization` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER,`hasParameterPack` INTEGER,`takesVarArgs` INTEGER,`isDestructor` INTEGER, `isExplicit` INTEGER, `isFinal` INTEGER, `isImplicit` INTEGER, `isOverride` INTEGER, `isPureVirtual` INTEGER, `isVirtual` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();                
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPMethodSpecialization\" (`ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT, `TypeTableSubId` INTEGER, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `FunctionScope` TEXT, `FunctionScopeSubId` INTEGER, `hasParameterPack` INTEGER,`requiredArgumentCount` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `isDestructor` INTEGER, `isExplicit` INTEGER, `isFinal` INTEGER, `isImplicit` INTEGER, `isOverride` INTEGER, `isPureVirtual` INTEGER, `isVirtual` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute(); 
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPMethodTemplateSpecialization\" (`ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `FunctionScope` TEXT,`FunctionScopeSubId` INTEGER,  `hasParameterPack` INTEGER,`requiredArgumentCount` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `isDestructor` INTEGER, `isExplicit` INTEGER, `isFinal` INTEGER, `isImplicit` INTEGER, `isOverride` INTEGER, `isPureVirtual` INTEGER, `isVirtual` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute(); 
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPNamespace\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `NamespaceScopeTable` TEXT,`NamespaceScopeTableSubId` INTEGER, `ScopeBindingTable` TEXT,`ScopeBindingTableSubId` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPNamespaceAlias\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `BindingTable` TEXT,`BindingTableSubId` INTEGER, `NamespaceScopeTable` TEXT,`NamespaceScopeTableSubId` INTEGER,  `isInline` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPParameter\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `DefaultValue` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `ScopeTable` TEXT,`ScopeTableSubId` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isGloballyQualified` INTEGER, `isMutable` INTEGER, `isParameterPack` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPParameterSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `DefaultValue` INTEGER, `TypeTable` TEXT, `TypeTableSubId` INTEGER, `ScopeTable` TEXT, `ScopeTableSubId` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isGloballyQualified` INTEGER, `isMutable` INTEGER, `isParameterPack` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPTemplateNonTypeParameter\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `InitValue` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `DefaultValueTable` TEXT,`DefaultValueTableSubId` INTEGER, `parameterPosition` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isMutable` INTEGER, `isParameterPack` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPTemplateTemplateParameter\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `CompositeScopeTable` TEXT,`CompositeScopeTableSubId` INTEGER, `DefaultTypeTable` TEXT,`DefaultTypeTableSubId` INTEGER, `DefaultValueTable` TEXT,`DefaultValueTableSubId` INTEGER, `parameterPosition` INTEGER, `isAnonym` INTEGER, `isFinal` INTEGER, `isParameterPack` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPTemplateTypeParameter\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `DefaultTypeTable` TEXT,  `DefaultTypeTableSubId` INTEGER, `DefaultValueTable` TEXT, `DefaultValueTableSubId` INTEGER,  `parameterPosition` INTEGER, `isParameterPack` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPFunctionTemplate\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `FunctionTypeTable` TEXT,`FunctionTypeTableSubId` INTEGER, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `FunctionScopeTable` TEXT,`FunctionScopeTableSubId` INTEGER, `hasParameterPack` INTEGER,`requiredArgumentCount` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPMethodTemplate\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `FunctionTypeTable` TEXT,`FunctionTypeTableSubId` INTEGER, `DeclaredTypeTable` TEXT,`DeclaredTypeTableSubId` INTEGER, `FunctionScopeTable` TEXT, `FunctionScopeTableSubId` INTEGER, `hasParameterPack` INTEGER, `requiredArgumentCount` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `isDestructor` INTEGER, `isExplicit` INTEGER, `isFinal` INTEGER, `isImplicit` INTEGER, `isOverride` INTEGER, `isPureVirtual` INTEGER, `isVirtual` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPConstructorTemplate\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `FunctionTypeTable` TEXT, `FunctionTypeTableSubId` INTEGER, `DeclaredTypeTable` TEXT, `DeclaredTypeTableSubId` INTEGER, `FunctionScopeTable` TEXT, `FunctionScopeTableSubId` INTEGER, `hasParameterPack` INTEGER, `requiredArgumentCount` INTEGER, `isAuto` INTEGER, `isConstExpr` INTEGER, `isDeleted` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isInline` INTEGER, `isMutable` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `isDestructor` INTEGER, `isExplicit` INTEGER, `isFinal` INTEGER, `isImplicit` INTEGER, `isOverride` INTEGER, `isPureVirtual` INTEGER, `isVirtual` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPTypedef\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPTypedefSpecialization\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT, `TypeTableSubId` INTEGER,  PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `PDOMCPPUnknownMemberClass` ( `ID` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `PDOMCPPUnknownMember` ( `ID` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `PDOMCPPUnknownMemberClassInstance` ( `ID` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `PDOMCPPUnknownMethod` ( `ID` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPUsingDeclaration\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCPPVariable\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `Value` INTEGER, `TypeTable` TEXT, `TypeTableSubId` INTEGER,  `isAuto` INTEGER, `isConstExpr` INTEGER, `isExtern` INTEGER, `isExternC` INTEGER, `isMutable` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ProblemBinding` ( `ID` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `ProblemType` ( `ID` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE `TypeOfDependentExpression` ( `ID` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                
                //------------//              
                
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCStructure\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `CompositeScopeTable` TEXT, `CompositeScopeSubId` INTEGER,`CompositeTypeTable` TEXT,`CompositeTypeSubId` INTEGER, `ScopeBindingTable` TEXT,`ScopeBindingTSubId` INTEGER, `isAnonymous` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCEnumeration\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `minValue` INTEGER, `maxValue` INTEGER, PRIMARY KEY(`ID`) )"); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCEnumerator\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `Value` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCField\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `InitialValue` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `CompositeTypeOwnereTable` TEXT, `CompositeTypeOwnereSubId` INTEGER,`isAuto` INTEGER, `isExtern` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCFunction\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `FunctionScopeTable` TEXT,`FunctionScopeTableSubId` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `isAuto` INTEGER, `isExtern` INTEGER, `isInline` INTEGER, `isNoReturn` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, `takesVarArgs` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CFunctionType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `ReturnTypeTable` TEXT, `ReturnTypeTableSubId` INTEGER,`takesVarArgs` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCVariable\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `InitialValue` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `isAuto` INTEGER, `isExtern` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCParameter\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `InitialValue` INTEGER, `ScopeTable` TEXT,`ScopeTableSubId` INTEGER, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `hasDeclaration` INTEGER, `hasDefinition` INTEGER, `isAuto` INTEGER, `isExtern` INTEGER, `isRegister` INTEGER, `isStatic` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CBasicType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"PDOMCTypedef\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CPointerType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `isConst` INTEGER, `isRestrict` INTEGER, `isVolatile` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CQualifierType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `isConst` INTEGER, `isRestrict` INTEGER, `isVolatile` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                ps = conn.prepareStatement("CREATE TABLE \"CArrayType\" ( `ID` INTEGER, `Name` TEXT, `Owner` INTEGER, `OwnerVariable` TEXT, `TypeTable` TEXT,`TypeTableSubId` INTEGER, `size` INTEGER, `hasSize` INTEGER, `isConst` INTEGER, `isRestrict` INTEGER, `isStatic` INTEGER, `isVariableLength` INTEGER, `isVolatile` INTEGER, PRIMARY KEY(`ID`) ) "); //$NON-NLS-1$
                ps.execute();
                
                System.out.println("A new database has been created."); //$NON-NLS-1$
               }
               
               
                
                return new DBController(Path);                  	
    }
    
    private boolean isInTable_Debug(String Table, int ID){
	    
    	if(this.inDatabase.containsKey(ID))
    		return true;
    	else
    		return false;
    }
    
    private boolean isInTable(String Table, int ID) throws SQLException {
    	Statement stmt = connection.createStatement();
    	ResultSet rs = stmt.executeQuery("SELECT * FROM `" + Table + "` WHERE ID = " + ID + ";"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    	return rs.next();
    }
    
    

    public boolean InsertBtreeIndex(int Owner, String OwnerVariable, String TableRef,int TableRefSubId, boolean debug) throws SQLException {
    	
    	if(debug)
    		return InsertBtreeIndex_Debug(Owner, OwnerVariable,TableRef);    	
    	    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `BTreeIndex`(`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`) VALUES (?,?,?,?);"); //$NON-NLS-1$    	

        ps.setInt(1, Owner);
        ps.setString(2, OwnerVariable);
        ps.setString(3, TableRef);
        ps.setInt(4, TableRefSubId);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
    private boolean InsertBtreeIndex_Debug(int owner, String ownerVariable, String tableRef) {
        
        System.out.println("INSERT INTO `BTreeIndex`(`Owner`,`OwnerVariable`,`TableRef`) + " //$NON-NLS-1$
        		+ "VALUES (" + owner + ", " + ownerVariable + ", " +tableRef + " );"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
        return false;
	}
   
	public boolean InsertCPPArrayType(int ID, String name, int owner, String ownerVariable, String TypeTable, int TypeTableSubId, long size, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertCPPArrayType_Debug(ID,name, owner,ownerVariable,size);    	
    	
    	if(this.isInTable("CPPArrayType", ID)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPArrayType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`Size`) VALUES (?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, ID);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, TypeTable);
        ps.setInt(6, TypeTableSubId);
        ps.setLong(7, size);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertCPPArrayType_Debug(int iD, String name, int owner, String ownerVariable, long size) {
		
    	if(this.isInTable_Debug("CPPArrayType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue"); //$NON-NLS-1$
        System.out.println("INSERT INTO `CPPArrayType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`Size`)  " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ "," + size + " );"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		 
        return false;
	}

	public boolean InsertCPPBasicType(int iD, String name, int owner, String ownerVariable,boolean debug) throws SQLException {
		
		if(debug)
    		return InsertCPPBasicType_Debug(iD,name, owner,ownerVariable);    	
    	
    	if(this.isInTable("CPPArrayType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPBasicType`(`ID`,`Name`,`Owner`,`OwnerVariable`) VALUES (?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
    }
	
	private boolean InsertCPPBasicType_Debug(int iD, String name, int owner, String ownerVariable) {
    	
    	if(this.isInTable_Debug("CPPBasicType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue"); //$NON-NLS-1$
        System.out.println("INSERT INTO `CPPBasicType`(`ID`,`Name`,`Owner`,`OwnerVariable`)  " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
		 
        return false;
	}

	public boolean InsertCPPClassInstance(int iD, String name, int owner, String ownerVariable,String compositeScopeTable,int compositeScopeTableSubId, 
			String specializedBindingTable,int specializedBindingTableSubId,String templateDefinitionTable,int templateDefinitionTableSubId,
			int isAnonym,int isFinal,int isExplicitSpecialization,boolean debug) throws SQLException {
		
		if(debug)
    		return InsertCPPClassInstance_Debug(iD,name, owner,ownerVariable,compositeScopeTable,specializedBindingTable,templateDefinitionTable,isAnonym,isFinal,isExplicitSpecialization);    	
    	
    	if(this.isInTable("CPPClassInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPClassInstance`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`CompositeScopeTableSubId`,`SpecializedBindingTable`,`SpecializedBindingTableSubId`,`TemplateDefinitionTable`,`TemplateDefinitionTableSubId`,`isAnonym`,`isFinal`,`isExplicitSpecialization`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, compositeScopeTable);
        ps.setInt(6, compositeScopeTableSubId);
        ps.setString(7, specializedBindingTable);
        ps.setInt(8, specializedBindingTableSubId);
        ps.setString(9, templateDefinitionTable);
        ps.setInt(10, templateDefinitionTableSubId);
        ps.setInt(11, isAnonym);
        ps.setInt(12, isFinal);
        ps.setInt(13, isExplicitSpecialization);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
	
	private boolean InsertCPPClassInstance_Debug(int iD, String name, int owner, String ownerVariable,
			String compositeScopeTable, String specializedBindingTable, String templateDefinitionTable,
			int isAnonym, int isFinal, int isExplicitSpecialization) {
    	
    	if(this.isInTable_Debug("CPPClassInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue"); //$NON-NLS-1$
        System.out.println("\"INSERT INTO `CPPClassInstance`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`SpecializedBindingTable`,`TemplateDefinitionTable`,`isAnonym`,`isFinal`,`isExplicitSpecialization`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+compositeScopeTable+ ", "+specializedBindingTable+ ", "+templateDefinitionTable+ ", "+isAnonym+ ", "+isFinal + ", "+isExplicitSpecialization +");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		 
        return false;
	}

	public boolean InsertCPPClassSpecializationScope(int iD, String name, int owner, String ownerVariable,String classTypeTable,int classTypeTableSubId, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertCPPClassSpecializationScope_Debug(iD,name, owner,ownerVariable,classTypeTable);    	
    	
    	if(this.isInTable("CPPClassSpecializationScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPClassSpecializationScope`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ClassTypeTable`,`ClassTypeTableSubId`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, classTypeTable);
        ps.setInt(6, classTypeTableSubId);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
	
	private boolean InsertCPPClassSpecializationScope_Debug(int iD, String name, int owner, String ownerVariable, String classTypeTable) {
    	if(this.isInTable_Debug("CPPClassSpecializationScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue"); //$NON-NLS-1$
        System.out.println("INSERT INTO `CPPClassSpecializationScope`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ClassTypeTable`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+classTypeTable +");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		 
        return false;
	}

	public boolean InsertCPPConstructorSpecialization(int iD, String name, int owner,
			String ownerVariable, String declaredTypeTable,int declaredTypeTableSubId, String functionScopeTable,int functionScopeTableSubId, String typeTable, int typeTableSubId,
			String classOwnerTable,int classOwnerTableSubId, String specializedBindingTable, int specializedBindingTableSubId, int requiredArgumentCount,
			int hasParameterPack, int isAuto, int isConstExpr, int isDeleted, int isExtern, int isExternC,
			int isInline, int isMutable, int isNoReturn, int isRegister, int isStatic, int isDestructor,
			int isExplicit, int isFinal, int isImplicit, int isOverride, int isPureVirtual, int isVirtual, int takesVarArgs, boolean debug)  throws SQLException {
		
		if(debug)
    		return InsertCPPConstructorSpecialization_Debug( iD,  name,  owner,  ownerVariable,  declaredTypeTable, functionScopeTable, typeTable, 
    				classOwnerTable, specializedBindingTable, requiredArgumentCount, hasParameterPack, isAuto,  isConstExpr, isDeleted, 
    				isExtern, isExternC, isInline, isMutable, isNoReturn, isRegister, isStatic, isDestructor, isExplicit, isFinal, isImplicit, isOverride, 
    				isPureVirtual, isVirtual,takesVarArgs);    	
    	
    	if(this.isInTable("CPPConstructorSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPConstructorSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScopeTable`,`FunctionScopeTableSubId`,`TypeTable`,`TypeTableSubId`,`ClassOwnerTable`,`ClassOwnerTableSubId`,`SpecializedBindingTable`,`SpecializedBindingTableSubId`,`requiredArgumentCount`,`hasParameterPack`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`,`takesVarArgs`) " //$NON-NLS-1$
                		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, declaredTypeTable);
        ps.setInt(6, declaredTypeTableSubId);
        ps.setString(7, functionScopeTable);
        ps.setInt(8, functionScopeTableSubId);
        ps.setString(9, typeTable);
        ps.setInt(10, typeTableSubId);
        ps.setString(11, classOwnerTable);
        ps.setInt(12, classOwnerTableSubId);
        ps.setString(13, specializedBindingTable);
        ps.setInt(14, specializedBindingTableSubId);
        ps.setInt(15, requiredArgumentCount);
        ps.setInt(16, hasParameterPack);
        ps.setInt(17, isAuto);
        ps.setInt(18, isConstExpr);
        ps.setInt(19, isDeleted);
        ps.setInt(20, isExtern);
        ps.setInt(21, isExternC);
        ps.setInt(22, isInline);
        ps.setInt(23, isMutable);
        ps.setInt(24, isNoReturn);
        ps.setInt(25, isRegister);
        ps.setInt(26, isStatic);
        ps.setInt(27, isDestructor);
        ps.setInt(28, isExplicit);
        ps.setInt(29, isFinal);
        ps.setInt(30, isImplicit);
        ps.setInt(31, isOverride);
        ps.setInt(32, isPureVirtual);
        ps.setInt(33,isVirtual);
        ps.setInt(34,takesVarArgs);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertCPPConstructorSpecialization_Debug(int iD, String name, int owner, String ownerVariable,
			String declaredTypeTable, String functionScopeTable, String typeTable, String classOwnerTable,
			String specializedBindingTable, int requiredArgumentCount, int hasParameterPack, int isAuto,
			int isConstExpr, int isDeleted, int isExtern, int isExternC, int isInline, int isMutable,
			int isNoReturn, int isRegister, int isStatic, int isDestructor, int isExplicit, int isFinal,
			int isImplicit, int isOverride, int isPureVirtual, int isVirtual, int takesVarArgs) {
    	
    	if(this.isInTable_Debug("CPPConstructorSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue"); //$NON-NLS-1$
        System.out.println("\"INSERT INTO `CPPConstructorSpecialization``ID`,`Name`,`Owner`,`OwnerVariable`,`DeclaredTypeTable`,`FunctionScopeTable`,`TypeTable`,`ClassOwnerTable`,`SpecializedBindingTable`,`requiredArgumentCount`,`hasParameterPack`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`,`takesVarArgs`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+declaredTypeTable+ ", "+functionScopeTable+ ", "+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		typeTable+ ", "+classOwnerTable+ ", "+specializedBindingTable + ", "+requiredArgumentCount +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		hasParameterPack+ ", "+isAuto+ ", "+isConstExpr + ", "+isDeleted + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isExtern+ ", "+isExternC+ ", "+isInline + ", "+requiredArgumentCount + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isMutable+ ", "+isNoReturn+ ", "+isRegister + ", "+isStatic + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isDestructor+ ", "+isExplicit+ ", "+isFinal + ", "+isImplicit + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isOverride+ ", "+isPureVirtual+ ", "+isVirtual + ", "+takesVarArgs+");");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		 
        return false;
	}

	public boolean InsertCPPFieldSpecialization(int iD, String name, int owner, String ownerVariable,long initialValue,String TypeTable, int TypeTableSubId, int isAuto, int isConstExpr,int isExtern, int isExternC,
			int isMutable,int isRegister, int isStatic,int fieldPosition, int visibility, boolean debug)  throws SQLException {
		
		if(debug)
    		return InsertCPPFieldSpecialization_Debug( iD,  name,  owner,  ownerVariable,  initialValue, isAuto,  isConstExpr,
    				isExtern, isExternC, isMutable, isRegister, isStatic,fieldPosition,visibility);    	
    	
    	if(this.isInTable("CPPFieldSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPFieldSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InitialValue`,`TypeTable`,`TypeTableSubId`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isRegister`,`isStatic`,`fieldPosition`,`visibility`)" //$NON-NLS-1$
                		+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, initialValue);
        ps.setString(6, TypeTable);
        ps.setInt(7, TypeTableSubId);
        ps.setInt(5, isAuto);
        ps.setInt(9, isConstExpr);
        ps.setInt(10, isExtern);
        ps.setInt(11, isExternC);
        ps.setInt(12, isMutable);
        ps.setInt(13, isRegister);
        ps.setInt(14, isStatic);
        ps.setInt(15, fieldPosition);
        ps.setInt(16, visibility);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
    }
    
	private boolean InsertCPPFieldSpecialization_Debug(int iD, String name, int owner, String ownerVariable,
			long initialValue, int isAuto, int isConstExpr, int isExtern, int isExternC, int isMutable,
			int isRegister, int isStatic, int fieldPosition, int visibility) {
    	
    	if(this.isInTable_Debug("CPPFieldSpecialization", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPPFieldSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InitialValue`,`TypeTable`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isRegister`,`isStatic`,`fieldPosition`,`visibility`)" //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+initialValue+ ", "+isAuto+ ", "+isConstExpr+ ", "+isExtern+ ", "+isExternC + ", "+isMutable + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		isRegister+ ", "+isStatic+  ", "+fieldPosition + ", "+ visibility + ");");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        
        return false;
	}

	public boolean InsertCPPFunctionType(int iD, String name, int owner, String ownerVariable,String returnTypeTable, int returnTypeTableSubId, String thisTypeTable, int thisTypeTableSubId,
			int hasRefQualifier,int isConst,int isRValueReference,int isVolatile, int takesVarArgs, boolean debug) throws SQLException  {

		if(debug)
    		return InsertCPPFunctionType_Debug( iD,  name,  owner,  ownerVariable,  returnTypeTable, thisTypeTable,  hasRefQualifier,
    				isConst, isRValueReference, isVolatile,takesVarArgs);    	
    	
    	if(this.isInTable("CPPFunctionType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPFunctionType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ReturnTypeTable`,`ReturnTypeTableSubId`,`ThisTypeTable`,`ThisTypeTableSubId`,`hasRefQualifier`,`isConst`,`isRValueReference`,`isVolatile`,`takesVarArgs`) " //$NON-NLS-1$
                		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, returnTypeTable);
        ps.setInt(6, returnTypeTableSubId);
        ps.setString(7, thisTypeTable);
        ps.setInt(8, thisTypeTableSubId);
        ps.setInt(9, hasRefQualifier);
        ps.setInt(10, isConst);
        ps.setInt(11, isRValueReference);
        ps.setInt(12, isVolatile);
        ps.setInt(13, takesVarArgs);
        ps.addBatch();
        ps.executeBatch();
		
        return false;  
    }
    
	private boolean InsertCPPFunctionType_Debug(int iD, String name, int owner, String ownerVariable,
			String returnTypeTable, String thisTypeTable, int hasRefQualifier, int isConst,
			int isRValueReference, int isVolatile, int takesVarArgs) {
		
    	if(this.isInTable_Debug("CPPFunctionType", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPPFunctionType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ReturnTypeTable`,`ThisTypeTable`,`hasRefQualifier`,`isConst`,`isRValueReference`,`isVolatile`,`takesVarArgs`)" //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+returnTypeTable+ ", "+thisTypeTable+ ", "+hasRefQualifier+ ", "+isConst+ ", "+isRValueReference + ", "+isVolatile +", " +takesVarArgs + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

        return false;
	}

	public boolean InsertCPPMethodSpecialization(int iD, String name, int owner,
			String ownerVariable, String declaredTypeTable, int declaredTypeTableSubId, String functionScopeTable,int functionScopeTableSubId,  String typeTable, int typeTableSubId,
			String classOwnerTable,int classOwnerTableSubId, String specializedBindingTable,int specializedBindingTableSubId, int requiredArgumentCount,
			int hasParameterPack, int isAuto, int isConstExpr, int isDeleted, int isExtern, int isExternC,
			int isInline, int isMutable, int isNoReturn, int isRegister, int isStatic, int isDestructor,
			int isExplicit, int isFinal, int isImplicit, int isOverride, int isPureVirtual, int isVirtual, int takesVarArgs, boolean debug)  throws SQLException {
		
		if(debug)
    		return InsertCPPMethodSpecialization_Debug( iD,  name,  owner,  ownerVariable,  declaredTypeTable, functionScopeTable, typeTable, 
    				classOwnerTable, specializedBindingTable, requiredArgumentCount, hasParameterPack, isAuto,  isConstExpr, isDeleted, 
    				isExtern, isExternC, isInline, isMutable, isNoReturn, isRegister, isStatic, isDestructor, isExplicit, isFinal, isImplicit, isOverride, 
    				isPureVirtual, isVirtual,takesVarArgs);    	
    	
    	if(this.isInTable("CPPMethodSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPMethodSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScopeTable`,`FunctionScopeTableSubId`,`TypeTable`,`TypeTableSubId`,`ClassOwnerTable`,`ClassOwnerTableSubId`,`SpecializedBindingTable`,`SpecializedBindingTableSubId`,`requiredArgumentCount`,`hasParameterPack`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`,`takesVarArgs`) " //$NON-NLS-1$
                		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$


        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, declaredTypeTable);
        ps.setInt(6, declaredTypeTableSubId);
        ps.setString(7, functionScopeTable);
        ps.setInt(8, functionScopeTableSubId);
        ps.setString(9, typeTable);
        ps.setInt(10, typeTableSubId);
        ps.setString(11, classOwnerTable);
        ps.setInt(12, classOwnerTableSubId);
        ps.setString(13, specializedBindingTable);
        ps.setInt(14,specializedBindingTableSubId);
        ps.setInt(15, requiredArgumentCount);
        ps.setInt(16, hasParameterPack);
        ps.setInt(17, isAuto);
        ps.setInt(18, isConstExpr);
        ps.setInt(19, isDeleted);
        ps.setInt(20, isExtern);
        ps.setInt(21, isExternC);
        ps.setInt(22, isInline);
        ps.setInt(23, isMutable);
        ps.setInt(24, isNoReturn);
        ps.setInt(25, isRegister);
        ps.setInt(26, isStatic);
        ps.setInt(27, isDestructor);
        ps.setInt(28, isExplicit);
        ps.setInt(29, isFinal);
        ps.setInt(30, isImplicit);
        ps.setInt(31, isOverride);
        ps.setInt(32, isPureVirtual);
        ps.setInt(33,isVirtual);
        ps.setInt(34,takesVarArgs);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
    }
    
	private boolean InsertCPPMethodSpecialization_Debug(int iD, String name, int owner, String ownerVariable,
			String declaredTypeTable, String functionScopeTable, String typeTable, String classOwnerTable,
			String specializedBindingTable, int requiredArgumentCount, int hasParameterPack, int isAuto,
			int isConstExpr, int isDeleted, int isExtern, int isExternC, int isInline, int isMutable,
			int isNoReturn, int isRegister, int isStatic, int isDestructor, int isExplicit, int isFinal,
			int isImplicit, int isOverride, int isPureVirtual, int isVirtual, int takesVarArgs) {
    	
    	if(this.isInTable_Debug("CPPMethodSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue"); //$NON-NLS-1$
        System.out.println("\"INSERT INTO `CPPMethodSpecialization``ID`,`Name`,`Owner`,`OwnerVariable`,`DeclaredTypeTable`,`FunctionScopeTable`,`TypeTable`,`ClassOwnerTable`,`SpecializedBindingTable`,`requiredArgumentCount`,`hasParameterPack`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`,`takesVarArgs`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+declaredTypeTable+ ", "+functionScopeTable+ ", "+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		typeTable+ ", "+classOwnerTable+ ", "+specializedBindingTable + ", "+requiredArgumentCount +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		hasParameterPack+ ", "+isAuto+ ", "+isConstExpr + ", "+isDeleted + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isExtern+ ", "+isExternC+ ", "+isInline + ", "+requiredArgumentCount + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isMutable+ ", "+isNoReturn+ ", "+isRegister + ", "+isStatic + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isDestructor+ ", "+isExplicit+ ", "+isFinal + ", "+isImplicit + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isOverride+ ", "+isPureVirtual+ ", "+isVirtual + ", "+takesVarArgs+");");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		 
        return false;
	}

	public boolean InsertCPPParameterPackType(int iD, String name, int owner, String ownerVariable,String TypeTable, int TypeTableSubId, boolean debug)  throws SQLException {
		
		if(debug)
    		return InsertCPPParameterPackType_Debug(iD,name, owner,ownerVariable,TypeTable);    	
    	
    	if(this.isInTable("CPPParameterPackType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPParameterPackType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, TypeTable);
        ps.setInt(6, TypeTableSubId);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertCPPParameterPackType_Debug(int iD, String name, int owner, String ownerVariable,
			String typeTable) {
    	
    	if(this.isInTable_Debug("CPPParameterPackType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue"); //$NON-NLS-1$
        System.out.println("INSERT INTO `CPPClassSpecializationScope`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ClassTypeTable`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+ownerVariable +");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		 
        return false;
	}

	public boolean InsertCPPParameterSpecialization(int iD, String name, int owner, String ownerVariable, long defaultValue, long initialValue,String typeTable,int typeTableSubId, int isAuto,int isConstExpr,int isExtern ,int isExternC,int isMutable,int isParameterPack,int isRegister,int isStatic,  boolean debug)  throws SQLException {
		
		if(debug)
    		return InsertCPPParameterSpecialization_Debug(iD,  name,  owner,  ownerVariable,  defaultValue,  initialValue, typeTable, isAuto, isConstExpr, isExtern 
    				, isExternC, isMutable, isParameterPack, isRegister, isStatic);    	
    	
    	if(this.isInTable("CPPParameterSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPParameterSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DefaultValue`,`InitialValue`,`TypeTable`,`TypeTableSubId`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isParameterPack`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, defaultValue);
        ps.setLong(6, initialValue);
        ps.setString(7, typeTable);
        ps.setInt(9, typeTableSubId);
        ps.setInt(8, isAuto);
        ps.setInt(10, isConstExpr);
        ps.setInt(11, isExtern);
        ps.setInt(12, isExternC);
        ps.setInt(13, isMutable);
        ps.setInt(14, isParameterPack);
        ps.setInt(15, isRegister);
        ps.setInt(16, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
    }
    
	private boolean InsertCPPParameterSpecialization_Debug(int iD, String name, int owner,
			String ownerVariable, long defaultValue, long initialValue, String typeTable, int isAuto,
			int isConstExpr, int isExtern, int isExternC, int isMutable, int isParameterPack, int isRegister,
			int isStatic) {
    	
    	if(this.isInTable_Debug("CPPParameterSpecialization", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPPParameterSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DefaultValue`,`InitialValue`,`TypeTable`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isParameterPack`,`isRegister`,`isStatic`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+defaultValue+ ", "+initialValue+ ", "+typeTable+ ", "+isAuto+ ", "+isConstExpr + ", "+isExtern + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		isExternC+ ", "+isMutable+ ", "+isParameterPack+  ", "+isRegister+  ", "+isStatic+  ");");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        
        return false;
	}

	public boolean InsertCPPPointerType(int iD, String name, int owner, String ownerVariable,String TypeTable,int TypeTableSubId, int isConst,int isRestrict,int isVolatile,boolean debug)  throws SQLException {
		
		if(debug)
    		return InsertCPPPointerType_Debug(iD,  name,  owner,  ownerVariable,  TypeTable,  isConst, isRestrict, isVolatile);
    	if(this.isInTable("CPPPointerType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPPointerType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`isConst`,`isRestrict`,`isVolatile`) VALUES (?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, TypeTable);
        ps.setInt(6, TypeTableSubId);
        ps.setInt(7, isConst);
        ps.setInt(8, isRestrict);
        ps.setInt(9, isVolatile);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
    }
    
	private boolean InsertCPPPointerType_Debug(int iD, String name, int owner, String ownerVariable,
			String typeTable, int isConst, int isRestrict, int isVolatile) {
    	
    	if(this.isInTable_Debug("CPPPointerType", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPPPointerType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`isConst`,`isRestrict`,`isVolatile`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+typeTable+ ", "+isConst+ ", "+isRestrict+ ", "+isVolatile+ ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$

        return false;
	}

	public boolean InsertCPPQualifierType(int iD, String name, int owner, String ownerVariable,String TypeTable,int TypeTableSubId,int isConst,int isVolatile,boolean debug ) throws SQLException {
		
		if(debug)
    		return InsertCPPQualifierType_Debug(iD,  name,  owner,  ownerVariable,  TypeTable,  isConst,  isVolatile);
    	if(this.isInTable("CPPQualifierType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPQualifierType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`isConst`,`isVolatile`) VALUES (?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, TypeTable);
        ps.setInt(6, TypeTableSubId);
        ps.setInt(7, isConst);
        ps.setInt(8, isVolatile);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertCPPQualifierType_Debug(int iD, String name, int owner, String ownerVariable,
			String typeTable, int isConst, int isVolatile) {
    	if(this.isInTable_Debug("CPPQualifierType", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPPPointerType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`isConst`,`isVolatile`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+typeTable+ ", "+isConst+ ","+isVolatile+ ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ 

        return false;
	}

	public boolean InsertCPPReferenceType(int iD, String name, int owner, String ownerVariable,String TypeTable,int TypeTableSubId, int isRValueReference,boolean debug) throws SQLException  {
		
		if(debug)
    		return InsertCPPReferenceType_Debug(iD,  name,  owner,  ownerVariable,  TypeTable,  isRValueReference);
    	if(this.isInTable("CPPReferenceType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPReferenceType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`isRValueReference`) VALUES (?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, TypeTable);
        ps.setInt(6, TypeTableSubId);
        ps.setInt(7, isRValueReference);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertCPPReferenceType_Debug(int iD, String name, int owner, String ownerVariable,
			String typeTable, int isRValueReference) {
       
    	if(this.isInTable_Debug("CPPReferenceType", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPPReferenceType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`isRValueReference`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+typeTable+ ", "+isRValueReference + ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 

        return false;
	}
	
	public boolean InsertCPPTemplateTypeArgument(int iD, int owner, String ownerVariable, long nonTypeValue,String ExpansionPatternTable,int ExpansionPatternTableSubId,String NonTypeEvaluationTable,int NonTypeEvaluationTableSubId,
			String TypeOfNonTypeValueTable, int TypeOfNonTypeValueTableSubId, String TypeValueTable, int TypeValueTableSubId, boolean debug)  throws SQLException {
		
		if(debug)
    		return InsertCPPTemplateTypeArgument_Debug(iD,  owner,  ownerVariable,  nonTypeValue,ExpansionPatternTable,NonTypeEvaluationTable, TypeOfNonTypeValueTable,TypeValueTable);
		
    	if(this.isInTable("CPPTemplateTypeArgument", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPTemplateTypeArgument`(`ID`,`Owner`,`OwnerVariable`,`NonTypeValue`,`ExpansionPatternTable`,`ExpansionPatternTableSubId`,`NonTypeEvaluationTable`,`NonTypeEvaluationTableSubId`,`TypeOfNonTypeValueTable`,`TypeOfNonTypeValueTableSubId`,`TypeValueTable`,`TypeValueTableSubId`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setLong(4, nonTypeValue);
        ps.setString(5, ExpansionPatternTable);
        ps.setInt(6, ExpansionPatternTableSubId);
        ps.setString(7, NonTypeEvaluationTable);
        ps.setInt(8, NonTypeEvaluationTableSubId);
        ps.setString(9, TypeOfNonTypeValueTable);
        ps.setInt(10, TypeOfNonTypeValueTableSubId);
        ps.setString(11, TypeValueTable);
        ps.setInt(12, TypeValueTableSubId);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertCPPTemplateTypeArgument_Debug(int iD, int owner, String ownerVariable,
			long nonTypeValue, String templateArgumentTable, String evaluationTable,
			String typeOfnonTypeValueTable, String typeValueTable) {
    	
    	if(this.isInTable_Debug("CPPTemplateTypeArgument", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPPTemplateTypeArgument`(`ID`,`Owner`,`OwnerVariable`,`NonTypeValue`,`ExpansionPatternTable`,`NonTypeEvaluationTable`,`TypeOfNonTypeValueTable`,`TypeValueTable`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + owner + ", " +ownerVariable + ", "+ nonTypeValue+ "," + templateArgumentTable + "," +evaluationTable+","+typeOfnonTypeValueTable +","+typeValueTable +");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
     
    	return false;
	}
	
	public boolean InsertCPPTypedefSpecialization(int iD, String name, int owner, String ownerVariable,String TypeTable, int TypeTableSubId, boolean debug)  throws SQLException {
		
		if(debug)
    		return InsertCPPTypedefSpecialization_Debug(iD,  name,  owner,  ownerVariable,  TypeTable);
    	if(this.isInTable("CPPTypedefSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPTypedefSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, TypeTable);
        ps.setInt(6, TypeTableSubId);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertCPPTypedefSpecialization_Debug(int iD, String name, int owner, String ownerVariable,
			String typeTable) {
      
    	if(this.isInTable_Debug("CPPTypedefSpecialization", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPPTypedefSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+typeTable+ ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
     
    	return false;
	}

	public boolean InsertIBinding(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos, boolean debug)  throws SQLException {
		
		if(debug)
    		return InsertIBinding_Debug(iD, owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("IBinding", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `IBinding`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertIBinding_Debug(int iD,  int owner, String ownerVariable,String tableRef) {
    	
    	if(this.isInTable_Debug("IBinding", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `IBinding`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertICPPBase(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId,int ArrayPos, boolean debug) throws SQLException  {
		
    	if(debug)
    		return InsertICPPBase_Debug(iD,   owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPBase", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPBase`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertICPPBase_Debug(int iD, int owner, String ownerVariable,
			String tableRef) {
    	
    	if(this.isInTable_Debug("ICPPBase", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPBase`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertICPPClassTemplatePartialSpecialization(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos,boolean debug)  throws SQLException {
    	
		if(debug)
    		return InsertICPPClassTemplatePartialSpecialization_Debug(iD,    owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPClassTemplatePartialSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPClassTemplatePartialSpecialization`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertICPPClassTemplatePartialSpecialization_Debug(int iD, int owner,
			String ownerVariable, String tableRef) {
    	
    	if(this.isInTable_Debug("ICPPClassTemplatePartialSpecialization", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPClassTemplatePartialSpecialization`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertICPPClassType(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos,boolean debug)  throws SQLException {
    	
		if(debug)
    		return InsertICPPClassType_Debug(iD,  owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPClassType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPClassType`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos); 
        
        return false;   	
    }
    
	private boolean InsertICPPClassType_Debug(int iD, int owner, String ownerVariable,
			String tableRef) {
    	
    	if(this.isInTable_Debug("ICPPClassType", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPClassType`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertICPPConstructor(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos,boolean debug)  throws SQLException {
    	
		if(debug)
    		return InsertICPPConstructor_Debug(iD,  owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPConstructor", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPConstructor`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos); 
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertICPPConstructor_Debug(int iD, int owner, String ownerVariable,
			String tableRef) {
    
    	if(this.isInTable_Debug("ICPPConstructor", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPConstructor`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertICPPField(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos, boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertICPPField_Debug(iD,  owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPField", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPField`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos); 
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;   	
    }
    
	private boolean InsertICPPField_Debug(int iD, int owner, String ownerVariable,
			String tableRef) {
		
    	if(this.isInTable_Debug("ICPPField", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPField`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertICPPMethod(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos, boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertICPPMethod_Debug(iD,  owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPMethod", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPMethod`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertICPPMethod_Debug(int iD, int owner, String ownerVariable,
			String tableRef) {
		
		if(this.isInTable_Debug("ICPPMethod", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPMethod`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertICPPNamespaceScope(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos,boolean debug) throws SQLException  {
    
		if(debug)
    		return InsertICPPNamespaceScope_Debug(iD,  owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPNamespaceScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPNamespaceScope`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
	
	private boolean InsertICPPNamespaceScope_Debug(int iD, int owner, String ownerVariable,
			String tableRef) {
    	
    	if(this.isInTable_Debug("ICPPNamespaceScope", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPNamespaceScope`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertICPPParameter(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos,boolean debug) throws SQLException  {
    	
    	if(debug)
    		return InsertICPPParameter_Debug(iD,    owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPParameter`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);

        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertICPPParameter_Debug(int iD, int owner, String ownerVariable,
			String tableRef) {
    	
    	if(this.isInTable_Debug("ICPPParameter", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPParameter`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}
	
	public boolean InsertICPPTemplateArgument(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos,boolean debug) throws SQLException  {
    	
    	if(debug)
    		return InsertICPPTemplateArgument_Debug(iD,    owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPTemplateArgument", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPTemplateArgument`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertICPPTemplateArgument_Debug(int iD, int owner, String ownerVariable,
			String tableRef) {
    	
    	if(this.isInTable_Debug("ICPPTemplateArgument", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPTemplateArgument`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}
	
	public boolean InsertICPPTemplateInstance(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos,boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertICPPTemplateInstance_Debug(iD,  owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPTemplateInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPTemplateInstance`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
    }
    
	private boolean InsertICPPTemplateInstance_Debug(int iD, int owner, String ownerVariable,
			String tableRef) {
		
    	if(this.isInTable_Debug("ICPPTemplateInstance", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPTemplateInstance`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertICPPUsingDeclaration(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos, boolean debug)  throws SQLException {
    
		if(debug)
    		return InsertICPPUsingDeclaration_Debug(iD,  owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("ICPPUsingDeclaration", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ICPPUsingDeclaration`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
	
	private boolean InsertICPPUsingDeclaration_Debug(int iD,  int owner, String ownerVariable,
			String tableRef) {
		
    	if(this.isInTable_Debug("ICPPUsingDeclaration", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ICPPUsingDeclaration`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}


	public boolean InsertIEnumerator(int iD, int owner, String ownerVariable, String TableRef,int tableRefSubID, int ArrayPos,boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertIEnumerator_Debug(iD,  owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("IEnumerator", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `IEnumerator`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`,`ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, tableRefSubID);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
      }
       
	private boolean InsertIEnumerator_Debug(int iD,  int owner, String ownerVariable,
			String tableRef) {
    	
    	if(this.isInTable_Debug("IEnumerator", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `IEnumerator`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}


	public boolean InsertIField(int iD, int owner, String ownerVariable, String TableRef,int  TableRefSubId,int ArrayPos,boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertIField_Debug(iD,  owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("IField", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `IField`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`,`ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
      	
      }
    
	private boolean InsertIField_Debug(int iD,  int owner, String ownerVariable,
			String tableRef) {
    	
    	if(this.isInTable_Debug("IField", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `IField`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", "  +owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertIPDOMCPPTemplateParameter(int iD, int owner, String ownerVariable, String TableRef,int TableRefSubId, int ArrayPos, boolean debug)  throws SQLException {
    	
		if(debug)
    		return InsertIPDOMCPPTemplateParameter_Debug(iD, owner,  ownerVariable,  TableRef);
		
    	if(this.isInTable("IPDOMCPPTemplateParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `IPDOMCPPTemplateParameter`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`, `ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, TableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
      	
      }
    
	private boolean InsertIPDOMCPPTemplateParameter_Debug(int iD, int owner,
			String ownerVariable, String tableRef) {
    
    	if(this.isInTable_Debug("IPDOMCPPTemplateParameter", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `IPDOMCPPTemplateParameter`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + owner + ", "+ ownerVariable+ "," + tableRef + ");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}


	public boolean InsertIType(int iD,  int owner, String ownerVariable, String tableRef, int  TableRefSubId, int ArrayPos,boolean debug)  throws SQLException {
    	
		if(debug)
    		return InsertIType_Debug(iD,  owner,  ownerVariable,  tableRef);
		
    	if(this.isInTable("IType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `IType`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`,`ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, tableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
      }
    
	private boolean InsertIType_Debug(int iD,  int owner, String ownerVariable, String tableRef) {
    	
    	if(this.isInTable_Debug("IType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `IType`(`ID`,`Owner`,`OwnerVariable`,`TableRef`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  +owner + ", "+ ownerVariable+ "," + tableRef + ");");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
     
    	return false;
	}

	public boolean InsertPDOMCPPAliasTemplate(int iD, String name, int owner, String ownerVariable, String TemplateTypeTable,int TemplateTypeTableSubId, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCPPAliasTemplate_Debug(iD,  name,  owner,  ownerVariable,TemplateTypeTable);
		
    	if(this.isInTable("PDOMCPPAliasTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPAliasTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TemplateTypeTable`,`TemplateTypeTableSubId`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, TemplateTypeTable);
        ps.setInt(6, TemplateTypeTableSubId);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;     	
      }
    
	private boolean InsertPDOMCPPAliasTemplate_Debug(int iD, String name, int owner, String ownerVariable,
			String templateTypeTable) {
    	if(this.isInTable_Debug("PDOMCPPAliasTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPAliasTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TemplateTypeTable`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+templateTypeTable + ");");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
     
    	return false;
	}

	public boolean InsertPDOMCPPClassInstance(int iD, String name, int owner, String ownerVariable,String compositeScopeTable,int compositeScopeTableSubId, String specializedBindingTable,
			int specializedBindingTableSubId, String templateDefinitionTable, int templateDefinitionTableSubId,
			int isFinal,int isAnonymous,int isExplicitSpecialization, boolean debug) throws SQLException {
    	
    	if(debug)
    		return InsertPDOMCPPClassInstance_Debug(iD,  name,  owner,  ownerVariable,compositeScopeTable,specializedBindingTable,templateDefinitionTable,isFinal,isAnonymous,isExplicitSpecialization);
		
    	if(this.isInTable("PDOMCPPClassInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPClassInstance`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`CompositeScopeTableSubId`,`SpecializedBindingTable`,`SpecializedBindingTableSubId`,`TemplateDefinitionTable`,`TemplateDefinitionTableSubId`,`isFinal`,`isAnonymous`,`isExplicitSpecialization`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);" +  //$NON-NLS-1$
                		""); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, compositeScopeTable);
        ps.setInt(6, compositeScopeTableSubId);
        ps.setString(7, specializedBindingTable);
        ps.setInt(8, specializedBindingTableSubId);
        ps.setString(9, templateDefinitionTable);
        ps.setInt(10, templateDefinitionTableSubId);
        ps.setInt(11, isFinal);
        ps.setInt(12, isAnonymous);
        ps.setInt(13, isExplicitSpecialization);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPClassInstance_Debug(int iD, String name, int owner, String ownerVariable,String compositeScopeTable,String specializedBindingTable,
			String templateDefinitionTable,
			int isFinal,int isAnonymous,int isExplicitSpecialization) {
    	
    	if(this.isInTable_Debug("PDOMCPPClassInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPClassInstance`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`SpecializedBindingTable`,`TemplateDefinitionTable`,`isFinal`,`isAnonymous`,`isExplicitSpecialization`) "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+compositeScopeTable + ","  + ","+specializedBindingTable + ","+templateDefinitionTable  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        		+ ","+ isFinal+ ","+isAnonymous + ","+isExplicitSpecialization + ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    	
    	return false;
	}
	
	public boolean InsertPDOMCPPClassSpecialization(int iD, String name, int owner, String ownerVariable,String compositeScopeTable, int compositeScopeTableSubId, String specializedBindingTable, int specializedBindingTableSubId,
			int isFinal,int isAnonymous, boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPClassSpecialization_Debug(iD,  name,  owner,  ownerVariable,compositeScopeTable,specializedBindingTable,isFinal,isAnonymous);
		
    	if(this.isInTable("PDOMCPPClassSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPClassSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`CompositeScopeTableSubId`,`SpecializedBindingTable`,`SpecializedBindingTableSubId`,`isFinal`,`isAnonymous`) VALUES (?,?,?,?,?,?,?,?,?,?);" +  //$NON-NLS-1$
                		""); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, compositeScopeTable);
        ps.setInt(6, compositeScopeTableSubId);
        ps.setString(7, specializedBindingTable);
        ps.setInt(8, specializedBindingTableSubId);
        ps.setInt(9, isFinal);
        ps.setInt(10, isAnonymous);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPClassSpecialization_Debug(int iD, String name, int owner,
			String ownerVariable, String compositeScopeTable, String specializedBindingTable, int isFinal,
			int isAnonymous) {
    	
    	if(this.isInTable_Debug("PDOMCPPClassSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPClassSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`SpecializedBindingTable`,`isFinal`,`isAnonymous`) "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+compositeScopeTable + ","  + ","+specializedBindingTable + ","+isFinal  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        		+ ","+ isAnonymous + ");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}

	public boolean InsertPDOMCPPClassTemplate(int iD, String name, int owner, String ownerVariable, String compositeScopeTable,int compositeScopeTableSubId, int isAnonymous, int isFinal, boolean debug) throws SQLException  {
		
		if(debug)
    		return InsertPDOMCPPClassTemplate_Debug(iD,  name,  owner,  ownerVariable,compositeScopeTable,isAnonymous,isFinal);
		
    	if(this.isInTable("PDOMCPPClassTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPClassTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`CompositeScopeTableSubId`,`isAnonymous`,`isFinal`) VALUES (?,?,?,?,?,?,?,?);" +  //$NON-NLS-1$
                		""); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, compositeScopeTable);
        ps.setInt(6, compositeScopeTableSubId);
        ps.setInt(7, isAnonymous);
        ps.setInt(8, isFinal);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
      }
    
	private boolean InsertPDOMCPPClassTemplate_Debug(int iD, String name, int owner, String ownerVariable,
			String compositeScopeTable,int isAnonymous, int isFinal) {
    
    	if(this.isInTable_Debug("PDOMCPPClassTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPClassTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`SpecializedClassTypeTable`,`isAnonymous`,`isFinal`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+compositeScopeTable + "," +isAnonymous+ "," +isFinal+ ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPClassTemplatePartialSpecialization(int iD, String name, int owner, String ownerVariable, String compositeScopeTable, int compositeScopeTableSubId, 
			String primaryClassTemplateTable, int primaryClassTemplateTableSubId, String primaryTemplateTable, int primaryTemplateTableSubId,
			 int isAnonymous, int isFinal, boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPClassTemplatePartialSpecialization_Debug(iD,  name,  owner,  ownerVariable,compositeScopeTable,primaryClassTemplateTable,
    				primaryTemplateTable,isAnonymous,isFinal);
		
    	if(this.isInTable("PDOMCPPClassTemplatePartialSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPClassTemplatePartialSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`CompositeScopeTableSubId`,`PrimaryClassTemplate`,`PrimaryClassTemplateSubId`,`PrimaryTemplate`,`PrimaryTemplateSubId`,`isAnonymous`,`isFinal`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);" +  //$NON-NLS-1$
                		""); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, compositeScopeTable);
        ps.setInt(6, compositeScopeTableSubId);
        ps.setString(7, primaryClassTemplateTable);
        ps.setInt(8, primaryClassTemplateTableSubId);
        ps.setString(9, primaryTemplateTable);
        ps.setInt(10, primaryTemplateTableSubId);
        ps.setInt(11, isAnonymous);
        ps.setInt(12, isFinal);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPClassTemplatePartialSpecialization_Debug(int iD, String name, int owner,
			String ownerVariable, String compositeScopeTable, String primaryClassTemplateTable, String primaryTemplateTable, int isAnonymous, int isFinal) {
    	
    	if(this.isInTable_Debug("PDOMCPPClassTemplatePartialSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPClassTemplatePartialSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`PrimaryClassTemplate`,`PrimaryTemplate`,`isAnonymous`,`isFinal`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+compositeScopeTable+ ","+ primaryClassTemplateTable //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        		+ ","+primaryTemplateTable+ ","+isAnonymous+ ","+isFinal+");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPClassType(int iD, String name, int owner, String ownerVariable, String compositeScopeTable,int compositeScopeTableSubId, int isFinal, int isAnonymous, boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertPDOMCPPClassType_Debug(iD,  name,  owner,  ownerVariable,compositeScopeTable,isFinal, isAnonymous);
		
    	if(this.isInTable("PDOMCPPClassType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPClassType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`CompositeScopeTableSubId`,`isFinal`,`isAnonymous`) VALUES (?,?,?,?,?,?,?,?);" +  //$NON-NLS-1$
                		""); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, compositeScopeTable);
        ps.setInt(6, compositeScopeTableSubId);
        ps.setInt(7, isFinal);
        ps.setInt(8, isAnonymous);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPClassType_Debug(int iD, String name, int owner, String ownerVariable,
			String compositeScopeTable, int isFinal, int isAnonymous) {
    	
    	if(this.isInTable_Debug("PDOMCPPClassType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPClassType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`isFinal`,`isAnonymous`)   "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+compositeScopeTable + ","  + ","+isFinal+ "," + isAnonymous + ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
    	
    	return false;
	}

	public boolean InsertPDOMCPPConstructor(int iD, String name, int owner, String ownerVariable,String typeTable,int typeTableSubId, String declaredTypeTable, int declaredTypeTableSubId,
			String functionScopeTable, int functionScopeTableSubId,
			int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic,int isDestructor,int isExplicit,int isFinal,int isImplicit,
			int isOverride,int isPureVirtual, int isVirtual, boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertPDOMCPPConstructor_Debug(iD,  name,  owner,  ownerVariable,typeTable,declaredTypeTable,functionScopeTable,hasParameterPack,requiredArgumentCount,
    				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual);
		
    	if(this.isInTable("PDOMCPPConstructor", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPConstructor`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScope`,`FunctionScopeSubId`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typeTable);
        ps.setInt(6, typeTableSubId);
        ps.setString(7, declaredTypeTable);
        ps.setInt(8, declaredTypeTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setInt(11, hasParameterPack);
        ps.setInt(12, requiredArgumentCount);
        ps.setInt(13, isAuto);
        ps.setInt(14, isConstExpr);
        ps.setInt(15, isDeleted);
        ps.setInt(16, isExtern);
        ps.setInt(17, isExternC);
        ps.setInt(18, isInline);
        ps.setInt(19, isMutable);
        ps.setInt(20, isNoReturn);
        ps.setInt(21, isRegister);
        ps.setInt(22, isStatic);
        ps.setInt(23, isDestructor);
        ps.setInt(24, isExplicit);
        ps.setInt(25, isFinal);
        ps.setInt(26, isImplicit);
        ps.setInt(27,isOverride);
        ps.setInt(28, isPureVirtual);
        ps.setInt(29, isVirtual);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
      }
    
	private boolean InsertPDOMCPPConstructor_Debug(int iD, String name, int owner, String ownerVariable,String typeTable,String declaredTypeTable, 
			String functionScopeTable, int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic, int isDestructor, int isExplicit, int isFinal,
			int isImplicit, int isOverride, int isPureVirtual, int isVirtual) {
		
    	if(this.isInTable_Debug("PDOMCPPConstructor", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
        System.out.println("INSERT INTO `PDOMCPPConstructor`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`DeclaredTypeTable`,`FunctionScope`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+declaredTypeTable+ ", "+typeTable+ ", "+functionScopeTable //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		+ ", "+ hasParameterPack + ", "+requiredArgumentCount + ", "+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isAuto+ ", "+isConstExpr + ", "+isDeleted +  //$NON-NLS-1$ //$NON-NLS-2$
        		isExtern+ ", "+isExternC+ ", "+isInline +   //$NON-NLS-1$ //$NON-NLS-2$
        		isMutable+ ", "+isNoReturn+ ", "+isRegister + ", "+isStatic +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isDestructor+ ", "+isExplicit+ ", "+isFinal + ", "+isImplicit +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isOverride+ ", "+isPureVirtual+ ", "+isVirtual +");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		 
        return false;
	}
		
	public boolean InsertPDOMCPPDeferredClassInstance(int iD ,boolean debug) throws SQLException  {
    
		if(debug)
    		return InsertPDOMCPPDeferredClassInstance_Debug(iD);
		
    	if(this.isInTable("PDOMCPPDeferredClassInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPDeferredClassInstance`(`ID`) VALUES (?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
      }
    
    private boolean InsertPDOMCPPDeferredClassInstance_Debug(int iD) {
		
    	if(this.isInTable_Debug("PDOMCPPDeferredClassInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPDeferredClassInstance`(`ID`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}

	public boolean InsertPDOMCPPEnumeration(int iD, String name, int owner, String ownerVariable, String typeTable,int typeTableSubId, long minValue, long maxValue, boolean debug) throws SQLException  {
   
		if(debug)
    		return InsertPDOMCPPEnumeration_Debug(iD,  name,  owner,  ownerVariable, typeTable,minValue,maxValue);
		
    	if(this.isInTable("PDOMCPPEnumeration", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPEnumeration`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`MinValue`,`MaxValue`) VALUES (?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typeTable);
        ps.setInt(6, typeTableSubId);
        ps.setLong(7, minValue);
        ps.setLong(8, maxValue);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPEnumeration_Debug(int iD, String name, int owner, String ownerVariable,
			String typeTable, long minValue, long maxValue) {
    	
    	if(this.isInTable_Debug("PDOMCPPEnumeration", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPEnumeration`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`MinValue`,`MaxValue`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ typeTable + ","+minValue+ ","+maxValue+ ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
    	
    	return false;
	}

	public boolean InsertPDOMCPPEnumerator(int iD, String name, int owner, String ownerVariable,String internalTypeTable,int internalTypeTableSubId, String typeTable, int typeTableSubId, long value, boolean debug) throws SQLException {
    	if(debug)
    		return InsertPDOMCPPEnumerator_Debug(iD,  name,  owner,  ownerVariable,internalTypeTable,typeTable,value);
		
    	if(this.isInTable("PDOMCPPEnumerator", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPEnumerator`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InternalTypeTable`,`InternalTypeTableSubId`,`TypeTable`,`TypeTableSubId`,`Value`) VALUES (?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, internalTypeTable);
        ps.setInt(6, internalTypeTableSubId);
        ps.setString(7, typeTable);
        ps.setInt(8, typeTableSubId);
        ps.setLong(9, value);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPEnumerator_Debug(int iD, String name, int owner, String ownerVariable,
			String internalTypeTable, String typeTable, long value) {
		
    	if(this.isInTable_Debug("PDOMCPPEnumeration", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPEnumerator`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InternalTypeTable`,`TypeTable`,`Value`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ ownerVariable + ","+ internalTypeTable + "," + value + ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
    	
    	return false;
	}

	public boolean InsertPDOMCPPField(int iD, String name, int owner, String ownerVariable,long initialValue, String typeTable,int typeTableSubId, String ClassOwnerTable, int ClassOwnerTableSubId,
			int fieldPosition,int isAuto,int isConstExpr,int isExtern,
			int isExternC,int isMutable,int isRegister,int isStatic, boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPField_Debug(iD,  name,  owner,  ownerVariable,initialValue,typeTable, ClassOwnerTable, fieldPosition,isAuto,isConstExpr,
    				isExtern,isExternC,isMutable,isRegister,isStatic);
		
    	if(this.isInTable("PDOMCPPField", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPField`(`ID`,`Name`,`Owner`,`OwnerVariable`,`Value`,`TypeTable`,`TypeTableSubId`,`ClassOwnerTable`,`ClassOwnerTableSubId`,`fieldPosition`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, initialValue);
        ps.setString(6, typeTable);
        ps.setInt(7, typeTableSubId);
        ps.setString(8, ClassOwnerTable);
        ps.setInt(9, ClassOwnerTableSubId);
        ps.setInt(10, fieldPosition);
        ps.setInt(11, isAuto);
        ps.setInt(12, isConstExpr);
        ps.setInt(13, isExtern);
        ps.setInt(14, isExternC);
        ps.setInt(15, isMutable);
        ps.setInt(16, isRegister);
        ps.setInt(17, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPField_Debug(int iD, String name, int owner, String ownerVariable, long initialValue,
			String typeTable, String iCCPClassTypeTable, int fieldPosition, int isAuto, int isConstExpr,
			int isExtern, int isExternC, int isMutable, int isRegister, int isStatic) {
    	
    	if(this.isInTable_Debug("PDOMCPPField", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPField`(`ID`,`Name`,`Owner`,`OwnerVariable`,`Value`,`TypeTable`,`ClassOwnerTable`,`fieldPosition`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isRegister`,`isStatic`)" //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+initialValue + ","  + ","+typeTable+ "," + iCCPClassTypeTable +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        		"," + fieldPosition + "," + isAuto+ "," +isConstExpr + "," +isExtern + "," +isExternC + "," +isMutable + "," +isRegister //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		+ "," +isStatic +");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}

	public boolean InsertPDOMCPPFieldSpecialization(int iD, String name, int owner, String ownerVariable,long initialValue, String typeTable,int typeTableSubId, String ClassOwnerTable,int ClassOwnerTableSubId, int fieldPosition,int isAuto,int isConstExpr,int isExtern,
			int isExternC,int isMutable,int isRegister,int isStatic, boolean debug) throws SQLException  {
    
		if(debug)
    		return InsertPDOMCPPFieldSpecialization_Debug(iD,  name,  owner,  ownerVariable,initialValue,typeTable, ClassOwnerTable, fieldPosition,isAuto,isConstExpr,
    				isExtern,isExternC,isMutable,isRegister,isStatic);
		
    	if(this.isInTable("PDOMCPPFieldSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPFieldSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`Value`,`TypeTable`,`TypeTableSubId`,`ClassOwnerTable`,`ClassOwnerTableSubId`,`fieldPosition`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, initialValue);
        ps.setString(6, typeTable);
        ps.setInt(7, typeTableSubId);
        ps.setString(8, ClassOwnerTable);
        ps.setInt(9, ClassOwnerTableSubId);
        ps.setInt(10, fieldPosition);
        ps.setInt(11, isAuto);
        ps.setInt(12, isConstExpr);
        ps.setInt(13, isExtern);
        ps.setInt(14, isExternC);
        ps.setInt(15, isMutable);
        ps.setInt(16, isRegister);
        ps.setInt(17, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;     	
      }
    
	private boolean InsertPDOMCPPFieldSpecialization_Debug(int iD, String name, int owner,
			String ownerVariable, long initialValue, String typeTable, String iCCPClassTypeTable, int fieldPosition,
			int isAuto, int isConstExpr, int isExtern, int isExternC, int isMutable, int isRegister,
			int isStatic) {

    	if(this.isInTable_Debug("PDOMCPPFieldSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPFieldSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`Value`,`TypeTable`,`ClassOwnerTable`,`fieldPosition`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isRegister`,`isStatic`)" //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+initialValue + ","  + ","+typeTable+ "," + iCCPClassTypeTable +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        		"," + fieldPosition + "," + isAuto+ "," +isConstExpr + "," +isExtern + "," +isExternC + "," +isMutable + "," +isRegister //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		+ "," +isStatic +");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}

	public boolean InsertPDOMCPPFunction(int iD, String name, int owner, String ownerVariable,String typeTable, int typeTableSubId, String declaredTypeTable, int declaredTypeTableSubId,
			String functionScopeTable,int functionScopeTableSubId, int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic, boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertPDOMCPPFunction_Debug(iD,  name,  owner,  ownerVariable,typeTable,declaredTypeTable,functionScopeTable,hasParameterPack,requiredArgumentCount,
    				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic);
		
    	if(this.isInTable("PDOMCPPFunction", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPFunction`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScope`,`FunctionScopeSubId`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typeTable);
        ps.setInt(6, typeTableSubId);
        ps.setString(7, declaredTypeTable);
        ps.setInt(8, declaredTypeTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setInt(11, hasParameterPack);
        ps.setInt(12, requiredArgumentCount);
        ps.setInt(13, isAuto);
        ps.setInt(14, isConstExpr);
        ps.setInt(15, isDeleted);
        ps.setInt(16, isExtern);
        ps.setInt(17, isExternC);
        ps.setInt(18, isInline);
        ps.setInt(19, isMutable);
        ps.setInt(20, isNoReturn);
        ps.setInt(21, isRegister);
        ps.setInt(22, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPFunction_Debug(int iD, String name, int owner, String ownerVariable,String typeTable,String declaredTypeTable, 
			String functionScopeTable, int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic) {
		
    	if(this.isInTable_Debug("PDOMCPPFunction", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPFunction`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`DeclaredTypeTable`,`FunctionScope`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`)" //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+typeTable + "," +declaredTypeTable+ "," +functionScopeTable+ "," +hasParameterPack+ ","  + ","+requiredArgumentCount +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		"," + isAuto+ "," +isConstExpr + "," + isDeleted + "," +isExtern + "," +isExternC+ "," +isInline + "," +isMutable + "," +isNoReturn+ "," +isRegister //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
        		+ "," +isStatic +");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}

	public boolean InsertPDOMCPPFunctionInstance(int iD, String name, int owner, String ownerVariable,String declaredTypeTable, int declaredTypeTableSubId, String typeTable, int typeTableSubId,
			String functionScopeTable, int functionScopeTableSubId, String templateDefinitionTable, int templateDefinitionTableSubId,
			int requiredArgumentCount,int isExplicitSpecialization,
			int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,int isInline,int isMutable,int isNoReturn,int isRegister,int isStatic,
			int hasParameterPack, int takesVarArgs, boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertPDOMCPPFunctionInstance_Debug(iD,  name,  owner,  ownerVariable,declaredTypeTable,typeTable,functionScopeTable,templateDefinitionTable,
    				requiredArgumentCount,isExplicitSpecialization,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
    				hasParameterPack,takesVarArgs);
		
    	
    	
    	if(this.isInTable("PDOMCPPFunctionInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPFunctionInstance`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`TypeTable`,`TypeTableSubId`,`FunctionScopeTable`,`FunctionScopeTableSubId`,`TemplateDefinitionTable`,`TemplateDefinitionTableSubId`,`requiredArgumentCount`,`isExplicitSpecialization`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`hasParameterPack`,`takesVarArgs`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, declaredTypeTable);
        ps.setInt(6, declaredTypeTableSubId);
        ps.setString(7, typeTable);
        ps.setInt(8, typeTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setString(11, templateDefinitionTable);
        ps.setInt(12, templateDefinitionTableSubId);
        ps.setInt(13, requiredArgumentCount);
        ps.setInt(14, isExplicitSpecialization);
        ps.setInt(15, isAuto);
        ps.setInt(16, isConstExpr);
        ps.setInt(17, isDeleted);
        ps.setInt(18, isExtern);
        ps.setInt(19, isExternC);
        ps.setInt(20, isInline);
        ps.setInt(21, isMutable);
        ps.setInt(22, isNoReturn);
        ps.setInt(23, isRegister);
        ps.setInt(24, isStatic);
        ps.setInt(25, hasParameterPack);
        ps.setInt(26, takesVarArgs);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPFunctionInstance_Debug(int iD, String name, int owner, String ownerVariable,String declaredTypeTable,String typeTable,
			String functionScopeTable,String templateDefinitionTable, int requiredArgumentCount,int isExplicitSpecialization,
			int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,int isInline,int isMutable,int isNoReturn,int isRegister,int isStatic,
			int hasParameterPack, int takesVarArgs) {
		
    	if(this.isInTable_Debug("PDOMCPPFunction", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPFunction`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DeclaredTypeTable`,`TypeTable`,`FunctionScopeTable`,`TemplateDefinitionTable`,`requiredArgumentCount`,`isExplicitSpecialization`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`hasParameterPack`,`takesVarArgs`)" //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+declaredTypeTable + ","+typeTable + "," +functionScopeTable + "," + templateDefinitionTable+ ","  + ","+requiredArgumentCount +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		"," +isExplicitSpecialization+ "," + isAuto+ "," +isConstExpr + "," + isDeleted + "," +isExtern + "," +isExternC+ "," +isInline + "," +isMutable + "," +isNoReturn+ "," +isRegister //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		+ "," +isStatic + "," + hasParameterPack + "," +  takesVarArgs +");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    	
    	return false;
	}

	public boolean InsertPDOMCPPMethod(int iD, String name, int owner, String ownerVariable,String typeTable,int typeTableSubId, String declaredTypeTable, int declaredTypeTableSubId,
			String functionScopeTable, int functionScopeTableSubId, int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic,int isDestructor,int isExplicit,int isFinal,int isImplicit,
			int isOverride,int isPureVirtual, int isVirtual, boolean debug) throws SQLException  {
    	
		if(debug)
    		return InsertPDOMCPPMethod_Debug(iD,  name,  owner,  ownerVariable,typeTable,declaredTypeTable,functionScopeTable,hasParameterPack,requiredArgumentCount,
    				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual);
		
    	if(this.isInTable("PDOMCPPMethod", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPMethod`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScope`,`FunctionScopeSubId`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typeTable);
        ps.setInt(6, typeTableSubId);
        ps.setString(7, declaredTypeTable);
        ps.setInt(8, declaredTypeTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setInt(11, hasParameterPack);
        ps.setInt(12, requiredArgumentCount);
        ps.setInt(13, isAuto);
        ps.setInt(14, isConstExpr);
        ps.setInt(15, isDeleted);
        ps.setInt(16, isExtern);
        ps.setInt(17, isExternC);
        ps.setInt(18, isInline);
        ps.setInt(19, isMutable);
        ps.setInt(20, isNoReturn);
        ps.setInt(21, isRegister);
        ps.setInt(22, isStatic);
        ps.setInt(23, isDestructor);
        ps.setInt(24, isExplicit);
        ps.setInt(25, isFinal);
        ps.setInt(26, isImplicit);
        ps.setInt(27,isOverride);
        ps.setInt(28, isPureVirtual);
        ps.setInt(29, isVirtual);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPMethod_Debug(int iD, String name, int owner, String ownerVariable,String typeTable,String declaredTypeTable, 
			String functionScopeTable, int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic, int isDestructor, int isExplicit, int isFinal,
			int isImplicit, int isOverride, int isPureVirtual, int isVirtual) {
		

    	if(this.isInTable_Debug("PDOMCPPMethod", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
        System.out.println("INSERT INTO `PDOMCPPMethod`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`DeclaredTypeTable`,`FunctionScope`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+declaredTypeTable+ ", "+typeTable+ ", "+functionScopeTable //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		+ ", "+ hasParameterPack + ", "+requiredArgumentCount + ", "+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isAuto+ ", "+isConstExpr + ", "+isDeleted +  //$NON-NLS-1$ //$NON-NLS-2$
        		isExtern+ ", "+isExternC+ ", "+isInline +   //$NON-NLS-1$ //$NON-NLS-2$
        		isMutable+ ", "+isNoReturn+ ", "+isRegister + ", "+isStatic +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isDestructor+ ", "+isExplicit+ ", "+isFinal + ", "+isImplicit +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isOverride+ ", "+isPureVirtual+ ", "+isVirtual +");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		 
        return false;
	}

    public boolean InsertPDOMCPPMethodSpecialization(int iD, String name, int owner, String ownerVariable,String typeTable,int typeTableSubId, String declaredTypeTable, int declaredTypeTableSubId,
			String functionScopeTable, int functionScopeTableSubId, int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic,int isDestructor,int isExplicit,int isFinal,int isImplicit,
			int isOverride,int isPureVirtual, int isVirtual, boolean debug) throws SQLException  {
    	
    	if(debug)
    		return InsertPDOMCPPMethodSpecialization_Debug(iD,  name,  owner,  ownerVariable,typeTable,declaredTypeTable,functionScopeTable,hasParameterPack,requiredArgumentCount,
    				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual);
		
    	if(this.isInTable("PDOMCPPMethodSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPMethodSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScope`,`FunctionScopeSubId`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typeTable);
        ps.setInt(6, typeTableSubId);
        ps.setString(7, declaredTypeTable);
        ps.setInt(8, declaredTypeTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setInt(11, hasParameterPack);
        ps.setInt(12, requiredArgumentCount);
        ps.setInt(13, isAuto);
        ps.setInt(14, isConstExpr);
        ps.setInt(15, isDeleted);
        ps.setInt(16, isExtern);
        ps.setInt(17, isExternC);
        ps.setInt(18, isInline);
        ps.setInt(19, isMutable);
        ps.setInt(20, isNoReturn);
        ps.setInt(21, isRegister);
        ps.setInt(22, isStatic);
        ps.setInt(23, isDestructor);
        ps.setInt(24, isExplicit);
        ps.setInt(25, isFinal);
        ps.setInt(26, isImplicit);
        ps.setInt(27,isOverride);
        ps.setInt(28, isPureVirtual);
        ps.setInt(29, isVirtual);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;        
    }
    
    private boolean InsertPDOMCPPMethodSpecialization_Debug(int iD, String name, int owner, String ownerVariable,String typeTable,String declaredTypeTable, 
			String functionScopeTable, int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic, int isDestructor, int isExplicit, int isFinal,
			int isImplicit, int isOverride, int isPureVirtual, int isVirtual) {
		
    	if(this.isInTable_Debug("PDOMCPPMethodSpecialization", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
        System.out.println("INSERT INTO `PDOMCPPMethodSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`DeclaredTypeTable`,`FunctionScope`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+declaredTypeTable+ ", "+typeTable+ ", "+functionScopeTable //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		+ ", "+ hasParameterPack + ", "+requiredArgumentCount + ", "+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isAuto+ ", "+isConstExpr + ", "+isDeleted +  //$NON-NLS-1$ //$NON-NLS-2$
        		isExtern+ ", "+isExternC+ ", "+isInline +   //$NON-NLS-1$ //$NON-NLS-2$
        		isMutable+ ", "+isNoReturn+ ", "+isRegister + ", "+isStatic +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isDestructor+ ", "+isExplicit+ ", "+isFinal + ", "+isImplicit +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isOverride+ ", "+isPureVirtual+ ", "+isVirtual +");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		 
        return false;
	}

    public boolean InsertPDOMCPPMethodTemplateSpecialization(int iD, String name, int owner, String ownerVariable,String typeTable,int typeTableSubId, String declaredTypeTable, int declaredTypeTableSubId,
			String functionScopeTable, int functionScopeTableSubId, int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic,int isDestructor,int isExplicit,int isFinal,int isImplicit,
			int isOverride,int isPureVirtual, int isVirtual, boolean debug) throws SQLException  {
    
    	if(debug)
    		return InsertPDOMCPPMethodTemplateSpecialization_Debug(iD,  name,  owner,  ownerVariable,typeTable,declaredTypeTable,functionScopeTable,hasParameterPack,requiredArgumentCount,
    				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,isDestructor,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual);
		
    	if(this.isInTable("PDOMCPPMethodTemplateSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPMethodTemplateSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScope`,`FunctionScopeSubId`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typeTable);
        ps.setInt(6, typeTableSubId);
        ps.setString(7, declaredTypeTable);
        ps.setInt(8, declaredTypeTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setInt(11, hasParameterPack);
        ps.setInt(12, requiredArgumentCount);
        ps.setInt(13, isAuto);
        ps.setInt(14, isConstExpr);
        ps.setInt(15, isDeleted);
        ps.setInt(16, isExtern);
        ps.setInt(17, isExternC);
        ps.setInt(18, isInline);
        ps.setInt(19, isMutable);
        ps.setInt(20, isNoReturn);
        ps.setInt(21, isRegister);
        ps.setInt(22, isStatic);
        ps.setInt(23, isDestructor);
        ps.setInt(24, isExplicit);
        ps.setInt(25, isFinal);
        ps.setInt(26, isImplicit);
        ps.setInt(27,isOverride);
        ps.setInt(28, isPureVirtual);
        ps.setInt(29, isVirtual);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;        
    }
        
    private boolean InsertPDOMCPPMethodTemplateSpecialization_Debug(int iD, String name, int owner, String ownerVariable,String typeTable,String declaredTypeTable, 
			String functionScopeTable, int hasParameterPack,int requiredArgumentCount,int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,
			int isInline,int isMutable,int isNoReturn,int isRegister, int isStatic, int isDestructor, int isExplicit, int isFinal,
			int isImplicit, int isOverride, int isPureVirtual, int isVirtual) {
		

    	if(this.isInTable_Debug("PDOMCPPMethodTemplateSpecialization", iD))  //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
        System.out.println("INSERT INTO `PDOMCPPMethodTemplateSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`DeclaredTypeTable`,`FunctionScope`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) " //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " + name + ", " +owner + ", "+ ownerVariable+ ", "+declaredTypeTable+ ", "+typeTable+ ", "+functionScopeTable //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		+ ", "+ hasParameterPack + ", "+requiredArgumentCount + ", "+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isAuto+ ", "+isConstExpr + ", "+isDeleted +  //$NON-NLS-1$ //$NON-NLS-2$
        		isExtern+ ", "+isExternC+ ", "+isInline +   //$NON-NLS-1$ //$NON-NLS-2$
        		isMutable+ ", "+isNoReturn+ ", "+isRegister + ", "+isStatic +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isDestructor+ ", "+isExplicit+ ", "+isFinal + ", "+isImplicit +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		isOverride+ ", "+isPureVirtual+ ", "+isVirtual +");"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		 
        return false;
	}

	public boolean InsertPDOMCPPNamespace(int iD, String name, int owner, String ownerVariable, String namespaceScopeTable,int namespaceScopeTableSubId, String scopeBindingTable, int scopeBindingTableSubId,  boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPNamespace_Debug(iD,  name,  owner,  ownerVariable,namespaceScopeTable,scopeBindingTable);
		
    	if(this.isInTable("PDOMCPPNamespace", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPNamespace`(`ID`,`Name`,`Owner`,`OwnerVariable`,`NamespaceScopeTable`,`NamespaceScopeTableSubId`,`ScopeBindingTable`,`ScopeBindingTableSubId`) VALUES (?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, namespaceScopeTable);
        ps.setInt(6, namespaceScopeTableSubId);
        ps.setString(7, scopeBindingTable);
        ps.setInt(8, scopeBindingTableSubId);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;     	
      }
    
	private boolean InsertPDOMCPPNamespace_Debug(int iD, String name, int owner, String ownerVariable,
			String namespaceScopeTable, String scopeBindingTable) {
	
    	if(this.isInTable_Debug("PDOMCPPNamespace", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPNamespace`(`ID`,`Name`,`Owner`,`OwnerVariable`,`NamespaceScopeTable`,`ScopeBindingTable`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner + ","+ ownerVariable + ","+ namespaceScopeTable + "," + scopeBindingTable + ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPNamespaceAlias(int iD, String name, int owner, String ownerVariable,String bindingsTable, int bindingsTableSubId, String namespaceScopeTable, int namespaceScopeTableSubId, int isInline, boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPNamespaceAlias_Debug(iD,  name,  owner,  ownerVariable,bindingsTable,namespaceScopeTable,isInline);
		
    	if(this.isInTable("PDOMCPPNamespaceAlias", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPNamespaceAlias`(`ID`,`Name`,`Owner`,`OwnerVariable`,`BindingTable`,`BindingTableSubId`,`NamespaceScopeTable`,`NamespaceScopeTableSubId`,`isInline`) VALUES (?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, bindingsTable);
        ps.setInt(6, bindingsTableSubId);
        ps.setString(7, namespaceScopeTable);
        ps.setInt(8, namespaceScopeTableSubId);
        ps.setInt(9, isInline);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPNamespaceAlias_Debug(int iD, String name, int owner, String ownerVariable,
			String bindingsTable, String namespaceScopeTable, int isInline) {
		
    	if(this.isInTable_Debug("PDOMCPPNamespaceAlias", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPNamespaceAlias`(`ID`,`Name`,`Owner`,`OwnerVariable`,`BindingsTable`,`NamespaceScopeTable`,`isInline`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ bindingsTable + ","+ namespaceScopeTable + "," + isInline + ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
    	
    	return false;
	}

	public boolean InsertPDOMCPPParameter(int iD, String name, int owner, String ownerVariable,long defaultValue,String TypeTable, int TypeTableSubId, String ScopeTable,int ScopeTableSubId, int isAuto,int isConstExpr, int isExtern,int isExternC,int isGloballyQualified,int isMutable,int isParameterPack,int isRegister,int isStatic, boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPParameter_Debug(iD,  name,  owner,  ownerVariable,defaultValue,TypeTable,ScopeTable,isAuto,isConstExpr,isExtern,isExternC,isGloballyQualified,isMutable,isParameterPack,isRegister,isStatic);
		
    	if(this.isInTable("PDOMCPPParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DefaultValue`,`TypeTable`,`TypeTableSubId`,`ScopeTable`,`ScopeTableSubId`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isGloballyQualified`,`isMutable`,`isParameterPack`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, defaultValue);
        ps.setString(6, TypeTable);
        ps.setInt(7, TypeTableSubId);
        ps.setString(8, ScopeTable);
        ps.setInt(9, ScopeTableSubId);
        ps.setInt(10, isAuto);
        ps.setInt(11, isConstExpr);
        ps.setInt(12, isExtern);
        ps.setInt(13, isExternC);
        ps.setInt(14, isGloballyQualified);
        ps.setInt(15, isMutable);
        ps.setInt(16, isParameterPack);
        ps.setInt(17, isRegister);
        ps.setInt(18, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;     	
      }
    
	private boolean InsertPDOMCPPParameter_Debug(int iD, String name, int owner, String ownerVariable,
			long defaultValue, String typeTable, String scopeTable, int isAuto, int isConstExpr, int isExtern,
			int isExternC, int isGloballyQualified, int isMutable, int isParameterPack, int isRegister,
			int isStatic) {
		
    	if(this.isInTable_Debug("PDOMCPPParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("\"INSERT INTO `PDOMCPPParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DefaultValue`,`TypeTable`,`ScopeTable`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isGloballyQualified`,`isMutable`,`isParameterPack`,`isRegister`,`isStatic`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ defaultValue + ","+ typeTable + "," + scopeTable  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		 + "," + isAuto  + "," + isConstExpr + "," + isExtern + "," + isExternC + "," + isGloballyQualified + "," + isMutable + "," + isParameterPack //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		 + "," + isRegister + "," + isStatic+ ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPParameterSpecialization(int iD, String name, int owner, String ownerVariable,long defaultValue,String TypeTable,int TypeTableSubId, String ScopeTable, int ScopeTableSubId,
			int isAuto,int isConstExpr, int isExtern,int isExternC,int isGloballyQualified,int isMutable,int isParameterPack,int isRegister,int isStatic, boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPParameterSpecialization_Debug(iD,  name,  owner,  ownerVariable,defaultValue,TypeTable,ScopeTable,isAuto,isConstExpr,isExtern,isExternC,isGloballyQualified,isMutable,isParameterPack,isRegister,isStatic);
		
    	if(this.isInTable("PDOMCPPParameterSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPParameterSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DefaultValue`,`TypeTable`,`TypeTableSubId`,`ScopeTable`,`ScopeTableSubId`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isGloballyQualified`,`isMutable`,`isParameterPack`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, defaultValue);
        ps.setString(6, TypeTable);
        ps.setInt(7, TypeTableSubId);
        ps.setString(8, ScopeTable);
        ps.setInt(9, ScopeTableSubId);
        ps.setInt(10, isAuto);
        ps.setInt(11, isConstExpr);
        ps.setInt(12, isExtern);
        ps.setInt(13, isExternC);
        ps.setInt(14, isGloballyQualified);
        ps.setInt(15, isMutable);
        ps.setInt(16, isParameterPack);
        ps.setInt(17, isRegister);
        ps.setInt(18, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPParameterSpecialization_Debug(int iD, String name, int owner, String ownerVariable,
			long defaultValue, String typeTable, String scopeTable, int isAuto, int isConstExpr, int isExtern,
			int isExternC, int isGloballyQualified, int isMutable, int isParameterPack, int isRegister,
			int isStatic) {
		
    	if(this.isInTable_Debug("PDOMCPPParameterSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("\"INSERT INTO `PDOMCPPParameterSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DefaultValue`,`TypeTable`,`ScopeTable`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isGloballyQualified`,`isMutable`,`isParameterPack`,`isRegister`,`isStatic`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ defaultValue + ","+ typeTable + "," + scopeTable  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		 + "," + isAuto  + "," + isConstExpr + "," + isExtern + "," + isExternC + "," + isGloballyQualified + "," + isMutable + "," + isParameterPack //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		 + "," + isRegister + "," + isStatic+ ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
    	
    	return false;
	}
      
    public boolean InsertPDOMCPPTemplateNonTypeParameter(int iD, String name, int owner, String ownerVariable,long initValue,String TypeTable, int TypeTableSubId, String DefaultValueTable, int DefaultValueTableSubId,
    		int parameterPosition,int isAuto,int isConstExpr,int isExtern,int isExternC,int isMutable,int isParameterPack,int isRegister,int isStatic, boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPTemplateNonTypeParameter_Debug(iD,  name,  owner,  ownerVariable,initValue,TypeTable,DefaultValueTable,parameterPosition,
    				isAuto,isConstExpr,isExtern,isExternC,isMutable,isParameterPack,isRegister,isStatic);
		
    	if(this.isInTable("PDOMCPPTemplateNonTypeParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPTemplateNonTypeParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InitValue`,`TypeTable`,`TypeTableSubId`,`DefaultValueTable`,`DefaultValueTableSubId`,`parameterPosition`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isParameterPack`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, initValue);
        ps.setString(6, TypeTable);
        ps.setInt(7, TypeTableSubId);
        ps.setString(8, DefaultValueTable);
        ps.setInt(9, DefaultValueTableSubId);
        ps.setInt(10, parameterPosition);
        ps.setInt(11, isAuto);
        ps.setInt(12, isConstExpr);
        ps.setInt(13, isExtern);
        ps.setInt(14, isExternC);
        ps.setInt(15, isMutable);
        ps.setInt(16, isParameterPack);
        ps.setInt(17, isRegister);
        ps.setInt(18, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
    private boolean InsertPDOMCPPTemplateNonTypeParameter_Debug(int iD, String name, int owner,
			String ownerVariable, long initValue, String typeTable, String templateArgnumentTable,
			int parameterPosition, int isAuto, int isConstExpr, int isExtern, int isExternC, int isMutable,
			int isParameterPack, int isRegister, int isStatic) {
		
    	if(this.isInTable_Debug("PDOMCPPTemplateNonTypeParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPTemplateNonTypeParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InitValue`,`TypeTable`,`DefaultValueTable`,`parameterPosition`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isParameterPack`,`isRegister`,`isStatic`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ initValue + ","+ typeTable + "," + templateArgnumentTable  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		 + "," + parameterPosition  + "," + isAuto + "," + isConstExpr + "," + isExtern + "," + isExternC + "," + isMutable + "," + isParameterPack //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		 + "," + isRegister + "," + isStatic+ ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPTemplateTemplateParameter(int iD, String name, int owner, String ownerVariable,String compositeScopeTable, int compositeScopeTableSubId,
			String defaultTypeTable, int defaultTypeTableSubId, String defaultValueTable, int defaultValueTableSubId, int parameterPosition,int isAnonym,int isFinal,int isParameterPack, boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPTemplateTemplateParameter_Debug(iD,  name,  owner,  ownerVariable,compositeScopeTable,defaultTypeTable,defaultValueTable,
    				parameterPosition,isAnonym,isFinal,isParameterPack);
		
    	if(this.isInTable("PDOMCPPTemplateTemplateParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPTemplateTemplateParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`CompositeScopeTableSubId`,`DefaultTypeTable`,`DefaultTypeTableSubId`,`DefaultValueTable`,`DefaultValueTableSubId`,`parameterPosition`,`isAnonym`,`isFinal`,`isParameterPack`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, compositeScopeTable);
        ps.setInt(6, compositeScopeTableSubId);
        ps.setString(7, defaultTypeTable);
        ps.setInt(8, defaultTypeTableSubId);
        ps.setString(9, defaultValueTable);
        ps.setInt(10, defaultValueTableSubId);
        ps.setInt(11, parameterPosition);
        ps.setInt(12, isAnonym);
        ps.setInt(13, isFinal);
        ps.setInt(14, isParameterPack);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPTemplateTemplateParameter_Debug(int iD, String name, int owner,
			String ownerVariable, String compositeScopeTable, String defaultTypeTable, String defaultValueTable, int parameterPosition, int isAnonym, int isFinal, int isParameterPack) {
    	
    	if(this.isInTable_Debug("PDOMCPPTemplateTemplateParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPTemplateTemplateParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`DefaultTypeTable`,`DefaultValueTable`,`parameterPosition`,`isAnonym`,`isFinal`,`isParameterPack`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ compositeScopeTable+ ","+defaultTypeTable + ","+ defaultValueTable //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		+ ","+ parameterPosition + ","+ isAnonym + "," + isFinal  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
        		 + "," + isParameterPack+ ");");   //$NON-NLS-1$ //$NON-NLS-2$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPTemplateTypeParameter(int iD, String name, int owner, String ownerVariable,String defaultTypeTable,int defaultTypeTableSubId, String defaultValueTable, int defaultValueTableSubId,
			int parameterPosition,int isParameterPack, boolean debug) throws SQLException  {
    	if(debug)
    		return InsertPDOMCPPTemplateTypeParameter_Debug(iD,  name,  owner,  ownerVariable,defaultTypeTable,defaultValueTable,parameterPosition,isParameterPack);
		
    	if(this.isInTable("PDOMCPPTemplateTypeParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPTemplateTypeParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DefaultTypeTable`,`DefaultTypeTableSubId`,`DefaultValueTable`,`DefaultValueTableSubId`,`parameterPosition`,`isParameterPack`) VALUES (?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, defaultTypeTable);
        ps.setInt(6, defaultTypeTableSubId);
        ps.setString(7, defaultValueTable);
        ps.setInt(8, defaultValueTableSubId);
        ps.setInt(9, parameterPosition);
        ps.setInt(10, isParameterPack);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPTemplateTypeParameter_Debug(int iD, String name, int owner,
			String ownerVariable, String defaultTypeTable, String defaultValue, int parameterPosition,
			int isParameterPack) {
    	
    	if(this.isInTable_Debug("PDOMCPPTemplateTypeParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPTemplateTypeParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DefaultTypeTable`,`DefaultValueTable`,`parameterPosition`,`isParameterPack`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ defaultTypeTable + ","+ defaultValue + "," + parameterPosition  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		 + "," + isParameterPack+ ");");   //$NON-NLS-1$ //$NON-NLS-2$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPTypedef(int iD, String name, int owner, String ownerVariable,String TypeTable, int TypeTableSubId, boolean debug) throws SQLException {
    	if(debug)
    		return InsertPDOMCPPTypedef_Debug(iD,  name,  owner,  ownerVariable,TypeTable);
		
    	if(this.isInTable("PDOMCPPTypedef", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPTypedef`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, TypeTable);
        ps.setInt(6, TypeTableSubId);
        
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPTypedef_Debug(int iD, String name, int owner, String ownerVariable,
			String typeTable) {
		

    	if(this.isInTable_Debug("PDOMCPPTypedef", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPTypedef`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ typeTable + ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPTypedefSpecialization(int iD, String name, int owner, String ownerVariable,String TypeTable, int TypeTableSubId, boolean debug) throws SQLException  {
		if(debug)
    		return InsertPDOMCPPTypedefSpecialization_Debug(iD,  name,  owner,  ownerVariable,TypeTable);
		
    	if(this.isInTable("PDOMCPPTypedefSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPTypedefSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, TypeTable);
        ps.setInt(6, TypeTableSubId);
        
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;     	
      }
    
	private boolean InsertPDOMCPPTypedefSpecialization_Debug(int iD, String name, int owner,
			String ownerVariable, String typeTable) {
    	
    	if(this.isInTable_Debug("PDOMCPPTypedefSpecialization", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPTypedefSpecialization`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+ typeTable + ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPUnknownMemberClass(int iD, boolean debug) throws SQLException {
	 	
		if(debug)
    		return InsertPDOMCPPUnknownMemberClass_Debug(iD);
		
    	if(this.isInTable("PDOMCPPUnknownMember", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPUnknownMember`(`ID`) VALUES (?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
	private boolean InsertPDOMCPPUnknownMemberClass_Debug(int iD) {
		

    	if(this.isInTable_Debug("PDOMCPPUnknownMember", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPUnknownMember`(`ID`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;    	
    }
    
    public boolean InsertPDOMCPPUnknownMemberClassInstance(int iD, boolean debug) throws SQLException {
    	
    	if(debug)
    		return InsertPDOMCPPUnknownMemberClassInstance_Debug(iD);
		
    	if(this.isInTable("PDOMCPPUnknownMemberClassInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPUnknownMemberClassInstance`(`ID`) VALUES (?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
      }
    
    private boolean InsertPDOMCPPUnknownMemberClassInstance_Debug(int iD) {
    	
    	if(this.isInTable_Debug("PDOMCPPUnknownMemberClassInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPUnknownMemberClassInstance`(`ID`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}

	public boolean InsertPDOMCPPUnknownMethod(int iD, boolean debug) throws SQLException {
    	
		if(debug)
    		return InsertPDOMCPPUnknownMethod_Debug(iD);
		
    	if(this.isInTable("PDOMCPPUnknownMethod", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPUnknownMethod`(`ID`) VALUES (?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;      	
      }
    
    private boolean InsertPDOMCPPUnknownMethod_Debug(int iD) {

    	if(this.isInTable_Debug("PDOMCPPUnknownMethod", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPUnknownMethod`(`ID`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}

	public boolean InsertPDOMCPPUsingDeclaration(int iD, String name, int owner, String ownerVariable,boolean debug) throws SQLException {
  		
		if(debug)
    		return InsertPDOMCPPUsingDeclaration_Debug(iD,  name,  owner,  ownerVariable);
		
    	if(this.isInTable("PDOMCPPUsingDeclaration", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPUsingDeclaration`(`ID`,`Name`,`Owner`,`OwnerVariable`) VALUES (?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);        
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
      }
    
	private boolean InsertPDOMCPPUsingDeclaration_Debug(int iD, String name, int owner,
			String ownerVariable) {
    	
    	if(this.isInTable_Debug("PDOMCPPUsingDeclaration", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPUsingDeclaration`(`ID`,`Name`,`Owner`,`OwnerVariable`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPVariable(int iD, String name, int owner, String ownerVariable,long value, String TypeTable, int TypeTableSubId, 
			int isAuto,int isConstExpr,int isExtern,int isExternC,int isMutable,int isRegister,int isStatic,boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCPPVariable_Debug(iD,  name,  owner,  ownerVariable,value,TypeTable,isAuto,isConstExpr,isExtern,isExternC,isMutable,isRegister,isStatic);
		
    	if(this.isInTable("PDOMCPPVariable", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPVariable`(`ID`,`Name`,`Owner`,`OwnerVariable`,`Value`,`TypeTable`,`TypeTableSubId`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, value);
        ps.setString(6, TypeTable);
        ps.setInt(7, TypeTableSubId);
        ps.setInt(8, isAuto);
        ps.setInt(9, isConstExpr);
        ps.setInt(10, isExtern);
        ps.setInt(11, isExternC);
        ps.setInt(12, isMutable);
        ps.setInt(13, isRegister);
        ps.setInt(14, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;     	
      }
      
    private boolean InsertPDOMCPPVariable_Debug(int iD, String name, int owner, String ownerVariable,
			long initialValue, String typeTable, int isAuto, int isConstExpr, int isExtern, int isExternC,
			int isMutable, int isRegister, int isStatic) {
		
    	if(this.isInTable_Debug("PDOMCPPVariable", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPVariable`(`ID`,`Name`,`Owner`,`OwnerVariable`,`Value`,`TypeTable`,`isAuto`,`isConstExpr`,`isExtern`,`isExternC`,`isMutable`,`isRegister`,`isStatic`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + "," + initialValue + "," + typeTable + "," + isAuto + "," + isConstExpr //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        		+ "," + isExtern + "," +isExternC + "," + isMutable + "," +isRegister + "," + isStatic +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 
    	
    	return false;
	}

	public boolean InsertProblemBinding(int iD, boolean debug) throws SQLException {
		if(debug)
    		return InsertProblemBinding_Debug(iD);
		
    	if(this.isInTable("ProblemBinding", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ProblemBinding`(`ID`) VALUES (?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;     	
      }
       
	private boolean InsertProblemBinding_Debug(int iD) {
		
    	if(this.isInTable_Debug("ProblemBinding", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ProblemBinding`(`ID`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}

	public boolean InsertProblemType(int iD, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertProblemType_Debug(iD);
		
    	if(this.isInTable("ProblemType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `ProblemType`(`ID`) VALUES (?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;     	
      }
       
	private boolean InsertProblemType_Debug(int iD) {
    	
    	if(this.isInTable_Debug("ProblemType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `ProblemType`(`ID`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}

	public boolean InsertTypeOfDependentExpression(int iD, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertTypeOfDependentExpression_Debug(iD);
		
    	if(this.isInTable("TypeOfDependentExpression", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `TypeOfDependentExpression`(`ID`) VALUES (?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;    	
      }
      
	private boolean InsertTypeOfDependentExpression_Debug(int iD) {
    	
    	if(this.isInTable_Debug("TypeOfDependentExpression", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `TypeOfDependentExpression`(`ID`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}
    
	public boolean InsertPDOMCPPClassScope(int iD, String scopeName, int owner, String ownerVariable,
			String classTypeTable, int classTypeTableSubId, String scopeBindingTable, int scopeBindingTableSubid, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCPPClassScope_Debug(iD,scopeName, owner,ownerVariable,classTypeTable,scopeBindingTable);    	
    	
    	if(this.isInTable("PDOMCPPClassScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPClassScope`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ClassTypeTable`,`ClassTypeTableSubId`,`ScopeBindingTable`,`ScopeBindingTableSubId`) VALUES (?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, scopeName);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, classTypeTable);
        ps.setInt(6, classTypeTableSubId);
        ps.setString(7, scopeBindingTable);
        ps.setInt(8, scopeBindingTableSubid);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCPPClassScope_Debug(int iD, String scopeName, int owner, String ownerVariable,
			String classTypeTable, String scopeBindingsTable) {
		
		if(this.isInTable_Debug("PDOMCPPClassScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPClassScope`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ClassTypeTable`,`ScopeBindingTable`)  "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  scopeName + ", "+ owner+ "," + ownerVariable+ "," +classTypeTable+ "," + scopeBindingsTable +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPBase(int iD, String classDefinitionName, int owner, String ownerVariable,
			String baseClassTable, int baseClassTableSubId, String baseClassTypeTable,int baseClassTypeTableSubId, int isInheritedConstructorsSource,
			int isVirtual, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCPPBase_Debug(iD,classDefinitionName, owner,ownerVariable,baseClassTable,baseClassTypeTable,isInheritedConstructorsSource,isVirtual);    	
    	
    	if(this.isInTable("PDOMCPPBase", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPBase`(`ID`,`Name`,`Owner`,`OwnerVariable`,`BaseClassTable`,`BaseClassTableSubId`,`BaseClassTypeTable`,`BaseClassTypeTableSubId`,`isInheritedConstructorsSource`,`isVirtual`) VALUES (?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, classDefinitionName);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, baseClassTable);
        ps.setInt(6, baseClassTableSubId);
        ps.setString(7, baseClassTypeTable);
        ps.setInt(8, baseClassTypeTableSubId);
        ps.setInt(9, isInheritedConstructorsSource);
        ps.setInt(10, isVirtual);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCPPBase_Debug(int iD, String classDefinitionName, int owner,
			String ownerVariable, String baseClassTable, String baseClassTypeTable,
			int isInheritedConstructorsSource, int isVirtual) {
		
		if(this.isInTable_Debug("PDOMCPPBase", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPBase`(`ID`,`Name`,`Owner`,`OwnerVariable`,`BaseClassTable`,`BaseClassTypeTable`,`isInheritedConstructorsSource`,`isVirtual`) "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  classDefinitionName + ", "+ owner+ "," + ownerVariable+ "," +baseClassTable+ "," + baseClassTypeTable+ "," +isInheritedConstructorsSource+ "," + isVirtual+  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ 
    	
    	return false;
	}

	public boolean InsertCPPTemplateNonTypeArgument(int iD, String string, int owner, String ownerVariable,
			long nonTypeValue, String expansionPatternTable, int expansionPatternTableSubId,String nonTypeEvaluationTable, int nonTypeEvaluationTableSubId,
			String originalTypeValueTable, int originalTypeValueTableSubId, String typeOfNonTypeValueTable, int typeOfNonTypeValueTableSubId,  String typeValueTable, int typeValueTableSubId, int isNonTypeValue, int isPackExpansion,
			int isTypeValue, boolean debug) throws SQLException {
		

		if(debug)
    		return InsertCPPTemplateNonTypeArgument_Debug(iD,string, owner,ownerVariable,nonTypeValue,expansionPatternTable,nonTypeEvaluationTable,originalTypeValueTable,typeValueTable,isNonTypeValue, isPackExpansion, isTypeValue);    	
    	
    	if(this.isInTable("PDOMCPPBase", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPPTemplateNonTypeArgument`(`ID`,`Name`,`Owner`,`OwnerVariable`,`NonTypeValue`,`ExpansionPatternTable`,`ExpansionPatternTableSubId`,`NonTypeEvaluationTable`,`NonTypeEvaluationTableSubId`,`OriginalTypeValueTable`,`OriginalTypeValueTableSubId`,`TypeOfNonTypeValueTable`,`TypeOfNonTypeValueTableSubId`,`TypeValueTable`,`TypeValueTableSubId`,`isNonTypeValue`,`isPackExpansion`,`isTypeValue`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, string);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, nonTypeValue);
        ps.setString(6, expansionPatternTable);
        ps.setInt(7, expansionPatternTableSubId);
        ps.setString(8, nonTypeEvaluationTable);
        ps.setInt(9, nonTypeEvaluationTableSubId);
        ps.setString(10, originalTypeValueTable);
        ps.setInt(11, originalTypeValueTableSubId);
        ps.setString(12, typeOfNonTypeValueTable);
        ps.setInt(13, typeOfNonTypeValueTableSubId);
        ps.setString(14, typeValueTable);
        ps.setInt(15, typeValueTableSubId);
        ps.setInt(16, isNonTypeValue);
        ps.setInt(17, isPackExpansion);
        ps.setInt(18, isTypeValue);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertCPPTemplateNonTypeArgument_Debug(int iD, String string, int owner,
			String ownerVariable, long nonTypeValue, String expansionPatternTable,
			String nonTypeEvaluationTable, String originalTypeValueTable, String typeValueTable,
			int isNonTypeValue, int isPackExpansion, int isTypeValue) {
		
		if(this.isInTable_Debug("CPPTemplateNonTypeArgument", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPPTemplateNonTypeArgument`(`ID`,`Name`,`Owner`,`OwnerVariable`,`NonTypeValue`,`ExpansionPatternTable`,`NonTypeEvaluationTable`,`OriginalTypeValueTable`,`TypeOfNonTypeValueTable`,`TypeValueTable`,`isNonTypeValue`,`isPackExpansion`,`isTypeValue`)" +  //$NON-NLS-1$
        		 "VALUES (" + iD + ", " +  string + ", "+ owner+ "," + ownerVariable+ "," +nonTypeValue+ "," + expansionPatternTable+ "," +nonTypeEvaluationTable+ "," + originalTypeValueTable+  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
        		 "," +typeValueTable+ "," +isNonTypeValue+ "," +isPackExpansion+ "," +isTypeValue + ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
    	
    	return false;		
	}

	public boolean InsertPDOMCPPGlobalScope(int iD, String scopeName, int owner, String ownerVariable,
			String scopeBindingTable, int scopeBindingTableSubId, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCPPGlobalScope_Debug(iD,scopeName, owner,ownerVariable,scopeBindingTable);    	
    	
    	if(this.isInTable("PDOMCPPGlobalScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPGlobalScope`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ScopeBindingTable`,`ScopeBindingTableSubId`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, scopeName);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, scopeBindingTable);
        ps.setInt(6, scopeBindingTableSubId);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCPPGlobalScope_Debug(int iD, String scopeName, int owner, String ownerVariable,
			String scopeBindingTable) {
		
		if(this.isInTable_Debug("PDOMCPPGlobalScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPGlobalScope`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ScopeBindingTable`) "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  scopeName + ", "+ owner+ "," + ownerVariable+ "," +scopeBindingTable +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 
    	
    	return false;
	}

	public boolean InsertPDOMCPPClassSpecializationScope(int iD, String scopeName, int owner,
			String ownerVariable, String classTypeTable, int classTypeTableSubId, String originalClassTypeTable, int originalClassTypeTableSubId, 
			String scopeBindingTable, int scopeBindingTableSubId,  boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCPPClassSpecializationScope_Debug(iD,scopeName, owner,ownerVariable,classTypeTable,originalClassTypeTable,scopeBindingTable);    	
    	
    	if(this.isInTable("PDOMCPPClassSpecializationScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPClassSpecializationScope`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ClassTypeTable`,`ClassTypeTableSubId`,`OriginalClassTypeTable`,`OriginalClassTypeTableSubId`,`ScopeBindingTable`,`ScopeBindingTableSubId`) VALUES (?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, scopeName);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, classTypeTable);
        ps.setInt(6, classTypeTableSubId);
        ps.setString(7, originalClassTypeTable);
        ps.setInt(8, originalClassTypeTableSubId);
        ps.setString(9, scopeBindingTable);
        ps.setInt(10, scopeBindingTableSubId);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCPPClassSpecializationScope_Debug(int iD, String scopeName, int owner,
			String ownerVariable, String classTypeTable, String originalClassTypeTable,
			String scopeBindingTable) {
		
		if(this.isInTable_Debug("PDOMCPPClassSpecializationScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPClassSpecializationScope`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ClassTypeTable`,`OriginalClassTypeTable`,`ScopeBindingTable`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  scopeName + ", "+ owner+ "," + ownerVariable+ "," +classTypeTable + "," +originalClassTypeTable+ "," +scopeBindingTable +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ 
    	
    	return false;
	}

	public boolean InsertPDOMCEnumeration(int iD, String name, int owner, String ownerVariable, long minValue,
			long maxValue, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCEnumeration_Debug(iD,name, owner,ownerVariable,minValue,maxValue);    	
    	
    	if(this.isInTable("PDOMCEnumeration", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCEnumeration`(`ID`,`Name`,`Owner`,`OwnerVariable`,`minValue`,`maxValue`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, minValue);
        ps.setLong(6, maxValue);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCEnumeration_Debug(int iD, String name, int owner, String ownerVariable,
			long minValue, long maxValue) {
		
		if(this.isInTable_Debug("PDOMCPPClassSpecializationScope", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCEnumeration`(`ID`,`Name`,`Owner`,`OwnerVariable`,`minValue`,`maxValue`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable+ "," +minValue + "," +maxValue +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 
    	
    	return false;
	}

	public boolean InsertPDOMCEnumerator(int iD, String name, int owner, String ownerVariable,
			String typeTable,int TypeTableSubId, long value, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCEnumerator_Debug(iD,name, owner,ownerVariable,typeTable,value);    	
    	
    	if(this.isInTable("PDOMCEnumerator", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCEnumerator`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`Value`) VALUES (?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typeTable);
        ps.setInt(6, TypeTableSubId);
        ps.setLong(7, value);
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCEnumerator_Debug(int iD, String name, int owner, String ownerVariable,
			String typeTable, long value) {
		
		if(this.isInTable_Debug("PDOMCEnumerator", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCEnumerator`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`Value`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable+ "," +typeTable + "," +value +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 
    	
    	return false;
	}

	public boolean InsertPDOMCField(int iD, String name, int owner, String ownerVariable, long initialValue,
			String typeTable, int TypeTableSubId, String compositeTypOwnereTable, int CompositeTypeOwnereSubId,int isAuto, int isExtern, int isRegister,
			int isStatic, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCField_Debug(iD,name, owner,ownerVariable,initialValue,
    				typeTable, compositeTypOwnereTable, isAuto, isExtern, isRegister,
    				isStatic)  ;  	
    	
    	if(this.isInTable("PDOMCField", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCField`(`ID`,`Name`,`Owner`,`OwnerVariable`,`initialValue`,`TypeTable`,`TypeTableSubId`,`CompositeTypeOwnereTable`,`CompositeTypeOwnereSubId`,`isAuto`,`isExtern`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, initialValue);
        ps.setString(6, typeTable);
        ps.setInt(7, TypeTableSubId);
        ps.setString(8, compositeTypOwnereTable);
        ps.setInt(9, CompositeTypeOwnereSubId);
        ps.setInt(10, isAuto);
        ps.setInt(11, isExtern);
        ps.setInt(12, isRegister);
        ps.setInt(13, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCField_Debug(int iD, String name, int owner, String ownerVariable,
			long initialValue, String typeTable, String compositeTypOwnereTable, int isAuto, int isExtern,
			int isRegister, int isStatic) {
		
		if(this.isInTable_Debug("PDOMCField", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCField`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InitialValue`,`TypeTable`,`CompositeTypOwnereTable`,`isAuto`,`isExtern`,`isRegister`,`isStatic`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable+ "," +initialValue + "," +typeTable+ "," + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		compositeTypOwnereTable + "," +isAuto+ "," +isExtern+ "," +isRegister+ "," +isStatic +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
    	
    	return false;  	
	}

	public boolean InsertPDOMCFunction(int iD, String name, int owner, String ownerVariable,
			String functionScopeTable,int FunctionScopeTableSubId, String typeTable,int TypeTableSubId, int isAuto, int isExtern, int isInline,
			int isNoReturn, int isRegister, int isStatic, int takesVarArgs, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCFunction_Debug(iD,name, owner,ownerVariable,functionScopeTable,
    				typeTable, isAuto, isExtern, isInline,isNoReturn,isRegister,
    				isStatic,takesVarArgs)  ;  	
    	
    	if(this.isInTable("PDOMCFunction", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCFunction`(`ID`,`Name`,`Owner`,`OwnerVariable`,`FunctionScopeTable`,`FunctionScopeTableSubId`,`TypeTable`,`TypeTableSubId`,`isAuto`,`isExtern`,`isInline`,`isNoReturn`,`isRegister`,`isStatic`,`takesVarArgs`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, functionScopeTable);
        ps.setInt(6, FunctionScopeTableSubId);
        ps.setString(7, typeTable);
        ps.setInt(8, TypeTableSubId);
        ps.setInt(9, isAuto);
        ps.setInt(10, isExtern);
        ps.setInt(11, isInline);
        ps.setInt(12, isNoReturn);
        ps.setInt(13, isRegister);
        ps.setInt(14, isStatic);
        ps.setInt(15, takesVarArgs);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCFunction_Debug(int iD, String name, int owner, String ownerVariable,
			String functionScopeTable, String functionTypeTable, int isAuto, int isExtern, int isInline,
			int isNoReturn, int isRegister, int isStatic, int takesVarArgs) {
		
		if(this.isInTable_Debug("PDOMCFunction", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCFunction`(`ID`,`Name`,`Owner`,`OwnerVariable`,`FunctionScopeTable`,`TypeTable`,`isAuto`,`isExtern`,`isInline`,`isNoReturn`,`isRegister`,`isStatic`,`takesVarArgs`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable+ "," +functionScopeTable + "," +functionTypeTable+ "," + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		isAuto + "," +isExtern+ "," +isInline+ "," +isNoReturn+ "," +isRegister + "," +isStatic+ "," +takesVarArgs +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 
    	
    	return false;
	}

	public boolean InsertIParameter(int iD, int owner, String ownerVariable,
			String tableRef,int TableRefSubId, int ArrayPos,boolean debug) throws SQLException {
		
		if(debug)
    		return InsertIParameter_Debug(iD,owner,ownerVariable,ArrayPos)  ;  
		
		if(this.isInTable("IParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `IParameter`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`TableRefSubId`,`ArrayPos`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setInt(2, owner);
        ps.setString(3, ownerVariable);
        ps.setString(4, tableRef);
        ps.setInt(5, TableRefSubId);
        ps.setInt(6, ArrayPos);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;		
	}
	
	
	
	private boolean InsertIParameter_Debug(int iD, int owner, String ownerVariable, int arrayPos) {
		
		if(this.isInTable_Debug("IParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `IParameter`(`ID`,`Owner`,`OwnerVariable`,`TableRef`,`ArrayPos`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD  + ", "+ owner+ "," + ownerVariable+ "," + arrayPos +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
    	
    	return false;
	}

	public boolean InsertPDOMCParameter(int iD, String name, int owner, String ownerVariable,
			long initialValue, String scopeTable, int ScopeTableSubId, String typeTable,int TypeTableSubId, int hasDeclaration, int hasDefinition,
			int isAuto, int isExtern, int isRegister, int isStatic, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCParameter_Debug(iD,name, owner,ownerVariable,initialValue,
    				scopeTable, typeTable, hasDeclaration, hasDefinition,isAuto,isExtern,
    				isRegister,isStatic)  ;  	
    	
    	if(this.isInTable("PDOMCParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InitialValue`,`ScopeTable`,`ScopeTableSubId`,`TypeTable`,`TypeTableSubId`,`hasDeclaration`,`hasDefinition`,`isAuto`,`isExtern`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, initialValue);
        ps.setString(6, scopeTable);
        ps.setInt(7, ScopeTableSubId);
        ps.setString(8, typeTable);
        ps.setInt(9, TypeTableSubId);
        ps.setInt(10, hasDeclaration);
        ps.setInt(11, hasDefinition);
        ps.setInt(12, isAuto);
        ps.setInt(13, isExtern);
        ps.setInt(14, isRegister);
        ps.setInt(15, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCParameter_Debug(int iD, String name, int owner, String ownerVariable,
			long initialValue, String scopeTable, String typTable, int hasDeclaration, int hasDefinition,
			int isAuto, int isExtern, int isRegister, int isStatic) {
		
		if(this.isInTable_Debug("PDOMCParameter", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCParameter`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InitialValue`,`ScopeTable`,`TypeTable`,`hasDeclaration`,`hasDefinition`,`isAuto`,`isExtern`,`isRegister`,`isStatic`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable+ "," +initialValue + "," +scopeTable+ "," + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		typTable + "," +hasDeclaration+ "," +hasDefinition+ "," +isAuto+ "," +isExtern + "," +isRegister+ "," +isStatic +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 
    	
    	return false;
	}

	public boolean InsertPDOMCStructure(int iD, String name, int owner, String ownerVariable,
			String compositeScopeTable,int CompositeScopeSubId, String compositeTypTable, int CompositeTypeSubId, String sopeBindingTable,int ScopeBindingTSubId, int isAnonymous,
			boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCStructure_Debug(iD,name, owner,ownerVariable,compositeScopeTable,
    				compositeTypTable, sopeBindingTable, isAnonymous)  ;  	
    	
    	if(this.isInTable("PDOMCStructure", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCStructure`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`CompositeScopeSubId`,`CompositeTypeTable`,`CompositeTypeSubId`,`ScopeBindingTable`,`ScopeBindingTSubId`,`isAnonymous`) VALUES (?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, compositeScopeTable);
        ps.setInt(6, CompositeScopeSubId);
        ps.setString(7, compositeTypTable);
        ps.setInt(8, CompositeTypeSubId);
        ps.setString(9, sopeBindingTable);
        ps.setInt(10, ScopeBindingTSubId);
        ps.setInt(11, isAnonymous);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCStructure_Debug(int iD, String name, int owner, String ownerVariable,
			String compositeScopeTable, String compositeTypTable, String sopeBindingTable, int isAnonymous) {
		
		if(this.isInTable_Debug("PDOMCStructure", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCStructure`(`ID`,`Name`,`Owner`,`OwnerVariable`,`CompositeScopeTable`,`CompositeTypeTable`,`ScopeBindingTable`,`isAnonymous`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable+ "," +compositeScopeTable + "," +compositeTypTable+ "," + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		sopeBindingTable + "," +isAnonymous+  ");");   //$NON-NLS-1$ //$NON-NLS-2$ 
    	
    	return false;
	}

	public boolean InsertPDOMCTypedef(int iD, String name, int owner, String ownerVariable, String typTable,int TypeTableSubId,
			boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCTypedef_Debug(iD,name, owner,ownerVariable,typTable)  ;  	
    	
    	if(this.isInTable("PDOMCTypedef", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCTypedef`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`) VALUES (?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typTable);
        ps.setInt(6, TypeTableSubId);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCTypedef_Debug(int iD, String name, int owner, String ownerVariable,
			String typTable) {
		
		if(this.isInTable_Debug("PDOMCTypedef", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCTypedef`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable+ "," +typTable +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 
    	
    	return false;
	}

	public boolean InsertPDOMCVariable(int iD, String name, int owner, String ownerVariable,
			long initialValue, String typeTable, int TypeTableSubId, int isAuto, int isExtern, int isRegister, int isStatic,
			boolean debug) throws SQLException {
		

		if(debug)
    		return InsertPDOMCVariable_Debug(iD,name, owner,ownerVariable,initialValue,
    				typeTable,isAuto,isExtern,
    				isRegister,isStatic)  ;  	
    	
    	if(this.isInTable("PDOMCVariable", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCVariable`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InitialValue`,`TypeTable`,`TypeTableSubId`,`isAuto`,`isExtern`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setLong(5, initialValue);
        ps.setString(6, typeTable);
        ps.setInt(7, TypeTableSubId);
        ps.setInt(8, isAuto);
        ps.setInt(9, isExtern);
        ps.setInt(10, isRegister);
        ps.setInt(11, isStatic);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}

	private boolean InsertPDOMCVariable_Debug(int iD, String name, int owner, String ownerVariable,
			long initialValue, String typTable,  int isAuto, int isExtern, int isRegister,
			int isStatic) {

		if(this.isInTable_Debug("PDOMCVariable", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCVariable`(`ID`,`Name`,`Owner`,`OwnerVariable`,`InitialValue`,`TypeTable`,`isAuto`,`isExtern`,`isRegister`,`isStatic`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable+ "," +initialValue + "," +typTable+ "," + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		isAuto + "," +isExtern + "," +isRegister + "," +isStatic+  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
    	
    	return false;
	}

	public boolean InsertCArrayType(int iD, String string, int owner, String ownerVariable, String typTable,int typTableSubId,
			long size, int hasSize, int isConst, int isRestrict, int isStatic, int isVariableLength,
			int isVolatile, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertCArrayType_Debug(iD,string, owner,ownerVariable,typTable,
    				size,hasSize,isConst,isRestrict,isStatic,
    				isVariableLength,isVolatile)  ;  	
    	
    	if(this.isInTable("CArrayType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CArrayType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`size`,`hasSize`,`isConst`,`isRestrict`,`isStatic`,`isVariableLength`,`isVolatile`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, string);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typTable);
        ps.setInt(6, typTableSubId);
        ps.setLong(7, size);
        ps.setInt(8, hasSize);
        ps.setInt(9, isConst);
        ps.setInt(10, isRestrict);
        ps.setInt(11, isStatic);
        ps.setInt(12, isVariableLength);
        ps.setInt(13, isVolatile);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}


	private boolean InsertCArrayType_Debug(int iD, String string, int owner, String ownerVariable,
			String typTable, long size, int hasSize, int isConst, int isRestrict, int isStatic,
			int isVariableLength, int isVolatile) {
		
		if(this.isInTable_Debug("CArrayType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CArrayType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`size`,`hasSize`,`isConst`,`isRestrict`,`isStatic`,`isVariableLength`,`isVolatile`)"  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  string + ", "+ owner+ "," + ownerVariable+ "," +typTable + "," +size+ "," + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        		hasSize + "," +isConst + "," +isRestrict + "," +isStatic + "," + isVariableLength+ "," + isVolatile +  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 
    	
    	return false;
	}


	public boolean InsertCBasicType(int iD, String name, int owner, String ownerVariable, boolean debug) throws SQLException {
		

		if(debug)
    		return InsertCBasicType_Debug(iD,name, owner,ownerVariable)  ;  	
    	
    	if(this.isInTable("CBasicType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CBasicType`(`ID`,`Name`,`Owner`,`OwnerVariable`) VALUES (?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}


	private boolean InsertCBasicType_Debug(int iD, String name, int owner, String ownerVariable) {
		
		if(this.isInTable_Debug("CBasicType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CBasicType`(`ID`,`Name`,`Owner`,`OwnerVariable`) "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable+  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
    	
    	return false;
	}


	public boolean InsertCFunctionType(int iD, String string, int owner, String ownerVariable,
			String returnTypeTable, int ReturnTypeTableSubId,  int takesVarArgs, boolean debug) throws SQLException {
		

		if(debug)
    		return InsertCFunctionType_Debug(iD,string, owner,ownerVariable,returnTypeTable,
    				takesVarArgs)  ;  	
    	
    	if(this.isInTable("CFunctionType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CFunctionType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ReturnTypeTable`,`ReturnTypeTableSubId`,`takesVarArgs`) VALUES (?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, string);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, returnTypeTable);
        ps.setInt(6, ReturnTypeTableSubId);
        ps.setInt(7, takesVarArgs);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}


	private boolean InsertCFunctionType_Debug(int iD, String string, int owner, String ownerVariable,
			String returnTypeTable, int takesVarArgs) {
		
		if(this.isInTable_Debug("CFunctionType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CFunctionType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`ReturnTypeTable`,`takesVarArgs`) "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  string + ", "+ owner+ "," + ownerVariable+ "," +returnTypeTable+ "," +takesVarArgs+  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 
    	
    	return false;
	}


	public boolean InsertCPointerType(int iD, String string, int owner, String ownerVariable,
			String typeTable,int TypeTableSubId, int isConst, int isRestrict, int isVolatile, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertCPointerType_Debug(iD,string, owner,ownerVariable,typeTable,isConst,isRestrict,
    				isVolatile)  ;  	
    	
    	if(this.isInTable("CPointerType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CPointerType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`isConst`,`isRestrict`,`isVolatile`) VALUES (?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, string);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typeTable);
        ps.setInt(6, TypeTableSubId);
        ps.setInt(7, isConst);
        ps.setInt(8, isRestrict);
        ps.setInt(9, isVolatile);

        
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}


	private boolean InsertCPointerType_Debug(int iD, String string, int owner, String ownerVariable,
			String typeTable, int isConst, int isRestrict, int isVolatile) {
		
		if(this.isInTable_Debug("CPointerType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CPointerType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`isConst`,`isRestrict`,`isVolatile`) "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  string + ", "+ owner+ "," + ownerVariable+ "," +typeTable+ "," +isConst+ "," +isRestrict+ "," +isVolatile+  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ 
    	
    	return false;
	}


	public boolean InsertCQualifierType(int iD, String string, int owner, String ownerVariable,
			String typeTable,int TypeTableSubId, int isConst, int isRestrict, int isVolatile, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertCQualifierType_Debug(iD,string, owner,ownerVariable,typeTable,isConst,isRestrict,
    				isVolatile)  ;  	
    	
    	if(this.isInTable("CQualifierType", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `CQualifierType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`TypeTableSubId`,`isConst`,`isRestrict`,`isVolatile`) VALUES (?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, string);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, typeTable);
        ps.setInt(6, TypeTableSubId);
        ps.setInt(7, isConst);
        ps.setInt(8, isRestrict);
        ps.setInt(9, isVolatile);

        
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}


	private boolean InsertCQualifierType_Debug(int iD, String string, int owner, String ownerVariable,
			String typeTable, int isConst, int isRestrict, int isVolatile) {
		
		if(this.isInTable_Debug("CQualifierType", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `CQualifierType`(`ID`,`Name`,`Owner`,`OwnerVariable`,`TypeTable`,`isConst`,`isRestrict`,`isVolatile`) "  //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  string + ", "+ owner+ "," + ownerVariable+ "," +typeTable+ "," +isConst+ "," +isRestrict+ "," +isVolatile+  ");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ 
    	
    	return false;
	}


	public boolean InsertPDOMCPPFunctionTemplate(int iD, String name, int owner, String ownerVariable,
			String functionTypeTable, int functionTypeTableSubId, String declaredTypTable, int declaredTypTableSubId,  String functionScopeTable, int functionScopeTableSubId, int hasParameterPack, int requiredArgumentCount, int isAuto, int isConstExpr,int isDeleted, 
			int isExtern, int isExternC, int isInline, int isMutable, int isNoReturn, int isRegister,
			int isStatic, boolean debug) throws SQLException {
		
	   	if(debug)
    		return InsertPDOMCPPFunctionTemplate_Debug(iD,  name,  owner,  ownerVariable,functionTypeTable,
    				declaredTypTable, functionScopeTable, hasParameterPack,requiredArgumentCount,
    				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic);
		
    	if(this.isInTable("PDOMCPPFunctionTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPFunctionTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`FunctionTypeTable`,`FunctionTypeTableSubId`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScopeTable`,`FunctionScopeTableSubId`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    				       		   

        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, functionTypeTable);
        ps.setInt(6, functionTypeTableSubId);
        ps.setString(7, declaredTypTable);
        ps.setInt(8, declaredTypTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setInt(11, hasParameterPack);
        ps.setInt(12, requiredArgumentCount);
        ps.setInt(13, isAuto);
        ps.setInt(14, isConstExpr);
        ps.setInt(15, isDeleted);
        ps.setInt(16, isExtern);
        ps.setInt(17, isExternC);
        ps.setInt(18, isInline);
        ps.setInt(19, isMutable);
        ps.setInt(20, isNoReturn);
        ps.setInt(21, isRegister);
        ps.setInt(22, isStatic);
       
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
      	
      }
    
	private boolean InsertPDOMCPPFunctionTemplate_Debug(int iD, String name, int owner, String ownerVariable,
			String functionTypeTable, String declaredTypTable, String functionScopeTable, int hasParameterPack, int requiredArgumentCount, int isAuto,
			int isConstExpr, int isDeleted, int isExtern, int isExternC, int isInline, int isMutable,
			int isNoReturn, int isRegister, int isStatic) {
		
    	if(this.isInTable_Debug("PDOMCPPFunctionTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPFunctionTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`FunctionTypeTable`,`DeclaredTypTable`,`FunctionScopeTable`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`)" //$NON-NLS-1$
        		+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+functionTypeTable + ","  +declaredTypTable+ ","  +functionScopeTable+ ","  +hasParameterPack+ ","  + ","+requiredArgumentCount +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		"," + isAuto+ "," +isConstExpr + "," + isDeleted + "," +isExtern + "," +isExternC+ "," +isInline + "," +isMutable + "," +isNoReturn+ "," +isRegister //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
        		+ "," +isStatic +");");   //$NON-NLS-1$ //$NON-NLS-2$
    	
    	return false;
	}
	


	public boolean InsertPDOMCPPMethodTemplate(int iD, String name, int owner, String ownerVariable,
			String functionTypeTable, int functionTypeTableSubId, String declaredTypTable, int declaredTypTableSubId, String functionScopeTable, int functionScopeTableSubId, int hasParameterPack, int requiredArgumentCount, int isAuto, int isConstExpr,int isDeleted, 
			int isExtern, int isExternC, int isInline, int isMutable, int isNoReturn, int isRegister,
			int isStatic, int isDestructor, int isExplicit, int isFinal, int isImplicit,
			int isOverride, int isPureVirtual, int isVirtual, boolean debug) throws SQLException {
		
		
	   	if(debug)
    		return InsertPDOMCPPMethodTemplate_Debug(iD,  name,  owner,  ownerVariable,functionTypeTable,
    				declaredTypTable, functionScopeTable, hasParameterPack,requiredArgumentCount,
    				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
    				isDestructor, isExplicit, isFinal, isImplicit,
    				isOverride, isPureVirtual, isVirtual);
		
    	if(this.isInTable("PDOMCPPMethodTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPMethodTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`FunctionTypeTable`,`FunctionTypeTableSubId`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScopeTable`,`FunctionScopeTableSubId`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    				       		   

        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, functionTypeTable);
        ps.setInt(6, functionTypeTableSubId);
        ps.setString(7, declaredTypTable);
        ps.setInt(8, declaredTypTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setInt(11, hasParameterPack);
        ps.setInt(12, requiredArgumentCount);
        ps.setInt(13, isAuto);
        ps.setInt(14, isConstExpr);
        ps.setInt(15, isDeleted);
        ps.setInt(16, isExtern);
        ps.setInt(17, isExternC);
        ps.setInt(18, isInline);
        ps.setInt(19, isMutable);
        ps.setInt(20, isNoReturn);
        ps.setInt(21, isRegister);
        ps.setInt(22, isStatic);
        ps.setInt(23, isDestructor);
        ps.setInt(24, isExplicit);
        ps.setInt(25, isFinal);
        ps.setInt(26, isImplicit);
        ps.setInt(27, isOverride);
        ps.setInt(28, isPureVirtual);
        ps.setInt(29, isVirtual);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}


	private boolean InsertPDOMCPPMethodTemplate_Debug(int iD, String name, int owner, String ownerVariable,
			String functionTypeTable, String declaredTypTable, String functionScopeTable, int hasParameterPack, int requiredArgumentCount, int isAuto,
			int isConstExpr, int isDeleted, int isExtern, int isExternC, int isInline, int isMutable,
			int isNoReturn, int isRegister, int isStatic, int isDestructor, int isExplicit, int isFinal, int isImplicit,
			int isOverride, int isPureVirtual, int isVirtual) {
		
		if(this.isInTable_Debug("PDOMCPPMethodTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPMethodTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`FunctionTypeTable`,`DeclaredTypTable`,`FunctionScopeTable`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`)" //$NON-NLS-1$
    			+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+functionTypeTable + ","  +declaredTypTable+ ","  +functionScopeTable+ ","  +hasParameterPack+ ","  + ","+requiredArgumentCount +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		"," + isAuto+ "," +isConstExpr + "," + isDeleted + "," +isExtern + "," +isExternC+ "," +isInline + "," +isMutable + "," +isNoReturn+ "," +isRegister //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
        		+ "," +isStatic+ "," +isDestructor+ "," +isExplicit+ "," +isFinal+ "," +isImplicit+ "," +isOverride+ "," +isPureVirtual + "," +isVirtual+");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
    	
    	return false;
	}


	public boolean InsertPDOMCPPConstructorTemplate(int iD, String name, int owner, String ownerVariable,
			String functionTypeTable, int functionTypeTableSubId, String declaredTypTable, int declaredTypTableSubId, String functionScopeTable, int functionScopeTableSubId, int hasParameterPack, int requiredArgumentCount, int isAuto, int isConstExpr,int isDeleted, 
			int isExtern, int isExternC, int isInline, int isMutable, int isNoReturn, int isRegister,
			int isStatic, int isDestructor, int isExplicit, int isFinal, int isImplicit,
			int isOverride, int isPureVirtual, int isVirtual, boolean debug) throws SQLException {
		
	   	if(debug)
    		return InsertPDOMCPPConstructorTemplate_Debug(iD,  name,  owner,  ownerVariable,functionTypeTable,
    				declaredTypTable, functionScopeTable, hasParameterPack,requiredArgumentCount,
    				isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
    				isDestructor, isExplicit, isFinal, isImplicit,
    				isOverride, isPureVirtual, isVirtual);
		
    	if(this.isInTable("PDOMCPPConstructorTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPConstructorTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`FunctionTypeTable`,`FunctionTypeTableSubId`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`FunctionScopeTable`,`FunctionScopeTableSubId`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    				       		   

        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, functionTypeTable);
        ps.setInt(6, functionTypeTableSubId);
        ps.setString(7, declaredTypTable);
        ps.setInt(8, declaredTypTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setInt(11, hasParameterPack);
        ps.setInt(12, requiredArgumentCount);
        ps.setInt(13, isAuto);
        ps.setInt(14, isConstExpr);
        ps.setInt(15, isDeleted);
        ps.setInt(16, isExtern);
        ps.setInt(17, isExternC);
        ps.setInt(18, isInline);
        ps.setInt(19, isMutable);
        ps.setInt(20, isNoReturn);
        ps.setInt(21, isRegister);
        ps.setInt(22, isStatic);
        ps.setInt(23, isDestructor);
        ps.setInt(24, isExplicit);
        ps.setInt(25, isFinal);
        ps.setInt(26, isImplicit);
        ps.setInt(27, isOverride);
        ps.setInt(28, isPureVirtual);
        ps.setInt(29, isVirtual);
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
	}


	private boolean InsertPDOMCPPConstructorTemplate_Debug(int iD, String name, int owner, String ownerVariable,
			String functionTypeTable, String declaredTypTable, String functionScopeTable, int hasParameterPack, int requiredArgumentCount, int isAuto,
			int isConstExpr, int isDeleted, int isExtern, int isExternC, int isInline, int isMutable,
			int isNoReturn, int isRegister, int isStatic, int isDestructor, int isExplicit, int isFinal, int isImplicit,
			int isOverride, int isPureVirtual, int isVirtual) {
		
		if(this.isInTable_Debug("PDOMCPPConstructorTemplate", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPConstructorTemplate`(`ID`,`Name`,`Owner`,`OwnerVariable`,`FunctionTypeTable`,`DeclaredTypTable`,`FunctionScopeTable`,`hasParameterPack`,`requiredArgumentCount`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`)" //$NON-NLS-1$
    			+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+functionTypeTable + ","  +declaredTypTable+ ","  +functionScopeTable+ ","  +hasParameterPack+ ","  + ","+requiredArgumentCount +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		"," + isAuto+ "," +isConstExpr + "," + isDeleted + "," +isExtern + "," +isExternC+ "," +isInline + "," +isMutable + "," +isNoReturn+ "," +isRegister //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
        		+ "," +isStatic+ "," +isDestructor+ "," +isExplicit+ "," +isFinal+ "," +isImplicit+ "," +isOverride+ "," +isPureVirtual + "," +isVirtual+");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
    	
    	return false;
	}


	public boolean InsertPDOMCPPMethodInstance(int iD, String name, int owner, String ownerVariable,String declaredTypeTable, int declaredTypeTableSubId, String typeTable, int typeTableSubId,
			String functionScopeTable, int functionScopeTableSubId, String templateDefinitionTable, int templateDefinitionTableSubId, int requiredArgumentCount,int isExplicitSpecialization,
			int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,int isInline,int isMutable,int isNoReturn,int isRegister,int isStatic,
			int hasParameterPack, int takesVarArgs, int isDestructor, int isExplicit, int isFinal, int isImplicit,
			int isOverride, int isPureVirtual, int isVirtual, boolean debug) throws SQLException {
		
		if(debug)
    		return InsertPDOMCPPMethodInstance_Debug(iD,  name,  owner,  ownerVariable,declaredTypeTable,typeTable,functionScopeTable,templateDefinitionTable,
    				requiredArgumentCount,isExplicitSpecialization,isAuto,isConstExpr,isDeleted,isExtern,isExternC,isInline,isMutable,isNoReturn,isRegister,isStatic,
    				hasParameterPack,takesVarArgs,isDestructor
    				,isExplicit,isFinal,isImplicit,isOverride,isPureVirtual,isVirtual);
		
    	if(this.isInTable("PDOMCPPMethodInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	PreparedStatement ps = connection
                .prepareStatement("INSERT INTO `PDOMCPPMethodInstance`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DeclaredTypeTable`,`DeclaredTypeTableSubId`,`TypeTable`,`TypeTableSubId`,`FunctionScopeTable`,`FunctionScopeTableSubId`,`TemplateDefinitionTable`,`TemplateDefinitionTableSubId`,`requiredArgumentCount`,`isExplicitSpecialization`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`hasParameterPack`,`takesVarArgs`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"); //$NON-NLS-1$
    	
        ps.setInt(1, iD);
        ps.setString(2, name);
        ps.setInt(3, owner);
        ps.setString(4, ownerVariable);
        ps.setString(5, declaredTypeTable);
        ps.setInt(6, declaredTypeTableSubId);
        ps.setString(7, typeTable);
        ps.setInt(8, typeTableSubId);
        ps.setString(9, functionScopeTable);
        ps.setInt(10, functionScopeTableSubId);
        ps.setString(11, templateDefinitionTable);
        ps.setInt(12, templateDefinitionTableSubId);
        ps.setInt(13, requiredArgumentCount);
        ps.setInt(14, isExplicitSpecialization);
        ps.setInt(15, isAuto);
        ps.setInt(16, isConstExpr);
        ps.setInt(17, isDeleted);
        ps.setInt(18, isExtern);
        ps.setInt(19, isExternC);
        ps.setInt(20, isInline);
        ps.setInt(21, isMutable);
        ps.setInt(22, isNoReturn);
        ps.setInt(23, isRegister);
        ps.setInt(24, isStatic);
        ps.setInt(25, hasParameterPack);
        ps.setInt(26, takesVarArgs);
        ps.setInt(27, isDestructor);
        ps.setInt(28, isExplicit);
        ps.setInt(29, isFinal);
        ps.setInt(30, isImplicit);
        ps.setInt(31, isOverride);
        ps.setInt(32, isPureVirtual);
        ps.setInt(33, isVirtual);        
        
        ps.addBatch();
        ps.executeBatch();   
        
        return false;
      	
      }
    
	private boolean InsertPDOMCPPMethodInstance_Debug(int iD, String name, int owner, String ownerVariable,String declaredTypeTable,String typeTable,
			String functionScopeTable,String templateDefinitionTable, int requiredArgumentCount,int isExplicitSpecialization,
			int isAuto,int isConstExpr,int isDeleted,int isExtern,int isExternC,int isInline,int isMutable,int isNoReturn,int isRegister,int isStatic,
			int hasParameterPack, int takesVarArgs, int isDestructor, int isExplicit, int isFinal, int isImplicit, int isOverride, int isPureVirtual, int isVirtual) {
		
    	if(this.isInTable_Debug("PDOMCPPMethodInstance", iD)) //$NON-NLS-1$
    		return true;
    	
    	this.inDatabase.put(iD, "someValue");  //$NON-NLS-1$
    	System.out.println("INSERT INTO `PDOMCPPMethodInstance`(`ID`,`Name`,`Owner`,`OwnerVariable`,`DeclaredTypeTable`,`TypeTable`,`FunctionScopeTable`,`TemplateDefinitionTable`,`requiredArgumentCount`,`isExplicitSpecialization`,`isAuto`,`isConstExpr`,`isDeleted`,`isExtern`,`isExternC`,`isInline`,`isMutable`,`isNoReturn`,`isRegister`,`isStatic`,`hasParameterPack`,`takesVarArgs`,`isDestructor`,`isExplicit`,`isFinal`,`isImplicit`,`isOverride`,`isPureVirtual`,`isVirtual`) " //$NON-NLS-1$
    			+ "VALUES (" + iD + ", " +  name + ", "+ owner+ "," + ownerVariable + ","+declaredTypeTable + ","+typeTable + "," +functionScopeTable + "," + templateDefinitionTable+ ","  + ","+requiredArgumentCount +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		"," +isExplicitSpecialization+ "," + isAuto+ "," +isConstExpr + "," + isDeleted + "," +isExtern + "," +isExternC+ "," +isInline + "," +isMutable + "," +isNoReturn+ "," +isRegister //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        		+ "," +isStatic + "," + hasParameterPack + "," +  takesVarArgs + "," +isDestructor+ "," +isExplicit+ "," +isFinal+ "," +isImplicit+ "," +isOverride+ "," +isPureVirtual+ "," +isVirtual +");");   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
    	
    	return false;
	}



	
    
    
    
}