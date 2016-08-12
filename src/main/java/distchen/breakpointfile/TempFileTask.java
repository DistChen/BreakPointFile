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

public class TempFileTask extends Thread{

    private TempFile tempFile;
    private String filePath;

    public TempFileTask(TempFile tempFile){
        this.tempFile = tempFile;
        this.filePath = this.tempFile.getPath()+File.separator+this.tempFile.getFileName();
    }

    @Override
    public void run(){
        System.out.println("----------所有线程已完成，开始整合数据段文件----------");
        try {
            FileOutputStream output = new FileOutputStream(this.filePath);
            FileInputStream ins;
            byte[] buffer = new byte[1024];
            String filePartName;
            for (int i=0;i<this.tempFile.getParts().size();i++){
                filePartName = this.filePath+".tmp"+(i+1);
                System.out.println("处理数据:"+filePartName);
                ins = new FileInputStream(filePartName);
                int len = 0;
                while((len=ins.read(buffer))>0){
                    output.write(buffer, 0, len);
                }
            }
            output.flush();
            output.close();
            System.out.println("-----------------文件下载完毕-----------------");
            System.out.println("文件下载地址:"+this.filePath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
