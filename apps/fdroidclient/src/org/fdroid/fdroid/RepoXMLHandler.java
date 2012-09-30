/*
 * Copyright (C) 2010-12  Ciaran Gultnieks, ciaran@ciarang.com
 * Copyright (C) 2009  Roberto Jacinto, roberto.jacinto@caixamagica.pt
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.fdroid.fdroid;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.net.ssl.SSLHandshakeException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class RepoXMLHandler extends DefaultHandler {

    // The ID of the repo we're processing.
    private int repo;

    private Vector<DB.App> apps;

    private DB.App curapp = null;
    private DB.Apk curapk = null;
    private StringBuilder curchars = new StringBuilder();

    private String pubkey;
    private String hashType;

    // The date format used in the repo XML file.
    private SimpleDateFormat mXMLDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public RepoXMLHandler(int repo, Vector<DB.App> apps) {
        this.repo = repo;
        this.apps = apps;
        pubkey = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        curchars.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        super.endElement(uri, localName, qName);
        String curel = localName;
        String str = curchars.toString();
        if (str != null) {
            str = str.trim();
        }

        if (curel.equals("application") && curapp != null) {

            // If we already have this application (must be from scanning a
            // different repo) then just merge in the apks.
            // TODO: Scanning the whole app list like this every time is
            // going to be stupid if the list gets very big!
            boolean merged = false;
            for (DB.App app : apps) {
                if (app.id.equals(curapp.id)) {
                    app.apks.addAll(curapp.apks);
                    merged = true;
                    break;
                }
            }
            if (!merged)
                apps.add(curapp);

            curapp = null;

        } else if (curel.equals("package") && curapk != null && curapp != null) {
            curapp.apks.add(curapk);
            curapk = null;
        } else if (curapk != null && str != null) {
            if (curel.equals("version")) {
                curapk.version = str;
            } else if (curel.equals("versioncode")) {
                try {
                    curapk.vercode = Integer.parseInt(str);
                } catch (NumberFormatException ex) {
                    curapk.vercode = 0;
                }
            } else if (curel.equals("size")) {
                try {
                    curapk.detail_size = Integer.parseInt(str);
                } catch (NumberFormatException ex) {
                    curapk.detail_size = 0;
                }
            } else if (curel.equals("hash")) {
                if (hashType == null || hashType.equals("md5")) {
                    if (curapk.detail_hash == null) {
                        curapk.detail_hash = str;
                        curapk.detail_hashType = "MD5";
                    }
                } else if (hashType.equals("sha256")) {
                    curapk.detail_hash = str;
                    curapk.detail_hashType = "SHA-256";
                }
            } else if (curel.equals("sig")) {
                curapk.sig = str;
            } else if (curel.equals("srcname")) {
                curapk.srcname = str;
            } else if (curel.equals("apkname")) {
                curapk.apkName = str;
            } else if (curel.equals("sdkver")) {
                try {
                    curapk.minSdkVersion = Integer.parseInt(str);
                } catch (NumberFormatException ex) {
                    curapk.minSdkVersion = 0;
                }
            } else if (curel.equals("added")) {
                try {
                    curapk.added = str.length() == 0 ? null : mXMLDateFormat
                            .parse(str);
                } catch (ParseException e) {
                    curapk.added = null;
                }
            } else if (curel.equals("permissions")) {
                curapk.detail_permissions = DB.CommaSeparatedList.make(str);
            } else if (curel.equals("features")) {
                curapk.features = DB.CommaSeparatedList.make(str);
            }
        } else if (curapp != null && str != null) {
            if (curel.equals("id")) {
                curapp.id = str;
            } else if (curel.equals("name")) {
                curapp.name = str;
            } else if (curel.equals("icon")) {
                curapp.icon = str;
            } else if (curel.equals("description")) {
                // This is the old-style description. We'll read it
                // if present, to support old repos, but in newer
                // repos it will get overwritten straight away!
                curapp.detail_description = "<p>" + str + "</p>";
            } else if (curel.equals("desc")) {
                // New-style description.
                curapp.detail_description = str;
            } else if (curel.equals("summary")) {
                curapp.summary = str;
            } else if (curel.equals("license")) {
                curapp.license = str;
            } else if (curel.equals("category")) {
                curapp.category = str;
            } else if (curel.equals("source")) {
                curapp.detail_sourceURL = str;
            } else if (curel.equals("donate")) {
                curapp.detail_donateURL = str;
            } else if (curel.equals("web")) {
                curapp.detail_webURL = str;
            } else if (curel.equals("tracker")) {
                curapp.detail_trackerURL = str;
            } else if (curel.equals("added")) {
                try {
                    curapp.added = str.length() == 0 ? null : mXMLDateFormat
                            .parse(str);
                } catch (ParseException e) {
                    curapp.added = null;
                }
            } else if (curel.equals("lastupdated")) {
                try {
                    curapp.lastUpdated = str.length() == 0 ? null
                            : mXMLDateFormat.parse(str);
                } catch (ParseException e) {
                    curapp.lastUpdated = null;
                }
            } else if (curel.equals("marketversion")) {
                curapp.curVersion = str;
            } else if (curel.equals("marketvercode")) {
                try {
                    curapp.curVercode = Integer.parseInt(str);
                } catch (NumberFormatException ex) {
                    curapp.curVercode = 0;
                }
            } else if (curel.equals("antifeatures")) {
                curapp.antiFeatures = DB.CommaSeparatedList.make(str);
            } else if (curel.equals("requirements")) {
                curapp.requirements = DB.CommaSeparatedList.make(str);
            }
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        super.startElement(uri, localName, qName, attributes);
        if (localName == "repo") {
            String pk = attributes.getValue("", "pubkey");
            if (pk != null)
                pubkey = pk;
        } else if (localName == "application" && curapp == null) {
            curapp = new DB.App();
            curapp.detail_Populated = true;
        } else if (localName == "package" && curapp != null && curapk == null) {
            curapk = new DB.Apk();
            curapk.id = curapp.id;
            curapk.repo = repo;
            hashType = null;
        } else if (localName == "hash" && curapk != null) {
            hashType = attributes.getValue("", "type");
        }
        curchars.setLength(0);
    }

    // Get a remote file. Returns the HTTP response code.
    // If 'etag' is not null, it's passed to the server as an If-None-Match
    // header, in which case expect a 304 response if nothing changed.
    // In the event of a 200 response ONLY, 'retag' (which should be passed
    // empty) may contain an etag value for the response, or it may be left
    // empty if none was available.
    private static int getRemoteFile(Context ctx, String url, String dest,
            String etag, StringBuilder retag) throws MalformedURLException,
            IOException {

        URL u = new URL(url);
        HttpURLConnection uc = (HttpURLConnection) u.openConnection();
        if (etag != null)
            uc.setRequestProperty("If-None-Match", etag);
        int code = uc.getResponseCode();
        if (code == 200) {

            FileOutputStream f = ctx.openFileOutput(dest, Context.MODE_PRIVATE);

            BufferedInputStream getit = new BufferedInputStream(
                    new URL(url).openStream());
            BufferedOutputStream bout = new BufferedOutputStream(f, 1024);
            byte data[] = new byte[1024];

            int readed = getit.read(data, 0, 1024);
            while (readed != -1) {
                bout.write(data, 0, readed);
                readed = getit.read(data, 0, 1024);
            }
            bout.close();
            getit.close();
            f.close();

            String et = uc.getHeaderField("ETag");
            if (et != null)
                retag.append(et);
        }
        return code;

    }

    // Do an update from the given repo. All applications found, and their
    // APKs, are added to 'apps'. (If 'apps' already contains an app, its
    // APKs are merged into the existing one).
    // Returns null if successful, otherwise an error message to be displayed
    // to the user (if there is an interactive user!)
    // 'newetag' should be passed empty. On success, it may contain an etag
    // value for the index that was successfully processed, or it may contain
    // null if none was available.
    public static String doUpdate(Context ctx, DB.Repo repo,
            Vector<DB.App> apps, StringBuilder newetag, Vector<Integer> keeprepos) {
        try {

            int code = 0;
            if (repo.pubkey != null) {

                // This is a signed repo - we download the jar file,
                // check the signature, and extract the index...
                Log.d("FDroid", "Getting signed index from " + repo.address);
                String address = repo.address + "/index.jar";
                PackageManager pm = ctx.getPackageManager();
                try {
                    PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
                    address += "?" + pi.versionName;
                } catch (Exception e) {
                }
                code = getRemoteFile(ctx, address, "tempindex.jar",
                        repo.lastetag, newetag);
                if (code == 200) {
                    String jarpath = ctx.getFilesDir() + "/tempindex.jar";
                    JarFile jar;
                    JarEntry je;
                    try {
                        jar = new JarFile(jarpath, true);
                        je = (JarEntry) jar.getEntry("index.xml");
                        File efile = new File(ctx.getFilesDir(),
                                "/tempindex.xml");
                        InputStream in = new BufferedInputStream(
                                jar.getInputStream(je), 8192);
                        OutputStream out = new BufferedOutputStream(
                                new FileOutputStream(efile), 8192);
                        byte[] buffer = new byte[8192];
                        while (true) {
                            int nBytes = in.read(buffer);
                            if (nBytes <= 0)
                                break;
                            out.write(buffer, 0, nBytes);
                        }
                        out.flush();
                        out.close();
                        in.close();
                    } catch (SecurityException e) {
                        Log.e("FDroid", "Invalid hash for index file");
                        return "Invalid hash for index file";
                    }
                    Certificate[] certs = je.getCertificates();
                    jar.close();
                    if (certs == null) {
                        Log.d("FDroid", "No signature found in index");
                        return "No signature found in index";
                    }
                    Log.d("FDroid", "Index has " + certs.length + " signature"
                            + (certs.length > 1 ? "s." : "."));

                    boolean match = false;
                    for (Certificate cert : certs) {
                        String certdata = Hasher.hex(cert.getEncoded());
                        if (repo.pubkey.equals(certdata)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) {
                        Log.d("FDroid", "Index signature mismatch");
                        return "Index signature mismatch";
                    }
                }

            } else {

                // It's an old-fashioned unsigned repo...
                Log.d("FDroid", "Getting unsigned index from " + repo.address);
                code = getRemoteFile(ctx, repo.address + "/index.xml",
                        "tempindex.xml", repo.lastetag, newetag);
            }

            if (code == 200) {
                // Process the index...
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                RepoXMLHandler handler = new RepoXMLHandler(repo.id, apps);
                xr.setContentHandler(handler);

                InputStreamReader isr = new FileReader(new File(
                        ctx.getFilesDir() + "/tempindex.xml"));
                InputSource is = new InputSource(isr);
                xr.parse(is);

                if (handler.pubkey != null && repo.pubkey == null) {
                    // We read an unsigned index, but that indicates that
                    // a signed version is now available...
                    Log.d("FDroid",
                            "Public key found - switching to signed repo for future updates");
                    repo.pubkey = handler.pubkey;
                    try {
                        DB db = DB.getDB();
                        db.updateRepoByAddress(repo);
                    } finally {
                        DB.releaseDB();
                    }
                }

            } else if (code == 304) {
                // The index is unchanged since we last read it. We just mark
                // everything that came from this repo as being updated.
                Log.d("FDroid", "Repo index for " + repo.address
                        + " is up to date (by etag)");
                keeprepos.add(repo.id);
                // Make sure we give back the same etag. (The 200 route will
                // have supplied a new one.
                newetag.append(repo.lastetag);

            } else {
                return "Failed to read index - HTTP response "
                        + Integer.toString(code);
            }

        } catch (SSLHandshakeException sslex) {
            Log.e("FDroid", "SSLHandShakeException updating from "
                    + repo.address + ":\n" + Log.getStackTraceString(sslex));
            return "A problem occurred while establishing an SSL connection. If this problem persists, AND you have a very old device, you could try using http instead of https for the repo URL.";
        } catch (Exception e) {
            Log.e("FDroid", "Exception updating from " + repo.address + ":\n"
                    + Log.getStackTraceString(e));
            return "Failed to update - " + e.getMessage();
        } finally {
            ctx.deleteFile("tempindex.xml");
            ctx.deleteFile("tempindex.jar");
        }

        return null;
    }

}
