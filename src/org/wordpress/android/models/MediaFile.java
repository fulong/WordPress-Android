package org.wordpress.android.models;

import java.util.Date;
import java.util.Map;

import android.webkit.MimeTypeMap;

import org.wordpress.android.WordPress;

public class MediaFile {

    protected int id;
    protected long postID;
    protected String filePath = null; //path of the file into disk
    protected String fileName = null; //name of the file into the server
    protected String title = null;
    protected String description = null;
    protected String caption = null;
    protected int horizontalAlignment; //0 = none, 1 = left, 2 = center, 3 = right
    protected boolean verticalAligment = false; //false = bottom, true = top
    protected int width = 500, height;
    protected String MIMEType = ""; //do not store this value
    protected String videoPressShortCode = null;
    protected boolean featured = false;
    protected boolean isVideo = false;
    protected boolean featuredInPost;
    protected String fileURL = null; // url of the file to download
    protected String thumbnailURL = null;  // url of the thumbnail to download
    private String blogId;
    private long dateCreatedGmt;
    private String uploadState = null;
    private String mediaId;

    /*
     *  wrappers for extracting values from the resultMap passed to the MediaFile constructor
     *   getMapStr() is guaranteed to return "" if value doesn't exist (never returns null)
     *   getMapInt() & getMapLong() are guaranteed to return 0 if value doesn't exist or isn't a number
     *   getMapDate() DOES return null if value doesn't exist or isn't a date
     */
    private static String getMapStr(final Map<?, ?> map, final String key) {
        if (map==null || key==null || !map.containsKey(key))
            return "";
        return map.get(key).toString();
    }
    private static int getMapInt(final Map<?, ?> map, final String key) {
        try {
            return Integer.parseInt(getMapStr(map, key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private static long getMapLong(final Map<?, ?> map, final String key) {
        try {
            return Long.parseLong(getMapStr(map, key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private static Date getMapDate(final Map<?, ?> map, final String key) {
        if (map==null || key==null || !map.containsKey(key))
            return null;
        try {
            return (Date) map.get(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public MediaFile(String blogId, Map<?, ?> resultMap) {
        
        boolean isDotCom = (WordPress.getCurrentBlog() != null && WordPress.getCurrentBlog().isDotcomFlag());
        
        setBlogId(blogId);
        setMediaId(getMapStr(resultMap, "attachment_id"));
        setPostID(getMapLong(resultMap, "parent"));
        setTitle(getMapStr(resultMap, "title"));
        setCaption(getMapStr(resultMap, "caption"));
        setDescription(getMapStr(resultMap, "description"));
        
        // get the file name from the link
        String link = getMapStr(resultMap, "link");
        setFileName(new String(link).replaceAll("^.*/([A-Za-z0-9_-]+)\\.\\w+$", "$1"));
        
        String fileType = new String(link).replaceAll(".*\\.(\\w+)$", "$1").toLowerCase();
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileType);
        setMIMEType(mimeType);
        
        // make the file urls be https://... so that we can get these images with oauth when the blogs are private
        // assume no https for images in self-hosted blogs
        String fileUrl = getMapStr(resultMap, "link");
        if (isDotCom)
            fileUrl = fileUrl.replace("http:", "https:"); 
        setFileURL(fileUrl);
        
        String thumbnailURL = getMapStr(resultMap, "thumbnail");
        if (thumbnailURL.startsWith("http")) {
            if (isDotCom)
                thumbnailURL = thumbnailURL.replace("http:", "https:");
            setThumbnailURL(thumbnailURL);
        }

        Date date = getMapDate(resultMap, "date_created_gmt");
        if (date != null)
            setDateCreatedGMT(date.getTime());

        Object meta = resultMap.get("metadata");
        if(meta != null && meta instanceof Map) {
            Map<?, ?> metadata = (Map<?, ?>) meta;
            setWidth(getMapInt(metadata, "width"));
            setHeight(getMapInt(metadata, "height"));
        }
    }
    
    public MediaFile() {
        // TODO Auto-generated constructor stub
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaId() {
        return mediaId;
    }
    
    public void setMediaId(String id) {
        mediaId = id;
    }
    
    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public long getPostID() {
        return postID;
    }

    public void setPostID(long postID) {
        this.postID = postID;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
    
    public boolean isVerticalAlignmentOnTop() {
        return verticalAligment;
    }

    public void setVerticalAlignmentOnTop(boolean verticalAligment) {
        this.verticalAligment = verticalAligment;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMIMEType() {
        return MIMEType;
    }

    public void setMIMEType(String type) {
        MIMEType = type;
    }

    public String getVideoPressShortCode() {
        return videoPressShortCode;
    }

    public void setVideoPressShortCode(String videoPressShortCode) {
        this.videoPressShortCode = videoPressShortCode;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    public boolean isFeaturedInPost() {
        return featuredInPost;
    }

    public void setFeaturedInPost(boolean featuredInPost) {
        this.featuredInPost = featuredInPost;
    }

    public void save() {
        WordPress.wpDB.saveMediaFile(this);
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
        
    }

    public void setDateCreatedGMT(long date_created_gmt) {
        this.dateCreatedGmt = date_created_gmt;
    }
    

    public long getDateCreatedGMT() {
        return dateCreatedGmt;
    }

    public void setUploadState(String uploadState) {
        this.uploadState = uploadState;
    }
    
    public String getUploadState() {
        return uploadState;
    }

}
