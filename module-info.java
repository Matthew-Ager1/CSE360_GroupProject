/**
 * 
 */
/**
 * 
 */
module CSE360_GroupProject {
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires org.mongodb.driver.sync.client;
	requires org.mongodb.driver.core;
	requires jbcrypt;
	requires org.mongodb.bson;
    requires java.sql;
	requires junit;
	exports Core;
	exports Articles;
	exports AccountCreation;
	exports Database;
	exports Database.Models;
	exports HomePage;
	exports LoginPage;
}