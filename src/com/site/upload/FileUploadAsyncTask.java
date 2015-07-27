package com.site.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.site.tools.PublicWay;
import com.site.tools.SiteUtil;

public class FileUploadAsyncTask extends AsyncTask<File, Integer, String> {

	private String url = "http://op.yg84.com:7000/universe/car2/upload?";
	private Context context;
	private ProgressDialog pd;
	private long totalSize;
	private String showMsg;
	
	public FileUploadAsyncTask(Context context,ProgressDialog pd) {
		this.context = context;
		this.pd=pd;
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("上传中....");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected String doInBackground(File... files) {
		// 保存需上传文件信息
		MultipartEntityBuilder entitys = MultipartEntityBuilder.create();
		entitys.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		entitys.setCharset(Charset.forName(HTTP.UTF_8));
		Map<String, String> params = new HashMap<String, String>();
		params.put("cityId", SiteUtil.getCity());
		params.put("linelist", SiteUtil.getLineIds());
		params.put("stopName", SiteUtil.getStopName());
		params.put("stopId", SiteUtil.getStopId());
		params.put("jingdu", SiteUtil.getLongitude());
		params.put("weidu", SiteUtil.getLatitude());
		Iterator iterator = params.entrySet().iterator();  
	    while (iterator.hasNext()) 
	    {  
	            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();  
	            entitys.addTextBody(entry.getKey(), entry.getValue(), ContentType.create("text/plain", Charset.forName("UTF-8")));  
	    }  
	     
        // 发送的文件   
        if (files != null)
        {  
            for(int i=0;i<files.length;i++)
            {  
            	entitys.addPart("file"+i, new FileBody(files[i]));
            }  
        }  
		
		
//	     File file = new File("/system/media/Pre-loaded/Pictures/Picture_05_Stream.jpg");
//	     File file1 = new File("/system/media/Pre-loaded/Pictures/Picture_05_Stream.jpg");
//	     
//		entitys.addPart("file1", new FileBody(file));
//		
//		entitys.addPart("file2", new FileBody(file1));
		
		HttpEntity httpEntity = entitys.build();
		totalSize = httpEntity.getContentLength();
		
		ProgressOutHttpEntity progressHttpEntity = new ProgressOutHttpEntity(
				httpEntity, new ProgressListener()
				{
					@Override
					public void transferred(long transferedBytes) 
					{
						publishProgress((int) (100 * transferedBytes / totalSize));
					}
				});
		try {
			return uploadFile(url, progressHttpEntity);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String result) 
	{
		pd.dismiss();
//		Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
		submitResult(result);
	}

	public void submitResult(String json)
	{
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(json);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String status = jsonObject.get("status").getAsString();
		if ("00".equals(status))
		{
			showMsg="上传成功";
		} else 
		{
			showMsg="上传失败,请重试";
		}
		showDialog();
	}

	public void showDialog() 
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				for (int i = 0; i < PublicWay.activityList.size(); i++)
				{
					if (null != PublicWay.activityList.get(i)) 
					{
						PublicWay.activityList.get(i).finish();
					}
				}
				System.exit(0);
			}
					 
			}).setTitle("提示").setMessage(showMsg).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}
	
	/**
	 * 上传文件到服务器
	 * 
	 * @param url
	 *            服务器地址
	 * @param entity
	 *            文件
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static String uploadFile(String url, ProgressOutHttpEntity entity) throws IllegalStateException, IOException 
	{
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		// 设置连接超时时间
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		HttpResponse httpResponse = null;
		String result="";
		try {
			 httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				    InputStream inStream =  httpResponse.getEntity().getContent();  
			        // 分段读取输入流数据   
			        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			        byte[] buf = new byte[1024];  
			        int len = -1;  
			        while ((len = inStream.read(buf)) != -1) {  
			            baos.write(buf, 0, len);  
			        }  
			        // 将数据转换为字符串保存   
			        String inputContent = new String(baos.toByteArray());  
			        // 数据接收完毕退出   
			        inStream.close();  
			       
				return inputContent;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally
		{
			 // 获取请求返回的状态码   
	        int statusCode =httpResponse.getStatusLine().getStatusCode();  
			
			if (httpClient != null && httpClient.getConnectionManager() != null)
			{
				httpClient.getConnectionManager().shutdown();
			}
		}
		return "文件上传失败";
	}

	 
}
