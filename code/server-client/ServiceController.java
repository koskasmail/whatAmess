package code.next;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController 
{

	@RequestMapping("/read")
	public String readJson() 
	{
	

		
		return null;
	}
	
	
//	@RequestMapping("/")
//	public String root() 
//	{
//	
//		String stHelp = "";
//		
//		stHelp += "<h1>Main Menu</h1>";
//		stHelp += "<hr/>";
//		stHelp += "<h3>Welcome To The Main Menu Page</h3>";
//		
//		return stHelp;
//	}
	
	@RequestMapping("/test")
	public String mainTest() 
	{
	
		String stHelp = "";
		
		stHelp += "<h1>Test Home Page</h1>";
		stHelp += "<hr/>";
		stHelp += "<h3>To Be Continue</h3>";
		
		return stHelp;
	}
	
	@RequestMapping("/we")
	public String mainCtl() 
	{
	
		String stHelp = "";
		
		stHelp += "<h1>Home Page</h1>";
		stHelp += "<hr/>";
		stHelp += "<h3>To Be Continue</h3>";
		
		return stHelp;
	}
//  @RequestMapping(method=RequestMethod.POST,value="/go")
	@RequestMapping(method=RequestMethod.GET, value= "/whoami/{id}")
//	@RequestMapping("/whoami/")
//	@RequestMapping(method=RequestMethod.GET,value="/whoami/{me}")
	public String whoAmi(@RequestHeader String id) 
	{
		String myData = id ;
		String output = "";
		
		output += "<h1>Hello i am "+ myData +"</h1>";
		output += "<hr/>";
		output += "<h3>To Be Continue</h3>";
		
		return myData;
	}
	
	
	 @RequestMapping(method=RequestMethod.POST,value="/info")
	 public String 
     Topic(@RequestBody String user, String password)
	 {
		
//		String output = " <div><u><i> Welcome </i></u> </div> "
//						+ " <div> user:::" + user + " </div> "
//					    + "<div>docId:::" + password + " </div> " ;

		 String output = "We are Here: " + user + "password: " + password;
		
		return output ;
	 }
	 
	
	 @RequestMapping(method=RequestMethod.POST,value="/007")
	 public String addTopic(@RequestBody String signature, String user, String docId)
	 {
		
		String output = " <div>signature::: " + signature + "</div>" 
						+ "<div> user:::" + user + " </div> "
					    + "<div>docId:::" + docId + " </div> " ;

		return output ;
	 }
	
	 
	 @RequestMapping(method=RequestMethod.POST,value="/go")
	 public String addTrex(@RequestBody String signature, String user, String docId)
	 {
		
		Go go = new Go(signature, user, docId);
		
		System.out.println(signature);
		System.out.println("---------------------------");
		
		System.out.println("user:: " + go.getUser() );
		System.out.println("docId:: " + go.getDocId() );
		System.out.println("signature:: " + go.getSignature() );

			
		
		//return (go.PrintItAll()) ;
//		return ("<img src='http://localhost:8080/signature=data:image/png;base64,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&user=TYARONK&docId={350389DC-829A-4A8F-A589-07495149C5AB}' alt='drawing'>");
//		return "<img src='" + go.getSignature()	 + " '>";
//		return signature;	
//		return user;
		
		return go.ReturnItAll();
	 }
	 
	 
	 
	@RequestMapping("/rami/{id}")
	public String getTopic(@PathVariable String id)
	{
		return "<h1> Rami Id >>> " + id + " </h1>";
	}
	 
	
	
	public ServiceController() 
	{
	}

	
}
