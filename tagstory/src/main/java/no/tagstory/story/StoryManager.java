package no.tagstory.story;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import no.tagstory.utils.Database;
import no.tagstory.utils.StoryParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.regex.Pattern;

public class StoryManager {

    private Database database;
    private Context context;

    public StoryManager(Context context) {
        this.context = context;
        database = new Database(this.context);
        database.open();
    }

    public void closeDatabase() {
        database.close();
    }

    public Cursor getCursorOverStories() {
        // TODO Convert cursor to a StoryList-object and return a list
        return database.getStoryList();
    }

    public Story getStory(String id) {
        try {
            return StoryParser.parseJsonToStory(context, id);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteStory(String id) {
        return  database.deleteStory(id) && deleteAllAssetsRelativeToStoryAndItself(id) &&
                  context.deleteFile(id + ".json");
    }

    public boolean hasStory(String id) {
        return database.hasStory(id);
    }

    public boolean deleteAllAssetsRelativeToStoryAndItself(String id) {
            try {
                String mFileName = id.concat(".json");
                String mParsedLine;
                InputStream mInputStream = context.openFileInput(mFileName);
                InputStreamReader mInputStreamReader = new InputStreamReader(
                        mInputStream);

                BufferedReader mBufferedReader = new BufferedReader(mInputStreamReader);
                mParsedLine = mBufferedReader.readLine();
                JSONObject mStoryObject = new JSONObject(mParsedLine).getJSONObject(StoryParser.STORY);
                if (mStoryObject.optString(StoryParser.IMAGE, "") != null) {
                    context.deleteFile((String) mStoryObject.get(StoryParser.IMAGE));
                }

                JSONObject mTagObjects = mStoryObject.getJSONObject(StoryParser.TAGS);
                Iterator<String> mKeys = mTagObjects.keys();
                while (mKeys.hasNext()) {
                    String mKey = mKeys.next();
                    JSONObject mTag = mTagObjects.getJSONObject(mKey);

                    if (mTag.has(StoryParser.TAG_OPTIONS)) {
                        JSONArray mOptions = mTag.getJSONArray(StoryParser.TAG_OPTIONS);
                        for (int index = 0; index < mOptions.length(); index++) {
                            JSONObject mOption = mOptions.getJSONObject(index);
                            if (mOption.has(StoryParser.HINT_IMAGE_SOURCE)) {
                                context.deleteFile((String) mOption.get(StoryParser.HINT_IMAGE_SOURCE));
                            }
                            if (mOption.has(StoryParser.HINT_SOUND_SOURCE)) {
                                String mFile = (String) mOption.get(StoryParser.HINT_SOUND_SOURCE);
                                context.deleteFile(mFile);
                            }
                        }
                    }
                }
                mInputStream.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        return false;
    }
}
