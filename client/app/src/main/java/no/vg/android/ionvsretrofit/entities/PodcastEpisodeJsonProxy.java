package no.vg.android.ionvsretrofit.entities;

import java.util.List;

/**
 * Represents single episode from JSON feed.
 */
public class PodcastEpisodeJsonProxy {

    public String _id;
    public String title;
    public String slug;
    public String subtitle;
    public String link;
    public String duration;
    public List<String> keywords;
    public String mimetype;
    public Integer filesize;
    public String pubDate;
    public String showId;
    public Integer numPlays;
    public String description;
    public String audioFile;
    public String image;
    public String audioUrl;
    public String guid;
    public String logo;
    public String logoThumb;
    public String author;
    public List<Attachment> attachments;

    public static class Attachment {
        public String url;
        public long filesize;
        /** Mime type*/
        public String mimetype;
    }
}
