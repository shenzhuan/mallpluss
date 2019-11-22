package com.mei.zhuang.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.CodingErrorAction;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * HttpClient工具类
 * @author 唐鑫炯
 *
 */
@Slf4j
public class HttpClientUtil {

	//连接池最大连接数
	private final static Integer MAX_TOTAL=500;

	//路由最大连接数
	private final static Integer MAX_PER_ROUTE=500;

	//连接建立后数据传输超时时长
	private final static Integer socketTimeout = 3000000;

	// 建立连接超时时长
	private final static Integer connectTimeout = 3000000;

	//建立请求超时时长
	private final static Integer connectionRequestTimeout = 3000000;

	private final static String DEFAULT_CHARSET="UTF-8";

	//连接管理器
	private static PoolingHttpClientConnectionManager connManager = null;

	public static final String success_key="success";
	public static final String success_value="true";
	public static final String returnStr="returnStr";
	public static final String returnStr_value="NULL";

	//静态初始化
	static{
		//创建http,https构建器
		RegistryBuilder<ConnectionSocketFactory> registryBuilder=RegistryBuilder.<ConnectionSocketFactory>create();
		//注册hhtp协议
		registryBuilder.register("http", new PlainConnectionSocketFactory());

		try {
			//创建SSL连接工厂，并将自定义的SSLContext和允许任何主机名通过校验的指令传入
			LayeredConnectionSocketFactory sslSf=new SSLConnectionSocketFactory(SSLContext.getDefault());
			//将SSL自定义工厂注册到hhtps协义上
			registryBuilder.register("https", sslSf);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
		}

		//创建注册器
		Registry<ConnectionSocketFactory> registry=registryBuilder.build();

		//创建连接管理器
		connManager=new PoolingHttpClientConnectionManager(registry);
		//创建连接管理器配置
		ConnectionConfig connectionConfig=ConnectionConfig.custom()
				.setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE)
				.setCharset(Consts.UTF_8)
				.build();
		connManager.setDefaultConnectionConfig(connectionConfig);
		//设置连接池最大连接数
		connManager.setMaxTotal(MAX_TOTAL);
		//设置单个路由最大的连接线程数量
		connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
	}

	/**
	 * 获取HttpClient
	 * @return
	 */
	private static CloseableHttpClient getCloseableHttpClient(){
		//上下文
		HttpClientContext httpClientContext=HttpClientContext.create();
		//会话
		CookieStore cookieStore=new BasicCookieStore();
		httpClientContext.setCookieStore(cookieStore);

		//请求配置
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();

		return HttpClients.custom().setConnectionManager(connManager)
				.setDefaultRequestConfig(globalConfig)
				.setDefaultCookieStore(cookieStore).build();
	}

	/**
	 * URL带参数，需要编码后传入
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String get(String url,String charset){
		return get(url,null,charset);
	}

	/**
	 * GET请求，包含请求参数
	 * @param url
	 * @param paramsMap
	 * @param charset
	 * @return
	 */
	public static String get(String url,Map<String,String> paramsMap,String charset){

		log.info("url:"+url);
		log.info("param:"+paramsMap);
		log.info("charset:"+charset);

		//访问地址不能为空
		if(StringUtils.isEmpty(url)){
			return null;
		}

		//将请求参数封装成List<NameValuePair>
		List<NameValuePair> paramslist=getParamsList(paramsMap);

		//参数不为空
		if(!CollectionUtils.isEmpty(paramslist)){
			url=url+"?"+URLEncodedUtils.format(paramslist, getCharset(charset));
		}
		return excute(new HttpGet(url),charset);
	}


	/**
	 * http 请求，包含请求参数和字符集
	 * @param url
	 * @param paramsMap
	 * @param charset
	 * @return
	 */
	public static String sendRequest(String url,Map<String,String> paramsMap,String charset,String mothod){
		if("get".equalsIgnoreCase(mothod)){
			return get(url, paramsMap, charset);
		}else if("post".equalsIgnoreCase(mothod)){
			return post(url, paramsMap, charset);
		}
		return null;
	}
	public static String sendRequest(String url,Map<String,String> paramsMap,String charset,String mothod,String sendFormat){
		if("json".equals(sendFormat)){
			return postJSON(url, JSON.toJSONString(paramsMap), charset);
		}else{
			return sendRequest(url, paramsMap, charset, mothod);
		}
	}

	/**
	 * POST请求，包含请求参数和字符集
	 * @param url
	 * @param paramsMap
	 * @param charset
	 * @return
	 */
	public static String post(String url,Map<String,String> paramsMap,String charset){

		log.info("url:"+url);
		log.info("param:"+paramsMap);
		log.info("charset:"+charset);

		//访问地址不能为空
		if(StringUtils.isEmpty(url)){
			return null;
		}

		//将请求参数封装成List<NameValuePair>
		List<NameValuePair> paramslist=getParamsList(paramsMap);

		//httppost请求
		HttpPost httpPost=new HttpPost(url);
		try {
			//如果参数不为空，则将参数设置到请求体中
			if(!CollectionUtils.isEmpty(paramslist)){
				httpPost.setEntity(new UrlEncodedFormEntity(paramslist,getCharset(charset)));
			}
		} catch (UnsupportedEncodingException e) {
			log.error("UrlEncodedFormEntity转换字符异常",e);
			return null;
		}
		return excute(httpPost,charset);
	}

	/**
	 * POST请求，包含请求参数和字符集
	 * @param url
	 * @param paramList 支持更丰富的参数表达，如数组
	 * @param charset
	 * @return
	 */
	public static String post(String url,List<NameValuePair> paramList,String charset){

		log.info("url:"+url);
		log.info("param:{}",paramList);
		log.info("charset:"+charset);

		//访问地址不能为空
		if(StringUtils.isEmpty(url)){
			return null;
		}

		//httppost请求
		HttpPost httpPost=new HttpPost(url);
		try {
			//如果参数不为空，则将参数设置到请求体中
			if(!CollectionUtils.isEmpty(paramList)){
				httpPost.setEntity(new UrlEncodedFormEntity(paramList,getCharset(charset)));
			}
		} catch (UnsupportedEncodingException e) {
			log.error("UrlEncodedFormEntity转换字符异常",e);
			return null;
		}
		return excute(httpPost,charset);
	}

	/**
	 * 只接收String类型的参数请求，并是POST的请求
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String post(String url,String params,String charset){
		//如果url为空则返回
		if (StringUtils.isEmpty(url)) {
			log.error("url不能为空");
			return null;
		}
		//httppost请求
		HttpPost httpPost=new HttpPost(url);
		httpPost.setEntity(new StringEntity(params,getCharset(charset)));

		return excute(httpPost,charset);
	}
	public static String postJSON(String url,String jsonString,String charset){

		log.info("url:"+url);
		log.info("json param:"+jsonString);
		log.info("charset:"+charset);

		if (StringUtils.isEmpty(url)) {
			log.error("url不能为空");
			return null;
		}
		HttpPost httpPost=new HttpPost(url);
		httpPost.setEntity(new StringEntity(jsonString,ContentType.APPLICATION_JSON));
		return excute(httpPost,charset);
	}

	public static String postFile(String url,String jsonString,String charset){
		if (StringUtils.isEmpty(url)) {
			log.error("url不能为空");
			return null;
		}
		HttpPost httpPost=new HttpPost(url);
		httpPost.setEntity(new StringEntity(jsonString,ContentType.APPLICATION_JSON));
		return excute(httpPost,charset);
	}

	/**
	 * 通用返回类型为String的执行
	 * @param request
	 * @param charset
	 * @return
	 */
	private static String excute(HttpRequestBase request,String charset){
		//请求对象
		CloseableHttpClient httpClient=getCloseableHttpClient();
		request.setConfig(getRequestConfig());
		//响 应对象
		CloseableHttpResponse httpResponse = null;
		//返回结果
		String responseStr=null;
		try {
			//调用远程接口
			httpResponse=httpClient.execute(request);
			//查看返回的状态码是否调用成功
			if(httpResponse.getStatusLine().getStatusCode()!=HttpStatus.SC_OK){
				log.error("接口调用失败，状态码："+httpResponse.getStatusLine().getStatusCode());
				return null;
			}
			//获取返回的结果数据(String类型)
			responseStr=EntityUtils.toString(httpResponse.getEntity(),getCharset(charset));
		} catch (ClientProtocolException e) {
			log.error("客户端连接异常：",e);
			return null;
		} catch (IOException e) {
			log.error("IO异常：",e);
			return null;
		}finally{
			close(httpResponse);
		}
		log.info("响应："+responseStr);
		return responseStr;
	}

	/**
	 * 关闭资源
	 * @param httpResponse
	 */
	private static void close(CloseableHttpResponse httpResponse){
		if(httpResponse!=null){
			try {
				httpResponse.close();
			} catch (IOException e) {
				log.error("关闭CloseableHttpResponse异常",e);
			}
		}

	}

	/**
	 * 获取请求配置
	 * @return
	 */
	private static RequestConfig getRequestConfig(){
		//请求配置信息
		return RequestConfig.custom()
				.setConnectionRequestTimeout(connectionRequestTimeout)
				.setConnectTimeout(connectTimeout)
				.setSocketTimeout(socketTimeout).build();
	}

	/**
	 * 将请求参数封装成List<NameValuePair>
	 * @param paramsMap
	 * @return
	 */
	private static List<NameValuePair> getParamsList(Map<String,String> paramsMap){
		List<NameValuePair> paramsList = null;
		if (!CollectionUtils.isEmpty(paramsMap)) {
			paramsList = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		return paramsList;
	}

	/**
	 * 获取字符集
	 * @param charset
	 * @return
	 */
	private static String getCharset(String charset){
		if(StringUtils.isEmpty(charset)) return DEFAULT_CHARSET;
		return charset;
	}




	/**
	 * @author Martin
	 * @param request
	 * @param charset
	 * @return
	 */
	private  static <T> T  excute(HttpRequestBase request,String charset,Class<T> responseType){
		//请求对象
		CloseableHttpClient httpClient=getCloseableHttpClient();
		request.setConfig(getRequestConfig());
		//响 应对象
		CloseableHttpResponse httpResponse = null;
		//返回结果
		T response=null;
		try {
			//调用远程接口
			httpResponse=httpClient.execute(request);
			//查看返回的状态码是否调用成功
			if(httpResponse.getStatusLine().getStatusCode()!=HttpStatus.SC_OK){
				log.error("接口调用失败，状态码："+httpResponse.getStatusLine().getStatusCode());
				return null;
			}

			if(responseType==String.class){
				response=(T)EntityUtils.toString(httpResponse.getEntity(),getCharset(charset));
			}else if(responseType==byte[].class){
				response=(T)EntityUtils.toByteArray(httpResponse.getEntity());
			}
		} catch (ClientProtocolException e) {
			log.error("客户端连接异常：",e);
			return null;
		} catch (IOException e) {
			log.error("IO异常：",e);
			return null;
		}finally{
			close(httpResponse);
		}
		return response;
	}

	/**
	 * @author Martin
	 * POST 文件下载
	 * @param url
	 * @param param
	 * @param charset
	 * @return
	 */
	public static byte[] postForDownload(String url,Map<String,String> param,String charset){
		log.info("url:"+url);
		log.info("param:"+param);
		log.info("charset:"+charset);

		if(StringUtils.isEmpty(url)){
			return null;
		}

		List<NameValuePair> paramslist=getParamsList(param);

		HttpPost httpPost=new HttpPost(url);
		try {
			if(!CollectionUtils.isEmpty(paramslist)){
				httpPost.setEntity(new UrlEncodedFormEntity(paramslist,getCharset(charset)));
			}
		} catch (UnsupportedEncodingException e) {
			log.error("UrlEncodedFormEntity转换字符异常",e);
			return null;
		}

		return excute(httpPost,charset,byte[].class);
	}

	/**
	 * @author Martin
	 * get 文件下载
	 * @param url
	 * @param charset
	 * @return
	 */
	public static byte[] getForDownload(String url,String charset){
		log.info("url:"+url);
		log.info("charset:"+charset);

		if(StringUtils.isEmpty(url)){
			return null;
		}

		return excute(new HttpGet(url),charset,byte[].class);
	}



	public static void main(String[] args) throws IOException {
		Map<String,String> param=new HashMap<>();
		param.put("ticket","gQFX7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2xuV0lwYmpscHduZS1MYksyVmxkAAIE43MwVgMEAAAAAA==");
		param.put("originId","gh_d87d328725e");
		param.put("expire_seconds","-1");
		param.put("url","http://weixin.qq.com/q/lnWIpbjlpwne-LbK2Vld==");

		byte[] bytes=postForDownload(
				"http://120.132.27.130/weixin/wechat/qrcoder/qrCodePicture",
				param,
				"utf-8"
		);

		OutputStream outputStream=new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\qrcode.jpg"));
		outputStream.write(bytes);

		outputStream.flush();
		outputStream.close();
	}
}
