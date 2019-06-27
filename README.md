1. Install JenaFuseki 3.10 or higher
- Default settings, port 3030 
- Launch fuseki-server.bat
	Test access with http://localhost:3030 or http://localhost:3030/inferred
2. Install Triple Store Parliament 2.7.10 or higher
- Default settings, port 8089
- launch StartParliament.bat
	Test access with http://localhost:8089/parliament/
- Insert data in default graph (see in this repository "src/mail/resources/")
  - SHOPPING_CENTRE_WGS84.n3 
  - county.ttl	
- Create indexes (possible that it already ran)
3. Install tomcat 8.5.37 or higher
- Build war file
- Insert war in webapps
  default: http://localhost:8080/LinkedDataApplication/index
