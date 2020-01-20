/**
*   Helper Class for use in the controller
*/

package at.tugraz.iaik.lightest.atv.api.common;

import at.tugraz.iaik.lightest.atv.api.controller.DefaultVerifyController;
import at.tugraz.iaik.lightest.atv.api.model.GDRequest;
import at.tugraz.iaik.lightest.atv.api.model.NewInstance;
import eu.lightest.verifier.model.report.BufferedFileReportObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class Helper {

    private static Helper helper;
    
    private static final Logger logger = LoggerFactory.getLogger(Helper.class);
    
    private Helper(){}

    public void writeOutputStream(long id, NewInstance instance, File transaction_file, File policy_file){

        try {
            OutputStream policyOutputStream = new FileOutputStream(policy_file);
            policyOutputStream.write(instance.getPolicy());
            policyOutputStream.close();

            OutputStream certificateOutputStream = new FileOutputStream(transaction_file);
            certificateOutputStream.write(instance.getTransaction());
            certificateOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeOutputStream(long id, GDRequest instance, String policy, File transaction_file, File policy_file){
        
        try {
            OutputStream policyOutputStream = new FileOutputStream(policy_file);
            policyOutputStream.write(policy.getBytes());
            policyOutputStream.close();
            
            OutputStream certificateOutputStream = new FileOutputStream(transaction_file);
            certificateOutputStream.write(instance.getTransaction().getBytes());
            certificateOutputStream.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, File> createFiles(long id, String file){

        HashMap<String,File> fileMap = new HashMap<String, File>();

        String mainFileName = file+"_file";


        // Use the id to create its own directory
        new File(id+"/").mkdirs();

        // Store the resultant files in the directory
        File transaction_file = new File(id + "/" + file);
        File policy_file = new File(id + "/" + "policy.tpl");
        File report_file = new File(id + "/" + "report");
        File result_file = new File(id + "/" + "result");

        fileMap.put(mainFileName,transaction_file);
        fileMap.put("policy_file", policy_file);
        fileMap.put("report_file", report_file);
        fileMap.put("result_file", result_file);

        return fileMap;
    }

    public static Helper getHelper(){

        if (helper == null) {
            helper = new Helper();
        }

        return helper;
    }
    
    public void writeReportToLog(BufferedFileReportObserver bufferedFileReportObserver) {
        logger.info("");
        System.out.println();
        
        logger.info("ATV REPORT:");
        System.out.println("ATV REPORT:");
        
        for(String line : bufferedFileReportObserver.getBuffer()) {
            logger.info(line);
            System.out.println(line);
        }
    
        System.out.println();
    }
}
