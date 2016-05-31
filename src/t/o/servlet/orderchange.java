package t.o.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import t.o.bean.DBorder;
import t.o.bean.DBuser;
import t.o.bean.orderBean;
import t.o.bean.orderlisBean;
import t.o.bean.userBean;

public class orderchange extends HttpServlet {
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
			sql = "select * from t_o_Order";
			showpage = 1;
		}
		if(request.getParameter("searchtext")!=null){
			String search_type = request.getParameter("search_type");
			String order= new String(request.getParameter("searchtext").toString().getBytes("iso-8859-1"),"utf-8");
			System.out.println(search_type);
			if(search_type.equals("order_no")){
				sql = "select * from t_o_Order where o_orderNo like '%"+order+"%'";
				showpage = 1;
			}
			if(search_type.equals("order_user")){
				sql = "select * from t_o_Order where o_orderUser like '%"+order+"%'";
				showpage = 1;
			}
			if(search_type.equals("order_state")){
				sql = "select * from t_o_Order where o_orderState like '%"+order+"%'";
				showpage = 1;
			}
		}
		ArrayList<orderBean> orderList = new ArrayList<orderBean>();
		orderList = DBorder.getom(sql);
		session.setAttribute("orderchange", orderList);
		//��ʾ��ҳ��
		allrow = orderList.size();
		lastyerow = allrow % yerow;
		if(lastyerow == 0){
			yeshu = allrow/yerow;
		}
		else{
			yeshu = (allrow-lastyerow)/yerow;
			yeshu ++;
		}

		if(request.getParameter("ToPage")==null){
			ArrayList<orderBean> list = new ArrayList<orderBean>();
			if(allrow<yerow){
				for(int i=0;i<allrow;i++){
					list.add(orderList.get(i));
				}
			}
			else{
				for(int i=0;i<yerow;i++){
					list.add(orderList.get(i));
				}
			}
			session.setAttribute("orderList", list);
		}
		if(request.getParameter("ToPage")!=null){
			if(request.getParameter("ToPage").equals("")){
				ArrayList<orderBean> list = new ArrayList<orderBean>();
				if(allrow<yerow){
					for(int i=0;i<allrow;i++){
						list.add(orderList.get(i));
					}
				}
				else{
					for(int i=0;i<yerow;i++){
						list.add(orderList.get(i));
					}
				}
				session.setAttribute("orderList", list);
			}
			else{
				if(Integer.parseInt(request.getParameter("ToPage"))*yerow<=allrow){
					String pagea = request.getParameter("ToPage");
					int page = Integer.parseInt(pagea);
					ArrayList<orderBean> list = new ArrayList<orderBean>();
					for(int i=(page-1)*yerow;i<page*yerow;i++){
						list.add(orderList.get(i));
					}
					session.setAttribute("orderList", list);
					showpage = page;
				}
				if(Integer.parseInt(request.getParameter("ToPage"))*yerow>=allrow){
					String pagea = request.getParameter("ToPage");
					int page = Integer.parseInt(pagea);
					ArrayList<orderBean> list = new ArrayList<orderBean>();
					for(int i=(page-1)*yerow;i<allrow;i++){
						list.add(orderList.get(i));
					}
					session.setAttribute("orderList", list);
					showpage = page;
				}
			}
		}
		
		String str = "";  
		int next, prev;  
		prev=showpage-1;  
		next=showpage+1; 
		str+="<form aciont='orderchange'>";
		str += "��ѯ��<span>"+allrow+"</span>����¼"+
		"    ��<span>"+yeshu+"</span>ҳ";  
		str +=" ��<span>"+showpage+"</span>ҳ ";  
		if(showpage>1) 
			str += " <A href=orderchange?ToPage=1"+">��ҳ</A> ";  
		else 
			str += " ��ҳ ";  
		if(showpage>1)
			str += " <A href=orderchange?ToPage=" + prev + ">��һҳ</A> ";  
		else
			str += " ��һҳ ";  
		if(showpage<yeshu)  
			str += " <A href=orderchange?ToPage=" + next + ">��һҳ</A> ";  
		else 
			str += " ��һҳ ";  
		if(yeshu>1&&showpage!=yeshu)  
			str += " <A href=orderchange?ToPage=" + yeshu + 
			">βҳ</A>";  
		else
			str += " βҳ ";  
		str+="��<input name=ToPage type=text size=2>ҳ <input type=submit value=GO></form>" ;
		
		session.setAttribute("str", str);
		if(request.getParameter("order_no")!=null){
			String order_no = request.getParameter("order_no"); 
	    	ArrayList<orderBean> orderlist = (ArrayList<orderBean>)session.getAttribute("orderchange");
	    	ArrayList<orderlisBean> olist = new ArrayList<orderlisBean>();
	    	int size = orderlist.size();
	    	orderBean orderbean;
	    	for(int i=0;i<size;i++){
	    		orderbean = orderlist.get(i);
	    		String allprice = String.valueOf(orderbean.getOrder_allprice());
	    		String orderno = orderbean.getOrder_no();
	    		session.setAttribute("allprice", allprice);
	    		session.setAttribute("orderno", orderno);
	    		if(order_no.equals(orderbean.getOrder_no())){
	    			String food = orderbean.getOrder_food();
	    			while(food.indexOf(";")!=-1){
	    				String foodno = food.substring(0,food.indexOf(";"));
		    			food = food.substring(food.indexOf(";")+1,food.lastIndexOf(";")+1);
		    			String foodname = food.substring(0,food.indexOf(";"));
		    			food = food.substring(food.indexOf(";")+1,food.lastIndexOf(";")+1);
		    			int foodtime = Integer.parseInt(food.substring(0,food.indexOf("��")));
		    			food = food.substring(food.indexOf(";")+1,food.lastIndexOf(";")+1);
		    			float foodprice = Float.valueOf(food.substring(0,food.indexOf("Ԫ")));
		    			food = food.substring(food.indexOf(";")+1,food.lastIndexOf(";")+1);
		    			orderlisBean obean = new orderlisBean(foodno,foodname,foodtime,foodprice);
		    			olist.add(obean);
	    			}
	    			session.setAttribute("olist", olist);
	    			break;
	    		}
	    	}
			RequestDispatcher dispatcher = request.getRequestDispatcher("admin/orderchange.jsp");
			dispatcher.forward(request, response);
		}
		else if(request.getParameter("delete")!=null){
			String order_no = request.getParameter("delete");
			String sql = "delete from t_o_Order where o_orderNo = '" + order_no + "'";
			int result = DBuser.uploadUser(sql);
			if(result > 0){
				response.getWriter().write("<script language='JavaScript'>" +
						"alert('�h�������ɹ���');" + 
						"window.location.href='orderchange';" + 
						"</script>"
						);
			}else{
				response.getWriter().write("<script language='JavaScript'>" +
						"alert('�h���������ɹ���');" + 
						"window.location.href='orderchange';" + 
						"</script>"
						);
			}
		}
		else if(request.getParameter("ok")!=null){
			String order_no = request.getParameter("ok");
			String sql = "update t_o_Order set o_orderState = '���' where o_orderNo = '"+order_no+"'";
			int result = DBuser.uploadUser(sql);
			if(result > 0){
				response.getWriter().write("<script language='JavaScript'>" +
						"alert('��������ɣ�');" + 
						"window.location.href='orderchange';" + 
						"</script>"
						);
			}else{
				response.getWriter().write("<script language='JavaScript'>" +
						"alert('�д���');" + 
						"window.location.href='orderchange';" + 
						"</script>"
						);
			}
		}
		else{
			RequestDispatcher dispatcher = request.getRequestDispatcher("admin/ordersearch.jsp");
			dispatcher.forward(request, response);
		}
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
