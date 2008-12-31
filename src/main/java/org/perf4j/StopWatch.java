/* Copyright Homeaway, Inc 2005-2007. All Rights Reserved.
 * No unauthorized use of this software.
 */
package org.perf4j;

import java.io.Serializable;

/**
 * The StopWatch class is the main object that is used to log timing statements in perf4j. The general usage pattern
 * is to create a StopWatch before a section of code that is to be timed and then stop it before it is passed to a
 * logging method:
 * <p/>
 * <pre>
 * StopWatch stopWatch = new StopWatch();
 * try {
 *     ...code being timed...
 *     log.info(stopWatch.stop("methodBeingTimed.success"));
 * } catch (Exception e) {
 *     log.error(stopWatch.stop("methodBeingTimed.fail"), e);
 * }
 * </pre>
 * <p/>
 * Note that a StopWatch is reusable. That is, you can call <tt>start()</tt> and <tt>stop()</tt> in succession
 * and the <tt>getElapsedTime()</tt> method will refer to the time since the most recent <tt>start()</tt> call.
 * <p/>
 *
 * @author Alex Devine
 */
public class StopWatch implements Serializable, Cloneable {

    public static final String DEFAULT_LOGGER_NAME = "org.perf4j.TimingLogger";

    private long startTime;
    private long elapsedTime;
    private String tag;
    private String message;

    /**
     * Creates a StopWatch with a blank tag, no message and started at the instant of creation.
     */
    public StopWatch() {
        this("", null);
    }

    /**
     * Creates a StopWatch with the specified tag, no message and started at the instant of creation.
     *
     * @param tag The tag name for this timing call. Tags are used to group timing logs, thus each block of code being
     *            timed should have a unique tag. Note that tags can take a hierarchical format using dot notation -
     *            See the {@link org.perf4j.GroupedTimingStatistics#setCreateRollupStatistics(boolean)} method for
     *            more information.
     */
    public StopWatch(String tag) {
        this(tag, null);
    }

    /**
     * Creates a StopWatch with the specified tag and message, started an the instant of creation.
     *
     * @param tag     The tag name for this timing call. Tags are used to group timing logs, thus each block of code
     *                being timed should have a unique tag. Note that tags can take a hierarchical format using dot
     *                notation - See the {@link org.perf4j.GroupedTimingStatistics#setCreateRollupStatistics(boolean)}
     *                method for more information.
     * @param message Additional text to be printed with the logging statement of this StopWatch.
     */
    public StopWatch(String tag, String message) {
        this(System.currentTimeMillis(), -1L, tag, message);
    }

    /**
     * Creates a StopWatch with a specified start and elapsed time, tag, and message. This constructor should normally
     * not be called by third party code; it is intended to allow for deserialization of StopWatch logs.
     *
     * @param startTime   The start time in milliseconds
     * @param elapsedTime The elapsed time in milliseconds
     * @param tag         The tag used to group timing logs of the same code block
     * @param message     Additional message text
     */
    public StopWatch(long startTime, long elapsedTime, String tag, String message) {
        this.startTime = startTime;
        this.elapsedTime = elapsedTime;
        this.tag = tag;
        this.message = message;
    }

    // --- Bean Properties ---

    /**
     * Gets the time when this instance was created, or when one of the <tt>start()</tt> messages was last called.
     *
     * @return The start time in milliseconds since the epoch.
     */
    public long getStartTime() { return startTime; }

    /**
     * Gets the time in milliseconds between when this StopWatch was last started and stopped. Is <tt>stop()</tt> was
     * not called, then the time returned is the time since the StopWatch was started.
     *
     * @return The elapsed time in milliseconds.
     */
    public long getElapsedTime() {
        return (elapsedTime == -1L) ?
               System.currentTimeMillis() - startTime :
               elapsedTime;
    }

    /**
     * Gets the tag used to group this StopWatch instance with other instances used to time the same code block.
     *
     * @return The grouping tag.
     */
    public String getTag() { return tag; }

    /**
     * Sets the grouping tag for this StopWatch instance.
     *
     * @param tag The grouping tag.
     */
    public void setTag(String tag) { this.tag = tag; }

    /**
     * Gets any additional message that was set on this StopWatch instance.
     *
     * @return The message associated with this StopWatch, which may be null.
     */
    public String getMessage() { return message; }

    /**
     * Sends a message on this StopWatch instance to be printed when this instance is logged.
     *
     * @param message The message associated with this StopWatch, which may be null.
     */
    public void setMessage(String message) { this.message = message; }

    // --- Start/Stop methods ---

    /**
     * Starts this StopWatch, which sets its startTime property to the current time and resets the elapsedTime property.
     * For single-use StopWatch instance you should not need to call this method as a StopWatch is automatically
     * started when it is created. Note any existing tag and message are not changed.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        elapsedTime = -1L;
    }

    /**
     * Starts this StopWatch and sets its tag to the specified value. For single-use StopWatch instance you should
     * not need to call this method as a StopWatch is automatically started when it is created. Note any existing
     * message on this StopWatch is not changed.
     *
     * @param tag The grouping tag for this StopWatch
     */
    public void start(String tag) {
        start();
        this.tag = tag;
    }

    /**
     * Starts this StopWatch and sets its tag and message to the specified values. For single-use StopWatch instance
     * you should not need to call this method as a StopWatch is automatically started when it is created.
     *
     * @param tag     The grouping tag for this StopWatch
     * @param message A descriptive message about the code being timed, may be null
     */
    public void start(String tag, String message) {
        start();
        this.tag = tag;
        this.message = message;
    }

    /**
     * Stops this StopWatch, which "freezes" its elapsed time. You should normally call this method (or one of the
     * other stop methods) before passing this instance to a logger.
     *
     * @return this.toString(), which is a message suitable for logging
     */
    public String stop() {
        elapsedTime = System.currentTimeMillis() - startTime;
        return this.toString();
    }

    /**
     * Stops this StopWatch and sets its grouping tag.
     *
     * @param tag The grouping tag for this StopWatch
     * @return this.toString(), which is a message suitable for logging
     */
    public String stop(String tag) {
        stop();
        this.tag = tag;
        return this.toString();
    }

    /**
     * Stops this StopWatch and sets its grouping tag and message.
     *
     * @param tag     The grouping tag for this StopWatch
     * @param message A descriptive message about the code being timed, may be null
     * @return this.toString(), which is a message suitable for logging
     */
    public String stop(String tag, String message) {
        stop();
        this.tag = tag;
        this.message = message;
        return this.toString();
    }

    // --- Object Methods ---

    public String toString() {
        return "start[" + startTime +
               "] time[" + getElapsedTime() +
               "] tag[" + tag +
               ((message == null) ? "]" : "] message[" + message + "]");
    }

    public StopWatch clone() {
        try {
            return (StopWatch) super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new Error("Unexpected CloneNotSupportedException");
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StopWatch)) {
            return false;
        }

        StopWatch stopWatch = (StopWatch) o;

        if (elapsedTime != stopWatch.elapsedTime) {
            return false;
        }
        if (startTime != stopWatch.startTime) {
            return false;
        }
        if (message != null ? !message.equals(stopWatch.message) : stopWatch.message != null) {
            return false;
        }
        if (tag != null ? !tag.equals(stopWatch.tag) : stopWatch.tag != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (int) (startTime ^ (startTime >>> 32));
        result = 31 * result + (int) (elapsedTime ^ (elapsedTime >>> 32));
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}