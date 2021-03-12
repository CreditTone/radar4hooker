package gz.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import gz.radar.Android;

public class XLog {

	private static volatile String APPEND_TEXT_FILE = null;

	static {
        try {
            APPEND_TEXT_FILE = "/data/user/0/"+ Android.getApplication().getPackageName() + "/xinit/xinit.log";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void appendText(final String content) {
	    if (APPEND_TEXT_FILE == null) {
	        return;
        }
	    Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FileWriter writer = null;
                try {
                    File parentFile = new File(APPEND_TEXT_FILE).getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdir();
                    }
                    String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String newcontent = android.os.Process.myPid() + "_" + datetime  +">>>" + content + "\n";
                    // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                    writer = new FileWriter(APPEND_TEXT_FILE, true);
                    writer.write(newcontent);
                } catch (IOException e) {
                    System.out.println("e = " + e);
                }finally {
                    if (writer != null) {
                        try {
                            writer.flush();
                            writer.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        };
        if (Thread.currentThread().getId() <= 2) {
            new Thread(runnable).start();
        }else{
            runnable.run();
        }
    }

    public static void appendText(Exception ex) {
        appendText(getException(ex));
    }

    public static String getException(Exception e) {
        Writer writer = null;
        PrintWriter printWriter = null;
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            return writer.toString();
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (printWriter != null)
                    printWriter.close();
            } catch (IOException e1) { }
        }
    }
    
    public static String getPrettyHtml(String str) {
    	StringBuilder builder = new StringBuilder();
    	builder.append("<html>");
    	builder.append("<body>");
    	if (str != null) {
    		String[] lines = str.split("\n");
    		for (int i = 0; i < lines.length; i++) {
    			builder.append("<p>");
    			builder.append(lines[i]);
    			builder.append("</p>");
    			builder.append("</br>");
			}
    	}
    	builder.append("</body>");
    	builder.append("</html>");
    	return builder.toString();
    }
    
}
