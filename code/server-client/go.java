package code.next; 

public class Go {

	private String user;
	private String docId;
	private String signature;
	
	public Go(String signature, String user, String docId ) {
		super();
		this.user = user;
		this.docId = docId;
		this.signature = signature;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String PrintItAll() {
//		String out = "" + user + "<br/>" + docId + "<br/>" + "<img id='sign' src='" + signature + " width=50% height=50% > ";
		String out = "" + user + "<br/>" + docId + "<br/>" + signature ;
	
		return out;
	}
	

	public String ReturnItAll() {
		
		String signatureChange = signature.replace("signature=data%3Aimage%2Fpng%3Bbase64%2C","data:image/png;base64,").replace("", "");
		
		System.out.println("\n\n");
		System.out.println("signatureChange");
		
		
		String out = "" + user + "<br/>" 
	                    + docId + "<br/>" 
				        + signatureChange 
				        ;
		
		System.out.println("out\n\n");
		System.out.println(out);
		return out;
	}

}
