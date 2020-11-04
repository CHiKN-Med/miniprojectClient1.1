# The Quiz
The following repository is the client for group 301's quiz game. To run the program, follow the steps below.

1)
The server must be run first (https://github.com/CHiKN-Med/miniprojectServer). Run the server class, and the hosts local IP will be output in the terminal. Do note in order to use multiple computers, the computers have to be connected to the same network as the host (either via hotspotting or a router. Due to security reasons, eduroam cannot be used).

2)
Run the client's main class and enter the IP of the host. If desired, the user which hosts the server on their computer can write "localhost" in their client to join their server. After that, the user will be prompted to enter a username.

3)
Once a username has been set, the user will enter the chat lobby. In here the user can chat or start the quiz which will prompt every user into the quiz.

4)
In the quiz, the user will be presented a question with 4 buttons to select the answer. Once the quiz is complete, a waiting screen will be prompted for the clients that has finished, until all clients finish the quiz. Once everybody has answered, the window will display the winner and a leaderboard with all users.

