/**
 * Company: Dist
 * Date：2016/8/11
 * Author: ChenYanping
 * Desc：
 */
package distchen.breakpointfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

public class TempFilePart implements Serializable{

    private String url;
    private String path;
    private String fileName;
    private int seq;
    private long startPos;
    private long endPos;

    public TempFilePart(String url,String path,String fileName,int seq,long startPos,long endPos){
        this.url = url;
        this.path = path;
        this.fileName = fileName;
        this.seq =seq;
        this.startPos=startPos;
        this.endPos=endPos;
        this.generateFile();
    }

    private void generateFile(){
        try {
            FileOutputStream output = new FileOutputStream(this.path+ File.separator+fileName+".tmp"+seq);
            output.flush();
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
