package info.movito.themoviedbapi;

import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.tools.ApiUrl;

public class TmdbDiscoverAdditions extends AbstractTmdbApi {

    public static final String TMDB_METHOD_DISCOVER = "discover";
    private static final String PARAM_WITH_PEOPLE = "with_people";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String PARAM_WITH_ORIGINAL_LANGUAGE = "with_original_language";


    public TmdbDiscoverAdditions(TmdbApi tmdbApi) {
        super(tmdbApi);
    }

    public MovieResultsPage getDiscoverWithPeople(String people) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_DISCOVER, "movie");
        apiUrl.addParam(PARAM_WITH_PEOPLE, people);
        apiUrl.addParam(PARAM_SORT_BY, "popularity.desc");
        return mapJsonResult(apiUrl, MovieResultsPage.class);
    }

    public MovieResultsPage getDiscoverWithOriginalLanguage(String language) {
        ApiUrl apiUrl = new ApiUrl(TMDB_METHOD_DISCOVER, "movie");
        apiUrl.addParam(PARAM_WITH_ORIGINAL_LANGUAGE, language);
        apiUrl.addParam(PARAM_SORT_BY, "popularity.desc");
        return mapJsonResult(apiUrl, MovieResultsPage.class);
    }
}
