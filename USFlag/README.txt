The USFlag project creates a window that displays the flag of the United States of America to scale. The
dimensions of every single component in the flag are exact. The window can be resized, causing the flag
to scale.

The requirements for this project are that the flag meets the exact specifications on
http://www.usflag.org/flag.specs.html, and that the code is modular and well documented. My project meets
all of these requirements; as previously stated, the dimensions are almost perfect, the project is split
into three modules, and each file has comments explaining its function.

The project has two main problems: First, the program does not ensure that the flag is to scale once the
window is resized, and, since components have to have integer dimensions, the size of each component may
be a pixel larger or smaller than expected. The second flaw cannot be removed, and I do not know of a
solution to the first.

USFlag is split into four classes: Main, which creates the flag window, FlagFrame, which contains CowPanel,
CowPanel, which contains the flag, and Star, which represents a perfect 5-point star.

The most difficult part of the project was calculating the points of the star, although this was still
fairly simple.

The specifications for the flag dimensions came from http://www.usflag.org/flag.specs.html. All other work
is my own.

-Calvin