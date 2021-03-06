/**
 * Copyright 2019 Association for the promotion of open-source insurance software and for the establishment of open interface standards in the insurance industry (Verein zur Förderung quelloffener Versicherungssoftware und Etablierung offener Schnittstellenstandards in der Versicherungsbranche)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aposin.licensescout.finder;

import java.io.IOException;
import java.io.InputStream;

import org.aposin.licensescout.model.LSMessageDigest;
import org.aposin.licensescout.util.CryptUtil;
import org.aposin.licensescout.util.ILSLog;

/**
 * Base class for implementations of finder handlers.
 *
 * @param <F> type of the entry object (used for detection of file/directory nature)
 * @param <C> type of entry container objects
 * @param <I> type of the entry object (used for creation of entry container objects)
 */
public abstract class AbstractFinderHandler<F, C extends EntryContainer, I> implements FinderHandler<F, C, I> {

    private final ILSLog log;

    /**
     * Constructor.
     * 
     * @param log the logger
     */
    protected AbstractFinderHandler(final ILSLog log) {
        this.log = log;
    }

    /**
     * @return the log
     */
    protected final ILSLog getLog() {
        return log;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LSMessageDigest calculateMessageDigest(final C entryContainer) throws IOException {
        try (final InputStream inputStream = entryContainer.getInputStream()) {
            return CryptUtil.calculateMessageDigest(inputStream);
        }
    }

}