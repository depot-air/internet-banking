package com.dwidasa.admin.pages.token;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.IbToken;

@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, Constants.RoleName.DAY_ADMIN, Constants.RoleName.SUPERUSER})
public class TokenDataImport {

    @Property
    @Persist
    private String groupIdValue;
    
    @Property
    private String noErrorFlag;
    
    @Property
    private String imported;
    
    @InjectComponent
    private Form form;
    
    @Inject
    private Messages messages;
    
    @Property
    private UploadedFile uploadedFile;
    
	private static final String GROUPS_BLACK = "BLACK";
	
	public Connection getPostgreConnection() throws ClassNotFoundException, SQLException {		
		String driver = "org.postgresql.Driver";
		//ke development
//		String url = "jdbc:postgresql://192.168.77.30:5432/ib2_migration";
//		String username = "postgres";
//		String password = "postgres";
		//192.168.100.47:5432/ib2
		//ke production
		String url = "jdbc:postgresql://192.168.100.47:5432/ib2";
		String username = "ib2";
		String password = "ib2";
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		}
		catch (BusinessException be){
            be.printStackTrace();
            System.out.println("BE: "+be);
//            form.clearErrors();
//            form.recordError(((BusinessException) be).getFullMessage());
//            noErrorFlag = null;
            
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("E: "+e);
//            form.clearErrors();
//            form.recordError(e.getMessage());
//            noErrorFlag = null;
        }
//		catch (ClassNotFoundException e) {
//			System.out.println("Oops! Can't find class oracle.jdbc.driver.OracleDriver");
//			e.printStackTrace();
//		} catch (Exception e) {
//			System.out.println("error : " + e);
//			e.printStackTrace();
//		} 
		return conn;
	}
	public Connection getSqlServerConnection() throws ClassNotFoundException, SQLException{
    	String username = "velis";
		String password = "velis";
		String url = "jdbc:sqlserver://172.16.100.145;database=va_5";
//		192.168.100.47:5432/ib2
    	Connection conn = null;
    	try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(url, username, password);
		}
    	catch (BusinessException be){
            be.printStackTrace();
            System.out.println("BE: "+be);
//            form.clearErrors();
//            form.recordError(((BusinessException) be).getFullMessage());
//            noErrorFlag = null;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("E: "+e);
//            form.clearErrors();
//            form.recordError(e.getMessage());
//            noErrorFlag = null;
        }
