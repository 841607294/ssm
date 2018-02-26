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
		// �����url(ǰ���http��// ��Ȼ�ᱨ��)
		String url = "http://huaban.com/explore/rabbit/";
		// 1.����HttpClient���� ����ʹ��Ĭ�ϵ����õ�httpclient
		CloseableHttpClient client = HttpClients.createDefault();
		// 2.����ĳ�����󷽷���ʵ��������ʹ��get����
		HttpGet httpGet = new HttpGet(url);
		InputStream inputStream = null;
		CloseableHttpResponse response = null;
		try {
			// 3.ִ�����󣬻�ȡ��Ӧ
			response = client.execute(httpGet);
			// �������Ƿ�ɹ��������ӡ����http״̬��
			System.out.println(response.getStatusLine().getStatusCode());
			// 4.��ȡ��Ӧ��ʵ�����ݣ�����������Ҫץȡ����ҳ����
			HttpEntity entity = response.getEntity();
			// 5.�����ӡ������̨���棬����ʹ��EntityUtils��Ҳ������inputStream��
			if (entity != null) {
				System.out.println(EntityUtils.toString(entity, "utf-8"));
			}
			EntityUtils.consume(entity);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 6.�ر����ӣ��ͷ���Դ������Ҫ��
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
