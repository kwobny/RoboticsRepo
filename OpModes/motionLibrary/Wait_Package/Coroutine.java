package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package;

public abstract class Coroutine implements Callback {

  //data members

  public Callback callback;
  public final WaitCondition condition = new WaitCondition() {
    @Override
    public boolean pollCondition() {
      return isDone;
    }
  };

  private boolean isDone;
  private final WaitTask task = new WaitTask();
  protected WaitCore waitCore;

  {
    task.autoEndTask = false;
  }

  public Coroutine(final WaitCore waitCore) {
    this.waitCore = waitCore;
  }
  public Coroutine(final WaitCore waitCore, final Callback callback) {
    this.waitCore = waitCore;
    this.callback = callback;
  }

  @Override
  public final void run() {
    _start();
    waitCore.setTimeout(task);
  }

  protected final Callback endCallback = new Callback() {
    @Override
    public void run() {
      isDone = true;
      waitCore.removeTimeout(task);
      _end();
      if (callback != null) {
        callback.run();
      }

    }
  };

  protected final void setCondition(final WaitCondition condition) {
    task.condition = condition;
  }
  protected final void setCallback(final Callback callback) {
    task.callback = callback;
  }
  protected final void setNext(final WaitCondition condition, final Callback callback) {
    task.condition = condition;
    task.callback = callback;
  }
  protected final void setRunWhile(final Callback callback) {
    task.runWhile = callback;
  }

  //these two abstract methods are meant to be private and only accessible to/used by the superclass.
  protected abstract void _start();
  protected abstract void _end();

}

/*
Example implementation:

public class Example extends Coroutine {

  public Example(final WaitCore core) {
    super(core);
  }

  @Override
  protected void _start() {
    //initialize everything (next condition and callback) and do some stuff
    setNext(condition, callback);
  }

  //some intermediate callbacks

  //last callback before end
  private final Callback secondToLast = new Callback() {
    @Override
    public void run() {
      //do some stuff
      setNext(condition, endCallback);
    }
  };

  @Override
  protected void _end() {
    //clean up stuff
  }

}

*/