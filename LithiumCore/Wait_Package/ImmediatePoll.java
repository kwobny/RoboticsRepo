package org.firstinspires.ftc.teamcode.LithiumCore.Wait_Package;

public enum ImmediatePoll implements WaitCondition {
  obj;

  @Override
  public boolean pollCondition() {
    return true;
  }
}