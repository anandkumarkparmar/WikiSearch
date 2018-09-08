package in.anandkparmar.wikisearch.network.responses;

import java.util.List;

public class SearchListResponse {
    private Pages query;

    public Pages getQuery() {
        return query;
    }

    public void setQuery(Pages query) {
        this.query = query;
    }

    public class Pages {
        private List<SearchListItem> pages;

        public List<SearchListItem> getPages() {
            return pages;
        }

        public void setPages(List<SearchListItem> pages) {
            this.pages = pages;
        }
    }

    public class SearchListItem {
        private String title;
        private String description;
        private String pagelanguage;
        private String fullurl;
        private String editurl;
        private String canonicalurl;
        private Long pageid;
        private Thumbnail thumbnail;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPagelanguage() {
            return pagelanguage;
        }

        public void setPagelanguage(String pagelanguage) {
            this.pagelanguage = pagelanguage;
        }

        public String getFullurl() {
            return fullurl;
        }

        public void setFullurl(String fullurl) {
            this.fullurl = fullurl;
        }

        public String getEditurl() {
            return editurl;
        }

        public void setEditurl(String editurl) {
            this.editurl = editurl;
        }

        public String getCanonicalurl() {
            return canonicalurl;
        }

        public void setCanonicalurl(String canonicalurl) {
            this.canonicalurl = canonicalurl;
        }

        public Long getPageid() {
            return pageid;
        }

        public void setPageid(Long pageid) {
            this.pageid = pageid;
        }

        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(Thumbnail thumbnail) {
            this.thumbnail = thumbnail;
        }
    }

    public class Thumbnail {
        private String source;
        private String width;
        private String height;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }
    }
}
