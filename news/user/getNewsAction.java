package news.user;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class getNewsAction implements ServletContextListener {
    private Timer timer = null;
    private TimerTask task=null;
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("================>[ServletContextListener]自动加载启动开始.");
        timer = new Timer(true);
        java.util.Date date = (new Date());
        MyTimerTask task = new MyTimerTask();
        timer.schedule(task, 2000,7200000);		//延迟2秒后，每隔20秒运行一次
        System.out.println("[GetNetworkData]运行了");
    }
    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("Current Time："+format.format(calendar.getTime()));//获取当前系统时间
            try {//多用函数
                //loginGPS();
                getNEWSData();
            } catch (MalformedURLException | JSONException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //boolean login=false;
    String urlData="http://api.tianapi.com/caijing/index?key=0f41868f6535bee8999e62c3ac7c260e&num=10";
    //String urlLogin="http://www.bcxgps.com/page/login/BcxLoad.action?r=g";
    /*
    public void loginGPS() throws MalformedURLException, JSONException, SQLException {
        if(!login){
            CookieManager manager = new CookieManager();
            CookieHandler.setDefault(manager);
            URL urlConn=new URL(urlLogin);
            getUrl(urlConn);
            login=true;
            System.out.println("登录网站");
        }
    }
    */
    public void getNEWSData() throws MalformedURLException, JSONException, SQLException {//多用throw
        URL urlConn=new URL(urlData);
        getUrl(urlConn);
        System.out.println("获取数据");
    }
    private void getUrl(URL urlConn) throws JSONException, SQLException {
        try {
            //创建连接
            HttpURLConnection connection = (HttpURLConnection) urlConn.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            // POST请求
            DataOutputStream out = new
                    DataOutputStream(connection.getOutputStream());
            JSONObject obj = new JSONObject();
            String json = java.net.URLEncoder.encode(obj.toString(), "utf-8");	//这里传递参数
            out.writeBytes(json);
            out.flush();
            out.close();
            // 读取响应
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream(),"UTF-8"));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            System.out.println(sb);

            newsDao dao=new newsDao();
            dao.addGPSRecord(sb);

            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
