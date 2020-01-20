/**
 * Instance class contains the id of a past verification to be retrieved
 */
package at.tugraz.iaik.lightest.atv.api.model;

public class Instance {
    private long id;

    //for deserialization
    public Instance(){}

    public Instance(long id) {
        this.id = id;
    }

    public long getId()
    {
        return id;
    }
}
