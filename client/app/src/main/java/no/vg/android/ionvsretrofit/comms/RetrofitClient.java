package no.vg.android.ionvsretrofit.comms;

import no.vg.android.ionvsretrofit.entities.PodcastEpisodeJsonListProxy;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitClient {
    @GET("{path}")
    Call<PodcastEpisodeJsonListProxy> getDecoded(@Path("path") String path, @Query("cacheBreaker") String cacheBreaker);

    @GET("{path}")
    Call<ResponseBody> getString(@Path("path") String path, @Query("cacheBreaker") String cacheBreaker);
}
