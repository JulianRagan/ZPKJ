package com.jr.extra;

import com.jr.data.UserErrorContainer;

/**
 * Interface for building user error handler on client application side
 *
 * @author jragan
 */
public interface UserErrorListener {

    void errorOccured(UserErrorContainer e);
}
