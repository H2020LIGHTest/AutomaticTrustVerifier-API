/**
* A G+D Instance. 
* Unique to G+D requests
*/
package at.tugraz.iaik.lightest.atv.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

public class GDRequest {
    @JsonProperty("idMethod")
    private final long idMethod;
    @JsonProperty("idType")
    private final long idType;
    @JsonProperty("authMethod")
    private final long authMethod;
    @JsonProperty("authType")
    private final String authType;

    public GDRequest(long idMethod, long idType, long authMethod, String authType) {
        this.idMethod = idMethod;
        this.idType = idType;
        this.authMethod = authMethod;
        this.authType = authType;
    }

    public String getAuthType() {
        return authType;
    }

    public long getAuthMethod() {
        return authMethod;
    }

    public long getIdMethod() {
        return idMethod;
    }
    
    public long getIdType() {
        return idType;
    }
    
    
    @Override
    public String toString() {
        return "GDInstance{" +
                "idMethod=" + idMethod +
                ", idType=" + idType +
                ", authMethod=" + authMethod +
                ", authType='" + authType + '\'' +
                '}';
    }
    
    public String getTransaction() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
