/**
 * Company: Dist
 * Date：2016/8/11
 * Author: ChenYanping
 * Desc：
 */
package distchen.breakpointfile;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class TempFile implements Serializable{
    private String url;
    private String path;
    private String fileName;
    private String mime;
    private long size;
    private List<TempFilePart> parts;

    public TempFile(String url,String path,String fileName,long size,List parts){
        this.url = url;
        this.path = path;
        this.fileName = fileName;
        this.size = size;
        this.parts = parts;
        this.mime = this.fileName.substring(this.fileName.lastIndexOf("/")+1);
    }

    public void continueDownLoad(){
        CyclicBarrier barrier = new CyclicBarrier(this.parts.size(),new TempFileTask(this));
        System.out.printf("----------开始下载文件，文件被切分成%s段并行下载----------\n",this.parts.size());
        this.parts.stream().forEach(filePart-> new TempFilePartTask(barrier,filePart).start());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TempFilePart> getParts() {
        return parts;
    }

    public void setParts(List<TempFilePart> parts) {
        this.parts = parts;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
