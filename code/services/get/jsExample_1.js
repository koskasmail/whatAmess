
			
	        ////----[User-Name]----------------//// lshulamits/tyaronk
	        var getUserData = "lshulamits";   // Test erea, user prakitut == true  
//			var getUserData = ecm.model.desktop.userId;        
	        
	        ////----[url]----------------////        
	        var txtUrl = "https://rmiapplictest/RmiTashtiotWS/api/GroupsApi/IsUserExistsInGroup?groupCode=1002&userName=";

	        ////----[url+User-Name]----------------////
	        const url = txtUrl + getUserData;
	        
	        getText = function (url, callback) 
	        {
	            var request = new XMLHttpRequest();
	            request.onreadystatechange = function () {
	                if (request.readyState == 4 && request.status == 200) {
	                    callback(request.responseText); // Another callback here
	                }
	                else
	                {
	                    console.log("else > error");
	                    sessionStorage.setItem('praklitut', false);
	                }
	            };
	            request.open('GET', url);
	            request.send();
	        }

	        function mycallback(data) {
	            console.log ("data ==> " +  data);
	            sessionStorage.setItem('praklitut', data);
	            
	            if (data == true) {
	            	return true;
	            }
	            
	            if (data == false)
	            	return false;
	            
	        }

	        
	        //// sessionStorage - if empty call the service.
	        if (sessionStorage.getItem('praklitut') != null) {
	            console.log('sessionStorage >> praklitut is ' + sessionStorage.getItem('praklitut'));
	        }
	        else
	        {
	           getText(url, mycallback); //passing mycallback as a method
	           console.log('sessionStorage >> praklitut is empty');
	        }
	        
		
