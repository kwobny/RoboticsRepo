package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.functiontypes.Consumer;

//This interface describes the for each method present in java 8.

public interface Foreachable<T> {
  public void forEach(Consumer<? super T> action);
}