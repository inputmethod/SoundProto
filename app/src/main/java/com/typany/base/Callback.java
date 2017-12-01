package com.typany.base;

/**
 * A callback that use a result and may throw an exception.
 * Implementors define a single method with one argument called
 * {@code call}.
 *
 * <p>The {@code Callback} interface is similar to {@link
 * Runnable}, in that both are designed for classes whose
 * instances are potentially executed by another thread.  A
 * {@code Runnable}, however, does not return a result and cannot
 * throw a checked exception.
 *
 * @param <V> the method argument {@code call}
 */
@FunctionalInterface
public interface Callback<V> {
    /**
     * Use the result, or throws an exception if unable to do so.
     *
     * @throws Exception if unable to use the result
     */
    void callback(V result) throws Exception;
}