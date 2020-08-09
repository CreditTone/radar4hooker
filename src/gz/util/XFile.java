package gz.util;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import gz.com.alibaba.fastjson.JSON;
import gz.com.alibaba.fastjson.JSONObject;
import gz.radar.Android;

public class XFile {

    public static String readXinitFile(String fileName) throws Exception {
        File file = new File("/data/user/0/"+Android.getApplication().getPackageName()+"/xinit/"+fileName);
        return readFile(file.getAbsolutePath());
    }

    public static String readFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            fis = new FileInputStream(file);//通过字节流获取
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String line;
            sb.append(br.readLine());
            while ((line = br.readLine()) != null) {
                sb.append("\n" + line);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void writeXinitFile(String fileName, byte[] dataBytes) throws Exception {
        File file = new File("/data/user/0/"+Android.getApplication().getPackageName()+"/xinit/"+fileName);
        writeFile(file.getAbsolutePath(), dataBytes);
    }

    public static void writeFile(String filePath, byte[] dataBytes) throws Exception {
        FileOutputStream fs = null;
        try {
            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
                fs = new FileOutputStream(file);
                fs.write(dataBytes, 0, dataBytes.length);
                fs.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fs != null)
                fs.close();
        }
    }

    public static void appendLine2XinitFile(String fileName, String content) throws Exception {
        File file = new File("/data/user/0/"+Android.getApplication().getPackageName()+"/xinit/"+fileName);
        writeFile(file.getAbsolutePath(), content + "\n", true);
    }

    public static void writeXinitFile(String fileName, String content, boolean append) throws Exception {
        File file = new File("/data/user/0/"+Android.getApplication().getPackageName()+"/xinit/"+fileName);
        writeFile(file.getAbsolutePath(), content, append);
    }

    public static void writeFile(String filePath, String content, boolean append) throws Exception {
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(filePath, append);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFile(String filePath) {
        try{
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            if (file.isFile()) {
                file.delete();
                return;
            }
            file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    deleteFile(pathname.getAbsolutePath());
                    return false;
                }
            });
            file.delete();
        }catch (Exception e) {}
    }


    public static List<String> readLines(String filepath) {
        List<String> lines = new ArrayList<>();
        String content = readFile(filepath);
        String[] splits = content.split("\n");
        for (String line : splits) {
            if (TextUtils.isEmpty(line)) {
                continue;
            }
            lines.add(line);
        }
        return lines;
    }

    public static List<String> readXiniFileLines(String fileName) throws Exception {
        File file = new File("/data/user/0/"+Android.getApplication().getPackageName()+"/xinit/"+fileName);
        return readLines(file.getAbsolutePath());
    }

    public static JSONObject readFileAsJSONObject(String filepath) throws Exception {
        String content = readFile(filepath);
        if (content == null || content.isEmpty()) {
            return new JSONObject();
        }
        return JSON.parseObject(content);
    }

    public static JSONObject readXinitFileAsJSONObject(String fileName) throws Exception {
        File file = new File("/data/user/0/"+Android.getApplication().getPackageName()+"/xinit/"+fileName);
        return readFileAsJSONObject(file.getAbsolutePath());
    }
}
