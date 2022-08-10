	public static boolean submitPraklitutService(PluginLogger logger, HttpServletRequest request) throws IOException {

		String methodName = "execute";

//		JSONObject commonData = JSONObject.parse(request.getParameter(COMMON_DATA));
//		System.err.println(commonData.toString());

//		String stUser = commonData.get("loggedInUsername").toString();
//		System.err.println("------------" + stUser);
		String stUser = request.getParameter("loggedInUsername");
		String stURL = "https://rmiapplictest/RmiTashtiotWS/api/GroupsApi/IsUserExistsInGroup?groupCode=1002&userName="
				+ stUser;
		URL obj = new URL(stURL);

		HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
		httpURLConnection.setRequestMethod("GET");

		System.err.println("con ==> " + httpURLConnection);
		System.err.println("------");

		BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
		String inputLine;

		String output = "";

		while ((inputLine = in.readLine()) != null) {
			output = inputLine;
			System.err.println(inputLine);
		}

		System.err.println("------");
		System.err.println("output-----" + output);
		System.err.println("------");

		in.close();

		int responseCode = 0;
		responseCode = httpURLConnection.getResponseCode();

		System.out.println(">>> " + httpURLConnection.getResponseMessage());

		System.err.println("POST Response Code :: " + responseCode);

		System.err.println("responseCode  " + responseCode);

		logger.logError(PraklitutService.class, methodName + "jaron is ==== " + stUser, " >>>> " + " <<<< ");

		logger.logError(PraklitutService.class, methodName + "submitPraklitutService",
				"----[submitPraklitutService]---- = " + responseCode + " ----[user]---- =  " + stUser + ">>>>");

		if (output.equalsIgnoreCase("true")) {
			return true;
		}
		else
		{
			return false;
		}
		
		//return false;
	}
