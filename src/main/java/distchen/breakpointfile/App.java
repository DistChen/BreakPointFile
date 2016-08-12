/**
 * Company: Dist
 * Date：2016/8/11
 * Author: ChenYanping
 * Desc：
 */
package distchen.breakpointfile;

public class App {

    public static void main(String [] args){
        String source = "http://117.78.35.239/demo.doc";
        new BreakPointDownload(source,"D:/BreakPointFile").startDownload();
    }
}
