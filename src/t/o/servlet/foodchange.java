package t.o.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

public class foodchange extends HttpServlet {
	
	private int allrow = 0;//����
	private int yerow = 10;//ÿҳ����
	private int yeshu = 0;//ҳ��
	private int lastyerow = 0;//���һҳ������
	private int showpage = 1;//��ǰҳ
	private String sql = null;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		if(session.getAttribute("adminName")==null){
			response.getWriter().write("<script language='JavaScript'>" +
					"window.open('admin/login.jsp',target='_top');" + 
					"</script>"
					);
			}
		if(request.getParameter("ToPage")==null){	
			sql = "select * from t_o_Food";
			showpage = 1;
		}
		if(request.getParameter("foodsearch")!=null){
			String food_type = request.getParameter("foodtype");
			String food= new String(request.getParameter("foodsearch").toString().getBytes("iso-8859-1"),"utf-8");
			if(food_type.equals("food_name")){
				sql = "select * from t_o_Food where o_foodName like '%"+food+"%'";
				showpage = 1;
			}
			if(food_type.equals("food_no")){
				sql = "select * from t_o_Food where o_foodNo like '%"+food+"%'";
				showpage = 1;
			}
		}
		if(request.getParameter("food_type")!=null){
			String food_type = request.getParameter("food_type");
			if(food_type.equals("all")){
				sql = "select * from t_o_Food";
				showpage = 1;
			}
			if(food_type.equals("my")){
				sql = "select * from t_o_Food where o_foodType = '���Ʋ�'";
				showpage = 1;
			}
			if(food_type.equals("china")){
				sql = "select * from t_o_Food where o_foodType = '�в�'";
				showpage = 1;
			}
			if(food_type.equals("abroad")){
				sql = "select * from t_o_Food where o_foodType = '����'";
				showpage = 1;
			}
			if(food_type.equals("nosh")){
				sql = "select * from t_o_Food where o_foodType = 'С��'";
				showpage = 1;
			}
			if(food_type.equals("cake")){
				sql = "select * from t_o_Food where o_foodType = '���'";
				showpage = 1;
			}
			if(food_type.equals("drink")){
				sql = "select * from t_o_Food where o_foodType = '��Ʒ'";
				showpage = 1;
			}
		}
		ArrayList<foodBean> foodList = new ArrayList<foodBean>();
		foodList = DBfood.getfm(sql);
		session.setAttribute("foodchange", foodList);
		//��ʾ��ҳ��
		allrow = foodList.size();
		lastyerow = allrow % yerow;
		if(lastyerow == 0){
			yeshu = allrow/yerow;
		}
		else{
			yeshu = (allrow-lastyerow)/yerow;
			yeshu ++;
		}

		if(request.getParameter("ToPage")==null){
			ArrayList<foodBean> list = new ArrayList<foodBean>();
			if(allrow<yerow){
				for(int i=0;i<allrow;i++){
					list.add(foodList.get(i));
				}
			}
			else{
				for(int i=0;i<yerow;i++){
					list.add(foodList.get(i));
				}
			}
			session.setAttribute("foodList", list);
		}
		if(request.getParameter("ToPage")!=null){
			if(request.getParameter("ToPage").equals("")){
				ArrayList<foodBean> list = new ArrayList<foodBean>();
				if(allrow<yerow){
					for(int i=0;i<allrow;i++){
						list.add(foodList.get(i));
					}
				}
				else{
					for(int i=0;i<yerow;i++){
						list.add(foodList.get(i));
					}
				}
				session.setAttribute("foodList", list);
			}
			else{
				if(Integer.parseInt(request.getParameter("ToPage"))*yerow<=allrow){
					String pagea = request.getParameter("ToPage");
					int page = Integer.parseInt(pagea);
					ArrayList<foodBean> list = new ArrayList<foodBean>();
					for(int i=(page-1)*yerow;i<page*yerow;i++){
						list.add(foodList.get(i));
					}
					session.setAttribute("foodList", list);
					showpage = page;
				}
				if(Integer.parseInt(request.getParameter("ToPage"))*yerow>=allrow){
					String pagea = request.getParameter("ToPage");
					int page = Integer.parseInt(pagea);
					ArrayList<foodBean> list = new ArrayList<foodBean>();
					for(int i=(page-1)*yerow;i<allrow;i++){
						list.add(foodList.get(i));
					}
					session.setAttribute("foodList", list);
					showpage = page;
				}
			}
		}
		
