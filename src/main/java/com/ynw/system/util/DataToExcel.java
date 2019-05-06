package com.ynw.system.util;

import jxl.Sheet;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jxl.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataToExcel<T> {

    @Value("${FilePath}")
    private String FilePath;

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }

    /**
     *  下载Excel到指定目录
     * @param sheetName
     * @param title
     * @param content
     * @param fileName
     */
    public void downloadNativeExcel(String sheetName, String[] title, String[][] content, String fileName) {
        //创建HSSFWorkbook
        HSSFWorkbook wb = DataToExcel.getHSSFWorkbook(sheetName, title, content, null);
        try {
            String filepath = FilePath + File.separator + "system" + File.separator;
            //判断是否存在目录. 不存在则创建
            isChartPathExist(filepath);
            //输出Excel文件1
            FileOutputStream output=new FileOutputStream(filepath+fileName);
            wb.write(output);//写入磁盘
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        String[] title = {"id","姓名","性别","年龄"};
//        String[][] content = {{"123","qq","nan","15"},{"1234","qq1","nans","15"}};
//        downloadNativeExcel("D:/excel","学生信息表",title, content, "测试.xls");
//    }

    /**
     * 判断文件夹是否存在，如果不存在则新建
     *
     * @param dirPath 文件夹路径
     */
    private static void isChartPathExist(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     *  下载Excel
     * @param fileName
     * @param response
     */
    public static void downloadExcel(String fileName, HttpServletResponse response) {
        try {
            HSSFWorkbook wb;
            //判断文件是否存在，不存在创建空的excel输出
            if (new File(fileName).exists()) {
                wb = new HSSFWorkbook(new FileInputStream(fileName));
            } else {
                String[] title = {"id","编号","昵称","姓名","性别","年龄","生日","身份证","电话","行业","学校","学历",
                        "故乡","城市","注册时间","推荐人", "最后登录时间"};
                String[][] content = new String[1][];
                content[0] = new String[title.length];
                content[0][0] = null;
                content[0][1] = null;
                content[0][3] = null;
                content[0][4] = null;
                content[0][4] = null;
                content[0][5] = null;
                content[0][6] = null;
                content[0][7] = null;
                content[0][8] = null;
                content[0][9] = null;
                content[0][10] = null;
                content[0][11] = null;
                content[0][12] = null;
                content[0][13] = null;
                content[0][14] = null;
                content[0][15] = null;
                content[0][16] = null;
                wb = DataToExcel.getHSSFWorkbook("模板", title, content, null);
            }
            setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    /**
     *  下载Excel
     * @param sheetName
     * @param title
     * @param content
     * @param fileName
     * @param response
     */
    public static void downloadExcel(String sheetName, String[] title, String[][] content, String fileName, HttpServletResponse response) {
        //创建HSSFWorkbook
        HSSFWorkbook wb = DataToExcel.getHSSFWorkbook(sheetName, title, content, null);
        try {
            setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 查询指定目录中Excel表格中所有数据
     * @param file 为文件完整路径
     * @return
     */
    public static List<String> getAllByExcel(String file){
        List<String> list = new ArrayList<>();
        try {
            Workbook rwb = Workbook.getWorkbook(new File(file));
            Sheet rs = rwb.getSheet(0);
//            int clos=rs.getColumns();//得到所有的列
            int rows=rs.getRows();//得到所有的行
//            System.out.println("clos:"+clos+" rows:"+rows);
            for(int i=1;i<rows;i++){
                int j=0;
                //第一个是列数，第二个是行数
                String id=rs.getCell(j++, i).getContents();//默认最左边编号也算一列 所以这里得j++
                String name=rs.getCell(j++, i).getContents();
                String sex=rs.getCell(j++, i).getContents();
                String num=rs.getCell(j++, i).getContents();

//                System.out.println("id:"+id+" name:"+name+" sex:"+sex+" num:"+num);
                list.add(id);
//                list.add(new Stu(Integer.parseInt(id), name, sex, Integer.parseInt(num)));
            }
        } catch (BiffException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

}
