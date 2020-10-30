package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import daoimpl.StudentDaoImpl;
import model.Student;

@WebServlet("/updateStudentServlet")
public class UpdateStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    String xh = request.getParameter("xhh");
	        String xm = request.getParameter("xmm");
	        String xb = request.getParameter("xbb");
	        String sfz = request.getParameter("sfzz");
	        String major = request.getParameter("majorr");
	        String xy = request.getParameter("xyy");
	        String mm = request.getParameter("mmm");
	        Student student = new Student(xh,xm,xb,sfz,major,xy,mm);
	        StudentDaoImpl studentDaoImpl = new StudentDaoImpl();
	        boolean a = studentDaoImpl.updateStudent(student);
	        if(a==true){
				String msg ="修改成功";
				request.setAttribute("msg", msg);
				request.getRequestDispatcher("helloStudent.jsp").forward(request, response);
			}
			else{
				String msg ="修改失败";
				request.setAttribute("msg", msg);
			}
	}

}
