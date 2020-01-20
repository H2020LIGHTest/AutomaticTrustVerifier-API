/**
 * InstanceVerification class represents a default verification response
 * It contains the verification result of "0" or "1"
 * And a report of the verification result in String
 * And the id for the verification
 */
package at.tugraz.iaik.lightest.atv.api.model;

import eu.lightest.verifier.controller.VerificationProcess;

import java.util.ArrayList;

public class GDResponse extends Response {
    private long id;
    private String verificationResult;
    private int result;
    private ArrayList<String> report;
    private String loa;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }

    public long getResult() {
        return result;
    }
    
    public String getLoa() {
        return loa;
    }
    
    public void setLoa(String loa) {
        this.result = loa == null ? VerificationProcess.STATUS_FAIL : VerificationProcess.STATUS_OK;
        this.loa = loa == null ? "untrusted" : loa;
    }
    
    public ArrayList<String> getReport() {
        return report;
    }
    
    public void setReport(ArrayList<String> report) {
        this.report = report;
    }
}
