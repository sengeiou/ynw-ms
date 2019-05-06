package com.ynw.system.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author
 * @version
 */
@Component
public class HandleFile {

//    @Value("${server.servlet.context-path}")
//    private static String path;
    @Value("${FilePath}")
    private  String baseFile;

    /**
     *  上传单个文件
     * @param file
     * @param request
     * @return
     */
    public  String upload(MultipartFile file, HttpServletRequest request, String id){
        //拿取旧文件名
        String oldFileName = file.getOriginalFilename();
        //取扩展名
        String ext = oldFileName.substring(oldFileName.lastIndexOf("."));
        //取真实路径
        String realPath = request.getSession().getServletContext().getRealPath("image");
        //用时间戳命名文件夹和文件名
        SimpleDateFormat sdfFile = new SimpleDateFormat("HHmmssSSS");
        String newFile = sdfFile.format(new Date())+ext;
        //新路径
        String newPath = baseFile + File.separator + id;
//        String newPath = "D:/file" + File.separator +"user" + File.separator + id;
        //文件全名
        String fileFullName = newPath + File.separator + newFile;
        File path = new File(newPath);
        //判断是否创建文件夹
        if (!path.exists()) {
            path.mkdirs();
        }
        //复制文件
        try {
            //得到输入流
            InputStream in = file.getInputStream();
            //创建输出流
            FileOutputStream out = new FileOutputStream(fileFullName);
            //复制文件
            FileCopyUtils.copy(in, out);
            return "user" + File.separator + id + File.separator + newFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     *  上传多个文件
     * @param request
     * @return
     * @throws IOException
     */
    public  List<String> saveFiles(HttpServletRequest request) throws IOException {
        List<MultipartFile> file1 = new ArrayList<MultipartFile>();
        List<String> urls = new ArrayList<String>();
        if (request instanceof MultipartHttpServletRequest) {
            file1 = ((MultipartHttpServletRequest) request).getFiles("fileName");
        }
        for (MultipartFile file : file1) {
            String url = saveFile(file,TokenUtil.getAcUserId(request.getHeader("token")));
            if (url == null) {
                return null;
            }
            urls.add(url);
        }
        return urls;
    }

    /**
     * 保存文件到本地,并返回文件的url相对路径
     *
     * @param file
     *            文件
     * @return 文件的url相对路径
     * @throws IOException
     */
    public  String saveFile(MultipartFile file, String userId) throws IOException {
        //资源服务根目录
        //String basePath = "D:\\Java\\apache-tomcat-8.5.34\\webapps\\ROOT";
        /*第2步：创建文件夹*/
        /*1.1 根据今天的日期创建文件夹相对路径 upload/2017-05-12 */
       /* Date now = new Date();// 获取当前时间
        Long longNow = now.getTime();
        DateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
        String strToday = dFormat.format(longNow);*/
        String relFolderPath = File.separator + userId; // 文件夹相对路径

        /*1.2 文件夹全路径：项目路径/upload/2017-05-12 */
//        String fullFolderPath = baseFile + File.separator + relFolderPath; // 文件夹全路径
        String fullFolderPath = "D:" + File.separator + relFolderPath; // 文件夹全路径
        /*1.3 根据文件夹全路径判断，如果文件夹不存在，创建文件夹*/
        File outFolder = new File(fullFolderPath);
        if (!outFolder.exists()) {
            outFolder.mkdirs();
        }

        /*第2步：创建文件*/
        /*2.1 根据文件夹全路径，文件名，当前日期的时间戳 创建文件全路径*/
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        String fileName = file.getOriginalFilename(); //获取文件名（含后缀）
        int lastPos=fileName.lastIndexOf('.');
        String fileExe="";
        if(lastPos!=-1){
            fileExe=fileName.substring(lastPos);
        }
        String strP = sdf.format(new Date()) + (new Random().nextInt(900) + 100);
        String fullFilePath = fullFolderPath + File.separator + strP + fileExe; // 文件全路径

        String fullUrlPath = File.separator + userId + File.separator + strP + fileExe;// 要存入数据的相对路径

        /*第2.2步：根据文件全路径，创建文件*/
        File outFile = new File(fullFilePath);
        if (!outFile.exists()) {// 如果文件不存在，创建文件
            outFile.createNewFile();
        }

        /*第3步：根据空文件，创建字符流的目的地（输出流）*/
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outFile)); // 创建文件输出流

        /*第4步：根据上传的文件，创建缓冲区，生成字符流，存入缓冲区*/
        byte[] buffer = file.getBytes();// 创建文件流

        /*第5步：把缓冲区的字符流 写入 字符流目的地（把输入流写入输出流）*/
        stream.write(buffer);

        /* 第6步：刷新流，关闭流 */
        stream.close();
        return fullUrlPath;




        //资源服务根目录
//        String basePath = "/home/dev/static_res/app/";
//        //String basePath = "D:\\Java\\apache-tomcat-8.5.34\\webapps\\ROOT";
//        /*第2步：创建文件夹*/
//        /*1.1 根据今天的日期创建文件夹相对路径 upload/2017-05-12 */
//        Date now = new Date();// 获取当前时间
//        Long longNow = now.getTime();
//        DateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
//        String strToday = dFormat.format(longNow);
//        String relFolderPath = basePath + File.separator + strToday; // 文件夹相对路径
//
//        /*1.2 文件夹全路径：项目路径/upload/2017-05-12 */
//        String fullFolderPath = basePath + File.separator + relFolderPath; // 文件夹全路径
//
//        /*1.3 根据文件夹全路径判断，如果文件夹不存在，创建文件夹*/
//        File outFolder = new File(fullFolderPath);
//        if (!outFolder.exists()) {
//            outFolder.mkdirs();
//        }
//
//        /*第2步：创建文件*/
//        /*2.1 根据文件夹全路径，文件名，当前日期的时间戳 创建文件全路径*/
//        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
//        String fileName = file.getOriginalFilename(); //获取文件名（含后缀）
//        int lastPos=fileName.lastIndexOf('.');
//        String fileExe="";
//        if(lastPos!=-1){
//            fileExe=fileName.substring(lastPos);
//        }
//        String strP = sdf.format(new Date()) + (new Random().nextInt(900) + 100);
//        String fullFilePath = fullFolderPath + File.separator + strP + fileExe; // 文件全路径
//
//        String fullUrlPath = "user/" + strToday + "/" + strP + fileExe;// 要存入数据的相对路径
//
//        /*第2.2步：根据文件全路径，创建文件*/
//        File outFile = new File(fullFilePath);
//        if (!outFile.exists()) {// 如果文件不存在，创建文件
//            outFile.createNewFile();
//        }
//
//        /*第3步：根据空文件，创建字符流的目的地（输出流）*/
//        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outFile)); // 创建文件输出流
//
//        /*第4步：根据上传的文件，创建缓冲区，生成字符流，存入缓冲区*/
//        byte[] buffer = file.getBytes();// 创建文件流
//
//        /*第5步：把缓冲区的字符流 写入 字符流目的地（把输入流写入输出流）*/
//        stream.write(buffer);
//
//        /* 第6步：刷新流，关闭流 */
//        stream.close();
//        System.out.println(fullUrlPath);
//        return fullUrlPath;
    }

