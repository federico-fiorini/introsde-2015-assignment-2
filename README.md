# IntroSDE Assignment #2: RESTful Services
**Introduction to Service Design and Engineering - University of Trento**

--------------

A RESTful web service in Java to manage people and their health status.

Using JPA (Java Persistence API) to map models to a sqlite database and Jersey to manage REST requests.

The project contains a `client` package to test the server part.

Project based the following [requirements](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/assignments/assignment-2).



####IMPLEMENTATION

#####Server

Standalone HTTP server:

- `rest.lifestyle`: contains the main class to be run to start the standalone server and it's configuration

Main packages:

- `rest.lifestyle.dao`: contains the enum class that manages the connection to the database
- `rest.lifestyle.model`: contains the model classes that map to the database tables
- `rest.lifestyle.resources`: contains classes that represent a resource in REST 

#####Client

- `rest.lifestyle.client`: contains the main class to be run to test the server with all the requests in both XML and JSON and to write the log files
- `rest.lifestyle.client.util`: contains some helper classes to parse and format XML and JSON strings
	
####HOW TO RUN IT
	# Clone the code from this repo
	git clone https://github.com/federico-fiorini/introsde-2015-assignment-2.git
	cd introsde-2015-assignment-2
	
	# To run the server
	ant run
	
	# To run the client
	ant execute.client

######Deployed on heroku server:
	https://damp-cliffs-5416.herokuapp.com/rest

######Worked in pair with Daniel Bruzual:
	Git: https://github.com/djbb7/introsde-2015-assignment-2
	Heroku: https://powerful-thicket-8477.herokuapp.com