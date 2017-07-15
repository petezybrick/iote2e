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

public class CreateBasicDaoVo {
	private static final Logger logger = LogManager.getLogger(CassandraBaseDao.class);	
	private BasicDataSource ds;
	private List<String> tableNames;
	private String targetFolder;
	private String packageName;
	private String jdbcDriverClassName; // "oracle.jdbc.driver.OracleDriver" "com.mysql.jdbc.Driver";
	
//	"jdbcDriverClassName": "com.mysql.jdbc.Driver",
//	"jdbcInsertBlockSize": "1000",
//	"jdbcLogin": "",
//	"jdbcPassword": "Password*8",
//	"jdbcUrl": "jdbc:mysql://iote2e-mysql-master:3306/db_iote2e_batch",
	// "blood_glucose|blood_pressure" "/tmp" "com.pzybrick.iote2e.stream.persist" "iote2e_batch" "Password*8" "jdbc:mysql://localhost:3307/db_iote2e_batch"  "com.mysql.jdbc.Driver"
	
	
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

	
	public static String tableNameToClass( String tableName ) {
		return commonTableAttr( tableName, 0 );
	}
	
	public static String columnNameToAttribute( String columnName ) {
		return commonTableAttr( columnName, 1 );
	}
	
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
	
	
	public static class JdbcTypeItem {
		private String dataType;
		private String getter;
		private String setter;
		
		public String getDataType() {
			return dataType;
		}
		public String getGetter() {
			return getter;
		}
		public String getSetter() {
			return setter;
		}
		public JdbcTypeItem setDataType(String dataType) {
			this.dataType = dataType;
			return this;
		}
		public JdbcTypeItem setGetter(String getter) {
			this.getter = getter;
			return this;
		}
		public JdbcTypeItem setSetter(String setter) {
			this.setter = setter;
			return this;
		}
		
	}

	public static class ColumnItem {
		private String name;
		private int type;
		private long size;
		private int decimalDigits;
		private String javaType;
		private String jdbcGet;
		private String jdbcSet;
		private boolean pk;

		public String getName() {
			return name;
		}

		public int getType() {
			return type;
		}

		public long getSize() {
			return size;
		}

		public int getDecimalDigits() {
			return decimalDigits;
		}

		public ColumnItem setName(String name) {
			this.name = name;
			return this;
		}

		public ColumnItem setType(int type) {
			this.type = type;
			return this;
		}

		public ColumnItem setSize(long size) {
			this.size = size;
			return this;
		}

		public ColumnItem setDecimalDigits(int decimalDigits) {
			this.decimalDigits = decimalDigits;
			return this;
		}

		@Override
		public String toString() {
			return "ColumnItem [name=" + name + ", type=" + type + ", size=" + size + ", decimalDigits=" + decimalDigits
					+ ", javaType=" + javaType + ", jdbcGet=" + jdbcGet + ", jdbcSet=" + jdbcSet + "]";
		}

		public String getJavaType() {
			return javaType;
		}

		public String getJdbcGet() {
			return jdbcGet;
		}

		public ColumnItem setJavaType(String javaType) {
			this.javaType = javaType;
			return this;
		}

		public ColumnItem setJdbcGet(String jdbcGet) {
			this.jdbcGet = jdbcGet;
			return this;
		}

		public String getJdbcSet() {
			return jdbcSet;
		}

		public ColumnItem setJdbcSet(String jdbcSet) {
			this.jdbcSet = jdbcSet;
			return this;
		}

		public boolean isPk() {
			return pk;
		}

		public ColumnItem setPk(boolean pk) {
			this.pk = pk;
			return this;
		}

	}


	public List<String> getTableNames() {
		return tableNames;
	}

	public String getTargetFolder() {
		return targetFolder;
	}

	public String getPackageName() {
		return packageName;
	}

	public CreateBasicDaoVo setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
		return this;
	}

	public CreateBasicDaoVo setTargetFolder(String targetFolder) {
		if( !targetFolder.endsWith("/")) targetFolder = targetFolder + "/";
		this.targetFolder = targetFolder;
		return this;
	}

	public CreateBasicDaoVo setPackageName(String packageName) {
		this.packageName = packageName;
		return this;
	}


	public BasicDataSource getDs() {
		return ds;
	}


	public CreateBasicDaoVo setDs(BasicDataSource ds) {
		this.ds = ds;
		return this;
	}

	public String getJdbcDriverClassName() {
		return jdbcDriverClassName;
	}

	public CreateBasicDaoVo setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
		return this;
	}

}
