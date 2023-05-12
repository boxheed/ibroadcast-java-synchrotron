package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*
import static com.google.common.base.Strings.isNullOrEmpty

import okhttp3.*
import groovy.json.*
import groovy.contracts.*

public class IBroadcast {

    public static final String IBROADCAST_STATUS_URL = 'https://json.ibroadcast.com/s/JSON/status'
    public static final String IBROADCAST_TRACKS_URL = 'https://library.ibroadcast.com'
    public static final String IBROADCAST_ALBUMS_URL = 'https://library.ibroadcast.com/albums'
    public static final String IBROADCAST_MD5_URL = 'https://sync.ibroadcast.com'
    public static final String IBROADCAST_UPLOAD_URL = 'https://sync.ibroadcast.com'

    public static final String CLIENT_VERSION = "0.1"
    public static final String CLIENT_NAME = 'ibroadcast-sync-client'
    public static final String CLIENT_TYPES = '1'


    @Requires({'iBroadcast: username and password must be specified' && !isNullOrEmpty(username) && !isNullOrEmpty(pass) })
    public static final def auth(String username, String pass) {
        JsonBuilder builder = new JsonBuilder()
        builder {
            mode 'status'
            email_address username
            password pass
            version CLIENT_VERSION
            client CLIENT_NAME
            supported_types CLIENT_TYPES
        }
        def payload = JsonOutput.prettyPrint(builder.toString())
        sendRequest(IBROADCAST_STATUS_URL, payload, 
            {result -> 
                info("Successfully connected to iBroadcast")
                return [userId: result.user.id, userToken: result.user.token] }, 
            {result ->
                error("Couldn't retrieve token from iBroadcast: {}", result)
                throw new RuntimeException()
            }
        )
    }


    @Requires({!isNullOrEmpty(credentials.userId) && !isNullOrEmpty(credentials.userToken) })
    public static final def listTracks(def credentials) {
        JsonBuilder builder = new JsonBuilder()
        builder {
            mode 'status'
            version CLIENT_VERSION
            client CLIENT_NAME
            supported_types CLIENT_TYPES
            user_id credentials.userId
            token credentials.userToken
        }
        def payload = JsonOutput.prettyPrint(builder.toString())
        sendRequest(IBROADCAST_TRACKS_URL, payload, 
            {result -> 
                info("Retrieved track listing")
                return result 
            },
            {result ->
                error("Couldn't retrieve track listing  from iBroadcast: {}", result)
                throw new RuntimeException()
            }
        )
    }

    @Requires({!isNullOrEmpty(credentials.userId) && !isNullOrEmpty(credentials.userToken) })
    public static final def listAlbums(def credentials) {
        JsonBuilder builder = new JsonBuilder()
        builder {
            mode 'status'
            version CLIENT_VERSION
            client CLIENT_NAME
            supported_types CLIENT_TYPES
            user_id credentials.userId
            token credentials.userToken
        }
        def payload = JsonOutput.prettyPrint(builder.toString())
        sendRequest(IBROADCAST_ALBUMS_URL, payload, 
            {result -> 
                info("Retrieved album listing")
                return result 
            },
            {result ->
                error("Couldn't retrieve album listing  from iBroadcast: {}", result)
                throw new RuntimeException()
            }
        )
    }

    public static def getMusicChecksums(def credentials) {
        JsonBuilder builder = new JsonBuilder()
        builder {
            mode 'status'
            version CLIENT_VERSION
            client CLIENT_NAME
            supported_types CLIENT_TYPES
            user_id credentials.userId
            token credentials.userToken
        }
        def payload = JsonOutput.prettyPrint(builder.toString())
        def userId = credentials.userId
        def userToken = credentials.userToken
        def url = IBROADCAST_MD5_URL + "?user_id=${userId}&token=${userToken}"
        sendRequest(url, payload, 
            {result -> 
                info("Retrieved track checksums")
                return result.md5 
            },
            {result ->
                error("Couldn't retrieve track checksums from iBroadcast: {}", result)
                throw new RuntimeException()
            }
        )
    }

    public static def upload(def credentials, def track) {
        OkHttpClient okclient = new OkHttpClient()
            .newBuilder()
            .build();
        String contentType = URLConnection.guessContentTypeFromName(track.getName())
        String fileName = track.getName()
        RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", fileName,
                RequestBody.create(MediaType.parse(contentType), track))
            .addFormDataPart("file_path", track.getPath())
            .addFormDataPart("method", CLIENT_NAME)
            .addFormDataPart("user_id", credentials.userId)
            .addFormDataPart("token", credentials.userToken)
            .build();

        Request request = new Request.Builder()
            .url(IBROADCAST_UPLOAD_URL)
            .post(requestBody)
            .build();
        try (Response response = okclient.newCall(request).execute()) {
            String content = response.body().string()
            def jsonSlurper = new JsonSlurper()
            def object = jsonSlurper.parseText(content)
            debug("Received response with code {} from iBroadcast", response.code)
            //TODO object.result may not be correct
            if(response.code != 200 || !object.result)  {
                error("Couldn't upload track to iBroadcast: {}", object)
                throw new RuntimeException()
            } else {
                info("Upload result: {}, {}", response.code, object.message)
            }
        }

        return null
    }

    public static def trash() {
        return null
    }

    public static final def sendRequest(def url, def payload, Closure success, Closure error) {
        debug("Sending request to {}", url)
        OkHttpClient okclient = new OkHttpClient()
            .newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, payload)
        Request request = new Request.Builder()
            .url(url)
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .build();
        try(Response response = okclient.newCall(request).execute()) {
            String content = response.body().string()
            def jsonSlurper = new JsonSlurper()
            def object = jsonSlurper.parseText(content)
            debug("Received response with code {} from iBroadcast", response.code)
            //TODO object.result may not be correct
            if(response.code != 200 || !object.result)  {
                return error(object)
            } else {
                return success(object)
            }
        }
    }

}