/**
*   Controller to handle G+D requests
*/

package at.tugraz.iaik.lightest.atv.api.controller;

import at.tugraz.iaik.lightest.atv.api.model.GDRequest;
import at.tugraz.iaik.lightest.atv.api.model.Response;
import org.springframework.hateoas.MediaTypes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/v2",produces = MediaTypes.HAL_JSON_VALUE)
public class GDVerifyController {

    public GDVerifyController() {
    }


    @GetMapping
    @ResponseBody
    public Response v2verify(){

        Response response;
        response = new Response();

        response.setResult(2);
        response.setVerificationResult("This is for testing");

        return response;
   }

    @PostMapping("/verifyInstanceGD")
    @ResponseBody
    public Response postVerification(@RequestBody GDRequest instance){

        Response response = new Response();
        response.setResult(10);

        if(instance == null){
            return null;
        }

        if(instance.getAuthType().contentEquals("0024#FE01")){
            response.setVerificationResult("medium");
        }else if (instance.getAuthType().contentEquals("0024#FE02")){
            response.setVerificationResult("low");
        }else{
            response.setVerificationResult("None");
        }

        return response;
    }

}
