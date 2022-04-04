package cn.codepod.tool.editor;

import cn.codepod.tool.entity.MarkdownImage;
import cn.codepod.tool.util.NotificationUtil;
import cn.codepod.tool.util.OssUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.NonEmptyInputValidator;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei
 * @date 2022/4/4 14:43
 */
public class MarkdownImageRenameAction extends MarkdownImageAction {

    @Override
    protected void imageProcess(@NotNull AnActionEvent event, Editor editor, Caret caret, MarkdownImage markdownImage) {

        String url = markdownImage.getUrl();

        String ossKey = OssUtil.getOssKey(url);

        String filename = ossKey;

        int offset = caret.getOffset();

        int index = ossKey.lastIndexOf('/');
        if (index != -1) {
            filename = ossKey.substring(index + 1);
        }

        String newName = Messages.showInputDialog("Please input a new name", "Rename Image", null, filename, new NonEmptyInputValidator());

        if (StringUtil.isEmpty(newName) || filename.equals(newName)) {
            NotificationUtil.show("The input name is invalid.");
            caret.setSelection(offset, offset);
            return;
        }

        String newUrl = OssUtil.rename(ossKey, newName);

        NotificationUtil.show("The file is renamed.");

        WriteCommandAction.runWriteCommandAction(event.getProject(), null, null, () -> {
            String text = caret.getSelectedText();
            text = text.replace(markdownImage.getUrl(), newUrl);
            Document document = editor.getDocument();
            int start = caret.getSelectionStart();
            int end = caret.getSelectionEnd();
            document.replaceString(start, end, text);
        });

    }
}
