package com.framework.leopardus.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.framework.leopardus.enums.RESTHeaders;
import com.framework.leopardus.interfaces.RESTCallback;
import com.framework.leopardus.utils.storage.RESTInternalStorage;

public class RESTSimpleTool {

	private String user;
	private String host;
	private String passwd;
	private HttpClient httpClient;
 	private boolean logged = false;
	private boolean requirelogin = false;
	HttpContext localContext = new BasicHttpContext();
	private static Map<String, RESTSimpleTool> instances = new HashMap<String, RESTSimpleTool>();
	private static RESTInternalStorage rest_is;
	
	public static boolean INFO = false;

	private RESTSimpleTool(String host) {
		this(host, null, null);
	}

	public void enableInternalStorage(Context context) {
		if (rest_is == null) {
			rest_is = new RESTInternalStorage(context);
		}
	}

	public void store(String url, int status, String content) {
		if (rest_is != null) {
			try {
				if (status >= 200 && status < 300) {
					int r = rest_is.updateContent(url, content);
					if (r == -1) {
						Log.e("Leopardus",
								"RESTSimpleTool(store) error: When save or update "
										+ url);
					}
				}
				// TODO: implement delete when status require it
			} catch (Exception e) {
				Log.e("Leopardus",
						"RESTSimpleTool(store) error: " + e.getMessage());
			}
		}
	}

	private RESTSimpleTool(String host, String user, String passwd) {
		httpClient = new DefaultHttpClient();
		this.host = host;
		requirelogin = user != null && passwd != null;
		this.user = user;
		this.passwd = passwd;
	}

	public static RESTSimpleTool getInstance(String host, String user,
			String passwd) {
		if (!host.endsWith("/")) {
			host += "/";
		}
		if (!host.startsWith("http://") && !host.startsWith("https://")) {
			host = "http://" + host;
		}
		if (!instances.containsKey(host)) {
			RESTSimpleTool rst = null;
			if (user != null && passwd != null) {
				rst = new RESTSimpleTool(host, user, passwd);
			} else {
				rst = new RESTSimpleTool(host);
			}
			instances.put(host, rst);
		}
		RESTSimpleTool instance = instances.get(host);
		instance.request(RESTHeaders.GET, "", new RESTCallback(instance) {
			
			@Override
			public void onFinish(int status, String section, HttpResponse resp) {
				this.getRESTSimpleToolInstance().requirelogin = false;
			}
		});
		return instance;
	}

	public String requestFromCache(String url) {
		if (rest_is != null) {
			return rest_is.getContent(url);
		}
		return null;
	}

	public void request(RESTHeaders protocol, String url, RESTCallback callback) {
		request(protocol, url, null, callback);
	}

	public void request(final RESTHeaders protocol, String apiSection,
			final Map<String, Object> args, final RESTCallback callback) {
		if (apiSection.startsWith("/")) {
			apiSection = apiSection.substring(1);
		}
		if (apiSection == null) {
			apiSection = "";
		}
		if (!apiSection.endsWith("/") && !apiSection.equals("")) {
			apiSection += "/";
		}
		if (callback != null) {
			callback.setRequestUrl(host + apiSection);
		}
		final String APISection = apiSection;
		Runnable r = new Runnable() {
			public void run() {
				if (protocol.equals(RESTHeaders.GET)) {
					onGet(APISection, null, callback);
				} else if (protocol.equals(RESTHeaders.POST)) {
					onPost(APISection, args, callback);
				} else if (protocol.equals(RESTHeaders.PUT)) {
					onPut(APISection, args, callback);
				} else if (protocol.equals(RESTHeaders.DELETE)) {
					onDelete(APISection, null, callback);
				}
			}
		};
		Thread hilo = new Thread(r);
		// hilo.run();
		hilo.start();
	}

	private void onGet(String apiSection, Map<String, Object> args,
			RESTCallback callback) {
		HttpGet get = new HttpGet(host + apiSection);
		get.setHeader("Content-Type", "application/json");
		if (requirelogin) {
			get.addHeader("Authorization", getLoginHeader());
		}
		try {
			HttpResponse resp;
			resp = httpClient.execute(get,localContext);
			if (callback != null) {
				// TODO:
				callback.onFinish(resp.getStatusLine().getStatusCode(),
						apiSection, resp);
			}
		} catch (ClientProtocolException e) {
			if (callback != null) {
				callback.onClientProtocolException(e);
			}
		} catch (IOException e) {
			if (callback != null) {
				callback.onIOException(e);
			}
		}
	}

