package t.o.userServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import t.o.bean.DBfood;
import t.o.bean.carBean;
import t.o.bean.foodBean;
import t.o.bean.randomno;

public class shoppingcar extends HttpServlet {
	ArrayList foodorderList = new ArrayList();
	int num = 1;
	Map<String, carBean> cart;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		String sqla = "select * from t_o_Food";
		session.setAttribute("foodList", DBfood.getfm(sqla));
		String currentpage = (String)session.getAttribute("currentpage");
		System.out.println("currentpage:"+currentpage);
		if(session.getAttribute("userName")==null){
			response.getWriter().write("<script language='JavaScript'>" +
					"alert('�Բ��𣬵��ϵͳֻΪ�����Ա���ţ����¼������');" + 
					"history.back();" + 
					"</script>"
					);
		}
		else if(request.getParameter("foodnum")!=null){
			int foodnum = Integer.parseInt(request.getParameter("foodnum"));
			String food_no = request.getParameter("food_no");
			if(foodnum<1){
				cart.remove(food_no);
			}
			else{
				System.out.println(foodnum);
				cart.get(food_no).setQuantity(foodnum);
			}
			session.setAttribute("carsize", cart.size());
			response.sendRedirect(getServletContext().getContextPath()+"/shopingcar.jsp");
		}
		else if(request.getParameter("delete_no")!=null){
			String delete_no = request.getParameter("delete_no");
			cart.remove(delete_no);
			session.setAttribute("carsize", cart.size());
			response.sendRedirect(getServletContext().getContextPath()+"/shopingcar.jsp");
		}
		else if(request.getParameter("deleteall")!=null){
			cart = new HashMap();
			session.setAttribute("cart", cart);
			session.setAttribute("carsize", cart.size());
			response.sendRedirect(getServletContext().getContextPath()+"/shopingcar.jsp");
		}
		else if(request.getParameter("define")!=null){
			String orderfood = "";
			float zj = Float.valueOf(request.getParameter("define"));
			System.out.println("zj::::"+zj);
			Map cart = (Map)session.getAttribute("cart");
			Set foodorderBeans = cart.keySet();
			Object[] isbn = foodorderBeans.toArray();
			int size = cart.size();
			for(int i=0;i<size;i++){
				carBean carbean = (carBean)cart.get(isbn[i]);
				String food_no = carbean.getFoodbean().getFood_no();
				String food_name = carbean.getFoodbean().getFood_name();
				int foodnum = carbean.getQuantity();
				float food_price = Float.valueOf(carbean.getFoodbean().getFood_price());
				orderfood = orderfood+food_no+";"+food_name+";"+foodnum+"��;"+foodnum*food_price+"Ԫ;";
			}
			String orderno = randomno.orderno();
			String roomname = session.getAttribute("userName").toString();
			String sql = "insert into t_o_Order values(NULL,'"+orderno+"','"+roomname+"','"+orderfood+"',"+zj+",'','δ���')";
			System.out.println(sql);
			int result = DBfood.uploadFood(sql);
			if(result>0){
				cart = new HashMap();
				session.setAttribute("cart", cart);
				session.setAttribute("carsize", 0);
				response.getWriter().write("<script language='JavaScript'>" +
						"alert('�����ɹ�������');" +
						"window.location.href=\""+currentpage+"\""+
						"</script>"
						);
				
			}
			else{
				response.getWriter().write("<script language='JavaScript'>" +
						"alert('����ʧ�ܣ�');" + 
						"window.close();" + 
						"</script>"
						);
			}
		}
		else{
			String food_no = request.getParameter("food_no").trim();
			ArrayList<foodBean> foodList = (ArrayList<foodBean>)session.getAttribute("foodList");
			foodBean foodbean;
			for(int i=0;i<foodList.size();i++){
				foodbean = foodList.get(i);
				if(food_no.equals(foodbean.getFood_no().trim())){
					foodbean = foodList.get(i);
					foodorderList.add(foodbean);
					//����Ʒ���빺�ﳵ
					cart =
						(Map)request.getSession(false).getAttribute("cart");
					
					if(cart == null){
						//�������ﳵ
						System.out.println("��ʼ�������ﳵ");
						//Map<Object,Object> map = new HashMap<Object,Object>();
						cart = new HashMap();
						session.setAttribute("cart", cart);
						System.out.println("�������ﳵ�ɹ�");
					}
					
					carBean carbean = new carBean(foodbean, num);
					//���ɵĹ����¼�͹��ﳵ�����м�¼�жϣ����������ͬ��
					//��Ʒ����ı�������������put����
					if(cart.get(food_no)!=null){	
						System.out.println("��ͬ");
						int numfood = cart.get(food_no).getQuantity();
						numfood++;
						System.out.println(numfood);
						cart.get(food_no).setQuantity(numfood);
					}
					else{
						System.out.println("��ͬ");
						System.out.println(carbean);
						cart.put(foodbean.getFood_no(), carbean);
						System.out.println("���빺�ﳵ�ɹ�");
					}
			
					break;
				}
			}
			//System.out.println("URL:"+request.getRequestURI());
			session.setAttribute("carsize", cart.size());
			session.setAttribute("foodorderList", foodorderList);
			response.getWriter().write("<script language='JavaScript'>" +
					"alert('��ͳɹ�������');" +
					"window.location.href=\""+currentpage+"\""+
					"</script>"
					);
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
