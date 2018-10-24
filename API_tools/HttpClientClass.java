/*
 * The MIT License
 *
 * Copyright 2018 mw2.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package API_tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author mw2
 */
public class HttpClientClass extends Toolkit.ReadAndProcess {

private int statusCode;
private long entityLength;
private Header contentType;
private String protocolVersion;
private String reasonPhrase;
private Header[] allHeaders;
private static final Logger logger = Logger.getLogger(HttpClientClass.class.getCanonicalName());
private String url;
private CloseableHttpClient httpClient;
private HttpClientContext context;
private String encoding = "utf-8";
CloseableHttpResponse response;
    
public HttpClientClass(String url, String username, String password) {
this.url = url;
try
  {
  context = HttpClientContext.create();
  CredentialsProvider credsProvider = new BasicCredentialsProvider();
  credsProvider.setCredentials(new AuthScope(null, -1),
  new UsernamePasswordCredentials(username, password));
  context.setCredentialsProvider(credsProvider);
  httpClient = HttpClients.createDefault();
  }
catch (Exception ioe)
  {
  close();
  logger.log(Level.SEVERE,"Fail to init Client",ioe);
  throw new RuntimeException("Fail to init Client", ioe);
  }
}

public HttpClientClass(String url) {
this.url = url;
try
  {
  httpClient = HttpClients.createDefault();
  context = null;
  }
catch (Exception ioe)
  {
  close();
  logger.log(Level.SEVERE,"Fail to init Client",ioe);
  throw new RuntimeException("Fail to init Client", ioe);
  }
}

public  void close() {
if (this.httpClient != null)
  {
  try
    {
    httpClient.close();
    }
  catch (Exception ioe)
    {
    close();
    logger.log(Level.SEVERE,"Fail to init Client",ioe);
    throw new RuntimeException("Fail to init Client", ioe);
    }
  }
}
    
@Override
public void readProcess(int startnum) {
//
}

public StringBuilder getUrl(String urlSufix, HashMap<String,String> headers) {
sbTx = new StringBuilder();
String getUrl = url+urlSufix;
//System.out.println(getUrl);
HttpGet httpget = new HttpGet(getUrl);
for (String h : headers.keySet())
  {
  httpget.addHeader(h, headers.get(h));
  }
allHeaders = httpget.getAllHeaders();
//printHeaders(allHeaders);
try
  {
  if (context ==null) {response = httpClient.execute(httpget);}
  else {response = httpClient.execute(httpget, context);}
  //System.out.println(response);
  getResponse();
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error: " +ex, "Error", JOptionPane.ERROR_MESSAGE);}
return sbTx;
}

public StringBuilder postParams(String urlSuffix, HashMap<String,String> headers, HashMap<String,String> params) throws UnsupportedEncodingException {
sbTx = new StringBuilder();
HttpPost httppost = new HttpPost(url+urlSuffix);
for (String h : headers.keySet())
  {
  httppost.addHeader(h, headers.get(h));
  }
allHeaders = httppost.getAllHeaders();
StringBuilder stEntity= new StringBuilder();
for (String p: params.keySet())
  {
  if (!p.equals("N/A")) {stEntity.append(p).append("=");}
  stEntity.append(params.get(p)).append("\n");
  }
httppost.setEntity(new StringEntity(stEntity.toString()));

printHeaders(allHeaders);

try
  {
  if (context ==null) {response = httpClient.execute(httppost);}
  else {response = httpClient.execute(httppost, context);}
  getResponse();
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error: " +ex, "Error", JOptionPane.ERROR_MESSAGE);}

return sbTx;
}

public StringBuilder postFile(String urlSuffix, HashMap<String,String> headers, String filedata, String format) throws UnsupportedEncodingException {
sbTx = new StringBuilder();
String postUrl = url+urlSuffix;
System.out.println(postUrl);
HttpPost httppost = new HttpPost(postUrl);
for (String h : headers.keySet())
  {
  System.out.println(h + " " + headers.get(h));
  httppost.addHeader(h, headers.get(h));
  }
allHeaders = httppost.getAllHeaders();
httppost.setEntity(new StringEntity(filedata, format));

printHeaders(allHeaders);

try
  {
  if (context ==null) {response = httpClient.execute(httppost);}
  else {response = httpClient.execute(httppost, context);}
  getResponse();
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error: " +ex, "Error", JOptionPane.ERROR_MESSAGE);}
return sbTx;
}

private void printHeaders(Header[] headers) {
for (int i=0; i<headers.length;i++) {
System.out.println(headers[i]);
}
}

private void getResponse () throws IOException {
statusCode = response.getStatusLine().getStatusCode();
HttpEntity entity = response.getEntity();   
  if (entity != null)
    {
    entityLength = entity.getContentLength();
    contentType = entity.getContentType();
    protocolVersion = response.getProtocolVersion().toString();
    reasonPhrase = response.getStatusLine().getReasonPhrase();
    } 
  if (statusCode == 200)
    {
    InputStream is = entity.getContent();
    getWebpage(is);
    }
  else {sbTx.append(statusCode).append(" - ").append(reasonPhrase);}
}

@Override
public void process(BufferedReader rd) {
try
  {
  readCharToStringBuilder(rd);
  }
catch (IOException e) {System.err.println("Caught IOException: " + e.getMessage());}
}

public String extractNestedJson(JSONObject json, String name, ArrayList<String> nameList, ArrayList<String> typeList, String separator) throws JSONException
{
//Extract readers sub-object
if(!json.has(name)) {return "N/A";} 
StringBuilder sb = new StringBuilder();
//Extract the sub-json object
JSONObject subJson = json.getJSONObject(name);
String nj = extractJson(subJson, nameList, typeList, separator);
return nj;
   }

private String appendDQ(String str) {
//Add double quotes to the input string
return "\"" + str + "\"";
}

public String extractJson(JSONObject json, ArrayList<String> nameList, ArrayList<String> typeList, String separator) throws JSONException {
//Create StringBuilder 
StringBuilder sb = new StringBuilder();
//Iterate over the nameList array of all the metrics to look for
for (int i=0; i < nameList.size(); i++)
  {//Get the corrosponding type and name
  String curtype = typeList.get(i);
  String curName = nameList.get(i);
  //Get string values and append them with double quotes, and add a comma
  if (json.has(curName) && curtype.equals("string")) {sb.append(appendDQ(json.getString(curName))).append(separator);}
  // If url was not found (error json recieved) append Error and a comma
  else if (json.has("Error")) {sb.append("Error"+",");}
  //Get double values and append them and a comma
  else if (json.has(curName) && curtype.equals("double")) {sb.append(json.getDouble(curName)).append(separator);}
  //Get integer values and append them and a comma
  else if (json.has(curName) && curtype.equals("int")) {sb.append(json.getInt(curName)).append(separator);}
  //Append n/a and a comma if that metric was not found. Unless it's part of the readers sub-object
  else if (!curtype.startsWith("readers")) {sb.append("n/a,");}
  }
String s = sb.toString();
s = s.replaceAll("\n", "");
return s;
}

public int getStatusCode(){
return statusCode;
}

public long getEntityLength() {
return entityLength;
}

public Header getContentType() {
return contentType;
}

public String getProtocolVersion() {
return protocolVersion;
}

public String getReasonPhrase() {
return reasonPhrase;
}

public Header[] getAllHeaders() {
return allHeaders;
}
}