# FRAIM
Edge implementation of PointNet Neural Network architechture through TFLite Micro to identify malicious GCode in 3D printer instructions.

**Why am I building this?**
I am building FRAIM to provide a prototype mitigation strategy for the rise in the 3D printing of illicit objects, such as firearms.

**What is the end goal?**
A C header file containing a two-class PointNet TFLite micro model, along with supporting GCode parsing logic able to run on microcontroller targets.

*because of the questionable legality and limited number of firearm models available on the internet,* I'm using the **3D Benchy** and its numerous variations as a proxy for the target class (i.e., firearm) in this case.