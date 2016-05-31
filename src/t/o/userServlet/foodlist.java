package t.o.userServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import t.o.bean.DBfood;
import t.o.bean.foodBean;

public class foodlist extends HttpServlet {

	private int allrow = 0;//����
	private int yerow = 15;//ÿҳ����
	private int yeshu = 0;//ҳ��
	private int lastyerow = 0;//���һҳ������
	private int showpage = 1;//��ǰҳ
	private String sql = null;
	String food_type = null;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("URL:"+request.getRequestURI());
		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		food_type = request.getParameter("foodtype");
		session.setAttribute("currentpage", request.getRequestURI()+"?foodtype="+food_type);
		String url = food_type;
		if(food_type.equals("nosh")){
			food_type = "С��";
		}
		if(food_type.equals("my")){
			food_type = "���Ʋ�";
		}
		if(food_type.equals("cake")){
			food_type = "���";
		}
		if(food_type.equals("china")){
			food_type = "�в�";
		}
		if(food_type.equals("abroad")){
			food_type = "����";
		}
		if(food_type.equals("drink")){
			food_type = "��Ʒ";
		}
		ArrayList<foodBean> foodList = null;
		if(request.getParameter("ToPage")==null){	
			sql = "select * from t_o_Food where o_foodType = '"+food_type+"'";
			showpage = 1;
			foodList = new ArrayList<foodBean>();
			foodList = DBfood.getfm(sql);
			session.setAttribute("foodchange", foodList);
		}else{
			foodList = (ArrayList<foodBean>)session.getAttribute("foodchange");
		}
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
		str+="<form aciont='foodlist'>";
		str += "��ѯ��<span>"+allrow+"</span>����¼"+
		"    ��<span>"+yeshu+"</span>ҳ";  
		str +=" ��<span>"+showpage+"</span>ҳ ";  
		if(showpage>1) 
			str += " <A href=foodlist?foodtype="+url+"&ToPage=1"+">��ҳ</A> ";  
		else 
			str += " ��ҳ ";  
		if(showpage>1)
			str += " <A href=foodlist?foodtype="+url+"&ToPage=" + prev + ">��һҳ</A> ";  
		else
			str += " ��һҳ ";  
		if(showpage<yeshu)  
			str += " <A href=foodlist?foodtype="+url+"&ToPage=" + next + ">��һҳ</A> ";  
		else 
			str += " ��һҳ ";  
		if(yeshu>1&&showpage!=yeshu)  
			str += " <A href=foodlist?foodtype="+url+"&ToPage=" + yeshu + 
			">βҳ</A>";  
		else
			str += " βҳ ";  
		str+="��<input id=go name=ToPage type=text size=2>ҳ <input type=button value=GO onclick='goo();'></form>" ;
		
		session.setAttribute("str", str);
		RequestDispatcher dispatcher = request.getRequestDispatcher(url+".jsp");
		dispatcher.forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

}
