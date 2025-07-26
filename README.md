# FRAIM  
FRAIM, or Firearm Risk and Interdiction model, is a neural network designed to run on 3D printer mainboard hardware to detect and halt the printing of firearms in cases where it is illegal. It uses TFLite Micro to run inference on a range of microcontroller hardware commonly used in 3D printers, serving as a proof-of-concept for potential future implementations.  

**Why am I building this?**  
I am building FRAIM to provide a prototype mitigation strategy for the rise in the 3D printing of illicit objects, such as firearms.

**What is the end goal?**  
A C header file containing a PointNet TFLite micro autoencoder model, along with supporting GCode parsing logic able to run on microcontroller targets.

*because of the questionable legality and limited number of firearm models available on the internet,* I'm using the **3D Benchy** and its numerous variations as a proxy for the target class (i.e., firearm) in this case.