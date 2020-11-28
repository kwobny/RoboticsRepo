package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

//This class is the element which is stored in a binding full pile.
//Each element can only belong to 1 pile.
public abstract class BoundElement<T extends BoundElement<T>> {

  //these properties have default access
  boolean needsRemoving = false;
  boolean isInPile = false;

  BindingPile<T> ownerPile;

  //Utility methods accessible to any subclass

  //Use this method with caution. This method does not actually determine if the element is in any pile.
  //This method only determines if the element is currently in the binding pile that it is bound to. That means that it cannot be used to determine if it is in any other pile (such as a hashset pile) and should probably only be used if you are sure that the element is going to stay in 1 single pile.
  //If you have access to the target pile, it is better to just use the contains method.
  //Used only in cases where you would need an "is active" sort of variable, basically to avoid variables with duplicate intentions and to save space.
  protected boolean getIsInPile() {
    return isInPile;
  }

  protected BindingPile<T> getOwnerPile() {
    return ownerPile;
  }
  
}

/*
To subclass:
public class {NAME} extends BoundElement<{NAME}>

Example:
public class SubClass extends BoundElement<SubClass>
*/