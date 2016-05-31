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

import t.o.bean.CommentBean;
import t.o.bean.DBcomment;
import t.o.bean.DBfood;
import t.o.bean.CommentBean;

public class Commentchange extends HttpServlet {

	private int allrow = 0;//����
	private int yerow = 10;//ÿҳ����
	private int yeshu = 0;//ҳ��
	private int lastyerow = 0;//���һҳ������
	private int showpage = 1;//��ǰҳ
	private String sql = null;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			sql = "select * from t_o_Comment inner join t_o_Food on t_o_Comment.o_foodNo = t_o_Food.o_foodNo order by o_cmttime desc ";
			showpage = 1;
		}
		//��������
		if(request.getParameter("cmtsearch")!=null){
			String food_type = request.getParameter("foodtype");
			String food= new String(request.getParameter("cmtsearch").toString().getBytes("iso-8859-1"),"utf-8");
			if(food_type.equals("food_name")){
				sql = "select * from t_o_Comment inner join t_o_Food on t_o_Comment.o_foodNo = t_o_Food.o_foodNo where t_o_Food.o_foodName like '%"+food+"%'";
				showpage = 1;
			}
			if(food_type.equals("food_no")){
				sql = "select * from t_o_Comment inner join t_o_Food on t_o_Comment.o_foodNo = t_o_Food.o_foodNo where t_o_Food.o_foodNo like '%"+food+"%'";
				showpage = 1;
			}
		}
		//��������������
		ArrayList<CommentBean> cmtList = new ArrayList<CommentBean>();
		cmtList = DBcomment.getcommentwithname(sql);
		session.setAttribute("Commentchange", cmtList);
		//��ʾ��ҳ��
		allrow = cmtList.size();
		lastyerow = allrow % yerow;
		if(lastyerow == 0){
			yeshu = allrow/yerow;
		}
		else{
			yeshu = (allrow-lastyerow)/yerow;
			yeshu ++;
		}
		if(request.getParameter("ToPage")==null){
			ArrayList<CommentBean> list = new ArrayList<CommentBean>();
			if(allrow<yerow){
				for(int i=0;i<allrow;i++){
					list.add(cmtList.get(i));
				}
			}
			else{
				for(int i=0;i<yerow;i++){
					list.add(cmtList.get(i));
				}
			}
			session.setAttribute("cmtList", list);
		}
		if(request.getParameter("ToPage")!=null){
			if(request.getParameter("ToPage").equals("")){
				ArrayList<CommentBean> list = new ArrayList<CommentBean>();
				if(allrow<yerow){
					for(int i=0;i<allrow;i++){
						list.add(cmtList.get(i));
					}
				}
				else{
					for(int i=0;i<yerow;i++){
						list.add(cmtList.get(i));
					}
				}
				session.setAttribute("cmtList", list);
			}
			else{
				if(Integer.parseInt(request.getParameter("ToPage"))*yerow<=allrow){
					String pagea = request.getParameter("ToPage");
					int page = Integer.parseInt(pagea);
					ArrayList<CommentBean> list = new ArrayList<CommentBean>();
					for(int i=(page-1)*yerow;i<page*yerow;i++){
						list.add(cmtList.get(i));
					}
					session.setAttribute("cmtList", list);
					showpage = page;
				}
				if(Integer.parseInt(request.getParameter("ToPage"))*yerow>=allrow){
					String pagea = request.getParameter("ToPage");
					int page = Integer.parseInt(pagea);
					ArrayList<CommentBean> list = new ArrayList<CommentBean>();
					for(int i=(page-1)*yerow;i<allrow;i++){
						list.add(cmtList.get(i));
					}
					session.setAttribute("cmtList", list);
					showpage = page;
				}
			}
		}
		String str = "";  
		int next, prev;  
		prev=showpage-1;  
		next=showpage+1; 
		str+="<form aciont='Commentchange'>";
		str += "��ѯ��<span>"+allrow+"</span>����¼"+
		"    ��<span>"+yeshu+"</span>ҳ";  
		str +=" ��<span>"+showpage+"</span>ҳ ";  
		if(showpage>1) 
			str += " <A href=Commentchange?ToPage=1"+">��ҳ</A> ";  
		else 
			str += " ��ҳ ";  
		if(showpage>1)
			str += " <A href=Commentchange?ToPage=" + prev + ">��һҳ</A> ";  
		else
			str += " ��һҳ ";  
		if(showpage<yeshu)  
			str += " <A href=Commentchange?ToPage=" + next + ">��һҳ</A> ";  
		else 
			str += " ��һҳ ";  
		if(yeshu>1&&showpage!=yeshu)  
			str += " <A href=Commentchange?ToPage=" + yeshu + 
			">βҳ</A>";  
		else
			str += " βҳ ";  
		str+="��<input name=ToPage type=text size=2>ҳ <input type=submit value=GO></form>" ;
		
		session.setAttribute("str", str);
		if(request.getParameter("delete")!=null){
			System.out.println("��ʼɾ������");
			String comment_Id = request.getParameter("delete");
			String sql = "delete from t_o_Comment where o_commentId = '" + comment_Id  + "'";
			int result = DBfood.uploadFood(sql);
			if(result > 0){
				response.getWriter().write("<script language='JavaScript'>" +
						"window.location.href='Commentchange';" + 
						"</script>"
						);
			}else{
				response.getWriter().write("<script language='JavaScript'>" +
						"alert('�h�����۲��ɹ���');" + 
						"window.location.href='Commentchange';" + 
						"</script>"
						);
			}
		}
		else{
			RequestDispatcher dispatcher = request.getRequestDispatcher("admin/commentsearch.jsp");
			dispatcher.forward(request, response);
		}
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	
	}
	

}
