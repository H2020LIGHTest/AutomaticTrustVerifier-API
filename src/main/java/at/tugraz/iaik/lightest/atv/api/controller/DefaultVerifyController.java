/**
 * This is the default ATV API. It is used for verifying electronic transactions
 * as defined in  the LIGHTest specification. There are two main functions  and
 * this class uses the standard ATV library as a requirement.
 */

package at.tugraz.iaik.lightest.atv.api.controller;

import at.tugraz.iaik.lightest.atv.api.common.Helper;
import at.tugraz.iaik.lightest.atv.api.model.Instance;
import at.tugraz.iaik.lightest.atv.api.model.InstanceVerification;
import at.tugraz.iaik.lightest.atv.api.model.NewInstance;
import at.tugraz.iaik.lightest.atv.api.model.Response;
import eu.lightest.verifier.controller.VerificationProcess;
import eu.lightest.verifier.exceptions.DNSException;
import eu.lightest.verifier.model.report.BufferedFileReportObserver;
import eu.lightest.verifier.model.report.Report;
import eu.lightest.verifier.controller.VerificationProcess;
import eu.lightest.verifier.model.report.StdOutReportObserver;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(value = "/api/v1", produces = MediaTypes.HAL_JSON_VALUE)
public class DefaultVerifyController {
    
    Helper helper = Helper.getHelper();
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultVerifyController.class);
    
    // This is used for random string generation
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    
    /** Test function to ensure the API works
     *
     * @return response A class object containing the verification result report (file) and result (boolean)
     */
    
    @GetMapping
    @ResponseBody
    public Response v1verify() {
        
        Response response = new Response();
        
        response.setResult(1);
        response.setVerificationResult("This is for testing");
        
        return response;
    }
    
    @GetMapping(value =  "/log", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String log() throws IOException {
    
        String response = "";// = "<html><body>";
    
        File log_file = new File("/tmp/atvapi.log");
        FileInputStream log_file_stream = null;
        StringBuilder log_content_builder = new StringBuilder();
        try {
            log_file_stream = new FileInputStream(log_file);
            int content;
            while ((content = log_file_stream.read()) != -1) {
                // convert to char and display it
                if (content == '\n')
                    log_content_builder.append("     <br/>\n");
                else
                    log_content_builder.append((char) content);
            }

            response += log_content_builder.toString();
    
    
        } catch (FileNotFoundException e) {
            e.printStackTrace();
  
            response +=  "Reading of Log failed: " + e.getMessage();
            
        }
        finally {
            if(log_file_stream != null) {
                log_file_stream.close();
            }
        }
    
        //response +="</body></html>";
        
        return response;
    }
    
    /**
     * Creates an instance and verifies that instance
     *
     * @param newInstance
     * @return result
     */
    @PostMapping("/addInstance")
    @ResponseBody
    public InstanceVerification addInstance(@RequestBody NewInstance newInstance) throws IOException {
        
        
        
        
        InstanceVerification result = new InstanceVerification();
        if(newInstance == null) {
            
            result.setId(0);
            result.setResult(0);
            result.setVerificationResult("The newInstance is null");
            return result;
        }
        
        // Create all the necessary files
        Long id = new Random().nextLong();
        id = Math.abs(id); // no one needs negative folder names ...
    
        System.out.println("start ATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPI");
        System.out.println("Processing ID " + id);
        
        // Use helper class to create new files
        HashMap<String, File> fileHashMap = helper.createFiles(id, "transaction.asics");
        
        // Write policy and transaction to file for later use
        helper.writeOutputStream(id, newInstance, fileHashMap.get("transaction.asics_file"), fileHashMap.get("policy_file"));
    
        System.out.println("Received transaction: " + Files.probeContentType(fileHashMap.get("transaction.asics_file").toPath()));
        System.out.println("Received policy: \n" + new String(newInstance.getPolicy()));
        
        ArrayList<String> report = new ArrayList();
        
        result.setId(id);
        // Verify the transaction here! Uncomment the line when verify returns something officially
        result.setResult(verify(newInstance,
                fileHashMap.get("policy_file"),
                fileHashMap.get("transaction.asics_file"),
                fileHashMap.get("report_file"),
                fileHashMap.get("result_file"),
                report));
        
        // {#STATUS_FAIL} (0) or {#STATUS_OK} (1)
        if(result.getResult() == 1) {
            result.setVerificationResult("OK");
        } else {
            result.setVerificationResult("FAIL");
        }
        
        result.setReport(report);
    
        System.out.println("Processed ID " + id);
        System.out.println("done ATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPIATVAPI");
        
        return result;
    }
    
    /**
     * Retrieves an instance verification result
     *
     * @param instance
     * @return
     */
    @PostMapping("/findInstance")
    @ResponseBody
    public InstanceVerification findInstancebyID(@RequestBody Instance instance) {
        
        InstanceVerification instanceVerification = new InstanceVerification();
        
        if(instance == null) {
            instanceVerification.setId(0);
            instanceVerification.setResult(0);
            instanceVerification.setVerificationResult("The Instance is null");
            return instanceVerification;
        }
        
        File result_file = new File(instance.getId() + "/" + "result");
        instanceVerification.setId(instance.getId());
        
        // Read the result file and provide response.
        try {
            List<String> allLines = Files.readAllLines(result_file.toPath());
            instanceVerification.setResult(Integer.parseInt(allLines.get(0)));
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return instanceVerification;
    }
    
    /**
     * Actual verification and call to the ATV library occurs here
     *
     */
    
    private int verify(NewInstance instance, File policy_file, File transaction_file,
                       File report_file, File result_file, ArrayList<String> reportbuffer) {
        Report report = new Report();
        int result = 0;
    
        StdOutReportObserver stdOutReportObserver = new StdOutReportObserver();
        report.addObserver(stdOutReportObserver);
        
        BufferedFileReportObserver bufferedFileReportObserver = new BufferedFileReportObserver();
        report.addObserver(bufferedFileReportObserver);
    
        Security.setProperty("keystore.type", "jks");
        
        // Create an instance of the verification process
        VerificationProcess verificationProcess = new VerificationProcess(transaction_file, policy_file, report);
        
        // prepare file printer
        PrintWriter out = null;
        // Check transaction
        try {
            result = verificationProcess.checkTransactionForAPI();
            
            // write the result to file
            out = new PrintWriter(result_file);
            out.println(result);
            
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null)
                out.close();
        }
    
        logger.info("Verification done!");
        
        helper.writeReportToLog(bufferedFileReportObserver);
        
        // Save the verification report to file
        try {
            bufferedFileReportObserver.saveToFile(report_file);
            reportbuffer.addAll(bufferedFileReportObserver.getBuffer());
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    @NotNull
    private String generateFileNames() {
        int len = 10;
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
    
}
