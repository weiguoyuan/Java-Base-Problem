package weiguoyuan.Net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by william on 2017/11/20.
 * 测试获取指定url文档
 */
public class TestURLConnection {
    public static void main(String[] args){
        URLClient client = new URLClient();
        System.out.println(client.getDocumentAt("https://www.baidu.com"));
    }
}

class URLClient {
    protected URLConnection connection;//应用程序和URL之间创建通信链路的类的抽象超类 可以从资源读或往资源写

    public String getDocumentAt(String urlString){
        StringBuffer document = new StringBuffer();
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine())!=null){
                document.append(line+"\n");
            }
            reader.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return document.toString();
    }
}