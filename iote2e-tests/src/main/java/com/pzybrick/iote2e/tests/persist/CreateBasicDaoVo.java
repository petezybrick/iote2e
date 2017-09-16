/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.tests.persist;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.persist.CassandraBaseDao;


/**
 * The Class CreateBasicDaoVo.
 */
public class CreateBasicDaoVo {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(CassandraBaseDao.class);	
	
	/** The ds. */
	private BasicDataSource ds;
	
	/** The table names. */
	private List<String> tableNames;
	
	/** The target folder. */
	private String targetFolder;
	
	/** The package name. */
	private String packageName;
	
	/** The jdbc driver class name. */
	private String jdbcDriverClassName; // "oracle.jdbc.driver.OracleDriver" "com.mysql.jdbc.Driver";
	
//	"jdbcDriverClassName": "com.mysql.jdbc.Driver",
//	"jdbcInsertBlockSize": "1000",
//	"jdbcLogin": "",
//	"jdbcPassword": "Password*8",
//	"jdbcUrl": "jdbc:mysql://iote2e-mysql-master:3306/db_iote2e_batch",
	// "blood_glucose|blood_pressure" "/tmp" "com.pzybrick.iote2e.stream.persist" "iote2e_batch" "Password*8" "jdbc:mysql://localhost:3307/db_iote2e_batch"  "com.mysql.jdbc.Driver"
	
	
	/**
 * The main method.
 *
 * @param args the arguments
 */
public static void main(String[] args) {
		try {
			List<String> tableNames = Arrays.asList(args[0].split("[|]"));
			String targetFolder = args[1];
			String packageName = args[2];
	        String jdbcLogin = args[3];
	        String jdbcPassword = args[4];
	        String jdbcUrl = args[5];
	        String jdbcDriverClassName = args[6];
	        BasicDataSource ds = new BasicDataSource();
			ds.setDriverClassName(jdbcDriverClassName);
			ds.setUsername(jdbcLogin);
			ds.setPassword(jdbcPassword);
			ds.setUrl(jdbcUrl);
			ds.setDefaultAutoCommit(true);
			
			CreateBasicDaoVo generateVos = new CreateBasicDaoVo().setDs(ds).setTableNames(tableNames)
					.setPackageName(packageName).setTargetFolder(targetFolder).setJdbcDriverClassName(jdbcDriverClassName);
			generateVos.process();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Process.
	 *
	 * @throws Exception the exception
	 */
	public void process() throws Exception {
		Map<String, List<ColumnItem>> columnItemsByTableName = new HashMap<String, List<ColumnItem>>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			// get the column info for each table
			for (String tableName : tableNames) {
				System.out.println("processing " + tableName);
				ResultSet rsPK = databaseMetaData.getPrimaryKeys(null, null, tableName );
				Set<String> pkNames = new HashSet<String>();
				while( rsPK.next() ) {
					pkNames.add( rsPK.getString("COLUMN_NAME"));
				}
				
				List<ColumnItem> columnItems = new ArrayList<ColumnItem>();
				columnItemsByTableName.put(tableName, columnItems);
				ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, null);

				while (resultSet.next()) {
					String columnName = resultSet.getString("COLUMN_NAME");
					int dataType = resultSet.getInt("DATA_TYPE");
					long columnSize = resultSet.getLong("COLUMN_SIZE");
					int decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
					
					JdbcTypeItem jdbcTypeItem = findJdbcTypes( dataType, columnSize, decimalDigits );
					ColumnItem columnItem = new ColumnItem()
							.setName(columnName)
							.setType(dataType)
							.setSize(columnSize)
							.setDecimalDigits(decimalDigits)
							.setJavaType(jdbcTypeItem.getDataType())
							.setJdbcGet(jdbcTypeItem.getGetter())
							.setJdbcSet(jdbcTypeItem.getSetter())
							.setPk( pkNames.contains(columnName));
					columnItems.add(columnItem);
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}

		// create VO
		for( Map.Entry<String, List<ColumnItem>> entry : columnItemsByTableName.entrySet() ) {
			List<String> voClassRows = new ArrayList<String>();
			String className = tableNameToClass(entry.getKey()) + "Vo";
			voClassRows.add("package " + packageName + ";\n");
			voClassRows.add("");
			voClassRows.add("import java.sql.ResultSet;");
			voClassRows.add("import java.sql.SQLException;");
			voClassRows.add("import java.sql.Timestamp;");
			voClassRows.add("import org.apache.logging.log4j.LogManager;");
			voClassRows.add("import org.apache.logging.log4j.Logger;");
			voClassRows.add("");
			voClassRows.add("");
			voClassRows.add("public class " + className + " {");
			voClassRows.add("\tprivate static final Logger logger = LogManager.getLogger(" + className + ".class);");
			for( ColumnItem columnItem : entry.getValue() ) {
				String javaName = columnNameToAttribute( columnItem.getName() );
				voClassRows.add("\tprivate " + columnItem.getJavaType() + " " + javaName + ";");
			}
			// Constructors
			voClassRows.add("\n\n\tpublic " + className + "() {" );
			voClassRows.add("\t}" );
			voClassRows.add("\n\n\tpublic " + className + "(ResultSet rs) throws SQLException {" );
			for( ColumnItem columnItem : entry.getValue() ) {
				String javaName = columnNameToAttribute( columnItem.getName() );
				voClassRows.add("\t\tthis." + javaName + " = rs." + columnItem.getJdbcGet() + columnItem.getName() + "\");" );
			}
			voClassRows.add("\t}" );
			
			// Getters
			voClassRows.add("\n" );
			for( ColumnItem columnItem : entry.getValue() ) {
				String javaName = columnNameToAttribute( columnItem.getName() );
				String getter = "get" + javaName.substring(0, 1).toUpperCase() + javaName.substring(1);
				voClassRows.add("\tpublic " + columnItem.getJavaType() + " " + getter + "() {");
				voClassRows.add("\t\treturn this." + javaName + ";");
				voClassRows.add("\t}");
			}
			// Setters
			voClassRows.add("\n" );
			for( ColumnItem columnItem : entry.getValue() ) {
				String javaName = columnNameToAttribute( columnItem.getName() );
				String setter = "set" + javaName.substring(0, 1).toUpperCase() + javaName.substring(1);
				voClassRows.add("\tpublic " + className + " " + setter + "( " + columnItem.getJavaType() + " " + javaName + " ) {");
				voClassRows.add("\t\tthis." + javaName + " = " + javaName  + ";");
				voClassRows.add("\t\treturn this;");
				voClassRows.add("\t}");
			}
				
			voClassRows.add( "}" );
			
			// Empty builder
			voClassRows.add( "" );
			String instanceName = className.substring(0,1).toLowerCase() + className.substring(1);
			voClassRows.add( "// " + className + " " + instanceName + " = new " + className + "()");
			for( ColumnItem columnItem : entry.getValue() ) {
				String javaName = columnNameToAttribute( columnItem.getName() );
				voClassRows.add( "//\t .set" + javaName.substring(0, 1).toUpperCase() + javaName.substring(1) + "(\"xxx\")" );
			}
			voClassRows.add( "//\t ;" );

			
			File output = new File(this.targetFolder + className + ".java");
			output.delete();
			FileUtils.writeLines(output, voClassRows);
			System.out.println("===================================================");
			for( String voClassRow : voClassRows ) {
				System.out.println(voClassRow);
			}
		}

		// create DAO
		for( Map.Entry<String, List<ColumnItem>> entry : columnItemsByTableName.entrySet() ) {
			List<String> daoClassRows = new ArrayList<String>();
			String className = tableNameToClass(entry.getKey()) + "Dao";
			daoClassRows.add("package " + packageName + ";");
			daoClassRows.add("");
			daoClassRows.add("import java.sql.ResultSet;");
			daoClassRows.add("import java.sql.SQLException;");
			daoClassRows.add("import java.sql.Connection;");
			daoClassRows.add("import java.sql.Timestamp;");
			daoClassRows.add("import java.sql.PreparedStatement;");
			daoClassRows.add("import org.apache.logging.log4j.LogManager;");
			daoClassRows.add("import org.apache.logging.log4j.Logger;");
			daoClassRows.add("import com.pzybrick.iote2e.common.config.MasterConfig;");

			daoClassRows.add("");
			daoClassRows.add("");
			daoClassRows.add("public class " + className + " {");
			daoClassRows.add("\tprivate static final Logger logger = LogManager.getLogger(" + className + ".class);");
			
			StringBuilder sbDelete = new StringBuilder().append("\tprivate static String sqlDeleteByPk = \"DELETE FROM ").append(entry.getKey()).append(" WHERE ");
			int numPks = 0;
			for( ColumnItem columnItem : entry.getValue() ) {
				if( columnItem.isPk() ) {
					numPks++;
					if( numPks > 1 ) sbDelete.append(" AND ");
					sbDelete.append(columnItem.getName()).append("=?");
				}
			}
			sbDelete.append("\";");
			daoClassRows.add(sbDelete.toString());
			
			StringBuilder sbInsert = new StringBuilder().append("\tprivate static String sqlInsert = \"INSERT INTO ").append(entry.getKey()).append(" (");
			StringBuilder sbQmarks = new StringBuilder();
			boolean isFirst = true;
			for( ColumnItem columnItem : entry.getValue() ) {
				if( "insert_ts".equalsIgnoreCase(columnItem.getName())) continue;
				if( !isFirst ) {
					sbInsert.append(",");
					sbQmarks.append(",");
				} else isFirst = false;
				sbInsert.append(columnItem.getName());
				sbQmarks.append("?");
			}
			sbInsert.append(") VALUES (").append(sbQmarks).append(")\";");
			daoClassRows.add(sbInsert.toString());
			
			StringBuilder sbFind = new StringBuilder().append("\tprivate static String sqlFindByPk = \"SELECT ");
			isFirst = true;
			for( ColumnItem columnItem : entry.getValue() ) {
				if( !isFirst ) {
					sbFind.append(",");
				} else isFirst = false;
				sbFind.append(columnItem.getName());
			}
			sbFind.append(" FROM ").append(entry.getKey()).append(" WHERE ");
			numPks = 0;
			for( ColumnItem columnItem : entry.getValue() ) {
				if( columnItem.isPk() ) {
					numPks++;
					if( numPks > 1 ) sbDelete.append(" AND ");
					sbFind.append(columnItem.getName()).append("=?");
				}
			}
			sbFind.append("\";");			
			daoClassRows.add(sbFind.toString());
			daoClassRows.add("");
			
			String voName = tableNameToClass(entry.getKey()) + "Vo";
			String instanceName = voName.substring(0,1).toLowerCase() + voName.substring(1);
			
			daoClassRows.add("\tpublic static void insertBatchMode( Connection con, " + voName + " " + instanceName + " ) throws Exception {");
			daoClassRows.add("\t\tPreparedStatement pstmt = null;");
			daoClassRows.add("\t\ttry {");
			daoClassRows.add("\t\t\tpstmt = con.prepareStatement(sqlInsert);");
			daoClassRows.add("\t\t\tint offset = 1;");
			for( ColumnItem columnItem : entry.getValue() ) {
				if( "insert_ts".equalsIgnoreCase(columnItem.getName())) continue;
				String javaName = columnNameToAttribute( columnItem.getName() );
				String getter = "get" + javaName.substring(0, 1).toUpperCase() + javaName.substring(1) + "()";
				daoClassRows.add("\t\t\tpstmt." + columnItem.getJdbcSet() + " offset++, " + instanceName + "." + getter + " );");
			}
			daoClassRows.add("\t\t\tpstmt.execute();");
			daoClassRows.add("\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\tlogger.error(e.getMessage(), e);");
			daoClassRows.add("\t\t\tthrow e;");
			daoClassRows.add("\t\t} finally {");
			daoClassRows.add("\t\t\ttry {");
			daoClassRows.add("\t\t\t\tif (pstmt != null)");
			daoClassRows.add("\t\t\t\tpstmt.close();");
			daoClassRows.add("\t\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\t\tlogger.warn(e);");
			daoClassRows.add("\t\t\t}");
			daoClassRows.add("\t\t}");
			daoClassRows.add("\t}");
			
			daoClassRows.add("");
			
			
			daoClassRows.add("\tpublic static void insert( MasterConfig masterConfig, " + voName + " " + instanceName + " ) throws Exception {");
			daoClassRows.add("\t\tConnection con = null;");
			daoClassRows.add("\t\tPreparedStatement pstmt = null;");
			daoClassRows.add("\t\ttry {");
			daoClassRows.add("\t\t\tcon = PooledDataSource.getInstance(masterConfig).getConnection();");
			daoClassRows.add("\t\t\tcon.setAutoCommit(false);");
			daoClassRows.add("\t\t\tpstmt = con.prepareStatement(sqlInsert);");
			
			daoClassRows.add("\t\t\tint offset = 1;");
			for( ColumnItem columnItem : entry.getValue() ) {
				if( "insert_ts".equalsIgnoreCase(columnItem.getName())) continue;
				String javaName = columnNameToAttribute( columnItem.getName() );
				String getter = "get" + javaName.substring(0, 1).toUpperCase() + javaName.substring(1) + "()";
				daoClassRows.add("\t\t\tpstmt." + columnItem.getJdbcSet() + " offset++, " + instanceName + "." + getter + " );");
			}
			daoClassRows.add("\t\t\tpstmt.execute();");
			daoClassRows.add("\t\t\tcon.commit();");
			daoClassRows.add("\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\tlogger.error(e.getMessage(), e);");
			
			daoClassRows.add("\t\t\tif( con != null ) {");
			daoClassRows.add("\t\t\t\ttry {");
			daoClassRows.add("\t\t\t\t\tcon.rollback();");
			daoClassRows.add("\t\t\t\t} catch(Exception erb ) {");
			daoClassRows.add("\t\t\t\t\tlogger.warn(e.getMessage(), e);");
			daoClassRows.add("\t\t\t\t}");
			daoClassRows.add("\t\t\t}");
			
			daoClassRows.add("\t\t\tthrow e;");
			daoClassRows.add("\t\t} finally {");
			daoClassRows.add("\t\t\ttry {");
			daoClassRows.add("\t\t\t\tif (pstmt != null)");
			daoClassRows.add("\t\t\t\t\tpstmt.close();");
			daoClassRows.add("\t\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\t\tlogger.warn(e);");
			daoClassRows.add("\t\t\t}");
			
			daoClassRows.add("\t\t\ttry {");
			daoClassRows.add("\t\t\t\tif (con != null)");
			daoClassRows.add("\t\t\t\t\tcon.close();");
			daoClassRows.add("\t\t\t} catch (Exception exCon) {");
			daoClassRows.add("\t\t\t\tlogger.warn(exCon.getMessage());");
			daoClassRows.add("\t\t\t}");
			
			daoClassRows.add("\t\t}");
			daoClassRows.add("\t}");
			
			daoClassRows.add("");		
			
			
			daoClassRows.add("\tpublic static void deleteByPk( MasterConfig masterConfig, " + voName + " " + instanceName + " ) throws Exception {");
			daoClassRows.add("\t\tConnection con = null;");
			daoClassRows.add("\t\tPreparedStatement pstmt = null;");
			daoClassRows.add("\t\ttry {");
			daoClassRows.add("\t\t\tcon = PooledDataSource.getInstance(masterConfig).getConnection();");
			daoClassRows.add("\t\t\tcon.setAutoCommit(true);");
			daoClassRows.add("\t\t\tpstmt = con.prepareStatement(sqlDeleteByPk);");
			
			daoClassRows.add("\t\t\tint offset = 1;");
			for( ColumnItem columnItem : entry.getValue() ) {
				if( columnItem.isPk() ) {
					String javaName = columnNameToAttribute( columnItem.getName() );
					String getter = "get" + javaName.substring(0, 1).toUpperCase() + javaName.substring(1) + "()";
					daoClassRows.add("\t\t\tpstmt." + columnItem.getJdbcSet() + " offset++, " + instanceName + "." + getter + " );");
				}
			}
			daoClassRows.add("\t\t\tpstmt.execute();");
			daoClassRows.add("\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\tlogger.error(e.getMessage(), e);");
			
			daoClassRows.add("\t\t\tif( con != null ) {");
			daoClassRows.add("\t\t\t\ttry {");
			daoClassRows.add("\t\t\t\t\tcon.rollback();");
			daoClassRows.add("\t\t\t\t} catch(Exception erb ) {");
			daoClassRows.add("\t\t\t\t\tlogger.warn(e.getMessage(), e);");
			daoClassRows.add("\t\t\t\t}");
			daoClassRows.add("\t\t\t}");
			
			daoClassRows.add("\t\t\tthrow e;");
			daoClassRows.add("\t\t} finally {");
			daoClassRows.add("\t\t\ttry {");
			daoClassRows.add("\t\t\t\tif (pstmt != null)");
			daoClassRows.add("\t\t\t\t\tpstmt.close();");
			daoClassRows.add("\t\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\t\tlogger.warn(e);");
			daoClassRows.add("\t\t\t}");
			
			daoClassRows.add("\t\t\ttry {");
			daoClassRows.add("\t\t\t\tif (con != null)");
			daoClassRows.add("\t\t\t\t\tcon.close();");
			daoClassRows.add("\t\t\t} catch (Exception exCon) {");
			daoClassRows.add("\t\t\t\tlogger.warn(exCon.getMessage());");
			daoClassRows.add("\t\t\t}");
			daoClassRows.add("\t\t}");
			daoClassRows.add("\t}");			
			
			daoClassRows.add("");
			daoClassRows.add("\tpublic static void deleteBatchMode( Connection con, " + voName + " " + instanceName + " ) throws Exception {");
			daoClassRows.add("\t\tPreparedStatement pstmt = null;");
			daoClassRows.add("\t\ttry {");
			daoClassRows.add("\t\t\tpstmt = con.prepareStatement(sqlDeleteByPk);");
			
			daoClassRows.add("\t\t\tint offset = 1;");
			for( ColumnItem columnItem : entry.getValue() ) {
				if( columnItem.isPk() ) {
					String javaName = columnNameToAttribute( columnItem.getName() );
					String getter = "get" + javaName.substring(0, 1).toUpperCase() + javaName.substring(1) + "()";
					daoClassRows.add("\t\t\tpstmt." + columnItem.getJdbcSet() + " offset++, " + instanceName + "." + getter + " );");
				}
			}
			daoClassRows.add("\t\t\tpstmt.execute();");
			daoClassRows.add("\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\tlogger.error(e.getMessage(), e);");
			daoClassRows.add("\t\t\tthrow e;");
			daoClassRows.add("\t\t} finally {");
			daoClassRows.add("\t\t\ttry {");
			daoClassRows.add("\t\t\t\tif (pstmt != null)");
			daoClassRows.add("\t\t\t\t\tpstmt.close();");
			daoClassRows.add("\t\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\t\tlogger.warn(e);");
			daoClassRows.add("\t\t\t}");
			
			daoClassRows.add("\t\t}");
			daoClassRows.add("\t}");
			daoClassRows.add("");
			
			
			daoClassRows.add("\tpublic static " + voName + " findByPk( MasterConfig masterConfig, " + voName + " " + instanceName + " ) throws Exception {");
			daoClassRows.add("\t\tConnection con = null;");
			daoClassRows.add("\t\tPreparedStatement pstmt = null;");
			daoClassRows.add("\t\ttry {");
			daoClassRows.add("\t\t\tcon = PooledDataSource.getInstance(masterConfig).getConnection();");
			daoClassRows.add("\t\t\tcon.setAutoCommit(true);");
			daoClassRows.add("\t\t\tpstmt = con.prepareStatement(sqlFindByPk);");
			
			daoClassRows.add("\t\t\tint offset = 1;");
			for( ColumnItem columnItem : entry.getValue() ) {
				if( columnItem.isPk() ) {
					String javaName = columnNameToAttribute( columnItem.getName() );
					String getter = "get" + javaName.substring(0, 1).toUpperCase() + javaName.substring(1) + "()";
					daoClassRows.add("\t\t\tpstmt." + columnItem.getJdbcSet() + " offset++, " + instanceName + "." + getter + " );");
				}
			}
			daoClassRows.add("\t\t\tResultSet rs = pstmt.executeQuery();");
			daoClassRows.add("\t\t\tif( rs.next() ) return new " + voName + "(rs);");
			daoClassRows.add("\t\t\telse return null;");
			
			daoClassRows.add("\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\tlogger.error(e.getMessage(), e);");
			
			daoClassRows.add("\t\t\tthrow e;");
			daoClassRows.add("\t\t} finally {");
			daoClassRows.add("\t\t\ttry {");
			daoClassRows.add("\t\t\t\tif (pstmt != null)");
			daoClassRows.add("\t\t\t\t\tpstmt.close();");
			daoClassRows.add("\t\t\t} catch (Exception e) {");
			daoClassRows.add("\t\t\t\tlogger.warn(e);");
			daoClassRows.add("\t\t\t}");
			
			daoClassRows.add("\t\t}");
			daoClassRows.add("\t}");		
			
				
			daoClassRows.add("}" );
			File output = new File(this.targetFolder + className + ".java");
			output.delete();
			FileUtils.writeLines(output, daoClassRows);
			System.out.println("===================================================");
			for( String voClassRow : daoClassRows ) {
				System.out.println(voClassRow);
			}
		}
	}

	
	/**
	 * Table name to class.
	 *
	 * @param tableName the table name
	 * @return the string
	 */
	public static String tableNameToClass( String tableName ) {
		return commonTableAttr( tableName, 0 );
	}
	
	/**
	 * Column name to attribute.
	 *
	 * @param columnName the column name
	 * @return the string
	 */
	public static String columnNameToAttribute( String columnName ) {
		return commonTableAttr( columnName, 1 );
	}
	
	/**
	 * Common table attr.
	 *
	 * @param name the name
	 * @param startOffset the start offset
	 * @return the string
	 */
	public static String commonTableAttr( String name, int startOffset ) {
		name = name.toLowerCase();
		StringBuilder targetName = new StringBuilder();
		String to;
		boolean isNextUpper = true;
		if( startOffset == 1 ) {
			targetName.append(name.substring(0,1));
			isNextUpper = false;
		}
		for( int i=startOffset ; i<name.length() ; i++ ) {
			if( isNextUpper ) {
				targetName.append(name.substring(i,i+1).toUpperCase());
				isNextUpper = false;
			} else {
				if( name.substring(i,i+1).equals("_")) isNextUpper = true;
				else targetName.append(name.substring(i,i+1));
			}  
		}
		
		return targetName.toString();
	}
	
	
	/**
	 * Find jdbc types.
	 *
	 * @param sqlType the sql type
	 * @param columnSize the column size
	 * @param decimalDigits the decimal digits
	 * @return the jdbc type item
	 */
	public static JdbcTypeItem findJdbcTypes( int sqlType, long columnSize, int decimalDigits ) {
		if( Types.CHAR == sqlType || Types.VARCHAR == sqlType ) {
			return new JdbcTypeItem().setDataType("String").setGetter("getString(\"").setSetter("setString(");
		} else if( Types.VARBINARY == sqlType || Types.BINARY == sqlType ) {
			return new JdbcTypeItem().setDataType("byte[]").setGetter("getBytes(\"").setSetter("setBytes(");
		} else if( Types.TIMESTAMP == sqlType  ) {
			return new JdbcTypeItem().setDataType("Timestamp").setGetter("getTimestamp(\"").setSetter("setTimestamp(");
		} else if( Types.DATE == sqlType  ) {
			return new JdbcTypeItem().setDataType("Date").setGetter("getDate(\"").setSetter("setDate(");
		}  else if( Types.DECIMAL == sqlType || Types.INTEGER == sqlType ) {
			if( decimalDigits == 0 ) {
				return new JdbcTypeItem().setDataType("int").setGetter("getInt(\"").setSetter("setInt(");
			} else {
				return new JdbcTypeItem().setDataType("float").setGetter("getFloat(\"").setSetter("setFloat(");
			}
		} else {
			return new JdbcTypeItem().setDataType("UNKNOWN").setGetter("getUNKNOWN(\"").setSetter("setUNKNOWN(");
		}
	}
	
	
	/**
	 * The Class JdbcTypeItem.
	 */
	public static class JdbcTypeItem {
		
		/** The data type. */
		private String dataType;
		
		/** The getter. */
		private String getter;
		
		/** The setter. */
		private String setter;
		
		/**
		 * Gets the data type.
		 *
		 * @return the data type
		 */
		public String getDataType() {
			return dataType;
		}
		
		/**
		 * Gets the getter.
		 *
		 * @return the getter
		 */
		public String getGetter() {
			return getter;
		}
		
		/**
		 * Gets the setter.
		 *
		 * @return the setter
		 */
		public String getSetter() {
			return setter;
		}
		
		/**
		 * Sets the data type.
		 *
		 * @param dataType the data type
		 * @return the jdbc type item
		 */
		public JdbcTypeItem setDataType(String dataType) {
			this.dataType = dataType;
			return this;
		}
		
		/**
		 * Sets the getter.
		 *
		 * @param getter the getter
		 * @return the jdbc type item
		 */
		public JdbcTypeItem setGetter(String getter) {
			this.getter = getter;
			return this;
		}
		
		/**
		 * Sets the setter.
		 *
		 * @param setter the setter
		 * @return the jdbc type item
		 */
		public JdbcTypeItem setSetter(String setter) {
			this.setter = setter;
			return this;
		}
		
	}

	/**
	 * The Class ColumnItem.
	 */
	public static class ColumnItem {
		
		/** The name. */
		private String name;
		
		/** The type. */
		private int type;
		
		/** The size. */
		private long size;
		
		/** The decimal digits. */
		private int decimalDigits;
		
		/** The java type. */
		private String javaType;
		
		/** The jdbc get. */
		private String jdbcGet;
		
		/** The jdbc set. */
		private String jdbcSet;
		
		/** The pk. */
		private boolean pk;

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		public int getType() {
			return type;
		}

		/**
		 * Gets the size.
		 *
		 * @return the size
		 */
		public long getSize() {
			return size;
		}

		/**
		 * Gets the decimal digits.
		 *
		 * @return the decimal digits
		 */
		public int getDecimalDigits() {
			return decimalDigits;
		}

		/**
		 * Sets the name.
		 *
		 * @param name the name
		 * @return the column item
		 */
		public ColumnItem setName(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Sets the type.
		 *
		 * @param type the type
		 * @return the column item
		 */
		public ColumnItem setType(int type) {
			this.type = type;
			return this;
		}

		/**
		 * Sets the size.
		 *
		 * @param size the size
		 * @return the column item
		 */
		public ColumnItem setSize(long size) {
			this.size = size;
			return this;
		}

		/**
		 * Sets the decimal digits.
		 *
		 * @param decimalDigits the decimal digits
		 * @return the column item
		 */
		public ColumnItem setDecimalDigits(int decimalDigits) {
			this.decimalDigits = decimalDigits;
			return this;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ColumnItem [name=" + name + ", type=" + type + ", size=" + size + ", decimalDigits=" + decimalDigits
					+ ", javaType=" + javaType + ", jdbcGet=" + jdbcGet + ", jdbcSet=" + jdbcSet + "]";
		}

		/**
		 * Gets the java type.
		 *
		 * @return the java type
		 */
		public String getJavaType() {
			return javaType;
		}

		/**
		 * Gets the jdbc get.
		 *
		 * @return the jdbc get
		 */
		public String getJdbcGet() {
			return jdbcGet;
		}

		/**
		 * Sets the java type.
		 *
		 * @param javaType the java type
		 * @return the column item
		 */
		public ColumnItem setJavaType(String javaType) {
			this.javaType = javaType;
			return this;
		}

		/**
		 * Sets the jdbc get.
		 *
		 * @param jdbcGet the jdbc get
		 * @return the column item
		 */
		public ColumnItem setJdbcGet(String jdbcGet) {
			this.jdbcGet = jdbcGet;
			return this;
		}

		/**
		 * Gets the jdbc set.
		 *
		 * @return the jdbc set
		 */
		public String getJdbcSet() {
			return jdbcSet;
		}

		/**
		 * Sets the jdbc set.
		 *
		 * @param jdbcSet the jdbc set
		 * @return the column item
		 */
		public ColumnItem setJdbcSet(String jdbcSet) {
			this.jdbcSet = jdbcSet;
			return this;
		}

		/**
		 * Checks if is pk.
		 *
		 * @return true, if is pk
		 */
		public boolean isPk() {
			return pk;
		}

		/**
		 * Sets the pk.
		 *
		 * @param pk the pk
		 * @return the column item
		 */
		public ColumnItem setPk(boolean pk) {
			this.pk = pk;
			return this;
		}

	}


	/**
	 * Gets the table names.
	 *
	 * @return the table names
	 */
	public List<String> getTableNames() {
		return tableNames;
	}

	/**
	 * Gets the target folder.
	 *
	 * @return the target folder
	 */
	public String getTargetFolder() {
		return targetFolder;
	}

	/**
	 * Gets the package name.
	 *
	 * @return the package name
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Sets the table names.
	 *
	 * @param tableNames the table names
	 * @return the creates the basic dao vo
	 */
	public CreateBasicDaoVo setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
		return this;
	}

	/**
	 * Sets the target folder.
	 *
	 * @param targetFolder the target folder
	 * @return the creates the basic dao vo
	 */
	public CreateBasicDaoVo setTargetFolder(String targetFolder) {
		if( !targetFolder.endsWith("/")) targetFolder = targetFolder + "/";
		this.targetFolder = targetFolder;
		return this;
	}

	/**
	 * Sets the package name.
	 *
	 * @param packageName the package name
	 * @return the creates the basic dao vo
	 */
	public CreateBasicDaoVo setPackageName(String packageName) {
		this.packageName = packageName;
		return this;
	}


	/**
	 * Gets the ds.
	 *
	 * @return the ds
	 */
	public BasicDataSource getDs() {
		return ds;
	}


	/**
	 * Sets the ds.
	 *
	 * @param ds the ds
	 * @return the creates the basic dao vo
	 */
	public CreateBasicDaoVo setDs(BasicDataSource ds) {
		this.ds = ds;
		return this;
	}

	/**
	 * Gets the jdbc driver class name.
	 *
	 * @return the jdbc driver class name
	 */
	public String getJdbcDriverClassName() {
		return jdbcDriverClassName;
	}

	/**
	 * Sets the jdbc driver class name.
	 *
	 * @param jdbcDriverClassName the jdbc driver class name
	 * @return the creates the basic dao vo
	 */
	public CreateBasicDaoVo setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
		return this;
	}

}
