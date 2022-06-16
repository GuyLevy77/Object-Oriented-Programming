# Tic Tac Toe

Strategy for WhateverPlayer:
Passing all board and insert all of the empty squares to a new array.
When we have an array just with empty squares all we need to do is to
raffle one of the squares and fill it with the current player's mark.

Strategy for CleverPlayer:
The main idea is trying to create a line's streak.
For that - we pass with a for loop and try to fill squares line by line.

Strategy for SnartypamtsPlayer:
The main idea is trying to create a col's streak.
We begin from the second col in order to block the CleverPlayer if he began.
After this we have an advantage and we just need to keep fill the board col after col.


=============== Tournaments with 500 rounds ===============

Tournament between WhateverPlayer to CleverPlayer:
=== player 1 (WhateverPlayer): 29 | player 2 (CleverPlayer): 471 | Draws: 0 ===

Tournament between CleverPlayer to SnartypamtsPlayer:
=== player 1 (CleverPlayer): 0 | player 2 (SnartypamtsPlayer): 500 | Draws: 0 ===

Tournament between WhateverPlayer to SnartypamtsPlayer:
=== player 1 (WhateverPlayer): 12 | player 2 (SnartypamtsPlayer): 488 | Draws: 0 ===

Tournament between WhateverPlayer to WhateverPlayer:
=== player 1 (WhateverPlayer): 261 | player 2 (WhateverPlayer): 237 | Draws: 2 ===

Tournament between CleverPlayer to CleverPlayer:
=== player 1: 250 | player 2: 250 | Draws: 0 ===

Tournament between SnartypamtsPlayer to SnartypamtsPlayer:
=== player 1: 250 | player 2: 250 | Draws: 0 ===

=============== Tournaments with 10000 rounds ===============

Tournament between WhateverPlayer to WhateverPlayer:
=== player 1: 4959 | player 2: 4987 | Draws: 54 ===
