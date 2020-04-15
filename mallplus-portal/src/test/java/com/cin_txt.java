package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class cin_txt {

    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public  boolean createFile(String fileName,String filecontent){
        Boolean bool = false;
        File file = new File(fileName);
        try {
            System.out.println(file.exists());
            //如果文件不存在，则创建新的文件
            if(!file.exists()){

                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+fileName);
                //创建文件成功后，写入内容到文件里
                writeFileContent(fileName, filecontent);
            }else {
                bool = true;
                //创建文件成功后，写入内容到文件里
                writeFileContent(fileName, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public  boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
    /* 接收物流轨迹数据
    *  存入数据库，以MySql为例
    *  数据库表expressTable简单设计如下【示例】
    +---+----------+------+------+----------------------------------------------------------------------------------------------+
    |id |  no      |type  |status|  content																					    |
    +---+----------+------+------+----------------------------------------------------------------------------------------------+
    | 1 |711340200 | ZTO  | 签收 |{"time":"2019-06-20 21:11:49","status":"到达【邮政绍兴中心】"}......						    |
    +---+----------+------+------+----------------------------------------------------------------------------------------------+
    | 2 |711340201 | YTO  | 在途 |{"time":"2019-06-20 19:24:20","status":"【柯桥营揽快包】已收件,揽投员:边叶洋13205750305"}...  |
    +---+----------+------+------+----------------------------------------------------------------------------------------------+
    //===========有物流轨迹时推送示例==================
    {
    	"code": "OK",
    	"no": "3102532447583",
    	"type": "YD",
    	"list": [{
    		"content": "【北京市】已离开 北京分拨中心；发往 上海浦东分拨中心",
    		"time": "2019-06-09 21:52:14"
    	}, {
    		"content": "【北京市】已离开 北京分拨中心；发往 上海浦东分拨中心",
    		"time": "2019-06-09 19:31:08"
    	}, {
    		"content": "【北京市】已到达 北京分拨中心",
    		"time": "2019-06-09 19:29:53"
    	}, {
    		"content": "【北京市】北京金韵公司 已揽收",
    		"time": "2019-06-09 14:40:30"
    	}],
    	"state": "2",
    	"msg": "查询成功",
    	"name": "韵达快递",
    	"site": "www.yundaex.com",
    	"phone": "95546",
    	"logo": "http:\/\/img3.fegine.com\/express\/yd.jpg",
    	"courier": "",
    	"courierPhone": ""
    }
    //===========无物流轨迹时推送示例==================
    {"code":"205","no":"7800980680581","type":"ZTO","list":[],"state":"0","msg":"暂无轨迹信息"}
    */
    public  void updateOrIntoDB(String jsonData) throws Exception{
        JsonParser parser = new JsonParser() ;
        JsonObject object = (JsonObject)parser.parse(jsonData);
        String code = object.get("code").getAsString();

        if(code.equals("OK")) {
            String no = object.get("no").getAsString();
            String type = object.get("type").getAsString();
            String content = jsonData;
            String state = object.get("state").getAsString();
            //有物流轨迹时，物流状态代码列表
            Map<String, String> maps = new HashMap<>();
            maps.put("-1", "单号或代码错误");
            maps.put("0", "暂无轨迹");
            maps.put("1", "快递收件");
            maps.put("2", "在途中");
            maps.put("3", "签收");
            maps.put("4", "问题件");
            maps.put("5", "疑难件");
            maps.put("6", "退件签收");
            maps.put("7", "快递收件(揽件)");

            Class.forName("com.mysql.jdbc.Driver");//加载驱动

            String jdbc="jdbc:mysql://127.0.0.1:3306/mydb?characterEncoding=utf8";
            Connection conn=DriverManager.getConnection(jdbc, "账号", "密码");//链接到数据库

            Statement smt=conn.createStatement();   //容器
            String sql="replace into expressTable (no, type, status, content) values ('"+no+"','"+type+"','"+maps.get(state)+"','"+content+"')";   //SQL语句
            smt.executeUpdate(sql);         //将sql语句上传至数据库执行
            smt.close();
            conn.close();//关闭通道
        }
    }
}
