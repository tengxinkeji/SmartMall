package com.common.app.sd;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.common.app.Common;
import com.common.app.degexce.CustomExce;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Operation about SD card
 *
 * @Create 2013-6-17
 */
public class SDCardUtils {

    /**
     * Check the SD card
     *
     * @return
     */
    private static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息     *
     * @return
     */
    private static ArrayList<String> getDevMountList() {
        String[] toSearch = FileUtils.readFile("/etc/vold.fstab").split(" ");
        ArrayList<String> out = new ArrayList<String>();
        for (int i = 0; i < toSearch.length; i++) {
            if (toSearch[i].contains("dev_mount")) {
                if (new File(toSearch[i + 2]).exists()) {
                    out.add(toSearch[i + 2]);
                }
            }
        }
        return out;
    }

    /**
     * 获取扩展SD卡存储目录
     *
     * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
     * 否则：返回内置SD卡目录
     *
     * @return
     */
    private static File getExternalSdCardPath() {
        if (checkSDCardAvailable()) {
            File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            sdCardFile=new File(sdCardFile, Enum_Dir.DIR_root.toString());
            if (!sdCardFile.exists()){
                sdCardFile.mkdirs();
            }
            return sdCardFile;
        }

        String path = null;

        File sdCardFile = null;

        ArrayList<String> devMountList = getDevMountList();
        for (String devMount : devMountList) {
            File file = new File(devMount);
            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();
                File testWritable = new File(path, Common.DIR_root);
                if (testWritable.exists()){
                    path=testWritable.getAbsolutePath();
                }else{
                    if (testWritable.mkdirs()) {
                        path=testWritable.getAbsolutePath();
                    } else {
                        path = null;
                    }
                }

            }
        }

        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile;
        }

        return null;
    }

    /**获取目录*/
    public static File getDirFile(Enum_Dir enum_dir) throws CustomExce{
        File imgFile=getExternalSdCardPath();
        if (imgFile==null){
            throw new CustomExce("获取SD卡失败："+FileUtils.readFile("/etc/vold.fstab"));
        }else{
            if (Enum_Dir.DIR_root==enum_dir){
                return imgFile;
            }else{
                imgFile=new File(imgFile, enum_dir.toString());
                if (!imgFile.exists())
                    imgFile.mkdirs();
            }

        }

        return imgFile;
    }

    /**
     * 递归删除文件和文件夹
     * 要删除的根目录
     */
    public static void deleteDirFile(File file){

        if(file.isFile()){
//            if (file.getName().endsWith(".apk"))
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                deleteDirFile(f);
            }
            file.delete();
        }
    }


    /**
     * Check if the file is exists
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean isFileExistsInSDCard(String filePath, String fileName){
        boolean flag = false;
        if (checkSDCardAvailable()) {
            File file = new File(filePath, fileName);
            if (file.exists()) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Write file to SD card
     * @param filePath
     * @param filename
     * @param content
     * @return
     * @throws Exception
     */
    public static boolean saveFileToSDCard(String filePath, String filename, String content)
            throws Exception {
        boolean flag = false;
        if (checkSDCardAvailable()) {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(filePath, filename);
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(content.getBytes());
            outStream.close();
            flag = true;
        }
        return flag;
    }

    /**
     * Read file as stream from SD card
     *
     * @param fileName
     *            String PATH =
     *            Environment.getExternalStorageDirectory().getAbsolutePath() +
     *            "/dirName";
     * @return
     */
    public static byte[] readFileFromSDCard(String filePath, String fileName) {
        byte[] buffer = null;
        try {
            if (checkSDCardAvailable()) {
                String filePaht = filePath + "/" + fileName;
                FileInputStream fin = new FileInputStream(filePaht);
                int length = fin.available();
                buffer = new byte[length];
                fin.read(buffer);
                fin.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * Delete file
     *
     * @param filePath
     * @param fileName
     *            filePath =
     *            android.os.Environment.getExternalStorageDirectory().getPath()
     * @return
     */
    public static boolean deleteSDFile(String filePath, String fileName) {
        File file = new File(filePath + "/" + fileName);
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return file.delete();
    }

    /**获取img
     *
     * */
    public static Drawable getImageDrawable(File file){
        if(!file.exists()) return null;
        try{
            InputStream inp = new FileInputStream(file);
            BitmapDrawable bd = new BitmapDrawable(BitmapFactory.decodeStream(inp));
            Drawable d = (Drawable) bd;
            return d;

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
