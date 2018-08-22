package com.sharewith.smartudy.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.sharewith.smartudy.Interface.AsyncResponse;
import com.sharewith.smartudy.activity.QnAActivity;
import com.sharewith.smartudy.adapter.WriteFragmentRecyclerAdapter;
import com.sharewith.smartudy.dto.WriteFragComponent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpUtils extends AsyncTask<String, String, String>{
    private AsyncResponse delegate = null;
    public static final int POST = 1;
    public static final int GET = 0;
    public static final int MULTIPART = 2;
    private static int Method = GET;
    private static HashMap<String,String> map;
    private static String url = "";
    private Context context;
    public static boolean auto_login = false;
    HttpURLConnection con = null;
    private String boundary = "****";
    private String CRLF = "\r\n";//캐리지 리턴 + 라인 피드
    private WriteFragComponent multipartdata;


    public HttpUtils(int method, HashMap<String,String> map, String url, Context context){
        HttpUtils.Method = method;
        HttpUtils.map = map;
        HttpUtils.url = url;
        this.context = context;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(delegate != null)
            delegate.getAsyncResponse(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strs) {
        if(Method == MULTIPART)
            return multipartRequest(multipartdata,delegate);
        else
            return request();
    }

    private void setCookieHeader(){
        SharedPreferences pref = context.getSharedPreferences("sessionCookie",Context.MODE_PRIVATE);
        String sessionid = pref.getString("sessionid",null);
        if(sessionid!=null) {
            Log.d("LOG","세션 아이디"+sessionid+"가 요청 헤더에 포함 되었습니다.");
            con.setRequestProperty("Cookie", sessionid);
        }
    }

    private void setMultipartAttrs(DataOutputStream dos,String key,String value) {
        if(value == null || value.equals("")) return;
        try{
        dos.writeBytes("--"+boundary + CRLF);
        dos.writeBytes("Content-Disposition: form-data; name=\""+key+"\"" + CRLF);
        dos.writeBytes(CRLF);
        dos.write(value.getBytes("UTF-8"));
        dos.writeBytes(CRLF);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setMultipartHedaer() {
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        con.setDefaultUseCaches(false);
        con.setUseCaches(false);
        con.setDoInput(true);
        con.setDoOutput(true);
    }

    private void setBinaryAttrs(DataOutputStream dos,String key,String path) {
        if(path == null || path.equals("")) return;
        File file = new File(path);
        if(!file.exists()) {
            Log.d("httputils", path + "에 해당하는 파일이 존재 하지 않습니다.");
            return;
        }
        try {
            dos.writeBytes("--"+boundary + CRLF);
            dos.writeBytes("Content-Disposition: form-data; name=\""+key+"\";filename=\"" + file.getName() + "\"" + CRLF);
            dos.writeBytes(CRLF);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath()); //path -> 이미지 파일의 절대경로
            int buffersize;
            while( (buffersize = Math.min(fis.available(),1024)) > 0) { //운영체제에서 1024바이트 단위로 데이터를 읽기 때문
                byte[] buffer = new byte[buffersize];
                fis.read(buffer,0,buffersize);
                dos.write(buffer);
            }
            dos.writeBytes(CRLF);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getResponseFromServer() {
        String line;
        String page = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                page += line;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        Log.d("HttpUtils","서버로 부터 받은 데이터" + page);
        return page;
    }

    private String multipartRequest(WriteFragComponent data,AsyncResponse delegate) {//delegate -> Async 작업이 끝나면 자동으로 호출되는 후처리 클래스
        this.delegate = delegate;
        try {
            URL Url = new URL(url);
            Log.d("httputils", "서버로 MultiPart 요청 : " + url);
            con = (HttpURLConnection) Url.openConnection();
            setMultipartHedaer();
            con.setRequestMethod("POST");
            //setMultipartHedaer();
            //setCookieHeader(); //사용자가 로그인해서 세션 쿠키를 서버로부터 발급받은적 있다면 그 다음 요청 헤더 부터는 그 세션 쿠키를 포함해서 전송해야 함.
            OutputStream os = con.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            setMultipartAttrs(dos, "title", data.getTitle());
            setMultipartAttrs(dos, "content", data.getContent());
            setMultipartAttrs(dos,"grp",data.getGrp());
            setMultipartAttrs(dos, "subject", data.getSubject());
            setMultipartAttrs(dos, "money", data.getMoney());
            setMultipartAttrs(dos, "hashtag", data.getHashtag());
            setMultipartAttrs(dos,"category",data.getCategory());
            for (String path : data.getImages())
                setBinaryAttrs(dos, "images", path);
            for (String path : data.getAudios())
                setBinaryAttrs(dos, "audios", Uri.parse(path).getPath());
            for (String path : data.getDraws())
                setBinaryAttrs(dos, "draws", path);
            dos.writeBytes("--" + boundary + "--" + CRLF);
            dos.flush();
            dos.close();

            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) //이때 서버로 요청
                Log.d("LOG", "HTTP_OK를 받지 못했습니다.");
            String jsonResult = getResponseFromServer();
            //getCookieHeader();
            return jsonResult;
        }
        catch(Exception e) {
            e.printStackTrace();
        }finally {
            if (con != null)
                con.disconnect();
        }
        return null;
    }

       /*
            POST /files HTTP/1.1
            Host: localhost
            Cache-Control: no-cache
            Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW

            ------WebKitFormBoundary7MA4YWxkTrZu0gW
            Content-Disposition: form-data; name="file"; filename="sav.txt"
            Content-Type: text/plain

            나는 데이터 입니다.
            ------WebKitFormBoundary7MA4YWxkTrZu0gW
            Content-Disposition: form-data; name="file"; filename="otherFile.txt"
            Content-Type: text/plain

            나는 다른 데이터입니다.
            ------WebKitFormBoundary7MA4YWxkTrZu0gW--
        */


    private String request(){ //key&value로 전송하고 json으로 받기.
        String params = makeParameter(map);
        try{
            if(Method == HttpUtils.POST) {
                URL Url = new URL(url);
                con = (HttpURLConnection) Url.openConnection();
                con.setRequestProperty("Accept","application/json");
                con.setRequestMethod("POST");
                con.setDefaultUseCaches(false);
                con.setUseCaches(false);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Accept-Charset", "UTF-8");
                OutputStream os = con.getOutputStream();
                os.write(params.getBytes("UTF-8"));
                os.flush();
                os.close();
            }
            else if(Method == HttpUtils.GET) {
                StringBuilder sb = new StringBuilder();
                sb.append("?"+params);
                url += sb.toString();
                URL Url = new URL(url);
                con = (HttpURLConnection) Url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept","application/json");
                con.setDefaultUseCaches(false);
                con.setUseCaches(false);
                con.setDoInput(true);
                con.setRequestProperty("Accept-Charset", "UTF-8");
                con.setDoOutput(false); //DoOutPut설정시 GET으로 설정해도 자동으로 POST로 바뀐다.
            }
            //setCookieHeader();
            //사용자가 로그인해서 세션 쿠키를 서버로부터 발급받은적 있다면 그 다음 요청 헤더 부터는 그 세션 쿠키를 포함해서 전송해야 함.


            Log.d("HttpUtils",url+"로 HTTP 요청 전송");
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) { //이때 요청이 보내짐.
                Log.d("LOG", "HTTP_OK를 받지 못했습니다.");
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            String line;
            String page = "";
            while ((line = reader.readLine()) != null){
                page += line;
            }
            //getCookieHeader();
            return page;
        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }

        return null;

    }
    private String makeParameter(HashMap<String,String> params){
        String str = "";
        StringBuffer sb = new StringBuffer();
        if (params == null)
            sb.append("");
        else {
            String key;
            String value;
            for (String k : params.keySet()) {
                key = k;
                value = params.get(k);
                try {
                    if(Method != POST)
                        value = URLEncoder.encode(value,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append(key+"="+value+"&");
            }
            str = sb.toString();
        }
        str = str.substring(0,sb.length()-1);
        return str;
    }

    private void getCookieHeader(){//Set-Cookie에 배열로 돼있는 쿠키들을 스트링 한줄로 변환
        List<String> cookies = con.getHeaderFields().get("Set-Cookie");
        //cookies -> [JSESSIONID=D3F829CE262BC65853F851F6549C7F3E; Path=/smartudy; HttpOnly] -> []가 쿠키1개임.
        //Path -> 쿠키가 유효한 경로 ,/smartudy의 하위 경로에 위의 쿠키를 사용 가능.
        if (cookies != null) {
            for (String cookie : cookies) {
                String sessionid = cookie.split(";\\s*")[0];
                //JSESSIONID=FB42C80FC3428ABBEF185C24DBBF6C40를 얻음.
                //세션아이디가 포함된 쿠키를 얻었음.
                //이제 해야할일은, 그 세션아이디를 sharedpreference에 저장하는것. 저장하기 전에 기존에 저장된 세션아이디가 있는지 확인한다.
                //기존에 저장된 세션의 아이디가 현재 수신한 세션의 아이디와 일치하는 경우 서버의 세션이 만료되지 않았다는 의미이다.
                //기존에 저장된 세션의 아이디가 현재 수신한 세션의 아이디와 일치하지 않는 경우 서버의 세션이 만료되어 다른 세션 아이디를 발급 했다는것이다.
                //기존에 저장된 세션의 아이디가 존재하고, 현재 수신한 세션의 아이디와 일치 하지 않는 경우 서버의 세션 아이디로 교체 해야 한다.
                //기존에 저장된 세션의 아이디가 없는 경우, 그냥 서버의 세션 아이디를 저장 하면 된다.
                //요청을 보낼때마다 sharedpref에서 세션아이디 필드가 존재하는지 확인해서 있으면 쿠키를 포함해서 보내고, 없으면 보내지 않는다.
                setSessionIdInSharedPref(sessionid);

            }
        }
    }

    private void setSessionIdInSharedPref(String sessionid){
        SharedPreferences pref = context.getSharedPreferences("sessionCookie",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        if(pref.getString("sessionid",null) == null){ //처음 로그인하여 세션아이디를 받은 경우
            Log.d("LOG","처음 로그인하여 세션 아이디를 pref에 넣었습니다."+sessionid);
        }else if(!pref.getString("sessionid",null).equals(sessionid)){ //서버의 세션 아이디 만료 후 갱신된 아이디가 수신된경우
            Log.d("LOG","기존의 세션 아이디"+pref.getString("sessionid",null)+"가 만료 되어서 "
            +"서버의 세션 아이디 "+sessionid+" 로 교체 되었습니다.");
        }
        edit.putString("sessionid",sessionid);
        edit.apply(); //비동기 처리
    }


    //게터 세터
    public AsyncResponse getDelegate() {
        return delegate;
    }
    public void setDelegate(AsyncResponse delegate) {
        this.delegate = delegate;
    }
    public WriteFragComponent getMultipartdata() {
        return multipartdata;
    }
    public void setMultipartdata(WriteFragComponent multipartdata) {
        this.multipartdata = multipartdata;
    }
}