/*
 * Copyright (C) 2024 by SkyWatch Space Applications Inc. http://www.skywatch.com
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */
package org.esa.snap.stac;

// Author Alex McVittie, SkyWatch Space Applications Inc. January 2024


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * The StacCatalog class allows you to interact with a specific catalog
 */
public class StacCatalog implements StacComponent {

    private final String rootURL;

    private final JSONObject catalogJSON;

    private final JSONObject collectionJSON;

    private final String[] allCollections;

    private final HashMap<String, String> collectionsWithURLs;

    /**
     *
     * @param catalogURL
     */
    public StacCatalog(String catalogURL) {
        rootURL = catalogURL;

        catalogJSON = getJSONFromURL(catalogURL);
        collectionJSON = getJSONFromURL(catalogURL + "/collections");

        collectionsWithURLs = new HashMap<>();

        // Store list of collections
        allCollections = new String[((JSONArray) collectionJSON.get("collections")).size()];
        for (int x = 0; x < ((JSONArray) collectionJSON.get("collections")).size(); x++) {
            JSONObject curCollection = (JSONObject) ((JSONArray) collectionJSON.get("collections")).get(x);
            allCollections[x] = (String) curCollection.get(ID);
            for (Object o : (JSONArray) curCollection.get(LINKS)) {
                if (Objects.equals(SELF, ((JSONObject) o).get(REL))) {
                    collectionsWithURLs.put((String) curCollection.get(ID), (String) ((JSONObject) o).get(HREF));
                }
            }
        }
        Arrays.sort(allCollections);
    }


    @Override
    public JSONObject getJSON() {
        return catalogJSON;
    }

    @Override
    public String getId() {
        return (String) catalogJSON.get(ID);
    }

    @Override
    public String getSelfURL() {
        return null;
    }

    @Override
    public String getRootURL() {
        return rootURL;
    }

    public String getVersion() {
        return (String) catalogJSON.get(STAC_VERSION);
    }

    public String getTitle() {
        return (String) catalogJSON.get(TITLE);
    }

    public String[] listCollections() {
        return this.allCollections;
    }

    public int getNumCollections() {
        return this.allCollections.length;
    }

    public StacCollection getCollection(String collectionName) {
        if (collectionsWithURLs.containsKey(collectionName)) {
            return new StacCollection(collectionsWithURLs.get(collectionName));
        }
        return null;
    }

    public boolean containsCollection(String collectionName) {
        for (String collection : allCollections) {
            if (Objects.equals(collectionName, collection)) {
                return true;
            }
        }
        return false;
    }
}