		String str = "";  
		int next, prev;  
		prev=showpage-1;  
		next=showpage+1; 
		str+="<form aciont='foodchange'>";
		str += "��ѯ��<span>"+allrow+"</span>����¼"+
		"    ��<span>"+yeshu+"</span>ҳ";  
		str +=" ��<span>"+showpage+"</span>ҳ ";  
		if(showpage>1) 
			str += " <A href=foodchange?ToPage=1"+">��ҳ</A> ";  
		else 
			str += " ��ҳ ";  
		if(showpage>1)
			str += " <A href=foodchange?ToPage=" + prev + ">��һҳ</A> ";  
		else
			str += " ��һҳ ";  
		if(showpage<yeshu)  
			str += " <A href=foodchange?ToPage=" + next + ">��һҳ</A> ";  
		else 
			str += " ��һҳ ";  
		if(yeshu>1&&showpage!=yeshu)  
			str += " <A href=foodchange?ToPage=" + yeshu + 
			">βҳ</A>";  
		else
			str += " βҳ ";  
		str+="��<input name=ToPage type=text size=2>ҳ <input type=submit value=GO></form>" ;
		
		session.setAttribute("str", str);
		if(request.getParameter("foodno")!=null){
			request.setAttribute("foodno",request.getParameter("foodno"));
			RequestDispatcher dispatcher = request.getRequestDispatcher("admin/foodchange.jsp");
			dispatcher.forward(request, response);
		}
		else if(request.getParameter("delete")!=null){
			String food_no = request.getParameter("delete");
			String sql = "delete from t_o_Food where o_foodNo = '" + food_no + "'";
			int result = DBfood.uploadFood(sql);
			if(result > 0){
				
				String path = this.getServletConfig().getServletContext().getRealPath("/");    
				String filePath = path+"images/foodimg/"+food_no+".jpg";
				filePath = filePath.toString();
				java.io.File myFilePath = new java.io.File(filePath);
			    myFilePath.delete(); 
				    
				response.getWriter().write("<script language='JavaScript'>" +
						"window.location.href='foodchange';" + 
						"</script>"
						);
			}else{
				response.getWriter().write("<script language='JavaScript'>" +
						"alert('�h����Ʒ���ɹ���');" + 
						"window.location.href='foodchange';" + 
						"</script>"
						);
			}
		}
		else{
			RequestDispatcher dispatcher = request.getRequestDispatcher("admin/foodsearch.jsp");
			dispatcher.forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String path = request.getContextPath();
		String filePath=this.getServletConfig().getServletContext().getRealPath("/");
		HttpSession session = request.getSession();
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

					if(item.getName()!=""){
						File fullFile = new File(item.getName());
						File uploadedFile = new File(filePath+"/images/foodimg",food_no+".jpg");
						item.write(uploadedFile);
					}
				}
				if("food_no".equals(item.getFieldName())){
					food_no = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
//					out.println(food_no+"<br>");
				}
				if("food_name".equals(item.getFieldName())){
					food_name = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
//					out.println(food_name+"<br>");
				}
				if("food_type".equals(item.getFieldName())){
					food_type = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
					if(food_type.equals("--��ѡ��--")){
						food_type = (String)session.getAttribute("food_type");
					}
					
//					out.println(food_type+"<br>");
				}
				if("food_price".equals(item.getFieldName())){
					food_price = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
//					out.println(food_price+"<br>");
				}
				if("food_cook".equals(item.getFieldName())){
					food_cook = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
//					out.println(food_cook+"<br>");
				}		
				if("food_synopsis".equals(item.getFieldName())){
					food_synopsis = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
//					out.println(food_synopsis+"<br>");
				}
				if("food_remark".equals(item.getFieldName())){
					food_remark = new String(item.getString().getBytes("iso-8859-1"),"utf-8");
//					out.println(food_remark+"<br>");
				}
			}
		}
		catch(Exception e) {
			// ������ת����ҳ��
			out.print(e.toString());
		}
		String sql = "update t_o_Food set o_foodName='"+food_name+"',o_foodType='"+food_type+"',o_foodPrice='"+food_price+"',o_foodCook='"+food_cook+"',o_foodSynopsis='"+food_synopsis+"',o_foodRemark='"+food_remark+"' where o_foodNo='"+food_no+"'";
		int result = DBfood.uploadFood(sql);
		if(result>0){
			response.getWriter().write("<script language='JavaScript'>" +
					"alert('�޸���Ʒ�ɹ���');" + 
					"window.location.href('foodchange?foodno=" + food_no+
					"');</script>"
					);
		}
		else{
			response.getWriter().write("<script language='JavaScript'>" +
					"alert('�޸�ʧ�ܣ�');" + 
					"window.location.href('foodchange?foodno=" + food_no+
					"');</script>"
					);
		}
	}

}
