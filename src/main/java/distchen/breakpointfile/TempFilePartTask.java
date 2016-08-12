/**
 * Company: Dist
 * Date：2016/8/12
 * Author: ChenYanping
 * Desc：
 */
package distchen.breakpointfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CyclicBarrier;

public class TempFilePartTask extends Thread{

    private CyclicBarrier barrier;
    private TempFilePart filePart;
    private String filePath;

    public TempFilePartTask(CyclicBarrier barrier,TempFilePart filePart){
        this.barrier = barrier;
        this.filePart = filePart;
        this.filePath = this.filePart.getPath()+ File.separator+this.filePart.getFileName()+".tmp"+this.filePart.getSeq();
    }

    @Override
    public void run(){
        try {
            FileInputStream ins = new FileInputStream(this.filePath);
            long existSize = ins.available();
            ins.close();
            long targetSize = this.filePart.getEndPos()-this.filePart.getStartPos()+1;
            if (existSize == targetSize){
                System.out.printf("线程%s跳过下载:%s-%s范围的数据文件已存在\n",this.getName(),this.filePart.getStartPos(),this.filePart.getEndPos());
                this.barrier.await();
            }else{
                this.continueDownload(this.filePart.getStartPos()+existSize);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void continueDownload(long existSize){
        System.out.printf("线程%s继续下载:已下载%s字节,继续下载范围%s-%s\n",this.getName(),existSize - this.filePart.getStartPos() ,existSize,this.filePart.getEndPos());
        FileOutputStream output=null;
        try{
            URL url = new URL(this.filePart.getUrl());
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestProperty("RANGE",String.format("bytes=%d-%d",existSize,this.filePart.getEndPos()));
            InputStream input = httpConnection.getInputStream();
            output = new FileOutputStream(this.filePath,true);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len=input.read(buffer))>0){
                output.write(buffer, 0, len);
            }
        } catch(Exception e){
            e.printStackTrace ();
        }finally {
            try {
                output.flush();
                output.close();
                this.barrier.await();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
