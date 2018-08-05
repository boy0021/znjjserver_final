package apply;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import utl.RandomCode;

public class ApplyCode extends HttpServlet {

	public ApplyCode() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String myRandNumCode = RandomCode.getRandNumCode();
		JSONObject jsonobject =  new JSONObject();
		response.setContentType("text/html");
		//PrintWriter out = response.getWriter();
		//out.println("</HTML>");
		jsonobject.put("ret", 1);
		jsonobject.put("key", myRandNumCode);
		response.getWriter().println(jsonobject.toString());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet( request,  response);
	}

	public void init() throws ServletException {
	}

}