	private void onPost(String apiSection, Map<String, Object> args,
			RESTCallback callback) {
		HttpPost post = new HttpPost(host + apiSection);
		post.setHeader("content-type", "application/json");
		if (requirelogin) {
			post.addHeader("Authorization", getLoginHeader());
		}
		JSONObject dato = new JSONObject();
		if (args != null) {
			for (String llave : args.keySet()) {
				try {
					dato.put(llave, args.get(llave));
				} catch (JSONException e) {
					throw new RuntimeException(
							"Leopardus(RestSimpleTool): Error adding argument "
									+ llave + ":"
									+ String.valueOf(args.get(llave))
									+ " to request: " + e.getMessage());
				}
			}
		}
		try {
			StringEntity entity = new StringEntity(dato.toString());
			post.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"Leopardus(RestSimpleTool): Error with argument coding: "
							+ e.getMessage());
		}
		try {
			HttpResponse resp;
			resp = httpClient.execute(post,localContext);
			if (callback != null) {
				// TODO: Evaluate action of internal storage
				// store(host+apiSection, resp.getStatusLine().getStatusCode(),
				// resp);
				callback.onFinish(resp.getStatusLine().getStatusCode(),
						apiSection, resp);
			}
		} catch (ClientProtocolException e) {
			if (callback != null) {
				callback.onClientProtocolException(e);
			}
		} catch (IOException e) {
			if (callback != null) {
				callback.onIOException(e);
			}
		}
	}

	private void onPut(String apiSection, Map<String, Object> args,
			RESTCallback callback) {
		// HttpClient httpClient= new DefaultHttpClient();
		HttpPut put = new HttpPut(host + apiSection);
		put.setHeader("content-type", "application/json");
		if (requirelogin) {
			put.addHeader("Authorization", getLoginHeader());
		}
		JSONObject dato = new JSONObject();
		if (args != null) {
			for (String llave : args.keySet()) {
				try {
					dato.put(llave, args.get(llave));
				} catch (JSONException e) {
					throw new RuntimeException(
							"Leopardus(RestSimpleTool): Error adding argument "
									+ llave + ":"
									+ String.valueOf(args.get(llave))
									+ " to request: " + e.getMessage());
				}
			}
		}
		try {
			StringEntity entity = new StringEntity(dato.toString());
			put.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"Leopardus(RestSimpleTool): Error with argument coding: "
							+ e.getMessage());
		}
		try {
			HttpResponse resp;
			resp = httpClient.execute(put,localContext);
			if (callback != null) {
				// TODO: Evaluate action of internal storage
				// store(host+apiSection, resp.getStatusLine().getStatusCode(),
				// resp);
				callback.onFinish(resp.getStatusLine().getStatusCode(),
						apiSection, resp);
			}
		} catch (ClientProtocolException e) {
			if (callback != null) {
				callback.onClientProtocolException(e);
			}
		} catch (IOException e) {
			if (callback != null) {
				callback.onIOException(e);
			}
		}
	}

	private void onDelete(String apiSection, Map<String, Object> args,
			RESTCallback callback) {
		HttpDelete del = new HttpDelete(host + apiSection);
		del.setHeader("Content-Type", "application/json");
		if (requirelogin) {
			del.addHeader("Authorization", getLoginHeader());
		}
		try {
			HttpResponse resp;
			resp = httpClient.execute(del,localContext);
			if (callback != null) {
				// TODO: Evaluate action of internal storage
				// store(host+apiSection, resp.getStatusLine().getStatusCode(),
				// resp);
				callback.onFinish(resp.getStatusLine().getStatusCode(),
						apiSection, resp);
			}
		} catch (ClientProtocolException e) {
			if (callback != null) {
				callback.onClientProtocolException(e);
			}
		} catch (IOException e) {
			if (callback != null) {
				callback.onIOException(e);
			}
		}
	}

	public boolean isLogged() {
		return logged;
	}

	private String getLoginHeader() {
		return "Basic "
				+ Base64.encodeToString((user + ":" + passwd).getBytes(),
						Base64.NO_WRAP);
	}

	@Override
	public String toString() {
		return host;
	}

}
