package victor.pettengill.popularmovies.beans;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by appimagetech on 24/07/17.
 */

public class Trailer {

    private String trailerId;
    private String trailerName;
    private String trailerSite;
    private String trailerType;
    private String trailerKey;

    public Trailer() {
    }

    public Trailer(JSONObject object) throws JSONException, ParseException {

        if(object.has("id")) {
            trailerId = object.getString("id");
        }

        if(object.has("name")) {
            trailerName = object.getString("name");
        }

        if(object.has("site")) {
            trailerSite = object.getString("site");
        }

        if(object.has("type")) {
            trailerType = object.getString("type");
        }

        if(object.has("key")) {
            trailerKey = object.getString("key");
        }

    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerSite() {
        return trailerSite;
    }

    public void setTrailerSite(String trailerSite) {
        this.trailerSite = trailerSite;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }


}