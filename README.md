# Chatting_Application is a simple chat server application with a graphical user interface (GUI) for communication between a server and a client.

# Server Class
  This is the main class for the server application. It initializes the GUI and contains methods for starting the server, managing input/output streams, and handling messages.

# Main Server Class
  This class contains the main method, which creates an instance of the Server class and starts the server by calling the startServer method.

# Client Class
  This class represents the client-side of the chat application.
  It establishes a socket connection to a server using a user-input IP address.
  The GUI is created using Swing, featuring a text area for displaying messages, a text field for typing messages, and a chat sound effect played when receiving a message.
  It uses DataInputStream and DataOutputStream to handle communication with the server.
  A separate thread continuously reads messages from the server and displays them in the text area.

# Main Client Class
  Contains the main method for launching the client application. Instantiates the Client class, sets up I/O streams, and initializes the GUI.


This program sets up a basic chat server with a GUI. 
It can send and receive messages between the server and a client, displaying them in a chat area and playing a sound when a message is received. 
The server is started and waits for a client to connect to it.
