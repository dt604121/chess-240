## Phase 0 
~ Set up based on the video ~
Commit #1 & #2 - ChessBoard.java / ChessPosition.java / ChessPiece.java
* implement the constructor + getters/setters / generating the overrides for the following classes: 
ChessBoard, ChessPiece, ChessPosition, ChessMove
1) ChessBoard.java - implement the constructor / data structure for the squares
2) Implement addPiece/getPiece making sure to compensate for the fact that the board is 1-8 but in 
CS it's 0-7 so we need to subtract one from the row and the column.
3) Implement resetBoard by reinitializing the board to clear it and then setting up each of the chess
pieces. Be sure to generate/include the overrides for toString, equals, and hash to make sure that the 
object methods are taking into account row/col.
4) ChessPiece.java - Create the PieceMovesCalculator interface file. Use switch statements to check the
different cases for the chess pieces, creating the neccessary calculator for each (as well as the
file that goes along with it). 
5) ChessPosition.java - Initialize row/col as well as the getters/setters. Be sure to generate/include the 
overrides for toString, equals, and hash to make sure that the object methods are taking into account row/col.
6) ChessMove.java - Initialize the constructor as well as the getter functions. Generate Overrides.

Now that things have been setup and initialized the meat of the program is in implementing the PieceMovesCalculator 
Interface along with the classes for each chess piece.

1) For PieceMovesCalculator we can create the file and then hover over it to add the methods. Go forth and create files
for the rest of the individual pieces making sure they implement from the interface -> and hovering again.
Intermediary step because these actually require logic. Psuedo code the heck out of it. The goal is to use the current 
position to calculate all valid moves for that piece. Looking at the tests also provides much needed clarity and also
needed edge cases / scenarios to consider. 
~ For each piece read through how it moves and get the specs. Using that we will loop through the neccessary times
(multiple for loops neccesary more on this later). Specifically think through the row / col movements and how things
are getting added and subtracted.. keeping color in mind oc. Continue to calculate the valid moves and adding them to 
the list unless the following happens:\
   a) we are about to run into a friendly piece depending on color..\
   b) we reach the edge of the board..\
   c) we can capture an enemy..
2) Bishop Piece: need all 4 parts of the x -> upper left, upper right, lower left, lower right
we need to keep track of both the current pieces position along with what the original's was..
3) 























## Phase 1 

## Phase 2

## Phase 3

## Phase 4

## Phase 5

## Phase 6