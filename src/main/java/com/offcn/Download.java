package com.offcn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.apache.http.client.methods.CloseableHttpResponse;

public class Download {

	public static void down(String url) throws ClientProtocolException, IOException {

		CloseableHttpClient chc = HttpClients.createDefault();

		HttpGet hg = new HttpGet(url);

		HttpResponse hr = chc.execute(hg);

		HttpEntity entiy = hr.getEntity();

		InputStream is = entiy.getContent();

		File file = new File("E:\\aaa" + url);

		FileOutputStream fos = new FileOutputStream(file);

		int n = -1;

		byte[] bytes = new byte[1024];

		while ((n = is.read(bytes)) != -1) {
			fos.write(bytes, 0, n);
		}

		fos.close();

		is.close();

	}

	public static List<String> getImgUrl(String url) throws IOException {

		List<String> list = new ArrayList<String>();

		Document doc = Jsoup.connect(url).get();

		Element ele = doc.select(".bigUl").first();

		Elements img = ele.select("img");

		for (Element el : img) {
			list.add(el.attr("src"));
		}

		return list;
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		List list = getImgUrl("http://huaban.com/explore/rabbit/");

		for (int i = 0; i < list.size(); i++) {
			down(list.get(i).toString());
		}
	}

	@Test
	public void pachong() {
		// 请求的url(前面加http：// 不然会报错)
		String url = "http://huaban.com/explore/rabbit/";
		// 1.创建HttpClient对象 这里使用默认的配置的httpclient
		CloseableHttpClient client = HttpClients.createDefault();
		// 2.创建某种请求方法的实例。这里使用get方法
		HttpGet httpGet = new HttpGet(url);
		InputStream inputStream = null;
		CloseableHttpResponse response = null;
		try {
			// 3.执行请求，获取响应
			response = client.execute(httpGet);
			// 看请求是否成功，这儿打印的是http状态码
			System.out.println(response.getStatusLine().getStatusCode());
			// 4.获取响应的实体内容，就是我们所要抓取得网页内容
			HttpEntity entity = response.getEntity();
			// 5.将其打印到控制台上面，这里使用EntityUtils（也可以用inputStream）
			if (entity != null) {
				System.out.println(EntityUtils.toString(entity, "utf-8"));
			}
			EntityUtils.consume(entity);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 6.关闭连接，释放资源（很重要）
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
