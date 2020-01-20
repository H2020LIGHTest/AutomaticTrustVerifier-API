/**
 * InstanceVerification class represents a default verification response
 * It contains the verification result of "0" or "1"
 * And a report of the verification result in String
 * And the id for the verification
 */
package at.tugraz.iaik.lightest.atv.api.model;

import java.util.ArrayList;

public class InstanceVerification extends Response {
    private long id;
    private String verificationResult;
    private int result;
    private ArrayList<String> report;


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

    public void setResult(int result) {
        this.result = result;
    }
    
    public ArrayList<String> getReport() {
        return report;
    }
    
    public void setReport(ArrayList<String> report) {
        this.report = report;
    }
}
