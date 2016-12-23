package net.agusharyanto.objectcollector;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class UploadImageAsyncInvokeURLTask extends AsyncTask<Void, Integer, String> {
    public String mNoteItWebUrl = "your-url.com";
    
    private OnPostExecuteListener        mPostExecuteListener = null;
    private int seq = 0;
   // JSONObject data;
    private ProgressDialog dialog;
   public HashMap<String, Photo> hashphotopath = new HashMap<String, Photo>();
	public Context context;
    public static interface OnPostExecuteListener{    
        void onPostExecute(String result);
    }
 
    public UploadImageAsyncInvokeURLTask(Context context,
										 OnPostExecuteListener postExecuteListener) throws Exception {
    	this.context = context;
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setMessage("Uploading photo, please wait.");
		dialog.setMax(100);
		dialog.setCancelable(true);
        mPostExecuteListener = postExecuteListener;
        if (mPostExecuteListener == null)
            throw new Exception("Param cannot be null.");
        
    }
	@Override
	protected void onPreExecute() {
		this.dialog.show();
	}
	
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		dialog.setProgress(progress[0]);
		dialog.setMessage("Uploading photo " + seq + ", please wait.");
	}

	@Override
    protected String doInBackground(Void... params) {
 
        String result = "";
        
        String surl=AppConfig.URL_SERVER+mNoteItWebUrl;
    	for (int i = 0; i < hashphotopath.size(); i++) {
			seq++;
			// dialog.setMessage("Uploading photo "+(i+1)+", please wait.");
			String savedImagePath = (String) hashphotopath.get(i + "").fullpathfile;
			if (savedImagePath.equals("")){
				continue;
			}

			//Log.d("xxx****", "filename:" + savedImagePath);
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			DataInputStream inStream = null;

			String exsistingFileName = savedImagePath;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";

			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1024 * 1024;

			String urlString = AppConfig.URL_SERVER+ "/upload.php";
			// float currentRating = ratingbar.getRating();

			File file = new File(savedImagePath);
			int sentBytes = 0;
			long fileSize = file.length();

			// Log.d("***aaaa****","filename:"+savedImagePath+" length:"+fileSize+" url:"+urlString);
			try {
				// ------------------ CLIENT REQUEST

				// open a URL connection to the Servlet
				URL url = new URL(urlString);
				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				// Allow Inputs
				conn.setDoInput(true);
				// Allow Outputs
				conn.setDoOutput(true);
				// Don't use a cached copy.
				conn.setUseCaches(false);
				// Use a post method.
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
						+ exsistingFileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				FileInputStream fileInputStream = new FileInputStream(
						new File(exsistingFileName));
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				publishProgress(0);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);

					// Update progress dialog
					sentBytes += bufferSize;
					publishProgress((int) (sentBytes * 100 / fileSize));

					bytesAvailable = fileInputStream.available();
					bufferSize = Math
							.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0,
							bufferSize);
				}

				// send multipart form data necesssary after file
				// data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens
						+ lineEnd);
				dos.flush();
				dos.close();
				fileInputStream.close();
			} catch (MalformedURLException e) {
				result = "timeout";
			} catch (IOException e) {
				result = "timeout";
			}

			// ------------------ read the SERVER RESPONSE
			try {
				inStream = new DataInputStream(conn.getInputStream());
			    result = convertStreamToString(inStream);

			} catch (Exception e) {

			}
    	}
        return result;
    }
 
    @Override
    protected void onPostExecute(String result) {
        if (mPostExecuteListener != null){
            try {
                //JSONObject json = new JSONObject(result);
            	this.dialog.dismiss();
                mPostExecuteListener.onPostExecute(result);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private static String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
 
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
 
} // AsyncInvokeURLTask