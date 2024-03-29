# Pepse Game

============== Differences between 2 UML diagrams - before & after ===================

In the beginning, we read the entire pdf to realize which methods and fields
should be implemented in each class.
At first glance, every relation seems like a dependency relationship, and that is how we
created our first UML diagram.
While we started implementing the classes, we have got a deeper understanding of the relationships and we
found out we should add a new class called "Leaf" which extends "Block" and contains a dependency
relationship with "tree".
In addition, we added a compositions relations between "PepseGameManager" to "Terrain", "Avatar", and "Tree"
– because we can't create a world without those basic classes and We realized that "Terrain" consists and
creates "Block" objects and therefore dependency relationship was added.


========================= Infinite world implementation  =============================

We have chosen to implement the infinite world as follows:

First we have created a world in size of 2 screens, so there will be objects in the current screen and
also for half screen in each direction. We did it in order to avoid extreme cases, for example leaves that
will be left after deleting their stem, and then they will be created again (leaves on leaves).

Secondly, we saved 3 private fields of the game manager class:
* rightTerrianCoord - The rightmost point on the X-axis where game objects were created.
* leftTerrianCoord - The leftmost point on the X-axis where game objects were created.
* curr_place - The last place where the Avatar was when the game was last updated

Then, we overrode the update method and using the 3 fields we mentioned earlier, we could have known how
much the avatar's position changed (using the getCenter method). Depending on the direction the avatar went,
we re-created new objects between the most extreme coordinate in this direction and the same coordinate plus
the position differences, we also deleted accordingly the objects in the same way on the other side.

We will also note that our update function has a condition which ensures us that will update the game only
when the difference of the positions is bigger than half of the screen width, in order to reduce the number
of times we have to go through all the game objects, in this way we are saving unnecessary running time.


========================= trees package implementation  =============================

We chose to implement two classes in this package, the Tree class and the Leaf class.
* The Tree class is responsible for creating the entire tree -  determine the places of tree using random
  function, creating the leaves and creating the stem.

* The Leaf class is responsible for creating instance of a single leaf. Using a few simple methods the class
  is managing the movement of the leaves on the tree, the falling leaves, randomize of the leaf's
  lifetime and also randomize the leaf's death time.
  With the help of all these the leaf enters a cycle of life and death as required in the
  instructions of the exercise.

We did not see the need for additional classes since the trees package has only two main roles which are:
the creation of the trees in the lotteries places, and the management of the life cycle of the leaves.
Therefore 2 classes are sufficient.



================================= Design Dilemmas  ====================================

3 main dilemmas we had:

* The first dilemma was if we should create a class of constants so that we could store there constants that
  are relevant to all classes of the game, such as the seed value or the layers of all the game objects.
  At the end we chose not to implement it since there are fields out there that can stay private and there is
  no need to make them public.

* Another dilemma was whether to add another class that would be responsible for the production of the stem
  but we did not see how this optimizes our work since the stem is just a show of a few blocks,
  and has no special features like the leaves has.

* Another dilemma was whether to make the leaf inherits from GameObject or from Block,
  we finally decided that the leaf would inherit from Brick since it is actually a Block that has special
  properties, such as movement, falling and disappearance.
