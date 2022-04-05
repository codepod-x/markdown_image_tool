package cn.codepod.tool.icon;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author zhanglei
 * @date 2022/4/5 11:01
 */
public interface MarkdownIcons {
    Icon DEFAULT = IconLoader.getIcon("/icons/default.png", MarkdownIcons.class);
    Icon DELETE = IconLoader.getIcon("/icons/delete.png", MarkdownIcons.class);
    Icon DELETE_DIALOG = IconLoader.getIcon("/icons/delete-dialog.png", MarkdownIcons.class);
    Icon RENAME = IconLoader.getIcon("/icons/rename.png", MarkdownIcons.class);
}
