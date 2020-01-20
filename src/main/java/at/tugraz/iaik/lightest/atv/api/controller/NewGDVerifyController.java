/**
 * This is the default ATV API. It is used for verifying electronic transactions
 * as defined in  the LIGHTest specification. There are two main functions  and
 * this class uses the standard ATV library as a requirement.
 */

package at.tugraz.iaik.lightest.atv.api.controller;

import at.tugraz.iaik.lightest.atv.api.common.Helper;
import at.tugraz.iaik.lightest.atv.api.model.*;
import eu.lightest.verifier.controller.VerificationProcess;
import eu.lightest.verifier.exceptions.DNSException;
import eu.lightest.verifier.model.report.BufferedFileReportObserver;
import eu.lightest.verifier.model.report.Report;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Controller
@RequestMapping(value = "/api/v3", produces = MediaTypes.HAL_JSON_VALUE)
public class NewGDVerifyController {
    
    Helper helper = Helper.getHelper();
    
    private static final Logger logger = LoggerFactory.getLogger(NewGDVerifyController.class);
    
    // This is used for random string generation
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    
    /** Test function to ensure the API works
     *
     * @return response A class object containing the verification result report (file) and result (boolean)
     */
    
    @GetMapping
    @ResponseBody
    public Response v3verify() {
        
        Response response = new Response();
        
        response.setResult(1);
        response.setVerificationResult("This is for FIDO testing");
        
        return response;
    }
    
    @GetMapping(value =  "/log", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String log() throws IOException {
    
        String response = "";// = "<html><body>";
    
        File log_file = new File("log/log.log");
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
            log_file_stream.close();
        }
    
        //response +="</body></html>";
        
        return response;
    }
    
    /**
     * Creates an instance and verifies that instance
     *
     * @param request
     * @return result
     */
    @PostMapping("/verifyInstanceGD")
    @ResponseBody
    public GDResponse addInstance(@RequestBody GDRequest request) throws IOException {
    
        String storedPolicy = loadStoredPolicy();
        
        System.out.println("Received policy: \n" + storedPolicy);
        System.out.println("Received transaction: \n" + request.getTransaction());
    
    
        GDResponse response = new GDResponse();
        if(request == null) {
            
            response.setId(0);
            response.setResult(0);
            response.setVerificationResult("request is null");
            return response;
        }
        
        // Create all the necessary files
        Long id = new Random().nextLong();
        id = Math.abs(id); // no one needs negative folder names ...
    
        logger.info("Processing ID " + id);
        
        // Use helper class to create new files
        HashMap<String, File> fileHashMap = helper.createFiles(id, "transaction.json");
        
        // Write policy and transaction to file for later use
        helper.writeOutputStream(id, request, storedPolicy, fileHashMap.get("transaction.json_file"), fileHashMap.get("policy_file"));
        
        ArrayList<String> report = new ArrayList();
        
        response.setId(id);
        // Verify the transaction here! Uncomment the line when verify returns something officially
        response.setLoa(verify(request,
                fileHashMap.get("policy_file"),
                fileHashMap.get("transaction.json_file"),
                fileHashMap.get("report_file"),
                fileHashMap.get("result_file"),
                report));
        
        if(response.getLoa() != null) {
            response.setVerificationResult("OK");
        } else {
            response.setVerificationResult("FAIL");
        }
        
        response.setReport(report);
        
        return response;
    }
    
    private String loadStoredPolicy() throws IOException {
        InputStream inputStream = getClass()
                .getClassLoader().getResourceAsStream("storedPolicies/fido1.tpl");
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }
    
    /**
     * Actual verification and call to the ATV library occurs here
     *
     */
    
    private String verify(GDRequest instance, File policy_file, File transaction_file,
                       File report_file, File result_file, ArrayList<String> reportbuffer) {
        Report report = new Report();
        String loa = null;
        
        
        BufferedFileReportObserver bufferedFileReportObserver = new BufferedFileReportObserver();
        report.addObserver(bufferedFileReportObserver);
    
        Security.setProperty("keystore.type", "jks");
        
        // Create an instance of the verification process
        VerificationProcess verificationProcess = new VerificationProcess(transaction_file, policy_file, report);
        
        // prepare file printer
        PrintWriter out = null;
        // Check transaction
        try {
            loa = verificationProcess.checkTransactionForAPI("loa");
            
            // write the result to file
            out = new PrintWriter(result_file);
            out.println(loa);
            
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null)
                out.close();
        }
        
        
        // Save the verification report to file
        try {
            bufferedFileReportObserver.saveToFile(report_file);
            reportbuffer.addAll(bufferedFileReportObserver.getBuffer());
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return loa;
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
