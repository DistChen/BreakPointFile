/**
 * Company: Dist
 * Date：2016/8/11
 * Author: ChenYanping
 * Desc：
 */
package distchen.breakpointfile;

public class App {

    public static void main(String [] args){
        String source = "http://*****/demo.doc";
        /**
         *
         */
        new BreakPointDownload(source,"D:/BreakPointFile").download();
    }
}
