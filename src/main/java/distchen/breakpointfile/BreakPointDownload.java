/**
 * Company: Dist
 * Date：2016/8/11
 * Author: ChenYanping
 * Desc：
 */
package distchen.breakpointfile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BreakPointDownload {

    private String url;
    private String output;
    private String fileName;
    private String filePath;
    private TempFile file;
    public static long MAXBLOCK = 10*1024*1024; // 10M

    public BreakPointDownload(String url, String output){
        this.url = url;
        this.output = output;
        this.fileName = this.url.substring(this.url.lastIndexOf("/")+1);
        this.filePath = this.output+File.separator+this.fileName;
    }

    public void startDownload(){
        File file = new File(this.filePath+".tmp.info");
        if (!file.exists()){
            long size = this.getFileSize();
            if (size > 0){
                List<TempFilePart> parts = new ArrayList<>();
                if (size > MAXBLOCK){
                    int block = (int)(size / MAXBLOCK);
                    for (int i=0;i < block;i++){
                        parts.add(new TempFilePart(this.url,this.output,this.fileName,i+1,i*MAXBLOCK,(i+1)*MAXBLOCK-1));
                    }
                    parts.add(new TempFilePart(this.url,this.output,this.fileName,block+1,block*MAXBLOCK,size-1));
                }else{
                    parts.add(new TempFilePart(this.url,this.output,this.fileName,1,0,size-1));
                }
                this.file = new TempFile(this.url,this.output,this.fileName,size,parts);
                this.generateTempFile();
            }else{
                System.err.println("根据 URL 不能找到此文件");
                return;
            }
        }else{
            restoreTempFile();
        }
        if (this.file!=null){
            this.file.continueDownLoad();
        }
    }

    private void generateTempFile(){
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(this.filePath+".tmp.info"));
            output.writeObject(this.file);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreTempFile(){
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(this.filePath+".tmp.info"));
            this.file = (TempFile ) input.readObject();
            input.close();
        } catch (IOException e) {
            System.err.println("未找到临时文件");
        }catch (ClassNotFoundException e) {
            System.err.println("临时文件已损坏，未能序列化");
        }
    }

    private long getFileSize(){
        long length = -1;
        try{
            URL url = new URL(this.url);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            if(httpConnection.getResponseCode() >= 400) {
                length = -2;
            }else{
                length = Long.parseLong(httpConnection.getHeaderField("Content-Length"));
            }
        } catch(Exception e){
            e.printStackTrace ();
        }
        return length;
    }
}
