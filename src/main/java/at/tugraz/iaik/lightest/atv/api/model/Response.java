/**
* Basic response structure.
* See InstanceVerification
**/
package at.tugraz.iaik.lightest.atv.api.model;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1513207428686438208L;
    private String verificationResult;
    private long result;

    public String getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }

    public void setResult(long result) {
        this.result = result;
    }

    public long getResult() {
        return result;
    }
}
