package it.cnr.isti.labsedc.concern.rest.monitoring;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Root resource (exposed at "myresource" path)
 */
@Path("monitoring/biecointerface/loadrules")

public class LoadRules {
		
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	
    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getIt() {
        return homePage();
        
    }
    
    
    private String homePage() {
	
    	return"<!DOCTYPE html>\n"
    			+ "<html>\n"
    			+ "<head lang=\"en\">\n"
    			+ "    <meta charset=\"UTF-8\"/>\n"
    			+ "    <title>Load Rules or ruleset to monitor</title>\n"
    			+ "    <link rel=\"stylesheet\" href=\"built/style.css\"/>\n"
    			+ "    <style type=\"text/css\">\n"
    			+ "    div {\n"
    			+ "        display: flex;\n"
    			+ "        flex-direction: column;\n"
    			+ "        align-items: center;\n"
    			+ "    }\n"
    			+ "    input {\n"
    			+ "        margin-top: 10px;\n"
    			+ "    }\n"
    			+ "     \n"
    			+ "    textarea {\n"
    			+ "        margin-top: 15px;\n"
    			+ "        width: 70%;\n"
    			+ "    }\n"
    			+ "</style>\n"
    			+ " \n"
    			+ "<body>    <center>\n"
    			+ "        <h1 style=\"color: green;\">\n"
    			+ "          Concern Rules Loading\n"
    			+ "        </h1>\n"
    			+ "        <div>\n"
    			+ "            <input type=\"file\">\n"
    			+ "            <textarea cols=\"30\" rows=\"20\"\n"
    			+ "                      placeholder=\"rules loaded will appear here\">\n"
    			+ "            </textarea>\n"
    			+ "            <br />\n"
    			+ "            <button onclick=\"executePost()\">Sent loaded rules to the Monitor</button>\n"
    			+ "        </div>\n"
    			+ "    </center>\n"
    			+ "        <script type=\"text/javascript\">\n"
    			+ " let input = document.querySelector('input')\n"
    			+ "let textarea = document.querySelector('textarea')\n"
    			+ "input.addEventListener('change', () => {\n"
    			+ "	let files = input.files;\n"
    			+ "\n"
    			+ "	if (files.length == 0) return;\n"
    			+ "	const file = files[0];\n"
    			+ "	let reader = new FileReader();\n"
    			+ "	reader.onload = (e) => {\n"
    			+ "		const file = e.target.result;\n"
    			+ "		const lines = file.split(/\\r\\n|\\n/);\n"
    			+ "		textarea.value = lines.join('\\n');\n"
    			+ "	};\n"
    			+ "	reader.onerror = (e) => alert(e.target.error.name);\n"
    			+ "	reader.readAsText(file);\n"
    			+ "});\n"
    			+ "    </script>\n"
    			+ "    \n"
    			+ "     <script>\n"
    			+ "        //Usually, you put script-tags into the head\n"
    			+ "        function executePost() {\n"
    			+ "            //This performs a POST-Request.\n"
    			+ "            //Use \"$.get();\" in order to perform a GET-Request (you have to take a look in the rest-API-documentation, if you're unsure what you need)\n"
    			+ "            //The Browser downloads the webpage from the given url, and returns the data.\n"
    			+ "            $.post( \"http://hostserver.com/MEApp/MEService\", function( data ) {\n"
    			+ "                 //As soon as the browser finished downloading, this function is called.\n"
    			+ "                 $('#demo').html(data);\n"
    			+ "            });\n"
    			+ "        }\n"
    			+ "    </script>\n"
    			+ "    \n"
    			+ "</body>\n"
    			+ "</html>\n";
    	    	
    }
    
}

