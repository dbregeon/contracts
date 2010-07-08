This is an attempt at providing design by contract functionality to Java developers.

It uses AspectJ and maven2 as the build system.

My goal is to provide two functions.

First enable contract checking on Mock objects (ie verify the call
to the mock respects the contract and that the stubs defined also respect the contract).
This is functional although not entirely satisfactory as it is not integrated in the mock library
(Mockito for the purpose of this project) and so you have to create a Proxy to the Mock. It would be
nicer to tell Mockito to enforce the contracts it finds.

Second enable contract checking during integration testing (or even possibly in production). This is
implemented through an aspect which wraps implementation of contracted methods and verifies pre and 
post conditions.

Currently only pre and post conditions are supported. Contracts are plain java classes. Contracts are
declared through annotations. 

@Contract declares that this class is under contract and the class
implementing the contract is passed as argument. This enables some support for refactoring.
@Preconditioned on a method signals the method as having a precondition. The name of the method is
passed as a parameter. This provides limited support for refactoring when the IDE checks character
strings in addition to language elements.
@Postconditioned on a method signals the method as having a postcondition.

This is only a personal project, feel free to clone and use in your projects. Your comments are much
appreciated and the only thing that really matters. 