//    	catch(Exception e){
//			e.printStackTrace();
//			System.out.println("error : " + e);
//		}    
    	return conn;
    }
	
	private void importToken(InputStream namaFile) {
		
//		Connection connVelis = null;
//		Connection connPostgre = null;
//		
//		Statement stmtVelis = null;
//		Statement stmtPostgre = null;
//		
//		Statement stmtVelis2 = null;
//		Statement stmtPostgre2 = null;
//		
//		ResultSet rs = null;
//		ResultSet rsInner = null;
//		ResultSet rsInner2 = null;
//		ResultSet rsInner3 = null;
//		
//		
//		try {
//			connVelis = getSqlServerConnection();
//			connPostgre = getPostgreConnection();
//			
//			stmtVelis = connVelis.createStatement();
//			stmtPostgre = connPostgre.createStatement();
//
//			stmtVelis2 = connVelis.createStatement();
//			stmtPostgre2 = connPostgre.createStatement();
//		
////			System.out.println("select dari vabctk");
//			String qSelectVacbtck = "select * from vabctk";
//			System.out.println("qSelect1= " + qSelectVacbtck);
//			rs = stmtVelis.executeQuery(qSelectVacbtck);	
//			int jumlah = 0;
//			int i = 0;
//            while (rs.next()) {
//            	i += 1;
////            	System.out.println("select dari vabctk");
////            	System.out.println("-----------------------" + i + "------------------");
//            	System.out.println("VACBTK ke: "+ i + ", vbctfl =" + rs.getString("vbctfl") + ", vbctst =" + rs.getString("vbctst") 
//            			+ ", vbctgr =" + rs.getString("vbctgr") + ", vbctcl =" + rs.getString("vbctcl") 
//            			+ ", vbcitr =" + rs.getString("vbcitr") + ", vbcitt =" + rs.getString("vbcitt") 
//            			+ ", vbctid =" + rs.getString("vbctid") + ", vbctiu =" + rs.getString("vbctiu") 
//            			+ ", vbctfd =" + rs.getString("vbctfd") + ", vbctfu =" + rs.getString("vbctfu"));
////            	System.out.println("select dari m_vabctk untuk vbctfl yg sama");
//            	String qSelectM_Vacbtk = "select * from m_vabctk where vbctfl = '"+ rs.getString("vbctfl") + "'";
//            	System.out.println("qSelect2= " + qSelectM_Vacbtk);
//            	rsInner = stmtPostgre.executeQuery(qSelectM_Vacbtk);
//            	
//            	//jika blm ada di tabel m_vabctk
//            	if(!rsInner.next()){
////            		System.out.println("belum ada di m_vabctk, masukkan vbctfl ke table m_vabctk");
//            		String qInsertM_Vacbtk = "INSERT INTO m_vabctk (vbctfl, vbctst, vbctgr, vbctcl, vbcitr, vbcitt, vbctid, " +
//                			"vbctiu, vbctfd, vbctfu)  " +
//                			"VALUES('" + rs.getString("vbctfl") + "', '" + rs.getString("vbctst") + "', '" + rs.getString("vbctgr") + "', '" + rs.getString("vbctcl") + "'" +
//                					", '" + rs.getString("vbcitr") + "', '" + rs.getString("vbcitt") + "'" +
//                					", '" + rs.getString("vbctid") + "', '" + rs.getString("vbctiu") + "'" +
//                					", '" + rs.getString("vbctfd") + "', '" + rs.getString("vbctfu") + "') ";
//                	System.out.println("qInsert1= " + qInsertM_Vacbtk);
//                	
//                	stmtPostgre.executeUpdate(qInsertM_Vacbtk);
//                	
////                	System.out.println("select dari vavstm untuk vbctfl tersebut");
//                	String qSelectVavstm = "select * from vavstm where vavtif = '"+ rs.getString("vbctfl") + "'";
//        			System.out.println("qSelect3= " + qSelectVavstm);
//        			rsInner2 = stmtVelis2.executeQuery(qSelectVavstm);	
//        			
//        			int j = 0;
//                    while (rsInner2.next()) {
//                    	j += 1;
////                    	System.out.println("-----------------------" + j + "------------------" );
//                    	System.out.println("VAVSTM ke: "+ j + ", vavtsn =" + rsInner2.getString("VAVTSN") + ", vavtst =" + rsInner2.getString("VAVTST") 
//                    			+ ", vavtsd =" + rsInner2.getString("VAVTSD") + ", vavtcd =" + rsInner2.getString("VAVTCD") 
//                    			+ ", vavtpr =" + rsInner2.getString("VAVTPR") + ", vavtpd =" + rsInner2.getString("VAVTPD") 
//                    			+ ", vavtps =" + rsInner2.getString("VAVTPS") + ", vavtrd =" + rsInner2.getString("VAVTRD") 
//                    			+ ", vavtif =" + rsInner2.getString("VAVTIF") + ", vavtpf =" + rsInner2.getString("VAVTPF") 
//                    			+ ", vavtvt =" + rsInner2.getString("VAVTVT") + ", vavtan =" + rsInner2.getString("VAVTAN")
//                    			+ ", vavtm1 =" + rsInner2.getString("VAVTM1") + ", vavtm2 =" + rsInner2.getString("VAVTM2")
//                    			+ ", vavtm3 =" + rsInner2.getString("VAVTM3") + ", vavtm4 =" + rsInner2.getString("VAVTM4")
//                    			+ ", vavtm5 =" + rsInner2.getString("VAVTM5") + ", vavtm6 =" + rsInner2.getString("VAVTM6")
//                    			+ ", vavtm7 =" + rsInner2.getString("VAVTM7") + ", vavtm8 =" + rsInner2.getString("VAVTM8")
//                    			+ ", vavtup =" + rsInner2.getString("VAVTUP") + ", vavtuu =" + rsInner2.getString("VAVTUU"));
////                    	System.out.println("Select di m_ib_token untuk vavtsn = serial_number");
//                    	String qSelectMIB_Token = "select * from m_ib_token where serial_number = '"+ rsInner2.getString("VAVTSN") + "'";
//                    	System.out.println("qSelect4= " + qSelectMIB_Token);
//            			rsInner3 = stmtPostgre2.executeQuery(qSelectMIB_Token);	
//                    	
//                    	//jika blm ada di tabel m_ib_token
//                    	if(!rsInner3.next()){
////                    		System.out.println("insert ke m_ib_token dengan status : " + rsInner2.getString("VAVTCD"));
//                    		String status = null;
//                    		if(rsInner2.getInt("VAVTCD")  == 1){
////                    			System.out.println("imported");
//                    			status = com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_IMPORTED;
//                    		}else if(rsInner2.getInt("VAVTCD")  == 2){
//                    			status = com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_ACTIVE;
////                    			System.out.println("act");
//                    		}else if(rsInner2.getInt("VAVTCD")  == 3){
//                    			status = com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_LOST;
////                    			System.out.println("lost");
//                    		}else if(rsInner2.getInt("VAVTCD")  == 9){
//                    			status = com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_TERMINATED;
////                    			System.out.println("term");
//                    		}
//                    		String qInsertMIB_Token = "INSERT INTO m_ib_token (serial_number,status) VALUES('"+ rsInner2.getString("VAVTSN") + "','" + status +"') ";
//                    		System.out.println("qInsert2= " + qInsertMIB_Token);
//                    		stmtPostgre.executeUpdate(qInsertMIB_Token);
//                    		jumlah ++;
//                    	}
////                    	System.out.println("rsInner 3 nulls");
//                    }
////                    System.out.println("rsInner2 null");
//            	}
////            	System.out.println("rsInner null");
//            }
////            System.out.println("rs null");
//            noErrorFlag = "Y";
//            imported = "Jumlah data berhasil diimport: "+jumlah;
//            System.out.println(imported);
//		}
//		catch (BusinessException be){
//            be.printStackTrace();
//            System.out.println("BE2: "+be);
//            form.clearErrors();
//            form.recordError(((BusinessException) be).getFullMessage());
//            noErrorFlag = null;
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            System.out.println("E2: "+e);
//            form.clearErrors();
//            form.recordError(e.getMessage());
//            noErrorFlag = null;
//        }
		
		try {
			readExcel(namaFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
	void setupRender() {
    	if(groupIdValue == null){
    		groupIdValue = "";
		}    
    }

    public Object onSuccess() {
        return null;
    }
    
    
    void onValidateFromForm() {
    	
    	try {
    		
			if (!uploadedFile.getFileName().toLowerCase().endsWith(".xls")) {
				
				String errorMsg = "Invalid File Type  " + uploadedFile.getContentType();
				
				form.recordError(errorMsg);
				
			} else {
				
				InputStream fileInput = uploadedFile.getStream();
				System.out.println("File "+fileInput.read());
				
				System.out.println("start importing.......................");
				imported = "0";
				importToken(uploadedFile.getStream());
			}
			
		} catch (Exception e) {
		   		form.clearErrors();
                form.recordError(messages.get("invalid-input"));
		}
//    	if(groupIdValue != null){
//    		System.out.println("start importing.......................");
//    		imported = "0";
//			importToken();		
//    	}else{
//    		form.clearErrors();
//            form.recordError(messages.get("invalid-input"));
////            form.
//    	}
    }
    
    
	public void readExcel(InputStream s) throws Exception {

		Connection connPostgre = null;
		Statement stmtPostgre = null;
		ResultSet rs = null;
		
		try {
			
			connPostgre = getPostgreConnection();
			stmtPostgre = connPostgre.createStatement();

		
			
			HSSFWorkbook wb = new HSSFWorkbook(s);

			HSSFSheet sheet = wb.getSheetAt((int) 0);
			
			List<IbToken> ibTokens = new ArrayList<IbToken>();

			if (sheet != null) {

				Iterator<?> rowIterator = sheet.rowIterator();
				
				

				while (rowIterator.hasNext()) {
					
					HSSFRow row = (HSSFRow) rowIterator.next();

					HSSFCell cellStudentID = row.getCell(0);

					HSSFCell cellStudentName = row.getCell(1);
					
					IbToken iToken = new IbToken();

					try {
						
						String No = "";
						String serianNumber = "";
						
						if(cellStudentID.getCellType() == cellStudentID.CELL_TYPE_NUMERIC){
							No = String.valueOf(Math.round(cellStudentID.getNumericCellValue()));
						}else{
							No = cellStudentID.getStringCellValue().toString().replace("No", "0");
						}
						
						if(cellStudentName.getCellType() == cellStudentName.CELL_TYPE_NUMERIC){
							serianNumber = String.valueOf(Math.round(cellStudentName.getNumericCellValue()));
						}else{
							serianNumber = cellStudentName.getStringCellValue().toString();
						}
						
						Long nomor = Long.parseLong(No);
						iToken.setId(nomor);
						iToken.setSerialNumber(serianNumber);
						System.out.println(No + "        " + serianNumber);
						ibTokens.add(iToken);

					} catch (NullPointerException e) {

						continue;

					}

				}

			} else {

				System.out.println("Sheet not found");

			}

			for(int i=1; i<ibTokens.size(); i++){
				
        		String qInsertMIB_Token = "INSERT INTO m_ib_token (serial_number,status) VALUES('"+ ibTokens.get(i).getSerialNumber() + "','" + com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_IMPORTED +"') ";
        		System.out.println("qInsert2= " + qInsertMIB_Token);
        		stmtPostgre.executeUpdate(qInsertMIB_Token);
			}
			
			
		} catch (FileNotFoundException fne) {

			System.out.println("File not found");

		}

	}
	
}



