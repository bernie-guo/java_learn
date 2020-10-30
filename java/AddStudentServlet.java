package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ServiceImpl.StudnetServiceImpl;
import daoimpl.StudentDaoImpl;
import model.Student;

/**
 * Servlet implementation class AddStudentervlet
 */
@WebServlet("/addStudentServlet")
public class AddStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddStudentServlet() {
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
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String flag = request.getParameter("flag");//�����ȡ����
		switch (flag) {
		case "select":
			seleclt(request, response);
			break;
		case "add":
			add(request, response);
			break;
		case "delete":
			delete(request, response);
			break;
		case "update":
			update(request, response);
			break;
		}
	}

	public void seleclt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentDaoImpl studentDaoImpl = new StudentDaoImpl();
		List<Student> students=studentDaoImpl.Getall();
		request.setAttribute("students", students);//��request�����м�����Ϊstudents�����ԣ�����ֵΪstudents
		request.getRequestDispatcher("manager/selectstudent.jsp").forward(request, response);

	}

	public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StudentDaoImpl studentDaoImpl=new StudentDaoImpl();
        Student ss=new Student();//����һ��ѧ���������ڷ�װ�ύ������
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String xh=request.getParameter("XH"); ss.setXh(xh);
		String xm=request.getParameter("XM");xm = new String(xm.getBytes("ISO-8859-1"),"UTF-8");ss.setXm(xm);
		System.out.println(xm+"��");
		String xb=request.getParameter("XB");xb = new String(xb.getBytes("ISO-8859-1"),"UTF-8");ss.setXb(xb);
		String sfz=request.getParameter("SFZ");ss.setSfz(sfz);
		String major=request.getParameter("MAJOR");major = new String(major.getBytes("ISO-8859-1"),"UTF-8");ss.setMajor(major);
		String xy=request.getParameter("XY");xy = new String(xy.getBytes("ISO-8859-1"),"UTF-8");ss.setXy(xy);
		String mm=request.getParameter("MM"); ss.setMm(mm);
		//Student student = new Student(xh,xm,xb,sfz,major,xy,mm);
		
		boolean s= studentDaoImpl.addStudent(ss);
		//System.out.println(s);
		if(s==true){
			String msg ="��ӳɹ���";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("manager/NewStudent.jsp").forward(request, response);
		}
	}

	public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StudentDaoImpl studentDaoImpl=new StudentDaoImpl();
        Student ss=new Student();//����һ��ѧ���������ڷ�װ�ύ������
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String xh=request.getParameter("xh"); ss.setXh(xh);
		boolean a=studentDaoImpl.delStudent(ss);
		if(a==true){
			String msg ="ɾ���ɹ���";
			List<Student> students=studentDaoImpl.Getall();
			request.setAttribute("students", students);
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("manager/selectstudent.jsp").forward(request, response);	   
		}
	}

	public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		Student s=new Student();
		String xh = request.getParameter("xh");s.setXh(xh);
		String xm = request.getParameter("xm");xm = new String(xm.getBytes("ISO-8859-1"),"UTF-8");s.setXm(xm);
		String xb = request.getParameter("xb");xb = new String(xb.getBytes("ISO-8859-1"),"UTF-8");s.setXb(xb);
		String sfz = request.getParameter("sfz");s.setSfz(sfz);
		String major = request.getParameter("major");major = new String(major.getBytes("ISO-8859-1"),"UTF-8");s.setMajor(major);
		String xy= request.getParameter("xy");xy = new String(xy.getBytes("ISO-8859-1"),"UTF-8");s.setXy(xy);
		String mm = request.getParameter("mm");s.setMm(mm);
		boolean ss= new StudentDaoImpl().updateStudent(s);
		if(ss==true){
			String msg ="�޸ĳɹ�";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("manager/updateStudent.jsp").forward(request, response);
		}
		else{
			String msg ="�޸�ʧ��";
			request.setAttribute("msg", msg);
		}
	}

}