    /**
     * 上传视频方法
     * @param file
     * @return
     * @throws IOException
     */
//    public String saveFileVideo(String title,MultipartFile file) throws IOException {
//        InputStream inputStream = null;
//        String fileName = file.getOriginalFilename();
//        //4.1 文件流
//        try {
//            inputStream = file.getInputStream();
//       } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        String playURL = VideoUtil.uploadStream(title, fileName, inputStream);
//        return playURL;
//    }

    /**
     * 上传App文件,并返回文件的url相对路径
     *
     * @param file
     *            文件
     * @param basePath
     *            项目根目录
     * @return 文件的url相对路径
     * @throws IOException
     */
    public String saveAPP(MultipartFile file, String basePath, String userId) throws IOException {

        /*第2步：创建文件夹*/
        /*1.1 根据今天的日期创建文件夹相对路径 upload/2017-05-12 */
        /*Date now = new Date();// 获取当前时间
        Long longNow = now.getTime();
        DateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
        String strToday = dFormat.format(longNow);*/
        String relFolderPath = "APK/" + File.separator + userId; // 文件夹相对路径

        /*1.2 文件夹全路径：项目路径/upload/2017-05-12 */
        String fullFolderPath = basePath + File.separator + relFolderPath; // 文件夹全路径

        /*1.3 根据文件夹全路径判断，如果文件夹不存在，创建文件夹*/
        File outFolder = new File(fullFolderPath);
        if (!outFolder.exists()) {
            outFolder.mkdirs();
        }

        /*第2步：创建文件*/
        /*2.1 根据文件夹全路径，文件名，当前日期的时间戳 创建文件全路径*/
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        String fileName = file.getOriginalFilename(); //获取文件名（含后缀）
        int lastPos=fileName.lastIndexOf('.');
        String fileExe="";
        if(lastPos!=-1){
            fileExe=fileName.substring(lastPos);
        }
        String strP = "manage" + sdf.format(new Date()) + (new Random().nextInt(900) + 100);
        String fullFilePath = fullFolderPath + File.separator + strP + fileExe; // 文件全路径

        String fullUrlPath = "APK/" + userId + "/" + strP + fileExe;// 要存入数据的相对路径

        /*第2.2步：根据文件全路径，创建文件*/
        File outFile = new File(fullFilePath);
        if (!outFile.exists()) {// 如果文件不存在，创建文件
            outFile.createNewFile();
        }

        /*第3步：根据空文件，创建字符流的目的地（输出流）*/
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outFile)); // 创建文件输出流

        /*第4步：根据上传的文件，创建缓冲区，生成字符流，存入缓冲区*/
        byte[] buffer = file.getBytes();// 创建文件流

        /*第5步：把缓冲区的字符流 写入 字符流目的地（把输入流写入输出流）*/
        stream.write(buffer);

        /* 第6步：刷新流，关闭流 */
        stream.close();

        return fullUrlPath;
    }
    /**
     * 根据文件绝对路径，删除一个图片，删除记录
     *
     * @param fullFilePath
     */
    public void deleteFile(String fullFilePath) {
        File deleteFile = new File(fullFilePath);
        deleteFile.delete();
    }
    /**
     * @功能 下载文件
     * @param filePath
     * @param downloadName
     * @return
     */
    public void downloadFile(HttpServletResponse resp, HttpServletRequest request, String filePath, String downloadName) {
        String fileName = null;
        try {
            fileName = new String(downloadName.getBytes("utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ///G:\manage\target\pm\APK
        String realPath =request.getSession().getServletContext().getRealPath(File.separator).replace("/", "\\");
//        String realPath=File.separator+"home"+File.separator+"tomcat"+File.separator+"apache-tomcat-9.0.1"+File.separator+"files";
        String path = realPath + File.separator + filePath;
        File file = new File(path);
        resp.reset();
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("utf-8");
        resp.setContentLength((int) file.length());
        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = resp.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
