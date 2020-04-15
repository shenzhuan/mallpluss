package com;
/**
 * 该文件仅用于示例，接收推送数据。
 * 部署到公网可以访问的路径，成功回调后会根据您指定的路径生成txt文件，可查看推送的快递物流轨迹
 * 部署好后 调用推送接口url为 http://网址/demo/GetPushDataServlet
 */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetPushDataServlet
 */

public class Demo2 extends HttpServlet {
    private static final long serialVersionUID = 1L;



    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        //response.getWriter().append("Served at: ").append(request.getContextPath());
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        //doGet(request, response);
        String jsonData = request.getParameter("data");
        cin_txt fileUtil = new cin_txt();
        //完整文件路径。
        Boolean bool = fileUtil.createFile("/data/express.txt",jsonData );
        System.out.println(bool);
        //更新或者插入数据库(参考)
//		try {
//			fileUtil.updateOrIntoDB(jsonData);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        //必须只返回success字符串，关闭错误提示。
        response.getWriter().append("success");
    }

}
