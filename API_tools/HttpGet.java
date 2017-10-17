/*
 * The MIT License
 *
 * Copyright 2017 mw2.
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
import javax.swing.JOptionPane;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author mw2
 */
public class HttpGet extends Toolkit.ReadAndProcess {

private int statusCode;
private long entityLength;
private Header contentType;
private String protocolVersion;
private String reasonPhrase;
private Header[] allHeaders;

@Override
public void readProcess(int startnum) {
//
}

public StringBuilder getHttpClient(String url, String[] headName, String[] headVal) {
sbTx = new StringBuilder();
CloseableHttpClient httpclient = HttpClients.createDefault();
org.apache.http.client.methods.HttpGet httpget = new org.apache.http.client.methods.HttpGet(url);
int headSize = headName.length;
if (headVal.length > headSize) {headSize = headVal.length;}
for (int i=0; i<headSize; i++)
  {
httpget.addHeader(headName[i], headVal[i]);
  }
allHeaders = httpget.getAllHeaders();
try
  {
  CloseableHttpResponse response = httpclient.execute(httpget);
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
  response.close();
  }
catch(Exception ex){JOptionPane.showMessageDialog(null, "Error: " +ex, "Error", JOptionPane.ERROR_MESSAGE);}
return sbTx;
}

@Override
public void process(BufferedReader rd) {
try
  {
  readCharToStringBuilder(rd);
  }
catch (IOException e) {System.err.println("Caught IOException: " + e.getMessage());}
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