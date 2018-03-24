package cn.edu.neu.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.provider.MediaStore.Images.Thumbnails;
import android.widget.Toast;
import cn.edu.neu.global.GlobalData;

/*
 * �ɼ����Ķ��������ļ�����/data/data/<package-name>/files/Ŀ¼��
 * �ļ�����ʽaction.txt
 */
public class FileHelper {

	private Context context;  
    private String filePath;
    private static FileHelper fileHelper = null;
    
    public static void initFileHelper(Context context,String path){
    	if(fileHelper==null){
    		fileHelper = new FileHelper(context, path);
    	}
    }
    public static FileHelper getInstance(){
    	return fileHelper;
    }
	private FileHelper(Context context,String filePath) {
		this.context = context;
		this.filePath = filePath;
	} 
	/**
	 * �ж�Ŀ¼�Ƿ����
	 * @return
	 */
	public boolean dirExist(){
		return new File(filePath).exists();
	}
	/**
	 * �ж�Ŀ¼�Ƿ�Ϊ��
	 * @return
	 */
	public boolean dirEmpty(){
		File dir = new File(filePath);
		String[] filesName = dir.list();
		if(filesName.length==0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * ����˽���ļ�
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File creatDataFile(String fileName) throws IOException {  
        File file = new File(filePath +"/" + fileName);  
        file.createNewFile();  
        return file;  
    }  
	/**
	 * ����˽��Ŀ¼
	 * @param dirName
	 * @return
	 */
	public File creatDataDir(String dirName) {  
        File dir = new File(filePath + dirName);  
        dir.mkdir();  
        return dir;  
    }  
	/**
	 * ɾ��˽���ļ�
	 * @param fileName
	 * @return
	 */
	public boolean delDataFile(String fileName) {  
        File file = new File(filePath + "/" + fileName);  
        return delFile(file);  
    }  
	/**
	 * ɾ��˽��Ŀ¼
	 * @param dirName
	 * @return
	 */
	public boolean delDataDir(String dirName) {  
        File file = new File(filePath + dirName);  
        return delDir(file);  
    }  
	/**
	 * ɾ��һ���ļ�
	 * @param file
	 * @return
	 */
	public boolean delFile(File file) {  
        if (file.isDirectory())  
            return false;  
        return file.delete();  
    }  
	/**
	 * ɾ��һ��Ŀ¼�����Ƿǿ�Ŀ¼
	 * @param dir
	 * @return
	 */
	public boolean delDir(File dir) {  
        if (dir == null || !dir.exists() || dir.isFile()) {  
            return false;  
        }  
        for (File file : dir.listFiles()) {  
            if (file.isFile()) {  
                file.delete();  
            } else if (file.isDirectory()) {  
                delDir(file);// �ݹ�  
            }  
        }  
        dir.delete();  
        return true;  
    }  
	
	public void syncGlobalData(){
		GlobalData.existDataAction.clear();
		if(dirExist()){
			File dir = new File(filePath);
			String[] actionsFileArray = dir.list();
			for(int i = 0;i<actionsFileArray.length;i++){
				String[] sub = actionsFileArray[i].split("\\.");
				GlobalData.existDataAction.add(sub[0]);
			}
		}else{
			Toast.makeText(context, "�ļ�ͬ������,Ŀ¼������", Toast.LENGTH_LONG).show();
		}
	}
	public void writeData(String filename,String data){
	    FileOutputStream out = null;
	    BufferedWriter writer = null;
	    try {
	    	out = context.openFileOutput(filename, Context.MODE_PRIVATE);
	    	writer = new BufferedWriter(new OutputStreamWriter(out));
	    	writer.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(writer!=null){
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
