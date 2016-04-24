# ga-bin-packing
##A Genetic Algorithm to Solve the Bin Packing Problem

###Genetic Parameter Explanation:

  __int popSize:__ This number determines how large the population of solutions should be while solving a given problem.
  
  __int dominants:__ This number determines how many of the solutions in a population are dominant. 
    Dominant solutions will pass on their "genes" with a higher frequency than regular solutions. 
    Dominance is directly related to the fitness of a solution.
    
  __double domRate:__ This decimal number should be between 0 and 1 and it determines how dominant the dominants are. 
    If "1" is passed to this parameter, then the most dominant solution will mate with all other solutions. 
    If ".5" is passed, then the most dominant solution will mate with half of the population (in order of their fitness). Then the next most dominant will mate with one less than the previous dominant and so on.
    If "0" is passed, then no dominance is in effect, and the mating would be entirely at random.
    
  __double mutationRate:__ Should be between 0 and 1 and it determines the rate at which mutations happen. 
    Passing "0.5" would result in a 50% chance of mutation.
