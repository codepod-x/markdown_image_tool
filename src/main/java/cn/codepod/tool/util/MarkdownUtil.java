package cn.codepod.tool.util;

import cn.codepod.tool.entity.MarkdownImage;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhanglei
 * @date 2022/4/3 14:09
 */
public class MarkdownUtil {

    private static final Pattern PATTERN = Pattern.compile("!\\[(.+?\\..+?)]\\((.+?)\\)");

    public static boolean isMarkdownFile(Editor editor) {
        if (null == editor) {
            return false;
        }
        if (!(editor instanceof EditorEx)) {
            return false;
        }
        return "Markdown".equals(((EditorEx) editor).getVirtualFile().getFileType().getName());
    }

    @Nullable
    public static String getMarkdownFileName(Editor editor, boolean withSuffix) {
        if (null == editor) {
            return null;
        }
        if (!(editor instanceof EditorEx)) {
            return null;
        }
        String name = ((EditorEx) editor).getVirtualFile().getName();

        return withSuffix ? name : name.replace(".md", "");
    }

    public static MarkdownImage getMarkDownImage(@NotNull String imageTexts) {
        Matcher matcher = PATTERN.matcher(imageTexts);
        if (matcher.find()) {
            String description = matcher.group(1).trim();
            String url = matcher.group(2).trim();
            String full = matcher.group(0).trim();
            return new MarkdownImage(description, url, full);
        }
        return null;
    }

}
