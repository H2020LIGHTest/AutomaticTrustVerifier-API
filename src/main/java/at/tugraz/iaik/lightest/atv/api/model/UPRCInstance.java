/**
 * UPRC instance class inherits from the standard NewInstance
 * However, it replaces transaction with certificate on the frontend
 * It contains a policy and certificate in byte form
 */

package at.tugraz.iaik.lightest.atv.api.model;

public class UPRCInstance extends NewInstance {

    private final byte[] certificate;

    /**
     * Default constructor
     * @param policy
     * @param certificate
     */
    public UPRCInstance(byte[] policy, byte[] certificate) {
        super(policy);
        this.certificate = certificate;
    }

    @Override
    public byte[] getPolicy() {
        return super.getPolicy();
    }

    public byte[] getCertificate() {
        return certificate;
    }
}
