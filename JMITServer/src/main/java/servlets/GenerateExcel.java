package servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import utils.Constants;
import database.CacheConnection;
import database.DataManager;
import pojo.Logininfo;
import pojo.Registration;

/**
 * Servlet implementation class create_user
 */
@WebServlet("/GenerateExcel")
public class GenerateExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenerateExcel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		StringBuilder sb = new StringBuilder();
		String event_id = null;
		Map m = request.getParameterMap();
		Set s = m.entrySet();
		Iterator it = s.iterator();
		System.out.println("request excel");
		while (it.hasNext()) {

			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();

			String key = entry.getKey();
			String[] value = entry.getValue();
			switch (key) {
			case Constants.event_id:
				event_id = value[0].toString();
				break;

			}
		}
		CacheConnection.setVerbose(true);

		// Get a cached connection
		java.sql.Connection connection = CacheConnection.checkOut();
		try {
			connection.setReadOnly(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean b = false;
		try {
			ArrayList<Registration> list=DataManager.getregistration(connection, event_id);
			export(list,response);
		} catch (Exception e) {
			e.printStackTrace();	
		}

		CacheConnection.checkIn(connection);

	}
	void export(ArrayList<Registration> arrayList,HttpServletResponse response) {
		          HSSFWorkbook workbook = new HSSFWorkbook();
		  
		          HSSFSheet firstSheet = workbook.createSheet("Entries");
		          HSSFFont font = workbook.createFont();
		          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		          HSSFRow rowA = firstSheet.createRow(0);
		          HSSFCell cellA = rowA.createCell(0);
		          HSSFRichTextString h1=new HSSFRichTextString("Roll no");
		          h1.applyFont(font);
		          cellA.setCellValue(h1);
		          HSSFCell cell = rowA.createCell(1);
		          HSSFRichTextString h2=new HSSFRichTextString("Name");
		          h2.applyFont(font);
		          cell.setCellValue(h2);
		          HSSFCell cell1 = rowA.createCell(2);
		          HSSFRichTextString h3=new HSSFRichTextString("Phone no");
		          h3.applyFont(font);
		          cell1.setCellValue(h3);
		          HSSFCell cell2 = rowA.createCell(3);
		          HSSFRichTextString h4=new HSSFRichTextString("Email");
		          h4.applyFont(font);
		          cell2.setCellValue(h4);
		          HSSFCell cell3 = rowA.createCell(4);
		          HSSFRichTextString h5=new HSSFRichTextString("Dept");
		          h5.applyFont(font);
		          cell3.setCellValue(h5);
		          int i = 1;
		          for (Registration registration : arrayList) {
		              HSSFRow row = firstSheet.createRow(i);
		              row.createCell(0).setCellValue(registration.getRollNo());
		              row.createCell(1).setCellValue(registration.getUserName());
		              row.createCell(2).setCellValue(registration.getPhNo());
		              row.createCell(3).setCellValue(registration.getEmail());
		              row.createCell(4).setCellValue(registration.getDepartment());
		              i++ ;
		          }
		          OutputStream outstream = null;
		          try {
		            response.setContentType("application/vnd.ms-excel");
		            response.addHeader("content-disposition", "attachment; filename=data.xls");
		            outstream = response.getOutputStream();
		            workbook.write(outstream);
		           }
		          catch (Exception e) {
		             e.printStackTrace();
		          }finally {
		        	  try {
						outstream.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		          }
	}
}
