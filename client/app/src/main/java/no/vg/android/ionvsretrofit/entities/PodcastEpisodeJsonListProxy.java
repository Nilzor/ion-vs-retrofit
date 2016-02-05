package no.vg.android.ionvsretrofit.entities;

import java.util.List;

/**
 * Proxy for deserializing JSON list of {@link PodcastEpisodeJsonProxy}
 */
public class PodcastEpisodeJsonListProxy  {
    public String name;
    public String description;
    public String subtitle;
    public String logo;
    public String link;
    public String logoMediumThumb;
    public String logoThumb;
    public String lastModified;
    public String generator;
    public String iTunesLink;
    public String subCategory;

    public List<PodcastEpisodeJsonProxy> episodes;
}
