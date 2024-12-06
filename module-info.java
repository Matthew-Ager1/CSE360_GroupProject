/**
 * 
 */
/**
 * 
 */
module CSE360_GroupProject {
	// Required JavaFX modules
   requires javafx.controls;
   requires javafx.graphics;
   requires javafx.base;
   // External libraries
   requires org.mongodb.driver.sync.client;
   requires org.mongodb.driver.core;
   requires org.mongodb.bson;
   requires jbcrypt;
   // Export the Core package to javafx.graphics
   exports Core to javafx.graphics;
}
