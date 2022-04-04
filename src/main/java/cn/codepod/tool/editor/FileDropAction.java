package cn.codepod.tool.editor;

import cn.codepod.tool.entity.SettingConfig;
import cn.codepod.tool.service.SettingStateService;
import cn.codepod.tool.util.MarkdownUtil;
import cn.codepod.tool.util.NotificationUtil;
import cn.codepod.tool.util.OssUtil;
import com.intellij.ide.dnd.FileCopyPasteUtil;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CustomFileDropHandler;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author zhanglei
 * @date 2022/4/3 11:24
 */
public class FileDropAction extends CustomFileDropHandler {

    @Override
    public boolean canHandle(@NotNull Transferable t, @Nullable Editor editor) {
        return checkCondition(t, editor);
    }

    @Override
    public boolean handleDrop(@NotNull Transferable t, @Nullable Editor editor, Project project) {
        try {
            if (!checkCondition(t, editor)) {
                return false;
            }
            if (editor == null) {
                return false;
            }
            Caret caret = editor.getCaretModel().getCurrentCaret();
            Document document = editor.getDocument();

            List<File> fileList = FileCopyPasteUtil.getFileList(t);

            if (fileList == null || fileList.size() < 1) {
                return false;
            }
            String markdownFileName = MarkdownUtil.getMarkdownFileName(editor, false);
            WriteCommandAction.runWriteCommandAction(project, null, null, () -> {
                for (File file : fileList) {
                    String markdownImageText = OssUtil.upload(file, markdownFileName);
                    document.insertString(caret.getOffset(), markdownImageText);
                    caret.moveToOffset(caret.getOffset() + markdownImageText.length());
                    NotificationUtil.show(NotificationType.INFORMATION, String.format("File: %s is uploaded.", file.getName()));
                }
            });
            return true;
        } catch (Exception e) {
            NotificationUtil.show(NotificationType.ERROR, String.format("An error happened: %s", e.getMessage()));
        }
        return false;
    }

    private boolean checkCondition(@NotNull Transferable t, @Nullable Editor editor) {
        if (!MarkdownUtil.isMarkdownFile(editor)) {
            return false;
        }

        SettingConfig config = SettingStateService.getInstance().getState();

        if (!config.isEnable()) {
            return false;
        }

        String[] imgSuffixes = {"bmp", "gif", "jpeg", "jpg", "png", "webp"};
        Boolean writeable = Optional.ofNullable(editor).map(Editor::getDocument).map(Document::isWritable).orElse(false);
        if (!writeable) {
            return false;
        }
        List<File> fileList = FileCopyPasteUtil.getFileList(t);
        if (fileList == null || fileList.size() < 1) {
            return false;
        }
        return fileList.stream()
                .allMatch(file -> Arrays.stream(imgSuffixes)
                        .anyMatch(suffix -> file.getName().endsWith(suffix)));
    }
}
