package cn.codepod.tool.entity;

import java.util.Objects;

/**
 * @author zhanglei
 * @date 2022/4/4 15:15
 */
public class MarkdownImage {
    private String description;
    private String url;
    private String full;

    public MarkdownImage(String description, String url) {
        this.description = description;
        this.url = url;
        this.full = String.format("![%S](%s)", description, url);
    }

    public MarkdownImage(String description, String url, String full) {
        this.description = description;
        this.url = url;
        this.full = full;
    }

    public static MarkdownImage of(String description, String url) {
        return new MarkdownImage(description, url);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarkdownImage that = (MarkdownImage) o;
        return Objects.equals(description, that.description) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, url);
    }

}
