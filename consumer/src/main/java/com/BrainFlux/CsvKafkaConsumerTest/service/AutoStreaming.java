package com.BrainFlux.CsvKafkaConsumerTest.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Service
public class AutoStreaming {

    private String IP;
    private int PORT;
    private String USER;
    private String PASSWORD;

    //    outputSetting
    private String PANEL;
    private String MMXFILE;

    //    Directory
    private String EEGDIRECTORY;
    private String PSCLIDIRECTORY;
    private String CSVDIRECTORY;
    private String DESTINATION;
    private String FINISHEDFILES;
    private String LOGPATH;



    public AutoStreaming(String IP, int PORT, String USER, String PASSWORD, String PANEL, String MMXFILE, String EEGDIRECTORY,
                         String CSVDIRECTORY, String PSCLIDIRECTORY, String DESTINATION, String FINISHEDFILES, String LOGPATH){
        this.IP = IP;
        this.PORT = PORT;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
        this.PANEL = PANEL;
        this.MMXFILE = MMXFILE;
        this.EEGDIRECTORY = EEGDIRECTORY;
        this.PSCLIDIRECTORY = PSCLIDIRECTORY;
        this.CSVDIRECTORY = CSVDIRECTORY;
        this.DESTINATION = DESTINATION;
        this.FINISHEDFILES = FINISHEDFILES;
        this.LOGPATH = LOGPATH;
    }

    public AutoStreaming(){
    }




    private List<File> findERGFiles (String dir){
        File directory = new File(dir);
        File[] files = directory.listFiles();
        LinkedList<File> queueList = new LinkedList<>();
        List<File> eegFileList = new ArrayList<>();
        if(files == null || files.length==0){
            return eegFileList;
        }
        for (int i = 0; i <files.length;i++){
            if(files[i].isDirectory()){
                queueList.add(files[i]);
            }
        }

        while (! queueList.isEmpty()){
            File tempDirectory = queueList.removeFirst();
            File[] currectFiles = tempDirectory.listFiles();
            for (int j = 0; j< currectFiles.length;j ++){
                if(currectFiles[j].isDirectory()){
                    queueList.add(currectFiles[j]);
                }else{
                    if(currectFiles[j].getName().endsWith(".eeg")){
                        String erdFile = currectFiles[j].getAbsolutePath().replace(".eeg",".erd");
                        eegFileList.add(new File(erdFile));
                    }
                }
            }
        }

        return eegFileList;
    };


    private List<File> findProcessedFiles(String dir){
        File directory = new File(dir);
        File[] files = directory.listFiles();
        System.out.println(files[0]);
        LinkedList<File> queueList = new LinkedList<>();
        List<File> layFileList = new ArrayList<>();
        if(files == null || files.length==0){
            return layFileList;
        }
        for (int i = 0; i <files.length;i++){
            if(files[i].isDirectory()){
                queueList.add(files[i]);
            }
        }

        while (! queueList.isEmpty()){
            File tempDirectory = queueList.removeFirst();
            File[] currectFiles = tempDirectory.listFiles();
            for (int j = 0; j< currectFiles.length;j ++){
                if(currectFiles[j].isDirectory()){
                    queueList.add(currectFiles[j]);
                }else{
                    if(currectFiles[j].getName().endsWith(".lay")){
                        layFileList.add(currectFiles[j]);
                    }
                }
            }
        }

        return layFileList;
    }



    private int switchRegistry(String fileType){
        String value;
        String entry = "HKEY_CURRENT_USER\\Software\\Persyst\\PSMMarker\\Settings";
        String parameter = "_ForceRawTrends";

        if(fileType.equals("ar")){
            value = "0x00000000";
        }else {
            value = "0x00000001";
        }

        try{
            Runtime.getRuntime().exec("reg add " + entry + " /v " + parameter + " /t REG_DWORD /d " + value + " /f");
            setLog(LocalDateTime.now()+": change registry to generate "+ fileType + " successful !" );
            return 0;
        }catch (Exception e){
            setLog(LocalDateTime.now()+": change registry to generate "+ fileType + " failed !" );
            return 1;
        }
    }



    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }



    private void setLog(String log){
        try {
            File writename = new File(LOGPATH);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(writename, true)));
            out.write(log+"\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setLog2(String log,String path){
        try {
            File writename = new File(path);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(writename, true)));
            out.write(log+"\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean moveFile(String path, File file){
        File folder = new File(path);
        String year = file.getName().substring(4,8);
        String pid = file.getName().substring(0,12);
        Queue<File> fileQueue = new LinkedList<>();
        if(folder.listFiles()!=null){
            for(File f: folder.listFiles()){
                if(f.getName().equals(year)){
                    fileQueue.add(f);
                }

            }
        }
        while (!fileQueue.isEmpty()){
            File current = fileQueue.poll();
            if(current.getName().equals(year)){
                for(File f: current.listFiles()){
                    fileQueue.add(f);
                }
            }else if(current.getName().equals(pid)){
               return file.renameTo(new File(current.getPath()+"/"+file.getName()));
            }
        }
        File newFolder = new File(path+"/"+year);
        newFolder.mkdir();
        File subFolder = new File(path+"/"+year+"/"+pid);
        subFolder.mkdir();
        return file.renameTo(new File(subFolder.getPath()+"/"+file.getName()));

    }

    public List<File> getAllCSV(String dir){
        List<File> toBeSent = new ArrayList<>();
        File folder = new File(dir);
        File[] files = folder.listFiles();
        for(File f: files){
            if(f.isFile() && f.getName().toLowerCase().endsWith(".csv")){
                toBeSent.add(f);
            }
        }
        return toBeSent;
    }

    public static void main(String args[]) throws IOException {
//        try{
//            Properties pps = new Properties();
//            InputStream in = new BufferedInputStream(new FileInputStream(
//                    new File("config.properties")));
//            pps.load(in);
//            AutoStreaming autoScp = new AutoStreaming(pps.getProperty("IP"),Integer.parseInt(pps.getProperty("PORT")),pps.getProperty("USER"),
//                    pps.getProperty("PASSWORD"),pps.getProperty("PANEL"),pps.getProperty("MMXFILE"),pps.getProperty("EEGDIRECTORY"),
//                    pps.getProperty("CSVDIRECTORY"),pps.getProperty("PSCLIDIRCTORY"),pps.getProperty("DESTINATION"),pps.getProperty("FINISHEDFILES"),pps.getProperty("LOGPATH"));
//            autoScp.cronJob(19,0,0);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        ProducerCSVService producerCSVService = new ProducerCSVService();
//        AutoStreaming autoStreaming=new AutoStreaming() ;
//        List<File> fileList= autoStreaming.getAllCSV("C:\\Users\\Administrator\\Desktop\\kafkaTest");
//        for (int n=0;n<fileList.size();n++){
//            System.out.println( fileList.get(n));
//        }
//        AutoStreaming autoStreaming=new AutoStreaming();
//        File file=new File("C:\\Users\\Administrator\\Desktop\\kafkaTest\\PUH-2015-015_09ar.csv");
//        autoStreaming.moveFile("C:\\Users\\Administrator\\Desktop\\kafkaTest",file);



    }
}

