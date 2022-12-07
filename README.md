# Chesstacular
**By**: `Ali Sartaz Khan`, `Jonathan Houge`, `Julius Ramirez`, and `Khojiakbar Yokubjonov` \
**Assigned TA:** `Shrey Goel`\
**Trello Board:** [**Here**](https://trello.com/w/335project) \
**Video Link:** [**Here**]()
## How to run
* Run `./335 Project/src/Client.java`
* If `Remote play` enabled: Run `./335 Project/src/Server.java` first
* Modes:
  - `Local`: One client
  - `Remote`: Two Clients and One Server (Can play on same computer/network)
  - `Robot`: One Client vs AI Robot
 * Watch the video to see a [**Full Demo**]() on how to run all the features
## Features
* `8x8` board with all pieces arranged with initial state
* **White** makes the first move
* Friendly piece collision detection 
* Move Functionality special cases:
  - **Pawns**: En passant and Pawn Promotion
  - **Castling**
  - **Check/Checkmate**
* Alternate Play
* Board highlights possible moves and currently selected piece
* Recognizes end of game conditions
* Functionality to play again

## Wow Factors
* Saving and loading multiple games: Has a directory `Saved Games` that stores previous games
* Built-in `Robot` implementation
* Timed game mode for `Remote` & `Robot` play \
(The timer at the bottom of the UI may not be visible if the screen resolution is too small.\
We have noticed that the bottom timer was invisible on the `1366 X 768` resololution, but it works well on the `1980 X 1080` resolution.)



