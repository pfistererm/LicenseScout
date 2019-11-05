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

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aposin.licensescout.license.License;
import org.aposin.licensescout.license.LicenseStoreData;
import org.aposin.licensescout.util.ILFLog;
import org.aposin.licensescout.util.JavaUtilLog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Base class for subclasses of {@link AbstractFinder}.
 * 
 */
public abstract class BaseFinderTest {

    private ILFLog log;
    private LicenseStoreData licenseStoreData;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Logger.getGlobal().setLevel(Level.FINEST);
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINEST);
        Logger.getGlobal().addHandler(consoleHandler);
        // Handler[] handlers = Logger.getGlobal().getHandlers();
        // handlers[0].setLevel(Level.FINEST);
    }

    @Before
    public void setUp() throws Exception {
        log = new JavaUtilLog(Logger.getGlobal());
        licenseStoreData = createLicenseStoreData();
    }

    /**
     * @return the log
     */
    protected final ILFLog getLog() {
        return log;
    }

    /**
     * Reads license information from the file 'src/test/resources/scans/licenses.xml'.
     * 
     * @return a license store data object
     * @throws Exception
     */
    private LicenseStoreData createLicenseStoreData() throws Exception {
        final LicenseStoreData licenseStoreData = new LicenseStoreData();
        licenseStoreData.readLicenses(new File("src/test/resources/scans/licenses.xml"), null, false, getLog());
        return licenseStoreData;
    }

    protected final LicenseStoreData getLicenseStoreData() {
        return licenseStoreData;
    }

    /**
     * Creates a finder and executes the license scan.
     * 
     * @param scanDirectory
     * @return the finder result
     * @throws Exception
     */
    protected FinderResult doScan(final File scanDirectory) throws Exception {
        final AbstractFinder finder = createFinder();
        return doScan(finder, scanDirectory);
    }

    /**
     * Creates an initializes a finder class.
     * 
     * <p>Subclasses are required to implement this
     * method to create an instance of a specific finder class.</p>
     * 
     * @return a finder class instance
     */
    protected abstract AbstractFinder createFinder();

    /**
     * @param finder a finder instance
     * @param scanDirectory
     * @return the finder result
     * @throws Exception
     */
    protected FinderResult doScan(final AbstractFinder finder, final File scanDirectory) throws Exception {
        finder.setScanDirectory(scanDirectory);
        return finder.findLicenses();
    }

    /**
     * Checks the scan directory and the expected number of found archives.
     * 
     * @param finderResult a finder result
     * @param expectedScanDirectory
     * @param expectedArchiveCount
     */
    protected void assertResultsBase(final FinderResult finderResult, final File expectedScanDirectory,
                                     final int expectedArchiveCount) {
        Assert.assertEquals("scanDirectory", expectedScanDirectory, finderResult.getScanDirectory());
        Assert.assertEquals("number of archive files", expectedArchiveCount, finderResult.getArchiveFiles().size());
    }

    /**
     * Obtains the License instance to check against.
     * 
     * @param expectLicense true if an Apache license is expected, false if no license is expected
     * @return  the License instance for Apache-2.0 or none
     */
    protected License getExpectedLicense(final boolean expectLicense) {
        final License expectedLicenseInner = expectLicense
                ? getLicenseStoreData().getLicenseBySpdxIdentifier("Apache-2.0")
                : null;
        return expectedLicenseInner;
    }

}
