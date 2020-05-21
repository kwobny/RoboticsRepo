# RoboticsRepo
[![Run on Repl.it](https://repl.it/badge/github/kwobny/RoboticsRepo)](https://repl.it/github/kwobny/RoboticsRepo)

# How to resolve repl it git issues:
Not committing:
1. Open the repl it bash shell by pressing Ctrl+Shift+P, searching "open shell" and clicking it
2. Try to commit the changes. Do this by typing "git commit -a -m 'whatever commit message'". the -a flag says commit all changes, and the -m flag represents the commit message.
3. If you get an error that says something like another process is running in the repository, try to resolve the issue by deleting the index.lock file in the .git directory. Type in the shell, "rm -f .git/index.lock", and execute. Then repeat step 2 and see if the issue is resolved
4. Now, push the committed changes from the local repository into the remote repository. Execute "git push origin 'whatever branch name'".
5. The issue should be resolved

How to delete a branch from local repository: execute "git branch -d 'branch name'"

If all else fails: just make a new repl it and reconnect it to github. Make sure that the .replit file has correct information. if not, then delete it

# Notes:
Main move commands are rotate robot and moveLinTrans. Main wait commands are wait for time, for distance or displacement traveled (you pick which quantity), and for angle traveled/for certain orientation passed.

One complex wait command you can write is wait for degrees traveled in turn drive/rotational translate. This would use wait for distance/displacement

This library is easily applicable to robots with the same library/MadHardware api (would need to rewrite/look over whole code if not), with the same number of wheels (need to rewrite motor buffers and a whole bunch of stuff), and with the same type of wheels (need to rewrite lin trans move if not), and with the robot going forwards with positive power on the wheels.

---

How the code library classes are structured (elements listed in order of where they appear in class definition, with 1 being at the top and 3 being at the bottom):
1. class wide settings variables and normal variables
2. resource objects
3. class methods

What the main class is in charge of: the main class is basically the hub for all of the individual classes, and is the way the user interacts with library. the main class also is in charge of **setup of the library and the looping of the library.**

A wait callback of value 0 is no callback

When modifying any of the values in the individual motor buffers, always do it with the intent of syncing the motors right after the action occurs. Do this so that individual motor buffer values stay consistant with the universal buffer.

When using the library in teleOp, make sure to call the loop command once every teleOp loop. Also be mindful that the loop command probably waits for some short time before resuming execution.

At the end of the autonomous, make sure to call the endProgram function. This should be the absolute last function called. If you need some extra functionality, the function only consists of a while true loop with the loop function executing inside it. The only wait functions that would be useful after the function is called is set timeout and set interval.

For translations: positive x is to the right of robot, and positive y is to the front of robot

For rotations: positive degrees is clockwise, negative degrees is counterclockwise. All is relative to the current heading of robot.

Wait system functionality:

There are 6 different functionalities, 4 which are built in commands and 2 which (are similar) and are used for custom waits:
1. Simple wait: waits for one condition and pauses all code execution until satisfied
2. Complex wait: waits for multiple conditions with a choosable completion condition. Executes a callback after each condition ends, pauses all code until completion condition (either AND or OR) is reached.
3. Set timeout: sets a timeout. is like simple wait, but does not block code execution.
4. Set interval: sets a wait to start over again after it completes. does not block code execution.

The 2 more functionalities are:
1. Poll and generate data
2. custom/manual waits.

To use custom waits:

just put a while loop in your code, with the wait as your condition. Make sure to flip the output of the poll. Inside the loop, make sure to call the loop command from the motion library.

When polling manually for waits, first call the generate data command through the .waiters.generateData function, then poll. a value of true means condition is complete, false means the wait is still continuing.

**Notes about RPS**
1. When setting state, you need to explicitly call saveCurrentPosition, or have the second parameter of the first call to state setting function be true.
2. When you are measuring multiple quantities, it is helpful to call coreDistFunc explicitly, so that it is only called once and extra computation time is not wasted.

# Todos for Todos:
Create an algorithm to isolate lintrans component from any set of buffer values, for the wait for displacement.

Find how to use the lintrans motor buffer, combined with the lintrans component of the change in distance to generate an rx and ry value, which also accounts for error values in the change in distance buffer.

Find how to get time taken to traverse lengthened distance, for ILI.

# Todo:
Think of integrating PID control into the robot's movements.

IMPORTANT: Consider using the java object wait method, or the Thread.sleep method to execute code in a small period of seconds. Look up the difference between the 2 methods. This will be separate from the wait for time implementation, and will only be used to execute polling code as often as necessary. This will not be used when the robot waits during autonomous. For example, this can be used to execute motorCali every interval instead of the ineffecient polling wait for time command.

If using the above methods, then you will need to take the things executing periodically in execute interval for the system itself, and the methods executing in the loop that are part of they system, and put it in the thing.

You will only need to add one part of code in the loop function, a sleep function in the loop. Make sure the time slept is much less than high frequency maint period

You will also need to change the access modifiers of involved methods as fit.

Also consider using the sleep() and idle() commands that (might) are built into the ftc library itself (linear Opmode)

Also consider making a full on multithreaded version of the motion library. If doing this, keep in mind the changeInTime value for the wait for time function

Also think of retesting the java reflection library, maybe missed something:

Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
                translucentConversionListenerClazz);

Maybe forgot the second parameter in get declared method call. Here is the link to the actual example code: https://www.programcreek.com/java-api-examples/?class=java.lang.reflect.Method&method=setAccessible

---

Clean up the notes section, and sort them into operation imperative/required notes, and suggestion notes.

Think of making wait for time exempt from the pause code execution.

Tweak maint period values

Consider making wait for distance execute every period of seconds instead of in the loop, to make code more efficient and fast.

If all the wait arguments are doubles, then switch the wait data types from Object to double.

Think about making subpackages in the library to reduce clutter

Think about having separate wait condition classes, one for each subsystem (RPS, SCS, Sensors, Arm, etc.) and one common wait core class to process them all. In this way, all of the waits are not stored on one class.

When designing RPS, choose what point on the robot will be considered its position (most likely going to be center of robot in between wheels)

Make sure that the upload motors function only calls saveCurrentPosition if the amount of ticks traveled by 1 wheel exceeds a certain value. Or, in saveCurrentPosition, make sure that the position is saved only if a certain amount of distance/displacement is covered between the last save.

Consider making interval wait be based upon set timeout instead of being its own thing

In motorcali, make a system which detects if a motor is being held back or stopped, by tracking the distance to power ratio of each of the 4 motors over time to see if they fluctuate. Do this to prevent motor burnout. If the system determines that this is happening, make it stop the robot, and reset the motor accelerations, speedfactors, and motor values immediately, until something like a button is pressed.

For teleop, make a coasting system which slowly decelerates the robot when the controller is in the middle. Also make the robot accelerate slowly instead of abruptly speeding up.

For teleop, make a control model where one of the joysticks translates the robot in any direction, and the dpad strafes or drives in 4 distinct directions. Make one of the 2 forward buttons a toggle switch for driving translating to a certain constant angle, or translating relative to the heading of the robot. Make the other button a toggle switch for coasting accelerate and decelerate. also have the abcd pad have buttons to activate certain features.

In the control model, the gunner also controls the rotation of the robot, but this control is overrided when the driver turns the robot, or something else etc.

One of the features will be a thing which automatically sets the relative translating angle, using a distance sensor, according to the slant of the object in front of it. Also have it auto rotate the robot towards the slant.

Make a toggle which controls if the robot drives slowly or fastly.

---

Make wait for distance have an Enable Incremental Linear Interpolation option which allows you to slow down to the next speed that is inputted at the end. Accomplish this by calculating the scale factor (shown below) and setting the universal buffer's motor value multiplier every period/so often to decrease slowly. Make sure to reset this to 1 when done with maneuver/traveled full distance.

For ILI, make it clear in code, instructions, and documentation that the ILI is only to be used when the magnitude of the linear translation component stays constant. Direction can be non-constant, but the speed of the robot should be constant when calculating for the time taken to travel a distance.

Also determine your input units for the ILI.

Make ILI input be 1 double for speed (includes a final speed of 0), and 2 doubles for speed in x and y direction. There will be no getting values from any aux buffer because it was deemed unnecessary.

# Ideas for wait commands:
For wait for distance, this is an idea: whenever the poll for the wait distance occurs,
1. Get the change in the distance (displacement) traveled by each motor. If getPos command for each motor returns the wheel angle, then get the change in the angle (watch out for crossing over from 360 to 0 degrees), and multiply it by a constant (2 * pi * r?) to get change in distance.
2. Multiply that change by the amount of power the linear translate contributes for that wheel divided by the universal value for that wheel (which would be linTrans/Universal, also take into account the scale factor for the lintrans buffer and how it is considered when adding lintrans to universal). This will extract the linear translation component of the distance traveled. Because this ratio may always change due to a change in linTrans command (for example rotational translate), this is the reason why the total distance must be measured in changes, instead of the whole thing.
3. Take all four of these change values, and compare them against the current linear translate power values and rx and ry, in some way to come out with a dx and dy value. Make sure to account for inconsistancies in motor readings (maybe take averages for certain quantities?).
4. Take the dx and dy value and find the resultant of these using pythagorean theorem. This will give you the total distance traversed by the robot from the last time the poll was executed. Sum up all of these change in distance values to get the total distance traversed by the robot since the start.
5. Compare this total distance with the distance condition given, and return either true or false.

Additional idea:
Have a value titled totalDistance. Make sure to reset this at the beginning of every wait for distance thing.
whenever syncMotors executes, have it execute a function that, if in a distance wait state, gets the distance traversed from the last time syncMotors was called, to now. This will happen using the steps listed above. Do this step before the function executes motorCali and actually uploads values to the wheels.
Add the distance traversed to total distance.
Whenever the actual distance poll executes, measure the change in wheel position from the last syncMotor command. Use these values to calculate the displacement from last syncMotor command, but do not add it to the total distance variable. only use it and the total distance to calculate total distance as of now.

Another idea:
Alongside wait for distance traversed, also make a function which waits for displacement. Do this by getting the change in each wheel distance from the start of the maneuver, and using some sort of algorithm (develop this) to isolate the linear translate portion of the distance. Use this then to find the displacement from the starting position using inverse of the lintrans command.

Robot Positioning System (RPS):

is a system which lets you find displacement from starting point or from any point relative to start. Also would let you shift/translate reference point. Also would have a function which returns total distance (not displacement) traveled from start

If not using new method of isolating lin trans, then when isolating linear translate part of distance, watch out for if the universal buffer part of wheel value is 0, to avoid dividing by 0. Also check if the whole ratio is 0, because then the distance traveled by that motor must be 0.

Think of using gyro for finding rotational component of change in wheel distances

Also think of using the built in distance/acceleration tracker to compare with distance traveled from the wheels.

Make a way to shift the orientation/angle of the coordinate plane/y axis, alongside shifting the origin point.

Alongside a shift origin command, maybe have a shift robot position command for calibration

A potential system would be a function to change the position of the robot in relation ot the origin, and a function to change the robot's orientation.

Consider having a separate class for reading robot position that is similar to ellapsed time, in the way that you can reset it without affecting other instances of distance readers.

Decide whether sync distance will execute every number of ticks traveled or on every sync motor command.

The two scenarios to test are: the robot always changing linTrans component with no rotation, and the robot going in a direction while turning.

In the turning scenario, find a way to find displacement, accounting for the rotation. Do this because there could be a forward lintrans component, while having a non-forward displacement.

Make a system for getting the **distance** traveled in the x and y direction, alongside the total distance.

Find some way to connect the rotational difference threshold to the motor cali factors and to the total error of the wheel encoders themselves.

Make sure that saveDistances only executes when at least one wheel travels enough distance.

Make code which handles situations where the wheel error value is determined to be too large, and make a way to detect such error, and classify it against a threshold. To sum it up, still need to work on interpreting the difference between the 2 rotational values, and what they mean in relation to the error. Also need to work on the error threshold, and what to do if that threshold is exceeded.

Maybe find some way to determine the maximum error of the displacement and distance values using the angular displacement from gyro in conjunction with the difference in rotational values

New method for syncing distance:
1. In every sync motor command, call the sync distance function, as long as the number of ticks traveled by at least one wheel exceeds a threshold value.
2. Firstly, take the distances traveled by each wheel and put them in some sort of formula (motor buffer independent) to isolate rotational and translational components
3. Then put these components into an algorithm to determine the total distance traveled, and also the displacement. Make sure to account for rotation of robot when calculating for displacement.
4. Now you are done

Speed Control System (SCS?):
allows you to adjust the universal speedFactor in complex ways as a piecewise function with a variable x axis for each piece.

This system will have advanced pieces such as PID control

consider moving the common acceleration system to this class

Make sure to look over code in upload motors which controls how the function reacts to overflows in motor value.
