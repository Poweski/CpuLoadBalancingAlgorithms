# CpuLoadBalancingAlgorithms

Simulation of a distributed processor load balancing algorithm. There are N identical ones working in the system
processors. New tasks (processes) appear on each of them, with DIFFERENT frequencies and DIFFERENT
requirements (each process requires a specific, different share of the processor's computing power, e.g. ~3%).
Simulate the following allocation strategies:

A task appears on processor x. Then:

### Strategy 1
x asks a randomly selected processor y about the current load. If it is less than the threshold p, the process is there
sent. If not, we draw and ask the next one, trying at most 10 times. If all are drawn
loaded above the threshold p, the process is executed on x.

### Strategy 2
If the load x exceeds the threshold p, the process is sent to a randomly selected processor y
with a load less than p (if the drawn y has a load>p, the drawing is repeated until successful). if not
exceeds - the process is executed on x.

### Strategy 3
As above, except that processors with a load lower than the minimum threshold r ask randomly selected ones
processors and if the load of the questioner is greater than p, the questioner takes over some of his tasks (assume which).

### Tips:
Carry out a simulation of strategies 1,2,3 for N=approx. 50-100 and a long series of tasks to be performed (select the parameters
independently, so that everything works :). In each case, report as the result:
1. Average load on processors (decide wisely how it will be calculated).
2. Average deviation from the value in point 1 above.
3. Number of load queries and process migrations (relocations).
4. The user should be able to provide (change) the value of p,r,z,N.
