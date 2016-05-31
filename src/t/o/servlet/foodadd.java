package t.o.servlet;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.jms.Session;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

import t.o.bean.DBfood;
import t.o.bean.foodBean;

public class foodadd extends HttpServlet {
	private String uploadPath = "..\\..\\images"; // �ϴ��ļ���Ŀ¼
	private String tempPath = "d:\\12"; // ��ʱ�ļ�Ŀ¼

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		if(session.getAttribute("adminName")==null){
			response.getWriter().write("<script language='JavaScript'>" +
					"window.open('admin/login.jsp',target='_top');" + 
					"</script>"
					);
			}
		
		String path = request.getContextPath();
		String filePath=this.getServletConfig().getServletContext().getRealPath("/");
		String food_no = null;
		String food_name = null;
		String food_type = null;
		String food_price = null;
		String food_cook = null;
		String food_synopsis = null;
		String food_remark = null;
		try {
			DiskFileUpload fu = new DiskFileUpload();
			// ��������ļ��ߴ磬������4MB
			fu.setSizeMax(4194304);
			// ���û�������С��������4kb
			fu.setSizeThreshold(4096);
			// ������ʱĿ¼��
			fu.setRepositoryPath(filePath+"/images/tmp");

			// �õ����е��ļ���
			List fileItems = fu.parseRequest(request);//�����û�����Ĳ���,ȡ���ļ��ϴ���Ϣ
			Iterator i = fileItems.iterator();
			// ���δ���ÿһ���ļ���
			while(i.hasNext()) {
				FileItem item = (FileItem)i.next();
				//�����һ����ͨ�ı�����File���
				if( !item.isFormField() ){
					//item.getName ���ص����������ļ�������:E:\\xx\11.doc
					//����������һ��fullFile��ȡ�ļ���
					File fullFile = new File(item.getName());
					File uploadedFile = new File(filePath+"/images/foodimg/"+food_no+".jpg");
					item.write(uploadedFile);
//					System.out.println(filePath);
				}
				if("food_no".equals(item.getFieldName())){
					food_no = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
					out.println(food_no+"<br>");
				}
				if("food_name".equals(item.getFieldName())){
					food_name = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
					out.println(food_name+"<br>");
				}
				if("food_type".equals(item.getFieldName())){
					food_type = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
					out.println(food_type+"<br>");
				}
				if("food_price".equals(item.getFieldName())){
					food_price = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
					out.println(food_price+"<br>");
				}
				if("food_cook".equals(item.getFieldName())){
					food_cook = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
					out.println(food_cook+"<br>");
				}		
				if("food_synopsis".equals(item.getFieldName())){
					food_synopsis = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
					out.println(food_synopsis+"<br>");
				}
				if("food_remark".equals(item.getFieldName())){
					food_remark = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
					out.println(food_remark+"<br>");
				}
			}
		}
		catch(Exception e) {
			// ������ת����ҳ��
			out.print(e.toString());
		}
		String sql = "insert into t_o_Food values(NULL,'"+food_no+"','"+food_name+"','"+food_type+"','"+food_price+"','"+food_cook+"','"+food_synopsis+"','"+food_remark+"')";
		int result = DBfood.uploadFood(sql);
		if(result>0){
			foodBean foodbean = new foodBean(food_no,food_name,food_type,food_price,food_cook,food_synopsis,food_remark);
			request.setAttribute("foodaddshow", foodbean);
			RequestDispatcher dispatcher = request.getRequestDispatcher("admin/foodaddshow.jsp");
			dispatcher.forward(request, response);
		}
		else{
			out.print("�ύʧ�ܣ�");
		}
		out.println("<a href='a_food_food_add.jsp'>����</a>");
	}
}
