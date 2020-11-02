package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.MathFunctions.*;

//This class is immutable
//This class can be extended. Only subclasses have access to a bare constructor. This is so that the subclass can set function properties in somewhere other than the first line. The subclass has to call setParams sometime.
public class SigmoidFunc implements MathFunction {

  private double L; //curve's maximum value
  private double kNeg; //negative of the logistic growth rate
  private double x0; //midpoint of curve

  public SigmoidFunc(final double L, final double k, final double x0) {
    setParams(L, k, x0);
  }
  protected SigmoidFunc() {
    //
  }
  protected void setParams(final double L, final double k, final double x0) {
    this.L = L;
    this.kNeg = -k;
    this.x0 = x0;
  }

  @Override
  public double yValueOf(final double x) {
    return L/(1 + Math.exp(kNeg * (x - x0)));
  }
}