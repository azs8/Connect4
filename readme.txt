The main program can be re-compiled using :
	javac connect.java
The main program can be run using :
	java MainGame

Optional arguments include :
	-h
		This will cause player 1 be a human player and accept user input. To place a
		piece in the nth column, just enter n. The first column is column 0, the last, 6.
		Without this option, two AI opponents will play each other. The p1 commands do
		nothing when a human is playing.
	-p1offense
		This will cause player1 to more highly value offensive plays.
	-p1defense
		This will cause player1 to more highly value offensive plays. In the case that
		both p1offense and p1defense are arguments, player1 will value offense and
		defense the same.
	-p1depth2
	-p1depth4
	-p1depth8
		Sets the depth of the minimiax search algorithm for player1. The default depth
		is 6. If multiple commands are entered, the last depth entered is chosen. 
	-p2offense
		This will cause player2 to more highly value offensive plays.
	-p2defense
		This will cause player2 to more highly value offensive plays. In the case that
		both p1offense and p1defense are arguments, player2 will value offense and
		defense the same.
	-p2depth2
	-p2depth4
	-p2depth8
		Sets the depth of the minimiax search algorithm for player2. The default depth
		is 6. If multiple commands are entered, the last depth entered is chosen.
	-p1NoDefaultMove
		Prevents player1 from automatically playing in the center column.
	-p2NoDefaultMove
		Prevents player2 from automatically playing in the third column.