package news.user;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class newsDao {
    Statement statement=null;
    public void addGPSRecord(StringBuffer sb) throws JSONException, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException classnotfoundexception) {
            classnotfoundexception.printStackTrace();
        }
        System.out.println("加载了JDBC驱动");
        Connection conn = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/xm08?user=XM08&password=123456&useUnicode=true&characterEncoding=UTF-8");
        System.out.println("准备statement。");
        statement = conn.createStatement();
        System.out.println("已经链接上数据库！");
        System.out.println("Connect Database Ok！！！<br>");
        ////////////////////////////////////////////////////
        //字符串转换成json并解析出来
        String result=sb.toString();
        if(!result.isEmpty()){
            JSONObject json=new JSONObject(result);
            JSONArray array=(JSONArray) json.get("list");
            for(int i=0;i<array.length();i++){
                JSONObject record=(JSONObject) array.get(i);
                addGPSRecord(record);
            }
        }
        statement.close();
        conn.close();
        System.out.println("Database Closed！！！<br>");
        System.out.println("操作数据完毕，关闭了数据库！");
    }

    private void addGPSRecord(JSONObject record) throws JSONException {
        String ctime="2020-11-23";
        String title="湖南国际交易现场发生";
        String description="澎湃财经";
        String picUrl="https://";
        String url="https://";

        ctime=record.getString("ctime");
        title=record.getString("title");
        description=record.getString("description");
        picUrl=record.getString("picUrl");
        url=record.getString("url");
        ////////////////////////////////////////////////////
        //然后链接数据库，开始操作数据表
        try {
            String sql = "insert into news(ctime,title,description,picUrl,url) values('"
                    + ctime + "'," + title + ","+description+",'"+picUrl+"','"+url+"')";
            System.out.println("即将执行的SQL语句是："+sql);
            statement.executeUpdate(sql);
        } catch (SQLException sqlexception) {
            sqlexception.printStackTrace();
        }
    }
}
