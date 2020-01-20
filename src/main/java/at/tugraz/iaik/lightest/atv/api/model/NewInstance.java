/**
 * NewInstance class represents a default verification instance
 * It contains a policy and transaction in Bytes
 */
package at.tugraz.iaik.lightest.atv.api.model;

public class NewInstance {

    private final byte[] policy;

    private byte[] transaction;

    /**
     * Default constructor
     * @param policy
     * @param transaction
     */
    public NewInstance(byte[] policy, byte[] transaction) {
        this.policy = policy;
        this.transaction = transaction;
    }

    /**
     * Overloaded constructor for UPRCInstance
     * @param policy
     */
    public NewInstance(byte[] policy) {
        this.policy = policy;
    }

    public byte[] getPolicy() {
        return policy;
    }

    public byte[] getTransaction() {
        return transaction;
    }
}
