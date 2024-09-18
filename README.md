<H1> TUC Chess AI agent</H1>
This repository holds the enviroment and the agent of TUC-CHESS that was designed and developed for the purposes of the course "Artificial Intelligence" during the 2022 year. 

The chess game environment was provided is not conventional chess, it uses the dimensions of 6x8 it generates random prizes, has special rules, such as time restriction per move.

There are 2 different approaches on the way the agent makes desicions.

The first one is using the Min-Max algorithm with an extension of alpha beta pruning, to reduce the search space and enable it to search deeper inside the tree structure.

The second game playing logic was created by implementing the algorithm of Monte Carlo Tree search with an Upper Confidence bound.
<H2> Pieces </H2>
The pieces found in this special chess are the following:

- ![image info](./chess/king_black.gif), ![image info](./chess/king_white.gif) A king for = 8 points 
- ![image info](./chess/pawn_black.gif) ![image info](./chess/pawn_white.gif) 7 pawns worth = 1 point each 
- ![image info](./chess/rook_black.gif) ![image info](./chess/rook_white.gif) 2 rooks = 3 points each
- ![image info](./chess/prize.png) and prizes, that are neutral

<H2> Rules of TUC chess </H2>
The special chess board found in this game is made of 7 rows and 5 columns.
<H2> Installation </H2>
